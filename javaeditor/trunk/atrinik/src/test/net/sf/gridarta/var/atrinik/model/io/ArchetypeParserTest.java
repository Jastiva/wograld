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

package net.sf.gridarta.var.atrinik.model.io;

import java.io.IOException;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.DefaultAnimationObjects;
import net.sf.gridarta.model.archetype.AbstractArchetypeBuilder;
import net.sf.gridarta.model.io.AbstractArchetypeParserTest;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.DefaultArchetypeSet;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.DefaultFaceObjects;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.archetype.DefaultArchetypeFactory;
import net.sf.gridarta.var.atrinik.model.gameobject.DefaultGameObjectFactory;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression tests for {@link ArchetypeParser}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeParserTest extends AbstractArchetypeParserTest<GameObject, MapArchObject, Archetype> {

    /**
     * The loaded archetypes.
     */
    @Nullable
    private ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet;

    /**
     * Checks that mpart_id fields are parsed correctly.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMpartIdOk() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object head\n");
        input.append("mpart_id 1\n");
        input.append("end\n");
        input.append("More\n");
        input.append("Object tail\n");
        input.append("mpart_id 1\n");
        input.append("end\n");
        check(input.toString(), false, false, 2);
        Assert.assertEquals(1, getArchetypeSet().getArchetype("head").getMultiShapeID());
        Assert.assertEquals(1, getArchetypeSet().getArchetype("tail").getMultiShapeID());
    }

    /**
     * Checks that mpart_id fields are parsed correctly.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testMpartIdInconsistent() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object head\n");
        input.append("mpart_id 1\n");
        input.append("end\n");
        input.append("More\n");
        input.append("Object tail\n");
        input.append("mpart_id 2\n");
        input.append("end\n");
        check(input.toString(), false, true, 2);
        Assert.assertEquals(1, getArchetypeSet().getArchetype("head").getMultiShapeID());
        Assert.assertEquals(2, getArchetypeSet().getArchetype("tail").getMultiShapeID());
    }

    /**
     * Checks that inventory game objects are recognized.
     * @throws IOException if the test fails
     * @throws UndefinedArchetypeException if the test fails
     */
    @Test
    public void testInventoryGameObjects() throws IOException, UndefinedArchetypeException {
        final StringBuilder input = new StringBuilder();
        input.append("Object arch1\n");
        input.append("end\n");
        input.append("Object arch2\n");
        input.append("arch arch1\n");
        input.append("name name1\n");
        input.append("end\n");
        input.append("end\n");
        check(input.toString(), false, false, 2);
        final Archetype arch2 = getArchetypeSet().getArchetype("arch2");
        Assert.assertEquals(1, arch2.countInvObjects());
        final GameObject inv = arch2.iterator().next();
        Assert.assertEquals("arch1", inv.getArchetype().getArchetypeName());
        Assert.assertEquals("name1", inv.getAttributeString(BaseObject.NAME));
    }

    /**
     * Initializes the test.
     */
    @BeforeClass
    public static void setUp() {
        final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");
        actionBuilder.addParent(ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta.var.atrinik"));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected AbstractArchetypeParser<GameObject, MapArchObject, Archetype, ? extends AbstractArchetypeBuilder<GameObject, MapArchObject, Archetype>> newArchetypeParser() {
        final FaceObjects faceObjects = new DefaultFaceObjects(true);
        final GUIUtils guiUtils = new GUIUtils();
        final SystemIcons systemIcons = new SystemIcons(guiUtils);
        final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final AnimationObjects animationObjects = new DefaultAnimationObjects();
        final ArchetypeTypeSet archetypeTypeSet = new ArchetypeTypeSet();
        final DefaultGameObjectFactory gameObjectFactory = new DefaultGameObjectFactory(faceObjectProviders, animationObjects, archetypeTypeSet);
        final DefaultArchetypeFactory archetypeFactory = new DefaultArchetypeFactory(faceObjectProviders, animationObjects);
        archetypeSet = new DefaultArchetypeSet<GameObject, MapArchObject, Archetype>(archetypeFactory, null);
        archetypeSet.setLoadedFromArchive(true);
        assert archetypeSet != null;
        final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser = new DefaultGameObjectParser(gameObjectFactory, archetypeSet);
        assert archetypeSet != null;
        final IsoMapSquareInfo isoMapSquareInfo = new IsoMapSquareInfo(1, 1, 1, 1);
        final MultiPositionData multiPositionData = new MultiPositionData(isoMapSquareInfo);
        assert archetypeSet != null;
        return new ArchetypeParser(gameObjectParser, animationObjects, archetypeSet, gameObjectFactory, multiPositionData);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("NullableProblems")
    protected ArchetypeSet<GameObject, MapArchObject, Archetype> getArchetypeSet() {
        if (archetypeSet == null) {
            throw new IllegalStateException();
        }
        return archetypeSet;
    }

}
