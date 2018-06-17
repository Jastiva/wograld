/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * An undefined {@link Archetype} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestUndefinedArchetype extends AbstractArchetype<TestGameObject, TestMapArchObject, TestArchetype> implements TestArchetype {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param archetypeName the name of the archetype
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    public TestUndefinedArchetype(@NotNull final String archetypeName, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(archetypeName, faceObjectProviders, animationObjects);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected TestArchetype getThis() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsesDirection(final boolean usesDirection) {
        throw new UnsupportedOperationException();
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
    @Override
    public boolean usesDirection() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public TestArchetype clone() {
        return super.clone();
    }

}
