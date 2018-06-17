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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.gui.utils.ToggleTristateCheckBox;
import net.sf.gridarta.gui.utils.tristate.TristateCheckBox;
import net.sf.gridarta.gui.utils.tristate.TristateState;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.AttributeBitmask;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ActionListener for the change buttons of bitmasks.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MaskChangeAL<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link DialogAttributeBitmask} where this change button belongs to.
     */
    @NotNull
    private final DialogAttributeBitmask<G, A, R> dialogAttributeBitmask;

    /**
     * The all-bits check box.
     */
    @NotNull
    private final TristateCheckBox allCheckbox;

    /**
     * The single-bit check boxes.
     */
    @NotNull
    private final JCheckBox[] checkbox;

    /**
     * The top-level container of the dialog.
     */
    @NotNull
    private final Container gridPanel;

    /**
     * The {@link ChangeListener} attached to all single-bit check boxes.
     */
    @NotNull
    private final ChangeListener singleChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            allCheckbox.getTristateModel().setTristateState(getBitState());
        }
    };

    /**
     * The {@link ChangeListener} attached to the "All" check box.
     */
    @NotNull
    private final ChangeListener allChangeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            switch (allCheckbox.getTristateModel().getTristateState()) {
            case DESELECTED:
                setAllBits(false);
                break;

            case INDETERMINATE:
                // ignore
                break;

            case SELECTED:
                setAllBits(true);
                break;
            }
        }
    };

    /**
     * Constructor.
     * @param label the name of this action
     * @param dialogAttributeBitmask the GUI-bitmask attribute where this change
     * button belongs to
     */
    public MaskChangeAL(@NotNull final String label, @NotNull final DialogAttributeBitmask<G, A, R> dialogAttributeBitmask) {
        super(label);
        this.dialogAttributeBitmask = dialogAttributeBitmask;

        gridPanel = new JPanel(new GridLayout(0, 2, 3, 3));
        final AttributeBitmask attributeBitmask = dialogAttributeBitmask.getBitmask();
        final int number = attributeBitmask.getNumber();
        final int value = dialogAttributeBitmask.getValue();
        checkbox = new JCheckBox[number];
        for (int i = 0; i < number; i++) {
            final String name = attributeBitmask.getBitName(i);
            if (name != null) {
                checkbox[i] = new JCheckBox(name);
                checkbox[i].addChangeListener(singleChangeListener);
                checkbox[i].setSelected(isActive(i + 1, value));
                gridPanel.add(checkbox[i]);
            }
        }
        if (gridPanel.getComponentCount() % 2 != 0) {
            gridPanel.add(new JPanel());
        }
        allCheckbox = new ToggleTristateCheckBox("All"); // TODO: I18N/L10N
        allCheckbox.addChangeListener(allChangeListener);
        gridPanel.add(allCheckbox);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final Integer newValue = showBitmaskDialog();
        if (newValue != null) {
            dialogAttributeBitmask.setValue(newValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * Open a popup frame to select bitmask-entries via choose boxes.
     * @return Integer with new value or <code>null</code> if the user cancelled
     *         the dialog
     */
    @Nullable
    private Integer showBitmaskDialog() {
        setValue();
        final String attributeName = dialogAttributeBitmask.getRef().getAttributeName();
        final String title = "Choose " + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
        if (JOptionPane.showConfirmDialog(dialogAttributeBitmask.getInput(), gridPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            return getValue();
        }
        return null;
    }

    /**
     * Returns the currently selected values.
     * @return the currently selected values
     */
    private int getValue() {
        final AttributeBitmask attributeBitmask = dialogAttributeBitmask.getBitmask();
        final int number = attributeBitmask.getNumber();
        int newValue = 0;
        for (int i = 0; i < number; i++) {
            if (checkbox[i] != null && checkbox[i].isSelected()) {
                newValue |= 1 << i;
            }
        }
        return newValue;
    }

    /**
     * Sets the currently selected values to the default values.
     */
    private void setValue() {
        final int value = dialogAttributeBitmask.getValue();
        final int number = dialogAttributeBitmask.getBitmask().getNumber();
        for (int i = 0; i < number; i++) {
            if (checkbox[i] != null) {
                checkbox[i].setSelected(isActive(i + 1, value));
            }
        }
    }

    /**
     * Sets all bit-value check boxes to the given state.
     * @param state the state
     */
    private void setAllBits(final boolean state) {
        final AttributeBitmask attributeBitmask = dialogAttributeBitmask.getBitmask();
        final int number = attributeBitmask.getNumber();
        for (int i = 0; i < number; i++) {
            if (checkbox[i] != null) {
                checkbox[i].setSelected(state);
            }
        }
    }

    /**
     * Returns the {@link TristateState} that reflects the current state of all
     * bit-value check boxes.
     * @return the tristate state
     */
    @NotNull
    private TristateState getBitState() {
        final AttributeBitmask attributeBitmask = dialogAttributeBitmask.getBitmask();
        final int number = attributeBitmask.getNumber();
        int i;
        for (i = 0; i < number; i++) {
            if (checkbox[i] != null) {
                break;
            }
        }
        if (i >= number) {
            return TristateState.DESELECTED;
        }
        final boolean state = checkbox[i++].isSelected();
        while (i < number) {
            if (checkbox[i] != null && checkbox[i].isSelected() != state) {
                return TristateState.INDETERMINATE;
            }
            i++;
        }
        return TristateState.getTristateState(state);
    }

    /**
     * Check whether the given bit-index is an active bit in the bitmask.
     * @param index index of the bit to check (range from 1-'number')
     * @param mask bitmask to check against
     * @return <code>true</code> if the bit is active in mask, otherwise
     *         <code>false</code>
     */
    private static boolean isActive(final int index, final int mask) {
        final int bit = 1 << (index - 1);
        return (mask & bit) == bit;
    }

}
