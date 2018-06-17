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

package net.sf.gridarta.gui.utils.tabbedpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A list of {@link AbstractButton buttons} divided into two parts. At most one
 * button in each part is active at any time.
 * @author Andreas Kirschbaum
 */
public class DoubleButtonList {

    /**
     * The {@link Container} that contains all buttons.
     */
    @NotNull
    private final Container buttons = new JPanel();

    /**
     * The {@link Container} that contains all buttons in the standard
     * location.
     */
    @NotNull
    private final ButtonList leftButtonList;

    /**
     * The {@link Container} that contains all buttons in the alternative
     * location.
     */
    @NotNull
    private final ButtonList rightButtonList;

    /**
     * Creates a new instance.
     * @param location the location
     */
    public DoubleButtonList(@NotNull final Location location) {
        leftButtonList = new ButtonList(location);
        rightButtonList = new ButtonList(location);
        buttons.setLayout(new BorderLayout());
        buttons.add(leftButtonList.getButtons(), location.getBorderLocationStandardLocation());
        buttons.add(rightButtonList.getButtons(), location.getBorderLocationAlternativeLocation());
    }

    /**
     * Adds a {@link ButtonListListener} to be notified.
     * @param listener the button list listener
     */

    public void addButtonListListener(@NotNull final ButtonListListener listener) {
        leftButtonList.addButtonListListener(listener);
        rightButtonList.addButtonListListener(listener);
    }

    /**
     * Adds a button.
     * @param button the button
     * @param alternativeLocation whether to add the button into the alternative
     * location
     */
    public void addButton(@NotNull final AbstractButton button, final boolean alternativeLocation) {
        final ButtonList buttonList = alternativeLocation ? rightButtonList : leftButtonList;
        buttonList.addButton(button);
        buttons.validate();
    }

    /**
     * Removes a button.
     * @param button the button
     * @param alternativeLocation whether to remove the button from the
     * alternative location
     */
    public void removeButton(@NotNull final AbstractButton button, final boolean alternativeLocation) {
        final ButtonList buttonList = alternativeLocation ? rightButtonList : leftButtonList;
        buttonList.removeButton(button);
        buttons.validate();
    }

    /**
     * Selects a button.
     * @param button the button
     * @param alternativeLocation whether to select the button in the
     * alternative location
     */
    public void selectButton(@NotNull final AbstractButton button, final boolean alternativeLocation) {
        final ButtonList buttonList = alternativeLocation ? rightButtonList : leftButtonList;
        buttonList.selectButton(button);
    }

    /**
     * Returns the currently selected button.
     * @param alternativeLocation whether to remove the button from the
     * alternative location
     * @return the selected button or <code>null</code> if no button exists
     */
    @Nullable
    public AbstractButton getSelectedButton(final boolean alternativeLocation) {
        final ButtonList buttonList = alternativeLocation ? rightButtonList : leftButtonList;
        return buttonList.getSelectedButton();
    }

    /**
     * Returns the {@link Container} that contains all buttons.
     * @return the container
     */
    @NotNull
    public Component getButtons() {
        return buttons;
    }

    /**
     * Returns the total number of buttons.
     * @return the total number of buttons
     */
    public int getButtonCount() {
        return leftButtonList.getButtonCount() + rightButtonList.getButtonCount();
    }

}
