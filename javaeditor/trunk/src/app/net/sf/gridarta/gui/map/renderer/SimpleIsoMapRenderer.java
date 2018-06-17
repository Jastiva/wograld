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

import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;

/**
 * Renders maps without MapGrid or validation errors.
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 */
public class SimpleIsoMapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractSimpleIsoMapRenderer<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The game object type number of spawn points.
     */
    private final int spawnPointTypeNo;

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
     * The icon for unknown squares.
     */
    @NotNull
    private final ImageIcon unknownSquareIcon;

    /**
     * Creates a new instance.
     * @param spawnPointTypeNo the game object type number for spawn points
     * @param mapModel the map model to render
     * @param multiPositionData the multi position data to for multi-part
     * objects
     * @param isoMapSquareInfo the iso square info to use
     * @param unknownSquareIcon the icon for unknown squares
     */
    public SimpleIsoMapRenderer(final int spawnPointTypeNo, @NotNull final MapModel<G, A, R> mapModel, @NotNull final MultiPositionData multiPositionData, @NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final ImageIcon unknownSquareIcon) {
        super(mapModel, null, isoMapSquareInfo);
        this.spawnPointTypeNo = spawnPointTypeNo;
        this.multiPositionData = multiPositionData;
        this.isoMapSquareInfo = isoMapSquareInfo;
        this.unknownSquareIcon = unknownSquareIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paint(@NotNull final Graphics2D graphics2D, final int xStart, final int yStart, @NotNull final G gameObject) {
        final GameObject<G, A, R> head = gameObject.getHead();
        final ImageIcon icon = "trans.101".equals(head.getFaceObjName()) ? unknownSquareIcon : head.getNormalImage();
        final int yOffset = icon.getIconHeight() - isoMapSquareInfo.getYLen();
        if (head.getMultiRefCount() > 0) {
            // multi-part images have to be painted with correct offset
            // TODO: This should be improved, especially regarding multi arch mobs inside spawn points.
            final BaseObject<G, A, R, ?> tmpNode = gameObject.isMulti() ? gameObject : null;
            final int x = xStart - multiPositionData.getXOffset(head.getArchetype().getMultiShapeID(), tmpNode == null ? head.getArchetype().getMultiPartNr() : tmpNode.getArchetype().getMultiPartNr());
            final int y = yStart - yOffset + multiPositionData.getYOffset(head.getArchetype().getMultiShapeID(), tmpNode == null ? head.getArchetype().getMultiPartNr() : tmpNode.getArchetype().getMultiPartNr());
            if (tmpNode != null && tmpNode.getArchetype().isLowestPart() || head.getArchetype().isLowestPart()) {
                icon.paintIcon(this, graphics2D, x, y);
            } else {
                final BaseObject<G, A, R, ?> env = head.getContainerGameObject();
                if (env != null && env.getTypeNo() == spawnPointTypeNo) {
                    icon.paintIcon(this, graphics2D, x, y);
                }
            }
        } else {
            int xOffset = 0;
            if (icon.getIconWidth() > isoMapSquareInfo.getXLen()) {
                xOffset = (icon.getIconWidth() - isoMapSquareInfo.getXLen()) / 2;
            }
            icon.paintIcon(this, graphics2D, xStart - xOffset, yStart - yOffset);
        }

        // Paint first object (most likely a mob) in spawn points.
        if (head.getTypeNo() == spawnPointTypeNo) {
            final G mob = head.getFirst();
            if (mob != null) {
                paint(graphics2D, xStart, yStart, mob);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
    }

}
