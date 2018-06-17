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
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.filter.NamedFilter;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.settings.GlobalSettings;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for Plugin Parameters.
 * @author tchize
 */
public class PluginParameterFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    @NotNull
    private final MapManager<G, A, R> mapManager;

    @NotNull
    private final NamedFilter defaultFilterList;

    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link PluginParameterCodec} for converting {@link PluginParameter
     * PluginParameters} to or from XML representation.
     */
    @NotNull
    private final PluginParameterCodec<G, A, R> codec = new PluginParameterCodec<G, A, R>();

    public PluginParameterFactory(@NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final MapManager<G, A, R> mapManager, @NotNull final NamedFilter defaultFilterList, @NotNull final GlobalSettings globalSettings) {
        this.archetypeSet = archetypeSet;
        this.mapManager = mapManager;
        this.defaultFilterList = defaultFilterList;
        this.globalSettings = globalSettings;
    }

    @NotNull
    public PluginParameter<G, A, R> createParameter(@NotNull final Element parameterNode) throws NoSuchParameterException {
        return createParameter(parameterNode.getChildText("type"), parameterNode);
    }

    /**
     * Creates a new {@link StringParameter} from XML representation.
     * @param parameterNode the XML representation
     * @return the string parameter
     */
    @NotNull
    public PluginParameter<G, A, R> createStringParameter(@NotNull final Element parameterNode) {
        final PluginParameter<G, A, R> parameter = new StringParameter<G, A, R>();
        codec.fromXML(parameter, parameterNode);
        return parameter;
    }

    @NotNull
    public PluginParameter<G, A, R> createParameter(@NotNull final String type) throws NoSuchParameterException {
        if (type.equals(StringParameter.PARAMETER_TYPE)) {
            return new StringParameter<G, A, R>();
        }
        if (type.equals(IntegerParameter.PARAMETER_TYPE)) {
            return new IntegerParameter<G, A, R>();
        }
        if (type.equals(DoubleParameter.PARAMETER_TYPE)) {
            return new DoubleParameter<G, A, R>();
        }
        if (type.equals(BooleanParameter.PARAMETER_TYPE)) {
            return new BooleanParameter<G, A, R>();
        }
        if (type.equals(ArchParameter.PARAMETER_TYPE)) {
            return new ArchParameter<G, A, R>(archetypeSet);
        }
        if (type.equals(MapParameter.PARAMETER_TYPE)) {
            return new MapParameter<G, A, R>(mapManager);
        }
        if (type.equals(MapPathParameter.PARAMETER_TYPE)) {
            return new MapPathParameter<G, A, R>(globalSettings.getMapsDirectory());
        }
        if (type.equals(FilterParameter.PARAMETER_TYPE)) {
            return new FilterParameter<G, A, R>(defaultFilterList);
        }
        throw new NoSuchParameterException(type);
    }

    @NotNull
    public PluginParameter<G, A, R> createParameter(@NotNull final String type, @NotNull final Element parameterNode) throws NoSuchParameterException {
        final PluginParameter<G, A, R> p = createParameter(type);
        codec.fromXML(p, parameterNode);
        return p;
    }

    @NotNull
    public String[] getTypes() {
        return new String[] { StringParameter.PARAMETER_TYPE, IntegerParameter.PARAMETER_TYPE, DoubleParameter.PARAMETER_TYPE, BooleanParameter.PARAMETER_TYPE, ArchParameter.PARAMETER_TYPE, MapParameter.PARAMETER_TYPE, FilterParameter.PARAMETER_TYPE, MapPathParameter.PARAMETER_TYPE, };
    }

}
