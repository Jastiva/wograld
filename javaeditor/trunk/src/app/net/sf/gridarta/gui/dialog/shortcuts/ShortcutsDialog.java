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

package net.sf.gridarta.gui.dialog.shortcuts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.IconManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShortcutsDialog extends JOptionPane {

    /**
     * The serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Prefix for internal category names. Used only for sorting tree nodes.
     */
    @NotNull
    private static final String CATEGORY_PREFIX = "1";

    /**
     * Prefix for internal action names. Used only for sorting tree nodes.
     */
    @NotNull
    private static final String ACTION_PREFIX = "2";

    /**
     * The {@link ShortcutsManager} to affect.
     */
    @NotNull
    private final ShortcutsManager shortcutsManager;

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link Pattern} to split a list of action categories.
     */
    @NotNull
    private static final Pattern PATTERN_CATEGORIES = StringUtils.PATTERN_COMMA;

    /**
     * The {@link Pattern} to split a category into sub-categories.
     */
    @NotNull
    private static final Pattern PATTERN_SUB_CATEGORIES = StringUtils.PATTERN_SLASH;

    /**
     * The {@link JButton} for ok.
     * @serial
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "shortcutsOkay", this));

    /**
     * The {@link Action} for the "set shortcut" button.
     * @serial
     */
    @NotNull
    private final Action aSetShortcut = ACTION_BUILDER.createAction(false, "shortcutsSetShortcut", this);

    /**
     * The {@link Action} for the "unset shortcut" button.
     * @serial
     */
    @NotNull
    private final Action aUnsetShortcut = ACTION_BUILDER.createAction(false, "shortcutsUnsetShortcut", this);

    /**
     * The {@link JDialog} instance.
     * @serial
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The {@link JTree} showing all actions.
     * @serial
     */
    @NotNull
    private final JTree actionsTree = new JTree() {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public String convertValueToText(final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
            final Action action = getAction(value);
            if (action != null) {
                return ActionUtils.getActionName(action);
            }

            return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
        }

    };


    /**
     * The description of the selected action.
     * @serial
     */
    @NotNull
    private final JTextArea actionDescription = new JTextArea();

    /**
     * The shortcut of the selected action.
     * @serial
     */
    @NotNull
    private final JTextArea actionShortcut = new JTextArea();

    /**
     * The selected {@link Action} or <code>null</code>.
     */
    @Nullable
    private Action selectedAction;

    /**
     * Creates a new instance.
     * @param parentComponent the parent component for the dialog
     * @param shortcutsManager the shortcuts manager to affect
     */
    public ShortcutsDialog(@NotNull final Component parentComponent, @NotNull final ShortcutsManager shortcutsManager) {
        this.shortcutsManager = shortcutsManager;
        okButton.setDefaultCapable(true);
        final JButton defaultsButton = new JButton(ACTION_BUILDER.createAction(false, "shortcutsDefaults", this));
        setOptions(new Object[] { okButton, defaultsButton });

        setMessage(createPanel());

        dialog = createDialog(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "shortcuts.title"));
        dialog.setResizable(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setModal(true);

        dialog.setMinimumSize(new Dimension(400, 300));
        dialog.setPreferredSize(new Dimension(800, 600));
        dialog.pack();
    }

    /**
     * Opens the dialog. Returns when the dialog has been dismissed.
     * @param parentComponent the parent component for the dialog
     */
    public void showDialog(@NotNull final Component parentComponent) {
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);
        setInitialValue(actionsTree);
        actionsTree.setSelectionRow(0);
        updateSelectedAction();
    }

    /**
     * Creates the GUI.
     * @return the panel containing the GUI
     */
    @NotNull
    private JPanel createPanel() {
        final DefaultMutableTreeNode top = new DefaultMutableTreeNode(ActionBuilderUtils.getString(ACTION_BUILDER, "shortcuts.allActions"));
        createNodes(top);

        actionsTree.setModel(new DefaultTreeModel(top, false));
        actionsTree.setRootVisible(false);
        actionsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        final TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

            @Override
            public void valueChanged(@NotNull final TreeSelectionEvent e) {
                setSelectedAction(getAction(actionsTree.getLastSelectedPathComponent()));
            }

        };
        actionsTree.addTreeSelectionListener(treeSelectionListener);

        final Icon emptyIcon = IconManager.getDefaultIconManager().getIcon(ActionBuilderUtils.getString(ACTION_BUILDER, "shortcuts.defaultIcon"));
        //noinspection RefusedBequest
        final TreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer() {

            /**
             * The serial version UID.
             */
            private static final long serialVersionUID = 1L;

            /**
             * The leaf icon. Set to <code>null</code> to use the default icon.
             */
            @Nullable
            private Icon icon;

            @Override
            public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
                if (leaf) {
                    final Action action = getAction(value);
                    if (action != null) {
                        final Icon tmpIcon = ActionUtils.getActionIcon(action);
                        icon = tmpIcon == null ? emptyIcon : tmpIcon;
                        setToolTipText(ActionUtils.getActionDescription(action));
                    } else {
                        icon = emptyIcon;
                    }
                } else {
                    icon = emptyIcon;
                }
                return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            }

            @Override
            public Icon getLeafIcon() {
                return icon;
            }

        };
        actionsTree.setCellRenderer(treeCellRenderer);

        ToolTipManager.sharedInstance().registerComponent(actionsTree);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        final JScrollPane actionsScrollPane = new JScrollPane();
        actionsScrollPane.setViewportView(actionsTree);
        actionsScrollPane.setBackground(actionsTree.getBackground());
        actionsScrollPane.getViewport().add(actionsTree);
        actionsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        actionsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        actionsScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        final JComponent actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "shortcuts.borderAllActions")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        actionsPanel.add(actionsScrollPane, gbc);

        actionDescription.setEditable(false);
        actionDescription.setColumns(20);
        actionDescription.setLineWrap(true);
        actionDescription.setWrapStyleWord(true);
        actionDescription.setBorder(new LineBorder(Color.black));
        actionDescription.setFocusable(false);

        actionShortcut.setEditable(false);
        actionShortcut.setColumns(20);
        actionShortcut.setRows(3);
        actionShortcut.setLineWrap(true);
        actionShortcut.setWrapStyleWord(true);
        actionShortcut.setBorder(new LineBorder(Color.black));
        actionShortcut.setFocusable(false);

        final Container actionDescriptionPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        actionDescriptionPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "shortcuts.actionDescription"), gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        actionDescriptionPanel.add(actionDescription, gbc);

        final Container actionShortcutPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        actionShortcutPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "shortcuts.shortcut"), gbc);
        gbc.gridy++;
        actionShortcutPanel.add(actionShortcut, gbc);

        final Component addShortcutButton = new JButton(aSetShortcut);
        final Component removeShortcutButton = new JButton(aUnsetShortcut);
        final Container actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        actionButtonsPanel.add(addShortcutButton, gbc);
        gbc.gridy++;
        actionButtonsPanel.add(removeShortcutButton, gbc);

        final JComponent selectedActionPanel = new JPanel();
        selectedActionPanel.setLayout(new GridBagLayout());
        selectedActionPanel.setBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "shortcuts.selectedAction")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        selectedActionPanel.add(actionDescriptionPanel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.gridy++;
        selectedActionPanel.add(actionShortcutPanel, gbc);
        gbc.gridy++;
        selectedActionPanel.add(actionButtonsPanel, gbc);

        final JPanel panel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(actionsPanel, gbc);
        gbc.gridx++;
        gbc.weightx = 0.0;
        panel.add(selectedActionPanel, gbc);

        panel.setBorder(GUIConstants.DIALOG_BORDER);
        return panel;
    }

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void shortcutsOkay() {
        setValue(okButton);
    }

    /**
     * Action method for restore to defaults.
     */
    @ActionMethod
    public void shortcutsDefaults() {
        if (ACTION_BUILDER.showQuestionDialog(dialog, "shortcutsRestoreDefaults")) {
            shortcutsManager.revertAll();
            updateSelectedAction();
            shortcutsManager.saveShortcuts();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (newValue != UNINITIALIZED_VALUE) {
            dialog.dispose();
        }
    }

    /**
     * Creates nodes for all actions.
     * @param root the root node
     */
    private void createNodes(@NotNull final DefaultMutableTreeNode root) {
        for (final Action action : shortcutsManager) {
            final String categories = ActionUtils.getActionCategory(action);
            for (final String category : PATTERN_CATEGORIES.split(categories, -1)) {
                addNode(root, category, action);
            }
        }
    }

    /**
     * Adds an {@link Action} to a branch node.
     * @param root the root node
     * @param category the category to add to
     * @param action the action to add
     */
    private static void addNode(@NotNull final DefaultMutableTreeNode root, @NotNull final CharSequence category, @NotNull final Action action) {
        final DefaultMutableTreeNode node = getOrCreateNodeForCategory(root, category);
        final MutableTreeNode treeNode = new DefaultMutableTreeNode(action, false);
        insertChildNode(node, treeNode);
    }

    /**
     * Returns the branch {@link DefaultMutableTreeNode} for a given category.
     * @param root the root node to start from
     * @param category the category
     * @return the branch node for the category
     */
    @NotNull
    private static DefaultMutableTreeNode getOrCreateNodeForCategory(@NotNull final DefaultMutableTreeNode root, @NotNull final CharSequence category) {
        DefaultMutableTreeNode node = root;
        for (final String subCategory : PATTERN_SUB_CATEGORIES.split(category, -1)) {
            node = getOrCreateChildNode(node, subCategory);
        }
        return node;
    }

    /**
     * Returns a child node by category name.
     * @param root the root node
     * @param subCategory the node name
     * @return the child node
     */
    @NotNull
    private static DefaultMutableTreeNode getOrCreateChildNode(@NotNull final MutableTreeNode root, @NotNull final String subCategory) {
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final TreeNode treeNode = root.getChildAt(i);
            final String nodeTitle = getTitle(treeNode);
            if (nodeTitle != null && nodeTitle.equals(CATEGORY_PREFIX + subCategory)) {
                assert treeNode instanceof DefaultMutableTreeNode;
                return (DefaultMutableTreeNode) treeNode;
            }
        }

        final DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(subCategory);
        insertChildNode(root, treeNode);
        return treeNode;
    }

    /**
     * Inserts a new child node into a branch node.
     * @param branchNode the branch node
     * @param childNode the child node
     */
    private static void insertChildNode(@NotNull final MutableTreeNode branchNode, @NotNull final MutableTreeNode childNode) {
        branchNode.insert(childNode, getInsertionIndex(branchNode, childNode));
    }

    /**
     * Returns the index to insert a new child node into a parent node.
     * @param parentNode the parent node
     * @param childNode the child node
     * @return the insertion index
     */
    private static int getInsertionIndex(@NotNull final TreeNode parentNode, @NotNull final TreeNode childNode) {
        final String childTitle = getTitle(childNode);
        if (childTitle == null) {
            throw new IllegalArgumentException();
        }

        final int childCount = parentNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final TreeNode treeNode = parentNode.getChildAt(i);
            final String nodeTitle = getTitle(treeNode);
            if (nodeTitle != null && nodeTitle.compareToIgnoreCase(childTitle) > 0) {
                return i;
            }
        }

        return childCount;
    }

    /**
     * Returns the {@link Action} for a node in the action tree.
     * @param node the node
     * @return the action or <code>null</code>
     */
    @Nullable
    private static Action getAction(@Nullable final Object node) {
        if (node == null || !(node instanceof DefaultMutableTreeNode)) {
            return null;
        }

        final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) node;
        final Object userObject = defaultMutableTreeNode.getUserObject();
        if (userObject == null || !(userObject instanceof Action)) {
            return null;
        }

        return (Action) userObject;
    }

    /**
     * Returns the category for a node in the action tree.
     * @param node the node
     * @return the category or <code>null</code>
     */
    @Nullable
    private static String getTitle(@NotNull final TreeNode node) {
        if (!(node instanceof DefaultMutableTreeNode)) {
            return null;
        }

        final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) node;
        final Object userObject = defaultMutableTreeNode.getUserObject();
        if (userObject == null) {
            return null;
        }

        if (userObject instanceof String) {
            return CATEGORY_PREFIX + userObject;
        }

        if (userObject instanceof Action) {
            return ACTION_PREFIX + ActionUtils.getActionName((Action) userObject);
        }

        return null;
    }

    /**
     * Updates the selected action.
     * @param selectedAction the new selected action
     */
    private void setSelectedAction(@Nullable final Action selectedAction) {
        if (this.selectedAction == selectedAction) {
            return;
        }

        this.selectedAction = selectedAction;
        updateSelectedAction();
    }

    /**
     * Updates the information shown for the selected action.
     */
    private void updateSelectedAction() {
        actionDescription.setText(selectedAction == null ? "" : ActionUtils.getActionDescription(selectedAction));
        if (selectedAction == null) {
            actionShortcut.setText("");
        } else {
            actionShortcut.setText(ActionUtils.getShortcutDescription(selectedAction, Action.ACCELERATOR_KEY));
        }
        aSetShortcut.setEnabled(selectedAction != null);
        aUnsetShortcut.setEnabled(getUnsetShortcutEnabled() != null);
    }

    /**
     * Returns whether "unset shortcut" is enabled.
     * @return the action to affect if enabled or <code>null</code>
     */
    @Nullable
    private Action getUnsetShortcutEnabled() {
        final Action action = selectedAction;
        return action != null && ActionUtils.getShortcut(action) != null ? action : null;
    }

    /**
     * The action method for the "set shortcut" button.
     */
    @ActionMethod
    public void shortcutsSetShortcut() {
        final Action action = selectedAction;
        if (action == null) {
            return;
        }

        final KeyStrokeDialog keyStrokeDialog = new KeyStrokeDialog(dialog, shortcutsManager, action);
        if (keyStrokeDialog.showDialog(dialog)) {
            ActionUtils.setActionShortcut(action, keyStrokeDialog.getKeyStroke());
            updateSelectedAction();
            shortcutsManager.saveShortcuts();
        }
    }

    /**
     * The action method for the "set shortcut" button.
     */
    @ActionMethod
    public void shortcutsUnsetShortcut() {
        final Action action = getUnsetShortcutEnabled();
        if (action != null) {
            ActionUtils.setActionShortcut(action, null);
            updateSelectedAction();
            shortcutsManager.saveShortcuts();
        }
    }

}
