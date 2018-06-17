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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A named folder within the {@link ArchetypeChooserModel}. A folder consists of
 * a set of {@link Archetype Archetypes}. One (or no) archetype may be selected
 * at any time.
 * @author Andreas Kirschbaum
 */
public class ArchetypeChooserFolder<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The folder name.
     */
    @NotNull
    private final String name;

    /**
     * The {@link Archetype Archetypes} in this folder.
     */
    @NotNull
    private final Set<R> archetypes = new HashSet<R>();

    /**
     * The selected {@link Archetype} or <code>null</code> if none is selected.
     * It must be part of {@link #archetypes}.
     */
    @Nullable
    private R selectedArchetype;

    /**
     * The registered listeners.
     */
    @NotNull
    private final EventListenerList2<ArchetypeChooserFolderListener<G, A, R>> listeners = new EventListenerList2<ArchetypeChooserFolderListener<G, A, R>>(ArchetypeChooserFolderListener.class);

    /**
     * Creates a new instance.
     * @param name the folder name
     */
    public ArchetypeChooserFolder(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Adds a listener to be notified of changes.
     * @param listener the listener
     */
    public void addArchetypeChooserFolderListener(@NotNull final ArchetypeChooserFolderListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be notified of changes.
     * @param listener the listener
     */
    public void removeArchetypeChooserFolderListener(@NotNull final ArchetypeChooserFolderListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the folder name.
     * @return the folder name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns the {@link Archetype Archetypes}.
     * @return the archetypes in no particular order
     */
    @NotNull
    public Collection<R> getArchetypes() {
        return Collections.unmodifiableSet(archetypes);
    }

    /**
     * Adds an {@link Archetype} to this folder.
     * @param archetype the archetype to add
     */
    public void addArchetype(@NotNull final R archetype) {
        archetypes.add(archetype);
    }

    /**
     * Returns the selected {@link Archetype}.
     * @return the selected archetype or <code>null</code> if none is selected
     */
    @Nullable
    public R getSelectedArchetype() {
        return selectedArchetype;
    }

    /**
     * Sets the selected {@link Archetype}.
     * @param selectedArchetype the selected archetype or <code>null</code> if
     * none is selected
     */
    public void setSelectedArchetype(@Nullable final R selectedArchetype) {
        if (selectedArchetype != null && !archetypes.contains(selectedArchetype)) {
            throw new IllegalArgumentException("selected archetype " + selectedArchetype.getAnimName() + " is not part of the folder");
        }

        if (this.selectedArchetype == selectedArchetype) {
            return;
        }

        this.selectedArchetype = selectedArchetype;
        for (final ArchetypeChooserFolderListener<G, A, R> listener : listeners.getListeners()) {
            listener.selectedArchetypeChanged(selectedArchetype);
        }
    }

    /**
     * Returns whether an {@link Archetype} is part of this folder.
     * @param archetype the archetype
     * @return whether the archetype is part of this folder
     */
    public boolean containsArchetype(@NotNull final R archetype) {
        return archetypes.contains(archetype);
    }

}
