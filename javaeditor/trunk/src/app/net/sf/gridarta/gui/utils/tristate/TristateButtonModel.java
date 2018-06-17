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

package net.sf.gridarta.gui.utils.tristate;

import java.awt.event.ItemEvent;
import javax.swing.JToggleButton.ToggleButtonModel;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ToggleButtonModel} supporting three states (deselected,
 * indeterminate, selected).
 * @author Andreas Kirschbaum
 */
public class TristateButtonModel extends ToggleButtonModel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The current state.
     * @serial
     */
    @NotNull
    private TristateState state = TristateState.DESELECTED;

    /**
     * Creates a new instance.
     * @param initialState the initial state
     */
    public TristateButtonModel(@NotNull final TristateState initialState) {
        setTristateState(initialState);
    }

    /**
     * Create a new instance. The default state is {@link
     * TristateState#DESELECTED}.
     */
    public TristateButtonModel() {
        this(TristateState.DESELECTED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        updateCheckboxState();
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public void setSelected(final boolean b) {
        setTristateState(TristateState.getTristateState(b));
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public void setArmed(final boolean b) {
        // ignore
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public void setPressed(final boolean b) {
        // ignore
    }

    /**
     * Returns the internal state.
     * @return the internal state
     */
    @NotNull
    public TristateState getTristateState() {
        return state;
    }

    /**
     * Sets the state. Both internal and check box state are updated.
     * @param state the new state
     */
    public final void setTristateState(@NotNull final TristateState state) {
        this.state = state;
        updateCheckboxState();

        if (state == TristateState.INDETERMINATE && isEnabled()) {
            fireStateChanged();
            final int indeterminate = 3;
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, indeterminate));
        }
    }

    /**
     * Updates the check box state to reflect the internal state.
     */
    private void updateCheckboxState() {
        super.setSelected(state != TristateState.DESELECTED);
        super.setArmed(state == TristateState.INDETERMINATE);
        super.setPressed(state == TristateState.INDETERMINATE);

    }

}
