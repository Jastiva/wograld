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
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for classes implementing {@link MapArchObjectParser
 * MapArchObjectParsers}. This class contains the common code for reading map
 * game objects. Subclasses can extend the parser ({@link #parseLine(String,
 * MapArchObject, BufferedReader)}. No support for writing is present.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapArchObjectParser<A extends MapArchObject<A>> implements MapArchObjectParser<A> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(@NotNull final BufferedReader reader, @NotNull final A mapArchObject) throws IOException {
        final String firstLine = reader.readLine();
        if (firstLine == null) {
            throw new IOException("unexpected end of file in map header");
        }

        final String trimmedFirstLine = StringUtils.removeTrailingWhitespace(firstLine);

        if (!trimmedFirstLine.equals("arch map")) {
            throw new InvalidMapFormatException("unexpected first line of map file: '" + trimmedFirstLine + "', expected 'arch map'");
        }

        int width = 0;
        int height = 0;
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                throw new IOException("unexpected end of file in map header");
            }

            final String trimmedLine = StringUtils.removeTrailingWhitespace(line);

            if (parseLine(trimmedLine, mapArchObject, reader)) {
                // ignore
            } else if (trimmedLine.equals("msg")) {
                while (true) {
                    final String msgLine = reader.readLine();
                    if (msgLine == null) {
                        throw new IOException("unexpected end of file in msg...endmsg field");
                    }

                    final String trimmedMsgLine = StringUtils.removeTrailingWhitespace(msgLine);

                    if (trimmedMsgLine.equals("endmsg")) {
                        break;
                    }

                    if (mapArchObject.getText().length() > 0) {
                        mapArchObject.addText("\n");
                    }
                    mapArchObject.addText(trimmedMsgLine);
                }
            } else if (trimmedLine.equals("end")) {
                break;
            } else if (trimmedLine.startsWith("name ")) {
                mapArchObject.setMapName(trimmedLine.substring(5));
            } else if (trimmedLine.startsWith("width ")) {
                width = NumberUtils.parseInt(trimmedLine.substring(6));
            } else if (trimmedLine.startsWith("height ")) {
                height = NumberUtils.parseInt(trimmedLine.substring(7));
            } else if (trimmedLine.startsWith("enter_x ")) {
                mapArchObject.setEnterX(NumberUtils.parseInt(trimmedLine.substring(8)));
            } else if (trimmedLine.startsWith("enter_y ")) {
                mapArchObject.setEnterY(NumberUtils.parseInt(trimmedLine.substring(8)));
            } else if (trimmedLine.startsWith("reset_timeout ")) {
                mapArchObject.setResetTimeout(NumberUtils.parseInt(trimmedLine.substring(14)));
            } else if (trimmedLine.startsWith("swap_time ")) {
                mapArchObject.setSwapTime(NumberUtils.parseInt(trimmedLine.substring(10)));
            } else if (trimmedLine.startsWith("difficulty ")) {
                mapArchObject.setDifficulty(NumberUtils.parseInt(trimmedLine.substring(11)));
            } else if (trimmedLine.startsWith("darkness ")) {
                mapArchObject.setDarkness(NumberUtils.parseInt(trimmedLine.substring(9)));
            } else if (trimmedLine.startsWith("fixed_resettime ")) {
                if (NumberUtils.parseInt(trimmedLine.substring(16)) != 0) {
                    mapArchObject.setFixedReset(true);
                }
            } else if (trimmedLine.startsWith("outdoor ")) {
                if (NumberUtils.parseInt(trimmedLine.substring(8)) != 0) {
                    mapArchObject.setOutdoor(true);
                }
            } else if (trimmedLine.startsWith("tile_path_")) {
                // get tile path
                try {
                    final int index = Integer.valueOf(trimmedLine.substring(10, 11));
                    if (index > 0 && index <= Direction.values().length && trimmedLine.length() >= 12 && trimmedLine.charAt(11) == ' ') {
                        mapArchObject.setTilePath(Direction.values()[index - 1], trimmedLine.substring(12));
                    } else {
                        throw new InvalidMapFormatException("unexpected map attribute: '" + trimmedLine + "'");
                    }
                } catch (final NumberFormatException ignored) {
                    //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                    throw new InvalidMapFormatException("unexpected map attribute: '" + trimmedLine + "'");
                }
            } else {
                throw new InvalidMapFormatException("unexpected map attribute: '" + trimmedLine + "'");
            }
        }

        mapArchObject.setMapSize(new Size2D(Math.max(1, width), Math.max(1, height)));
    }

    /**
     * Parse a line for this editor type.
     * @param line the line to parse
     * @param mapArchObject the map arch object to update
     * @param reader the reader for reading additional lines
     * @return whether the line has been consumed
     * @throws IOException if an I/O error occurs
     */
    protected abstract boolean parseLine(@NotNull final String line, @NotNull final A mapArchObject, @NotNull final BufferedReader reader) throws IOException;

}
