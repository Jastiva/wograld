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
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for Swing related functions.
 * @author Andreas Kirschbaum
 */
public class SwingUtils {

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta.gui.panel.tools");

    /**
     * Private constructor to prevent instantiation.
     */
    private SwingUtils() {
    }

    /**
     * Create a {@link javax.swing.JLabel} instance.
     * @param key the action key to use
     * @param component the component this label is attached to
     * @return the label
     */
    @NotNull
    public static Component createLabel(@NotNull final String key, @Nullable final Component component) {
        final JComponent label = component == null ? ACTION_BUILDER.createLabel(key + ".text") : ACTION_BUILDER.createLabel(component, key + ".text");
        label.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, key + ".shortdescription"));
        return label;
    }

    /**
     * Adds an accelerator key for a component. The accelerator key is extracted
     * from the action to be added. If the action has no attached accelerator
     * key, nothing is added.
     * @param textComponent the text component to add to
     * @param action the action to add
     */
    public static void addAction(@NotNull final JComponent textComponent, @NotNull final Action action) {
        final KeyStroke keyStroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        if (keyStroke != null) {
            final String actionId = ActionUtils.getActionId(action);
            textComponent.getInputMap().put(keyStroke, actionId);
            textComponent.getActionMap().put(actionId, action);
        }
    }

}
