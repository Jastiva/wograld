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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog that asks for a {@link KeyStroke}.
 * @author Andreas Kirschbaum
 */
public class KeyStrokeDialog extends JOptionPane {

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
     * The {@link ShortcutsManager} for checking conflicts.
     */
    @NotNull
    private final ShortcutsManager shortcutsManager;

    /**
     * The {@link Action} being redefined. This action will not cause
     * conflicts.
     */
    @NotNull
    private final Action action;

    /**
     * The {@link JButton} for ok.
     * @serial
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "keyStrokeOkay", this));

    /**
     * The {@link JButton} for cancel.
     * @serial
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "keyStrokeCancel", this));

    /**
     * The {@link JDialog} instance.
     * @serial
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The key stroke input field.
     * @serial
     */
    @NotNull
    private final KeyStrokeField keyStroke;

    /**
     * The text area showing conflicts between the new key stroke and assigned
     * key strokes.
     * @serial
     */
    @NotNull
    private final JTextArea conflictsTextArea = new JTextArea();

    /**
     * Creates a new instance.
     * @param parentComponent the parent component for the dialog
     * @param shortcutsManager the shortcuts manager for checking conflicts
     * @param action the action being redefined; this action will not cause
     * conflicts
     */
    public KeyStrokeDialog(@NotNull final Component parentComponent, @NotNull final ShortcutsManager shortcutsManager, @NotNull final Action action) {
        this.shortcutsManager = shortcutsManager;
        this.action = action;
        okButton.setDefaultCapable(true);
        setOptions(new Object[] { okButton, cancelButton, });

        keyStroke = new KeyStrokeField(ActionUtils.getShortcut(action));
        final KeyStrokeFieldListener keyStrokeFieldListener = new KeyStrokeFieldListener() {

            @Override
            public void keyStrokeChanged(@NotNull final KeyStroke keyStroke) {
                updateKeyStroke();
            }

        };
        keyStroke.addKeyStrokeListener(keyStrokeFieldListener);

        conflictsTextArea.setEditable(false);
        conflictsTextArea.setLineWrap(true);
        conflictsTextArea.setWrapStyleWord(true);
        conflictsTextArea.setFocusable(false);
        conflictsTextArea.setRows(3);

        setMessage(createPanel());

        dialog = createDialog(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "keyStroke.title"));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setModal(true);
        dialog.pack();
    }

    /**
     * Opens the dialog. Returns when the dialog has been dismissed.
     * @param parentComponent the parent component for the dialog
     * @return whether "ok" was selected
     */
    public boolean showDialog(@NotNull final Component parentComponent) {
        dialog.setLocationRelativeTo(parentComponent);
        updateKeyStroke();
        keyStroke.requestFocusInWindow();
        setInitialValue(keyStroke);
        dialog.setVisible(true);
        return getValue() == okButton;
    }

    /**
     * Creates the GUI.
     * @return the panel containing the GUI
     */
    @NotNull
    private JPanel createPanel() {
        final GridBagConstraints gbc = new GridBagConstraints();

        final JComponent keyStrokePanel = new JPanel(new GridBagLayout());
        keyStrokePanel.setBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "keyStroke.borderKeyStroke")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        keyStrokePanel.add(keyStroke, gbc);

        final JComponent conflictsPanel = new JPanel(new GridBagLayout());
        conflictsPanel.setBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "keyStroke.borderConflicts")));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        conflictsPanel.add(conflictsTextArea, gbc);

        final JPanel panel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panel.add(keyStrokePanel, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(conflictsPanel, gbc);

        panel.setBorder(GUIConstants.DIALOG_BORDER);
        return panel;
    }

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void keyStrokeOkay() {
        setValue(okButton);
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void keyStrokeCancel() {
        setValue(cancelButton);
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
     * Updates the information shown for the selected action.
     */
    private void updateKeyStroke() {
        final KeyStroke newKeyStroke = getKeyStroke();
        Action conflictAction = null;
        if (newKeyStroke != null) {
            for (final Action tmp : shortcutsManager) {
                if (tmp != action) {
                    final KeyStroke tmpKeyStroke = ActionUtils.getShortcut(tmp);
                    if (newKeyStroke.equals(tmpKeyStroke)) {
                        conflictAction = tmp;
                        break;
                    }
                }
            }
        }
        if (conflictAction == null) {
            conflictsTextArea.setText("");
            okButton.setEnabled(true);
        } else {
            conflictsTextArea.setText(ACTION_BUILDER.format("keyStroke.borderConflictAssigned", ActionUtils.getActionName(conflictAction), ActionUtils.getActionCategory(conflictAction)));
            okButton.setEnabled(false);
        }
    }

    /**
     * Returns the currently shown key stroke.
     * @return the key stroke or <code>null</code>
     */
    @Nullable
    @SuppressWarnings("NullableProblems")
    public KeyStroke getKeyStroke() {
        return keyStroke.getKeyStroke();
    }

}
