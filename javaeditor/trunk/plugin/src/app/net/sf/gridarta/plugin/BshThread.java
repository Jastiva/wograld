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
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;

/**
 * A BshThread.
 * @author tchize
 * @todo Document this class.
 */
public class BshThread<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends Thread {

    /**
     * The PluginModel of this BshThread.
     */
    private final Plugin<G, A, R> plugin;

    /**
     * The Interpreter of this BshThread.
     */
    private final Interpreter interpreter;

    /**
     * Whether the plugin has been executed successfully.
     */
    private boolean success;

    /**
     * Creates a new instance.
     * @param name the name to assign to this thread
     * @param plugin the plugin model for this thread
     * @param interpreter the interpreter for this thread
     */
    public BshThread(final String name, final Plugin<G, A, R> plugin, final Interpreter interpreter) {
        super(name);
        this.plugin = plugin;
        this.interpreter = interpreter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            interpreter.set("scriptThread", this);
            interpreter.eval(plugin.getCode());
            success = true;
        } catch (final EvalError e) {
            e.printStackTrace(interpreter.getErr());
        }
    }

    /**
     * Returns whether the plugin has been executed successfully.
     * @return whether the plugin has been executed successfully
     */
    public boolean isSuccess() {
        return success;
    }

}
