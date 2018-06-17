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

import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.Action;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.model.mapmanager.FileControl;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating {@link MapMenuAction} instances.
 * @author Andreas Kirschbaum
 */
public class ActionFactory {

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
     * The {@link MapImageCache} to query.
     */
    @NotNull
    private final MapImageCache<?, ?, ?> mapImageCache;

    /**
     * Maps {@link MapMenuEntryMap} instance to associated {@link
     * MapMenuAction}.
     */
    @NotNull
    private final Map<MapMenuEntryMap, MapMenuAction> actions = new IdentityHashMap<MapMenuEntryMap, MapMenuAction>();

    /**
     * Keys of {@link #actions} to delete when {@link #end()} is called.
     */
    @NotNull
    private final Map<MapMenuEntryMap, Void> keysToDelete = new IdentityHashMap<MapMenuEntryMap, Void>();

    /**
     * Creates a new instance.
     * @param mapViewsManager the map views manager for opening map files
     * @param fileControl the file control for reporting errors
     * @param mapImageCache the map image cache to query
     */
    public ActionFactory(@NotNull final MapViewsManager<?, ?, ?> mapViewsManager, @NotNull final FileControl<?, ?, ?> fileControl, @NotNull final MapImageCache<?, ?, ?> mapImageCache) {
        this.mapViewsManager = mapViewsManager;
        this.fileControl = fileControl;
        this.mapImageCache = mapImageCache;
    }

    /**
     * Begins a lookup sequence.
     */
    public void begin() {
        keysToDelete.clear();
        for (final MapMenuEntryMap mapMenuEntryMap : actions.keySet()) {
            keysToDelete.put(mapMenuEntryMap, null);
        }
    }

    /**
     * Ends a lookup sequence. All actions not looked up between the preceding
     * call to {@link #begin()} are deleted.
     */
    public void end() {
        for (final MapMenuEntryMap mapMenuEntryMap : keysToDelete.keySet()) {
            actions.remove(mapMenuEntryMap);
        }
        keysToDelete.clear();
    }

    /**
     * Returns an {@link Action} for a {@link MapMenuEntryMap} instance.
     * @param mapMenuEntryMap the instance
     * @return the action
     */
    @NotNull
    public Action getAction(@NotNull final MapMenuEntryMap mapMenuEntryMap) {
        keysToDelete.remove(mapMenuEntryMap);
        final MapMenuAction existingAction = actions.get(mapMenuEntryMap);
        final MapMenuAction action;
        if (existingAction == null) {
            action = new MapMenuAction(mapMenuEntryMap, mapViewsManager, fileControl);
            actions.put(mapMenuEntryMap, action);
        } else {
            action = existingAction;
        }
        action.update(mapImageCache);
        return action;
    }

}
