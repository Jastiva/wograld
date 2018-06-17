/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.mapcontrol;

import java.io.File;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating {@link MapControl} instances.
 * @author Andreas Kirschbaum
 */
public interface MapControlFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Create a new map control instance.
     * @param objects the objects to insert into the new map
     * @param mapArchObject the map arch object to use for the new map
     * @param file the associated file
     * @return the new map control instance
     */
    @NotNull
    MapControl<G, A, R> newMapControl(@Nullable List<G> objects, @NotNull A mapArchObject, @Nullable File file);

    /**
     * Create a new pickmap map control instance.
     * @param objects the objects to insert into the new pickmap
     * @param mapArchObject the map arch object to use for the new pickmap
     * @param file the associated file
     * @return the new pickmap map control instance
     */
    @NotNull
    MapControl<G, A, R> newPickmapControl(@Nullable List<G> objects, @NotNull A mapArchObject, @Nullable File file);

}
