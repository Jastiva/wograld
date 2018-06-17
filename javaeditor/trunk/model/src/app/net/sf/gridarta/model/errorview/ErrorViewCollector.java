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

import java.io.File;
import java.net.URL;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for adding messages to a {@link ErrorView} instance using a
 * fixed category name.
 * @author Andreas Kirschbaum
 */
public class ErrorViewCollector {

    /**
     * The {@link ErrorView} to add to.
     */
    @NotNull
    private final ErrorView errorView;

    /**
     * The file being read.
     */
    @NotNull
    private final String file;

    /**
     * Creates a new instance.
     * @param errorView the error view to add to
     * @param file the file being read
     */
    public ErrorViewCollector(@NotNull final ErrorView errorView, @NotNull final File file) {
        this.errorView = errorView;
        this.file = file.getPath();
    }

    /**
     * Creates a new instance.
     * @param errorView the error view to add to
     * @param url the URL being read
     */
    public ErrorViewCollector(@NotNull final ErrorView errorView, @NotNull final URL url) {
        this(errorView, new File(url.toString()));
    }

    /**
     * Adds a warning message.
     * @param category the category
     */
    public void addWarning(@NotNull final ErrorViewCategory category) {
        errorView.addWarning(category, file);
    }

    /**
     * Adds a warning message.
     * @param category the category
     * @param message the message
     */
    public void addWarning(@NotNull final ErrorViewCategory category, @NotNull final String message) {
        errorView.addWarning(category, file + ": " + message);
    }

    /**
     * Adds a warning message.
     * @param category the category
     * @param lineNo the line number
     * @param message the message
     */
    public void addWarning(@NotNull final ErrorViewCategory category, final int lineNo, @NotNull final String message) {
        errorView.addWarning(category, lineNo, file + ": " + message);
    }

    /**
     * Adds an error message.
     * @param category the category
     */
    public void addError(@NotNull final ErrorViewCategory category) {
        errorView.addError(category, file);
    }

    /**
     * Adds an error message.
     * @param category the category
     * @param message the message
     */
    public void addError(@NotNull final ErrorViewCategory category, @NotNull final String message) {
        errorView.addError(category, file + ": " + message);
    }

    /**
     * Adds an error message.
     * @param category the category
     * @param lineNo the line number
     * @param message the message
     */
    public void addError(@NotNull final ErrorViewCategory category, final int lineNo, @NotNull final String message) {
        errorView.addError(category, lineNo, file + ": " + message);
    }

}
