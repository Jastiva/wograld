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

package net.sf.gridarta.gui.panel.connectionview;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract controller base class for map view controls.
 * @author Andreas Kirschbaum
 */
public abstract class Control<K, G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The view of this controller.
     */
    @NotNull
    private final View<K, G, A, R> view;

    /**
     * Creates a new instance.
     * @param view the view to use
     */
    protected Control(@NotNull final View<K, G, A, R> view) {
        this.view = view;

        view.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                highlightSelectedEntries();
            }
        });

        //noinspection RefusedBequest
        view.addListMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                highlightSelectedEntries();
                if (e.getClickCount() == 2) {
                    final Iterator<Connection<K>> it = view.getSelectedConnections().iterator();
                    if (it.hasNext()) {
                        doubleClick(it.next());
                    }
                }
            }
        });
    }

    /**
     * Called if an entry is double-clicked.
     * @param connection the first selected connection
     */
    protected abstract void doubleClick(@NotNull final Connection<K> connection);

    /**
     * Returns the view for this controller.
     * @return the view for this controller
     */
    @NotNull
    public JPanel getView() {
        return view;
    }

    /**
     * Sets the map selection to the currently selected entries.
     */
    private void highlightSelectedEntries() {
        final MapView<G, A, R> mapView = view.getMapView();
        if (mapView == null) {
            return;
        }

        final MapGrid mapGrid = mapView.getMapGrid();
        mapGrid.unSelect();

        final Point point = new Point();

        final Iterable<Connection<K>> connections = view.getSelectedConnections();
        for (final Iterable<GameObject<?, ?, ?>> connection : connections) {
            for (final GameObject<?, ?, ?> object : connection) {
                final BaseObject<?, ?, ?, ?> topObject = object.getTopContainer();
                point.setLocation(topObject.getMapX(), topObject.getMapY());
                mapGrid.select(point, SelectionMode.ADD);
            }
        }
    }

}
