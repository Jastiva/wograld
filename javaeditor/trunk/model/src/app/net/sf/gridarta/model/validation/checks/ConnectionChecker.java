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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.connectionview.Connections;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.MapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.ConnectionUnknownError;
import net.sf.gridarta.model.validation.errors.ConnectionWithoutSinksError;
import net.sf.gridarta.model.validation.errors.ConnectionWithoutSourcesError;
import org.jetbrains.annotations.NotNull;

/**
 * Checks that for each connection value at least one source and at least one
 * sink exists.
 * @author Andreas Kirschbaum
 */
public class ConnectionChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements MapValidator<G, A, R> {

    /**
     * The {@link GameObjectMatcher} for matching sources.
     */
    @NotNull
    private final GameObjectMatcher sourceGameObjectMatcher;

    /**
     * The {@link GameObjectMatcher} for matching sinks.
     */
    @NotNull
    private final GameObjectMatcher sinkGameObjectMatcher;

    /**
     * The {@link GameObjectMatcher} for matching sinks.
     */
    @NotNull
    private final GameObjectMatcher sink2GameObjectMatcher;

    /**
     * Create a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param sourceGameObjectMatcher the game object matcher for matching
     * sources
     * @param sinkGameObjectMatcher the game object matcher for matching sinks
     * @param sink2GameObjectMatcher the game object matcher for matching sinks
     */
    public ConnectionChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GameObjectMatcher sourceGameObjectMatcher, @NotNull final GameObjectMatcher sinkGameObjectMatcher, @NotNull final GameObjectMatcher sink2GameObjectMatcher) {
        super(validatorPreferences);
        this.sourceGameObjectMatcher = sourceGameObjectMatcher;
        this.sinkGameObjectMatcher = sinkGameObjectMatcher;
        this.sink2GameObjectMatcher = sink2GameObjectMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final Map<Integer, Rec> info = new HashMap<Integer, Rec>();
        for (final Iterable<G> mapSquare : mapModel) {
            for (final G gameObject : mapSquare) {
                check(gameObject, info);
                for (final G invObject : gameObject.recursive()) {
                    check(invObject, info);
                }
            }
        }

        for (final Map.Entry<Integer, Rec> e : info.entrySet()) {
            e.getValue().addErrors(e.getKey(), mapModel, errorCollector);
        }
    }

    /**
     * Processes a game object.
     * @param gameObject the game object to process
     * @param info the connection info to update
     */
    private void check(@NotNull final G gameObject, @NotNull final Map<Integer, Rec> info) {
        final int[] values = Connections.parseConnections(gameObject);
        if (values == null) {
            return;
        }

        for (final int value : values) {
            getRec(value, info).add(gameObject);
        }
    }

    /**
     * Returns the record that describes a connection value. A new record is
     * created if none exists yet.
     * @param value the connection value
     * @param info the connection info to use
     * @return the record
     */
    private Rec getRec(final int value, @NotNull final Map<Integer, Rec> info) {
        final Rec existingRec = info.get(value);
        if (existingRec != null) {
            return existingRec;
        }

        final Rec rec = new Rec();
        info.put(value, rec);
        return rec;
    }

    /**
     * Returns whether a given game object is a source if connected.
     * @param gameObject the game object
     * @return whether the game object is a source
     */
    private boolean isSource(@NotNull final GameObject<?, ?, ?> gameObject) {
        return sourceGameObjectMatcher.isMatching(gameObject);
    }

    /**
     * Returns whether a given game object is a sink if connected.
     * @param gameObject the game object
     * @return whether the game object is a sink
     */
    private boolean isSink(@NotNull final GameObject<G, A, R> gameObject) {
        return sinkGameObjectMatcher.isMatching(gameObject) || sink2GameObjectMatcher.isMatching(gameObject);
    }

    /**
     * Manages information about one connection set.
     */
    private class Rec {

        /**
         * The source game objects in this connection set.
         */
        @NotNull
        private final Set<G> sourceGameObjects = new HashSet<G>();

        /**
         * The sink game objects in this connection set.
         */
        @NotNull
        private final Set<G> sinkGameObjects = new HashSet<G>();

        /**
         * The game objects in this connection set which are neither sources nor
         * sinks.
         */
        @NotNull
        private final Collection<G> unknowns = new HashSet<G>();

        /**
         * Adds a game object to this connection set.
         * @param gameObject the game object to add
         */
        public void add(@NotNull final G gameObject) {
            boolean isUnknown = true;
            if (isSource(gameObject)) {
                sourceGameObjects.add(gameObject);
                isUnknown = false;
            }
            if (isSink(gameObject)) {
                sinkGameObjects.add(gameObject);
                isUnknown = false;
            }
            if (isUnknown) {
                unknowns.add(gameObject);
            }
        }

        /**
         * Creates validation errors for this connection set.
         * @param connection the connection value
         * @param mapModel the map model
         * @param errorCollector the error collector to add new errors to
         */
        public void addErrors(final int connection, @NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
            if (!unknowns.isEmpty()) {
                for (final G gameObject : unknowns) {
                    errorCollector.collect(new ConnectionUnknownError<G, A, R>(gameObject));
                }
                return;
            }
            if (sourceGameObjects.isEmpty()) {
                errorCollector.collect(new ConnectionWithoutSourcesError<G, A, R>(mapModel, connection, sinkGameObjects));
            }
            if (sinkGameObjects.isEmpty()) {
                errorCollector.collect(new ConnectionWithoutSinksError<G, A, R>(mapModel, connection, sourceGameObjects));
            }
        }

    }

}
