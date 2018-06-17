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

package net.sf.gridarta.model.gameobject;

import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This exception is thrown in case a method of a {@link GameObject} without a
 * container or the wrong container was invoked when a GameObject requires a
 * container.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @noinspection UncheckedExceptionClass
 */
public class NotInsideContainerException extends IllegalStateException {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The expected container if != null or null if expected to be in any
     * container.
     * @serial
     */
    @Nullable
    private final GameObjectContainer<?, ?, ?> container;

    /**
     * The {@link GameObject} that was not inside a / the container but should
     * have been.
     * @serial
     */
    @NotNull
    private final BaseObject<?, ?, ?, ?> item;

    /**
     * Create a NotInsideContainerException.
     * @param item GameObject that was not inside a container but should have
     * been.
     */
    public NotInsideContainerException(@NotNull final GameObject<?, ?, ?> item) {
        this(null, item);
    }

    /**
     * Create a NotInsideContainerException.
     * @param container Container that was queried or <code>null</code> if a
     * container was expected but not found.
     * @param item GameObject that was not inside a container but should have
     * been.
     */
    public NotInsideContainerException(@Nullable final GameObjectContainer<?, ?, ?> container, @NotNull final GameObject<?, ?, ?> item) {
        super(container == null ? item.toString() + " was expected to be inside a container but wasn't." : item.toString() + " was expected to be inside " + container + " but was in " + item.getContainer());
        this.container = container;
        this.item = item;
    }

    /**
     * Returns the GameObject that was not inside a container but should have
     * been.
     * @return the game object that was not inside a container but should have
     *         been
     */
    @NotNull
    public BaseObject<?, ?, ?, ?> getItem() {
        return item;
    }

    /**
     * Returns the Container that the GameObject was expected in.
     * @return the container the game object was expected in or
     *         <code>null</code> if it was expected to have any container but
     *         had not
     */
    @Nullable
    public GameObjectContainer<?, ?, ?> getContainer() {
        return container;
    }

}
