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

package net.sf.gridarta.var.crossfire.model.archetype;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.AbstractArchetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The class <code>UndefinedArchetype</code> implements an undefined Crossfire
 * archetype.
 * @author Andreas Kirschbaum
 */
public class UndefinedArchetype extends AbstractArchetype<GameObject, MapArchObject, Archetype> implements Archetype {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new undefined archetype with the given archetype name.
     * @param archetypeName the archetype name
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    public UndefinedArchetype(@NotNull final String archetypeName, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(archetypeName, faceObjectProviders, animationObjects);
        setEditorFolder(net.sf.gridarta.model.gameobject.GameObject.EDITOR_FOLDER_INTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUndefinedArchetype() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public UndefinedArchetype clone() {
        return (UndefinedArchetype) super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean usesDirection() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected Archetype getThis() {
        return this;
    }

}
