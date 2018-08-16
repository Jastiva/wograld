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

package com.realtime.wograld.jxclient.commands;

import com.realtime.wograld.jxclient.server.wograld.WograldDrawinfoListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for {@link Command} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractCommand implements Command {

    /**
     * The name of the command.
     */
    @NotNull
    private final String commandName;

    /**
     * The connection instance.
     */
    @NotNull
    private final WograldServerConnection wograldServerConnection;

    /**
     * Creates a new instance.
     * @param commandName the name of the command
     * @param wograldServerConnection the connection instance
     */
    protected AbstractCommand(@NotNull final String commandName, @NotNull final WograldServerConnection wograldServerConnection) {
        this.commandName = commandName;
        this.wograldServerConnection = wograldServerConnection;
    }

    /**
     * Displays a regular output message.
     * @param message the message
     */
    protected void drawInfo(@NotNull final String message) {
        drawInfo(message, WograldDrawinfoListener.NDI_BLACK);
    }

    /**
     * Displays an error message.
     * @param message the error message
     */
    protected void drawInfoError(@NotNull final String message) {
        drawInfo(message, WograldDrawinfoListener.NDI_RED);
    }

    /**
     * Displays a message.
     * @param message the message
     * @param color the color code
     */
    protected void drawInfo(@NotNull final String message, final int color) {
        wograldServerConnection.drawInfo(message, color);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getCommandName() {
        return commandName;
    }

}
