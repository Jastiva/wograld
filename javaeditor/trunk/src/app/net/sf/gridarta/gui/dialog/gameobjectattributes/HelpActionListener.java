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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import net.sf.gridarta.model.archetypetype.ArchetypeAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * ActionListener for help-buttons.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class HelpActionListener implements ActionListener {

    /**
     * The {@link ArchetypeAttribute} which contains help information.
     */
    @NotNull
    private final ArchetypeAttribute archetypeAttribute;

    /**
     * The component for help dialogs.
     */
    @NotNull
    private final Component parent;

    /**
     * Constructor.
     * @param a the gameObject attribute where this help button belongs to
     * @param parent the parent component for help dialogs
     */
    public HelpActionListener(@NotNull final ArchetypeAttribute a, @NotNull final Component parent) {
        archetypeAttribute = a;
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        popupHelp(archetypeAttribute.getAttributeName(), archetypeAttribute.getDescription());
    }

    /**
     * Spawns a popup-message to display the help text of an attribute.
     * @param title name of attribute
     * @param msg message text
     */
    private void popupHelp(@NotNull final String title, @NotNull final String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Help: " + title, JOptionPane.PLAIN_MESSAGE);
    }

}
