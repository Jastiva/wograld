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

public class NextLine implements ActionListener {

    private final boolean select;

    public NextLine(final boolean select) {
        this.select = select;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final int caret1 = textArea.getCaretPosition();
        final int line = textArea.getCaretLine();

        if (line == textArea.getLineCount() - 1) {
            textArea.getToolkit().beep();
            return;
        }

        int magic = textArea.getMagicCaretPosition();
        if (magic == -1) {
            magic = textArea.offsetToX(line, caret1 - textArea.getLineStartOffset(line));
        }

        final int caret2 = textArea.getLineStartOffset(line + 1) + textArea.xToOffset(line + 1, magic);
        if (select) {
            textArea.select(textArea.getMarkPosition(), caret2);
        } else {
            textArea.setCaretPosition(caret2);
        }
        textArea.setMagicCaretPosition(magic);
    }

}
