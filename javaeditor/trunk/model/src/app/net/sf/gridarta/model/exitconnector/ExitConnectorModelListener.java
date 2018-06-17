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

package net.sf.gridarta.model.exitconnector;

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners interested in {@link ExitConnectorModel} related
 * events.
 * @author Andreas Kirschbaum
 */
public interface ExitConnectorModelListener extends EventListener {

    /**
     * Called whenever the exit location has changed.
     * @param exitLocation the new exit location or <code>null</code>
     */
    void exitLocationChanged(@Nullable ExitLocation exitLocation);

    /**
     * Called whenever the setting "paste exit name" has changed.
     * @param pasteExitName the new setting
     */
    void pasteExitNameChanged(boolean pasteExitName);

    /**
     * Called whenever the setting "automatically create exits" has changed.
     * @param pasteExitName the new setting
     */
    void autoCreateExitChanged(boolean pasteExitName);

    /**
     * Called whenever the setting "exit archetype name" has changed.
     * @param exitArchetypeName the new setting
     */
    void exitArchetypeNameChanged(@NotNull String exitArchetypeName);

}
