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

package net.sf.gridarta.model.mapcontrol;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.autojoin.AutojoinListsHelper;
import net.sf.gridarta.model.exitconnector.ExitMatcher;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.DefaultMapReader;
import net.sf.gridarta.model.io.DefaultMapWriter;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.MapArchObjectParserFactory;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.io.TestMapArchObjectParserFactory;
import net.sf.gridarta.model.io.TestMapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObjectFactory;
import net.sf.gridarta.model.mapmanager.AbstractMapManager;
import net.sf.gridarta.model.mapmanager.DefaultMapManager;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.TestFileControl;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.mapmodel.TestMapModelHelper;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.match.TypeNrsGameObjectMatcher;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.settings.TestGlobalSettings;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class for creating {@link MapControl} instances for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestMapControlCreator {

    /**
     * The {@link MapManager} instance.
     */
    @NotNull
    private final MapManager<TestGameObject, TestMapArchObject, TestArchetype> mapManager;

    /**
     * The pickmap {@link MapManager} instance.
     */
    @NotNull
    private final MapManager<TestGameObject, TestMapArchObject, TestArchetype> pickmapManager;

    /**
     * The {@link PathManager} instance.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link ExitMatcher} instance.
     */
    @NotNull
    private final ExitMatcher<TestGameObject, TestMapArchObject, TestArchetype> exitMatcher;

    /**
     * The {@link MapArchObjectParserFactory} instance.
     */
    @NotNull
    private final MapArchObjectParserFactory<TestMapArchObject> mapArchObjectParserFactory;

    /**
     * The {@link GameObjectParser} instance.
     */
    @NotNull
    private final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser;

    /**
     * The {@link MapReaderFactory} instance.
     */
    @NotNull
    private final MapReaderFactory<TestGameObject, TestMapArchObject> mapReaderFactory;

    /**
     * The {@link GlobalSettings} instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapArchObjectFactory} instance.
     */
    @NotNull
    private final MapArchObjectFactory<TestMapArchObject> mapArchObjectFactory = new TestMapArchObjectFactory();

    /**
     * The {@link FileControl} instance.
     */
    @NotNull
    private final FileControl<TestGameObject, TestMapArchObject, TestArchetype> fileControl = new TestFileControl();

    /**
     * The {@link MapModelFactory} instance.
     */
    @NotNull
    private final MapModelFactory<TestGameObject, TestMapArchObject, TestArchetype> mapModelFactory;

    /**
     * The {@link MapControlFactory} instance.
     */
    @NotNull
    private final MapControlFactory<TestGameObject, TestMapArchObject, TestArchetype> mapControlFactory;

    /**
     * The {@link TestMapModelCreator} instance.
     */
    @NotNull
    private final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);

    /**
     * The {@link MapWriter} instance.
     */
    @NotNull
    private final MapWriter<TestGameObject, TestMapArchObject, TestArchetype> mapWriter;

    /**
     * Creates a new instance.
     */
    public TestMapControlCreator() {
        final GameObjectMatcher exitGameObjectMatcher = new TypeNrsGameObjectMatcher(TestMapModelHelper.EXIT_TYPE);
        exitMatcher = new ExitMatcher<TestGameObject, TestMapArchObject, TestArchetype>(exitGameObjectMatcher);
        mapArchObjectParserFactory = new TestMapArchObjectParserFactory();
        gameObjectParser = mapModelCreator.newGameObjectParser();
        mapReaderFactory = new TestMapReaderFactory(mapArchObjectParserFactory, mapArchObjectFactory, gameObjectParser, mapModelCreator.getMapViewSettings());
        globalSettings = new TestGlobalSettings();
        mapWriter = new DefaultMapWriter<TestGameObject, TestMapArchObject, TestArchetype>(mapArchObjectParserFactory, gameObjectParser);
        final ArchetypeChooserModel<TestGameObject, TestMapArchObject, TestArchetype> archetypeChooserModel = new ArchetypeChooserModel<TestGameObject, TestMapArchObject, TestArchetype>();
        pathManager = new PathManager(globalSettings);
        mapModelFactory = new MapModelFactory<TestGameObject, TestMapArchObject, TestArchetype>(archetypeChooserModel, mapModelCreator.getAutojoinLists(), mapModelCreator.getMapViewSettings(), mapModelCreator.getGameObjectFactory(), mapModelCreator.getGameObjectMatchers(), mapModelCreator.getTopmostInsertionMode());
        mapControlFactory = new TestMapControlFactory(mapWriter, globalSettings, mapModelFactory);
        final AbstractMapManager<TestGameObject, TestMapArchObject, TestArchetype> tmpMapManager = new DefaultMapManager<TestGameObject, TestMapArchObject, TestArchetype>(mapReaderFactory, mapControlFactory, globalSettings, mapModelCreator.getFaceObjectProviders());
        tmpMapManager.setFileControl(fileControl);
        mapManager = tmpMapManager;
        final AbstractMapManager<TestGameObject, TestMapArchObject, TestArchetype> tmpPickmapManager = new DefaultMapManager<TestGameObject, TestMapArchObject, TestArchetype>(mapReaderFactory, mapControlFactory, globalSettings, mapModelCreator.getFaceObjectProviders());
        tmpPickmapManager.setFileControl(fileControl);
        pickmapManager = tmpPickmapManager;
    }

    /**
     * Creates a new {@link TestMapModelHelper} instance.
     * @return the new instance
     * @throws DuplicateArchetypeException if an internal error occurs
     */
    public TestMapModelHelper newMapModelCreator() throws DuplicateArchetypeException {
        return mapModelCreator.newTestMapModelHelper();
    }

    /**
     * Creates a new map control.
     * @param mapFile the map file
     * @param mapName the map name
     * @param mapSize the map size
     * @return the map control
     */
    public MapControl<TestGameObject, TestMapArchObject, TestArchetype> newMapControl(@Nullable final File mapFile, @NotNull final String mapName, @NotNull final Size2D mapSize) {
        final TestMapArchObject mapArchObject = mapArchObjectFactory.newMapArchObject(false);
        mapArchObject.setMapSize(mapSize);
        mapArchObject.setMapName(mapName);
        return mapManager.newMap(null, mapArchObject, mapFile, true);
    }

    /**
     * Returns the {@link PathManager}.
     * @return the path manager
     */
    @NotNull
    public PathManager getPathManager() {
        return pathManager;
    }

    /**
     * Returns the {@link GlobalSettings}.
     * @return the global settings
     */
    @NotNull
    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    /**
     * Returns the {@link ExitMatcher}.
     * @return the exit matcher
     */
    @NotNull
    public ExitMatcher<TestGameObject, TestMapArchObject, TestArchetype> getExitMatcher() {
        return exitMatcher;
    }

    /**
     * Returns the {@link ArchetypeSet}.
     * @return the archetype set
     */
    @NotNull
    public ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> getArchetypeSet() {
        return mapModelCreator.getArchetypeSet();
    }

    /**
     * Returns the {@link MapManager}.
     * @return the map manager
     */
    @NotNull
    public MapManager<TestGameObject, TestMapArchObject, TestArchetype> getMapManager() {
        return mapManager;
    }

    /**
     * Returns the {@link InsertionModeSet}.
     * @return the insertion mode set
     */
    @NotNull
    public InsertionModeSet<TestGameObject, TestMapArchObject, TestArchetype> getInsertionModeSet() {
        return mapModelCreator.getInsertionModeSet();
    }

    /**
     * Returns the {@link FileControl} instance.
     * @return the file control instance
     */
    @NotNull
    public FileControl<TestGameObject, TestMapArchObject, TestArchetype> getFileControl() {
        return fileControl;
    }

    /**
     * Returns a new {@link AutojoinListsHelper} instance.
     * @return the autojoin lists helper instance
     */
    @NotNull
    public AutojoinListsHelper newAutojoinListsHelper() {
        return new AutojoinListsHelper(mapModelCreator);
    }

    /**
     * Returns the {@link TestMapModelCreator} instance.
     * @return the test map model creator instance
     */
    @NotNull
    public TestMapModelCreator getMapModelCreator() {
        return mapModelCreator;
    }

    /**
     * Returns a new {@link MapManager} instance.
     * @return the map manager instance
     */
    @NotNull
    public MapManager<TestGameObject, TestMapArchObject, TestArchetype> newMapManager() {
        return new DefaultMapManager<TestGameObject, TestMapArchObject, TestArchetype>(mapReaderFactory, mapControlFactory, globalSettings, mapModelCreator.getFaceObjectProviders());
    }

    /**
     * Returns the {@link MapArchObjectFactory} instance.
     * @return the test map arch object factory instance
     */
    @NotNull
    public MapArchObjectFactory<TestMapArchObject> getMapArchObjectFactory() {
        return mapArchObjectFactory;
    }

    /**
     * Returns the {@link MapModelFactory} instance.
     * @return the test map model factory instance
     */
    @NotNull
    public MapModelFactory<TestGameObject, TestMapArchObject, TestArchetype> getMapModelFactory() {
        return mapModelFactory;
    }

    /**
     * Returns the {@link MapManager pickmap manager} instance.
     * @return the pickmap manager instance
     */
    @NotNull
    public MapManager<TestGameObject, TestMapArchObject, TestArchetype> getPickmapManager() {
        return pickmapManager;
    }

    /**
     * Returns the {@link MapWriter} instance.
     * @return the map writer instance
     */
    @NotNull
    public MapWriter<TestGameObject, TestMapArchObject, TestArchetype> getMapWriter() {
        return mapWriter;
    }

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings to pass to the map reader
     * constructor
     * @param mapFile the file to pass to the map reader constructor
     * @return the map reader instance
     * @throws IOException if the map reader cannot be created
     */
    @NotNull
    public DefaultMapReader<TestGameObject, TestMapArchObject, TestArchetype> newMapReader(@NotNull final MapViewSettings mapViewSettings, @NotNull final File mapFile) throws IOException {
        return new DefaultMapReader<TestGameObject, TestMapArchObject, TestArchetype>(mapArchObjectParserFactory, mapArchObjectFactory, gameObjectParser, mapViewSettings, mapFile);
    }

}
