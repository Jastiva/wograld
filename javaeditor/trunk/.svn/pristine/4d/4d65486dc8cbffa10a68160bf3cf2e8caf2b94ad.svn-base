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

package net.sf.gridarta.plugin.parameter;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link PluginParameter} that holds a {@link MapControl} value.
 */
public class MapParameter<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractPluginParameter<G, A, R, Object> { // XXX: find correct parameter type: String or MapControl<GameObject, MapArchObject, Archetype>

    /**
     * The string representation of this parameter type.
     */
    @NotNull
    public static final String PARAMETER_TYPE = MapControl.class.getName();

    /**
     * The string representation for the current map.
     */
    @NotNull
    private static final String CURRENT_MAP = "[Current Map]";

    /**
     * The {@link MapManager} for converting map paths to {@link MapControl}
     * instances.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * Creates a new instance.
     * @param mapManager the map manager for converting map paths to map control
     * instances
     */
    public MapParameter(@NotNull final MapManager<G, A, R> mapManager) {
        this.mapManager = mapManager;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getParameterType() {
        return PARAMETER_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public <T> T visit(@NotNull final PluginParameterVisitor<G, A, R, T> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValue() {
        final String s = (String) super.getValue();
        if (CURRENT_MAP.equals(s)) {
            return mapManager.getCurrentMap();
        }

        for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
            if (mapControl.getMapModel().getMapArchObject().getMapName().equalsIgnoreCase(s)) {
                return mapControl;
            }
        }

        return mapManager.getCurrentMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setValue(@Nullable final Object value) {
        if (value == null) {
            return super.setValue(null);
        } else {
            final MapControl<?, ?, ?> map = (MapControl<?, ?, ?>) value;
            return super.setValue(map.getMapModel().getMapArchObject().getMapName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setStringValue(@NotNull final String value) {
        return super.setValue(value);
    }

    /**
     * Selects the current map.
     */
    public void setValueToCurrent() {
        super.setValue(CURRENT_MAP);
    }

}
