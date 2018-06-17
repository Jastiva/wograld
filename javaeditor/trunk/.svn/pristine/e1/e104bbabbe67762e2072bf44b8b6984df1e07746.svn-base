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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.gridarta.textedit.textarea.InputHandler;

/**
 * Action listener for COPY actions.
 * @author Andreas Vogl
 */
public class Copy implements ActionListener {

    /**
     * Copy current selection into the system clipboard. {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        // get the selected string
        final String text = InputHandler.getTextArea(e).getSelectedText();
        if (text != null) {
            final StringSelection selection = new StringSelection(text);
            // set above string to the system clipboard
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        }
    }

}
