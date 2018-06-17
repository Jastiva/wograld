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
import org.jetbrains.annotations.NotNull;

/**
 * Super-interface for validators.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface Validator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Get a Key that uniquely identifies this Validator. The key can then be
     * used for purposes like i18n/l10n or preferences.
     * @return key
     */
    @NotNull
    String getKey();

    /**
     * Set whether this Validator should be enabled.
     * @param enabled <code>true</code> if this validator should be enabled,
     * otherwise <code>false</code>
     */
    void setEnabled(boolean enabled);

    /**
     * Get whether this Validator is enabled.
     * @return enabled
     */
    boolean isEnabled();

    /**
     * Get whether this Validator should be enabled per default.
     * @return defaultEnabled
     */
    boolean isDefaultEnabled();

}
