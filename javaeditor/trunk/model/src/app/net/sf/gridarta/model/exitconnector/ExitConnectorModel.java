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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores information needed by the exit connector.
 * @author Andreas Kirschbaum
 */
public interface ExitConnectorModel {

    /**
     * The default value for {@link #isPasteExitName()}.
     */
    boolean PASTE_EXIT_NAME_DEFAULT = true;

    /**
     * The default value for {@link #isAutoCreateExit()}.
     */
    boolean AUTO_CREATE_EXIT_DEFAULT = true;

    /**
     * The default value for {@link #getExitArchetypeName()}.
     */
    String EXIT_ARCHETYPE_NAME_DEFAULT = "invis_exit";

    /**
     * Returns the remembered exit location.
     * @return the exit location or <code>null</code>
     */
    @Nullable
    ExitLocation getExitLocation();

    /**
     * Sets the remembered exit location.
     * @param exitLocation the exit location or <code>null</code>
     */
    void setExitLocation(@Nullable ExitLocation exitLocation);

    /**
     * Returns whether the exit name should be updated.
     * @return whether the exit name should be updated
     */
    boolean isPasteExitName();

    /**
     * Sets whether the exit name should be updated.
     * @param pasteExitName whether the exit name should be updated
     */
    void setPasteExitName(boolean pasteExitName);

    /**
     * Returns whether exit game objects should be auto-created when needed.
     * @return whether exit game objects should be auto-created when needed
     */
    boolean isAutoCreateExit();

    /**
     * Sets whether exit game objects should be auto-created when needed.
     * @param autoCreateExit whether exit game objects should be auto-created
     * when needed
     */
    void setAutoCreateExit(boolean autoCreateExit);

    /**
     * Returns the archetype name when creating exit game objects.
     * @return the archetype name
     */
    @NotNull
    String getExitArchetypeName();

    /**
     * Sets the archetype name for creating exit game objects.
     * @param exitArchetypeName the archetype name
     */
    void setExitArchetypeName(@NotNull String exitArchetypeName);

    /**
     * Adds an {@link ExitConnectorModelListener} to be notified of changes.
     * @param listener the listener
     */
    void addExitConnectorModelListener(@NotNull ExitConnectorModelListener listener);

    /**
     * Removes an {@link ExitConnectorModelListener} to be notified of changes.
     * @param listener the listener
     */
    void removeExitConnectorModelListener(@NotNull ExitConnectorModelListener listener);

}
