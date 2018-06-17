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

package net.sf.gridarta.gui.dialog.plugin;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterViewFactory;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.filter.Filter;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.Plugin;
import net.sf.gridarta.plugin.PluginConsole;
import net.sf.gridarta.plugin.PluginExecException;
import net.sf.gridarta.plugin.PluginExecutor;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginModelListener;
import net.sf.gridarta.plugin.PluginModelLoader;
import net.sf.gridarta.plugin.PluginModelParser;
import net.sf.gridarta.plugin.PluginParameters;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.FileChooserUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.ReflectionAction;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Controller for plugins.
 * @author tchize
 * @todo documentation
 */
public class PluginController<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(PluginController.class);

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    @NotNull
    private final PluginModel<G, A, R> pluginModel;

    @NotNull
    private final PluginExecutor<G, A, R> pluginExecutor;

    @NotNull
    private final PluginView<G, A, R> view;

    /**
     * The plugin parameters to use.
     */
    @NotNull
    private final PluginParameters pluginParameters;

    /**
     * The parent component for dialog boxes.
     */
    @NotNull
    private final Component parent;

    @NotNull
    private final FilterControl<G, A, R> filterControl;

    /**
     * The default directory for saving plugins.
     */
    @Nullable
    private File pluginsDir;

    @NotNull
    private final PluginModelParser<G, A, R> pluginModelParser;

    @NotNull
    private final PluginModelLoader<G, A, R> pluginModelLoader;

    /**
     * The {@link PluginModelListener} attached to {@link #pluginModel}.
     */
    private final PluginModelListener<G, A, R> pluginModelListener = new PluginModelListener<G, A, R>() {

        @Override
        public void pluginCreated(@NotNull final Plugin<G, A, R> plugin) {
            // ignore
        }

        @Override
        public void pluginDeleted(@NotNull final Plugin<G, A, R> plugin) {
            // ignore
        }

        @Override
        public void pluginRegistered(@NotNull final Plugin<G, A, R> plugin) {
            final String filterName = "(s)" + plugin.getName();

            if (plugin.isFilter()) {
                final Filter<?, ?> filter = plugin.getPluginAsFilter(pluginParameters);
                if (filter != null) {
                    filterControl.addFilter(filterName, filter);
                }
            }

            if (plugin.isAutoBoot()) {
                plugin.runPlugin(pluginParameters);
            }
        }

        @Override
        public void pluginUnregistered(@NotNull final Plugin<G, A, R> plugin) {
            final String filterName = "(s)" + plugin.getName();
            filterControl.removeFilter(filterName);
        }

    };

    /**
     * Creates a new instance.
     */
    public PluginController(@NotNull final FilterControl<G, A, R> filterControl, @NotNull final PluginParameters pluginParameters, @NotNull final Component parent, @NotNull final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory, @NotNull final File pluginsDir, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, @NotNull final PluginExecutor<G, A, R> pluginExecutor, @NotNull final SystemIcons systemIcons) {
        this.parent = parent;
        this.filterControl = filterControl;
        this.pluginParameters = pluginParameters;
        this.pluginsDir = pluginsDir;
        this.pluginModel = pluginModel;
        this.pluginExecutor = pluginExecutor;
        pluginModelParser = new PluginModelParser<G, A, R>(pluginParameterFactory);
        pluginModelLoader = new PluginModelLoader<G, A, R>(pluginModelParser);
        //noinspection ResultOfObjectAllocationIgnored
        new PluginManagerFactory<G, A, R>(this, pluginModel, pluginParameterViewFactory, pluginParameterFactory, systemIcons);
        ActionUtils.newAction(ACTION_BUILDER, "Plugin", this, "savePlugins");
        ActionUtils.newAction(ACTION_BUILDER, "Plugin", this, "importPlugin");
        view = new PluginView<G, A, R>(this, pluginModel, pluginParameterViewFactory, systemIcons);
        pluginModel.addPluginModelListener(pluginModelListener);
    }

    /**
     * Saves all unsaved plugins.
     */
    @ActionMethod
    public void savePlugins() {
        for (final Plugin<G, A, R> plugin : pluginModel) {
            if (!savePlugin(plugin)) {
                return;
            }
        }
    }

    @ActionMethod
    public void importPlugin() {
        final JFileChooser choose = new JFileChooser();
        choose.setDialogTitle("import plugin");
        FileChooserUtils.sanitizeCurrentDirectory(choose);
        if (choose.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try {
                pluginModel.addPlugin(pluginModelLoader.loadXML(choose.getSelectedFile()));
            } catch (final IOException ex) {
                log.warn("can't load plugin: " + ex.getMessage());
            } catch (final JDOMException ex) {
                log.warn("can't parse plugin: " + ex.getMessage());
            }
        }
    }

    /**
     * Prompts the user for all unsaved plugins.
     * @return whether no unsaved plugins remain
     */
    public boolean canExit() {
        for (final Plugin<G, A, R> plugin : pluginModel) {
            if (plugin.isModified()) {
                final int result = ACTION_BUILDER.showConfirmDialog(parent, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, "pluginConfirmSaveChanges", plugin.getName());
                if (result == JOptionPane.YES_OPTION) {
                    if (!savePlugin(plugin)) {
                        return false;
                    }
                } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Saves one plugin. Does nothing if the plugin is unchanged.
     * @param plugin the plugin to save
     * @return whether the plugin has been saved, or if the plugin was not
     *         modified
     */
    private boolean savePlugin(@NotNull final Plugin<G, A, R> plugin) {
        if (!plugin.isModified()) {
            return true;
        }

        try {
            final File file = plugin.getFile();
            if (file != null) {
                savePlugin(plugin, file);
            } else {
                if (!savePluginAs(plugin, true)) {
                    return false;
                }
            }
        } catch (final IOException ex) {
            // XXX: notify user
            return false;
        }

        plugin.resetModified();
        return true;
    }

    /**
     * Prompts the user for a file name to save a plugin. The plugin is saved
     * regardless whether the plugin is modified or not.
     * @param plugin the plugin to save
     * @param updatePluginFile if set, update the plugin file to the save
     * location
     * @return whether the plugin has been saved
     */
    public boolean savePluginAs(@NotNull final Plugin<G, A, R> plugin, final boolean updatePluginFile) {
        final JFileChooser chooser = new JFileChooser();
        final File pluginFile = plugin.getFile();
        FileChooserUtils.setCurrentDirectory(chooser, pluginFile != null ? pluginFile : pluginsDir);
        chooser.setDialogTitle("save plugin " + plugin.getName());
        final int result = chooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        final File file = chooser.getSelectedFile();
        if (updatePluginFile) {
            plugin.setFile(file);
        }
        pluginsDir = file.getParentFile();

        try {
            savePlugin(plugin, file);
        } catch (final IOException ex) {
            return false;
        }

        return true;
    }

    /**
     * Saves a plugin to a given file.
     * @param plugin the plugin to save
     * @param file the file to save to
     * @throws IOException if the file cannot be saved
     */
    private void savePlugin(@NotNull final Plugin<G, A, R> plugin, @NotNull final File file) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file);
        try {
            final Element root = pluginModelParser.toXML(plugin);
            final Document d = new Document(root);
            final XMLOutputter out = new XMLOutputter();
            out.setFormat(Format.getPrettyFormat());
            out.output(d, fos);
        } finally {
            fos.close();
        }
    }

    public void runPlugin(@NotNull final Plugin<G, A, R> plugin) {
        final Plugin<G, A, R> clonedPlugin = plugin.clonePlugin();

        if (!view.getRunValues(clonedPlugin, parent)) {
            return;
        }

        final PluginConsole console = view.createConsole(clonedPlugin.getName());
        try {
            pluginExecutor.doRunPlugin(clonedPlugin, console); // XXX: drops Thread
        } catch (final PluginExecException ex) {
            console.print(ex.getMessage());
        }
    }

    @ActionMethod
    public void runPlugin(@NotNull final String name) {
        final Plugin<G, A, R> plugin = pluginModel.getPlugin(name);
        if (plugin != null) {
            runPlugin(plugin);
        }
    }

    @NotNull
    public PluginView<G, A, R> getView() {
        return view;
    }

    /**
     * Creates an action to run a plugin plugin.
     * @param plugin the plugin to run
     * @return the action
     */
    @NotNull
    public Action createRunAction(@NotNull final Plugin<G, A, R> plugin) {
        final Action action = ActionUtils.newAction(ACTION_BUILDER, "Plugin", this, "runPlugin");
        action.putValue(ReflectionAction.REFLECTION_ARGUMENTS, new Object[] { plugin.getName(), });
        action.putValue(Action.NAME, ACTION_BUILDER.format("runPlugin.text", plugin.getName()));
        return action;
    }

}
