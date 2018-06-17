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

import java.awt.Color;

/**
 * Class with constants used in Gridarta and derivates.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class CommonConstants {

    /**
     * The directory that contains all (html) help files.
     */
    public static final String HELP_DIR = "resource/HelpFiles";

    /**
     * Background Color (for the Panels).
     */
    public static final Color BG_COLOR = new Color(100, 219, 169);

    /**
     * Name of the file that contains the type definitions.
     */
    public static final String TYPEDEF_FILE = "types.xml";

    /**
     * The height of rigid area between the two tab-panes on the pickmap- and
     * archetype-panel.
     */
    public static final int SPACE_PICKMAP_ARCHETYPE_TOP = 10;

    /**
     * Default file name for new maps.
     */
    public static final String DEFAULT_MAP_FILENAME = "<new map>";

    /**
     * Don't instantiate.
     */
    private CommonConstants() {
    }

}
