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
import net.sf.gridarta.model.anim.DefaultAnimationObjects;
import net.sf.gridarta.model.face.DefaultFaceObjects;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.archetype.DefaultArchetype;
import org.jetbrains.annotations.NotNull;

/**
 * Creates {@link GameObject GameObjects}.
 * @author Andreas Kirschbaum
 */
public class GameObjectCreator {

    /**
     * The {@link Archetype} for created game objects.
     */
    @NotNull
    private final Archetype archetype;

    /**
     * The {@link FaceObjectProviders} for created game objects.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link AnimationObjects} for created game objects.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * Creates a new instance.
     */
    public GameObjectCreator() {
        final FaceObjects faceObjects = new DefaultFaceObjects(false);
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        faceObjectProviders = new FaceObjectProviders(1, faceObjects, systemIcons);
        animationObjects = new DefaultAnimationObjects();
        archetype = new DefaultArchetype("arch", faceObjectProviders, animationObjects);
    }

    /**
     * Creates a new {@link GameObject}.
     * @param elevation the game object's elevation
     * @return the new game object
     */
    @NotNull
    public GameObject newGameObject(final int elevation) {
        final GameObject gameObject = new GameObject(archetype, faceObjectProviders, animationObjects);
        if (elevation != 0) {
            gameObject.setAttributeInt(GameObject.ELEVATION, elevation);
        }
        return gameObject;
    }

}
