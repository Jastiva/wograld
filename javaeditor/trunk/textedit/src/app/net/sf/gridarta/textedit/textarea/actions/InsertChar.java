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

public class InsertChar implements ActionListener, InputHandler.NonRepeatable {

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final String str = e.getActionCommand();
        final int repeatCount = textArea.getInputHandler().getRepeatCount();

        if (textArea.isEditable()) {
            final StringBuilder buf = new StringBuilder();
            for (int i = 0; i < repeatCount; i++) {
                buf.append(str);
            }
            textArea.overwriteSetSelectedText(buf.toString());
        } else {
            textArea.getToolkit().beep();
        }
    }

}
