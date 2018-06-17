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

package net.sf.gridarta.model.artifact;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestArchetypeBuilder;
import net.sf.gridarta.model.archetype.TestArchetypeFactory;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.DefaultArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.errorview.TestErrorView;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.gameobject.TestGameObjectFactory;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.TestArchetypeParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for artifacts definitions.
 * @author Andreas Kirschbaum
 */
public class TestParser {

    /**
     * The {@link FaceObjectProviders} instance.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link ArchetypeSet} instance.
     */
    @NotNull
    private final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet;

    /**
     * The {@link AnimationObjects} instance.
     */
    @NotNull
    private final AnimationObjects animationObjects = new TestAnimationObjects();

    /**
     * The {@link ErrorViewCollector} instance.
     */
    @NotNull
    private final ErrorViewCollector errorViewCollector;

    /**
     * The {@link ArtifactParser} instance.
     */
    @NotNull
    private final ArtifactParser<TestGameObject, TestMapArchObject, TestArchetype> artifactParser;

    /**
     * Creates a new instance.
     */
    public TestParser() {
        this(new TestErrorView());
    }

    /**
     * Creates a new instance.
     * @param errorView the error view to use for parsing
     */
    public TestParser(@NotNull final TestErrorView errorView) {
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final File file = new File("*string*");
        errorViewCollector = new ErrorViewCollector(errorView, file);
        final FaceObjects faceObjects = new TestFaceObjects();
        faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final TestArchetypeFactory archetypeFactory = new TestArchetypeFactory(faceObjectProviders, animationObjects);
        final TestGameObjectFactory gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        archetypeSet = new DefaultArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype>(archetypeFactory, null);
        final TestArchetypeBuilder archetypeBuilder = new TestArchetypeBuilder(gameObjectFactory);
        final AbstractArchetypeParser<TestGameObject, TestMapArchObject, TestArchetype, TestArchetypeBuilder> archetypeParser = new TestArchetypeParser(archetypeBuilder, animationObjects, archetypeSet);
        final List<TestGameObject> invObjects = new ArrayList<TestGameObject>();
        artifactParser = new ArtifactParser<TestGameObject, TestMapArchObject, TestArchetype>(archetypeSet, errorView, archetypeParser, invObjects);
    }

    /**
     * Adds a new archetype.
     * @param archetypeName the archetype name
     * @param attributes the archetype's attributes; may be empty
     * @throws DuplicateArchetypeException if the archetype name is not unique
     */
    public void addArchetype(@NotNull final String archetypeName, @NotNull final String... attributes) throws DuplicateArchetypeException {
        final TestArchetype baseArchetype = new TestDefaultArchetype(archetypeName, faceObjectProviders, animationObjects);
        final StringBuilder objectText = new StringBuilder();
        for (final String attribute : attributes) {
            objectText.append(attribute);
            objectText.append('\n');
        }
        baseArchetype.setObjectText(objectText.toString());
        archetypeSet.addArchetype(baseArchetype);
    }

    /**
     * Parses artifacts definitions.
     * @param artifacts the artifacts definitions
     * @throws IOException if parsing fails
     */
    public void parseArtifacts(@NotNull final String artifacts) throws IOException {
        final Reader reader = new StringReader(artifacts);
        final BufferedReader bufferedReader = new BufferedReader(reader);
        try {
            artifactParser.loadArtifact(bufferedReader, errorViewCollector, "", "panel", "folder");
        } finally {
            bufferedReader.close();
        }
    }

    /**
     * Returns an {@link Archetype} by name.
     * @param archetypeName the archetype name
     * @return the archetype
     * @throws UndefinedArchetypeException if the archetype name is undefined
     */
    @NotNull
    public Archetype<TestGameObject, TestMapArchObject, TestArchetype> getArchetype(@NotNull final String archetypeName) throws UndefinedArchetypeException {
        return archetypeSet.getArchetype(archetypeName);
    }

    /**
     * Returns the number of defined archetypes.
     * @return the number of defined archetypes
     */
    public int getArchetypeCount() {
        return archetypeSet.getArchetypeCount();
    }

}
