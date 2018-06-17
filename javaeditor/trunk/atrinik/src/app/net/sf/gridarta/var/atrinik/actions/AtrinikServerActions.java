/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.var.atrinik.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import net.sf.gridarta.actions.AbstractServerActions;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link AbstractServerActions} implementation for connecting to an Atrinik
 * server.
 * @author Andreas Kirschbaum
 */
public class AtrinikServerActions extends AbstractServerActions<GameObject, MapArchObject, Archetype> {

    /**
     * Command type.
     */
    private static final int SERVER_CMD_CONTROL = 0;

    /**
     * The name of the application requesting control.
     */
    @NotNull
    private static final String APPLICATION_NAME_IDENTIFIER = "gridarta";

    /**
     * The charset name for encoding strings in the protocol.
     */
    @NotNull
    private static final String CHARSET_NAME = "US-ASCII";

    /**
     * Command sub-type: teleport character to map.
     */
    private static final int CMD_CONTROL_UPDATE_MAP = 1;

    /**
     * Creates a new instance.
     * @param mapViewManager the map view manager for tracking the current map
     * view
     * @param fileControl the file control for saving maps
     * @param pathManager the path manager for converting path names
     */
    public AtrinikServerActions(@NotNull final MapViewManager<GameObject, MapArchObject, Archetype> mapViewManager, @NotNull final FileControl<GameObject, MapArchObject, Archetype> fileControl, @NotNull final PathManager pathManager) {
        super(mapViewManager, fileControl, pathManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void teleportCharacterToMap(@NotNull final String mapPath, final int mapX, final int mapY) throws IOException {
        final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        tmp.write(0);
        tmp.write(0);
        tmp.write(SERVER_CMD_CONTROL);
        tmp.write(APPLICATION_NAME_IDENTIFIER.getBytes(CHARSET_NAME));
        tmp.write(0); // termination of application name identifier
        tmp.write(CMD_CONTROL_UPDATE_MAP);
        tmp.write(mapPath.getBytes(CHARSET_NAME));
        tmp.write(0);
        tmp.write(mapX >> 8);
        tmp.write(mapX);
        tmp.write(mapY >> 8);
        tmp.write(mapY);
        final byte[] packet = tmp.toByteArray();
        packet[0] = (byte) ((packet.length - 2) >> 8);
        packet[1] = (byte) (packet.length - 2);
        try {
            final Socket socket = new Socket("127.0.0.1", 13327);
            try {
                final OutputStream outputStream = socket.getOutputStream();
                outputStream.write(packet);
                socket.shutdownOutput();
            } finally {
                socket.close();
            }
        } catch (final IOException ex) {
            throw new IOException("127.0.0.1:13327: " + ex.getMessage(), ex);
        }
    }

}
