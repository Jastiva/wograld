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
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for classes that read or write {@link MapArchObject} instances.
 * @author Andreas Kirschbaum
 */
public interface MapArchObjectParser<A extends MapArchObject<A>> {

    /**
     * Parse a map arch object from a stream.
     * @param reader the stream to read
     * @param mapArchObject the map arch object to update
     * @throws IOException if the stream cannot be read or if a parsing error
     * occurs; the map arch object may have been partially updated
     */
    void load(@NotNull BufferedReader reader, @NotNull A mapArchObject) throws IOException;

    /**
     * Write a map arch object to a stream.
     * @param appendable the stream to write to
     * @param mapArchObject the map arch object to write
     * @throws IOException if the stream cannot be written
     */
    void save(@NotNull Appendable appendable, @NotNull A mapArchObject) throws IOException;

}
