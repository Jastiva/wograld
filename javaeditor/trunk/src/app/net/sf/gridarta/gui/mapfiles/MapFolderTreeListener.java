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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners interested in events of {@link MapFolderTree
 * MapFolderTrees}.
 * @author Andreas Kirschbaum
 */
public interface MapFolderTreeListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * The active folder has changed.
     * @param mapFolder the new active folder, or <code>null</code> if no folder
     * is active
     */
    void activeMapFolderChanged(@Nullable MapFolder<G, A, R> mapFolder);

    /**
     * A folder has been added to the model.
     * @param mapFolder the added folder
     */
    void folderAdded(@NotNull MapFolder<G, A, R> mapFolder);

    /**
     * A folder has been removed from the model.
     * @param mapFolder the removed folder
     */
    void folderRemoved(@NotNull MapFolder<G, A, R> mapFolder);

}
