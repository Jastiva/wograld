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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.MissingResourceException;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tab in a {@link TabbedPanel} component. It consists of an {@link
 * AbstractButton} to show or hide an associated {@link JComponent} that is
 * displayed when the tab is active. Each tab is associated with a {@link
 * Location}.
 * @author Andreas Kirschbaum
 */
public class Tab {

    /**
     * Additional space before and after the buttons' text.
     */
    private static final int SPACE = 6;

    /**
     * The key used to store the open status of a tab.
     */
    private static final String TAB_OPEN_PREFIX = "MainWindow.tab_open.";

    /**
     * The key used to store the preferred width of a tab.
     */
    private static final String TAB_WIDTH_PREFIX = "MainWindow.tab_width.";

    /**
     * The key used to store the preferred height of a tab.
     */
    private static final String TAB_HEIGHT_PREFIX = "MainWindow.tab_height.";

    /**
     * The key used to store the current location of a tab.
     */
    private static final String TAB_LOCATION = "MainWindow.tab_location.";

    /**
     * The key used to store the current location of a tab.
     */
    private static final String TAB_ALT_LOCATION = "MainWindow.tab_alt_location.";

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
     * The tab's identification string.
     */
    @NotNull
    private final String ident;

    /**
     * The {@link JComponent} that is shown when this tab is active.
     */
    @NotNull
    private final JComponent component;

    /**
     * The {@link AbstractButton} for showing or hiding the {@link JComponent}.
     */
    @NotNull
    private AbstractButton button;

    /**
     * The {@link #button}'s action.
     */
    @NotNull
    private final TabButtonAction action;

    /**
     * The registered {@link TabListener TabListeners} to notify.
     */
    @NotNull
    private final EventListenerList2<TabListener> listeners = new EventListenerList2<TabListener>(TabListener.class);

    /**
     * The button's context menu.
     */
    @NotNull
    private final JPopupMenu popupMenu = ACTION_BUILDER.createPopupMenu(false, "tabButtonMenu");

    /**
     * The "Move To" menu within {@link #popupMenu}.
     */
    @NotNull
    private final JMenu moveToMenu;

    /**
     * The {@link ToggleAction} to update if the "split mode" state changes.
     */
    @Nullable
    private ToggleAction splitModeAction;

    /**
     * The tab's location.
     */
    @NotNull
    private Location location;

    /**
     * Whether the tab is shown in the alternative location (<code>true</code>)
     * or in the standard location (<code>false</code>).
     */
    private boolean alternativeLocation;

    /**
     * The tab's index for ordering.
     */
    private final int index;

    /**
     * The tab's width when in locations {@link Location#LEFT} or {@link
     * Location#RIGHT}.
     */
    private int width;

    /**
     * The tab's height when in locations {@link Location#TOP} or {@link
     * Location#BOTTOM}.
     */
    private int height;

    /**
     * The tab's open status.
     */
    private boolean open;

    /**
     * The tab's {@link Severity}.
     */
    @NotNull
    private Severity severity = Severity.DEFAULT;

    /**
     * The {@link MouseListener} attached to {@link #button} to open the context
     * menu.
     * @noinspection RefusedBequest
     */
    @NotNull
    private final MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mousePressed(@NotNull final MouseEvent e) {
            checkPopup(e);
        }

        @Override
        public void mouseReleased(@NotNull final MouseEvent e) {
            checkPopup(e);
        }

        /**
         * Opens the popup menu if requested.
         * @param e the mouse event
         */
        private void checkPopup(@NotNull final MouseEvent e) {
            if (e.isPopupTrigger()) {
                getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }
        }

    };

    /**
     * Creates a new instance.
     * @param ident the tab's identification string
     * @param component the component that is shown when this tab is active
     * @param defaultLocation the tab's default location
     * @param alternativeLocation whether the tab is shown in the alternative
     * location
     * @param index the tab's index for ordering
     * @param defaultOpen the tab's default opened status
     */
    public Tab(@NotNull final String ident, @NotNull final JComponent component, @NotNull final Location defaultLocation, final boolean alternativeLocation, final int index, final boolean defaultOpen) {
        this.ident = ident;
        this.component = component;
        final Dimension preferredSize = component.getPreferredSize();
        final int defaultWidth = preferences.getInt(TAB_WIDTH_PREFIX + ident, preferredSize.width);
        final int defaultHeight = preferences.getInt(TAB_HEIGHT_PREFIX + ident, preferredSize.height);
        final Dimension minimumSize = component.getMinimumSize();
        width = Math.max(defaultWidth, minimumSize.width);
        height = Math.max(defaultHeight, minimumSize.height);
        open = preferences.getBoolean(TAB_OPEN_PREFIX + ident, defaultOpen);
        action = new TabButtonAction(ident);
        final String locationString = preferences.get(TAB_LOCATION + ident, defaultLocation.toString());
        try {
            location = Location.valueOf(locationString);
        } catch (final IllegalArgumentException ignored) {
            location = defaultLocation;
        }
        this.alternativeLocation = preferences.getBoolean(TAB_ALT_LOCATION + ident, alternativeLocation);
        this.index = index;
        button = createButton();
        final JMenu tmp = MenuUtils.findMenu(popupMenu, "tabButtonMoveTo");
        if (tmp == null) {
            throw new MissingResourceException("missing action ", MenuUtils.class.getName(), "tabButtonMoveTo");
        }
        moveToMenu = tmp;
    }

    /**
     * Creates a button for this tab.
     * @return the button
     */
    @NotNull
    private AbstractButton createButton() {
        final int acceleratorIndex = index < 10 ? index : -1;
        action.setIndex(acceleratorIndex);
        final AbstractButton result = location.createButton((String) action.getValue(Action.NAME));
        result.setMargin(location.isTopOrBottom() ? new Insets(0, SPACE, 0, SPACE) : new Insets(SPACE, 0, SPACE, 0));
        if (acceleratorIndex != -1) {
            result.setMnemonic((int) '0' + index);
        }
        result.addMouseListener(mouseListener);
        return result;
    }

    /**
     * Adds a {@link TabListener} to be notified.
     * @param listener the listener
     */
    public void addTabListener(@NotNull final TabListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link TabListener} to be notified.
     * @param listener the listener
     */
    public void removeTabListener(@NotNull final TabListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the {@link Component} that is shown when this tab is active.
     * @return the component
     */
    @NotNull
    public Component getComponent() {
        return component;
    }

    /**
     * Returns the {@link AbstractButton} for showing or hiding the component.
     * @return the button
     */
    @NotNull
    public AbstractButton getButton() {
        return button;
    }

    /**
     * Returns the button's context menu.
     * @return the menu
     */
    @NotNull
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    /**
     * Returns the "Move To" menu within the button's context menu.
     * @return the "Move To" menu
     */
    @NotNull
    public JMenu getMoveToMenu() {
        return moveToMenu;
    }

    /**
     * Sets the {@link ToggleAction} to update if the "split mode" state
     * changes.
     * @param splitModeAction the toggle action
     */
    public void setSplitModeAction(@Nullable final ToggleAction splitModeAction) {
        this.splitModeAction = splitModeAction;
    }

    /**
     * Returns whether the button is shown in the alternative location.
     * @return whether the button is shown in the alternative location
     */
    public boolean isAlternativeLocation() {
        return alternativeLocation;
    }

    /**
     * Sets whether the button is shown in the alternative location.
     * @param alternativeLocation whether the button is shown in the alternative
     * location
     */
    public void setAlternativeLocation(final boolean alternativeLocation) {
        this.alternativeLocation = alternativeLocation;
        preferences.putBoolean(TAB_ALT_LOCATION + ident, alternativeLocation);
        if (splitModeAction != null) {
            splitModeAction.setSelected(alternativeLocation);
        }
    }

    /**
     * Returns the tab's location.
     * @return the location
     */
    @NotNull
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the tab's location.
     * @param location the location
     */
    public void setLocation(@NotNull final Location location) {
        if (this.location == location) {
            return;
        }

        this.location = location;
        button = createButton();
        preferences.put(TAB_LOCATION + ident, location.toString());
    }

    /**
     * Returns the tab's size. Depending on the tab's location either width or
     * height is returned.
     * @return the size
     */
    public int getSize() {
        return location.isTopOrBottom() ? height : width;
    }

    /**
     * Sets the tab's size.
     * @param size the size; depending on the tab's location either width or
     * height
     */
    public void setSize(final int size) {
        if (location.isTopOrBottom()) {
            height = size;
            preferences.putInt(TAB_HEIGHT_PREFIX + ident, size);
        } else {
            width = size;
            preferences.putInt(TAB_WIDTH_PREFIX + ident, size);
        }
    }

    /**
     * Returns the tab's open status.
     * @return whether the tab is open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets the tab's open status.
     * @param open the tab's open status
     */
    public void setOpen(final boolean open) {
        if (this.open == open) {
            return;
        }

        this.open = open;
        preferences.putBoolean(TAB_OPEN_PREFIX + ident, open);
    }

    /**
     * Returns the tab's {@link Severity}.
     * @return the severity
     */
    @NotNull
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Sets the tab's {@link Severity}.
     * @param severity the severity
     */
    protected void setSeverity(@NotNull final Severity severity) {
        if (this.severity == severity) {
            return;
        }

        this.severity = severity;
        for (final TabListener listener : listeners.getListeners()) {
            listener.severityChanged(severity);
        }
    }

}
