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

package net.sf.gridarta.gui.dialog.find;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.select.ArchetypeNameMatchCriteria;
import net.sf.gridarta.model.select.AttributeOtherValueMatchCriteria;
import net.sf.gridarta.model.select.AttributeValueMatchCriteria;
import net.sf.gridarta.model.select.MatchCriteria;
import net.sf.gridarta.model.select.ObjectNameMatchCriteria;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * This dialog manages the find action.
 * @author Andreas Kirschbaum
 */
public class FindDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The dialog instance.
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The parent component for dialogs.
     */
    @NotNull
    private final Component parent;

    /**
     * Whether this find dialog has been displayed.
     */
    private boolean isBuilt;

    /**
     * The {@link MapView} to operate on.
     */
    @NotNull
    private MapView<G, A, R> mapView;

    /**
     * The text input field for the string to find.
     */
    @NotNull
    private final JTextComponent findInput = new JTextField(20);

    /**
     * The checkbox for matching 'name' attributes.
     */
    @NotNull
    private final AbstractButton findNameCheckbox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhereName"));

    /**
     * The checkbox for matching archetype names.
     */
    @NotNull
    private final AbstractButton findArchCheckbox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhereArch"));

    /**
     * The checkbox for matching 'msg' attributes.
     */
    @NotNull
    private final AbstractButton findMsgCheckbox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhereMsg"));

    /**
     * The checkbox for matching 'face' or 'animation' attributes.
     */
    @NotNull
    private final AbstractButton findFaceCheckbox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhereFace"));

    /**
     * The checkbox for matching 'slaying' attributes.
     */
    @NotNull
    private final AbstractButton findSlayingCheckbox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhereSlaying"));

    /**
     * The checkbox for matching all other attributes.
     */
    @NotNull
    private final AbstractButton findOtherCheckbox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhereOther"));

    /**
     * Creates a new instance.
     * @param parent the parent component for dialogs
     */
    public FindDialog(@NotNull final Component parent) {
        dialog = createDialog(parent, "");
        dialog.setModal(false);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.parent = parent;

        TextComponentUtils.setAutoSelectOnFocus(findInput);
    }

    /**
     * Replace objects on the map.
     * @param mapView map view of the active map where the action was invoked
     */
    public void display(@NotNull final MapView<G, A, R> mapView) {
        if (isBuilt) {
            this.mapView = mapView;

            dialog.pack();
            dialog.toFront();
        } else {
            this.mapView = mapView;
            final JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5));

            final Container lineFind = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JComponent labelFind = ActionBuilderUtils.newLabel(ACTION_BUILDER, "findFind");
            labelFind.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "findFind.shortdescription"));
            lineFind.add(labelFind);
            lineFind.add(Box.createVerticalStrut(3));
            lineFind.add(findInput);
            lineFind.add(Box.createVerticalStrut(3));
            mainPanel.add(lineFind);

            final Container lineWhere = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JComponent labelWhere = ActionBuilderUtils.newLabel(ACTION_BUILDER, "findWhere");
            labelWhere.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "findWhere.shortdescription"));
            lineWhere.add(labelWhere);
            lineWhere.add(Box.createVerticalStrut(5));

            final JPanel panelWhere = new JPanel();
            panelWhere.setLayout(new BoxLayout(panelWhere, BoxLayout.Y_AXIS));
            panelWhere.add(findNameCheckbox);
            panelWhere.add(findArchCheckbox);
            panelWhere.add(findMsgCheckbox);
            panelWhere.add(findFaceCheckbox);
            panelWhere.add(findSlayingCheckbox);
            panelWhere.add(findOtherCheckbox);
            lineWhere.add(panelWhere);
            mainPanel.add(lineWhere);

            final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "findOk", this));
            final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "findCancel", this));

            findNameCheckbox.setSelected(true);
            findMsgCheckbox.setSelected(true);
            findSlayingCheckbox.setSelected(true);

            setMessage(mainPanel);
            setOptions(new Object[] { okButton, cancelButton });
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
            dialog.setLocationRelativeTo(parent);

            isBuilt = true;
        }
        dialog.setTitle(ActionBuilderUtils.format(ACTION_BUILDER, "findTitle", mapView.getMapControl().getMapModel().getMapArchObject().getMapName()));
        dialog.setVisible(true);
        findInput.requestFocusInWindow();
    }

    /**
     * Re-executes the previous find operation.
     * @param mapView the map view to operate on
     * @param forward whether to search forward (<code>true</code>) or backward
     * (<code>false</code>)
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean findAgain(@NotNull final MapView<G, A, R> mapView, final boolean forward, final boolean performAction) {
        if (this.mapView != mapView) {
            return false;
        }
        if (performAction) {
            doFind(forward);
        }
        return true;
    }

    /**
     * Action method for Ok button.
     */
    @ActionMethod
    public void findOk() {
        if (doFind(true)) {
            dialog.setVisible(false);
        }
    }

    /**
     * Action method for Cancel button.
     */
    @ActionMethod
    public void findCancel() {
        dialog.setVisible(false);
    }

    /**
     * Executes one find operation.
     * @param forward whether to search forward (<code>true</code>) or backward
     * (<code>false</code>)
     * @return whether the find operation was successful
     */
    private boolean doFind(final boolean forward) {
        final String findString = findInput.getText().trim();
        final Collection<MatchCriteria<G, A, R>> matchCriterias = new ArrayList<MatchCriteria<G, A, R>>();
        if (findNameCheckbox.isSelected()) {
            matchCriterias.add(new ObjectNameMatchCriteria<G, A, R>(findString));
        }
        if (findArchCheckbox.isSelected()) {
            matchCriterias.add(new ArchetypeNameMatchCriteria<G, A, R>(findString));
        }
        if (findMsgCheckbox.isSelected()) {
            matchCriterias.add(new AttributeValueMatchCriteria<G, A, R>("msg", findString));
        }
        if (findFaceCheckbox.isSelected()) {
            matchCriterias.add(new AttributeValueMatchCriteria<G, A, R>("face", findString));
            matchCriterias.add(new AttributeValueMatchCriteria<G, A, R>("animation", findString));
        }
        if (findSlayingCheckbox.isSelected()) {
            matchCriterias.add(new AttributeValueMatchCriteria<G, A, R>("slaying", findString));
        }
        if (findOtherCheckbox.isSelected()) {
            matchCriterias.add(new AttributeOtherValueMatchCriteria<G, A, R>(findString, "msg", "face", "animation", "slaying"));
        }
        return !matchCriterias.isEmpty() && doFind(matchCriterias, forward) > 0;
    }

    /**
     * This method performs the actual find action on a map.
     * @param matchCriterias the matching criterias to find
     * @param forward whether to search forward (<code>true</code>) or backward
     * (<code>false</code>)
     * @return the number of objects found
     */
    private int doFind(@NotNull final Iterable<MatchCriteria<G, A, R>> matchCriterias, final boolean forward) {
        final List<G> matchingGameObjects = new ArrayList<G>();
        final Collection<MapSquare<G, A, R>> matchingMapSquares = new ArrayList<MapSquare<G, A, R>>();
        final MapControl<G, A, R> mapControl = mapView.getMapControl();
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        for (final MapSquare<G, A, R> mapSquare : mapModel) {
            boolean matchesMapSquare = false;
            for (final G gameObject : mapSquare.recursive()) {
                for (final MatchCriteria<G, A, R> matchCriteria : matchCriterias) {
                    if (matchCriteria.matches(gameObject)) {
                        matchingGameObjects.add(gameObject);
                        matchesMapSquare = true;
                    }
                }
            }
            if (matchesMapSquare) {
                matchingMapSquares.add(mapSquare);
            }
        }
        selectMapSquares(matchingMapSquares);
        setMapCursor(forward, matchingGameObjects);
        return matchingGameObjects.size();
    }

    /**
     * Selects a set of {@link MapSquare MapSquares} on the map.
     * @param mapSquares the map squares to select
     */
    private void selectMapSquares(@NotNull final Iterable<MapSquare<G, A, R>> mapSquares) {
        final MapGrid mapGrid = mapView.getMapGrid();
        mapGrid.unSelect();
        final Point p = new Point();
        for (final MapSquare<G, A, R> mapSquare : mapSquares) {
            p.x = mapSquare.getMapX();
            p.y = mapSquare.getMapY();
            mapGrid.select(p, SelectionMode.ADD);
        }
    }

    /**
     * Moves the cursor to the next or previous matching game object.
     * @param forward whether to move the cursor forward (<code>true</code>) or
     * backward (<code>false</code>)
     * @param gameObjects the game objects to consider
     */
    private void setMapCursor(final boolean forward, @NotNull final List<G> gameObjects) {
        if (gameObjects.isEmpty()) {
            return;
        }

        final MapCursor<G, A, R> mapCursor = mapView.getMapCursor();
        final int index;
        final G selectedGameObject = mapCursor.getGameObject();
        if (selectedGameObject == null) {
            index = 0;
        } else {
            final int selectedIndex = gameObjects.indexOf(selectedGameObject);
            if (selectedIndex == -1) {
                index = 0;
            } else if (forward) {
                index = selectedIndex + 1 < gameObjects.size() ? selectedIndex + 1 : 0;
            } else {
                index = selectedIndex > 0 ? selectedIndex - 1 : gameObjects.size() - 1;
            }
        }
        mapCursor.setGameObject(gameObjects.get(index));
    }

    /**
     * Disposes the find dialog.
     * @param mapView the map view to dispose the dialog of; do nothing if no
     */
    public void dispose(@NotNull final MapView<G, A, R> mapView) {
        if (mapView == this.mapView) {
            dialog.setVisible(false);
        }
    }

}
