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
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link ArchetypeType}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeTest {

    /**
     * Checks that empty expansions work correctly.
     */
    @Test
    public void testAttributes1() {
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjects faceObjects = new TestFaceObjects();
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final ArchetypeAttributeSection archetypeAttributeSection = new ArchetypeAttributeSection();
        final ArchetypeAttributesDefinition typeAttributes = new ArchetypeAttributesDefinition();
        final BaseObject<?, ?, ?, ?> archetype = new TestDefaultArchetype("base", faceObjectProviders, animationObjects);

        final ArchetypeType archetypeType1 = new ArchetypeType("name1", 1, "", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        Assert.assertEquals("name1 (1)", archetypeType1.getDisplayName(archetype));
    }

    /**
     * Checks that ${xxx} expansions work correctly.
     */
    @Test
    public void testAttributes2() {
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjects faceObjects = new TestFaceObjects();
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final ArchetypeAttributeSection archetypeAttributeSection = new ArchetypeAttributeSection();
        final ArchetypeAttributesDefinition typeAttributes = new ArchetypeAttributesDefinition();
        final BaseObject<?, ?, ?, ?> archetype = new TestDefaultArchetype("base", faceObjectProviders, animationObjects);

        final ArchetypeType archetypeType2 = new ArchetypeType("name2", 1, "a=${a}${a}", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        Assert.assertEquals("name2 (1) [a=]", archetypeType2.getDisplayName(archetype));

        archetype.setObjectText("a xyz\n");
        Assert.assertEquals("name2 (1) [a=xyzxyz]", archetypeType2.getDisplayName(archetype));

        archetype.setObjectText("a ${a}\n");
        Assert.assertEquals("name2 (1) [a=${a}${a}]", archetypeType2.getDisplayName(archetype));

        // syntax errors are not fatal
        final ArchetypeType archetypeType3 = new ArchetypeType("name", 1, "${}", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        Assert.assertEquals("name (1) []", archetypeType3.getDisplayName(archetype));

        final ArchetypeType archetypeType4 = new ArchetypeType("name", 1, "${a", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        Assert.assertEquals("name (1) [${a]", archetypeType4.getDisplayName(archetype));
    }

    /**
     * Checks that ${xxx?true:false} expansions work correctly.
     */
    @Test
    public void testAttributes3() {
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjects faceObjects = new TestFaceObjects();
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final ArchetypeAttributeSection archetypeAttributeSection = new ArchetypeAttributeSection();
        final ArchetypeAttributesDefinition typeAttributes = new ArchetypeAttributesDefinition();
        final BaseObject<?, ?, ?, ?> archetype = new TestDefaultArchetype("base", faceObjectProviders, animationObjects);

        final ArchetypeType archetypeType2 = new ArchetypeType("name2", 1, "a=${a?True:False}", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        Assert.assertEquals("name2 (1) [a=False]", archetypeType2.getDisplayName(archetype));

        archetype.setObjectText("a xyz\n");
        Assert.assertEquals("name2 (1) [a=True]", archetypeType2.getDisplayName(archetype));

        archetype.setObjectText("a 0\n");
        Assert.assertEquals("name2 (1) [a=False]", archetypeType2.getDisplayName(archetype));

        // empty replacements are allowed
        final ArchetypeType archetypeType3 = new ArchetypeType("name", 1, "${a?:}", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        archetype.setObjectText("a 0\n");
        Assert.assertEquals("name (1) []", archetypeType3.getDisplayName(archetype));
        archetype.setObjectText("a 1\n");
        Assert.assertEquals("name (1) []", archetypeType3.getDisplayName(archetype));

        // replacements with multiple colons
        final ArchetypeType archetypeType4 = new ArchetypeType("name", 1, "${a?b:c:d}", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        archetype.setObjectText("a 0\n");
        Assert.assertEquals("name (1) [c:d]", archetypeType4.getDisplayName(archetype));
        archetype.setObjectText("a 1\n");
        Assert.assertEquals("name (1) [b]", archetypeType4.getDisplayName(archetype));

        // syntax errors are not fatal
        final ArchetypeType archetypeType5 = new ArchetypeType("name", 1, "${?", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        archetype.setObjectText("a 0\n");
        Assert.assertEquals("name (1) [${?]", archetypeType5.getDisplayName(archetype));
        archetype.setObjectText("a 1\n");
        Assert.assertEquals("name (1) [${?]", archetypeType5.getDisplayName(archetype));

        final ArchetypeType archetypeType6 = new ArchetypeType("name", 1, "${a?", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        archetype.setObjectText("a 0\n");
        Assert.assertEquals("name (1) [${a?]", archetypeType6.getDisplayName(archetype));
        archetype.setObjectText("a 1\n");
        Assert.assertEquals("name (1) [${a?]", archetypeType6.getDisplayName(archetype));

        final ArchetypeType archetypeType7 = new ArchetypeType("name", 1, "${a?:", true, null, false, "description", "use", 1, archetypeAttributeSection, typeAttributes);
        archetype.setObjectText("a 0\n");
        Assert.assertEquals("name (1) [${a?:]", archetypeType7.getDisplayName(archetype));
        archetype.setObjectText("a 1\n");
        Assert.assertEquals("name (1) [${a?:]", archetypeType7.getDisplayName(archetype));
    }

    /**
     * Checks that {@link ArchetypeType#iterator()} returns a read-only
     * iterator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorReadOnly() {
        final ArchetypeAttributeSection archetypeAttributeSection = new ArchetypeAttributeSection();
        archetypeAttributeSection.add(new ArchetypeAttributeInt("name", "name", "", 1, "section", 0, 0, 0, 0));
        final ArchetypeType archetypeType = new ArchetypeType("name", 0, "", false, null, false, null, null, 0, archetypeAttributeSection, new ArchetypeAttributesDefinition());
        final Iterator<ArchetypeAttribute> iterator = archetypeType.iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        iterator.remove();
    }

}
