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

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.autojoin.InsertionResult;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectListener;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.model.validation.DefaultErrorCollector;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Size2D;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of {@link MapModel} that covers the similarities between
 * crossfire and daimonin. The transaction system is not implemented in a way
 * that supports overriding its method for change. Therefore all methods
 * belonging to the transaction system are final.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @see MapModel class MapModel for a documentation of the transaction system.
 */
public class DefaultMapModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapModel<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DefaultMapModel.class);

    /**
     * Sync Lock Object.
     */
    private final transient Object syncLock = new Object();

    /**
     * The MapArchObject associated with this model.
     */
    @NotNull
    private final A mapArchObject;

    /**
     * The {@link AutojoinLists} for performing autojoining.
     */
    @NotNull
    private final AutojoinLists<G, A, R> autojoinLists;

    /**
     * The {@link ArchetypeChooserModel} to use when inserting directional game
     * objects.
     */
    @NotNull
    private final ArchetypeChooserModel<G, A, R> archetypeChooserModel;

    /**
     * The map, containing all arches grid-wise. Index: [width][height]
     */
    @NotNull
    private final MapSquareGrid<G, A, R> mapGrid;

    /**
     * The registered {@link MapModelListener MapModelListeners}.
     */
    @NotNull
    private final EventListenerList2<MapModelListener<G, A, R>> mapModelListeners = new EventListenerList2<MapModelListener<G, A, R>>(MapModelListener.class);

    /**
     * The registered {@link MapTransactionListener MapTransactionListeners}.
     */
    @NotNull
    private final EventListenerList2<MapTransactionListener<G, A, R>> mapTransactionListeners = new EventListenerList2<MapTransactionListener<G, A, R>>(MapTransactionListener.class);

    /**
     * The transaction depth. A value of 0 means there's no transaction going
     * on. A value &gt; 0 means there's a transaction going on and denotes the
     * nesting level.
     * @invariant transactionDepth &gt;= 0
     */
    private int transactionDepth;

    /**
     * The thread that performs the current transaction.
     * @invariant transactionDepth &gt; 0 || transactionThread == null
     */
    @Nullable
    private transient Thread transactionThread;

    /**
     * The ArrayList with changed squares.
     */
    @NotNull
    private final Set<MapSquare<G, A, R>> changedSquares = new HashSet<MapSquare<G, A, R>>();

    /**
     * Records unchanged square contents for all squares in {@link
     * #changedSquares}.
     */
    @NotNull
    private final SavedSquares<G, A, R> savedSquares;

    /**
     * The ArrayList with changed gameObjects.
     */
    @NotNull
    private final Set<G> changedGameObjects = new HashSet<G>();

    /**
     * The ArrayList with transient changed gameObjects.
     */
    @NotNull
    private final Set<G> transientChangedGameObjects = new HashSet<G>();

    /**
     * The errors of this map model.
     */
    @NotNull
    private ErrorCollector<G, A, R> errors = new DefaultErrorCollector<G, A, R>();

    /**
     * Contains the edit types that have already been (requested and) calculated
     * (edit types get calculated only when needed to save time).
     */
    private int activeEditType;

    /**
     * The {@link GameObjectFactory} for creating {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link GameObjectMatchers} to use.
     */
    @NotNull
    private final GameObjectMatchers gameObjectMatchers;

    /**
     * The "topmost" {@link InsertionMode}.
     */
    @NotNull
    private final InsertionMode<G, A, R> topmostInsertionMode;

    /**
     * The backing map file. Set to <code>null</code> if the map has not yet
     * been saved.
     */
    @Nullable
    private File mapFile;

    /**
     * Set if the map has changed since last save.
     */
    private boolean modified;

    /**
     * The {@link LightMapModelTracker} tracking this instance.
     */
    @NotNull
    private final LightMapModelTracker<G, A, R> lightMapModelTracker = new LightMapModelTracker<G, A, R>(this);

    /**
     * The {@link MapArchObjectListener} used to detect changes in {@link
     * #mapArchObject} and set the {@link #modified} flag accordingly.
     */
    private final transient MapArchObjectListener mapArchObjectListener = new MapArchObjectListener() {

        @Override
        public void mapMetaChanged() {
            setModified();
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D mapSize) {
            resizeMapInt(mapSize);
        }

    };

    /**
     * Create a new instance.
     * @param autojoinLists the autojoin lists instance to use
     * @param mapArchObject the map arch object to associate with this model
     * @param archetypeChooserModel the archetype chooser control
     * @param activeEditType the active edit types
     * @param gameObjectFactory the game object factory for creating game
     * objects
     * @param gameObjectMatchers the game object matchers to use
     * @param topmostInsertionMode the "topmost" insertion mode
     */
    public DefaultMapModel(@NotNull final AutojoinLists<G, A, R> autojoinLists, @NotNull final A mapArchObject, @NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, final int activeEditType, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final InsertionMode<G, A, R> topmostInsertionMode) {
        this.mapArchObject = mapArchObject;
        this.autojoinLists = autojoinLists;
        this.archetypeChooserModel = archetypeChooserModel;
        this.activeEditType = activeEditType;
        this.gameObjectFactory = gameObjectFactory;
        this.gameObjectMatchers = gameObjectMatchers;
        this.topmostInsertionMode = topmostInsertionMode;
        savedSquares = new SavedSquares<G, A, R>(gameObjectFactory, gameObjectMatchers);
        mapArchObject.addMapArchObjectListener(mapArchObjectListener);
        mapGrid = new MapSquareGrid<G, A, R>(this, mapArchObject.getMapSize());
        modified = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mapClosed() {
        mapArchObject.removeMapArchObjectListener(mapArchObjectListener);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public A getMapArchObject() {
        return mapArchObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<MapSquare<G, A, R>> iterator() {
        return new MapSquareIterator<G, A, R>(this, null, 1, false);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapSquare<G, A, R> getMapSquare(@NotNull final Point pos) {
        return mapGrid.getMapSquare(pos.x, pos.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObjectListToMap(@NotNull final Iterable<G> objects) {
        for (final G gameObject : objects) {
            if (!gameObject.isInContainer()) { // only map arches....
                addGameObjectToMap(gameObject, new Point(gameObject.getMapX(), gameObject.getMapY()), topmostInsertionMode);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearMap() {
        mapGrid.clearMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return mapGrid.isEmpty();
    }

    /**
     * Resizes the map grid after the map size has changed.
     * @param newSize the new map size
     */
    private void resizeMapInt(@NotNull final Size2D newSize) {
        final Size2D mapSize = mapGrid.getMapSize();
        if (newSize.equals(mapSize)) {
            return;
        }

        final Collection<GameObject<G, A, R>> objectsToDelete = new HashSet<GameObject<G, A, R>>();

        // no other thread may access this map model while resizing
        synchronized (syncLock) {
            lightMapModelTracker.mapSizeChanging(newSize, mapSize);

            // first delete all arches in the area that will get cut off
            // (this is especially important to remove all multi-part objects
            // reaching into that area)
            if (mapSize.getWidth() > newSize.getWidth()) {
                // clear out the right stripe (as far as being cut off)
                mapGrid.collectHeads(newSize.getWidth(), 0, mapSize.getWidth(), mapSize.getHeight(), objectsToDelete);
            }

            if (mapSize.getHeight() > newSize.getHeight()) {
                // clear out the bottom stripe (as far as being cut off)
                mapGrid.collectHeads(0, newSize.getHeight(), Math.min(mapSize.getWidth(), newSize.getWidth()), mapSize.getHeight(), objectsToDelete);
            }

            for (final GameObject<G, A, R> node : objectsToDelete) {
                node.remove();
            }

            mapGrid.resize(newSize);
            discardInvalidMapSquares(changedSquares, newSize);
            discardInvalidGameObjects(changedGameObjects, newSize);
            discardInvalidGameObjects(transientChangedGameObjects, newSize);

            fireMapSizeChanged(newSize);
        }
    }

    /**
     * Discards map squares that are out of map bounds.
     * @param mapSquares the map squares to check
     * @param mapSize the new map size
     */
    private void discardInvalidMapSquares(@NotNull final Collection<MapSquare<G, A, R>> mapSquares, @NotNull final Size2D mapSize) {
        final Iterator<MapSquare<G, A, R>> it = mapSquares.iterator();
        while (it.hasNext()) {
            final MapSquare<G, A, R> mapSquare = it.next();
            if (mapSquare.getMapX() >= mapSize.getWidth() || mapSquare.getMapY() >= mapSize.getHeight()) {
                it.remove();
            }
        }
    }

    /**
     * Discards game objects that are out of map bounds.
     * @param gameObjects the game objects to check
     * @param mapSize the new map size
     */
    private void discardInvalidGameObjects(@NotNull final Iterable<G> gameObjects, @NotNull final Size2D mapSize) {
        final Iterator<G> it2 = gameObjects.iterator();
        while (it2.hasNext()) {
            final GameObject<G, A, R> gameObject = it2.next();
            final G topGameObject = gameObject.getTopContainer();
            if (topGameObject.getContainer() == null || topGameObject.getMapX() >= mapSize.getWidth() || topGameObject.getMapY() >= mapSize.getHeight()) {
                it2.remove();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapModelListener(@NotNull final MapModelListener<G, A, R> listener) {
        mapModelListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapModelListener(@NotNull final MapModelListener<G, A, R> listener) {
        mapModelListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapTransactionListener(@NotNull final MapTransactionListener<G, A, R> listener) {
        mapTransactionListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapTransactionListener(@NotNull final MapTransactionListener<G, A, R> listener) {
        mapTransactionListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginSquareChange(@NotNull final MapSquare<G, A, R> mapSquare) {
        if (transactionDepth == 0) {
            log.error("beginSquareChange: square (" + mapSquare + ") is about to change outside a transaction");
            return;
        }

        savedSquares.recordMapSquare(mapSquare);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endSquareChange(@NotNull final MapSquare<G, A, R> mapSquare) {
        if (transactionDepth == 0) {
            log.error("endSquareChange: square (" + mapSquare + ") was changed outside a transaction");
            final Set<MapSquare<G, A, R>> mapSquares = new HashSet<MapSquare<G, A, R>>(1);
            mapSquares.add(mapSquare);
            lightMapModelTracker.mapSquaresChanged(Collections.unmodifiableCollection(mapSquares));
            fireMapSquaresChangedEvent(mapSquares);
        } else {
            synchronized (changedSquares) {
                changedSquares.add(mapSquare);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginGameObjectChange(@NotNull final G gameObject) {
        if (transactionDepth == 0) {
            log.error("beginGameObjectChange: game object (" + gameObject.getBestName() + "@" + gameObject.getMapSquare() + ") is about to change outside a transaction");
            return;
        }

        final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
        assert mapSquare != null;
        savedSquares.recordMapSquare(mapSquare);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGameObjectChange(@NotNull final G gameObject) {
        if (transactionDepth == 0) {
            log.error("endGameObjectChange: game object (" + gameObject.getBestName() + "@" + gameObject.getMapSquare() + ") was changed outside a transaction");
            final Set<G> gameObjects = new HashSet<G>(1);
            gameObjects.add(gameObject);
            fireGameObjectsChangedEvent(gameObjects, Collections.<G>emptySet());
        } else {
            synchronized (changedGameObjects) {
                changedGameObjects.add(gameObject);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transientGameObjectChange(@NotNull final G gameObject) {
        if (transactionDepth == 0) {
            log.error("transientGameObjectChange: game object (" + gameObject.getBestName() + "@" + gameObject.getMapSquare() + ") was changed outside a transaction");
            final Set<G> gameObjects = new HashSet<G>(1);
            gameObjects.add(gameObject);
            fireGameObjectsChangedEvent(Collections.<G>emptySet(), gameObjects);
        } else {
            synchronized (transientChangedGameObjects) {
                transientChangedGameObjects.add(gameObject);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginTransaction(@NotNull final String name) {
        if (transactionDepth == 0) {
            firePreBeginTransaction();
            transactionThread = Thread.currentThread();
            transactionDepth++;
            fireBeginTransaction(name);
        } else {
            // == is okay for threads.
            //noinspection ObjectEquality
            if (transactionThread != Thread.currentThread()) {
                throw new IllegalStateException("A transaction must only be used by one thread.");
            }
            transactionDepth++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endTransaction() {
        endTransaction(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endTransaction(final boolean fireEvent) {
        if (transactionDepth <= 0) {
            throw new IllegalStateException("Tried to end a transaction but no transaction was open.");
        }
        transactionDepth--;
        assert transactionDepth >= 0;
        if (transactionDepth == 0) {
            commitTransaction();
        } else if (fireEvent && transactionDepth == 1) {
            fireEvents();
        }
    }

    /**
     * Deliver all pending events.
     */
    private void fireEvents() {
        // Call lightMapModelTracker first as it might change more game objects
        transactionDepth++; // temporarily increase transaction depth because updating light information causes changes
        try {
            // Create copy to avoid ConcurrentModificationExceptions due to newly changed squares
            final Collection<MapSquare<G, A, R>> mapSquares = new HashSet<MapSquare<G, A, R>>(changedSquares);
            for (final G gameObject : changedGameObjects) {
                final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
                if (mapSquare != null) {
                    mapSquares.add(mapSquare);
                }
            }
            lightMapModelTracker.mapSquaresChanged(mapSquares);
        } finally {
            transactionDepth--;
        }
        if (!changedGameObjects.isEmpty() || !transientChangedGameObjects.isEmpty()) {
            transientChangedGameObjects.removeAll(changedGameObjects);
            transactionDepth++; // temporarily increase transaction depth because updating edit types causes transient changes
            try {
                for (final G gameObject : changedGameObjects) {
                    if (gameObject.isHead()) {
                        for (G env = gameObject; env != null; env = env.getContainerGameObject()) {
                            updateEditType(env, activeEditType);
                        }
                    }
                }
            } finally {
                transactionDepth--;
            }
            fireGameObjectsChangedEvent(Collections.unmodifiableSet(changedGameObjects), Collections.unmodifiableSet(transientChangedGameObjects));
            changedGameObjects.clear();
        }
        if (!changedSquares.isEmpty()) {
            fireMapSquaresChangedEvent(Collections.unmodifiableSet(changedSquares));
            changedSquares.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endAllTransactions() {
        if (transactionDepth > 0) {
            commitTransaction();
        }
    }

    /**
     * Performs ending a transaction. Resets all transaction states and fires an
     * event.
     */
    private void commitTransaction() {
        transactionDepth = 0;
        transactionThread = null;
        fireEvents();
        fireEndTransaction();
        firePostEndTransaction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTransactionDepth() {
        return transactionDepth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAnyTransactionActive() {
        return transactionDepth > 0;
    }

    /**
     * Fire a MapSquaresChangedEvent.
     * @param mapSquares Squares to fire event for
     */
    private void fireMapSquaresChangedEvent(final Set<MapSquare<G, A, R>> mapSquares) {
        setModified();
        for (final MapModelListener<G, A, R> listener : mapModelListeners.getListeners()) {
            listener.mapSquaresChanged(mapSquares);
        }
    }

    /**
     * Fire a GameObjectsChangedEvent.
     * @param gameObjects game objects to fire event for
     * @param transientGameObjects transient game objects to fire event for
     */
    private void fireGameObjectsChangedEvent(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
        if (!gameObjects.isEmpty()) {
            setModified();
        }
        for (final MapModelListener<G, A, R> listener : mapModelListeners.getListeners()) {
            listener.mapObjectsChanged(gameObjects, transientGameObjects);
        }
    }

    /**
     * Fire a map size changed event.
     * @param newSize the new map size
     */
    private void fireMapSizeChanged(@NotNull final Size2D newSize) {
        setModified();
        for (final MapModelListener<G, A, R> listener : mapModelListeners.getListeners()) {
            listener.mapSizeChanged(newSize);
        }
    }

    /**
     * Fire a pre-begin transaction event.
     */
    private void firePreBeginTransaction() {
        for (final MapTransactionListener<G, A, R> listener : mapTransactionListeners.getListeners()) {
            listener.preBeginTransaction();
        }
    }

    /**
     * Fire a begin transaction event.
     * @param name the transaction name
     */
    private void fireBeginTransaction(@NotNull final String name) {
        savedSquares.clear();
        for (final MapTransactionListener<G, A, R> listener : mapTransactionListeners.getListeners()) {
            listener.beginTransaction(this, name);
        }
    }

    /**
     * Fire an end transaction event.
     */
    private void fireEndTransaction() {
        for (final MapTransactionListener<G, A, R> listener : mapTransactionListeners.getListeners()) {
            listener.endTransaction(this, savedSquares);
        }
        savedSquares.clear();
    }

    /**
     * Fire a post-end transaction event.
     */
    private void firePostEndTransaction() {
        for (final MapTransactionListener<G, A, R> listener : mapTransactionListeners.getListeners()) {
            listener.postEndTransaction();
        }
    }

    /**
     * Fires a map file changed event.
     * @param oldMapFile the map file before the change
     */
    private void fireMapFileChanged(@Nullable final File oldMapFile) {
        for (final MapModelListener<G, A, R> listener : mapModelListeners.getListeners()) {
            listener.mapFileChanged(oldMapFile);
        }
    }

    /**
     * Fires a map size changed event.
     */
    private void fireModifiedChanged() {
        for (final MapModelListener<G, A, R> listener : mapModelListeners.getListeners()) {
            listener.modifiedChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeGameObject(@NotNull final G gameObject, final boolean join) {
        final MapSquare<?, ?, ?> mapSquare = gameObject.getMapSquare();
        assert mapSquare != null;
        if (gameObject.isInContainer()) {
            gameObject.remove();
        } else {
            gameObject.remove();

            if (join) {
                autojoinLists.joinDelete(this, mapSquare.getMapLocation(), gameObject.getArchetype());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMultiArchFittingToMap(@NotNull final Archetype<G, A, R> archetype, @NotNull final Point pos, final boolean allowDouble) {
        for (Archetype<G, A, R> part = archetype; part != null; part = part.getMultiNext()) {
            final Point point = new Point(part.getMultiX(), part.getMultiY());
            point.translate(pos.x, pos.y);
            // outside map
            if (!mapArchObject.isPointValid(point)) {
                return false;
            }

            if (!allowDouble) {
                final String temp = part.getArchetypeName();
                for (final BaseObject<G, A, R, ?> node : mapGrid.getMapSquare(point.x, point.y)) {
                    if (node.getArchetype().getArchetypeName().equals(temp)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrors(@NotNull final ErrorCollector<G, A, R> errors) {
        this.errors = errors;
        for (final MapModelListener<G, A, R> listener : mapModelListeners.getListeners()) {
            listener.errorsChanged(errors);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ErrorCollector<G, A, R> getErrors() {
        return errors;
    }

    /**
     * {@inheritDoc}
     * @xxx I'm too complex
     */
    @Nullable
    @Override
    public G insertArchToMap(@NotNull final BaseObject<G, A, R, ?> templateBaseObject, @Nullable final G nextGameObject, @NotNull final Point pos, final boolean join) {
        // map coordinates must be valid
        if (!mapArchObject.isPointValid(pos)) {
            return null;
        }

        final G newGameObject;
        final GameObject<G, A, R> nextGameObjectEnv = nextGameObject == null ? null : nextGameObject.getContainerGameObject();
        if (nextGameObject == null || nextGameObjectEnv == null) {
            newGameObject = insertBaseObject(templateBaseObject, pos, true, join, topmostInsertionMode);
            if (newGameObject == null) {
                return null;
            }

            int position = 0;
            for (final G gameObject : mapGrid.getMapSquare(pos.x, pos.y).reverse()) {
                // This is okay because nextGameObject is on the desired square.
                //noinspection ObjectEquality
                if (gameObject == nextGameObject) {
                    break;
                }
                position++;
            }
            for (int i = 0; i < position - 1; i++) {
                newGameObject.moveDown();
            }
        } else {
            newGameObject = templateBaseObject.newInstance(gameObjectFactory);
            nextGameObjectEnv.addLast(newGameObject);
            if (templateBaseObject instanceof Archetype) {
                gameObjectFactory.createInventory(newGameObject, templateBaseObject);
            }
        }

        return newGameObject;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G insertBaseObject(@NotNull final BaseObject<G, A, R, ?> baseObject, @NotNull final Point pos, final boolean allowMany, final boolean join, @NotNull final InsertionMode<G, A, R> insertionMode) {
        if (!mapArchObject.isPointValid(pos)) {
            return null;
        }

        final R realArchetype = baseObject.getArchetype();
        final R effectiveArchetype;
        if (join) {
            final InsertionResult<G, A, R> insertionResult = autojoinLists.joinInsert(this, pos, realArchetype);
            final G gameObject = insertionResult.getGameObject();
            if (gameObject != null) {
                return gameObject;
            }

            effectiveArchetype = insertionResult.getArchetype();
            if (effectiveArchetype == null) {
                return null; // only one autojoin type per square allowed
            }
        } else {
            effectiveArchetype = realArchetype;
        }

        if (!isMultiArchFittingToMap(effectiveArchetype, pos, allowMany)) {
            return null;
        }

        final Integer direction = effectiveArchetype.usesDirection() ? archetypeChooserModel.getDirection() : null;

        final List<G> parts = new ArrayList<G>();
        for (R archetypePart = effectiveArchetype; archetypePart != null; archetypePart = archetypePart.getMultiNext()) {
            final G part;
            if (archetypePart == effectiveArchetype) {
                part = baseObject.newInstance(gameObjectFactory);
                part.setArchetype(archetypePart);
            } else {
                part = archetypePart.newInstance(gameObjectFactory);
            }
            if (direction != null) {
                part.setAttributeInt(BaseObject.DIRECTION, direction);
            }
            if (!parts.isEmpty()) {
                parts.get(0).addTailPart(part);
            }
            parts.add(part);
        }

        for (final G part : parts) {
            final int mapX = pos.x + part.getArchetype().getMultiX();
            final int mapY = pos.y + part.getArchetype().getMultiY();
            insertionMode.insert(part, mapGrid.getMapSquare(mapX, mapY));
        }

        final G head = parts.get(0);
        if (baseObject instanceof Archetype) {
            gameObjectFactory.createInventory(head, effectiveArchetype);
        }

        return head;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGameObjectToMap(@NotNull final G gameObject, @NotNull final Point pos, @NotNull final InsertionMode<G, A, R> insertionMode) {
        if (!mapArchObject.isPointValid(pos)) {
            log.error("addGameObjectToMap: trying to insert game object out of map bounds at " + pos.x + "/" + pos.y + ", map bounds is " + mapArchObject.getMapSize());
            return;
        }

        insertionMode.insert(gameObject, mapGrid.getMapSquare(pos.x, pos.y));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveEnv(@NotNull final G gameObject, @NotNull final Point pos, @NotNull final G nextGameObject) {
        if (!nextGameObject.isHead()) {
            throw new IllegalArgumentException();
        }
        assert !gameObject.isMulti(); // no tail parts should be present when inside inventory of another game object
        final GameObjectContainer<G, A, R> nextGameObjectContainer;
        if (nextGameObject.isInContainer()) {
            nextGameObjectContainer = nextGameObject.getContainer();
            if (nextGameObjectContainer == null) {
                throw new IllegalArgumentException();
            }
        } else {
            nextGameObjectContainer = getMapSquare(pos);
        }
        final MapSquare<G, A, R> mapSquare = nextGameObjectContainer.getMapSquare();
        if (mapSquare == null || mapSquare.getMapModel() != this) {
            throw new IllegalArgumentException();
        }
        gameObject.remove();
        nextGameObjectContainer.insertAfter(nextGameObject, gameObject);

        // regenerate tail parts when inserted into a map square
        if (!nextGameObject.isInContainer() && gameObject.getArchetype().isMulti()) {
            final Point tmp = new Point();
            for (R archetypeTail = gameObject.getArchetype().getMultiNext(); archetypeTail != null; archetypeTail = archetypeTail.getMultiNext()) {
                final G gameObjectTail = archetypeTail.newInstance(gameObjectFactory);
                gameObject.addTailPart(gameObjectTail);
                tmp.x = pos.x + archetypeTail.getMultiX();
                tmp.y = pos.y + archetypeTail.getMultiY();
                addGameObjectToMap(gameObjectTail, tmp, topmostInsertionMode);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveInv(@NotNull final G gameObject, @NotNull final GameObject<G, A, R> prevGameObject) {
        if (!gameObject.isHead() || !prevGameObject.isHead()) {
            throw new IllegalArgumentException();
        }

        gameObject.remove();
        gameObject.removeTailParts();
        prevGameObject.addFirst(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAreaEmpty(final int left, final int top, final int width, final int height) {
        final Point point = new Point();
        for (int x = left; x < left + width; x++) {
            point.x = x;
            for (int y = top; y < top + height; y++) {
                point.y = y;
                if (!getMapSquare(point).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addActiveEditType(final int editType) {
        // calculate only if needed
        if ((activeEditType & editType) != editType) {
            beginTransaction("update edit types");
            try {
                for (final Iterable<G> mapSquare : this) {
                    for (final GameObject<G, A, R> gameObject : mapSquare) {
                        updateEditType(gameObject.getHead(), editType);
                    }
                }
            } finally {
                endTransaction();
            }
            // from now on we have this type, so we don't have to calculate it again
            activeEditType |= editType;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapFile(@Nullable final File mapFile) {
        if (this.mapFile == null ? mapFile == null : this.mapFile.equals(mapFile)) {
            return;
        }

        final File oldMapFile = this.mapFile;
        this.mapFile = mapFile;
        fireMapFileChanged(oldMapFile);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public File getMapFile() {
        return mapFile;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<G> getAllGameObjects() {
        final List<G> gameObjects = new ArrayList<G>();
        for (final Iterable<G> mapSquare : this) {
            for (final G gameObject : mapSquare) {
                if (gameObject.isHead()) {
                    gameObjects.add(gameObject);
                }
            }
        }
        return gameObjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetModified() {
        if (!modified) {
            return;
        }

        modified = false;
        fireModifiedChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void facesReloaded() {
        beginTransaction("reload faces");
        try {
            for (final Iterable<G> mapSquare : this) {
                for (final G gameObject : mapSquare) {
                    gameObject.facesReloaded();
                }
            }
        } finally {
            endTransaction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextPoint(final Point point, final int direction) {
        final Size2D mapSize = mapArchObject.getMapSize();
        if (direction > 0) {
            point.x++;
            if (point.x >= mapSize.getWidth()) {
                point.x = 0;
                point.y++;
                if (point.y >= mapSize.getHeight()) {
                    point.y = 0;
                }
            }
        } else {
            point.x--;
            final int mapHeight = mapSize.getHeight();
            if (point.x < 0) {
                point.x = mapSize.getWidth() - 1;
                point.y--;
                if (point.y < 0) {
                    point.y = mapSize.getHeight() - 1;
                }
            }
        }
    }

    /**
     * Marks the map as being modified.
     */
    private void setModified() {
        if (modified) {
            return;
        }

        modified = true;
        fireModifiedChanged();
    }

    /**
     * Updates the edit type of a {@link GameObject}. These are determined by
     * various attributes of the game object.
     * @param gameObject the game object
     * @param checkType bitmask containing the edit type(s) to be calculated
     */
    private void updateEditType(@NotNull final GameObject<G, A, R> gameObject, final int checkType) {
        assert gameObject.isHead();
        if (checkType == 0) {
            return;
        }

        // all flags from 'checkType' must be unset in this game object because
        // they get recalculated now
        final int editType = gameObject.getEditType();
        final int retainedEditType = editType == BaseObject.EDIT_TYPE_NONE ? 0 : editType & ~checkType;
        final int newEditType = retainedEditType | calculateEditType(gameObject, checkType);
        gameObject.setEditType(newEditType);
    }

    /**
     * Returns the edit type for a {@link GameObject}.
     * @param gameObject the game object
     * @param checkType the edit type to calculate
     * @return the edit type
     */
    private int calculateEditType(@NotNull final GameObject<?, ?, ?> gameObject, final int checkType) {
        int editType = 0;
        for (final NamedGameObjectMatcher matcher : gameObjectMatchers) {
            final int matcherEditType = matcher.getEditType();
            if ((matcherEditType & checkType) != 0 && matcher.isMatching(gameObject)) {
                editType |= matcherEditType;
            }
        }
        return editType;
    }

}
