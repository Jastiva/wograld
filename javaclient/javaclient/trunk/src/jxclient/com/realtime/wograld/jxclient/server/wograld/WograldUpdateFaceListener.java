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

package com.realtime.wograld.jxclient.server.wograld;

import java.nio.ByteBuffer;
import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * Listener to be notified of updated face information.
 * @author Andreas Kirschbaum
 */
public interface WograldUpdateFaceListener extends EventListener {

    /**
     * Notifies that face information has been received from the Wograld
     * server.
     * @param faceNum the face ID
     * @param faceSetNum the face set
     * @param packet the packet data; must not be changed
     */
    void updateFace(int faceNum, int faceSetNum, @NotNull ByteBuffer packet);

}
