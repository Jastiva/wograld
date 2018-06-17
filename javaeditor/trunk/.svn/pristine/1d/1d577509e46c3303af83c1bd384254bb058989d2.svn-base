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
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Makes basic Gridarta classes available to scripts.
 * @author Andreas Kirschbaum
 */
public class PluginParameters {

    /**
     * All defined variables for new plugins. Maps variable name to value.
     */
    @NotNull
    private final Map<String, Object> variables = new HashMap<String, Object>();

    /**
     * Defines a variable to be passed to plugins.
     * @param name the variable name
     * @param value the variable's value
     */
    public void addPluginParameter(@NotNull final String name, @Nullable final Object value) {
        variables.put(name, value);
    }

    /**
     * Adds variables to a {@link Interpreter} instance.
     * @param interpreter the interpreter to modify
     * @param pluginRunMode the plugin run mode to use
     * @throws EvalError if a variable cannot be set
     */
    public void setInterpreterValues(@NotNull final Interpreter interpreter, @NotNull final PluginRunMode pluginRunMode) throws EvalError {
        for (final Map.Entry<String, Object> variable : variables.entrySet()) {
            interpreter.set(variable.getKey(), variable.getValue());
        }
        interpreter.set("pluginRunMode", pluginRunMode);
    }

}
