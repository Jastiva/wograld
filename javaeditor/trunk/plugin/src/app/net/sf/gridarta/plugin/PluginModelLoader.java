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

package net.sf.gridarta.plugin;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for loading plugins.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class PluginModelLoader<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(PluginModelLoader.class);

    /**
     * The {@link PluginModelParser} to use.
     */
    @NotNull
    private final PluginModelParser<G, A, R> pluginModelParser;

    /**
     * Creates a new instance.
     * @param pluginModelParser the plugin model parser to use
     */
    public PluginModelLoader(@NotNull final PluginModelParser<G, A, R> pluginModelParser) {
        this.pluginModelParser = pluginModelParser;
    }

    /**
     * Load all files as plugins from the given directory.
     * @param errorView the error view for reporting errors
     * @param pluginsDir the directory to load
     * @param pluginModel the plugin mode to add to
     */
    public void loadPlugins(@NotNull final ErrorView errorView, @NotNull final File pluginsDir, @NotNull final PluginModel<G, A, R> pluginModel) {
        final File[] files = pluginsDir.listFiles();
        if (files == null) {
            errorView.addWarning(ErrorViewCategory.SCRIPTS_DIR_INVALID, pluginsDir + ": directory not readable");
            return;
        }

        final int pluginCount = pluginModel.getPluginCount();
        for (final File pluginFile : files) {
            final String name = pluginFile.getName();
            if (!name.startsWith(".") && !name.endsWith("~") && pluginFile.isFile()) {
                try {
                    final Plugin<G, A, R> plugin = loadXML(pluginFile);
                    if (pluginModel.getPlugin(plugin.getName()) != null) {
                        errorView.addWarning(ErrorViewCategory.SCRIPTS_FILE_INVALID, pluginFile + ": duplicate plugin '" + plugin.getName() + "'");
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("storing with code " + plugin.getCode());
                        }
                        pluginModel.addPlugin(plugin);
                    }
                } catch (final IOException ex) {
                    errorView.addWarning(ErrorViewCategory.SCRIPTS_FILE_INVALID, pluginFile + ": " + ex.getMessage());
                } catch (final JDOMException ex) {
                    errorView.addWarning(ErrorViewCategory.SCRIPTS_FILE_INVALID, pluginFile + ": " + ex.getMessage());
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + (pluginModel.getPluginCount() - pluginCount) + " plugins from '" + pluginsDir + "'.");
        }
    }

    public Plugin<G, A, R> loadXML(@NotNull final File file) throws IOException, JDOMException {
        final SAXBuilder builder = new SAXBuilder(false); /*non validating*/
        final Document d = builder.build(file);
        return loadXML(d, file);
    }

    @NotNull
    private Plugin<G, A, R> loadXML(final Document doc, @Nullable final File file) throws IOException {
        if (!doc.hasRootElement()) {
            throw new IOException("plugin file is empty");
        }

        final Element elt = doc.getRootElement();
        if (!elt.getName().equalsIgnoreCase("script")) {
            throw new IOException("missing root element named \"script\"");
        }

        final Plugin<G, A, R> plugin = pluginModelParser.fromXML(elt);
        plugin.setFile(file);
        if (log.isDebugEnabled()) {
            log.debug("plugin: " + plugin.getName());
        }

        return plugin;
    }

}
