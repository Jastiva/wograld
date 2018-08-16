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

package com.realtime.wograld.jxclient.sound;

import com.realtime.wograld.jxclient.server.wograld.WograldMusicListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import org.jetbrains.annotations.NotNull;

/**
 * Monitors music commands and generates appropriate sound effects.
 * @author Andreas Kirschbaum
 */
public class MusicWatcher {

    /**
     * The {@link SoundManager} instance to watch.
     */
    @NotNull
    private final SoundManager soundManager;

    /**
     * The wograld stats listener.
     */
    @NotNull
    private final WograldMusicListener wograldMusicListener = new WograldMusicListener() {

        @Override
        public void commandMusicReceived(@NotNull final String music) {
            soundManager.playMusic(music.equals("NONE") ? null : music);
        }

    };

    /**
     * Creates a new instance.
     * @param wograldServerConnection the connection instance
     * @param soundManager the sound manager instance to watch
     */
    public MusicWatcher(@NotNull final WograldServerConnection wograldServerConnection, @NotNull final SoundManager soundManager) {
        wograldServerConnection.addWograldMusicListener(wograldMusicListener);
        this.soundManager = soundManager;
    }

}
