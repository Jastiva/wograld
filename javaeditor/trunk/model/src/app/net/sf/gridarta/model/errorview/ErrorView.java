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

package net.sf.gridarta.model.errorview;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for classes displaying error messages.
 * @author Andreas Kirschbaum
 */
public interface ErrorView {

    /**
     * Adds an error message.
     * @param categoryName the error category
     * @param message the error message
     */
    void addError(@NotNull ErrorViewCategory categoryName, @NotNull String message);

    /**
     * Adds an error message.
     * @param categoryName the error category
     * @param lineNo the line number of the error message
     * @param message the error message
     */
    void addError(@NotNull ErrorViewCategory categoryName, int lineNo, @NotNull String message);

    /**
     * Adds a warning message.
     * @param categoryName the warning category
     * @param message the warning message
     */
    void addWarning(@NotNull ErrorViewCategory categoryName, @NotNull String message);

    /**
     * Adds a warning message.
     * @param categoryName the warning category
     * @param lineNo the line number of the error message
     * @param message the warning message
     */
    void addWarning(@NotNull ErrorViewCategory categoryName, int lineNo, @NotNull String message);

    /**
     * Whether at least one error message has been added.
     * @return whether error exist
     */
    boolean hasErrors();

    /**
     * Waits until the dialog has been dismissed.
     * @throws InterruptedException if the current thread has been interrupted
     * while waiting for the dialog to close
     */
    void waitDialog() throws InterruptedException;

}
