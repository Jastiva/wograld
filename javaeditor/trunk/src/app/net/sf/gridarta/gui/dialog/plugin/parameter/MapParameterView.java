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
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.plugin.parameter.MapParameter;
import org.jetbrains.annotations.NotNull;

public class MapParameterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements PluginParameterView<G, A, R> {

    @NotNull
    private final MapParameter<G, A, R> parameter;

    @NotNull
    private final JComponent config = new JPanel();

    @NotNull
    private final JComboBox value;

    public MapParameterView(@NotNull final MapParameter<G, A, R> param, @NotNull final MapManager<G, A, R> mapManager) {
        parameter = param;
        value = new JComboBox();
        final ComboBoxModel myModel = new MapParameterComboBoxModel<G, A, R>(mapManager);
        value.setModel(myModel);
        value.setRenderer(new MapParameterCellRenderer(mapManager));
        value.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (value.getSelectedIndex() == 0) {
                        parameter.setValueToCurrent();
                    } else {
                        parameter.setValue(value.getSelectedItem());
                    }
                }
            }
        });
        value.setSelectedItem(parameter.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JComponent getValueComponent() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JComponent getConfigComponent() {
        return config;
    }

}
