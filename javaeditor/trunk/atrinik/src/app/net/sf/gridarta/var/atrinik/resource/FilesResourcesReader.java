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

package net.sf.gridarta.var.atrinik.resource;

import java.io.File;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.FaceProvider;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.resource.AbstractFilesResourcesReader;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Loads all resources from individual files.
 * @author Andreas Kirschbaum
 */
public class FilesResourcesReader extends AbstractFilesResourcesReader<GameObject, MapArchObject, Archetype> {

    /**
     * The "arch" directory to read.
     */
    @NotNull
    private final File archDirectory;

    /**
     * The {@link ArchFaceProvider} to use.
     */
    @NotNull
    private final ArchFaceProvider archFaceProvider;

    /**
     * Creates a new instance.
     * @param archDirectory the "arch" directory to read
     * @param collectedDirectory the collected directory
     * @param archetypeSet the archetype set to update
     * @param archetypeParser the archetype parser to use
     * @param faceObjects the face objects instance
     * @param animationObjects the animation objects instance
     * @param archFaceProvider the arch face provider to use
     */
    public FilesResourcesReader(@NotNull final File archDirectory, @NotNull final File collectedDirectory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final AbstractArchetypeParser<GameObject, MapArchObject, Archetype, ?> archetypeParser, @NotNull final FaceObjects faceObjects, @NotNull final AnimationObjects animationObjects, @NotNull final ArchFaceProvider archFaceProvider) {
        super(archDirectory, archetypeSet, archetypeParser, archFaceProvider, collectedDirectory, null, animationObjects, faceObjects);
        this.archDirectory = archDirectory;
        this.archFaceProvider = archFaceProvider;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FaceProvider read(@NotNull final ErrorView errorView, @NotNull final List<GameObject> invObjects) {
        addPNGFace(new File(archDirectory, "dev/editor/bug.101.png").getAbsolutePath(), "/dev/editor", "bug.101.png", errorView, archFaceProvider);
        return super.read(errorView, invObjects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isValidEntry(final int folderLevel, final String name) {
        return folderLevel != 1 || !name.equalsIgnoreCase("dev");
    }

}
