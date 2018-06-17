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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import net.sf.gridarta.gui.map.renderer.AbstractMapRenderer;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.crossfire.IGUIConstants;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Renders maps without MapGrid.
 * @author Andreas Kirschbaum
 * @author serpentshard
 */
public class SimpleFlatMapRenderer extends AbstractMapRenderer<GameObject, MapArchObject, Archetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map model to render.
     * @serial
     */
    @NotNull
    private final MapModel<GameObject, MapArchObject, Archetype> mapModel;

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * Temporary variable.
     * @serial
     */
    @NotNull
    private final Point offset = new Point();

    /**
     * The {@link SmoothingRenderer} for rendering smoothed faces.
     */
    @NotNull
    private final SmoothingRenderer smoothingRenderer;

    /**
     * Creates a new instance.
     * @param mapModel the map model to render
     * @param systemIcons the system icons for creating icons
     * @param smoothingRenderer the smoothing renderer to use
     */
    public SimpleFlatMapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final SystemIcons systemIcons, @NotNull final SmoothingRenderer smoothingRenderer) {
        super(mapModel, null);
        this.mapModel = mapModel;
        this.systemIcons = systemIcons;
        this.smoothingRenderer = smoothingRenderer;
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
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
     //   final int viewWidth = mapSize.getWidth() * IGUIConstants.SQUARE_WIDTH;
    //    final int viewWidth = mapSize.getWidth() * 64;
     //   final int viewHeight = mapSize.getHeight() * IGUIConstants.SQUARE_HEIGHT;
     //   final int viewHeight = mapSize.getHeight() * 64;
           final int viewWidth = (mapSize.getWidth()+ mapSize.getHeight() )* 32;
            final int viewHeight = (mapSize.getWidth() + mapSize.getHeight()) * 32;
        final BufferedImage image = new BufferedImage(viewWidth, viewHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = image.getGraphics();
        try {
            paintComponent(g);
        } finally {
            g.dispose();
        }
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
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        g.setColor(Color.white);
      //  g.fillRect(0, 0, mapSize.getWidth() * IGUIConstants.SQUARE_WIDTH, mapSize.getHeight() * IGUIConstants.SQUARE_HEIGHT);
        g.fillRect(0, 0, mapSize.getWidth() * 64, mapSize.getHeight() * 64);
        final Point point = new Point();
        for (point.y = 0; point.y < mapSize.getHeight(); point.y++) {
            for (point.x = 0; point.x < mapSize.getWidth(); point.x++) {
                int x2 = point.x * 32;
                int y2 = point.y * 32;
                paintSquare(g, point, x2,y2,mapSize.getWidth(), mapSize.getHeight());
            }
        }
    }

    /**
     * Paint one square.
     * @param graphics the graphics context to draw to
     * @param point the map coordinates of the square to draw
     */
    private void paintSquare(@NotNull final Graphics graphics, @NotNull final Point point, int x, int y, int mapwidth, int mapheight) {
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
       
      //  if (mapModel.getMapSquare(point).isEmpty()) {
       if (midsquare.isEmpty()) {
         //   systemIcons.getEmptySquareIcon().paintIcon(this, graphics, point.x * 64, point.y * 64);
         //   systemIcons.getEmptySquareIcon().paintIcon(this, graphics, (point.x + point.y - 9) * 32, (point.y - point.x + 8) * 32);
            systemIcons.getEmptySquareIcon().paintIcon(this, graphics, (point.x + point.y ) * 32, (point.y - point.x +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32);
            return;
        } else {

       
     //   graphics.fillRect((point.x+point.y)*32, (point.y-point.x-2+this.mapModel.getMapArchObject().getMapSize().getWidth())*32, 64, 128);
        //
        if((ty > 1)&&(tx < mapwidth-1)){
                
                   if(!TopLeftsquare.isEmpty()){
                       for (final GameObject node : TopLeftsquare) {
                  
        
                   // if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                  //    if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {  
                         //  final ImageIcon img = node.getImage(mapViewSettings);
                            final ImageIcon img = node.getNormalImage();
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }

    graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x -2 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +1 )* 32, (point.y - point.x-1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x+32, offset.y+32, offset.x + 64, offset.y + 64, this);    
      //   graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (point.x+point.y) + 32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
     //         graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);               
           if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }
                        //    }
                        }
                   }
                }
        //
         if((ty > 0)&&(tx < mapwidth-2)){
                     if(!TopRightsquare.isEmpty()){
                       for (final GameObject node : TopRightsquare) {
                
        
                  // if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      //  if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {  
                         //  final ImageIcon img = node.getImage(mapViewSettings);
                        final ImageIcon img = node.getNormalImage();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
            graphics.drawImage(img.getImage(), (point.x + point.y +1) * 32, (point.y - point.x -2 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2 )* 32, (point.y - point.x-1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x, offset.y+32, offset.x + 32, offset.y + 64, this);    
      //   g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
      //   g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);     
  //   graphics.drawImage(img.getImage(), (point.x+point.y)+32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);
   //  graphics.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);
           if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }
         
                         //   }
                        }
                   }
                }
         //
          if((ty > 0)&&(tx < mapwidth-1)){
                    if(!Northeastsquare.isEmpty()){
                        for (final GameObject node : Northeastsquare) {
                  //  filterControl.objectInSquare(filterState, node);
                   //   if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {  
                         //  final ImageIcon img = node.getImage(mapViewSettings);
                        final ImageIcon img = node.getNormalImage();
                  //  if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                         //  final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
              graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x -2 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2 )* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);    
      //   g.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
      //   g.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);     
     //    graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this); 
      //    graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this); 
          if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }
                       //     }
                        }
                    }
                 }
          //
           if(ty > 0){
                if(!Northsquare.isEmpty()){
                for (final GameObject node : Northsquare) {
                  //  filterControl.objectInSquare(filterState, node);
                    // if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {  
                         //  final ImageIcon img = node.getImage(mapViewSettings);
                        final ImageIcon img = node.getNormalImage();
                   // if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                       //    final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
    graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x -1 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +1 )* 32, (point.y - point.x+1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x+32, offset.y, offset.x + 64, offset.y + 64, this);    
     //    graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (point.x+point.y) + 32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);
     //    graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);
                      if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }
                        
                       //     }
                }
            }
                }
           //
             if(tx < mapwidth-1){
               if(!Eastsquare.isEmpty()){
                for (final GameObject node : Eastsquare) {
               //     filterControl.objectInSquare(filterState, node);
         //   if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {  
                         //  final ImageIcon img = node.getImage(mapViewSettings);
                        final ImageIcon img = node.getNormalImage();
                //    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                    //    final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
      // graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
           graphics.drawImage(img.getImage(), (point.x + point.y +1 ) * 32, (point.y - point.x -1 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x, offset.y, offset.x + 32, offset.y + 64, this);                 
        // graphics.drawImage(img.getImage(), (point.x+point.y)+32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);
    //   graphics.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);                     
                          if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }
                  //  }
                }
                } 
                      }
             //
              for (final GameObject node : midsquare) {
                //    filterControl.objectInSquare(filterState, node);
                 //    if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
               //   if (node.getAttributeInt(GameObject.INVISIBLE, true) == 0) {
                      // nothing
                //  } else {
                 
                   // if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        // let this method adjust coord
                        
                      //  paintGameObject(g, x, y, node);
                         //  final ImageIcon img = node.getImage(mapViewSettings);
                           final ImageIcon img = node.getNormalImage();
         if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
            offset.x = 0;
            offset.y = 0;
           
        } else {
            
            offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
           // offset.y = IGUIConstants.SQUARE_HEIGHT * (node.getArchetype().getMultiY() - node.getMinY());
            offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
           
        }
    
       
       //  graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+64, offset.y+64, this);
          graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
         
       // if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
       //     }
                //    }
                }
              //
                 if(tx > 0){
                if(!Westsquare.isEmpty()){
                for (final GameObject node : Westsquare) {
                 //   filterControl.objectInSquare(filterState, node);
                 //     if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) { 
                  //  if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                       
                    //    final ImageIcon img = node.getImage(mapViewSettings);
                    final ImageIcon img = node.getNormalImage();
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
       graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +1+this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +1)* 32, (point.y - point.x+2+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x+32, offset.y, offset.x + 64, offset.y + 32, this);
// graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);
    //     graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (point.x+point.y) + 32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);
                      if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }   
                        
                  //  }
                }
                }
                     }
                 
                 //
                    if(ty < mapheight-1){
               if(!Southsquare.isEmpty()){
                for (final GameObject node : Southsquare) {
                 //   filterControl.objectInSquare(filterState, node);
                    //  if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) { 
                   // if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        final ImageIcon img = node.getNormalImage();
                        //  final ImageIcon img = node.getImage(mapViewSettings);
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
          graphics.drawImage(img.getImage(), (point.x + point.y +1 ) * 32, (point.y - point.x +1+this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+2+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 32, offset.y + 32, this);
// graphics.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);
     //    graphics.drawImage(img.getImage(), (point.x+point.y)+32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)+32, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);
                       if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }     
                        
                //   }
                }
                } 
                }
                    
                    //
                    if(ty > 0){
                if(!Northsquare.isEmpty()){
                for (final GameObject node : Northsquare) {
                 //   filterControl.objectInSquare(filterState, node);
               //     if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) { 
                  //  if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
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
 graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x -2 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +1 )* 32, (point.y - point.x-1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x+32, offset.y+32, offset.x + 64, offset.y + 64, this);    
     //    graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (point.x+point.y) + 32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) -32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
   //       graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) -32, offset.x+32, offset.y+32, offset.x+64, offset.y+64, this);
                        if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }     
                        
                         //   }
                }
            }
                }
         //
                       if(tx < mapwidth-1){
               if(!Eastsquare.isEmpty()){
                for (final GameObject node : Eastsquare) {
                 //   filterControl.objectInSquare(filterState, node);
               //      if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) { 
                //    if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                      
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
              graphics.drawImage(img.getImage(), (point.x + point.y +1) * 32, (point.y - point.x -2 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2 )* 32, (point.y - point.x-1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x, offset.y+32, offset.x + 32, offset.y + 64, this);    
      //   graphics.drawImage(img.getImage(), (point.x+point.y)+32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) - 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);
   //       graphics.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) - 32, offset.x, offset.y+32, offset.x+32, offset.y+64, this);
                         if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }     
                 //   }
                }
                } 
                      }
                       //
                        for (final GameObject node : midsquare) {
                 //   filterControl.objectInSquare(filterState, node);
                   //   if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
                 //   if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                        
                        final ImageIcon img = node.getSecondImg();
       
                            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {   
                                offset.x = 0;
                                offset.y = 0;
            
                            } else {
        
                                offset.x = 64*(node.getArchetype().getMultiX() - node.getMinX());
                                offset.y = 64*(node.getArchetype().getMultiY() - node.getMinY());
                                // keep these in rectangular rather than diagonal, until operation below
                            }
              graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x -2 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2 )* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);    
      //   graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this);
        //  graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-64, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), offset.x, offset.y, offset.x+64, offset.y+64, this);
  //    graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
                        if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }     
                        
                  //  }
                }
                        //
                 if(tx > 0){
                if(!Westsquare.isEmpty()){
                for (final GameObject node : Westsquare) {
                 //   filterControl.objectInSquare(filterState, node);
                 //    if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
                 //   if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                       
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
            graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x -1 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +1 )* 32, (point.y - point.x+1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x+32, offset.y, offset.x + 64, offset.y + 64, this);    
      //   graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (point.x+point.y) + 32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);
    //        graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x+32, offset.y, offset.x+64, offset.y+64, this);                
                       if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }        
                        
                 //   }
                }
                }
                     }
                 //
                  if(ty < mapheight-1){
               if(!Southsquare.isEmpty()){
                for (final GameObject node : Southsquare) {
                  //  filterControl.objectInSquare(filterState, node);
                //     if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
                 //   if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
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
               graphics.drawImage(img.getImage(), (point.x + point.y +1 ) * 32, (point.y - point.x -1 +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+1+this.mapModel.getMapArchObject().getMapSize().getWidth() )* 32, offset.x, offset.y, offset.x + 32, offset.y + 64, this);                                
      //   graphics.drawImage(img.getImage(), (point.x+point.y)+32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);
      //   graphics.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32)-32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 32, offset.x, offset.y, offset.x+32, offset.y+64, this);
                      if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }          
                        
                  //  }
                }
                } 
                }
                  //
                  if((ty < mapheight-1)&&(tx > 0)){
                    if(!SouthWestsquare.isEmpty()){
                for (final GameObject node : SouthWestsquare) {
                  //  filterControl.objectInSquare(filterState, node);
                //    if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
                  //  if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
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
              graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
     //    graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+64, offset.y+64, this);
      //    graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32), (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+64, offset.y+64, this);
                            if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }       
                        
               //     }
                }
                } 
     
                }
                  //
                   if((ty < mapheight-1)&&(tx > 1)){
                   if(!LowerLeftsquare.isEmpty()){
                for (final GameObject node : LowerLeftsquare) {
               //     filterControl.objectInSquare(filterState, node);
               //     if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
                 //   if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
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
               graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +1+this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +1)* 32, (point.y - point.x+2+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x+32, offset.y, offset.x + 64, offset.y + 32, this);
      //   graphics.drawImage(img.getImage(), (point.x+point.y), (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) +32, (point.x+point.y) + 32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);
    //     graphics.drawImage(img.getImage(), (x+y), (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) +32, (x+y) + 32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x+32, offset.y, offset.x+64, offset.y+32, this);
                      if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }          
                        
                 //   }
                }
                } 
       
                }
                   //
                     if((ty < mapheight-2)&&(tx > 0)){
                    if(!LowerRightsquare.isEmpty()){
                for (final GameObject node :  LowerRightsquare) {
                //    filterControl.objectInSquare(filterState, node);
        
                  //  if (filterControl.canShow(node) && mapViewSettings.isEditType(node)) {
                   //       if (node.getAttributeInt(GameObject.INVISIBLE, true) != 0) {
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
       graphics.drawImage(img.getImage(), (point.x + point.y +1 ) * 32, (point.y - point.x +1+this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+2+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 32, offset.y + 32, this);
      //   graphics.drawImage(img.getImage(), (point.x+point.y)+32, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) +32, (point.x+point.y) + 64, (point.y-point.x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);
     //    graphics.drawImage(img.getImage(), (x+y)+32, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) +32, (x+y) + 64, (y-x+this.mapModel.getMapArchObject().getMapSize().getWidth()*32) + 64, offset.x, offset.y, offset.x+32, offset.y+32, this);
                        if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }         
                        
                 //   }
                }
                } 
       
                    
                }
         
       }
        //
    /*    int layer = -1;
        for (final BaseObject<?, ?, ?, ?> node : mapModel.getMapSquare(point)) {
            if (node.getAttributeInt(GameObject.INVISIBLE, true) == 0) {
                layer++;
            }
            final ImageIcon img = node.getNormalImage();
            if (!node.isMulti() || (img.getIconWidth() == 64 && img.getIconHeight() == 64)) {
                offset.x = 0;
                offset.y = 0;
            } else {
                // this is an oversized image, so it must be shifted
             //   offset.x = IGUIConstants.SQUARE_WIDTH * (node.getArchetype().getMultiX() - node.getMinX());
                 offset.x = 64 * (node.getArchetype().getMultiX() - node.getMinX());
              //  offset.y = IGUIConstants.SQUARE_HEIGHT * (node.getArchetype().getMultiY() - node.getMinY());
                offset.y = 64 * (node.getArchetype().getMultiY() - node.getMinY());
            }
        //    graphics.drawImage(img.getImage(), point.x * IGUIConstants.SQUARE_WIDTH, point.y * IGUIConstants.SQUARE_HEIGHT, point.x * IGUIConstants.SQUARE_WIDTH + IGUIConstants.SQUARE_WIDTH, point.y * IGUIConstants.SQUARE_HEIGHT + IGUIConstants.SQUARE_HEIGHT, offset.x, offset.y, offset.x + IGUIConstants.SQUARE_WIDTH, offset.y + IGUIConstants.SQUARE_HEIGHT, this);
        //    graphics.drawImage(img.getImage(), point.x * 64, point.y * 64, point.x * 64 + 64, point.y * 64 + 64, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
          //  graphics.drawImage(img.getImage(), (point.x + point.y - 9) * 32, (point.y - point.x + 8) * 32, (point.x + point.y - 7)* 32, (point.y - point.x + 10)* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
            graphics.drawImage(img.getImage(), (point.x + point.y ) * 32, (point.y - point.x +this.mapModel.getMapArchObject().getMapSize().getWidth()) * 32, (point.x + point.y +2)* 32, (point.y - point.x+this.mapModel.getMapArchObject().getMapSize().getWidth() + 2)* 32, offset.x, offset.y, offset.x + 64, offset.y + 64, this);
            if (node.getAttributeInt(GameObject.SMOOTHLEVEL, true) > 0) {
    // todo when wograld has more transition tiles      smoothingRenderer.paintSmooth(graphics, point, node.getAttributeInt(GameObject.SMOOTHLEVEL, true), layer, false, 0, 0);
            }
        }
        if (layer > -1) {
         //   smoothingRenderer.paintSmooth(graphics, point, 1, layer + 1, true, 0, 0);
        }  */
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
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
