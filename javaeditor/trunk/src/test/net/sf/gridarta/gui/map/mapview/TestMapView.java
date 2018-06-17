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

import java.awt.Component;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapView} implementation for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestMapView extends AbstractMapView<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * The controller of this view.
     */
    @NotNull
    private final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl;

    /**
     * Creates a new instance.
     * @param mapControl the controller of this view
     * @param mapGrid the map grid for this map view
     * @param mapCursor the map cursor for this map view
     */
    public TestMapView(@NotNull final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl, @NotNull final MapGrid mapGrid, @NotNull final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> mapCursor) {
        super(mapControl.getMapModel(), mapGrid, mapCursor);
        this.mapControl = mapControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getWindowTitle() {
        return "title";
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Component getComponent() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<TestGameObject, TestMapArchObject, TestArchetype> getMapControl() {
        return mapControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JInternalFrame getInternalFrame() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapRenderer getRenderer() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JScrollPane getScrollPane() {
        throw new AssertionError();
    }

}
