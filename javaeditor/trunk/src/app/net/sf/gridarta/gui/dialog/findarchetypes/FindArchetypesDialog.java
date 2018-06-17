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

package net.sf.gridarta.gui.dialog.findarchetypes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.panel.archetypechooser.ArchetypeChooserControl;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.utils.SwingUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog used to find archetypes.
 * @author Andreas Kirschbaum
 */
public class FindArchetypesDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Do not perform an auto-search until the input name reaches this length.
     */
    private static final int MINIMUM_AUTO_SEARCH_LENGTH = 3;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The archetype set to search.
     */
    @NotNull
    private final ArchetypeChooserControl<G, A, R> archetypeChooserControl;

    /**
     * The insertion object chooser to use when selecting search results.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The name input field.
     */
    @NotNull
    private final JTextComponent nameField = new JTextField();

    /**
     * The table model for the search results table {@link #resultTable}.
     */
    @NotNull
    private final TableModel<G, A, R> resultTableModel;

    /**
     * The search results table.
     */
    @NotNull
    private final JTable resultTable;

    /**
     * The scroll pane which contains the search results table {@link
     * #resultTable}.
     */
    @NotNull
    private final JScrollPane scrollPane;

    /**
     * The complete dialog.
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The previously processed search string.
     */
    @NotNull
    private String previousSearch = "";

    /**
     * Creates a new instance.
     * @param parent the parent component
     * @param archetypeChooserControl the archetype chooser control to to
     * search
     * @param objectChooser the insertion object chooser to use when selecting
     * search results
     * @param archetypeTypeSet the instance for looking up archetype types
     */
    public FindArchetypesDialog(@NotNull final Component parent, @NotNull final ArchetypeChooserControl<G, A, R> archetypeChooserControl, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        this.archetypeChooserControl = archetypeChooserControl;
        this.objectChooser = objectChooser;

        final JPanel panel = new JPanel(new GridBagLayout());

        SwingUtils.addAction(nameField, ACTION_BUILDER.createAction(false, "findArchetypesScrollUp", this));
        SwingUtils.addAction(nameField, ACTION_BUILDER.createAction(false, "findArchetypesScrollDown", this));

        final DocumentListener documentListener = new DocumentListener() {

            @Override
            public void changedUpdate(@NotNull final DocumentEvent e) {
                doSearch(false);
            }

            @Override
            public void insertUpdate(@NotNull final DocumentEvent e) {
                doSearch(false);
            }

            @Override
            public void removeUpdate(@NotNull final DocumentEvent e) {
                doSearch(false);
            }

        };
        nameField.getDocument().addDocumentListener(documentListener);

        resultTableModel = new TableModel<G, A, R>(archetypeTypeSet);
        resultTable = new JTable(resultTableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setRowSelectionAllowed(true);
        resultTable.setColumnSelectionAllowed(false);
        final ListSelectionListener listSelectionListener = new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                highlightSelectedEntry();
            }

        };
        resultTable.getSelectionModel().addListSelectionListener(listSelectionListener);
        //noinspection RefusedBequest
        final MouseListener mouseListener = new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                highlightSelectedEntry();
                if (e.getClickCount() > 1) {
                    findArchetypesClose();
                }
            }

        };
        resultTable.addMouseListener(mouseListener);
        scrollPane = new JScrollPane(resultTable);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "findArchetypesName"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        final JButton searchButton = new JButton(ACTION_BUILDER.createAction(false, "findArchetypesSearch", this));
        final JButton closeButton = new JButton(ACTION_BUILDER.createAction(false, "findArchetypesClose", this));
        final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new JButton[] { searchButton, closeButton, }, nameField);
        dialog = optionPane.createDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "findArchetypesTitle"));
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dialog.setModal(false);
        dialog.setResizable(true);
        dialog.setSize(500, 250);
        dialog.setLocationRelativeTo(parent);
    }

    /**
     * Shows the dialog.
     */
    public void show() {
        scrollPane.getVerticalScrollBar().setValue(0);

        if (dialog.isShowing()) {
            dialog.toFront();
        } else {
            nameField.setText("");
            resultTable.clearSelection();
            try {
                resultTableModel.clear();
            } finally {
                resultTableModel.finishUpdate();
            }
            dialog.setVisible(true);
        }

        nameField.selectAll();
        nameField.requestFocusInWindow();
    }

    /**
     * Action method for "search" button.
     */
    @ActionMethod
    public void findArchetypesSearch() {
        previousSearch = "";
        doSearch(true);
        nameField.selectAll();
    }

    /**
     * Searches for the current name. Does nothing if the current name matches
     * {@link #previousSearch}.
     * @param force if set, perform the search even if the current name is too
     * short
     */
    private void doSearch(final boolean force) {
        @Nullable final R selectedArchetype;
        try {
            resultTableModel.clear();
            final String name = nameField.getText();
            if (name.length() <= 0 || name.equals(previousSearch)) {
                return;
            }
            if (!force && name.length() < MINIMUM_AUTO_SEARCH_LENGTH) {
                return;
            }
            previousSearch = name;

            final CharSequence lowerCaseName = name.toLowerCase();
            R exactArchetypeNameMatch = null;
            R exactDisplayNameMatch = null;
            for (final R archetype : archetypeChooserControl) {
                if (!archetype.isTail()) {
                    final String archetypeName = archetype.getArchetypeName();
                    final String objName = archetype.getObjName();
                    if (archetypeName.toLowerCase().contains(lowerCaseName) || objName.toLowerCase().contains(lowerCaseName)) {
                        if (exactArchetypeNameMatch != null) {
                            // ignore
                        } else if (archetypeName.equals(name)) {
                            exactArchetypeNameMatch = archetype;
                        } else if (exactDisplayNameMatch != null) {
                            // ignore
                        } else if (objName.equals(name)) {
                            exactDisplayNameMatch = archetype;
                        }
                        resultTableModel.add(archetype);
                    }
                }
            }

            if (exactArchetypeNameMatch != null) {
                selectedArchetype = exactArchetypeNameMatch;
            } else if (exactDisplayNameMatch != null) {
                selectedArchetype = exactDisplayNameMatch;
            } else {
                selectedArchetype = null;
            }

            resultTableModel.sortTable();
        } finally {
            resultTableModel.finishUpdate();
        }

        final int index = resultTableModel.findTableIndex(selectedArchetype);
        if (index != -1) {
            selectRow(index);
        }
    }

    /**
     * Action method for "close" button.
     */
    public void findArchetypesClose() {
        dialog.setVisible(false);
    }

    /**
     * Highlights the selected row from {@link #resultTable} in the insertion
     * object chooser.
     */
    private void highlightSelectedEntry() {
        final int index = resultTable.getSelectedRow();
        if (index == -1) {
            return;
        }

        final R archetype = resultTableModel.get(index);
        objectChooser.moveArchetypeChooserToFront();
        archetypeChooserControl.selectArchetype(archetype);
    }

    /**
     * Action method to scroll up one line in the result table.
     */
    @ActionMethod
    public void findArchetypesScrollUp() {
        final int index = resultTable.getSelectedRow();
        if (index > 0) {
            selectRow(index - 1);
        }
    }

    /**
     * Action method to scroll down one line in the result table.
     */
    @ActionMethod
    public void findArchetypesScrollDown() {
        final int index = resultTable.getSelectedRow();
        if (index != -1 && index + 1 < resultTableModel.getRowCount()) {
            selectRow(index + 1);
        }
    }

    /**
     * Selects one row in the result table.
     * @param index the row index to select
     */
    private void selectRow(final int index) {
        resultTable.setRowSelectionInterval(index, index);
        resultTable.scrollRectToVisible(new Rectangle(resultTable.getCellRect(index, 0, true)));
    }

}
