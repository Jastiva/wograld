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

package net.sf.gridarta.gui.mapfiles;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import net.sf.gridarta.gui.dialog.newmap.NewMapDialogFactory;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.pickmapsettings.PickmapSettings;
import net.sf.gridarta.model.pickmapsettings.PickmapSettingsListener;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates functionality related to actions for pickmap folder selection.
 * @author Andreas Kirschbaum
 */
public class MapFolderTreeActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The model to operate on.
     */
    @NotNull
    private final MapFolderTree<G, A, R> mapFolderTree;

    /**
     * The {@link PickmapSettings} to use.
     */
    @NotNull
    private final PickmapSettings pickmapSettings;

    /**
     * The factory for creating new pickmap folders.
     */
    @NotNull
    private final NewMapDialogFactory<G, A, R> newMapDialogFactory;

    /**
     * The dialog key for "confirm delete folder?".
     */
    @NotNull
    private final String confirmDeleteFolderKey;

    /**
     * The dialog key for "folder is not empty".
     */
    @NotNull
    private final String deleteFolderNotEmptyKey;

    /**
     * The pickmap folders menu to update.
     */
    @Nullable
    private JMenu folderMenu;

    /**
     * The last known active folder.
     */
    @Nullable
    private MapFolder<G, A, R> activeMapFolder;

    /**
     * Action for "create folder".
     */
    @NotNull
    private final Action aCreateFolderAction;

    /**
     * Action called for "delete active folder".
     */
    private final Action aDeletePickmapFolder;

    /**
     * The {@link MapFolderTreeListener} which is registered to the selected
     * pickmap.
     */
    private final MapFolderTreeListener<G, A, R> mapFolderTreeListener = new MapFolderTreeListener<G, A, R>() {

        @Override
        public void activeMapFolderChanged(@Nullable final MapFolder<G, A, R> mapFolder) {
            activeMapFolder = mapFolder;
            refresh();
        }

        @Override
        public void folderAdded(@NotNull final MapFolder<G, A, R> mapFolder) {
            refresh();
        }

        @Override
        public void folderRemoved(@NotNull final MapFolder<G, A, R> mapFolder) {
            refresh();
        }

    };

    /**
     * The {@link PickmapSettingsListener} attached to {@link
     * #pickmapSettings}.
     */
    @NotNull
    private final PickmapSettingsListener pickmapSettingsListener = new PickmapSettingsListener() {

        @Override
        public void lockedChanged(final boolean locked) {
            refresh();
        }

    };

    /**
     * Creates a new instance.
     * @param mapFolderTree the model to operate on
     * @param pickmapSettings the pickmap settings to use
     * @param newMapDialogFactory the factory for creating new pickmap folders
     * @param createFolderKey the action key for "create folder"
     * @param deleteFolderKey the action key for "delete folder"
     * @param confirmDeleteFolderKey the dialog key for "confirm delete
     * folder?"
     * @param deleteFolderNotEmptyKey the dialog key for "delete folder not
     * empty"
     */
    public MapFolderTreeActions(@NotNull final MapFolderTree<G, A, R> mapFolderTree, @NotNull final PickmapSettings pickmapSettings, @NotNull final NewMapDialogFactory<G, A, R> newMapDialogFactory, @NotNull final String createFolderKey, @NotNull final String deleteFolderKey, @NotNull final String confirmDeleteFolderKey, @NotNull final String deleteFolderNotEmptyKey) {
        this.mapFolderTree = mapFolderTree;
        this.pickmapSettings = pickmapSettings;
        this.newMapDialogFactory = newMapDialogFactory;
        this.confirmDeleteFolderKey = confirmDeleteFolderKey;
        this.deleteFolderNotEmptyKey = deleteFolderNotEmptyKey;
        mapFolderTree.addModelListener(mapFolderTreeListener);
        activeMapFolder = mapFolderTree.getActiveMapFolder();
        aCreateFolderAction = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, createFolderKey);
        aDeletePickmapFolder = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, deleteFolderKey);
        pickmapSettings.addPickmapSettingsListener(pickmapSettingsListener);
        refresh();
    }

    /**
     * Sets the pickmap folders menu to manage. If the passed menu is
     * <code>null</code>, nothing is updated.
     * @param folderMenu the pickmap folders menu
     */
    @Deprecated
    public void setPickmapFoldersMenu(@Nullable final JMenu folderMenu) {
        this.folderMenu = folderMenu;
        refresh();
    }

    /**
     * Updates the actions' states.
     */
    private void refresh() {
        aCreateFolderAction.setEnabled(doCreateFolder(false));
        aDeletePickmapFolder.setEnabled(doDeletePickmapFolder(false));

        if (folderMenu == null) {
            return;
        }

        MenuUtils.removeAll(folderMenu);
        for (final MapFolder<G, A, R> mapFolder : mapFolderTree) {
            final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
            final Action action = new MapFolderTreeAction<G, A, R>(mapFolderTree, mapFolder, menuItem);
            menuItem.setAction(action);
            menuItem.setSelected(mapFolder == activeMapFolder);
            folderMenu.add(menuItem);
        }
    }

    /**
     * Action callback function to create a new pickmap folder.
     */
    @ActionMethod
    public void createPickmapFolder() {
        doCreateFolder(true);
    }

    /**
     * Executes the "create folder" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doCreateFolder(final boolean performAction) {
        if (pickmapSettings.isLocked()) {
            return false;
        }

        if (performAction) {
            newMapDialogFactory.showNewPickmapFolderDialog(mapFolderTree, activeMapFolder);
        }

        return true;
    }

    /**
     * Action callback function to delete the current pickmap folder.
     */
    @ActionMethod
    public void deletePickmapFolder() {
        doDeletePickmapFolder(true);
    }

    /**
     * Executes the "delete pickmap" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doDeletePickmapFolder(final boolean performAction) {
        if (pickmapSettings.isLocked()) {
            return false;
        }

        final MapFolder<G, A, R> mapFolder = activeMapFolder;
        if (mapFolder == null || mapFolder.getParent() == null) {
            return false;
        }

        if (performAction) {
            if (mapFolder.getPickmaps() > 0 && !ACTION_BUILDER.showQuestionDialog(null/*XXX:parent*/, confirmDeleteFolderKey, mapFolder.getName(), mapFolder.getPickmaps())) {
                return false;
            }

            try {
                mapFolderTree.removeMapFolder(mapFolder, true);
            } catch (final MapFolderNotEmptyException ex) {
                ACTION_BUILDER.showMessageDialog(null/*XXX:parent*/, deleteFolderNotEmptyKey, mapFolder.getName());
            }
        }

        return true;
    }

}
