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

package net.sf.gridarta.maincontrol;

import java.util.HashSet;
import java.util.Set;
import net.sf.gridarta.gui.filter.DefaultFilterControl;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.renderer.ImageCreator;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.filter.NamedFilter;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.DefaultMapReaderFactory;
import net.sf.gridarta.model.io.DefaultMapWriter;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.GameObjectParserFactory;
import net.sf.gridarta.model.io.MapArchObjectParserFactory;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapcontrol.MapControlFactory;
import net.sf.gridarta.model.mapmanager.DefaultMapManager;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mapmodel.TopmostInsertionMode;
import net.sf.gridarta.model.mapviewsettings.DefaultMapViewSettings;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for creating {@link ImageCreator} instances.
 * @author Andreas Kirschbaum
 */
public class ImageCreatorFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * Creates a new instance.
     * @param systemIcons the system icons for creating icons
     */
    public ImageCreatorFactory(@NotNull final SystemIcons systemIcons) {
        this.systemIcons = systemIcons;
    }

    /**
     * Creates a new {@link ImageCreator} instance.
     * @param editorFactory the editor factory to use
     * @param faceObjectProviders the face object providers to use
     * @param animationObjects the animation objects to use
     * @param archetypeSet the archetype set to use
     * @return the new instance
     */
    @NotNull
    public ImageCreator<G, A, R> newImageCreator(@NotNull final EditorFactory<G, A, R> editorFactory, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeSet<G, A, R> archetypeSet) {
        final GlobalSettings globalSettings = editorFactory.newGlobalSettings();
        final MapArchObjectFactory<A> mapArchObjectFactory = editorFactory.newMapArchObjectFactory(globalSettings);
        final MapArchObjectParserFactory<A> mapArchObjectParserFactory = editorFactory.newMapArchObjectParserFactory();
        final ArchetypeTypeSet archetypeTypeSet = new ArchetypeTypeSet();
        final GameObjectFactory<G, A, R> gameObjectFactory = editorFactory.newGameObjectFactory(faceObjectProviders, animationObjects, archetypeTypeSet);
        final GameObjectMatchers gameObjectMatchers = new GameObjectMatchers();
        final GameObjectParserFactory<G, A, R> gameObjectParserFactory = editorFactory.newGameObjectParserFactory(gameObjectFactory, archetypeSet);
        final GameObjectParser<G, A, R> gameObjectParser = gameObjectParserFactory.newGameObjectParser();
        final MapViewSettings mapViewSettings = new DefaultMapViewSettings();
        final MapReaderFactory<G, A> mapReaderFactory = new DefaultMapReaderFactory<G, A, R>(mapArchObjectFactory, mapArchObjectParserFactory, gameObjectParserFactory, mapViewSettings);
        final MapWriter<G, A, R> mapWriter = new DefaultMapWriter<G, A, R>(mapArchObjectParserFactory, gameObjectParser);
        final AutojoinLists<G, A, R> autojoinLists = new AutojoinLists<G, A, R>(mapViewSettings);
        final ArchetypeChooserModel<G, A, R> archetypeChooserModel = new ArchetypeChooserModel<G, A, R>();
        final InsertionMode<G, A, R> topmostInsertionMode = new TopmostInsertionMode<G, A, R>();
        final MapModelFactory<G, A, R> mapModelFactory = new MapModelFactory<G, A, R>(archetypeChooserModel, autojoinLists, mapViewSettings, gameObjectFactory, gameObjectMatchers, topmostInsertionMode);
        final MapControlFactory<G, A, R> mapControlFactory = editorFactory.newMapControlFactory(mapWriter, globalSettings, mapModelFactory);
        final MapManager<G, A, R> mapManager = new DefaultMapManager<G, A, R>(mapReaderFactory, mapControlFactory, globalSettings, faceObjectProviders);
        final Set<NamedGameObjectMatcher> matchers = new HashSet<NamedGameObjectMatcher>();
        final NamedFilter defaultNamedFilterList = new NamedFilter(matchers);
        final FilterControl<G, A, R> filterControl = new DefaultFilterControl<G, A, R>(defaultNamedFilterList);
        final RendererFactory<G, A, R> rendererFactory = editorFactory.newRendererFactory(mapViewSettings, filterControl, gameObjectParser, faceObjectProviders, systemIcons);
        return new ImageCreator<G, A, R>(mapManager, rendererFactory);
    }

}
