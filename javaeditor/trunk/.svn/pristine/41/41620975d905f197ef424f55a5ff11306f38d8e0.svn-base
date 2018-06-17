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

import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Defines {@link ActionMethod ActionMethods} to move tab locations.
 * @author Andreas Kirschbaum
 */
public class MoveToActions {

    /**
     * The tab.
     */
    @NotNull
    private final Tab tab;

    /**
     * The {@link TabbedPanel} to forward to.
     */
    @NotNull
    private final TabbedPanel tabbedPanel;

    /**
     * Creates a new instance.
     * @param tab the tab
     * @param tabbedPanel the tabbed panel to forward to
     */
    public MoveToActions(@NotNull final Tab tab, @NotNull final TabbedPanel tabbedPanel) {
        this.tab = tab;
        this.tabbedPanel = tabbedPanel;
    }

    /**
     * Action method to move the menu to {@link Location#TOP}.
     */
    @ActionMethod
    public void tabButtonMoveToTop() {
        tabbedPanel.moveTab(tab, Location.TOP);
    }

    /**
     * Action method to move the menu to {@link Location#BOTTOM}.
     */
    @ActionMethod
    public void tabButtonMoveToBottom() {
        tabbedPanel.moveTab(tab, Location.BOTTOM);
    }

    /**
     * Action method to move the menu to {@link Location#LEFT}.
     */
    @ActionMethod
    public void tabButtonMoveToLeft() {
        tabbedPanel.moveTab(tab, Location.LEFT);
    }

    /**
     * Action method to move the menu to {@link Location#RIGHT}.
     */
    @ActionMethod
    public void tabButtonMoveToRight() {
        tabbedPanel.moveTab(tab, Location.RIGHT);
    }

    /**
     * Action method to query split mode.
     * @return whether split mode is enabled
     */
    @ActionMethod
    public boolean isTabSplitMode() {
        return tab.isAlternativeLocation();
    }

    /**
     * Action method set split mode.
     * @param splitMode whether to enable split mode
     */
    @ActionMethod
    public void setTabSplitMode(final boolean splitMode) {
        tabbedPanel.setTabSplitMode(tab, splitMode);
    }

}
