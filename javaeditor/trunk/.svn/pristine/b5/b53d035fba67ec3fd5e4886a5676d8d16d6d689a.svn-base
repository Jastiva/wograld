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
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for classes that read or write {@link GameObject} instances.
 * @author Andreas Kirschbaum
 */
public interface GameObjectParser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Parse a game object from a stream.
     * @param reader the stream to read
     * @param objects if non-<code>null</code>, add the read game objects to
     * this collection
     * @return the read game object, or <code>null</code> if the end of the
     *         stream was reached
     * @throws IOException if the stream cannot be read or if a parsing error
     * occurs
     */
    @Nullable
    G load(@NotNull BufferedReader reader, @Nullable Collection<G> objects) throws IOException;

    /**
     * Parse a game object from a stream.
     * @param reader the stream to read
     * @param firstLine the first line to parse before reading from
     * <code>reader</code>
     * @param objects if non-<code>null</code>, add the read game objects to
     * this collection
     * @return the read game object, or <code>null</code> if the end of the
     *         stream was reached
     * @throws IOException if the stream cannot be read or if a parsing error
     * occurs
     */
    @Nullable
    G load(@NotNull BufferedReader reader, @NotNull String firstLine, @Nullable Collection<G> objects) throws IOException;

    /**
     * Write a game object object to a stream.
     * @param appendable the stream to write to
     * @param gameObject the game object to write
     * @throws IOException if the stream cannot be written
     */
    void save(@NotNull Appendable appendable, @NotNull G gameObject) throws IOException;

    /**
     * Browse first through the archetype list and attach map arches to it then
     * browse through the face list and try to find the pictures.
     * @param mapViewSettings the map view settings instance
     * @param objects the list of game objects to collect
     */
    void collectTempList(@NotNull MapViewSettings mapViewSettings, List<G> objects);

    /**
     * Returns the modified fields of a {@link GameObject}. These fields are
     * written into map files.
     * @param gameObject the game object
     * @return the modified fields in writing order
     */
    @NotNull
    Map<String, String> getModifiedFields(@NotNull G gameObject);

    /**
     * Adds the modified fields of a {@link GameObject} to a map.
     * @param gameObject the game object
     * @param fields the map to update
     */
    void addModifiedFields(@NotNull G gameObject, @NotNull Map<String, String> fields);

}
