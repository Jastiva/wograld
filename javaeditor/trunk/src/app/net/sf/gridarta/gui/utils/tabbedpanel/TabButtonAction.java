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

package net.sf.gridarta.gui.utils.tabbedpanel;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link javax.swing.Action} for buttons in {@link TabbedPanel
 * TabbedPanels}.
 * @author Andreas Kirschbaum
 */
public class TabButtonAction extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The resource key.
     */
    @NotNull
    private final String ident;

    /**
     * Creates a new instance.
     * @param ident the resource key
     */
    public TabButtonAction(@NotNull final String ident) {
        this.ident = ident;
        putValue(SHORT_DESCRIPTION, ACTION_BUILDER.format("tabButton." + ident + ".shortdescription"));
    }

    /**
     * Sets the accelerator index.
     * @param index the index
     */
    public void setIndex(final int index) {
        if (index >= 0) {
            putValue(NAME, ACTION_BUILDER.format("tabButton." + ident + ".title2", index));
        } else {
            putValue(NAME, ActionBuilderUtils.getString(ACTION_BUILDER, "tabButton." + ident + ".title1"));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

}
