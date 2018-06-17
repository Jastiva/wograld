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

package net.sf.gridarta.var.crossfire;

import net.sf.gridarta.gui.utils.GUIConstants;

/**
 * Defines common UI constants used in different dialogs and all used icon
 * files.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public interface IGUIConstants extends GUIConstants {

    /**
     * The width of a square in pixels.
     */
    int SQUARE_WIDTH = 32;

    /**
     * The height of a square in pixels.
     */
    int SQUARE_HEIGHT = 32;

    /**
     * Default map size (both height and width).
     */
    int DEF_MAPSIZE = 10;

    /**
     * Default width for pickmaps.
     */
    int DEF_PICKMAP_WIDTH = 7;

    /**
     * Default height for pickmaps.
     */
    int DEF_PICKMAP_HEIGHT = 12;

    /**
     * The directory that contains all pickmaps.
     */
    String PICKMAP_DIR = "editor/pickmaps";

    /**
     * The directory that contains all scripts.
     */
    String SCRIPTS_DIR = "editor/scripts";

    // name of the arch resource files (these get read and written in the arch dir)

    String ARCH_FILE = "archetypes";        // file with all arches

    /**
     * File to store the animation tree information after arch collection.
     */
    String ANIMTREE_FILE = "animtree";

    String SMOOTH_FILE = "smooth";

}
