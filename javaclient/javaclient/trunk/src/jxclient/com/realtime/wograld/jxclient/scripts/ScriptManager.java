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

package com.realtime.wograld.jxclient.scripts;

import com.realtime.wograld.jxclient.items.FloorView;
import com.realtime.wograld.jxclient.items.ItemSet;
import com.realtime.wograld.jxclient.mapupdater.MapUpdaterState;
import com.realtime.wograld.jxclient.queue.CommandQueue;
import com.realtime.wograld.jxclient.server.wograld.WograldDrawinfoListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.skills.SkillSet;
import com.realtime.wograld.jxclient.spells.Spell;
import com.realtime.wograld.jxclient.stats.Stats;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jetbrains.annotations.NotNull;

/**
 * Maintains currently running script processes.
 * @author Andreas Kirschbaum
 */
public class ScriptManager {

    /**
     * The {@link CommandQueue} for sending commands.
     */
    @NotNull
    private final CommandQueue commandQueue;

    /**
     * The {@link WograldServerConnection} instance.
     */
    @NotNull
    private final WograldServerConnection wograldServerConnection;

    /**
     * The {@link Stats} instance to watch.
     */
    @NotNull
    private final Stats stats;

    /**
     * The {@link FloorView} to use.
     */
    @NotNull
    private final FloorView floorView;

    /**
     * The {@link ItemSet} instance to use.
     */
    @NotNull
    private final ItemSet itemSet;

    /**
     * The spells manager instance to use.
     */
    @NotNull
    private final Iterable<Spell> spellsManager;

    /**
     * The {@link MapUpdaterState} instance to use.
     */
    @NotNull
    private final MapUpdaterState mapUpdaterState;

    /**
     * The {@link SkillSet} for looking up skill names.
     */
    @NotNull
    private final SkillSet skillSet;

    /**
     * All running {@link ScriptProcess}es.
     */
    @NotNull
    private final Collection<ScriptProcess> scriptProcesses = new CopyOnWriteArraySet<ScriptProcess>();

    /**
     * The script ID for the next created script.
     */
    private int nextScriptId = 1;

    /**
     * Creates a new instance.
     * @param commandQueue the command queue for sending commands
     * @param wograldServerConnection the connection instance
     * @param stats the stats instance to watch
     * @param floorView the floor view to use
     * @param itemSet the item set instance to use
     * @param spellsManager the spells manager instance to use
     * @param mapUpdaterState the map updater state instance to use
     * @param skillSet the skill set for looking up skill names
     */
    public ScriptManager(@NotNull final CommandQueue commandQueue, @NotNull final WograldServerConnection wograldServerConnection, @NotNull final Stats stats, @NotNull final FloorView floorView, @NotNull final ItemSet itemSet, @NotNull final Iterable<Spell> spellsManager, @NotNull final MapUpdaterState mapUpdaterState, @NotNull final SkillSet skillSet) {
        this.commandQueue = commandQueue;
        this.wograldServerConnection = wograldServerConnection;
        this.stats = stats;
        this.floorView = floorView;
        this.itemSet = itemSet;
        this.spellsManager = spellsManager;
        this.mapUpdaterState = mapUpdaterState;
        this.skillSet = skillSet;
    }

    /**
     * Creates a new script instance.
     * @param command the script command including arguments
     */
    public void newScript(@NotNull final String command) {
        final DefaultScriptProcess scriptProcess;
        try {
            scriptProcess = new DefaultScriptProcess(nextScriptId, command, commandQueue, wograldServerConnection, stats, floorView, itemSet, spellsManager, mapUpdaterState, skillSet);
        } catch (final IOException ex) {
            wograldServerConnection.drawInfo("Unable to run script: "+ex.getMessage(), WograldDrawinfoListener.NDI_RED);
            return;
        }
        nextScriptId++;
        scriptProcesses.add(scriptProcess);
        scriptProcess.addScriptProcessListener(new ScriptProcessListener() {

            @Override
            public void scriptTerminated(final String result) {
                scriptProcesses.remove(scriptProcess);
                if (result == null) {
                    wograldServerConnection.drawInfo("Script '"+scriptProcess+"' finished.", WograldDrawinfoListener.NDI_BLACK);
                } else {
                    wograldServerConnection.drawInfo("Script '"+scriptProcess+"' failed: "+result, WograldDrawinfoListener.NDI_RED);
                }
            }

        });
        wograldServerConnection.drawInfo("Script '"+scriptProcess+"' started.", WograldDrawinfoListener.NDI_BLACK);
        new Thread(scriptProcess, "JXClient:ScriptProcess:"+scriptProcess).start();
    }

    /**
     * Returns all running scripts matching a given (partial) name or a script
     * ID.
     * @param partialScriptName the partial name or a script ID to match
     * against; an empty string matches all scripts
     * @return the matching scripts, possibly empty
     */
    @NotNull
    public Set<ScriptProcess> getScripts(@NotNull final String partialScriptName) {
        try {
            return getScriptByScriptId(Integer.parseInt(partialScriptName));
        } catch (final NumberFormatException ignored) {
            return getScriptsByName(partialScriptName);
        }
    }

    /**
     * Returns all running scripts matching a given script ID.
     * @param scriptId the script ID
     * @return the matching scripts, possibly empty
     */
    @NotNull
    private Set<ScriptProcess> getScriptByScriptId(final int scriptId) {
        final Set<ScriptProcess> result = new HashSet<ScriptProcess>();
        for (final ScriptProcess scriptProcess : scriptProcesses) {
            if (scriptProcess.getScriptId() == scriptId) {
                result.add(scriptProcess);
                break;
            }
        }
        return result;
    }

    /**
     * Returns all running scripts matching a given (partial) name.
     * @param partialScriptName the partial script name; an empty string matches
     * all scripts
     * @return the matching scripts, possibly empty
     */
    @NotNull
    private Set<ScriptProcess> getScriptsByName(@NotNull final CharSequence partialScriptName) {
        final Set<ScriptProcess> result = new HashSet<ScriptProcess>();
        for (final ScriptProcess scriptProcess : scriptProcesses) {
            if (scriptProcess.getFilename().contains(partialScriptName)) {
                result.add(scriptProcess);
            }
        }
        return result;
    }

    /**
     * Returns whether at least one script is running.
     * @return whether at least one script is running
     */
    public boolean hasScripts() {
        return !scriptProcesses.isEmpty();
    }

}
