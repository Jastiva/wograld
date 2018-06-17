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

package net.sf.gridarta.model.validation.checks;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.EnvironmentInvError;
import net.sf.gridarta.model.validation.errors.EnvironmentMapError;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GameObjectValidator} that checks for valid environment. Warnings can
 * be generated for game objects placed directly on maps, or for game objects
 * placed into inventories of game objects not having some type.
 * @author Andreas Kirschbaum
 */
public class EnvironmentChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * The {@link ArchetypeType ArchetypeTypes} that are not allowed on maps.
     */
    @NotNull
    private final Map<ArchetypeType, Void> noMap = new IdentityHashMap<ArchetypeType, Void>();

    /**
     * Maps {@link ArchetypeType ArchetypeTypes} to allowed environment types.
     * Game object types not included are allowed in any inventory.
     */
    @NotNull
    private final Map<ArchetypeType, int[]> inv = new IdentityHashMap<ArchetypeType, int[]>();

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    public EnvironmentChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * Marks an {@link ArchetypeType} to not be allowed directly on maps.
     * @param archetypeType the archetype type
     */
    public void addNoMap(@NotNull final ArchetypeType archetypeType) {
        noMap.put(archetypeType, null);
    }

    /**
     * Sets the allowed environment game object types for an {@link
     * ArchetypeType}.
     * @param archetypeType the archetype type
     * @param types the allowed environment types
     */
    public void addInv(@NotNull final ArchetypeType archetypeType, @NotNull final int[] types) {
        if (inv.containsKey(archetypeType)) {
            throw new IllegalArgumentException();
        }

        final int[] tmp = types.clone();
        Arrays.sort(tmp);
        inv.put(archetypeType, tmp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final G envGameObject = gameObject.getContainerGameObject();
        if (envGameObject == null) {
            for (final ArchetypeType archetypeType : noMap.keySet()) {
                if (archetypeType.matches(gameObject)) {
                    errorCollector.collect(new EnvironmentMapError<G, A, R>(gameObject, archetypeType.getTypeName()));
                    break;
                }
            }
        } else {
            for (final Map.Entry<ArchetypeType, int[]> entry : inv.entrySet()) {
                final ArchetypeType archetypeType = entry.getKey();
                if (archetypeType.matches(gameObject)) {
                    final int[] types = entry.getValue();
                    if (types != null && Arrays.binarySearch(types, envGameObject.getTypeNo()) < 0) {
                        final String typeDescription = archetypeType.getTypeName();
                        final String envTypeDescription = Integer.toString(envGameObject.getTypeNo()); // XXX: use envArchetypeType.getTypeName()
                        errorCollector.collect(new EnvironmentInvError<G, A, R>(gameObject, typeDescription, envTypeDescription));
                    }
                }
            }
        }
    }

}
