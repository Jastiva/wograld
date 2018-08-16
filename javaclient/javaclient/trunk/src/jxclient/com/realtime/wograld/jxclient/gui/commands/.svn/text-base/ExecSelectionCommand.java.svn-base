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

import com.realtime.wograld.jxclient.gui.list.GUIItemList;
import com.realtime.wograld.jxclient.items.FloorView;
import com.realtime.wograld.jxclient.queue.CommandQueue;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GUICommand} that executes a command on the selected item of a {@link
 * GUIItemList}.
 * @author Andreas Kirschbaum
 */
public class ExecSelectionCommand implements GUICommand {

    /**
     * The list to execute in.
     */
    @NotNull
    private final GUIItemList list;

    /**
     * The command to execute.
     */
    @NotNull
    private final CommandType command;

    /**
     * The connection to execute commands on.
     */
    @NotNull
    private final WograldServerConnection wograldServerConnection;

    /**
     * The {@link FloorView} to use.
     */
    @NotNull
    private final FloorView floorView;

    /**
     * The command queue to use.
     */
    @NotNull
    private final CommandQueue commandQueue;

    /**
     * Creates a new instance.
     * @param list the list to execute in
     * @param command the command to execute
     * @param wograldServerConnection the connection to execute commands on
     * @param floorView the floor view to use
     * @param commandQueue the command queue to use
     */
    public ExecSelectionCommand(@NotNull final GUIItemList list, @NotNull final CommandType command, @NotNull final WograldServerConnection wograldServerConnection, @NotNull final FloorView floorView, @NotNull final CommandQueue commandQueue) {
        this.list = list;
        this.command = command;
        this.wograldServerConnection = wograldServerConnection;
        this.floorView = floorView;
        this.commandQueue = commandQueue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canExecute() {
        return CommandType.canExecute(list.getSelectedItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        command.execute(list.getSelectedItem(), wograldServerConnection, floorView.getCurrentFloor(), commandQueue);
    }

}
