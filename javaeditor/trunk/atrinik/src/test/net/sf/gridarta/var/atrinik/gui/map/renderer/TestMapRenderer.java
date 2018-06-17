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

package net.sf.gridarta.var.atrinik.gui.map.renderer;

import java.awt.Graphics2D;
import java.util.Locale;
import javax.swing.Icon;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.gui.map.renderer.IsoMapRenderer;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link IsoMapRenderer} for regression tests. It records all painting
 * operations.
 * @author Andreas Kirschbaum
 */
public class TestMapRenderer extends IsoMapRenderer<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The string builder that collects all paint operations.
     */
    @NotNull
    private final StringBuilder sb = new StringBuilder();

    /**
     * Creates a new instance.
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
    public TestMapRenderer(@NotNull final MapViewSettings mapViewSettings, @NotNull final FilterControl<TestGameObject, TestMapArchObject, TestArchetype> filterControl, @NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final MapGrid mapGrid, @NotNull final MultiPositionData multiPositionData, @NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final GridMapSquarePainter gridMapSquarePainter, @NotNull final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser, @NotNull final SystemIcons systemIcons) {
        super(100, mapViewSettings, filterControl, mapModel, mapGrid, multiPositionData, isoMapSquareInfo, gridMapSquarePainter, gameObjectParser, systemIcons);
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    protected void paintIcon(@NotNull final Graphics2D g, @NotNull final Icon icon) {
        final double[] matrix = new double[6];
        g.getTransform().getMatrix(matrix);
        appendDouble(matrix[0]);
        sb.append(' ');
        appendDouble(matrix[1]);
        sb.append(' ');
        appendDouble(matrix[2]);
        sb.append(' ');
        appendDouble(matrix[3]);
        sb.append(' ');
        appendDouble(matrix[4]);
        sb.append(' ');
        appendDouble(matrix[5]);
        sb.append('\n');
    }

    /**
     * Appends a <code>double></code> value to {@link #sb}.
     * @param value the double value
     */
    private void appendDouble(final double value) {
        sb.append(String.format((Locale) null, "%.03g", value));
    }

    /**
     * Returns and clears the accumulated painting operations.
     * @return the painting operations
     */
    @NotNull
    public String getPaintingOperations() {
        final String result = sb.toString();
        sb.setLength(0);
        return result;
    }

}
