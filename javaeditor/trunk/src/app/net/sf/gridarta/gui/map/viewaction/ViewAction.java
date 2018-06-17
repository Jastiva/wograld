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

package net.sf.gridarta.gui.map.viewaction;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Action to manage one edit type represented by one {@link
 * NamedGameObjectMatcher}.
 * @author Andreas Kirschbaum
 */
public class ViewAction extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The name for sorting the menu entries.
     */
    private final String name;

    /**
     * The managed edit type.
     */
    private final int editType;

    /**
     * The menu item for this action.
     */
    @NotNull
    private final AbstractButton checkBoxMenuItem = new JCheckBoxMenuItem(this);

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings instance
     * @param matcher the matcher
     */
    public ViewAction(@NotNull final MapViewSettings mapViewSettings, @NotNull final NamedGameObjectMatcher matcher) {
        super(ACTION_BUILDER.format("show.text", matcher.getName()));
        this.mapViewSettings = mapViewSettings;
        name = matcher.getName();
        editType = matcher.getEditType();
        updateAction();
    }

    /**
     * Returns the name for sorting the menu entries.
     * @return the name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Update the action's state to match the current edit type settings.
     */
    public final void updateAction() {
        checkBoxMenuItem.setSelected(mapViewSettings.isEditType(editType));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (!isEnabled()) {
            return;
        }

        mapViewSettings.toggleEditType(editType);
    }

    /**
     * Returns the menu item for this action.
     * @return the menu item
     */
    @NotNull
    public Component getCheckBoxMenuItem() {
        return checkBoxMenuItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
