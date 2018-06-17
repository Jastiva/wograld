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

import org.jetbrains.annotations.NotNull;

/**
 * Configuration parameters for {@link Validator Validators}.
 * @author Andreas Kirschbaum
 */
public interface ValidatorPreferences {

    /**
     * Loads the stored "enabled" attribute.
     * @param key the validator's key
     * @param defaultEnabled whether the validator is enabled by default
     * @return whether the validator is enabled
     */
    boolean loadEnabled(@NotNull String key, boolean defaultEnabled);

    /**
     * Saves the "enabled" attribute.
     * @param key the validator's key
     * @param enabled the enabled state
     */
    void saveEnabled(@NotNull String key, boolean enabled);

}
