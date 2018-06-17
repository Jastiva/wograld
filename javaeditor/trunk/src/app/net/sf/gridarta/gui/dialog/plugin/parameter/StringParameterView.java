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

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.AbstractPluginParameter;
import org.jetbrains.annotations.NotNull;

public class StringParameterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements PluginParameterView<G, A, R> {

    @NotNull
    private final JTextComponent value = new JTextField();

    @NotNull
    private final JComponent config = new JPanel();

    @NotNull
    private final AbstractPluginParameter<G, A, R, String> linkedParameter;

    public StringParameterView(@NotNull final AbstractPluginParameter<G, A, R, String> parameter) {
        linkedParameter = parameter;
        final Document d = value.getDocument();
        d.addDocumentListener(new DocumentListener() {

            private void doIt() {
                linkedParameter.setValue(value.getText());
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                doIt();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                doIt();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                doIt();
            }
        });
    }

    @NotNull
    @Override
    public JComponent getConfigComponent() {
        return config;
    }

    @NotNull
    @Override
    public JComponent getValueComponent() {
        value.setText(linkedParameter.getValue());
        return value;
    }

}
