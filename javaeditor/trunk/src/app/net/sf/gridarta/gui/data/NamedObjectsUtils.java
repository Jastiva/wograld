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

package net.sf.gridarta.gui.data;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.sf.gridarta.model.data.NamedObject;
import net.sf.gridarta.model.data.NamedObjects;
import net.sf.gridarta.model.data.NamedTreeNode;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for {@link NamedObjects} related functions.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class NamedObjectsUtils {

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Private constructor to prevent instantiation.
     */
    private NamedObjectsUtils() {
    }

    /**
     * Show a dialog for choosing from the object tree.
     * @param parentComponent parent component to show dialog on
     * @param initial initially selected node name
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param namedObjects the named objects tree to show
     * @return name of selected node or <code>null</code> if the user abandoned
     *         the selection dialog by using cancel or close instead of okay
     */
    
    // if the filetreechooseaction is lacking hierarchy
    // this may mean that no path was included in the wograld.0 file
    // which depending on if it came from Gridarta's collector, may be result of Collector
    // or else may be the way the wograld.0 file was issued with server
    
    
    @Nullable
    public static <E extends NamedObject> String showNodeChooserDialog(@NotNull final Component parentComponent, @NotNull final String initial, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final NamedObjects<E> namedObjects) {
        final NamedObject initialObject = namedObjects.get(initial);
        final String initialPath = initialObject != null ? initialObject.getPath() : initial;
        final JTree tree = new JTree(namedObjects.getTreeRoot());
        tree.setCellRenderer(new NamedNodeTreeCellRenderer(faceObjectProviders));
        final JScrollPane scrollPane = new JScrollPane(tree);
        tree.setExpandsSelectedPaths(true);
        final TreePath path = namedObjects.getTreeRoot().getPathFor(initialPath);
        tree.makeVisible(path);
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
        final JOptionPane pane = new JOptionPane(scrollPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);
        final Window dialog = pane.createDialog(parentComponent, ACTION_BUILDER.format("chooseNamedObject.title", namedObjects.getName()));
        pane.selectInitialValue();
        //noinspection RefusedBequest
        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null) {
                        final TreeNode node = (TreeNode) selPath.getLastPathComponent();
                        if (node.isLeaf()) {
                            pane.setValue(0);
                        }
                    }
                }
            }

        });
        dialog.setVisible(true);
        dialog.dispose();
        // This only looks inconvertible. getValue() returns an Integer here.
        if (pane.getValue() == null || !pane.getValue().equals(0)) {
            return null;
        }

        final TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            return null;
        }

        return ((NamedTreeNode<?>) selectionPath.getLastPathComponent()).getName();
        //return showConfirmDialog(parentComponent, scrollPane, "Choose a face", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == OK_OPTION && tree.getSelectionPath() != null ? ((NamedTreeNode<?>) tree.getSelectionPath().getLastPathComponent()).getName() : null;
    }

}
