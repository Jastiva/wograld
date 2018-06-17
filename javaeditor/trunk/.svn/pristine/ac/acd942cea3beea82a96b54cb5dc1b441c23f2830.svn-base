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
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Component} that permanently displays another {@link Component} and
 * optionally displays more components on the borders. The layout is similar to
 * {@link java.awt.BorderLayout} but includes {@link javax.swing.JSplitPane
 * JSplitPanes}.
 * @author Andreas Kirschbaum
 */
public class BorderPanel extends Container {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link BorderSplitPane BorderSplitPanes} showing the optional
     * components.
     */
    @NotNull
    private final BorderSplitPane[] splitPanes = new BorderSplitPane[Location.values().length];

    /**
     * Creates a new instance.
     * @param component the permanently shown component
     * @param borderPanelListener the listener to notify
     */
    public BorderPanel(@NotNull final Component component, @NotNull final BorderPanelListener borderPanelListener) {
        Component nextComponent = component;
        for (final Location location : Location.values()) {
            final BorderSplitPaneListener borderSplitPaneListener = new BorderSplitPaneListener() {

                @Override
                public void sizeChanged(@NotNull final Component optionalComponent, final int size) {
                    borderPanelListener.sizeChanged(optionalComponent, size);
                }

                @Override
                public void size2Changed(final int size2) {
                    borderPanelListener.size2Changed(location, size2);
                }

            };
            final BorderSplitPane tmp = new BorderSplitPane(nextComponent, location, borderSplitPaneListener, borderPanelListener.getSize2(location));
            splitPanes[location.ordinal()] = tmp;
            nextComponent = tmp;
        }
        setLayout(new BorderLayout());
        add(nextComponent, BorderLayout.CENTER);
    }

    /**
     * Sets the optional {@link Component} for a location.
     * @param location the location
     * @param component the component
     * @param alternativeLocation whether the component should be shown in the
     * alternative location
     * @param size the size of the component; width or height, depending on
     * <code>location</code>
     * @noinspection TypeMayBeWeakened
     */
    public void setComponent(@NotNull final Location location, @NotNull final Component component, final boolean alternativeLocation, final int size) {
        splitPanes[location.ordinal()].setOptionalComponent(component, alternativeLocation, size);
    }

    /**
     * Unsets the optional {@link Component} for a location.
     * @param location the location
     * @param alternativeLocation whether the component in the alternative
     * location should be unset
     * @noinspection TypeMayBeWeakened
     */
    public void unsetComponent(@NotNull final Location location, final boolean alternativeLocation) {
        splitPanes[location.ordinal()].setOptionalComponent(null, alternativeLocation, 0);
    }

}
