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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link Index} implementations.
 * @param <V> the value's type
 * @author Andreas Kirschbaum
 */
public class AbstractIndex<V extends Serializable> implements Index<V> {

    /**
     * Objects used to synchronize accesses to other fields.
     * @serial
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * The registered listeners.
     */
    @NotNull
    private final EventListenerList2<IndexListener<V>> indexListeners = new EventListenerList2<IndexListener<V>>(IndexListener.class);

    /**
     * Maps value to timestamp.
     * @serial
     */
    @NotNull
    private final Map<V, Long> timestamps = new HashMap<V, Long>();

    /**
     * Maps value to name.
     * @serial
     */
    @NotNull
    private final Map<V, String> names = new HashMap<V, String>();

    /**
     * Pending values.
     * @serial
     */
    @NotNull
    private final Collection<V> pending = new HashSet<V>();

    /**
     * Whether the state ({@link #timestamps} or {@link #names}) was modified
     * since last save.
     */
    private boolean modified;

    /**
     * Whether a transaction is active.
     */
    private boolean transaction;

    /**
     * The values to delete at the end of the current transaction. Empty if no
     * transaction is active.
     * @serial
     */
    @NotNull
    private final Collection<V> transactionDelete = new HashSet<V>();

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return timestamps.size();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Collection<V> findPartialName(@NotNull final String name) {
        synchronized (sync) {
            final String nameLowerCase = name.toLowerCase();
            final Collection<V> result = new HashSet<V>();
            for (final Entry<V, String> e : names.entrySet()) {
                if (e.getValue().toLowerCase().contains(nameLowerCase)) {
                    result.add(e.getKey());
                }
            }
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginUpdate() {
        synchronized (sync) {
            transaction = true;
            transactionDelete.clear();
            transactionDelete.addAll(timestamps.keySet());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endUpdate() {
        synchronized (sync) {
            if (!transaction) {
                throw new IllegalStateException();
            }
            transaction = false;

            final Collection<V> tmp = new ArrayList<V>(transactionDelete);
            transactionDelete.clear();
            if (!tmp.isEmpty()) {
                modified = true;
                for (final V value : tmp) {
                    timestamps.remove(value);
                    pending.remove(value);
                    names.remove(value);
                }
                for (final V value : tmp) {
                    for (final IndexListener<V> listener : indexListeners.getListeners()) {
                        listener.valueRemoved(value);
                        listener.nameChanged();
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(@NotNull final V value, final long timestamp) {
        synchronized (sync) {
            final Long oldTimestamp = timestamps.put(value, timestamp);
            final boolean notifyPending;
            final boolean notifyAdded;
            if (oldTimestamp == null || oldTimestamp != timestamp) {
                notifyPending = pending.add(value) && pending.size() == 1;
                notifyAdded = oldTimestamp == null;
                modified = true;
            } else {
                notifyPending = false;
                notifyAdded = false;
            }
            transactionDelete.remove(value);
            if (notifyAdded) {
                for (final IndexListener<V> listener : indexListeners.getListeners()) {
                    listener.valueAdded(value);
                }
            }
            if (notifyPending) {
                for (final IndexListener<V> listener : indexListeners.getListeners()) {
                    listener.pendingChanged();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull final V value) {
        synchronized (sync) {
            if (timestamps.remove(value) == null) {
                return;
            }
            modified = true;
            pending.remove(value);
            names.remove(value);
            for (final IndexListener<V> listener : indexListeners.getListeners()) {
                listener.valueRemoved(value);
                listener.nameChanged();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPending(@NotNull final V value) {
        synchronized (sync) {
            final boolean notifyPending = pending.add(value) && pending.size() == 1;
            if (!timestamps.containsKey(value)) {
                modified = true;
                timestamps.put(value, 0L);
                for (final IndexListener<V> listener : indexListeners.getListeners()) {
                    listener.valueAdded(value);
                }
            }
            if (notifyPending) {
                for (final IndexListener<V> listener : indexListeners.getListeners()) {
                    listener.pendingChanged();
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(@NotNull final V value, final long timestamp, @NotNull final String name) {
        synchronized (sync) {
            timestamps.put(value, timestamp);
            final String oldName = names.put(value, name);
            if (oldName != null && oldName.equals(name)) {
                return;
            }
            modified = true;
            for (final IndexListener<V> listener : indexListeners.getListeners()) {
                listener.nameChanged();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getName(@NotNull final V value) {
        synchronized (sync) {
            return names.get(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public V removePending() {
        synchronized (sync) {
            final Iterator<V> it = pending.iterator();
            if (!it.hasNext()) {
                return null;
            }

            final V result = it.next();
            it.remove();
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModified() {
        synchronized (sync) {
            return modified;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIndexListener(@NotNull final IndexListener<V> listener) {
        indexListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIndexListener(@NotNull final IndexListener<V> listener) {
        indexListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(@NotNull final ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (sync) {
            objectOutputStream.writeObject(timestamps);
            objectOutputStream.writeObject(names);
            modified = false;
        }
    }

    /**
     * {@inheritDoc}
     * @noinspection unchecked
     */
    @Override
    @SuppressWarnings("unchecked")
    public void load(@NotNull final ObjectInputStream objectInputStream) throws IOException {
        synchronized (sync) {
            final Map<V, Long> tmpTimestamps;
            final Map<V, String> tmpNames;
            try {
                tmpTimestamps = (Map<V, Long>) objectInputStream.readObject();
                tmpNames = (Map<V, String>) objectInputStream.readObject();
            } catch (final ClassNotFoundException ex) {
                throw new IOException(ex.getMessage(), ex);
            }
            if (transaction) {
                throw new IOException("cannot restore state within active transaction");
            }

            // drop excess elements from tmpNames to force a consistent state
            tmpNames.keySet().retainAll(tmpTimestamps.keySet());

            clear();
            timestamps.putAll(tmpTimestamps);
            names.putAll(tmpNames);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        synchronized (sync) {
            timestamps.clear();
            names.clear();
            pending.clear();
            transactionDelete.clear();
            modified = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexingFinished() {
        for (final IndexListener<V> listener : indexListeners.getListeners()) {
            listener.indexingFinished();
        }
    }

}
