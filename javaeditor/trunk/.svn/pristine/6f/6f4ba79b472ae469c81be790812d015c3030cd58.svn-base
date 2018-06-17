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

package net.sf.gridarta.model.artifact;

import java.io.IOException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.errorview.TestErrorView;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link ArtifactParser}.
 * @author Andreas Kirschbaum
 */
public class ArtifactParserTest {

    /**
     * Checks that a missing "object" line is detected.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMissingObject() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestErrorView errorView = new TestErrorView();
        final TestParser parser = new TestParser(errorView);
        parser.addArchetype("base", "sp 2");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("artifact art\n");
        artifacts.append("def_arch base\n");
        artifacts.append("Object obj\n");
        artifacts.append("sp 3\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertFalse(errorView.hasWarnings());
        Assert.assertFalse(errorView.hasErrors());
        Assert.assertEquals(2, parser.getArchetypeCount());
        final Archetype<TestGameObject, TestMapArchObject, TestArchetype> archetype = parser.getArchetype("art");
        Assert.assertEquals("sp 3\nname base\n", archetype.getObjectText());
    }

    /**
     * Checks that an artifact definition inherits the def_arch archetype's name
     * by default.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testDefaultName1() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestParser parser = new TestParser();
        parser.addArchetype("horn");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("artifact horn_fools\n");
        artifacts.append("def_arch horn\n");
        artifacts.append("Object obj\n");
        artifacts.append("title of fools\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertEquals("horn of fools", parser.getArchetype("horn_fools").getBestName());
    }

    /**
     * Checks that an artifact definition inherits the def_arch archetype's name
     * by default.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testDefaultName2() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestParser parser = new TestParser();
        parser.addArchetype("horn", "name horn2");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("artifact horn_fools\n");
        artifacts.append("def_arch horn\n");
        artifacts.append("Object obj\n");
        artifacts.append("title of fools\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertEquals("horn2 of fools", parser.getArchetype("horn_fools").getBestName());
    }

    /**
     * Checks that an artifact definition inherits the def_arch archetype's name
     * by default.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testDefaultName3() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestParser parser = new TestParser();
        parser.addArchetype("horn", "name base_horn");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("artifact horn_fools\n");
        artifacts.append("def_arch horn\n");
        artifacts.append("Object obj\n");
        artifacts.append("title of fools\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertEquals("base_horn of fools", parser.getArchetype("horn_fools").getBestName());
    }

    /**
     * Checks that an artifact definition inherits the def_arch archetype's name
     * by default.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testDefaultName4() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestParser parser = new TestParser();
        parser.addArchetype("horn", "name base_horn");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("artifact horn_fools\n");
        artifacts.append("def_arch horn\n");
        artifacts.append("Object obj\n");
        artifacts.append("name special_horn\n");
        artifacts.append("title of fools\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertEquals("special_horn of fools", parser.getArchetype("horn_fools").getBestName());
    }

    /**
     * Checks for spurious error messages when setting an artifact's name when
     * the base archetype doesn't has an explicit name.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testDefaultName5a() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestParser parser = new TestParser();
        parser.addArchetype("note", "face note.101", "layer 3", "identified 1", "type 8", "material 1", "value 8", "weight 75", "level 1", "exp 10");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("Allowed none\n");
        artifacts.append("chance 1\n");
        artifacts.append("artifact my_notebook_item\n");
        artifacts.append("def_arch note\n");
        artifacts.append("Object\n");
        artifacts.append("name A notebook\n");
        artifacts.append("msg\n");
        artifacts.append("This should be giving a false positive for duplicate attributes.\n");
        artifacts.append("endmsg\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertEquals("A notebook", parser.getArchetype("my_notebook_item").getBestName());
    }

    /**
     * Checks for spurious error messages when setting an artifact's name when
     * the base archetype doesn't has an explicit name.
     * @throws DuplicateArchetypeException if the test fails
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testDefaultName5b() throws DuplicateArchetypeException, IOException, UndefinedArchetypeException {
        final TestParser parser = new TestParser();
        parser.addArchetype("note", "face note.101", "name note", "layer 3", "identified 1", "type 8", "material 1", "value 8", "weight 75", "level 1", "exp 10");
        final StringBuilder artifacts = new StringBuilder();
        artifacts.append("Allowed none\n");
        artifacts.append("chance 1\n");
        artifacts.append("artifact my_notebook_item\n");
        artifacts.append("def_arch note\n");
        artifacts.append("Object\n");
        artifacts.append("name A notebook\n");
        artifacts.append("msg\n");
        artifacts.append("This should be giving a false positive for duplicate attributes.\n");
        artifacts.append("endmsg\n");
        artifacts.append("end\n");
        parser.parseArtifacts(artifacts.toString());
        Assert.assertEquals("A notebook", parser.getArchetype("my_notebook_item").getBestName());
    }

}
