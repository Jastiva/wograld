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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import net.sf.gridarta.textedit.textarea.InputHandler;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

/**
 * Action listener for PASTE actions.
 * @author Andreas Vogl
 */
public class Paste implements ActionListener {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(Paste.class);

    /**
     * {@inheritDoc} Get content of the system clipboard and insert it at caret
     * position.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final StringBuilder buff = new StringBuilder();

        // set above string to the system clipboard
        final Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(textArea);
        try {
            // now read the system clipboard in plain-text format
            final DataFlavor dataFlavor = new DataFlavor("text/plain; charset=unicode");
            final BufferedReader bufferedReader = new BufferedReader(dataFlavor.getReaderForText(trans));
            try {
                // read everything into the buffer 'buff'
                boolean start = true;
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (start) {
                        start = false;
                    } else {
                        buff.append('\n');
                    }
                    buff.append(line);
                }
            } finally {
                bufferedReader.close();
            }

            final CharSequence selectedText = textArea.getSelectedText();
            if (selectedText != null && selectedText.length() > 0) {
                textArea.setSelectedText(buff.toString());
            } else {
                textArea.getDocument().insertString(textArea.getCaretPosition(), buff.toString(), null);
            }
        } catch (final ClassNotFoundException ex) {
            log.error("syntax.InputHandler: Paste action failed due to ClassNotFoundException");
        } catch (final UnsupportedFlavorException ex) {
            log.error("syntax.InputHandler: Paste action failed because clipboard data flavour is not supported.");
        } catch (final IOException ex) {
            log.error("syntax.InputHandler: Paste action failed due to IOException");
        } catch (final BadLocationException ex) {
            log.error("syntax.InputHandler: Paste action failed due to BadLocationException");
        } catch (final NullPointerException ex) {
            // this happens when clipboard is empty, it's not an error
        }
    }

}
