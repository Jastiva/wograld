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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.IOUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link MapReader}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
// myInput.close() is invoked in this.close()
public class DefaultMapReader<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapReader<G, A> {

    /**
     * ArchObjects that are read from the map.
     */
    @NotNull
    private final List<G> objects = new ArrayList<G>();

    /**
     * Contains the map arch object.
     */
    @NotNull
    private final A mapArchObject;

    /**
     * Open a file for reading it as a map.
     * @param mapArchObjectParserFactory the map arch object parser factory
     * instance
     * @param mapArchObjectFactory the map arch object factory instance
     * @param gameObjectParser the game object parser instance
     * @param mapViewSettings the map view settings instance
     * @param file the file to open
     * @throws IOException in case the file couldn't be read
     * @throws InvalidMapFormatException in case the file is in wrong format
     */
    public DefaultMapReader(@NotNull final MapArchObjectParserFactory<A> mapArchObjectParserFactory, @NotNull final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final GameObjectParser<G, A, R> gameObjectParser, @NotNull final MapViewSettings mapViewSettings, @NotNull final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        try {
            final InputStreamReader isr = new InputStreamReader(fis, IOUtils.MAP_ENCODING);
            try {
                final BufferedReader br = new BufferedReader(isr);
                try {
                    mapArchObject = mapArchObjectFactory.newMapArchObject(false);
                    mapArchObjectParserFactory.newMapArchObjectParser().load(br, mapArchObject);
                    while (gameObjectParser.load(br, objects) != null) {
                    }
                } finally {
                    br.close();
                }
            } finally {
                isr.close();
            }
        } finally {
            fis.close();
        }

        gameObjectParser.collectTempList(mapViewSettings, objects);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public A getMapArchObject() {
        return mapArchObject;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<G> getGameObjects() {
        return objects;
    }

}
