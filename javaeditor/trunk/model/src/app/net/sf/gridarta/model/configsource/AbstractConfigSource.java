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
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.IOUtils;
import net.sf.gridarta.utils.StringParameterBuilder;
import net.sf.gridarta.utils.SyntaxErrorException;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link ConfigSource} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractConfigSource implements ConfigSource {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        final String result = ACTION_BUILDER.getString("optionsConfigSource_" + getName());
        return result == null ? getName() : result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public File getFile(@NotNull final GlobalSettings globalSettings, @NotNull final String type, final int index) throws IOException {
        final String key = "configSource." + getName() + "." + type + "." + index;
        final String spec = ACTION_BUILDER.getString(key);
        if (spec == null) {
            return null;
        }

        final StringParameterBuilder stringParameterBuilder = new StringParameterBuilder();
        stringParameterBuilder.addParameter("COLLECTED", globalSettings.getCollectedDirectory().getPath());
        stringParameterBuilder.addParameter("ARCH", globalSettings.getArchDirectory().getPath());
        stringParameterBuilder.addParameter("MAPS", globalSettings.getMapsDirectory().getPath());
        final String result;
        try {
            result = stringParameterBuilder.replace(spec);
        } catch (final SyntaxErrorException ex) {
            throw new IOException(ex.getMessage() + " in " + key + "=" + spec, ex);
        }
        return IOUtils.getFile(null, result);
    }

}
