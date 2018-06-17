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

package net.sf.gridarta.model.mapcontrol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.IOUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Implements map models.
 * @author Andreas Kirschbaum
 */
public class DefaultMapControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapControl<G, A, R> {

    /**
     * The registered event listeners.
     */
    @NotNull
    private final EventListenerList2<MapControlListener<G, A, R>> listenerList = new EventListenerList2<MapControlListener<G, A, R>>(MapControlListener.class);

    /**
     * The use counter. The instance is freed when it reaches zero and if there
     * are no map views.
     */
    private int useCounter = 1;

    /**
     * Flag that indicates whether this is a pickmap or not.
     */
    private final boolean isPickmap;

    /**
     * The {@link MapModel}.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link MapWriter} for saving this map.
     */
    @NotNull
    private final MapWriter<G, A, R> mapWriter;

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * Creates a new instance.
     * @param mapModel the map model to use
     * @param isPickmap true if this is a pickmap
     * @param mapWriter the map writer for saving map control instances
     * @param globalSettings the global settings to use
     */
    public DefaultMapControl(@NotNull final MapModel<G, A, R> mapModel, final boolean isPickmap, @NotNull final MapWriter<G, A, R> mapWriter, @NotNull final GlobalSettings globalSettings) {
        this.mapModel = mapModel;
        this.isPickmap = isPickmap;
        this.mapWriter = mapWriter;
        this.globalSettings = globalSettings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapControlListener(@NotNull final MapControlListener<G, A, R> listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapControlListener(@NotNull final MapControlListener<G, A, R> listener) {
        listenerList.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acquire() {
        useCounter++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release() {
        assert useCounter > 0;
        useCounter--;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUseCounter() {
        return useCounter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPickmap() {
        return isPickmap;
    }

    /**
     * Saves the map.
     * @param file the file to save to
     * @throws IOException if encoding fails
     */
    private void encodeMapFile(@NotNull final File file) throws IOException {
        final OutputStream outputStream = new FileOutputStream(file);
        try {
            final Writer writer = new OutputStreamWriter(outputStream, IOUtils.MAP_ENCODING);
            try {
                mapWriter.encodeMapFile(mapModel, writer);
            } finally {
                writer.close();
            }
        } finally {
            outputStream.close();
        }

        mapModel.resetModified();
        for (final MapControlListener<G, A, R> listener : listenerList.getListeners()) {
            listener.saved(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() throws IOException {
        final File file = mapModel.getMapFile();
        if (file == null) {
            throw new IllegalStateException();
        }

        if (mapModel.isModified()) {
            mapModel.getMapArchObject().updateModifiedAttribute(globalSettings.getUserName());
        }
        encodeMapFile(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAs(@NotNull final File file) throws IOException {
        mapModel.setMapFile(file);
        encodeMapFile(file);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapModel<G, A, R> getMapModel() {
        return mapModel;
    }

}
