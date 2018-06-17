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

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link JCheckBox} that supports three states: deselected, indeterminate,
 * selected.
 * @author Andreas Kirschbaum
 */
public class TristateCheckBox extends JCheckBox {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Whether {@link #addMouseListener(MouseListener)} should ignore added
     * listeners.
     */
    private boolean acceptMouseListeners;

    /**
     * The {@link ChangeListener} on model changes to maintain correct
     * focusability.
     */
    @NotNull
    private final ChangeListener enableListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            setFocusable(getModel().isEnabled());
        }

    };

    /**
     * Creates a new instance without icon which is initially {@link
     * TristateState#DESELECTED}.
     * @param text the text of the check box
     */
    public TristateCheckBox(@NotNull final String text) {
        this(text, null, TristateState.DESELECTED);
    }

    /**
     * Creates a new instance.
     * @param text the text of the check box
     * @param icon the icon to display
     * @param initialState the initial state
     */
    private TristateCheckBox(@NotNull final String text, @Nullable final Icon icon, @NotNull final TristateState initialState) {
        this(text, icon, new TristateButtonModel(initialState));
    }

    /**
     * Creates a new instance.
     * @param text the text of the check box
     * @param icon the icon to display
     * @param buttonModel the button model to use
     */
    private TristateCheckBox(@NotNull final String text, @Nullable final Icon icon, @NotNull final ButtonModel buttonModel) {
        super(text, icon);
        acceptMouseListeners = true;
        setModel(buttonModel);
        //noinspection RefusedBequest
        final MouseListener mouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(@NotNull final MouseEvent e) {
                iterateState(e.getModifiers());
            }

        };
        super.addMouseListener(mouseAdapter);
        final ActionMap actions = new ActionMapUIResource();
        final Action action = new AbstractAction() {

            /**
             * The serial version UID.
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(@NotNull final ActionEvent e) {
                iterateState(e.getModifiers());
            }

            @Override
            protected Object clone() {
                try {
                    return super.clone();
                } catch (final CloneNotSupportedException ex) {
                    throw new AssertionError(ex);
                }
            }

        };
        actions.put("pressed", action);
        actions.put("released", null);
        //noinspection ThisEscapedInObjectConstruction
        SwingUtilities.replaceUIActionMap(this, actions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setModel(@NotNull final ButtonModel newModel) {
        final ButtonModel oldModel = getModel();
        if (oldModel instanceof TristateButtonModel) {
            oldModel.removeChangeListener(enableListener);
        }

        super.setModel(newModel);

        if (model instanceof TristateButtonModel) {
            model.addChangeListener(enableListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addMouseListener(@NotNull final MouseListener l) {
        if (acceptMouseListeners) {
            super.addMouseListener(l);
        }
    }

    /**
     * Increments the state.
     * @param modifiers the modifiers of this action
     */
    private void iterateState(final int modifiers) {
        if (!getModel().isEnabled()) {
            return;
        }

        grabFocus();

        final TristateButtonModel buttonModel = getTristateModel();
        buttonModel.setTristateState(nextState(buttonModel.getTristateState()));

        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText(), System.currentTimeMillis(), modifiers));
    }

    /**
     * Returns the next state.
     * @param state the current state
     * @return the next state
     */
    @NotNull
    protected TristateState nextState(@NotNull final TristateState state) {
        return state.nextTristateState();
    }

    /**
     * Returns the model as a {@link TristateButtonModel}.
     * @return the model
     */
    @NotNull
    public TristateButtonModel getTristateModel() {
        return (TristateButtonModel) getModel();
    }

}
