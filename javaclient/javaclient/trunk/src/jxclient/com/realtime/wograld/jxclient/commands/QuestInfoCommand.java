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

import com.realtime.wograld.jxclient.queue.CommandQueue;
import com.realtime.wograld.jxclient.util.Patterns;
import com.realtime.wograld.jxclient.gui.commands.CommandCallback;
import com.realtime.wograld.jxclient.gui.commands.CommandList;
import com.realtime.wograld.jxclient.gui.commands.CommandListType;
import com.realtime.wograld.jxclient.gui.commands.GUICommandFactory;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.util.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the "bind" command. It associates a key with a command.
 * @author Andreas Kirschbaum
 */
public class QuestInfoCommand extends AbstractCommand {

    /**
     * The {@link CommandCallback} to use.
     */
    @NotNull
    private final CommandCallback commandCallback;

    /**
     * The {@link GUICommandFactory} for creating commands.
     */
    @NotNull
    private final GUICommandFactory guiCommandFactory;

    @NotNull
    private final CommandQueue commandQueue;

    /**
     * Creates a new instance.
     * @param wograldServerConnection the connection instance
     * @param commandCallback the command callback to use
     * @param guiCommandFactory the gui command factory for creating commands
     */
    public QuestInfoCommand(@NotNull final WograldServerConnection wograldServerConnection, @NotNull final CommandCallback commandCallback, @NotNull final GUICommandFactory guiCommandFactory, @NotNull final CommandQueue commandQueue) {
        super("aboutquest", wograldServerConnection);
        this.commandCallback = commandCallback;
        this.guiCommandFactory = guiCommandFactory;
        this.commandQueue = commandQueue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean allArguments() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull final String args) {
       
//        final String qname = args[0];
        final String[] tmp = Patterns.PATTERN_WHITESPACE.split(args, 2);
        
    	this.commandQueue.sendNcom(false, "questinfo "+tmp[0]);    
    }

}
