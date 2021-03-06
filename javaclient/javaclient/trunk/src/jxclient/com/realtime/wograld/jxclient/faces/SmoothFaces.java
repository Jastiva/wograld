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
 * Copyright (C) 2006-2010 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.faces;

import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.wograld.WograldSmoothListener;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Maintains smoothing information received from the Wograld server.
 * @author Andreas Kirschbaum
 */
public class SmoothFaces {

    /**
     * All known smoothing mappings. Maps face to associated smoothing face.
     */
    @NotNull
    private final Map<Integer, Integer> smoothFaces = new HashMap<Integer, Integer>();

    /**
     * Creates a new instance.
     * @param wograldServerConnection the wograld server connection to
     * monitor
     */
    public SmoothFaces(@NotNull final WograldServerConnection wograldServerConnection) {
        final WograldSmoothListener wograldSmoothListener = new WograldSmoothListener() {

            @Override
            public void commandSmoothReceived(final int face, final int smoothFace) {
                synchronized (smoothFaces) {
                    smoothFaces.put(face, smoothFace);
                }
            }

        };
        wograldServerConnection.addWograldSmoothListener(wograldSmoothListener);
    }

    /**
     * Returns the smoothing face associated with a given face.
     * @param face the face
     * @return the smoothing face or <code>0</code>
     */
    public int getSmoothFace(final int face) {
        final Integer smooth;
        synchronized (smoothFaces) {
            smooth = smoothFaces.get(face);
        }
        return smooth == null ? 0 : smooth;
    }

}
