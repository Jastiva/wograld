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

import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.MapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.BlockedSpawnPointError;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapValidator} to assert that mobs or spawn points aren't on blocked
 * squares.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class BlockedSpawnPointChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements MapValidator<G, A, R> {

    /**
     * The x offset for checking blocked squares.
     */
    @NotNull
    private static final int[] freeArrX = { 0, 0, 1, 1, 1, 0, -1, -1, -1, 0, 1, 2, 2, 2, 2, 2, 1, 0, -1, -2, -2, -2, -2, -2, -1, 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0, -1, -2, -3, -3, -3, -3, -3, -3, -3, -2, -1, };

    /**
     * The y offset for checking blocked squares.
     */
    @NotNull
    private static final int[] freeArrY = { 0, -1, -1, 0, 1, 1, 1, 0, -1, -2, -2, -2, -1, 0, 1, 2, 2, 2, 2, 2, 1, 0, -1, -2, -2, -3, -3, -3, -3, -2, -1, 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 2, 1, 0, -1, -2, -3, -3, -3, };

    static {
        assert freeArrX.length == freeArrY.length;
    }

    /**
     * The object types to check.
     */
    @NotNull
    private final Collection<Integer> typeNumbers = new HashSet<Integer>();

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param typeNumbers the object types to check
     */
    public BlockedSpawnPointChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final Integer... typeNumbers) {
        super(validatorPreferences);
        this.typeNumbers.addAll(Arrays.asList(typeNumbers));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final BlockedMatrix<G, A, R> blocked = new BlockedMatrix<G, A, R>(mapModel);
        checkSpawnPoints(mapModel, blocked, errorCollector);
    }

    /**
     * Checks for blocked spawn points.
     * @param mapModel the map model to check
     * @param blocked the blocked matrix corresponding to <code>mapModel</code>
     * @param errorCollector where to add the errors to
     */
    private void checkSpawnPoints(@NotNull final Iterable<MapSquare<G, A, R>> mapModel, @NotNull final BlockedMatrix<G, A, R> blocked, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final Point pos = new Point();
        for (final MapSquare<G, A, R> mapSquare : mapModel) {
            for (final G gameObject : mapSquare) {
                mapSquare.getMapLocation(pos);
                checkSpawnPoint(gameObject, pos, blocked, errorCollector);
                for (final G invObject : gameObject.recursive()) {
                    checkSpawnPoint(invObject, pos, blocked, errorCollector);
                }
            }
        }
    }

    /**
     * Checks one game object.
     * @param gameObject the game object to check
     * @param pos the location of <code>gameObject</code> on the map
     * @param blocked the blocked matrix to check
     * @param errorCollector where to add the errors to
     */
    private void checkSpawnPoint(@NotNull final G gameObject, @NotNull final Point pos, @NotNull final BlockedMatrix<G, A, R> blocked, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final GameObject<G, A, R> head = gameObject.getHead();
        if (!typeNumbers.contains(head.getTypeNo())) {
            return;
        }

        final int spawnRange = Math.min(head.getAttributeInt(BaseObject.LAST_HEAL), freeArrX.length);
        for (int i = 0; i < spawnRange; i++) {
            if (!blocked.isBlocked(pos.x + freeArrX[i], pos.y + freeArrY[i])) {
                return;
            }
        }

        errorCollector.collect(new BlockedSpawnPointError<G, A, R>(gameObject));
    }

}
