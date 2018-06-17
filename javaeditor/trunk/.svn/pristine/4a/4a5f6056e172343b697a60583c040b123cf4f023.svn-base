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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import net.sf.gridarta.preferences.FilePreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression tests for {@link MapMenu}.
 * @author Andreas Kirschbaum
 */
public class MapMenuTest {

    /**
     * Checks that {@link MapMenu#getOrCreateDirectory(MutableTreeNode, String)}
     * works as expected.
     */
    @Test
    public void testSubDir1() {
        final MapMenu mapMenu = new MapMenu("test");
        final MutableTreeNode dir = mapMenu.getRoot();
        checkDir(dir);
        mapMenu.getOrCreateDirectory(dir, "dir1");
        checkDir(dir, "dir1");
        mapMenu.getOrCreateDirectory(dir, "dir2");
        mapMenu.getOrCreateDirectory(dir, "dir3");
        checkDir(dir, "dir1", "dir2", "dir3");
        mapMenu.getOrCreateDirectory(dir, "dir2");
        checkDir(dir, "dir1", "dir2", "dir3");
    }

    /**
     * Checks that {@link MapMenu#getOrCreateDirectory(MutableTreeNode, String)}
     * rejects invalid path names.
     */
    @Test
    public void testSubDir2() {
        final MapMenu mapMenu = new MapMenu("test");
        final MutableTreeNode dir = mapMenu.getRoot();
        checkDir(dir);
        mapMenu.getOrCreateDirectory(dir, "dir1");
        mapMenu.getOrCreateDirectory(dir, "dir2");
        mapMenu.getOrCreateDirectory(dir, "dir3");
        checkDir(dir, "dir1", "dir2", "dir3");
        try {
            mapMenu.getOrCreateDirectory(dir, "dir2/sub");
            Assert.fail("IllegalArgumentException expected");
        } catch (final IllegalArgumentException ignored) {
        }
        try {
            mapMenu.getOrCreateDirectory(dir, "dir4/sub");
            Assert.fail("IllegalArgumentException expected");
        } catch (final IllegalArgumentException ignored) {
        }
        try {
            mapMenu.getOrCreateDirectory(dir, "");
            Assert.fail("IllegalArgumentException expected");
        } catch (final IllegalArgumentException ignored) {
        }
        checkDir(dir, "dir1", "dir2", "dir3");
    }

    /**
     * Checks that a {@link MapMenuEntryDir} instance contains the given child
     * nodes.
     * @param dir the map menu entry dir instance
     * @param paths the paths of the child nodes
     * @noinspection TypeMayBeWeakened // IDEA bug
     */
    private static void checkDir(@NotNull final MutableTreeNode dir, @NotNull final String... paths) {
        Assert.assertEquals(paths.length, dir.getChildCount());
        for (int i = 0; i < paths.length; i++) {
            final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) dir.getChildAt(i);
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) treeNode.getUserObject();
            Assert.assertEquals(paths[i], mapMenuEntry.getTitle());
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
