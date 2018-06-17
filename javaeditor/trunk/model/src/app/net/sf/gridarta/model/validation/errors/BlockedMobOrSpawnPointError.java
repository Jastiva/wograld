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

package net.sf.gridarta.model.validation.errors;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;

/**
 * Validation error that's used when the BlockedMobOrSpawnPointChecker detected
 * a possible error on the map.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class BlockedMobOrSpawnPointError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends SquareValidationError<G, A, R> implements CorrectableError {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Blocking GameObjects.
     * @serial
     */
    @NotNull
    private final Collection<G> blockers;

    /**
     * Mob or spawn GameObjects.
     * @serial
     */
    @NotNull
    private final Collection<G> mobsOrSpawnPoints;

    /**
     * Create a BlockedMobOrSpawnPointError.
     * @param mapSquare the square on which the error occurred
     * @param blockers blocking GameObjects
     * @param mobsOrSpawnPoints mob or spawn GameObjects
     */
    public BlockedMobOrSpawnPointError(@NotNull final MapSquare<G, A, R> mapSquare, @NotNull final Collection<G> blockers, @NotNull final Collection<G> mobsOrSpawnPoints) {
        super(mapSquare);
        this.blockers = new ArrayList<G>(blockers);
        this.mobsOrSpawnPoints = new ArrayList<G>(mobsOrSpawnPoints);
    }

    /**
     * Returns the blocking {@link GameObject GameObjects}.
     * @return the blocking game objects
     */
    @NotNull
    public Collection<G> getBlockers() {
        return Collections.unmodifiableCollection(blockers);
    }

    /**
     * Returns the mob or spawn point {@link GameObject GameObjects}.
     * @return the mob or spawn point game objects
     */
    @NotNull
    public Collection<G> getMobsOrSpawnPoints() {
        return Collections.unmodifiableCollection(mobsOrSpawnPoints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void correct(@NotNull final Component parentComponent) {
        // TODO: Query whether the user wants to delete the game object, delete the spawn / mob or cancel.
    }

}
