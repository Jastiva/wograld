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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common interface for renderers of map control instances.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface MapRenderer {

    /**
     * Returns an image of the entire map view.
     * @return an image of the entire map view
     */
    @NotNull
    BufferedImage getFullImage();

    /**
     * Returns the map location at the given point or null if no map location is
     * at the point.
     * @param point the coordinates in the renderer view
     * @param retPoint if <code>(retPoint != null)</code> this point will be
     * returned otherwise a new point will be created
     * @return the map location
     */
    @Nullable
    Point getSquareLocationAt(@NotNull Point point, @Nullable Point retPoint);

    /**
     * Repaint the view because some view parameters may have changed.
     */
    void forceRepaint();

    /**
     * Saves an image of the entire view to a file.
     * @param file file to write image file to
     * @throws IOException in case of I/O problems
     */
    void printFullImage(@NotNull File file) throws IOException;

    /**
     * Adds a {@link MouseMotionListener} to be notified about mouse events.
     * @param mouseMotionListener the listener to add
     */
    void addMouseMotionListener(@NotNull final MouseMotionListener mouseMotionListener);

    /**
     * Removes a {@link MouseMotionListener} to be notified about mouse events.
     * @param mouseMotionListener the listener to remove
     */
    void removeMouseMotionListener(@NotNull final MouseMotionListener mouseMotionListener);

    void addMouseListener(@NotNull MouseListener l);

    void removeMouseListener(@NotNull MouseListener l);

    /**
     * Returns coordinates, length and width of map square.
     * @param p the map coordinates
     * @return the boundary rectangle of square
     */
    @NotNull
    Rectangle getSquareBounds(@NotNull Point p);

    /**
     * Ensures that a rectangular area is visible.
     * @param aRect the area
     */
    void scrollRectToVisible(@NotNull Rectangle aRect);

    /**
     * If set, inverts the setting of {@link net.sf.gridarta.model.mapviewsettings.MapViewSettings#isLightVisible()}.
     * @param lightVisible whether lighted map squares are inverted
     */
    void setLightVisible(final boolean lightVisible);

}
