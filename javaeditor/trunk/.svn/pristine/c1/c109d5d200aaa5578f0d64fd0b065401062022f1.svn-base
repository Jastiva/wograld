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

import java.awt.Window;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterViewFactory;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

public class PluginManagerFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The synchronization object.
     */
    private final Object sync = new Object();

    private Window scriptManager;

    private final PluginController<G, A, R> pluginController;

    private final PluginModel<G, A, R> pluginModel;

    private final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory;

    @NotNull
    private final PluginParameterFactory<G, A, R> pluginParameterFactory;

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * Creates a new instance.
     * @param systemIcons the system icons for creating icons
     */
    public PluginManagerFactory(@NotNull final PluginController<G, A, R> pluginController, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, @NotNull final SystemIcons systemIcons) {
        this.pluginController = pluginController;
        this.pluginModel = pluginModel;
        this.pluginParameterViewFactory = pluginParameterViewFactory;
        this.pluginParameterFactory = pluginParameterFactory;
        this.systemIcons = systemIcons;
        ActionUtils.newAction(ACTION_BUILDER, "Plugin", this, "editPlugins");
    }

    @ActionMethod
    public void editPlugins() {
        synchronized (sync) {
            if (scriptManager == null) {
                scriptManager = new PluginManager<G, A, R>(pluginController, pluginModel, pluginParameterViewFactory, pluginParameterFactory, systemIcons);
            }

            scriptManager.pack();
            scriptManager.setVisible(true);
        }
    }

}
