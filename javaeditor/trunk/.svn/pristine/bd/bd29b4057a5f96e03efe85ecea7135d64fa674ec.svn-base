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

package net.sf.gridarta.textedit.textarea.tokenmarker;

/**
 * Describes a parameter.
 * @author Andreas Kirschbaum
 */
public class Parameter {

    /**
     * The parameter name.
     */
    private final String name;

    /**
     * The token id used to highlight the parameter name.
     */
    private final byte id;

    /**
     * The parameter type for the value of this parameter.
     */
    private final ParameterType parameterType;

    /**
     * Creates a new instance.
     * @param name the parameter name
     * @param id the token id used to highlight the parameter name
     * @param parameterType the parameter type for the value of this parameter
     */
    public Parameter(final String name, final byte id, final ParameterType parameterType) {
        this.name = name;
        this.id = id;
        this.parameterType = parameterType;
    }

    /**
     * Returns the parameter name.
     * @return the parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the token id used to highlight the parameter name.
     * @return the token id
     */
    public byte getId() {
        return id;
    }

    /**
     * Returns the parameter type for the value of this parameter.
     * @return the parameter type
     */
    public ParameterType getParameterType() {
        return parameterType;
    }

}
