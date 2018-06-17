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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.gui.utils.borderpanel.BorderPanel;
import net.sf.gridarta.gui.utils.borderpanel.BorderPanelListener;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} that always displays another component and optionally a
 * number of tabs around it. Each tab has an associated component which is shown
 * while the tab is active.
 * @author Andreas Kirschbaum
 */
public class TabbedPanel extends Container {

    /**
     * The key used to store the preferred height of a tab.
     */
    private static final String TAB_PREFIX = "MainWindow.tab";

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link Preferences}.
     */
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The {@link BorderPanel}.
     * @serial
     */
    @NotNull
    private final BorderPanel borderPanel;

    /**
     * The {@link Tab Tabs} currently shown in {@link #borderPanel}.
     */
    @NotNull
    private final Map<Component, Tab> openTabs = new IdentityHashMap<Component, Tab>();

    /**
     * The {@link ButtonListsListener} attached to {@link #buttonLists}.
     */
    @NotNull
    private final ButtonListsListener buttonListsListener = new ButtonListsListener() {

        @Override
        public void tabChanged(@Nullable final Tab prevTab, @Nullable final Tab tab) {
            if (prevTab != null) {
                close(prevTab);
            }
            if (tab != null) {
                open(tab);
            }
        }

    };

    /**
     * The list of buttons for each {@link Location}.
     */
    @NotNull
    private final ButtonLists buttonLists = new ButtonLists(buttonListsListener);

    /**
     * Creates a new instance.
     * @param centerComponent the component to display in the center location
     */
    public TabbedPanel(@NotNull final Component centerComponent) {
        final BorderPanelListener borderPanelListener = new BorderPanelListener() {

            @Override
            public void sizeChanged(@NotNull final Component component, final int size) {
                final Tab tab = openTabs.get(component);
                if (tab != null) {
                    tab.setSize(size);
                }
            }

            @Override
            public void size2Changed(@NotNull final Location location, final int size2) {
                preferences.putInt(TAB_PREFIX + location.getName() + ".position", size2);
            }

            @Override
            public int getSize2(final Location location) {
                return preferences.getInt(TAB_PREFIX + location.getName() + ".position", -1);
            }

        };
        borderPanel = new BorderPanel(centerComponent, borderPanelListener);
        setLayout(new BorderLayout());
        add(borderPanel, BorderLayout.CENTER);
    }

    /**
     * Adds a tab.
     * @param tab the tab to add
     */
    public void addTab(@NotNull final Tab tab) {
        final DoubleButtonList buttonList = buttonLists.addTab(tab);
        fillContextMenu(tab, true);
        tabAdded(tab, buttonList, tab.isOpen());
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
        return buttonLists.getActiveTab(location, alternativeLocation);
    }

    /**
     * Moves the tab to the given location.
     * @param tab the tab
     * @param location the location
     */
    public void moveTab(@NotNull final Tab tab, @NotNull final Location location) {
        if (tab.getLocation() == location) {
            return;
        }

        final boolean open = tab.isOpen();
        final DoubleButtonList[] result = buttonLists.moveTab(tab, location);
        final DoubleButtonList oldButtonList = result[0];
        final DoubleButtonList newButtonList = result[1];

        fillContextMenu(tab, false);
        tabRemoved(oldButtonList);
        tabAdded(tab, newButtonList, open);
    }

    /**
     * Toggles split mode for the given tab.
     * @param tab the tab
     * @param splitMode whether to enable split mode
     */
    public void setTabSplitMode(@NotNull final Tab tab, final boolean splitMode) {
        if (tab.isAlternativeLocation() == splitMode) {
            return;
        }

        final boolean open = tab.isOpen();
        final DoubleButtonList buttonList = buttonLists.toggleTabSplitMode(tab);
        if (open) {
            buttonList.selectButton(tab.getButton(), tab.isAlternativeLocation());
        }
    }

    /**
     * Called whenever a {@link Tab} has been added to a {@link ButtonList}.
     * Shows the button list component if the first tab has been added.
     * @param tab the tab
     * @param buttonList the button list
     * @param open whether the tab is open
     */
    private void tabAdded(@NotNull final Tab tab, @NotNull final DoubleButtonList buttonList, final boolean open) {
        if (buttonList.getButtonCount() == 1) {
            add(buttonList.getButtons(), tab.getLocation().getBorderLocation());
            validate();
        }

        if (open) {
            buttonList.selectButton(tab.getButton(), tab.isAlternativeLocation());
        }
    }

    /**
     * Called whenever a {@link Tab} has been added from a {@link ButtonList}.
     * Hides the button list component if the last tab has been removed.
     * @param buttonList the button list
     */
    private void tabRemoved(@NotNull final DoubleButtonList buttonList) {
        if (buttonList.getButtonCount() == 0) {
            remove(buttonList.getButtons());
            validate();
        }
    }

    /**
     * Fills in context popup menu entries for a tab in a given location.
     * @param tab the tab
     * @param initialize whether this is the initial call
     */
    private void fillContextMenu(@NotNull final Tab tab, final boolean initialize) {
        final MoveToActions moveToActions = new MoveToActions(tab, this);
        if (initialize) {
            final JPopupMenu popupMenu = tab.getPopupMenu();
            final ToggleAction splitModeAction = (ToggleAction) ACTION_BUILDER.createToggle(false, "tabSplitMode", moveToActions);
            splitModeAction.setSelected(tab.isAlternativeLocation());
            popupMenu.insert(splitModeAction.createCheckBoxMenuItem(), 0);
            tab.setSplitModeAction(splitModeAction);
        }

        final JMenu moveToMenu = tab.getMoveToMenu();
        MenuUtils.removeAll(moveToMenu);
        final Location location = tab.getLocation();
        for (final Location thisLocation : Location.values()) {
            if (thisLocation != location) {
                moveToMenu.add(ACTION_BUILDER.createAction(true, "tabButtonMoveTo" + thisLocation.getName(), moveToActions));
            }
        }
    }

    /**
     * Opens a {@link Tab}.
     * @param tab the tab
     */
    private void open(@NotNull final Tab tab) {
        openTabs.put(tab.getComponent(), tab);
        borderPanel.setComponent(tab.getLocation(), tab.getComponent(), tab.isAlternativeLocation(), tab.getSize());
        tab.setOpen(true);
    }

    /**
     * Closes a {@link Tab}.
     * @param tab the tab
     */
    private void close(@NotNull final Tab tab) {
        closeInt(tab);
        tab.setOpen(false);
    }

    /**
     * Closes a {@link Tab} but does not update the tab's open status.
     * @param tab the tab
     */
    private void closeInt(final Tab tab) {
        borderPanel.unsetComponent(tab.getLocation(), tab.isAlternativeLocation());
        openTabs.remove(tab.getComponent());
    }

}
