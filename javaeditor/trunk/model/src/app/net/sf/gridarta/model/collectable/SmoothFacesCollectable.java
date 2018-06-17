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

package net.sf.gridarta.model.collectable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import net.sf.gridarta.model.smoothface.SmoothFace;
import net.sf.gridarta.model.smoothface.SmoothFaces;
import net.sf.gridarta.utils.IOUtils;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Collectable} that creates the Crossfire specific smooth faces file.
 * This file contains information needed for smoothing faces between adjacent
 * map squares.
 * @author Andreas Kirschbaum
 */
public class SmoothFacesCollectable implements Collectable {

    /**
     * The {@link SmoothFaces} being collected.
     */
    @NotNull
    private final SmoothFaces smoothFaces;

    /**
     * The smooth file to write to.
     */
    @NotNull
    private final String smoothFile;

    /**
     * Creates a new instance.
     * @param smoothFaces the smooth faces to collect
     * @param smoothFile the smooth file to write to
     */
    public SmoothFacesCollectable(@NotNull final SmoothFaces smoothFaces, @NotNull final String smoothFile) {
        this.smoothFaces = smoothFaces;
        this.smoothFile = smoothFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collect(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
    //    final FileOutputStream fos = new FileOutputStream(new File(collectedDirectory, smoothFile));
        final FileOutputStream fos = new FileOutputStream(smoothFile);
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos, IOUtils.MAP_ENCODING);
            try {
                final BufferedWriter out = new BufferedWriter(osw);
                try {
                    out.append("default_smoothed.111 sdefault.001\n");
                    for (final Map.Entry<String, SmoothFace> e : smoothFaces) {
                        final SmoothFace smoothFace = e.getValue();
                        out.append(smoothFace.getFace()).append(' ').append(smoothFace.getValue()).append('\n');
                    }
                } finally {
                    out.close();
                }
            } finally {
                osw.close();
            }
        } finally {
            fos.close();
        }
    }

}
