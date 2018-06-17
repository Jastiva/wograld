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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks {@link JTree} instances for drop targets. Supports dropping into
 * branch nodes.
 * @author Andreas Kirschbaum
 */
public class TreeDropTarget {

    /**
     * The {@link DropTargetListener} attached to the tree.
     * @noinspection FieldCanBeLocal
     */
    @NotNull
    private final DropTargetListener dropTargetListener = new DropTargetListener() {

        @Override
        public void dragEnter(@NotNull final DropTargetDragEvent dtde) {
            checkDragEvent(dtde);
        }

        @Override
        public void dragOver(@NotNull final DropTargetDragEvent dtde) {
            checkDragEvent(dtde);
        }

        @Override
        public void dropActionChanged(@NotNull final DropTargetDragEvent dtde) {
            checkDragEvent(dtde);
        }

        @Override
        public void dragExit(@NotNull final DropTargetEvent dte) {
            // ignore
        }

        @Override
        public void drop(@NotNull final DropTargetDropEvent dtde) {
            final Point location = dtde.getLocation();
            final DropTargetContext dropTargetContext = dtde.getDropTargetContext();
            final JTree tree = (JTree) dropTargetContext.getComponent();
            final TreePath parentTreePath = tree.getClosestPathForLocation(location.x, location.y);
            final MutableTreeNode parentTreeNode = (MutableTreeNode) parentTreePath.getLastPathComponent();

            final int dropAction = dtde.getDropAction();
            if (dropAction != DnDConstants.ACTION_COPY && dropAction != DnDConstants.ACTION_MOVE) {
                dtde.rejectDrop();
                return;
            }

            try {
                final Transferable transferable = dtde.getTransferable();
                final DataFlavor[] flavors = transferable.getTransferDataFlavors();
                for (final DataFlavor flavor : flavors) {
                    if (transferable.isDataFlavorSupported(flavor)) {
                        dtde.acceptDrop(dropAction);
                        final Rectangle parentRectangle = tree.getPathBounds(parentTreePath);
                        final int relativeY = location.y - parentRectangle.y;
                        final TreePath treePath = (TreePath) transferable.getTransferData(flavor);
                        final MutableTreeNode draggedTreeNode = (MutableTreeNode) treePath.getLastPathComponent();
                        final DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
                        final MutableTreeNode parent;
                        final int index;
                        if (parentTreeNode == treeModel.getRoot()) {
                            // drop into root => insert into root
                            // insert at front of parentTreeNode
                            parent = parentTreeNode;
                            index = 0;
                        } else if (!parentTreeNode.getAllowsChildren()) {
                            // drop into leaf => insert before or after leaf
                            parent = (MutableTreeNode) parentTreeNode.getParent();
                            assert parent != null;
                            if (relativeY < parentRectangle.height / 2) {
                                // insert before parentTreeNode
                                index = treeModel.getIndexOfChild(parent, parentTreeNode);
                            } else {
                                // insert after parentTreeNode
                                index = treeModel.getIndexOfChild(parent, parentTreeNode) + 1;
                            }
                        } else if (tree.isExpanded(parentTreePath)) {
                            // drop into expanded branch => insert before branch or into branch
                            if (relativeY < parentRectangle.height / 2) {
                                // insert before parentTreeNode
                                parent = (MutableTreeNode) parentTreeNode.getParent();
                                assert parent != null;
                                index = treeModel.getIndexOfChild(parent, parentTreeNode);
                            } else {
                                // insert at front of parentTreeNode
                                parent = parentTreeNode;
                                index = 0;
                            }
                        } else {
                            // drop into collapsed branch => insert before, into, or after branch
                            if (relativeY < parentRectangle.height / 4) {
                                // insert before parentTreeNode
                                parent = (MutableTreeNode) parentTreeNode.getParent();
                                assert parent != null;
                                index = treeModel.getIndexOfChild(parent, parentTreeNode);
                            } else if (relativeY >= parentRectangle.height * 3 / 4) {
                                // insert after parentTreeNode
                                parent = (MutableTreeNode) parentTreeNode.getParent();
                                assert parent != null;
                                index = treeModel.getIndexOfChild(parent, parentTreeNode) + 1;
                            } else {
                                // insert at end of parentTreeNode
                                parent = parentTreeNode;
                                index = parent.getChildCount();
                            }
                        }
                        treeModel.insertNodeInto(draggedTreeNode, parent, index);
                        tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(draggedTreeNode)));
                        dtde.dropComplete(true);
                        return;
                    }
                }
            } catch (final IOException ignored) {
                // ignore
            } catch (final UnsupportedFlavorException ignored) {
                // ignore
            }
            dtde.rejectDrop();
        }

    };

    /**
     * Creates a new instance.
     * @param tree the tree to monitor
     */
    public TreeDropTarget(@NotNull final JTree tree) {
        //noinspection ResultOfObjectAllocationIgnored
        new DropTarget(tree, dropTargetListener);
    }

    /**
     * Called when a drag operation is ongoing.
     * @param dtde the drop target drag event
     */
    private static void checkDragEvent(@NotNull final DropTargetDragEvent dtde) {
        final int dragOperation = dtde.getDropAction();
        switch (dragOperation) {
        case DnDConstants.ACTION_COPY:
        case DnDConstants.ACTION_MOVE:
            dtde.acceptDrag(dragOperation);
            return;
        }

        dtde.rejectDrag();
    }

}
