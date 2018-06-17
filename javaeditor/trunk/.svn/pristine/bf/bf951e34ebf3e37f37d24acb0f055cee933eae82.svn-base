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

package net.sf.gridarta.gui.mapmenu;

import org.jetbrains.annotations.NotNull;

/**
 * An {@link AbstractMapMenuPreferences} implementation for regression tests.
 * @author Andreas Kirschbaum
 */
public class TestMapMenuPreferences extends AbstractMapMenuPreferences {

    /**
     * Creates a new instance.
     */
    public TestMapMenuPreferences() {
        super("test");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEntry(@NotNull final MapMenuEntryMap mapMenuEntry) {
        final MapMenu mapMenu = getMapMenu();
        mapMenu.insertNodeInto(mapMenuEntry, mapMenu.getRoot(), 0);
    }

}
