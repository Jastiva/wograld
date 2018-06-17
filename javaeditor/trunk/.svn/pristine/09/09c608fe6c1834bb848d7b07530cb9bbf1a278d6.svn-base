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

package net.sf.gridarta.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TreeNode implementation for Named Objects (like arches, faces, animations,
 * artifacts etc.), used for displaying faces in a {@link javax.swing.JTree}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class NamedTreeNode<E extends NamedObject> implements TreeNode, Comparable<NamedTreeNode<E>>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The children. They are mapped name as key to child node as value.
     * @serial
     */
    @NotNull
    private final SortedMap<String, NamedTreeNode<E>> childNodes = new TreeMap<String, NamedTreeNode<E>>();

    /**
     * The node array. It is required for implementing the TreeNode interface
     * with index methods since the TreeMap does not allow indexed access
     * despite the fact that it is ordered. Basically it is redundant.
     * @serial
     */
    @Nullable
    private NamedTreeNode<?>[] childNodeArray;

    /**
     * The parent node, which may be <code>null</code> for the root node.
     * @serial
     */
    @Nullable
    private final NamedTreeNode<E> parent;

    /**
     * The node name.
     * @serial
     */
    @NotNull
    private final String name;

    /**
     * The node object.
     * @serial
     */
    @Nullable
    private final NamedObject namedObject;

    /**
     * Directory state, <code>true</code> for directory nodes,
     * <code>false</code> for face nodes.
     * @serial
     */
    private final boolean dir;

    /**
     * Create a root node. All other nodes must be created via {@link
     * #append(NamedObject)}.
     */
    public NamedTreeNode() {
        this(true, null, "/");
    }

    /**
     * Create a NamedTreeNode. The constructor has been made private to force
     * users on factory methods
     * @param dir set this to <code>true</code> for directory nodes,
     * <code>false</code> for face nodes
     * @param parent the parent node or <code>null</code> for root node
     * @param name the name, which is the directory name for directory nodes and
     * the face name for face nodes
     */
    private NamedTreeNode(final boolean dir, @Nullable final NamedTreeNode<E> parent, @NotNull final String name) {
        this.dir = dir;
        this.parent = parent;
        this.name = name;
        namedObject = null;
    }

    /**
     * Create a NamedTreeNode. The constructor has been made private to force
     * users on factory methods
     * @param dir set this to <code>true</code> for directory nodes,
     * <code>false</code> for face nodes
     * @param parent the parent node or <code>null</code> for root node
     * @param namedObject the abstract named object to show as this tree node
     */
    private NamedTreeNode(final boolean dir, @Nullable final NamedTreeNode<E> parent, @NotNull final NamedObject namedObject) {
        this.dir = dir;
        this.parent = parent;
        name = namedObject.getName();
        this.namedObject = namedObject;
    }

    /**
     * Append a node.
     * @param namedObject the node to append
     * @throws IllegalNamedObjectException if the named object cannot be added
     */
    public void append(@NotNull final NamedObject namedObject) throws IllegalNamedObjectException {
        if (parent != null) {
            throw new IllegalNamedObjectException("node '" + namedObject.getPath() + "' is not a root node");
        }
        append(namedObject.getPath(), namedObject);
    }

    /**
     * Appends a node. Direct invocation should only occur on the root node.
     * @param path the path of node to append (parent path eventually removed)
     * @param namedObject the abstract named object to show as this tree node
     * @throws IllegalNamedObjectException if the named object cannot be added
     */
    private void append(@NotNull final String path, @NotNull final NamedObject namedObject) throws IllegalNamedObjectException {
        childNodeArray = null;
        if (!dir) {
            throw new IllegalNamedObjectException("cannot insert '" + path + "' into non-directory node for '" + namedObject.getPath() + "'");
        }
        String realName = path.startsWith("/") ? path.substring(1) : path;
        // Now we have the child node name in realName
        if (realName.contains("/")) {
            // Find directory node and append there
            final String dirName = realName.substring(0, realName.indexOf('/'));
            realName = realName.substring(realName.indexOf('/'));
            NamedTreeNode<E> dirNode = childNodes.get(dirName);
            if (dirNode == null) {
                dirNode = new NamedTreeNode<E>(true, this, dirName);
                childNodes.put(dirName, dirNode);
            }
            dirNode.append(realName, namedObject);
        } else {
            // Append face node to this
            //realName = realName.substring(0, realName.length() - 4);
            final NamedTreeNode<E> faceNode = new NamedTreeNode<E>(false, this, namedObject);
            childNodes.put(realName, faceNode);
        }
    }

    /**
     * Makes sure that the variable childNodeArray points to a valid array.
     */
    private void initLazyArray() {
        if (childNodeArray == null) {
            childNodeArray = childNodes.values().toArray(new NamedTreeNode<?>[childNodes.size()]);
        }
    }

    // --Commented out by Inspection START (09.06.05 04:00):
    //    /** Check whether this is a directory node.
    //     * @return <code>true</code> for directory nodes, <code>false</code> for face nodes
    //     */
    //    public final boolean isDir() {
    //        return dir;
    //    }
    // --Commented out by Inspection STOP (09.06.05 04:00)

    /**
     * Returns the AbstractNamedObject of this node. Only returns an object for
     * leaf nodes. For dir nodes, it returns <code>null</code>
     * @return the abstract named object of leaf node / <code>null</code> for
     *         dir nodes
     */
    @Nullable
    public NamedObject getNamedObject() {
        return namedObject;
    }

    /**
     * Returns the name.
     * @return the name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Enumeration<NamedTreeNode<E>> children() {
        return Collections.enumeration(childNodes.values());
    }

    /**
     * {@inheritDoc}
     * @return the same value as (currently unused) <code>isDir()</code>
     */
    @Override
    public boolean getAllowsChildren() {
        return dir;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public TreeNode getChildAt(final int childIndex) {
        initLazyArray();
        assert childNodeArray != null;
        return childNodeArray[childIndex];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getChildCount() {
        return childNodes.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex(@NotNull final TreeNode node) {
        initLazyArray();
        final int result = Arrays.binarySearch(childNodeArray, node);
        return result < 0 ? -1 : result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public NamedTreeNode<E> getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     * @return <code>true</code> for face nodes, <code>false</code> for
     *         directory nodes
     */
    @Override
    public boolean isLeaf() {
        return !dir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final NamedTreeNode<?> namedTreeNode = (NamedTreeNode<?>) obj;
        return dir == namedTreeNode.dir && name.equals(namedTreeNode.name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (dir ? 1 : 0) ^ name.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull final NamedTreeNode<E> o) {
        return dir ^ !o.dir ? name.compareTo(o.name) : dir ? -1 : 1;
    }

    /**
     * Get the path for a node.
     * @param path the String specifying the node
     * @return the tree path pointing to initial
     */
    @NotNull
    public TreePath getPathFor(@NotNull final String path) {
        final Collection<NamedTreeNode<E>> nodes = new ArrayList<NamedTreeNode<E>>();
        String currentPath = path;
        // XXX This is ugly but it works, no time to think about it now.
        String dirPart;
        for (NamedTreeNode<E> currentNode = this; currentNode != null; currentNode = currentNode.childNodes.get(dirPart)) {
            nodes.add(currentNode);
            if (currentPath.indexOf('/', 1) >= 0) {
                dirPart = currentPath.substring(1, currentPath.indexOf('/', 1));
                currentPath = currentPath.substring(currentPath.indexOf('/', 1));
            } else if (currentPath.indexOf('/') >= 0) {
                dirPart = currentPath.substring(1);
                currentPath = "";
            } else {
                break;
            }
        }
        return new TreePath(nodes.toArray());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return name;
    }

}
