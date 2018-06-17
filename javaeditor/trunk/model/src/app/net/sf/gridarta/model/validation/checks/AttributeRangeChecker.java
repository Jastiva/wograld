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
import java.util.IdentityHashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import org.jetbrains.annotations.NotNull;

/**
 * A validator that checks for suspicious attribute values.
 * @author Andreas Kirschbaum
 */
public class AttributeRangeChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * Maps object type to {@link Type} instance.
     */
    @NotNull
    private final Map<Integer, Type<G, A, R>> types = new HashMap<Integer, Type<G, A, R>>();

    /**
     * Maps matcher to {@link Type} instance.
     */
    @NotNull
    private final Map<GameObjectMatcher, Type<G, A, R>> matchers = new IdentityHashMap<GameObjectMatcher, Type<G, A, R>>();

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     */
    public AttributeRangeChecker(@NotNull final ValidatorPreferences validatorPreferences) {
        super(validatorPreferences);
    }

    /**
     * Adds an attribute to check. The validator generates an error if the
     * attribute value is outside the given value range.
     * @param type the object type to check
     * @param name the attribute name to check
     * @param displayName the description for error messages
     * @param minValue the minimum expected value
     * @param maxValue the maximum expected value
     * @throws InvalidCheckException if the check is invalid
     */
    public void add(final int type, @NotNull final String name, @NotNull final String displayName, final int minValue, final int maxValue) throws InvalidCheckException {
        if (minValue == Integer.MIN_VALUE && maxValue == Integer.MAX_VALUE) {
            return;
        }

        getType(type).add(name, displayName, new Range(minValue, maxValue));
    }

    /**
     * Adds an attribute to check. The validator generates an error if the
     * attribute value is outside the given value range.
     * @param matcher the game object matcher
     * @param name the attribute name to check
     * @param displayName the description for error messages
     * @param minValue the minimum expected value
     * @param maxValue the maximum expected value
     * @throws InvalidCheckException if the check is invalid
     */
    public void add(@NotNull final GameObjectMatcher matcher, @NotNull final String name, @NotNull final String displayName, final int minValue, final int maxValue) throws InvalidCheckException {
        if (minValue == Integer.MIN_VALUE && maxValue == Integer.MAX_VALUE) {
            return;
        }

        getMatcher(matcher).add(name, displayName, new Range(minValue, maxValue));
    }

    /**
     * Returns the {@link Type} instance for a given object type.
     * @param type the object type to look up
     * @return the corresponding <code>Type</code> instance
     */
    @NotNull
    private Type<G, A, R> getType(final int type) {
        final Type<G, A, R> oldType = types.get(type);
        if (oldType != null) {
            return oldType;
        }

        final Type<G, A, R> newType = new Type<G, A, R>();
        types.put(type, newType);
        return newType;
    }

    /**
     * Returns the {@link Type} instance for a given {@link GameObjectMatcher}.
     * @param matcher the game object matcher to look up
     * @return the corresponding <code>Type</code> instance
     */
    @NotNull
    private Type<G, A, R> getMatcher(@NotNull final GameObjectMatcher matcher) {
        final Type<G, A, R> oldType = matchers.get(matcher);
        if (oldType != null) {
            return oldType;
        }

        final Type<G, A, R> newType = new Type<G, A, R>();
        matchers.put(matcher, newType);
        return newType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (!gameObject.isHead()) {
            return;
        }

        for (final Map.Entry<GameObjectMatcher, Type<G, A, R>> e : matchers.entrySet()) {
            if (e.getKey().isMatching(gameObject)) {
                e.getValue().validate(gameObject, errorCollector);
            }
        }

        final Type<G, A, R> type = types.get(gameObject.getTypeNo());
        if (type == null) {
            return;
        }

        type.validate(gameObject, errorCollector);
    }

}
