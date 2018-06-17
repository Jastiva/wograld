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

package net.sf.gridarta.var.atrinik.resource;

import java.io.File;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.FaceProvider;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.resource.AbstractCollectedResourcesReader;
import net.sf.gridarta.var.atrinik.IGUIConstants;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Loads all resources from collected files.
 * @author Andreas Kirschbaum
 */
public class CollectedResourcesReader extends AbstractCollectedResourcesReader<GameObject, MapArchObject, Archetype> {

    /**
     * Creates a new instance.
     * @param collectedDirectory the collected directory
     * @param archetypeSet the archetype set to update
     * @param archetypeParser the archetype parser to use
     * @param faceObjects the face objects instance
     * @param animationObjects the animation objects instance
     */
    public CollectedResourcesReader(@NotNull final File collectedDirectory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final AbstractArchetypeParser<GameObject, MapArchObject, Archetype, ?> archetypeParser, @NotNull final FaceObjects faceObjects, @NotNull final AnimationObjects animationObjects) {
        super(collectedDirectory, null, archetypeSet, archetypeParser, animationObjects, faceObjects, IGUIConstants.ANIMTREE_FILE, IGUIConstants.ARCH_FILE);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FaceProvider read(@NotNull final ErrorView errorView, @NotNull final List<GameObject> invObjects) {
        loadAnimations(errorView);
        loadArchetypes(errorView, invObjects);
        return loadFacesCollection(errorView);
    }

}
