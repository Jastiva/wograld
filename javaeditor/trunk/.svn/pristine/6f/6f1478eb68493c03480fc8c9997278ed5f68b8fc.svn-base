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
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.preferences.FilePreferencesFactory;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.DefaultActionBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Regression tests for {@link MapMenuLoader}.
 * @author Andreas Kirschbaum
 */
public class MapMenuPreferencesTest {

    /**
     * Checks that saving and re-loading entries works as expected.
     * @throws IOException if the test fails
     */
    @Test
    public void test1() throws IOException {
        final MapMenuLoader pref = new MapMenuLoader("key");
        Assert.assertEquals(0, pref.loadNumEntries());
        try {
            pref.loadEntry(0);
            Assert.fail();
        } catch (final IOException ignored) {
            // ignore
        }
        pref.saveNumEntries(2);
        Assert.assertEquals(2, pref.loadNumEntries());
        pref.saveEntry(1, "title1", "filename1", "directory1/sub", MapMenuLoader.Type.DIR);
        pref.saveEntry(0, "title0", "filename0", "directory0/sub", MapMenuLoader.Type.MAP);

        final MapMenuLoader.Result result0 = pref.loadEntry(0);
        final MapMenuEntry mapMenuEntry0 = result0.getMapMenuEntry();
        Assert.assertEquals("directory0/sub", result0.getDirectory());
        Assert.assertEquals("title0", mapMenuEntry0.getTitle());
        Assert.assertTrue(mapMenuEntry0 instanceof MapMenuEntryMap);
        final MapMenuEntryMap mapMenuEntryMap0 = (MapMenuEntryMap) mapMenuEntry0;
        Assert.assertEquals(new File("filename0"), mapMenuEntryMap0.getMapFile());

        final MapMenuLoader.Result result1 = pref.loadEntry(1);
        final MapMenuEntry mapMenuEntry1 = result1.getMapMenuEntry();
        Assert.assertEquals("directory1/sub", result1.getDirectory());
        Assert.assertEquals("title1", mapMenuEntry1.getTitle());
        Assert.assertTrue(mapMenuEntry1 instanceof MapMenuEntryDir);

        try {
            pref.loadEntry(2);
            Assert.fail();
        } catch (final IOException ignored) {
            // ignore
        }
        pref.removeEntry(0);
        try {
            pref.loadEntry(0);
            Assert.fail();
        } catch (final IOException ignored) {
            // ignore
        }
        pref.loadEntry(1);
    }

    /**
     * Checks that loading a {@link MapMenu} instance works as expected.
     * @throws BackingStoreException if the test fails
     */
    @Test
    public void test2() throws BackingStoreException {
        final ActionBuilder actionBuilder = new DefaultActionBuilder("net.sf.gridarta");
        ActionBuilderFactory.getInstance().putActionBuilder("net.sf.gridarta", actionBuilder);
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("net.sf.gridarta.gui.mapmenu.testLoad1");
        actionBuilder.addBundle(resourceBundle);

        final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);
        for (final String key : resourceBundle.keySet()) {
            final String value = resourceBundle.getString(key);
            preferences.put(key, value);
        }

        final MapMenu mapMenu1 = new MapMenu("test");
        mapMenu1.load();
        Assert.assertEquals(10, mapMenu1.size());

        for (final String key : preferences.keys()) {
            preferences.remove(key);
        }

        final MapMenu mapMenu2 = new MapMenu("test");
        mapMenu2.load();
        Assert.assertEquals(0, mapMenu2.size());

        mapMenu1.saveAlways();

        mapMenu2.load();
        Assert.assertEquals(10, mapMenu2.size());

        compareTrees(mapMenu1.getRoot(), mapMenu2.getRoot());
    }

    /**
     * Checks that saving and re-loading a {@link MapMenu} having directories
     * with the same name works.
     */
    @Test
    public void test3() {
        final ActionBuilder actionBuilder = new DefaultActionBuilder("net.sf.gridarta");
        ActionBuilderFactory.getInstance().putActionBuilder("net.sf.gridarta", actionBuilder);

        final MapMenu mapMenu1 = new MapMenu("test");
        mapMenu1.addMapMenuEntry("dir1", new MapMenuEntryMap(new File("file1"), "title1"));
        mapMenu1.addMapMenuEntry("dir2", new MapMenuEntryMap(new File("file2"), "title2"));
        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) mapMenu1.getRoot().getFirstChild();
        final MapMenuEntry mapMenuEntry = (MapMenuEntry) treeNode.getUserObject();
        mapMenuEntry.setTitle("dir2");

        mapMenu1.saveAlways();

        final MapMenu mapMenu2 = new MapMenu("test");
        mapMenu2.load();

        compareTrees(mapMenu1.getRoot(), mapMenu2.getRoot());
    }

    /**
     * Compares two {@link TreeNode} instances. Fails the test case if the nodes
     * are not equal.
     * @param treeNode1 the first tree node instance
     * @param treeNode2 the second tree node instance
     */
    private static void compareTrees(@NotNull final TreeNode treeNode1, @NotNull final TreeNode treeNode2) {
        Assert.assertEquals(treeNode1.getChildCount(), treeNode2.getChildCount());
        for (int i = 0; i < treeNode1.getChildCount(); i++) {
            final DefaultMutableTreeNode childTreeNode1 = (DefaultMutableTreeNode) treeNode1.getChildAt(i);
            final DefaultMutableTreeNode childTreeNode2 = (DefaultMutableTreeNode) treeNode2.getChildAt(i);
            final MapMenuEntry mapMenuEntry1 = (MapMenuEntry) childTreeNode1.getUserObject();
            final MapMenuEntry mapMenuEntry2 = (MapMenuEntry) childTreeNode2.getUserObject();
            Assert.assertSame(mapMenuEntry1.getClass(), mapMenuEntry2.getClass());
            Assert.assertEquals(mapMenuEntry1.getTitle(), mapMenuEntry2.getTitle());
            final MapMenuEntryVisitor mapMenuEntryVisitor = new MapMenuEntryVisitor() {

                @Override
                public void visit(@NotNull final MapMenuEntryDir mapMenuEntry) {
                    compareTrees(childTreeNode1, childTreeNode2);
                }

                @Override
                public void visit(@NotNull final MapMenuEntryMap mapMenuEntry) {
                    final MapMenuEntryMap mapMenuEntryMap1 = (MapMenuEntryMap) mapMenuEntry1;
                    final MapMenuEntryMap mapMenuEntryMap2 = (MapMenuEntryMap) mapMenuEntry2;
                    Assert.assertEquals(mapMenuEntryMap1.getMapFile(), mapMenuEntryMap2.getMapFile());
                }

            };
            mapMenuEntry1.visit(mapMenuEntryVisitor);
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
