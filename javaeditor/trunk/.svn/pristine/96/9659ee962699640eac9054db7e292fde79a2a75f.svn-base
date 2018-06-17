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

package net.sf.gridarta.model.autojoin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

/**
 * Implements {@link AutojoinList} related functions.
 * @author Andreas Kirschbaum
 */
public class AutojoinListsHelper {

    /**
     * A {@link Pattern} that matches alternative archetypes.
     */
    @NotNull
    private static final Pattern ALTERNATIVES_PATTERN = Pattern.compile("\\|");

    /**
     * The {@link TestMapModelCreator} instance.
     */
    @NotNull
    private final TestMapModelCreator mapModelCreator;

    /**
     * The {@link AutojoinLists} instance.
     */
    @NotNull
    private final AutojoinLists<TestGameObject, TestMapArchObject, TestArchetype> autojoinLists;

    /**
     * Creates a new instance.
     */
    public AutojoinListsHelper() {
        this(new TestMapModelCreator(false));
    }

    /**
     * Creates a new instance.
     * @param mapModelCreator the map model creator instance
     */
    public AutojoinListsHelper(@NotNull final TestMapModelCreator mapModelCreator) {
        this.mapModelCreator = mapModelCreator;
        autojoinLists = mapModelCreator.getAutojoinLists();
    }

    /**
     * Creates a new {@link AutojoinLists} instance.
     * @param archetypeNames the names of the archetypes
     * @throws IllegalAutojoinListException if the autojoin lists instance
     * cannot be created
     */
    public void newAutojoinLists(@NotNull final String... archetypeNames) throws IllegalAutojoinListException {
        final List<List<TestArchetype>> archetypes = new ArrayList<List<TestArchetype>>();
        for (final String archetypeNameList : archetypeNames) {
            final List<TestArchetype> archetypeList = new ArrayList<TestArchetype>();
            if (!archetypeNameList.isEmpty()) {
                for (final String archetypeName : ALTERNATIVES_PATTERN.split(archetypeNameList, -1)) {
                    archetypeList.add(mapModelCreator.getArchetype(archetypeName));
                }
            }
            archetypes.add(archetypeList);
        }
        autojoinLists.addAutojoinList(new AutojoinList<TestGameObject, TestMapArchObject, TestArchetype>(archetypes));
    }

    /**
     * Creates a new {@link AutojoinLists} instance and expects an error.
     * @param expectedException the expected error message
     * @param archetypeNames the archetype names
     */
    public void newAutojoinListsFail(@NotNull final String expectedException, @NotNull final String... archetypeNames) {
        try {
            newAutojoinLists(archetypeNames);
            Assert.fail();
        } catch (final IllegalAutojoinListException ex) {
            Assert.assertEquals(expectedException, ex.getMessage());
        }
    }

}
