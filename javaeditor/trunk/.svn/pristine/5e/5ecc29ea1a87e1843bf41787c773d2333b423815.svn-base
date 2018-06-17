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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.prefs.BackingStoreException;
import java.util.regex.Pattern;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains a set of preference values. The values are stored in a flat file.
 * @author Andreas Kirschbaum
 */
public class Storage {

    /**
     * The pattern that matches a single equal sign ("=").
     */
    @NotNull
    public static final Pattern PATTERN_EQUAL = Pattern.compile("=");

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(Storage.class);

    /**
     * The default key name for loading/saving values.
     */
    @NotNull
    private final String defaultPath;

    /**
     * The file for loading/saving values.
     */
    @Nullable
    private final File file;

    /**
     * If set, do not save changes into {@link #file}.
     */
    private boolean noSave = true;

    /**
     * Pattern to ignore in path names.
     */
    private static final Pattern PATTERN_IGNORE = Pattern.compile("[\\[].*");

    /**
     * The stored values. Maps path name to key name to value.
     */
    private final Map<String, Map<String, String>> values = new TreeMap<String, Map<String, String>>();

    /**
     * Create a new instance.
     * @param defaultPath the default key name for loading/saving values
     * @param file the file for loading/saving values or <code>null</code> to
     * not use a backing file
     */
    public Storage(@NotNull final String defaultPath, @Nullable final File file) {
        if (log.isDebugEnabled()) {
            log.debug("new");
        }

        this.defaultPath = defaultPath;
        this.file = file;

        loadValues();
        noSave = false;
    }

    /**
     * Make sure a node exists.
     * @param path the node path name
     */
    public void newNode(@NotNull final String path) {
        if (log.isDebugEnabled()) {
            log.debug("newNode(" + path + ")");
        }

        if (!values.containsKey(path)) {
            values.put(path, new TreeMap<String, String>());
        }
    }

    /**
     * Return the names of the children of a node. The returned array will be of
     * size zero if the node has no children.
     * @param path the node path name
     * @return the children names
     */
    @NotNull
    public String[] childrenNames(@NotNull final String path) {
        if (log.isDebugEnabled()) {
            log.debug("childrenNames(" + path + ")");
        }

        final String prefix = path + "/";
        final Set<String> result = new TreeSet<String>();
        for (final String key : values.keySet()) {
            if (key.startsWith(prefix)) {
                result.add(key.substring(prefix.length()));
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * Return the value associated with the specified key at a node, or
     * <code>null</code> if there is no association for this key.
     * @param path the node path name
     * @param key the key to get value for
     * @return the value
     */
    @Nullable
    public String getValue(@NotNull final String path, @NotNull final String key) {
        if (log.isDebugEnabled()) {
            log.debug("getValue(" + path + ", " + key + ")");
        }

        final Map<String, String> map = values.get(path);
        assert map != null; // AbstractPreferences.getSpi() ensures this
        return map.get(key);
    }

    /**
     * Return all of the keys that have an associated value in a node. The
     * returned array will be of size zero if the node has no preferences.
     * @param path the node path name
     * @return the key names
     */
    @NotNull
    public String[] getKeys(@NotNull final String path) {
        if (log.isDebugEnabled()) {
            log.debug("getKeys(" + path + ")");
        }

        final Map<String, String> map = values.get(path);
        assert map != null; // AbstractPreferences.keysSpi() ensures this
        final Set<String> keys = map.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * Put the given key-value association into a node.
     * @param path the node path name
     * @param key the key to store
     * @param value the value to store
     */
    public void putValue(@NotNull final String path, @NotNull final String key, @NotNull final String value) {
        if (log.isDebugEnabled()) {
            log.debug("putValue(" + path + ", " + key + ", " + value + ")");
        }

        final Map<String, String> map = values.get(path);
        assert map != null; // AbstractPreferences.putSpi() ensures this
        final String oldValue = map.put(key, value);
        if (oldValue == null || !oldValue.equals(value)) {
            setChanged();
        }
    }

    /**
     * Remove a preference node including all preferences that it contains.
     * @param path the node path name
     */
    public void removeNode(@NotNull final String path) {
        if (log.isDebugEnabled()) {
            log.debug("removeNode(" + path + ")");
        }

        if (values.remove(path) != null) {
            setChanged();
        }
    }

    /**
     * Remove the association (if any) for the specified key at a node.
     * @param path the node path name to remove from
     * @param key the key to remove
     */
    public void removeValue(@NotNull final String path, @NotNull final String key) {
        if (log.isDebugEnabled()) {
            log.debug("removeValue(" + path + ", " + key + ")");
        }

        final Map<String, String> map = values.get(path);
        assert map != null; // AbstractPreferences.removeSpi() ensures this
        if (map.remove(key) != null) {
            setChanged();
        }
    }

    /**
     * Save changes to the underlying file.
     * @param sync not currently used
     * @throws BackingStoreException in case sync was not possible, i.e. due to
     * I/O problems
     */
    public void sync(final boolean sync) throws BackingStoreException {
        if (log.isDebugEnabled()) {
            log.debug("sync(" + sync + ")");
        }

        try {
            saveValues();
        } catch (final IOException ex) {
            throw new BackingStoreException(ex);
        }
    }

    /**
     * This function is called whenever the contents of {@link #values} has
     * changed.
     */
    private void setChanged() {
        if (noSave) {
            return;
        }

        try {
            saveValues();
        } catch (final IOException ex) {
            log.warn(file + ": " + ex.getMessage());
        }
    }

    /**
     * Load the values from the backing file.
     */
    private void loadValues() {
        if (file == null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("loadValues: " + file);
        }

        values.clear();

        try {
            final FileInputStream fis = new FileInputStream(file);
            try {
                final InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                try {
                    final LineNumberReader lnr = new LineNumberReader(isr);
                    try {
                        loadValues(lnr);
                    } finally {
                        lnr.close();
                    }
                } finally {
                    isr.close();
                }
            } finally {
                fis.close();
            }
        } catch (final FileNotFoundException ignored) {
            // ignore
        } catch (final IOException ex) {
            log.warn(file + ": " + ex.getMessage());
        }
    }

    /**
     * Load the values from a {@link LineNumberReader}.
     * @param lnr the line number reader
     * @throws IOException if an I/O error occurs
     */
    private void loadValues(@NotNull final LineNumberReader lnr) throws IOException {
        String path = defaultPath;
        while (true) {
            final String line2 = lnr.readLine();
            if (line2 == null) {
                break;
            }
            final String line = Codec.decode(line2.trim());
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }

            if (line.startsWith("[") && line.endsWith("]")) {
                path = line.substring(1, line.length() - 1);
                continue;
            }

            final String[] tmp = PATTERN_EQUAL.split(line, 2);
            if (tmp.length != 2) {
                log.warn(file + ":" + lnr.getLineNumber() + ": syntax error");
                continue;
            }
            final String key = tmp[0];
            final String value = tmp[1];

            newNode(path);
            putValue(path, key, value);
        }
    }

    /**
     * Save the values to the backing file.
     * @throws IOException if the values cannot be saved
     */
    private void saveValues() throws IOException {
        if (file == null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("saveValues: " + file);
        }

        final File tmpFile = new File(file.getPath() + ".tmp");
        final FileOutputStream fos = new FileOutputStream(tmpFile);
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            try {
                final BufferedWriter bw = new BufferedWriter(osw);
                try {
                    final Map<String, String> defaultNode = values.get(defaultPath);
                    if (defaultNode != null) {
                        saveNode(bw, null, defaultNode);
                    }

                    for (final Map.Entry<String, Map<String, String>> e : values.entrySet()) {
                        if (!e.getKey().equals(defaultPath)) {
                            saveNode(bw, e.getKey(), e.getValue());
                        }
                    }
                } finally {
                    bw.close();
                }
            } finally {
                osw.close();
            }
        } finally {
            fos.close();
        }

        //noinspection ResultOfMethodCallIgnored
        file.delete(); // Windows cannot overwrite destination file on rename
        if (!tmpFile.renameTo(file)) {
            throw new IOException("cannot rename " + tmpFile + " to " + file);
        }
    }

    /**
     * Save one node.
     * @param writer the <code>Writer</code> to write to
     * @param path the node path name
     * @param node the node to save
     * @throws IOException if the node cannot be saved
     */
    private static void saveNode(@NotNull final BufferedWriter writer, @Nullable final String path, @NotNull final Map<String, String> node) throws IOException {
        if (node.isEmpty()) {
            return;
        }

        final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

        if (path != null) {
            writer.newLine();
            writer.write("[");
            writer.write(Codec.encode(path));
            writer.write("]");
            writer.newLine();
        }

        for (final Map.Entry<String, String> entry : node.entrySet()) {
            writer.newLine();

            final String comment = actionBuilder.getString("prefs." + PATTERN_IGNORE.matcher(entry.getKey()).replaceAll(""));
            if (comment != null) {
                writer.write("# ");
                writer.write(comment);
                writer.newLine();
            }
            writer.write(Codec.encode(entry.getKey()));
            writer.write("=");
            writer.write(Codec.encode(entry.getValue()));
            writer.newLine();
        }
    }

}
