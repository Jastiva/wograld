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

package com.realtime.wograld.jxclient.skills;

import com.realtime.wograld.jxclient.guistate.GuiStateListener;
import com.realtime.wograld.jxclient.guistate.GuiStateManager;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.wograld.WograldSkillInfoListener;
import com.realtime.wograld.jxclient.server.wograld.WograldStatsListener;
import com.realtime.wograld.jxclient.server.socket.ClientSocketState;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintain the set of skills as sent by the server.
 * @author Andreas Kirschbaum
 */
public class SkillSet {

    /**
     * Maps stat number to skill instance. Entries may be <code>null</code> if
     * the server did not provide a mapping.
     */
    @NotNull
    private final Skill[] numberedSkills = new Skill[WograldStatsListener.CS_NUM_SKILLS];

    /**
     * Maps skill name to skill instance.
     */
    @NotNull
    private final Map<String, Skill> namedSkills = new HashMap<String, Skill>();

    /**
     * The {@link WograldSkillInfoListener} attached to the server connection
     * for detecting changed skill info.
     */
    @NotNull
    private final WograldSkillInfoListener wograldSkillInfoListener = new WograldSkillInfoListener() {

        @Override
        public void clearSkills() {
            clearNumberedSkills();
            Arrays.fill(numberedSkills, null);
        }

        @Override
        public void addSkill(final int skillId, @NotNull final String skillName) {
            SkillSet.this.addSkill(skillId, skillName);
        }

    };

    /**
     * The {@link GuiStateListener} for detecting established or dropped
     * connections.
     */
    @NotNull
    private final GuiStateListener guiStateListener = new GuiStateListener() {

        @Override
        public void start() {
            // ignore
        }

        @Override
        public void metaserver() {
            // ignore
        }

        @Override
        public void preConnecting(@NotNull final String serverInfo) {
            // ignore
        }

        @Override
        public void connecting(@NotNull final String serverInfo) {
            clearNumberedSkills();
        }

        @Override
        public void connecting(@NotNull final ClientSocketState clientSocketState) {
            // ignore
        }

        @Override
        public void connected() {
            // ignore
        }

        @Override
        public void connectFailed(@NotNull final String reason) {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param wograldServerConnection the server connection to monitor
     * @param guiStateManager the gui state manager to watch
     */
    public SkillSet(@NotNull final WograldServerConnection wograldServerConnection, @NotNull final GuiStateManager guiStateManager) {
        wograldServerConnection.addWograldSkillInfoListener(wograldSkillInfoListener);
        guiStateManager.addGuiStateListener(guiStateListener);
    }

    /**
     * Adds a new skill to the list of known skills.
     * @param id the numerical identifier for the new skill
     * @param skillName the skill name
     */
    private void addSkill(final int id, @NotNull final String skillName) {
        final int index = id-WograldStatsListener.CS_STAT_SKILLINFO;
        final Skill oldSkill = numberedSkills[index];
        final Skill newSkill = getNamedSkill(skillName);
        if (oldSkill == newSkill) {
            return;
        }

        if (oldSkill != null) {
            oldSkill.set(0, 0);
        }
        numberedSkills[index] = newSkill;
    }

    /**
     * Returns the skill instance for a given skill name.
     * @param skillName the skill name to look up
     * @return the skill instance
     */
    public Skill getNamedSkill(final String skillName) {
        final Skill oldSkill = namedSkills.get(skillName);
        if (oldSkill != null) {
            return oldSkill;
        }

        final Skill newSkill = new Skill(skillName);
        namedSkills.put(skillName, newSkill);
        return newSkill;
    }

    /**
     * Clears all stat info in {@link #numberedSkills}.
     */
    public void clearNumberedSkills() {
        for (final Skill skill : numberedSkills) {
            if (skill != null) {
                skill.set(0, 0);
            }
        }
    }

    /**
     * Returns the given skill as a Skill object.
     * @param id the numerical skill identifier
     * @return the skill object matching the given identifier; may be
     *         <code>null</code> for undefined skills
     */
    @Nullable
    public Skill getSkill(final int id) {
        return numberedSkills[id-WograldStatsListener.CS_STAT_SKILLINFO];
    }

}
