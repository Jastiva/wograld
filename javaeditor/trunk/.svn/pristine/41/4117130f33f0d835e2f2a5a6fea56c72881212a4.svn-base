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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * A dialog that displays one bookmark and allows to edit the values.
 * @author Andreas Kirschbaum
 */
public class EditBookmarkDialog extends JOptionPane {

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
     * The dialog.
     * @serial
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The text input field for the description.
     * @serial
     */
    @NotNull
    private final JTextComponent descriptionField = new JTextField();

    /**
     * The {@link JButton} for ok.
     * @serial
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "editBookmarkOkay", this));

    /**
     * The {@link JButton} for cancel.
     * @serial
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "editBookmarkCancel", this));

    /**
     * Creates a new instance.
     * @param parentComponent the parent component for the dialog
     * @param defaultDescription the default value for the description field
     */
    public EditBookmarkDialog(@NotNull final Component parentComponent, @NotNull final String defaultDescription) {
        okButton.setDefaultCapable(true);
        setOptions(new Object[] { okButton, cancelButton });

        setMessage(createPanel());

        TextComponentUtils.setAutoSelectOnFocus(descriptionField);
        descriptionField.setText(defaultDescription);

        final DocumentListener documentListener = new DocumentListener() {

            @Override
            public void insertUpdate(@NotNull final DocumentEvent e) {
                updateOkButton();
            }

            @Override
            public void removeUpdate(@NotNull final DocumentEvent e) {
                updateOkButton();
            }

            @Override
            public void changedUpdate(@NotNull final DocumentEvent e) {
                updateOkButton();
            }

        };
        descriptionField.getDocument().addDocumentListener(documentListener);
        updateOkButton();

        dialog = createDialog(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "editBookmark.title"));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
    }

    /**
     * Opens the dialog. Returns when the dialog has been dismissed.
     * @return whether "ok" was selected
     */
    public boolean showDialog() {
        dialog.setVisible(true);
        return getValue() == okButton;
    }

    /**
     * Creates the GUI.
     * @return the panel containing the GUI
     */
    @NotNull
    private JPanel createPanel() {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.setBorder(GUIConstants.DIALOG_BORDER);

        mainPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "editBookmark.description"));
        mainPanel.add(Box.createVerticalStrut(5));

        mainPanel.add(descriptionField);
        mainPanel.add(Box.createVerticalStrut(5));

        return mainPanel;
    }

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void editBookmarkOkay() {
        if (isOkEnabled()) {
            setValue(okButton);
        }
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void editBookmarkCancel() {
        setValue(cancelButton);
    }

    /**
     * Returns the description.
     * @return the description
     */
    @NotNull
    public String getDescription() {
        return descriptionField.getText().trim();
    }

    /**
     * Sets the enabled state of {@link #okButton} depending on the contents of
     * {@link #descriptionField}.
     */
    private void updateOkButton() {
        okButton.setEnabled(isOkEnabled());
    }

    /**
     * Returns whether the "ok" button is enabled.
     * @return whether the "ok" button is enabled
     */
    private boolean isOkEnabled() {
        return !getDescription().isEmpty();
    }

}
