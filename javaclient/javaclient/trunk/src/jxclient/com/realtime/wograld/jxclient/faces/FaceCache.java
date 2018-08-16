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

package com.realtime.wograld.jxclient.faces;

import com.realtime.wograld.jxclient.server.wograld.WograldFaceListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

/**
 * A cache for {@link Face} instances.
 * @author Andreas Kirschbaum
 */
public class FaceCache {

    /**
     * The cached faces. Empty slots are set to <code>null</code>.
     */
    @NotNull
    private final Face[] faces = new Face[65536];

    /**
     * The listener to receive face commands.
     */
    @NotNull
    private final WograldFaceListener wograldFaceListener = new WograldFaceListener() {

        @Override
        public void faceReceived(final int faceNum, final int faceSetNum, final int faceChecksum, final int faceQuad, @NotNull final String faceName) {
            // XXX: ignores faceSetNum
            if (faces[faceNum] != null) {
                System.err.println("Warning: defining duplicate face "+faceNum+" ("+faceName+")");
                return;
                // do not allow replacing faces that have faceimages, with ones that dont, concurrently
                // but this is a crude way to try to fix that
                // also, as of may 2013 the server does not send faces on event of spell list, but may fix
                // but does for both inventory and for standard map
            }
            if(faceChecksum == 0){
                System.err.println("Warning: received zero checksum face on "+faceNum+" ("+faceName+")");
                return;
                // because the server does not as of may 2013 generate faces for spell list
                // the client is expected to generate them from the index with no checksum
                // hence any received faces with checksum of 0, might collision with faces for spell icons
                // the result is that askfacefacequeue retains record of needing but not getting the face, but server
                // has record of sending it, ah well
                // this code does not deal with the separate issue of checksum collisions
                // or of the fact that server tries to retrieve image based on index rather than checksum 
                // or of the fact that askfacefacequeue does not compare checksums on received images from server
                // but file caching does
                
            }
            faces[faceNum] = new Face(faceNum, faceName, faceChecksum);
            if(faceNum > 10000){
                faces[faceNum].setTopPartStatus(true);
                /* quad = 4 + 1 = 5, due to issues below and bits */
                faces[faceNum].setFaceQuad(5);
            } else {
            /* encoding issue; the bottom portion of an image may or may not reference */
            /* a top portion as well as covering portions of east and north tiles */
            /* but at the moment, the top face itself does not itself have a top portion */
            /* on the other hand, the face command is only sent for the bottom portion */

            
            faces[faceNum].setFaceQuad(faceQuad);
            }
            /* constructor for Face defaults quad to value of 7 */
            
            // a quick test of the later procedure, comment out later to restore connectivity
      /*      if((faceNum > 2196) && (faceNum < 2213)){
                faces[faceNum].setFaceQuad(0);
                // creating a temporary problem with cwall
                // actually, looking up by name would be more robust to server changes/alternate server
            }
             // if number and bmap name do not line up, then it is because server graphics were changed
             * // so of course this code would not work then; hence it is temporary and unimportant
            
            if((faceNum > 7799) && (faceNum < 7824)){
                faces[faceNum].setFaceQuad(0);
            }  */
            
     /*       if(faceNum == 3560){
                faces[faceNum].setFaceQuad(0);
                // grass
            }
            
            if((faceNum > 2103) && (faceNum < 2109)){
                faces[faceNum].setFaceQuad(0);
                //cobblestones
            }  
            
            if(faceNum == 7794){
                faces[faceNum].setFaceQuad(0);
                //woodfloor
                
            }
            
            if(faceNum == 5083){
                faces[faceNum].setFaceQuad(0);
                // no magic
            }
            
            if(faceNum == 5084){
                faces[faceNum].setFaceQuad(0);
                // no spells
            }
            
            if(faceNum == 2824){
                faces[faceNum].setFaceQuad(0);
                // dungeon floor, as found on top of zoo
            }
            
            if(faceNum == 2531){
                faces[faceNum].setFaceQuad(0);
                // dirtfloor, as found on top of zoo
            }
            
            if(faceNum == 5565){
                faces[faceNum].setFaceQuad(0);
                //pstone1, as in zoo lower level
            }
            
            if((faceNum > 2369) && (faceNum < 2374)){
                faces[faceNum].setFaceQuad(0);
                // deep sea
            }
            
            if((faceNum > 4241) && (faceNum < 4247)){
                faces[faceNum].setFaceQuad(0);
                // lava2
            }
            
            if(faceNum == 6149){
                faces[faceNum].setFaceQuad(0);
                //shop_empty, which is also used in player creation room
            }  */
                    
            
           
      /*      if((faceNum > 1619) && (faceNum < 1650)){
                faces[faceNum].setFaceQuad(0);
            }  */
            
        }

    };

    /**
     * Creates a new instance.
     */
    public FaceCache() {
        faces[0] = new Face(0, "empty", 0);
    }

    /**
     * Creates a new instance.
     * @param wograldServerConnection the server connection to use
     */
    public FaceCache(@NotNull final WograldServerConnection wograldServerConnection) {
        this();
        wograldServerConnection.addWograldFaceListener(wograldFaceListener);
    }

    /**
     * Adds a new face to the cache.
     * @param face the face to add
     */
    public void addFace(@NotNull final Face face) {
        faces[face.getFaceNum()] = face;
    }

    /**
     * Returns a face by face id.
     * @param faceNum the face id to look up
     * @return the face
     */
    
    public Face getFace(final int faceNum) {
        final Face face = faces[faceNum];
        if (face != null) {
            return face;
        }

        System.err.println("Warning: accessing undefined face "+faceNum);
        
      /*  if(faceNum > 10000){
                Face face2 = faces[faceNum-10000];
                if(face2 != null){
                 faces[faceNum] = new Face(faceNum, face2.getFaceName()+"ceil",face2.getFaceChecksum());
                 // so it isnt the same checksum
                 // are we sure the server does not send this
                 faces[faceNum].setTopPartStatus(true);
                 return faces[faceNum];
                 
                }
                return null;
        }
        else {  */
            return null;
     //   }
      /*  faces[faceNum] = new Face(faceNum, "face#"+faceNum, 0);
        if(faceNum > 10000){
            faces[faceNum].setTopPartStatus(true);
           
        }
        return faces[faceNum];  */
    }
    
    public Face getSpellFace(final int faceNum){
        final Face face = faces[faceNum];
        if (face != null) {
            return face;
        }

        System.err.println("Warning: accessing undefined face "+faceNum);
        
        
            faces[faceNum] = new Face(faceNum, "face#"+faceNum, 0);
            return face;
        
        
    }

    /**
     * Forgets about all face information. Should be called when connecting to a
     * Wograld server.
     */
    public void reset() {
        Arrays.fill(faces, null);
        faces[0] = new Face(0, "empty", 0);
    }

}
