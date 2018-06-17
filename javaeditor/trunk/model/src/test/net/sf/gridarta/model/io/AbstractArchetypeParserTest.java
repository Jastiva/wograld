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
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sf.gridarta.model.archetype.AbstractArchetypeBuilder;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.errorview.TestErrorView;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

/**
 * Abstract base class for regression tests for {@link ArchetypeParser}.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractArchetypeParserTest<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Creates a new archetype parser and parses the given input.
     * @param input the input to parse
     * @param hasErrors whether errors are expected
     * @param hasWarnings whether warnings are expected
     * @param archetypes the number of archetypes to expect
     * @throws IOException if parsing fails
     */
    protected void check(@NotNull final String input, final boolean hasErrors, final boolean hasWarnings, final int archetypes) throws IOException {
        final AbstractArchetypeParser<G, A, R, ?> archetypeParser = newArchetypeParser();
        final TestErrorView errorView = new TestErrorView();
        final List<G> invObjects = new ArrayList<G>();
        final Reader reader = new StringReader(input);
        final BufferedReader bufferedReader = new BufferedReader(reader);
        try {
            archetypeParser.parseArchetypeFromStream(bufferedReader, null, null, null, "panel", "folder", "", invObjects, new ErrorViewCollector(errorView, new File("*string*")));
        } finally {
            bufferedReader.close();
        }
        Assert.assertEquals(hasErrors, errorView.hasErrors());
        Assert.assertEquals(hasWarnings, errorView.hasWarnings());
        Assert.assertEquals(archetypes, getArchetypeSet().getArchetypeCount());
    }

    /**
     * Creates a new {@link AbstractArchetypeParser} instance.
     * @return the new instance
     */
    @NotNull
    protected abstract AbstractArchetypeParser<G, A, R, ? extends AbstractArchetypeBuilder<G, A, R>> newArchetypeParser();

    /**
     * Returns the {@link ArchetypeSet}.
     * @return the archetype set
     */
    @NotNull
    protected abstract ArchetypeSet<G, A, R> getArchetypeSet();

}
