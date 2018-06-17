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

package net.sf.gridarta.model.face;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.gridarta.model.data.AbstractNamedObject;
import net.sf.gridarta.model.data.NamedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A FaceObject stores information and meta information about a face and
 * provides methods for accessing these. Mainly this is: <ul> <li>Face name</li>
 * <li>Weakly referenced Image Icon for different appearances of the face</li>
 * <li>File name of file where the face originally came from</li> <li>File name
 * where the face actually was loaded from (may be the same or different from
 * the previous one)</li> <li>File offset in the file name</li> <li>File
 * size</li> </ul>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo think how this class could be serialized because size, offset etc. are
 * not serializable.
 */
public class DefaultFaceObject extends AbstractNamedObject implements FaceObject {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * A {@link Pattern} matching face names having alternative face names.
     */
    @NotNull
    private static final Pattern ALTERNATIVE_FACE_NAME_PATTERN = Pattern.compile("\\.a\\.");

    /**
     * The face name.
     * @serial
     */
    @NotNull
    private final String faceName;

    /**
     * The filename the face originally came from (origin location).
     * @serial
     */
    private final String originalFilename;

    /**
     * The offset in the file the face was loaded from.
     * @serial
     */
    private final int offset;

    /**
     * The size in the file the face was loaded from.
     * @serial
     */
    private final int size;

    /**
     * Whether this face is a "double" face.
     * @serial
     * @see "Usage FACE_FLAG_DOUBLE in client/src/client.c, client/src/map.c
     *      defined by client/src/include/main.h"
     */
    private final boolean isDouble;

    /**
     * Whether this face is an "up" face.
     * @serial
     * @see "Usage FACE_FLAG_UP in client/src/client.c, client/src/map.c defined
     *      by client/src/include/main.h"
     */
    private final boolean isUp;

    /**
     * The alternative face name for image.a.nnn faces.
     * @serial
     */
    @Nullable
    private final String alternativeFaceName;
    
    // These fields are loaded from arch files
    // and stored into a collected file called "faces"
    // which server uses for some protocol features.
    // They are meant to be associated with bmaps, not archs
    // which is why they are stored here.
    
    // They are useless to the editor function itself,
    // and do not need to be loaded to editor when loading from collect
    // Hope that this does not skew the intended role of faceobjects
    // Copying them over from objecttext, should make them easier and faster
    // to get from collect, than getting them from objecttext later
    // Or should they be stored in archetypes (instances) until the collect
    // process is run?
    
    // It is of course possible for some archetypes to not have animations
    // and it is possible for there to be more than one arc that lists an
    // attribute for the face.  If there is no animation then just write to the one face, 
    // else the convention seems to be to copy the attribute to all
    // faces of the animation. If there is more than one nonempty source of an attribute
    // for the face, ignore the new one
    // Perhaps an exception should be thrown?
    
    // quad could be made a bitfield?
    // the three bits of it are detailed on jwogclient
    // perhaps also the editor could describe it for each arch
    // and perhaps it is just a crude form of multishapeID
   
    private String visibility_str="";
    
    private String magicmap_str="";
    
    private String color_fg_str="";
    
    private String color_bg_str="";
    
    private String is_floor_str="";
    
    private String quad_str="";

    /**
     * Create a FaceObject.
     * @param faceName name of face, e.g. <samp>"bug.111"</samp>
     * @param originalFilename original filename without .png extension, e.g.
     * <samp>"/system/bug.111"</samp>
     * @param offset offset in the file denoted by <var>actualFilename</var>,
     * e.g. <samp>148676</samp>
     * @param size size in the file denoted by <var>actualFilename</var>, e.g.
     * <samp>567</samp>,
     */
    public DefaultFaceObject(final String faceName, final String originalFilename, final int offset, final int size) {
        super(originalFilename);
        this.faceName = faceName;
        this.originalFilename = originalFilename;
        this.offset = offset;
        this.size = size;
        isUp = faceName.contains(".u.");
        isDouble = faceName.contains(".d.");
        final Matcher matcher = ALTERNATIVE_FACE_NAME_PATTERN.matcher(faceName);
        alternativeFaceName = matcher.find() ? matcher.replaceFirst(".b.").intern() : null;
    }

    /**
     * {@inheritDoc}
     * @return the same String as <code>getFaceName()</code>
     */
    @NotNull
    @Override
    public String getName() {
        return faceName;
    }

    /**
     * {@inheritDoc}
     * @see #getName()
     */
    @NotNull
    @Override
    public String getFaceName() {
        return faceName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * Get the offset of this face in the actualFilename.
     * @return offset of this face in the actual file
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Get the size of this face in the actualFilename.
     * @return size of this face in the actual file
     */
    public int getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUp() {
        return isUp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDouble() {
        return isDouble;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getAlternativeFaceName() {
        return alternativeFaceName;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getDisplayIconName() {
        return faceName;
    }

    /**
     * {@inheritDoc} Overridden to sort bug.101 and bug.111 before all other
     * faces.
     */
    @Override
    public int compareTo(@NotNull final NamedObject o) {
        final boolean iAmBug = faceName.equals("bug.111") || faceName.equals("bug.101");
        final boolean otherIsBug = o.getName().equals("bug.111") || o.getName().equals("bug.101");
        if (iAmBug && otherIsBug) {
            return 0;
        }
        if (iAmBug) {
            return -1;
        }
        if (otherIsBug) {
            return 1;
        }
        return super.compareTo(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return faceName.hashCode() ^ super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final DefaultFaceObject defaultFaceObject = (DefaultFaceObject) obj;
        return faceName.equals(defaultFaceObject.faceName) && super.equals(obj);
    }
    
    public boolean setVisibility(String vis){
        if(!vis.isEmpty()){
            if(visibility_str.isEmpty()){
            visibility_str=vis;
            return true;
            }
            else {
                if(visibility_str.equals(vis)){
                    return true;
                } else {
                return false;
                }
            }
        } else {
            return true;
        }
    }
    
    public boolean setMagicmap(String magicmap){
        if(!magicmap.isEmpty()){
            if(magicmap_str.isEmpty()){
            magicmap_str=magicmap;
            return true;
            }
            else {
                 if(magicmap_str.equals(magicmap)){
                    return true;
                } else {
                return false;
                }
            }
        } else {
            return true;
        }
        
    }
    
    public boolean setForegroundColor(String colorFg){
        if(!colorFg.isEmpty()){
            if(color_fg_str.isEmpty()){
            color_fg_str=colorFg;
            return true;
            }
            else {
                 if(color_fg_str.equals(colorFg)){
                    return true;
                } else {
                return false;
                }
            }
            } else {
            return true;
        }
        
    }
    
    public boolean setBackgroundColor(String colorBg){
        if(!colorBg.isEmpty()){
            if(color_bg_str.isEmpty()){
            color_bg_str=colorBg;
            return true;
            }
            else {
                if(color_bg_str.equals(colorBg)){
                    return true;
                } else {
                return false;
                }
            }
        } else {
            return true;
        }
    }
    
    public boolean setIsFloor(String isFl){
        if(!isFl.isEmpty()){
            if(is_floor_str.isEmpty()){
            is_floor_str=isFl;
            return true;
            }
            else {
                if(is_floor_str.equals(isFl)){
                    return true;
                } else {
                return false;
                }
            }
        } else {
            return true;
        }
        
    }
    
    public boolean setQuad(String quadrant){
        if(!quadrant.isEmpty()){
            if(quad_str.isEmpty()){
            quad_str=quadrant;
            return true;
            }
            else {
                if(quad_str.equals(quadrant)){
                    return true;
                } else {
                return false;
                }
            }
        } else {
            return true;
        }
        
    }
    
    public String getVisibility(){
        return visibility_str;
    }
    
    public String getMagicmap(){
        return magicmap_str;
    }
    
    public String getForegroundColor(){
        return color_fg_str;
    }
    
    public String getBackgroundColor(){
        return color_bg_str;
    }
    
    public String getIsFloor(){
        return is_floor_str;
    }
    
    public String getFaceQuad(){
        return quad_str;
    }

}
