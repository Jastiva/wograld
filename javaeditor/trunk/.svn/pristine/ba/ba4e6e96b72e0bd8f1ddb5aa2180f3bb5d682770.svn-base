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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;

/**
 * Paints overlays for map grids. Instances do not hold internal state; they may
 * be accessed concurrently.
 * @author Andreas Kirschbaum
 */
public class GridMapSquarePainter {

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_SELECTION}.
     */
    private final Image selImg;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_SELECTION_NORTH}.
     */
    private final Image selImgNorth;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_SELECTION_EAST}.
     */
    private final Image selImgEast;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_SELECTION_SOUTH}.
     */
    private final Image selImgSouth;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_SELECTION_WEST}.
     */
    private final Image selImgWest;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_SELECTING}.
     */
    private final Image preSelImg;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_CURSOR}.
     */
    private final Image cursorImg;

    /**
     * The overlay {@link Image} for {@link MapGrid#GRID_FLAG_ERROR}.
     */
    private final Image warningSquareImg;

    /**
     * The overlay {@link Image} for map squares that are affected by light
     * emitting game objects.
     */
    private final Image lightSquare;

    /**
     * Creates a new instance.
     * @param systemIcons the system icons for creating icons
     */
    public GridMapSquarePainter(@NotNull final SystemIcons systemIcons) {
        selImg = systemIcons.getMapSelectedIcon().getImage();
        selImgNorth = systemIcons.getMapSelectedIconNorth().getImage();
        selImgEast = systemIcons.getMapSelectedIconEast().getImage();
        selImgSouth = systemIcons.getMapSelectedIconSouth().getImage();
        selImgWest = systemIcons.getMapSelectedIconWest().getImage();
        preSelImg = systemIcons.getMapPreSelectedIcon().getImage();
        cursorImg = systemIcons.getMapCursorIcon().getImage();
        warningSquareImg = systemIcons.getWarningSquareIcon().getImage();
        lightSquare = systemIcons.getLightSquareIcon().getImage();
    }

    /**
     * Paints overlay images for one grid square.
     * @param graphics the graphics to paint into
     * @param gridFlags the grid flags to paint
     * @param light whether this map square is affected by a light emitting game
     * object
     * @param x the x-coordinate to paint at
     * @param y the y-coordinate to paint at
     * @param imageObserver the image observer to notify
     */
    public void paint(@NotNull final Graphics graphics, final int gridFlags, final boolean light, final int x, final int y, @NotNull final ImageObserver imageObserver) {
        if ((gridFlags & MapGrid.GRID_FLAG_SELECTION) != 0) {
            graphics.drawImage(selImg, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_SELECTION_NORTH) != 0) {
            graphics.drawImage(selImgNorth, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_SELECTION_EAST) != 0) {
            graphics.drawImage(selImgEast, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_SELECTION_SOUTH) != 0) {
            graphics.drawImage(selImgSouth, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_SELECTION_WEST) != 0) {
            graphics.drawImage(selImgWest, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_SELECTING) != 0) {
            graphics.drawImage(preSelImg, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_CURSOR) != 0) {
            graphics.drawImage(cursorImg, x, y, imageObserver);
        }
        if ((gridFlags & MapGrid.GRID_FLAG_ERROR) != 0) {
            graphics.drawImage(warningSquareImg, x, y, imageObserver);
        }
        if (light) {
            graphics.drawImage(lightSquare, x, y, imageObserver);
        }
    }

}
