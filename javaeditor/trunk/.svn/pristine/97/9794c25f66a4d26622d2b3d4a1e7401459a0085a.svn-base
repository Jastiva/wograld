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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link MapRenderer} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestMapRenderer extends AbstractMapRenderer<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param mapModel the rendered map model
     */
    public TestMapRenderer(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel) {
        super(mapModel, null);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public BufferedImage getFullImage() {
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Point getSquareLocationAt(@NotNull final Point point, @Nullable final Point retPoint) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forceRepaint() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Rectangle getSquareBounds(@NotNull final Point p) {
        return new Rectangle(1, 1);
    }

}
