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

package net.sf.gridarta.model.index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An index of values. Values are associated with a timestamp and zero or one
 * name. The timestamp is used to mark values as pending whenever they have
 * changed. Pending values may be retrieved for re-indexing.
 * <p/>
 * Values may be looked up by partial names.
 * <p/>
 * All methods are synchronized: concurrent access from multiple threads is
 * supported.
 * @param <V> the value's type
 * @author Andreas Kirschbaum
 */
public interface Index<V> {

    /**
     * Returns the number of values in this cache.
     * @return the number of values
     */
    int size();

    /**
     * Returns all matching values for a (possibly partial) key name.
     * @param name the partial name to look up
     * @return the matching values
     */
    @NotNull
    Collection<V> findPartialName(@NotNull String name);

    /**
     * Starts an update. Updates do not nest. This function and {@link
     * #endUpdate()} should be called from only a single thread.
     */
    void beginUpdate();

    /**
     * Ends an update. Removes all values from the index that were not {@link
     * #add(Object, long) added} since the last call to {@link #beginUpdate()}.
     * Must not be called unless a directly preceding call to
     * <code>beginUpdate()</code> was made.
     */
    void endUpdate();

    /**
     * Adds a value to the cache. Newly added values and existing values for
     * which the new timestamp does not match the old timestamp are marked as
     * pending.
     * @param value the value to add
     * @param timestamp a timestamp associated with the value
     */
    void add(@NotNull V value, long timestamp);

    /**
     * Removes a value from the cache. Does nothing if the values does not
     * exist.
     * @param value the value to remove
     */
    void remove(@NotNull V value);

    /**
     * Marks a value as pending. The value is added if it does not exist.
     * @param value the value to mark pending
     */
    void setPending(@NotNull V value);

    /**
     * Associates a value with a name. Adds the value if it does not exist.
     * @param value the value to associate with a name
     * @param timestamp the value's timestamp
     * @param name the name associated with the value
     */
    void setName(@NotNull V value, long timestamp, @NotNull String name);

    /**
     * Returns the name associated with a value.
     * @param value the value
     * @return the name or <code>null</code> if the value does not exist or has
     *         no associated name
     */
    @Nullable
    String getName(@NotNull V value);

    /**
     * Returns one pending value.
     * @return a pending value or <code>null</code> if no pending values exist
     */
    @Nullable
    V removePending();

    /**
     * Returns whether the state was modified since last save.
     * @return whether the state was modified
     */
    boolean isModified();

    /**
     * Adds an {@link IndexListener} to be notified of changes.
     * @param listener the index listener to add
     */
    void addIndexListener(@NotNull IndexListener<V> listener);

    /**
     * Removes an {@link IndexListener} to be notified of changes.
     * @param listener the index listener to remove
     */
    void removeIndexListener(@NotNull IndexListener<V> listener);

    /**
     * Saves the state to an {@link ObjectOutputStream}. Pending maps are not
     * saved.
     * @param objectOutputStream the object output stream to write to
     * @throws IOException if an I/O error occurs
     */
    void save(@NotNull ObjectOutputStream objectOutputStream) throws IOException;

    /**
     * Restores the state from an {@link ObjectInputStream}. The previous state
     * is overwritten.
     * @param objectInputStream the input stream to read from
     * @throws IOException if an I/O error occurs
     */
    void load(@NotNull ObjectInputStream objectInputStream) throws IOException;

    /**
     * Clears all values from the index.
     */
    void clear();

    /**
     * Should be called after indexing has finished.
     */
    void indexingFinished();

}
