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

package net.sf.gridarta.gui.dialog.shrinkmapsize;

import java.awt.Component;
import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog to ask which empty borders to remove from a map.
 * @author Andreas Kirschbaum
 */
public class ShrinkMapSizeDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

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
     * The manager for this dialog.
     */
    @NotNull
    private final ShrinkMapSizeDialogManager<G, A, R> shrinkMapSizeDialogManager;

    /**
     * The affected map view of this dialog.
     */
    @NotNull
    private final MapView<G, A, R> mapView;

    /**
     * Checkbox to remove empty squares at the east edge.
     */
    @NotNull
    private final AbstractButton eastCheckBox = new JCheckBox(ACTION_BUILDER.createToggle(false, "shrinkMapSizeDialogEast", this));

    /**
     * Checkbox to remove empty squares at the south edge.
     */
    @NotNull
    private final AbstractButton southCheckBox = new JCheckBox(ACTION_BUILDER.createToggle(false, "shrinkMapSizeDialogSouth", this));

    /**
     * Text area for displaying warning messages.
     */
    @NotNull
    private final JLabel warnings = new JLabel();

    /**
     * JButton for ok.
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "shrinkMapSizeDialogOk", this));

    /**
     * JButton for cancel.
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "shrinkMapSizeDialogCancel", this));

    /**
     * The associated {@link JDialog} instance.
     */
    @Nullable
    private JDialog dialog;

    /**
     * Creates a new instance.
     * @param shrinkMapSizeDialogManager the manager for this dialog
     * @param mapView the map view to affect
     */
    public ShrinkMapSizeDialog(@NotNull final ShrinkMapSizeDialogManager<G, A, R> shrinkMapSizeDialogManager, @NotNull final MapView<G, A, R> mapView) {
        this.shrinkMapSizeDialogManager = shrinkMapSizeDialogManager;

        okButton.setDefaultCapable(true);
        setOptions(new Object[] { okButton, cancelButton });

        this.mapView = mapView;
        setMessage(createPanel());

        dialog = createDialog(mapView.getComponent(), ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogTitle"));
        dialog.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final JDialog createDialog(@NotNull final Component parentComponent, @NotNull final String title) {
        final JDialog result = super.createDialog(parentComponent, title);
        result.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        result.getRootPane().setDefaultButton(okButton);
        result.setModal(false);
        result.pack();
        return result;
    }

    /**
     * Creates the GUI.
     * @return the panel containing the GUI
     */
    @NotNull
    private JPanel createPanel() {
        final JComponent label = ActionBuilderUtils.newLabel(ACTION_BUILDER, "shrinkMapSizeDialogLabel");
        label.setAlignmentX(0.0F);
        eastCheckBox.setAlignmentX(0.0F);
        southCheckBox.setAlignmentX(0.0F);
        warnings.setAlignmentX(0.0F);
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(GUIConstants.DIALOG_BORDER);
        mainPanel.add(label);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(eastCheckBox);
        mainPanel.add(southCheckBox);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(warnings);
        mainPanel.add(Box.createVerticalStrut(5));
        final int shrinkFlags = ShrinkMapSizeUtils.getShrinkFlags(mapView.getMapControl().getMapModel());
        eastCheckBox.setEnabled((shrinkFlags & ShrinkMapSizeUtils.SHRINK_EAST) != 0);
        eastCheckBox.setSelected((shrinkFlags & ShrinkMapSizeUtils.SHRINK_EAST) != 0);
        southCheckBox.setEnabled((shrinkFlags & ShrinkMapSizeUtils.SHRINK_SOUTH) != 0);
        southCheckBox.setSelected((shrinkFlags & ShrinkMapSizeUtils.SHRINK_SOUTH) != 0);
        updateWarnings();
        return mainPanel;
    }

    /**
     * Updates the {@link #warnings} label.
     */
    private void updateWarnings() {
        final StringBuilder sb = new StringBuilder(100);
        if (!eastCheckBox.isEnabled() && !southCheckBox.isEnabled()) {
            sb.append("<p>");
            sb.append(ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogNoEmptySpace"));
        } else {
            final MapArchObject<A> mapArchObject = mapView.getMapControl().getMapModel().getMapArchObject();
            if (eastCheckBox.isEnabled() && eastCheckBox.isSelected()) {
                if (!mapArchObject.getTilePath(Direction.NORTH).isEmpty()) {
                    sb.append("<p>");
                    sb.append(ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogEastBreaksNorthTiling"));
                }
                if (!mapArchObject.getTilePath(Direction.SOUTH).isEmpty()) {
                    sb.append("<p>");
                    sb.append(ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogEastBreaksSouthTiling"));
                }
            }
            if (southCheckBox.isEnabled() && southCheckBox.isSelected()) {
                if (!mapArchObject.getTilePath(Direction.EAST).isEmpty()) {
                    sb.append("<p>");
                    sb.append(ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogSouthBreaksEastTiling"));
                }
                if (!mapArchObject.getTilePath(Direction.WEST).isEmpty()) {
                    sb.append("<p>");
                    sb.append(ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogSouthBreaksWestTiling"));
                }
            }
        }
        okButton.setEnabled(eastCheckBox.isSelected() || southCheckBox.isSelected());
        if (sb.length() > 0) {
            sb.insert(0, ActionBuilderUtils.getString(ACTION_BUILDER, "shrinkMapSizeDialogWarning"));
            sb.insert(0, "<html>");
            warnings.setText(sb.toString());
        } else {
            warnings.setText("");
        }
    }

    /**
     * Action method for ok.
     */
    @ActionMethod
    public void shrinkMapSizeDialogOk() {
        shrinkMapSize();
        setValue(okButton);
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void shrinkMapSizeDialogCancel() {
        setValue(cancelButton);
    }

    /**
     * Shrinks the map model.
     */
    private void shrinkMapSize() {
        int shrinkFlags = 0;
        if (eastCheckBox.isSelected()) {
            shrinkFlags |= ShrinkMapSizeUtils.SHRINK_EAST;
        }
        if (southCheckBox.isSelected()) {
            shrinkFlags |= ShrinkMapSizeUtils.SHRINK_SOUTH;
        }
        ShrinkMapSizeUtils.shrinkMap(mapView.getMapControl().getMapModel(), shrinkFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (newValue != UNINITIALIZED_VALUE) {
            shrinkMapSizeDialogManager.disposeDialog(mapView);
        }
    }

    /**
     * Returns the {@link JDialog} for this instance.
     * @return the dialog
     */
    @Nullable
    public Window getDialog() {
        return dialog;
    }

    /**
     * Action method for "shrinkMapSizeDialogEast".
     * @return whether the check box is checked
     */
    @ActionMethod
    public boolean isShrinkMapSizeDialogEast() {
        return eastCheckBox.isSelected();
    }

    /**
     * Action method for "shrinkMapSizeDialogEast".
     * @param flag whether the check box is checked
     */
    @ActionMethod
    public void setShrinkMapSizeDialogEast(final boolean flag) {
        eastCheckBox.setSelected(!flag);
        updateWarnings();
    }

    /**
     * Action method for "shrinkMapSizeDialogSouth".
     * @return whether the check box is checked
     */
    @ActionMethod
    public boolean isShrinkMapSizeDialogSouth() {
        return southCheckBox.isSelected();
    }

    /**
     * Action method for "shrinkMapSizeDialogSouth".
     * @param flag whether the check box is checked
     */
    @ActionMethod
    public void setShrinkMapSizeDialogSouth(final boolean flag) {
        southCheckBox.setSelected(!flag);
        updateWarnings();
    }

}
