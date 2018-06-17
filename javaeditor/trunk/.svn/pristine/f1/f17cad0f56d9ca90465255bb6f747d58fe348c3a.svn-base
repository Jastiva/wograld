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

package net.sf.gridarta.model.resource;

import java.io.File;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link AbstractFilesResourcesReader} for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestFilesResourcesReader extends AbstractFilesResourcesReader<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * Creates a new instance.
     * @param archDirectory the "arch" directory to read
     * @param archetypeSet the archetype set to update
     * @param archetypeParser the archetype parser to use
     * @param archFaceProvider the arch face provider to use
     * @param collectedDirectory the collected directory
     * @param imageSet the active image set or <code>null</code>
     * @param animationObjects the animation objects instance
     * @param faceObjects the face objects instance
     */
    public TestFilesResourcesReader(@NotNull final File archDirectory, @NotNull final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet, @NotNull final AbstractArchetypeParser<TestGameObject, TestMapArchObject, TestArchetype, ?> archetypeParser, @NotNull final ArchFaceProvider archFaceProvider, @NotNull final File collectedDirectory, @Nullable final String imageSet, @NotNull final AnimationObjects animationObjects, @NotNull final FaceObjects faceObjects) {
        super(archDirectory, archetypeSet, archetypeParser, archFaceProvider, collectedDirectory, imageSet, animationObjects, faceObjects);
    }

    @Override
    protected boolean isValidEntry(final int folderLevel, final String name) {
        return true;
    }

}
