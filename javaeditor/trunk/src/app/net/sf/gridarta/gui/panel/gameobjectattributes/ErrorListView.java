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

package net.sf.gridarta.gui.panel.gameobjectattributes;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.errors.ValidationError;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An ErrorPanel displays errors to the user.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ErrorListView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list for displaying the errors.
     * @serial include
     */
    private final JList errorList = new JList();

    /**
     * The JLabel for displaying the error text.
     * @serial include
     */
    private final JEditorPane errorMsg = new JEditorPane("text/html", "");

    /**
     * The registered listeners to notify.
     */
    @NotNull
    private final EventListenerList2<ErrorListViewListener> listeners = new EventListenerList2<ErrorListViewListener>(ErrorListViewListener.class);

    /**
     * Last known active map. This map has {@link #mapModelListener} attached.
     * Set to <code>null</code> if no map is active.
     */
    @Nullable
    private MapModel<G, A, R> currentMapModel;

    /**
     * The currently displayed errors.
     */
    @Nullable
    private Vector<ValidationError<G, A, R>> errors;

    /**
     * The {@link MapView} for displaying map errors. Set to <code>null</code>
     * if no map is active.
     */
    @Nullable
    private MapView<G, A, R> mapView;

    /**
     * The list selection listener to detect selected list entries.
     */
    private final ListSelectionListener listSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            highlightEntries(((JList) e.getSource()).getSelectedIndex());
        }

    };

    /**
     * The {@link MapModelListener} which is attached to {@link
     * #currentMapModel}.
     */
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            // ignore
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            // ignore
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            updateErrors(errors);
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            // ignore
        }

    };

    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            if (currentMapModel != null) {
                currentMapModel.removeMapModelListener(mapModelListener);
            }
            currentMapModel = mapView == null ? null : mapView.getMapControl().getMapModel();
            if (currentMapModel != null) {
                currentMapModel.addMapModelListener(mapModelListener);
            }

            ErrorListView.this.mapView = mapView;
            updateErrors();
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
     * Create a ConnectionPanel.
     * @param mapViewManager the map view manager to track
     */
    public ErrorListView(@NotNull final MapViewManager<G, A, R> mapViewManager) {
        setLayout(new BorderLayout());
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(errorList), new JScrollPane(errorMsg));
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);
        errorMsg.setEditable(false);
        errorList.addListSelectionListener(listSelectionListener);
        //noinspection RefusedBequest
        errorList.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                highlightEntries(errorList.getSelectedIndex());
            }
        });
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        mapView = mapViewManager.getActiveMapView();
        mapViewManagerListener.activeMapViewChanged(mapView);
        updateErrors();
    }

    /**
     * Adds an {@link ErrorListViewListener} to be notified about changes.
     * @param listener the error list view listener
     */
    public void addErrorListViewListener(@NotNull final ErrorListViewListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an {@link ErrorListViewListener} to be notified about changes.
     * @param listener the error list view listener
     */
    public void removeErrorListViewListener(@NotNull final ErrorListViewListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns whether any warnings are shown.
     * @return whether any warnings are shown
     */
    public boolean hasWarnings() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * Display an error message.
     * @param index the index of the error message; clears the error message
     * display for invalid values
     */
    private void highlightEntries(final int index) {
        if (errors == null) {
            errorMsg.setText(null);
            errorMsg.setCaretPosition(0);
            return;
        }
        final ValidationError<G, A, R> error;
        try {
            error = errors.elementAt(index);
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            errorMsg.setText(null);
            errorMsg.setCaretPosition(0);
            return;
        }
        errorMsg.setText(error.getMessage());

        final MapView<G, A, R> tmpMapView = mapView;
        if (tmpMapView != null) {
            final Iterator<G> gameObjectIterator = error.getGameObjects().iterator();
            if (gameObjectIterator.hasNext()) {
                final G gameObject = gameObjectIterator.next();
                tmpMapView.getMapCursor().setGameObject(gameObject);
            } else {
                final Iterator<MapSquare<G, A, R>> mapSquareIterator = error.getMapSquares().iterator();
                if (mapSquareIterator.hasNext()) {
                    final MapSquare<G, A, R> mapSquare = mapSquareIterator.next();
                    tmpMapView.getMapCursor().setMapSquare(mapSquare);
                }
            }
        }
        errorMsg.setCaretPosition(0);
    }

    /**
     * Notifies all listeners that the warnings may have changed.
     * @param hasWarnings whether any warnings are shown
     */
    private void fireErrorsUpdated(final boolean hasWarnings) {
        for (final ErrorListViewListener listener : listeners.getListeners()) {
            listener.warningsChanged(hasWarnings);
        }
    }

    /**
     * Updates the displayed errors.
     */
    private void updateErrors() {
        if (mapView != null) {
            updateErrors(mapView.getMapControl().getMapModel().getErrors());
            return;
        }

        errors = null;
        errorList.setModel(new DefaultListModel());
        fireErrorsUpdated(false);
    }

    /**
     * Updates the errors for the current map.
     * @param errors the errors
     */
    public void updateErrors(@NotNull final ErrorCollector<G, A, R> errors) {
        assert mapView != null;
        final List<ValidationError<G, A, R>> errorVector = new ArrayList<ValidationError<G, A, R>>();
        for (final ValidationError<G, A, R> validationError : errors.getErrors()) {
            errorVector.add(validationError);
        }
        this.errors = new Vector<ValidationError<G, A, R>>(errorVector);
        errorList.setListData(this.errors);

        fireErrorsUpdated(hasWarnings());
    }

}
