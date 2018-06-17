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

import java.awt.Point;
import java.io.IOException;
import java.io.Writer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link MapWriter}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DefaultMapWriter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapWriter<G, A, R> {

    /**
     * The {@link MapArchObjectParserFactory} to use.
     */
    @NotNull
    private final MapArchObjectParserFactory<A> mapArchObjectParserFactory;

    /**
     * The {@link GameObjectParser} to use.
     */
    @NotNull
    private final GameObjectParser<G, A, R> gameObjectParser;

    /**
     * Creates a new instance.
     * @param mapArchObjectParserFactory the map arch object parser factory to
     * use
     * @param gameObjectParser the game object parser to use
     */
    public DefaultMapWriter(@NotNull final MapArchObjectParserFactory<A> mapArchObjectParserFactory, @NotNull final GameObjectParser<G, A, R> gameObjectParser) {
        this.mapArchObjectParserFactory = mapArchObjectParserFactory;
        this.gameObjectParser = gameObjectParser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeMapFile(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Writer writer) throws IOException {
        final A mapArch = mapModel.getMapArchObject();

        mapArchObjectParserFactory.newMapArchObjectParser().save(writer, mapArch);

        // first, write all one tile parts
        final Size2D mapSize = mapArch.getMapSize();
        final int width = mapSize.getWidth();
        final int height = mapSize.getHeight();
        final Point pos = new Point();
        for (pos.x = 0; pos.x < width; pos.x++) {
            for (pos.y = 0; pos.y < height; pos.y++) {
                for (final G gameObject : mapModel.getMapSquare(pos)) {
                    if (gameObject.isHead()) {
                        gameObjectParser.save(writer, gameObject);
                    }
                } // node
            } // y
        } // x
    }

}
