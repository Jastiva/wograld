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

package net.sf.gridarta.model.mapmodel;

import java.util.EventListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners listening on map transactions of {@link MapModel
 * MapModels}.
 * @author Andreas Kirschbaum
 */
public interface MapTransactionListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * A map transaction is about to start.
     */
    void preBeginTransaction();

    /**
     * A map transaction has started.
     * @param mapModel the map model
     * @param name the transaction name
     */
    void beginTransaction(@NotNull MapModel<G, A, R> mapModel, @NotNull String name);

    /**
     * A map transaction has ended.
     * @param mapModel the map model
     * @param savedSquares the squares affected by the transaction
     */
    void endTransaction(@NotNull MapModel<G, A, R> mapModel, @NotNull SavedSquares<G, A, R> savedSquares);

    /**
     * A map transaction has been finished.
     */
    void postEndTransaction();

}
