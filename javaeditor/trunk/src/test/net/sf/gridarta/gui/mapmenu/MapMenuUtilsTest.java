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

package net.sf.gridarta.gui.mapmenu;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import net.sf.gridarta.preferences.FilePreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression tests for {@link MapMenuUtils}.
 * @author Andreas Kirschbaum
 */
public class MapMenuUtilsTest {

    /**
     * Checks that {@link MapMenuUtils#removeMap(MutableTreeNode, File)} works
     * as expected.
     */
    @Test
    public void testRemoveMap1() {
        final MutableTreeNode dir = new DefaultMutableTreeNode(new MapMenuEntryDir("Test"), true);
        dir.insert(new DefaultMutableTreeNode(new MapMenuEntryMap(new File("file1"), "title1"), false), 0);
        dir.insert(new DefaultMutableTreeNode(new MapMenuEntryMap(new File("file2"), "title2"), false), 1);
        dir.insert(new DefaultMutableTreeNode(new MapMenuEntryMap(new File("file1"), "title3"), false), 2);
        dir.insert(new DefaultMutableTreeNode(new MapMenuEntryMap(new File("file3"), "title4"), false), 3);
        dir.insert(new DefaultMutableTreeNode(new MapMenuEntryMap(new File("file2"), "title5"), false), 4);
        checkFile(dir, "file1", "file2", "file1", "file3", "file2");
        MapMenuUtils.removeMap(dir, new File("file2"));
        checkFile(dir, "file1", "file1", "file3");
        MapMenuUtils.removeMap(dir, new File("file2"));
        checkFile(dir, "file1", "file1", "file3");
        MapMenuUtils.removeMap(dir, new File("file1"));
        checkFile(dir, "file3");
        MapMenuUtils.removeMap(dir, new File("file3"));
        checkFile(dir);
    }

    /**
     * Checks that a {@link MapMenuEntryDir} instance contains the given child
     * nodes.
     * @param dir the map menu entry dir instance
     * @param files the files of the child nodes
     * @noinspection TypeMayBeWeakened // IDEA bug
     */
    private static void checkFile(@NotNull final TreeNode dir, @NotNull final String... files) {
        Assert.assertEquals(files.length, dir.getChildCount());
        for (int i = 0; i < files.length; i++) {
            final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) dir.getChildAt(i);
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) treeNode.getUserObject();
            if (files[i] == null) {
                Assert.assertSame(MapMenuEntryDir.class, mapMenuEntry.getClass());
            } else {
                Assert.assertSame(MapMenuEntryMap.class, mapMenuEntry.getClass());
                final MapMenuEntryMap mapMenuEntryMap = (MapMenuEntryMap) mapMenuEntry;
                Assert.assertEquals(new File(files[i]), mapMenuEntryMap.getMapFile());
            }
        }
    }

    /**
     * Initializes the test case.
     */
    @BeforeClass
    public static void setUp() {
        System.setProperty("java.util.prefs.PreferencesFactory", "net.sf.gridarta.preferences.FilePreferencesFactory");
        FilePreferencesFactory.initialize("user_net_sf_gridarta", null);
    }

}
