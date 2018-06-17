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

import java.util.ArrayList;
import java.util.MissingResourceException;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ConfigSourceFactory} that is configured through action keys.
 * @author Andreas Kirschbaum
 */
public class DefaultConfigSourceFactory implements ConfigSourceFactory {

    /**
     * The action key for configuration source class names. The class names are
     * separated by spaces.
     */
    @NotNull
    private static final String CONFIG_SOURCES_KEY = "configSources";

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The defined configuration sources. The list is never empty; the first
     * entry is the default configuration source.
     */
    @NotNull
    private final ArrayList<ConfigSource> configSources = new ArrayList<ConfigSource>();

    /**
     * Creates a new instance.
     */
    public DefaultConfigSourceFactory() {
        final String configSourceClassNames = ActionBuilderUtils.getString(ACTION_BUILDER, CONFIG_SOURCES_KEY);
        for (final String configSourceClassName : StringUtils.PATTERN_SPACE.split(configSourceClassNames, -1)) {
            final Class<?> configSourceClass;
            try {
                configSourceClass = Class.forName(configSourceClassName);
            } catch (final ClassNotFoundException ex) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw new MissingResourceException("Class does not exist: " + configSourceClassName + ": " + ex.getMessage(), "net.sf.gridarta", CONFIG_SOURCES_KEY);
            }

            final Class<? extends ConfigSource> configSourceClass2;
            try {
                configSourceClass2 = configSourceClass.asSubclass(ConfigSource.class);
            } catch (final ClassCastException ex) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw new MissingResourceException("Class does not implement ConfigSource: " + configSourceClassName + ": " + ex.getMessage(), "net.sf.gridarta", CONFIG_SOURCES_KEY);
            }

            final ConfigSource configSource;
            try {
                configSource = configSourceClass2.newInstance();
            } catch (final InstantiationException ex) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw new MissingResourceException("Class cannot be instantiated: " + configSourceClassName + ": " + ex.getMessage(), "net.sf.gridarta", CONFIG_SOURCES_KEY);
            } catch (final IllegalAccessException ex) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw new MissingResourceException("Class cannot be instantiated: " + configSourceClassName + ": " + ex.getMessage(), "net.sf.gridarta", CONFIG_SOURCES_KEY);
            }

            configSources.add(configSource);
        }

        configSources.trimToSize();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ConfigSource[] getConfigSources() {
        return configSources.toArray(new ConfigSource[configSources.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ConfigSource getConfigSource(@NotNull final String name) {
        for (final ConfigSource configSource : configSources) {
            if (configSource.getName().equals(name)) {
                return configSource;
            }
        }

        return configSources.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ConfigSource getFilesConfigSource() {
        return getConfigSource("ARCH_DIRECTORY");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ConfigSource getDefaultConfigSource() {
        return configSources.get(0);
    }

}
