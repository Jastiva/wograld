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

package net.sf.gridarta.model.archetype;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.AnimationObject;
import net.sf.gridarta.model.baseobject.AbstractBaseObject;
import net.sf.gridarta.model.baseobject.BaseObjectVisitor;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.MultiArchData;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link Archetype} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractArchetype<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractBaseObject<G, A, R, R> implements Archetype<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of this archetype.
     * @serial
     */
    @NotNull
    private String archetypeName;

    /**
     * The x-distance of this part to the head part. Set to zero for single-part
     * objects.
     * @serial
     */
    private int multiX;

    /**
     * The y-distance of this part to the head part. Set to zero for single-part
     * objects.
     * @serial
     */
    private int multiY;

    /**
     * Set if this part of a multi-part object is the lowest part. The lowest
     * part is the part with the minimum y screen coordinate. Set to
     * <code>true</code> for single-part objects.
     * @serial
     */
    private boolean isLowestPart = true;

    /**
     * The multi shape id.
     * @serial
     */
    private int multiShapeID;

    /**
     * The multi part id.
     * @serial
     */
    private int multiPartNr;

    /**
     * The location in the archetype selector.
     * @serial
     */
    @Nullable
    private String editorFolder;

    /**
     * If this flag is set, this Archetype is not a "real" Archetype but comes
     * from the artifacts file. Such Archetypes instances are not included in
     * the Archetype collection process, since the artifacts file is the same
     * for editor and server.
     * @serial
     */
    private boolean artifact;
    
   
     // fields to write to faceobjects once lines to be filled are filled,
    // and once faceobjects and animationobjects are loaded
    // of course, editor does not need these for functions other than
    // to write to a collect output
    private String visibility_str="";
    
    private String magicmap_str="";
    
    private String color_fg_str="";
    
    private String color_bg_str="";
    
    private String is_floor_str="";
    
    private String quad_str="";

     @Nullable
    private final AnimationObjects animationObjects;
     
    @NotNull
    private final transient FaceObjectProviders faceObjectProviders;
     
    /**
     * Creates a new instance.
     * @param archetypeName the name of the archetype
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    protected AbstractArchetype(@NotNull final String archetypeName, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(faceObjectProviders, animationObjects);
        this.archetypeName = archetypeName.intern();
        this.animationObjects=animationObjects;
        this.faceObjectProviders=faceObjectProviders;
        updateArchetype();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MapSquare<G, A, R> getMapSquare() {
        return null; // archetypes are never part of a container
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getAttributeString(@NotNull final String attributeName, final boolean queryArchetype) {
        final String result = getAttributeValue(attributeName);
        return result == null ? "" : result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    protected String getEffectiveFaceName(@NotNull final String faceName) {
        final String normalizedFaceName = faceName.length() > 0 ? faceName.intern() : null;
        //Strings are interned
        //noinspection StringEquality
        return normalizedFaceName != null && normalizedFaceName.length() > 0 ? normalizedFaceName : null;
    }

    /**
     * {@inheritDoc}
     * @noinspection NoopMethodInAbstractClass
     */
    @Override
    public void notifyBeginChange() {
        // ignore
    }

    /**
     * {@inheritDoc}
     * @noinspection NoopMethodInAbstractClass
     */
    @Override
    public void notifyEndChange() {
        // ignore
    }

    /**
     * {@inheritDoc}
     * @noinspection NoopMethodInAbstractClass
     */
    @Override
    public void notifyTransientChange() {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public R getArchetype() {
        return getThis();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public R clone() {
        //noinspection OverriddenMethodCallDuringObjectConstruction
        final AbstractArchetype<G, A, R> clone = (AbstractArchetype<G, A, R>) super.clone();
        return clone.getThis();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G asGameObject() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G newInstance(@NotNull final GameObjectFactory<G, A, R> gameObjectFactory) {
        return gameObjectFactory.createGameObject(getThis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final BaseObjectVisitor<G, A, R> baseObjectVisitor) {
        baseObjectVisitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getArchetypeName() {
        return archetypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArchetypeName(@NotNull final String archetypeName) {
        this.archetypeName = archetypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiX(final int multiX) {
        this.multiX = multiX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiY(final int multiY) {
        this.multiY = multiY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMultiX() {
        return multiX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMultiY() {
        return multiY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLowestPart() {
        return isLowestPart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLowestPart(final boolean isLowestPart) {
        this.isLowestPart = isLowestPart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMultiShapeID() {
        return multiShapeID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiShapeID(final int multiShapeID) {
        this.multiShapeID = multiShapeID;
        final MultiArchData<G, A, R, R> multi = getMulti();
        if (multi != null) {
            multi.setMultiShapeID(multiShapeID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMultiPartNr() {
        return multiPartNr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiPartNr(final int multiPartNr) {
        this.multiPartNr = multiPartNr;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getEditorFolder() {
        return editorFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEditorFolder(@Nullable final String editorFolder) {
        this.editorFolder = editorFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isArtifact() {
        return artifact;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArtifact() {
        artifact = true;
    }
    
    public void set_visibility(String vis){
        visibility_str=vis;
    }
    
     public void set_magicmap(String magicmap){
     magicmap_str=magicmap;
     }
               
     public void setForegroundColor(String colorFg ){
          color_fg_str=colorFg;
     }
         
     
     
     public void setBackgroundColor(String colorBg){
         color_bg_str=colorBg;
     }
     
     public void setFloorStr(String isFl){
      is_floor_str=isFl;

     }
     
     public void setQuad(String quadrant){
         quad_str=quadrant; 
     }
     
     public void augmentFacedata(){
       
         if((faceObjectProviders != null) && (faceName != null)){
             // faceName is now protected in abstractbaseobject, but do not write to it from here
            FaceObject defaultFace = faceObjectProviders.getFaceObFromName(faceName );
            if(defaultFace != null){
                augmentOneFace(defaultFace);
                
            }
         }
         if((animationObjects != null)&& (animName != null)){
      AnimationObject anim = animationObjects.get(animName);
            if(anim != null){
                FaceObject animFrame;
                
                int anFacings = anim.getFacings();
                int anFrameCount = anim.getFrameCount();
                // facings at least start with 0
                for( int dir=0; dir < anFacings; dir++){
                    for( int count=0; count < anFrameCount; count++){
                        String animName=anim.getFrame(dir, count);
                        if(!animName.isEmpty()){
                        animFrame=faceObjectProviders.getFaceObFromName(animName);
                        if(animFrame != null){
                                augmentOneFace(animFrame);
                            }
                        }
                        
                    } 
                }
            }
         } 
     }
     
     @Override
     public void augmentOneFace(FaceObject faceob){
          boolean notDup = faceob.setVisibility(visibility_str);
                if(notDup == false){
                    // duplicate visibility
                }
                notDup = faceob.setMagicmap(magicmap_str);
                if(notDup == false){
                    // duplicate magicmap
                }
                notDup = faceob.setForegroundColor(color_fg_str);
                if(notDup == false){
                    // duplicate color_fg
                }
                notDup = faceob.setBackgroundColor(color_bg_str);
                if(notDup == false){
                    // duplicate color_bg
                }
                notDup = faceob.setIsFloor(is_floor_str);
                if(notDup == false){
                    // duplicate is_floor in archetype
                }
                notDup = faceob.setQuad(quad_str);
                if(notDup == false){
                    // duplicate quad
                }
     }

}
