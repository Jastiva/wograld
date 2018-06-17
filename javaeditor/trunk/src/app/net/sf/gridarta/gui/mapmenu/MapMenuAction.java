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

package net.sf.gridarta.gui.mapmenu;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link AbstractAction} for a {@link MapMenuEntryMap} instance.
 * @author Andreas Kirschbaum
 */
public class MapMenuAction extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The associated {@link MapMenuEntryMap} instance.
     */
    @NotNull
    private final MapMenuEntryMap mapMenuEntryMap;

    /**
     * The {@link MapViewsManager} for opening map files.
     */
    @NotNull
    private final MapViewsManager<?, ?, ?> mapViewsManager;

    /**
     * The {@link FileControl} for reporting errors.
     */
    @NotNull
    private final FileControl<?, ?, ?> fileControl;

    /**
     * Creates a new instance.
     * @param mapMenuEntryMap the map menu entry for this action
     * @param mapViewsManager the map views manager for opening map files
     * @param fileControl the file control for reporting errors
     */
    public MapMenuAction(@NotNull final MapMenuEntryMap mapMenuEntryMap, @NotNull final MapViewsManager<?, ?, ?> mapViewsManager, @NotNull final FileControl<?, ?, ?> fileControl) {
        this.mapMenuEntryMap = mapMenuEntryMap;
        this.mapViewsManager = mapViewsManager;
        this.fileControl = fileControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final File mapFile = mapMenuEntryMap.getMapFile();
        try {
            mapViewsManager.openMapFileWithView(mapFile, null, null);
        } catch (final IOException ex) {
            fileControl.reportLoadError(mapFile, ex.getMessage());
        }
    }

    /**
     * Updates properties.
     * @param mapImageCache the map image cache to query
     */
    public void update(@NotNull final MapImageCache<?, ?, ?> mapImageCache) {
        final File mapFile = mapMenuEntryMap.getMapFile();
        final String title = mapMenuEntryMap.getTitle();
        final String shortDescription = ACTION_BUILDER.format("mapItem.shortdescriptionformat", title, mapFile);
        putValue(SHORT_DESCRIPTION, shortDescription);
        putValue(MNEMONIC_KEY, null);
        putValue(NAME, title);
        final Image icon = mapImageCache.getOrCreateIcon(mapFile);
        if (icon != null) {
            putValue(SMALL_ICON, new ImageIcon(icon));
        } else {
            putValue(SMALL_ICON, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
