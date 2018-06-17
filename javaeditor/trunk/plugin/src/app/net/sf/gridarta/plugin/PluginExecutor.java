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

import bsh.ConsoleInterface;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import java.io.CharArrayWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.NoSuchParameterException;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Allows execution of {@link Plugin Plugins}.
 * @author Andreas Kirschbaum
 */
public class PluginExecutor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link PluginModel} to execute.
     */
    @NotNull
    private final PluginModel<G, A, R> pluginModel;

    /**
     * The parameters to pass to the plugin.
     */
    @NotNull
    private final PluginParameters pluginParameters;

    /**
     * Creates a new instance.
     * @param pluginModel the plugin model to execute
     * @param pluginParameters the parameters to pass to the plugin
     */
    public PluginExecutor(@NotNull final PluginModel<G, A, R> pluginModel, @NotNull final PluginParameters pluginParameters) {
        this.pluginModel = pluginModel;
        this.pluginParameters = pluginParameters;
    }

    /**
     * Executes an editor plugin.
     * @param plugin the plugin name to execute
     * @param params the plugin parameters
     * @throws PluginExecException if an error occurs
     */
    public void executePlugin(@NotNull final String plugin, @NotNull final Iterable<String> params) throws PluginExecException {
        final Plugin<G, A, R> modelTemplate = pluginModel.getPlugin(plugin);
        if (modelTemplate == null) {
            throw new PluginExecException("plugin " + plugin + " does not exist");
        }

        final Plugin<G, A, R> clonedPlugin = modelTemplate.clonePlugin();

        for (final String param : params) {
            final String[] tmp = StringUtils.PATTERN_EQUAL.split(param, 2);
            if (tmp.length != 2) {
                throw new PluginExecException("syntax error: " + param);
            }

            final int index = clonedPlugin.getParameter(tmp[0]);
            if (index == -1) {
                final StringBuilder sb = new StringBuilder();
                boolean firstParameter = true;
                for (final PluginParameter<?, ?, ?> parameter : clonedPlugin) {
                    sb.append(firstParameter ? " " : ", ");
                    sb.append(parameter.getName());
                    firstParameter = false;
                }
                throw new PluginExecException("plugin " + plugin + " has no parameter " + tmp[0] + "; available parameters:" + sb);
            }

            final PluginParameter<G, A, R> parameter;
            try {
                parameter = clonedPlugin.getParameter(index);
            } catch (final NoSuchParameterException ex) {
                throw new AssertionError(ex);
            }
            if (!parameter.setStringValue(tmp[1])) {
                throw new PluginExecException("invalid value " + tmp[1] + " for parameter " + tmp[0]);
            }
        }

        final ConsoleInterface console = new ConsoleInterface() {

            @Override
            public Reader getIn() {
                return new InputStreamReader(System.in);
            }

            @Override
            public PrintStream getOut() {
                return System.out;
            }

            @Override
            public PrintStream getErr() {
                return System.err;
            }

            @Override
            public void println(final Object o) {
                System.out.println(o);
            }

            @Override
            public void print(final Object o) {
                System.out.print(o);
            }

            @Override
            public void error(final Object o) {
                System.err.println(o);
            }

        };
        final BshThread<G, A, R> pluginThread = doRunPlugin(clonedPlugin, console);
        try {
            pluginThread.join();
        } catch (final InterruptedException ex) {
            pluginThread.interrupt();
            Thread.currentThread().interrupt();
            throw new PluginExecException("interrupted", ex);
        }

        if (!pluginThread.isSuccess()) {
            throw new PluginExecException("plugin failed");
        }
    }

    /**
     * Runs a plugin model. The model is expected to have all needed parameters
     * set.
     * @param plugin the plugin model
     * @param console the console to use
     * @return the plugin thread
     * @throws PluginExecException if an error occurs
     */
    @NotNull
    public BshThread<G, A, R> doRunPlugin(@NotNull final Plugin<G, A, R> plugin, @NotNull final ConsoleInterface console) throws PluginExecException {
        final Interpreter runner = new Interpreter();
        runner.setConsole(console);
        try {
            pluginParameters.setInterpreterValues(runner, PluginRunMode.BATCH);
            for (final PluginParameter<G, A, R> parameter : plugin) {
                runner.set(parameter.getName(), parameter.getValue());
            }
        } catch (final TargetError ex) {
            final CharArrayWriter charArrayWriter = new CharArrayWriter();
            try {
                final PrintWriter printWriter = new PrintWriter(charArrayWriter);
                try {
                    ex.getTarget().printStackTrace(printWriter);
                } finally {
                    printWriter.close();
                }
            } finally {
                charArrayWriter.close();
            }
            throw new PluginExecException("target error: " + charArrayWriter, ex);
        } catch (final EvalError ex) {
            throw new PluginExecException("evaluation error: " + ex.getMessage(), ex);
        }
        final BshThread<G, A, R> pluginThread = new BshThread<G, A, R>(plugin.getName(), plugin, runner);
        pluginThread.start();
        return pluginThread;
    }

}
