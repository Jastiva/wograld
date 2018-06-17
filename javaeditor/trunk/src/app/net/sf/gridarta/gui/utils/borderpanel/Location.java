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

package net.sf.gridarta.gui.utils.borderpanel;

import java.awt.BorderLayout;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import org.jetbrains.annotations.NotNull;

/**
 * A location.
 * @author Andreas Kirschbaum
 */
public enum Location {

    /**
     * The location on the top side.
     */
    TOP {
        @NotNull
        @Override
        public AbstractButton createButton(@NotNull final String title) {
            return new JToggleButton(title);
        }

        @NotNull
        @Override
        public String getBorderLocation() {
            return BorderLayout.NORTH;
        }

        @NotNull
        @Override
        public String getBorderLocationStandardLocation() {
            return BorderLayout.WEST;
        }

        @NotNull
        @Override
        public String getBorderLocationAlternativeLocation() {
            return BorderLayout.EAST;
        }

        @Override
        public int getAxis() {
            return BoxLayout.X_AXIS;
        }

        @Override
        public int getSplitPaneOrientation() {
            return JSplitPane.VERTICAL_SPLIT;
        }

        @Override
        public int getSplitPaneOppositeOrientation() {
            return JSplitPane.HORIZONTAL_SPLIT;
        }

        @Override
        public double getSplitPaneResizeWeight() {
            return 0.0;
        }

        @Override
        public boolean isTopOrLeft() {
            return true;
        }

        @Override
        public boolean isTopOrBottom() {
            return true;
        }

        @NotNull
        @Override
        public String getName() {
            return "Top";
        }

    },

    /**
     * The location on the bottom side.
     */
    BOTTOM {
        @NotNull
        @Override
        public AbstractButton createButton(@NotNull final String title) {
            return new JToggleButton(title);
        }

        @NotNull
        @Override
        public String getBorderLocation() {
            return BorderLayout.SOUTH;
        }

        @NotNull
        @Override
        public String getBorderLocationStandardLocation() {
            return BorderLayout.WEST;
        }

        @NotNull
        @Override
        public String getBorderLocationAlternativeLocation() {
            return BorderLayout.EAST;
        }

        @Override
        public int getAxis() {
            return BoxLayout.X_AXIS;
        }

        @Override
        public int getSplitPaneOrientation() {
            return JSplitPane.VERTICAL_SPLIT;
        }

        @Override
        public int getSplitPaneOppositeOrientation() {
            return JSplitPane.HORIZONTAL_SPLIT;
        }

        @Override
        public double getSplitPaneResizeWeight() {
            return 1.0;
        }

        @Override
        public boolean isTopOrLeft() {
            return false;
        }

        @Override
        public boolean isTopOrBottom() {
            return true;
        }

        @NotNull
        @Override
        public String getName() {
            return "Bottom";
        }

    },

    /**
     * The location on the left side.
     */
    LEFT {
        @NotNull
        @Override
        public AbstractButton createButton(@NotNull final String title) {
            return new VerticalToggleButton(title, false);
        }

        @NotNull
        @Override
        public String getBorderLocation() {
            return BorderLayout.WEST;
        }

        @NotNull
        @Override
        public String getBorderLocationStandardLocation() {
            return BorderLayout.NORTH;
        }

        @NotNull
        @Override
        public String getBorderLocationAlternativeLocation() {
            return BorderLayout.SOUTH;
        }

        @Override
        public int getAxis() {
            return BoxLayout.Y_AXIS;
        }

        @Override
        public int getSplitPaneOrientation() {
            return JSplitPane.HORIZONTAL_SPLIT;
        }

        @Override
        public int getSplitPaneOppositeOrientation() {
            return JSplitPane.VERTICAL_SPLIT;
        }

        @Override
        public double getSplitPaneResizeWeight() {
            return 0.0;
        }

        @Override
        public boolean isTopOrLeft() {
            return true;
        }

        @Override
        public boolean isTopOrBottom() {
            return false;
        }

        @NotNull
        @Override
        public String getName() {
            return "Left";
        }

    },

    /**
     * The location on the right side.
     */
    RIGHT {
        @NotNull
        @Override
        public AbstractButton createButton(@NotNull final String title) {
            return new VerticalToggleButton(title, true);
        }

        @NotNull
        @Override
        public String getBorderLocation() {
            return BorderLayout.EAST;
        }

        @NotNull
        @Override
        public String getBorderLocationStandardLocation() {
            return BorderLayout.NORTH;
        }

        @NotNull
        @Override
        public String getBorderLocationAlternativeLocation() {
            return BorderLayout.SOUTH;
        }

        @Override
        public int getAxis() {
            return BoxLayout.Y_AXIS;
        }

        @Override
        public int getSplitPaneOrientation() {
            return JSplitPane.HORIZONTAL_SPLIT;
        }

        @Override
        public int getSplitPaneOppositeOrientation() {
            return JSplitPane.VERTICAL_SPLIT;
        }

        @Override
        public double getSplitPaneResizeWeight() {
            return 1.0;
        }

        @Override
        public boolean isTopOrLeft() {
            return false;
        }

        @Override
        public boolean isTopOrBottom() {
            return false;
        }

        @NotNull
        @Override
        public String getName() {
            return "Right";
        }

    };

    /**
     * Creates an {@link AbstractButton} for this location.
     * @param title the button's title
     * @return the button
     */
    @NotNull
    public abstract AbstractButton createButton(@NotNull final String title);

    /**
     * Returns the {@link BorderLayout} constraint of this location.
     * @return the constraint
     */
    @NotNull
    public abstract String getBorderLocation();

    /**
     * Returns the {@link BorderLayout} constraint of this location for buttons
     * in the standard location.
     * @return the constraint
     */
    @NotNull
    public abstract String getBorderLocationStandardLocation();

    /**
     * Returns the {@link BorderLayout} constraint of this location for buttons
     * in the alternative location.
     * @return the constraint
     */
    @NotNull
    public abstract String getBorderLocationAlternativeLocation();

    /**
     * Returns the {@link BoxLayout} axis of this location.
     * @return the axis
     */
    public abstract int getAxis();

    /**
     * Returns the orientation for a {@link JSplitPane}.
     * @return the orientation
     */
    public abstract int getSplitPaneOrientation();

    /**
     * Returns the opposite orientation for a {@link JSplitPane}.
     * @return the opposite orientation
     */
    public abstract int getSplitPaneOppositeOrientation();

    /**
     * Returns the resize weight for a {@link JSplitPane}.
     * @return the resize weight
     */
    public abstract double getSplitPaneResizeWeight();

    /**
     * Returns whether this location is {@link #TOP} or {@link #LEFT}.
     * @return whether this location is top or left
     */
    public abstract boolean isTopOrLeft();

    /**
     * Returns whether this location is {@link #TOP} or {@link #BOTTOM}.
     * @return whether this location is top or bottom
     */
    public abstract boolean isTopOrBottom();

    /**
     * Returns a name for building resource keys.
     * @return the name
     */
    @NotNull
    public abstract String getName();

}
