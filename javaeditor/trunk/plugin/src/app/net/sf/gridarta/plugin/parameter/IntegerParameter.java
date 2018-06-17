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
 * A {@link PluginParameter} that holds an integer value.
 */
public class IntegerParameter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractPluginParameter<G, A, R, Integer> {

    /**
     * The string representation of this parameter type.
     */
    @NotNull
    public static final String PARAMETER_TYPE = Integer.class.getName();

    /**
     * The minimal allowed value.
     */
    private int min = Integer.MIN_VALUE;

    /**
     * The maximal allowed value.
     */
    private int max = Integer.MAX_VALUE;

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
        final int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (final NumberFormatException ignored) {
            return false;
        }
        setValue(intValue);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getParameterType() {
        return PARAMETER_TYPE;
    }

    /**
     * Returns the maximal allowed value.
     * @return the maximal allowed value
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximal allowed value.
     * @param max the maximal allowed value
     */
    public void setMax(final int max) {
        this.max = Math.min(min, max);
        if (getValue() > this.max) {
            setValue(this.max);
        }
    }

    /**
     * Returns the minimal allowed value.
     * @return the minimal allowed value
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimal allowed value.
     * @param min the minimal allowed value
     */
    public void setMin(final int min) {
        this.min = Math.min(min, max);
        if (getValue() < this.min) {
            setValue(this.min);
        }
    }

}
