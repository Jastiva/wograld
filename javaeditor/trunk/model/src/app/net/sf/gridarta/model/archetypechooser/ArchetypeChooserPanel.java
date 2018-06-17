/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.model.archetypechooser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A named panel within the {@link ArchetypeChooserModel}. A panel consists of a
 * set of {@link ArchetypeChooserFolder ArchetypeChooserFolders}. One panel is
 * selected at any time.
 * @author Andreas Kirschbaum
 */
public class ArchetypeChooserPanel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The panel name.
     * @serial
     */
    @NotNull
    private final String name;

    /**
     * The {@link ArchetypeChooserFolder ArchetypeChooserFolders} in this panel.
     * The folders are ordered by folder name. The first entry includes
     * archetypes of all other folders.
     * @serial
     */
    @NotNull
    private final List<ArchetypeChooserFolder<G, A, R>> folders = new ArrayList<ArchetypeChooserFolder<G, A, R>>();

    /**
     * The selected {@link ArchetypeChooserFolder}. It must be part of {@link
     * #folders}.
     * @serial
     */
    @NotNull
    private ArchetypeChooserFolder<G, A, R> selectedFolder = new ArchetypeChooserFolder<G, A, R>("all");

    /**
     * The {@link ArchetypeChooserFolderListener} attached to {@link
     * #selectedFolder}.
     */
    @NotNull
    private final transient ArchetypeChooserFolderListener<G, A, R> archetypeChooserFolderListener = new ArchetypeChooserFolderListener<G, A, R>() {

        @Override
        public void selectedArchetypeChanged(@Nullable final R selectedArchetype) {
            fireSelectedArchetypeChanged(selectedArchetype);
        }

    };

    /**
     * The registered listeners.
     */
    @NotNull
    private final EventListenerList2<ArchetypeChooserPanelListener<G, A, R>> listeners = new EventListenerList2<ArchetypeChooserPanelListener<G, A, R>>(ArchetypeChooserPanelListener.class);

    /**
     * Creates a new instance.
     * @param name the panel name
     */
    public ArchetypeChooserPanel(@NotNull final String name) {
        this.name = name;
        folders.add(selectedFolder);
        selectedFolder.addArchetypeChooserFolderListener(archetypeChooserFolderListener);
    }

    /**
     * Adds a listener to be notified of changes.
     * @param listener the listener
     */
    public void addArchetypeChooserPanelListener(@NotNull final ArchetypeChooserPanelListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be notified of changes.
     * @param listener the listener
     */
    public void removeArchetypeChooserPanelListener(@NotNull final ArchetypeChooserPanelListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the panel name.
     * @return the panel name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns the {@link ArchetypeChooserFolder ArchetypeChooserFolders}.
     * @return the folders ordered by folder name; the first entry contains all
     *         archetypes
     */
    @NotNull
    public List<ArchetypeChooserFolder<G, A, R>> getFolders() {
        return Collections.unmodifiableList(folders);
    }

    /**
     * Adds an {@link Archetype} to this panel.
     * @param folder the folder name to add to
     * @param archetype the archetype to add
     */
    public void addArchetype(@NotNull final String folder, @NotNull final R archetype) {
        getOrCreateFolder(folder).addArchetype(archetype);
        getDefaultFolder().addArchetype(archetype);
    }

    /**
     * Returns the default {@link ArchetypeChooserFolder} that contains all
     * {@link Archetype Archetypes} of all folders.
     * @return the default folder
     */
    @NotNull
    public ArchetypeChooserFolder<G, A, R> getDefaultFolder() {
        return folders.get(0);
    }

    /**
     * Returns an {@link ArchetypeChooserFolder} by folder name. The folder is
     * created if it does not yet exist.
     * @param folderName the folder name
     * @return the folder
     */
    @NotNull
    private ArchetypeChooserFolder<G, A, R> getOrCreateFolder(@NotNull final String folderName) {
        final ArchetypeChooserFolder<G, A, R> existingFolder = getFolder(folderName);
        if (existingFolder != null) {
            return existingFolder;
        }

        final ArchetypeChooserFolder<G, A, R> folder = new ArchetypeChooserFolder<G, A, R>(folderName);
        folders.add(folder);
        return folder;
    }

    /**
     * Returns an {@link ArchetypeChooserFolder} by folder name.
     * @param folderName the folder name
     * @return the folder or <code>null</code> if no such folder exists
     */
    @Nullable
    public ArchetypeChooserFolder<G, A, R> getFolder(@NotNull final String folderName) {
        for (int i = 1; i < folders.size(); i++) {
            final ArchetypeChooserFolder<G, A, R> folder = folders.get(i);
            if (folder.getName().equals(folderName)) {
                return folder;
            }
        }

        return null;
    }

    /**
     * Returns the selected {@link ArchetypeChooserFolder}.
     * @return the selected folder
     */
    @NotNull
    public ArchetypeChooserFolder<G, A, R> getSelectedFolder() {
        return selectedFolder;
    }

    /**
     * Sets the selected {@link ArchetypeChooserFolder}.
     * @param selectedFolder the selected folder
     */
    public void setSelectedFolder(@NotNull final ArchetypeChooserFolder<G, A, R> selectedFolder) {
        if (!folders.contains(selectedFolder)) {
            throw new IllegalArgumentException("selected folder " + selectedFolder.getName() + " is not part of the panel");
        }

        if (this.selectedFolder == selectedFolder) {
            return;
        }

        this.selectedFolder.removeArchetypeChooserFolderListener(archetypeChooserFolderListener);
        this.selectedFolder = selectedFolder;
        this.selectedFolder.addArchetypeChooserFolderListener(archetypeChooserFolderListener);

        for (final ArchetypeChooserPanelListener<G, A, R> listener : listeners.getListeners()) {
            listener.selectedFolderChanged(selectedFolder);
        }
        fireSelectedArchetypeChanged(selectedFolder.getSelectedArchetype());
    }

    /**
     * Notifies all registered {@link ArchetypeChooserPanelListener
     * ArchetypeChooserPanelListeners} that the selected archetype has changed.
     * @param selectedArchetype the new selected archetype or <code>null</code>
     * if none is selected
     */
    private void fireSelectedArchetypeChanged(@Nullable final R selectedArchetype) {
        for (final ArchetypeChooserPanelListener<G, A, R> listener : listeners.getListeners()) {
            listener.selectedArchetypeChanged(selectedArchetype);
        }
    }

}
