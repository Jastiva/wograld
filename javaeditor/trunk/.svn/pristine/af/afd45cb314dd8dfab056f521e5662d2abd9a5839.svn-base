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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;
import javax.swing.Icon;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.DefaultIsoGameObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.MapGridEvent;
import net.sf.gridarta.model.mapgrid.MapGridListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.mapviewsettings.MapViewSettingsListener;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link MapRenderer} that renders isometric squares. It also visualizes
 * selections and validation errors.
 * @author unknown
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractIsoMapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapRenderer<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The background color for created images.
     */
    @NotNull
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 0);

    /**
     * The game object type number of spawn points.
     */
    private final int spawnPointTypeNo;

    /**
     * The offset to map borders (32 for std. rectangular maps, 0 for
     * pickmaps).
     */
    @NotNull
    private final Point borderOffset;

    /**
     * The origin is the point in the north-west corner.
     */
    @NotNull
    private final Point origin = new Point();

    /**
     * The {@link MapModel} to render.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The size of the map to render.
     */
    @NotNull
    private Size2D mapSize;

    /**
     * The {@link MapGrid} to render.
     */
    @NotNull
    private final MapGrid mapGrid;

    /**
     * The {@link Icon} drawn into squares without {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final Icon unknownSquareIcon;

    /**
     * Temporary point. Used to avoid creating millions of points.
     */
    @NotNull
    private final Point tmpPoint = new Point();

    /**
     * Temporary rectangle. Used to avoid creating millions of rectangles.
     */
    private final Rectangle tmpRec = new Rectangle();

    /**
     * The {@link MapViewSettings} instance to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The {@link MultiPositionData} instance to query for multi-part objects.
     */
    @NotNull
    private final MultiPositionData multiPositionData;

    /**
     * The {@link IsoMapSquareInfo} to use.
     */
    @NotNull
    private final IsoMapSquareInfo isoMapSquareInfo;

    /**
     * The {@link GridMapSquarePainter} to use.
     */
    @NotNull
    private final GridMapSquarePainter gridMapSquarePainter;

    /**
     * The {@link MapViewSettingsListener} attached to {@link
     * #mapViewSettings}.
     */
    @NotNull
    private final MapViewSettingsListener mapViewSettingsListener = new MapViewSettingsListener() {

        @Override
        public void gridVisibleChanged(final boolean gridVisible) {
            forceRepaint();
        }

        @Override
        public void lightVisibleChanged(final boolean lightVisible) {
            forceRepaint();
        }

        @Override
        public void smoothingChanged(final boolean smoothing) {
            // does not render smoothed faces
        }

        @Override
        public void doubleFacesChanged(final boolean doubleFaces) {
            forceRepaint();
        }

        @Override
        public void alphaTypeChanged(final int alphaType) {
            forceRepaint();
        }

        @Override
        public void editTypeChanged(final int editType) {
            // changed game objects will be rendered
        }

        @Override
        public void autojoinChanged(final boolean autojoin) {
            // ignore
        }

    };

    /**
     * The {@link MapModelListener} to track changes in {@link #mapModel}.
     */
    @NotNull
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore: will trigger an mapGridChanged() callback
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            repaint(); // TODO: only repaint a specific region
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            repaint(); // TODO: only repaint a specific region
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            // ignore
        }

    };

    /**
     * The {@link MapGridListener} to track changes in {@link #mapGrid}.
     */
    @NotNull
    private final MapGridListener mapGridListener = new MapGridListener() {

        @Override
        public void mapGridChanged(@NotNull final MapGridEvent e) {
            repaint();
        }

        @Override
        public void mapGridResized(@NotNull final MapGridEvent e) {
            mapSize = e.getSource().getSize();
            calculateOrigin();
            resizeFromModel();
            repaint();
        }

    };

    /**
     * Creates a new instance.
     * @param spawnPointTypeNo the game object type number for spawn points
     * @param mapViewSettings the map view settings instance to use
     * @param mapModel the map model to render
     * @param mapGrid the grid to render
     * @param borderOffsetX the horizontal border size
     * @param borderOffsetY the vertical border size
     * @param multiPositionData the multi position data to query for multi-part
     * objects
     * @param isoMapSquareInfo the iso square info to use
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param unknownSquareIcon the icons for unknown squares
     */
    protected AbstractIsoMapRenderer(final int spawnPointTypeNo, @NotNull final MapViewSettings mapViewSettings, @NotNull final MapModel<G, A, R> mapModel, @NotNull final MapGrid mapGrid, final int borderOffsetX, final int borderOffsetY, @NotNull final MultiPositionData multiPositionData, @NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<G, A, R> gameObjectParser, @NotNull final Icon unknownSquareIcon) {
        super(mapModel, gameObjectParser);
        this.spawnPointTypeNo = spawnPointTypeNo;
        this.mapViewSettings = mapViewSettings;
        this.multiPositionData = multiPositionData;
        this.isoMapSquareInfo = isoMapSquareInfo;
        this.gridMapSquarePainter = gridMapSquarePainter;
        borderOffset = new Point(borderOffsetX, borderOffsetY);
        this.mapModel = mapModel;
        mapSize = this.mapModel.getMapArchObject().getMapSize();
        this.mapGrid = mapGrid;
        this.unknownSquareIcon = unknownSquareIcon;

        mapViewSettings.addMapViewSettingsListener(mapViewSettingsListener);

        setToolTipText("dummy");
        setFocusable(true);
        calculateOrigin();
        resizeFromModel();
        mapModel.addMapModelListener(mapModelListener);
        mapGrid.addMapGridListener(mapGridListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
        mapModel.removeMapModelListener(mapModelListener);
        mapGrid.removeMapGridListener(mapGridListener);
        mapViewSettings.removeMapViewSettingsListener(mapViewSettingsListener);
    }

    /**
     * Calculates the origin which is located in the NORTH_WEST-corner of the
     * map. On non-pickmaps there is an extra border.
     */
    private void calculateOrigin() {
        origin.setLocation(borderOffset.x + mapSize.getHeight() * isoMapSquareInfo.getXLen2(), borderOffset.y);
    }

    /**
     * Sets the offset to map borders (32 for std. rectangular maps, 0 for
     * pickmaps).
     * @param x x offset to map borders (32 for std. rectangular maps, 0 for
     * pickmaps)
     * @param y y offset to map borders (32 for std. rectangular maps, 0 for
     * pickmaps)
     */
    private void setBorderOffset(final int x, final int y) {
        borderOffset.setLocation(x, y);
        calculateOrigin();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public BufferedImage getFullImage() {
        // set map dimensions for iso view
        final int mapWidth = mapSize.getWidth();
        final int mapHeight = mapSize.getHeight();
        final int sum = mapWidth + mapHeight;
        final int viewWidth = sum * isoMapSquareInfo.getXLen2();
        final int viewHeight = sum * isoMapSquareInfo.getYLen2();

        // first create a storing place for the image
        final BufferedImage image = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, viewWidth, viewHeight);

        // paint the map view into the image
        final Point storeOffset = new Point(borderOffset);
        setBorderOffset(0, 0);

        clearBackground(graphics);
        final Point pos = new Point();
        for (pos.y = 0; pos.y < mapHeight; pos.y++) {
            int xStart = origin.x - (pos.y + 1) * isoMapSquareInfo.getXLen2();
            int yStart = origin.y + pos.y * isoMapSquareInfo.getYLen2();
            for (pos.x = 0; pos.x < mapWidth; pos.x++) {
                paintSquare(graphics, xStart, yStart, mapModel.getMapSquare(pos));
                xStart += isoMapSquareInfo.getXLen2();
                yStart += isoMapSquareInfo.getYLen2();
            }
        }

        paintMapGrid(graphics);
        paintMapSelection(graphics);

        setBorderOffset(storeOffset.x, storeOffset.y);
        return image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(@NotNull final Graphics g) {
        paintComponent2((Graphics2D) g);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceRepaint() {
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Rectangle getSquareBounds(@NotNull final Point p) {
        tmpRec.x = origin.x - (p.y + 1) * isoMapSquareInfo.getXLen2() + p.x * isoMapSquareInfo.getXLen2();
        tmpRec.y = origin.y + p.y * isoMapSquareInfo.getYLen2() + p.x * isoMapSquareInfo.getYLen2();
        tmpRec.width = isoMapSquareInfo.getXLen();
        tmpRec.height = isoMapSquareInfo.getYLen();
        return tmpRec;
    }

    /**
     * Clears the window to background color if necessary.
     * @param g the graphics to use for clearing
     */
    protected abstract void clearBackground(@NotNull final Graphics g);

    /**
     * Paints one square.
     * @param g the graphics context to paint to
     * @param x the square coordinate to paint to
     * @param y the square coordinate to paint to
     * @param square the square to paint
     */
    protected abstract void paintSquare(@NotNull final Graphics2D g, final int x, final int y, @NotNull final MapSquare<G, A, R> square);

    /**
     * Paints this component.
     * @param g the graphics context to paint to
     */
    private void paintComponent2(@NotNull final Graphics2D g) {
        clearBackground(g);
        final Rectangle rec = getRepaintRec(getVisibleRect());
        final Point pos = new Point();
        final Point endPos = rec.getLocation();
        endPos.translate(rec.width, rec.height);
        for (pos.y = rec.y; pos.y < endPos.y; pos.y++) {
            int xStart = origin.x - (pos.y - rec.x + 1) * isoMapSquareInfo.getXLen2();
            int yStart = origin.y + (pos.y + rec.x) * isoMapSquareInfo.getYLen2();
            for (pos.x = rec.x; pos.x < endPos.x; pos.x++) {
                if (g.hitClip(xStart, yStart - isoMapSquareInfo.getYLen() * 4, isoMapSquareInfo.getXLen(), isoMapSquareInfo.getYLen() * 5)) {
                    paintSquare(g, xStart, yStart, mapModel.getMapSquare(pos));
                }
                xStart += isoMapSquareInfo.getXLen2();
                yStart += isoMapSquareInfo.getYLen2();
            }
        }

        paintMapGrid(g);
        paintMapSelection(g);
    }

    /**
     * Returns smallest {@link Rectangle} on the map that needs to be repaint.
     * @param visibleRectangle the visible rectangle
     * @return the rectangle to repaint
     */
    private Rectangle getRepaintRec(@NotNull final Rectangle visibleRectangle) {
        // This Rectangle will be returned
        final Rectangle rec = new Rectangle();
        // Upper left corner of viewport
        final Point posUL = visibleRectangle.getLocation();
        // Dimension of viewport
        final Dimension visDim = visibleRectangle.getSize();
        // Other positions of viewport corners
        final Point posUR = new Point(posUL.x + visDim.width, posUL.y);
        final Point posDL = new Point(posUL.x, posUL.y + visDim.height);
        final Point posDR = new Point(posUR.x, posDL.y);
        // Calculate map positions of corners and from them properties of Rectangle
        final Point mapPosUL = getSquareLocationAt(posUL, tmpPoint);
        rec.x = mapPosUL == null ? 0 : mapPosUL.x;
        final Point mapPosUR = getSquareLocationAt(posUR, tmpPoint);
        rec.y = mapPosUR == null ? 0 : mapPosUR.y;
        final Point mapPosDL = getSquareLocationAt(posDL, tmpPoint);
        rec.height = mapPosDL == null ? mapSize.getHeight() - rec.y : mapPosDL.y - rec.y + 1;
        final Point mapPosDR = getSquareLocationAt(posDR, tmpPoint);
        rec.width = mapPosDR == null ? mapSize.getWidth() - rec.x : mapPosDR.x - rec.x + 1;
        return rec;
    }

    /**
     * Checks whether a {@link GameObject} is visible according to current
     * editor settings.
     * @param gameObject the game object
     * @return whether the game object is visible
     */
    protected abstract boolean isGameObjectVisible(@NotNull final G gameObject);

    /**
     * Paints a single {@link GameObject} if it is visible according to current
     * editor settings.
     * @param g the graphics to paint to
     * @param xStart the x offset for painting
     * @param yStart the y offset for painting
     * @param gameObject the game object to paint
     */
    protected void paintGameObjectIfVisible(@NotNull final Graphics2D g, final int xStart, final int yStart, @NotNull final G gameObject) {
        final G head = gameObject.getHead();
        if (isGameObjectVisible(head)) {
            paintGameObject(g, xStart, yStart, gameObject, false);
        }
    }

    /**
     * Paints a single {@link GameObject}.
     * @param g the graphics to paint to
     * @param xStart the x offset for painting
     * @param yStart the y offset for painting
     * @param gameObject the game object to paint
     * @param inSpawnPoint whether <code>gameObject</code> is within a spawn
     * point
     */
    private void paintGameObject(@NotNull final Graphics2D g, final int xStart, final int yStart, @NotNull final G gameObject, final boolean inSpawnPoint) {
        final G head = gameObject.getHead();
        final Icon icon = "trans.101".equals(head.getFaceObjName()) ? unknownSquareIcon : head.getImage(mapViewSettings);
        final int xOffset = head.getAttributeInt(DefaultIsoGameObject.ALIGN);
        final int zoom = head.getAttributeInt(DefaultIsoGameObject.ZOOM);
        final double rotate = getRotate(head);
        final int alpha = head.getAttributeInt(DefaultIsoGameObject.ALPHA);
        final int tmpIconWidth;
        final int tmpIconHeight;
        if (zoom == 0 || zoom == 100) {
            tmpIconWidth = icon.getIconWidth();
            tmpIconHeight = icon.getIconHeight();
        } else {
            tmpIconWidth = (icon.getIconWidth() * zoom + 50) / 100;
            tmpIconHeight = (icon.getIconHeight() * zoom + 50) / 100;
        }
        final int iconWidth;
        final int iconHeight;
        if (rotate < 0.001) {
            iconWidth = tmpIconWidth;
            iconHeight = tmpIconHeight;
        } else {
            iconWidth = (int) (Math.abs(Math.cos(rotate) * tmpIconWidth) + 0.5) + (int) (Math.abs(Math.sin(rotate) * tmpIconHeight) + 0.5);
            iconHeight = (int) (Math.abs(Math.sin(rotate) * tmpIconWidth) + 0.5) + (int) (Math.abs(Math.cos(rotate) * tmpIconHeight) + 0.5);
        }
        if (head.getMultiRefCount() > 0) {
            final R archetype = gameObject.getArchetype();
            if (inSpawnPoint || archetype.isLowestPart()) {
                final int headMultiShapeID = head.getArchetype().getMultiShapeID();
                final int multiPartNr = archetype.getMultiPartNr();
                final int x = xStart - multiPositionData.getXOffset(headMultiShapeID, multiPartNr) + multiPositionData.getWidth(headMultiShapeID) / 2 - iconWidth / 2;
                final int y = yStart - multiPositionData.getYOffset(headMultiShapeID, multiPartNr) + isoMapSquareInfo.getYLen() - iconHeight;
                paintScaledIcon(g, icon, x + xOffset, y, zoom, alpha, rotate, tmpIconWidth, tmpIconHeight, iconWidth, iconHeight);
            }
        } else {
            final int x;
            if (iconWidth > isoMapSquareInfo.getXLen()) {
                x = xStart + isoMapSquareInfo.getXLen2() - iconWidth / 2;
            } else {
                x = xStart;
            }
            final int y = yStart + isoMapSquareInfo.getYLen() - iconHeight;
            paintScaledIcon(g, icon, x + xOffset, y, zoom, alpha, rotate, tmpIconWidth, tmpIconHeight, iconWidth, iconHeight);
        }

        // Paint first object (most likely a mob) in spawn points.
        if (!inSpawnPoint && isSpawnPoint(head)) {
            final G mob = head.getFirst();
            if (mob != null) {
                final int yOffset = mob.getAttributeInt(DefaultIsoGameObject.Z);
                paintGameObject(g, xStart, yStart - yOffset, mob, true);
            }
        }
    }

    /**
     * Returns the rotation angle of a game object.
     * @param head the head of the game object
     * @return the rotation angle in radians
     */
    private static double getRotate(@NotNull final BaseObject<?, ?, ?, ?> head) {
        final int rotate = -head.getAttributeInt(DefaultIsoGameObject.ROTATE) % 360;
        return (rotate < 0 ? rotate + 360 : rotate) * 2 * Math.PI / 360;
    }

    /**
     * Paints an icon at a given zoom factor and alpha value.
     * @param g the graphics to paint into
     * @param icon the icon to paint
     * @param x the x coordinate to paint at
     * @param y the y coordinate to paint at
     * @param zoom the zoom factor in percent
     * @param alpha the alpha value (0..255)
     * @param rotate the rotation angle in radians
     * @param oldIconWidth the width of the icon before rotation in pixel
     * @param oldIconHeight the height of the icon before rotation in pixel
     * @param newIconWidth the width of the icon after rotation in pixel
     * @param newIconHeight the height of the icon after rotation in pixel
     */
    private void paintScaledIcon(@NotNull final Graphics2D g, @NotNull final Icon icon, final int x, final int y, final int zoom, final int alpha, final double rotate, final int oldIconWidth, final int oldIconHeight, final int newIconWidth, final int newIconHeight) {
        if (zoom <= 0 || zoom == 100) {
            paintAlphaIcon(g, icon, x, y, alpha, rotate, oldIconWidth, oldIconHeight, newIconWidth, newIconHeight);
        } else {
            final AffineTransform savedTransform = g.getTransform();
            try {
                g.translate(x, y);
                g.scale(zoom / 100.0, zoom / 100.0);
                paintAlphaIcon(g, icon, 0, 0, alpha, rotate, oldIconWidth, oldIconHeight, newIconWidth, newIconHeight);
            } finally {
                g.setTransform(savedTransform);
            }
        }
    }

    /**
     * Paints an icon at a given alpha value.
     * @param g the graphics to paint into
     * @param icon the icon to paint
     * @param x the x coordinate to paint at
     * @param y the y coordinate to paint at
     * @param alpha the alpha value (0..255)
     * @param rotate the rotation angle in radians
     * @param oldIconWidth the width of the icon before rotation in pixel
     * @param oldIconHeight the height of the icon before rotation in pixel
     * @param newIconWidth the width of the icon after rotation in pixel
     * @param newIconHeight the height of the icon after rotation in pixel
     */
    private void paintAlphaIcon(@NotNull final Graphics2D g, @NotNull final Icon icon, final int x, final int y, final int alpha, final double rotate, final int oldIconWidth, final int oldIconHeight, final int newIconWidth, final int newIconHeight) {
        if (alpha <= 0 || alpha >= 255) {
            paintRotatedIcon(g, icon, x, y, rotate, oldIconWidth, oldIconHeight, newIconWidth, newIconHeight);
        } else {
            final Composite savedComposite = g.getComposite();
            try {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255.0F));
                paintRotatedIcon(g, icon, x, y, rotate, oldIconWidth, oldIconHeight, newIconWidth, newIconHeight);
            } finally {
                g.setComposite(savedComposite);
            }
        }
    }

    /**
     * Paints an icon at a given rotation angle.
     * @param g the graphics to paint into
     * @param icon the icon to paint
     * @param x the x coordinate to paint at
     * @param y the y coordinate to paint at
     * @param rotate the rotation angle in radians
     * @param oldIconWidth the width of the icon before rotation in pixel
     * @param oldIconHeight the height of the icon before rotation in pixel
     * @param newIconWidth the width of the icon after rotation in pixel
     * @param newIconHeight the height of the icon after rotation in pixel
     */
    private void paintRotatedIcon(@NotNull final Graphics2D g, @NotNull final Icon icon, final int x, final int y, final double rotate, final int oldIconWidth, final int oldIconHeight, final int newIconWidth, final int newIconHeight) {
        if (rotate < 0.001) {
            g.translate(x, y);
            try {
                paintIcon(g, icon);
            } finally {
                g.translate(-x, -y);
            }
        } else {
            final AffineTransform savedTransform = g.getTransform();
            try {
                g.translate(x + newIconWidth / 2, y + newIconHeight / 2);
                g.rotate(rotate);
                g.translate(-oldIconWidth / 2, -oldIconHeight / 2);
                paintIcon(g, icon);
            } finally {
                g.setTransform(savedTransform);
            }
        }
    }

    /**
     * Paints an icon.
     * @param g the graphics to paint into
     * @param icon the icon to paint
     */
    protected void paintIcon(@NotNull final Graphics2D g, @NotNull final Icon icon) {
        icon.paintIcon(this, g, 0, 0);
    }

    /**
     * Returns whether the given {@link BaseObject} is a spawn point.
     * @param gameObject the game object
     * @return whether it is a spawn point
     */
    private boolean isSpawnPoint(@NotNull final BaseObject<G, A, R, ?> gameObject) {
        return gameObject.getTypeNo() == spawnPointTypeNo;
    }

    /**
     * Paints the selection for the whole map. It's recommended to paint the
     * complete selection after the map itself, otherwise map elements actually
     * might hide selections.
     * @param g the graphics for painting
     */
    private void paintMapSelection(@NotNull final Graphics g) {
        final boolean lightVisible = isLightVisible() ^ mapViewSettings.isLightVisible();
        final Point point = new Point();
        for (int y = 0; y < mapSize.getHeight(); y++) {
            int xStart = origin.x - (y + 1) * isoMapSquareInfo.getXLen2();
            int yStart = origin.y + y * isoMapSquareInfo.getYLen2();
            point.y = y;
            for (int x = 0; x < mapSize.getWidth(); x++) {
                if (g.hitClip(xStart, yStart, isoMapSquareInfo.getXLen(), isoMapSquareInfo.getYLen())) {
                    final int gridFlags = mapGrid.getFlags(x, y);
                    point.x = x;
                    final boolean light = lightVisible && mapModel.getMapSquare(point).isLight();
                    gridMapSquarePainter.paint(g, gridFlags, light, xStart, yStart, this);
                } else {
                    /* DO NOTHING if outside clip region.
                    * DO NOT use continue. xStart and yStart are recalculated at the end of the loop.
                    */
                }
                xStart += isoMapSquareInfo.getXLen2();
                yStart += isoMapSquareInfo.getYLen2();
            }
        }
    }

    /**
     * Paints the grid of the whole map. The grid is not painted if it is
     * disabled. It's recommended to paint the complete grid after the map
     * itself, otherwise map elements actually might hide parts of the grid.
     * @param g the graphics for painting
     */
    private void paintMapGrid(@NotNull final Graphics g) {
        if (mapViewSettings.isGridVisible()) {
            // draw iso grid
            g.setColor(Color.black);

            final int mapWidth = mapSize.getWidth();
            final int mapHeight = mapSize.getHeight();
            for (int x = 0; x <= mapWidth; x++) {
                g.drawLine(origin.x + x * isoMapSquareInfo.getXLen2() - 1, origin.y + x * isoMapSquareInfo.getYLen2() - 1, origin.x - (mapHeight - x) * isoMapSquareInfo.getXLen2() - 1, origin.y + (mapHeight + x) * isoMapSquareInfo.getYLen2() - 1);
            }
            for (int y = 0; y <= mapHeight; y++) {
                g.drawLine(origin.x - y * isoMapSquareInfo.getXLen2() - 1, origin.y + y * isoMapSquareInfo.getYLen2() - 1, origin.x + (mapWidth - y) * isoMapSquareInfo.getXLen2() - 1, origin.y + (mapWidth + y) * isoMapSquareInfo.getYLen2() - 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Point getSquareLocationAt(@NotNull final Point point, @Nullable final Point retPoint) {
        final int x0 = point.x - origin.x;
        final int y0 = point.y - origin.y;
        final int yt = (2 * y0 - x0) / 2;
        final int xt = yt + x0;
        final int xm = xt / isoMapSquareInfo.getXLen2();
        final int ym = yt / isoMapSquareInfo.getYLen2() / 2;
        if (xm < 0 || xm >= mapSize.getWidth() || ym < 0 || ym >= mapSize.getHeight()) {
            return null;
        }
        if (retPoint != null) {
            retPoint.setLocation(xm, ym);
            return retPoint;
        }

        return new Point(xm, ym);
    }

    /**
     * Refreshes the data in the view from the model.
     */
    private void resizeFromModel() {
        // define how much drawing space we need for the map
        final int sum = mapSize.getWidth() + mapSize.getHeight();
        final Dimension forcedSize = new Dimension(2 * borderOffset.x + sum * isoMapSquareInfo.getXLen2(), 2 * borderOffset.y + sum * isoMapSquareInfo.getYLen2());
        setPreferredSize(forcedSize);
        setMinimumSize(forcedSize);
        revalidate();
    }

}
