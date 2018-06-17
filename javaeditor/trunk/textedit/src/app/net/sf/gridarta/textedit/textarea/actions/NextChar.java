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
import net.sf.gridarta.textedit.textarea.InputHandler;
import net.sf.gridarta.textedit.textarea.JEditTextArea;

public class NextChar implements ActionListener {

    private final boolean select;

    public NextChar(final boolean select) {
        this.select = select;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final int caret = textArea.getCaretPosition();
        if (caret == textArea.getDocumentLength()) {
            textArea.getToolkit().beep();
            return;
        }

        if (select) {
            textArea.select(textArea.getMarkPosition(), caret + 1);
        } else {
            textArea.setCaretPosition(caret + 1);
        }
    }

}
