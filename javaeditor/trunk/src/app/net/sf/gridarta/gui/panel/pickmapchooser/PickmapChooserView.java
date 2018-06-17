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
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.mapfiles.MapFile;
import net.sf.gridarta.gui.mapfiles.MapFolder;
import net.sf.gridarta.gui.mapfiles.MapFolderListener;
import net.sf.gridarta.gui.mapfiles.MapFolderTree;
import net.sf.gridarta.gui.mapfiles.MapFolderTreeListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.utils.CommonConstants;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A View for choosing pickmaps.
 * @author Andreas Kirschbaum
 */
public class PickmapChooserView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(PickmapChooserView.class);

    /**
     * The attached {@link PickmapChooserModel}.
     */
    private final PickmapChooserModel<G, A, R> pickmapChooserModel;

    /**
     * The model to display.
     */
    @NotNull
    private final MapFolderTree<G, A, R> mapFolderTree;

    /**
     * Panel with pickmaps.
     */
    private final JTabbedPane pickmapPanel = new JTabbedPane(SwingConstants.TOP);

    /**
     * The last known active folder.
     */
    private MapFolder<G, A, R> activeMapFolder;

    /**
     * The listener attached to {@link #mapFolderTree}.
     */
    private final MapFolderTreeListener<G, A, R> mapFolderTreeListener = new MapFolderTreeListener<G, A, R>() {

        @Override
        public void activeMapFolderChanged(@Nullable final MapFolder<G, A, R> mapFolder) {
            if (activeMapFolder != null) {
                activeMapFolder.removeFolderListener(mapFolderListener);
            }
            activeMapFolder = mapFolder;
            if (activeMapFolder != null) {
                activeMapFolder.addFolderListener(mapFolderListener);
            }

            synchronized (mapFolderTree) {
                pickmapChooserModel.clear();
                pickmapPanel.removeAll();
                if (activeMapFolder != null) {
                    for (final MapFile<G, A, R> mapFile : activeMapFolder) {
                        addPickmap(mapFile);
                    }
                }
            }
        }

        @Override
        public void folderAdded(@NotNull final MapFolder<G, A, R> mapFolder) {
            // ignore
        }

        @Override
        public void folderRemoved(@NotNull final MapFolder<G, A, R> mapFolder) {
            // ignore
        }

    };

    /**
     * The listener attached to {@link #activeMapFolder}.
     */
    private final MapFolderListener<G, A, R> mapFolderListener = new MapFolderListener<G, A, R>() {

        @Override
        public void pickmapAdded(@NotNull final MapFile<G, A, R> mapFile) {
            addPickmap(mapFile);
        }

        @Override
        public void pickmapRemoved(@NotNull final MapFile<G, A, R> mapFile) {
            removePickmap(mapFile);
        }

        @Override
        public void pickmapReverted(@NotNull final MapFile<G, A, R> mapFile, @NotNull final MapControl<G, A, R> oldPickmap) {
            revertPickmap(mapFile, oldPickmap);
        }

    };

    /**
     * The {@link PickmapChooserModelListener} attached to {@link
     * #pickmapChooserModel} to detect modified pickmaps.
     */
    private final PickmapChooserModelListener<G, A, R> pickmapChooserModelListener = new PickmapChooserModelListener<G, A, R>() {

        @Override
        public void activePickmapChanged(@Nullable final MapFile<G, A, R> mapFile) {
        }

        @Override
        public void pickmapReverted(@NotNull final MapFile<G, A, R> mapFile) {
        }

        @Override
        public void pickmapModifiedChanged(final int index, final MapFile<G, A, R> mapFile) {
            pickmapPanel.setTitleAt(index, getTitle(mapFile));
        }

    };

    /**
     * Creates a new instance.
     * @param pickmapChooserModel the attached pickmap chooser model
     * @param mapFolderTree the model to display
     */
    public PickmapChooserView(@NotNull final PickmapChooserModel<G, A, R> pickmapChooserModel, @NotNull final MapFolderTree<G, A, R> mapFolderTree) {
        this.pickmapChooserModel = pickmapChooserModel;
        this.mapFolderTree = mapFolderTree;
        pickmapChooserModel.addPickmapChooserListener(pickmapChooserModelListener);
        pickmapPanel.setBorder(BorderFactory.createEmptyBorder(CommonConstants.SPACE_PICKMAP_ARCHETYPE_TOP, 0, 0, 0));
        mapFolderTree.addModelListener(mapFolderTreeListener);
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (mapFolderTree) {
            mapFolderTreeListener.activeMapFolderChanged(mapFolderTree.getActiveMapFolder());
        }
    }

    /**
     * Sets the popup menu to show.
     * @param popupMenu the popup menu to use
     */
    public void setPopupMenu(@NotNull final JPopupMenu popupMenu) {
        pickmapPanel.setComponentPopupMenu(popupMenu);
    }

    /**
     * Adds a {@link ChangeListener} to be notified when the selected pickmap
     * tab changes.
     * @param changeListener the listener to add
     */
    public void addChangeListener(@NotNull final ChangeListener changeListener) {
        pickmapPanel.addChangeListener(changeListener);
    }

    /**
     * Called when a new pickmap has been added to the model.
     * @param mapFile the pickmap
     */
    private void addPickmap(@NotNull final MapFile<G, A, R> mapFile) {
        try {
            mapFile.loadPickmap();
        } catch (final IOException ex) {
            log.warn("cannot load pickmap " + mapFile.getFile() + ": " + ex.getMessage());
            return;
        }

        final int index = pickmapChooserModel.addMapFile(mapFile);
        final MapView<G, A, R> mapView = mapFile.getMapView();
        pickmapPanel.insertTab(getTitle(mapFile), null, mapView == null ? null : mapView.getScrollPane(), null, index);
    }

    /**
     * Called when a pickmap has been removed from the model.
     * @param mapFile the pickmap
     */
    private void removePickmap(@NotNull final MapFile<G, A, R> mapFile) {
        final int index = pickmapChooserModel.removeMapFile(mapFile);
        if (index >= 0) {
            pickmapPanel.remove(index);
        }
    }

    /**
     * Called when a pickmap in the model has been reverted.
     * @param mapFile the pickmap
     * @param oldPickmap the map control before revert
     */
    private void revertPickmap(@NotNull final MapFile<G, A, R> mapFile, @NotNull final MapControl<G, A, R> oldPickmap) {
        final int index = pickmapChooserModel.revertMapFile(oldPickmap, mapFile);
        if (index == -1) {
            return;
        }
        pickmapPanel.setTitleAt(index, getTitle(mapFile));
        final MapView<G, A, R> mapView = mapFile.getMapView();
        pickmapPanel.setComponentAt(index, mapView == null ? null : mapView.getScrollPane());
    }

    /**
     * Sets the active pickmap.
     * @param mapFile the pickmap
     */
    public void setActivePickmap(@NotNull final MapFile<G, A, R> mapFile) {
        final int index = pickmapChooserModel.indexOf(mapFile);
        if (index == -1) {
            return;
        }
        pickmapPanel.setSelectedIndex(index);
    }

    /**
     * Returns the active folder.
     * @return the active folder
     */
    @Nullable
    public MapFolder<G, A, R> getActiveFolder() {
        return activeMapFolder;
    }

    /**
     * Returns the title of a pickmap to use as the tab name.
     * @param mapFile the pickmap
     * @return the title
     */
    public String getTitle(@NotNull final MapFile<G, A, R> mapFile) {
        final String name = mapFile.getFile().getName();
        final MapControl<G, A, R> mapControl = mapFile.getPickmap();
        final boolean modified = mapControl != null && mapControl.getMapModel().isModified();
        return modified ? name + " *" : name;
    }

    /**
     * Returns the {@link JTabbedPane} with all pickmaps.
     * @return the <code>JTabbedPane</code> with all pickmaps
     */
    public Component getPickmapPanel() {
        return pickmapPanel;
    }

    /**
     * Returns the selected index.
     * @return the index
     */
    public int getSelectedIndex() {
        return pickmapPanel.getSelectedIndex();
    }

}
