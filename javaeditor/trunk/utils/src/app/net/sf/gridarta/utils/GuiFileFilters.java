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

package net.sf.gridarta.utils;

import javax.swing.filechooser.FileFilter;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.util.filter.file.EndingFileFilter;

/**
 * Utility class defining {@link FileFilter FileFilters}.
 * @author Andreas Kirschbaum
 */
public class GuiFileFilters {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Private constructor to prevent instantiation.
     */
    private GuiFileFilters() {
    }

    /**
     * Swing FileFilter for map files.
     */
    public static final FileFilter mapFileFilter = new HideFileFilterProxy(new MapFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.maps"), ".lua", ".py", ".txt", ".text"));

    /**
     * Swing FileFilter for Python scripts.
     */
    public static final FileFilter pythonFileFilter = new HideFileFilterProxy(new EndingFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.python"), ".py"));

    /**
     * Swing FileFilter for Lua scripts.
     */
    public static final FileFilter luaFileFilter = new HideFileFilterProxy(new EndingFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.lua"), ".lua"));

}
