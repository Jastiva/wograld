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

package net.sf.gridarta.model.scripts;

import org.jetbrains.annotations.NotNull;

/**
 * The class <code>UndefinedEventArchetypeException</code> is thrown when an
 * event game object cannot be created.
 * @author Andreas Kirschbaum
 * @noinspection AbstractClassExtendsConcreteClass, AbstractClassWithoutAbstractMethods
 */
public abstract class UndefinedEventArchetypeException extends Exception {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param msg the exception message
     */
    protected UndefinedEventArchetypeException(@NotNull final String msg) {
        super(msg);
    }

    /**
     * Creates a new instance.
     * @param msg the exception message
     * @param cause the cause
     */
    protected UndefinedEventArchetypeException(@NotNull final String msg, @NotNull final Throwable cause) {
        super(msg, cause);
    }

}
