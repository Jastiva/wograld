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
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link PluginParameter} that holds an {@link Archetype} value.
 */
public class ArchParameter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractPluginParameter<G, A, R, Archetype<G, A, R>> {

    /**
     * The string representation of this parameter type.
     */
    @NotNull
    public static final String PARAMETER_TYPE = Archetype.class.getName();

    /**
     * The {@link ArchetypeSet} for looking up archetypes.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    private String valueString;

    /**
     * Creates a new instance.
     * @param archetypeSet the archetype set for looking up archetypes
     */
    public ArchParameter(@NotNull final ArchetypeSet<G, A, R> archetypeSet) {
        this.archetypeSet = archetypeSet;
    }

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
    @Nullable
    @Override
    public Archetype<G, A, R> getValue() {
        if (super.getValue() == null) {
            setValue(archetypeSet.getOrCreateArchetype(valueString));
        }
        return super.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setStringValue(@NotNull final String value) {
        final Archetype<G, A, R> archetype;
        try {
            archetype = archetypeSet.getArchetype(value);
        } catch (final UndefinedArchetypeException ignored) {
            return false;
        }
        setValue(archetype);
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

    @Nullable
    public String getValueString() {
        return valueString;
    }

}
