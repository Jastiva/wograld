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

package net.sf.gridarta.gui.map.mapview;

import java.awt.Point;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link MapViewFactory} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestMapViewFactory implements MapViewFactory<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapView<TestGameObject, TestMapArchObject, TestArchetype> newMapView(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl, @Nullable final Point viewPosition, final int viewCounter) {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControl.getMapModel();
        final MapGrid mapGrid = new MapGrid(mapModel.getMapArchObject().getMapSize());
        return new TestMapView(mapControl, mapGrid, new MapCursor<TestGameObject, TestMapArchObject, TestArchetype>(mapGrid, mapModel));
    }

}
