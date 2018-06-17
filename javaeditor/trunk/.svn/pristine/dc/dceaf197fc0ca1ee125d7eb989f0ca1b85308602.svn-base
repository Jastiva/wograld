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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.BooleanParameter;
import org.jetbrains.annotations.NotNull;

public class BooleanParameterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements PluginParameterView<G, A, R>, ActionListener {

    @NotNull
    private final AbstractButton value;

    @NotNull
    private final AbstractButton config;

    @NotNull
    private final BooleanParameter<G, A, R> parameter;

    public BooleanParameterView(@NotNull final BooleanParameter<G, A, R> param) {
        parameter = param;
        value = new JCheckBox();
        value.setActionCommand("toggle");
        value.addActionListener(this);
        config = new JButton("...");
        config.setBorderPainted(false);
        config.setActionCommand("config");
        config.addActionListener(this);
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

    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (e.getActionCommand().equals("toggle")) {
            if (value.isSelected()) {
                parameter.setValue(Boolean.TRUE);
                value.setText(parameter.getTrueText());
            } else {
                parameter.setValue(Boolean.FALSE);
                value.setText(parameter.getFalseText());
            }
        }
        if (e.getActionCommand().equals("config")) {
            final String yes = JOptionPane.showInputDialog("Checked text", parameter.getTrueText());
            final String no = JOptionPane.showInputDialog("Unchecked text", parameter.getFalseText());
            parameter.setTrueText(yes);
            parameter.setFalseText(no);
        }
    }

}
