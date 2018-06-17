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

package net.sf.gridarta.model.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common interface for ArchetypeParsers.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface ArchetypeParser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Name of the system-archetype containing path of starting map.
     */
    String START_ARCH_NAME = "map";

    /**
     * Here we read an Archetype from a {@link BufferedReader}, parse the data
     * and put the result in the ArchetypeSet in CMainControl.
     * @param in <code>BufferedReader</code> file stream of archetype data
     * @param prototype Prototype Archetype (only for artifacts)
     * @param line first line, pre-parsed (only for artifacts)
     * @param archName archetype-object name (only for artifacts)
     * @param panelName the panel name to add the archetype to
     * @param folderName the folder name to add the archetype to
     * @param archPath the archetype path
     * @param invObjects collects all inventory objects
     * @param errorViewCollector the error view collector to use
     * @throws IOException in case of I/O problems
     * @throws java.io.EOFException in case an incomplete object was found
     */
    void parseArchetypeFromStream(@NotNull BufferedReader in, @Nullable R prototype, @Nullable String line, @Nullable String archName, @NotNull String panelName, @NotNull String folderName, @NotNull String archPath, @NotNull List<G> invObjects, @NotNull ErrorViewCollector errorViewCollector) throws IOException;

}
