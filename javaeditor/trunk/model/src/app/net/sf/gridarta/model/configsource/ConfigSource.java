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

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.settings.GlobalSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Possible source locations for configuration files.
 * @author Andreas Kirschbaum
 */
public interface ConfigSource {

    /**
     * Returns the internal name.
     * @return the internal name
     */
    @NotNull
    String getName();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    String toString();

    /**
     * Reads config files.
     * @param globalSettings the global settings to use
     * @param resources the resources to update
     * @param errorView the error view for reporting errors
     */
    <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void read(@NotNull GlobalSettings globalSettings, @NotNull AbstractResources<G, A, R> resources, @NotNull ErrorView errorView);

    /**
     * Whether the "archetype directory" input field in the settings dialog
     * should be enabled.
     * @return whether the input field should be enabled
     */
    boolean isArchDirectoryInputFieldEnabled();

    /**
     * Returns a config file.
     * @param globalSettings the global settings to use
     * @param type the file type
     * @param index the file index, starting with <code>0</code>
     * @return the file or <code>null</code> if no more files are available
     * @throws IOException if an error occurs
     */
    @Nullable
    File getFile(@NotNull GlobalSettings globalSettings, @NotNull String type, int index) throws IOException;

}
