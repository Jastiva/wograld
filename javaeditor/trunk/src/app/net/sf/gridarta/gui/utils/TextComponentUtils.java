/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for {@link JTextComponent} related functions.
 * @author Andreas Kirschbaum
 */
public class TextComponentUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private TextComponentUtils() {
    }

    /**
     * Selects all text of a {@link JTextComponent} when the component gains the
     * focus.
     * @param textComponent the text component
     */
    public static void setAutoSelectOnFocus(@NotNull final JTextComponent textComponent) {
        final FocusListener focusListener = new FocusListener() {

            @Override
            public void focusGained(@NotNull final FocusEvent e) {
                textComponent.selectAll();
            }

            @Override
            public void focusLost(@NotNull final FocusEvent e) {
                // ignore
            }

        };
        textComponent.addFocusListener(focusListener);
    }

    /**
     * Transfers the focus to another component when ENTER is pressed.
     * @param textField the text field to track
     * @param nextComponent the component to transfer the focus to
     */
    public static void setActionNextFocus(@NotNull final JTextField textField, @NotNull final Component nextComponent) {
        final ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(@NotNull final ActionEvent e) {
                nextComponent.requestFocusInWindow();
            }

        };
        textField.addActionListener(actionListener);
    }

}
