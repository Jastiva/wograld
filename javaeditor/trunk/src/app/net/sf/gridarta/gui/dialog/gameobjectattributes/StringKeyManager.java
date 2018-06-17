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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * KeySelectionManager to manage the select-per-keystroke in a JComboBox (The
 * default KeySelectionManager fails because all strings start with whitespace '
 * '). <p/> Unfortunately, this class cannot be used anymore because it does not
 * work together with the listener <code>TypesBoxAL</code>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class StringKeyManager implements JComboBox.KeySelectionManager {

    /**
     * JComboBox reference.
     */
    private final JComboBox box;

    /**
     * Creates a new instance.
     * @param box the JComboBox to create a <code>StringKeyManager</code> for
     */
    public StringKeyManager(final JComboBox box) {
        this.box = box;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectionForKey(final char aKey, final ComboBoxModel aModel) {
        for (int i = 0; i < aModel.getSize(); i++) {
            if (((String) aModel.getElementAt(i)).toLowerCase().charAt(1) == aKey) {
                //typeListener.ignoreEvent = true;
                box.setSelectedIndex(i); // should happen automatically, but doesn't
                //typeListener.ignoreEvent = false;
                //typeListener.listenAction = true;
                return i;
            }
        }
        return -1;  // no match found
    }

}
