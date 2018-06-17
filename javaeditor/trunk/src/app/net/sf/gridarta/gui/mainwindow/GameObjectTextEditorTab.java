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

package net.sf.gridarta.gui.mainwindow;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Set;
import net.sf.gridarta.gui.panel.gameobjecttexteditor.GameObjectTextEditor;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModel;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModelListener;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.gridarta.gui.utils.tabbedpanel.Tab;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapmodel.MapTransactionListener;
import net.sf.gridarta.model.mapmodel.SavedSquares;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link Tab} displaying the game object text editor.
 * @author Andreas Kirschbaum
 */
public class GameObjectTextEditorTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends Tab {

    /**
     * The displayed {@link GameObjectTextEditor} instance.
     */
    @NotNull
    private final GameObjectTextEditor gameObjectTextEditor;

    /**
     * Last known active map. This map has {@link #mapModelListener} attached.
     */
    @Nullable
    private MapControl<G, A, R> currentMapControl;

    /**
     * Whether {@link #autoApplyArchPanelChanges()} is currently running.
     */
    private boolean isInAutoApplyArchPanelChanges;

    /**
     * The currently selected game object.
     */
    @Nullable
    private G selectedGameObject;

    /**
     * Records whether a map transaction is active. This is used to prevent
     * start recursive map transactions in {@link #autoApplyArchPanelChanges()}.
     */
    private boolean isInMapTransaction;

    /**
     * The map manager listener.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            if (currentMapControl != null) {
                final MapModel<G, A, R> mapModel = currentMapControl.getMapModel();
                mapModel.removeMapModelListener(mapModelListener);
                mapModel.removeMapTransactionListener(mapTransactionListener);
            }
            currentMapControl = mapControl;
            if (currentMapControl != null) {
                final MapModel<G, A, R> mapModel = currentMapControl.getMapModel();
                mapModel.addMapModelListener(mapModelListener);
                mapModel.addMapTransactionListener(mapTransactionListener);
            }
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            // ignore
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

    };

    /**
     * The map transaction listener which is attached to {@link
     * #currentMapControl}. It triggers auto-apply whenever a map transaction is
     * about to start.
     */
    @NotNull
    private final MapTransactionListener<G, A, R> mapTransactionListener = new MapTransactionListener<G, A, R>() {

        @Override
        public void preBeginTransaction() {
            autoApplyArchPanelChanges();
            isInMapTransaction = true;
        }

        @Override
        public void beginTransaction(@NotNull final MapModel<G, A, R> mapModel, @NotNull final String name) {
            // ignore
        }

        @Override
        public void endTransaction(@NotNull final MapModel<G, A, R> mapModel, @NotNull final SavedSquares<G, A, R> savedSquares) {
            // ignore
        }

        @Override
        public void postEndTransaction() {
            isInMapTransaction = false;
        }

    };

    /**
     * The map model listener which is attached to {@link #currentMapControl}.
     */
    @NotNull
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            if (selectedGameObject == null) {
                return;
            }

            final G topGameObject = selectedGameObject.getTopContainer();
            for (final Iterable<G> mapSquare : mapSquares) {
                for (final G gameObject : mapSquare) {
                    if (gameObject == topGameObject) {
                        refreshDisplay();
                    }
                }
            }
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            for (final GameObject<G, A, R> gameObject : gameObjects) {
                if (selectedGameObject == gameObject.getHead()) {
                    refreshDisplay();
                }
            }
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
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

    /**
     * Creates a new instance.
     * @param ident the tab's identification string
     * @param gameObjectTextEditor the game object text editor to show
     * @param defaultLocation the tab's default location
     * @param alternativeLocation whether the tab is shown in the alternative
     * location
     * @param index the tab's index for ordering
     * @param defaultOpen the tab's default opened status
     * @param selectedSquareModel the selected square mode to track
     * @param mapManager the map manager to track
     */
    public GameObjectTextEditorTab(@NotNull final String ident, @NotNull final GameObjectTextEditor gameObjectTextEditor, @NotNull final Location defaultLocation, final boolean alternativeLocation, final int index, final boolean defaultOpen, @NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, @NotNull final MapManager<G, A, R> mapManager) {
        super(ident, gameObjectTextEditor, defaultLocation, alternativeLocation, index, defaultOpen);
        this.gameObjectTextEditor = gameObjectTextEditor;
        selectedSquareModel.addSelectedSquareListener(new SelectedSquareModelListener<G, A, R>() {

            @Override
            public void selectionChanged(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
                autoApplyArchPanelChanges();
                selectedGameObject = gameObject;
                refreshDisplay();
            }

        });
        currentMapControl = mapManager.getCurrentMap();
        if (currentMapControl != null) {
            final MapModel<G, A, R> mapModel = currentMapControl.getMapModel();
            mapModel.addMapModelListener(mapModelListener);
            mapModel.addMapTransactionListener(mapTransactionListener);
        }
        mapManager.addMapManagerListener(mapManagerListener);
        selectedGameObject = selectedSquareModel.getSelectedGameObject();
        refreshDisplay();
        gameObjectTextEditor.getTextPane().addFocusListener(new FocusListener() {

            @Override
            public void focusGained(final FocusEvent e) {
                // ignore
            }

            @Override
            public void focusLost(final FocusEvent e) {
                autoApplyArchPanelChanges();
            }

        });
    }

    /**
     * Applies pending changed. Protects against recursive calls.
     */
    private void autoApplyArchPanelChanges() {
        final GameObject<G, A, R> gameObject = selectedGameObject;
        if (gameObject == null || isInAutoApplyArchPanelChanges || isInMapTransaction) {
            return;
        }

        final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
        if (mapSquare == null) {
            return;
        }

        final MapModel<G, A, R> mapModel = mapSquare.getMapModel();

        isInAutoApplyArchPanelChanges = true;
        try {
            mapModel.beginTransaction("Change object attributes");
            try {
                gameObjectTextEditor.applyChanges(gameObject);
            } finally {
                mapModel.endTransaction();
            }
        } finally {
            isInAutoApplyArchPanelChanges = false;
        }
    }

    /**
     * Refreshes the tab's state from a {@link #selectedGameObject}.
     */
    private void refreshDisplay() {
        setSeverity(gameObjectTextEditor.refreshDisplay(selectedGameObject));
    }

}
