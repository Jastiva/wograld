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

package net.sf.gridarta.textedit.textarea.actions;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.textedit.scripteditor.Actions;
import net.sf.gridarta.textedit.textarea.InputHandler;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import net.sf.gridarta.textedit.textarea.SyntaxDocument;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Action listener for "replace".
 * @author Andreas Kirschbaum
 */
public class Replace implements ActionListener {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The text that was previously selected to find.
     */
    @NotNull
    private String textToFind = "";

    /**
     * The text that was previously selected as replacement.
     */
    @NotNull
    private String textToReplace = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        Actions.FIND_AGAIN.setType(FindType.REPLACE);
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final ReplaceDialog pane = new ReplaceDialog(textArea);
        pane.dialog = pane.createDialog(textArea, ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEdit.replace.title"));
        pane.dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pane.dialog.getRootPane().setDefaultButton(pane.replaceButton);
        pane.dialog.setModal(false);
        pane.dialog.setVisible(true);
        pane.findTextField.selectAll();
        pane.replaceTextField.selectAll();
    }

    /**
     * Replace the next occurrence of {@link #textToFind} with {@link
     * #textToReplace}.
     * @param textArea the text area to affect
     * @return <code>true</code> if a match was found, or <code>false</code> if
     *         no match was found
     */
    public boolean replace(final JEditTextArea textArea) {
        final int startPos = textArea.getCaretPosition();
        final String text = textArea.getText();
        final int foundIndex = text.indexOf(textToFind, startPos + 1);
        if (foundIndex == -1) {
            JOptionPane.showMessageDialog(textArea, ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEdit.find.notFound"));
            return false;
        }

        SyntaxDocument.beginCompoundEdit();
        try {
            textArea.select(foundIndex, foundIndex + textToFind.length());
            textArea.setSelectedText(textToReplace);
            textArea.select(foundIndex, foundIndex + textToReplace.length());
        } finally {
            SyntaxDocument.endCompoundEdit();
        }

        return true;
    }

    /**
     * The replace dialog implementation.
     */
    public class ReplaceDialog extends JOptionPane {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * JButton for ok.
         * @serial
         */
        private final JButton replaceButton = new JButton(ACTION_BUILDER.createAction(false, "replaceButton", this));

        /**
         * JButton for ok.
         * @serial
         */
        private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "cancelButton", this));

        /**
         * Textfield for the text to find.
         * @serial
         */
        private final JTextComponent findTextField = new JTextField(32);

        /**
         * Textfield for the text to replace with.
         * @serial
         */
        private final JTextComponent replaceTextField = new JTextField(32);

        /**
         * The dialog.
         * @serial
         */
        private JDialog dialog;

        /**
         * The text area to replace in.
         * @serial
         */
        private final JEditTextArea textArea;

        /**
         * Create a new instance.
         * @param textArea The text area to replace in.
         */
        public ReplaceDialog(final JEditTextArea textArea) {
            this.textArea = textArea;
            replaceButton.setDefaultCapable(true);
            setOptions(new Object[] { replaceButton, cancelButton, });
            setMessage(createPanel());
        }

        /**
         * Action method for okay.
         */
        @ActionMethod
        public void replaceButton() {
            textToFind = findTextField.getText();
            textToReplace = replaceTextField.getText();
            if (replace(textArea)) {
                setValue(replaceButton);
                dialog.dispose();
            }
        }

        /**
         * Action method for cancel.
         */
        @ActionMethod
        public void cancelButton() {
            setValue(cancelButton);
            dialog.dispose();
        }

        /**
         * Creates the text field panel.
         * @return the text field panel
         */
        private JPanel createPanel() {
            final JPanel replacePanel = new JPanel();
            replacePanel.setLayout(new BoxLayout(replacePanel, BoxLayout.Y_AXIS));

            // find text field
            final Container findTextPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            findTextPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "scriptEdit.replace.find"));
            findTextPanel.add(findTextField);
            final String selectedText = textArea.getSelectedText();
            findTextField.setText(selectedText != null ? selectedText : textToFind);
            replacePanel.add(findTextPanel);
            replacePanel.add(Box.createVerticalStrut(5));

            // replacement text field
            final Container replaceTextPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            replaceTextPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "scriptEdit.replace.replace"));
            replaceTextPanel.add(replaceTextField);
            replaceTextField.setText(textToReplace);
            replacePanel.add(replaceTextPanel);
            replacePanel.add(Box.createVerticalStrut(5));

            return replacePanel;
        }

    }

}
