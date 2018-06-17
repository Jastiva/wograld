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

import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains a set of {@link ButtonLists} for {@link Location Locations} of
 * {@link Tab Tabs}.
 * @author Andreas Kirschbaum
 */
public class ButtonLists {

    /**
     * The list of buttons for each {@link Location}.
     */
    @NotNull
    private final DoubleButtonList[] buttonLists = new DoubleButtonList[Location.values().length];

    /**
     * Maps button instance to {@link Tab} instance.
     */
    @NotNull
    private final Map<AbstractButton, Tab> tabs = new IdentityHashMap<AbstractButton, Tab>();

    /**
     * Creates a new instance.
     * @param buttonListsListener the listener to notify
     */
    public ButtonLists(@NotNull final ButtonListsListener buttonListsListener) {
        for (final Location location : Location.values()) {
            final DoubleButtonList buttonList = new DoubleButtonList(location);
            buttonLists[location.ordinal()] = buttonList;
            final ButtonListListener buttonListListener = new ButtonListListener() {

                @Override
                public void selectedButtonChanged(@Nullable final AbstractButton prevSelectedButton, @Nullable final AbstractButton selectedButton) {
                    final Tab prevTab = tabs.get(prevSelectedButton);
                    final Tab tab = tabs.get(selectedButton);
                    if (prevTab == tab) {
                        return;
                    }
                    buttonListsListener.tabChanged(prevTab, tab);
                }

            };

            buttonList.addButtonListListener(buttonListListener);
        }
    }

    /**
     * Adds a {@link Tab} to the button list associated with the tab's
     * location.
     * @param tab the tab to add
     * @return the button list the tab was added to
     */
    @NotNull
    public DoubleButtonList addTab(@NotNull final Tab tab) {
        final DoubleButtonList buttonList = buttonLists[tab.getLocation().ordinal()];
        final AbstractButton button = tab.getButton();
        buttonList.addButton(button, tab.isAlternativeLocation());
        tabs.put(button, tab);
        return buttonList;
    }

    /**
     * Moves a {@link Tab} to a new location.
     * @param tab the tab to move
     * @param location the new location
     * @return an array consisting of two button lists the old button from which
     *         the tab has been removed from and the new button list to which
     *         the tab was added to
     */
    @NotNull
    public DoubleButtonList[] moveTab(@NotNull final Tab tab, @NotNull final Location location) {
        if (tabs.get(tab.getButton()) == null) {
            throw new IllegalArgumentException();
        }

        final DoubleButtonList oldButtonList = buttonLists[tab.getLocation().ordinal()];
        final DoubleButtonList newButtonList = buttonLists[location.ordinal()];
        final AbstractButton oldButton = tab.getButton();
        oldButtonList.removeButton(oldButton, tab.isAlternativeLocation());
        oldButton.setSelected(false);
        tab.setLocation(location);
        final AbstractButton newButton = tab.getButton();
        newButtonList.addButton(newButton, tab.isAlternativeLocation());
        tabs.remove(oldButton);
        tabs.put(newButton, tab);
        return new DoubleButtonList[] { oldButtonList, newButtonList, };
    }

    @NotNull
    public DoubleButtonList toggleTabSplitMode(@NotNull final Tab tab) {
        final boolean alternativeLocation = !tab.isAlternativeLocation();
        final DoubleButtonList buttonList = buttonLists[tab.getLocation().ordinal()];
        final AbstractButton oldButton = tab.getButton();
        buttonList.removeButton(oldButton, tab.isAlternativeLocation());
        oldButton.setSelected(false);
        tab.setAlternativeLocation(alternativeLocation);
        final AbstractButton newButton = tab.getButton();
        buttonList.addButton(newButton, tab.isAlternativeLocation());
        tabs.remove(oldButton);
        tabs.put(newButton, tab);
        return buttonList;
    }

    /**
     * Returns the active {@link Tab} on a given {@link Location} of the main
     * view.
     * @param location the location
     * @param alternativeLocation whether the alternative location should be
     * checked
     * @return the tab or <code>null</code> if no active tab exists
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    public Tab getActiveTab(@NotNull final Location location, final boolean alternativeLocation) {
        final DoubleButtonList buttonList = buttonLists[location.ordinal()];
        final AbstractButton selectedButton = buttonList.getSelectedButton(alternativeLocation);
        if (selectedButton == null) {
            return null;
        }

        final Tab tab = tabs.get(selectedButton);
        assert tab != null;
        return tab;
    }

}
