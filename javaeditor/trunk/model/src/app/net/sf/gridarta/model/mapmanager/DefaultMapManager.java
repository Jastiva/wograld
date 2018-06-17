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

package net.sf.gridarta.model.mapmanager;

import java.io.File;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages all opened maps.
 * @author Andreas Kirschbaum
 */
public class DefaultMapManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapManager<G, A, R> {

    /**
     * The {@link MapControlFactory} instance.
     */
    @NotNull
    private final MapControlFactory<G, A, R> mapControlFactory;

    /**
     * Create a new map manager.
     * @param mapReaderFactory the map reader factory instance
     * @param mapControlFactory the map control factory instance
     * @param globalSettings the global settings instance
     * @param faceObjectProviders the face object providers
     */
    public DefaultMapManager(@NotNull final MapReaderFactory<G, A> mapReaderFactory, @NotNull final MapControlFactory<G, A, R> mapControlFactory, @NotNull final GlobalSettings globalSettings, @NotNull final FaceObjectProviders faceObjectProviders) {
        super(mapReaderFactory, globalSettings, faceObjectProviders);
        this.mapControlFactory = mapControlFactory;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected MapControl<G, A, R> createMapControl(@Nullable final List<G> objects, @NotNull final A mapArchObject, @Nullable final File file) {
        return mapControlFactory.newMapControl(objects, mapArchObject, file);
    }

}
