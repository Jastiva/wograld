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

package net.sf.gridarta.gui.panel.pickmapchooser;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.gui.dialog.newmap.NewMapDialogFactory;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapfiles.DuplicatePickmapException;
import net.sf.gridarta.gui.mapfiles.InvalidNameException;
import net.sf.gridarta.gui.mapfiles.MapFile;
import net.sf.gridarta.gui.mapfiles.MapFolder;
import net.sf.gridarta.gui.mapfiles.MapFolderTree;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooserTab;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlListener;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.pickmapsettings.PickmapSettings;
import net.sf.gridarta.model.pickmapsettings.PickmapSettingsListener;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The <code>PickmapChooserControl</code> manages the pickmap panel and most
 * pickmap-related code in general.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class PickmapChooserControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ObjectChooserTab<G, A, R> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(PickmapChooserControl.class);

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link PickmapChooserModel} to control.
     */
    @NotNull
    private final PickmapChooserModel<G, A, R> pickmapChooserModel;

    /**
     * The {@link PickmapSettings} to use.
     */
    @NotNull
    private final PickmapSettings pickmapSettings;

    /**
     * The {@link MapArchObjectFactory} instance.
     */
    @NotNull
    private final MapArchObjectFactory<A> mapArchObjectFactory;

    /**
     * The map reader factory instance.
     */
    @NotNull
    private final MapReaderFactory<G, A> mapReaderFactory;

    /**
     * The view for this control.
     */
    @NotNull
    private final PickmapChooserView<G, A, R> view;

    /**
     * The model.
     */
    @NotNull
    private final MapFolderTree<G, A, R> mapFolderTree;

    /**
     * The factory for creating new pickmaps.
     */
    @NotNull
    private final NewMapDialogFactory<G, A, R> newMapDialogFactory;

    /**
     * The {@link MapManager} instance for loading pickmaps.
     */
    @NotNull
    private final MapManager<G, A, R> pickmapManager;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The {@link FileControl} to use.
     */
    @NotNull
    private FileControl<G, A, R> fileControl;

    /**
     * Action called for "add new pickmap".
     */
    private final Action aAddNewPickmap = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, "addNewPickmap");

    /**
     * Action called for "open active pickmap as map".
     */
    private final Action aOpenPickmapMap = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, "openPickmapMap");

    /**
     * Action called for "delete active pickmap".
     */
    private final Action aDeletePickmap = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, "deletePickmap");

    /**
     * Action called for "save active pickmap".
     */
    private final Action aSavePickmap = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, "savePickmap");

    /**
     * Action called for "revert active pickmap".
     */
    private final Action aRevertPickmap = ActionUtils.newAction(ACTION_BUILDER, "Pickmap", this, "revertPickmap");

    /**
     * The parent component for error messages.
     */
    @NotNull
    private final Component parent;

    /**
     * The current active pickmap on top.
     */
    @Nullable
    private MapFile<G, A, R> currentMapFile;

    /**
     * The current active pickmap on top.
     */
    @Nullable
    private MapControl<G, A, R> currentPickmapControl;

    /**
     * Set if the pickmap panel is active.
     */
    private boolean active;

    /**
     * The map control listener which is registered to the selected pickmap.
     */
    private final MapControlListener<G, A, R> mapControlListener = new MapControlListener<G, A, R>() {

        @Override
        public void saved(@NotNull final DefaultMapControl<G, A, R> mapControl) {
            /*
             * If we open a pickmap in the editor, is handled as normal map.
             * to find out its original a pickmap we check the file name.
             */
            reloadPickmap(mapControl.getMapModel().getMapFile());
        }

    };

    /**
     * The {@link MapModelListener} which is registered to the selected
     * pickmap.
     */
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            // ignore
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            // ignore
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            refresh();
        }

    };

    /**
     * The pickmap chooser listener used to track the currently active pickmap.
     */
    private final PickmapChooserModelListener<G, A, R> pickmapChooserModelListener = new PickmapChooserModelListener<G, A, R>() {

        @Override
        public void activePickmapChanged(@Nullable final MapFile<G, A, R> mapFile) {
            setCurrentPickmap(mapFile);
        }

        @Override
        public void pickmapReverted(@NotNull final MapFile<G, A, R> mapFile) {
            setCurrentPickmap(mapFile);
        }

        @Override
        public void pickmapModifiedChanged(final int index, final MapFile<G, A, R> mapFile) {
            // ignore
        }

    };

    /**
     * The {@link ChangeListener} attached to {link #view}.
     */
    @NotNull
    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            final MapFile<G, A, R> mapFile = getSelectedPickmap();
            final MapControl<G, A, R> mapControl = mapFile == null ? null : mapFile.getPickmap();
            if (mapControl != null) {
                mapViewsManager.getMapViewFrame(mapControl).getMapCursor().deactivate();
            }
            pickmapChooserModel.fireActivePickmapChanged(mapFile);
        }

    };

    /**
     * The {@link MapManagerListener} to track existing pickmaps.
     */
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            if (!mapControl.isPickmap()) {
                mapControl.addMapControlListener(mapControlListener);
            }
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            if (!mapControl.isPickmap()) {
                mapControl.removeMapControlListener(mapControlListener);
            }
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
     * Create a PickmapChooserControl.
     * @param pickmapChooserModel the pickmap chooser model to control
     * @param pickmapSettings the pickmap settings to use
     * @param newMapDialogFactory the factory for creating new pickmaps
     * @param mapArchObjectFactory the map arch object factory instance
     * @param mapReaderFactory the map reader factory instance
     * @param mapFolderTree the map folder tree instance
     * @param mapManager the map manager instance
     * @param parent the parent component
     * @param pickmapManager the pickmap manager for loading pickmaps
     * @param mapViewsManager the map views
     */
    public PickmapChooserControl(@NotNull final PickmapChooserModel<G, A, R> pickmapChooserModel, @NotNull final PickmapSettings pickmapSettings, @NotNull final NewMapDialogFactory<G, A, R> newMapDialogFactory, @NotNull final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final MapReaderFactory<G, A> mapReaderFactory, @NotNull final MapFolderTree<G, A, R> mapFolderTree, @NotNull final MapManager<G, A, R> mapManager, @NotNull final Component parent, @NotNull final MapManager<G, A, R> pickmapManager, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.pickmapChooserModel = pickmapChooserModel;
        this.pickmapSettings = pickmapSettings;
        this.mapArchObjectFactory = mapArchObjectFactory;
        this.mapReaderFactory = mapReaderFactory;
        this.mapFolderTree = mapFolderTree;
        this.parent = parent;
        this.newMapDialogFactory = newMapDialogFactory;
        this.pickmapManager = pickmapManager;
        this.mapViewsManager = mapViewsManager;
        ACTION_BUILDER.createToggles(true, this, "lockAllPickmaps");
        pickmapChooserModel.addPickmapChooserListener(pickmapChooserModelListener);
        view = new PickmapChooserView<G, A, R>(pickmapChooserModel, mapFolderTree);
        refresh();
        view.addChangeListener(changeListener);
        mapManager.addMapManagerListener(mapManagerListener);
        pickmapSettings.addPickmapSettingsListener(pickmapSettingsListener);
    }

    /**
     * Sets the popup menu to show.
     * @param popupMenu the popup menu to use
     */
    public void setPopupMenu(@NotNull final JPopupMenu popupMenu) {
        view.setPopupMenu(popupMenu);
    }

    @Deprecated
    public void setFileControl(@NotNull final FileControl<G, A, R> fileControl) {
        this.fileControl = fileControl;
    }

    /**
     * Add a new pickmap.
     * @param mapSize the pickmap size in squares
     * @param pickmapName the name of the pickmap
     * @throws IOException if an I/O error occurs
     * @throws DuplicatePickmapException if the pickmap name is not unique
     */
    public void newPickmap(@NotNull final Size2D mapSize, @NotNull final String pickmapName) throws DuplicatePickmapException, IOException {
        final MapFolder<G, A, R> activeMapFolder = view.getActiveFolder();
        if (activeMapFolder == null) {
            return;
        }

        if (!MapFile.isValidPickmapName(pickmapName)) {
            throw new IOException("invalid pickmap name: " + pickmapName);
        }

        for (final MapFile<G, A, R> mapFile : activeMapFolder) {
            if (pickmapName.equals(mapFile.getFile().getName())) {
                throw new DuplicatePickmapException(pickmapName);
            }
        }
        final File file = new File(activeMapFolder.getDir(), pickmapName);

        if (file.isDirectory()) {
            throw new IOException("pickmap file is a directory: " + file);
        }
        if (file.exists()) {
            throw new IOException("pickmap file exists: " + file);
        }

        final File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("cannot create directory: " + dir);
            }
        }

        final A mapArchObject = mapArchObjectFactory.newMapArchObject(true);
        mapArchObject.setMapSize(mapSize);
        mapArchObject.setMapName(pickmapName);
        final MapControl<G, A, R> mapControl = pickmapManager.newMap(null, mapArchObject, file, false);
        try {
            mapControl.save();
        } finally {
            pickmapManager.release(mapControl);
        }

        final MapFile<G, A, R> mapFile;
        try {
            mapFile = activeMapFolder.addPickmap(pickmapName, mapReaderFactory, pickmapManager);
        } catch (final InvalidNameException ex) {
            throw new AssertionError(ex); // the name has been checked before
        }
        view.setActivePickmap(mapFile);
    }

    /**
     * Reload a pickmap. Does nothing if the given file is unknown.
     * @param file the file of the pickmap
     */
    private void reloadPickmap(@NotNull final File file) {
        final MapFile<G, A, R> mapFile = pickmapChooserModel.getPickmap(file);
        if (mapFile != null) {
            try {
                mapFile.revert();
            } catch (final IOException ex) {
                log.warn("cannot reload pickmap " + mapFile.getFile() + ": " + ex.getMessage());
            }
        }
    }

    /**
     * Return whether all pickmaps are locked.
     * @return <code>true</code> if all pickmaps are locked
     */
    @ActionMethod
    public boolean isLockAllPickmaps() {
        return pickmapSettings.isLocked();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G getSelection() {
        final MapControl<G, A, R> pickmap = currentPickmapControl;
        if (pickmap == null) {
            return null;
        }

        final MapView<G, A, R> mapView = mapViewsManager.getMapViewFrame(pickmap);
        return mapView == null ? null : mapView.getSelectedGameObject();
    }

    /**
     * Invoked when the user wants to exit the application.
     * @return <code>true</code> if exiting the application is allowed
     */
    public boolean canExit() {
        final Collection<MapControl<G, A, R>> unsavedPickmaps = new HashSet<MapControl<G, A, R>>();
        mapFolderTree.getUnsavedPickmaps(unsavedPickmaps);
        for (final MapControl<G, A, R> pickmap : unsavedPickmaps) {
            final int result = ACTION_BUILDER.showConfirmDialog(parent, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, "pickmapConfirmSaveChanges", pickmap.getMapModel().getMapArchObject().getMapName());
            if (result == JOptionPane.YES_OPTION) {
                try {
                    pickmap.save();
                } catch (final IOException e) {
                    ACTION_BUILDER.showMessageDialog(parent, "encodeMapFile", pickmap.getMapModel().getMapFile(), e.getMessage());
                    return false;
                }
                if (pickmap.getMapModel().isModified()) {
                    return false;
                }
            } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the selected pickmap.
     * @return the selected pickmap
     */
    @Nullable
    public MapFile<G, A, R> getSelectedPickmap() {
        final int selectedIndex = view.getSelectedIndex();
        if (selectedIndex == -1) {
            return null;
        }
        try {
            return pickmapChooserModel.get(selectedIndex);
        } catch (final IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Component getComponent() {
        return view.getPickmapPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive(final boolean active) {
        if (this.active == active) {
            return;
        }

        this.active = active;
        refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(final G gameObject) {
        final G selection = getSelection();
        return selection != null && gameObject.isEqual(selection);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<G> getSelections() {
        final MapControl<G, A, R> pickmap = currentPickmapControl;
        if (pickmap == null) {
            return Collections.emptyList();
        }

        final MapView<G, A, R> mapView = mapViewsManager.getMapViewFrame(pickmap);
        return mapView == null ? Collections.<G>emptyList() : mapView.getSelectedGameObjects();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getTitle() {
        return ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.pickmapsTabTitle");
    }

    /**
     * Invoked when user wants to open a new pickmap.
     */
    @ActionMethod
    public void addNewPickmap() {
        doAddNewPickmap(true);
    }

    /**
     * Open active pickmap as normal map for extensive editing.
     */
    @ActionMethod
    public void openPickmapMap() {
        doOpenPickmapMap(true);
    }

    /**
     * Invoked when the user wants to delete the active pickmap.
     */
    @ActionMethod
    public void deletePickmap() {
        doDeletePickmap(true);
    }

    /**
     * Save current active pickmap.
     */
    @ActionMethod
    public void savePickmap() {
        doSavePickmap(true);
    }

    /**
     * Invoked when user wants to revert the current pickmap to previously saved
     * state.
     */
    @ActionMethod
    public void revertPickmap() {
        doRevertPickmap(true);
    }

    /**
     * Determine if "open pickmap as map" is enabled.
     * @return the pickmap to open as map, or <code>null</code> if "open pickmap
     *         as map" is disabled
     */
    @Nullable
    private MapFile<G, A, R> getOpenPickmapMap() {
        if (pickmapSettings.isLocked() || !active) {
            return null;
        }
        return currentMapFile;
    }

    /**
     * Set the currently active pickmap.
     * @param currentMapFile the currently active pickmap
     */
    private void setCurrentPickmap(@Nullable final MapFile<G, A, R> currentMapFile) {
        final MapControl<G, A, R> pickmapControl = currentMapFile == null ? null : currentMapFile.getPickmap();
        if (this.currentMapFile == currentMapFile && currentPickmapControl == pickmapControl) {
            return;
        }

        if (currentPickmapControl != null) {
            currentPickmapControl.removeMapControlListener(mapControlListener);
            currentPickmapControl.getMapModel().removeMapModelListener(mapModelListener);
        }

        this.currentMapFile = currentMapFile;
        currentPickmapControl = pickmapControl;

        if (currentPickmapControl != null) {
            currentPickmapControl.addMapControlListener(mapControlListener);
            currentPickmapControl.getMapModel().addMapModelListener(mapModelListener);
        }

        refresh();
    }

    /**
     * Set whether all pickmaps are locked.
     * @param lockAllPickmaps <code>true</code> if all pickmaps are locked
     */
    @ActionMethod
    public void setLockAllPickmaps(final boolean lockAllPickmaps) {
        pickmapSettings.setLocked(lockAllPickmaps);
    }

    /**
     * Update the actions' states.
     */
    private void refresh() {
        aAddNewPickmap.setEnabled(doAddNewPickmap(false));
        aOpenPickmapMap.setEnabled(doOpenPickmapMap(false));
        aDeletePickmap.setEnabled(doDeletePickmap(false));
        aSavePickmap.setEnabled(doSavePickmap(false));
        aRevertPickmap.setEnabled(doRevertPickmap(false));
    }

    /**
     * Performs or checks availability of the "add new pickmap" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doAddNewPickmap(final boolean performAction) {
        if (pickmapSettings.isLocked()) {
            return false;
        }

        if (performAction) {
            newMapDialogFactory.showNewPickmapDialog();
        }

        return true;
    }

    /**
     * Performs or checks availability of the "open pickmap map" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doOpenPickmapMap(final boolean performAction) {
        final MapFile<G, A, R> mapFile = getOpenPickmapMap();
        if (mapFile == null) {
            return false;
        }

        if (performAction) {
            final File file = mapFile.getFile();
            try {
                mapViewsManager.openMapFileWithView(file, null, null);
            } catch (final IOException ex) {
                fileControl.reportLoadError(file, ex.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Performs or checks availability of the "delete pickmap" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doDeletePickmap(final boolean performAction) {
        if (pickmapSettings.isLocked() || !active) {
            return false;
        }

        final MapFile<G, A, R> mapFile = currentMapFile;
        if (mapFile == null) {
            return false;
        }

        if (performAction) {
            if (!ACTION_BUILDER.showQuestionDialog(parent, "confirmDeletePickmap", mapFile.getFile().getName())) {
                return false;
            }

            mapFile.remove(true);
        }

        return true;
    }

    /**
     * Performs or checks availability of the "save pickmap" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSavePickmap(final boolean performAction) {
        if (pickmapSettings.isLocked() || !active) {
            return false;
        }

        if (currentMapFile == null || currentPickmapControl == null || !currentPickmapControl.getMapModel().isModified()) {
            return false;
        }

        final MapFile<G, A, R> mapFile = currentMapFile;
        if (mapFile == null) {
            return false;
        }

        if (performAction) {
            try {
                mapFile.save();
            } catch (final IOException e) {
                ACTION_BUILDER.showMessageDialog(parent, "encodeMapFile", mapFile, e.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Performs or checks availability of the "revert pickmap" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doRevertPickmap(final boolean performAction) {
        if (pickmapSettings.isLocked() || !active) {
            return false;
        }

        final MapFile<G, A, R> mapFile = currentMapFile;
        if (mapFile == null) {
            return false;
        }

        final File mapFileFile = mapFile.getFile();
        if (currentPickmapControl == null || !currentPickmapControl.getMapModel().isModified() || !mapFileFile.exists()) {
            return false;
        }

        final MapControl<G, A, R> mapControl = mapFile.getPickmap();
        if (mapControl == null) {
            return false;
        }

        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final File mapControlMapFile = mapModel.getMapFile();
        if (mapControlMapFile == null) {
            return false;
        }

        if (performAction) {
            if (!mapControlMapFile.exists()) {
                ACTION_BUILDER.showMessageDialog(parent, "revertPickmapGone", mapControlMapFile);
                return false;
            }

            if (mapModel.isModified() && !ACTION_BUILDER.showQuestionDialog(parent, "confirmRevertPickmap", mapFileFile.getName())) {
                return false;
            }

            try {
                mapFile.revert();
            } catch (final IOException ex) {
                ACTION_BUILDER.showMessageDialog(null/*XXX:parent*/, "pickmapIOError", mapFileFile.getName(), ex.getMessage());
                return false;
            }
        }

        return true;
    }

}
