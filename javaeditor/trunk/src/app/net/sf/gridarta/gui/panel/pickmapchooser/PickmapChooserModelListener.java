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

package net.sf.gridarta.gui.panel.pickmapchooser;

import net.sf.gridarta.gui.mapfiles.MapFile;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners for pickmap related events.
 * @author Andreas Kirschbaum
 */
public interface PickmapChooserModelListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Notifies that the active pickmap has changed.
     * @param mapFile the active pickmap or <code>null</code> if no pickmap
     * exists
     */
    void activePickmapChanged(@Nullable MapFile<G, A, R> mapFile);

    /**
     * Notifies that one pickmap has been reverted.
     * @param mapFile the pickmap that has been reverted
     */
    void pickmapReverted(@NotNull MapFile<G, A, R> mapFile);

    /**
     * Notifies that one pickmap's modified flag has changed.
     * @param index the index of the changed pickmap
     * @param mapFile the pickmap that has changed
     */
    void pickmapModifiedChanged(int index, MapFile<G, A, R> mapFile);

}
