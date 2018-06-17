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

import bsh.EvalError;
import bsh.Interpreter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.filter.Filter;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.NoSuchParameterException;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.plugin.parameter.PluginParameterCodec;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.plugin.parameter.StringParameter;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model for plugins.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class Plugin<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<PluginParameter<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(Plugin.class);

    /**
     * The executable code.
     */
    @NotNull
    private String code = "";

    /**
     * The {@link PluginParameter PluginParameters} for this plugin.
     */
    @NotNull
    private final List<PluginParameter<G, A, R>> parameters = new ArrayList<PluginParameter<G, A, R>>();

    /**
     * The plugin name.
     */
    @NotNull
    private final String name;

    /**
     * The {@link PluginParameterFactory} for creating plugin parameters.
     */
    @NotNull
    private final PluginParameterFactory<G, A, R> pluginParameterFactory;

    /**
     * The {@link PluginParameterCodec} for converting {@link PluginParameter
     * PluginParameters} to or from XML representation.
     */
    @NotNull
    private final PluginParameterCodec<G, A, R> codec = new PluginParameterCodec<G, A, R>();

    /**
     * Whether this plugin is run whenever the editor starts.
     */
    private boolean autoBoot;

    /**
     * Whether this plugin is a filter.
     */
    private boolean filter;

    /**
     * Whether this plugin is a stand-alone plugin.
     */
    private boolean script;

    /**
     * The {@link ChangeListener ChangeListeners} to inform about changes.
     */
    @NotNull
    private final Collection<ChangeListener> listeners = new HashSet<ChangeListener>();

    /**
     * The location to save this plugin to; set to <code>null</code> if the
     * plugin has no associated location.
     */
    @Nullable
    private File file;

    /**
     * Whether the plugin contents has been modified since last save.
     */
    private boolean modified;

    /**
     * Creates a new instance.
     * @param name the plugin name
     * @param pluginParameterFactory the plugin parameter factory for creating
     * plugin parameters
     */
    public Plugin(@NotNull final String name, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory) {
        this.name = name;
        this.pluginParameterFactory = pluginParameterFactory;
    }

    /**
     * Returns the name of this plugin.
     * @return the name of this plugin
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns the executable code of this plugin.
     * @return the executable code of this plugin
     */
    @NotNull
    public String getCode() {
        return code;
    }

    /**
     * Sets the executable code of this plugin.
     * @param code the executable code of this plugin
     */
    public void setCode(@NotNull final String code) {
        if (this.code.equals(code)) {
            return;
        }

        this.code = code;
        modified = true;
        notifyListeners();
    }

    /**
     * Returns whether this plugin has at least one parameter.
     * @return whether this plugin has at least one parameter
     */
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    /**
     * Returns the index for a plugin parameter name.
     * @param paramName the plugin parameter name
     * @return the index or <code>-1</code> if the parameter name does not
     *         exist
     */
    public int getParameter(@NotNull final String paramName) {
        int index = 0;
        for (final PluginParameter<G, A, R> param : parameters) {
            if (param.getName().equals(paramName)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return name;
    }

    /**
     * Creates a new plugin parameter.
     */
    public void newParameter() {
        final PluginParameter<G, A, R> pluginParameter;
        try {
            pluginParameter = pluginParameterFactory.createParameter(StringParameter.PARAMETER_TYPE);
        } catch (final NoSuchParameterException ex) {
            log.warn("Cannot create parameter: " + ex.getMessage());
            return;
        }
        parameters.add(pluginParameter);
        modified = true;
        notifyListeners();
    }

    /**
     * Removes a plugin parameter.
     * @param index the plugin parameter's index
     */
    public void removeParameter(final int index) {
        parameters.remove(index);
        modified = true;
        notifyListeners();
    }

    /**
     * Adds a {@link ChangeListener} to be notified about changes.
     * @param listener the change listener
     */
    public void addChangeListener(@NotNull final ChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link ChangeListener} to be notified about changes.
     * @param listener the change listener
     */
    public void removeListener(@NotNull final ChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered {@link ChangeListener ChangeListeners} that this
     * plugin has changed.
     */
    private void notifyListeners() {
        final ChangeEvent e = new ChangeEvent(this);
        for (final ChangeListener listener : listeners) {
            listener.stateChanged(e);
        }
    }

    /**
     * Returns a clone copy of this plugin. The copy include name, executable
     * code, type and a clone of each parameter. The change listeners are not
     * copied.
     * @return a clone of this plugin
     */
    @NotNull
    public Plugin<G, A, R> clonePlugin() {
        final Plugin<G, A, R> model = new Plugin<G, A, R>(name, pluginParameterFactory);
        model.code = code;
        model.autoBoot = autoBoot;
        model.filter = filter;
        model.script = script;
        model.modified = modified;
        for (final PluginParameter<G, A, R> param : parameters) {
            final Element paramXml = codec.toXML(param);
            final PluginParameter<G, A, R> clonedParam;
            try {
                clonedParam = pluginParameterFactory.createParameter(paramXml);
            } catch (final NoSuchParameterException ex) {
                throw new AssertionError(ex);
            }
            model.addParameter(clonedParam);
        }
        return model;
    }

    /**
     * Adds a plugin parameter to this plugin.
     * @param pluginParameter the plugin parameter to add
     */
    public void addParameter(@NotNull final PluginParameter<G, A, R> pluginParameter) {
        parameters.add(pluginParameter);
        modified = true;
        notifyListeners();
    }

    /**
     * Returns the {@link PluginParameter} at a given index.
     * @param index the index of parameter to return; must be between
     * <code>0</code> and <code>getParametersCount()</code>
     * @return the plugin parameter
     * @throws NoSuchParameterException if the index is invalid
     */
    public PluginParameter<G, A, R> getParameter(final int index) throws NoSuchParameterException {
        try {
            return parameters.get(index);
        } catch (final IndexOutOfBoundsException ignored) {
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new NoSuchParameterException(index);
        }
    }

    /**
     * Returns whether this plugin is run whenever the editor starts.
     * @return whether this plugin is run whenever the editor starts
     */
    public boolean isAutoBoot() {
        return autoBoot;
    }

    /**
     * Returns whether this plugin is a stand-alone plugin.
     * @return whether this plugin is a stand-alone plugin
     */
    public boolean isScript() {
        return script;
    }

    /**
     * Returns whether this plugin is a filter.
     * @return whether this plugin is a filter
     */
    public boolean isFilter() {
        return filter;
    }

    /**
     * Sets whether this plugin is run whenever the editor starts.
     * @param autoBoot whether this plugin is run whenever the editor starts
     */
    public void setAutoBoot(final boolean autoBoot) {
        if (this.autoBoot == autoBoot) {
            return;
        }
        this.autoBoot = autoBoot;
        modified = true;
        notifyListeners();
    }

    /**
     * Sets whether this plugin is a stand-alone plugin.
     * @param script whether this plugin is a stand-alone plugin
     */
    public void setScript(final boolean script) {
        if (this.script == script) {
            return;
        }
        this.script = script;
        modified = true;
        notifyListeners();
    }

    /**
     * Sets whether this plugin is a filter.
     * @param filter whether this plugin is a filter
     */
    public void setFilter(final boolean filter) {
        if (this.filter == filter) {
            return;
        }
        this.filter = filter;
        modified = true;
        notifyListeners();
    }

    /**
     * Changes the type of a plugin parameter.
     * @param index the plugin parameter's index
     * @param newType the new type
     * @throws NoSuchParameterException if the index is invalid
     */
    public void convertType(final int index, @NotNull final String newType) throws NoSuchParameterException {
        parameters.set(index, pluginParameterFactory.createParameter(newType, codec.toXML(parameters.get(index))));
        modified = true;
        notifyListeners();
    }

    /**
     * Changes the type of a plugin parameter.
     * @param pluginParameter the plugin parameter
     * @param newType the new type
     * @throws NoSuchParameterException if the index is invalid
     */
    public void convertType(@NotNull final PluginParameter<G, A, R> pluginParameter, @NotNull final String newType) throws NoSuchParameterException {
        final int index = parameters.indexOf(pluginParameter);
        convertType(index, newType);
    }

    /**
     * Returns whether the plugin contents have been modified since last save.
     * @return whether the plugin contents have been modified since last save
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Marks the plugin as unmodified since last save.
     */
    public void resetModified() {
        if (!modified) {
            return;
        }

        modified = false;
        notifyListeners();
    }

    /**
     * Returns the location to save this plugin to.
     * @return the location to save to, or <code>null</code> if the plugin has
     *         no associated location
     */
    @Nullable
    public File getFile() {
        return file;
    }

    /**
     * Sets the location to save this plugin to.
     * @param file the save location or <code>null</code>
     */
    public void setFile(@Nullable final File file) {
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<PluginParameter<G, A, R>> iterator() {
        return Collections.unmodifiableList(parameters).iterator();
    }

    public void runPlugin(@NotNull final PluginParameters pluginParameters) {
        final Interpreter runner = new Interpreter();
        try {
            pluginParameters.setInterpreterValues(runner, PluginRunMode.AUTO_RUN);
            runner.eval(code);
        } catch (final EvalError e) {
            log.warn("Evaluation error on (auto-run)" + name, e);
        }
    }

    @Nullable
    public Filter<?, ?> getPluginAsFilter(@NotNull final PluginParameters pluginParameters) {
        final Interpreter runner = new Interpreter();
        try {
            pluginParameters.setInterpreterValues(runner, PluginRunMode.FILTER);
            return (Filter<?, ?>) runner.eval(code);
        } catch (final EvalError e) {
            log.warn("Evaluation error on (filter)" + name, e);
        } catch (final ClassCastException e) {
            log.warn("Plugin did not return a net.sf.gridarta.model.filter.Filter object" + name, e);
        }
        return null;
    }

    /**
     * Returns XML representation for a {@link PluginParameter}.
     * @param pluginParameter the plugin parameter
     * @return the plugin parameter in XML representation
     */
    @NotNull
    public Content toXML(@NotNull final PluginParameter<G, A, R> pluginParameter) {
        return codec.toXML(pluginParameter);
    }

}
