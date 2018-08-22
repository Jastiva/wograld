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

package com.realtime.wograld.jxclient.guistate;

/**
 * Possible gui states.
 * @author Andreas Kirschbaum
 */
public enum GuiState {

    /**
     * Display the start screen.
     */
    START,

    /**
     * Display the server selection screen.
     */
    METASERVER,

    /**
     * Display the screen while a server connection is established.
     */
    CONNECTING,

    /**
     * Display the main playing screen.
     */
    CONNECTED,

    /**
     * Display a failed connection attempt.
     */
    CONNECT_FAILED

}