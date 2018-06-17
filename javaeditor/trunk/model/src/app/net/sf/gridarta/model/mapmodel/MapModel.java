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
import java.io.Serializable;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.ErrorCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A MapModel reflects the data of a map. This MapModel interface covers the
 * similarities between the current CFEditor and DaiEditor implementations.
 * <h2>Transaction System</h2> The purpose of the transaction system in MapModel
 * is to allow several subsequent changes to the model to be collected as a
 * single big change before the registered listeners (usually the user
 * interface) gets notified. This prevents the user interface from performing
 * hundreds of updates when a single one would be enough. The transaction system
 * will also be used for implementing undo / redo. <h3>Concurrency</h3> It's not
 * purpose of the transaction system to provide concurrent transactions for
 * concurrent threads. A MapModel will protect itself against concurrent
 * modification. <h3>Performance</h3>
 * <p/>
 * The transaction system is efficient-safe. The following operations are very
 * cheap: <ul> <li>Beginning a nested transaction</li> <li>Ending a nested
 * transaction</li> <li>Ending an outermost transaction without having changed
 * something</li> </ul> Whether beginning the outermost transaction will be a
 * cheap operation is not yet decided.
 * <p/>
 * Transactions are recorded.
 * <p/>
 * It is not (yet?) the purpose of the transaction system to provide real
 * transactions (ACID). Queries to the MapModel during an ongoing transaction
 * will reflect the intermediate state. And there is no rollback yet, but this
 * is planned for future as it's required for undo anyway. <h3>Future</h3> The
 * following features are not yet implemented but planned. <h4>Undo System</h4>
 * The transaction system will serve as a base for an undo / redo system.
 * Beginning the outermost transaction stores a new undo record in the undo
 * buffer. An undo record consists of the transaction's name plus the current
 * map state.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @invariant !isAnyTransactionActive() || getTransactionDepth() > 0
 */
public interface MapModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends Iterable<MapSquare<G, A, R>>, Serializable {

    /**
     * This function must be called if this instance is freed.
     */
    void mapClosed();

    /**
     * Clears this map completely. After invoking this method, no objects will
     * remain on this map.
     */
    void clearMap();

    /**
     * Returns whether the map is empty.
     * @return <code>true</code> if the map is empty, or <code>false</code> if
     *         the map is not empty
     */
    boolean isEmpty();

    /**
     * Returns the Map Arch Object with the meta information about the map.
     * @return the map arch object with the meta information about the map
     */
    @NotNull
    A getMapArchObject();

    /**
     * Register a map listener.
     * @param listener the listener to register
     */
    void addMapModelListener(@NotNull MapModelListener<G, A, R> listener);

    /**
     * Unregister a map listener.
     * @param listener the listener to unregister
     */
    void removeMapModelListener(@NotNull MapModelListener<G, A, R> listener);

    /**
     * Registers a map transaction listener.
     * @param listener the the listener to register
     */
    void addMapTransactionListener(@NotNull MapTransactionListener<G, A, R> listener);

    /**
     * Unregisters a map transaction listener.
     * @param listener the listener to unregister
     */
    void removeMapTransactionListener(@NotNull MapTransactionListener<G, A, R> listener);

    /**
     * Method to notify the model that a map square is about to change.
     * @param mapSquare the map square that is about to change
     */
    void beginSquareChange(@NotNull MapSquare<G, A, R> mapSquare);

    /**
     * Method to notify the model that a map square was changed. A change to a
     * square is atomic if {@link #getTransactionDepth()} returns 0, otherwise
     * it is transactional. The model then notifies the registered listeners of
     * the changes.
     * @param mapSquare the map square that has changed
     */
    void endSquareChange(@NotNull MapSquare<G, A, R> mapSquare);

    /**
     * Method to notify the model that a game object is about to change.
     * @param gameObject the game object that is about to change
     */
    void beginGameObjectChange(@NotNull G gameObject);

    /**
     * Method to notify the model that a game object was changed. A change to a
     * game object is atomic if {@link #getTransactionDepth()} returns 0,
     * otherwise it is transactional. The model then notifies the registered
     * listeners of the changes.
     * @param gameObject the game object that has changed
     */
    void endGameObjectChange(@NotNull G gameObject);

    /**
     * Method to notify the model that a game object was changed but need not be
     * restored by undo/redo. A change to a game object is atomic if {@link
     * #getTransactionDepth()} returns 0, otherwise it is transactional. The
     * model then notifies the registered listeners of the changes.
     * @param gameObject the game object that has changed
     */
    void transientGameObjectChange(@NotNull G gameObject);

    /**
     * Starts a new transaction. Transactions may be nested. Transactions serve
     * the purpose of firing events to the views when more changes are known to
     * come before the view is really required to update. Each invocation of
     * this function requires its own invocation of {@link #endTransaction()}.
     * <p/>
     * A transaction has a name. The name of the outermost transaction is used
     * as a String presented to the user for undoing the operation enclosed by
     * that transaction.
     * <p/>
     * Beginning a nested transaction is a cheap operation.
     * @param name the name of the transaction
     * @see #endTransaction()
     * @see #endTransaction(boolean)
     * @see #endAllTransactions()
     * @see #getTransactionDepth()
     * @see #isAnyTransactionActive()
     */
    void beginTransaction(@NotNull String name);

    /**
     * End a transaction. Invoking this method will reduce the transaction depth
     * by only 1.
     * <p/>
     * Ending a nested operation is a cheap operation. Ending a transaction
     * without changes also is a cheap operation.
     * <p/>
     * If the last transaction is ended, the changes are committed.
     * <p/>
     * Same as {@link #endTransaction(boolean) endTransaction(false)}.
     * @see #beginTransaction(String)
     * @see #endTransaction(boolean)
     * @see #endAllTransactions()
     * @see #getTransactionDepth()
     * @see #isAnyTransactionActive()
     */
    void endTransaction();

    /**
     * End a transaction. Invoking this method will reduce the transaction depth
     * by only 1.
     * <p/>
     * Ending a nested operation is a cheap operation. Ending a transaction
     * without changes also is a cheap operation.
     * <p/>
     * If the last transaction is ended, the changes are committed.
     * <p/>
     * An example where setting <var>fireEvent</var> to <code>true</code> is
     * useful even though the outermost transaction is not ended is when during
     * painting the UI should be updated though painting is not finished.
     * @param fireEvent <code>true</code> if an event should be fired even in
     * case this doesn't end the outermost transaction.
     * @note If the outermost transaction is ended, <var>fireEvent</var> is
     * ignored and the event is always fired.
     * @note An event is never fired when there were no changes, no matter
     * whether the outermost transaction is ended or <var>fireEvent</var> is set
     * to <code>true</code>.
     * @note If the event is fired, the internal change list is not cleared.
     * @see #beginTransaction(String)
     * @see #endTransaction()
     * @see #endAllTransactions()
     * @see #getTransactionDepth()
     * @see #isAnyTransactionActive()
     */
    void endTransaction(boolean fireEvent);

    /**
     * Ends all transaction. Invoking this method will reduce set the
     * transaction depth to 0. You shouldn't invoke this method regularly. It is
     * meant as a fallback in high level methods / exception handlers to prevent
     * errors from causing unclosed transactions.
     * @see #beginTransaction(String)
     * @see #endTransaction()
     * @see #endTransaction(boolean)
     * @see #getTransactionDepth()
     * @see #isAnyTransactionActive()
     */
    void endAllTransactions();

    /**
     * Get the transaction depth, which is the number of {@link
     * #beginTransaction(String)} invocations without matching {@link
     * #endTransaction()} invocations. A transaction depth of 0 means there is
     * no ongoing transaction.
     * @return transaction depth
     * @see #beginTransaction(String)
     * @see #endTransaction()
     * @see #endTransaction(boolean)
     * @see #endAllTransactions()
     * @see #isAnyTransactionActive()
     */
    int getTransactionDepth();

    /**
     * Returns whether a transaction is currently active. This method will
     * return <code>true</code> if and only if {@link #getTransactionDepth()}
     * returns a value greater than zero.
     * @return <code>true</code> if there's a transaction going on, otherwise
     *         <code>false</code>.
     * @see #beginTransaction(String)
     * @see #endTransaction()
     * @see #endTransaction(boolean)
     * @see #endAllTransactions()
     * @see #getTransactionDepth()
     */
    boolean isAnyTransactionActive();

    /**
     * Get the square at a specified location.
     * @param pos location to get square at
     * @return square at <var>p</var>
     * @throws IndexOutOfBoundsException in case p specifies a location that's
     * not valid within this map model
     */
    @NotNull
    MapSquare<G, A, R> getMapSquare(@NotNull Point pos);

    /**
     * Adds a list of {@link GameObject GameObjects} to this map.
     * @param objects the game objects to add
     */
    void addObjectListToMap(@NotNull Iterable<G> objects);

    /**
     * Add a gameObject to the map. Including multi square objects. This
     * function allows to insert any given GameObject. Make sure that the given
     * <var>gameObject</var> is a new and unlinked object.
     * @param gameObject The new GameObject with set destination coordinates to
     * be linked onto the map.
     * @param pos the insert location
     * @param insertionMode the insertion mode to use
     */
    void addGameObjectToMap(@NotNull G gameObject, @NotNull Point pos, @NotNull InsertionMode<G, A, R> insertionMode);

    /**
     * Moves a {@link GameObject} to its environment.
     * @param gameObject the game object to move
     * @param pos the insertion position
     * @param nextGameObject the next game object
     */
    void moveEnv(@NotNull G gameObject, @NotNull Point pos, @NotNull G nextGameObject);

    /**
     * Moves a {@link GameObject} to the inventory of another game object.
     * @param gameObject the game object to move
     * @param prevGameObject the previous game object
     */
    void moveInv(@NotNull G gameObject, @NotNull GameObject<G, A, R> prevGameObject);

    /**
     * Inserts a {@link BaseObject} to a map. Archetypes are instantiated, game
     * objects are cloned. The direction of the inserted game object is set to
     * the direction of the archetype chooser. This function allows multi-square
     * game objects.
     * @param baseObject the base object
     * @param pos the insert-location on this map
     * @param allowMany whether duplicates are allowed
     * @param join if set, auto-joining is supported; autojoining is only done
     * if enabled in the main control
     * @param insertionMode the insertion mode to use
     * @return the inserted game object or <code>null</code> if nothing was
     *         inserted
     */
    @Nullable
    G insertBaseObject(@NotNull BaseObject<G, A, R, ?> baseObject, @NotNull Point pos, boolean allowMany, boolean join, @NotNull InsertionMode<G, A, R> insertionMode);

    /**
     * Insert a game object to the map at a specified position. This function
     * allows either to choose from the archetypes or to insert a copy from an
     * existing game object. It also works for container-inventory.
     * @param templateBaseObject a clone copy of this game object gets inserted
     * to the map; it can be an archetype  of a game object
     * @param nextGameObject the new game object gets inserted before this; if
     * <code>null</code>, the game object gets inserted at bottom
     * @param pos the map position to insert the new game object
     * @param join if set, auto-joining is supported
     * @return the inserted game object, or <code>null</code> if an error
     *         occurred
     */
    @Nullable
    G insertArchToMap(@NotNull BaseObject<G, A, R, ?> templateBaseObject, @Nullable G nextGameObject, @NotNull Point pos, boolean join);

    /**
     * Delete an existing {@link GameObject} from the map.
     * @param gameObject the game object to remove
     * @param join if set, auto-joining is supported
     */
    void removeGameObject(@NotNull G gameObject, boolean join);

    /**
     * Checks whether an GameObject (multi-arch) would still fit on this map.
     * @param archetype the archetype to check
     * @param pos position of multi-square head
     * @param allowDouble whether overlapping multi-square arches should be
     * allowed (check is done using the archetype name)
     * @return whether the multi-arch would still fit on this map
     * @retval <code>true</code> if the multi-square archetype would still fit
     * on this map
     * @retval <code>false</code> otherwise
     */
    boolean isMultiArchFittingToMap(@NotNull Archetype<G, A, R> archetype, @NotNull Point pos, boolean allowDouble);

    /**
     * Sets the errors in this map.
     * @param errors the errors
     */
    void setErrors(@NotNull ErrorCollector<G, A, R> errors);

    /**
     * Gets the errors in this map.
     * @return the errors
     */
    @NotNull
    ErrorCollector<G, A, R> getErrors();

    /**
     * Checks whether an area of a map is completely empty.
     * @param left the left border of the area
     * @param top the top border of the area
     * @param width the with of the area
     * @param height the height of the area
     * @return whether the area is completely empty
     */
    boolean isAreaEmpty(int left, int top, int width, int height);

    /**
     * Add edit type to the bitmask of active types. If this is a new type, it
     * gets calculated for every arch on the map. Once it is calculated, we save
     * that state in 'activeEditType' so we don't need to do it again.
     * @param editType new edit type
     */
    void addActiveEditType(int editType);

    /**
     * Sets the map file.
     * @param mapFile the map file or <code>null</code> if the map has not yet
     * been saved
     */
    void setMapFile(@Nullable File mapFile);

    /**
     * Returns the map file.
     * @return the map file or <code>null</code> if the map has not yet been
     *         saved
     */
    @Nullable
    File getMapFile();

    /**
     * Returns all game objects. Only top-level head parts are returned; tail
     * parts are ignored as are objects in inventories.
     * @return all game objects
     */
    @NotNull
    List<G> getAllGameObjects();

    /**
     * Return whether the map has been modified from the on-disk state.
     * @return <code>true</code> if the map has been modified from the on-disk
     *         state
     */
    boolean isModified();

    /**
     * Resets the modified flag to false.
     */
    void resetModified();

    /**
     * Will be called whenever the archetype faces have been reloaded.
     */
    void facesReloaded();

    /**
     * Moves the given point forward or backward one map square.
     * @param point the point to start with and modify
     * @param direction the direction (<code>-1</code> or <code>+1</code>)
     */
    void nextPoint(Point point, int direction);

}
