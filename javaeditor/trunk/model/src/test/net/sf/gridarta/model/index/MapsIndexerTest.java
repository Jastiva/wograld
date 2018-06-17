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

package net.sf.gridarta.model.index;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.TestMapControlCreator;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.settings.TestGlobalSettings;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link MapsIndexer}.
 * @author Andreas Kirschbaum
 */
public class MapsIndexerTest {

    /**
     * Checks that basic indexing works as expected.
     * @throws InterruptedException if the test fails
     * @throws IOException if the test fails
     */
    @Test
    public void test1() throws InterruptedException, IOException {
        final GlobalSettings globalSettings = new TestGlobalSettings();
        final TestMapControlCreator mapControlCreator = new TestMapControlCreator();
        final MapManager<TestGameObject, TestMapArchObject, TestArchetype> mapManager = mapControlCreator.newMapManager();
        final File mapsDirectory = createMapsDirectory(mapControlCreator, globalSettings, "map1:Map1", "path/map2:Map Name2");
        try {
            final MapsIndex index = new MapsIndex();
            globalSettings.setMapsDirectory(mapsDirectory);
            final MapsIndexer<TestGameObject, TestMapArchObject, TestArchetype> indexer = new MapsIndexer<TestGameObject, TestMapArchObject, TestArchetype>(index, mapManager, globalSettings);
            indexer.start();
            indexer.waitForIdle();
            indexer.stop();
            Assert.assertEquals(2, index.findPartialName("").size());
            assertEquals(index.findPartialName("Map1"), mapsDirectory, "map1");
            assertEquals(index.findPartialName("Map Name2"), mapsDirectory, "path/map2");
        } finally {
            deleteTempDir(mapsDirectory);
        }
    }

    /**
     * Checks that a {@link Collection} of {@link File Files} contains exactly
     * one expected value. Fails the test otherwise.
     * @param mapPaths the collection of files
     * @param mapsDirectory the maps directory
     * @param mapPath the map path (relative to mapsDirectory) of the expected
     * value
     */
    private static void assertEquals(@NotNull final Collection<File> mapPaths, @NotNull final File mapsDirectory, @NotNull final String mapPath) {
        Assert.assertEquals(1, mapPaths.size());
        final Iterator<File> it = mapPaths.iterator();
        Assert.assertEquals(new File(mapsDirectory, mapPath), it.next());
    }

    /**
     * Creates a maps directory consisting of a set of maps. Each "spec" value
     * describes a map to create. Its format is "<map path>:<map name>", for
     * example "path/to/map:Name of Map".
     * @param mapControlCreator the map control creator for creating the maps
     * @param globalSettings the global settings to use
     * @param specs the maps to create
     * @return the maps directory
     * @throws IOException if the maps directory could not be created
     */
    @NotNull
    private static File createMapsDirectory(@NotNull final TestMapControlCreator mapControlCreator, @NotNull final GlobalSettings globalSettings, @NotNull final String... specs) throws IOException {
        final File mapsDirectory = createTempDir("gridarta");
        @Nullable File directoryToDelete = mapsDirectory;
        try {
            for (final String spec : specs) {
                final String[] tmp = StringUtils.PATTERN_COLON.split(spec, 2);
                if (tmp.length != 2) {
                    throw new IllegalArgumentException();
                }

                final File mapFile = new File(mapsDirectory, tmp[0]);
                final String mapName = tmp[1];

                if (!mapFile.getParentFile().exists() && !mapFile.getParentFile().mkdirs()) {
                    throw new IOException("cannot create directory: " + mapFile.getParentFile());
                }

                final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapControlCreator.getMapModelCreator().newMapModel(5, 5);
                final DefaultMapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = new DefaultMapControl<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, false, mapControlCreator.getMapWriter(), globalSettings);
                mapControl.getMapModel().getMapArchObject().setMapName(mapName);
                mapControl.saveAs(mapFile);
            }
            directoryToDelete = null;
        } finally {
            if (directoryToDelete != null) {
                deleteTempDir(directoryToDelete);
            }
        }
        return mapsDirectory;
    }

    /**
     * Creates an empty directory in the default temporary directory using the
     * given prefix.
     * @param prefix the prefix string to be used in generating the directory's
     * name; must be at least three characters long.
     * @return a newly-created empty directory
     * @throws IOException if no directory could be created
     */
    @NotNull
    private static File createTempDir(@NotNull final String prefix) throws IOException {
        final String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir == null) {
            throw new IOException("the system property 'java.io.tmpdir' does not exist");
        }

        final File tmpFile = new File(tmpDir);
        if (!tmpFile.exists() && !tmpFile.mkdirs()) {
            throw new IOException("cannot create temporary directory " + tmpFile);
        }

        final File result = File.createTempFile(prefix, null);
        if (!result.delete()) {
            throw new IOException("cannot delete temporary file " + result);
        }
        if (!result.mkdir()) {
            throw new IOException("cannot create temporary directory " + result);
        }

        return result;
    }

    /**
     * Deletes a temporary directory.
     * @param dir the directory
     */
    private static void deleteTempDir(final File dir) {
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    deleteTempDir(file);
                } else if (!file.delete()) {
                    Assert.fail("cannot delete file " + file);
                }
            }
        }
        if (!dir.delete()) {
            Assert.fail("cannot delete directory " + dir);
        }
    }

}
