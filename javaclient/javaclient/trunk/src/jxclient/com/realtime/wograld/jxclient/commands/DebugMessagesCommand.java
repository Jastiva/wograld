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

import com.realtime.wograld.jxclient.gui.log.MessageBufferUpdater;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.wograld.MessageTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the "debug_messages" command. It prints text to the log window
 * using different message types or enables/disables printing message types.
 * @author Andreas Kirschbaum
 */
public class DebugMessagesCommand extends AbstractCommand {

    /**
     * The connection instance.
     */
    @NotNull
    private final WograldServerConnection wograldServerConnection;

    /**
     * Creates a new instance.
     * @param wograldServerConnection the connection instance
     */
    public DebugMessagesCommand(@NotNull final WograldServerConnection wograldServerConnection) {
        super("debug_messages", wograldServerConnection);
        this.wograldServerConnection = wograldServerConnection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean allArguments() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull final String args) {
        if (args.equals("colors")) {
            for (int color = 0; color < MessageBufferUpdater.NUM_COLORS; color++) {
                drawInfo("This line is color #"+color+" ("+MessageBufferUpdater.getColorName(color)+").", color);
            }
        } else if (args.equals("types")) {
            for (final int type : MessageTypes.getAllTypes()) {
                wograldServerConnection.drawextinfo(0, type, 0, "This line is type #"+type+".");
            }
        } else if (args.equals("on")) {
            wograldServerConnection.drawInfoSetDebugMode(true);
        } else if (args.equals("off")) {
            wograldServerConnection.drawInfoSetDebugMode(false);
        } else {
            drawInfoError("Valid arguments are 'colors', 'types', 'on', or 'off'. 'colors' prints messages using different message types, 'on' and 'off' enable/disable printing of message types.");
            return;
        }

    }

}