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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for ISO {@link MapRenderer MapRenderers}.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractSimpleIsoMapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapRenderer<G, A, R> {

    /**
     * The background {@link Color} for created images.
     */
    @NotNull
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 0);

    /**
     * The {@link MapModel} to render.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link IsoMapSquareInfo} to use.
     */
    @NotNull
    private final IsoMapSquareInfo isoMapSquareInfo;

    /**
     * The origin is the point in the north-west corner.
     */
    @NotNull
    private final Point origin = new Point();

    /**
     * Creates a new instance.
     * @param mapModel the rendered map model
     * @param gameObjectParser the game object parser for generating tooltip
     * information or <code>null</code>
     * @param isoMapSquareInfo the iso map square info to use
     */
    protected AbstractSimpleIsoMapRenderer(@NotNull final MapModel<G, A, R> mapModel, @Nullable final GameObjectParser<G, A, R> gameObjectParser, @NotNull final IsoMapSquareInfo isoMapSquareInfo) {
        super(mapModel, gameObjectParser);
        this.mapModel = mapModel;
        this.isoMapSquareInfo = isoMapSquareInfo;
    }

    /**
     * Paints this component.
     * @param graphics the graphics context to paint to
     */
    protected void paintComponent2(@NotNull final Graphics2D graphics) {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        final Rectangle rec = new Rectangle(0, 0, mapSize.getWidth(), mapSize.getHeight());
        final Point pos = new Point();
        final Point endPos = rec.getLocation();
        endPos.translate(rec.width, rec.height);
        for (pos.y = rec.y; pos.y < endPos.y; pos.y++) {
            int xStart = origin.x - (pos.y - rec.x + 1) * isoMapSquareInfo.getXLen2();
            int yStart = origin.y + (pos.y + rec.x) * isoMapSquareInfo.getYLen2();
            for (pos.x = rec.x; pos.x < endPos.x; pos.x++) {
                if (graphics.hitClip(xStart, yStart - isoMapSquareInfo.getYLen() * 4, isoMapSquareInfo.getXLen(), isoMapSquareInfo.getYLen() * 5)) {
                    final GameObjectContainer<G, A, R> square = mapModel.getMapSquare(pos);
                    if (square.isEmpty()) {
                        // empty square: Draw empty square icon if not a pickmap
                        // graphics.drawImage(emptySquareIcon.getImage(), xStart, yStart, emptySquareIcon.getImageObserver());
                    } else {
                        // normal square
                        for (final G node : square) {
                            paint(graphics, xStart, yStart, node);
                        }
                    }
                }
                xStart += isoMapSquareInfo.getXLen2();
                yStart += isoMapSquareInfo.getYLen2();
            }
        }
    }

    /**
     * Paints a single GameObject.
     * @param graphics2D the graphics to paint to
     * @param xStart the x offset for painting
     * @param yStart the y offset for painting
     * @param gameObject the game object to paint
     */
    protected abstract void paint(@NotNull final Graphics2D graphics2D, final int xStart, final int yStart, @NotNull final G gameObject);

    /**
     * The origin which is located in the NORTH_WEST-corner of the map is
     * calculated. On non-pickmaps there is an extra border.
     */
    protected void calculateOrigin() {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        origin.setLocation(mapSize.getHeight() * isoMapSquareInfo.getXLen2(), 0);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Point getSquareLocationAt(@NotNull final Point point, @Nullable final Point retPoint) {
        throw new IllegalStateException();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public BufferedImage getFullImage() {
        // set map dimensions for iso view
        calculateOrigin();
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        final int sum = mapSize.getWidth() + mapSize.getHeight();
        final int viewWidth = sum * isoMapSquareInfo.getXLen2();
        final int viewHeight = sum * isoMapSquareInfo.getYLen2();

        // first create a storing place for the image
        final BufferedImage image = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, viewWidth, viewHeight);

        // paint the map view into the image
        paintComponent2(graphics);
        return image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceRepaint() {
        throw new IllegalStateException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(@NotNull final Graphics g) {
        paintComponent2((Graphics2D) g);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Rectangle getSquareBounds(@NotNull final Point p) {
        throw new IllegalStateException();
    }

}
