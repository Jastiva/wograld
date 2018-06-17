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

import java.io.File;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryMap;
import net.sf.gridarta.gui.mapmenu.MapMenuPreferences;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the recent menu. Creates new entries whenever a map is opened.
 * @author Andreas Kirschbaum
 */
public class RecentManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Creates a new instance.
     * @param mapManager the map manager to track for opened maps
     * @param mapMenuPreferences the map menu preferences to use
     */
    public RecentManager(@NotNull final MapManager<G, A, R> mapManager, @NotNull final MapMenuPreferences mapMenuPreferences) {
        final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

            @Override
            public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
                // ignore
            }

            @Override
            public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
                if (interactive) {
                    final MapModel<G, A, R> mapModel = mapControl.getMapModel();
                    final File mapFile = mapModel.getMapFile();
                    if (mapFile != null) {
                        final MapMenuEntryMap mapMenuEntry = new MapMenuEntryMap(mapFile, mapModel.getMapArchObject().getMapName());
                        mapMenuPreferences.addEntry(mapMenuEntry);
                    }
                }
            }

            @Override
            public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
                // ignore
            }

            @Override
            public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
                // ignore
            }

        };
        mapManager.addMapManagerListener(mapManagerListener);
    }

}
