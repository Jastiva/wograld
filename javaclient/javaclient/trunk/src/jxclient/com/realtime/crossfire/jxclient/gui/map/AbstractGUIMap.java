/*
 * This file is part of JXClient, the Fullscreen Java Crossfire Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.crossfire.jxclient.gui.map;

import com.realtime.crossfire.jxclient.faces.Face;
import com.realtime.crossfire.jxclient.faces.FacesProvider;
// import com.realtime.crossfire.jxclient.faces.ColorFilter;
// import com.realtime.crossfire.jxclient.faces.RedBlueGreenFilter;
import com.realtime.crossfire.jxclient.gui.gui.AbstractGUIElement;
import com.realtime.crossfire.jxclient.gui.gui.GUIElement;
import com.realtime.crossfire.jxclient.gui.gui.GUIElementListener;
import com.realtime.crossfire.jxclient.gui.gui.TooltipManager;
import com.realtime.crossfire.jxclient.map.CfMap;
import com.realtime.crossfire.jxclient.map.CfMapSquare;
// import com.realtime.crossfire.jxclient.map.Comparator2D;
// import com.realtime.crossfire.jxclient.map.CfMapClump;
import com.realtime.crossfire.jxclient.mapupdater.MapListener;
import com.realtime.crossfire.jxclient.mapupdater.MapScrollListener;
import com.realtime.crossfire.jxclient.mapupdater.MapUpdaterState;
import com.realtime.crossfire.jxclient.mapupdater.NewmapListener;
import com.realtime.crossfire.jxclient.server.crossfire.MapSizeListener;
import com.realtime.crossfire.jxclient.server.crossfire.messages.Map2;
import com.realtime.crossfire.jxclient.util.MathUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
// import java.awt.image.ImageFilter;
// import java.awt.image.FilteredImageSource;
import java.awt.image.WritableRaster;
// import java.awt.image.DataBuffer;
import java.awt.image.ColorModel;
// import java.awt.image.SampleModel;
import java.awt.image.PixelGrabber;
// import java.awt.Polygon;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
// import java.awt.Shape;
import java.awt.HeadlessException;
import java.util.ArrayDeque;
// import java.util.Arrays;
// import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.ImageIcon;
// import javax.swing.GrayFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
// import net.infonode.util.ImageUtils;


/**
 * Abstract base class for {@link GUIElement GUIElements} that display map
 * views.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractGUIMap extends AbstractGUIElement {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * The {@link MapUpdaterState} instance to display.
     */
    @NotNull
    private final MapUpdaterState mapUpdaterState;

    /**
     * The {@link FacesProvider} for looking up faces.
     */
    @NotNull
   //  private final FacesProvider facesProvider;
    protected final FacesProvider facesProvider;

    /**
     * The {@link SmoothingRenderer} to use or <code>null</code> to not draw
     * smoothed faces.
     */
    @Nullable
  //  private final SmoothingRenderer smoothingRenderer;
    protected final SmoothingRenderer smoothingRenderer;

    /**
     * Synchronizes access to {@link #bufferedImage}, {@link #clearMapPending},
     * and {@link #scrollMapPending}.
     */
    @NotNull
  //  private final Object bufferedImageSync = new Object();
    protected final Object bufferedImageSync = new Object();

    /**
     * An {@link BufferedImage} having the size of this component. It contains
     * the rendered map contents.
     */
    @Nullable
    private transient BufferedImage bufferedImage = null;

    /**
     * Whether the map area should be blanked.
     */
    private boolean clearMapPending = true;

    /**
     * Pending map scrolls.
     */
    //private
    @NotNull
    protected final Deque<Long> scrollMapPending = new ArrayDeque<Long>();

    /**
     * The map width in squares.
     */
    private int mapWidth = 0;

    /**
     * The map height in squares.
     */
    private int mapHeight = 0;

    /**
     * The size of one tile.
     */
    private final int tileSize;

    /**
     * The x offset of the tile representing the player.
     */
    private int playerX = 0;

    /**
     * The y offset of the tile representing the player.
     */
    private int playerY = 0;

    /**
     * The x offset for drawing the square at coordinate 0 of the map.
     */
    //private
    protected int offsetX = 0;

    /**
     * The y offset for drawing the square at coordinate 0 of the map.
     */
    //private
    protected int offsetY = 0;

    /**
     * The tile x coordinate where map drawing starts. May be positive if the
     * map view is larger than the gui's area.
     */
    //private
    protected int displayMinX = 0;

    /**
     * The tile x coordinate where map drawing ends. May be less than {@link
     * #mapWidth} if the map view is larger than the gui's area.
     */
    //private
    protected int displayMaxX = 0;

    /**
     * The tile y coordinate where map drawing starts. May be positive if the
     * map view is larger than the gui's area.
     */
    //private
    protected int displayMinY = 0;

    /**
     * The tile y coordinate where map drawing ends. May be less than {@link
     * #mapWidth} if the map view is larger than the gui's area.
     */
    //private
    protected int displayMaxY = 0;

    /**
     * The distance the leftmost visible tile reaches outside the map window.
     * <code>-{@link #tileSize}<displayMinOffsetX<=0</code>.
     */
    private int displayMinOffsetX = 0;

    /**
     * The number of pixels that are visible in the rightmost visible tile.
     * <code>0<=displayMaxOffsetX<{@link #tileSize}</code>.
     */
    private int displayMaxOffsetX = 0;

    /**
     * The distance the topmost visible tile reaches outside the map window.
     * <code>-{@link #tileSize}<displayMinOffsetX<=0</code>.
     */
    private int displayMinOffsetY = 0;

    /**
     * The number of pixels that are visible in the bottommost visible tile.
     * <code>0<=displayMaxOffsetY<{@link #tileSize}</code>.
     */
    private int displayMaxOffsetY = 0;

    /**
     * Maps {@link Color} to an image filled with this color with a size of one
     * square.
     */
    @NotNull
    private final Map<Color, Image> images = new HashMap<Color, Image>();

    /**
     * The {@link MapListener} registered to receive map updates.
     */
    

    /**
     * The {@link NewmapListener} registered to receive newmap commands.
     */
    @NotNull
    private final NewmapListener newmapListener = new NewmapListener() {

        @Override
        public void commandNewmapReceived() {
            synchronized (bufferedImageSync) {
                clearMapPending = true;
                scrollMapPending.clear();
            }
            setChanged();
        }

    };

    /**
     * Blanks the map display.
     * @param g the graphics to paint to
     */
    private void clearMap(@NotNull final Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setBackground(DarknessColors.FOG_OF_WAR_COLOR);
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    /**
     * The {@link MapScrollListener} registered to receive map_scroll commands.
     */
    @NotNull
    private final MapScrollListener mapscrollListener = new MapScrollListener() {

        @Override
        public void mapScrolled(final int dx, final int dy) {
            synchronized (bufferedImageSync) {
                scrollMapPending.offerLast(((long)dx<<32)|(dy&0xFFFFFFFFL));
            }
            setChanged();
        }

    };

    /**
     * The {@link MapSizeListener} registered to detect map size changes.
     */
    @NotNull
    private final MapSizeListener mapSizeListener = new MapSizeListener() {

        @Override
        public void mapSizeChanged(final int mapWidth, final int mapHeight) {
            setMapSize(mapWidth, mapHeight);
            redrawAll();
        }

    };

    /**
     * Creates a new instance.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param mapUpdaterState the map updater state instance to use
     * @param facesProvider the faces provider for looking up faces
     * @param smoothingRenderer the smoothing renderer to use or
     * <code>null</code> to not draw smoothed faces
     */
    protected AbstractGUIMap(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, @NotNull final MapUpdaterState mapUpdaterState, @NotNull final FacesProvider facesProvider, @Nullable final SmoothingRenderer smoothingRenderer) {
        super(tooltipManager, elementListener, name, Transparency.OPAQUE);
        this.smoothingRenderer = smoothingRenderer;
        tileSize = facesProvider.getSize();
        assert tileSize > 0;
        this.mapUpdaterState = mapUpdaterState;
        this.facesProvider = facesProvider;
        this.mapUpdaterState.addMapSizeListener(mapSizeListener);
        
        this.mapUpdaterState.addCrossfireNewmapListener(newmapListener);
        this.mapUpdaterState.addCrossfireMapScrollListener(mapscrollListener);
        setMapSize(this.mapUpdaterState.getMapWidth(), this.mapUpdaterState.getMapHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();
        mapUpdaterState.removeMapSizeListener(mapSizeListener);
        mapUpdaterState.removeCrossfireNewmapListener(newmapListener);
        mapUpdaterState.removeCrossfireMapScrollListener(mapscrollListener);
      //  mapUpdaterState.removeCrossfireMapListener(mapListener);
    }

    /**
     * Redraws the complete map view.
     */
    private void redrawAll() {
        final CfMap map = mapUpdaterState.getMap();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (map) {
            //noinspection NestedSynchronizedStatement
            synchronized (bufferedImageSync) {
                final Graphics g = createBufferGraphics(map);
                try {
                    redrawTiles(g, map, displayMinX, displayMinY, displayMaxX, displayMaxY);
                    markPlayer(g, 0, 0);
                } finally {
                    g.dispose();
                }
            }
        }
    }

    /**
     * Redraws all non-dirty tiles.
     * @param g the graphics to draw into
     * @param map the map
     */
    //private
    protected void redrawAllUnlessDirty(@NotNull final Graphics g, @NotNull final CfMap map) {
        redrawTilesUnlessDirty2(g, map, displayMinX-offsetX/tileSize, displayMinY-offsetY/tileSize, displayMaxX-offsetX/tileSize, displayMaxY-offsetY/tileSize);
    }

    /**
     * Redraws a rectangular area of tiles.
     * @param g the graphics to draw into
     * @param map the map to draw
     * @param x0 the left edge to redraw (inclusive)
     * @param y0 the top edge to redraw (inclusive)
     * @param x1 the right edge to redraw (exclusive)
     * @param y1 the bottom edge to redraw (exclusive)
     */
    //private
    protected void redrawTiles(@NotNull final Graphics g, @NotNull final CfMap map, final int x0, final int y0, final int x1, final int y1) {
     /*   for (int x = x0; x < 2*x1; x++) {
            for (int y = y0; y < 2*y1; y++) {
           //     final int mapSquareX = x-offsetX/tileSize;
           //     final int mapSquareY = y-offsetY/tileSize;
           //     final CfMapSquare mapSquare = map.getMapSquare(mapSquareX, mapSquareY);
        //        redrawSquare(g, mapSquare, map, mapSquareX, mapSquareY);
           //     redrawSquare(g,mapSquare,map,(mapSquareX+mapSquareY-7),(mapSquareY-mapSquareX+10));
                redrawSquare2(g, map, x, y);
            }
        }  */
        
        
        // horizontal offsets should have been set when writing to map structs
        // from network
        // but need to draw floors or layers in proper order
        // also, perhaps the loop bounds need editing, but the adjustments to
        // what needs to be cleared based on floor
        // should have been done on processmapscroll
        for(int floor = 3; floor > 0; floor--){
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3L0(g, map, x, y,floor);
            }
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3(g, map, x, y,floor);
            }
        }
        
        
        
        // try conditioning these upon face quads
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3L0(g, map, x, y,floor);
            }
            
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3(g, map, x, y,floor);
            }
            
        }
        
        
        
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2L0(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3L0(g, map, x, y,0);
            }
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3(g, map, x, y,0);
            }
        }
        
        
        // try conditioning these upon face quads
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2L0(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3L0(g, map, x, y,0);
            }
            
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3(g, map, x, y,0);
            }
            
        }
        
        
        for(int floor = 4; floor < 7; floor++){
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3L0(g, map, x, y,floor);
            }
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3(g, map, x, y,floor);
            }
        }
        
        
        // try conditioning these upon face quads
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3L0(g, map, x, y,floor);
            }
            
        }
        
         for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3(g, map, x, y,floor);
            }
            
        }
        
        
        
        }
        
        
        
    }

    /**
     * Redraws a rectangular area of non-dirty tiles.
     * @param g the graphics to draw into
     * @param map the map to draw
     * @param x0 the left edge to redraw (inclusive)
     * @param y0 the top edge to redraw (inclusive)
     * @param x1 the right edge to redraw (exclusive)
     * @param y1 the bottom edge to redraw (exclusive)
     */
    private void redrawTilesUnlessDirty(@NotNull final Graphics g, @NotNull final CfMap map, final int x0, final int y0, final int x1, final int y1) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                redrawSquareUnlessDirty(g, map, x, y);
            }
        }
    }
    
    //private
    protected void redrawTilesUnlessDirty2(@NotNull final Graphics g, @NotNull final CfMap map, final int x0, final int y0, final int x1, final int y1) {
      /*  for (int x = x0; x < 2*x1; x++) {
            for (int y = y0; y < 2*y1; y++) {
                redrawSquareUnlessDirty2(g, map, x, y);
            }
        } */
        
        // horizontal offsets should have been set when writing to map structs
        // from network
        // but need to draw floors or layers in proper order
        // also, perhaps the loop bounds need editing, but the adjustments to
        // what needs to be cleared based on floor
        // should have been done on processmapscroll
        for(int floor = 3; floor > 0; floor--){
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3L0(g, map, x, y,floor);
            }
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3(g, map, x, y,floor);
            }
        }
        
        
        
        
        // try conditioning these upon face quads
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty3L0(g, map, x, y,floor);
            }
            
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty3(g, map, x, y,floor);
            }
            
        }
        
        
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2L0(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3L0(g, map, x, y,0);
            }
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3(g, map, x, y,0);
            }
        }
        
        
        // try conditioning these upon face quads
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty2L0(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty3L0(g, map, x, y,0);
            }
            
        }
        
         for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty2(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty3(g, map, x, y,0);
            }
            
        }
        
        
        
        for(int floor = 4; floor < 7; floor++){
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3L0(g, map, x, y,floor);
            }
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3(g, map, x, y,floor);
            }
        }
        
        
        // try conditioning these upon face quads
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty2L0(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty3L0(g, map, x, y,floor);
            }
            
        }
        
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty2(g, map, x, y,floor);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTopUnlessDirty3(g, map, x, y,floor);
            }
            
        }
        
        
        
        }
        
        
    }

    /**
     * Redraws one square if it is not dirty. This function is called for tiles
     * that need to be repainted due to scrolling. Skipping dirty squares
     * prevents multiple repainting.
     * @param g the graphics to draw into
     * @param map the map to draw
     * @param x the x-coordinate of the square to clear
     * @param y the y-coordinate of the square to clear
     */
    private void redrawSquareUnlessDirty(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
        final CfMapSquare mapSquare = map.getMapSquareUnlessDirty(x, y);
        if (mapSquare != null) {
           // redrawSquare(g, mapSquare, map, x, y);
            redrawSquare(g,mapSquare,map,x,y);
        }
    }
    private void redrawSquareUnlessDirty2L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor){
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
   
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
     
        boolean foundSquare = false;
            
     
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
      
       
      g.drawImage(imageIcon.getImage(), px, py, 96,96,null); 
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        
          //  if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
          if (!foundSquare){
              if(floor == 3){
               ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
              }
       
        //    paintSquareBackground(g, px, py, false, mapSquare);
        }
   
          if (mapSquare.isFogOfWar(3*floor)) {
  
                
            } else {
  
          }
            
        } 
 
       }  
        
    }
    
    
    private void redrawSquareUnlessDirty2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
     
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
   
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
     
        boolean foundSquare = false;
            for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     
            Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
      
       
      g.drawImage(imageIcon.getImage(), px, py, 96,96,null); 
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
          //  if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
          if (!foundSquare){
              if(floor == 3){
               ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
              }
       
        //    paintSquareBackground(g, px, py, false, mapSquare);
        }
   
          if (mapSquare.isFogOfWar(3*floor)) {
  
                
            } else {
  
          }
            
        } 
 
       }  
        
    }
    
    private void redrawSquareUnlessDirty3L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor){
          int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
     
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
               
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
    
        boolean foundSquare = false;
            
     
         
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
            
   
    
        g.drawImage(imageIcon.getImage(), px+48, py+48, 96,96,null);    
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        
           // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
               if(!foundSquare){
                   if(floor == 3){
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                   }
       
       //     paintSquareBackground(g, px, py, false, mapSquare);
        }
   
               if (mapSquare.isFogOfWar(3*floor)) {
       
                
            } else {
  
               }
            
        }
        
        
        
       }
    }
    
    private void redrawSquareUnlessDirty3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
        
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
     
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
               
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
    
        boolean foundSquare = false;
            for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     
         
            Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
            
   
    
        g.drawImage(imageIcon.getImage(), px+48, py+48, 96,96,null);    
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
           // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
               if(!foundSquare){
                   if(floor == 3){
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                   }
       
       //     paintSquareBackground(g, px, py, false, mapSquare);
        }
   
               if (mapSquare.isFogOfWar(3*floor)) {
       
                
            } else {
  
               }
            
        }
        
        
        
       }
        
    
          
        
    }
    
    private void redrawTopUnlessDirty2L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
     
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx-1, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
    
           
      
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
   //             if (!foundSquare) {
     //               foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
       //         }
                int coversthings = face.getFaceQuad();
                if(coversthings != 0){
            
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum() + 10000,null);
  
            if(imageIcon2 != null){
            g.drawImage(imageIcon2.getImage(), px, py, 96,96,null);
            
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            } // imageIcon2 != null
                } // face is not just a floor
            }
        
 
                
        //    paintColoredSquare(g, DarknessColors.FOG_OF_WAR_COLOR, offsetX+x*tileSize+16, offsetY+y*tileSize+16);

        }  
      }    
    }
    
    private void redrawTopUnlessDirty2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
     
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx-1, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
    
            for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
      
            Face face = mapSquare.getFace(layer);
            if (face != null) {
   //             if (!foundSquare) {
     //               foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
       //         }
                int coversthings = face.getFaceQuad();
                if(coversthings != 0){
            
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum() + 10000,null);
  
            if(imageIcon2 != null){
            g.drawImage(imageIcon2.getImage(), px, py, 96,96,null);
            
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            } // imageIcon2 != null
                } // face is not just a floor
            }
        }
 
                
        //    paintColoredSquare(g, DarknessColors.FOG_OF_WAR_COLOR, offsetX+x*tileSize+16, offsetY+y*tileSize+16);

        }  
      }    
    }
    
    private void redrawTopUnlessDirty3L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
         int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           

        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx-1, newy+2);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
     
            
     
    
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
            //    if (!foundSquare) {
              //      foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
              //  }
                int coversthings = face.getFaceQuad();
                if(coversthings != 0){
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
            if(imageIcon2 != null){
   
           g.drawImage(imageIcon2.getImage(), px+48, py+48, 96,96,null); 
           
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            } // imageIcon2 != null
                } // face is not just a floor
            }
        
        }
       }
    }
    
    private void redrawTopUnlessDirty3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
           
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           

        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx-1, newy+2);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
     
            for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     
    
            Face face = mapSquare.getFace(layer);
            if (face != null) {
            //    if (!foundSquare) {
              //      foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
              //  }
                int coversthings = face.getFaceQuad();
                if(coversthings != 0){
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
            if(imageIcon2 != null){
   
           g.drawImage(imageIcon2.getImage(), px+48, py+48, 96,96,null); 
           
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            } // imageIcon2 != null
                } // face is not just a floor
            }
        }
        }
       }
    }

private void redrawSquare2L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
       
   
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
        boolean foundSquare = false;
    
         
               
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    
      
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        
           // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
                if( !foundSquare) {
       //     paintSquareBackground(g, px, py, false, mapSquare);
                    if(floor == 3){
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                    }
       
        }
            if (mapSquare.isFogOfWar(3*floor)) {
    
            } else {
        
    
            }
            
        }
        
     
        
        }
     
    }


   //private 
    protected void redrawSquare2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
    /*    int newx = (x-y+17)/2;
        int newy = (x+y-3)/2;
     
          if (( (((x-y+17)%4) == 1) && ((((x+y-3)-2)%4) == 1) ) || (((((x-y+17)-2)%4) == 1) && (((x+y-3)%4) == 1))){ */
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
        boolean foundSquare = false;
     /*   int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();  */
           for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     
       /*      CfMapSquare headMapSquare = mapSquare.getHeadMapSquare(layer);
            if (headMapSquare != null) {
                Face headFace = headMapSquare.getFace(layer);
                assert headFace != null; // getHeadMapSquare() would have been cleared in this case
                int dx = headMapSquare.getX()-mapSquareX;
                int dy = headMapSquare.getY()-mapSquareY;
                assert dx > 0 || dy > 0;
                if (!foundSquare) {
                    foundSquare = true;
          //          paintSquareBackground(g, px, py, true, mapSquare);
                }
                
                ImageIcon imageIcon = facesProvider.getImageIcon(headFace.getFaceNum(), null);
        int sx = imageIcon.getIconWidth()-tileSize*dx;
         int sy = imageIcon.getIconHeight()-tileSize*dy;
       //     g.drawImage(imageIcon.getImage(), px+16, py+16, px+48, py+48, 16, 16, 48, 48, null);
       // use g.drawImage(imageIcon.getImage(), px, py, px+64, py+64, sx, sy, sx+128, sy+128, null);
     
               
            }  */
               
            Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    
      //   g.drawImage(imageIcon.getImage(), px, py, px+64, py+64, 0, 0, 128, 128, null);
       //     g.drawImage(imageIcon.getImage(),px,py,64,64,null);
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
           // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
                if( !foundSquare) {
       //     paintSquareBackground(g, px, py, false, mapSquare);
                    if(floor == 3){
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                    }
        /*        Polygon q = new Polygon();
         
                q.addPoint(px+32,py);
         q.addPoint(px+64,py+32);
         q.addPoint(px+32,py+64); 
         q.addPoint(px,py+32);
                g.drawPolygon(q);
            g.setColor(Color.BLACK);
            g.fillPolygon(q);*/
        }
            if (mapSquare.isFogOfWar(3*floor)) {
    //            ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
//  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
     //           ImageIcon imageIcon = facesProvider.getFogofwarImageIcon();
 // g.drawImage(imageIcon.getImage(),px,py,96,96,null);
         /*       Polygon q = new Polygon();
         q.addPoint(px+32,py);
         q.addPoint(px+64,py+32);
         q.addPoint(px+32,py+64); 
         q.addPoint(px,py+32);
                g.drawPolygon(q);
            g.setColor(DarknessColors.FOG_OF_WAR_COLOR);
            g.fillPolygon(q); */
                
            } else {
    /*    int darkness = mapSquare.getDarkness();
        if (darkness < 255) {
            if(darkness > 128){
                if(darkness > 192){
                    ImageIcon imageIcon = facesProvider.getDark1ImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                }
                else {
                    ImageIcon imageIcon = facesProvider.getDark2ImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                    
                } 
            } else {
              if(darkness > 64){
                ImageIcon imageIcon = facesProvider.getDark3ImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                
            }
            else {
                  ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                
            }  
            } */
                
            
            
            
      //     paintColoredSquare(g, DarknessColors.getDarknessColor(darkness), offsetX+x*tileSize+16, offsetY+y*tileSize+16);
     /*  Polygon r = new Polygon();
         r.addPoint(px+32,py);
         r.addPoint(px+64,py+32);
         r.addPoint(px+32,py+64); 
         r.addPoint(px,py+32);
       g.drawPolygon(r);
            g.setColor(DarknessColors.getDarknessColor(darkness));
            g.fillPolygon(r); */
      //  } 
            }
            
        }
        
     
        
        }
     
    }
    
    private void redrawSquare3L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor){
         int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
              
        CfMapSquare mapSquare = map.getMapSquare(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
        boolean foundSquare = false;
    
           
     
       
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
      
   
            g.drawImage(imageIcon.getImage(), px+48, py+48,96,96,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        
            // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
                if (!foundSquare){
                    if(floor == 3){
                     ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                    }
        
        }
                if (mapSquare.isFogOfWar(3*floor)) {
      
                
            } else {
  

                }
  
            
        }
        
        }
    }
    
    //private
    protected void redrawSquare3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
    /*    int newx = (x-y+17)/2;
        int newy = (x+y-3)/2;
     
          if (( (((x-y+17)%4) == 1) && ((((x+y-3)-2)%4) == 1) ) || (((((x-y+17)-2)%4) == 1) && (((x+y-3)%4) == 1))){ */
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
              
        CfMapSquare mapSquare = map.getMapSquare(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
        boolean foundSquare = false;
     /*   int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();  */
            for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     
        /*        CfMapSquare headMapSquare = mapSquare.getHeadMapSquare(layer);
            if (headMapSquare != null) {
                Face headFace = headMapSquare.getFace(layer);
                assert headFace != null; // getHeadMapSquare() would have been cleared in this case
                int dx = headMapSquare.getX()-mapSquareX;
                int dy = headMapSquare.getY()-mapSquareY;
                assert dx > 0 || dy > 0;
                if (!foundSquare) {
                    foundSquare = true;
            //        paintSquareBackground(g, px, py, true, mapSquare);
                }
                
                ImageIcon imageIcon = facesProvider.getImageIcon(headFace.getFaceNum(), null);
         int sx = imageIcon.getIconWidth()-tileSize*dx;
         int sy = imageIcon.getIconHeight()-tileSize*dy;
       //     g.drawImage(imageIcon.getImage(), px+16, py+16, px+48, py+48, 16, 16, 48, 48, null);
      // use   g.drawImage(imageIcon.getImage(), px+32, py+32, px+96, py+96, sx, sy, sx+128, sy+128, null);
               
            }  */
            Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
      
    //     g.drawImage(imageIcon.getImage(), px+32, py+32, px+96, py+96, 0, 0, 128, 128, null);
     //    g.drawImage(imageIcon.getImage(), px+32, py+32, 64, 64, null);
    //       g.drawImage(imageIcon.getImage(), px+32, py+32,null);
            g.drawImage(imageIcon.getImage(), px+48, py+48,96,96,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
            // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
                if (!foundSquare){
                    if(floor == 3){
                     ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                    }
           /*     Polygon q = new Polygon();
         q.addPoint(px+64,py+32);
            q.addPoint(px+96,py+64);
            q.addPoint(px+64,py+96);
            q.addPoint(px+32,py+64);
                
                g.drawPolygon(q);
            g.setColor(Color.BLACK);
            g.fillPolygon(q);*/
       //     paintSquareBackground(g, px, py, false, mapSquare);
        }
                if (mapSquare.isFogOfWar(3*floor)) {
         //           ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
 // g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
         //       ImageIcon imageIcon = facesProvider.getFogofwarImageIcon();
 // g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
         /*       Polygon q = new Polygon();
         q.addPoint(px+32,py);
         q.addPoint(px+64,py+32);
         q.addPoint(px+32,py+64); 
         q.addPoint(px,py+32);
                g.drawPolygon(q);
            g.setColor(DarknessColors.FOG_OF_WAR_COLOR);
            g.fillPolygon(q); */
                
            } else {
   /*     int darkness = mapSquare.getDarkness();
        if (darkness < 255) {
            if(darkness > 128){
                if(darkness > 192){
                    ImageIcon imageIcon = facesProvider.getDark1ImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                }
                else {
                    ImageIcon imageIcon = facesProvider.getDark2ImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                    
                } 
            } else {
              if(darkness > 64){
                ImageIcon imageIcon = facesProvider.getDark3ImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                
            }
            else {
                  ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+48,py+48,96,96,null);
                
            }  
            } */
                
            
            
            
      //     paintColoredSquare(g, DarknessColors.getDarknessColor(darkness), offsetX+x*tileSize+16, offsetY+y*tileSize+16);
     /*  Polygon r = new Polygon();
         r.addPoint(px+32,py);
         r.addPoint(px+64,py+32);
         r.addPoint(px+32,py+64); 
         r.addPoint(px,py+32);
       g.drawPolygon(r);
            g.setColor(DarknessColors.getDarknessColor(darkness));
            g.fillPolygon(r); */
     //   } 
                
                }
   /*         if (mapSquare.isFogOfWar()) {
      //      paintColoredSquare(g, DarknessColors.FOG_OF_WAR_COLOR, offsetX+x*tileSize+16, offsetY+y*tileSize+16);
                Polygon q = new Polygon();
                q.addPoint(px+64,py+32);
            q.addPoint(px+96,py+64);
            q.addPoint(px+64,py+96);
            q.addPoint(px+32,py+64);
         
                g.drawPolygon(q);
            g.setColor(DarknessColors.FOG_OF_WAR_COLOR);
            g.fillPolygon(q);
            }
        int darkness = mapSquare.getDarkness();
           
        if (darkness < CfMapSquare.DARKNESS_FULL_BRIGHT) {
    //       paintColoredSquare(g, DarknessColors.getDarknessColor(darkness), offsetX+x*tileSize+16, offsetY+y*tileSize+16);
       Polygon r = new Polygon();
         r.addPoint(px+64,py+32);
            r.addPoint(px+96,py+64);
            r.addPoint(px+64,py+96);
            r.addPoint(px+32,py+64);
       g.drawPolygon(r);
            g.setColor(DarknessColors.getDarknessColor(darkness));
            g.fillPolygon(r);
        } */
            
        }
        
        }
     
    }
    
    private void redrawFogOfWar2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
      
    /*    int newx = (x-y+17)/2;
        int newy = (x+y-3)/2;
     
          if (( (((x-y+17)%4) == 1) && ((((x+y-3)-2)%4) == 1) ) || (((((x-y+17)-2)%4) == 1) && (((x+y-3)%4) == 1))){ */
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
              
        CfMapSquare mapSquare = map.getMapSquare(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
        
                if (mapSquare.isFogOfWar(0)) {
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px-48,py,96,96,null);
         
                } 
   
            
            }
        
        }
     
    }
    
    private void redrawFogOfWar3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
      
    /*    int newx = (x-y+17)/2;
        int newy = (x+y-3)/2;
     
          if (( (((x-y+17)%4) == 1) && ((((x+y-3)-2)%4) == 1) ) || (((((x-y+17)-2)%4) == 1) && (((x+y-3)%4) == 1))){ */
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
              
        CfMapSquare mapSquare = map.getMapSquare(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
        
                if (mapSquare.isFogOfWar(0)) {
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py+48,96,96,null);
         
                } 
   
            
            }
        
        }
     
    }
    
    private void redrawTop2L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor){
          int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx-1, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
 //       boolean foundSquare = false;
   /*     int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();   */
           
     
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
   //             if (!foundSquare) {
     //               foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
       //         }
              int coversthings = face.getFaceQuad();
              if(coversthings != 0){
              
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
    if(imageIcon2 != null){
    //     g.drawImage(imageIcon2.getImage(), px, py, px+64, py+64, 0, 0, 128, 128, null);
 // g.drawImage(imageIcon2.getImage(), px, py, 64, 64, null);
            g.drawImage(imageIcon2.getImage(), px, py, 96,96,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
              } // imageIcon2 != null
            } // face is not just a floor
            }
          
        }
        }
    }
    
    private void redrawTop2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx-1, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
 //       boolean foundSquare = false;
   /*     int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();   */
           for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     /*
             CfMapSquare headMapSquare = mapSquare.getHeadMapSquare(layer);
            if (headMapSquare != null) {
                Face headFace = headMapSquare.getFace(layer);
                assert headFace != null; // getHeadMapSquare() would have been cleared in this case
                int dx = headMapSquare.getX()-mapSquareX;
                int dy = headMapSquare.getY()-mapSquareY;
                assert dx > 0 || dy > 0;
                if (!foundSquare) {
                    foundSquare = true;
          //          paintSquareBackground(g, px, py, true, mapSquare);
                }
                
                ImageIcon imageIcon2 = facesProvider.getImageIcon(headFace.getFaceNum()+10000, null);
        int sx = imageIcon2.getIconWidth()-tileSize*dx;
         int sy = imageIcon2.getIconHeight()-tileSize*dy;

            }  */
               
            Face face = mapSquare.getFace(layer);
            if (face != null) {
   //             if (!foundSquare) {
     //               foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
       //         }
              int coversthings = face.getFaceQuad();
              if(coversthings != 0){
              
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
    if(imageIcon2 != null){
    //     g.drawImage(imageIcon2.getImage(), px, py, px+64, py+64, 0, 0, 128, 128, null);
 // g.drawImage(imageIcon2.getImage(), px, py, 64, 64, null);
            g.drawImage(imageIcon2.getImage(), px, py, 96,96,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
              } // imageIcon2 != null
            } // face is not just a floor
            }
        }  
        }
        }
    }
    
    private void redrawTop3L0(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor){
         int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx-1, newy+2);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
    //    boolean foundSquare = false;
    /*    int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();    */
           
    
            Face face = mapSquare.getFace(3*floor);
            if (face != null) {
      //          if (!foundSquare) {
        //            foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
          //      }
                    int coversthings = face.getFaceQuad();
                    if(coversthings != 0){
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
     if(imageIcon2 != null){
    //     g.drawImage(imageIcon2.getImage(), px+32, py+32, px+96, py+96, 0, 0, 128, 128, null);
 //   g.drawImage(imageIcon2.getImage(), px+32, py+32, 64, 64, null);
//  g.drawImage(imageIcon2.getImage(), px+32, py+32, null);
     g.drawImage(imageIcon2.getImage(), px+48, py+48, 96,96,null);       
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
                    } // imageicon2 != null
                    } // face is not just a floor
            
            }
          
        }
        }
    }
    
    private void redrawTop3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
    
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx-1, newy+2);
        if (mapSquare != null) {
          int px = offsetX+x*48;
        int py = offsetY+y*48;
    //    boolean foundSquare = false;
    /*    int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();    */
            for (int layer = (3*floor)+1; layer < 3*(floor+1); layer++) {
     /*           CfMapSquare headMapSquare = mapSquare.getHeadMapSquare(layer);
            if (headMapSquare != null) {
                Face headFace = headMapSquare.getFace(layer);
                assert headFace != null; // getHeadMapSquare() would have been cleared in this case
                int dx = headMapSquare.getX()-mapSquareX;
                int dy = headMapSquare.getY()-mapSquareY;
                assert dx > 0 || dy > 0;
                if (!foundSquare) {
                    foundSquare = true;
            //        paintSquareBackground(g, px, py, true, mapSquare);
                }
                
                ImageIcon imageIcon2 = facesProvider.getImageIcon(headFace.getFaceNum()+10000, null);
         int sx = imageIcon2.getIconWidth()-tileSize*dx;
         int sy = imageIcon2.getIconHeight()-tileSize*dy;      
            }  */
            Face face = mapSquare.getFace(layer);
            if (face != null) {
      //          if (!foundSquare) {
        //            foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
          //      }
                    int coversthings = face.getFaceQuad();
                    if(coversthings != 0){
            ImageIcon imageIcon2 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
     if(imageIcon2 != null){
    //     g.drawImage(imageIcon2.getImage(), px+32, py+32, px+96, py+96, 0, 0, 128, 128, null);
 //   g.drawImage(imageIcon2.getImage(), px+32, py+32, 64, 64, null);
//  g.drawImage(imageIcon2.getImage(), px+32, py+32, null);
     g.drawImage(imageIcon2.getImage(), px+48, py+48, 96,96,null);       
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
                    } // imageicon2 != null
                    } // face is not just a floor
            
            }
        }  
        }
        }
    }

    /**
     * Redraws one square.
     * @param g the graphics to draw into
     * @param mapSquare the map square to draw
     * @param map the map
     * @param x the x-coordinate of the map tile to redraw
     * @param y the y-coordinate of the map tile to redraw
     */
    protected void redrawSquare(@NotNull final Graphics g, @NotNull final CfMapSquare mapSquare, @NotNull final CfMap map, final int x, final int y) {
        redrawSquare(g, x, y, mapSquare, map);
        if (x < 0 || y < 0 || x >= mapWidth || y >= mapHeight || mapSquare.isFogOfWar(0)) {
            paintColoredSquare(g, DarknessColors.FOG_OF_WAR_COLOR, offsetX+x*tileSize, offsetY+y*tileSize);
        //    paintColoredSquare(g,DarknessColors.FOG_OF_WAR_COLOR,offsetX+(x+y)*tileSize-7,offsetY+(y-x)*tileSize+10);
        }
        final int darkness = mapSquare.getDarkness(0);
        if (darkness < CfMapSquare.DARKNESS_FULL_BRIGHT) {
           paintColoredSquare(g, DarknessColors.getDarknessColor(darkness), offsetX+x*tileSize, offsetY+y*tileSize);
       //     paintColoredSquare(g,DarknessColors.getDarknessColor(darkness),offsetX+(x+y)*tileSize-7,offsetY+(y-x)*tileSize+10);
        }
    }

    /**
     * Redraws one layer of a square.
     * @param g the graphics to draw into
     * @param x the x coordinate of the square to redraw
     * @param y the y coordinate of the square to redraw
     * @param mapSquare the map square
     * @param map the map
     */
    private void redrawSquare(@NotNull final Graphics g, final int x, final int y, @NotNull final CfMapSquare mapSquare, @NotNull final CfMap map) {
        final int px = offsetX+x*tileSize;
       // final int px = offsetX + (x+y)*tileSize - 7;
        final int py = offsetY+y*tileSize;
       // final int py = offsetY + (y-x)*tileSize +10;
        final int mapSquareX = mapSquare.getX();
        final int mapSquareY = mapSquare.getY();
        boolean foundSquare = false;
        for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
          //  final CfMapSquare headMapSquare = mapSquare.getHeadMapSquare(layer);
            final CfMapSquare headMapSquare = mapSquare;
            if (headMapSquare != null) {
                final Face headFace = headMapSquare.getFace(layer);
                assert headFace != null; // getHeadMapSquare() would have been cleared in this case
                final int dx = headMapSquare.getX()-mapSquareX;
                final int dy = headMapSquare.getY()-mapSquareY;
                assert dx > 0 || dy > 0;
                if (!foundSquare) {
                    foundSquare = true;
                    paintSquareBackground(g, px, py, true, mapSquare);
                }
                paintImage(g, headFace, px, py, tileSize*dx, tileSize*dy);
               //   paintImage(g,headFace,offsetX+x*tileSize, offsetY+y*tileSize,tileSize*dx,tileSize*dy);
               // paintImage(g,headFace,px,py, tileSize*(dx+dy),tileSize*(dy-dx));
            }

            final Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
                    paintSquareBackground(g, px, py, true, mapSquare);
                }
                paintImage(g, face, px, py, 0, 0);
                if (smoothingRenderer != null) {
                    smoothingRenderer.paintSmooth(g, x, y, px, py, layer, map, tileSize);
                }
            }
        }
        if (!foundSquare) {
            paintSquareBackground(g, px, py, false, mapSquare);
        }
    }

    /**
     * Paints the background of a map square.
     * @param g the graphics to paint into
     * @param px the x-offset for painting
     * @param py the y-offset for painting
     * @param hasImage whether the square contains at least one image
     * @param mapSquare the map square
     */
    protected abstract void paintSquareBackground(@NotNull final Graphics g, final int px, final int py, final boolean hasImage, @NotNull final CfMapSquare mapSquare);

    /**
     * Paints a face into a tile.
     * @param g the graphics to draw into
     * @param face the face to draw
     * @param px the x coordinate of the square to redraw
     * @param py the y coordinate of the square to redraw
     * @param offsetX the x-offset for shifting the original face
     * @param offsetY the y-offset for shifting the original face
     */
    private void paintImage(@NotNull final Graphics g, @NotNull final Face face, final int px, final int py, final int offsetX, final int offsetY) {
        final ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        final int sx = imageIcon.getIconWidth()-offsetX;
       // final int sx = (imageIcon.getIconWidth()+imageIcon.getIconHeight())-offsetX -7;
         final int sy = imageIcon.getIconHeight()-offsetY;
      //  final int sy = (imageIcon.getIconHeight()-imageIcon.getIconWidth())-offsetY + 10;
        g.drawImage(imageIcon.getImage(), px, py, px+tileSize, py+tileSize, sx-tileSize, sy-tileSize, sx, sy, null);
    }

    /**
     * Paints the player location.
     * @param g the graphics to paint to
     * @param dx the x distance to map has just scrolled
     * @param dy the y distance to map has just scrolled
     */
    protected abstract void markPlayer(@NotNull final Graphics g, final int dx, final int dy);

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(@NotNull final Graphics g) {
        super.paintComponent(g);
        synchronized (bufferedImageSync) {
            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    /**
     * Sets the map size.
     * @param mapWidth the map width in squares
     * @param mapHeight the map height in squares
     */
    private void setMapSize(final int mapWidth, final int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        final int nX = MathUtils.divRoundUp(playerX, tileSize);
        displayMinOffsetX = playerX-nX*tileSize;
        assert -tileSize < displayMinOffsetX && displayMinOffsetX <= 0;
        assert (playerX-displayMinOffsetX)%tileSize == 0;
        displayMinX = (mapWidth-1)/2-nX;
        final int tilesX = MathUtils.divRoundUp(getWidth()-displayMinOffsetX, tileSize);
        displayMaxX = displayMinX+tilesX;
        assert (displayMaxX-displayMinX)*tileSize >= getWidth();
        assert (displayMaxX-displayMinX)*tileSize-getWidth() < 2*tileSize;
        displayMaxOffsetX = MathUtils.mod(-displayMinOffsetX-getWidth(), tileSize);
        offsetX = displayMinOffsetX-displayMinX*tileSize;

        final int nY = MathUtils.divRoundUp(playerY, tileSize);
        displayMinOffsetY = playerY-nY*tileSize;
        assert -tileSize < displayMinOffsetY && displayMinOffsetY <= 0;
        assert (playerY-displayMinOffsetY)%tileSize == 0;
        displayMinY = (mapHeight-1)/2-nY;
        final int tilesY = MathUtils.divRoundUp(getHeight()-displayMinOffsetY, tileSize);
        displayMaxY = displayMinY+tilesY;
        assert (displayMaxY-displayMinY)*tileSize >= getHeight();
        assert (displayMaxY-displayMinY)*tileSize-getHeight() < 2*tileSize;
        displayMaxOffsetY = MathUtils.mod(-displayMinOffsetY-getHeight(), tileSize);
        offsetY = displayMinOffsetY-displayMinY*tileSize;

        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        final GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        bufferedImage = graphicsConfiguration.createCompatibleImage(Math.max(1, getWidth()), Math.max(1, getHeight()), Transparency.OPAQUE);
        redrawAll();
    }
    
    // This method returns a buffered image with the contents of an image
   public static BufferedImage toBufferedImage(Image image) {
    if (image instanceof BufferedImage) {
        return (BufferedImage)image;
    }

    // This code ensures that all the pixels in the image are loaded
    image = new ImageIcon(image).getImage();

    // Determine if the image has transparent pixels; for this method's
    // implementation, see Determining If an Image Has Transparent Pixels
    boolean hasAlpha = hasAlpha(image);

    // Create a buffered image with a format that's compatible with the screen
    BufferedImage bimage = null;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try {
        // Determine the type of transparency of the new buffered image
        int transparency = Transparency.OPAQUE;
        if (hasAlpha) {
            transparency = Transparency.BITMASK;
        }

        // Create the buffered image
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage(
            image.getWidth(null), image.getHeight(null), transparency);
    } catch (HeadlessException e) {
        // The system does not have a screen
    }

    if (bimage == null) {
        // Create a buffered image using the default color model
        int type = BufferedImage.TYPE_INT_RGB;
        if (hasAlpha) {
            type = BufferedImage.TYPE_INT_ARGB;
        }
        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
    }

    // Copy image to buffered image
    Graphics g = bimage.createGraphics();

    // Paint the image onto the buffered image
    g.drawImage(image, 0, 0, null);
    g.dispose();

    return bimage;
}

    /**
     * Returns the x offset of the tile representing the player.
     * @return the x offset
     */
    public int getPlayerX() {
        return playerX;
    }

    /**
     * Returns the y offset of the tile representing the player.
     * @return the y offset
     */
    public int getPlayerY() {
        return playerY;
    }

    /**
     * Returns the x offset for drawing the square at coordinate 0 of the map.
     * @return the x offset
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Returns the y offset for drawing the square at coordinate 0 of the map.
     * @return the y offset
     */
    public int getOffsetY() {
        return offsetY;
    }
    
    public static boolean hasAlpha(Image image) {
    // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }

    // Use a pixel grabber to retrieve the image's color model;
    // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

    // Get the image's color model
        ColorModel cm = pg.getColorModel();
            return cm.hasAlpha();
    }
    
    public void handlesinglepixel(int x, int y, int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red   = (pixel >> 16) & 0xff;
        int green = (pixel >>  8) & 0xff;
        int blue  = (pixel      ) & 0xff;
        // Deal with the pixel as necessary...
        pixel = pixel & 0xff;
 }

 private BufferedImage handlepixels(Image img, int x, int y, int w, int h) {
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
        BufferedImage k = toBufferedImage(img);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!");
            return k;
        }
      //  if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
      //      System.err.println("image fetch aborted or errored");
      //      return;
      //  }
        
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
            //    handlesinglepixel(x+i, y+j, pixels[j * w + i]);
                int green = ( pixels[j * w + i] >> 8 ) & 0xff;
                int green2 = (green & 0x01) << 8;
                pixels[j * w + i] = pixels[j * w + i] | green2;
                
            }
        }
        // looks as though it is not writing back to the image
        
         WritableRaster l = k.getRaster();
      //   DataBuffer m = l.getDataBuffer();
     //    SampleModel n = l.getSampleModel();
      //   ColorModel p = k.getColorModel();
     //    int [] pixelset = new int [4096];
      //   int [] pixelset2 = null;            
    //     pixelset = n.getPixels(0,0,64,64,pixelset2,m);
    //     pixelset = l.getPixels(0,0,32,32,pixelset2);
   //      RedBlueGreenFilter colorfilter = new RedBlueGreenFilter();
     //    colorfilter.setPixels(0, 0, 64, 64, p, pixelset, 0, 64);
         /* I thought that with pointers, there would not be copying and hence no need to write back */
      //   n.setPixels(0,0,64,64,pixelset,m);
         
     //    l.setPixels(0,0,64,64,pixels);
         // trouble setting pixels
         k.setData(l);
         return k;
 }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        playerX = width/2-tileSize/2;
        playerY = height/2-tileSize/2;
        setMapSize(mapWidth, mapHeight);
        redrawAll();
    }

    /**
     * Updates the map display after the map contents have scrolled.
     * @param g the graphics to update
     * @param map the map to scroll; must hold the lock
     * @param dx the x-distance
     * @param dy the y-distance
     */
  //  private void updateScrolledMap(@NotNull final Graphics g, @NotNull final CfMap map) {
    protected void updateScrolledMap(@NotNull final Graphics g, @NotNull final CfMap map) {    
        while (true) {
            final Long scrollMap = scrollMapPending.pollFirst();
            if (scrollMap == null) {
                break;
            }
            final long tmp = scrollMap;
            final int zx = (int)(tmp>>32);
            final int zy = (int)tmp;
     
            int dx = zx + zy;
        int dy = zy - zx;
        
    //    if (Math.abs(dx)*48 >= getWidth() || Math.abs(dy)*48 >= getHeight()) {
       if((dx != 0) || (dy != 0)){     
            redrawAllUnlessDirty(g, map);
    //       redrawAll();
       
         } 
    /*   else {
            final int x = dx > 0 ? dx : 0;
            final int w = dx > 0 ? -dx : dx;
            final int y = dy > 0 ? dy : 0;
            final int h = dy > 0 ? -dy : dy;
            g.copyArea(x*48, y*48, getWidth()+w*48, getHeight()+h*48, -dx*48, -dy*48);
// part of yy and ys  offsets should be defined by if the screen width is even or odd?
            if (dx > 0) {
                final int ww = (displayMaxOffsetX == 0 ? 0 : 1)+dx;
                for(int yy = displayMinY; yy < displayMaxY+4; yy++){
                    for(int xx = displayMaxX-2*ww; xx < 2*displayMaxX; xx++){
                  //      map.dirty(xx,yy);
                        redrawSquareUnlessDirty2(g,map,xx,yy);
                    } 
                    for(int xx = displayMaxX-2*ww; xx < 2*displayMaxX; xx++){
                       
                        redrawSquareUnlessDirty3(g,map,xx,yy);
                    }   
                } 
                for(int yy = displayMinY; yy < displayMaxY; yy++){
                    for(int xx = displayMaxX-2*ww; xx < 2*displayMaxX; xx++){
                        redrawTopUnlessDirty2(g,map,xx,yy);
                    }
                    for(int xx = displayMaxX-2*ww; xx < 2*displayMaxX; xx++){
                        redrawTopUnlessDirty3(g,map,xx,yy);
                    }
                }   
            } else if (dx < 0) {
                final int ww = (displayMinOffsetX == 0 ? 0 : 1)-dx;
                for(int yy = displayMinY; yy < displayMaxY+4; yy++){
                    for(int xx = displayMinX; xx < displayMinX+2*ww; xx++){
                        redrawSquareUnlessDirty2(g,map,xx,yy);
                   //     map.dirty(xx,yy);
                    }
                    for(int xx = displayMinX; xx < displayMinX+2*ww; xx++){
                        redrawSquareUnlessDirty3(g,map,xx,yy);
                    }  
                }
                 for(int yy = displayMinY; yy < displayMaxY; yy++){
                    for(int xx = displayMinX; xx < displayMinX+2*ww; xx++){
                        redrawTopUnlessDirty2(g,map,xx,yy);
                    }
                    for(int xx = displayMinX; xx < displayMinX+2*ww; xx++){
                        redrawTopUnlessDirty3(g,map,xx,yy);
                    }
                }  
            }
            if (dy > 0) {
                final int hs = (displayMaxOffsetY == 0 ? 0 : 1)+dy;
                for(int ys = displayMaxY-2*hs; ys < displayMaxY+4; ys++){
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                   //     map.dirty(xs, ys);
                        redrawSquareUnlessDirty2(g,map,xs,ys);
                    }
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                        redrawSquareUnlessDirty3(g,map,xs,ys);
                    } 
                }
                for(int ys = displayMaxY-2*hs; ys < displayMaxY; ys++){
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                        redrawTopUnlessDirty2(g,map,xs,ys);
                    }
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                        redrawTopUnlessDirty3(g,map,xs,ys);
                    }
                }  
            } else if (dy < 0) {
                final int hs = (displayMinOffsetY == 0 ? 0 : 1)-dy;
                for(int ys = displayMinY; ys < displayMinY+2*hs; ys++){
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                   //     map.dirty(xs,ys);
                        redrawSquareUnlessDirty2(g,map,xs,ys);
                    }
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                        redrawSquareUnlessDirty3(g,map,xs,ys);
                    } 
                }
                for(int ys = displayMinY; ys < displayMinY+2*hs; ys++){
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                        redrawTopUnlessDirty2(g,map,xs,ys);
                    }
                    for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                        redrawTopUnlessDirty3(g,map,xs,ys);
                    }
                }  
                
            } 
        
        } */ 
            markPlayer(g, dx, dy);
            
        }  
     /*   
        for(int ys = displayMinY; ys < displayMaxY; ys++){
            for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                redrawFogOfWar2(g,map,xs,ys);
            }
            for(int xs = displayMinX; xs < 2*displayMaxX; xs++){
                redrawFogOfWar3(g,map,xs,ys);
            }
            // may need to redraw the tile north of that to eliminate
            // the top half of the square; unfortunately other stuff in the code
            // may not support this?
            // perhaps, redraw tops of tiles that are at x+1,y-1, and also
            // redraw any tops of tiles that cover the blocked tile we are now drawing
        }  */
        
        
        
      /*  if (Math.abs(dx)*tileSize >= getWidth() || Math.abs(dy)*tileSize >= getHeight()) { */
        
       //     redrawAllUnlessDirty(g, map);
      /*  } else {
            final int x = zx > 0 ? zx : 0;
            final int w = zx > 0 ? -zx : zx;
            final int y = zy > 0 ? zy : 0;
            final int h = zy > 0 ? -zy : zy;
            g.copyArea(x*tileSize, y*tileSize, getWidth()+w*tileSize, getHeight()+h*tileSize, -(zx+zy)*tileSize, -(zy-zx)*tileSize); */
          //  g.copyArea(x*tileSize,y*tileSize,getWidth()+w*tileSize,getHeight()+h*tileSize,-dx*tileSize,-dy*tileSize);
          //  g.copyArea((x+y)*tileSize-7,(y-x)*tileSize+10,getWidth()+(w+h)*tileSize,getHeight()+(h-w)*tileSize,-(dx+dy)*tileSize,(dx-dy)*tileSize);

       /*     if (zx > 0) {
                final int ww = (displayMaxOffsetX == 0 ? 0 : 1)+zx;
                final int hh = (displayMaxOffsetY == 0 ? 0 : 1)+zx; */ /* orig not */
            //    final int ww = (displayMaxOffsetX == 0 ? 0 : 1)+(dx+dy);
            // orig    redrawTilesUnlessDirty(g, map, displayMaxX-ww, displayMinY, displayMaxX, displayMaxY);
       /*         redrawTilesUnlessDirty(g, map, displayMaxX-ww, displayMaxY-hh, displayMaxX, displayMaxY); */
                
       //      final int ww = (displayMinOffsetX == 0 ? 0 : 1)-dx;   
       //     redrawTilesUnlessDirty(g, map, displayMinX, displayMinY, displayMinX+ww, displayMaxY);    
     /*       } else if (zx < 0) {
                final int ww = (displayMinOffsetX == 0 ? 0 : 1)-zx;
                final int hh = (displayMinOffsetY == 0 ? 0 : 1)-zy; *//*orig not */
          // orig      redrawTilesUnlessDirty(g, map, displayMinX, displayMinY, displayMinX+ww, displayMaxY);
       /*         redrawTilesUnlessDirty(g, map, displayMinX, displayMinY, displayMinX+ww, displayMinY+hh);  */
           //     final int ww = (displayMaxOffsetX == 0 ? 0 : 1)+dx;
          //     redrawTilesUnlessDirty(g, map, displayMaxX-ww, displayMinY, displayMaxX, displayMaxY); 
       /*     }
            if (zy > 0) {
                final int hh = (displayMaxOffsetY == 0 ? 0 : 1)+zy;
                redrawTilesUnlessDirty(g, map, displayMinX, displayMaxY-hh, displayMaxX, displayMaxY);
            } else if (zy < 0) {
                final int hh = (displayMinOffsetY == 0 ? 0 : 1)-zy;
                redrawTilesUnlessDirty(g, map, displayMinX, displayMinY, displayMaxX, displayMinY+hh);
            }
        } */
    }

    /**
     * Fills a square with one {@link Color}.
     * @param g the graphics to paint into
     * @param color the color
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    protected void paintColoredSquare(@NotNull final Graphics g, @NotNull final Color color, final int x, final int y) {
        Image image = images.get(color);
        if (image == null) {
            final BufferedImage tmp = new BufferedImage(tileSize, tileSize, color.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
            final Graphics g2 = tmp.createGraphics();
            try {
                g2.setColor(color);
                g2.fillRect(0, 0, tileSize, tileSize);
            } finally {
                g2.dispose();
            }
            image = tmp;
            images.put(color, image);
        }
        g.drawImage(image, x, y, tileSize, tileSize, null);
    }

    /**
     * Returns the map width in squares.
     * @return the map width
     */
    protected int getMapWidth() {
        return mapWidth;
    }

    /**
     * Returns the map height in squares.
     * @return the map height
     */
    protected int getMapHeight() {
        return mapHeight;
    }

    /**
     * Returns a {@link Graphics} instance for painting into {@link
     * #bufferedImage}. The returned value must be freed by calling {@link
     * Graphics#dispose()} on the returned instance.
     * @param map the map instance that is painted; must hold the lock
     * @return the graphics instance
     */
    @NotNull
  //  private Graphics2D createBufferGraphics(@NotNull final CfMap map) {
     protected Graphics2D createBufferGraphics(@NotNull final CfMap map) {   
        assert Thread.holdsLock(bufferedImageSync);
        assert bufferedImage != null;
        final Graphics2D graphics = bufferedImage.createGraphics();
        if (clearMapPending) {
            clearMapPending = false;
            clearMap(graphics);
        }
        
        return graphics;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(tileSize, tileSize);
    }

}

