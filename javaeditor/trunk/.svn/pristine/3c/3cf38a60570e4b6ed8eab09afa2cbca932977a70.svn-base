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

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.gridarta.gui.utils.JFileField;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.MapPathParameter;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PluginParameterView} that displays a {@link MapPathParameter}.
 * @author Andreas Kirschbaum
 */
public class MapPathParameterView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements PluginParameterView<G, A, R> {

    @NotNull
    private final JComponent config = new JPanel();

    /**
     * The button that displays the map path.
     */
    @NotNull
    private final JFileField valueComponent;

    /**
     * Creates a new instance.
     * @param parent the parent component for the file chooser
     * @param parameter the parameter to affect
     */
    public MapPathParameterView(@NotNull final JComponent parent, @NotNull final MapPathParameter<G, A, R> parameter) {
        final String value = parameter.getValue();
        valueComponent = new JFileField(parent, null, parameter.getBaseDir(), new File(value == null ? "" : value), JFileChooser.FILES_AND_DIRECTORIES);
        valueComponent.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(final DocumentEvent e) {
                parameter.setFile(valueComponent.getFile());
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                parameter.setFile(valueComponent.getFile());
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                parameter.setFile(valueComponent.getFile());
            }

        });
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JComponent getValueComponent() {
        return valueComponent;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JComponent getConfigComponent() {
        return config;
    }

} // class MapPathParameterView
