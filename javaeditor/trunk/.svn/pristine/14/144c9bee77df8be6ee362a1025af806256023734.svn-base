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

import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.CustomTypeError;
import org.jetbrains.annotations.NotNull;

/**
 * Checks that a game object does not set a custom type.
 * @author Andreas Kirschbaum
 */
public class CustomTypeChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * The allowed type changes. Maps source type to destination type to
     * environment type. The environment type may be <code>null</code> to denote
     * "any environment".
     */
    @NotNull
    private final Map<Integer, Map<Integer, Integer>> ignoreEntries = new HashMap<Integer, Map<Integer, Integer>>();

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    public CustomTypeChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final int archetypeTypeNo = gameObject.getArchetype().getTypeNo();
        final int gameObjectTypeNo = gameObject.getTypeNo();
        if (gameObjectTypeNo != archetypeTypeNo && !isAllowedTypeChange(gameObject, archetypeTypeNo, gameObjectTypeNo)) {
            errorCollector.collect(new CustomTypeError<G, A, R>(gameObject, archetypeTypeNo, gameObjectTypeNo));
        }
    }

    /**
     * Returns whether the given game's changed type is allowed.
     * @param gameObject the game object to check
     * @param archetypeTypeNo the old type
     * @param gameObjectTypeNo the new type
     * @return whether the type change is allowed
     */
    private boolean isAllowedTypeChange(@NotNull final GameObject<G, A, R> gameObject, final int archetypeTypeNo, final int gameObjectTypeNo) {
        final Map<Integer, Integer> entries = ignoreEntries.get(archetypeTypeNo);
        if (entries == null) {
            return false;
        }

        final Integer allowedEnvType = entries.get(gameObjectTypeNo);
        if (allowedEnvType == null) {
            return entries.containsKey(gameObjectTypeNo);
        }

        final BaseObject<G, A, R, ?> envGameObject = gameObject.getContainerGameObject();
        if (envGameObject == null) {
            return false;
        }

        final int envTypeNo = envGameObject.getTypeNo();
        return envTypeNo == allowedEnvType;
    }

    /**
     * Adds an allowed type change.
     * @param fromType the allowed source type
     * @param toType the allowed destination type
     */
    public void addIgnore(final int fromType, final int toType) {
        getIgnoreSet(fromType).put(toType, null);
    }

    /**
     * Adds an allowed type change.
     * @param fromType the allowed source type
     * @param toType the allowed destination type
     * @param envType the type of the parent object
     */
    public void addIgnore(final int fromType, final int toType, final int envType) {
        getIgnoreSet(fromType).put(toType, envType);
    }

    /**
     * Returns the allowed type changes for a given source type.
     * @param type the source type
     * @return the allowed type changes
     */
    @NotNull
    private Map<Integer, Integer> getIgnoreSet(final int type) {
        final Map<Integer, Integer> ignoreEntrySet = ignoreEntries.get(type);
        if (ignoreEntrySet != null) {
            return ignoreEntrySet;
        }

        final Map<Integer, Integer> newIgnoreEntrySet = new HashMap<Integer, Integer>();
        ignoreEntries.put(type, newIgnoreEntrySet);
        return newIgnoreEntrySet;
    }

}
