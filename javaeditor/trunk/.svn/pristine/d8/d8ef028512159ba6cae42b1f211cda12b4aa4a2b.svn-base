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
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import net.sf.gridarta.gui.dialog.gameobjectattributes.GameObjectAttributesDialogFactory;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModel;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModelListener;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareView;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
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
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common base class for the panel that allows users to edit a GameObject's
 * attributes.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class GameObjectAttributesControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The MainControl to use, e.g. for accessing AnimationObjects.
     */
    @NotNull
    private final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory;

    /**
     * The object chooser instance.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link SelectedSquareView} to update.
     */
    @NotNull
    private final SelectedSquareView<G, A, R> selectedSquareView;

    /**
     * The {@link GameObjectFactory} for creating new {@link GameObject
     * GameObjects}.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The main panel.
     */
    @NotNull
    private final JTabbedPane panelDesktop = new JTabbedPane(SwingConstants.TOP);

    /**
     * The model used by this controller.
     */
    @NotNull
    private final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel;

    /**
     * The registered {@link GameObjectAttributesControlListener
     * GameObjectAttributesControlListeners} to notify.
     */
    @NotNull
    private final EventListenerList2<GameObjectAttributesControlListener> listeners = new EventListenerList2<GameObjectAttributesControlListener>(GameObjectAttributesControlListener.class);

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
     * The overall {@link Severity}.
     */
    @NotNull
    private Severity severity = Severity.DEFAULT;

    /**
     * Action for "add to inventory".
     */
    @NotNull
    private final Action aMapArchAddInv = ACTION_BUILDER.createAction(false, "mapArchAddInv", this);

    /**
     * Action for "add to environment".
     */
    @NotNull
    private final Action aMapArchAddEnv = ACTION_BUILDER.createAction(false, "mapArchAddEnv", this);

    /**
     * Action for "edit attributes".
     */
    @NotNull
    private final Action aMapArchAttribute = ACTION_BUILDER.createAction(false, "mapArchAttrib", this);

    /**
     * All active tabs. Tabs not currently visible are missing.
     */
    @NotNull
    private final Collection<GameObjectAttributesTab<G, A, R>> tabs = new HashSet<GameObjectAttributesTab<G, A, R>>();

    /**
     * Maps tab to tab index. Tabs not currently visible are missing.
     */
    @NotNull
    private final Map<GameObjectAttributesTab<G, A, R>, Integer> tabIndex = new IdentityHashMap<GameObjectAttributesTab<G, A, R>, Integer>();

    /**
     * Maps tab's component to tab.
     */
    @NotNull
    private final Map<Component, GameObjectAttributesTab<G, A, R>> componentTabs = new IdentityHashMap<Component, GameObjectAttributesTab<G, A, R>>();

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
     * The listener to detect changes of the selected game object.
     */
    @NotNull
    private final SelectedSquareModelListener<G, A, R> selectedSquareModelListener = new SelectedSquareModelListener<G, A, R>() {

        @Override
        public void selectionChanged(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            gameObjectAttributesModel.setSelectedGameObject(gameObject);
        }

    };

    /**
     * The listener attached to {@link #gameObjectAttributesModel}.
     */
    @NotNull
    private final GameObjectAttributesModelListener<G, A, R> gameObjectAttributesModelListener = new GameObjectAttributesModelListener<G, A, R>() {

        @Override
        public void selectedGameObjectChanged(@Nullable final G selectedGameObject) {
            autoApplyArchPanelChanges();
            GameObjectAttributesControl.this.selectedGameObject = selectedGameObject;
            refreshDisplay();
        }

        @Override
        public void refreshSelectedGameObject() {
            // ignore
        }

    };

    /**
     * The listener attached to all tabs.
     */
    @NotNull
    private final GameObjectAttributesTabListener<G, A, R> gameObjectAttributesTabListener = new GameObjectAttributesTabListener<G, A, R>() {

        @Override
        public void tabSeverityChanged(@NotNull final GameObjectAttributesTab<G, A, R> tab, @NotNull final Severity tabSeverity) {
            setTabSeverity(tab, tabSeverity);
        }

        @Override
        public void apply() {
            autoApplyArchPanelChanges();
        }

    };

    /**
     * Create the GameObjectAttributesPanel.
     * @param gameObjectAttributesModel the model to use
     * @param gameObjectAttributesDialogFactory the factory for creating game
     * object attributes dialog instances
     * @param objectChooser the object chooser instance
     * @param mapManager the map manager instance
     * @param selectedSquareModel the selected square model instance
     * @param selectedSquareView the selected square view to update
     * @param gameObjectFactory the game object factory for creating new game
     * objects
     */
    public GameObjectAttributesControl(@NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final MapManager<G, A, R> mapManager, @NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, @NotNull final SelectedSquareView<G, A, R> selectedSquareView, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory) {
        super(new BorderLayout());
        this.gameObjectAttributesModel = gameObjectAttributesModel;
        this.gameObjectAttributesDialogFactory = gameObjectAttributesDialogFactory;
        this.objectChooser = objectChooser;
        this.selectedSquareView = selectedSquareView;
        this.gameObjectFactory = gameObjectFactory;

        final Container mapArchPanel = new JPanel();
        mapArchPanel.setLayout(new BorderLayout());
        add(mapArchPanel, BorderLayout.CENTER);

        final Container buttonPanel = createButtonPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        mapArchPanel.add(buttonPanel, BorderLayout.WEST);
        mapArchPanel.add(panelDesktop, BorderLayout.CENTER);

        selectedSquareModel.addSelectedSquareListener(selectedSquareModelListener);
        gameObjectAttributesModel.addGameObjectAttributesModelListener(gameObjectAttributesModelListener); // this listener must be registered before any tab, including TextEditorTab

        selectedGameObject = gameObjectAttributesModel.getSelectedGameObject();
        refreshDisplay();
        currentMapControl = mapManager.getCurrentMap();
        if (currentMapControl != null) {
            final MapModel<G, A, R> mapModel = currentMapControl.getMapModel();
            mapModel.addMapModelListener(mapModelListener);
            mapModel.addMapTransactionListener(mapTransactionListener);
        }
        mapManager.addMapManagerListener(mapManagerListener);
    }

    /**
     * Adds a {@link GameObjectAttributesControlListener} to notify.
     * @param listener the listener
     */
    public void addGameObjectAttributesControlListener(@NotNull final GameObjectAttributesControlListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link GameObjectAttributesControlListener} to notify.
     * @param listener the listener
     */
    public void removeGameObjectAttributesControlListener(@NotNull final GameObjectAttributesControlListener listener) {
        listeners.remove(listener);
    }

    /**
     * Adds a tab.
     * @param tab the tab to add
     */
    public void addTab(@NotNull final GameObjectAttributesTab<G, A, R> tab) {
        tabIndex.put(tab, panelDesktop.getTabCount());
        addTabInt(tab);
        panelDesktop.add(tab.getPanel(), tab.getName());
        setTabSeverity(tab, tab.getTabSeverity());
    }

    /**
     * Adds a tab which is not shown in the tab panel.
     * @param tab the tab to add
     */
    private void addTabInt(final GameObjectAttributesTab<G, A, R> tab) {
        tabs.add(tab);
        componentTabs.put(tab.getPanel(), tab);
        tab.addGameObjectAttributesTabListener(gameObjectAttributesTabListener);
    }

    /**
     * Selects a tab. The tab must exist.
     * @param tab the tab
     */
    public void selectTab(@NotNull final GameObjectAttributesTab<G, A, R> tab) {
        if (!tabs.contains(tab)) {
            throw new IllegalArgumentException();
        }

        panelDesktop.setSelectedComponent(tab.getPanel());
    }

    /**
     * Returns the selected tab.
     * @return tab the selected tab
     */
    @NotNull
    public GameObjectAttributesTab<G, A, R> getSelectedTab() {
        final Component component = panelDesktop.getSelectedComponent();
        final GameObjectAttributesTab<G, A, R> tab = componentTabs.get(component);
        assert tab != null;
        return tab;
    }

    /**
     * Sets the tab color of a tab.
     * @param tab the tab
     * @param tabSeverity the tab severity
     */
    private void setTabSeverity(@NotNull final GameObjectAttributesTab<G, A, R> tab, @NotNull final Severity tabSeverity) {
        final Integer index = tabIndex.get(tab);
        if (index == null) {
            return;
        }

        panelDesktop.setForegroundAt(index, tabSeverity.getColor());

        Severity mainSeverity = tabSeverity;
        for (final Map.Entry<GameObjectAttributesTab<G, A, R>, Integer> pair2 : tabIndex.entrySet()) {
            final Severity tmpSeverity = pair2.getKey().getTabSeverity();
            if (tmpSeverity.getLevel() > mainSeverity.getLevel()) {
                mainSeverity = tmpSeverity;
            }
        }
        if (severity == mainSeverity) {
            return;
        }
        severity = mainSeverity;
        for (final GameObjectAttributesControlListener listener : listeners.getListeners()) {
            listener.severityChanged(mainSeverity);
        }
    }

    /**
     * Returns the overall {@link Severity}.
     * @return the overall severity
     */
    @NotNull
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Update the displayed information for the selected game object.
     */
    private void refreshDisplay() {
        aMapArchAddInv.setEnabled(selectedGameObject != null);
        aMapArchAddEnv.setEnabled(selectedGameObject != null);
        aMapArchAttribute.setEnabled(selectedGameObject != null);
        gameObjectAttributesModel.fireRefreshSelectedGameObject();
    }

    /**
     * Action method for displaying the attributes of the currently selected
     * object.
     */
    @ActionMethod
    public void mapArchAttrib() {
        if (selectedGameObject != null) {
            gameObjectAttributesDialogFactory.showAttributeDialog(selectedGameObject);
        }
    }

    /**
     * Action method for adding an object to the inventory of the currently
     * selected object.
     */
    @ActionMethod
    public void mapArchAddInv() {
        final BaseObject<G, A, R, ?> arch = objectChooser.getSelection();
        if (arch == null) { // nothing selected?
            return;
        }

        final GameObject<G, A, R> gameObject = selectedGameObject;
        if (gameObject == null) {
            return;
        }

        final G invNew = arch.newInstance(gameObjectFactory);

        final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
        assert mapSquare != null;
        final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
        mapModel.beginTransaction("Add to inventory");
        try {
            gameObject.addLast(invNew);
            if (arch instanceof Archetype) {
                gameObjectFactory.createInventory(invNew, arch);
            }
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Action method for adding the currently selected object to the inventory
     * of a new object.
     */
    @ActionMethod
    public void mapArchAddEnv() {
        final BaseObject<G, A, R, ?> baseObject = objectChooser.getSelection();
        if (baseObject == null) {
            return;
        }

        final G prevGameObject = selectedGameObject;
        if (prevGameObject == null) {
            return;
        }

        final MapSquare<G, A, R> mapSquare = prevGameObject.getMapSquare();
        assert mapSquare != null;
        final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
        mapModel.beginTransaction("Add to environment"); // TODO; I18N/L10N
        try {
            final G insertedGameObject = mapModel.insertArchToMap(baseObject, prevGameObject, mapSquare.getMapLocation(), true);
            if (insertedGameObject != null) {
                mapModel.removeGameObject(prevGameObject, true);
                insertedGameObject.addLast(prevGameObject);
                selectedSquareView.setSelectedGameObject(insertedGameObject);
            }
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Same as {@link #applyArchPanelChanges()} but does protect against
     * recursive calls.
     */
    private void autoApplyArchPanelChanges() {
        if (selectedGameObject == null || isInAutoApplyArchPanelChanges || isInMapTransaction) {
            return;
        }

        isInAutoApplyArchPanelChanges = true;
        try {
            applyArchPanelChanges();
        } finally {
            isInAutoApplyArchPanelChanges = false;
        }
    }

    /**
     * When the "apply"-button on the ArchPanel (at the bottom of the window) is
     * pressed, this function updates the active arch object.
     */
    private void applyArchPanelChanges() {
        final GameObject<G, A, R> gameObject = selectedGameObject;
        if (gameObject == null) {
            return;
        }

        final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
        if (mapSquare == null) {
            // auto-apply of deleted game object
            return;
        }

        // suppress unwanted map transactions if nothing has changed
        boolean canApply = false;
        for (final GameObjectAttributesTab<G, A, R> tab : tabs) {
            if (tab.canApply()) {
                canApply = true;
                break;
            }
        }
        if (!canApply) {
            return;
        }

        final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
        mapModel.beginTransaction("Change object attributes");
        try {
            for (final GameObjectAttributesTab<G, A, R> tab : tabs) {
                if (tab.canApply()) {
                    tab.apply();
                }
            }
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Creates the button panel containing the actions.
     * @return the button panel
     */
    @NotNull
    private Container createButtonPanel() {
        final Container buttonPanel = new JPanel();
        buttonPanel.add(new JButton(aMapArchAddInv));
        buttonPanel.add(new JButton(aMapArchAddEnv));
        buttonPanel.add(new JButton(aMapArchAttribute));
        return buttonPanel;
    }

}
