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

package net.sf.gridarta.model.io;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link MapReaderFactory} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestMapReaderFactory implements MapReaderFactory<TestGameObject, TestMapArchObject> {

    /**
     * The {@link MapArchObjectParserFactory} instance.
     */
    @NotNull
    private final MapArchObjectParserFactory<TestMapArchObject> mapArchObjectParserFactory;

    /**
     * The {@link MapArchObjectFactory} instance.
     */
    @NotNull
    private final MapArchObjectFactory<TestMapArchObject> mapArchObjectFactory;

    /**
     * The {@link GameObjectParser} instance.
     */
    @NotNull
    private final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser;

    /**
     * The {@link MapViewSettings} instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * Creates a new instance.
     * @param mapArchObjectParserFactory the map arch object parser factory
     * instance
     * @param mapArchObjectFactory the map arch object factory instance
     * @param gameObjectParser the game object parser instance
     * @param mapViewSettings the map view settings instance
     */
    public TestMapReaderFactory(@NotNull final MapArchObjectParserFactory<TestMapArchObject> mapArchObjectParserFactory, @NotNull final MapArchObjectFactory<TestMapArchObject> mapArchObjectFactory, @NotNull final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser, @NotNull final MapViewSettings mapViewSettings) {
        this.mapArchObjectParserFactory = mapArchObjectParserFactory;
        this.mapArchObjectFactory = mapArchObjectFactory;
        this.gameObjectParser = gameObjectParser;
        this.mapViewSettings = mapViewSettings;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapReader<TestGameObject, TestMapArchObject> newMapReader(@NotNull final File file) throws IOException {
        return new DefaultMapReader<TestGameObject, TestMapArchObject, TestArchetype>(mapArchObjectParserFactory, mapArchObjectFactory, gameObjectParser, mapViewSettings, file);
    }

}
