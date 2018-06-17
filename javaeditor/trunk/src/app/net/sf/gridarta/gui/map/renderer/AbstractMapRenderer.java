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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.errors.ValidationError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for classes implementing {@link MapRenderer}.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JComponent implements MapRenderer {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The MapSquares that are known to contain errors.
     */
    @NotNull
    private Map<MapSquare<G, A, R>, ValidationError<G, A, R>> erroneousMapSquares = Collections.emptyMap();

    /**
     * Used to avoid creation millions of points.
     */
    @NotNull
    private final Point tmpPoint = new Point();

    /**
     * The rendered {@link MapModel}.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link GameObjectParser} for creating tooltip information or
     * <code>null</code>.
     */
    @Nullable
    private final GameObjectParser<G, A, R> gameObjectParser;

    /**
     * Whether the setting for lighted map squares is inverted.
     */
    private boolean lightVisible;

    /**
     * Creates a new instance.
     * @param mapModel the rendered map model
     * @param gameObjectParser the game object parser for generating tooltip
     * information or <code>null</code>
     */
    protected AbstractMapRenderer(@NotNull final MapModel<G, A, R> mapModel, @Nullable final GameObjectParser<G, A, R> gameObjectParser) {
        this.mapModel = mapModel;
        this.gameObjectParser = gameObjectParser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printFullImage(@NotNull final File file) throws IOException {
        ImageIO.write(getFullImage(), "png", file);
    }

    /**
     * Sets the MapSquares that are known to contain errors.
     * @param erroneousMapSquares the MapSquares that are known to contain
     * errors
     */
    public void setErroneousMapSquares(@NotNull final Map<MapSquare<G, A, R>, ValidationError<G, A, R>> erroneousMapSquares) {
        this.erroneousMapSquares = new HashMap<MapSquare<G, A, R>, ValidationError<G, A, R>>(erroneousMapSquares);
    }

    /**
     * Must be called when this renderer is not used anymore.
     */
    public abstract void closeNotify();

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Nullable
    @Override
    public String getToolTipText(@NotNull final MouseEvent event) {
        final Point mapLocation = getSquareLocationAt(event.getPoint(), tmpPoint);
        if (mapLocation == null) {
            return null;
        }

        final MapSquare<G, A, R> mapSquare = mapModel.getMapSquare(mapLocation);

        final ToolTipAppender<G, A, R> toolTipAppender = new ToolTipAppender<G, A, R>(gameObjectParser);
        for (final G gameObject : mapSquare.reverse()) {
            toolTipAppender.appendGameObject(gameObject, false, "");
        }

        final ValidationError<G, A, R> error = erroneousMapSquares.get(mapSquare);
        if (error != null) {
            toolTipAppender.appendValidationError(error);
        }
        return toolTipAppender.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLightVisible(final boolean lightVisible) {
        if (this.lightVisible == lightVisible) {
            return;
        }

        this.lightVisible = lightVisible;
        forceRepaint();
    }

    /**
     * Returns whether the setting for lighted map squares should be inverted.
     * @return whether the setting should be inverted
     */
    protected boolean isLightVisible() {
        return lightVisible;
    }

}
