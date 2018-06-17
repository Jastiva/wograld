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

package net.sf.gridarta.plugin.parameter;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PluginParameter} that holds a boolean value.
 */
public class BooleanParameter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractPluginParameter<G, A, R, Boolean> {

    /**
     * The string representation of this parameter type.
     */
    @NotNull
    public static final String PARAMETER_TYPE = Boolean.class.getName();

    @NotNull
    private String trueText = "Yes";

    @NotNull
    private String falseText = "No";

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public <T> T visit(@NotNull final PluginParameterVisitor<G, A, R, T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setStringValue(@NotNull final String value) {
        if (value.equals("true")) {
            setValue(Boolean.TRUE);
            return true;
        } else if (value.equals("false")) {
            setValue(Boolean.FALSE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getParameterType() {
        return PARAMETER_TYPE;
    }

    @NotNull
    public String getFalseText() {
        return falseText;
    }

    @NotNull
    public String getTrueText() {
        return trueText;
    }

    public void setFalseText(@NotNull final String falseText) {
        this.falseText = falseText;
    }

    public void setTrueText(@NotNull final String trueText) {
        this.trueText = trueText;
    }

}
