/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.autovalidator;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListener;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListenerManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the auto-validator for map validation. It tracks changes to maps,
 * then asynchronously re-runs the map validator.
 * @author Andreas Kirschbaum
 */
public class AutoValidator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Preferences key for auto validation.
     */
    private static final String PREFERENCES_VALIDATOR_AUTO = "autoValidate";

    /**
     * Preferences.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The map validators to run.
     */
    @NotNull
    private final DelegatingMapValidator<G, A, R> validators;

    /**
     * The {@link DelayedMapModelListenerManager}.
     */
    @NotNull
    private final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager;

    /**
     * The last known enabled state.
     */
    private boolean wasEnabled;

    /**
     * The preference change listener to detect an changed options. The
     * auto-validator is re-enabled if either {@link #PREFERENCES_VALIDATOR_AUTO}
     * was enabled, or a validator check was changed while the validator is
     * enabled.
     */
    @NotNull
    private final PreferenceChangeListener preferenceChangeListener = new PreferenceChangeListener() {

        @Override
        public void preferenceChange(final PreferenceChangeEvent evt) {
            final String key = evt.getKey();
            if (key.equals(PREFERENCES_VALIDATOR_AUTO)) {
                if (wasEnabled != isEnabled()) {
                    wasEnabled = !wasEnabled;
                    validateAllMaps();
                }
            } else if (key.startsWith("Validator.")) {
                validateAllMaps();
            }
        }

    };

    /**
     * The {@link DelayedMapModelListener} used to detect changed maps.
     */
    @NotNull
    private final DelayedMapModelListener<G, A, R> delayedMapModelListener = new DelayedMapModelListener<G, A, R>() {

        @Override
        public void mapModelChanged(@NotNull final MapModel<G, A, R> mapModel) {
            if (wasEnabled) {
                validators.validateAll(mapModel);
            }
        }

    };

    /**
     * Create a new instance.
     * @param validators the map validators
     * @param autoDefault whether the auto-validator is enabled by default
     * @param delayedMapModelListenerManager the delayed map model listener
     * manager to use
     */
    public AutoValidator(@NotNull final DelegatingMapValidator<G, A, R> validators, final boolean autoDefault, @NotNull final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager) {
        this.validators = validators;
        this.delayedMapModelListenerManager = delayedMapModelListenerManager;
        setDefaultPreferencesValue(autoDefault);
        wasEnabled = isEnabled(); // call to isEnabled() must not happen before setDefaultPreferencesValue()
        preferences.addPreferenceChangeListener(preferenceChangeListener);
        delayedMapModelListenerManager.addDelayedMapModelListener(delayedMapModelListener);
    }

    /**
     * Schedules all maps for validation.
     */
    private void validateAllMaps() {
        if (wasEnabled) {
            delayedMapModelListenerManager.scheduleAllMapModels();
        }
    }

    /**
     * Set the default preferences value for {@link #PREFERENCES_VALIDATOR_AUTO}.
     * @param autoDefault the default value to set
     */
    private static void setDefaultPreferencesValue(final boolean autoDefault) {
        final boolean enabled = preferences.getBoolean(PREFERENCES_VALIDATOR_AUTO, autoDefault);
        if (enabled != isEnabled()) {
            preferences.putBoolean(PREFERENCES_VALIDATOR_AUTO, autoDefault);
        }
    }

    /**
     * Return whether the auto-validator is enabled.
     * @return whether the auto-validator is enabled
     */
    public static boolean isEnabled() {
        return preferences.getBoolean(PREFERENCES_VALIDATOR_AUTO, false);
    }

    /**
     * Set whether the auto-validator is enabled.
     * @param enabled whether the auto-validator is enabled
     */
    public static void setEnabled(final boolean enabled) {
        preferences.putBoolean(PREFERENCES_VALIDATOR_AUTO, enabled);
    }

}
