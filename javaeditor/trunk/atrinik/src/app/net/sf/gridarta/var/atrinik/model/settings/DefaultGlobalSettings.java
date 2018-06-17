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

package net.sf.gridarta.var.atrinik.model.settings;

import java.io.File;
import net.sf.gridarta.var.atrinik.IGUIConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Maintains path names to resources.
 * @author Andreas Kirschbaum
 */
public class DefaultGlobalSettings extends net.sf.gridarta.model.settings.DefaultGlobalSettings {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getPickmapDir() {
        return new File(getArchDirectory(), IGUIConstants.PICKMAP_DIR);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getConfigurationDirectory() {
        return new File(getArchDirectory(), "dev/editor/conf");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public File getCollectedDirectory() {
        return getArchDirectory();
    }

}
