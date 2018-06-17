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

package net.sf.gridarta.gui.dialog.golocation;

import net.sf.gridarta.gui.map.AbstractPerMapDialogManager;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Manager for {@link GoLocationDialog} instances. This class makes sure that
 * for each map view no more than one go location dialog exists.
 * @author Andreas Kirschbaum
 */
public class GoLocationDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractPerMapDialogManager<G, A, R, GoLocationDialog<G, A, R>> {

    /**
     * Creates a new instance.
     * @param mapViewManager the view map manager
     */
    public GoLocationDialogManager(@NotNull final MapViewManager<G, A, R> mapViewManager) {
        super(mapViewManager);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected GoLocationDialog<G, A, R> allocate(@NotNull final MapView<G, A, R> mapView) {
        return new GoLocationDialog<G, A, R>(this, mapView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void activate(@NotNull final GoLocationDialog<G, A, R> dialog) {
        dialog.getDialog().toFront();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispose(@NotNull final GoLocationDialog<G, A, R> dialog) {
        dialog.getDialog().dispose();
    }

}
