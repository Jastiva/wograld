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

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.sf.gridarta.utils.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the contents of a recent or bookmark menu.
 * @author Andreas Kirschbaum
 */
public class MapMenu {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(MapMenu.class);

    /**
     * The {@link MapMenuLoader} for loading/storing the menu contents.
     */
    @NotNull
    private final MapMenuLoader mapMenuLoader;

    /**
     * The root node of {@link #treeModel}.
     */
    @NotNull
    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new MapMenuEntryDir("Bookmarks"), true);

    /**
     * The {@link DefaultTreeModel} that contains all menu entries.
     */
    @NotNull
    private final DefaultTreeModel treeModel = new DefaultTreeModel(root);

    /**
     * Recently deleted nodes. The first node is the last deleted node.
     */
    @NotNull
    private final Deque<DeletedNode> deletedNodes = new ArrayDeque<DeletedNode>();

    /**
     * Whether {@link #treeModel} has been modified since last save.
     */
    private boolean treeModelModified;

    /**
     * The {@link TreeModelListener} attached to {@link #treeModel} for
     * detecting modifications.
     */
    @NotNull
    private final TreeModelListener treeModelListener = new TreeModelListener() {

        @Override
        public void treeNodesChanged(final TreeModelEvent e) {
            treeModelModified = true;
        }

        @Override
        public void treeNodesInserted(final TreeModelEvent e) {
            treeModelModified = true;
        }

        @Override
        public void treeNodesRemoved(final TreeModelEvent e) {
            treeModelModified = true;
        }

        @Override
        public void treeStructureChanged(final TreeModelEvent e) {
            treeModelModified = true;
        }

    };

    /**
     * Creates a new instance.
     * @param key the preferences key prefix
     */
    public MapMenu(@NotNull final String key) {
        mapMenuLoader = new MapMenuLoader(key);
        treeModel.addTreeModelListener(treeModelListener);
    }

    /**
     * Loads the contents from preferences.
     */
    public void load() {
        final int num = mapMenuLoader.loadNumEntries();
        root.removeAllChildren();
        for (int i = 0; i < num; i++) {
            final MapMenuLoader.Result result;
            try {
                result = mapMenuLoader.loadEntry(i);
            } catch (final IOException ex) {
                log.warn("dropping invalid bookmark: " + ex.getMessage());
                continue;
            }
            addMapMenuEntry(result.getDirectory(), result.getMapMenuEntry());
        }
        treeModelModified = false;
    }

    /**
     * Saves the contents to preferences if modified since last save.
     */
    public void save() {
        if (!treeModelModified) {
            return;
        }
        treeModelModified = false;

        saveAlways();
    }

    /**
     * Saves the contents to preferences.
     */
    public void saveAlways() {
        final int prevNum = mapMenuLoader.loadNumEntries();
        final int num = saveEntries(root, 0, "");
        mapMenuLoader.saveNumEntries(num);
        for (int i = num; i < prevNum; i++) {
            mapMenuLoader.removeEntry(i);
        }
    }

    /**
     * Saves a {@link TreeNode} instance's contents to preferences.
     * @param treeNode the tree node instance
     * @param startIndex the preference index for saving the first entry
     * @param directory the directory of the tree node
     * @return the preference index for saving the next entry
     */
    private int saveEntries(@NotNull final DefaultMutableTreeNode treeNode, final int startIndex, @NotNull final String directory) {
        final int[] index = { startIndex };
        for (int i = 0; i < treeModel.getChildCount(treeNode); i++) {
            final DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode) treeModel.getChild(treeNode, i);
            final MapMenuEntry childMapMenuEntry = (MapMenuEntry) childTreeNode.getUserObject();
            final String title = childMapMenuEntry.getTitle();
            final MapMenuEntryVisitor mapMenuEntryVisitor = new MapMenuEntryVisitor() {

                @Override
                public void visit(@NotNull final MapMenuEntryDir mapMenuEntry) {
                    mapMenuLoader.saveEntry(index[0]++, childMapMenuEntry.getTitle(), "", directory, MapMenuLoader.Type.DIR);
                    index[0] = saveEntries(childTreeNode, index[0], directory.isEmpty() ? title : directory + "/" + title);
                }

                @Override
                public void visit(@NotNull final MapMenuEntryMap mapMenuEntry) {
                    final MapMenuEntryMap mapMenuEntryMap = (MapMenuEntryMap) childMapMenuEntry;
                    mapMenuLoader.saveEntry(index[0]++, title, mapMenuEntryMap.getMapFile().getPath(), directory, MapMenuLoader.Type.MAP);
                }

            };
            childMapMenuEntry.visit(mapMenuEntryVisitor);
        }
        return index[0];
    }

    /**
     * Adds a {@link MapMenuEntry} to this menu.
     * @param directory the directory to add to
     * @param mapMenuEntry the map menu entry
     * @return the tree path for the inserted node
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    public TreePath addMapMenuEntry(@NotNull final String directory, @NotNull final MapMenuEntry mapMenuEntry) {
        return addMapMenuEntry(directory, new DefaultMutableTreeNode(mapMenuEntry, mapMenuEntry.allowsChildren()));
    }

    /**
     * Adds a {@link DefaultMutableTreeNode} to this menu.
     * @param directory the directory to add to
     * @param treeNode the tree node
     * @return the tree path for the inserted node
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    public TreePath addMapMenuEntry(@NotNull final String directory, @NotNull final DefaultMutableTreeNode treeNode) {
        final String[] paths = StringUtils.PATTERN_SLASH.split(directory);
        DefaultMutableTreeNode dir2 = root;
        for (final String path : paths) {
            if (!path.isEmpty()) {
                dir2 = getOrCreateDirectory(dir2, path);
            }
        }
        treeModel.insertNodeInto(treeNode, dir2, dir2.getChildCount());
        return new TreePath(treeModel.getPathToRoot(treeNode));
    }

    /**
     * Returns the {@link MapMenuEntryDir} for a given path. If more than one
     * matching path exists the last one is returned.
     * @param this2 the tree node to insert into
     * @param path the path; must not contain '/'
     * @return the entry
     */
    @NotNull
    public DefaultMutableTreeNode getOrCreateDirectory(@NotNull final MutableTreeNode this2, @NotNull final String path) {
        if (!MapMenuEntryDir.isValidDirectory(path)) {
            throw new IllegalArgumentException("invalid directory name '" + path + "'");
        }

        for (int i = this2.getChildCount() - 1; i >= 0; i--) {
            final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) this2.getChildAt(i);
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) treeNode.getUserObject();
            if (mapMenuEntry.allowsChildren() && mapMenuEntry.getTitle().equals(path)) {
                return treeNode;
            }
        }

        final DefaultMutableTreeNode new2 = new DefaultMutableTreeNode(new MapMenuEntryDir(path), true);
        treeModel.insertNodeInto(new2, this2, this2.getChildCount());
        return new2;
    }


    /**
     * Inserts a new node into the tree.
     * @param mapEntry the map entry to add
     * @param parent the parent node to add to
     * @param index the index to add at
     * @return the tree path for the inserted node
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    public TreePath insertNodeInto(@NotNull final MapMenuEntry mapEntry, @NotNull final DefaultMutableTreeNode parent, final int index) {
        final DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(mapEntry, mapEntry.allowsChildren());
        treeModel.insertNodeInto(treeNode, parent, index);
        save();
        return new TreePath(treeModel.getPathToRoot(treeNode));
    }

    /**
     * Removes a node from the tree.
     * @param treeNode the tree node to remove
     * @noinspection TypeMayBeWeakened
     */
    public void removeNode(@NotNull final DefaultMutableTreeNode treeNode) {
        if (treeNode != root) {
            final String directory = getDirectory(treeNode);
            treeModel.removeNodeFromParent(treeNode);
            deletedNodes.addFirst(new DeletedNode(directory, treeNode));
            while (deletedNodes.size() > 10) {
                deletedNodes.removeLast();
            }
            save();
        }
    }

    /**
     * Returns the directory of a {@link TreeNode}.
     * @param treeNode the tree node
     * @return the directory
     */
    @NotNull
    private String getDirectory(@NotNull final TreeNode treeNode) {
        final TreeNode[] treePath = treeModel.getPathToRoot(treeNode);
        if (treePath == null) {
            throw new IllegalArgumentException();
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i + 1 < treePath.length; i++) {
            final TreeNode tmp = treePath[i];
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) ((DefaultMutableTreeNode) tmp).getUserObject();
            if (sb.length() > 0) {
                sb.append('/');
            }
            sb.append(mapMenuEntry.getTitle());
        }
        return sb.toString();
    }

    /**
     * Returns the last deleted node.
     * @param delete whether to delete the returned node
     * @return the deleted node or <code>null</code>
     */
    @Nullable
    public DeletedNode getDeletedNode(final boolean delete) {
        return delete ? deletedNodes.pollFirst() : deletedNodes.peekFirst();
    }

    /**
     * Returns the number of entries in this menu.
     * @return the number of entries
     */
    public int size() {
        return size(root);
    }

    /**
     * Returns the number of entries in a sub-tree.
     * @param root the root of the sub-tree
     * @return the number of entries
     */
    private int size(@NotNull final TreeNode root) {
        final int childCount = treeModel.getChildCount(root);
        int result = childCount;
        for (int i = 0; i < childCount; i++) {
            final DefaultMutableTreeNode childTreeNode = (DefaultMutableTreeNode) treeModel.getChild(root, i);
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) childTreeNode.getUserObject();
            if (mapMenuEntry.allowsChildren()) {
                result += size(childTreeNode);
            }
        }
        return result;
    }

    /**
     * Creates a new {@link JTree} for this map menu.
     * @return the tree
     */
    @NotNull
    public JTree newTree() {
        return new AutoscrollJTree(treeModel);
    }

    /**
     * Returns the root node.
     * @return the root node
     */
    @NotNull
    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    /**
     * Result value consisting of a {@link TreeNode} and its location
     * (directory).
     * @noinspection PublicInnerClass
     */
    public static class DeletedNode {

        /**
         * The entry's directory.
         */
        @NotNull
        private final String directory;

        /**
         * The entry.
         */
        @NotNull
        private final DefaultMutableTreeNode treeNode;

        /**
         * Creates a new instance.
         * @param directory the entry's directory
         * @param treeNode the tree node
         */
        public DeletedNode(@NotNull final String directory, @NotNull final DefaultMutableTreeNode treeNode) {
            this.directory = directory;
            this.treeNode = treeNode;
        }

        /**
         * Returns the entry's directory.
         * @return the entry's directory
         */
        @NotNull
        public String getDirectory() {
            return directory;
        }

        /**
         * Returns the tree node.
         * @return the tree node
         */
        @NotNull
        public DefaultMutableTreeNode getTreeNode() {
            return treeNode;
        }

    }

}
