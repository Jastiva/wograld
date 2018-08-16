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

package com.realtime.wograld.jxclient.mapupdater;

import com.realtime.wograld.jxclient.faces.FacesManager;
import com.realtime.wograld.jxclient.guistate.GuiStateListener;
import com.realtime.wograld.jxclient.guistate.GuiStateManager;
import com.realtime.wograld.jxclient.map.CfMap;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.socket.ClientSocketState;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to update a {@link CfMap} model from protocol commands.
 * @author Andreas Kirschbaum
 */
public class CfMapUpdater {

    /**
     * The updated {@link MapUpdaterState} instance.
     */
    @NotNull
    private final MapUpdaterState mapUpdaterState;

    /**
     * The {@link GuiStateListener} for detecting established or dropped
     * connections.
     */
    @NotNull
    private final GuiStateListener guiStateListener = new GuiStateListener() {

        @Override
        public void start() {
            mapUpdaterState.reset();
        }

        @Override
        public void metaserver() {
            mapUpdaterState.reset();
        }

        @Override
        public void preConnecting(@NotNull final String serverInfo) {
            // ignore
        }

        @Override
        public void connecting(@NotNull final String serverInfo) {
            mapUpdaterState.reset();
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
     * @param mapUpdaterState the map updater state to update
     * @param wograldServerConnection the connection to monitor
     * @param facesManager the faces manager to track for updated faces
     * @param guiStateManager the gui state manager to watch
     */
    public CfMapUpdater(@NotNull final MapUpdaterState mapUpdaterState, @NotNull final WograldServerConnection wograldServerConnection, @NotNull final FacesManager facesManager, @NotNull final GuiStateManager guiStateManager) {
        this.mapUpdaterState = mapUpdaterState;
        facesManager.addFacesManagerListener(mapUpdaterState);
        wograldServerConnection.setWograldUpdateMapListener(mapUpdaterState);
        guiStateManager.addGuiStateListener(guiStateListener);
        wograldServerConnection.addWograldTickListener(mapUpdaterState);
    }

}
