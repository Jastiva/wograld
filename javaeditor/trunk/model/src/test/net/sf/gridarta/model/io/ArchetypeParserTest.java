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

import java.io.IOException;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.AbstractArchetypeBuilder;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestArchetypeBuilder;
import net.sf.gridarta.model.archetype.TestArchetypeFactory;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.DefaultArchetypeSet;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.gameobject.TestGameObjectFactory;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link AbstractArchetypeParser}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeParserTest extends AbstractArchetypeParserTest<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * The loaded archetypes.
     */
    @Nullable
    private ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet;

    /**
     * Checks that a missing "object" line is detected.
     * @throws IOException if the test fails
     */
    @Test
    public void testMissingObject() throws IOException {
        check("end\n", false, true, 0);
    }

    /**
     * Checks that an empty archetype can be parsed.
     * @throws IOException if the test fails
     */
    @Test
    public void testEmpty() throws IOException {
        final StringBuilder input = new StringBuilder();
        input.append("Object test\n");
        input.append("end\n");
        check(input.toString(), false, false, 1);
    }

    /**
     * Checks that a missing "end" line is reported.
     * @throws IOException if the test fails
     */
    @Test
    public void testMissingEnd() throws IOException {
        check("Object test\n", true, false, 0);
    }

    /**
     * Checks that msg...endmsg fields are parsed correctly.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMsgTextEmpty() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object test\n");
        input.append("msg\n");
        input.append("endmsg\n");
        input.append("end\n");
        check(input.toString(), false, false, 1);
        Assert.assertNull(getArchetypeSet().getArchetype("test").getMsgText());
    }

    /**
     * Checks that msg...endmsg fields are parsed correctly.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMsgTextLines() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object test\n");
        input.append("msg\n");
        input.append("abc\n");
        input.append("def\n");
        input.append("ghi\n");
        input.append("endmsg\n");
        input.append("end\n");
        check(input.toString(), false, false, 1);
        final StringBuilder msgText = new StringBuilder();
        msgText.append("abc\n");
        msgText.append("def\n");
        msgText.append("ghi\n");
        Assert.assertEquals(msgText.toString(), getArchetypeSet().getArchetype("test").getMsgText());
    }

    /**
     * Checks that msg...endmsg fields are parsed correctly.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMsgTextTrailingWhitespace() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object test\n");
        input.append("msg\n");
        input.append("abc   \n");
        input.append("def\n");
        input.append("ghi   \n");
        input.append("\n");
        input.append("endmsg\n");
        input.append("end\n");
        check(input.toString(), false, false, 1);
        final StringBuilder msgText = new StringBuilder();
        msgText.append("abc\n");
        msgText.append("def\n");
        msgText.append("ghi\n");
        msgText.append("\n");
        Assert.assertEquals(msgText.toString(), getArchetypeSet().getArchetype("test").getMsgText());
    }

    /**
     * Checks that msg...endmsg fields are parsed correctly.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMsgTextLeadingWhitespace() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object test\n");
        input.append("msg\n");
        input.append("\n");
        input.append("   abc\n");
        input.append("def\n");
        input.append("   ghi\n");
        input.append("endmsg\n");
        input.append("end\n");
        check(input.toString(), false, false, 1);
        final StringBuilder msgText = new StringBuilder();
        msgText.append("\n");
        msgText.append("   abc\n");
        msgText.append("def\n");
        msgText.append("   ghi\n");
        Assert.assertEquals(msgText.toString(), getArchetypeSet().getArchetype("test").getMsgText());
    }

    /**
     * Checks that msg...endmsg fields are parsed correctly.
     * @throws IOException if the test fails
     */
    @Test
    public void testMsgTextTruncated() throws IOException {
        final StringBuilder input = new StringBuilder();
        input.append("Object test\n");
        input.append("msg\n");
        input.append("abc\n");
        input.append("def\n");
        check(input.toString(), true, false, 0);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected AbstractArchetypeParser<TestGameObject, TestMapArchObject, TestArchetype, ? extends AbstractArchetypeBuilder<TestGameObject, TestMapArchObject, TestArchetype>> newArchetypeParser() {
        final FaceObjects faceObjects = new TestFaceObjects();
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final TestGameObjectFactory gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        final TestArchetypeBuilder archetypeBuilder = new TestArchetypeBuilder(gameObjectFactory);
        final TestArchetypeFactory archetypeFactory = new TestArchetypeFactory(faceObjectProviders, animationObjects);
        archetypeSet = new DefaultArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype>(archetypeFactory, null);
        archetypeSet.setLoadedFromArchive(true);
        assert archetypeSet != null;
        return new TestArchetypeParser(archetypeBuilder, animationObjects, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("NullableProblems")
    protected ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> getArchetypeSet() {
        final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> result = archetypeSet;
        if (result == null) {
            throw new IllegalStateException();
        }
        return result;
    }

}
