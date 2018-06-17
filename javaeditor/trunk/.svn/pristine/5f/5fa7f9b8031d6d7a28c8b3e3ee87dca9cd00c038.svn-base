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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for classes managing a single dialog instance. The dialog
 * instance can be shown by calling the associated action. The actual creation
 * of the dialog is usually deferred until the dialog is shown for the first
 * time.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractDialogManager {

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Creates a new instance.
     * @param actionName the name of the action to show the dialog
     */
    protected AbstractDialogManager(@NotNull final String actionName) {
        ACTION_BUILDER.initAction(true, new AbstractAction() {

                /**
                 * The serial version UID.
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(@NotNull final ActionEvent e) {
                    showDialog();
                }

                @NotNull
                @Override
                public Object clone() {
                    try {
                        return super.clone();
                    } catch (@NotNull CloneNotSupportedException ex) {
                        throw new AssertionError(ex);
                    }
                }

            }, actionName);
    }

    /**
     * Displays the dialog.
     */
    protected abstract void showDialog();

}
