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

import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.japi.swing.misc.ConsoleProgress;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Run archetype collection.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @todo allow arch collection to be configured (input directories, output
 * directory)
 */
public class CollectArchesCommand implements Command {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(CollectArchesCommand.class);

    /**
     * The resources to collect.
     */
    @NotNull
    private final AbstractResources<?, ?, ?> resources;

    /**
     * The {@link GlobalSettings} instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * Creates a new instance.
     * @param resources the resources to collect
     * @param globalSettings the global settings instance
     */
    public CollectArchesCommand(@NotNull final AbstractResources<?, ?, ?> resources, @NotNull final GlobalSettings globalSettings) {
        this.resources = resources;
        this.globalSettings = globalSettings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int execute() {
        if (!resources.canWriteCollected()) {
            log.fatal("Cannot collect resources");
            return 1;
        }
        final Collector collector = new Collector(new ConsoleProgress(), resources, globalSettings.getCollectedDirectory());
        collector.start();
        try {
            collector.waitUntilFinished();
        } catch (final InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return 1;
        }
        return 0;
    }

}
