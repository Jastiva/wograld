/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.utils;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import net.sf.japi.swing.action.ActionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class implementing {@link Action} related functions.
 * @author Andreas Kirschbaum
 */
public class ActionUtils {

    /**
     * Action key for the action's category.
     */
    @NotNull
    private static final String CATEGORY = "Category";

    /**
     * Category value for {@link Action Actions} not defining a {@link
     * #CATEGORY}.
     */
    @NotNull
    private static final String UNDEFINED_CATEGORY = "Other";

    /**
     * The shortcut description for actions without shortcuts.
     */
    @NotNull
    public static final String NO_SHORTCUT = "none";

    /**
     * {@link Action} key to store the default shortcut.
     */
    @NotNull
    public static final String DEFAULT_ACCELERATOR_KEY = "DefaultAcceleratorKey";

    /**
     * Private construct to prevent instantiation.
     */
    private ActionUtils() {
    }

    /**
     * Returns the name of an {@link Action}.
     * @param action the action
     * @return the name
     */
    @NotNull
    public static String getActionName(@NotNull final Action action) {
        final String name = getStringValue(action, Action.NAME);
        if (name != null && !name.isEmpty()) {
            return name;
        }

        final String shortDescription = getStringValue(action, Action.SHORT_DESCRIPTION);
        if (shortDescription != null) {
            return shortDescription;
        }

        return getActionId(action);
    }

    /**
     * Returns an {@link Action}'s ID string.
     * @param action the action
     * @return the ID string
     * @throws IllegalArgumentException if the action does not define an ID
     * string
     */
    @NotNull
    public static String getActionId(@NotNull final Action action) {
        final String id = getStringValue(action, ActionBuilder.ACTION_ID);
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return id;
    }

    /**
     * Returns the description for an {@link Action}.
     * @param action the action
     * @return the description
     */
    @NotNull
    public static String getActionDescription(@NotNull final Action action) {
        final String longDescription = getStringValue(action, Action.LONG_DESCRIPTION);
        if (longDescription != null) {
            return longDescription;
        }

        final String shortDescription = getStringValue(action, Action.SHORT_DESCRIPTION);
        if (shortDescription != null) {
            return shortDescription;
        }

        return "";
    }

    /**
     * Returns the shortcut of an {@link Action}.
     * @param action the action
     * @return the shortcut or <code>null</code>
     */
    @Nullable
    public static KeyStroke getShortcut(@NotNull final Action action) {
        return getShortcut(action, Action.ACCELERATOR_KEY);
    }

    /**
     * Returns the alternative shortcut of an {@link Action}.
     * @param action the action
     * @return the alternative shortcut or <code>null</code>
     */
    @Nullable
    public static KeyStroke getAlternativeShortcut(@NotNull final Action action) {
        return getShortcut(action, ActionBuilder.ACCELERATOR_KEY_2);
    }

    /**
     * Returns the shortcut of an {@link Action}.
     * @param action the action
     * @param key the action key to query
     * @return the shortcut or <code>null</code>
     */
    @Nullable
    private static KeyStroke getShortcut(@NotNull final Action action, @NotNull final String key) {
        final Object acceleratorKey = action.getValue(key);
        return acceleratorKey instanceof KeyStroke ? (KeyStroke) acceleratorKey : null;
    }

    /**
     * Sets the shortcut of an {@link Action}.
     * @param action the action
     * @param shortcut the shortcut or <code>null</code> to remove it
     */
    public static void setActionShortcut(@NotNull final Action action, @Nullable final KeyStroke shortcut) {
        action.putValue(Action.ACCELERATOR_KEY, shortcut);
    }

    /**
     * Returns a description of the shortcut of an {@link Action}. Returns
     * {@link #NO_SHORTCUT} if the action defines no shortcut.
     * @param action the action
     * @param key the action key to query
     * @return the description
     */
    @NotNull
    public static String getShortcutDescription(@NotNull final Action action, @NotNull final String key) {
        final KeyStroke shortcut = getShortcut(action, key);
        return shortcut == null ? NO_SHORTCUT : shortcut.toString();
    }

    /**
     * Returns an {@link Icon} associated with the action.
     * @param action the action
     * @return the icon or <code>null</code>
     */
    @Nullable
    public static Icon getActionIcon(@NotNull final Action action) {
        final Object icon = action.getValue(Action.SMALL_ICON);
        return icon instanceof Icon ? (Icon) icon : null;
    }

    /**
     * Returns an {@link Action}'s value as a string.
     * @param action the action
     * @param key the key to query
     * @return the value as a string or <code>null</code> if no such key is
     *         defined of if the value is not a string
     */
    @Nullable
    private static String getStringValue(@NotNull final Action action, @NotNull final String key) {
        final Object value = action.getValue(key);
        return value != null && value instanceof String ? (String) value : null;

    }

    /**
     * Creates new {@link Action} instances. Calls {@link
     * #newAction(ActionBuilder, String, Object, String)} for all keys.
     * @param actionBuilder the action builder to use
     * @param category the category to set
     * @param object the object which defines the action method
     * @param keys the actions' keys
     */
    public static void newActions(@NotNull final ActionBuilder actionBuilder, @NotNull final String category, @NotNull final Object object, @NotNull final String... keys) {
        for (final String key : keys) {
            newAction(actionBuilder, category, object, key);
        }
    }

    /**
     * Creates a new {@link Action} instance. It is basically the same as {@link
     * ActionBuilder#createAction(boolean, String, Object)} except that the
     * category is set.
     * @param actionBuilder the action builder to use
     * @param category the category to set
     * @param object the object which defines the action method
     * @param key the action's key
     * @return the new action
     */
    @NotNull
    public static Action newAction(@NotNull final ActionBuilder actionBuilder, @NotNull final String category, @NotNull final Object object, @NotNull final String key) {
        final Action action = actionBuilder.createAction(true, key, object);
        action.putValue(CATEGORY, category);
        return action;
    }

    /**
     * Returns an {@link Action}'s category.
     * @param action the action
     * @return the category
     */
    @NotNull
    public static String getActionCategory(@NotNull final Action action) {
        final String category = getStringValue(action, CATEGORY);
        return category == null ? UNDEFINED_CATEGORY : category;
    }

}
