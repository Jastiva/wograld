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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import net.sf.gridarta.MainControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link JSplitPane} that keeps its size even upon ancestor layout changes
 * and is restored upon editor restarts. <p> The minimized or maximized state of
 * the oneTouchExpandable buttons cannot be set programatically. Therefore
 * calling {@link JSplitPane#setDividerLocation(int)} does not work reliably.
 * This class attempts to set the minimized/maximized state by calling the
 * {@link ActionListener ActionListeners} attached to these buttons.
 * @author Andreas Kirschbaum
 */
public class GSplitPane extends JSplitPane {

    /**
     * The preferences value for a minimized divider.
     */
    @NotNull
    private static final String MIN = "min";

    /**
     * The preferences value for a maximized divider.
     */
    @NotNull
    private static final String MAX = "max";

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The preferences.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The preferences key for restoring the divider location.
     */
    @NotNull
    private final String preferencesKey;

    /**
     * The {@link ActionListener ActionListeners} attached to the
     * "oneTouchExpandable" buttons of the {@link JSplitPane}. The size if
     * either two or zero; zero if no action listeners could be found. If action
     * listener have been found, index <code>0</code> is the minimize button,
     * index <code>1</code> is the maximize button.
     */
    @NotNull
    private final List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    /**
     * The default divider location. Set to <code>null</code> to not change the
     * divider location.
     */
    @Nullable
    private final String defaultDividerLocation;

    /**
     * The {@link HierarchyListener} for updating the initial divider location.
     */
    @NotNull
    private final HierarchyListener hierarchyListener = new HierarchyListener() {

        @Override
        public void hierarchyChanged(final HierarchyEvent e) {
            if ((e.getChangeFlags() & (long) HierarchyEvent.SHOWING_CHANGED) != 0L && isShowing()) {
                removeHierarchyListener(hierarchyListener);
                if (defaultDividerLocation == null) { // should not happen
                    // ignore
                } else if (defaultDividerLocation.equals(MIN)) {
                    if (!actionListeners.isEmpty()) {
                        actionListeners.get(0).actionPerformed(new ActionEvent(GSplitPane.this, 0, ""));
                    }
                } else if (defaultDividerLocation.equals(MAX)) {
                    if (!actionListeners.isEmpty()) {
                        actionListeners.get(1).actionPerformed(new ActionEvent(GSplitPane.this, 0, ""));
                    }
                } else {
                    try {
                        setDividerLocation(Integer.parseInt(defaultDividerLocation));
                    } catch (final NumberFormatException ignored) {
                        // ignore
                    }
                }
            }
        }
    };

    /**
     * Create a new GSplitPane.
     * @param newOrientation {@link JSplitPane#HORIZONTAL_SPLIT} or {@link
     * JSplitPane#VERTICAL_SPLIT}.
     * @param newLeftComponent the Component that will appear on the left of a
     * horizontally-split pane, or at the top of a vertically-split pane
     * @param newRightComponent the Component that will appear on the right of a
     * horizontally-split pane, or at the bottom of a vertically-split pane
     * @param preferencesKey the preferences key for restoring the divider
     * location
     * @param defaultDividerLocation the default divider location in case none
     * has been saved; <code>-1</code> to not set the divider location if no
     * default divider location has been saved
     */
    public GSplitPane(final int newOrientation, @NotNull final Component newLeftComponent, @NotNull final Component newRightComponent, @NotNull final String preferencesKey, final int defaultDividerLocation) {
        super(newOrientation, newLeftComponent, newRightComponent);
        this.preferencesKey = preferencesKey;
        setOneTouchExpandable(true);
        this.defaultDividerLocation = preferences.get(preferencesKey, defaultDividerLocation == -1 ? null : Integer.toString(defaultDividerLocation));

        // Hacks to circumvent undesired behavior when maximizing/de-maximizing
        // the application's main window: without this code, the minimized or
        // maximized state (oneTouchExpandable) does not stick and cannot be
        // restored when the editor is restarted.
        //
        // This code walks the component hierarchy trying to find the
        // oneTouchExpandable buttons. The contained ActionListeners are
        // replaced with a proxy that shows the left/right component as
        // appropriate for the action.
        setBorder(new EmptyBorder(0, 0, 0, 0));
        for (final Component component : getComponents()) {
            if (component != newLeftComponent && component != newRightComponent) {
                if (component instanceof Container) {
                    final Container container = (Container) component;
                    for (final Component component2 : container.getComponents()) {
                        if (component2 instanceof AbstractButton) {
                            final AbstractButton button = (AbstractButton) component2;
                            for (final ActionListener actionListener : button.getActionListeners()) {
                                if (actionListener.getClass().getName().endsWith("$OneTouchActionHandler")) {
                                    actionListeners.add(actionListener);
                                    button.removeActionListener(actionListener);
                                    if (actionListeners.size() == 1) {
                                        button.addActionListener(new ActionListener() {

                                            @Override
                                            public void actionPerformed(final ActionEvent e) {
                                                getRightComponent().setVisible(true);
                                                actionListener.actionPerformed(e);
                                            }

                                        });
                                    } else {
                                        button.addActionListener(new ActionListener() {

                                            @Override
                                            public void actionPerformed(final ActionEvent e) {
                                                getLeftComponent().setVisible(true);
                                                actionListener.actionPerformed(e);
                                            }

                                        });
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (actionListeners.size() != 2) {
            actionListeners.clear();
        }
        //noinspection VariableNotUsedInsideIf
        if (this.defaultDividerLocation != null) {
            addHierarchyListener(hierarchyListener);
        }
    }

    /**
     * Saves the current divider location into the preferences.
     */
    public void saveLocation() {
        final int dividerLocation = getDividerLocation();
        if (dividerLocation != -1) {
            final String value;
            switch (getState(dividerLocation)) {
            case -1:
                value = MIN;
                break;

            default:
                value = Integer.toString(dividerLocation);
                break;

            case +1:
                value = MAX;
                break;
            }
            preferences.put(preferencesKey, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDividerLocation(final int location) {
        super.setDividerLocation(location);
        final int state = getState(location);
        getLeftComponent().setVisible(state != -1);
        getRightComponent().setVisible(state != +1);
    }

    /**
     * Returns the minimized/maximized state for a given divider location.
     * @param dividerLocation the divider location to get the state of
     * @return the state: <code>-1</code>=minimized, <code>0</code>=normal,
     *         <code>+1</code>=maximized
     */
    private int getState(final int dividerLocation) {
        final Dimension leftSize = leftComponent.getMinimumSize();
        final Dimension rightSize = rightComponent.getMinimumSize();
        final int minLimit = getOrientation() == HORIZONTAL_SPLIT ? leftSize.width : leftSize.height;
        final int maxLimit = getOrientation() == HORIZONTAL_SPLIT ? getWidth() - rightSize.width - getDividerSize() : getHeight() - rightSize.height - getDividerSize();
        if (dividerLocation < minLimit) {
            return -1;
        } else if (dividerLocation > maxLimit) {
            return +1;
        } else {
            return 0;
        }
    }

}
