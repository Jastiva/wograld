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

package net.sf.gridarta.model.validation.checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.SquareValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.BlockedMobOrSpawnPointError;
import org.jetbrains.annotations.NotNull;

/**
 * A SquareValidator to assert that mobs or spawn points aren't on blocked
 * squares.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class BlockedMobOrSpawnPointChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements SquareValidator<G, A, R> {

    /**
     * List with blocking objects.
     */
    @NotNull
    private final List<G> blockers = new ArrayList<G>();

    /**
     * List with blocked objects.
     */
    @NotNull
    private final List<G> mobsOrSpawnPoints = new ArrayList<G>();

    /**
     * The object types to check.
     */
    @NotNull
    private final Collection<Integer> typeNumbers = new HashSet<Integer>();

    /**
     * Create a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param typeNumbers The object types to check.
     */
    public BlockedMobOrSpawnPointChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final Integer... typeNumbers) {
        super(validatorPreferences);
        this.typeNumbers.addAll(Arrays.asList(typeNumbers));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateSquare(@NotNull final MapSquare<G, A, R> mapSquare, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final G gameObject : mapSquare) {
            if (typeNumbers.contains(gameObject.getTypeNo())) {
                mobsOrSpawnPoints.add(gameObject);
            } else if (gameObject.getAttributeInt(BaseObject.NO_PASS, true) == 1) {
                blockers.add(gameObject);
            }
        }
        if (!blockers.isEmpty() && !mobsOrSpawnPoints.isEmpty()) {
            errorCollector.collect(new BlockedMobOrSpawnPointError<G, A, R>(mapSquare, new ArrayList<G>(blockers), new ArrayList<G>(mobsOrSpawnPoints)));
        }
        blockers.clear();
        mobsOrSpawnPoints.clear();
    }

}
