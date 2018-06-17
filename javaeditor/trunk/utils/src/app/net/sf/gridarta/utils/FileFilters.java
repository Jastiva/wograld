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
import org.jetbrains.annotations.NotNull;

/**
 * Utility class defining {@link FileFilter FileFilters}.
 * @author Andreas Kirschbaum
 */
public class FileFilters {

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Private constructor to prevent instantiation.
     */
    private FileFilters() {
    }

    /**
     * Swing FileFilter for .arc files.
     */
    @NotNull
    public static final FileFilter arcFileFilter = new HideFileFilterProxy(new EndingFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.arc"), ".arc"));

    /**
     * Swing FileFilter for .anim files.
     */
    @NotNull
    public static final FileFilter animFileFilter = new HideFileFilterProxy(new EndingFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.anim"), ".anim"));

    /**
     * Swing FileFilter for .face files.
     */
    @NotNull
    public static final FileFilter faceFileFilter = new HideFileFilterProxy(new EndingFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.face"), ".face"));

    /**
     * Swing FileFilter for png graphics.
     */
    @NotNull
    public static final FileFilter pngFileFilter = new HideFileFilterProxy(new EndingFileFilter(true, ActionBuilderUtils.getString(ACTION_BUILDER, "fileDialog.filter.png"), ".png"));

}
