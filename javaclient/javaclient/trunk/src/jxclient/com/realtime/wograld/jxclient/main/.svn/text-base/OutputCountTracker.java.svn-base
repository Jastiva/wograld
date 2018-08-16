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

package com.realtime.wograld.jxclient.main;

import com.realtime.wograld.jxclient.guistate.GuiStateListener;
import com.realtime.wograld.jxclient.guistate.GuiStateManager;
import com.realtime.wograld.jxclient.queue.CommandQueue;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.wograld.WograldUpdateItemListener;
import com.realtime.wograld.jxclient.server.socket.ClientSocketState;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks received Wograld messages and resets the "output-count" setting
 * whenever a player logs in.
 * @author Andreas Kirschbaum
 */
public class OutputCountTracker {

    /**
     * The {@link WograldServerConnection} to track.
     */
    @NotNull
    private final WograldServerConnection server;

    /**
     * The {@link CommandQueue} for sending commands.
     */
    @NotNull
    private final CommandQueue commandQueue;

    /**
     * The {@link WograldUpdateItemListener} to receive item updates.
     */
    @NotNull
    private final WograldUpdateItemListener wograldUpdateItemListener = new WograldUpdateItemListener() {

        @Override
        public void delinvReceived(final int tag) {
            // ignore
        }

        @Override
        public void delitemReceived(@NotNull final int[] tags) {
            // ignore
        }

        @Override
        public void addItemReceived(final int location, final int tag, final int flags, final int weight, final int faceNum, @NotNull final String name, @NotNull final String namePl, final int anim, final int animSpeed, final int nrof, final int type) {
            // ignore
        }

        @Override
        public void playerReceived(final int tag, final int weight, final int faceNum, @NotNull final String name) {
            commandQueue.sendNcom(true, 1, "output-count 1"); // to make message merging work reliably
        }

        @Override
        public void upditemReceived(final int flags, final int tag, final int valLocation, final int valFlags, final int valWeight, final int valFaceNum, @NotNull final String valName, @NotNull final String valNamePl, final int valAnim, final int valAnimSpeed, final int valNrof) {
            // ignore
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
            server.removeWograldUpdateItemListener(wograldUpdateItemListener);
        }

        @Override
        public void metaserver() {
            server.removeWograldUpdateItemListener(wograldUpdateItemListener);
        }

        @Override
        public void preConnecting(@NotNull final String serverInfo) {
            // ignore
        }

        @Override
        public void connecting(@NotNull final String serverInfo) {
            server.addWograldUpdateItemListener(wograldUpdateItemListener);
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
     * @param guiStateManager the gui state manager to track
     * @param server the wograld server connection to track
     * @param commandQueue the command queue for sending commands
     */
    public OutputCountTracker(@NotNull final GuiStateManager guiStateManager, @NotNull final WograldServerConnection server, @NotNull final CommandQueue commandQueue) {
        this.server = server;
        this.commandQueue = commandQueue;
        guiStateManager.addGuiStateListener(guiStateListener);
    }

}
