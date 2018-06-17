/*
 * InputHandler.java - Manages key bindings and executes actions
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.BadLocationException;
import net.sf.gridarta.textedit.textarea.InputHandler;
import net.sf.gridarta.textedit.textarea.JEditTextArea;

public class Delete implements ActionListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);

        if (!textArea.isEditable()) {
            textArea.getToolkit().beep();
            return;
        }

        if (textArea.getSelectionStart() == textArea.getSelectionEnd()) {
            final int caret = textArea.getCaretPosition();
            if (caret == textArea.getDocumentLength()) {
                textArea.getToolkit().beep();
                return;
            }

            try {
                textArea.getDocument().remove(caret, 1);
            } catch (final BadLocationException bl) {
                bl.printStackTrace();
            }
        } else {
            textArea.setSelectedText("");
        }
    }

}
