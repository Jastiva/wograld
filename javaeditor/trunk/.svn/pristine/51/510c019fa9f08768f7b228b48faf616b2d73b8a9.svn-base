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
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Collector is capable of iterating over a collection of {@link
 * net.sf.gridarta.model.collectable.Collectable Collectables} and collecting
 * them in a separate <code>Thread</code> with a nice GUI.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class Collector implements Runnable {

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The Collectables.
     */
    @NotNull
    private final AbstractResources<?, ?, ?> resources;

    /**
     * The destination directory to write files to.
     */
    @NotNull
    private final File collectedDirectory;

    /**
     * The Progress to use.
     */
    @NotNull
    private final Progress progress;

    /**
     * The worker thread.
     */
    @Nullable
    private Thread thread;

    /**
     * Create a Collector.
     * @param progress the progress to use
     * @param resources the resources to collect
     * @param collectedDirectory the destination directory to write files to
     */
    public Collector(@NotNull final Progress progress, @NotNull final AbstractResources<?, ?, ?> resources, @NotNull final File collectedDirectory) {
        if (!resources.canWriteCollected()) {
            throw new IllegalArgumentException();
        }
        this.progress = progress;
        this.resources = resources;
        this.collectedDirectory = collectedDirectory;
    }

    /**
     * Starts collecting.
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Waits until collection has finished.
     * @throws InterruptedException if waiting was interrupted
     */
    public void waitUntilFinished() throws InterruptedException {
        if (thread != null) {
            thread.join();
            thread = null;
        }
    }

    /*
     * Collect the existing arches and create archive-files for editor use as
     * well as the Crossfire or Daimonin server. The arches also get a special path variable
     * included which is used in the editor to categorize the arches.
     * <p/>
     * Output is: "archetypes", "daimonin.0" / "crossfire.0", "animations", "bmaps"
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            try {
                resources.writeCollected(progress, collectedDirectory);
            } catch (final IOException e) {
                ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorIOException", "arches, animations and animtree, images", e);
            }
        } finally {
            progress.finished();
        }
    }

}
