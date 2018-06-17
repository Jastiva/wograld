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

package net.sf.gridarta.gui.map.renderer;

import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link ToolTipAppender}.
 * @author Andreas Kirschbaum
 */
public class ToolTipAppenderTest {

    /**
     * Checks that HTML tags are correctly encoded in tooltip.
     */
    @Test
    public void testEmbeddedHtml1() {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser = mapModelCreator.newGameObjectParser();
        final ToolTipAppender<TestGameObject, TestMapArchObject, TestArchetype> toolTipAppender = new ToolTipAppender<TestGameObject, TestMapArchObject, TestArchetype>(gameObjectParser);
        final GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> gameObjectFactory = mapModelCreator.getGameObjectFactory();
        final TestArchetype archetype = gameObjectFactory.newArchetype("arch");
        final TestGameObject gameObject = gameObjectFactory.createGameObject(archetype);
        gameObject.setAttributeString("<key>", "<value>&<value>");
        toolTipAppender.appendGameObject(gameObject, false, "");
        final String tooltip = toolTipAppender.finish();
        Assert.assertEquals("<html><b>arch</b><br>&lt;key&gt; &lt;value&gt;&amp;&lt;value&gt;", tooltip);
    }

}
