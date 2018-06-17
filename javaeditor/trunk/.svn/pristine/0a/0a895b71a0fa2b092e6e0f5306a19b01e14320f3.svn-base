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

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.SlayingError;
import org.jetbrains.annotations.NotNull;

/**
 * An GameObjectValidator to assert that game objects do not have critical
 * slaying strings.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class SlayingChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * The regular expression for any "slaying" field not matching any specific
     * game object.
     */
    @NotNull
    private final Pattern defaultRegex;

    /**
     * The matchers to check.
     */
    @NotNull
    private final Collection<Entry<G, A, R>> entries = new ArrayList<Entry<G, A, R>>();

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param defaultRegex the regular expression for any "slaying" field not
     * matching any specific game object
     */
    public SlayingChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final Pattern defaultRegex) {
        super(validatorPreferences);
        this.defaultRegex = defaultRegex;
    }

    /**
     * Adds a matcher to check.
     * @param gameObjectMatcher selects the game objects to check
     * @param regex the allowed values of the "slaying" field
     */
    public void addMatcher(@NotNull final GameObjectMatcher gameObjectMatcher, @NotNull final Pattern regex) {
        entries.add(new Entry<G, A, R>(gameObjectMatcher, regex));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        boolean useDefaultRegex = true;
        for (final Entry<G, A, R> entry : entries) {
            if (entry.validate(gameObject, errorCollector)) {
                useDefaultRegex = false;
            }
        }

        if (useDefaultRegex) {
            final CharSequence slaying = gameObject.getAttributeString(BaseObject.SLAYING, true);
            if (!defaultRegex.matcher(slaying).matches()) {
                errorCollector.collect(new SlayingError<G, A, R>(gameObject));
            }
        }
    }

}
