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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import net.sf.gridarta.model.direction.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class implements a layout that is similar to {@link
 * java.awt.BorderLayout} but implements those directions used in Daimonin. It
 * provides eight directions. This LayoutManager respects the components
 * preferred size for suggesting a preferred size to the container. This
 * LayoutManager does not respect the components individual sizes when doing the
 * overall layout. The overall layout is solely determined by the container's
 * width and height.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DirectionLayout implements LayoutManager2, Serializable {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The array to quickly find the direction enum constants for a daimonin
     * direction. This must be outside the enum because enum instances are
     * static itself, so referencing this from an enum constructor would be
     * illegal if this were declared inside the enum.
     */
    @NotNull
    private static final Direction[] daimoninToDirectionMap = new Direction[8];

    static {
        addDirection(Direction.NORTH);
        addDirection(Direction.EAST);
        addDirection(Direction.SOUTH);
        addDirection(Direction.WEST);
        addDirection(Direction.NORTH_EAST);
        addDirection(Direction.SOUTH_EAST);
        addDirection(Direction.SOUTH_WEST);
        addDirection(Direction.NORTH_WEST);
    }

    /**
     * Empty dimension as fallback for <code>null</code> components. Will never
     * be modified.
     */
    @NotNull
    private static final Dimension empty = new Dimension(0, 0);

    /**
     * Adds an entry to {@link #daimoninToDirectionMap}.
     * @param direction the entry to add
     */
    private static void addDirection(@NotNull final Direction direction) {
        daimoninToDirectionMap[direction.ordinal()] = direction;
    }

    /**
     * Horizontal Gap.
     * @serial include
     */
    private final int hGap;

    /**
     * Vertical Gap.
     * @serial include
     */
    private final int vGap;

    /**
     * NorthWest Component.
     * @serial include
     */
    @Nullable
    private Component cNW;

    /**
     * North Component.
     * @serial include
     */
    @Nullable
    private Component cN;

    /**
     * NorthEast Component.
     * @serial include
     */
    @Nullable
    private Component cNE;

    /**
     * East Component.
     * @serial include
     */
    @Nullable
    private Component cE;

    /**
     * SouthEast Component.
     * @serial include
     */
    @Nullable
    private Component cSE;

    /**
     * South Component.
     * @serial include
     */
    @Nullable
    private Component cS;

    /**
     * SouthWest Component.
     * @serial include
     */
    @Nullable
    private Component cSW;

    /**
     * West Component.
     * @serial include
     */
    @Nullable
    private Component cW;

    /**
     * Center Component.
     * @serial include
     */
    @Nullable
    private Component cC;

    /**
     * Creates a DirectionLayout with zero gaps.
     */
    public DirectionLayout() {
        this(0, 0);
    }

    /**
     * Creates a DirectionLayout with specified gaps.
     * @param hGap horizontal gap between components
     * @param vGap vertical gap between components
     */
    private DirectionLayout(final int hGap, final int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ChainOfInstanceofChecks")
    @Override
    public void addLayoutComponent(@NotNull final Component comp, @Nullable final Object constraints) {
        if (constraints == null) {
            cC = comp;
            return;
        }
        final Direction direction;
        if (constraints instanceof Integer) {
            direction = getDirectionFromDaimonin((Integer) constraints);
            if (direction == null) {
                throw new IllegalArgumentException("Illegal constraint number: " + constraints);
            }
        } else if (!(constraints instanceof Direction)) {
            throw new IllegalArgumentException("DirectionLayout does not support " + constraints.getClass().getName() + " constraints.");
        } else {
            direction = (Direction) constraints;
        }
        synchronized (comp.getTreeLock()) {
            switch (direction) {
            case NORTH:
                cN = comp;
                break;
            case EAST:
                cE = comp;
                break;
            case SOUTH:
                cS = comp;
                break;
            case WEST:
                cW = comp;
                break;
            case NORTH_EAST:
                cNE = comp;
                break;
            case SOUTH_EAST:
                cSE = comp;
                break;
            case SOUTH_WEST:
                cSW = comp;
                break;
            case NORTH_WEST:
                cNW = comp;
                break;
            default:
                assert false;
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLayoutComponent(@NotNull final String name, @NotNull final Component comp) {
        throw new IllegalArgumentException("DirectionLayout does not support String constraints.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("ObjectEquality")
    public void removeLayoutComponent(@NotNull final Component comp) {
        synchronized (comp.getTreeLock()) {
            if (cC == comp) {
                cC = null;
            } else if (cN == comp) {
                cN = null;
            } else if (cE == comp) {
                cE = null;
            } else if (cS == comp) {
                cS = null;
            } else if (cW == comp) {
                cW = null;
            } else if (cNE == comp) {
                cNE = null;
            } else if (cSE == comp) {
                cSE = null;
            } else if (cSW == comp) {
                cSW = null;
            } else if (cNW == comp) {
                cNW = null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension minimumLayoutSize(@NotNull final Container parent) {
        synchronized (parent.getTreeLock()) {
            final Dimension dim = new Dimension(0, 0); // return dimension
            final Dimension dC = getMinimumSize(cC);
            final Dimension dN = getMinimumSize(cN);
            final Dimension dE = getMinimumSize(cE);
            final Dimension dS = getMinimumSize(cS);
            final Dimension dW = getMinimumSize(cW);
            final Dimension dNE = getMinimumSize(cNE);
            final Dimension dSE = getMinimumSize(cSE);
            final Dimension dSW = getMinimumSize(cSW);
            final Dimension dNW = getMinimumSize(cNW);
            dim.height = vGap * 4 + dNW.height + max(dW.height, dN.height) + max(dSW.height, dC.height, dNE.height) + max(dS.height, dE.height) + dSE.height;
            dim.width = max(hGap * 2 + dSW.width + max(dNW.width, dC.width, dSE.width) + dNE.width, hGap + max(dW.width, dS.width) + max(dN.width, dE.width), max(dNW.width, dSE.width));
            final Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
            return dim;
        }
    }

    /**
     * Returns the minimum size of a component or {@link #empty} for
     * <code>null</code>.
     * @param component the component or <code>null</code>
     * @return the minimum size; must not be modified
     */
    @NotNull
    private static Dimension getMinimumSize(@Nullable final Component component) {
        return component == null ? empty : component.getMinimumSize();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        synchronized (parent.getTreeLock()) {
            final Dimension dim = new Dimension(0, 0); // return dimension
            final Dimension dC = getPreferredSize(cC);
            final Dimension dN = getPreferredSize(cN);
            final Dimension dE = getPreferredSize(cE);
            final Dimension dS = getPreferredSize(cS);
            final Dimension dW = getPreferredSize(cW);
            final Dimension dNE = getPreferredSize(cNE);
            final Dimension dSE = getPreferredSize(cSE);
            final Dimension dSW = getPreferredSize(cSW);
            final Dimension dNW = getPreferredSize(cNW);
            dim.height = vGap * 4 + dNW.height + max(dW.height, dN.height) + max(dSW.height, dC.height, dNE.height) + max(dS.height, dE.height) + dSE.height;
            dim.width = hGap * 2 + dSW.width + max(dNW.width, dC.width, dSE.width) + dNE.width;
            final Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;
            return dim;
        }
    }

    /**
     * Returns the preferred size of a component or {@link #empty} for
     * <code>null</code>.
     * @param component the component or <code>null</code>
     * @return the preferred size; must not be modified
     */
    @NotNull
    private static Dimension getPreferredSize(@Nullable final Component component) {
        return component == null ? empty : component.getMinimumSize();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Dimension maximumLayoutSize(@NotNull final Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getLayoutAlignmentX(@NotNull final Container target) {
        return 0.5f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getLayoutAlignmentY(@NotNull final Container target) {
        return 0.5f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidateLayout(@NotNull final Container target) {
        /* Do Nothing, like BorderLayout. */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layoutContainer(@NotNull final Container parent) {
        synchronized (parent.getTreeLock()) {
            final Dimension size = parent.getSize();
            final Insets padding = parent.getInsets();
            final int innerWidth = size.width - padding.left - padding.right;
            final int innerHeight = size.height - padding.top - padding.bottom;
            int rows = 0;
            //noinspection VariableNotUsedInsideIf
            if (cNW != null) {
                rows++;
            }
            if (cW != null || cN != null) {
                rows++;
            }
            if (cSW != null || cC != null || cNE != null) {
                rows++;
            }
            if (cS != null || cE != null) {
                rows++;
            }
            //noinspection VariableNotUsedInsideIf
            if (cSE != null) {
                rows++;
            }
            if (rows <= 0) {
                return;
            }
            final int colWidth = (innerWidth - hGap * 2) / 3;
            final int rowHeight = (innerHeight - vGap * (rows - 1)) / rows;
            int top = padding.top;
            final int left = padding.left;
            if (cNW != null) {
                cNW.setBounds(left + colWidth + hGap, top, colWidth, rowHeight);
                top += rowHeight + vGap;
            }
            if (cW != null || cN != null) {
                if (cW != null) {
                    cW.setBounds(left + ((colWidth + hGap) / 2), top, colWidth, rowHeight);
                }
                if (cN != null) {
                    cN.setBounds(left + ((colWidth + hGap) * 3 / 2), top, colWidth, rowHeight);
                }
                top += rowHeight + vGap;
            }
            if (cSW != null || cC != null || cNE != null) {
                if (cSW != null) {
                    cSW.setBounds(left, top, colWidth, rowHeight);
                }
                if (cC != null) {
                    cC.setBounds(left + colWidth + hGap, top, colWidth, rowHeight);
                }
                if (cNE != null) {
                    cNE.setBounds(left + ((colWidth + hGap) * 2), top, colWidth, rowHeight);
                }
                top += rowHeight + vGap;
            }
            if (cS != null || cE != null) {
                if (cS != null) {
                    cS.setBounds(left + ((colWidth + hGap) / 2), top, colWidth, rowHeight);
                }
                if (cE != null) {
                    cE.setBounds(left + ((colWidth + hGap) * 3 / 2), top, colWidth, rowHeight);
                }
                top += rowHeight + vGap;
            }
            if (cSE != null) {
                cSE.setBounds(left + colWidth + hGap, top, colWidth, rowHeight);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return getClass().getName() + "[hGap=" + hGap + ",vGap=" + vGap + ']';
    }

    /**
     * Helper method for returning the maximum of an unspecified number of
     * arguments. (The class {@link Math} only provides max methods for 2
     * arguments.
     * @param numbers the numbers to get maximum of
     * @return the maximum of numbers, which is {@link Integer#MIN_VALUE} in
     *         case no number was supplied.
     */
    private static int max(@NotNull final int... numbers) {
        int ret = Integer.MIN_VALUE;
        for (final int number : numbers) {
            if (number > ret) {
                ret = number;
            }
        }
        return ret;
    }

    /**
     * Returns the {@link Direction} for a Daimonin direction.
     * @param daimoninDirection direction from Daimonin
     * @return the direction enum for <var>daimoninDirection</var>
     */
    @NotNull
    private static Direction getDirectionFromDaimonin(final int daimoninDirection) {
        return daimoninToDirectionMap[daimoninDirection];
    }

}
