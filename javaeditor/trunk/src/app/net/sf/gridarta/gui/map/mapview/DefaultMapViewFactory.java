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

package net.sf.gridarta.gui.map.mapview;

import java.awt.Point;
import net.sf.gridarta.gui.map.renderer.AbstractMapRenderer;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default {@link MapViewFactory} implementation.
 * @author Andreas Kirschbaum
 */
public class DefaultMapViewFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapViewFactory<G, A, R> {

    /**
     * Action Builder to create Actions.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link RendererFactory} to use.
     */
    @NotNull
    private final RendererFactory<G, A, R> rendererFactory;

    /**
     * The x distance when scrolling.
     */
    private final int xScrollDistance;

    /**
     * The y distance when scrolling.
     */
    private final int yScrollDistance;

    /**
     * The {@link PathManager} for converting path names.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * Creates a new instance.
     * @param rendererFactory the renderer factory to use
     * @param xScrollDistance the x distance when scrolling
     * @param yScrollDistance the y distance when scrolling
     * @param pathManager the path manager for converting path names
     */
    public DefaultMapViewFactory(@NotNull final RendererFactory<G, A, R> rendererFactory, final int xScrollDistance, final int yScrollDistance, @NotNull final PathManager pathManager) {
        this.rendererFactory = rendererFactory;
        this.xScrollDistance = xScrollDistance;
        this.yScrollDistance = yScrollDistance;
        this.pathManager = pathManager;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapView<G, A, R> newMapView(@NotNull final MapControl<G, A, R> mapControl, @Nullable final Point viewPosition, final int viewCounter) {
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final MapGrid mapGrid = new MapGrid(mapModel.getMapArchObject().getMapSize());
        final AbstractMapRenderer<G, A, R> renderer = mapControl.isPickmap() ? rendererFactory.newPickmapRenderer(mapModel, mapGrid) : rendererFactory.newMapRenderer(mapModel, mapGrid);
        renderer.setFocusable(true);
        final MapView<G, A, R> mapView = new DefaultMapView<G, A, R>(mapControl, viewCounter, pathManager, mapGrid, new MapCursor<G, A, R>(mapGrid, mapModel), renderer, viewPosition, xScrollDistance, yScrollDistance);
        mapView.getInternalFrame().setJMenuBar(ACTION_BUILDER.createMenuBar(false, "mapwindow"));
        return mapView;
    }

}
