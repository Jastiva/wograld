/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
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

package com.realtime.wograld.jxclient.gui.map;

import com.realtime.wograld.jxclient.faces.FacesProvider;
import com.realtime.wograld.jxclient.gui.gui.GUIElementListener;
import com.realtime.wograld.jxclient.gui.gui.TooltipManager;
import com.realtime.wograld.jxclient.map.CfMap;
import com.realtime.wograld.jxclient.map.CfMapSquare;
import com.realtime.wograld.jxclient.mapupdater.MapUpdaterState;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.realtime.wograld.jxclient.faces.Face;
import com.realtime.wograld.jxclient.gui.gui.AbstractGUIElement;
import com.realtime.wograld.jxclient.gui.gui.GUIElement;
import com.realtime.wograld.jxclient.gui.gui.TooltipManager;
import com.realtime.wograld.jxclient.mapupdater.MapListener;
import com.realtime.wograld.jxclient.mapupdater.MapScrollListener;
import com.realtime.wograld.jxclient.mapupdater.NewmapListener;
import com.realtime.wograld.jxclient.server.wograld.MapSizeListener;
import com.realtime.wograld.jxclient.server.wograld.messages.Map2;
import com.realtime.wograld.jxclient.util.MathUtils;
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

/**
 * Displays a small map view.
 * @author Lauwenmark
 * @author Andreas Kirschbaum
 */
public class GUIMiniMap extends AbstractGUIMap {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * The {@link MapUpdaterState} instance to use.
     */
    @NotNull
    private final MapUpdaterState mapUpdaterState;

    /**
     * The map width in squares.
     */
    private final int width;

    /**
     * The map height in squares.
     */
    private final int height;

    /**
     * The size of one tile.
     */
    private final int tileSize;

    /**
     * The colors for displaying magic map data.
     */
    @NotNull
    private static final Color[] TILE_COLORS = {
        Color.BLACK,
        Color.WHITE,
        Color.BLUE,
        Color.RED,
        Color.GREEN,
        Color.YELLOW,
        Color.PINK,
        Color.GRAY,
        Color.ORANGE,
        Color.CYAN,
        Color.MAGENTA,
        Color.DARK_GRAY,
        Color.DARK_GRAY,
        Color.DARK_GRAY,
        Color.DARK_GRAY,
        Color.DARK_GRAY,
    };
    
    @NotNull
    private final MapListener mapListener = new MapListener() {

        @Override
        public void mapChanged(@NotNull final CfMap map, @NotNull final Set<CfMapSquare> changedSquares) {
            assert Thread.holdsLock(map);
            synchronized (bufferedImageSync) {
                final int x0 = map.getOffsetX();
                final int y0 = map.getOffsetY();  
                final Graphics2D g = createBufferGraphics(map);
               updateScrolledMap( g, map);
          //     Set<CfMapSquare> squares2 = map.getDirtyMapSquares();
            //   squares2.addAll(changedSquares);
        //   redrawAllUnlessDirty(g, map);
               
               
                try {
                 
                       for(CfMapSquare mapSquare8: changedSquares){
                  //  for(CfMapSquare mapSquare8: squares2){
                          int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*4;
                            int py = offsetY+(screen_y)*4;
                   
                                                             CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare9 != null) {
                                     
                                            boolean foundSquare = false;
     
                                            for (int layer = 0; layer < 3; layer++) {
                                                Face face = mapSquare9.getFace(layer);
                                                if (face != null) {
                                                    if (!foundSquare) {
                                                        foundSquare = true;
               //     paintSquareBackground(g, px, py, true, mapSquare);
                                                    }
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,8,8,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
			
                                                }
                                            }
                                            if( !foundSquare) {
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py,8,8,null);
                                         
                                                
                                            }
                                        }
                                        
            
            		mapSquare8.recentDraw();
                                
                      }
for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.finishDraw();
}                  
           
                      
            
                    markPlayer(g, 0, 0);
                   
                } finally {
                    g.dispose();
                }
            }
            setChanged();
        }

    };

    /**
     * Creates a new instance.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param mapUpdaterState the map updater state instance to use
     * @param facesProvider the faces provider for looking up faces
     * @param width the map width in squares
     * @param height the map height in squares
     */
    public GUIMiniMap(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, @NotNull final MapUpdaterState mapUpdaterState, @NotNull final FacesProvider facesProvider, final int width, final int height) {
        super(tooltipManager, elementListener, name, mapUpdaterState, facesProvider, null);
        this.mapUpdaterState = mapUpdaterState;
        this.mapUpdaterState.addWograldMapListener(mapListener);
        this.width = width;
        this.height = height;
        tileSize = facesProvider.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintSquareBackground(@NotNull final Graphics g, final int px, final int py, final boolean hasImage, @NotNull final CfMapSquare mapSquare) {
        final Color color;
        if (hasImage) {
            color = Color.BLACK;
        } else {
            final int colorIndex = mapSquare.getColor();
            color = 0 <= colorIndex && colorIndex < TILE_COLORS.length ? TILE_COLORS[colorIndex] : Color.BLACK;
        }
        paintColoredSquare(g, color, px, py);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        mapUpdaterState.removeWograldMapListener(mapListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void markPlayer(@NotNull final Graphics g, final int dx, final int dy) {
        if (dx != 0 || dy != 0) {
            final int playerOffsetX = (getMapWidth()-1)/2;
            final int playerOffsetY = (getMapHeight()-1)/2;
            final int mapSquareX = playerOffsetX-dx;
            final int mapSquareY = playerOffsetY-dy;
            final CfMap map = mapUpdaterState.getMap();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (map) {
                //redrawSquare(g,map.getMapSquare(mapSquareX, mapSquareY), map, mapSquareX, mapSquareY)
                redrawSquare2(g,  map, mapSquareX, mapSquareY,0);
            }
        }
        g.setColor(Color.RED);
        g.fillRect(getPlayerX(), getPlayerY(), tileSize, tileSize);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width*tileSize, height*tileSize);
    }
    
    @Override
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
       
         } 
   
            markPlayer(g, dx, dy);
            
        }  
     
    }
    
    @Override
    protected void redrawAllUnlessDirty(@NotNull final Graphics g, @NotNull final CfMap map) {
        redrawTilesUnlessDirty2(g, map, displayMinX-offsetX/tileSize, displayMinY-offsetY/tileSize, displayMaxX-offsetX/tileSize, displayMaxY-offsetY/tileSize);
    }
    
    @Override
     protected void redrawTilesUnlessDirty2(@NotNull final Graphics g, @NotNull final CfMap map, final int x0, final int y0, final int x1, final int y1) {
      
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty2(g, map, x, y);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquareUnlessDirty3(g, map, x, y);
            }
        }
        
        
        
        
    }
    
    // @Override
private void redrawSquareUnlessDirty2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
     
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
   
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*4;
        int py = offsetY+y*4;
     
        boolean foundSquare = false;
            for (int layer = 0; layer < 3; layer++) {
     
            Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
      
       
      g.drawImage(imageIcon.getImage(), px, py, 8,8,null); 
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
          //  if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
          if (!foundSquare){
              
               ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,8,8,null);
       
        //    paintSquareBackground(g, px, py, false, mapSquare);
        }
   
          if (mapSquare.isFogOfWar(0)) {
  
                
            } else {
  
          }
            
        } 
 
       }  
        
    }

 // @Override    
 private void redrawSquareUnlessDirty3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
      
        
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
     
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
               
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*4;
        int py = offsetY+y*4;
    
        boolean foundSquare = false;
            for (int layer = 0; layer < 3; layer++) {
     
         
            Face face = mapSquare.getFace(layer);
            if (face != null) {
                if (!foundSquare) {
                    foundSquare = true;
             //       paintSquareBackground(g, px, py, true, mapSquare);
                }
            ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
            
   
    
        g.drawImage(imageIcon.getImage(), px+4, py+4, 8,8,null);    
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
           // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
               if(!foundSquare){
                   
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+4,py+4,8,8,null);
       
       //     paintSquareBackground(g, px, py, false, mapSquare);
        }
   
               if (mapSquare.isFogOfWar(0)) {
       
                
            } else {
  
               }
            
        }
        
        
        
       }
        
    
          
        
    }
    
/*
 // @Override
    private void redrawTopUnlessDirty2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
      
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
     
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx-1, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*4;
        int py = offsetY+y*4;
    
            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
      
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
            g.drawImage(imageIcon2.getImage(), px, py, 8,8,null);
            
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
    

 // @Override
    private void redrawTopUnlessDirty3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y) {
           
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           

        CfMapSquare mapSquare = map.getMapSquareUnlessDirty(newx-1, newy+2);
        if (mapSquare != null) {
          int px = offsetX+x*4;
        int py = offsetY+y*4;
     
            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
     
    
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
   
           g.drawImage(imageIcon2.getImage(), px+4, py+4, 8,8,null); 
           
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            } // imageIcon2 != null
                } // face is not just a floor
            }
        }
        }
       }
    } */
 
 @Override
 protected void redrawSquare2(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
    /*    int newx = (x-y+17)/2;
        int newy = (x+y-3)/2;
     
          if (( (((x-y+17)%4) == 1) && ((((x+y-3)-2)%4) == 1) ) || (((((x-y+17)-2)%4) == 1) && (((x+y-3)%4) == 1))){ */
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
           
        CfMapSquare mapSquare = map.getMapSquare(newx, newy);
        if (mapSquare != null) {
          int px = offsetX+x*4;
        int py = offsetY+y*4;
        boolean foundSquare = false;
     /*   int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();  */
           for (int layer = 0; layer < 3; layer++) {
     
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
  g.drawImage(imageIcon.getImage(),px,py,8,8,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
           // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
                if( !foundSquare) {
       //     paintSquareBackground(g, px, py, false, mapSquare);
                    ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px,py,8,8,null);
        /*        Polygon q = new Polygon();
         
                q.addPoint(px+32,py);
         q.addPoint(px+64,py+32);
         q.addPoint(px+32,py+64); 
         q.addPoint(px,py+32);
                g.drawPolygon(q);
            g.setColor(Color.BLACK);
            g.fillPolygon(q);*/
        }
            if (mapSquare.isFogOfWar(0)) {
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
 
 @Override
  protected void redrawSquare3(@NotNull final Graphics g, @NotNull final CfMap map, final int x, final int y, final int floor) {
      
    /*    int newx = (x-y+17)/2;
        int newy = (x+y-3)/2;
     
          if (( (((x-y+17)%4) == 1) && ((((x+y-3)-2)%4) == 1) ) || (((((x-y+17)-2)%4) == 1) && (((x+y-3)%4) == 1))){ */
        int newx = (x-y+18)/2;
        int newy = (x+y+2)/2;
        if (( (((x-y+18)%4) == 1) && ((((x+y+2)-2)%4) == 1) ) || (((((x-y+18)-2)%4) == 1) && (((x+y+2)%4) == 1))){
              
        CfMapSquare mapSquare = map.getMapSquare(newx, newy+1);
        if (mapSquare != null) {
          int px = offsetX+x*4;
        int py = offsetY+y*4;
        boolean foundSquare = false;
     /*   int mapSquareX = mapSquare.getX();
        int mapSquareY = mapSquare.getY();  */
            for (int layer = 0; layer < 3; layer++) {
     
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
            g.drawImage(imageIcon.getImage(), px+4, py+4,8,8,null);
             if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                }
            }
        }
            // if (!foundSquare || x < 0 || y < 0 || x >= mapWidth || y >= mapHeight) {
                if (!foundSquare){
                     ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
  g.drawImage(imageIcon.getImage(),px+4,py+4,8,8,null);
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
                if (mapSquare.isFogOfWar(0)) {
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
 
 @Override
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
        for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare2(g, map, x, y,0);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawSquare3(g, map, x, y,0);
            }
        }
       /* for (int y = y0-8; y < y1; y++) {
            for (int x = x0; x < 2*x1; x++) {
                redrawTop2(g, map, x, y);
            }
            for (int x = x0; x < 2*x1; x++) {
                redrawTop3(g, map, x, y);
            }
        } */  
    }

}
