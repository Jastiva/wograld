/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.gridarta.model.collectable.Collectable;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;

/**
 * Writes resources into a collection.
 * @author Andreas Kirschbaum
 */
public class CollectedResourcesWriter {

    /**
     * All {@link Collectable} resources.
     */
    @NotNull
    private final Collection<Collectable> collectables = new ArrayList<Collectable>();

    /**
     * Adds a {@link Collectable} resource.
     * @param collectable the collectable resource
     */
    public void addCollectable(@NotNull final Collectable collectable) {
        collectables.add(collectable);
    }

    /**
     * Writes the resources into a collection.
     * @param progress the progress to use
     * @param collectedDirectory the collected directory to write to
     * @throws IOException if collection fails
     */
    public void write(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
        for (final Collectable collectable : collectables) {
            collectable.collect(progress, collectedDirectory);
        }
    }

}
