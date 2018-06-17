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

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListener;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListenerManager;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for a panel that holds information about connections of
 * the selected key on the selected map.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @todo more user interface features
 * @todo make this a MapHighlightModel
 * @todo better react on changes, don't always scan the whole map
 * @todo attach
 * @serial exclude
 * @noinspection AbstractClassExtendsConcreteClass
 */
public abstract class View<K, G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The connections.
     */
    @NotNull
    private final Map<K, Connection<K>> connections;

    /**
     * The List with the connections.
     */
    @NotNull
    private final JList connectionList = new JList();

    /**
     * Map view corresponding to {@link #connectionList}.
     */
    @Nullable
    private MapView<G, A, R> mapView;

    /**
     * The map view listener to detect changed active maps.
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            View.this.mapView = mapView;
            scanMapForConnections();
        }

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            // ignore
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            // ignore
        }

    };

    /**
     * The map model listener to detect map changes.
     */
    @NotNull
    private final DelayedMapModelListener<G, A, R> delayedMapModelListener = new DelayedMapModelListener<G, A, R>() {

        @Override
        public void mapModelChanged(@NotNull final MapModel<G, A, R> mapModel) {
            if (mapView != null && mapModel == mapView.getMapControl().getMapModel()) {
                scanMapForConnections();
            }
        }

    };

    /**
     * Create a panel.
     * @param keyComparator the comparator for comparing <code>K</code> values
     * @param cellRenderer the cell renderer for displaying key and values
     * @param mapViewManager the map view manager
     * @param delayedMapModelListenerManager the delayed map model listener
     * manager to use
     * @noinspection TypeMayBeWeakened // JList does not use type parameters
     */
    protected View(@NotNull final Comparator<K> keyComparator, @NotNull final CellRenderer<K> cellRenderer, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager) {
        connections = new TreeMap<K, Connection<K>>(keyComparator);
        setLayout(new BorderLayout());
        connectionList.setCellRenderer(cellRenderer);
        add(new JScrollPane(connectionList));
        delayedMapModelListenerManager.addDelayedMapModelListener(delayedMapModelListener);
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
    }

    /**
     * Return the current map view.
     * @return the current map view, or <code>null</code> if none is available
     */
    @Nullable
    public MapView<G, A, R> getMapView() {
        return mapView;
    }

    /**
     * Register a {@link ListSelectionListener} for the connection list.
     * @param listener the listener to register
     */
    public void addListSelectionListener(@NotNull final ListSelectionListener listener) {
        connectionList.addListSelectionListener(listener);
    }

    /**
     * Register a {@link MouseListener} for the connection list.
     * @param listener the listener to register
     */
    public void addListMouseListener(@NotNull final MouseListener listener) {
        connectionList.addMouseListener(listener);
    }

    /**
     * Scans the current map ({@link #mapView}) for the contained connections.
     */
    private void scanMapForConnections() {
        connections.clear();
        if (mapView != null) {
            scanMapForConnections(mapView.getMapControl().getMapModel());
        }
        connectionList.setListData(connections.values().toArray());
    }

    /**
     * Scans the given map for the contained connections.
     * @param mapModel the map model to process
     */
    private void scanMapForConnections(@NotNull final Iterable<MapSquare<G, A, R>> mapModel) {
        for (final Iterable<G> mapSquare : mapModel) {
            for (final G gameObject : mapSquare) {
                scanGameObjectForConnections(gameObject);
            }
        }
    }

    /**
     * Scan the given game object for connections.
     * @param gameObject the game object to process
     */
    protected abstract void scanGameObjectForConnections(@NotNull final G gameObject);

    protected void addConnection(@NotNull final K key, @NotNull final GameObject<G, A, R> gameObject) {
        if (!connections.containsKey(key)) {
            connections.put(key, new Connection<K>(key));
        }
        connections.get(key).addGameObject(gameObject);
    }

    /**
     * Return a list of all selected connections.
     * @return a list of all selected connections
     */
    @NotNull
    public Iterable<Connection<K>> getSelectedConnections() {
        final Collection<Connection<K>> result = new ArrayList<Connection<K>>();
        for (final Object selection : connectionList.getSelectedValues()) {
            //JList does not use type parameters
            @SuppressWarnings("unchecked")
            final Connection<K> connection = (Connection<K>) selection;
            result.add(connection);
        }
        return result;
    }

}
