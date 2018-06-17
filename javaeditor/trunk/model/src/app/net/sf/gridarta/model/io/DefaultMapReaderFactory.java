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
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link MapReaderFactory}.
 * @author Andreas Kirschbaum
 */
public class DefaultMapReaderFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapReaderFactory<G, A> {

    /**
     * The {@link MapArchObjectFactory} instance.
     */
    @NotNull
    private final MapArchObjectFactory<A> mapArchObjectFactory;

    /**
     * The {@link MapArchObjectParserFactory} instance.
     */
    @NotNull
    private final MapArchObjectParserFactory<A> mapArchObjectParserFactory;

    /**
     * The {@link GameObjectParserFactory} instance.
     */
    @NotNull
    private final GameObjectParserFactory<G, A, R> gameObjectParserFactory;

    /**
     * The {@link MapViewSettings} instance to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * Creates a new instance.
     * @param mapArchObjectFactory the map arch object factory instance
     * @param mapArchObjectParserFactory the map arch object parser factory
     * instance
     * @param gameObjectParserFactory the game object parser factory instance
     * @param mapViewSettings the map view settings instance
     */
    public DefaultMapReaderFactory(@NotNull final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final MapArchObjectParserFactory<A> mapArchObjectParserFactory, @NotNull final GameObjectParserFactory<G, A, R> gameObjectParserFactory, @NotNull final MapViewSettings mapViewSettings) {
        this.mapArchObjectFactory = mapArchObjectFactory;
        this.mapArchObjectParserFactory = mapArchObjectParserFactory;
        this.gameObjectParserFactory = gameObjectParserFactory;
        this.mapViewSettings = mapViewSettings;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapReader<G, A> newMapReader(@NotNull final File file) throws IOException {
        final GameObjectParser<G, A, R> gameObjectParser = gameObjectParserFactory.newGameObjectParser();
        return new DefaultMapReader<G, A, R>(mapArchObjectParserFactory, mapArchObjectFactory, gameObjectParser, mapViewSettings, file);
    }

}
