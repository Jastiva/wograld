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

package net.sf.gridarta.gui.misc;

import java.awt.Component;
import java.lang.ref.WeakReference;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.about.AboutDialog;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The about dialog.
 * @author Andreas Kirschbaum
 */
public class About {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The AboutDialog.
     */
    @Nullable
    private WeakReference<AboutDialog> aboutDialogRef;

    /**
     * The parent component for the about dialog.
     */
    @NotNull
    private final Component parent;

    /**
     * The synchronization object.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * Creates a new instance.
     * @param parent the parent component for the about dialog
     */
    public About(@NotNull final Component parent) {
        this.parent = parent;
        ActionUtils.newAction(ACTION_BUILDER, "Help", this, "about");
    }

    /**
     * Action method for about.
     */
    @ActionMethod
    public void about() {
        AboutDialog aboutDialog;
        synchronized (sync) {
            if (aboutDialogRef == null) {
                aboutDialog = new AboutDialog(ACTION_BUILDER);
                aboutDialogRef = new WeakReference<AboutDialog>(aboutDialog);
            } else {
                aboutDialog = aboutDialogRef.get();
                if (aboutDialog == null) {
                    aboutDialog = new AboutDialog(ACTION_BUILDER);
                    aboutDialogRef = new WeakReference<AboutDialog>(aboutDialog);
                }
            }
        }
        aboutDialog.showAboutDialog(parent);
    }

}
