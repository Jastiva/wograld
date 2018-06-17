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

package net.sf.gridarta.model.configsource;

import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating {@link ConfigSource ConfigSources}.
 * @author Andreas Kirschbaum
 */
public interface ConfigSourceFactory {

    /**
     * Returns all defined configuration sources. The returned array may be
     * modified.
     * @return the defined configuration sources
     */
    @NotNull
    ConfigSource[] getConfigSources();

    /**
     * Returns a {@link ConfigSource} by name.
     * @param name the name
     * @return the configuration source of a default configuration source if the
     *         name does not exist
     */
    @NotNull
    ConfigSource getConfigSource(@NotNull String name);

    /**
     * Returns the {@link ConfigSource} that reads individual files.
     * @return the configuration source that reads individual files
     */
    @NotNull
    ConfigSource getFilesConfigSource();

    /**
     * Returns the default {@link ConfigSource}.
     * @return the default configuration source
     */
    @NotNull
    ConfigSource getDefaultConfigSource();

}
