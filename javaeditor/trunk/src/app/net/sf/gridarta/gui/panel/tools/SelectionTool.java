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
import java.awt.event.InputEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.sf.gridarta.gui.map.event.MouseOpEvent;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.utils.SwingUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.floodfill.FillUtils;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tool for Selection.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class SelectionTool<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends BasicAbstractTool<G, A, R> {

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
     * The action for "ignore walls".
     */
    private final ToggleAction autoFillAction = (ToggleAction) ACTION_BUILDER.createToggle(true, "selectionToolAutoFill", this);

    /**
     * The {@link JComboBox} for selecting the insertion mode.
     */
    private final JComboBox modeComboBox = createModeComboBox();

    /**
     * The object chooser to update.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * Whether auto-fill is enabled.
     */
    private boolean selectionToolAutoFill;

    /**
     * Create a BasicAbstractTool.
     * @param objectChooser the object chooser to update
     * @param insertionModeSet the insertion mode set to use
     */
    public SelectionTool(@NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        super("selection");
        this.objectChooser = objectChooser;
        this.insertionModeSet = insertionModeSet;
        selectionToolAutoFillChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pressed(final MouseOpEvent<G, A, R> e) {
        final Point mapLoc = e.getMapLocation();
        final int mod = e.getModifiers();
        final MapCursor<G, A, R> mapCursor = e.getMapCursor();
        mapCursor.beginTransaction();
        try {
            // left mouse button: select squares
            // Throw away old selection if neither SHIFT nor CTRL is not pressed
            if ((mod & (InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)) == 0) {
                mapCursor.deactivate();
            }
            mapCursor.setLocation(mapLoc);
            mapCursor.dragStart();
        } finally {
            mapCursor.endTransaction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dragged(final MouseOpEvent<G, A, R> e) {
        e.getMapCursor().dragTo(e.getMapLocation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void released(final MouseOpEvent<G, A, R> e) {
        final MapCursor<G, A, R> mapCursor = e.getMapCursor();
        final int modifiers = e.getModifiers();
        if (mapCursor.isOnGrid(e.getMapLocation())) {
            if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
                if ((modifiers & InputEvent.CTRL_DOWN_MASK) == 0) {
                    mapCursor.dragSelect(SelectionMode.ADD);
                } else {
                    mapCursor.dragSelect(SelectionMode.FLIP);
                }
            } else if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
                mapCursor.dragSelect(SelectionMode.SUB);
            } else {
                mapCursor.dragSelect(SelectionMode.ADD);
            }
        } else {
            mapCursor.dragRelease();
        }

        if (selectionToolAutoFill) {
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
            final MapView<G, A, R> mapView = e.getMapView();
            FillUtils.fill(mapView.getMapControl().getMapModel(), mapView.getSelectedSquares(), insertionMode, objectChooser.getSelections(), -1, false);
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

        panel.add(autoFillAction.createCheckBox(), gbcCheckBox);
        panel.add(SwingUtils.createLabel("selectionTool.mode", modeComboBox), gbcLabel);
        panel.add(modeComboBox, gbcComboBox);
        return panel;
    }

    /**
     * Create a {@link JComboBox} for selecting the scope to delete.
     * @return the combo box
     */
    private static JComboBox createModeComboBox() {
        final String[] options = { ActionBuilderUtils.getString(ACTION_BUILDER, "selectionTool.mode.auto"), ActionBuilderUtils.getString(ACTION_BUILDER, "selectionTool.mode.topmost"), ActionBuilderUtils.getString(ACTION_BUILDER, "selectionTool.mode.aboveFloor"), ActionBuilderUtils.getString(ACTION_BUILDER, "selectionTool.mode.belowFloor"), ActionBuilderUtils.getString(ACTION_BUILDER, "selectionTool.mode.bottommost"), };
        final JComboBox comboBox = new JComboBox(options);
        comboBox.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "selectionTool.mode.shortdescription"));
        return comboBox;
    }

    /**
     * Returns whether auto-fill is enabled.
     * @return whether auto-fill is enabled
     */
    @ActionMethod
    public boolean isSelectionToolAutoFill() {
        return selectionToolAutoFill;
    }

    /**
     * Sets whether auto-fill is enabled.
     * @param selectionToolAutoFill whether auto-fill is enabled
     */
    @ActionMethod
    public void setSelectionToolAutoFill(final boolean selectionToolAutoFill) {
        if (this.selectionToolAutoFill == selectionToolAutoFill) {
            return;
        }
        this.selectionToolAutoFill = selectionToolAutoFill;
        selectionToolAutoFillChanged();
    }

    /**
     * Updates the GUI state to reflect the current {@link
     * #selectionToolAutoFill} state.
     */
    private void selectionToolAutoFillChanged() {
        modeComboBox.setEnabled(selectionToolAutoFill);
    }

}
