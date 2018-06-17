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

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link ErrorView} suitable for unit tests.
 * @author Andreas Kirschbaum
 */
public class TestErrorView implements ErrorView {

    /**
     * The {@link Logger} for printing log messages.
     */
    private static final Category log = Logger.getLogger(TestErrorView.class);

    /**
     * Whether errors have been collected.
     */
    private boolean hasErrors;

    /**
     * Whether warnings have been collected.
     */
    private boolean hasWarnings;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError(@NotNull final ErrorViewCategory categoryName, @NotNull final String message) {
        hasErrors = true;
        if (log.isInfoEnabled()) {
            log.info("addError: " + categoryName + " " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError(@NotNull final ErrorViewCategory categoryName, final int lineNo, @NotNull final String message) {
        hasErrors = true;
        if (log.isInfoEnabled()) {
            log.info("addError: " + categoryName + " " + lineNo + " " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWarning(@NotNull final ErrorViewCategory categoryName, @NotNull final String message) {
        hasWarnings = true;
        if (log.isInfoEnabled()) {
            log.info("addWarning: " + categoryName + " " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWarning(@NotNull final ErrorViewCategory categoryName, final int lineNo, @NotNull final String message) {
        hasWarnings = true;
        if (log.isInfoEnabled()) {
            log.info("addWarning: " + categoryName + " " + lineNo + " " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * Returns whether at least one warning message has been added.
     * @return whether warnings exist
     */
    public boolean hasWarnings() {
        return hasWarnings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitDialog() throws InterruptedException {
        throw new AssertionError();
    }

}
