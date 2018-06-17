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

package net.sf.gridarta.gui.map.maptilepane;

import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.tiles.MapLink;
import org.jetbrains.annotations.NotNull;

/**
 * A Panel for managing the tiling of maps. Implementation for 4 tile paths.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class FlatMapTilePane<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapTilePane<G, A, R> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The tile links used for the attach map algorithm.
     */
    private static final MapLink[][] tileLink = new MapLink[8][0];

    /**
     * Maps map direction to {@link net.sf.gridarta.gui.utils.DirectionLayout}
     * direction.
     */
    private static final Direction[] directionMapping = { Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH, Direction.SOUTH, };

    /**
     * Indices of next focus.
     */
    private static final int[] nextFocus = { 1, 2, 3, 4, 5, 0 };

    /**
     * Create a MapTilePane.
     * @param mapManager the map manager to use
     * @param globalSettings the global settings instance
     * @param mapModel the map that's tiles are to be viewed / controlled
     * @param mapFileFilter the Swing file filter to use
     * @param mapPathNormalizer the map path normalizer for converting map paths
     * to files
     */
    public FlatMapTilePane(final MapManager<G, A, R> mapManager, @NotNull final GlobalSettings globalSettings, final MapModel<G, A, R> mapModel, final FileFilter mapFileFilter, @NotNull final MapPathNormalizer mapPathNormalizer) {
        super(mapManager, globalSettings, mapModel, tileLink, directionMapping, nextFocus, mapFileFilter, mapPathNormalizer);
    }

}
