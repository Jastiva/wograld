/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.textedit.textarea.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import net.sf.gridarta.textedit.scripteditor.Actions;
import net.sf.gridarta.textedit.textarea.InputHandler;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Action listener for "find".
 * @author Andreas Kirschbaum
 */
public class Find implements ActionListener {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The text that was previously selected.
     */
    @NotNull
    private String textToFind = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        Actions.FIND_AGAIN.setType(FindType.FIND);
        final JEditTextArea textArea = InputHandler.getTextArea(e);
        final String selectedText = textArea.getSelectedText();
        final String text = (String) JOptionPane.showInputDialog(textArea, ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEdit.find.text"), ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEdit.find.title"), JOptionPane.PLAIN_MESSAGE, null, null, selectedText != null ? selectedText : textToFind);
        if (text != null && text.length() > 0) {
            textToFind = text;
            find(textArea);
        }
    }

    /**
     * Find the next occurrence of {@link #textToFind}.
     * @param textArea The text area to search.
     */
    public void find(@NotNull final JEditTextArea textArea) {
        if (textToFind.length() == 0) {
            return;
        }

        final int startPos = textArea.getCaretPosition();
        final String text = textArea.getText();
        final int foundIndex = text.indexOf(textToFind, startPos + 1);
        if (foundIndex == -1) {
            JOptionPane.showMessageDialog(textArea, ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEdit.find.notFound"));
            return;
        }

        textArea.select(foundIndex, foundIndex + textToFind.length());
    }

}
