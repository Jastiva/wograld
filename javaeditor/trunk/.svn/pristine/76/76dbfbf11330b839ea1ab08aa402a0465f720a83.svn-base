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

package net.sf.gridarta.gui.panel.connectionview;

import java.awt.Component;
import java.util.Collection;
import java.util.TreeSet;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link DefaultListCellRenderer} for rendering {@link Connection} objects in
 * a list.
 * @author Andreas Kirschbaum
 * @noinspection AbstractClassExtendsConcreteClass
 */
public abstract class CellRenderer<K> extends DefaultListCellRenderer {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The StringBuilder used to format entries.
     */
    private final StringBuilder sbForFormat = new StringBuilder();

    /**
     * Used for sorting game object names.
     */
    private final Collection<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates a new instance.
     */
    protected CellRenderer() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        //DefaultListCellRenderer does not use type parameters
        @SuppressWarnings("unchecked")
        final Connection<K> connection = (Connection<K>) value;

        sbForFormat.setLength(0);
        sbForFormat.append(formatKey(connection.getKey()));
        sbForFormat.append(":");

        names.clear();
        for (final BaseObject<?, ?, ?, ?> gameObject : connection) {
            names.add(formatValue(gameObject));
        }
        for (final String name : names) {
            sbForFormat.append(" ");
            sbForFormat.append(name);
        }
        setText(sbForFormat.toString());

        return this;
    }

    /**
     * Returns a string representation for a key.
     * @param key the key
     * @return the string representation
     */
    @NotNull
    protected abstract String formatKey(@NotNull final K key);

    /**
     * Returns a string representation for a value.
     * @param gameObject the value
     * @return the string representation
     */
    @NotNull
    protected abstract String formatValue(@NotNull final BaseObject<?, ?, ?, ?> gameObject);

}
