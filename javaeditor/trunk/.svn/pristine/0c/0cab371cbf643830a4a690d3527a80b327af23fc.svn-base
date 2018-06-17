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

import java.util.HashMap;
import java.util.Map;

/**
 * Describes a command (excluding the command name).
 * @author Andreas Kirschbaum
 */
public class Spec {

    /**
     * The token id used to highlight this command.
     */
    private final byte id;

    /**
     * Maps parameter name to parameter specification.
     */
    private final Map<String, Parameter> parameters = new HashMap<String, Parameter>();

    /**
     * Creates a new instance.
     * @param id the token id used to highlight this command
     * @param parameters the possible parameters of this command
     */
    public Spec(final byte id, final Parameter... parameters) {
        this.id = id;
        for (final Parameter parameter : parameters) {
            if (this.parameters.put(parameter.getName(), parameter) != null) {
                throw new IllegalArgumentException("duplicate parameter name: " + parameter.getName());
            }
        }
    }

    /**
     * Returns the token id used to highlight this command.
     * @return the token id
     */
    public byte getId() {
        return id;
    }

    /**
     * Returns the parameter specification for a parameter name.
     * @param name the parameter name
     * @return the parameter specification, or <code>null</code> if the
     *         parameter name is invalid for this command
     */
    public Parameter getParameter(final String name) {
        return parameters.get(name);
    }

}
