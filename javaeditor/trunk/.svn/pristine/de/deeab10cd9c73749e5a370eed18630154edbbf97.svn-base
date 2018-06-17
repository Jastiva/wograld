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

package net.sf.gridarta.var.crossfire.gui.map.renderer;

import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.smoothface.SmoothFaces;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating {@link net.sf.gridarta.gui.map.renderer.MapRenderer}
 * instances.
 * @author Andreas Kirschbaum
 */
public class DefaultRendererFactory implements RendererFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link MapViewSettings} to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The {@link FilterControl} to use.
     */
    @NotNull
    private final FilterControl<GameObject, MapArchObject, Archetype> filterControl;

    /**
     * The {@link SmoothFaces} to use.
     */
    @NotNull
    private final SmoothFaces smoothFaces;

    /**
     * The {@link GridMapSquarePainter} to use.
     */
    @NotNull
    private final GridMapSquarePainter gridMapSquarePainter;

    /**
     * The {@link GameObjectParser} for creating tooltip information.
     */
    @NotNull
    private final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings to use
     * @param filterControl the filter control to use
     * @param smoothFaces the smooth faces to use
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param systemIcons the system icons for creating icons
     */
    public DefaultRendererFactory(@NotNull final MapViewSettings mapViewSettings, @NotNull final FilterControl<GameObject, MapArchObject, Archetype> filterControl, @NotNull final SmoothFaces smoothFaces, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final SystemIcons systemIcons) {
        this.mapViewSettings = mapViewSettings;
        this.filterControl = filterControl;
        this.smoothFaces = smoothFaces;
        this.gridMapSquarePainter = gridMapSquarePainter;
        this.gameObjectParser = gameObjectParser;
        this.faceObjectProviders = faceObjectProviders;
        this.systemIcons = systemIcons;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public SimpleFlatMapRenderer newSimpleMapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel) {
        final SmoothingRenderer smoothingRenderer = new SmoothingRenderer(mapModel, smoothFaces, faceObjectProviders);
        return new SimpleFlatMapRenderer(mapModel, systemIcons, smoothingRenderer);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FlatMapRenderer newMapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final MapGrid mapGrid) {
        final SmoothingRenderer smoothingRenderer = new SmoothingRenderer(mapModel, smoothFaces, faceObjectProviders);
        return new FlatMapRenderer(mapViewSettings, filterControl, mapModel, mapGrid, gridMapSquarePainter, gameObjectParser, systemIcons, smoothingRenderer);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FlatPickmapRenderer newPickmapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final MapGrid mapGrid) {
        return new FlatPickmapRenderer(mapViewSettings, mapModel, mapGrid, gridMapSquarePainter, gameObjectParser);
    }

}
