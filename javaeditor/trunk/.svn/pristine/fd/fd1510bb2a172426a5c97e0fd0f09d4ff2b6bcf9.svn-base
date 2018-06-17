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

package net.sf.gridarta.model.spells;

import org.jetbrains.annotations.NotNull;

/**
 * Describes a numbered in-game spell.
 * @author Andreas Kirschbaum
 */
public class NumberSpell extends Spell {

    /**
     * The spell number.
     */
    private final int number;

    /**
     * Create a new instance.
     * @param name the spell name
     * @param number the spell number
     */
    public NumberSpell(@NotNull final String name, final int number) {
        super(name);
        this.number = number;
    }

    /**
     * Return the spell number.
     * @return the spell number
     */
    public int getNumber() {
        return number;
    }

}
