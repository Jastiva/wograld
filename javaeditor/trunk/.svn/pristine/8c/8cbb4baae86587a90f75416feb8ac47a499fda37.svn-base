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

package net.sf.gridarta.var.atrinik.model.mapcontrol;

import java.io.File;
import java.util.List;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlFactory;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link MapControlFactory} to create Crossfire instance.
 * @author Andreas Kirschbaum
 */
public class DefaultMapControlFactory implements MapControlFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link MapWriter} for saving {@link MapControl} instances.
     */
    @NotNull
    private final MapWriter<GameObject, MapArchObject, Archetype> mapWriter;

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapModelFactory} for creating {@link MapModel} instances.
     */
    @NotNull
    private final MapModelFactory<GameObject, MapArchObject, Archetype> mapModelFactory;

    /**
     * Creates a new instance.
     * @param mapWriter the map writer for saving map control instances
     * @param globalSettings the global settings to use
     * @param mapModelFactory the map model factory for creating map model
     * instances
     */
    public DefaultMapControlFactory(@NotNull final MapWriter<GameObject, MapArchObject, Archetype> mapWriter, @NotNull final GlobalSettings globalSettings, @NotNull final MapModelFactory<GameObject, MapArchObject, Archetype> mapModelFactory) {
        this.mapWriter = mapWriter;
        this.globalSettings = globalSettings;
        this.mapModelFactory = mapModelFactory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<GameObject, MapArchObject, Archetype> newMapControl(@Nullable final List<GameObject> objects, @NotNull final MapArchObject mapArchObject, @Nullable final File file) {
        final MapModel<GameObject, MapArchObject, Archetype> mapModel = mapModelFactory.newMapModel(mapArchObject);
        if (objects != null) {
            mapModel.beginTransaction("init");
            try {
                mapModel.addObjectListToMap(objects);
            } finally {
                mapModel.endTransaction();
            }
            mapModel.resetModified();
        }
        final MapControl<GameObject, MapArchObject, Archetype> mapControl = new DefaultMapControl<GameObject, MapArchObject, Archetype>(mapModel, false, mapWriter, globalSettings);
        mapControl.getMapModel().setMapFile(file);
        return mapControl;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<GameObject, MapArchObject, Archetype> newPickmapControl(@Nullable final List<GameObject> objects, @NotNull final MapArchObject mapArchObject, @Nullable final File file) {
        mapArchObject.setDifficulty(1);
        final MapModel<GameObject, MapArchObject, Archetype> mapModel = mapModelFactory.newMapModel(mapArchObject);
        if (objects != null) {
            mapModel.beginTransaction("init");
            try {
                mapModel.addObjectListToMap(objects);
            } finally {
                mapModel.endTransaction();
            }
            mapModel.resetModified();
        }
        final MapControl<GameObject, MapArchObject, Archetype> mapControl = new DefaultMapControl<GameObject, MapArchObject, Archetype>(mapModel, true, mapWriter, globalSettings);
        mapControl.getMapModel().setMapFile(file);
        mapControl.getMapModel().resetModified();
        return mapControl;
    }

}
