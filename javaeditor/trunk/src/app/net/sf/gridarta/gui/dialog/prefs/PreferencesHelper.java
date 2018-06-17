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

package net.sf.gridarta.gui.dialog.prefs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for preference panes.
 * @author Andreas Kirschbaum
 */
public class PreferencesHelper {

    /**
     * The {@link Container} to add to.
     */
    @NotNull
    private final Container container;

    /**
     * The {@link GridBagConstraints} for adding elements.
     */
    @NotNull
    private final GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Creates a new instance.
     * @param container the container to add to
     */
    public PreferencesHelper(@NotNull final Container container) {
        this.container = container;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
    }

    /**
     * Adds a component to the container.
     * @param component the component to add
     */
    public void addComponent(@NotNull final Component component) {
        container.add(component, gbc);
        gbc.gridy++;
    }

}
