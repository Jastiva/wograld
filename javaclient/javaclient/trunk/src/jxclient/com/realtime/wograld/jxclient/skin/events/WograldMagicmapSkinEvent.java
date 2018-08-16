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

package com.realtime.wograld.jxclient.skin.events;

import com.realtime.wograld.jxclient.gui.commands.CommandList;
import com.realtime.wograld.jxclient.server.wograld.WograldMagicmapListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link SkinEvent} that executes a {@link CommandList} whenever a magicmap
 * protocol command is received.
 * @author Andreas Kirschbaum
 */
public class WograldMagicmapSkinEvent implements SkinEvent {

    /**
     * The {@link CommandList} to execute.
     */
    @NotNull
    private final CommandList commandList;

    /**
     * The {@link WograldServerConnection} for tracking magicmap commands.
     */
    @NotNull
    private final WograldServerConnection server;

    /**
     * The {@link WograldMagicmapListener} attached to {@link #server}.
     */
    @NotNull
    private final WograldMagicmapListener wograldMagicmapListener = new WograldMagicmapListener() {

        @Override
        public void commandMagicmapReceived() {
            commandList.execute();
        }

    };

    /**
     * Creates a new instance.
     * @param commandList the command list to execute
     * @param server the connection to attach to
     */
    public WograldMagicmapSkinEvent(@NotNull final CommandList commandList, @NotNull final WograldServerConnection server) {
        this.commandList = commandList;
        this.server = server;
        server.addWograldMagicmapListener(wograldMagicmapListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        server.removeWograldMagicmapListener(wograldMagicmapListener);
    }

}
