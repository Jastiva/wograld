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

package net.sf.gridarta.model.mapmanager;

import java.io.File;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The user wants to edit a script file.
     */
    void editScript();

    /**
     * The user wants to open a map file.
     */
    void openFile();

    /**
     * Save one map.
     * @param mapControl the map
     * @return <code>false</code> if saving failed, <code>true</code> otherwise
     */
    boolean save(@NotNull MapControl<G, A, R> mapControl);

    /**
     * Saves all maps.
     */
    void saveAllMaps();

    /**
     * Invoked when the user wants to close all maps.
     * @return <code>true</code> if all maps closed, <code>false</code> if user
     *         cancelled.
     */
    boolean closeAllMaps();

    /**
     * Asks the user for a filename, then saves the map.
     * @param mapControl the map
     * @return <code>false</code> if the user cancelled the save or if saving
     *         failed, <code>true</code> otherwise
     */
    boolean saveAs(@NotNull MapControl<G, A, R> mapControl);

    /**
     * Asks the user whether to save changes of a map. Returns <code>true</code>
     * (and does not ask the user) if the map is unmodified.
     * @param mapControl the map
     * @return whether closing the map is allowed
     */
    boolean confirmSaveChanges(@NotNull MapControl<G, A, R> mapControl);

    /**
     * Reports an error while saving a map file to the user.
     * @param mapControl the map control that failed saving
     * @param message the error message
     */
    void reportSaveError(@NotNull MapControl<G, A, R> mapControl, @NotNull String message);

    /**
     * Reports an error while saving a map file to the user.
     * @param mapFile the map file that failed saving
     * @param message the error message
     */
    void reportSaveError(@NotNull File mapFile, @NotNull String message);

    void reportLoadError(@Nullable File file, @NotNull String message);

    void reportOutOfMapBoundsDeleted(@NotNull File file, int outOfMapBoundsDeleted, @NotNull StringBuilder outOfMapBoundsObjects);

    void reportOutOfMemory(@NotNull File file);

    /**
     * Reports an error while teleporting a character to the current map.
     * @param mapPath the map path to teleport to
     * @param message the error message
     */
    void reportTeleportCharacterError(@NotNull String mapPath, @NotNull String message);

}
