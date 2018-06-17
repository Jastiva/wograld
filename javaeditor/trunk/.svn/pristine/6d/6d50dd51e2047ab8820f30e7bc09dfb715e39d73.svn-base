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

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import net.sf.gridarta.model.archetype.Archetype;
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
import net.sf.gridarta.utils.CommonConstants;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link AbstractIsoMapRenderer} to render map files.
 * @author Andreas Kirschbaum
 */
public class IsoPickmapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractIsoMapRenderer<G, A, R> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param spawnPointTypeNo the game object type number for spawn points
     * @param mapViewSettings the map view settings instance to use
     * @param mapModel the map model to render
     * @param mapGrid the grid to render
     * @param multiPositionData the multi position data query for multi-part
     * objects
     * @param isoMapSquareInfo the iso square info to use
     * @param gridMapSquarePainter the grid square painter to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param unknownSquareIcon the icon for unknown squares
     */
    public IsoPickmapRenderer(final int spawnPointTypeNo, @NotNull final MapViewSettings mapViewSettings, @NotNull final MapModel<G, A, R> mapModel, @NotNull final MapGrid mapGrid, @NotNull final MultiPositionData multiPositionData, @NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<G, A, R> gameObjectParser, @NotNull final Icon unknownSquareIcon) {
        super(spawnPointTypeNo, mapViewSettings, mapModel, mapGrid, 0, isoMapSquareInfo.getYLen(), multiPositionData, isoMapSquareInfo, gridMapSquarePainter, gameObjectParser, unknownSquareIcon);
        setBackground(CommonConstants.BG_COLOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void clearBackground(@NotNull final Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintSquare(@NotNull final Graphics2D g, final int x, final int y, @NotNull final MapSquare<G, A, R> square) {
        boolean foundFloor = false;
        for (final G node : square) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isGameObjectVisible(@NotNull final G gameObject) {
        return true;
    }

}
