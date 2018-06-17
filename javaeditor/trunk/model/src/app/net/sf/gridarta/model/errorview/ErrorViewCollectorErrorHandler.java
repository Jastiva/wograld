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
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An {@link ErrorHandler} that adds all reported problems to an {@link
 * ErrorViewCollector} instance.
 * @author Andreas Kirschbaum
 */
public class ErrorViewCollectorErrorHandler implements ErrorHandler {

    /**
     * The {@link ErrorViewCollector} instance for adding messages.
     */
    @NotNull
    private final ErrorViewCollector errorViewCollector;

    /**
     * The {@link ErrorViewCategory} to use when adding messages to {@link
     * #errorViewCollector}.
     */
    @NotNull
    private final ErrorViewCategory errorViewCategory;

    /**
     * Creates a new instance.
     * @param errorViewCollector the error view collector instance for adding
     * messages
     * @param errorViewCategory the error view category to use when adding
     * messages to <code>errorViewCollector</code>
     */
    public ErrorViewCollectorErrorHandler(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final ErrorViewCategory errorViewCategory) {
        this.errorViewCollector = errorViewCollector;
        this.errorViewCategory = errorViewCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        errorViewCollector.addWarning(errorViewCategory, exception.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final SAXParseException exception) throws SAXException {
        errorViewCollector.addError(errorViewCategory, exception.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        errorViewCollector.addError(errorViewCategory, exception.getMessage());
    }

}
