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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.sf.gridarta.gui.map.renderer.AbstractMapRenderer;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.model.io.GameObjectParser;
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
import net.sf.gridarta.var.crossfire.IGUIConstants;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is the default renderer of a map. It visualizes selections and
 * validation errors.
 * @author unknown
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>.
 * @author Andreas Kirschbaum
 * @author serpentshard
 */
public abstract class AbstractFlatMapRenderer extends AbstractMapRenderer<GameObject, MapArchObject, Archetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The offset to map borders (32 for std. rectangular maps, 0 for
     * pickmaps).
     * @serial
     */
    @NotNull
    private final Point borderOffset = new Point();

    /**
     * The {@link MapModel} to render.
     * @serial
     */
    @NotNull
    private final MapModel<GameObject, MapArchObject, Archetype> mapModel;

    /**
     * The {@link GridMapSquarePainter} to use.
     */
    @NotNull
    private final GridMapSquarePainter gridMapSquarePainter;

    /**
     * The map size in squares.
     * @serial
     */
    @NotNull
    private Size2D mapSize;

    /**
     * The {@link MapGrid} to render.
     */
    @NotNull
    private final MapGrid mapGrid;

    /**
     * Temporary point. Used to avoid creating millions of points.
     * @serial
     */
    @NotNull
    private final Point tmpPoint = new Point();

    /**
     * The {@link MapViewSettings} instance to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * Temporary rectangle. Used to avoid creating millions of rectangles.
     * @serial
     */
    @NotNull
    private final Rectangle tmpRec = new Rectangle();

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
            forceRepaint();
        }

        @Override
        public void doubleFacesChanged(final boolean doubleFaces) {
            // does not render double faces
        }

        @Override
        public void alphaTypeChanged(final int alphaType) {
            // does not render alpha types
        }

        @Override
        public void editTypeChanged(final int editType) {
            // changed game objects will be rendered
        }

        @Override
        public void autojoinChanged(final boolean autojoin) {
            // does not affect rendering
        }

    };

    /**
     * The {@link MapModelListener} to track changes in {@link #mapModel}.
     */
    @NotNull
    private final MapModelListener<GameObject, MapArchObject, Archetype> mapModelListener = new MapModelListener<GameObject, MapArchObject, Archetype>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore: will trigger an mapGridChanged() callback
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<GameObject, MapArchObject, Archetype>> mapSquares) {
            final Collection<MapSquare<GameObject, MapArchObject, Archetype>> toRepaint = new HashSet<MapSquare<GameObject, MapArchObject, Archetype>>();
            for (final MapSquare<GameObject, MapArchObject, Archetype> mapSquare : mapSquares) {
                getSquaresToRepaint(mapSquare, toRepaint);
            }
            for (final MapSquare<GameObject, MapArchObject, Archetype> mapSquare : toRepaint) {
            //    paintSquare(mapSquare);
             //   paintSquare2(g, getBorderOffsetX() + point.x* 32, getBorderOffsetY() + point.y * 32, point,mapGrid.getSize().getWidth(),mapGrid.getSize().getHeight());
                paintSquare3(mapSquare);
            }
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<GameObject> gameObjects, @NotNull final Set<GameObject> transientGameObjects) {
            final Collection<MapSquare<GameObject, MapArchObject, Archetype>> toRepaint = new HashSet<MapSquare<GameObject, MapArchObject, Archetype>>();
            addMapSquares(gameObjects, toRepaint);
            addMapSquares(transientGameObjects, toRepaint);
            for (final MapSquare<GameObject, MapArchObject, Archetype> mapSquare : toRepaint) {
             //   paintSquare(mapSquare);
                paintSquare3(mapSquare);
            }
        }

        /**
         * Adds all {@link MapSquare MapSquares} a {@link GameObject} occupies
         * to a collection.
         * @param gameObjects the game objects to process
         * @param toRepaint the collection
         */
        private void addMapSquares(@NotNull final Iterable<GameObject> gameObjects, @NotNull final Collection<MapSquare<GameObject, MapArchObject, Archetype>> toRepaint) {
            for (final net.sf.gridarta.model.gameobject.GameObject<GameObject, MapArchObject, Archetype> gameObject : gameObjects) {
                if (!gameObject.isInContainer()) {
                    for (net.sf.gridarta.model.gameobject.GameObject<GameObject, MapArchObject, Archetype> gameObjectPart = gameObject; gameObjectPart != null; gameObjectPart = gameObjectPart.getMultiNext()) {
                        final MapSquare<GameObject, MapArchObject, Archetype> square = gameObjectPart.getMapSquare();
                        if (square != null) {
                            getSquaresToRepaint(square, toRepaint);
                        }
                    }
                }
            }
        }

        /**
         * Determines the map squares to repaint.
         * @param mapSquare the map square
         * @param toRepaint the map squares to repaint; new map squares may be
         * added
         */
        private void getSquaresToRepaint(@NotNull final MapSquare<GameObject, MapArchObject, Archetype> mapSquare, @NotNull final Collection<MapSquare<GameObject, MapArchObject, Archetype>> toRepaint) {
            if (mapViewSettings.isSmoothing()) {
                final MapArchObject mapArchObject = mapModel.getMapArchObject();
                final Point point = new Point();
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        mapSquare.getMapLocation(point, dx, dy);
                        if (mapArchObject.isPointValid(point)) {
                            toRepaint.add(mapModel.getMapSquare(point));
                        }
                    }
                }
            } else {
                toRepaint.add(mapSquare);
            }
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<GameObject, MapArchObject, Archetype> errors) {
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
            final Rectangle recChange = mapGrid.getRecChange();
            int x1 = recChange.x;
            int y1 = recChange.y;
            int w1 = recChange.width;
            int h1 = recChange.height;
            // if recchange has not been updated, this will not work well?
            // are these in pixels or tiles, and diagonal or straight?
            final Rectangle recSecond = new Rectangle();
            recSecond.x=x1;
            recSecond.y=y1;
            recSecond.width=w1*8;
            recSecond.height=h1*8;
          
           // updateSquares(recChange);
            updateSquares(recSecond);
          
            
        //    repaint(0L, borderOffset.x + recChange.x * 64, borderOffset.y + recChange.y * 64, recChange.width * 64, recChange.height * 64);
            
            // screen boundaries look out
         //   repaint(0L, borderOffset.x + (recChange.x + recChange.y - 9) * 32, borderOffset.y + (recChange.y - recChange.x + 8) * 32, (recChange.width + recChange.height * 64000/1414), (recChange.width + recChange.height * 64000/1414));
      //   repaint(0L, borderOffset.x + (recChange.x + recChange.y ) * 32, borderOffset.y + (recChange.y - recChange.x +mapSize.getWidth()) * 32, (recChange.width + recChange.height) * 64000/1414, (recChange.width + recChange.height) * 64000/1414);
     //   repaint(0L, borderOffset.x + (recSecond.x + recSecond.y ) * 32, borderOffset.y + (recSecond.y - 2 - recSecond.x +mapSize.getWidth()) * 32, (recSecond.width + recSecond.height) * 64000/1414, (recSecond.width + recSecond.height) * 64000/1414);
          // forceRepaint();
          
         //   repaint(0L, borderOffset.x + (recSecond.x + recSecond.y ) * 32, borderOffset.y + (recSecond.y - 2 - recSecond.x +mapSize.getWidth()) * 32, (recSecond.width + recSecond.height) * 4 * 64000/1414, (recSecond.width + recSecond.height) * 4* 64000/1414);
           // is done by updatesquares repaint();
        }

        @Override
        public void mapGridResized(@NotNull final MapGridEvent e) {
            resizeMapGrid();
        }

    };

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings instance to use
     * @param mapModel the map model to render
     * @param mapGrid the grid to render
     * @param borderSize the size of the map borders in pixel
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     */
    protected AbstractFlatMapRenderer(@NotNull final MapViewSettings mapViewSettings, @NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final MapGrid mapGrid, final int borderSize, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser) {
        super(mapModel, gameObjectParser);
        this.mapViewSettings = mapViewSettings;
        this.mapModel = mapModel;
        this.gridMapSquarePainter = gridMapSquarePainter;
        mapSize = this.mapModel.getMapArchObject().getMapSize();
        this.mapGrid = mapGrid;

        mapViewSettings.addMapViewSettingsListener(mapViewSettingsListener);

        setToolTipText("dummy");
        setFocusable(true);
        borderOffset.setLocation(borderSize, borderSize);
    }

    /**
     * Finishes initialization of this instance.
     */
    protected void init() {
        resizeMapGrid();
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
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public BufferedImage getFullImage() {
        final int viewWidth = 64 * mapSize.getWidth();
        final int viewHeight = 64 * mapSize.getHeight();

        // first create a storing place for the image
        final BufferedImage bufImage = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = bufImage.getGraphics();
        try {
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, viewWidth, viewHeight);

            // paint the map view into the image
            final Point storeOffset = new Point(borderOffset);
            try {
                borderOffset.setLocation(0, 0);
                paintComponent(graphics, true, false,0);
                // the last argument to paint component, is irrelevant when second arg is true
            } finally {
                borderOffset.setLocation(storeOffset);
            }
        } finally {
            graphics.dispose();
        }
        return bufImage;
    }

    /**
     * {@inheritDoc}
     * @noinspection AbstractMethodOverridesConcreteMethod
     */
    @Override
    public abstract void paintComponent(@NotNull final Graphics g);

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceRepaint() {
        updateAll();
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Rectangle getSquareBounds(@NotNull final Point p) {
     //   tmpRec.x = borderOffset.x + p.x * 64;
    //    tmpRec.x = borderOffset.x + (p.x + p.y - 9)* 32;
    //    tmpRec.y = borderOffset.y + (p.y - p.x + 8) * 32;
        tmpRec.x = borderOffset.x + (p.x + p.y)* 32;
        tmpRec.y = borderOffset.y + (p.y - p.x + mapSize.getWidth()) * 32;
      //  tmpRec.width = 64;
      //  tmpRec.height = 64;
        tmpRec.width = 64;
        tmpRec.height = 64;
        
        return tmpRec;
    }

    /**
     * Paints one square.
     * @param square the square to paint
     */
    private void paintSquare(@NotNull final MapSquare<GameObject, MapArchObject, Archetype> square) {
        final Point pos = square.getMapLocation();
        updateSquare(pos);
        // update square on flatmaprenderer does some of the coord change
    //    repaint(0L, borderOffset.x + pos.x * IGUIConstants.SQUARE_WIDTH, borderOffset.y + pos.y * IGUIConstants.SQUARE_HEIGHT, IGUIConstants.SQUARE_WIDTH, IGUIConstants.SQUARE_HEIGHT);
       // repaint(0L, borderOffset.x + (pos.x + pos.y - 9) * 32, borderOffset.y + (pos.y - pos.x + 8) * 32, 64, 64);
        repaint(0L, borderOffset.x + (pos.x + pos.y ) * 32, borderOffset.y + (pos.y - 3 - pos.x +mapSize.getWidth()) * 32, 128, 256);
  //       repaint(0L, borderOffset.x + (recSecond.x + recSecond.y ) * 32, borderOffset.y + (recSecond.y - 2 - recSecond.x +mapSize.getWidth()) * 32, (recSecond.width + recSecond.height) * 64000/1414, (recSecond.width + recSecond.height) * 64000/1414);
    }
    
    private void paintSquare3(@NotNull final MapSquare<GameObject, MapArchObject, Archetype> square) {
       final Point pos = square.getMapLocation();
       int x1 = pos.x;
            int y1 = pos.y;
            int w1 = 64;
            int h1 = 64;
            // are these in pixels or tiles, and diagonal or straight?
            final Rectangle recSecond = new Rectangle();
            recSecond.x=x1;
            recSecond.y=y1;
          //  recSecond.width=w1*6;
           // recSecond.height=h1*8;
          recSecond.width=w1*3;
            recSecond.height=h1*5;
           // updateSquares(recChange);
            updateSquares(recSecond);
       // updateSquare(pos);
          //   repaint(0L, borderOffset.x + (recSecond.x + recSecond.y - 2 ) * 32, borderOffset.y + (recSecond.y - 2 - recSecond.x +mapSize.getWidth()) * 32, (recSecond.width + recSecond.height) * 64000/1414, (recSecond.width + recSecond.height) * 64000/1414);
        //    repaint(0L, borderOffset.x + (recSecond.x + recSecond.y - 2 ) * 32, borderOffset.y + (recSecond.y - 2 - recSecond.x +mapSize.getWidth()) * 32, (recSecond.width + recSecond.height) * 4* 64000/1414, (recSecond.width + recSecond.height) * 4 * 64000/1414);
            repaint();
           // forceRepaint();
    }

    /**
     * Paints this component.
     * @param graphics the graphics context to paint to
     * @param isSnapshot true when this drawing is for a "screenshot"-image,
     * false for normal drawing
     * @param checkClip if set, omit squares outside <code>graphics</code>'s
     * clip area
     */
    protected void paintComponent(@NotNull final Graphics graphics, final boolean isSnapshot, final boolean checkClip, int ypixeloffset) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        paintMap(graphics, checkClip);
        if (!isSnapshot) {
            paintMapGrid(graphics,ypixeloffset);
            paintMapSelection(graphics);
        }
    }

    /**
     * Paints the grid for one square. The grid is not painted if it is
     * disabled.
     * @param graphics the graphics context to draw in
     * @param point the map coordinates of the square to draw
     */
    protected void paintSquareGrid(@NotNull final Graphics graphics, @NotNull final Point point) {
        if (mapViewSettings.isGridVisible()) {
            // screen boundaries watch out
      
            graphics.drawLine(borderOffset.x + (point.x + point.y ) * 32, borderOffset.y + (point.y - point.x +mapSize.getWidth())* 32, borderOffset.x + (point.x + point.y ) * 32, borderOffset.y + (point.y - point.x + mapSize.getWidth() ) * 32 + 64);
       
             graphics.drawLine(borderOffset.x + (point.x + point.y ) * 32, borderOffset.y + (point.y - point.x +mapSize.getWidth()) * 32, borderOffset.x + (point.x + point.y ) * 32 + 64, borderOffset.y + (point.y - point.x +mapSize.getWidth()) * 32);
        }
    }

    /**
     * Paints the selection for one square.
     * @param graphics the graphics context to draw in
     * @param point map coordinates of the square to highlight
     */
    protected void paintSquareSelection(@NotNull final Graphics graphics, @NotNull final Point point) {
        final int gridFlags = mapGrid.getFlags(point.x, point.y);
        final boolean light = (isLightVisible() ^ mapViewSettings.isLightVisible()) && mapModel.getMapSquare(point).isLight();
      //  gridMapSquarePainter.paint(graphics, gridFlags, light, borderOffset.x + point.x * 64, borderOffset.y + point.y * 64, this);
     //   gridMapSquarePainter.paint(graphics, gridFlags, light, borderOffset.x + (point.x + point.y - 9) * 32, borderOffset.y + (point.y - point.x + 8)* 32, this);
        gridMapSquarePainter.paint(graphics, gridFlags, light, borderOffset.x + (point.x + point.y + 1 ) * 32, borderOffset.y + (point.y - point.x + mapSize.getWidth() - 1 )* 32, this);
    }

    /**
     * Paints the whole map.
     * @param graphics the graphics context to draw in
     * @param checkClip if set, omit squares outside <code>graphics</code>'s
     * clip area
     */
    private void paintMap(@NotNull final Graphics graphics, final boolean checkClip) {
        for (tmpPoint.y = 0; tmpPoint.y < mapGrid.getSize().getHeight(); tmpPoint.y++) {
            final int y = borderOffset.y + tmpPoint.y * 32;
            for (tmpPoint.x = 0; tmpPoint.x < mapGrid.getSize().getWidth(); tmpPoint.x++) {
                final int x = borderOffset.x + tmpPoint.x * 32;
                if (!checkClip || graphics.hitClip(x, y, 64, 64)) {
                    
                //    paintSquare(graphics, x, y, mapModel.getMapSquare(tmpPoint));
                    paintSquare2(graphics, x, y, tmpPoint,mapGrid.getSize().getWidth(),mapGrid.getSize().getHeight());
                }
            }
        }
    }

    /**
     * Paints the selection for the whole map. It's recommended to paint the
     * complete selection after the map itself, otherwise map elements actually
     * might hide selections.
     * @param graphics the graphics for painting
     */
    private void paintMapSelection(@NotNull final Graphics graphics) {
        for (tmpPoint.y = 0; tmpPoint.y < mapSize.getHeight(); tmpPoint.y++) {
            final int y = borderOffset.y + tmpPoint.y * 32;
            for (tmpPoint.x = 0; tmpPoint.x < mapSize.getWidth(); tmpPoint.x++) {
                final int x = borderOffset.x + tmpPoint.x * 32;
                if (graphics.hitClip(x, y, 64, 64)) {
                    paintSquareSelection(graphics, tmpPoint);
                }
            }
        }
    }
    
    public void paintMapSelection2(final Graphics graphics){
        paintMapSelection(graphics);
    }

    /**
     * Paints the grid of the whole map. The grid is not painted if it is
     * disabled. It's recommended to paint the complete grid after the map
     * itself, otherwise map elements actually might hide parts of the grid.
     * @param graphics the graphics for painting
     */
    private void paintMapGrid(@NotNull final Graphics graphics, int yoffset) {
        if (mapViewSettings.isGridVisible()) {
            final Size2D tmpMapSize = mapGrid.getSize();
            final int mapWidth = tmpMapSize.getWidth();
            final int mapHeight = tmpMapSize.getHeight();
            for (int x = 0; x <= mapWidth; x++) {
            //    graphics.drawLine(borderOffset.x + x * 32, borderOffset.y, borderOffset.x + x * 32, borderOffset.y + mapHeight * 32);
         graphics.drawLine( borderOffset.x+32+32*x,borderOffset.y+this.mapModel.getMapArchObject().getMapSize().getWidth()*32 - 32*x+yoffset,borderOffset.x+32+32*x+mapHeight*32,borderOffset.y+this.mapModel.getMapArchObject().getMapSize().getWidth()*32 - 32*x+mapHeight*32+yoffset);
            }
            for (int y = 0; y <= mapHeight; y++) {
               // graphics.drawLine(borderOffset.x, borderOffset.y + y * 32, borderOffset.x + mapWidth * 32, borderOffset.y + y * 32);
      graphics.drawLine( borderOffset.x+32+32*y,borderOffset.y+this.mapModel.getMapArchObject().getMapSize().getWidth()*32+64+32*y+yoffset,borderOffset.x+32+32*y+mapWidth*32,borderOffset.y+this.mapModel.getMapArchObject().getMapSize().getWidth()*32+64+32*y-mapWidth*32+yoffset);         
            }
        }
    }
    
    public void paintMapGrid2(Graphics graphics, int ypixeloffset){
        paintMapGrid(graphics, ypixeloffset);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Point getSquareLocationAt(@NotNull final Point point, @Nullable final Point retPoint) {
        final int mapWidth = mapSize.getWidth();
        final int mapHeight = mapSize.getHeight();
        final int xm;
        final int ym;
      //  if (point.x >= borderOffset.x && point.x < (mapWidth+mapHeight) * 2 * 64 + borderOffset.x && point.y >= borderOffset.y && point.y < (mapWidth+mapHeight) *2 * 64 + borderOffset.y) {
         //   xm = (point.x - borderOffset.x) / 64;
        //    xm = (((point.x - borderOffset.x) / 32) - ((point.y - borderOffset.y) / 32) + 17)/2;
       //     ym = (((point.x - borderOffset.x) / 32) + ((point.y - borderOffset.y) / 32) + 1)/2;
             xm = (((point.x - borderOffset.x) / 32) - ((point.y - borderOffset.y) / 32)+ mapSize.getWidth() )/2 - 1;
            ym = (((point.x - borderOffset.x) / 32) + ((point.y - borderOffset.y) / 32) - mapSize.getWidth() )/2;
         //    xm = (((point.x - borderOffset.x) / 32) - ((point.y - borderOffset.y) / 32) )/2;
       //     ym = (((point.x - borderOffset.x) / 32) + ((point.y - borderOffset.y) / 32) )/2;
            // unsure if I am working with view width or actual map width
            // both sets of bounds must be respected, unless we consider tile maps
            // or fancy relative coordinates and windowmanager stuff
      //  } else {
      //      xm = -1;
       //     ym = -1;
     //   }

     //   if (xm < 0 || xm >= mapWidth || ym < 0 || ym >= mapHeight) {
        // view or map?
     //    if ((xm + ym -9) < 0 || (xm + ym - 9) >= mapWidth*2 || (ym - xm + 8) < 0 || (ym - xm + 8) >= mapHeight*2) {   
         //   if ((xm + ym ) < -(mapWidth) || (xm + ym ) >= mapWidth*2 || (ym - xm +mapWidth) < -(mapHeight) || (ym - xm +mapWidth) >= mapHeight*2) { 
            
     //   if ((xm + ym ) < 0 || (xm + ym ) >= mapWidth*2 || (ym - xm ) < 0 || (ym - xm ) >= mapHeight*2)  {        
          //  return null;
      //  }
        if (retPoint != null) {
            retPoint.setLocation(xm, ym);
            return retPoint;
        }

        return new Point(xm, ym);
    }

    /**
     * Resizes the backing buffer to the new grid size. It will only be resized
     * if the map is not a pickmap.
     * @param size the new map size
     */
    protected abstract void resizeBackBuffer(@NotNull final Dimension size);

    /**
     * Paints one square.
     * @param g the graphics context to draw to
     * @param x the square coordinate to paint to
     * @param y the square coordinate to paint to
     * @param square the square to paint
     */
    protected abstract void paintSquare(@NotNull final Graphics g, final int x, final int y, @NotNull final MapSquare<GameObject, MapArchObject, Archetype> square);

    protected abstract void paintSquare2(@NotNull final Graphics g, final int x, final int y, @NotNull final Point point, int mapwidth, int mapheight);
    
    /**
     * Updates cached information to new map grid size.
     */
    private void resizeMapGrid() {
        // define how much drawing space we need for the map
        mapSize = mapGrid.getSize();
        final Dimension forcedSize = new Dimension((mapSize.getWidth()+mapSize.getHeight()) * 64 + 2 * borderOffset.x, (mapSize.getWidth()+mapSize.getHeight()) * 64 + 2 * borderOffset.y);
        resizeBackBuffer(forcedSize);
        setPreferredSize(forcedSize);
        setMinimumSize(forcedSize);
        revalidate();
        forceRepaint();
    }

    /**
     * Callback function that is called when a square may have changed.
     * @param point the coordinate of the changed square
     */
    protected abstract void updateSquare(@NotNull final Point point);

    /**
     * Callback function that is called when multiple squares may have changed.
     * @param rectangle the coordinates of the changed squares
     */
    protected abstract void updateSquares(@NotNull final Rectangle rectangle);

    /**
     * Callback function that is called when any square may have changed.
     */
    protected abstract void updateAll();

    /**
     * Returns the x offset to map borders.
     * @return the x offset
     */
    protected int getBorderOffsetX() {
        return borderOffset.x;
    }

    /**
     * Returns the y offset to map borders.
     * @return the y offset
     */
    protected int getBorderOffsetY() {
        return borderOffset.y;
    }

}
