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

package net.sf.gridarta.gui.panel.selectedsquare;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.dialog.gameobjectattributes.GameObjectAttributesDialogFactory;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The panel that displays the game objects of the currently selected map
 * square. Currently it uses a list but in future it will use a tree instead.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @todo turn this into a tree
 */
public class SelectedSquareView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The action builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The model for this view.
     */
    @NotNull
    private final SelectedSquareModel<G, A, R> selectedSquareModel;

    /**
     * The {@link SelectedSquareActions} for this controller.
     */
    @NotNull
    private final SelectedSquareActions<G, A, R> selectedSquareActions;

    /**
     * The {@link ModelUpdater} used for updating {@link #model}.
     */
    @NotNull
    private final ModelUpdater modelUpdater;

    /**
     * The factory for creating game object attributes dialog instances.
     */
    @NotNull
    private final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory;

    /**
     * The object chooser.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link DefaultListModel} of {@link #list}.
     */
    @NotNull
    private final DefaultListModel model = new DefaultListModel();

    /**
     * The list of game objects.
     */
    @NotNull
    private final JList list = new JList(model);

    /**
     * The arrow buttons.
     */
    @NotNull
    private final Container arrows = new JPanel();

    /**
     * The currently active {@link MapModel}.
     */
    @Nullable
    private MapModel<G, A, R> mapModel;

    /**
     * The currently tracked {@link MapCursor}. It has {@link
     * #mapCursorListener} attached.
     */
    @Nullable
    private MapCursor<G, A, R> mapCursor;

    /**
     * The action for "move square prev".
     */
    @NotNull
    private final Action aMoveSquarePrev = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquarePrev");

    /**
     * The action for "move square next".
     */
    @NotNull
    private final Action aMoveSquareNext = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareNext");

    /**
     * The action for "move square top".
     */
    @NotNull
    private final Action aMoveSquareTop = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareTop");

    /**
     * The action for "move square up".
     */
    @NotNull
    private final Action aMoveSquareUp = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareUp");

    /**
     * The action for "move square down".
     */
    @NotNull
    private final Action aMoveSquareDown = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareDown");

    /**
     * The action for "move square bottom".
     */
    @NotNull
    private final Action aMoveSquareBottom = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareBottom");

    /**
     * The action for "move square env".
     */
    @NotNull
    private final Action aMoveSquareEnv = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareEnv");

    /**
     * The action for "move square inv".
     */
    @NotNull
    private final Action aMoveSquareInv = ActionUtils.newAction(ACTION_BUILDER, "Selected Square View", this, "moveSquareInv");

    /**
     * The {@link MapModelListener} attached to {@link #mapModel}.
     */
    @NotNull
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            refresh();
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            if (selectedSquareModel.isSelectedMapSquares(mapSquares)) {
                refresh();
            }
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            if (selectedSquareModel.isSelectedGameObjects(gameObjects) || selectedSquareModel.isSelectedGameObjects(transientGameObjects)) {
                refresh();
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
     * The map view manager listener.
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            @Nullable final MapModel<G, A, R> mapModel;
            @Nullable final MapCursor<G, A, R> mapCursor;
            if (mapView == null) {
                mapModel = null;
                mapCursor = null;
            } else {
                mapModel = mapView.getMapControl().getMapModel();
                mapCursor = mapView.getMapCursor();
            }
            setMapCursor(mapModel, mapCursor);
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
     * The {@link SelectedSquareModelListener} attached to {@link
     * #selectedSquareModel}.
     */
    @NotNull
    private final SelectedSquareModelListener<G, A, R> selectedSquareModelListener = new SelectedSquareModelListener<G, A, R>() {

        @Override
        public void selectionChanged(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            refresh();
            updateActions();
        }

    };

    /**
     * The listener attached to {@link #list} to be informed about selection
     * changes.
     */
    @NotNull
    private final ListSelectionListener listSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            setSelectedIndex(list.getSelectedIndex());
        }

    };

    /**
     * The {@link MapCursorListener} attached to {@link #mapCursor}.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            // ignore
        }

        @Override
        public void mapCursorChangedMode() {
            // ignore
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            if (selectedSquareModel.setSelectedMapSquare(mapSquare, gameObject)) {
                refresh();
            }
        }

    };

    /**
     * The {@link MouseListener} attached to the view to process mouse actions.
     * @noinspection RefusedBequest
     */
    @NotNull
    private final MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mousePressed(final MouseEvent e) {
            if (isSelect(e)) {
                if (e.getClickCount() > 1) { // LMB Double click
                    final G gameObject = selectedSquareModel.getSelectedGameObject();
                    if (gameObject != null) {
                        gameObjectAttributesDialogFactory.showAttributeDialog(gameObject);
                    }
                }
            } else if (isInsert(e)) {
                insertGameObject(getListIndex(e));
            } else if (isDelete(e)) {
                deleteGameObject(getListIndex(e));
            }
        }

    };

    /**
     * Create a new instance.
     * @param selectedSquareModel the model for this view
     * @param selectedSquareActions the selected square actions to use
     * @param gameObjectAttributesDialogFactory the factory for creating game
     * object attributes dialog instances
     * @param objectChooser the object chooser
     * @param mapViewManager the map view manager to use
     * @param mapViewSettings the map view settings instance
     * @param compassIcon if non-<code>null</code>, display this icon on the top
     * of the list
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param unknownSquareIcon the icon for unknown squares
     */
    public SelectedSquareView(@NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, @NotNull final SelectedSquareActions<G, A, R> selectedSquareActions, @NotNull final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final MapViewSettings mapViewSettings, @Nullable final ImageIcon compassIcon, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final Icon unknownSquareIcon) {
        this.selectedSquareModel = selectedSquareModel;
        this.selectedSquareActions = selectedSquareActions;
        this.gameObjectAttributesDialogFactory = gameObjectAttributesDialogFactory;
        this.objectChooser = objectChooser;

        setLayout(new BorderLayout());

        modelUpdater = new ModelUpdater(model, mapViewSettings);
        list.setCellRenderer(new CellRenderer<G, A, R>(faceObjectProviders, unknownSquareIcon));
        list.setBackground(Color.lightGray);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        add(scrollPane, BorderLayout.CENTER);

        updateArrows(false);
        final Collection<Action> actions = new ArrayList<Action>();
        actions.add(aMoveSquareDown);
        actions.add(aMoveSquareUp);
        actions.add(aMoveSquareDown);
        actions.add(aMoveSquareBottom);
        actions.add(aMoveSquareEnv);
        actions.add(aMoveSquareInv);
        for (final Action action : actions) {
            final AbstractButton button = new JButton(action);
            arrows.add(button);
            button.setMargin(new Insets(0, 0, 0, 0));
        }
        if (compassIcon != null) {
            final Container compass = new JPanel();
            compass.add(new JLabel(compassIcon));
            add(compass, BorderLayout.NORTH);
        }
        list.addListSelectionListener(listSelectionListener);
        list.addMouseListener(mouseListener);
        list.setFocusable(false); // XXX Workaround for Mantis #0000154 This is not clean and should be removed as soon as cut/copy/paste of arches is possible

        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        selectedSquareModel.addSelectedSquareListener(selectedSquareModelListener);

        updateActions();
    }

    /**
     * Updates the parameters of the {@link #arrows} buttons.
     * @param mapSquareListBottom whether the map square list is shown at the
     * bottom
     */
    private void updateArrows(final boolean mapSquareListBottom) {
        if (mapSquareListBottom) {
            arrows.setLayout(new GridLayout(4, 1));
            add(arrows, BorderLayout.WEST);  // put up/down buttons west
        } else {
            arrows.setLayout(new FlowLayout());
            add(arrows, BorderLayout.SOUTH); // put up/down buttons south
        }
    }

    /**
     * Determine the list index for a given mouse event. This function differs
     * from <code>list.locationTopIndex(e.getPoint())</code> in that it always
     * returns a valid list index, or the list size (i.e., one element past the
     * last list index) if the point is after the end of the list.
     * @param e the mouse event containing the position to check
     * @return the selected list index
     */
    public int getListIndex(@NotNull final MouseEvent e) {
        final int lastIndex = model.getSize() - 1;
        if (lastIndex < 0) {
            return 0;
        }

        final RectangularShape bounds = list.getCellBounds(lastIndex, lastIndex);
        final int lowestY = (int) (bounds.getY() + bounds.getHeight());
        if ((int) e.getPoint().getY() >= lowestY) {
            return lastIndex + 1;
        }

        final int listIndex = list.locationToIndex(e.getPoint());
        assert listIndex >= 0; // -1 is returned only if the list is empty, but this was already checked
        return listIndex;
    }

    /**
     * Updates the enabled state of all actions.
     */
    private void updateActions() {
        aMoveSquarePrev.setEnabled(selectedSquareActions.doMoveSquarePrev(false));
        aMoveSquareNext.setEnabled(selectedSquareActions.doMoveSquareNext(false));
        aMoveSquareTop.setEnabled(selectedSquareActions.doMoveSquareTop(false));
        aMoveSquareUp.setEnabled(selectedSquareActions.doMoveSquareUp(false));
        aMoveSquareDown.setEnabled(selectedSquareActions.doMoveSquareDown(false));
        aMoveSquareBottom.setEnabled(selectedSquareActions.doMoveSquareBottom(false));
        aMoveSquareEnv.setEnabled(selectedSquareActions.doMoveSquareEnv(false));
        aMoveSquareInv.setEnabled(selectedSquareActions.doMoveSquareInv(false));
    }

    /**
     * Re-display the map square panel for {@link SelectedSquareModel#selectedMapSquare}.
     * Possibly updates information in <code>selectedMapSquare</code>.
     */
    private void refresh() {
        list.setEnabled(false);
        try {
            setSelectedIndex(modelUpdater.update(selectedSquareModel.getSelectedMapSquare(), selectedSquareModel.getSelectedGameObject()));
        } finally {
            list.setEnabled(true);
        }
        updateActions();
    }

    /**
     * Return a game object from the list.
     * @param index the index in the list
     * @return the game object, or <code>null</code> if the index is invalid
     */
    @Nullable
    @SuppressWarnings("TypeMayBeWeakened")
    private G getListGameObject(final int index) {
        final int actualIndex = getValidIndex(index);
        if (actualIndex >= model.getSize()) {
            return null;
        }

        //DefaultListModel does not use type parameters
        @SuppressWarnings("unchecked")
        final G gameObject = (G) model.getElementAt(actualIndex);
        return gameObject;
    }

    /**
     * Set the selected game object.
     * @param gameObject the game object to select, or <code>null</code> to
     * deselect it
     */
    public void setSelectedGameObject(@Nullable final G gameObject) {
        selectedSquareModel.setSelectedGameObject(gameObject);
    }

    /**
     * Set the currently selected list index. If an invalid index is given, the
     * nearest valid index is used instead.
     * @param index the index to select
     */
    private void setSelectedIndex(final int index) {
        final int actualIndex = getValidIndex(index);
        if (list.getSelectedIndex() != actualIndex) {
            list.setSelectedIndex(actualIndex);
        }
        //JList does not use type parameters
        @SuppressWarnings("unchecked")
        final G gameObject = (G) list.getSelectedValue();
        if (mapCursor != null) {
            mapCursor.setGameObject(gameObject);
        }
    }

    /**
     * Determine a valid list index near a given index.
     * @param index the index to look up
     * @return a valid index, or 0 if the list is empty
     */
    private int getValidIndex(final int index) {
        if (index < 0) {
            return 0;
        }
        final int size = model.getSize();
        if (index >= size) {
            return Math.max(0, size - 1);
        }
        return index;
    }

    /**
     * Action method for selecting the previous game object.
     */
    @ActionMethod
    public void moveSquarePrev() {
        selectedSquareActions.doMoveSquarePrev(true);
    }

    /**
     * Action method for selecting the next game object.
     */
    @ActionMethod
    public void moveSquareNext() {
        selectedSquareActions.doMoveSquareNext(true);
    }

    /**
     * Action method for moving an arch topmost within its square.
     */
    @ActionMethod
    public void moveSquareTop() {
        selectedSquareActions.doMoveSquareTop(true);
    }

    /**
     * Action method for moving an arch up within its square.
     */
    @ActionMethod
    public void moveSquareUp() {
        selectedSquareActions.doMoveSquareUp(true);
    }

    /**
     * Action method for moving an arch down within its square.
     */
    @ActionMethod
    public void moveSquareDown() {
        selectedSquareActions.doMoveSquareDown(true);
    }

    /**
     * Action method for moving an arch topmost within its square.
     */
    @ActionMethod
    public void moveSquareBottom() {
        selectedSquareActions.doMoveSquareBottom(true);
    }

    /**
     * Action method for moving a game object to its environment.
     */
    @ActionMethod
    public void moveSquareEnv() {
        selectedSquareActions.doMoveSquareEnv(true);
    }

    /**
     * Action method for moving a game object to the inventory of its preceding
     * game object.
     */
    @ActionMethod
    public void moveSquareInv() {
        selectedSquareActions.doMoveSquareInv(true);
    }

    /**
     * Deletes a {@link GameObject} with a specific list index.
     * @param index the list index of the game object to delete
     */
    private void deleteGameObject(final int index) {
        final MapCursor<G, A, R> mapCursor = this.mapCursor;
        if (mapCursor != null && index < model.getSize()) {
            setSelectedIndex(index);
            mapCursor.deleteSelectedGameObject(true);
        }
    }

    /**
     * Inserts a new game object.
     * @param index the list index to insert at
     */
    private void insertGameObject(final int index) {
        final BaseObject<G, A, R, ?> gameObject = objectChooser.getSelection();
        if (gameObject != null) {
            final MapCursor<G, A, R> mapCursor = this.mapCursor;
            if (mapCursor != null) {
                mapCursor.insertGameObject(true, gameObject, index >= model.getSize(), true);
            }
        }
        setSelectedIndex(index);
    }

    /**
     * Determines if "select" was selected.
     * @param e the mouse event to check for "select"
     * @return <code>true</code> is "select" was selected
     */
    private static boolean isSelect(@NotNull final InputEvent e) {
        return (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0;
    }

    /**
     * Determines if "insert" was selected.
     * @param e the mouse event to check for "insert"
     * @return <code>true</code> is "insert" was selected
     */
    private static boolean isInsert(@NotNull final InputEvent e) {
        return (e.getModifiers() & (InputEvent.BUTTON3_MASK | InputEvent.CTRL_MASK)) == InputEvent.BUTTON3_MASK;
    }

    /**
     * Determines if "delete" was selected.
     * @param e the mouse event to check for "delete"
     * @return <code>true</code> is "delete" was selected
     */
    private static boolean isDelete(@NotNull final InputEvent e) {
        return (e.getModifiers() & InputEvent.BUTTON2_MASK) != 0 || (e.getModifiers() & (InputEvent.BUTTON3_MASK | InputEvent.CTRL_MASK)) == (InputEvent.BUTTON3_MASK | InputEvent.CTRL_MASK);
    }

    private void setMapCursor(@Nullable final MapModel<G, A, R> mapModel, @Nullable final MapCursor<G, A, R> mapCursor) {
        if (this.mapModel != null) {
            this.mapModel.removeMapModelListener(mapModelListener);
        }
        this.mapModel = mapModel;
        if (this.mapModel != null) {
            this.mapModel.addMapModelListener(mapModelListener);
        }

        if (this.mapCursor != null) {
            this.mapCursor.removeMapCursorListener(mapCursorListener);
        }
        this.mapCursor = mapCursor;
        if (this.mapCursor != null) {
            this.mapCursor.addMapCursorListener(mapCursorListener);
        }

        final G gameObject = mapCursor == null ? null : mapCursor.getGameObject();
        final MapSquare<G, A, R> mapSquare = gameObject == null ? null : gameObject.getMapSquare();
        if (selectedSquareModel.setSelectedMapSquare(mapSquare, gameObject)) {
            refresh();
        }
    }

}
