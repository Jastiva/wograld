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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import net.sf.gridarta.model.anim.AnimationObject;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestArchetypeBuilder;
import net.sf.gridarta.model.archetype.TestArchetypeFactory;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.DefaultArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.TestErrorView;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.gameobject.TestGameObjectFactory;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.TestArchetypeParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.DefaultActionBuilder;
import net.sf.japi.util.IteratorEnumeration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link AbstractFilesResourcesReader}.
 * @author Andreas Kirschbaum
 */
public class AbstractFilesResourcesReaderTest {

    /**
     * The {@link Pattern} for matching validator keys for default values.
     */
    @NotNull
    private static final Pattern PATTERN_DEFAULT_KEY = Pattern.compile("fileDialog\\.filter\\..*");

    /**
     * checks that animations read from .anim files have correct path names.
     * @throws IOException if the test fails
     */
    @Test
    public void testAnimationPaths() throws IOException {
        final File tmpDirectory = File.createTempFile("gridarta", null);
        try {
            Assert.assertTrue(tmpDirectory.delete());
            Assert.assertTrue(tmpDirectory.mkdir());
            final File archDirectory = new File(tmpDirectory, "archetypes");
            Assert.assertTrue(archDirectory.mkdir());
            final File collectedDirectory = new File(tmpDirectory, "collected");
            Assert.assertTrue(collectedDirectory.mkdir());
            createAnimationFile(archDirectory, "anim1");
            createAnimationFile(new File(archDirectory, "p"), "anim2");
            createAnimationFile(new File(new File(new File(archDirectory, "p"), "q"), "r"), "anim3");

            final ActionBuilder actionBuilder = new DefaultActionBuilder("net.sf.gridarta");
            ActionBuilderFactory.getInstance().putActionBuilder("net.sf.gridarta", actionBuilder);
            final ResourceBundle resourceBundle = new ResourceBundle() {

                /**
                 * Maps key to associated object.
                 */
                @NotNull
                private final Map<String, Object> objects = new HashMap<String, Object>();

                @Nullable
                @Override
                protected Object handleGetObject(@NotNull final String key) {
                    final Object existingObject = objects.get(key);
                    if (existingObject != null) {
                        return existingObject;
                    }

                    final Object object;
                    if (PATTERN_DEFAULT_KEY.matcher(key).matches()) {
                        object = "Description";
                    } else {
                        return null;
                    }
                    objects.put(key, object);
                    return object;
                }

                @NotNull
                @Override
                public Enumeration<String> getKeys() {
                    return new IteratorEnumeration<String>(Collections.unmodifiableSet(objects.keySet()).iterator());
                }

            };
            actionBuilder.addBundle(resourceBundle);

            final AnimationObjects animationObjects = readAnimations(archDirectory, collectedDirectory);

            Assert.assertEquals(3, animationObjects.size());
            final AnimationObject anim1 = animationObjects.get("anim1");
            final AnimationObject anim2 = animationObjects.get("anim2");
            final AnimationObject anim3 = animationObjects.get("anim3");
            Assert.assertNotNull(anim1);
            Assert.assertNotNull(anim2);
            Assert.assertNotNull(anim3);
            Assert.assertEquals("anim1", anim1.getAnimName());
            Assert.assertEquals("anim2", anim2.getAnimName());
            Assert.assertEquals("anim3", anim3.getAnimName());
            Assert.assertEquals("/anim1", anim1.getPath());
            Assert.assertEquals("/p/anim2", anim2.getPath());
            Assert.assertEquals("/p/q/r/anim3", anim3.getPath());
        } finally {
            delete(tmpDirectory);
        }
    }

    /**
     * Reads resources from individual files.
     * @param archDirectory the arch directory
     * @param collectedDirectory the collected directory
     * @return the animation objects instance
     */
    @NotNull
    private static AnimationObjects readAnimations(@NotNull final File archDirectory, @NotNull final File collectedDirectory) {
        final ArchFaceProvider archFaceProvider = new ArchFaceProvider();
        final FaceObjects faceObjects = new TestFaceObjects();
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(1, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final ArchetypeFactory<TestGameObject, TestMapArchObject, TestArchetype> archetypeFactory = new TestArchetypeFactory(faceObjectProviders, animationObjects);
        final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet = new DefaultArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype>(archetypeFactory, null);
        final GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        final TestArchetypeBuilder archetypeBuilder = new TestArchetypeBuilder(gameObjectFactory);
        final AbstractArchetypeParser<TestGameObject, TestMapArchObject, TestArchetype, ?> archetypeParser = new TestArchetypeParser(archetypeBuilder, animationObjects, archetypeSet);
        final AbstractResourcesReader<TestGameObject> filesResourcesReader = new TestFilesResourcesReader(archDirectory, archetypeSet, archetypeParser, archFaceProvider, collectedDirectory, null, animationObjects, faceObjects);
        final List<TestGameObject> invObjects = new ArrayList<TestGameObject>();
        final ErrorView errorView = new TestErrorView();
        filesResourcesReader.read(errorView, invObjects);
        return animationObjects;
    }

    /**
     * Creates a new .anim file.
     * @param directory the directory to create the file in; will be created if
     * it does not yet exist
     * @param animName the name of the animation
     * @throws IOException if an I/O error occurs
     */
    private static void createAnimationFile(@NotNull final File directory, @NotNull final String animName) throws IOException {
        if (!directory.isDirectory()) {
            Assert.assertTrue(directory.mkdirs());
        }

        final File file = new File(directory, animName + ".anim");
        Assert.assertFalse(file.exists());
        final OutputStream fileOutputStream = new FileOutputStream(file);
        try {
            final Writer outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            try {
                final Writer bufferedWriter = new BufferedWriter(outputStreamWriter);
                try {
                    bufferedWriter.append("anim ").append(animName).append("\n");
                    bufferedWriter.append("face1\n");
                    bufferedWriter.append("face2\n");
                    bufferedWriter.append("mina\n");
                } finally {
                    bufferedWriter.close();
                }
            } finally {
                outputStreamWriter.close();
            }
        } finally {
            fileOutputStream.close();
        }
    }

    /**
     * Recursively deletes a directory.
     * @param file the file or directory to delete
     */
    private static void delete(@NotNull final File file) {
        //noinspection ResultOfMethodCallIgnored
        file.delete();

        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (final File subFile : files) {
                    delete(subFile);
                }
            }
        }

        if (file.exists()) {
            Assert.assertTrue(file.delete());
        }
    }

}
