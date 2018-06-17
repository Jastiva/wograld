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

/* Downloaded 02 nov 2004 from
 * http://www.softbear.com/java/mktview/StackLayout.java
 */

package net.sf.gridarta.gui.dialog.plugin;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A layoutManager which stacks components one on top of the other, regardless
 * of their size.
 * @author unknown
 */
public class StackLayout implements LayoutManager {

    /**
     * The vertical gap between components in pixels.
     */
    private final int vGap;

    /**
     * Create a StackLayout.
     * @param vGap vertical gap between components in pixels
     */
    public StackLayout(final int vGap) {
        this.vGap = vGap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayoutComponent(final String name, final Component comp) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        final Insets insets = parent.getInsets();
        final int componentCount = parent.getComponentCount();
        int w = 0;
        int h = 0;

        for (int i = 0; i < componentCount; i++) {
            final Component comp = parent.getComponent(i);
            final Dimension d = comp.getPreferredSize();
            if (w < d.width) {
                w = d.width;
            }
            h += d.height;
            if (i != 0) {
                h += vGap;
            }
        }
        return new Dimension(insets.left + insets.right + w, insets.top + insets.bottom + h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer(final Container parent) {
        final Insets insets = parent.getInsets();
        final int x = insets.left;
        int y = insets.top;
        final int w = preferredLayoutSize(parent).width;

        final int componentCount = parent.getComponentCount();
        for (int i = 0; i < componentCount; ++i) {
            final Component comp = parent.getComponent(i);
            final Dimension d = comp.getPreferredSize();
            comp.setBounds(x, y, w, d.height);
            y += d.height + vGap;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return preferredLayoutSize(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLayoutComponent(final Component comp) {
    }

}
