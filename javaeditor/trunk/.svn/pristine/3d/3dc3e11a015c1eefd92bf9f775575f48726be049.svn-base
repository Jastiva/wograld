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

package net.sf.gridarta.commands;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.gui.map.renderer.ImageCreator;
import org.jetbrains.annotations.NotNull;

/**
 * Run in single png mode.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class SinglePngCommand implements Command {

    /**
     * The input file.
     */
    @NotNull
    private final File in;

    /**
     * The output file.
     */
    @NotNull
    private final File out;

    /**
     * The {@link ImageCreator} to use.
     */
    @NotNull
    private final ImageCreator<?, ?, ?> imageCreator;

    /**
     * Creates a new instance.
     * @param in the input file
     * @param out the output file
     * @param imageCreator the image creator to use
     */
    public SinglePngCommand(@NotNull final File in, @NotNull final File out, @NotNull final ImageCreator<?, ?, ?> imageCreator) {
        this.in = in;
        this.out = out;
        this.imageCreator = imageCreator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int execute() {
        try {
            imageCreator.makeImage(in, out);
        } catch (final IOException ex) {
            System.err.println(out + ": " + ex.getMessage());
        }
        return 0;
    }

}
