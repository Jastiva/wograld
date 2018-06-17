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

package net.sf.gridarta.model.gameobject;

import javax.swing.ImageIcon;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GameObject} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestGameObject extends AbstractGameObject<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param archetype the base archetype
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    public TestGameObject(@NotNull final TestArchetype archetype, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(archetype, faceObjectProviders, animationObjects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean usesDirection() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ImageIcon getImage(@NotNull final MapViewSettings mapViewSettings) {
        return getNormalImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLightRadius() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagateElevation(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isScripted() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public TestGameObject clone() {
        return super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected TestGameObject getThis() {
        return this;
    }

}
