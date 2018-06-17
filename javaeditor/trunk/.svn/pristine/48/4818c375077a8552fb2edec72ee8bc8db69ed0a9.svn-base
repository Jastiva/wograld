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

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.AbstractGameObjectFactory;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.archetype.DefaultArchetype;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link net.sf.gridarta.model.gameobject.GameObjectFactory} to create
 * Crossfire related game objects.
 * @author Andreas Kirschbaum
 */
public class DefaultGameObjectFactory extends AbstractGameObjectFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link FaceObjectProviders} to use.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link AnimationObjects} for looking up animations.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers to use
     * @param animationObjects the animation objects for looking up animations
     */
    public DefaultGameObjectFactory(@NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        this.faceObjectProviders = faceObjectProviders;
        this.animationObjects = animationObjects;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Archetype newArchetype(@NotNull final String archetypeName) {
        return new DefaultArchetype(archetypeName, faceObjectProviders, animationObjects);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObject createGameObject(@NotNull final Archetype archetype) {
        return createGameObjectPart(archetype, null);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObject createGameObjectPart(@NotNull final Archetype archetype, @Nullable final GameObject head) {
        final GameObject gameObject = new GameObject(archetype, faceObjectProviders, animationObjects);
        if (head != null) {
            head.addTailPart(gameObject);
        }
        return gameObject;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObject cloneGameObject(@NotNull final GameObject gameObject) {
        return gameObject.clone();
    }

}
