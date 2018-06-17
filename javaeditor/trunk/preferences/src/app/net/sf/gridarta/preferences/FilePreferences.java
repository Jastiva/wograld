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

package net.sf.gridarta.preferences;

import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements a {@link Preferences} that stores all values in a {@link Storage}
 * instance.
 * @author Andreas Kirschbaum
 */
public abstract class FilePreferences extends AbstractPreferences {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(FilePreferences.class);

    /**
     * The node type of this node.
     */
    private final NodeType nodeType;

    /**
     * The full path name of this node. It is used as a key in the preferences
     * file.
     */
    private final String fullName;

    /**
     * The {@link Storage} instance used for loading/saving values.
     */
    @NotNull
    private final Storage storage;

    /**
     * Create a new instance.
     * @param parent the parent node, or <code>null</code> if this node is the
     * root node
     * @param name the name of this node
     * @param nodeType the node type of this node
     * @param storage the storage instance used for loading/saving values
     */
    protected FilePreferences(@Nullable final AbstractPreferences parent, @NotNull final String name, final NodeType nodeType, @NotNull final Storage storage) {
        super(parent, name);
        this.nodeType = nodeType;
        this.storage = storage;

        final StringBuilder sb = new StringBuilder(name);
        for (Preferences p = parent; p != null; p = p.parent()) {
            sb.insert(0, '_').insert(0, p.name());
        }
        sb.insert(0, nodeType.getPrefix());
        fullName = sb.toString();
        storage.newNode(fullName);
    }

    /**
     * Return the storage instance used for loading/saving values.
     * @return the storage instance
     */
    @NotNull
    protected Storage getStorage() {
        return storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        return storage.childrenNames(fullName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractPreferences childSpi(final String name) {
        return new FilePreferencesNode(this, name, nodeType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void flushSpi() throws BackingStoreException {
        throw new InternalError();
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public void flush() throws BackingStoreException {
        if (log.isDebugEnabled()) {
            log.debug("flush(" + fullName + ")");
        }

        storage.sync(false);
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Nullable
    @Override
    public String get(final String key, final String def) {
        if (key == null) {
            throw new IllegalArgumentException("null key");
        }

        synchronized (lock) {
            if (isRemoved()) {
                throw new IllegalStateException("removed node");
            }

            //noinspection CatchGenericClass
            try {
                final String value = getSpi(key);
                if (value != null) {
                    return value;
                }
            } catch (final Exception ignored) {
                // ignore
            }

            if (def != null) {
                putSpi(key, def);
            }
            return def;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(final String key, final boolean def) {
        final boolean result = super.getBoolean(key, def);
        putSpi(key, Boolean.toString(result));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(final String key, final double def) {
        final double result = super.getDouble(key, def);
        putSpi(key, Double.toString(result));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(final String key, final float def) {
        final float result = super.getFloat(key, def);
        putSpi(key, Float.toString(result));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(final String key, final int def) {
        final int result = super.getInt(key, def);
        putSpi(key, Integer.toString(result));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(final String key, final long def) {
        final long result = super.getLong(key, def);
        putSpi(key, Long.toString(result));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    protected String getSpi(final String key) {
        if (log.isDebugEnabled()) {
            log.debug("getSpi(" + fullName + ", key=" + key + ")");
        }

        return storage.getValue(fullName, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] keysSpi() throws BackingStoreException {
        if (log.isDebugEnabled()) {
            log.debug("keysSpi(" + fullName + ")");
        }

        return storage.getKeys(fullName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void putSpi(final String key, final String value) {
        if (log.isDebugEnabled()) {
            log.debug("putSpi(" + fullName + ", key=" + key + ", value=" + value + ")");
        }

        storage.putValue(fullName, key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        if (log.isDebugEnabled()) {
            log.debug("removeNodeSpi(" + fullName + ")");
        }

        storage.removeNode(fullName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeSpi(final String key) {
        if (log.isDebugEnabled()) {
            log.debug("removeSpi(" + fullName + ", key=" + key + ")");
        }

        storage.removeValue(fullName, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void syncSpi() throws BackingStoreException {
        throw new InternalError();
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public void sync() throws BackingStoreException {
        if (log.isDebugEnabled()) {
            log.debug("sync(" + fullName + ")");
        }

        storage.sync(true);
    }

}
