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
import net.sf.gridarta.textedit.textarea.InputHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Action listener for "find again".
 * @author Andreas Kirschbaum
 */
public class FindAgain implements ActionListener {

    /**
     * The {@link Find} instance to invoke {@link Find#find(net.sf.gridarta.textedit.textarea.JEditTextArea)}.
     */
    @NotNull
    private final Find find;

    /**
     * The {@link Replace} instance to invoke {@link Replace#replace(net.sf.gridarta.textedit.textarea.JEditTextArea)}.
     */
    @NotNull
    private final Replace replace;

    /**
     * The action to perform.
     */
    private FindType type = FindType.NONE;

    /**
     * Create a new instance.
     * @param find The <code>Find</code> instance to forward to.
     * @param replace The <code>Replace</code> instance to forward to.
     */
    public FindAgain(final Find find, final Replace replace) {
        this.find = find;
        this.replace = replace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        switch (type) {
        case NONE:
            break;

        case FIND:
            find.find(InputHandler.getTextArea(e));
            break;

        case REPLACE:
            replace.replace(InputHandler.getTextArea(e));
            break;
        }
    }

    /**
     * Set the operation to perform.
     * @param type The operation to perform.
     */
    public void setType(final FindType type) {
        this.type = type;
    }

}
