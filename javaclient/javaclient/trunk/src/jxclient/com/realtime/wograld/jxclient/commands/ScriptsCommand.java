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

import com.realtime.wograld.jxclient.scripts.ScriptManager;
import com.realtime.wograld.jxclient.scripts.ScriptProcess;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import java.util.Collection;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the "scripts" command. It lists running scripts.
 * @author Andreas Kirschbaum
 */
public class ScriptsCommand extends AbstractCommand {

    /**
     * The {@link ScriptManager} to use.
     */
    @NotNull
    private final ScriptManager scriptManager;

    /**
     * Creates a new instance.
     * @param scriptManager the script manager to use
     * @param wograldServerConnection the connection instance
     */
    public ScriptsCommand(@NotNull final ScriptManager scriptManager, @NotNull final WograldServerConnection wograldServerConnection) {
        super("scripts", wograldServerConnection);
        this.scriptManager = scriptManager;
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
        final Collection<ScriptProcess> scriptProcesses = new TreeSet<ScriptProcess>(scriptManager.getScripts(args));
        if (scriptProcesses.isEmpty()) {
            drawInfo(scriptManager.hasScripts() ? "No matching scripts." : "No scripts running.");
            return;
        }

        drawInfo("Running scripts:");
        for (final Object scriptProcess : scriptProcesses) {
            drawInfo("- "+scriptProcess);
        }
    }

}
