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

package net.sf.gridarta.model.anim;

import org.jetbrains.annotations.NotNull;

/**
 * This exception is thrown when parsing an animation definition file
 * (<code>arch/animations</code> and <code>arch/**.anim</code>) revealed that
 * such a file contains an error. In future, it might as well be used if a user
 * defines or changes an animation.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AnimationParseException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The line number that caused the error.
     * @serial include
     */
    private final int lineNumber;

    /**
     * Create an AnimationParseException.
     * @param expected expected parse element
     * @param actual actual parse element that gave the error
     * @param lineNumber number of erroneous line
     */
    public AnimationParseException(@NotNull final String expected, @NotNull final String actual, final int lineNumber) {
        super("Expected \"" + expected + "\", got \"" + actual + "\" in line " + lineNumber);
        this.lineNumber = lineNumber;
    }

    /**
     * Get the number of the erroneous line.
     * @return number of erroneous line
     */
    public int getLineNumber() {
        return lineNumber;
    }

}
