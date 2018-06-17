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

package net.sf.gridarta.gui.dialog.golocation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.map.AbstractPerMapDialogManager;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog to ask the user for coordinates to move the cursor to.
 * @author Andreas Kirschbaum
 */
public class GoLocationDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

    /**
     * The serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The dialog manager for this dialog.
     */
    @NotNull
    private final AbstractPerMapDialogManager<G, A, R, GoLocationDialog<G, A, R>> goLocationDialogManager;

    /**
     * The affected map view this go location dialog affects.
     */
    @NotNull
    private final MapView<G, A, R> mapView;

    /**
     * The text input field for the x coordinate.
     */
    @NotNull
    private final JTextField xCoordinateField = new JTextField();

    /**
     * The text input field for the y coordinate.
     */
    @NotNull
    private final JTextField yCoordinateField = new JTextField();

    /**
     * The {@link JButton} for ok.
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "goLocationOkay", this));

    /**
     * The {@link JButton} for cancel.
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "goLocationCancel", this));

    /**
     * The {@link JDialog} instance.}
     */
    @NotNull
    private final JDialog dialog;

    /**
     * Creates a new instance.
     * @param goLocationDialogManager the dialog manager for this dialog
     * @param mapView the map view to change the cursor
     */
    public GoLocationDialog(@NotNull final AbstractPerMapDialogManager<G, A, R, GoLocationDialog<G, A, R>> goLocationDialogManager, @NotNull final MapView<G, A, R> mapView) {
        this.goLocationDialogManager = goLocationDialogManager;

        okButton.setDefaultCapable(true);
        final JButton applyButton = new JButton(ACTION_BUILDER.createAction(false, "goLocationApply", this));
        setOptions(new Object[] { okButton, applyButton, cancelButton });

        this.mapView = mapView;
        setMessage(createPanel());

        TextComponentUtils.setAutoSelectOnFocus(xCoordinateField);
        TextComponentUtils.setAutoSelectOnFocus(yCoordinateField);
        TextComponentUtils.setActionNextFocus(xCoordinateField, yCoordinateField);

        dialog = createDialog(mapView.getComponent(), ActionBuilderUtils.getString(ACTION_BUILDER, "goLocation.title"));
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setModal(false);

        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * Creates the GUI.
     * @return the panel containing the GUI
     */
    @NotNull
    private JPanel createPanel() {
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.setBorder(GUIConstants.DIALOG_BORDER);

        mainPanel.add(new JLabel(ACTION_BUILDER.format("goLocationText", mapView.getMapControl().getMapModel().getMapArchObject().getMapName())));
        mainPanel.add(Box.createVerticalStrut(5));

        final JComponent coordinatesPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbcLabel = new GridBagConstraints();
        final GridBagConstraints gbcField = new GridBagConstraints();
        gbcLabel.insets = new Insets(2, 2, 2, 2);
        gbcField.insets = new Insets(2, 2, 2, 2);
        gbcLabel.anchor = GridBagConstraints.EAST;
        gbcField.gridwidth = GridBagConstraints.REMAINDER;
        coordinatesPanel.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder()), GUIConstants.DIALOG_BORDER));

        final Point point = mapView.getMapCursor().getLocation();

        coordinatesPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "goLocationX"), gbcLabel);
        xCoordinateField.setText(point == null ? "0" : Integer.toString(point.x));
        xCoordinateField.setColumns(3);
        coordinatesPanel.add(xCoordinateField, gbcField);

        coordinatesPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "goLocationY"), gbcLabel);
        yCoordinateField.setText(point == null ? "0" : Integer.toString(point.y));
        yCoordinateField.setColumns(3);
        coordinatesPanel.add(yCoordinateField, gbcField);

        mainPanel.add(coordinatesPanel);
        mainPanel.add(Box.createVerticalStrut(5));

        return mainPanel;
    }

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void goLocationOkay() {
        if (goLocation()) {
            setValue(okButton);
        }
    }

    /**
     * Action method for apply.
     */
    @ActionMethod
    public void goLocationApply() {
        goLocation();
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void goLocationCancel() {
        setValue(cancelButton);
    }

    /**
     * Moves the cursor to the given coordinates.
     * @return <code>true</code> if the cursor was moved, <code>false</code> if
     *         the coordinates were wrong
     */
    private boolean goLocation() {
        final Size2D mapSize = mapView.getMapControl().getMapModel().getMapArchObject().getMapSize();
        final Point point = new Point();
        try {
            point.x = parseCoordinate(xCoordinateField, mapSize.getWidth());
            point.y = parseCoordinate(yCoordinateField, mapSize.getHeight());
        } catch (final IllegalArgumentException ignored) {
            return false;
        }

        mapView.setCursorLocation(point);
        return true;
    }

    /**
     * Parses a coordinate input field.
     * @param textField the input text field to parse
     * @param range the coordinate value must be between 0 and
     * <code>range-1</code>
     * @return the coordinate value
     * @throws IllegalArgumentException if the coordinate value is invalid
     */
    private int parseCoordinate(@NotNull final JTextComponent textField, final int range) {
        final int result;
        try {
            result = Integer.parseInt(textField.getText());
        } catch (final NumberFormatException e) {
            ACTION_BUILDER.showMessageDialog(this, "goLocationCoordinateNotANumber");
            textField.requestFocus();
            throw e;
        } catch (final IllegalArgumentException e) {
            ACTION_BUILDER.showMessageDialog(this, "goLocationCoordinateOutOfRange");
            textField.requestFocus();
            throw e;
        }
        if (result < 0 || result >= range) {
            ACTION_BUILDER.showMessageDialog(this, "goLocationCoordinateOutOfRange");
            textField.requestFocus();
            throw new IllegalArgumentException("invalid coordinate value: " + result);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (newValue != UNINITIALIZED_VALUE) {
            goLocationDialogManager.disposeDialog(mapView);
        }
    }

    /**
     * Returns the {@link Window} for this instance.
     * @return the window
     */
    @NotNull
    public Window getDialog() {
        return dialog;
    }

}
