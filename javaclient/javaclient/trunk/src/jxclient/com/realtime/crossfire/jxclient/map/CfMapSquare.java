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

package com.realtime.crossfire.jxclient.map;

import com.realtime.crossfire.jxclient.faces.Face;
import com.realtime.crossfire.jxclient.server.crossfire.messages.Map2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a square in a {@link CfMap}. A square comprises of {@link
 * Map2#NUM_LAYERS} faces as well as a darkness value.
 * <p/>
 * This class assumes that the "head" part of a face is the part the server did
 * sent. This is the bottom-right part for multi-square objects. Not that this
 * definition is inconsistent to what the server assumes as the head part of an
 * object.
 * @author Andreas Kirschbaum
 */
public class CfMapSquare {

    /**
     * The default darkness value for newly created squares.
     */
    public static final int DEFAULT_DARKNESS = 255;

    /**
     * The default smooth value for newly created squares.
     */
    public static final int DEFAULT_SMOOTH = 0;

    /**
     * The default magic map color for newly created squares.
     */
    public static final int DEFAULT_COLOR = -1;

    /**
     * The darkness value for a full bright square.
     */
    public static final int DARKNESS_FULL_BRIGHT = 255;

    /**
     * The default face value for newly creates squares.
     */
    @Nullable
    public static final Face DEFAULT_FACE = null;
// it looks as though guimap gets to decide what to draw if there is no face
// which should allow toggling of blocked or blank, depending on floor
    
    /**
     * The {@link CfMap} this map square is part of.
     */
    @NotNull
    private final CfMap map;

    /**
     * The absolute x-coordinate of this square in its {@link CfMap}.
     */
    private final int x;

    /**
     * The absolute y-coordinate of this square in its {@link CfMap}.
     */
    private final int y;

    /**
     * Flag used to defer clearing the values: when a <code>CfMapSquare</code>
     * is cleared, the old values remain valid until at least one field is
     * re-set.
     */
    private boolean fogOfWar = false;
    
    // serpentshard added these 03/20/2013
    
    private boolean fogOfWar2 = false;
    
    private boolean fogOfWar3 = false;
    
    private boolean fogOfWar4 = false;
    
    private boolean fogOfWar5 = false;
    
    private boolean fogOfWar6 = false;
    
    private boolean fogOfWar7 = false;

    /**
     * The darkness value of the square in the range [0..255]. 0=dark, 255=full
     * bright={@link #DARKNESS_FULL_BRIGHT}.
     */
    private int darkness = DEFAULT_DARKNESS;
    
    // serpentshard added these 03/20/2013
    
    private int darkness2 = DEFAULT_DARKNESS;
    
    private int darkness3 = DEFAULT_DARKNESS;
    
    private int darkness4 = DEFAULT_DARKNESS;
    
    private int darkness5 = DEFAULT_DARKNESS;
    
    private int darkness6 = DEFAULT_DARKNESS;
    
    private int darkness7 = DEFAULT_DARKNESS;

    /**
     * The magic map color of the square. Set to {@link #DEFAULT_COLOR} if none
     * is known.
     */
    private int color = DEFAULT_COLOR;
    
    // additional code used in redrawing
    // is it being changed this epoch of mapchanged
    private boolean Changed = false;
    
    // additional code used in redrawing
    // for a given call to mapchanged, has the square already been drawn
    // but do not clear changed yet; later clear them both; should use enums instead?
    // update: after drawing a floor, clear it
    private boolean Drawn = false;
    
     // 03-21-2013 began storing these on the mapsquare itself
    // so that iterator of mapsquares for drawing can do several passes
              public    boolean drawRight =  false;
              public	boolean drawLeft = false;
              public	boolean drawTop = false;
              public	boolean drawTopRight = false;
	      public	boolean drawTopLeft = false;
              public	boolean drawUnderRight = false;
              public	boolean drawUnderLeft = false;
		
              public	boolean square9trivial = false;
              public	boolean squareBtrivial = false;
              public	boolean squareDtrivial = false;
              public	boolean squareAtrivial = false;
              public	boolean squareCtrivial = false;
              public	boolean squareEtrivial = false;
              public	boolean squareGtrivial = false;
              public	boolean squareHtrivial = false;
    

    /**
     * The faces (of head-parts) of all layers as sent by the server.
     */
    @NotNull
    private final Face[] faces = new Face[Map2.NUM_LAYERS];

    /**
     * If this square contains a non-head part of a multi-square object this
     * points to the head square.
     */
 //   @NotNull
 //   private final CfMapSquare[] heads = new CfMapSquare[Map2.NUM_LAYERS];

    /**
     * The smooth values of all layers as sent by the server.
     */
    @NotNull
    private final int[] smooths = new int[Map2.NUM_LAYERS];

    /**
     * Creates a new (empty) square.
     * @param map the map this map square is part of
     * @param x the absolute map x-coordinate of the top left corner of this
     * patch
     * @param y the absolute map y-coordinate of the top left corner of this
     * patch
     */
    public CfMapSquare(@NotNull final CfMap map, final int x, final int y) {
        this.map = map;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the absolute map x-coordinate of this square.
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the absolute map y-coordinate of this square.
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Marks this square as dirty, i.e., needing redraw.
     */
    public void dirty() {
        map.squareModified(this);
        
        // added this mechanic to help in redrawing
        Changed = true;
    }
    
   
    
    // more interface for redraw
    public void recentDraw(){
        Drawn = true;
    }
    
    // 03-21-2013 added this method to account for several passes to mapsquares
    public void floorDrawn(){
        Drawn = false;
    }
    
    // more interface for redraw
    public void finishDraw(){
        Drawn = false;
        Changed = false;
        drawRight =  false;
        drawLeft = false;
        drawTop = false;
        drawTopRight = false;
        drawTopLeft = false;
        drawUnderRight = false;
        drawUnderLeft = false;
		
        square9trivial = false;
        squareBtrivial = false;
        squareDtrivial = false;
        squareAtrivial = false;
        squareCtrivial = false;
        squareEtrivial = false;
        squareGtrivial = false;
        squareHtrivial = false;
    }
    
    // more interface for redraw
    public boolean isChanged(){
        return Changed;
    }
    
    // more interface for redraw
    public boolean wasDrawn(){
        return Drawn;
    }

    /**
     * Marks this square as 'fog-og-war'. The values will be still returned
     * until a new value will be set.
     */
    public void clear(int floor) {
        if(floor == 0){
            if (fogOfWar) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 0; layer < 3; layer++) {
                    
                   // if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if( faces[layer] != DEFAULT_FACE){
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 3){
                    return;
                }
            }

            fogOfWar = true;
        }
        if(floor == 1){
            if (fogOfWar2) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness2 == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 3; layer < 6; layer++) {
                    
                 //   if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if( faces[layer] != DEFAULT_FACE){
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 6){
                    return;
                }
            }

            fogOfWar2 = true;
        }
        if(floor == 2){
            if (fogOfWar3) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness3 == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 6; layer < 9; layer++) {
                    
                   // if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if (faces[layer] != DEFAULT_FACE){    
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 9){
                    return;
                }
            }

            fogOfWar3 = true;
        }
        if(floor == 3){
            if (fogOfWar4) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness4 == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 9; layer < 12; layer++) {
                   // if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if( faces[layer] != DEFAULT_FACE){
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 12){
                    return;
                }
            }

            fogOfWar4 = true;
        }
        if(floor == 4){
            if (fogOfWar5) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness5 == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 12; layer < 15; layer++) {
                  //  if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if ( faces[layer] != DEFAULT_FACE){
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 15){
                    return;
                }
            }

            fogOfWar5 = true;
        }
        if(floor == 5){
            if (fogOfWar6) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness6 == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 15; layer < 18; layer++) {
                   // if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if (faces[layer] != DEFAULT_FACE){
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 18){
                    return;
                }
            }

            fogOfWar6 = true;
        }
        if(floor == 6){
            if (fogOfWar7) {
                return;
            }

        // need to check individual values because the server sometimes sends a
        // "clear" command for already cleared squares; without this check the
        // black square would be displayed as fog-of-war
            if (darkness7 == DEFAULT_DARKNESS) {
                int layer;
             //   for (layer = 0; layer < faces.length; layer++) {
                for (layer = 18; layer < 21; layer++) {
                    
                  //  if (faces[layer] != DEFAULT_FACE || heads[layer] != null || smooths[layer] != 0) {
                    if (faces[layer] != DEFAULT_FACE){
                        break;
                    }
                }
           //     if (layer >= faces.length) {
                if(layer >= 21){
                    return;
                }
            }

            fogOfWar7 = true;
        }
        dirty();
        
    }

    /**
     * Sets the darkness value of this square.
     * @param darkness the new darkness value between <code>0</code> and
     * <code>255</code>; 0=dark, 255=full bright
     * @return whether fog-of-war has been cleared
     */
    public boolean setDarkness(final int darkness, final int floor) {
        if(floor == 0){
                final boolean result = fogOfWar;
                final boolean markDirty = fogOfWar || this.darkness != darkness;
                fogOfWar = false;
                this.darkness = darkness;
                if (markDirty) {
                    dirty();
                    // after some thought, decided to have only one set of dirtymapsquares
                    // and square.dirty and map.squaremodified
                }
                return result;
            }
        else if(floor == 1){
            final boolean result = fogOfWar2;
                final boolean markDirty = fogOfWar2 || this.darkness2 != darkness;
                fogOfWar2 = false;
                this.darkness2 = darkness;
                if (markDirty) {
                    dirty();
                }
                return result;
        }else if(floor == 2){
            final boolean result = fogOfWar3;
                final boolean markDirty = fogOfWar3 || this.darkness3 != darkness;
                fogOfWar3 = false;
                this.darkness3 = darkness;
                if (markDirty) {
                    dirty();
                }
                return result;
        } else if(floor == 3){
            final boolean result = fogOfWar4;
                final boolean markDirty = fogOfWar4 || this.darkness4 != darkness;
                fogOfWar4 = false;
                this.darkness4 = darkness;
                if (markDirty) {
                    dirty();
                }
                return result;
            
        } else if(floor == 4){
            final boolean result = fogOfWar5;
                final boolean markDirty = fogOfWar5 || this.darkness5 != darkness;
                fogOfWar5 = false;
                this.darkness5 = darkness;
                if (markDirty) {
                    dirty();
                }
                return result;
            
        } else if(floor == 5){
            final boolean result = fogOfWar6;
                final boolean markDirty = fogOfWar6 || this.darkness6 != darkness;
                fogOfWar6 = false;
                this.darkness6 = darkness;
                if (markDirty) {
                    dirty();
                }
                return result;
            
        } else if(floor == 6){
            final boolean result = fogOfWar7;
                final boolean markDirty = fogOfWar7 || this.darkness7 != darkness;
                fogOfWar7 = false;
                this.darkness7 = darkness;
                if (markDirty) {
                    dirty();
                }
                return result;
            
        } else {
            return false;
             // not sure, but trying to be consistent
            // see also isfogofwar, resetfogofwar
        }
        
        
        
    }

    /**
     * Returns the darkness value of this square.
     * @return the darkness value of the square; 0=dark, 255=full bright
     */
    public int getDarkness(final int floor) {
        if(floor == 0){
        return darkness;
        } else if (floor == 1){
            return darkness2;
        } else if (floor == 2){
            return darkness3;
        } else if (floor == 3){
            return darkness4;
        } else if (floor == 4){
            return darkness5;
        } else if (floor == 5){
            return darkness6;
        } else if (floor == 6){
            return darkness7;
        } else {
            return 0;
            // arbitrary, who knows what to do here
        }
    }

    /**
     * Sets the smooth value of this square.
     * @param layer the layer between <code>0</code> and <code>LAYERS-1</code>
     * @param smooth the new smooth value
     * @return whether fog-of-war has been cleared (1) or whether the smooth
     *         value has changed (2)
     */
    public int setSmooth(final int layer, final int smooth) {
        final boolean fogOfWarCleared = fogOfWar;
        final boolean smoothChanged = smooths[layer] != smooth;
        smooths[layer] = smooth;
        final boolean markDirty = fogOfWar || smoothChanged;
        fogOfWar = false;
        if (markDirty) {
            dirty();
        }
        return (fogOfWarCleared ? 1 : 0)|(smoothChanged ? 2 : 0);
    }

    /**
     * Returns the smooth value of this square.
     * @param layer the layer between <code>0</code> and <code>LAYERS-1</code>
     * @return the smooth value of the square
     */
    public int getSmooth(final int layer) {
        return smooths[layer];
    }

    /**
     * Sets the magic map color of this square.
     * @param color the new color
     * @return whether fog-of-war has been cleared
     */
    public boolean setColor(final int color) {
        final boolean result = fogOfWar;
        final boolean markDirty = fogOfWar || this.color != color;
        fogOfWar = false;
        this.color = color;
        if (markDirty) {
            dirty();
        }
        return result;
    }

    /**
     * Returns the magic map color of this square.
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the face of a layer.
     * @param layer the layer for the new face between <code>0</code> and
     * <code>LAYERS-1</code>
     * @param face the face to set
     */
    public void setFace(final int layer, @Nullable final Face face) {
        if (faces[layer] != face) {
            faces[layer] = face;
            dirty();
            
            // for now, use same dirtymapsquares and same squaremodified,
            // same square.dirty, regardless of floor
            // but x,y offset is done based on floor via mapupdaterstate
            // for use in guimap
        }
    }

    /**
     * Returns the face of a layer.
     * @param layer the layer to return the face
     * @return the face value
     */
    @Nullable
    public Face getFace(final int layer) {
        return faces[layer];
        // for now, use same dirtymapsquares and same squaremodified,
            // same square.dirty, regardless of floor
            // but x,y offset is done based on floor via mapupdaterstate
            // for use in guimap
        
        
    }

    /**
     * Sets the map square containing the head face for a layer.
     * @param layer the layer for the new head face between <code>0</code> and
     * <code>LAYERS-1</code>
     * @param mapSquare the map square containing the head face; may be
     * <code>null</code>
     * @param setAlways if set, always update the face; if unset, only update
     * when fog-of-war
     */
  //  public void setHeadMapSquare(final int layer, @Nullable final CfMapSquare mapSquare, final boolean setAlways) {
   //     if (heads[layer] != mapSquare && (setAlways || heads[layer] == null || heads[layer].isFogOfWar(layer))) {
   //         heads[layer] = mapSquare;
            // the transformation to the screen coordinates should preserve the relative x,y coordinates
            // of a bigface to its head, because all squares of a bigface are on same floor
    //        dirty();
             // for now, use same dirtymapsquares and same squaremodified,
            // same square.dirty, regardless of floor
            // but x,y offset is done based on floor via mapupdaterstate
            // for use in guimap
      //  }
   // }

    /**
     * Returns the map square of the head of a multi-square object.
     * @param layer the layer to return the head for
     * @return the head map square, or <code>null</code> if this square does not
     *         contain a multi-tail
     */
   // @Nullable
   // public CfMapSquare getHeadMapSquare(final int layer) {
        // suppress parts of fog-of-war objects if this square is not
        // fog-of-war
     //   if((0 <= layer)&&(layer < 3)){
       //     if (heads[layer] != null && !fogOfWar && heads[layer].fogOfWar) {
        //        return null;
        //    }
       // }
         // the transformation to the screen coordinates should preserve the relative x,y coordinates
            // of a bigface to its head, because all squares of a bigface are on same floor
        
     //   if((3 <= layer)&&(layer < 6)){
       //     if (heads[layer] != null && !fogOfWar2 && heads[layer].fogOfWar2) {
         //       return null;
       //     }
       // }
        
     //   if((6 <= layer)&&(layer < 9)){
       //     if (heads[layer] != null && !fogOfWar3 && heads[layer].fogOfWar3) {
         //       return null;
         //   }
      //  }
        
     //   if((9 <= layer)&&(layer < 12)){
       //     if (heads[layer] != null && !fogOfWar4 && heads[layer].fogOfWar4) {
         //       return null;
        //    }
      //  }
        
     //   if((12 <= layer)&&(layer < 15)){
       //     if (heads[layer] != null && !fogOfWar5 && heads[layer].fogOfWar5) {
         //       return null;
       //     }
     //   }
        
     //   if((15 <= layer)&&(layer < 18)){
       //     if (heads[layer] != null && !fogOfWar6 && heads[layer].fogOfWar6) {
         //       return null;
         //   }
      //  }
        
     //   if((18 <= layer)&&(layer < 21)){
       //     if (heads[layer] != null && !fogOfWar7 && heads[layer].fogOfWar7) {
         //       return null;
      //      }
      //  }
        

      //  return heads[layer];
   // }

    /**
     * Determines if the square is not up-to-date.
     * @return whether this square contains fog-of-war data
     */
    public boolean isFogOfWar(int layer) {
        if ((0 <= layer) && (layer < 3 )){
        return fogOfWar;
        } else if((3 <= layer) && (layer < 6)){
            return fogOfWar2;
        } else if((6 <= layer) && (layer < 9)){
            return fogOfWar3;
        } else if(( 9 <= layer) && ( layer < 12)){
            return fogOfWar4;
        } else if(( 12 <= layer) && ( layer < 15)){
            return fogOfWar5;
        } else if(( 15 <= layer) && ( layer < 18)){
            return fogOfWar6;
        } else if(( 18 <= layer) && (layer < 21)){
            return fogOfWar7;
        } else {
            return false;
            // not sure, but trying to be consistent
            // see also setdarkness, resetfogofwar
        }
    }
    // of course, this is an important method for both the map update
    // code from the server, and the drawing code in guimap

    /**
     * Returns and resets the "fog-of-war" flag.
     * @return whether this square's fog-of-war state has been reset
     */
    public boolean resetFogOfWar(final int floor) {
        if(floor == 0){
            if (!fogOfWar) {
              return false;
            }

            fogOfWar = false;
            dirty();
            // for now, use same dirtymapsquares and same squaremodified,
            // same square.dirty, regardless of floor
            // but x,y offset is done based on floor via mapupdaterstate
            // for use in guimap
            return true;  
            
            
        } else if (floor == 1){
           
            if (!fogOfWar2) {
                return false;
            }

            fogOfWar2 = false;
            dirty();
            return true;   
            
            
        } else if (floor == 2){
            if (!fogOfWar3) {
                return false;
            }

            fogOfWar3 = false;
            dirty();
            return true;   
            
            
        } else if (floor == 3){
            if (!fogOfWar4) {
                return false;
            }

            fogOfWar4 = false;
            dirty();
            return true;   
            
        } else if (floor == 4){
             if (!fogOfWar5) {
                return false;
            }

            fogOfWar5 = false;
            dirty();
            return true;   
            
        } else if (floor == 5){
             if (!fogOfWar6) {
                return false;
            }

            fogOfWar6 = false;
            dirty();
            return true;   
            
        } else if (floor == 6){
             if (!fogOfWar7) {
                return false;
            }

            fogOfWar7 = false;
            dirty();
            return true;   
            
        } else {
            return false;
            // nothing has been reset, because an invalid floor was entered
        }              
        
        
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return x+"/"+y;
    }

}
