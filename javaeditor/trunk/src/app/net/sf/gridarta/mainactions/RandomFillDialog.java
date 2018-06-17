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

package net.sf.gridarta.mainactions;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Displays a dialog asking for parameters for the "random fill" function.
 * @author Andreas Kirschbaum
 */
public class RandomFillDialog {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link JOptionPane} instance used to create dialogs.
     */
    @NotNull
    private final JOptionPane optionPane = new JOptionPane();

    /**
     * The "OK" button.
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "randomFillOkay", this));

    /**
     * The "Cancel" button.
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "randomFillCancel", this));

    /**
     * The text field for specifying the fill density.
     */
    @NotNull
    private final JTextComponent fillDensityTextField = new JTextField(16);

    /**
     * The {@link JDialog} instance or <code>null</code> if not yet created.
     */
    @Nullable
    private JDialog dialog;

    /**
     * Whether adjacent squares are checked.
     */
    private boolean skipAdjacentSquares;

    /**
     * Creates a new instance.
     */
    public RandomFillDialog() {
        okButton.setDefaultCapable(true);
        optionPane.setOptions(new Object[] { okButton, cancelButton });
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(GUIConstants.DIALOG_BORDER);
        panel.add(new JLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "randomFillDensity.text")));
        panel.add(fillDensityTextField);
        panel.add(Box.createVerticalStrut(5));
        final ToggleAction skipAdjacentSquaresAction = (ToggleAction) ACTION_BUILDER.createToggle(false, "randomFillSkipAdjacentSquares", this);
        panel.add(skipAdjacentSquaresAction.createCheckBox());
        optionPane.setMessage(panel);

        fillDensityTextField.setText(ActionBuilderUtils.getString(ACTION_BUILDER, "randomFillDensity.default"));
        TextComponentUtils.setAutoSelectOnFocus(fillDensityTextField);
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

        fillDensityTextField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * The {@link WindowListener} attached to {@link #dialog} to call {@link
     * JOptionPane#selectInitialValue()} after the dialog has been shown.
     */
    @NotNull
    private final WindowListener windowListener = new WindowListener() {

        @Override
        public void windowOpened(final WindowEvent e) {
            // ignore
        }

        @Override
        public void windowClosing(final WindowEvent e) {
            // ignore
        }

        @Override
        public void windowClosed(final WindowEvent e) {
            // ignore
        }

        @Override
        public void windowIconified(final WindowEvent e) {
            // ignore
        }

        @Override
        public void windowDeiconified(final WindowEvent e) {
            // ignore
        }

        @Override
        public void windowActivated(final WindowEvent e) {
            fillDensityTextField.requestFocusInWindow();
            assert dialog != null;
            dialog.removeWindowListener(windowListener);
        }

        @Override
        public void windowDeactivated(final WindowEvent e) {
            // ignore
        }

    };

    /**
     * Updates the enabled state of the "OK" button depending on the dialog's
     * contents.
     */
    private void updateOkButton() {
        okButton.setEnabled(isOkButtonEnabled());
    }

    /**
     * Displays the random fill dialog.
     * @param parent the parent component for the dialog
     * @return whether the dialog was closed with "OK"
     */
    public boolean showRandomFillDialog(@NotNull final Component parent) {
        final JDialog tmpDialog;
        if (dialog == null) {
            tmpDialog = optionPane.createDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "randomFillTitle"));
            dialog = tmpDialog;
            tmpDialog.getRootPane().setDefaultButton(okButton);
            optionPane.selectInitialValue();
            tmpDialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        } else {
            tmpDialog = dialog;
        }
        okButton.setEnabled(isOkButtonEnabled());
        tmpDialog.addWindowListener(windowListener);
        tmpDialog.setVisible(true);
        tmpDialog.removeWindowListener(windowListener);
        return optionPane.getValue() == okButton;
    }

    /**
     * Action method to close the dialog with "OK".
     */
    @ActionMethod
    public void randomFillOkay() {
        if (isOkButtonEnabled()) {
            optionPane.setValue(okButton);
        }
    }

    /**
     * Action method for "skip adjacent squares" action.
     * @return whether the checkbox is checked
     */
    @ActionMethod
    public boolean isRandomFillSkipAdjacentSquares() {
        return skipAdjacentSquares;
    }

    /**
     * Action method for "skip adjacent squares" action.
     * @param skipAdjacentSquares whether the checkbox is checked
     */
    @ActionMethod
    public void setRandomFillSkipAdjacentSquares(final boolean skipAdjacentSquares) {
        this.skipAdjacentSquares = skipAdjacentSquares;
    }

    /**
     * Action method to close the dialog with "Cancel".
     */
    @ActionMethod
    public void randomFillCancel() {
        optionPane.setValue(cancelButton);
    }

    /**
     * Returns whether the "OK" button is enabled.
     * @return whether the "OK" button is enabled
     */
    private boolean isOkButtonEnabled() {
        final int fillDensity = getFillDensity();
        return 0 < fillDensity && fillDensity <= 100;
    }

    /**
     * Returns the fill density.
     * @return the fill density
     */
    public int getFillDensity() {
        return NumberUtils.parseInt(fillDensityTextField.getText());
    }

}
