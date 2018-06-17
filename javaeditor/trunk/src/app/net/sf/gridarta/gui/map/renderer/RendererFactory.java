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

package net.sf.gridarta.gui.map.renderer;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating {@link MapRenderer} instances.
 * @author Andreas Kirschbaum
 */
public interface RendererFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Creates a new map renderer instance which paints only squares but no
     * grid, cursor, selection, or errors.
     * @param mapModel the map model to render
     * @return the new map renderer
     */
    @NotNull
    MapRenderer newSimpleMapRenderer(@NotNull MapModel<G, A, R> mapModel);

    /**
     * Creates a new {@link MapRenderer} suitable for painting maps.
     * @param mapModel the map model to render
     * @param mapGrid the map grid to render
     * @return the new map renderer
     */
    @NotNull
    AbstractMapRenderer<G, A, R> newMapRenderer(@NotNull MapModel<G, A, R> mapModel, @NotNull MapGrid mapGrid);

    /**
     * Creates a new {@link MapRenderer} suitable for painting pickmaps.
     * @param mapModel the map model to render
     * @param mapGrid the map grid to render
     * @return the new map renderer
     */
    @NotNull
    AbstractMapRenderer<G, A, R> newPickmapRenderer(@NotNull MapModel<G, A, R> mapModel, @NotNull MapGrid mapGrid);

}
