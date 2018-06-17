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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSplitPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} that permanently shows another component and optionally
 * displays a {@link JSplitPane} and one or two other components.
 * @author Andreas Kirschbaum
 */
public class BorderSplitPane extends Container {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The permanently shown {@link Component}.
     * @serial
     */
    @NotNull
    private final Component component;

    /**
     * The {@link Location} of the optional component.
     * @serial
     */
    @NotNull
    private final Location location;

    /**
     * The two optionally shown {@link Component Components}.
     * @serial
     */
    @NotNull
    private final Component[] optionalComponents = new Component[2];

    /**
     * The {@link JSplitPane} to separate {@link #component} and {@link
     * #optionalComponents}.
     * @serial
     */
    @NotNull
    private final JSplitPane splitPane;

    /**
     * The {@link JSplitPane} to separate both elements of {@link
     * #optionalComponents}.
     * @serial
     */
    @NotNull
    private final JSplitPane splitPane2;

    /**
     * Whether this component is visible.
     * @serial
     */
    private boolean showing;

    /**
     * The pending size of {@link #splitPane}. Unless <code>-1</code>, the split
     * pane will be resized to this size when it becomes visible. Unused if
     * {@link #showing} is set.
     * @serial
     */
    private int size = -1;

    /**
     * The pending size of {@link #splitPane2}. Unless <code>-1</code>, the
     * split pane will be resized to this size when it becomes visible. Unused
     * if {@link #showing} is set.
     * @serial
     */
    private int size2;

    /**
     * If set, do not report split pane size changes.
     */
    private boolean ignoreSizeChange;

    /**
     * Creates a new instance.
     * @param component the permanently shown component
     * @param location the location of the optional component
     * @param borderSplitPaneListener the listener to notify
     * @param size2 the initial size or <code>-1</code> for default
     */
    public BorderSplitPane(@NotNull final Component component, @NotNull final Location location, @NotNull final BorderSplitPaneListener borderSplitPaneListener, final int size2) {
        this.component = component;
        this.location = location;
        this.size2 = size2;
        splitPane = new JSplitPane(location.getSplitPaneOrientation(), true, null, null);
        splitPane.setOneTouchExpandable(false);
        splitPane.setResizeWeight(location.getSplitPaneResizeWeight());
        splitPane2 = new JSplitPane(location.getSplitPaneOppositeOrientation(), true, null, null);
        splitPane2.setOneTouchExpandable(false);
        splitPane2.setResizeWeight(0.5);
        setLayout(new BorderLayout());
        setOptionalComponent(null, false, 0, true);

        splitPane.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (ignoreSizeChange) {
                    return;
                }
                if (optionalComponents[0] != null || optionalComponents[1] != null) {
                    //Property names are interned strings.
                    //noinspection StringEquality
                    if (evt.getPropertyName() == JSplitPane.DIVIDER_LOCATION_PROPERTY) {
                        final Object value = evt.getNewValue();
                        if (value != null && value instanceof Integer) {
                            final int newSize = (Integer) value;
                            final int splitPaneSize = getSplitPaneSize(newSize);
                            for (final Component optionalComponent : optionalComponents) {
                                if (optionalComponent != null) {
                                    borderSplitPaneListener.sizeChanged(optionalComponent, splitPaneSize);
                                }
                            }
                        }
                    }
                }
            }

        });

        splitPane2.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (ignoreSizeChange) {
                    return;
                }
                if (optionalComponents[0] != null && optionalComponents[1] != null) {
                    //Property names are interned strings.
                    //noinspection StringEquality
                    if (evt.getPropertyName() == JSplitPane.DIVIDER_LOCATION_PROPERTY) {
                        final Object value = evt.getNewValue();
                        if (value != null && value instanceof Integer) {
                            final int newSize = (Integer) value;
                            BorderSplitPane.this.size2 = newSize;
                            borderSplitPaneListener.size2Changed(newSize);
                        }
                    }
                }
            }

        });

        final HierarchyListener hierarchyListener = new HierarchyListener() {

            @Override
            public void hierarchyChanged(final HierarchyEvent e) {
                if ((e.getChangeFlags() & (long) HierarchyEvent.SHOWING_CHANGED) != 0L && isShowing()) {
                    removeHierarchyListener(this);
                    assert !showing;
                    showing = true;
                    if (size != -1) {
                        setDividerSize(size);
                        size = -1;
                    }
                    setDividerSize2();
                }
            }

        };
        addHierarchyListener(hierarchyListener);
    }

    /**
     * Sets the optional {@link Component}.
     * @param optionalComponent the optional component or <code>null</code> to
     * remove the optional component
     * @param alternativeLocation whether the component should be shown in the
     * alternative location
     * @param size the size of the component; width for left or right location,
     * height for top or bottom location
     */
    public void setOptionalComponent(@Nullable final Component optionalComponent, final boolean alternativeLocation, final int size) {
        setOptionalComponent(optionalComponent, alternativeLocation, size, false);
    }

    /**
     * Sets the optional {@link Component}.
     * @param optionalComponent the optional component or <code>null</code> to
     * remove the optional component
     * @param alternativeLocation whether the component should be shown in the
     * alternative location
     * @param size the size of the component; width for left or right location,
     * height for top or bottom location
     * @param force if set, forcibly rebuild the layout
     */
    public final void setOptionalComponent(@Nullable final Component optionalComponent, final boolean alternativeLocation, final int size, final boolean force) {
        final int index = alternativeLocation ? 1 : 0;
        if (!force && optionalComponents[index] == optionalComponent) {
            return;
        }

        optionalComponents[index] = optionalComponent;

        final Component centerComponent;
        final boolean setSize2;
        if (optionalComponents[0] == null && optionalComponents[1] == null) {
            setSize2 = false;
            centerComponent = component;
        } else {
            centerComponent = splitPane;

            final Component tmp;
            if (optionalComponents[0] == null) {
                setSize2 = false;
                tmp = optionalComponents[1];
                assert tmp != null;
            } else if (optionalComponents[1] == null) {
                setSize2 = false;
                tmp = optionalComponents[0];
                assert tmp != null;
            } else {
                setSize2 = true;
                tmp = splitPane2;
                splitPane2.setTopComponent(optionalComponents[0]);
                splitPane2.setBottomComponent(optionalComponents[1]);
            }

            if (location.isTopOrLeft()) {
                splitPane.setTopComponent(tmp);
                splitPane.setBottomComponent(component);
            } else {
                splitPane.setTopComponent(component);
                splitPane.setBottomComponent(tmp);
            }
        }

        removeAll();
        add(centerComponent, BorderLayout.CENTER);
        ignoreSizeChange = true;
        try {
            validate();
        } finally {
            ignoreSizeChange = false;
        }

        final Component otherComponent = optionalComponents[1 - index];
        if (optionalComponent == null) {
            if (otherComponent == null) {
                // no resize
            } else {
                final Dimension preferredSizeTmp = otherComponent.getPreferredSize();
                final int preferredSize = getSplitPaneSize(preferredSizeTmp == null ? 50 : location.isTopOrBottom() ? preferredSizeTmp.height : preferredSizeTmp.width);
                setDividerSizeInt(preferredSize);
            }
        } else {
            if (otherComponent == null) {
                setDividerSizeInt(size);
            } else {
                final Dimension preferredSizeTmp = otherComponent.getPreferredSize();
                final int preferredSize = getSplitPaneSize(preferredSizeTmp == null ? 50 : location.isTopOrBottom() ? preferredSizeTmp.height : preferredSizeTmp.width);
                setDividerSizeInt(Math.max(size, preferredSize));
            }
        }

        if (setSize2) {
            setDividerSize2();
        }

        validate();
    }

    /**
     * Sets the new size of {@link #splitPane}.
     * @param size the size
     */
    private void setDividerSizeInt(final int size) {
        if (showing) {
            setDividerSize(size);
        } else {
            this.size = size;
        }
    }

    /**
     * Sets the new size of {@link #splitPane}.
     * @param size the size
     */
    private void setDividerSize(final int size) {
        splitPane.setDividerLocation(getSplitPaneSize(size));
    }

    /**
     * Sets the new size of {@link #splitPane2}.
     */
    private void setDividerSize2() {
        if (size2 == -1) {
            return;
        }
        splitPane2.setDividerLocation(size2);
    }

    /**
     * Converts between logical and physical sizes of {@link #splitPane}.
     * @param size the logical or physical size
     * @return the physical or logical size
     */
    private int getSplitPaneSize(final int size) {
        final Dimension preferredSize = getSplitPanePreferredSize();
        if (location.isTopOrBottom()) {
            preferredSize.height = size;
        } else {
            preferredSize.width = size;
        }
        setSplitPanePreferredSize(preferredSize);

        final int result;
        if (location.isTopOrLeft()) {
            result = size;
        } else {
            final int splitPaneSize = (location.isTopOrBottom() ? splitPane.getHeight() : splitPane.getWidth()) - splitPane.getDividerSize();
            result = Math.max(0, splitPaneSize - size);
        }
        return result;
    }

    /**
     * Calculates the preferred size of the {@link #splitPane split pane}.
     * @return the preferred size
     */
    @NotNull
    private Dimension getSplitPanePreferredSize() {
        for (final Component optionalComponent : optionalComponents) {
            if (optionalComponent != null) {
                final Dimension preferredSize = optionalComponent.getPreferredSize();
                if (preferredSize != null) {
                    return preferredSize;
                }
            }
        }

        for (final Component optionalComponent : optionalComponents) {
            if (optionalComponent != null) {
                return optionalComponent.getSize();
            }
        }

        return splitPane.getSize();
    }

    /**
     * Sets the preferred size of all shown optional components.
     * @param preferredSize the preferred size to set
     */
    private void setSplitPanePreferredSize(@NotNull final Dimension preferredSize) {
        for (final Component optionalComponent : optionalComponents) {
            if (optionalComponent != null) {
                optionalComponent.setPreferredSize(preferredSize);
            }
        }
    }

}
