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
import net.sf.gridarta.textedit.textarea.TextUtilities;

public class DeleteWord implements ActionListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final int start = textArea.getSelectionStart();
        if (start != textArea.getSelectionEnd()) {
            textArea.setSelectedText("");
        }

        final int line = textArea.getCaretLine();
        final int lineStart = textArea.getLineStartOffset(line);
        int caret = start - lineStart;

        final CharSequence lineText = textArea.getLineText(textArea.getCaretLine());

        if (caret == lineText.length()) {
            if (lineStart + caret == textArea.getDocumentLength()) {
                textArea.getToolkit().beep();
                return;
            }
            caret++;
        } else {
            final String noWordSep = (String) textArea.getDocument().getProperty("noWordSep");
            caret = TextUtilities.findWordEnd(lineText, caret, noWordSep);
        }

        try {
            textArea.getDocument().remove(start, caret + lineStart - start);
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
        }
    }

}
