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

package net.sf.gridarta.gui.treasurelist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.sf.gridarta.gui.dialog.help.Help;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import net.sf.gridarta.model.treasurelist.FolderTreasureObj;
import net.sf.gridarta.model.treasurelist.TreasureTree;
import net.sf.gridarta.model.treasurelist.TreasureTreeNode;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The CFTreasureListTree class fully manages treasurelists. CF data file
 * "treasures" gets parsed into a JTree structure.
 * @author Andreas Vogl
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo don't extend JTree, instead create a proper model
 */
public class CFTreasureListTree extends JTree {

    /**
     * The string displayed in attribute dialog for "none".
     */
    public static final String NONE_SYM = "<none>";

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * All defined treasure lists.
     */
    @NotNull
    private final TreasureTree treasureTree;

    /**
     * The main view.
     * @serial
     */
    @NotNull
    private final JFrame parent;

    /**
     * JDialog containing the tree.
     * @serial
     */
    @Nullable
    private JDialog frame;

    /**
     * The button for ok.
     * @serial
     */
    @NotNull
    private final AbstractButton okButton = new JButton("Select");

    /**
     * The button for none.
     * @serial
     */
    @NotNull
    private final AbstractButton noneButton = new JButton("None");

    /**
     * The text input field.
     * @serial
     */
    @Nullable
    private JTextComponent input;

    /**
     * Creates a new instance.
     * @param treasureTree the defined treasure lists
     * @param parent the parent frame for dialog boxes
     * @param archetypeSet the archetype set to get treasures from
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param systemIcons the system icons for creating icons
     */
    public CFTreasureListTree(@NotNull final TreasureTree treasureTree, @NotNull final JFrame parent, @NotNull final ArchetypeSet<?, ?, ?> archetypeSet, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final SystemIcons systemIcons) {
        super(treasureTree.getRoot());
        this.treasureTree = treasureTree;
        this.parent = parent;

        final FaceObjectProvidersListener faceObjectProvidersListener = new FaceObjectProvidersListener() {

            @Override
            public void facesReloaded() {
                repaint();
            }

        };
        faceObjectProviders.addFaceObjectProvidersListener(faceObjectProvidersListener);

        putClientProperty("JTree.lineStyle", "Angled");
        setCellRenderer(new TreasureCellRenderer(archetypeSet, treasureTree.getRoot(), faceObjectProviders, systemIcons));

        ActionUtils.newAction(ACTION_BUILDER, "Tool", this, "viewTreasurelists");
    }

    /**
     * Wrapper method for showing the dialog from the Resource menu.
     */
    @ActionMethod
    public void viewTreasurelists() {
        showDialog(null, parent);
    }

    /**
     * Shows the dialog window containing this tree. The user can select a
     * treasurelist which is returned as a string. The dialog window is built
     * only ONCE, then hidden/shown as needed. As a side-effect, only one
     * treasurelist window can be open at a time. When a second window is
     * opened, the first one gets (re-)moved.
     * @param input the textfield to show
     * @param parent the parent frame (attribute dialog)
     */
    public synchronized void showDialog(@Nullable final JTextComponent input, @NotNull final Component parent) {
        this.input = input;

        final boolean hasBeenDisplayed = frame != null;
        if (frame != null) {
            if (frame.isShowing()) {
                assert frame != null;
                frame.setVisible(false);
            }

            // collapse everything except root
            expandPath(new TreePath(treasureTree.getRoot()));
            for (int i = getRowCount() - 1; i > 0; i--) {
                collapseRow(i);
            }
        } else {
            // open a popup dialog which temporarily disables all other frames
            frame = new JDialog(this.parent, "Treasurelists", false);
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
            final JScrollPane scrollPane = new JScrollPane(this);
            scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

            final JPanel buttonPanel = buildButtonPanel();
            final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, buttonPanel);
            splitPane.setOneTouchExpandable(false);
            assert frame != null;
            splitPane.setDividerLocation(frame.getHeight() - buttonPanel.getMinimumSize().height - 4);

            splitPane.setDividerSize(4);
            splitPane.setResizeWeight(1.0);

            assert frame != null;
            frame.getContentPane().add(splitPane);

            expandPath(new TreePath(treasureTree.getRoot()));
        }

        okButton.setVisible(input != null);
        noneButton.setVisible(input != null);

        assert frame != null;
        frame.setSize(470, 550);
        assert frame != null;
        frame.setLocationRelativeTo(parent);

        if (input != null) {
            final String listName = input.getText().trim(); // name of pre-selected list
            final DefaultMutableTreeNode treasureNode = treasureTree.get(listName);
            if (treasureNode != null) {
                final DefaultMutableTreeNode[] node = new DefaultMutableTreeNode[2];
                node[0] = treasureTree.getRoot();
                node[1] = treasureNode;
                final TreePath treePath = new TreePath(node);
                expandPath(treePath);
                setSelectionPath(treePath);

                if (!hasBeenDisplayed) {
                    // If this is the first time, the frame has to be packed,
                    // otherwise no scrolling would be possible (see below).
                    assert frame != null;
                    frame.pack();
                    assert frame != null;
                    frame.setSize(470, 550);
                    setSelectionPath(treePath);
                }

                scrollRowToVisible(getRowCount() - 1);
                scrollPathToVisible(treePath);
            } else {
                scrollRowToVisible(0);
                setSelectionPath(null);
            }
        } else {
            scrollRowToVisible(0);
            setSelectionPath(null);
        }
        assert frame != null;
        frame.setVisible(true);
    }

    /**
     * Builds the button panel (bottom-line of the dialog window).
     * @return the panel containing all buttons
     */
    @NotNull
    private JPanel buildButtonPanel() {
        final JPanel buttonPanel = new JPanel(new BorderLayout());

        final Container leftSide = new JPanel();
        final Container rightSide = new JPanel();

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final String result = getSelectedTreasureList();
                if (result != null) {
                    selectValue(result);
                }
            }

        });
        rightSide.add(okButton);

        noneButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                // print "none" into the attribute dialog
                selectValue(NONE_SYM);
            }

        });
        rightSide.add(noneButton);

        final AbstractButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                assert frame != null;
                frame.setVisible(false);
            }

        });
        rightSide.add(cancelButton);

        final AbstractButton helpButton = new JButton("Help");
        helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                new Help(parent, "treasurelists.html").setVisible(true);
            }

        });
        leftSide.add(helpButton);
        final Component testButton = new JButton("Test");
        leftSide.add(testButton);
        testButton.setEnabled(false); // disable test button until implemented

        buttonPanel.add(leftSide, BorderLayout.WEST);
        buttonPanel.add(rightSide, BorderLayout.EAST);
        return buttonPanel;
    }

    /**
     * Selects a value and closes the dialog.
     * @param result the selected value
     */
    private void selectValue(@NotNull final String result) {
        if (input != null) {
            input.setText(" " + result);
        }
        assert frame != null;
        frame.setVisible(false);
    }

    /**
     * Returns the name of the currently selected treasurelist. If nothing is
     * selected, <code>null</code> is returned.
     * @return the name of the currently selected treasurelist or
     *         <code>null</code> if nothing is selected
     */
    @Nullable
    private String getSelectedTreasureList() {
        if (isSelectionEmpty()) {
            return null;
        }

        TreeNode node = (TreeNode) getSelectionPath().getLastPathComponent();

        if (node == treasureTree.getRoot()) {
            return null;
        }

        while (true) {
            final TreeNode parentNode = node.getParent();
            if (parentNode == treasureTree.getRoot()) {
                break;
            }
            node = parentNode;
        }

        // When a treasurelist inside a special sub folder (like
        // "God Intervention") is selected, also return null because those must
        // not be used on maps.
        final TreasureTreeNode treasureTreeNode = (TreasureTreeNode) node;
        if (treasureTreeNode.getTreasureObj() instanceof FolderTreasureObj) {
            ACTION_BUILDER.showMessageDialog(frame, "treasurelistForbidden", treasureTreeNode.getTreasureObj().getName());
            return null;
        }

        return treasureTreeNode.getTreasureObj().getName();
    }

}
