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

package net.sf.gridarta.model.mapcontrol;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;

/**
 * Currently nothing more than a marker interface for unification.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public interface MapControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Return flag that indicates whether this is a pickmap or not.
     * @return flag that indicates whether this is a pickmap or not
     */
    boolean isPickmap();

    /**
     * Returns the map model.
     * @return the map model
     */
    @NotNull
    MapModel<G, A, R> getMapModel();

    /**
     * Registers a {@link MapControlListener}.
     * @param listener the listener to register
     */
    void addMapControlListener(@NotNull MapControlListener<G, A, R> listener);

    /**
     * Unregisters a {@link MapControlListener}.
     * @param listener the listener to unregister
     */
    void removeMapControlListener(@NotNull MapControlListener<G, A, R> listener);

    /**
     * Saves the map to a file.
     * @throws IOException if saving fails
     */
    void save() throws IOException;

    /**
     * Saves the file with the given file name.
     * @param file the file to be saved to
     * @throws IOException if saving fails
     */
    void saveAs(@NotNull File file) throws IOException;

    /**
     * Increases the use counter.
     */
    void acquire();

    /**
     * Decreases the use counter and possibly frees the map control.
     */
    void release();

    /**
     * Returns the use counter.
     * @return the use counter
     */
    int getUseCounter();

}
