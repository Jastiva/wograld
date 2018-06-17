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

package net.sf.gridarta.gui.mapimagecache;

/**
 * The types of images that are cached.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public enum ImageType {

    /**
     * The icon is a small thumbnail usable as icon in file browsers.
     */
    ICON,

    /**
     * The preview is a large thumbnail.
     */
    PREVIEW;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        switch (this) {
        case ICON:
            return "icon";

        case PREVIEW:
            return "preview";

        default:
            assert false;
            throw new AssertionError();
        }
    }

}
