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

package net.sf.gridarta.var.atrinik.model.maparchobject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import net.sf.gridarta.model.io.AbstractMapArchObjectParser;
import net.sf.gridarta.model.maparchobject.AbstractMapArchObject;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.var.atrinik.model.io.MapArchObjectParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link MapArchObject}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MapArchObjectTest {

    /**
     * Test for {@link net.sf.gridarta.model.io.AbstractMapArchObjectParser#load(BufferedReader,
     * net.sf.gridarta.model.maparchobject.MapArchObject)}.
     * @throws IOException (unexpected)
     */
    @Test
    public void testParseMapArchNoAttributes() throws IOException {
        final MapArchObject mao = new MapArchObject();
        final AbstractMapArchObjectParser<MapArchObject> mapArchObjectParser = new MapArchObjectParser();
        final StringReader stringReader = new StringReader("arch map\n" + "end\n");
        final BufferedReader in = new BufferedReader(stringReader);
        try {
            mapArchObjectParser.load(in, mao);
        } finally {
            in.close();
        }
        Assert.assertEquals(AbstractMapArchObject.MAP_NAME_UNNAMED, mao.getMapName());
        Assert.assertEquals(Size2D.ONE, mao.getMapSize());
    }

}
