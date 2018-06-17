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

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.PluginParameter;

public class ParameterDescriptionEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JTextField {

    private static final long serialVersionUID = 1L;

    private final PluginParameter<G, A, R> parameter;

    public ParameterDescriptionEditor(final PluginParameter<G, A, R> param) {
        parameter = param;
        setText(param.getDescription());
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(final DocumentEvent e) {
                change();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                change();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                change();
            }

            private void change() {
                parameter.setDescription(getText());
            }
        });
    }

}
