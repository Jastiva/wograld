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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.filter.FilterState;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.filter.FilterConfig;
import net.sf.gridarta.model.filter.FilterConfigChangeType;
import net.sf.gridarta.model.filter.FilterConfigListener;
import net.sf.gridarta.model.gameobject.DefaultIsoGameObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link AbstractIsoMapRenderer} to render map files.
 * @author Andreas Kirschbaum
 */
public class IsoMapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractIsoMapRenderer<G, A, R> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    private final FilterControl<G, A, R> filterControl;

    @NotNull
    private final Color[] highLightMask = { new Color(1.0f, 0.0f, 0.0f, 0.33f), new Color(0.0f, 1.0f, 0.0f, 0.33f), new Color(0.0f, 1.0f, 1.0f, 0.33f), };

    @NotNull
    private final Icon emptySquareIcon;

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The {@link IsoMapSquareInfo} to use.
     */
    @NotNull
    private final IsoMapSquareInfo isoMapSquareInfo;

    /**
     * The filter state instance for this map renderer.
     */
    @NotNull
    private final FilterState filterState = new FilterState();

    /**
     * The x-coordinates for painting highlighted squares.
     */
    @NotNull
    private final int[] xPoints = new int[4];

    /**
     * The y-coordinates for painting highlighted squares.
     */
    @NotNull
    private final int[] yPoints = new int[4];

    /**
     * The {@link FilterConfigListener} attached to {@link #filterControl} to
     * repaint all after config changes.
     */
    @NotNull
    private final FilterConfigListener filterConfigListener = new FilterConfigListener() {

        @Override
        public void configChanged(@NotNull final FilterConfigChangeType filterConfigChangeType, @NotNull final FilterConfig<?, ?> filterConfig) {
            forceRepaint();
        }

    };

    /**
     * Creates a new instance.
     * @param spawnPointTypeNo the game object type number for spawn points
     * @param mapViewSettings the map view settings instance to use
     * @param filterControl the filter to use
     * @param mapModel the map model to render
     * @param mapGrid the grid to render
     * @param multiPositionData the multi position data to query for multi-part
     * objects
     * @param isoMapSquareInfo the iso square info to use
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param systemIcons the system icons for creating icons
     */
    public IsoMapRenderer(final int spawnPointTypeNo, @NotNull final MapViewSettings mapViewSettings, @NotNull final FilterControl<G, A, R> filterControl, @NotNull final MapModel<G, A, R> mapModel, @NotNull final MapGrid mapGrid, @NotNull final MultiPositionData multiPositionData, @NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<G, A, R> gameObjectParser, @NotNull final SystemIcons systemIcons) {
        super(spawnPointTypeNo, mapViewSettings, mapModel, mapGrid, isoMapSquareInfo.getXLen(), 2 * isoMapSquareInfo.getYLen(), multiPositionData, isoMapSquareInfo, gridMapSquarePainter, gameObjectParser, systemIcons.getUnknownSquareIcon());
        this.filterControl = filterControl;
        emptySquareIcon = systemIcons.getEmptySquareIcon();
        this.mapViewSettings = mapViewSettings;
        this.isoMapSquareInfo = isoMapSquareInfo;
        this.filterControl.addConfigListener(filterConfigListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
        super.closeNotify();
        filterControl.removeConfigListener(filterConfigListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void clearBackground(@NotNull final Graphics g) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isGameObjectVisible(@NotNull final G gameObject) {
        return mapViewSettings.isEditType(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintSquare(@NotNull final Graphics2D g, final int x, final int y, @NotNull final MapSquare<G, A, R> square) {
        filterControl.newSquare(filterState);
        if (square.isEmpty()) {
            emptySquareIcon.paintIcon(this, g, x, y);
        } else {
            boolean foundFloor = false;
            for (final G node : square) {
                filterControl.objectInSquare(filterState, node);
                if (filterControl.canShow(node)) {
                    final G head = node.getHead();
                    final int yOffset;
                    if (!foundFloor && head.getAttributeInt(DefaultIsoGameObject.LAYER) == 1) {
                        foundFloor = true;
                        yOffset = 0;
                    } else {
                        yOffset = head.getAttributeInt(DefaultIsoGameObject.Z);
                    }
                    paintGameObjectIfVisible(g, x, y - yOffset, node);
                }
            }
        }
        for (int i = 0; i < FilterControl.MAX_HIGHLIGHT; i++) {
            if (filterControl.isHighlightedSquare(filterState, i)) {
                final Color color = g.getColor();
                g.setColor(highLightMask[i]);
                xPoints[0] = x + isoMapSquareInfo.getXLen() / 2;
                xPoints[1] = x + isoMapSquareInfo.getXLen() - 1;
                xPoints[2] = xPoints[0];
                xPoints[3] = x;
                yPoints[0] = y;
                yPoints[1] = y + isoMapSquareInfo.getYLen() / 2;
                yPoints[2] = y + isoMapSquareInfo.getYLen() - 1;
                yPoints[3] = yPoints[1];
                g.fillPolygon(xPoints, yPoints, 4);
                g.setColor(color);
            }
        }
    }

}
