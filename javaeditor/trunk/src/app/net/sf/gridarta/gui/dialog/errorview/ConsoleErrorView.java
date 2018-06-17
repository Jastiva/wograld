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

package net.sf.gridarta.gui.dialog.errorview;

import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ErrorView} that reports all errors to the console.
 * @author Andreas Kirschbaum
 */
public class ConsoleErrorView implements ErrorView {

    /**
     * Whether errors have been reported.
     */
    private boolean hasErrors;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError(@NotNull final ErrorViewCategory categoryName, @NotNull final String message) {
        hasErrors = true;
        System.err.println("Error: " + categoryName + ": " + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError(@NotNull final ErrorViewCategory categoryName, final int lineNo, @NotNull final String message) {
        hasErrors = true;
        System.err.println("Error: " + categoryName + ":" + lineNo + ": " + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWarning(@NotNull final ErrorViewCategory categoryName, @NotNull final String message) {
        System.err.println(categoryName + ": " + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWarning(@NotNull final ErrorViewCategory categoryName, final int lineNo, @NotNull final String message) {
        System.err.println(categoryName + ":" + lineNo + ": " + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitDialog() {
        // nothing to wait for
    }

}
