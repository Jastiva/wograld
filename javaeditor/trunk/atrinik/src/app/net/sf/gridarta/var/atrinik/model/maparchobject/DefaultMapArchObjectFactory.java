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

package net.sf.gridarta.var.atrinik.model.maparchobject;

import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Factory to create {@link MapArchObject} instances.
 * @author Andreas Kirschbaum
 */
public class DefaultMapArchObjectFactory implements MapArchObjectFactory<MapArchObject> {

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * Creates a new instance.
     * @param globalSettings the global settings to use
     */
    public DefaultMapArchObjectFactory(@NotNull final GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public MapArchObject newMapArchObject(final boolean addDefaultAttributes) {
        final MapArchObject mapArchObject = new MapArchObject();
        if (addDefaultAttributes) {
            mapArchObject.addText("Created:  " + String.format("%tF", System.currentTimeMillis()) + " " + globalSettings.getUserName());
            mapArchObject.setDarkness(-1);
        }
        return mapArchObject;
    }

}
