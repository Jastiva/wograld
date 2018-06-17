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

package net.sf.gridarta.gui.panel.tools;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.sf.gridarta.gui.map.event.MouseOpEvent;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.utils.SwingUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.pickmapsettings.PickmapSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tool for Deletion.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DeletionTool<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends BasicAbstractTool<G, A, R> {

    /**
     * Index into {@link #deleteComboBox}: delete topmost object.
     */
    private static final int DELETE_TOPMOST = 0;

    /**
     * Index into {@link #deleteComboBox}: delete all objects.
     */
    private static final int DELETE_ALL = 1;

    /**
     * Index into {@link #deleteComboBox}: delete bottommost object.
     */
    private static final int DELETE_BOTTOMMOST = 2;

    /**
     * Index into {@link #scopeComboBox}: delete all objects.
     */
    private static final int SCOPE_SQUARE = 0;

    /**
     * Index into {@link #scopeComboBox}: delete all objects above floors.
     */
    private static final int SCOPE_ABOVE_FLOOR = 1;

    /**
     * Index into {@link #scopeComboBox}: delete all objects below floors.
     */
    private static final int SCOPE_BELOW_FLOOR = 2;

    /**
     * Index into {@link #scopeComboBox}: delete all objects matching the object
     * chooser.
     */
    private static final int SCOPE_SELECTED_OBJECT = 3;

    /**
     * Index into {@link #scopeComboBox}: delete all wall objects.
     */
    private static final int SCOPE_WALL = 4;

    /**
     * Index into {@link #scopeComboBox}: delete all floor objects.
     */
    private static final int SCOPE_FLOOR = 5;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta.gui.panel.tools");

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * A {@link GameObjectMatcher} matching floor game objects.
     */
    @Nullable
    private final GameObjectMatcher floorGameObjectMatcher;

    /**
     * A {@link GameObjectMatcher} matching wall game objects.
     */
    @Nullable
    private final GameObjectMatcher wallGameObjectMatcher;

    /**
     * A {@link GameObjectMatcher} matching monster game objects.
     */
    @Nullable
    private final GameObjectMatcher monsterGameObjectMatcher;

    /**
     * The {@link ObjectChooser} to use.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link PickmapSettings} to use.
     */
    @NotNull
    private final PickmapSettings pickmapSettings;

    /**
     * The action for "ignore walls".
     */
    private final ToggleAction ignoreWallsAction = (ToggleAction) ACTION_BUILDER.createToggle(true, "deletionToolExceptionsIgnoreWalls", this);

    /**
     * The action for "ignore floors".
     */
    private final ToggleAction ignoreFloorsAction = (ToggleAction) ACTION_BUILDER.createToggle(true, "deletionToolExceptionsIgnoreFloors", this);

    /**
     * The action for "ignore monsters".
     */
    private final ToggleAction ignoreMonstersAction = (ToggleAction) ACTION_BUILDER.createToggle(true, "deletionToolExceptionsIgnoreMonsters", this);

    /**
     * Whether "ignore walls" is enabled.
     */
    private boolean deletionToolExceptionsIgnoreWalls;

    /**
     * Whether "ignore floors" is enabled.
     */
    private boolean deletionToolExceptionsIgnoreFloors;

    /**
     * Whether "ignore monsters" is enabled.
     */
    private boolean deletionToolExceptionsIgnoreMonsters;

    /**
     * The {@link JComboBox} for selecting the objects to delete.
     */
    private final JComboBox deleteComboBox = createDeleteComboBox();

    /**
     * The {@link JComboBox} for selecting the scope to delete.
     */
    private final JComboBox scopeComboBox = createScopeComboBox();

    /**
     * Create a DeletionTool.
     * @param mapViewSettings the map view settings instance
     * @param objectChooser the object chooser to use
     * @param pickmapSettings the pickmap settings to use
     * @param floorGameObjectMatcher the floor matcher to use
     * @param wallGameObjectMatcher the wall matcher to use
     * @param monsterGameObjectMatcher the monster matcher to use
     */
    public DeletionTool(@NotNull final MapViewSettings mapViewSettings, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final PickmapSettings pickmapSettings, @Nullable final GameObjectMatcher floorGameObjectMatcher, @Nullable final GameObjectMatcher wallGameObjectMatcher, @Nullable final GameObjectMatcher monsterGameObjectMatcher) {
        super("deletion");
        this.mapViewSettings = mapViewSettings;
        this.objectChooser = objectChooser;
        this.pickmapSettings = pickmapSettings;
        this.floorGameObjectMatcher = floorGameObjectMatcher;
        this.wallGameObjectMatcher = wallGameObjectMatcher;
        this.monsterGameObjectMatcher = monsterGameObjectMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pressed(final MouseOpEvent<G, A, R> e) {
        final Point mapLoc = e.getMapLocation();
        final MapCursor<G, A, R> mapCursor = e.getMapCursor();
        final MapControl<G, A, R> mapControl = e.getMapControl();
    //    if(mapLoc != null){
     //       int x1 = mapLoc.x + 1;
     //       final Point p2 = new Point(x1,mapLoc.y);
     //       mapCursor.setLocationSafe(p2);
     //   }
     //   else {
            mapCursor.setLocationSafe(mapLoc);
     //   }
        
        if (mapLoc != null && mapCursor.isActive()) {
            // delete the topmost arch (matching the view settings) on that square and redraw the map
            deleteArch(mapLoc, mapControl);
        }
      //  mapCursor.setLocationSafe(mapLoc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dragged(final MouseOpEvent<G, A, R> e) {
        final Point mapLoc = e.getMapLocation();
        final MapCursor<G, A, R> mapCursor = e.getMapCursor();
        final MapControl<G, A, R> mapControl = e.getMapControl();
      //   if(mapLoc != null){
      //      int x1 = mapLoc.x + 1;
      //      final Point p2 = new Point(x1,mapLoc.y);
      //        if (mapCursor.setLocationSafe(p2)) {
            // delete the topmost arch (matching the view settings) on that square and redraw the map
      //          deleteArch(mapLoc, mapControl);
     //       }
    //    }
    //    else {
           
     //        if (mapCursor.setLocationSafe(mapLoc)) {
            // delete the topmost arch (matching the view settings) on that square and redraw the map
     //           deleteArch(mapLoc, mapControl);
     //       }
            
     //   }
             
        if (mapCursor.setLocationSafe(mapLoc)) {
            // delete the topmost arch (matching the view settings) on that square and redraw the map
            deleteArch(mapLoc, mapControl);
        }
      //  mapCursor.setLocationSafe(mapLoc);
    }

    /**
     * Delete an arch.
     * @param mapLoc position to delete topmost arch at
     * @param mapControl map control of map to delete arch from
     * @noinspection IfStatementWithIdenticalBranches
     */
    private void deleteArch(final Point mapLoc, final MapControl<G, A, R> mapControl) {
        if (mapControl.isPickmap() && pickmapSettings.isLocked()) {
            return;
        }

        final MapSquare<G, A, R> mapSquare = mapControl.getMapModel().getMapSquare(mapLoc);
        final int scopeType = scopeComboBox.getSelectedIndex();
        final int deleteType = deleteComboBox.getSelectedIndex();

        @Nullable final GameObject<G, A, R> start;
        @Nullable final GameObject<G, A, R> end;
        switch (scopeType) {
        case SCOPE_SQUARE:
            start = mapSquare.getFirst();
            end = mapSquare.getLast();
            break;

        case SCOPE_ABOVE_FLOOR:
            start = floorGameObjectMatcher == null ? null : mapSquare.getAfterLast(floorGameObjectMatcher);
            end = mapSquare.getLast();
            break;

        case SCOPE_BELOW_FLOOR:
            start = mapSquare.getFirst();
            end = floorGameObjectMatcher == null ? null : mapSquare.getBeforeFirst(floorGameObjectMatcher);
            break;

        case SCOPE_SELECTED_OBJECT:
            start = mapSquare.getFirst();
            end = mapSquare.getLast();
            break;

        case SCOPE_WALL:
            start = mapSquare.getFirst();
            end = mapSquare.getLast();
            break;

        case SCOPE_FLOOR:
            start = mapSquare.getFirst();
            end = mapSquare.getLast();
            break;

        default:
            return;
        }
        if (start == null || end == null) {
            return;
        }
        final Collection<G> gameObjectsToDelete = new HashSet<G>();
        boolean foundFirst = false;
        final GameObject<G, A, R> start2 = deleteType == DELETE_TOPMOST ? end : start;
        final GameObject<G, A, R> end2 = deleteType == DELETE_TOPMOST ? start : end;
        for (final GameObject<G, A, R> gameObject : deleteType == DELETE_TOPMOST ? mapSquare.reverse() : mapSquare) {
            if (gameObject == start2) {
                foundFirst = true;
            }
            if (foundFirst) {
                final G head = gameObject.getHead();
                if (mapViewSettings.isEditType(head)) {
                    boolean insert = false;
                    switch (scopeType) {
                    case SCOPE_SQUARE:
                    case SCOPE_ABOVE_FLOOR:
                    case SCOPE_BELOW_FLOOR:
                        insert = true;
                        break;

                    case SCOPE_SELECTED_OBJECT:
                        if (objectChooser.isMatching(head)) {
                            insert = true;
                        }
                        break;

                    case SCOPE_WALL:
                        if (wallGameObjectMatcher != null && wallGameObjectMatcher.isMatching(head)) {
                            insert = true;
                        }
                        break;

                    case SCOPE_FLOOR:
                        if (floorGameObjectMatcher != null && floorGameObjectMatcher.isMatching(head)) {
                            insert = true;
                        }
                        break;
                    }
                    if (insert) {
                        if (deletionToolExceptionsIgnoreWalls && wallGameObjectMatcher != null && wallGameObjectMatcher.isMatching(head)) {
                            // ignore
                        } else if (deletionToolExceptionsIgnoreFloors && floorGameObjectMatcher != null && floorGameObjectMatcher.isMatching(head)) {
                            // ignore
                        } else if (deletionToolExceptionsIgnoreMonsters && monsterGameObjectMatcher != null && monsterGameObjectMatcher.isMatching(head)) {
                            // ignore
                        } else {
                            gameObjectsToDelete.add(head);
                            if (deleteType != DELETE_ALL) {
                                break;
                            }
                        }
                    }
                }
                if (gameObject == end2) {
                    break;
                }
            }
        }
        if (!gameObjectsToDelete.isEmpty()) {
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Delete Object");
            try {
                for (final G gameObjectToDelete : gameObjectsToDelete) {
                    mapModel.removeGameObject(gameObjectToDelete, true);
                }
            } finally {
                mapModel.endTransaction();
            }
        }
        
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Component createOptionsView() {
        final Container panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        final GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.anchor = GridBagConstraints.EAST;

        final GridBagConstraints gbcComboBox = new GridBagConstraints();
        gbcComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbcComboBox.weightx = 1.0;
        gbcComboBox.gridwidth = GridBagConstraints.REMAINDER;

        final GridBagConstraints gbcCheckBox = new GridBagConstraints();
        gbcCheckBox.fill = GridBagConstraints.HORIZONTAL;
        gbcCheckBox.gridwidth = GridBagConstraints.REMAINDER;

        panel.add(SwingUtils.createLabel("deletionTool.delete", deleteComboBox), gbcLabel);
        panel.add(deleteComboBox, gbcComboBox);

        panel.add(SwingUtils.createLabel("deletionTool.scope", scopeComboBox), gbcLabel);
        panel.add(scopeComboBox, gbcComboBox);

        panel.add(ignoreWallsAction.createCheckBox(), gbcCheckBox);
        panel.add(ignoreFloorsAction.createCheckBox(), gbcCheckBox);
        panel.add(ignoreMonstersAction.createCheckBox(), gbcCheckBox);
        return panel;
    }

    /**
     * Create a {@link JComboBox} for selecting the objects to delete.
     * @return the combo box
     */
    private static JComboBox createDeleteComboBox() {
        final String[] options = { ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.delete.top"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.delete.all"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.delete.bottom"), };
        final JComboBox comboBox = new JComboBox(options);
        comboBox.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.delete.shortdescription"));
        return comboBox;
    }

    /**
     * Create a {@link JComboBox} for selecting the scope to delete.
     * @return the combo box
     */
    private static JComboBox createScopeComboBox() {
        final String[] options = { ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.square"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.aboveFloor"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.belowFloor"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.selectedObject"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.wall"), ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.floor"), };
        final JComboBox comboBox = new JComboBox(options);
        comboBox.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "deletionTool.scope.shortdescription"));
        return comboBox;
    }

    /**
     * Returns whether walls should not be deleted.
     * @return whether walls should not be deleted
     */
    @ActionMethod
    public boolean isDeletionToolExceptionsIgnoreWalls() {
        return deletionToolExceptionsIgnoreWalls;
    }

    /**
     * Sets whether walls should not be deleted.
     * @param deletionToolExceptionsIgnoreWalls whether walls should not be
     * deleted
     */
    @ActionMethod
    public void setDeletionToolExceptionsIgnoreWalls(final boolean deletionToolExceptionsIgnoreWalls) {
        this.deletionToolExceptionsIgnoreWalls = deletionToolExceptionsIgnoreWalls;
    }

    /**
     * Returns whether floors should not be deleted.
     * @return whether floors should not be deleted
     */
    @ActionMethod
    public boolean isDeletionToolExceptionsIgnoreFloors() {
        return deletionToolExceptionsIgnoreFloors;
    }

    /**
     * Sets whether floors should not be deleted.
     * @param deletionToolExceptionsIgnoreFloors whether floors should not be
     * deleted
     */
    @ActionMethod
    public void setDeletionToolExceptionsIgnoreFloors(final boolean deletionToolExceptionsIgnoreFloors) {
        this.deletionToolExceptionsIgnoreFloors = deletionToolExceptionsIgnoreFloors;
    }

    /**
     * Returns whether monsters should not be deleted.
     * @return whether monsters should not be deleted
     */
    @ActionMethod
    public boolean isDeletionToolExceptionsIgnoreMonsters() {
        return deletionToolExceptionsIgnoreMonsters;
    }

    /**
     * Sets whether monsters should not be deleted.
     * @param deletionToolExceptionsIgnoreMonsters whether monsters should not
     * be deleted
     */
    @ActionMethod
    public void setDeletionToolExceptionsIgnoreMonsters(final boolean deletionToolExceptionsIgnoreMonsters) {
        this.deletionToolExceptionsIgnoreMonsters = deletionToolExceptionsIgnoreMonsters;
    }

}
