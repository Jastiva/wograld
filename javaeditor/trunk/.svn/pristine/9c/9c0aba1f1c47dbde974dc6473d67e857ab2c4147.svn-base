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

package net.sf.gridarta.var.crossfire.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.collectable.AnimationObjectsCollectable;
import net.sf.gridarta.model.collectable.FaceObjectsCollectable;
import net.sf.gridarta.model.collectable.SmoothFacesCollectable;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.resource.CollectedResourcesWriter;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.smoothface.SmoothFaces;
import net.sf.gridarta.var.crossfire.IGUIConstants;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.collectable.CrossfireArchetypeSetCollectable;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;

/**
 * A resource loader for Crossfire resources.
 * @author Andreas Kirschbaum
 */
public class DefaultResources extends AbstractResources<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link GameObjectParser} to use.
     */
    @NotNull
    private final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser;

    /**
     * The {@link ArchetypeSet} to update.
     */
    @NotNull
    private final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet;

    /**
     * The {@link AbstractArchetypeParser} to use.
     */
    @NotNull
    private final AbstractArchetypeParser<GameObject, MapArchObject, Archetype, ?> archetypeParser;

    /**
     * The {@link FaceObjects} instance.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * The {@link AnimationObjects} instance.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * The {@link SmoothFaces} instance.
     */
    @NotNull
    private final SmoothFaces smoothFaces;

    /**
     * The {@link ArchFaceProvider} to use.
     */
    @NotNull
    private final ArchFaceProvider archFaceProvider;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Creates a new instance.
     * @param gameObjectParser the game object parser instance
     * @param archetypeSet the archetype set to update
     * @param archetypeParser the archetype parser to use
     * @param mapViewSettings the map view settings instance
     * @param faceObjects the face objects instance
     * @param animationObjects the animation objects instance
     * @param smoothFaces the smooth faces instance
     * @param archFaceProvider the arch face provider to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public DefaultResources(@NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final AbstractArchetypeParser<GameObject, MapArchObject, Archetype, ?> archetypeParser, @NotNull final MapViewSettings mapViewSettings, @NotNull final FaceObjects faceObjects, @NotNull final AnimationObjects animationObjects, @NotNull final SmoothFaces smoothFaces, @NotNull final ArchFaceProvider archFaceProvider, @NotNull final FaceObjectProviders faceObjectProviders) {
        super(gameObjectParser, archetypeSet, mapViewSettings);
        this.gameObjectParser = gameObjectParser;
        this.archetypeSet = archetypeSet;
        this.archetypeParser = archetypeParser;
        this.faceObjects = faceObjects;
        this.animationObjects = animationObjects;
        this.smoothFaces = smoothFaces;
        this.archFaceProvider = archFaceProvider;
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readFilesInt(@NotNull final GlobalSettings globalSettings, @NotNull final ErrorView errorView, @NotNull final List<GameObject> invObjects) {
        faceObjectProviders.setNormal(new FilesResourcesReader(globalSettings.getArchDirectory(), globalSettings.getCollectedDirectory(), archetypeSet, archetypeParser, faceObjects, animationObjects, archFaceProvider).read(errorView, invObjects));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected List<GameObject> readCollectedInt(@NotNull final GlobalSettings globalSettings, @NotNull final ErrorView errorView) {
        final List<GameObject> invObjects = new ArrayList<GameObject>();
        faceObjectProviders.setNormal(new CollectedResourcesReader(globalSettings.getConfigurationDirectory(), globalSettings.getCollectedDirectory(), archetypeSet, archetypeParser, faceObjects, animationObjects, smoothFaces).read(errorView, invObjects));
        return invObjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeCollectedInt(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
        final CollectedResourcesWriter collectedResourcesWriter = new CollectedResourcesWriter();
        collectedResourcesWriter.addCollectable(new CrossfireArchetypeSetCollectable(archetypeSet, gameObjectParser));
        collectedResourcesWriter.addCollectable(new AnimationObjectsCollectable(animationObjects, IGUIConstants.ANIMTREE_FILE));
        collectedResourcesWriter.addCollectable(new FaceObjectsCollectable(faceObjects, archFaceProvider));
        collectedResourcesWriter.addCollectable(new SmoothFacesCollectable(smoothFaces, IGUIConstants.SMOOTH_FILE));
        collectedResourcesWriter.write(progress, collectedDirectory);
    }

}
