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

package net.sf.gridarta.gui.utils;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI component for selecting a direction.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @noinspection AbstractClassExtendsConcreteClass
 */
public abstract class DirectionComponent extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder} instance.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Empty {@link Insets}.
     */
    @NotNull
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

    /**
     * The {@link ButtonGroup} for the direction buttons.
     */
    @NotNull
    private final ButtonGroup directionButtonGroup = new ButtonGroup();

    /**
     * The buttons in {@link #directionButtonGroup}.
     */
    @NotNull
    private final Collection<JToggleButton> directionButtons = new ArrayList<JToggleButton>();

    /**
     * Maps direction to button.
     */
    @NotNull
    private final Map<Integer, JToggleButton> directions = new HashMap<Integer, JToggleButton>(9);

    /**
     * Whether the buttons are currently enabled.
     */
    private boolean enableButtons;

    /**
     * Creates a new instance.
     * @param includeDefault whether the "default" button should be shown
     */
    protected DirectionComponent(final boolean includeDefault) {
        super(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(1, 1, 1, 1);
        createButton(7, gbc, 0, 0);
        createButton(8, gbc, 1, 0);
        createButton(1, gbc, 2, 0);
        createButton(6, gbc, 0, 1);
        createButton(0, gbc, 1, 1);
        createButton(2, gbc, 2, 1);
        createButton(5, gbc, 0, 2);
        createButton(4, gbc, 1, 2);
        createButton(3, gbc, 2, 2);
        if (includeDefault) {
            gbc.gridwidth = 3;
            createButton(null, gbc, 0, 3);
        }
        updateEnabled(true);
        updateDirection(0);
    }

    /**
     * Enables/disables the direction buttons for a given archetype.
     * @param enableButtons whether the buttons are enabled
     */
    protected final void updateEnabled(final boolean enableButtons) {
        this.enableButtons = enableButtons;
        updateEnabled();
    }

    /**
     * Sets the selected direction.
     * @param direction the direction
     */
    public final void updateDirection(@Nullable final Integer direction) {
        final AbstractButton selectedButton = directions.get(direction);
        if (selectedButton != null) {
            selectedButton.setSelected(true);
        }
    }

    /**
     * Creates a direction button.
     * @param direction the direction for the button
     * @param gbc the grid bag constraints to modify
     * @param x the x position of the button
     * @param y the y position of the button
     */
    private void createButton(@Nullable final Integer direction, @NotNull final GridBagConstraints gbc, final int x, final int y) {
        final JToggleButton button = new JToggleButton(ACTION_BUILDER.createAction(false, "direction" + direction, this));
        directionButtonGroup.add(button);
        button.setFocusable(false);
        //button.setEnabled(false);
        button.setMargin(EMPTY_INSETS);
        gbc.gridx = x;
        gbc.gridy = y;
        add(button, gbc);
        directionButtons.add(button);
        button.setSelected(true);
        directions.put(direction, button);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction0() {
        direction(0);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction1() {
        direction(1);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction2() {
        direction(2);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction3() {
        direction(3);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction4() {
        direction(4);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction5() {
        direction(5);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction6() {
        direction(6);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction7() {
        direction(7);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction8() {
        direction(8);
    }

    /**
     * Action method for default direction.
     */
    @ActionMethod
    public void directionnull() {
        direction(null);
    }

    /**
     * Called whenever a direction button has been selected.
     * @param direction the selected direction
     */
    protected abstract void direction(@Nullable final Integer direction);

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        updateEnabled();
    }

    /**
     * Enables/disables the buttons.
     */
    private void updateEnabled() {
        final boolean enabled = enableButtons && isEnabled();
        for (final Component button : directionButtons) {
            button.setEnabled(enabled);
        }
    }

}
