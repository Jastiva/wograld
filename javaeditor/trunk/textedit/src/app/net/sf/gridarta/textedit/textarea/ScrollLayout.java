/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.textedit.textarea;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScrollLayout implements LayoutManager {

    public static final String CENTER = "center";

    public static final String RIGHT = "right";

    public static final String BOTTOM = "bottom";

    /**
     * Adding components with this name to the text area will place them left of
     * the horizontal scroll bar. In jEdit, the status bar is added this way.
     */
    private static final String LEFT_OF_SCROLL_BAR = "los";

    @Nullable
    private Component center;

    @Nullable
    private Component right;

    @Nullable
    private Component bottom;

    @NotNull
    private final Collection<Component> leftOfScrollBar = new ArrayList<Component>();

    @NotNull
    private final Container textArea;

    public ScrollLayout(@NotNull final Container textArea) {
        this.textArea = textArea;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayoutComponent(@NotNull final String name, @NotNull final Component comp) {
        if (name.equals(CENTER)) {
            center = comp;
        } else if (name.equals(RIGHT)) {
            right = comp;
        } else if (name.equals(BOTTOM)) {
            bottom = comp;
        } else if (name.equals(LEFT_OF_SCROLL_BAR)) {
            leftOfScrollBar.add(comp);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLayoutComponent(@NotNull final Component comp) {
        if (center == comp) {
            center = null;
        }

        if (right == comp) {
            right = null;
        }

        if (bottom == comp) {
            bottom = null;
        } else {
            leftOfScrollBar.remove(comp);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension preferredLayoutSize(@NotNull final Container parent) {
        final Dimension dim = new Dimension();
        final Insets insets = textArea.getInsets();
        dim.width = insets.left + insets.right;
        dim.height = insets.top + insets.bottom;

        if (center != null) {
            final Dimension centerPref = center.getPreferredSize();
            dim.width += centerPref.width;
            dim.height += centerPref.height;
        }
        if (right != null) {
            final Dimension rightPref = right.getPreferredSize();
            dim.width += rightPref.width;
        }
        if (bottom != null) {
            final Dimension bottomPref = bottom.getPreferredSize();
            dim.height += bottomPref.height;
        }

        return dim;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension minimumLayoutSize(@NotNull final Container parent) {
        final Dimension dim = new Dimension();
        final Insets insets = textArea.getInsets();
        dim.width = insets.left + insets.right;
        dim.height = insets.top + insets.bottom;

        if (center != null) {
            final Dimension centerPref = center.getMinimumSize();
            dim.width += centerPref.width;
            dim.height += centerPref.height;
        }
        if (right != null) {
            final Dimension rightPref = right.getMinimumSize();
            dim.width += rightPref.width;
        }
        if (bottom != null) {
            final Dimension bottomPref = bottom.getMinimumSize();
            dim.height += bottomPref.height;
        }

        return dim;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer(@NotNull final Container parent) {
        final Dimension size = parent.getSize();
        final Insets insets = parent.getInsets();
        final int insetsTop = insets.top;
        int insetsLeft = insets.left;
        final int insetsBottom = insets.bottom;
        final int insetsRight = insets.right;

        final int rightWidth = right != null ? right.getPreferredSize().width : 0;
        final int bottomHeight = bottom != null ? bottom.getPreferredSize().height : 0;
        final int centerWidth = size.width - rightWidth - insetsLeft - insetsRight;
        final int centerHeight = size.height - bottomHeight - insetsTop - insetsBottom;

        if (center != null) {
            center.setBounds(insetsLeft, insetsTop, centerWidth, centerHeight);
        }

        if (right != null) {
            right.setBounds(insetsLeft + centerWidth, insetsTop, rightWidth, centerHeight);
        }

        // Lay out all status components, in order
        for (final Component comp : leftOfScrollBar) {
            final Dimension dim = comp.getPreferredSize();
            comp.setBounds(insetsLeft, insetsTop + centerHeight, dim.width, bottomHeight);
            insetsLeft += dim.width;
        }

        if (bottom != null) {
            bottom.setBounds(insetsLeft, insetsTop + centerHeight, size.width - rightWidth - insetsLeft - insetsRight, bottomHeight);
        }
    }

}
