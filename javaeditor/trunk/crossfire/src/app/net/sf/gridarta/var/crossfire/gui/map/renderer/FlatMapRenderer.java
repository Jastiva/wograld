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
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.lang.ref.SoftReference;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.filter.FilterState;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.model.filter.FilterConfig;
import net.sf.gridarta.model.filter.FilterConfigChangeType;
import net.sf.gridarta.model.filter.FilterConfigListener;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.crossfire.IGUIConstants;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.sf.gridarta.utils.Size2D;

/**
 * A {@link AbstractFlatMapRenderer} to render map files.
 * @author Andreas Kirschbaum
 * @author serpentshard
 */
public class FlatMapRenderer extends AbstractFlatMapRenderer {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(FlatMapRenderer.class);

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link MapModel} to render.
     * @serial
     */
    @NotNull
    private final MapModel<GameObject, MapArchObject, Archetype> mapModel;

    private final MapGrid mapGrid;
    
    /**
     * The {@link FilterControl} for filtering painted game objects.
     */
    @NotNull
    private final FilterControl<GameObject, MapArchObject, Archetype> filterControl;

    /**
     * The colors for highlighting.
     * @serial
     */
    @NotNull
    private final Color[] highLightMask = { new Color(1.0f, 0.0f, 0.0f, 0.33f), new Color(0.0f, 1.0f, 0.0f, 0.33f), new Color(0.0f, 1.0f, 1.0f, 0.33f), };

    /**
     * The {@link Icon} for painting empty map squares.
     */
    @NotNull
     private final ImageIcon emptySquareIcon;
  //  private final Icon emptySquareIcon;

    /**
     * The offset for painting the map contents.
     * @serial
     */
    @NotNull
    private final Point offset = new Point();

    /**
     * The back buffer for this map. A soft reference is used because back
     * buffers are huge and therefore frequently cause out of memory errors when
     * loading large and/or multiple maps.
     */
    @Nullable
    private SoftReference<BufferedImage> backBufferRef = null;

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The filter state instance for this map renderer.
     */
    @NotNull
    private final FilterState filterState = new FilterState();

    /**
     * The {@link SmoothingRenderer} for rendering smoothed faces.
     */
    @NotNull
    private final SmoothingRenderer smoothingRenderer;

    /**
     * The {@link FilterConfigListener} attached to {@link #filterControl} to
     * repaint all after config changes.
     */
    @NotNull
    private final FilterConfigListener filterConfigListener = new FilterConfigListener() {

        @Override
        public void configChanged(@NotNull final FilterConfigChangeType filterConfigChangeType, @NotNull final FilterConfig<?, ?> filterConfig) {
            forceRepaint();
        }

    };
    
    
//@NotNull
//    private final GridMapSquarePainter gridMapSquarePainter;
    
 //   @NotNull
 //   private final Point borderOffset = new Point();
    
 //   @NotNull
 //   private Size2D mapSize;
    
    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings instance to use
     * @param filterControl the filter to use
     * @param mapModel the map model to render
     * @param mapGrid the grid to render
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param systemIcons the system icons for creating icons
     * @param smoothingRenderer the smoothing renderer to use
     */
    public FlatMapRenderer(@NotNull final MapViewSettings mapViewSettings, @NotNull final FilterControl<GameObject, MapArchObject, Archetype> filterControl, @NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final MapGrid mapGrid, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final SystemIcons systemIcons, @NotNull final SmoothingRenderer smoothingRenderer) {
        super(mapViewSettings, mapModel, mapGrid, 32, gridMapSquarePainter, gameObjectParser);
        this.mapModel = mapModel;
        emptySquareIcon = systemIcons.getEmptySquareIcon();
        this.filterControl = filterControl;
        this.mapViewSettings = mapViewSettings;
        this.smoothingRenderer = smoothingRenderer;
        this.mapGrid = mapGrid;
        init();
        this.filterControl.addConfigListener(filterConfigListener);
      //  this.gridMapSquarePainter = gridMapSquarePainter;
      //  mapSize = this.mapModel.getMapArchObject().getMapSize();
     //   borderOffset.setLocation(borderSize, borderSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
        super.closeNotify();
        filterControl.removeConfigListener(filterConfigListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSquare(@NotNull final Point point) {
        if (!mapModel.getMapArchObject().isPointValid(point)) {
            return;
        }

        final Image backBuffer = getBackBufferImage();
        if (backBuffer == null) {
            return;
        }

        final Graphics g = backBuffer.getGraphics();
        try {
         //   paintSquare(g, getBorderOffsetX() + point.x * IGUIConstants.SQUARE_WIDTH, getBorderOffsetY() + point.y * IGUIConstants.SQUARE_HEIGHT, mapModel.getMapSquare(point));     
        //    paintSquare(g, getBorderOffsetX() + (point.x + point.y - 9)* 64, getBorderOffsetY() + (point.y - point.x + 8)* 64, mapModel.getMapSquare(point));
        //     paintSquare(g, getBorderOffsetX() + point.x* 32, getBorderOffsetY() + point.y * 32, mapModel.getMapSquare(point));
            paintSquare2(g, getBorderOffsetX() + point.x* 32, getBorderOffsetY() + point.y * 32, point,mapGrid.getSize().getWidth(),mapGrid.getSize().getHeight());
            // let paintsquare adjust coords
       // x     paintSquareGrid(g, point);
         //   paintSquareSelection(g, point);
           paintMapGrid2(g,0);
            paintMapSelection2(g);
        } finally {
            g.dispose();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSquares(@NotNull final Rectangle rectangle) {
        final Point point = new Point();
        if (mapViewSettings.isSmoothing()) {
            for (point.x = rectangle.x - 1; point.x < rectangle.x + rectangle.width + 1; point.x++) {
                for (point.y = rectangle.y - 1; point.y < rectangle.y + rectangle.height + 1; point.y++) {
                    updateSquare(point);
                }
            }
        } else {
            for (point.x = rectangle.x; point.x < rectangle.x + rectangle.width; point.x++) {
                for (point.y = rectangle.y; point.y < rectangle.y + rectangle.height; point.y++) {
                    updateSquare(point);
                }
            }
        }
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateAll() {
        final Image backBuffer = getBackBufferImage();
        if (backBuffer == null) {
            return;
        }

        final Graphics graphics = backBuffer.getGraphics();
        try {
            paintComponent(graphics, false, false,0);
        } finally {
            graphics.dispose();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(@NotNull final Graphics g) {
        final BufferedImage backBuffer = getBackBufferImage();
        if (backBuffer != null) {
            final int w = backBuffer.getWidth();
            final int h = backBuffer.getHeight();
            g.drawImage(backBuffer, 0, 0, w, h, 0, 0, w, h, null);
        } else {
            paintComponent(g, false, true,0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void resizeBackBuffer(@NotNull final Dimension size) {
        final RenderedImage backBuffer = getBackBufferImage();
        if (backBuffer != null && backBuffer.getWidth() == size.width && backBuffer.getHeight() == size.height) {
            return;
        }

        backBufferRef = null;

        if (log.isDebugEnabled()) {
            log.debug("Creating a backbuffer of size " + size.width + "x" + size.height + ".");
        }
        final BufferedImage newBackBuffer;
        //The backbuffer is optional
        //noinspection ErrorNotRethrown
        try {
            newBackBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        } catch (final OutOfMemoryError ignore) {
            if (log.isDebugEnabled()) {
                log.debug("out of memory creating new back buffer");
            }
            return;
        }
        final Graphics g = newBackBuffer.getGraphics();
        try {
            g.setColor(getBackground());
            g.fillRect(0, 0, size.width, size.height);
        } finally {
            g.dispose();
        }
        backBufferRef = new SoftReference<BufferedImage>(newBackBuffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintSquare(@NotNull final Graphics g, final int x, final int y, @NotNull final MapSquare<GameObject, MapArchObject, Archetype> square) {
        filterControl.newSquare(filterState);
        if (square.isEmpty()) {
         //   emptySquareIcon.paintIcon(this, g, x, y);
            // apparently x and y are already in pixels
         //   emptySquareIcon.paintIcon(this, g, (x+y-9*32), (y-x+8*32));
            emptySquareIcon.paintIcon(this, g, (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32));
        } else {
            
         //   g.fillRect(x, y, IGUIConstants.SQUARE_WIDTH, IGUIConstants.SQUARE_HEIGHT);
          //    g.fillRect(x, y, 64, 64);
            // apparently x and y are already in pixels
          //  g.fillRect((x+y-9*32), (y-x+8*32), 64, 64);
            // stop drawing corners over the art
       //     g.fillRect((x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), 64, 64);
            if (mapViewSettings.isSmoothing()) {
                final int borderOffsetX = getBorderOffsetX();
                final int borderOffsetY = getBorderOffsetY();
                int layer = -1;
                for (final GameObject node : square) {
                    if (node.getAttributeInt(GameObject.INVISIBLE, true) == 0) {
                        layer++;
                    }
                    filterControl.objectInSquare(filterState, node);
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                       // let this method adjust coord
                        paintGameObject(g, x, y, node);
                        if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
                            smoothingRenderer.paintSmooth(g, square.getMapLocation(), node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, borderOffsetX, borderOffsetY);
                        }
                    }
                }
                if (layer > -1) {
                    smoothingRenderer.paintSmooth(g, square.getMapLocation(), 1, layer + 1, true, borderOffsetX, borderOffsetY);
                }
            } else {
                for (final GameObject node : square) {
                    filterControl.objectInSquare(filterState, node);
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        // let this method adjust coord
                        
                        paintGameObject(g, x, y, node);
                    }
                }
            }
        }
        for (int i = 0; i < FilterControl.MAX_HIGHLIGHT; i++) {
            if (filterControl.isHighlightedSquare(filterState, i)) {
                final Color color = g.getColor();
                g.setColor(highLightMask[i]);
            //    g.fillRect(x, y, IGUIConstants.SQUARE_WIDTH, IGUIConstants.SQUARE_HEIGHT);
              //  g.fillRect(x, y, 64, 64);
                // apparently, x and y are already in pixels
             //   g.fillRect((x+y-9*32), (y-x+8*32), 64, 64);
                g.fillRect((x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), 64, 64);
                g.setColor(color);
            }
        }
    }
    
     /**
     * {@inheritDoc}
     */
    @Override
    protected void paintSquare2(@NotNull final Graphics g, final int x, final int y, @NotNull final Point point, int mapwidth, int mapheight) {
        final MapSquare<GameObject, MapArchObject, Archetype> midsquare = mapModel.getMapSquare(point);
        int tx = point.x;
        int ty = point.y;
        MapSquare<GameObject, MapArchObject, Archetype> Northsquare = mapModel.getMapSquare(point);
       if(ty > 0){
        Point p2 = new Point(tx,ty-1);
       
        Northsquare = mapModel.getMapSquare(p2);
       } 
       MapSquare<GameObject, MapArchObject, Archetype> Eastsquare = mapModel.getMapSquare(point);
       if(tx < mapwidth-1){
        Point p3 = new Point(tx+1,ty);
     Eastsquare = mapModel.getMapSquare(p3);
       }
       MapSquare<GameObject, MapArchObject, Archetype> Westsquare = mapModel.getMapSquare(point);
       if(tx > 0){
        Point p4 = new Point(tx-1,ty);
     Westsquare = mapModel.getMapSquare(p4);
       }
       MapSquare<GameObject, MapArchObject, Archetype> Southsquare = mapModel.getMapSquare(point);
       if(ty < mapheight-1){
        Point p5 = new Point(tx,ty+1);
     Southsquare = mapModel.getMapSquare(p5);
       }
       // mapsquares are not exactly the sources of images, but relate to them enough that their positions are named
       // the names do not necessarily relate to the destinations of corresponding images
        MapSquare<GameObject, MapArchObject, Archetype> Northeastsquare = mapModel.getMapSquare(point);
       if((ty > 0)&&(tx < mapwidth-1)){
        Point p6 = new Point(tx+1,ty-1);
       
        Northeastsquare = mapModel.getMapSquare(p6);
       }
       MapSquare<GameObject, MapArchObject, Archetype> TopLeftsquare = mapModel.getMapSquare(point);
       if((ty > 1)&&(tx < mapwidth-1)){
        Point p7 = new Point(tx+1,ty-2);
       
        TopLeftsquare = mapModel.getMapSquare(p7);
       }
       MapSquare<GameObject, MapArchObject, Archetype> TopRightsquare = mapModel.getMapSquare(point);
       if((ty > 0)&&(tx < mapwidth-2)){
        Point p8 = new Point(tx+2,ty-1);
       
        TopRightsquare = mapModel.getMapSquare(p8);
       }
       MapSquare<GameObject, MapArchObject, Archetype> SouthWestsquare = mapModel.getMapSquare(point);
       if((ty < mapheight-1)&&(tx > 0)){
        Point p9 = new Point(tx-1,ty+1);
       
        SouthWestsquare = mapModel.getMapSquare(p9);
       }
       MapSquare<GameObject, MapArchObject, Archetype> LowerLeftsquare = mapModel.getMapSquare(point);
       if((ty < mapheight-1)&&(tx > 1)){
        Point pA = new Point(tx-2,ty+1);
       
        LowerLeftsquare = mapModel.getMapSquare(pA);
       }
       MapSquare<GameObject, MapArchObject, Archetype> LowerRightsquare = mapModel.getMapSquare(point);
       if((ty < mapheight-2)&&(tx > 0)){
        Point pB = new Point(tx-1,ty+2);
       
        LowerRightsquare = mapModel.getMapSquare(pB);
       }
       
       
        filterControl.newSquare(filterState);
     //   if (midsquare.isEmpty()) {
         //   emptySquareIcon.paintIcon(this, g, x, y);
            // apparently x and y are already in pixels
         //   emptySquareIcon.paintIcon(this, g, (x+y-9*32), (y-x+8*32));
        //    emptySquareIcon.paintIcon(this, g, (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32));
      //  } else {
            
         //   g.fillRect(x, y, IGUIConstants.SQUARE_WIDTH, IGUIConstants.SQUARE_HEIGHT);
          //    g.fillRect(x, y, 64, 64);
            // apparently x and y are already in pixels
          //  g.fillRect((x+y-9*32), (y-x+8*32), 64, 64);
            
            // stop drawing corners over the art
       //     g.fillRect((x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), 64, 64);
   // smoothing         if (mapViewSettings.isSmoothing()) {
                // wograld does not support smoothing until there are more good transition tiles
           /*     final int borderOffsetX = getBorderOffsetX();
                final int borderOffsetY = getBorderOffsetY();
                int layer = -1;
                for (final GameObject node : midsquare) {
                    if (node.getAttributeInt(GameObject.INVISIBLE, true) == 0) {
                        layer++;
                    }
                    filterControl.objectInSquare(filterState, node);
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                       // let this method adjust coord
                        paintGameObject(g, x, y, node);
                        if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
                            smoothingRenderer.paintSmooth(g, midsquare.getMapLocation(), node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, borderOffsetX, borderOffsetY);
                        }
                    }
                }
                if (layer > -1) {
                    smoothingRenderer.paintSmooth(g, midsquare.getMapLocation(), 1, layer + 1, true, borderOffsetX, borderOffsetY);
                }  */
       // smoothing     } else {
                
                if((ty > 1)&&(tx < mapwidth-1)){
                
                   if(!TopLeftsquare.isEmpty()){
                       for (final GameObject node : TopLeftsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                           final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

      //   g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
      //   g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);     
         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);                    
                            }
                        }
                   } else {
                   //    emptySquareIcon.paintIcon(this, g, (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
               g.drawImage(emptySquareIcon.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x+32, offset.y+32, offset.x+64, offset.y+64,this);
         //   emptySquareIcon.paintIcon(this, g, (x+y)-32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-96);            
                   }
                }
      
                if((ty > 0)&&(tx < mapwidth-2)){
                     if(!TopRightsquare.isEmpty()){
                       for (final GameObject node : TopRightsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                           final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

      //   g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
      //   g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);     
         g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);                    
                            }
                        }
                   } else {
          g.drawImage(emptySquareIcon.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);                                     
         //  emptySquareIcon.paintIcon(this, g, (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-96);               
                     }
                }
                
                if((ty > 0)&&(tx < mapwidth-1)){
                    if(!Northeastsquare.isEmpty()){
                        for (final GameObject node : Northeastsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                           final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

      //   g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
      //   g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);     
         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this);                    
                            }
                        }
                    } else {
             //  emptySquareIcon.paintIcon(this, g, (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64); 
             g.drawImage(emptySquareIcon.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this);                                 
                    }
                 }
                
                if(ty > 0){
                if(!Northsquare.isEmpty()){
                for (final GameObject node : Northsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                           final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);
                      
                        
                            }
                }
            } else {
       //    emptySquareIcon.paintIcon(this, g, (x+y)-32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32); 
         g.drawImage(emptySquareIcon.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);             
                }
           }
                
                if(tx < mapwidth-1){
               if(!Eastsquare.isEmpty()){
                for (final GameObject node : Eastsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);
                       
                    }
                }
                } else {
          //  emptySquareIcon.paintIcon(this, g, (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32); 
          g.drawImage(emptySquareIcon.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);   
               }
                      }
                     
                if(!midsquare.isEmpty()){
                for (final GameObject node : midsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        // let this method adjust coord
                        
                        paintGameObject(g, x, y, node);
                    }
                }
                } else {
                       emptySquareIcon.paintIcon(this, g, (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32));   
                }
               
                
                if(tx > 0){
                if(!Westsquare.isEmpty()){
                for (final GameObject node : Westsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                       
                        final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);
                        
                        
                    }
                }
                } else {
      //   emptySquareIcon.paintIcon(this, g, (x+y)-32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32); 
             g.drawImage(emptySquareIcon.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);         
                }
                     }
                
                if(ty < mapheight-1){
               if(!Southsquare.isEmpty()){
                for (final GameObject node : Southsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);
                        
                        
                    }
                }
                } else {
           //  emptySquareIcon.paintIcon(this, g, (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32); 
             g.drawImage(emptySquareIcon.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);         
               }
                }
                
                // begin draw of top graphic
               // final ImageIcon img = node.getSecondImg();
                if(ty > 0){
                if(!Northsquare.isEmpty()){
                for (final GameObject node : Northsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                        //   final ImageIcon img = node.getImage(mapViewSettings);
                           final ImageIcon img = node.getSecondImg();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) -32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
                      
                        
                            }
                }
            }
                }
                
                //
                 if(tx < mapwidth-1){
               if(!Eastsquare.isEmpty()){
                for (final GameObject node : Eastsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      
                        //   final ImageIcon img = node.getImage(mapViewSettings);
                           final ImageIcon img = node.getSecondImg();
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) - 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);
                       
                    }
                }
                } 
                      }
                 //
                 
                 for (final GameObject node : midsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        final ImageIcon img = node.getSecondImg();
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this);
                        
                        
                    }
                }
                 //
                 
                 if(tx > 0){
                if(!Westsquare.isEmpty()){
                for (final GameObject node : Westsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                       
                       // final ImageIcon img = node.getImage(mapViewSettings);
                            final ImageIcon img = node.getSecondImg();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);
                        
                        
                    }
                }
                }
                     }
                 //
                 
                  if(ty < mapheight-1){
               if(!Southsquare.isEmpty()){
                for (final GameObject node : Southsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      //  final ImageIcon img = node.getImage(mapViewSettings);
                             final ImageIcon img = node.getSecondImg();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);
                        
                        
                    }
                }
                } 
                }
                  //
                if((ty < mapheight-1)&&(tx > 0)){
                    if(!SouthWestsquare.isEmpty()){
                for (final GameObject node : SouthWestsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      //  final ImageIcon img = node.getImage(mapViewSettings);
                             final ImageIcon img = node.getSecondImg();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+64, offset.y+64, this);
                        
                        
                    }
                }
                } 
     
                }
                
     //  MapSquare<GameObject, MapArchObject, Archetype> LowerLeftsquare = mapModel.getMapSquare(point);
                if((ty < mapheight-1)&&(tx > 1)){
                   if(!LowerLeftsquare.isEmpty()){
                for (final GameObject node : LowerLeftsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      //  final ImageIcon img = node.getImage(mapViewSettings);
                             final ImageIcon img = node.getSecondImg();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) +32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);
                        
                        
                    }
                }
                } 
       
                }
     //  MapSquare<GameObject, MapArchObject, Archetype> LowerRightsquare = mapModel.getMapSquare(point);
                if((ty < mapheight-2)&&(tx > 0)){
                    if(!LowerRightsquare.isEmpty()){
                for (final GameObject node :  LowerRightsquare) {
                    filterControl.objectInSquare(filterState, node);
        
                    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      //  final ImageIcon img = node.getImage(mapViewSettings);
                             final ImageIcon img = node.getSecondImg();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

         g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) +32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);
                        
                        
                    }
                }
                } 
       
                    
                }
       //
                
        // smoothing    }
     //   }
        for (int i = 0; i < FilterControl.MAX_HIGHLIGHT; i++) {
            if (filterControl.isHighlightedSquare(filterState, i)) {
                final Color color = g.getColor();
                g.setColor(highLightMask[i]);
            //    g.fillRect(x, y, IGUIConstants.SQUARE_WIDTH, IGUIConstants.SQUARE_HEIGHT);
              //  g.fillRect(x, y, 64, 64);
                // apparently, x and y are already in pixels
             //   g.fillRect((x+y-9*32), (y-x+8*32), 64, 64);
                g.fillRect((x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), 64, 64);
                g.setColor(color);
            }
        }
    }
    
    // protected void paintSquareSelection(@NotNull final Graphics graphics, @NotNull final Point point) {
    //    final int gridFlags = mapGrid.getFlags(point.x, point.y);
       // final boolean light = (isLightVisible() ^ mapViewSettings.isLightVisible()) && mapModel.getMapSquare(point).isLight();
     //   boolean light = false;
      //  gridMapSquarePainter.paint(graphics, gridFlags, light, borderOffset.x + point.x * 64, borderOffset.y + point.y * 64, this);
     //   gridMapSquarePainter.paint(graphics, gridFlags, light, borderOffset.x + (point.x + point.y - 9) * 32, borderOffset.y + (point.y - point.x + 8)* 32, this);
   //     gridMapSquarePainter.paint(graphics, gridFlags, light, borderOffset.x + (point.x + point.y ) * 32, borderOffset.y + (point.y - point.x + 1+ mapSize.getWidth() )* 32, this);
  //  }

    /**
     * Paints one game object.
     * @param g the graphics to paint into
     * @param x the x-coordinate to paint at
     * @param y the y-coordinate to paint at
     * @param node the game object to paint
     */
    //  private void paintGameObject(@NotNull final Graphics g, final int x, final int y, @NotNull final net.sf.gridarta.model.gameobject.GameObject<GameObject, MapArchObject, Archetype> node) {
    private void paintGameObject(@NotNull final Graphics g, final int x, final int y, @NotNull final GameObject node) {
        final ImageIcon img = node.getImage(mapViewSettings);
      //   final ImageIcon img = node.getSecondImg();
       // if (!node.isMulti() || (img.getIconWidth() == IGUIConstants.SQUARE_WIDTH && img.getIconHeight() == IGUIConstants.SQUARE_HEIGHT)) {
         if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
            offset.x = 0;
            offset.y = 0;
            // this is a test
            // what if multi, and w,h are less than 64? mess?
        } else {
            // this is an oversized image, so it must be shifted
            // XXX: it must also be clipped to not overwrite filter information
         //   offset.x = IGUIConstants.SQUARE_WIDTH * (node.getArchetype().getMultiX() - node.getMinX());
            offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
           // offset.y = IGUIConstants.SQUARE_HEIGHT * (node.getArchetype().getMultiY() - node.getMinY());
            offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
            // keep these in rectangular rather than diagonal, until operation below
        }
     //   g.drawImage(img.getImage(), x, y, x + IGUIConstants.SQUARE_WIDTH, y + IGUIConstants.SQUARE_HEIGHT, offset.x, offset.y, offset.x + IGUIConstants.SQUARE_WIDTH, offset.y + IGUIConstants.SQUARE_HEIGHT, this);
     //   g.drawImage(img.getImage(), x, y, x + 64, y + 64, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
        // code above can get multi-tile offset in the square coords, do not convert coords until below
         // the units are pixels?
         // there is also the map coord vs screen coord issue
    //     g.drawImage(img.getImage(), (x+y-9*32), (y-x+8*32), (x+y-9*32) + 64, (y-x+8*32) + 64, offset.x, offset.y, offset.x+64, offset.y+64, this);
         if(img != null){
         g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+64, offset.y+64, this);
         }
    }

    /**
     * Get the back buffer image for the map. Sets {@link #backBufferRef} to
     * <code>null</code> if the back buffer has been deleted by the garbage
     * collector.
     * @return the back buffer image, or <code>null</code> if the map has no
     *         back buffer
     */
    @Nullable
    private BufferedImage getBackBufferImage() {
        if (backBufferRef == null) {
            return null;
        }

        final BufferedImage backBuffer = backBufferRef.get();
        if (backBuffer == null) {
            if (log.isDebugEnabled()) {
                log.debug("lost back buffer");
            }
            backBufferRef = null;
            return null;
        }

        return backBuffer;
    }

}
