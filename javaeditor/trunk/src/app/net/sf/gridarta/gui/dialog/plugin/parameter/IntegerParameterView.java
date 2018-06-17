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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.IntegerParameter;
import org.jetbrains.annotations.NotNull;

public class IntegerParameterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements PluginParameterView<G, A, R>, ActionListener {

    @NotNull
    private final JSpinner value;

    @NotNull
    private final AbstractButton config;

    @NotNull
    private final IntegerParameter<G, A, R> parameter;

    public IntegerParameterView(@NotNull final IntegerParameter<G, A, R> parameter) {
        this.parameter = parameter;
        final SpinnerModel mdl = new SpinnerNumberModel(0, parameter.getMin(), parameter.getMax(), 1);
        value = new TooltipSpinner(mdl);
        try {
            value.setValue(parameter.getValue());
        } catch (final Exception ignored) {
        }
        value.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                IntegerParameterView.this.parameter.setValue((Integer) ((SpinnerNumberModel) value.getModel()).getNumber());
            }
        });
        config = new JButton("...");
        config.setBorderPainted(false);
        config.setActionCommand("Config");
        config.addActionListener(this);
        updateTooltip();
    }

    @NotNull
    @Override
    public JComponent getConfigComponent() {
        return config;
    }

    @NotNull
    @Override
    public JComponent getValueComponent() {
        return value;
    }

    private void updateTooltip() {
        final String toolTip = "[" + Integer.toString(parameter.getMin()) + "," + Integer.toString(parameter.getMax()) + "]";
        config.setToolTipText(toolTip);
        value.setToolTipText(toolTip);
    }

    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (e.getActionCommand().equals("Config")) {
            final String min = JOptionPane.showInputDialog("Minimum value:", parameter.getMin());
            final String max = JOptionPane.showInputDialog("Maximum value:", parameter.getMax());
            try {
                parameter.setMax(Integer.parseInt(max));
                parameter.setMin(Integer.parseInt(min));
                ((SpinnerNumberModel) value.getModel()).setMinimum(parameter.getMin());
                ((SpinnerNumberModel) value.getModel()).setMaximum(parameter.getMax());
                updateTooltip();
            } catch (final Exception ex) {
                JOptionPane.showMessageDialog(null, "Could not change integer configuration");
            }
        }
    }

}
