/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.dialog.newmap;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapfiles.DuplicateMapFolderException;
import net.sf.gridarta.gui.mapfiles.InvalidNameException;
import net.sf.gridarta.gui.mapfiles.MapFolder;
import net.sf.gridarta.gui.mapfiles.MapFolderTree;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog to create a new pickmap folder.
 * @author Andreas Kirschbaum
 */
public class NewPickmapFolderDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractNewMapDialog<G, A, R> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The model to add the pickmap folder to.
     */
    @NotNull
    private final MapFolderTree<G, A, R> mapFolderTree;

    /**
     * The parent folder to add the pickmap folder to.
     */
    @Nullable
    private final MapFolder<G, A, R> parent;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * Textfield for the name of the map. The filename of the new map is unset
     * and will be specified when the user saves the map for the first time.
     */
    @NotNull
    private final JTextComponent folderNameField = new JTextField(16);

    /**
     * Creates a "new pickmap folder" dialog.
     * @param parentComponent the parent component of this dialog
     * @param mapFolderTree the model to add the pickmap folder to
     * @param parent the parent folder to add the pickmap folder to
     * @param mapViewsManager the map views
     */
    public NewPickmapFolderDialog(@NotNull final Component parentComponent, @NotNull final MapFolderTree<G, A, R> mapFolderTree, @Nullable final MapFolder<G, A, R> parent, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.mapFolderTree = mapFolderTree;
        this.parent = parent;
        this.mapViewsManager = mapViewsManager;
        init1(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "newPickmapFolder.title"));
        init2();
        addDocumentListener(folderNameField);
        updateOkButton();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected JPanel createMapNamePanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "newMapPickmapFolderName"));
        panel.add(folderNameField);
        return panel;
    }

    @Nullable
    @Override
    protected JPanel createMapParametersPanel() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields(@NotNull final JPanel panel, @NotNull final GridBagConstraints gbcLabel, @NotNull final GridBagConstraints gbcField) {
        throw new AssertionError();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean createNew() {
        final String folderName = getFolderName();
        if (folderName == null) {
            return false;
        }

        final MapFolder<G, A, R> mapFolder;
        try {
            mapFolder = new MapFolder<G, A, R>(parent, folderName, mapFolderTree.getBaseDir(), mapViewsManager);
        } catch (final InvalidNameException ex) {
            ACTION_BUILDER.showMessageDialog(this, "newPickmapFolderInvalidName", folderName);
            folderNameField.requestFocus();
            return false;
        }

        final File folderDir = mapFolder.getDir();
        if (folderDir.exists()) {
            ACTION_BUILDER.showMessageDialog(this, "newPickmapFolderExists", folderName);
            folderNameField.requestFocus();
            return false;
        }
        if (!folderDir.mkdir()) {
            ACTION_BUILDER.showMessageDialog(this, "mkdirIOError", folderDir);
            folderNameField.requestFocus();
            return false;
        }

        try {
            mapFolderTree.addMapFolder(mapFolder);
        } catch (final DuplicateMapFolderException ex) {
            ACTION_BUILDER.showMessageDialog(this, "newPickmapFolderExists", folderName);
            folderNameField.requestFocus();
            return false;
        }

        mapFolderTree.setActiveMapFolder(mapFolder);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isOkButtonEnabled() {
        return super.isOkButtonEnabled() && getFolderName() != null;
    }

    /**
     * Returns the current folder name value.
     * @return the folder name or <code>null</code> if unset or invalid
     */
    @Nullable
    private String getFolderName() {
        final String folderName = folderNameField.getText();
        if (folderName.length() <= 0) {
            return null;
        }

        return folderName;
    }

}
