/*
 * This file is part of JXClient, the Fullscreen Java Crossfire Client.
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

package com.realtime.crossfire.jxclient.faces;

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners interested in {@link FaceQueue} events.
 * @author Andreas Kirschbaum
 */
public interface FaceQueueListener extends EventListener {

    /**
     * Called after a {@link Face} has been loaded successfully.
     * @param face the face that has been loaded
     * @param faceImages the face images instance that has been loaded
     */
    void faceLoaded(@NotNull Face face, @NotNull FaceImages faceImages);

    /**
     * Called after a {@link Face} could not be loaded.
     * @param face the face that has failed to load
     */
    void faceFailed(@NotNull Face face);

}
