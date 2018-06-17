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

package net.sf.gridarta.model.archetypetype;

import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link ArchetypeTypeSet}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeSetTest {

    /**
     * Checks that {@link ArchetypeTypeSet#iterator()} returns a read-only
     * iterator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorReadOnly() {
        final ArchetypeTypeSet archetypeTypeSet = new ArchetypeTypeSet();
        archetypeTypeSet.addArchetypeType(new ArchetypeType("name", 0, "", false, null, false, null, null, 0, new ArchetypeAttributeSection(), new ArchetypeAttributesDefinition()));
        final Iterator<ArchetypeType> iterator = archetypeTypeSet.iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        iterator.remove();
    }

}
