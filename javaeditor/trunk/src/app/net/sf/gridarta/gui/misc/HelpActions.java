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

import java.awt.Frame;
import net.sf.gridarta.gui.dialog.help.Help;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.tod.TipOfTheDayManager;
import org.jetbrains.annotations.NotNull;

/**
 * Actions that are related to displaying help information.
 * @author Andreas Kirschbaum
 */
public class HelpActions {

    /**
     * The main view {@link Frame}.
     */
    @NotNull
    private final Frame mainViewFrame;

    /**
     * Creates a new instance.
     * @param mainViewFrame the main view frame
     */
    public HelpActions(@NotNull final Frame mainViewFrame) {
        this.mainViewFrame = mainViewFrame;
        final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");
        ActionUtils.newActions(actionBuilder, "Help", this, "showHelp", "tipOfTheDay");
    }

    /**
     * Action for creating a new project.
     */
    @ActionMethod
    public void showHelp() {
        new Help(mainViewFrame, "start.html").setVisible(true);
    }

    /**
     * Action for creating a new project.
     */
    @ActionMethod
    public void tipOfTheDay() {
        TipOfTheDayManager.show(mainViewFrame);
    }

}
