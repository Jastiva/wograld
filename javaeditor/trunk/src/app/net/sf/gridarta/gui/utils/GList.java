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

package net.sf.gridarta.gui.utils;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.ListModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An extended {@link JList}. It <ul> <li>provides support for per-item tooltips
 * </ul>
 * @author Andreas Kirschbaum
 */
public class GList<T> extends JList {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ToolTipProvider} for per-item tooltips. Set to
     * <code>null</code> to not use per-item tooltips.
     */
    @Nullable
    private ToolTipProvider<T> toolTipProvider;

    /**
     * Creates a new instance.
     * @param dataModel the data model
     */
    public GList(@NotNull final ListModel dataModel) {
        super(dataModel);
    }

    /**
     * Sets or clears the {@link ToolTipProvider}.
     * @param toolTipProvider the tool tip provider to set or <code>null</code>
     * to clear the tool tip provider
     */
    public void setToolTipProvider(@Nullable final ToolTipProvider<T> toolTipProvider) {
        this.toolTipProvider = toolTipProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getToolTipText(@NotNull final MouseEvent event) {
        final int index = locationToIndex(event.getPoint());
        if (index == -1) {
            return super.getToolTipText(event);
        }

        final Rectangle rectangle = getCellBounds(index, index);
        if (!rectangle.contains(event.getPoint())) {
            return super.getToolTipText(event);
        }

        //JList does not use type parameters
        @SuppressWarnings("unchecked")
        final T element = (T) getModel().getElementAt(index);

        if (toolTipProvider == null) {
            return super.getToolTipText(event);
        }
        return toolTipProvider.getToolTipText(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getSelectedValue() {
        //JList does not use type parameters
        @SuppressWarnings("unchecked")
        final T selectedValue = (T) super.getSelectedValue();
        return selectedValue;
    }

}
