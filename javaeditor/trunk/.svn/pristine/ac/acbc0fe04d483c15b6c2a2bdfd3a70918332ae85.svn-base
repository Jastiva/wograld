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

package net.sf.gridarta.var.atrinik.model.gameobject;

import javax.swing.ImageIcon;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.DefaultIsoGameObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The <code>GameObject</code> class handles the Atrinik GameObjects. Usually,
 * an GameObject that is really used is derived from a (replaceable) default
 * arch.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo this class is not always the best place for multi-part object handling,
 * see also {@link net.sf.gridarta.model.baseobject.GameObjectContainer} for
 * issues about this
 */
public class GameObject extends DefaultIsoGameObject<GameObject, MapArchObject, Archetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ArchetypeTypeSet} for looking up archetype types.
     */
    @NotNull
    private final transient ArchetypeTypeSet archetypeTypeSet;

    /**
     * Creates a new instance.
     * @param archetype the base archetype
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     * @param archetypeTypeSet the archetype type set for looking up archetype
     * types
     */
    public GameObject(@NotNull final Archetype archetype, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        super(archetype, faceObjectProviders, animationObjects);
        this.archetypeTypeSet = archetypeTypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLast(@NotNull final GameObject gameObject) {
        // force type change when a MONSTER is put in a spawn point
        if (archetypeTypeSet.getArchetypeTypeByBaseObject(this).getTypeNo() == Archetype.TYPE_SPAWN_POINT && archetypeTypeSet.getArchetypeTypeByBaseObject(gameObject).getTypeNo() == Archetype.TYPE_MOB) {
            gameObject.setAttributeInt(TYPE, Archetype.TYPE_SPAWN_POINT_MOB); // change to SPAWN_POINT_MOB
        }
        super.addLast(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFirst(@NotNull final GameObject gameObject) {
        // force type change when a MONSTER is put in a spawn point
        if (archetypeTypeSet.getArchetypeTypeByBaseObject(this).getTypeNo() == Archetype.TYPE_SPAWN_POINT && archetypeTypeSet.getArchetypeTypeByBaseObject(gameObject).getTypeNo() == Archetype.TYPE_MOB) {
            gameObject.setAttributeInt(TYPE, Archetype.TYPE_SPAWN_POINT_MOB); // change to SPAWN_POINT_MOB
        }
        super.addFirst(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagateElevation(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        if (getAttributeInt(Z) == 0) {
            final int elevation = gameObject.getAttributeInt(Z, false);
            setAttributeInt(Z, elevation);
        }
        gameObject.removeAttribute(Z);
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
    @NotNull
    @Override
    protected GameObject getThis() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isScripted() {
        for (final BaseObject<?, ?, ?, ?> tmp : this) {
            if (tmp.getTypeNo() == Archetype.TYPE_EVENT_OBJECT) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ImageIcon getImage(@NotNull final MapViewSettings mapViewSettings) {
        final boolean drawDouble = getAttributeInt(DRAW_DOUBLE_ALWAYS) != 0 || (mapViewSettings.isDoubleFaces() && getAttributeInt(DRAW_DOUBLE) != 0);
        if (mapViewSettings.isAlphaType(getEditType())) {
            if (drawDouble) {
                return getTransparentDoubleImage();
            } else {
                return getTransparentImage();
            }
        } else {
            if (drawDouble) {
                return getDoubleImage();
            } else {
                return getNormalImage();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLightRadius() {
        return getAttributeInt(GLOW_RADIUS);
    }

}
