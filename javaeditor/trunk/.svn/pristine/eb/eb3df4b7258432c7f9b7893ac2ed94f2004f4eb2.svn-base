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

import java.awt.Component;
import net.sf.gridarta.gui.dialog.mapproperties.MapPropertiesDialogFactory;
import net.sf.gridarta.gui.dialog.newmap.NewMapDialogFactory;
import net.sf.gridarta.gui.dialog.prefs.AppPreferencesModel;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.mapview.MapViewFactory;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.gui.scripts.ScriptArchDataUtils;
import net.sf.gridarta.gui.scripts.ScriptedEventEditor;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeList;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.configsource.ConfigSourceFactory;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.DefaultMapReaderFactory;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.GameObjectParserFactory;
import net.sf.gridarta.model.io.MapArchObjectParserFactory;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapcontrol.MapControlFactory;
import net.sf.gridarta.model.mapmanager.AbstractMapManager;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.scripts.ScriptArchData;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.scripts.ScriptedEventFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.checks.AttributeRangeChecker;
import net.sf.gridarta.plugin.PluginExecutor;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginParameters;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.prefs.PreferencesGroup;
import org.jetbrains.annotations.NotNull;

/**
 * A factory to create editor-dependent objects.
 * @author Andreas Kirschbaum
 */
public interface EditorFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Creates a new {@link DefaultMainControl} instance.
     * @param forceReadFromFiles whether the current config source should be
     * ignored
     * @param errorView the error view for reporting errors
     * @param globalSettings the global settings to use
     * @param configSourceFactory the config source factory to use
     * @param pathManager the path manager to use
     * @param gameObjectMatchers the game object matchers to use
     * @param gameObjectFactory the game object factory to use
     * @param archetypeTypeSet the archetype type set to use
     * @param archetypeSet the archetype set to use
     * @param archetypeChooserModel the archetype chooser model to use
     * @param autojoinLists the autojoin lists to use
     * @param mapManager the map manager
     * @param pluginModel the script model
     * @param validators the map validators
     * @param scriptedEventEditor the scripted event editor
     * @param resources the resources
     * @param numberSpells the number spells to use
     * @param gameObjectSpells the game object spells to use
     * @param pluginParameterFactory the plugin parameter factory to use
     * @param validatorPreferences the validator preferences to use
     * @param mapWriter the map writer for saving temporary maps
     * @return the new instance
     */
    @NotNull
    DefaultMainControl<G, A, R> newMainControl(boolean forceReadFromFiles, @NotNull ErrorView errorView, @NotNull GlobalSettings globalSettings, @NotNull ConfigSourceFactory configSourceFactory, @NotNull PathManager pathManager, @NotNull GameObjectMatchers gameObjectMatchers, @NotNull GameObjectFactory<G, A, R> gameObjectFactory, @NotNull ArchetypeTypeSet archetypeTypeSet, @NotNull ArchetypeSet<G, A, R> archetypeSet, @NotNull ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull AutojoinLists<G, A, R> autojoinLists, @NotNull AbstractMapManager<G, A, R> mapManager, @NotNull PluginModel<G, A, R> pluginModel, @NotNull DelegatingMapValidator<G, A, R> validators, @NotNull ScriptedEventEditor<G, A, R> scriptedEventEditor, @NotNull AbstractResources<G, A, R> resources, @NotNull Spells<NumberSpell> numberSpells, @NotNull Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull PluginParameterFactory<G, A, R> pluginParameterFactory, @NotNull ValidatorPreferences validatorPreferences, @NotNull MapWriter<G, A, R> mapWriter);

    /**
     * Returns the offset for drawing double faces.
     * @return the offset
     */
    int getDoubleFaceOffset();

    /**
     * Creates a new {@link MapArchObjectFactory} instance.
     * @param globalSettings the global settings to use
     * @return the new instance
     */
    @NotNull
    MapArchObjectFactory<A> newMapArchObjectFactory(@NotNull GlobalSettings globalSettings);

    /**
     * Creates a new {@link MapArchObjectParserFactory} instance.
     * @return the new instance
     */
    @NotNull
    MapArchObjectParserFactory<A> newMapArchObjectParserFactory();

    /**
     * Creates a new {@link GameObjectFactory} instance.
     * @param faceObjectProviders the face object providers to use
     * @param animationObjects the animation objects for looking up animations
     * @param archetypeTypeSet the archetype type set for looking up archetype
     * types
     * @return the new instance
     */
    @NotNull
    GameObjectFactory<G, A, R> newGameObjectFactory(@NotNull FaceObjectProviders faceObjectProviders, @NotNull AnimationObjects animationObjects, @NotNull ArchetypeTypeSet archetypeTypeSet);

    /**
     * Creates a new {@link GameObjectParserFactory} instance.
     * @param gameObjectFactory the game object parser factory to use
     * @param archetypeSet the archetype set for looking up archetypes
     * @return the new instance
     */
    @NotNull
    GameObjectParserFactory<G, A, R> newGameObjectParserFactory(@NotNull GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final ArchetypeSet<G, A, R> archetypeSet);

    /**
     * Creates a new {@link GlobalSettings} instance.
     * @return the new instance
     */
    @NotNull
    GlobalSettings newGlobalSettings();

    /**
     * Creates a new {@link ArchetypeFactory} instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     * @return the new instance
     */
    @NotNull
    ArchetypeFactory<G, A, R> newArchetypeFactory(@NotNull FaceObjectProviders faceObjectProviders, @NotNull AnimationObjects animationObjects);

    /**
     * Creates a new {@link ArchetypeSet} instance.
     * @param globalSettings the global settings to use
     * @param archetypeFactory the archetype factory to use
     * @return the new instance
     */
    @NotNull
    ArchetypeSet<G, A, R> newArchetypeSet(@NotNull GlobalSettings globalSettings, @NotNull ArchetypeFactory<G, A, R> archetypeFactory);

    /**
     * Creates a new {@link MapControlFactory} instance.
     * @param mapWriter the map writer to use
     * @param globalSettings the global settings to use
     * @param mapModelFactory the map model factory to use
     * @return the new instance
     */
    @NotNull
    MapControlFactory<G, A, R> newMapControlFactory(@NotNull MapWriter<G, A, R> mapWriter, @NotNull GlobalSettings globalSettings, @NotNull MapModelFactory<G, A, R> mapModelFactory);

    /**
     * Returns whether the face file contains face numbers.
     * @return whether the face file contains face numbers
     */
    boolean getIncludeFaceNumbers();

    /**
     * Initializes smoothing information.
     * @param faceObjects the face objects to use
     */
    void initSmoothFaces(@NotNull FaceObjects faceObjects);

    /**
     * Initializes the map validators.
     * @param mapValidators the map validators that delegates to add to
     * @param errorViewCollector the error view collector to use
     * @param globalSettings the global settings instance
     * @param gameObjectMatchers the defined game object matchers
     * @param attributeRangeChecker the attribute range checker to use
     * @param validatorPreferences the validator preferences to use
     */
    void initMapValidators(@NotNull DelegatingMapValidator<G, A, R> mapValidators, @NotNull ErrorViewCollector errorViewCollector, @NotNull GlobalSettings globalSettings, @NotNull GameObjectMatchers gameObjectMatchers, @NotNull AttributeRangeChecker<G, A, R> attributeRangeChecker, @NotNull final ValidatorPreferences validatorPreferences);

    /**
     * Creates a new {@link AbstractArchetypeParser} instance.
     * @param errorView the error view for reporting errors
     * @param gameObjectParser the game object parser to use
     * @param animationObjects the animation objects to use
     * @param archetypeSet the archetype set to use
     * @param gameObjectFactory the game object factory to use
     * @param globalSettings the global settings to use
     * @return the new instance
     */
    AbstractArchetypeParser<G, A, R, ?> newArchetypeParser(@NotNull ErrorView errorView, GameObjectParser<G, A, R> gameObjectParser, AnimationObjects animationObjects, ArchetypeSet<G, A, R> archetypeSet, @NotNull GameObjectFactory<G, A, R> gameObjectFactory, @NotNull GlobalSettings globalSettings);

    /**
     * Creates a new {@link ScriptArchUtils} instance.
     * @param eventTypes the event types to use
     * @return the new instance
     */
    @NotNull
    ScriptArchUtils newScriptArchUtils(@NotNull ArchetypeTypeList eventTypes);

    /**
     * Creates a new {@link ScriptedEventFactory} instance.
     * @param scriptArchUtils the script arch utils to use
     * @param gameObjectFactory the game object factory for creating game
     * objects
     * @param scriptedEventEditor the scripted event editor to use
     * @param archetypeSet the archetype set to use
     * @return the new instance
     */
    @NotNull
    ScriptedEventFactory<G, A, R> newScriptedEventFactory(@NotNull ScriptArchUtils scriptArchUtils, @NotNull GameObjectFactory<G, A, R> gameObjectFactory, @NotNull ScriptedEventEditor<G, A, R> scriptedEventEditor, @NotNull ArchetypeSet<G, A, R> archetypeSet);

    /**
     * Creates a new {@link ScriptArchData} instance.
     * @return the new instance
     */
    @NotNull
    ScriptArchData<G, A, R> newScriptArchData();

    /**
     * Creates a new {@link ScriptArchDataUtils} instance.
     * @param scriptArchUtils the script arch utils to use
     * @param scriptedEventFactory the scripted event factory to use
     * @param scriptedEventEditor the scripted event editor to use
     * @return the new instance
     */
    @NotNull
    ScriptArchDataUtils<G, A, R> newScriptArchDataUtils(@NotNull ScriptArchUtils scriptArchUtils, @NotNull ScriptedEventFactory<G, A, R> scriptedEventFactory, @NotNull ScriptedEventEditor<G, A, R> scriptedEventEditor);

    /**
     * Creates a new {@link RendererFactory} instance.
     * @param mapViewSettings the map view settings to use
     * @param filterControl the filter control to use
     * @param gameObjectParser the game object parser for creating tooltip
     * information
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param systemIcons the system icons for creating icons
     * @return the new instance
     */
    @NotNull
    RendererFactory<G, A, R> newRendererFactory(@NotNull MapViewSettings mapViewSettings, @NotNull FilterControl<G, A, R> filterControl, @NotNull GameObjectParser<G, A, R> gameObjectParser, @NotNull FaceObjectProviders faceObjectProviders, @NotNull SystemIcons systemIcons);

    /**
     * Creates a new {@link MapViewFactory} instance.
     * @param rendererFactory the renderer factory to use
     * @param pathManager the path manager for converting path names
     * @return the new instance
     */
    @NotNull
    MapViewFactory<G, A, R> newMapViewFactory(@NotNull RendererFactory<G, A, R> rendererFactory, @NotNull PathManager pathManager);

    /**
     * Creates a new {@link AppPreferencesModel} instance.
     * @return the new instance
     */
    @NotNull
    AppPreferencesModel createAppPreferencesModel();

    /**
     * Creates a new {@link MapPropertiesDialogFactory} instance.
     * @param globalSettings the global settings to use
     * @param mapManager the map manager to use
     * @param mapPathNormalizer the map path normalizer for converting map paths
     * to files
     * @return the new instance
     */
    @NotNull
    MapPropertiesDialogFactory<G, A, R> newMapPropertiesDialogFactory(@NotNull GlobalSettings globalSettings, @NotNull MapManager<G, A, R> mapManager, @NotNull MapPathNormalizer mapPathNormalizer);

    /**
     * Creates a new {@link NewMapDialogFactory} instance.
     * @param mapViewsManager the map views
     * @param mapArchObjectFactory the map arch object factory instance
     * @param parent the parent component of the dialogs
     * @return the new new map dialog factory instance
     */
    @NotNull
    NewMapDialogFactory<G, A, R> newNewMapDialogFactory(@NotNull MapViewsManager<G, A, R> mapViewsManager, @NotNull MapArchObjectFactory<A> mapArchObjectFactory, @NotNull Component parent);

    /**
     * Creates a new {@link PreferencesGroup} instance.
     * @param globalSettings the global settings to use
     * @param validators the validators to use
     * @param appPreferencesModel the app preferences model to use
     * @param exitConnectorModel the exit connector model to use
     * @param configSourceFactory the config source factory to use
     * @return the new instance
     */
    @NotNull
    PreferencesGroup createPreferencesGroup(@NotNull GlobalSettings globalSettings, @NotNull DelegatingMapValidator<G, A, R> validators, @NotNull AppPreferencesModel appPreferencesModel, @NotNull ExitConnectorModel exitConnectorModel, @NotNull ConfigSourceFactory configSourceFactory);

    /**
     * Creates a new {@link GUIMainControl} instance.
     * @param mainControl the main control to use
     * @param errorView the error view to add errors to
     * @param guiUtils the gui utils for creating icons
     * @param configSourceFactory the config source factory
     * @param rendererFactory the renderer factory
     * @param filterControl the filter control
     * @param pluginExecutor the script executor to use
     * @param pluginParameters the script parameters to use
     * @param mapManager the map manager
     * @param pickmapManager the pickmap manager
     * @param mapModelFactory the map model factory
     * @param archetypeSet the archetype set
     * @param faceObjects the face objects
     * @param globalSettings the global settings
     * @param mapViewSettings the map view settings
     * @param faceObjectProviders the face object providers
     * @param pathManager the path manager
     * @param topmostInsertionMode the "topmost" insertion mode
     * @param gameObjectFactory the game object factory
     * @param systemIcons the system icons for creating icons
     * @param archetypeTypeSet the archetype type set
     * @param mapArchObjectFactory the map arch object factory to use
     * @param mapReaderFactory the map reader factory to use
     * @param validators the map validators
     * @param gameObjectMatchers the game object matchers
     * @param pluginModel the script model
     * @param animationObjects the animation objects
     * @param archetypeChooserModel the archetype chooser model
     * @param scriptedEventEditor the scripted event editor
     * @param resources the resources
     * @param numberSpells the number spells to use
     * @param gameObjectSpells the game object spells to use
     * @param pluginParameterFactory the plugin parameter factory to use
     * @return the new instance
     */
    @NotNull
    GUIMainControl<G, A, R> createGUIMainControl(@NotNull DefaultMainControl<G, A, R> mainControl, @NotNull ErrorView errorView, @NotNull GUIUtils guiUtils, @NotNull ConfigSourceFactory configSourceFactory, @NotNull RendererFactory<G, A, R> rendererFactory, @NotNull FilterControl<G, A, R> filterControl, @NotNull PluginExecutor<G, A, R> pluginExecutor, @NotNull PluginParameters pluginParameters, @NotNull AbstractMapManager<G, A, R> mapManager, @NotNull MapManager<G, A, R> pickmapManager, @NotNull MapModelFactory<G, A, R> mapModelFactory, @NotNull ArchetypeSet<G, A, R> archetypeSet, @NotNull FaceObjects faceObjects, @NotNull GlobalSettings globalSettings, @NotNull MapViewSettings mapViewSettings, @NotNull FaceObjectProviders faceObjectProviders, @NotNull PathManager pathManager, @NotNull InsertionMode<G, A, R> topmostInsertionMode, @NotNull GameObjectFactory<G, A, R> gameObjectFactory, @NotNull SystemIcons systemIcons, @NotNull ArchetypeTypeSet archetypeTypeSet, @NotNull MapArchObjectFactory<A> mapArchObjectFactory, @NotNull DefaultMapReaderFactory<G, A, R> mapReaderFactory, @NotNull DelegatingMapValidator<G, A, R> validators, @NotNull GameObjectMatchers gameObjectMatchers, @NotNull PluginModel<G, A, R> pluginModel, @NotNull AnimationObjects animationObjects, @NotNull ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull ScriptedEventEditor<G, A, R> scriptedEventEditor, @NotNull AbstractResources<G, A, R> resources, @NotNull Spells<NumberSpell> numberSpells, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull PluginParameterFactory<G, A, R> pluginParameterFactory);

    /**
     * Creates a new {@link AbstractResources} instance.
     * @param gameObjectParser the game object parser to use
     * @param archetypeSet the archetype set to use
     * @param archetypeParser the archetype parser to use
     * @param mapViewSettings the map view settings to use
     * @param faceObjects the face objects to use
     * @param animationObjects the animation objects to use
     * @param archFaceProvider the arch face provider to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @return the new instance
     */
    @NotNull
    AbstractResources<G, A, R> newResources(@NotNull final GameObjectParser<G, A, R> gameObjectParser, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final AbstractArchetypeParser<G, A, R, ?> archetypeParser, @NotNull final MapViewSettings mapViewSettings, @NotNull final FaceObjects faceObjects, @NotNull final AnimationObjects animationObjects, @NotNull final ArchFaceProvider archFaceProvider, @NotNull final FaceObjectProviders faceObjectProviders);

    /**
     * Creates the "open in client" action. Does nothing if this editor does not
     * support this action.
     * @param mapViewManager the map view manager for tracking the current map
     * view
     * @param fileControl the file control for saving maps
     * @param pathManager the path manager for converting path names
     */
    void newServerActions(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final FileControl<G, A, R> fileControl, @NotNull final PathManager pathManager);

}
