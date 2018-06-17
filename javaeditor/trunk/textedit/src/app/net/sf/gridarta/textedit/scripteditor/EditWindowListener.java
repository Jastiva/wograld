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

package net.sf.gridarta.textedit.scripteditor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Listener for close box on the window.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class EditWindowListener extends WindowAdapter {

    private final ScriptEditControl control; // controller

    /**
     * Constructor.
     * @param control ScriptEditControl
     */
    public EditWindowListener(final ScriptEditControl control) {
        this.control = control;
    }

    /**
     * Window close box has been clicked.
     * @param e WindowEvent
     */
    @Override
    public void windowClosing(final WindowEvent e) {
        control.closeAllTabs();
    }

    @Override
    public void windowClosed(final WindowEvent e) {
        control.closeAllTabs(); // (just make sure tabs are removed...)
    }

}
