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

package net.sf.gridarta.gui.dialog.bookmarks;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.dnd.DnDConstants;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.gui.mapmenu.MapMenu;
import net.sf.gridarta.gui.mapmenu.MapMenuEntry;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryDir;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryMap;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryTreeCellRenderer;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryVisitor;
import net.sf.gridarta.gui.mapmenu.TreeDragSource;
import net.sf.gridarta.gui.mapmenu.TreeDropTarget;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog that displays existing bookmarks and allows to edit or remove them.
 * @author Andreas Kirschbaum
 */
public class ManageBookmarksDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

    /**
     * The serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link MapImageCache} for creating map previews.
     */
    @NotNull
    private final MapImageCache<G, A, R> mapImageCache;

    /**
     * The {@link MapMenu} being edited.
     */
    @NotNull
    private final MapMenu mapMenu;

    /**
     * The {@link MapMenuEntryIcons} for looking up icons.
     */
    @NotNull
    private final MapMenuEntryIcons mapMenuEntryIcons;

    /**
     * The {@link JTree} of all bookmarks.
     * @serial
     */
    @NotNull
    private final JTree bookmarksTree;

    /**
     * The map preview image.
     * @serial
     */
    @NotNull
    private final JLabel preview = new JLabel();

    /**
     * The dialog.
     * @serial
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The {@link Action} for "edit bookmark".
     * @serial
     */
    @NotNull
    private final Action editAction = ACTION_BUILDER.createAction(false, "manageBookmarksEdit", this);

    /**
     * The {@link Action} for "remove bookmark".
     * @serial
     */
    @NotNull
    private final Action removeAction = ACTION_BUILDER.createAction(false, "manageBookmarksRemove", this);

    /**
     * The {@link Action} for "remove bookmark".
     * @serial
     */
    @NotNull
    private final Action unDeleteAction = ACTION_BUILDER.createAction(false, "manageBookmarksUnDelete", this);

    /**
     * The {@link Action} for "new directory".
     * @serial
     */
    @NotNull
    private final Action newDirectoryAction = ACTION_BUILDER.createAction(false, "manageBookmarksNewDirectory", this);

    /**
     * The {@link JButton} for cancel.
     * @serial
     */
    @NotNull
    private final JButton closeButton = new JButton(ACTION_BUILDER.createAction(false, "manageBookmarksClose", this));

    /**
     * The currently selected entry.
     * @serial
     */
    @Nullable
    private TreePath selectedTreePath;

    /**
     * Creates a new instance.
     * @param parentComponent the parent component for the dialog
     * @param mapImageCache the map image cache for creating map previews
     * @param mapMenu the map menu to edit
     */
    public ManageBookmarksDialog(@NotNull final Component parentComponent, @NotNull final MapImageCache<G, A, R> mapImageCache, @NotNull final MapMenu mapMenu) {
        this.mapImageCache = mapImageCache;
        this.mapMenu = mapMenu;
        mapMenuEntryIcons = new MapMenuEntryIcons(mapImageCache);
        bookmarksTree = mapMenu.newTree();
        //noinspection ResultOfObjectAllocationIgnored
        new TreeDragSource(bookmarksTree, DnDConstants.ACTION_COPY_OR_MOVE);
        //noinspection ResultOfObjectAllocationIgnored
        new TreeDropTarget(bookmarksTree);
        final JButton editButton = new JButton(editAction);
        final JButton removeButton = new JButton(removeAction);
        final JButton unDeleteButton = new JButton(unDeleteAction);
        final JButton newDirectoryButton = new JButton(newDirectoryAction);
        setOptions(new Object[] { editButton, removeButton, unDeleteButton, newDirectoryButton, closeButton });

        setMessage(createPanel());

        dialog = createDialog(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "manageBookmarks.title"));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(false);
        dialog.setResizable(true);
        dialog.pack();

        updateActions();
    }

    /**
     * Opens the dialog. Returns after the dialog has been dismissed.
     * @param parentComponent the parent component for the dialog
     */
    public void showDialog(@NotNull final Component parentComponent) {
        setInitialValue(bookmarksTree);
        bookmarksTree.setRootVisible(false);
        bookmarksTree.setSelectionRow(0);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setMinimumSize(new Dimension(600, 300));
        dialog.setPreferredSize(new Dimension(800, 600));
        dialog.setVisible(true);
        dialog.toFront();
    }

    /**
     * Creates the GUI.
     * @return the panel containing the GUI
     */
    @NotNull
    private JPanel createPanel() {
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        preview.setPreferredSize(new Dimension(200, 200));

        final JPanel mainPanel = new JPanel(new GridBagLayout());

        mainPanel.setBorder(GUIConstants.DIALOG_BORDER);

        final JScrollPane bookmarksScrollPane = new JScrollPane();
        bookmarksScrollPane.setViewportView(bookmarksTree);
        bookmarksScrollPane.setBackground(bookmarksTree.getBackground());
        bookmarksScrollPane.getViewport().add(bookmarksTree);
        bookmarksScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bookmarksScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        bookmarksScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(bookmarksScrollPane, gbc);

        gbc.weightx = 0.0;
        gbc.gridx = 1;
        mainPanel.add(preview, gbc);

        bookmarksTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        bookmarksTree.setCellRenderer(new MapMenuEntryTreeCellRenderer(mapImageCache));

        final TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                updateSelectedBookmark();
            }

        };
        bookmarksTree.addTreeSelectionListener(treeSelectionListener);
        updateSelectedBookmark();

        return mainPanel;
    }

    /**
     * Updates {@link #selectedTreePath} from {@link #bookmarksTree}.
     */
    private void updateSelectedBookmark() {
        selectedTreePath = bookmarksTree.getSelectionPath();
        updateActions();
    }

    /**
     * Action method for "edit bookmark".
     */
    @ActionMethod
    public void manageBookmarksEdit() {
        doEditBookmark(true);
    }

    /**
     * Action method for "remove bookmark".
     */
    @ActionMethod
    public void manageBookmarksRemove() {
        doRemoveBookmark(true);
    }

    /**
     * Action method for "undo deletion".
     */
    @ActionMethod
    public void manageBookmarksUnDelete() {
        doUnDeleteBookmark(true);
    }

    /**
     * Action method for "new directory".
     */
    @ActionMethod
    public void manageBookmarksNewDirectory() {
        doNewDirectory(true);
    }

    /**
     * Action method for "close bookmarks dialog".
     */
    @ActionMethod
    public void manageBookmarksClose() {
        setValue(closeButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (newValue != UNINITIALIZED_VALUE) {
            mapMenu.save();
        }
    }

    /**
     * Edits the selected bookmark.
     * @param performAction whether the action should be performed
     * @return whether the action can or was performed
     */
    private boolean doEditBookmark(final boolean performAction) {
        final TreePath tmpSelectedTreePath = selectedTreePath;
        if (tmpSelectedTreePath == null) {
            return false;
        }

        final DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) tmpSelectedTreePath.getLastPathComponent();
        if (selectedTreeNode == mapMenu.getRoot()) {
            return false;
        }

        if (performAction) {
            final DefaultMutableTreeNode tmpSelectedTreeNode = (DefaultMutableTreeNode) tmpSelectedTreePath.getLastPathComponent();
            final MapMenuEntryVisitor mapMenuEntryVisitor = new MapMenuEntryVisitor() {

                @Override
                public void visit(@NotNull final MapMenuEntryDir mapMenuEntry) {
                    final BookmarkDirectoryDialog bookmarkDirectoryDialog = new BookmarkDirectoryDialog(ManageBookmarksDialog.this, mapMenuEntry.getTitle());
                    if (bookmarkDirectoryDialog.showDialog()) {
                        mapMenuEntry.setTitle(bookmarkDirectoryDialog.getDirectory());
                        bookmarksTree.getModel().valueForPathChanged(tmpSelectedTreePath, mapMenuEntry);
                    }
                }

                @Override
                public void visit(@NotNull final MapMenuEntryMap mapMenuEntry) {
                    final EditBookmarkDialog editBookmarkDialog = new EditBookmarkDialog(ManageBookmarksDialog.this, mapMenuEntry.getTitle());
                    if (editBookmarkDialog.showDialog()) {
                        mapMenuEntry.setTitle(editBookmarkDialog.getDescription());
                        bookmarksTree.getModel().valueForPathChanged(tmpSelectedTreePath, mapMenuEntry);
                    }
                }

            };
            ((MapMenuEntry) tmpSelectedTreeNode.getUserObject()).visit(mapMenuEntryVisitor);
        }

        return true;
    }

    /**
     * Removes the selected bookmark.
     * @param performAction whether the action should be performed
     * @return whether the action can or was performed
     */
    private boolean doRemoveBookmark(final boolean performAction) {
        final TreePath tmpSelectedTreePath = selectedTreePath;
        if (tmpSelectedTreePath == null) {
            return false;
        }

        final DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) tmpSelectedTreePath.getLastPathComponent();
        if (selectedTreeNode == mapMenu.getRoot()) {
            return false;
        }

        if (performAction) {
            final int[] selectionRows = bookmarksTree.getSelectionRows();
            mapMenu.removeNode(selectedTreeNode);
            final int index = selectionRows == null ? 0 : selectionRows[0];
            if (index > 0) {
                bookmarksTree.setSelectionRow(index - 1);
            }
        }

        return true;
    }

    /**
     * Restores the last deleted bookmark.
     * @param performAction whether the action should be performed
     * @return whether the action can or was performed
     */
    private boolean doUnDeleteBookmark(final boolean performAction) {
        final MapMenu.DeletedNode deletedNode = mapMenu.getDeletedNode(performAction);
        if (deletedNode == null) {
            return false;
        }

        if (performAction) {
            final TreePath treePath = mapMenu.addMapMenuEntry(deletedNode.getDirectory(), deletedNode.getTreeNode());
            bookmarksTree.setSelectionPath(treePath);
        }

        return true;
    }

    /**
     * Create a new directory.
     * @param performAction whether the action should be performed
     * @return whether the action can or was performed
     */
    private boolean doNewDirectory(final boolean performAction) {
        final TreePath tmpSelectedTreePath = selectedTreePath;
        if (tmpSelectedTreePath == null) {
            return false;
        }

        if (performAction) {
            final DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) tmpSelectedTreePath.getLastPathComponent();
            final BookmarkDirectoryDialog bookmarkDirectoryDialog = new BookmarkDirectoryDialog(this, "");
            if (bookmarkDirectoryDialog.showDialog()) {
                final String directory = bookmarkDirectoryDialog.getDirectory();
                final MapMenuEntry mapEntry = new MapMenuEntryDir(directory);
                final DefaultMutableTreeNode parent;
                final int index;
                if (selectedTreeNode == mapMenu.getRoot()) {
                    parent = selectedTreeNode;
                    index = 0;
                } else {
                    parent = (DefaultMutableTreeNode) selectedTreeNode.getParent();
                    assert parent != null;
                    index = parent.getIndex(selectedTreeNode) + 1;
                    assert index != 0;
                }
                final TreePath treePath = mapMenu.insertNodeInto(mapEntry, parent, index);
                bookmarksTree.setSelectionPath(treePath);

            }
        }

        return true;
    }

    /**
     * Updates the actions' states to reflect the current selection.
     */
    private void updateActions() {
        editAction.setEnabled(doEditBookmark(false));
        removeAction.setEnabled(doRemoveBookmark(false));
        unDeleteAction.setEnabled(doUnDeleteBookmark(false));
        newDirectoryAction.setEnabled(doNewDirectory(false));
        @Nullable final Icon previewIcon;
        if (selectedTreePath == null) {
            previewIcon = null;
        } else {
            final DefaultMutableTreeNode tmpSelectedTreeNode = (DefaultMutableTreeNode) selectedTreePath.getLastPathComponent();
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) tmpSelectedTreeNode.getUserObject();
            previewIcon = mapMenuEntryIcons.getIcon(mapMenuEntry);
        }
        preview.setIcon(previewIcon);
    }

}
