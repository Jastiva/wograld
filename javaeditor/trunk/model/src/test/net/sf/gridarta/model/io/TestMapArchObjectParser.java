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
import java.util.Formatter;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapArchObjectParser} for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestMapArchObjectParser extends AbstractMapArchObjectParser<TestMapArchObject> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean parseLine(@NotNull final String line, @NotNull final TestMapArchObject mapArchObject, @NotNull final BufferedReader reader) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(@NotNull final Appendable appendable, @NotNull final TestMapArchObject mapArchObject) throws IOException {
        final Formatter format = new Formatter(appendable);
        appendable.append("arch map\n");
        if (mapArchObject.getMapName().length() > 0) {
            format.format("name %s\n", mapArchObject.getMapName());
        }
        if (mapArchObject.getSwapTime() != 0) {
            format.format("swap_time %d\n", mapArchObject.getSwapTime());
        }
        if (mapArchObject.getResetTimeout() != 0) {
            format.format("reset_timeout %d\n", mapArchObject.getResetTimeout());
        }
        if (mapArchObject.isFixedReset()) {
            appendable.append("fixed_resettime 1\n");
        }
        if (mapArchObject.getDifficulty() != 0) {
            format.format("difficulty %d\n", mapArchObject.getDifficulty());
        }
        if (mapArchObject.getDarkness() != 0) {
            format.format("darkness %d\n", mapArchObject.getDarkness());
        }
        format.format("width %d\n", mapArchObject.getMapSize().getWidth());
        format.format("height %d\n", mapArchObject.getMapSize().getHeight());
        if (mapArchObject.getEnterX() != 0) {
            format.format("enter_x %d\n", mapArchObject.getEnterX());
        }
        if (mapArchObject.getEnterY() != 0) {
            format.format("enter_y %d\n", mapArchObject.getEnterY());
        }
        if (mapArchObject.getText().trim().length() > 0) {
            format.format("msg\n" + "%s\n" + "endmsg\n", mapArchObject.getText().trim());
        }
        if (mapArchObject.isOutdoor()) {
            appendable.append("outdoor 1\n");
        }
        for (final Direction direction : Direction.values()) {
            if (mapArchObject.getTilePath(direction).length() > 0) {
                format.format("tile_path_%d %s\n", direction.ordinal() + 1, mapArchObject.getTilePath(direction));
            }
        }
        appendable.append("end\n");
    }

}
