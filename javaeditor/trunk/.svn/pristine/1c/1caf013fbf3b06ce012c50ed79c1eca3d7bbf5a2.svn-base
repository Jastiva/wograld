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

package net.sf.gridarta.var.atrinik.gui.map.renderer;

import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.gui.map.renderer.IsoMapRenderer;
import net.sf.gridarta.gui.map.renderer.IsoPickmapRenderer;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.gui.map.renderer.SimpleIsoMapRenderer;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
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
     * The {@link MultiPositionData} to query for multi-part objects.
     */
    @NotNull
    private final MultiPositionData multiPositionData;

    /**
     * The {@link IsoMapSquareInfo} to use.
     */
    @NotNull
    private final IsoMapSquareInfo isoMapSquareInfo;

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
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings to use
     * @param filterControl the filter control to use
     * @param multiPositionData the multi position data to query for multi-part
     * objects
     * @param isoMapSquareInfo the iso square info to use
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param systemIcons the system icons for creating icons
     */
    public DefaultRendererFactory(@NotNull final MapViewSettings mapViewSettings, @NotNull final FilterControl<GameObject, MapArchObject, Archetype> filterControl, @NotNull final MultiPositionData multiPositionData, @NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final SystemIcons systemIcons) {
        this.mapViewSettings = mapViewSettings;
        this.filterControl = filterControl;
        this.multiPositionData = multiPositionData;
        this.isoMapSquareInfo = isoMapSquareInfo;
        this.gridMapSquarePainter = gridMapSquarePainter;
        this.gameObjectParser = gameObjectParser;
        this.systemIcons = systemIcons;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public SimpleIsoMapRenderer<GameObject, MapArchObject, Archetype> newSimpleMapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel) {
        return new SimpleIsoMapRenderer<GameObject, MapArchObject, Archetype>(Archetype.TYPE_SPAWN_POINT, mapModel, multiPositionData, isoMapSquareInfo, systemIcons.getUnknownSquareIcon());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public IsoMapRenderer<GameObject, MapArchObject, Archetype> newMapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final MapGrid mapGrid) {
        return new IsoMapRenderer<GameObject, MapArchObject, Archetype>(Archetype.TYPE_SPAWN_POINT, mapViewSettings, filterControl, mapModel, mapGrid, multiPositionData, isoMapSquareInfo, gridMapSquarePainter, gameObjectParser, systemIcons);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public IsoPickmapRenderer<GameObject, MapArchObject, Archetype> newPickmapRenderer(@NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final MapGrid mapGrid) {
        return new IsoPickmapRenderer<GameObject, MapArchObject, Archetype>(Archetype.TYPE_SPAWN_POINT, mapViewSettings, mapModel, mapGrid, multiPositionData, isoMapSquareInfo, gridMapSquarePainter, gameObjectParser, systemIcons.getUnknownSquareIcon());
    }

}
