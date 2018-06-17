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

/**
 * Exception that's thrown in case an animation name was not unique.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class DuplicateAnimationException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * AnimationObject causing the problem.
     */
    private final AnimationObject duplicate;

    /**
     * Create a DuplicateAnimationException.
     * @param duplicate Duplicate animation that's the cause
     */
    public DuplicateAnimationException(final AnimationObject duplicate) {
        super("Non-unique animation " + duplicate.getAnimName());
        this.duplicate = duplicate;
    }

    /**
     * Get the duplicate that caused this exception.
     * @return duplicate
     */
    public AnimationObject getDuplicate() {
        return duplicate;
    }

}
