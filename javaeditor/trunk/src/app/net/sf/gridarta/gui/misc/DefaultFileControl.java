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

package net.sf.gridarta.gui.misc;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.gui.dialog.newmap.NewMapDialogFactory;
import net.sf.gridarta.gui.map.MapPreviewAccessory;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.CommonConstants;
import net.sf.gridarta.utils.FileChooserUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default {@link FileControl} implementation. Asks the user for directions.
 * @author Andreas Kirschbaum
 */
public class DefaultFileControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements FileControl<G, A, R> {

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link GlobalSettings}.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapImageCache}.
     */
    @NotNull
    private final MapImageCache<G, A, R> mapImageCache;

    /**
     * The {@link MapManager}.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The parent component for showing dialog boxes.
     */
    @NotNull
    private final Component parent;

    /**
     * The {@link FileFilter} for selecting map files.
     */
    @NotNull
    private final FileFilter mapFileFilter;

    /**
     * The {@link FileFilter} for selecting script files.
     */
    @NotNull
    private final FileFilter scriptFileFilter;

    /**
     * The {@link NewMapDialogFactory}.
     */
    @NotNull
    private final NewMapDialogFactory<G, A, R> newMapDialogFactory;

    /**
     * The file extension for script files.
     */
    @NotNull
    private final String scriptExtension;

    /**
     * The {@link ScriptEditControl} to forward to.
     */
    @NotNull
    private final ScriptEditControl scriptEditControl;

    /**
     * The {@link JFileChooser} for opening a file. Set to <code>null</code> if
     * not yet created.
     */
    @Nullable
    private JFileChooser fileChooser;

    /**
     * Creates a new instance.
     * @param globalSettings the global settings instance
     * @param mapImageCache the map image cache
     * @param mapManager the map manager
     * @param mapViewsManager the map views manager
     * @param parent the parent component for showing dialog boxes
     * @param mapFileFilter the file filter for selecting map files
     * @param scriptFileFilter the file filter for selecting script files
     * @param newMapDialogFactory the new map dialog factory
     * @param scriptExtension the file extension for script files
     * @param scriptEditControl the script edit control to forward to
     */
    public DefaultFileControl(@NotNull final GlobalSettings globalSettings, @NotNull final MapImageCache<G, A, R> mapImageCache, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewsManager<G, A, R> mapViewsManager, @NotNull final Component parent, @NotNull final FileFilter mapFileFilter, @NotNull final FileFilter scriptFileFilter, @NotNull final NewMapDialogFactory<G, A, R> newMapDialogFactory, @NotNull final String scriptExtension, @NotNull final ScriptEditControl scriptEditControl) {
        this.globalSettings = globalSettings;
        this.mapImageCache = mapImageCache;
        this.mapManager = mapManager;
        this.mapViewsManager = mapViewsManager;
        this.parent = parent;
        this.mapFileFilter = mapFileFilter;
        this.scriptFileFilter = scriptFileFilter;
        this.newMapDialogFactory = newMapDialogFactory;
        this.scriptExtension = scriptExtension;
        this.scriptEditControl = scriptEditControl;
    }

    /**
     * Creates the {@link JFileChooser} for opening a file.
     * @return the new file chooser
     */
    @NotNull
    private JFileChooser createFileChooser() {
        final JFileChooser tmpFileChooser = new JFileChooser();
        tmpFileChooser.setDialogTitle(ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.title"));
        tmpFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        tmpFileChooser.setMultiSelectionEnabled(true);
        tmpFileChooser.addChoosableFileFilter(scriptFileFilter);
        tmpFileChooser.addChoosableFileFilter(mapFileFilter);
        if (globalSettings.getMapsDirectory().exists()) {
            FileChooserUtils.setCurrentDirectory(tmpFileChooser, globalSettings.getMapsDirectory());
        }
        tmpFileChooser.setAccessory(new MapPreviewAccessory(mapImageCache, tmpFileChooser));
        return tmpFileChooser;
    }

    /**
     * {@inheritDoc}
     */
    @ActionMethod
    @Override
    public void editScript() {
        openFile(false);
    }

    /**
     * {@inheritDoc}
     */
    @ActionMethod
    @Override
    public void openFile() {
        openFile(true);
    }

    /**
     * The user wants to open a file. The file filters and preselected file
     * filter are set accordingly to <code>mapFilter</code>.
     * @param mapFilter set to <code>true</code> if the user probably wants to
     * open a map, <code>false</code> otherwise
     */
    private void openFile(final boolean mapFilter) {
        if (fileChooser == null) {
            fileChooser = createFileChooser();
        }
        final JFileChooser tmpFileChooser = fileChooser;
        if (mapFilter) {
            tmpFileChooser.setFileFilter(mapFileFilter);
        } else {
            tmpFileChooser.setFileFilter(scriptFileFilter);
        }
        FileChooserUtils.sanitizeCurrentDirectory(tmpFileChooser);
        final int returnVal = tmpFileChooser.showOpenDialog(parent);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final File[] files = tmpFileChooser.getSelectedFiles();
        for (final File file : files) {
            final boolean isScriptFile = file.getName().toLowerCase().endsWith(scriptExtension);
            if (file.isFile()) {
                if (isScriptFile) {
                    scriptEditControl.openScriptFile(file.getAbsolutePath());
                } else {
                    try {
                        mapViewsManager.openMapFileWithView(file, null, null);
                    } catch (final IOException ex) {
                        reportLoadError(file, ex.getMessage());
                    }
                }
            } else if (!file.exists()) {
                if (isScriptFile) {
                    scriptEditControl.newScript(); // TODO: pass filename
                } else {
                    newMapDialogFactory.newMap(); // XXX: pass file
                }
            } // If neither branch matches, it's a directory - what to do with directories?
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(@NotNull final MapControl<G, A, R> mapControl) {
        final File file = mapControl.getMapModel().getMapFile();
        if (file != null) {
            try {
                mapControl.save();
            } catch (final IOException ex) {
                reportSaveError(file, ex.getMessage());
                return false;
            }
            return true;
        }

        return saveAs(mapControl);
    }

    /**
     * {@inheritDoc}
     */
    @ActionMethod
    @Override
    public void saveAllMaps() {
        for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
            if (mapControl.getMapModel().isModified() && !save(mapControl)) {
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @ActionMethod
    @Override
    public boolean closeAllMaps() {
        while (true) {
            final MapControl<G, A, R> mapControl = mapManager.getOpenMap();
            if (mapControl == null) {
                break;
            }

            if (!confirmSaveChanges(mapControl)) {
                return false;
            }

            mapManager.closeMap(mapControl);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveAs(@NotNull final MapControl<G, A, R> mapControl) {
        final File file = saveMapAs(mapControl);
        if (file == null) {
            return false;
        }

        try {
            mapControl.saveAs(file);
        } catch (final IOException ex) {
            reportSaveError(file, ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Displays a "save as" dialog.
     * @param mapControl the map control for the dialog
     * @return the selected file or <code>null</code> if cancelled
     */
    @Nullable
    private File saveMapAs(@NotNull final MapControl<G, A, R> mapControl) {
        final JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        saveFileChooser.setDialogTitle("Save Map Or Script As");
        saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        saveFileChooser.setMultiSelectionEnabled(false);
        saveFileChooser.resetChoosableFileFilters();
        saveFileChooser.addChoosableFileFilter(scriptFileFilter);
        saveFileChooser.setFileFilter(mapFileFilter);

        final File mapFile = mapControl.getMapModel().getMapFile();
        final File defaultDir = mapFile == null ? null : mapFile.getParentFile();
        if (defaultDir != null && defaultDir.exists()) {
            FileChooserUtils.setCurrentDirectory(saveFileChooser, defaultDir);
            saveFileChooser.setSelectedFile(mapFile);
        } else {
            final File currentSaveMapDirectory = globalSettings.getCurrentSaveMapDirectory();
            FileChooserUtils.setCurrentDirectory(saveFileChooser, currentSaveMapDirectory);
            saveFileChooser.setSelectedFile(new File(currentSaveMapDirectory, mapFile == null ? CommonConstants.DEFAULT_MAP_FILENAME : mapFile.getName()));
        }

        final int returnVal = saveFileChooser.showSaveDialog(parent);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        globalSettings.setCurrentSaveMapDirectory(saveFileChooser.getCurrentDirectory());
        final File file = saveFileChooser.getSelectedFile();
        if (file.exists() && ACTION_BUILDER.showConfirmDialog(parent, JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION, "overwriteOtherFile", file.getName()) != JOptionPane.OK_OPTION) {
            return null;
        }

        return file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean confirmSaveChanges(@NotNull final MapControl<G, A, R> mapControl) {
        if (mapControl.getMapModel().isModified()) {
            final int result = ACTION_BUILDER.showConfirmDialog(parent, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, "confirmSaveChanges", mapControl.getMapModel().getMapArchObject().getMapName());
            if (result == JOptionPane.YES_OPTION) {
                if (!save(mapControl)) {
                    return false;
                }
            } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportSaveError(@NotNull final MapControl<G, A, R> mapControl, @NotNull final String message) {
        final File file = mapControl.getMapModel().getMapFile();
        assert file != null;
        reportSaveError(file, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportSaveError(@NotNull final File mapFile, @NotNull final String message) {
        ACTION_BUILDER.showMessageDialog(parent, "encodeMapFile", mapFile, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportLoadError(@Nullable final File file, @NotNull final String message) {
        ACTION_BUILDER.showMessageDialog(parent, "openFileLoadMap", file == null ? "<unknown>" : file, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportOutOfMapBoundsDeleted(@NotNull final File file, final int outOfMapBoundsDeleted, @NotNull final StringBuilder outOfMapBoundsObjects) {
        ACTION_BUILDER.showMessageDialog(parent, "openFileOutOfMapBoundsDeleted", file, outOfMapBoundsDeleted, outOfMapBoundsObjects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportOutOfMemory(@NotNull final File file) {
        ACTION_BUILDER.showMessageDialog(parent, "mapOutOfMemory", file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportTeleportCharacterError(@NotNull final String mapPath, @NotNull final String message) {
        ACTION_BUILDER.showMessageDialog(parent, "teleportCharacterError", mapPath, message);
    }

}
