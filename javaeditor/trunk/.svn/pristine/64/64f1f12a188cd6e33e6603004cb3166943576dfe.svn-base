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
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestArchetypeBuilder;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link ArchetypeParser} for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestArchetypeParser extends AbstractArchetypeParser<TestGameObject, TestMapArchObject, TestArchetype, TestArchetypeBuilder> {

    /**
     * Creates an ArchetypeParser.
     * @param archetypeBuilder the archetype builder to use
     * @param animationObjects the animation objects instance to use
     * @param archetypeSet the archetype set
     */
    public TestArchetypeParser(@NotNull final TestArchetypeBuilder archetypeBuilder, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet) {
        super(archetypeBuilder, animationObjects, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParseArchetype() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStartLine(@NotNull final String line) {
        return line.startsWith("Object");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean processLine(@NotNull final BufferedReader in, @NotNull final String line, @NotNull final String line2, @NotNull final TestArchetypeBuilder archetypeBuilder, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final List<TestGameObject> invObjects) throws IOException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetypePart(@Nullable final TestArchetype firstArch, @NotNull final TestArchetype archetype, @NotNull final ErrorViewCollector errorViewCollector) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetype(@NotNull final TestArchetype archetype) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean addToPanel(final boolean isInternPath, @NotNull final String editorFolder, @NotNull final TestArchetype archetype) {
        return true;
    }

}
