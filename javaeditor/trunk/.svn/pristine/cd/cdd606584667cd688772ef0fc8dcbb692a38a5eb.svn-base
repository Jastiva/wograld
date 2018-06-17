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

package net.sf.gridarta.gui.dialog.gomap;

import java.awt.Window;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.index.MapsIndex;
import net.sf.gridarta.model.index.MapsIndexer;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.ExiterListener;
import net.sf.japi.swing.action.ActionMethod;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Manager for {@link GoMapDialog} instances.
 * @author Andreas Kirschbaum
 */
public class GoMapDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(GoMapDialogManager.class);

    /**
     * The parent {@link Window} for go map dialogs.
     */
    @NotNull
    private final Window parent;

    /**
     * The {@link MapViewsManager} for opening maps.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The {@link MapsIndex} indexing all maps.
     */
    @NotNull
    private final MapsIndex mapsIndex = new MapsIndex();

    /**
     * The {@link MapsIndexer} indexing {@link #mapsIndex}.
     */
    @NotNull
    private final MapsIndexer<G, A, R> mapsIndexer;

    /**
     * Creates a new instance.
     * @param parent the parent window for go map dialogs
     * @param mapManager the map manager for opening maps
     * @param mapViewsManager the map views manager for opening maps
     * @param globalSettings the global settings instance
     * @param exiter the exiter instance
     */
    public GoMapDialogManager(@NotNull final Window parent, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewsManager<G, A, R> mapViewsManager, @NotNull final GlobalSettings globalSettings, @NotNull final Exiter exiter) {
        this.parent = parent;
        this.mapViewsManager = mapViewsManager;
        mapsIndexer = new MapsIndexer<G, A, R>(mapsIndex, mapManager, globalSettings);
        mapsIndexer.start();
        exiter.addExiterListener(new ExiterListener() {

            @Override
            public void preExitNotify() {
                // ignore
            }

            @Override
            public void appExitNotify() {
                try {
                    mapsIndexer.stop();
                } catch (final InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    log.warn("Waiting for MapsIndexer to terminate was interrupted");
                }
            }

            @Override
            public void waitExitNotify() {
                // ignore
            }

        });
    }

    /**
     * Action method to open the "go map" dialog.
     */
    @ActionMethod
    public void goMap() {
        new GoMapDialog<G, A, R>(parent, mapsIndex, mapViewsManager).showDialog();
    }

}
