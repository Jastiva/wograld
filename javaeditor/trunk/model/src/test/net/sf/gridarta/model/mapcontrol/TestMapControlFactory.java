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

package net.sf.gridarta.model.mapcontrol;

import java.io.File;
import java.util.List;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link MapControlFactory} implementation for testing purposes.
 * @author Andreas Kirschbaum
 */
public class TestMapControlFactory implements MapControlFactory<TestGameObject, TestMapArchObject, TestArchetype> {

    /**
     * The {@link MapWriter} for saving {@link MapControl} instances.
     */
    @NotNull
    private final MapWriter<TestGameObject, TestMapArchObject, TestArchetype> mapWriter;

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapModelFactory} for creating {@link MapModel} instances.
     */
    @NotNull
    private final MapModelFactory<TestGameObject, TestMapArchObject, TestArchetype> mapModelFactory;

    /**
     * Creates a new instance.
     * @param mapWriter the map writer for saving map control instances
     * @param globalSettings the global settings to use
     * @param mapModelFactory the map model factory for creating map model
     * instances
     */
    public TestMapControlFactory(@NotNull final MapWriter<TestGameObject, TestMapArchObject, TestArchetype> mapWriter, @NotNull final GlobalSettings globalSettings, @NotNull final MapModelFactory<TestGameObject, TestMapArchObject, TestArchetype> mapModelFactory) {
        this.mapWriter = mapWriter;
        this.globalSettings = globalSettings;
        this.mapModelFactory = mapModelFactory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<TestGameObject, TestMapArchObject, TestArchetype> newMapControl(@Nullable final List<TestGameObject> objects, @NotNull final TestMapArchObject mapArchObject, @Nullable final File file) {
        final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel = mapModelFactory.newMapModel(mapArchObject);
        if (objects != null) {
            mapModel.beginTransaction("init");
            try {
                mapModel.addObjectListToMap(objects);
            } finally {
                mapModel.endTransaction();
            }
            mapModel.resetModified();
        }
        final MapControl<TestGameObject, TestMapArchObject, TestArchetype> mapControl = new DefaultMapControl<TestGameObject, TestMapArchObject, TestArchetype>(mapModel, false, mapWriter, globalSettings);
        mapControl.getMapModel().setMapFile(file);
        return mapControl;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<TestGameObject, TestMapArchObject, TestArchetype> newPickmapControl(@Nullable final List<TestGameObject> objects, @NotNull final TestMapArchObject mapArchObject, @Nullable final File file) {
        throw new AssertionError();
    }

}
