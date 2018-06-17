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

package net.sf.gridarta.gui.dialog.plugin.parameter;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.Plugin;
import net.sf.gridarta.plugin.parameter.NoSuchParameterException;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ParameterTypeEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JComboBox {

    private static final long serialVersionUID = 1L;

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ParameterTypeEditor.class);

    private final PluginParameter<G, A, R> parameter;

    private final Plugin<G, A, R> plugin;

    private final ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!parameter.getParameterType().equals(e.getItem())) {
                    try {
                        plugin.convertType(parameter, (String) e.getItem());
                    } catch (final NoSuchParameterException ex) {
                        log.warn("Cannot create parameter for type " + e.getItem());
                    }
                }
            }
        }

    };

    public ParameterTypeEditor(@NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, final Plugin<G, A, R> plugin, final PluginParameter<G, A, R> parameter) {
        super(pluginParameterFactory.getTypes());
        this.parameter = parameter;
        this.plugin = plugin;
        setSelectedItem(parameter.getParameterType());
        addItemListener(itemListener);
    }

}
