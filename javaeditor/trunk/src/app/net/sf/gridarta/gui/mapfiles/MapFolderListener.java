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

package net.sf.gridarta.gui.mapfiles;

import java.util.EventListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners interested in events of {@link MapFolder
 * MapFolders}.
 * @author Andreas Kirschbaum
 */
public interface MapFolderListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * A pickmap has been added to the folder.
     * @param mapFile the added pickmap
     */
    void pickmapAdded(@NotNull MapFile<G, A, R> mapFile);

    /**
     * A pickmap has been removed from the folder. The passed
     * <code>pickmap</code> instance has not been removed from its {@link
     * MapFolder} but not yet unloaded.
     * @param mapFile the removed pickmap
     */
    void pickmapRemoved(@NotNull MapFile<G, A, R> mapFile);

    /**
     * A pickmap has been reverted to the contents of its underlying file.
     * @param mapFile the reverted pickmap
     * @param oldPickmap the map control of the pickmap before revert
     */
    void pickmapReverted(@NotNull MapFile<G, A, R> mapFile, @NotNull MapControl<G, A, R> oldPickmap);

}
