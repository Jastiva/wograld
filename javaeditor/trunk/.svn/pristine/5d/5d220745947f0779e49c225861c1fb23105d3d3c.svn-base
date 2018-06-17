/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.model.archetypeset;

import java.util.Collection;
import java.util.Iterator;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestArchetypeFactory;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link DefaultArchetypeSet}.
 * @author Andreas Kirschbaum
 */
public class DefaultArchetypeSetTest {

    /**
     * Checks that archetypes are returned in load order.
     * @throws DuplicateArchetypeException if the test fails
     */
    @Test
    public void testRetainLoadOrder() throws DuplicateArchetypeException {
        final FaceObjects faceObjects = new TestFaceObjects();
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new TestAnimationObjects();
        final ArchetypeFactory<TestGameObject, TestMapArchObject, TestArchetype> archetypeFactory = new TestArchetypeFactory(faceObjectProviders, animationObjects);
        final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet = new DefaultArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype>(archetypeFactory, null);
        archetypeSet.addArchetype(new TestDefaultArchetype("b", faceObjectProviders, animationObjects));
        archetypeSet.addArchetype(new TestDefaultArchetype("d", faceObjectProviders, animationObjects));
        archetypeSet.addArchetype(new TestDefaultArchetype("c", faceObjectProviders, animationObjects));
        archetypeSet.addArchetype(new TestDefaultArchetype("e", faceObjectProviders, animationObjects));
        archetypeSet.addArchetype(new TestDefaultArchetype("a", faceObjectProviders, animationObjects));
        final Collection<TestArchetype> archetypes = archetypeSet.getArchetypes();
        Assert.assertEquals(5, archetypes.size());
        final Iterator<TestArchetype> it = archetypes.iterator();
        Assert.assertEquals("b", it.next().getArchetypeName());
        Assert.assertEquals("d", it.next().getArchetypeName());
        Assert.assertEquals("c", it.next().getArchetypeName());
        Assert.assertEquals("e", it.next().getArchetypeName());
        Assert.assertEquals("a", it.next().getArchetypeName());
        Assert.assertFalse(it.hasNext());
    }

}
