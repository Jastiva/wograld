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

package net.sf.gridarta.model.collectable;

import java.io.File;
import java.io.IOException;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;

/**
 * A Collectable has information that can be collected. This is used during the
 * "collection" process to gather information into files suitable for the
 * game servers.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface Collectable {

    /**
     * Collects information.
     * @param progress the progress to report progress to
     * @param collectedDirectory the destination directory to collect data to
     * @throws IOException in case of I/O problems during collection
     */
    void collect(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException;

}
