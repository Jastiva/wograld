/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.map.renderer;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Creates images from map instances.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ImageCreator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ImageCreator.class);

    /**
     * The {@link MapManager} for loading map instances.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link RendererFactory} for creating images.
     */
    @NotNull
    private final RendererFactory<G, A, R> rendererFactory;

    /**
     * Creates a new instance.
     * @param mapManager the map manager for loading map instances
     * @param rendererFactory the renderer factory for creating images
     */
    public ImageCreator(@NotNull final MapManager<G, A, R> mapManager, @NotNull final RendererFactory<G, A, R> rendererFactory) {
        this.mapManager = mapManager;
        this.rendererFactory = rendererFactory;
    }

    public void createImage(@NotNull final MapModel<G, A, R> mapModel, @NotNull final File file) throws IOException {
        final File tmpFile = new File(file.getPath() + ".tmp");
        rendererFactory.newSimpleMapRenderer(mapModel).printFullImage(tmpFile);
        if (!tmpFile.renameTo(file)) {
            if (!file.exists() || !file.delete() || !tmpFile.renameTo(file)) {
                throw new IOException("cannot rename " + tmpFile + " to " + file);
            }
        }
        if (log.isInfoEnabled()) {
            log.info(ACTION_BUILDER.format("logImageCreated", file, mapModel.getMapArchObject().getMapName()));
        }
    }

    public void makeImage(final File in, final File out) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("converting " + in + " to " + out);
        }

        if (in.canRead()) {
            final MapControl<G, A, R> mapControl = mapManager.openMapFile(in, false);
            if (mapControl == null) {
                return;
            }
            try {
                createImage(mapControl.getMapModel(), out);
            } finally {
                mapManager.release(mapControl);
            }
        }
    }

}
