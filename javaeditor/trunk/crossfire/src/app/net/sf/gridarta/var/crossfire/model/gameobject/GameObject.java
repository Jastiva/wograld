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

package net.sf.gridarta.var.crossfire.model.gameobject;

import javax.swing.ImageIcon;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.AbstractGameObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The <code>GameObject</code> class handles the Crossfire GameObjects. Usually,
 * an GameObject that is really used is derived from a (replaceable) default
 * arch.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author serpentshard
 * @todo this class is not always the best place for multi-part object handling,
 * see also {@link net.sf.gridarta.model.baseobject.GameObjectContainer} for
 * issues about this
 */
public class GameObject extends AbstractGameObject<GameObject, MapArchObject, Archetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the "elevation" attribute.
     */
    @NotNull
    public static final String ELEVATION = "elevation";

    /**
     * The name of the "glow_radius" attribute.
     */
    @NotNull
    public static final String GLOW_RADIUS = "glow_radius";

    /**
     * The name of the "invisible" attribute.
     */
    @NotNull
    public static final String INVISIBLE = "invisible";

    /**
     * The name of the "smoothlevel" attribute.
     */
    @NotNull
    public static final String SMOOTHLEVEL = "smoothlevel";

    /**
     * Creates a new instance.
     * @param archetype the base archetype
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    public GameObject(@NotNull final Archetype archetype, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(archetype, faceObjectProviders, animationObjects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagateElevation(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        final int elevation = gameObject.getAttributeInt(ELEVATION, false);
        setAttributeInt(ELEVATION, elevation);
        gameObject.removeAttribute(ELEVATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean usesDirection() {
        return false; //getAttributeInt("is_turnable") != 0;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObject clone() {
        return super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isScripted() {
        for (final BaseObject<?, ?, ?, ?> tmp : this) {
            if (tmp.getTypeNo() == Archetype.TYPE_EVENT_CONNECTOR) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefaultGameObject() {
        if (!super.isDefaultGameObject()) {
            return false;
        }

        //XXX:  return loreText.equals(archetype.getLoreText())
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqual(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        return super.isEqual(gameObject) && gameObject.getLoreText().equals(getLoreText());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected GameObject getThis() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ImageIcon getImage(@NotNull final MapViewSettings mapViewSettings) {
        return getNormalImage();
    }
    
    @NotNull
  //  @Override
    public ImageIcon getSecondImg(){
        return getSecondImage();
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLightRadius() {
        return getAttributeInt(GLOW_RADIUS);
    }

}
