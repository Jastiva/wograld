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

import net.sf.gridarta.gui.utils.tristate.TristateCheckBox;
import net.sf.gridarta.gui.utils.tristate.TristateState;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link TristateCheckBox} that skips the indeterminate state when
 * activated.
 * @author Andreas Kirschbaum
 */
public class ToggleTristateCheckBox extends TristateCheckBox {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param text the check box text
     */
    public ToggleTristateCheckBox(@NotNull final String text) {
        super(text);
    }

    /**
     * {@inheritDoc} Skips the indeterminate state.
     */
    @NotNull
    @Override
    protected TristateState nextState(@NotNull final TristateState state) {
        final TristateState nextState = super.nextState(state);
        return nextState == TristateState.INDETERMINATE ? nextState.nextTristateState() : nextState;
    }

}
