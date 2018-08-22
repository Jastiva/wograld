/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.gui.commands;

import com.realtime.wograld.jxclient.gui.gui.GUIElement;
import java.awt.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GUICommand} which shows a target {@link GUIElement}.
 * @author Andreas Kirschbaum
 */
public class ShowCommand implements GUICommand {

    /**
     * The gui element to show.
     */
    @NotNull
    private final Component target;

    /**
     * Creates a new instance.
     * @param target the gui element to show
     */
    public ShowCommand(@NotNull final Component target) {
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canExecute() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        target.setVisible(true);
    }

}