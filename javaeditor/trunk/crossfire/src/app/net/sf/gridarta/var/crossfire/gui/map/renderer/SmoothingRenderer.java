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

package net.sf.gridarta.var.crossfire.gui.map.renderer;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.data.NamedObject;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.smoothface.SmoothFaces;
import net.sf.gridarta.var.crossfire.IGUIConstants;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer for smoothed faces as used by Crossfire.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class SmoothingRenderer {

    @NotNull
    private static final int[] dx = { 1, 2, 2, 2, 1, 0, 0, 0, };

    @NotNull
    private static final int[] dy = { 0, 0, 1, 2, 2, 2, 1, 0, };

    @NotNull
    private static final int[] bWeights = { 2, 0, 4, 0, 8, 0, 1, 0, };

    @NotNull
    private static final int[] cWeights = { 0, 2, 0, 4, 0, 8, 0, 1, };

    @NotNull
    private static final int[] bcExclude = { 1 + 2, /*north exclude northwest (bit0) and northeast(bit1)*/
        0, 2 + 4, /*east exclude northeast and southeast*/
        0, 4 + 8, /*and so on*/
        0, 8 + 1, 0 };

    /**
     * The {@link MapModel} to render.
     */
    @NotNull
    private final MapModel<GameObject, MapArchObject, Archetype> mapModel;

    /**
     * The {@link SmoothFaces} to use.
     */
    @NotNull
    private final SmoothFaces smoothFaces;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    @NotNull
    private final net.sf.gridarta.model.gameobject.GameObject<?, ?, ?>[][] layerNode = new net.sf.gridarta.model.gameobject.GameObject<?, ?, ?>[3][3];

    @NotNull
    private final int[] sLevels = new int[8];

    @NotNull
    private final FaceObject[] sFaces = new FaceObject[8];

    /**
     * Creates a new instance.
     * @param mapModel the map model to render
     * @param smoothFaces the smooth faces to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    protected SmoothingRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final SmoothFaces smoothFaces, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.mapModel = mapModel;
        this.smoothFaces = smoothFaces;
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * Draw the smoothing information at given position of map, for a given
     * limit smoothlevel, at a given layer. This operation may be recursive, if
     * all layer above current are to be drawn too.
     * @param graphics where to draw (graphics)
     * @param pos the coordinate of the map square to draw, in map coordinates
     * @param level the limit smoothlevel (smooth levels above this are drawn)
     * @param firstLayer the layer (map z coordinate) to draw; note that
     * invisible objects are supposed to not use a layer, to stay coherent with
     * client behavior
     * @param allLayers whether or not to draw also layers above current one
     * @param borderOffsetX the border x offset
     * @param borderOffsetY the border y offset
     */
    public void paintSmooth(@NotNull final Graphics graphics, @NotNull final Point pos, final int level, final int firstLayer, final boolean allLayers, final int borderOffsetX, final int borderOffsetY) {
        int layer = firstLayer;
        while (true) {
            final MapArchObject mapArchObject = mapModel.getMapArchObject();
            boolean foundLayer = false;
            final Point where = new Point();
            for (int deltaX = -1; deltaX <= 1; deltaX++) {
                where.x = pos.x + deltaX;
                for (int deltaY = -1; deltaY <= 1; deltaY++) {
                    where.y = pos.y + deltaY;
                    //false warning: cannot annotate with @Nullable
                    //noinspection AssignmentToNull
                    layerNode[deltaX + 1][deltaY + 1] = null;
                    if (mapArchObject.isPointValid(where)) {
                        int currentLayer = -1;
                        for (final net.sf.gridarta.model.gameobject.GameObject<GameObject, MapArchObject, Archetype> node : mapModel.getMapSquare(where)) {
                            if (node.getAttributeInt(GameObject.INVISIBLE, true) == 0) {
                                currentLayer++;
                                if (currentLayer == layer) {
                                    foundLayer = true;
                                    if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
                                        layerNode[deltaX + 1][deltaY + 1] = node;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            /*surrounding nodes having smoothlevel >0 found*/
            /*below is ripped and adapted from sdl client smooth renderer*/
            for (int i = 0; i < 8; i++) {
                final net.sf.gridarta.model.gameobject.GameObject<?, ?, ?> node = layerNode[dx[i]][dy[i]];
                if (node == null) {
                    sLevels[i] = 0;
                    //false warning: cannot annotate with @Nullable
                    //noinspection AssignmentToNull
                    sFaces[i] = null; /*black picture*/
                } else {
                    final int smoothlevel = node.getAttributeInt(GameObject.SMOOTHLEVEL, true);
                    if (smoothlevel <= level) {
                        sLevels[i] = 0;
                        //false warning: cannot annotate with @Nullable
                        //noinspection AssignmentToNull
                        sFaces[i] = null; /*black picture*/
                    } else {
                        sLevels[i] = smoothlevel;
                        sFaces[i] = smoothFaces.getSmoothFace(node);
                    }
                }
            }

            /* ok, now we have a list of smoothlevel higher than current square.
            * there are at most 8 different levels. so... let's check 8 times
            * for the lowest one (we draw from bottom to top!).
            */
            final boolean[] partDone = { false, false, false, false, false, false, false, false, };
            while (true) {
                int lowest = -1;
                for (int i = 0; i < 8; i++) {
                    if (sLevels[i] > 0 && !partDone[i] && (lowest < 0 || sLevels[i] < sLevels[lowest])) {
                        lowest = i;
                    }
                }
                if (lowest < 0) {
                    /*no more smooth to do on this square*/
                    /*here we know 'what' to smooth*/
                    break;
                }
                final NamedObject smoothFace = sFaces[lowest];
                /* we need to calculate the weight for border and weight for corners.
                * then we 'mark done' the corresponding squares
                */
                /*first, the border, which may exclude some corners*/
                int weight = 0;
                int weightC = 15;
                for (int i = 0; i < 8; i++) { /*check all nearby squares*/
                    if (sLevels[i] == sLevels[lowest] && sFaces[i] == smoothFace) {
                        partDone[i] = true;
                        weight += bWeights[i];
                        weightC &= ~bcExclude[i];
                    } else {
                        /*must remove the weight of a corner if not in smoothing*/
                        weightC &= ~cWeights[i];
                    }
                }
                if (smoothFace == null) {
                    continue;  /*Can't smooth black*/
                }
                /* now, it's quite easy. We must draw using a 32x32 part of
                * the picture smooth face.
                * This part is located using the 2 weights calculated:
                * (32*weight,0) and (32*weightC,32)
                */
                final ImageIcon img = faceObjectProviders.getDisplayIcon(smoothFace);
                if (weight > 0) {
                    drawImage(graphics, pos, borderOffsetX, borderOffsetY, IGUIConstants.SQUARE_WIDTH * weight, 0, img);
                }
                if (weightC > 0) {
                    drawImage(graphics, pos, borderOffsetX, borderOffsetY, IGUIConstants.SQUARE_WIDTH * weightC, IGUIConstants.SQUARE_HEIGHT, img);
                }
            } /*while there's some smooth to do*/

            if (!allLayers || !foundLayer) {
                break;
            }

            layer++;
        }
    }

    private void drawImage(@NotNull final Graphics graphics, @NotNull final Point pos, final int borderOffsetX, final int borderOffsetY, final int srcX, final int srcY, @NotNull final ImageIcon img) {
        graphics.drawImage(img.getImage(), borderOffsetX + pos.x * IGUIConstants.SQUARE_WIDTH, borderOffsetY + pos.y * IGUIConstants.SQUARE_HEIGHT, borderOffsetX + pos.x * IGUIConstants.SQUARE_WIDTH + IGUIConstants.SQUARE_WIDTH, borderOffsetY + pos.y * IGUIConstants.SQUARE_HEIGHT + IGUIConstants.SQUARE_HEIGHT, srcX, srcY, srcX + IGUIConstants.SQUARE_WIDTH, srcY + IGUIConstants.SQUARE_HEIGHT, null);
    }

}
