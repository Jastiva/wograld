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

package net.sf.gridarta.gui.scripts;

/**
 * Parameter for operation to perform in {@link ScriptArchDataUtils#modifyEventScript(int,
 * ScriptTask, javax.swing.JList, net.sf.gridarta.model.mapmanager.MapManager,
 * java.awt.Frame, Iterable)}.
 * @author Andreas Kirschbaum
 */
public enum ScriptTask {

    /**
     * Opens an editor for the script code.
     */
    EVENT_OPEN,

    /**
     * Opens a dialog to edit the script parameters.
     */
    EVENT_EDIT_PATH,

    /**
     * Removes the event object.
     */
    EVENT_REMOVE

}
