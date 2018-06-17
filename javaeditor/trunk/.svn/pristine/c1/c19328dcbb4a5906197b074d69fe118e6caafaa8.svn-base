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
import java.awt.Point;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A map view consists of a map grid and a map cursor, and is attached to a map
 * control.
 * @author Andreas Kirschbaum
 */
public interface MapView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * This function must be called when the view is closed.
     */
    void closeNotify();

    /**
     * Return one selected game object. If more than one game object is
     * selected, return a random one. If nothing is selected, return a random
     * game object from the entire map.
     * @return one random selected game object, or <code>null</code> if no game
     *         object is selected
     */
    @Nullable
    G getSelectedGameObject();

    /**
     * Return all selected game objects. If nothing is selected, return all game
     * objects from the entire map.
     * @return the selected game objects
     */
    @NotNull
    List<G> getSelectedGameObjects();

    /**
     * Returns the title for the map window.
     * @return the title
     */
    @NotNull
    String getWindowTitle();

    /**
     * Returns the component associated with this MapView that can be used as
     * parent for dialogs.
     * @return the component as parent for dialogs
     */
    @NotNull
    Component getComponent();

    /**
     * Return the controller of this view.
     * @return the controller of this view
     */
    @NotNull
    MapControl<G, A, R> getMapControl();

    /**
     * Activate this map view.
     */
    void activate();

    /**
     * Returns the {@link JInternalFrame} instance for this map view.
     * @return the internal frame
     */
    @NotNull
    JInternalFrame getInternalFrame();

    /**
     * Returns the selected squares.
     * @return the selected squares; the list may be modified by the caller
     */
    @NotNull
    List<MapSquare<G, A, R>> getSelectedSquares();

    /**
     * Returns the {@link MapGrid} of this view.
     * @return the map grid of this view
     */
    @NotNull
    MapGrid getMapGrid();

    /**
     * Returns the {@link MapCursor} of this view.
     * @return the map cursor of this view
     */
    @NotNull
    MapCursor<G, A, R> getMapCursor();

    /**
     * Returns the {@link MapRenderer} for this view.
     * @return the map renderer
     */
    @NotNull
    MapRenderer getRenderer();

    /**
     * Returns the {@link JScrollPane} of this map view.
     * @return the scroll pane
     */
    @NotNull
    JScrollPane getScrollPane();

    /**
     * Sets the cursor location. If the map location is not within map bounds
     * the cursor is set to the nearest valid location.
     * @param point the new location or <code>null</code> to remove the cursor;
     * will be modified to the actually set cursor location
     */
    void setCursorLocation(@Nullable Point point);

}
