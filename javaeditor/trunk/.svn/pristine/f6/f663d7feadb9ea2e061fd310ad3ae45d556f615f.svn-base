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

package net.sf.gridarta.model.match;

import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Decorates an arbitrary {@link GameObjectMatcher} with a localized name that
 * is suitable for the user interface. That name can be used to for instance for
 * menu item labels that are used on menu items to toggle the
 * <code>GameObjectMatcher</code> on / off.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class NamedGameObjectMatcher implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The edit type of this matcher.
     * @serial
     */
    private final int editType;

    /**
     * The localized name.
     * @serial
     */
    @NotNull
    private final String name;

    /**
     * Whether this matcher is a system matcher. System matchers are not shown
     * in the GUI.
     * @serial
     */
    private final boolean systemMatcher;

    /**
     * The environment {@link GameObjectMatcher}. If non-<code>null</code>, this
     * matcher matches a game object only if inside an environment game object
     * that matches this matcher.
     * @serial
     */
    @Nullable
    private final GameObjectMatcher envGameObjectMatcher;

    /**
     * The {@link GameObjectMatcher} to wrap.
     * @serial
     */
    @NotNull
    private final GameObjectMatcher gameObjectMatcher;

    /**
     * The id of this {@link GameObjectMatcher}.
     * @serial
     */
    @NotNull
    private final String id;

    /**
     * Creates a <code>NamedGameObjectMatcher</code>.
     * @param editType the edit type
     * @param id the ID for this <code>GameObjectMatcher</code>
     * @param name the localized name
     * @param systemMatcher whether this matcher is a system matcher
     * @param envGameObjectMatcher the environment matcher
     * @param gameObjectMatcher the matcher to delegate to
     */
    public NamedGameObjectMatcher(final int editType, @NotNull final String id, @NotNull final String name, final boolean systemMatcher, @Nullable final GameObjectMatcher envGameObjectMatcher, @NotNull final GameObjectMatcher gameObjectMatcher) {
        this.editType = editType;
        this.id = id;
        this.name = name;
        this.systemMatcher = systemMatcher;
        this.envGameObjectMatcher = envGameObjectMatcher;
        this.gameObjectMatcher = gameObjectMatcher;
    }

    /**
     * Gets the name of this <code>NamedGameObjectMatcher</code>.
     * @return the name of this <code>NamedGameObjectMatcher</code>
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        if (envGameObjectMatcher != null) {
            return envGameObjectMatcher.isMatching(gameObject) && isMatchingInventory(gameObject);
        }

        return gameObjectMatcher.isMatching(gameObject);
    }

    /**
     * Returns whether this matcher matches any inventory game object of a given
     * game object. {@link #envGameObjectMatcher} is ignored.
     * @param gameObject the game object to search
     * @return whether a matching inventory game object was found
     * @noinspection TypeMayBeWeakened // is false warning: weakened type would
     * cause compile-errors
     */
    private boolean isMatchingInventory(@NotNull final GameObject<?, ?, ?> gameObject) {
        for (final GameObject<?, ?, ?> inv : gameObject) {
            if (gameObjectMatcher.isMatching(inv)) {
                return true;
            }
            if (isMatchingInventory(inv)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ID of this <code>NamedGameObjectMatcher</code>.
     * @return the ID of this <code>NamedGameObjectMatcher<code>
     */
    @NotNull
    public String getID() {
        return id;
    }

    /**
     * Returns the edit type associated with this matcher. This value is a bit
     * mask and uniquely identifies this matcher.
     * @return the edit type
     */
    public int getEditType() {
        return editType;
    }

    /**
     * Returns whether this matcher is a system matcher.
     * @return whether this matcher is a system matcher
     */
    public boolean isSystemMatcher() {
        return systemMatcher;
    }

}
