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

package net.sf.gridarta.plugin.parameter;

import java.io.File;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for {@link PluginParameter PluginParameters} that hold a {@link
 * File} value.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractPathParameter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractPluginParameter<G, A, R, String> {

    /**
     * The base directory.
     */
    @NotNull
    private final File baseDir;

    /**
     * Creates a new instance.
     * @param baseDir the base directory
     */
    protected AbstractPathParameter(@NotNull final File baseDir) {
        this.baseDir = baseDir;
        setValue("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setStringValue(@NotNull final String value) {
        setValue(value);
        return true;
    }

    /**
     * Returns the base directory.
     * @return the base directory
     */
    @NotNull
    public File getBaseDir() {
        return baseDir;
    }

    /**
     * Sets the current {@link File}.
     * @param file the file
     */
    public void setFile(@NotNull final File file) {
        setValue(PathManager.getMapPath(file.getAbsolutePath(), baseDir));
    }

} // class AbstractPathParameter
