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

package net.sf.gridarta.gui.dialog.plugin.parameter;

import javax.swing.JComponent;
import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesModel;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.plugin.parameter.ArchParameter;
import net.sf.gridarta.plugin.parameter.BooleanParameter;
import net.sf.gridarta.plugin.parameter.DoubleParameter;
import net.sf.gridarta.plugin.parameter.FilterParameter;
import net.sf.gridarta.plugin.parameter.IntegerParameter;
import net.sf.gridarta.plugin.parameter.MapParameter;
import net.sf.gridarta.plugin.parameter.MapPathParameter;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.plugin.parameter.PluginParameterVisitor;
import net.sf.gridarta.plugin.parameter.StringParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating {@link PluginParameterView} instances.
 * @author Andreas Kirschbaum
 */
public class PluginParameterViewFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    @NotNull
    private final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel;

    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The parent component for showing dialogs.
     */
    @Nullable
    private JComponent parent;

    @NotNull
    private final PluginParameterVisitor<G, A, R, PluginParameterView<G, A, R>> visitor = new PluginParameterVisitor<G, A, R, PluginParameterView<G, A, R>>() {

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final ArchParameter<G, A, R> parameter) {
            return new ArchParameterView<G, A, R>(parameter, gameObjectAttributesModel, archetypeSet, objectChooser, faceObjectProviders);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final BooleanParameter<G, A, R> parameter) {
            return new BooleanParameterView<G, A, R>(parameter);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final DoubleParameter<G, A, R> parameter) {
            return new DoubleParameterView<G, A, R>(parameter);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final FilterParameter<G, A, R> parameter) {
            return new FilterParameterView<G, A, R>(parameter);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final IntegerParameter<G, A, R> parameter) {
            return new IntegerParameterView<G, A, R>(parameter);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final MapParameter<G, A, R> parameter) {
            return new MapParameterView<G, A, R>(parameter, mapManager);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final MapPathParameter<G, A, R> parameter) {
            assert parent != null;
            return new MapPathParameterView<G, A, R>(parent, parameter);
        }

        @NotNull
        @Override
        public PluginParameterView<G, A, R> visit(@NotNull final StringParameter<G, A, R> parameter) {
            return new StringParameterView<G, A, R>(parameter);
        }

    };

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public PluginParameterViewFactory(@NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final MapManager<G, A, R> mapManager, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.archetypeSet = archetypeSet;
        this.gameObjectAttributesModel = gameObjectAttributesModel;
        this.objectChooser = objectChooser;
        this.mapManager = mapManager;
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * @param parent the parent component for showing dialogs
     */
    @NotNull
    public PluginParameterView<G, A, R> getView(@NotNull final JComponent parent, @NotNull final PluginParameter<G, A, R> parameter) {
        this.parent = parent;
        try {
            return parameter.visit(visitor);
        } finally {
            this.parent = null;
        }
    }

}
