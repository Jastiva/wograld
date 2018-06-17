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
import net.sf.gridarta.textedit.scripteditor.CFPythonPopup;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import net.sf.gridarta.textedit.textarea.InputHandler;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import org.jetbrains.annotations.NotNull;

/**
 * When a '.' character is typed, in certain cases a selection menu pops up to
 * let the user choose a python function.
 * @author Andreas Vogl
 */
public class FunctionMenu implements ActionListener {

    // popup menu for CFPython function

    private final CFPythonPopup cfPythonPopup;

    /**
     * Creates a new instance.
     * @param scriptEditControl the script edit control to forward to
     */
    public FunctionMenu(@NotNull final ScriptEditControl scriptEditControl) {
        cfPythonPopup = new CFPythonPopup(scriptEditControl);
    }

    /**
     * {@inheritDoc} Get content of the system clipboard and insert it at caret
     * position.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final int caretPos = textArea.getCaretPosition(); // caret position

        try {
            // insert the '.' character
            final CharSequence selectedText = textArea.getSelectedText();
            if (selectedText != null && selectedText.length() > 0) {
                textArea.setSelectedText(".");
            } else {
                textArea.getDocument().insertString(caretPos, ".", null);
            }

            if (caretPos >= 7 && cfPythonPopup.isInitialized()) {
                final String fileName = textArea.getDocument().getText(textArea.getCaretPosition() - 9, 8);
                if (fileName.equalsIgnoreCase("cfpython")) {
                    final int line = textArea.getCaretLine();
                    final int offset = textArea.getCaretPosition() - textArea.getLineStartOffset(line);

                    cfPythonPopup.setCaretPosition(caretPos + 1);
                    cfPythonPopup.getMenu().show(textArea, textArea.offsetToX2(line, offset), 30 + textArea.lineToY(line));
                }
            }
        } catch (final BadLocationException ignored) {
        }
    }

}
