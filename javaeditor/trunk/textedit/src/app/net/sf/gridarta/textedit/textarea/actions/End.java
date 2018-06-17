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

public class End implements ActionListener {

    private final boolean select;

    public End(final boolean select) {
        this.select = select;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);

        int caret = textArea.getCaretPosition();

        final int lastOfLine = textArea.getLineEndOffset(textArea.getCaretLine()) - 1;
        int lastVisibleLine = textArea.getFirstLine() + textArea.getVisibleLines();
        if (lastVisibleLine >= textArea.getLineCount()) {
            lastVisibleLine = Math.min(textArea.getLineCount() - 1, lastVisibleLine);
        } else {
            lastVisibleLine -= textArea.getElectricScroll() + 1;
        }

        final int lastVisible = textArea.getLineEndOffset(lastVisibleLine) - 1;
        final int lastDocument = textArea.getDocumentLength();

        if (caret == lastDocument) {
            textArea.getToolkit().beep();
            return;
        } else if (!Boolean.TRUE.equals(textArea.getClientProperty(InputActions.SMART_HOME_END_PROPERTY))) {
            caret = lastOfLine;
        } else if (caret == lastVisible) {
            caret = lastDocument;
        } else if (caret == lastOfLine) {
            caret = lastVisible;
        } else {
            caret = lastOfLine;
        }

        if (select) {
            textArea.select(textArea.getMarkPosition(), caret);
        } else {
            textArea.setCaretPosition(caret);
        }
    }

}
