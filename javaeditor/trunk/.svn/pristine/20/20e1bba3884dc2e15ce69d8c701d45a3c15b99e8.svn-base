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

package net.sf.gridarta.gui.dialog.shortcuts;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.DummyAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manager for shortcuts of all {@link Action Actions} in an {@link
 * ActionBuilder} instance.
 * @author Andreas Kirschbaum
 */
public class ShortcutsManager implements Iterable<Action> {

    /**
     * The prefix for preferences keys for shortcuts.
     */
    @NotNull
    private static final String PREFERENCES_SHORTCUT_PREFIX = "shortcut.";

    /**
     * The prefix for preferences keys for shortcut comments.
     */
    @NotNull
    private static final String PREFERENCES_COMMENT_PREFIX = "prefs." + PREFERENCES_SHORTCUT_PREFIX;

    /**
     * The action names to hide.
     */
    @NotNull
    private final Collection<String> ignoreActions = new HashSet<String>();

    /**
     * A {@link Comparator} that compares {@link Action Actions} by name.
     */
    @NotNull
    private static final Comparator<Action> actionComparator = new Comparator<Action>() {

        @Override
        public int compare(@NotNull final Action o1, @NotNull final Action o2) {
            return ActionUtils.getActionName(o1).compareToIgnoreCase(ActionUtils.getActionName(o2));
        }

    };

    /**
     * The managed {@link ActionBuilder}.
     */
    @NotNull
    private final ActionBuilder actionBuilder;

    /**
     * The {@link Preferences} for storing/restoring shortcuts.
     */
    @NotNull
    private final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * Creates a new instance.
     * @param actionBuilder the action builder to manage
     */
    public ShortcutsManager(@NotNull final ActionBuilder actionBuilder) {
        this.actionBuilder = actionBuilder;

        ignoreActions.addAll(Arrays.asList(StringUtils.PATTERN_SPACES.split(ActionBuilderUtils.getString(actionBuilder, "shortcutsIgnoreActions"), 0)));

        for (final Action action : this) {
            final Object acceleratorKey = action.getValue(Action.ACCELERATOR_KEY);
            if (acceleratorKey != null) {
                action.putValue(ActionUtils.DEFAULT_ACCELERATOR_KEY, acceleratorKey);
            }
        }

        final Preferences commentPreferences = new AbstractPreferences(null, "") {

            @Override
            protected void putSpi(final String key, final String value) {
                throw new UnsupportedOperationException();
            }

            @Nullable
            @Override
            protected String getSpi(@NotNull final String key) {
                if (!key.startsWith(PREFERENCES_COMMENT_PREFIX)) {
                    return null;
                }

                final String actionKey = key.substring(PREFERENCES_COMMENT_PREFIX.length());
                final Action action = actionBuilder.getAction(actionKey);
                if (action == null || !isValidAction(action)) {
                    return null;
                }

                return actionBuilder.format("prefs.shortcut", ActionUtils.getActionName(action));
            }

            @Override
            protected void removeSpi(final String key) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void removeNodeSpi() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected String[] keysSpi() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected String[] childrenNamesSpi() {
                throw new UnsupportedOperationException();
            }

            @Override
            protected AbstractPreferences childSpi(final String name) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void syncSpi() {
                // ignore
            }

            @Override
            protected void flushSpi() {
                // ignore
            }

        };
        actionBuilder.addPref(commentPreferences);
    }

    /**
     * Restores all shortcuts from the preferences.
     */
    public void loadShortcuts() {
        for (final Action action : this) {
            final String value = preferences.get(PREFERENCES_SHORTCUT_PREFIX + ActionUtils.getActionId(action), null);
            if (value != null) {
                if (value.equals(ActionUtils.NO_SHORTCUT)) {
                    action.putValue(Action.ACCELERATOR_KEY, null);
                } else {
                    final Object acceleratorKey = KeyStroke.getKeyStroke(value);
                    if (acceleratorKey != null) {
                        action.putValue(Action.ACCELERATOR_KEY, acceleratorKey);
                    }
                }
            }
        }
    }

    /**
     * Saves all shortcuts to the preferences.
     */
    public void saveShortcuts() {
        try {
            for (final String key : preferences.keys()) {
                if (key.startsWith(PREFERENCES_SHORTCUT_PREFIX)) {
                    preferences.remove(key);
                }
            }
        } catch (final BackingStoreException ignored) {
            // ignore: just keep excess keys
        }

        for (final Action action : this) {
            final String acceleratorKey = ActionUtils.getShortcutDescription(action, Action.ACCELERATOR_KEY);
            final String defaultAcceleratorKey = ActionUtils.getShortcutDescription(action, ActionUtils.DEFAULT_ACCELERATOR_KEY);
            final String preferencesKey = PREFERENCES_SHORTCUT_PREFIX + ActionUtils.getActionId(action);
            if (acceleratorKey.equals(defaultAcceleratorKey)) {
                preferences.remove(preferencesKey);
            } else {
                preferences.put(preferencesKey, acceleratorKey);
            }
        }
    }

    /**
     * Displays a dialog to edit shortcuts.
     * @param parentComponent the parent component for the dialog
     */
    public void showShortcutsDialog(@NotNull final Component parentComponent) {
        final ShortcutsDialog shortcutsDialog = new ShortcutsDialog(parentComponent, this);
        shortcutsDialog.showDialog(parentComponent);
    }

    /**
     * Reverts all shortcuts to their default values.
     */
    public void revertAll() {
        for (final Action action : this) {
            action.putValue(Action.ACCELERATOR_KEY, action.getValue(ActionUtils.DEFAULT_ACCELERATOR_KEY));
        }
    }

    /**
     * Returns all {@link Action Actions}.
     * @return an iterator returning the actions
     */
    @Override
    public Iterator<Action> iterator() {
        final List<Action> result = new ArrayList<Action>();
        final ActionMap actionMap = actionBuilder.getActionMap();
        for (final Object key : actionMap.allKeys()) {
            if (key instanceof String) {
                final Action action = actionMap.get(key);
                assert action != null;
                if (isValidAction(action)) {
                    result.add(action);
                }
            }
        }
        Collections.sort(result, actionComparator);
        return result.iterator();
    }

    /**
     * Returns whether an {@link Action} is a global action.
     * @param action the action to check
     * @return whether the action is valid
     */
    private boolean isValidAction(@NotNull final Action action) {
        if (action instanceof DummyAction) {
            return false;
        }

        final String id;
        try {
            id = ActionUtils.getActionId(action);
        } catch (final IllegalArgumentException ignored) {
            return false;
        }

        return !ignoreActions.contains(id);
    }

}
