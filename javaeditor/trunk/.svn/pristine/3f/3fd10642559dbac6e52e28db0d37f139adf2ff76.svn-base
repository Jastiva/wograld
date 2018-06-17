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

package net.sf.gridarta.model.validation;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * This is the base class for validators.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @note that this class completely implements the interface {@link Validator}
 * but does not declare it to force subclasses to choose the interface to
 * implement.
 */
public abstract class AbstractValidator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Validator<G, A, R> {

    /**
     * The prefix for preference keys.
     */
    @NotNull
    private static final String VALIDATOR_PREFIX = "Validator.";

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link ValidatorPreferences} to use.
     */
    @NotNull
    private final ValidatorPreferences validatorPreferences;

    /**
     * Key.
     */
    @NotNull
    private final String key;

    /**
     * Whether this MapValidator is enabled.
     */
    private boolean enabled;

    /**
     * Whether this MapValidator is enabled by default.
     */
    private final boolean defaultEnabled;

    /**
     * Create an AbstractMapValidator. The key is generated from the validator's
     * name, which must end on "Checker".
     * @param validatorPreferences the validator preferences to use
     * @throws IllegalArgumentException in case the validator's name does not
     * end on "Checker".
     */
    protected AbstractValidator(@NotNull final ValidatorPreferences validatorPreferences) throws IllegalArgumentException {
        this.validatorPreferences = validatorPreferences;
        final String name = getClass().getSimpleName();
        if (!name.endsWith("Checker")) {
            throw new IllegalArgumentException("Class name must end with \"Checker\"");
        }
        key = VALIDATOR_PREFIX + name.substring(0, name.lastIndexOf("Checker"));
        final String defaultEnabledString = ActionBuilderUtils.getString(ACTION_BUILDER, key + ".default");
        defaultEnabled = Boolean.parseBoolean(defaultEnabledString);
        enabled = validatorPreferences.loadEnabled(key, defaultEnabled);
    }

    /**
     * Create an AbstractMapValidator.
     * @param validatorPreferences the validator preferences to use
     * @param key Key
     */
    protected AbstractValidator(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final String key) {
        this.validatorPreferences = validatorPreferences;
        this.key = key;
        final String defaultEnabledString = ActionBuilderUtils.getString(ACTION_BUILDER, key + ".default");
        defaultEnabled = Boolean.parseBoolean(defaultEnabledString);
        enabled = validatorPreferences.loadEnabled(key, defaultEnabled);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;
        validatorPreferences.saveEnabled(key, enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

}
