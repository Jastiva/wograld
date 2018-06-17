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

package net.sf.gridarta.model.face;

/**
 * Exception that's thrown in case a face name was not unique.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DuplicateFaceException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link FaceObject} causing the problem.
     * @serial
     */
    private final FaceObject duplicate;

    /**
     * The other {@link FaceObject} causing the problem.
     * @serial
     */
    private final FaceObject existing;

    /**
     * Creates a DuplicateFaceException.
     * @param duplicate the duplicate face that's the cause
     * @param existing the existing face
     */
    public DuplicateFaceException(final FaceObject duplicate, final FaceObject existing) {
        super("Non-unique face " + duplicate.getFaceName() + " from " + duplicate.getOriginalFilename());
        this.duplicate = duplicate;
        this.existing = existing;
    }

    /**
     * Returns the duplicate that caused this exception.
     * @return duplicate
     */
    public FaceObject getDuplicate() {
        return duplicate;
    }

    /**
     * Returns the other duplicate that caused this exception.
     * @return duplicate
     */
    public FaceObject getExisting() {
        return existing;
    }

}
