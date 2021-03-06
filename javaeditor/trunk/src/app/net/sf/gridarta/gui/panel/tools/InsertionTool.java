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
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.sf.gridarta.gui.map.event.MouseOpEvent;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModel;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareView;
import net.sf.gridarta.gui.utils.SwingUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.pickmapsettings.PickmapSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MouseOpListener for insertion.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class InsertionTool<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends BasicAbstractTool<G, A, R> {

    /**
     * Index into {@link #modeComboBox}: guess insertion location.
     */
    private static final int MODE_AUTO = 0;

    /**
     * Index into {@link #modeComboBox}: insert topmost.
     */
    private static final int MODE_TOPMOST = 1;

    /**
     * Index into {@link #modeComboBox}: insert above floor.
     */
    private static final int MODE_ABOVE_FLOOR = 2;

    /**
     * Index into {@link #modeComboBox}: insert below floor.
     */
    private static final int MODE_BELOW_FLOOR = 3;

    /**
     * Index into {@link #modeComboBox}: insert bottommost.
     */
    private static final int MODE_BOTTOMMOST = 4;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta.gui.panel.tools");

    /**
     * The {@link JComboBox} for selecting the insertion mode.
     */
    private final JComboBox modeComboBox = createModeComboBox();

    /**
     * The {@link SelectedSquareView}.
     */
    @NotNull
    private final SelectedSquareView<G, A, R> selectedSquareView;

    /**
     * The {@link SelectedSquareModel}.
     */
    @NotNull
    private final SelectedSquareModel<G, A, R> selectedSquareModel;

    /**
     * The ObjectChooser to use.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link PickmapSettings} to use.
     */
    @NotNull
    private final PickmapSettings pickmapSettings;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * Create a BasicAbstractTool.
     * @param selectedSquareView the selected square view
     * @param selectedSquareModel the selected square model
     * @param objectChooser the ObjectChooser to use
     * @param pickmapSettings the pickmap settings to use
     * @param insertionModeSet the insertion mode set to use
     */
    public InsertionTool(@NotNull final SelectedSquareView<G, A, R> selectedSquareView, @NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final PickmapSettings pickmapSettings, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        super("insertion");
        this.selectedSquareView = selectedSquareView;
        this.selectedSquareModel = selectedSquareModel;
        this.objectChooser = objectChooser;
        this.pickmapSettings = pickmapSettings;
        this.insertionModeSet = insertionModeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pressed(final MouseOpEvent<G, A, R> e) {
        doInsert(e, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dragged(final MouseOpEvent<G, A, R> e) {
        doInsert(e, false);
    }

    /**
     * Inserts an game object.
     * @param e the mouse event to process
     * @param isPressed whether the mouse was pressed (<code>true</code>) or
     * dragged (<code>false</code>)
     */
    private void doInsert(final MouseOpEvent<G, A, R> e, final boolean isPressed) {
        final MapControl<G, A, R> mapControl = e.getMapControl();
        if (mapControl.isPickmap() && pickmapSettings.isLocked()) {
            return;
        }

        final Point p = e.getMapLocation();
       

        final MapCursor<G, A, R> mapCursor = e.getMapCursor();

        if (isPressed) {
            if (p == null) {
                return;
            }
            if (!mapControl.isPickmap()) {
                
                mapCursor.setLocation(p);
              //  int x1= p.x+1;
               //     final Point p2 = new Point(x1,p.y);
              //  mapCursor.setLocation(p2);
                
            }
        } else {
            if (mapControl.isPickmap()) {
                return;
            }
          //  if(p!= null){
           //     int x1 = p.x + 1;
           //     final Point p2 = new Point(x1,p.y);
           //     if (!mapCursor.setLocationSafe(p2)) {
           //         return;
           //     }
          //  }
           // else {
                 if (!mapCursor.setLocationSafe(p)) {
                    return;
                }
           // }
        }

        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
        mapModel.beginTransaction("Insert Object");
        try {
            @Nullable final G insertedObject;
            if (mapControl.isPickmap()) {
                final GameObject<G, A, R> selectedGameObject = selectedSquareModel.getSelectedGameObject();
                if (selectedGameObject != null) {
                    final G head = selectedGameObject.getHead();

                    // check if all spaces are free that the multi will occupy
                    final Point point = new Point();
                    for (R part = head.getArchetype(); part != null; part = part.getMultiNext()) {
                        point.x = p.x + part.getMultiX();
                        point.y = p.y + part.getMultiY();
                        if (!mapArchObject.isPointValid(point) || !mapModel.getMapSquare(point).isEmpty()) {
                            return;
                        }
                    }

                    mapModel.insertBaseObject(head, p, true, false, insertionModeSet.getTopmostInsertionMode());
                }
                insertedObject = null;
            } else {
                final int modeIndex = modeComboBox.getSelectedIndex();
                final InsertionMode<G, A, R> insertionMode;
                switch (modeIndex) {
                case MODE_AUTO:
                    insertionMode = insertionModeSet.getAutoInsertionMode();
                    break;

                case MODE_TOPMOST:
                    insertionMode = insertionModeSet.getTopmostInsertionMode();
                    break;

                case MODE_ABOVE_FLOOR:
                    insertionMode = insertionModeSet.getAboveFloorInsertionMode();
                    break;

                case MODE_BELOW_FLOOR:
                    insertionMode = insertionModeSet.getBelowFloorInsertionMode();
                    break;

                case MODE_BOTTOMMOST:
                    insertionMode = insertionModeSet.getBottommostInsertionMode();
                    break;

                default:
                    throw new AssertionError();
                }
                final BaseObject<G, A, R, ?> selectedArchetype = objectChooser.getSelection();
                insertedObject = selectedArchetype == null ? null : mapModel.insertBaseObject(selectedArchetype, p, isPressed || modeIndex == MODE_AUTO, true, insertionMode);
            }
            selectedSquareView.setSelectedGameObject(insertedObject);
        } finally {
            mapModel.endTransaction();
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

        panel.add(SwingUtils.createLabel("insertionTool.mode", modeComboBox), gbcLabel);
        panel.add(modeComboBox, gbcComboBox);
        return panel;
    }

    /**
     * Create a {@link JComboBox} for selecting the scope to delete.
     * @return the combo box
     */
    private static JComboBox createModeComboBox() {
        final String[] options = { ActionBuilderUtils.getString(ACTION_BUILDER, "insertionTool.mode.auto"), ActionBuilderUtils.getString(ACTION_BUILDER, "insertionTool.mode.topmost"), ActionBuilderUtils.getString(ACTION_BUILDER, "insertionTool.mode.aboveFloor"), ActionBuilderUtils.getString(ACTION_BUILDER, "insertionTool.mode.belowFloor"), ActionBuilderUtils.getString(ACTION_BUILDER, "insertionTool.mode.bottommost"), };
        final JComboBox comboBox = new JComboBox(options);
        comboBox.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "insertionTool.mode.shortdescription"));
        return comboBox;
    }

}
