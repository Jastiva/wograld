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

package net.sf.gridarta.gui.utils.tabbedpanel;

import java.util.EventListener;
import javax.swing.AbstractButton;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners interested in {@link ButtonList} related events.
 * @author Andreas Kirschbaum
 */
public interface ButtonListListener extends EventListener {

    /**
     * The selected button has changed.
     * @param prevSelectedButton the old selected button or <code>null</code> if
     * no button was selected
     * @param selectedButton the new selected button or <code>null</code> if no
     * button is selected
     */
    void selectedButtonChanged(@Nullable AbstractButton prevSelectedButton, @Nullable AbstractButton selectedButton);

}
