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
// import com.realtime.crossfire.jxclient.gui.gui.TooltipManager;
import com.realtime.crossfire.jxclient.map.CfMap;
// import com.realtime.crossfire.jxclient.map.CfMapSquare;
// import com.realtime.crossfire.jxclient.map.Comparator2D;
// import com.realtime.crossfire.jxclient.map.CfMapClump;
import com.realtime.crossfire.jxclient.mapupdater.MapListener;
import com.realtime.crossfire.jxclient.mapupdater.MapScrollListener;
// import com.realtime.crossfire.jxclient.mapupdater.MapUpdaterState;
import com.realtime.crossfire.jxclient.mapupdater.NewmapListener;
import com.realtime.crossfire.jxclient.server.crossfire.MapSizeListener;
import com.realtime.crossfire.jxclient.server.crossfire.messages.Map2;
// import com.realtime.crossfire.jxclient.util.MathUtils;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Graphics;
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
// import org.jetbrains.annotations.NotNull;
// import org.jetbrains.annotations.Nullable;
// import net.infonode.util.ImageUtils;

// import com.realtime.crossfire.jxclient.faces.FacesProvider;
import com.realtime.crossfire.jxclient.faces.SmoothFaces;
import com.realtime.crossfire.jxclient.gui.gui.GUIElementListener;
import com.realtime.crossfire.jxclient.gui.gui.TooltipManager;
import com.realtime.crossfire.jxclient.map.CfMapSquare;
import com.realtime.crossfire.jxclient.mapupdater.MapUpdaterState;
import com.realtime.crossfire.jxclient.server.crossfire.CrossfireServerConnection;
import com.realtime.crossfire.jxclient.util.MathUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Display the map view. It supports both normal sized (32x32 pixel) and double
 * sized (64x64 pixel) sized tiles.
 * @author Lauwenmark
 * @author Andreas Kirschbaum
 */
public class GUIMap extends AbstractGUIMap {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * The {@link CrossfireServerConnection} to monitor.
     */
    @NotNull
    private final CrossfireServerConnection crossfireServerConnection;

    /**
     * The size of one tile.
     */
    private final int tileSize;
    
    private int x2;
    
    private int y2;
    
    private final MapUpdaterState mapUpdaterState;
    
    @NotNull
    private final MapListener mapListener = new MapListener() {

        @Override
        public void mapChanged(@NotNull final CfMap map, @NotNull final Set<CfMapSquare> changedSquares) {
            assert Thread.holdsLock(map);
            synchronized (bufferedImageSync) {
                final int x0 = map.getOffsetX();
                final int y0 = map.getOffsetY();
                x2 = x0;
                y2 = y0;
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
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
                   
     //   if (( (((vis_x-vis_y+18)%4) == 1) && ((((vis_x+vis_y+2)-2)%4) == 1) ) || (((((vis_x-vis_y+18)-2)%4) == 1) && (((vis_x+vis_y+2)%4) == 1))){

		/*	boolean drawRight =  false;
			boolean drawLeft = false;
			boolean drawTop = false;
			boolean drawTopRight = false;
			boolean drawTopLeft = false;
			boolean drawUnderRight = false;
			boolean drawUnderLeft = false;
		
			boolean square9trivial = false;
			boolean squareBtrivial = false;
			boolean squareDtrivial = false;
			boolean squareAtrivial = false;
			boolean squareCtrivial = false;
			boolean squareEtrivial = false;
			boolean squareGtrivial = false;
			boolean squareHtrivial = false  */
			


CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
				
                                        if (mapSquare9 != null) {
				boolean foundface = false;
				boolean coversEast = false;
				boolean coversNorth = false;
				boolean coversNortheast =false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face1 = mapSquare9.getFace(layer);
                                                if (face1 != null) {
					foundface = true;
            				int coversthings = face1.getFaceQuad();
            				coversthings = 7;
            
               				 int tmp = coversthings;
             				if((tmp - 4) > -1){
                 					coversEast = true;
             				}
             				tmp = coversthings % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast = true;
             				}
             				tmp = coversthings % 2;
             				if(tmp > 0){
                					coversNorth = true;  
             				}
             				if(coversthings == 7){
                 					break;
             				}
				}


				}
				
				if(foundface == false){
        					coversEast = true;
        					coversNortheast = true;
       					 coversNorth = true;
        				}
		
				if(coversNorth){
					mapSquare8.drawLeft = true;
				}
				if(coversEast){
					mapSquare8.drawRight =true;
				}
				if(coversNortheast){
					mapSquare8.drawTop = true;
				}
				if((!coversEast)&&(!coversNorth)&&(!coversNortheast)){
					mapSquare8.square9trivial = true;
				}
			}

			CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);

                                        if (mapSquareD != null) {
                                       	boolean foundface2 = false;
				boolean coversEast2 = false;
				boolean coversNorth2 = false;
				boolean coversNortheast2 = false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
					foundface2 = true;
            				int coversthings2 = face2.getFaceQuad();
            				coversthings2 = 7;
            
               				 int tmp = coversthings2;
             				if((tmp - 4) > -1){
                 					coversEast2 = true;
             				}
             				tmp = coversthings2 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast2 = true;
             				}
             				tmp = coversthings2 % 2;
             				if(tmp > 0){
                					coversNorth2= true;  
             				}
             				if(coversthings2 == 7){
                 					break;
             				}
					}
				}
				if(foundface2 == false){
        					coversEast2 = true;
        					coversNortheast2 = true;
       					 coversNorth2 = true;
        				}
				if(coversEast2){
					mapSquare8.drawUnderLeft = true;
				}
				if(coversNortheast2){
					mapSquare8.drawLeft = true;
				}
				if((!coversEast2)&&(!coversNorth2)&&(!coversNortheast2)){
					mapSquare8.squareDtrivial = true;
				}
			} 

			CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                            boolean foundface3 = false;
			        boolean coversNorth3 = false;
				boolean coversEast3 = false;
				boolean coversNortheast3 = false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face3 = mapSquareA.getFace(layer);
                                                if (face3 != null) {
				foundface3 = true;
            				int coversthings3 = face3.getFaceQuad();
            				coversthings3 = 7;
            
               				 int tmp = coversthings3;
             				if((tmp - 4) > -1){
                 					coversEast3 = true;
             				}
             				tmp = coversthings3 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast3 = true;
             				}
             				tmp = coversthings3 % 2;
             				if(tmp > 0){
                					coversNorth3= true;  
             				}
             				if(coversthings3 == 7){
                 					break;
             				}
					}
				}
				if(foundface3 == false){
        					coversEast3 = true;
        					coversNortheast3 = true;
       					 coversNorth3 = true;
        				}
				if(coversNorth3){
					mapSquare8.drawUnderRight = true;
				}
				if(coversNortheast3){
					mapSquare8.drawRight = true;
				}
				if((!coversEast3)&&(!coversNorth3)&&(!coversNortheast3)){
					mapSquare8.squareAtrivial = true;
				}	

			}

			  CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
                                     
                                            boolean foundface4 = false;
				boolean coversEast4 = false;
				boolean coversNorth4 =false;
				boolean coversNortheast4 =false;
     
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face4 = mapSquareE.getFace(layer);
                                                if (face4 != null) {
					foundface4= true;
            				int coversthings4 = face4.getFaceQuad();
            				coversthings4 = 7;
            
               				 int tmp = coversthings4;
             				if((tmp - 4) > -1){
                 					coversEast4 = true;
             				}
             				tmp = coversthings4 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast4 = true;
             				}
             				tmp = coversthings4 % 2;
             				if(tmp > 0){
                					coversNorth4= true;  
             				}
             				if(coversthings4 == 7){
                 					break;
             				}
					}
				}
				if(foundface4 == false){
        					coversEast4 = true;
        					coversNortheast4 = true;
       					 coversNorth4 = true;
        				}
				if(coversNortheast4){
					mapSquare8.drawUnderLeft = true;
					mapSquare8.drawUnderRight = true;
				}
				if((!coversEast4)&&(!coversNorth4)&&(!coversNortheast4)){
					mapSquare8.squareEtrivial = true;
				}
					

			}

			if(mapSquare8.drawLeft){
				       CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
                                            boolean foundface5 = false;
				boolean coversEast5 = false;
				boolean coversNorth5 = false;
				boolean coversNortheast5 = false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face5 = mapSquareB.getFace(layer);
                                                if (face5 != null) {
					foundface5= true;
            				int coversthings5 = face5.getFaceQuad();
            				coversthings5 = 7;
            
               				 int tmp = coversthings5;
             				if((tmp - 4) > -1){
                 					coversEast5 = true;
             				}
             				tmp = coversthings5 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast5 = true;
             				}
             				tmp = coversthings5 % 2;
             				if(tmp > 0){
                					coversNorth5= true;  
             				}
             				if(coversthings5 == 7){
                 					break;
             				}
					}
				}
				if(foundface5 == false){
        					coversEast5 = true;
        					coversNortheast5 = true;
       					 coversNorth5 = true;
        				}

				if(coversEast5){
					mapSquare8.drawTop = true;
		// can be a different issue than if top of center square has something to draw
				}
				if(coversNortheast5){
					mapSquare8.drawTopLeft = true;
					
				}
				if((!coversEast5)&&(!coversNorth5)&&(!coversNortheast5)){
					mapSquare8.squareBtrivial = true;
				}
				
			}

			}

			if(mapSquare8.drawRight){
				CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
                                            boolean foundface6 = false;
				boolean coversEast6 = false;
				boolean coversNorth6 = false;
				boolean coversNortheast6 =false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face6 = mapSquareC.getFace(layer);
                                                if (face6!= null) {
				foundface6= true;
            				int coversthings6 = face6.getFaceQuad();
            				coversthings6 = 7;
            
               				 int tmp = coversthings6;
             				if((tmp - 4) > -1){
                 					coversEast6 = true;
             				}
             				tmp = coversthings6 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast6 = true;
             				}
             				tmp = coversthings6 % 2;
             				if(tmp > 0){
                					coversNorth6= true;  
             				}
             				if(coversthings6 == 7){
                 					break;
             				}
					}
				}
				if(foundface6 == false){
        					coversEast6 = true;
        					coversNortheast6 = true;
       					 coversNorth6 = true;
        				}



				if(coversNorth6){
					mapSquare8.drawTop = true;
		// can be a different issue than if top of center square has something to draw
				}
				if(coversNortheast6){
					mapSquare8.drawTopRight = true;
					
				}	
				if((!coversEast6)&&(!coversNorth6)&&(!coversNortheast6)){
					mapSquare8.squareCtrivial = true;
				}	
			}
			}   

			if(mapSquare8.drawTop){
				 CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
                                            boolean foundface7 = false;
				boolean coversEast7 = false;
				boolean coversNorth7 = false;
				boolean coversNortheast7 = false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face7 = mapSquareL.getFace(layer);
                                                if (face7 != null) {
					foundface7= true;
            				int coversthings7 = face7.getFaceQuad();
            				coversthings7 = 7;
            
               				 int tmp = coversthings7;
             				if((tmp - 4) > -1){
                 					coversEast7 = true;
             				}
             				tmp = coversthings7 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast7 = true;
             				}
             				tmp = coversthings7 % 2;
             				if(tmp > 0){
                					coversNorth7= true;  
             				}
             				if(coversthings7 == 7){
                 					break;
             				}
					}
				}
				if(foundface7 == false){
        					coversEast7 = true;
        					coversNortheast7 = true;
       					 coversNorth7 = true;
        				}
				if(coversNorth7){
					
					mapSquare8.drawTopLeft = true;
				}

				if(coversEast7){
					mapSquare8.drawTopRight =true;
					
				}		
			}


			}   

			if(mapSquare8.drawUnderLeft){
				CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
                                            boolean foundface8 = false;
				boolean coversEast8 = false;
				boolean coversNortheast8 = false;
				boolean coversNorth8 = false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face8 = mapSquareG.getFace(layer);
                                                if (face8 != null) {
				foundface8= true;
            				int coversthings8 = face8.getFaceQuad();
            				coversthings8 = 7;
            
               				 int tmp = coversthings8;
             				if((tmp - 4) > -1){
                 					coversEast8 = true;
             				}
             				tmp = coversthings8 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast8 = true;
             				}
             				tmp = coversthings8 % 2;
             				if(tmp > 0){
                					coversNorth8= true;  
             				}
             				if(coversthings8 == 7){
                 					break;
             				}
					}
				}
				if(foundface8 == false){
        					coversEast8 = true;
        					coversNortheast8 = true;
       					 coversNorth8 = true;
        				}
				if((!coversEast8)&&(!coversNorth8)&&(!coversNortheast8)){
					mapSquare8.squareGtrivial = true;
				}
				
			}
			}
			
			if(mapSquare8.drawUnderRight){
			CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {     
                                            boolean foundface9 = false;
				boolean coversNorth9 = false;
				boolean coversNortheast9=false;
				boolean coversEast9=false;
                                            for (int layer = 0; layer < Map2.NUM_LAYERS; layer++) {
                                                Face face9 = mapSquareH.getFace(layer);
                                                if (face9 != null) {
				foundface9= true;
            				int coversthings9 = face9.getFaceQuad();
            				coversthings9 = 7;
            
               				 int tmp = coversthings9;
             				if((tmp - 4) > -1){
                 					coversEast9 = true;
             				}
             				tmp = coversthings9 % 4;
             				if((tmp - 2) > -1) {
                					coversNortheast9 = true;
             				}
             				tmp = coversthings9 % 2;
             				if(tmp > 0){
                					coversNorth9= true;  
             				}
             				if(coversthings9 == 7){
                 					break;
             				}
					}
				}
				if(foundface9 == false){
        					coversEast9 = true;
        					coversNortheast9 = true;
       					 coversNorth9 = true;
        				}
				if((!coversEast9)&&(!coversNorth9)&&(!coversNortheast9)){
					mapSquare8.squareHtrivial = true;
				}
				
			}

			}
                        
                       } // for changed squares, preprocessing need for additional draws
         
                       for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
                            
                                           //      if(mapSquare8.drawTopLeft){

			    boolean foundtile1 = false;
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(9);
                                                if (face != null) {
                                                    if (!foundtile1) {
                                                        foundtile1 = true;
                                                    }
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 9, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
	//	}
                                     
			// if(mapSquare8.drawTopRight){
					boolean foundtile2 = false;

                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(9);
                                                if (face != null) {
                                                    if (!foundtile2) {
                                                        foundtile2 = true;
                                                    }
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 9, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			// }
                                        
			// if(mapSquare8.drawTop){
			    boolean foundtile3 = false;
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(9);
                                                if (face != null) {
                                                    if (!foundtile3) {
                                                        foundtile3 = true;
                                                    }
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 9, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			// }

//      if(mapSquare8.drawTopLeft){
                           
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 10; layer < 12; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  if (!foundtile1) {
                                                        foundtile1 = true;
                                                    }
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }

					    if( !foundtile1) {
                                                
						// it is floor 3 
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                 g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);   
                                            }
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  else {
			      ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                               g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);

		}
// }

// if(mapSquare8.drawTopRight){
                                      
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    if (!foundtile2) {
                                                        foundtile2 = true;
                                                    }
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            if( !foundtile2) {
                                                // it is floor 3
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                 g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                         
                                                
                                            }
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } else {
                                             ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                 g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null); 
                                            
                                        }
			// }


//    if(mapSquare8.drawLeft){
		    
				  boolean foundtile4 = false;
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(9);
                                                if (face != null) {
                                                    if (!foundtile4) {
                                                        foundtile4 = true;
                                                    }
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 9, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
			// }
                            
                     
                                     
		//	if(mapSquare8.drawRight){  
				      boolean foundtile5 = false;
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(9);
                                                if (face != null) {
                                                    if (!foundtile5) {
                                                        foundtile5 = true;
                                                    }
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 9, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
		//	}



// if(mapSquare8.drawTop){
                                       
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            boolean foundSquare = false;
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                    if (!foundtile3) {
                                                        foundtile3 = true;
                                                    }
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            if( !foundtile3) {
                                                // it is floor 3
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                         
                                                
                                            }
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } else {
                                             ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);  
                                            
                                        }
			// }



					boolean foundtile6 = false;
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(9);
                                                if (face != null) {
                                                    if (!foundtile6) {
                                                        foundtile6 = true;
               
                                                    }
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 9, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


  
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                    if (!foundtile4) {
                                                        foundtile4 = true;
                                                    }
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
                                            if( !foundtile4) {
                                                // it is floor 3
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);  

                                            }
                                            
                                            
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } else {
                                            ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);  
                                            
                                        }
                                        
		
//	if(mapSquare8.drawRight){   
                                   
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                    if (!foundtile5) {
                                                        foundtile5 = true;
                                                    }
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				


                                                }
               
                                            }
                                            if( !foundtile5) {
                                                // it is floor 3
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                         
                                                
                                            }
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } else {
                                            ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                            
                                        }
		//	}


                                
					  boolean foundtile8 = false;
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(9);
                                                if (face2 != null) {
                                                    if (!foundtile8) {
                                                        foundtile8 = true;
               
                                                    }
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					boolean foundtile7 = false;
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            
                                                Face face2 = mapSquareD.getFace(9);
                                                if (face2 != null) {
                                                    if (!foundtile7) {
                                                        foundtile7 = true;
               
                                                    }
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    if (!foundtile6) {
                                                        foundtile6 = true;
             
                                                    }
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                            if( !foundtile6) {
                                                // it is floor 3, and the central square, so do it
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                         
                                                
                                            }
                                        } else {
                                            ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                        }



                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                    if (!foundtile8) {
                                                        foundtile8 = true;
               
                                                    }
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                                            if( !foundtile8) {
                                                // it is floor 3
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                         
                                                
                                            }
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } else {
                                             ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                            
                                        }

                                        
                                        
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            
     
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                    if (!foundtile7) {
                                                        foundtile7 = true;
               
                                                    }
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                            if( !foundtile7) {
                                                // it is floor 3
         
                                                ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                         
                                                
                                            }
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } else {
                                             ImageIcon imageIcon = facesProvider.getBlockedImageIcon();
                                                g.drawImage(imageIcon.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                            
                                        }



//    if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(9);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
			// }


//    if(mapSquare8.drawRight){
                                   
			    
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(9);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
			// }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(9);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                                        } 


//    if(mapSquare8.drawLeft){
                              
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
			// }

//    if(mapSquare8.drawRight){
                                    

                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
			// }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(9);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(9);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                                        } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(9);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }

                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        } 



                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(9);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(9);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }



                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 10; layer < 12; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
                                        
            
            		mapSquare8.recentDraw();
                                
                      } // floor 3
                       
           for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.floorDrawn();
}
           
    for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
                          
                                                  
                            if(mapSquare8.drawTopLeft){

			    
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(6);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
		}
                                     
			 if(mapSquare8.drawTopRight){
					
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(6);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			 }
                                        
			 if(mapSquare8.drawTop){
			    
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(6);
                                                if (face != null) {
                                                    
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			 }

      if(mapSquare8.drawTopLeft){
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 7; layer < 9; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }   
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  
 }

 if(mapSquare8.drawTopRight){
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
  }


    if(mapSquare8.drawLeft){
		    
				 
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(6);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
	 }
                            
                     
                                     
    if(mapSquare8.drawRight){  
				     
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(6);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}



 if(mapSquare8.drawTop){
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                   
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
	}

			
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(6);
                                                if (face != null) {
                                                   
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


   CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
    
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
		
	if(mapSquare8.drawRight){   
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                  
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				
                                                }
               
                                            }
                                          
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}

		
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(6);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                                                    
                                                Face face2 = mapSquareD.getFace(6);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                           
                                        }                               


                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                          
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 

           
                                  
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                           
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 



    if(mapSquare8.drawLeft){
                                  
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(6);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
	 }


    if(mapSquare8.drawRight){
                                   
			      CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(6);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	 }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(6);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                          } 


if(mapSquare8.drawLeft){
                                    
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                            } 
                                        
	}

if(mapSquare8.drawRight){
                                    
CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	}


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(6);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(6);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                           } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                             } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(6);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 6, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                            } 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(6);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
	}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(6);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }



                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 7; layer < 9; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
                                        
            
            		mapSquare8.recentDraw();
                                
                      } // floor 2          
           
 for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.floorDrawn();
}
 
   for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
                         
                                                      
                            if(mapSquare8.drawTopLeft){

			    
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(3);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
		}
                                     
			 if(mapSquare8.drawTopRight){
					
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(3);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			 }
                                        
			 if(mapSquare8.drawTop){
			    
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(3);
                                                if (face != null) {
                                                    
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			 }

      if(mapSquare8.drawTopLeft){
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 4; layer < 6; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }   
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  
 }

 if(mapSquare8.drawTopRight){
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
  }


    if(mapSquare8.drawLeft){
		    
				 
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(3);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
	 }
                            
                     
                                     
    if(mapSquare8.drawRight){  
				     
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(3);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}



 if(mapSquare8.drawTop){
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                           
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                   
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
	}

			
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(3);
                                                if (face != null) {
                                                   
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


   CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
    
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
		
	if(mapSquare8.drawRight){   
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                  
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				
                                                }
               
                                            }
                                          
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}

		
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(3);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                                                    
                                                Face face2 = mapSquareD.getFace(3);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                           
                                        }                               



                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                          
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 

           
                                        
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                           
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 



    if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(3);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
	 }


    if(mapSquare8.drawRight){
                                   
			      CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(3);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	 }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(3);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                          } 


if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                            } 
                                        
	}

if(mapSquare8.drawRight){
                                    
CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	}


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(3);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(3);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                           } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                             } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(3);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 3, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                            } 



                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(3);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
	}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(3);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }




                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 4; layer < 6; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
                                        
            
            
            		mapSquare8.recentDraw();
                                
                      } // floor 1 
 
  for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.floorDrawn();
}
 
   for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
       
                                                        
                            if(mapSquare8.drawTopLeft){

			    
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(0);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
		}
                                     
			 if(mapSquare8.drawTopRight){
					
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(0);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			 }
                                        
			 if(mapSquare8.drawTop){
			    
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(0);
                                                if (face != null) {
                                                    
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			 }

      if(mapSquare8.drawTopLeft){
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 1; layer < 3; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }   
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  
 }

 if(mapSquare8.drawTopRight){
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
  }


    if(mapSquare8.drawLeft){
		    
				 
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(0);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
	 }
                            
                     
                                     
    if(mapSquare8.drawRight){  
				     
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(0);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}



 if(mapSquare8.drawTop){
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                           
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                   
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
	}

			
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(0);
                                                if (face != null) {
                                                   
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


   CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
    
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
		
	if(mapSquare8.drawRight){   
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                  
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				
                                                }
               
                                            }
                                          
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}

		
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(0);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                                                    
                                                Face face2 = mapSquareD.getFace(0);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                           
                                        }                               



                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                          
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 

           
                                      
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                           
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 



    if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(0);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
	 }


    if(mapSquare8.drawRight){
                                   
			      CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(0);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	 }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(0);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                          } 


if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                            } 
                                        
	}

if(mapSquare8.drawRight){
                                    
CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	}


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(0);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(0);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                           } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                             } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(0);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 0, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                            } 



                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(0);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
	}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(0);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }




                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 1; layer < 3; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
            
            		mapSquare8.recentDraw();
                                
                      } // floor 0 
 
   for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.floorDrawn();
}
 
   for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;

                                
                            if(mapSquare8.drawTopLeft){

			    
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(12);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
		}
                                     
			 if(mapSquare8.drawTopRight){
					
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(12);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			 }
                                        
			 if(mapSquare8.drawTop){
			    
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(12);
                                                if (face != null) {
                                                    
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			 }

      if(mapSquare8.drawTopLeft){
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 13; layer < 15; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }   
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  
 }

 if(mapSquare8.drawTopRight){
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
  }


    if(mapSquare8.drawLeft){
		    
				 
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(12);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
	 }
                            
                     
                                     
    if(mapSquare8.drawRight){  
				     
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(12);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}



 if(mapSquare8.drawTop){
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                           
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                   
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
	}

			
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(12);
                                                if (face != null) {
                                                   
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


   CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
    
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
		
	if(mapSquare8.drawRight){   
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                  
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				
                                                }
               
                                            }
                                          
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}

		
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(12);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                                                    
                                                Face face2 = mapSquareD.getFace(12);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                           
                                        }                               



                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                          
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 

           
                                      
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                           
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 



    if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(12);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
	 }


    if(mapSquare8.drawRight){
                                   
			      CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(12);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	 }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(12);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                          } 


if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                            } 
                                        
	}

if(mapSquare8.drawRight){
                                    
CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	}


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(12);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(12);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                           } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                             } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(12);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 12, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                            } 



                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(12);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
	}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(12);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }




                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 13; layer < 15; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
                                                                
                            
            		mapSquare8.recentDraw();
                                
                      } // floor 4 
  
   for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.floorDrawn();
}
 
   for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
     
                                
                            if(mapSquare8.drawTopLeft){

			    
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(15);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
		}
                                     
			 if(mapSquare8.drawTopRight){
					
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(15);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			 }
                                        
			 if(mapSquare8.drawTop){
			    
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(15);
                                                if (face != null) {
                                                    
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			 }

      if(mapSquare8.drawTopLeft){
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 16; layer < 18; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }   
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  
 }

 if(mapSquare8.drawTopRight){
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
  }


    if(mapSquare8.drawLeft){
		    
				 
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(15);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
	 }
                            
                     
                                     
    if(mapSquare8.drawRight){  
				     
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(15);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}



 if(mapSquare8.drawTop){
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                           
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                   
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
	}

			
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(15);
                                                if (face != null) {
                                                   
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


   CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
    
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
		
	if(mapSquare8.drawRight){   
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                  
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				
                                                }
               
                                            }
                                          
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}

		
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(15);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                                                    
                                                Face face2 = mapSquareD.getFace(15);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                           
                                        }                               



                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                          
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 

           
                                      
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                           
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 



    if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(15);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
	 }


    if(mapSquare8.drawRight){
                                   
			      CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(15);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	 }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(15);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                          } 


if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                            } 
                                        
	}

if(mapSquare8.drawRight){
                                    
CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	}


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(15);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(15);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                           } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                             } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(15);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 15, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                            } 



                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(15);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
	}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(15);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }




                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 16; layer < 18; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
                                                                
            
            		mapSquare8.recentDraw();
                                
                      } // floor 5 
   
   for(CfMapSquare mapSquare8: changedSquares){
mapSquare8.floorDrawn();
}
 
   for(CfMapSquare mapSquare8: changedSquares){
                           
                           int tile_x = mapSquare8.getX()+x0;
                            int tile_y = mapSquare8.getY()+y0;
                            int screen_x = tile_x + tile_y - 9;
                            int screen_y = tile_y - tile_x + 8;
             //      if (( (((screen_x-screen_y+22)%4) == 1) && ((((screen_x+screen_y-2)-2)%4) == 1) ) || (((((screen_x-screen_y+22)-2)%4) == 1) && (((screen_x+screen_y-2)%4) == 1))){          
                            
                       
                            int px = offsetX+(screen_x)*48;
                            int py = offsetY+(screen_y)*48;
    
                            
                             
                            if(mapSquare8.drawTopLeft){

			    
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
                                            

                                                Face face = mapSquareJ.getFace(18);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }
                                                }
               
				} // mapsquareJ behind current tile, not being drawn this mapchanged
                                            
                                        } 
		}
                                     
			 if(mapSquare8.drawTopRight){
					
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                                Face face = mapSquareK.getFace(18);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }
                                                }
        
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
			 }
                                        
			 if(mapSquare8.drawTop){
			    
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                            
                                           
                                                Face face = mapSquareL.getFace(18);
                                                if (face != null) {
                                                    
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }
                                                }
               
                                            
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
			 }

      if(mapSquare8.drawTopLeft){
                            CfMapSquare mapSquareJ = map.getMapSquare(tile_x+1, tile_y-2);
                                        if (mapSquareJ != null) {

				if(!mapSquareJ.isChanged()){
      
                                            for(int layer = 19; layer < 21; layer++){
                                                Face face = mapSquareJ.getFace(layer);
                                                if (face != null) {
						  
          
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
        //   g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                                  
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }   
           
				} // mapsquareJ behind current tile, not being drawn this mapchanged
               
		}  
 }

 if(mapSquare8.drawTopRight){
                                        CfMapSquare mapSquareK = map.getMapSquare(tile_x+2, tile_y-1);
                                        if (mapSquareK != null) {
				if(!mapSquareK.isChanged()){
                                           
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareK.getFace(layer);
                                                if (face != null) {
                                                    
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
  g.drawImage(imageIcon.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
                                            
			} // mapsquareK behind current square, not being drawn this mapchanged
                                        } 
  }


    if(mapSquare8.drawLeft){
		    
				 
                                    CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
  
                                                Face face = mapSquareB.getFace(18);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }

                                                }

				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
	 }
                            
                     
                                     
    if(mapSquare8.drawRight){  
				     
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            
                                                Face face = mapSquareC.getFace(18);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }
				

                                                }

                                            
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}



 if(mapSquare8.drawTop){
                                        CfMapSquare mapSquareL = map.getMapSquare(tile_x+1, tile_y-1);
                                        if (mapSquareL != null) {
				if(!mapSquareL.isChanged()){
                                           
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareL.getFace(layer);
                                                if (face != null) {
                                                   
                             ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                 g.drawImage(imageIcon.getImage(),px,py-96,96,96,null);
                                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
                                            
				} // mapsquareL behind current square, not being drawn this mapchanged
                                            
                                        } 
	}

			
                                     //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                                Face face = mapSquare8.getFace(18);
                                                if (face != null) {
                                                   
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }

                                                }
                                            
                                            
                                        } 


   CfMapSquare mapSquareB = map.getMapSquare(tile_x, tile_y-1);
                                        if (mapSquareB != null) {
				if(!mapSquareB.isChanged()){
                                           
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                                                   
                              ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
         //  g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,64,0,128,128,null);
            g.drawImage(imageIcon.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);                                   
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

                                                }
               
                                            }
    
				} //mapsquareB behind current square, not being drawn this mapchanged
                                            
                                        } 
                                        
		
	if(mapSquare8.drawRight){   
                                     CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
				if(!mapSquareC.isChanged()){
                                            
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                                                  
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
    //      g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,64,128,null);
         g.drawImage(imageIcon.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
				
                                                }
               
                                            }
                                          
			} //mapsquareC behind current square, is not being drawn this mapchanged
                                           
                                        } 
	}

		
                                        CfMapSquare mapSquareA = map.getMapSquare(tile_x, tile_y+1);
                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                               
                                                Face face2 = mapSquareA.getFace(18);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                } 
                                            
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 
                                        
					
                                        CfMapSquare mapSquareD = map.getMapSquare(tile_x-1, tile_y);
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                                                    
                                                Face face2 = mapSquareD.getFace(18);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
        
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 

   //   CfMapSquare mapSquare9 = map.getMapSquare(tile_x, tile_y);
                                        if (mapSquare8 != null) {

                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                                                    
                                ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum(), null);
                                                  
                        g.drawImage(imageIcon.getImage(),px,py,96,96,null);
               
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }

			
                                                }
                                            }
                                           
                                        }                               



                                        if (mapSquareA != null) {
                                  if((!mapSquareA.isChanged())||(mapSquareA.wasDrawn())){
                                            
     
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face2 = mapSquareA.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);  
                                            
                         //     g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
              g.drawImage(imageIcon2.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }
               
                                            }
                          
		}  // squareA is either not being drawn this mapchanged, or will be drawn later in it
                                           
                                        } 

           
                                      
                                        if (mapSquareD != null) {
if((!mapSquareD.isChanged())||(mapSquareD.wasDrawn())){
                                       
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face2 = mapSquareD.getFace(layer);
                                                if (face2 != null) {
                                                   
                          ImageIcon imageIcon2 = facesProvider.getImageIcon(face2.getFaceNum(), null);          
                        //     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                     g.drawImage(imageIcon2.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    if (smoothingRenderer != null) {
            
                                                    }
				
                                                }           
                                            }
                                           
		} // squareD is either not being drawn this mapchanged, or will be drawn later in it     
                                        } 



    if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                           
                                                Face face = mapSquareB.getFace(18);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                           
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                                        } 
                                        
	 }


    if(mapSquare8.drawRight){
                                   
			      CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                           
                                                Face face = mapSquareC.getFace(18);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }

				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	 }

 if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){
				        
                                                Face face = mapSquare8.getFace(18);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
				
                                                }
         		
				
					} // square 9 not trivial
                                            
                          } 


if(mapSquare8.drawLeft){
                                   
                                        if (mapSquareB != null) {
					  
					  if(!mapSquare8.squareBtrivial){
				if(!mapSquareB.isChanged()){
 
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareB.getFace(layer);
                                                if (face != null) {
                     
                                                 
                ImageIcon imageIcon4 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    
                                                    if(imageIcon4 != null){
                   //    g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,64,64,128,128,null);
                       g.drawImage(imageIcon4.getImage(),px,py-96,px+48,py-48,32,32,64,64,null);                     
                                                    }
				
                                                }
                                            }
       
				} //mapsquareB behind current square, not being drawn this mapchanged
				
					} // square B not trivial
                                            
                            } 
                                        
	}

if(mapSquare8.drawRight){
                                    
CfMapSquare mapSquareC = map.getMapSquare(tile_x+1, tile_y);
                                        if (mapSquareC != null) {
					  
					  if(!mapSquare8.squareCtrivial){
				if(!mapSquareC.isChanged()){
 
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareC.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon5 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon5 != null){
                   //   g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,64,64,128,null);
                      g.drawImage(imageIcon5.getImage(),px+48,py-96,px+96,py-48,0,32,32,64,null);
                                                    }

                                                }
                                            }
       
				} //mapsquareC behind current square, not being drawn this mapchanged
				
					} // square C not trivial
                                            
                                        } 
                                        
	}


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                                Face face = mapSquareA.getFace(18);
                                                if (face != null) {
                     
                                                  ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                }

				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                                        }

 


                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                                Face face = mapSquareD.getFace(18);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
                                                }

				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                           } 

if (mapSquare8 != null) {
					  
					  if(!mapSquare8.square9trivial){

                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquare8.getFace(layer);
                                                if (face != null) {
                     
                                                 ImageIcon imageIcon6 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon6 != null){
                                                    g.drawImage(imageIcon6.getImage(),px,py-96,96,96,null);
							  }
                    
                                                    
				
                                                }
                                            }

					} // square 9 not trivial
                                            
                             } 

                                        
                                        CfMapSquare mapSquareE = map.getMapSquare(tile_x-1, tile_y+1);
                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){

                                                Face face = mapSquareE.getFace(18);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                           
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, 18, map, tileSize);
                                                    }
                                                }
               
                                            
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                        if (mapSquareA != null) {
					  
					  if(!mapSquare8.squareAtrivial){
				if(!mapSquareA.isChanged()){
 
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareA.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon3 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon3 != null){
                   
                        g.drawImage(imageIcon3.getImage(),px+48,py-48,px+96,py+48,0,0,32,64,null);
   
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareA behind current square, not being drawn this mapchanged
				
					} // square A not trivial
                                            
                            } 



                                        if (mapSquareD != null) {
					  
					  if(!mapSquare8.squareDtrivial){
				if(!mapSquareD.isChanged()){
 
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareD.getFace(layer);
                                                if (face != null) {
                     ImageIcon imageIcon7 = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon7 != null){
                   
                    g.drawImage(imageIcon7.getImage(),px,py-48,px+48,py+48,32,0,64,64,null);
                                                    }
                                                 
               
				
                                                }
                                            }
       
				} //mapsquareD behind current square, not being drawn this mapchanged
				
					} // square D not trivial
                                            
                                        } 


if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                     
                                                Face face2 = mapSquareG.getFace(18);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
	}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
  
                                                Face face2 = mapSquareH.getFace(18);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }




                                        if (mapSquareE != null) {
   
                                     if(!mapSquare8.squareEtrivial){
 if((!mapSquareE.isChanged())||(mapSquareE.wasDrawn())){
                                            
     
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face = mapSquareE.getFace(layer);
                                                if (face != null) {
                                                    
                   ImageIcon imageIcon = facesProvider.getImageIcon(face.getFaceNum()+10000, null);
                                                    if(imageIcon != null){
                                                      g.drawImage(imageIcon.getImage(),px,py,96,96,null);
                                                    }
                            
                                                    if (smoothingRenderer != null) {
            //        smoothingRenderer.paintSmooth(g, newx, newy, px+16, py+16, layer, map, tileSize);
                                                    }
                                                }
               
                                            }
}  // squareE is either not being drawn this mapchanged, or will be drawn later in it 
				} // squareE not trivial, or else it would be out of sight
		   
                                           
                                        }


                                   
			if(mapSquare8.drawUnderLeft){     
                                       CfMapSquare mapSquareG = map.getMapSquare(tile_x-2, tile_y+1);
                                        if (mapSquareG != null) {
				if(!mapSquare8.squareGtrivial){
                            if((!mapSquareG.isChanged())||(mapSquareG.wasDrawn())){
                                            

                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face2 = mapSquareG.getFace(layer);
                                                if (face2 != null) {
                                                    
               ImageIcon imageIcon8 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon8 != null){
                                              
                 //   g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,64,0,128,64,null);
                    g.drawImage(imageIcon8.getImage(),px,py+48,px+48,py+96,32,0,64,32,null);
                                                    }
               
                                                }                                 
                                            }
		}  // squareG is either not being drawn this mapchanged, or will be drawn later in it 
			} // squareG not trivial
                                            
                                        }
			}
                                     
			if(mapSquare8.drawUnderRight){   
                                       CfMapSquare mapSquareH = map.getMapSquare(tile_x-1, tile_y+2);
                                        if (mapSquareH != null) {
				if(!mapSquare8.squareHtrivial){
                          if((!mapSquareH.isChanged())||(mapSquareH.wasDrawn())){         
                                            
     
                                            for (int layer = 19; layer < 21; layer++) {
                                                Face face2 = mapSquareH.getFace(layer);
                                                if (face2 != null) {
                                                    
                                                    
               ImageIcon imageIcon9 = facesProvider.getImageIcon(face2.getFaceNum()+10000, null);
                                                    if(imageIcon9 != null){
           
                     //   g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,64,64,null);
                         g.drawImage(imageIcon9.getImage(),px+48,py+48,px+96,py+96,0,0,32,32,null);                          
                                                    }
                                                }
               
                                            }
}  // squareH is either not being drawn this mapchanged, or will be drawn later in it 
				}  // squareH not trivial
				}
                                            
                                        }
                                        
                                        
            
            		mapSquare8.recentDraw();
                                
                      } // floor 6
                       
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
     * @param crossfireServerConnection the server connection to monitor
     * @param smoothFaces the smooth faces to use
     */
    public GUIMap(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, @NotNull final MapUpdaterState mapUpdaterState, @NotNull final FacesProvider facesProvider, @NotNull final CrossfireServerConnection crossfireServerConnection, @NotNull final SmoothFaces smoothFaces) {
        super(tooltipManager, elementListener, name, mapUpdaterState, facesProvider, new SmoothingRenderer(smoothFaces, facesProvider));
        this.crossfireServerConnection = crossfireServerConnection;
        this.mapUpdaterState = mapUpdaterState;
        this.mapUpdaterState.addCrossfireMapListener(mapListener);
        tileSize = facesProvider.getSize();
        x2=0;
        y2=0;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        mapUpdaterState.removeCrossfireMapListener(mapListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintSquareBackground(@NotNull final Graphics g, final int px, final int py, final boolean hasImage, @NotNull final CfMapSquare mapSquare) {
        paintColoredSquare(g, Color.BLACK, px, py);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void markPlayer(@NotNull final Graphics g, final int dx, final int dy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(@NotNull final MouseEvent e) {
        super.mouseClicked(e);
        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            final int dx1 = e.getX()-getOffsetX();
            final int dy1 = e.getY()-getOffsetY();
            if (dx1 >= 0 && dy1 >= 0) {
                
                final int mapWidth = getMapWidth();
                final int mapHeight = getMapHeight();
                final int dx2 = dx1/tileSize-mapWidth/2;
                final int dy2 = dy1/tileSize-mapHeight/2;
                
                if (dx2 < mapWidth && dy2 < mapHeight) {
             
                    int screen_x = ((dx1/48) - (dy1/48) )/2;
      
                    int screen_y = ((dx1/48) +(dy1/48)-18)/2;
                 
                    int mapx = screen_x;
                    int mapy = screen_y;
                
                    crossfireServerConnection.sendLookat(mapx, mapy);
                }
            }
            break;

        case MouseEvent.BUTTON2:
        case MouseEvent.BUTTON3:
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getMapWidth()*tileSize, getMapHeight()*tileSize);
    }

    /**
     * Returns the minimal map width in squares needed to fill the map area.
     * @return the map width in squares
     */
    public int getPreferredMapWidth() {
        return MathUtils.divRoundUp(getWidth(), tileSize);
    }

    /**
     * Returns the minimal map height in squares needed to fill the map area.
     * @return the map height in squares
     */
    public int getPreferredMapHeight() {
        return MathUtils.divRoundUp(getHeight(), tileSize);
    }

}
