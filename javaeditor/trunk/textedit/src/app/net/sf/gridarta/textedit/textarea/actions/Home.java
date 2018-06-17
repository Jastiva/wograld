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

public class Home implements ActionListener {

    private final boolean select;

    public Home(final boolean select) {
        this.select = select;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);

        int caret = textArea.getCaretPosition();

        final int firstLine = textArea.getFirstLine();

        final int firstOfLine = textArea.getLineStartOffset(textArea.getCaretLine());
        final int firstVisibleLine = firstLine == 0 ? 0 : firstLine + textArea.getElectricScroll();
        final int firstVisible = textArea.getLineStartOffset(firstVisibleLine);

        if (caret == 0) {
            textArea.getToolkit().beep();
            return;
        } else if (!Boolean.TRUE.equals(textArea.getClientProperty(InputActions.SMART_HOME_END_PROPERTY))) {
            caret = firstOfLine;
        } else if (caret == firstVisible) {
            caret = 0;
        } else if (caret == firstOfLine) {
            caret = firstVisible;
        } else {
            caret = firstOfLine;
        }

        if (select) {
            textArea.select(textArea.getMarkPosition(), caret);
        } else {
            textArea.setCaretPosition(caret);
        }
    }

}
