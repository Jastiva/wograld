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

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.actions.ExitConnectorActions;
import net.sf.gridarta.gui.autovalidator.AutoValidator;
import net.sf.gridarta.gui.copybuffer.CopyBuffer;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListenerManager;
import net.sf.gridarta.gui.dialog.bookmarks.BookmarkActions;
import net.sf.gridarta.gui.dialog.find.FindDialogManager;
import net.sf.gridarta.gui.dialog.findarchetypes.FindArchetypesDialogManager;
import net.sf.gridarta.gui.dialog.gameobjectattributes.GameObjectAttributesDialogFactory;
import net.sf.gridarta.gui.dialog.goexit.GoExitDialogManager;
import net.sf.gridarta.gui.dialog.gomap.GoMapDialogManager;
import net.sf.gridarta.gui.dialog.mapproperties.MapPropertiesDialogFactory;
import net.sf.gridarta.gui.dialog.newmap.NewMapDialogFactory;
import net.sf.gridarta.gui.dialog.plugin.PluginController;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterViewFactory;
import net.sf.gridarta.gui.dialog.prefs.AppPreferencesModel;
import net.sf.gridarta.gui.dialog.replace.ReplaceDialogManager;
import net.sf.gridarta.gui.dialog.shortcuts.ShortcutsManager;
import net.sf.gridarta.gui.exitconnector.ExitConnectorController;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.mainwindow.GameObjectTab;
import net.sf.gridarta.gui.mainwindow.GameObjectTextEditorTab;
import net.sf.gridarta.gui.mainwindow.WarningsTab;
import net.sf.gridarta.gui.map.MapFileActions;
import net.sf.gridarta.gui.map.mapactions.EnterMap;
import net.sf.gridarta.gui.map.mapactions.MapActions;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewFactory;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.map.renderer.ImageCreator;
import net.sf.gridarta.gui.map.renderer.ImageCreator2;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.gui.map.viewaction.ViewActions;
import net.sf.gridarta.gui.mapcursor.MapCursorActions;
import net.sf.gridarta.gui.mapdesktop.MapDesktop;
import net.sf.gridarta.gui.mapdesktop.WindowMenuManager;
import net.sf.gridarta.gui.mapfiles.Loader;
import net.sf.gridarta.gui.mapfiles.MapFolderTree;
import net.sf.gridarta.gui.mapfiles.MapFolderTreeActions;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.gui.mapmenu.BookmarksMapMenuPreferences;
import net.sf.gridarta.gui.mapmenu.MapMenu;
import net.sf.gridarta.gui.mapmenu.MapMenuManager;
import net.sf.gridarta.gui.mapmenu.MapMenuPreferences;
import net.sf.gridarta.gui.mapmenu.RecentMapMenuPreferences;
import net.sf.gridarta.gui.mapuserlistener.MapKeyListener;
import net.sf.gridarta.gui.mapuserlistener.MapUserListenerManager;
import net.sf.gridarta.gui.misc.About;
import net.sf.gridarta.gui.misc.DefaultFileControl;
import net.sf.gridarta.gui.misc.HelpActions;
import net.sf.gridarta.gui.misc.MainToolbar;
import net.sf.gridarta.gui.misc.MainView;
import net.sf.gridarta.gui.misc.MainViewActions;
import net.sf.gridarta.gui.misc.MapPreview;
import net.sf.gridarta.gui.misc.RecentManager;
import net.sf.gridarta.gui.misc.StatusBar;
import net.sf.gridarta.gui.panel.archetypechooser.ArchetypeChooserControl;
import net.sf.gridarta.gui.panel.connectionview.ConnectionControl;
import net.sf.gridarta.gui.panel.connectionview.Control;
import net.sf.gridarta.gui.panel.connectionview.LockedItemsControl;
import net.sf.gridarta.gui.panel.connectionview.MonsterControl;
import net.sf.gridarta.gui.panel.gameobjectattributes.ArchTab;
import net.sf.gridarta.gui.panel.gameobjectattributes.ErrorListView;
import net.sf.gridarta.gui.panel.gameobjectattributes.EventsTab;
import net.sf.gridarta.gui.panel.gameobjectattributes.FaceTab;
import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesControl;
import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesModel;
import net.sf.gridarta.gui.panel.gameobjectattributes.MsgTextTab;
import net.sf.gridarta.gui.panel.gameobjectattributes.TextEditorTab;
import net.sf.gridarta.gui.panel.gameobjecttexteditor.GameObjectTextEditor;
import net.sf.gridarta.gui.panel.objectchooser.DefaultObjectChooser;
import net.sf.gridarta.gui.panel.pickmapchooser.PickmapChooserControl;
import net.sf.gridarta.gui.panel.pickmapchooser.PickmapChooserModel;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareActions;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModel;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareView;
import net.sf.gridarta.gui.panel.tools.ToolPalette;
import net.sf.gridarta.gui.scripts.ScriptArchDataUtils;
import net.sf.gridarta.gui.scripts.ScriptArchEditor;
import net.sf.gridarta.gui.scripts.ScriptedEventEditor;
import net.sf.gridarta.gui.spells.SpellsUtils;
import net.sf.gridarta.gui.treasurelist.CFTreasureListTree;
import net.sf.gridarta.gui.undo.UndoControl;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.gridarta.gui.utils.tabbedpanel.Tab;
import net.sf.gridarta.mainactions.DefaultExiter;
import net.sf.gridarta.mainactions.MainActions;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.AnimationValidator;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.ArchetypeValidator;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.configsource.ConfigSourceFactory;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.exitconnector.DefaultExitConnectorModel;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.exitconnector.ExitMatcher;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.CacheFiles;
import net.sf.gridarta.model.io.MapReaderFactory;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.AbstractMapManager;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.TypeNrsGameObjectMatcher;
import net.sf.gridarta.model.pickmapsettings.DefaultPickmapSettings;
import net.sf.gridarta.model.pickmapsettings.PickmapSettings;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.scripts.ScriptArchData;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import net.sf.gridarta.model.treasurelist.TreasureTree;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import net.sf.gridarta.model.validation.checks.BlockedSquareChecker;
import net.sf.gridarta.plugin.PluginExecutor;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginParameters;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import net.sf.gridarta.textedit.textarea.TextAreaDefaults;
import net.sf.gridarta.updater.UpdaterManager;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.GuiFileFilters;
import net.sf.gridarta.utils.ProcessRunner;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.prefs.PreferencesGroup;
import net.sf.japi.swing.prefs.PreferencesPane;
import net.sf.japi.swing.tod.TipOfTheDayManager;
import net.sf.japi.util.ThrowableHandler;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Creates the main GUI of Gridarta.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class GUIMainControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ThrowableHandler<Throwable> {

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(GUIMainControl.class);

    /**
     * The {@link Preferences}.
     */
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The {@link StatusBar} instance.
     */
    private final StatusBar<G, A, R> statusBar;

    /**
     * The {@link PickmapChooserControl}.
     */
    @NotNull
    private final PickmapChooserControl<G, A, R> pickmapChooserControl;

    /**
     * The main window's {@link JFrame}.
     */
    @NotNull
    private final JFrame mainViewFrame;

    /**
     * The {@link MainView} instance.
     */
    @NotNull
    private final MainView mainView;

    /**
     * The {@link PluginController} instance.
     */
    @NotNull
    private final PluginController<G, A, R> pluginControl;

    /**
     * The {@link MapManager} instance.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link MapViewsManager} instance.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The {@link GlobalSettings} instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link EditorFactory} instance.
     */
    @NotNull
    private final EditorFactory<G, A, R> editorFactory;

    /**
     * The extensions of event script files.
     */
    @NotNull
    private final String scriptExtension;

    /**
     * The {@link DelegatingMapValidator} instance that contains all defined map
     * validators.
     */
    @NotNull
    private final DelegatingMapValidator<G, A, R> validators;

    /**
     * The {@link RendererFactory} instance.
     */
    @NotNull
    private final RendererFactory<G, A, R> rendererFactory;

    /**
     * The {@link ScriptEditControl} instance.
     */
    @NotNull
    private final ScriptEditControl scriptEditControl;

    /**
     * The {@link NewMapDialogFactory} instance for creating new maps or
     * pickmaps.
     */
    @NotNull
    private final NewMapDialogFactory<G, A, R> newMapDialogFactory;

    /**
     * The {@link FileControl} instance.
     */
    @NotNull
    private final FileControl<G, A, R> fileControl;

    /**
     * The {@link PreferencesGroup} instance. Set to <code>null</code> if not
     * yet created.
     */
    @Nullable
    private PreferencesGroup preferencesGroup;

    /**
     * The {@link AppPreferencesModel} instance.
     */
    @NotNull
    private final AppPreferencesModel appPreferencesModel;

    /**
     * The {@link ProcessRunner} instance that controls the client. Set to
     * <code>null</code> if not yet created.
     */
    @Nullable
    private ProcessRunner controlClient;

    /**
     * The {@link ProcessRunner} instance that controls the server. Set to
     * <code>null</code> if not yet created.
     */
    @Nullable
    private ProcessRunner controlServer;

    /**
     * The {@link SpellsUtils} instance.
     */
    @Nullable
    private final SpellsUtils spellUtils;

    /**
     * The {@link ExitConnectorModel} instance.
     */
    @NotNull
    private final ExitConnectorModel exitConnectorModel;

    /**
     * The {@link ArchetypeSet} instance.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * The {@link ConfigSourceFactory} instance.
     */
    @NotNull
    private final ConfigSourceFactory configSourceFactory;

    /**
     * The {@link Exiter} instance for terminating the application.
     */
    @NotNull
    private final Exiter exiter;

    /**
     * The {@link ShortcutsManager} instance.
     */
    @NotNull
    private final ShortcutsManager shortcutsManager;

    /**
     * The {@link UpdaterManager} instance.
     */
    @NotNull
    private final UpdaterManager updaterManager;

    /**
     * Creates a new instance.
     * @param createDirectionPane whether the direction panel should be created
     * @param mapManager the map manager instance
     * @param pickmapManager the pickmap manager instance
     * @param archetypeSet the archetype set instance
     * @param faceObjects the face objects instance
     * @param globalSettings the global settings instance
     * @param mapViewSettings the map view settings instance
     * @param mapModelFactory the map model factory instance
     * @param mapReaderFactory the map reader factory instance
     * @param mapArchObjectFactory the map arch object factory instance
     * @param treasureTree the treasure tree instance
     * @param archetypeTypeSet the archetype type set instance
     * @param compassIcon the icon to display in the selected square view;
     * @param gridartaJarFilename the filename of the editor's .jar file
     * @param mapFileFilter the file filter for map files
     * @param scriptFileFilter the file filter for script files
     * @param scriptExtension the file extension for script files
     * @param validators the map validators
     * @param resources the resources to collect
     * @param gameObjectMatchers the game object matchers instance
     * @param errorView the error view for reporting errors
     * @param lockedItemsTypeNumbers the type numbers of game objects being
     * locked items
     * @param scriptsDir the plugin scripts directory
     * @param pluginModel the script model instance
     * @param archetypeChooserModel the archetype chooser model instance
     * @param animationObjects the animation objects instance
     * @param scriptArchEditor the script arch editor instance
     * @param scriptedEventEditor the scripted event editor instance
     * @param scriptArchData the script arch data instance
     * @param scriptArchDataUtils the script arch data utils instance
     * @param scriptArchUtils the script arch utils instance
     * @param autoValidatorDefault whether the auto validator is enabled by
     * @param spellFile the spell file instance
     * @param allowRandomMapParameters whether exit paths may point to random
     * map parameters
     * @param directionMap maps relative direction to map window direction
     * @param editorFactory the editor factory instance
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param pluginParameterFactory the plugin parameter factory instance
     * @param gameObjectFactory the game object factory instance
     * @param pathManager the path manager for converting path names
     * @param cacheFiles the cache files for icon and preview images
     * @param gameObjectSpells the game object spells instance
     * @param numberSpells the numbered spells instance
     * @param undefinedSpellIndex the index for "no spell"
     * @param systemIcons the system icons for creating icons
     * @param configSourceFactory the config source factory instance
     * @param topmostInsertionMode the "topmost" insertion mode instance
     * @param rendererFactory the renderer factory instance
     * @param filterControl the filter control instance
     * @param pluginExecutor the script executor instance
     * @param pluginParameters the script parameters instance
     */
    public GUIMainControl(final boolean createDirectionPane, @NotNull final AbstractMapManager<G, A, R> mapManager, @NotNull final MapManager<G, A, R> pickmapManager, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final FaceObjects faceObjects, @NotNull final GlobalSettings globalSettings, @NotNull final MapViewSettings mapViewSettings, @NotNull final MapModelFactory<G, A, R> mapModelFactory, @NotNull final MapReaderFactory<G, A> mapReaderFactory, final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final TreasureTree treasureTree, @NotNull final ArchetypeTypeSet archetypeTypeSet, @Nullable final ImageIcon compassIcon, @NotNull final String gridartaJarFilename, @NotNull final FileFilter mapFileFilter, @NotNull final FileFilter scriptFileFilter, @NotNull final String scriptExtension, @NotNull final DelegatingMapValidator<G, A, R> validators, @NotNull final AbstractResources<G, A, R> resources, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final ErrorView errorView, @NotNull final int[] lockedItemsTypeNumbers, @NotNull final String scriptsDir, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull final AnimationObjects animationObjects, @NotNull final ScriptArchEditor<G, A, R> scriptArchEditor, @NotNull final ScriptedEventEditor<G, A, R> scriptedEventEditor, @NotNull final ScriptArchData<G, A, R> scriptArchData, @NotNull final ScriptArchDataUtils<G, A, R> scriptArchDataUtils, @NotNull final ScriptArchUtils scriptArchUtils, final boolean autoValidatorDefault, @Nullable final String spellFile, final boolean allowRandomMapParameters, @NotNull final Direction[] directionMap, @NotNull final EditorFactory<G, A, R> editorFactory, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final PathManager pathManager, @NotNull final CacheFiles cacheFiles, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull final Spells<NumberSpell> numberSpells, final int undefinedSpellIndex, @NotNull final SystemIcons systemIcons, @NotNull final ConfigSourceFactory configSourceFactory, @NotNull final InsertionMode<G, A, R> topmostInsertionMode, @NotNull final RendererFactory<G, A, R> rendererFactory, @NotNull final FilterControl<G, A, R> filterControl, @NotNull final PluginExecutor<G, A, R> pluginExecutor, @NotNull final PluginParameters pluginParameters) {
        this.archetypeSet = archetypeSet;
        this.configSourceFactory = configSourceFactory;
        this.rendererFactory = rendererFactory;
        this.mapManager = mapManager;
        final MapViewFactory<G, A, R> mapViewFactory = editorFactory.newMapViewFactory(rendererFactory, pathManager);
        mapViewsManager = new MapViewsManager<G, A, R>(mapViewSettings, mapViewFactory, mapManager, pickmapManager);
        this.globalSettings = globalSettings;
        this.editorFactory = editorFactory;
        this.scriptExtension = scriptExtension;
        this.validators = validators;
        final ArchetypeChooserControl<G, A, R> archetypeChooserControl = new ArchetypeChooserControl<G, A, R>(archetypeChooserModel, createDirectionPane, faceObjectProviders);
        final ImageCreator<G, A, R> imageCreator = new ImageCreator<G, A, R>(mapManager, rendererFactory);
        final ImageCreator2<G, A, R> imageCreator2 = new ImageCreator2<G, A, R>(globalSettings, imageCreator);
        spellUtils = spellFile != null ? new SpellsUtils(spellFile) : null;
        appPreferencesModel = editorFactory.createAppPreferencesModel();
        final MapViewManager<G, A, R> mapViewManager = new MapViewManager<G, A, R>();
        statusBar = new StatusBar<G, A, R>(mapManager, mapViewManager, archetypeSet, faceObjects);
        final MapImageCache<G, A, R> mapImageCache = new MapImageCache<G, A, R>(mapManager, systemIcons.getDefaultIcon(), systemIcons.getDefaultPreview(), rendererFactory, cacheFiles);
        final MapDesktop<G, A, R> mapDesktop = new MapDesktop<G, A, R>(mapViewManager, mapManager, mapImageCache, mapViewsManager);
        final MapFolderTree<G, A, R> mapFolderTree = new MapFolderTree<G, A, R>(globalSettings.getPickmapDir());
        final ImageIcon icon = systemIcons.getAppIcon();
        mainViewFrame = new JFrame(ACTION_BUILDER.format("mainWindow.title", getBuildNumberAsString()));
        newMapDialogFactory = editorFactory.newNewMapDialogFactory(mapViewsManager, mapArchObjectFactory, mainViewFrame);
        final PickmapChooserModel<G, A, R> pickmapChooserModel = new PickmapChooserModel<G, A, R>();
        final PickmapSettings pickmapSettings = new DefaultPickmapSettings();
        pickmapChooserControl = new PickmapChooserControl<G, A, R>(pickmapChooserModel, pickmapSettings, newMapDialogFactory, mapArchObjectFactory, mapReaderFactory, mapFolderTree, mapManager, mainViewFrame, pickmapManager, mapViewsManager);
        newMapDialogFactory.setPickmapChooserControl(pickmapChooserControl);
        final CFTreasureListTree treasureListTree = new CFTreasureListTree(treasureTree, mainViewFrame, archetypeSet, faceObjectProviders, systemIcons);
        final ImageIcon noFaceSquareIcon = systemIcons.getNoFaceSquareIcon();
        final ImageIcon unknownSquareIcon = systemIcons.getUnknownSquareIcon();
        final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory = new GameObjectAttributesDialogFactory<G, A, R>(archetypeTypeSet, mainViewFrame, treasureListTree, faceObjectProviders, animationObjects, globalSettings, mapFileFilter, scriptFileFilter, faceObjects, gameObjectSpells, numberSpells, undefinedSpellIndex, treasureTree, noFaceSquareIcon, unknownSquareIcon, mapManager);
        final DefaultObjectChooser<G, A, R> objectChooser = new DefaultObjectChooser<G, A, R>(archetypeChooserControl, pickmapChooserControl, archetypeChooserModel, pickmapChooserModel, archetypeTypeSet);
        newMapDialogFactory.setObjectChooser(objectChooser);
        final SelectedSquareModel<G, A, R> selectedSquareModel = new SelectedSquareModel<G, A, R>();
        final SelectedSquareActions<G, A, R> selectedSquareActions = new SelectedSquareActions<G, A, R>(selectedSquareModel);
        final SelectedSquareView<G, A, R> selectedSquareView = new SelectedSquareView<G, A, R>(selectedSquareModel, selectedSquareActions, gameObjectAttributesDialogFactory, objectChooser, mapViewManager, mapViewSettings, compassIcon, faceObjectProviders, unknownSquareIcon);
        final GameObjectMatcher floorMatcher = gameObjectMatchers.getMatcher("system_floor", "floor");
        final GameObjectMatcher wallMatcher = gameObjectMatchers.getMatcher("system_wall", "wall");
        final ErrorViewCollector gameObjectMatchersErrorViewCollector = new ErrorViewCollector(errorView, new File(globalSettings.getConfigurationDirectory(), "GameObjectMatchers.xml"));
        final GameObjectMatcher belowFloorMatcher = gameObjectMatchers.getMatcherWarn(gameObjectMatchersErrorViewCollector, "system_below_floor", "below_floor");
        final GameObjectMatcher systemObjectMatcher = gameObjectMatchers.getMatcher("system_system_object");
        final InsertionModeSet<G, A, R> insertionModeSet = new InsertionModeSet<G, A, R>(topmostInsertionMode, floorMatcher, wallMatcher, belowFloorMatcher, systemObjectMatcher);
        final CopyBuffer<G, A, R> copyBuffer = new CopyBuffer<G, A, R>(mapViewSettings, gameObjectFactory, mapArchObjectFactory, mapModelFactory, insertionModeSet);
        final FindDialogManager<G, A, R> findDialogManager = new FindDialogManager<G, A, R>(mainViewFrame, mapViewManager);
        final ReplaceDialogManager<G, A, R> replaceDialogManager = new ReplaceDialogManager<G, A, R>(mainViewFrame, copyBuffer, objectChooser, mapViewManager, faceObjectProviders, insertionModeSet);
        exiter = new DefaultExiter(mainViewFrame);
        scriptEditControl = new ScriptEditControl(scriptFileFilter, scriptExtension, mainViewFrame, globalSettings.getMapsDirectory(), preferences, exiter);
        final TextAreaDefaults textAreaDefaults = new TextAreaDefaults(scriptEditControl);
        gameObjectAttributesDialogFactory.setTextAreaDefaults(textAreaDefaults);
        scriptEditControl.setTextAreaDefaults(textAreaDefaults);
        scriptedEventEditor.setScriptEditControl(scriptEditControl);
        scriptArchEditor.setScriptEditControl(scriptEditControl);
        fileControl = new DefaultFileControl<G, A, R>(globalSettings, mapImageCache, mapManager, mapViewsManager, mainViewFrame, GuiFileFilters.mapFileFilter, scriptFileFilter, newMapDialogFactory, scriptExtension, scriptEditControl);
        pickmapChooserControl.setFileControl(fileControl);
        mapViewsManager.setFileControl(fileControl);
        final GameObjectMatcher monsterMatcherTmp = gameObjectMatchers.getMatcherWarn(gameObjectMatchersErrorViewCollector, "system_monster", "monster");
        final GameObjectMatcher monsterMatcher = monsterMatcherTmp == null ? new TypeNrsGameObjectMatcher() : monsterMatcherTmp;
        final GameObjectMatcher exitGameObjectMatcherTmp = gameObjectMatchers.getMatcherWarn(gameObjectMatchersErrorViewCollector, "system_exit", "exit");
        final GameObjectMatcher exitGameObjectMatcher = exitGameObjectMatcherTmp == null ? new TypeNrsGameObjectMatcher() : exitGameObjectMatcherTmp;
        final ExitMatcher<G, A, R> exitMatcher = new ExitMatcher<G, A, R>(exitGameObjectMatcher);
        final MapPathNormalizer mapPathNormalizer = new MapPathNormalizer(globalSettings);
        final MapPropertiesDialogFactory<G, A, R> mapPropertiesDialogFactory = editorFactory.newMapPropertiesDialogFactory(globalSettings, mapManager, mapPathNormalizer);
        final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager = new DelayedMapModelListenerManager<G, A, R>(mapManager, exiter);
        final Control<?, G, A, R> lockedItemsControl = new LockedItemsControl<G, A, R>(mapViewManager, delayedMapModelListenerManager, lockedItemsTypeNumbers);
        final EnterMap<G, A, R> enterMap = new EnterMap<G, A, R>(mainViewFrame, directionMap, mapPathNormalizer, fileControl, mapViewsManager);
        new MapActions<G, A, R>(mainViewFrame, mapManager, mapViewManager, exitMatcher, GuiFileFilters.mapFileFilter, selectedSquareModel, allowRandomMapParameters, mapPropertiesDialogFactory, mapViewSettings, mapViewsManager, enterMap);
        final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel = new GameObjectAttributesModel<G, A, R>();
        final GameObjectAttributesControl<G, A, R> gameObjectAttributesControl = new GameObjectAttributesControl<G, A, R>(gameObjectAttributesModel, gameObjectAttributesDialogFactory, objectChooser, mapManager, selectedSquareModel, selectedSquareView, gameObjectFactory);
        final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory = new PluginParameterViewFactory<G, A, R>(archetypeSet, gameObjectAttributesModel, objectChooser, mapManager, faceObjectProviders);
        final File scriptsFile = new File(globalSettings.getMapsDirectory(), scriptsDir);
        pluginControl = new PluginController<G, A, R>(filterControl, pluginParameters, mainViewFrame, pluginParameterViewFactory, scriptsFile, pluginModel, pluginParameterFactory, pluginExecutor, systemIcons);
        final ToolPalette<G, A, R> toolPalette = new ToolPalette<G, A, R>(mapViewSettings, selectedSquareView, selectedSquareModel, objectChooser, pickmapSettings, floorMatcher, wallMatcher, monsterMatcher, insertionModeSet);
        updaterManager = new UpdaterManager(exiter, mapManager, mainViewFrame, gridartaJarFilename);

        final TextEditorTab<G, A, R> textEditorTab = new TextEditorTab<G, A, R>(gameObjectAttributesModel, archetypeTypeSet);
        final GameObjectTab<G, A, R> gameObjectTab = new GameObjectTab<G, A, R>("gameObject", gameObjectAttributesControl, Location.BOTTOM, false, 0, true);

        //noinspection ResultOfObjectAllocationIgnored
        new About(mainViewFrame);
        //noinspection ResultOfObjectAllocationIgnored
        new FindArchetypesDialogManager<G, A, R>(mainViewFrame, archetypeChooserControl, objectChooser, archetypeTypeSet);
        //noinspection ResultOfObjectAllocationIgnored
        new UndoControl<G, A, R>(mapManager, gameObjectFactory, gameObjectMatchers);
        final Action exitAction = ActionUtils.newAction(ACTION_BUILDER, "Other", this, "exit");
        final MapFolderTreeActions<G, A, R> mapFolderTreeActions = new MapFolderTreeActions<G, A, R>(mapFolderTree, pickmapSettings, newMapDialogFactory, "createPickmapFolder", "deletePickmapFolder", "confirmDeletePickmapFolder", "deletePickmapFolderNotEmpty");
        final ViewActions<G, A, R> viewActions = new ViewActions<G, A, R>(mapViewSettings, mapManager);
        //noinspection ResultOfObjectAllocationIgnored
        new MapFileActions<G, A, R>(imageCreator2, mapManager, mapViewsManager, mapViewManager, fileControl, mainViewFrame);
        //noinspection ResultOfObjectAllocationIgnored
        new MainActions<G, A, R>(findDialogManager, replaceDialogManager, mainViewFrame, globalSettings, validators, mapViewSettings, archetypeSet, copyBuffer, objectChooser, mapManager, mapViewManager, resources, faceObjectProviders, insertionModeSet, exiter);
        final HelpActions helpActions = new HelpActions(mainViewFrame);
        ActionUtils.newActions(ACTION_BUILDER, "Map", newMapDialogFactory, "newMap");
        final GoMapDialogManager<G, A, R> goMapDialogManager = new GoMapDialogManager<G, A, R>(mainViewFrame, mapManager, mapViewsManager, globalSettings, exiter);
        ActionUtils.newActions(ACTION_BUILDER, "Map", goMapDialogManager, "goMap");
        final GoExitDialogManager<G, A, R> goExitDialogManager = new GoExitDialogManager<G, A, R>(mainViewFrame, mapManager, mapViewManager, exitGameObjectMatcher, pathManager, enterMap, faceObjectProviders);
        ActionUtils.newActions(ACTION_BUILDER, "Map Navigation", goExitDialogManager, "goExit");
        ActionUtils.newActions(ACTION_BUILDER, "Tool", this, "cleanCompletelyBlockedSquares", "collectSpells", "controlClient", "controlServer", "gc", "options", "shortcuts", "zoom");
        //noinspection ResultOfObjectAllocationIgnored
        new MapCursorActions<G, A, R>(objectChooser, gameObjectAttributesDialogFactory, mapViewManager);
        ActionUtils.newAction(ACTION_BUILDER, "Script", scriptEditControl, "newScript");
        ActionUtils.newAction(ACTION_BUILDER, "Script", fileControl, "editScript");
        ActionUtils.newActions(ACTION_BUILDER, "Map", fileControl, "openFile", "saveAllMaps");
        final Action aCloseAllMaps = ActionUtils.newAction(ACTION_BUILDER, "Map,Window", fileControl, "closeAllMaps");
        mainView = new MainView(mainViewFrame, exitAction, mapDesktop, icon, exiter);
        ActionUtils.newAction(ACTION_BUILDER, "Tool", new MainViewActions<G, A, R>(mainView, gameObjectAttributesControl, gameObjectTab, textEditorTab), "gameObjectTextEditor");
        final BookmarksMapMenuPreferences bookmarksMapMenuPreferences = new BookmarksMapMenuPreferences();
        final MapMenu mapMenu = bookmarksMapMenuPreferences.getMapMenu();
        @Nullable final MapMenuManager<G, A, R> bookmarksMapMenuManager = new MapMenuManager<G, A, R>(mapMenu, mapViewsManager, fileControl, mapImageCache);
        //noinspection ResultOfObjectAllocationIgnored
        new BookmarkActions<G, A, R>(bookmarksMapMenuPreferences, mapMenu, mapViewManager, mainViewFrame, mapImageCache);
        editorFactory.newServerActions(mapViewManager, fileControl, pathManager);

        pickmapChooserControl.setPopupMenu(ACTION_BUILDER.createPopupMenu(true, "pickmaps"));
        final JMenuBar menuBar = ACTION_BUILDER.createMenuBar(true, "main");
        final MainToolbar mainToolbar = new MainToolbar(globalSettings);
        mainViewFrame.setJMenuBar(menuBar);
        mainViewFrame.add(mainToolbar.getComponent(), BorderLayout.NORTH);
        mainViewFrame.add(statusBar, BorderLayout.SOUTH);
        mainView.addTab(gameObjectTab);
        mainView.addTab(new Tab("selectedSquare", selectedSquareView, Location.RIGHT, false, 1, true));
        mainView.addTab(new Tab("tools", toolPalette, Location.LEFT, false, 2, false));
        mainView.addTab(new Tab("objects", objectChooser, Location.LEFT, false, 3, true));
        exitConnectorModel = new DefaultExitConnectorModel(); // XXX: should be part of DefaultMainControl
        final ExitConnectorActions<G, A, R> exitConnectorActions = new ExitConnectorActions<G, A, R>(exitConnectorModel, exitMatcher, archetypeSet, mapManager, fileControl, pathManager, insertionModeSet); // XXX: should be part of DefaultMainControl
        //noinspection ResultOfObjectAllocationIgnored
        new ExitConnectorController<G, A, R>(exitConnectorActions, exitConnectorModel, mapViewManager);
        final JMenu windowMenu = MenuUtils.getMenu(menuBar, "window");
        if (windowMenu == null) {
            log.warn("'main' menu bar does not define 'window' menu.");
        } else {
            //noinspection ResultOfObjectAllocationIgnored
            new WindowMenuManager<G, A, R>(windowMenu, mapViewManager, aCloseAllMaps, mapDesktop);
        }
        final JMenu pickmapFoldersMenu = MenuUtils.getMenu(menuBar, "pickmapFolders");
        if (pickmapFoldersMenu == null) {
            log.warn("'main' menu bar does not define 'pickmapFolders' menu.");
        } else {
            mapFolderTreeActions.setPickmapFoldersMenu(pickmapFoldersMenu);
        }
        viewActions.init(gameObjectMatchers.getFilters());
        final Container viewMenu = MenuUtils.getMenu(menuBar, "view");
        if (viewMenu == null) {
            log.warn("'main' menu bar does not define 'view' menu.");
        } else {
            viewActions.setMenu(viewMenu);
        }
        final JMenu pluginsMenu = MenuUtils.getMenu(menuBar, "plugins");
        if (pluginsMenu == null) {
            log.warn("'main' menu bar does not define 'plugins' menu.");
        } else {
            pluginControl.getView().setMenu(pluginsMenu);
        }
        final ErrorListView<G, A, R> errorListView = new ErrorListView<G, A, R>(mapViewManager);
        gameObjectAttributesControl.addTab(new ArchTab<G, A, R>(archetypeTypeSet, gameObjectAttributesModel));
        gameObjectAttributesControl.addTab(new MsgTextTab<G, A, R>(gameObjectAttributesModel));
        gameObjectAttributesControl.addTab(new EventsTab<G, A, R>(mainViewFrame, mapManager, gameObjectAttributesModel, scriptArchEditor, scriptArchData, scriptArchDataUtils, scriptArchUtils));
        gameObjectAttributesControl.addTab(new FaceTab<G, A, R>(gameObjectAttributesModel, faceObjects, faceObjectProviders, animationObjects, noFaceSquareIcon, unknownSquareIcon));
        gameObjectAttributesControl.addTab(textEditorTab);
        mainView.addTab(new Tab("monsters", new MonsterControl<G, A, R>(mapViewManager, delayedMapModelListenerManager, monsterMatcher).getView(), Location.BOTTOM, false, 4, false));
        mainView.addTab(new Tab("connections", new ConnectionControl<G, A, R>(mapViewManager, delayedMapModelListenerManager).getView(), Location.BOTTOM, false, 5, false));
        mainView.addTab(new Tab("lockedItems", lockedItemsControl.getView(), Location.BOTTOM, false, 6, false));
        mainView.addTab(new WarningsTab<G, A, R>("warnings", errorListView, Location.BOTTOM, false, 7, false));
        mainView.addTab(new GameObjectTextEditorTab<G, A, R>("textEditor", new GameObjectTextEditor(archetypeTypeSet), Location.RIGHT, true, 8, false, selectedSquareModel, mapManager));

        new ArchetypeValidator(animationObjects, faceObjects, errorView).validate(archetypeSet);
        new AnimationValidator(faceObjects, errorView).validate(animationObjects);

        if (globalSettings.isAutoPopupDocumentation()) {
            // do an automated help popup because the documentation version has increased
            // (people won't notice the documentation otherwise - nobody expects a documentation in open source)
            helpActions.showHelp();
            globalSettings.setAutoPopupDocumentation(false);
        }

        final MapMenuPreferences recentMapMenuPreferences = new RecentMapMenuPreferences();
        final MapMenuManager<G, A, R> recentMapMenuManager = new MapMenuManager<G, A, R>(recentMapMenuPreferences.getMapMenu(), mapViewsManager, fileControl, mapImageCache);
        final JMenu recentMenu = MenuUtils.getMenu(menuBar, "recent");
        if (recentMenu == null) {
            log.warn("'main' menu bar does not define 'recent' menu.");
        } else {
            recentMapMenuManager.setMenu(recentMenu);
        }

        final JMenu bookmarksMenu = MenuUtils.getMenu(menuBar, "bookmarks");
        if (bookmarksMenu == null) {
            log.warn("'main' menu bar does not define 'bookmarks' menu.");
        } else {
            bookmarksMapMenuManager.setMenu(bookmarksMenu);
        }

        final JMenu analyzeMenu = MenuUtils.getMenu(menuBar, "analyze");
        if (analyzeMenu == null) {
            log.warn("'main' menu bar does not define 'analyze' menu.");
        } else {
            filterControl.createMenuEntries(analyzeMenu);
        }
        final MapUserListenerManager<G, A, R> mapUserListenerManager = new MapUserListenerManager<G, A, R>(toolPalette, mapViewsManager);
        mapUserListenerManager.addMapManager(mapManager);
        mapUserListenerManager.addMapManager(pickmapManager);

        new MapKeyListener<G, A, R>(mapViewManager, mapManager);

        new Loader<G, A, R>(errorView, mapFolderTree, mapReaderFactory, pickmapManager, mapViewsManager).load();
        if (!pickmapChooserModel.isEmpty()) {
            objectChooser.movePickmapChooserToFront();
        }
        recentMapMenuManager.initRecent();
        if (bookmarksMapMenuManager != null) {
            bookmarksMapMenuManager.initRecent();
        }

        //noinspection ResultOfObjectAllocationIgnored
        new RecentManager<G, A, R>(mapManager, recentMapMenuPreferences);

        //noinspection ResultOfObjectAllocationIgnored
        new AutoValidator<G, A, R>(validators, autoValidatorDefault, delayedMapModelListenerManager);
        mapManager.setFileControl(fileControl);
        shortcutsManager = new ShortcutsManager(ACTION_BUILDER);
        shortcutsManager.loadShortcuts();
        delayedMapModelListenerManager.start();
    }

    /**
     * Returns the application's build number as a string.
     * @return the build number or <code>"unknown version"</code> if not
     *         available
     */
    @NotNull
    private static String getBuildNumberAsString() {
        try {
            return ResourceBundle.getBundle("build").getString("build.number");
        } catch (final MissingResourceException ignored) {
            return "unknown version";
        }
    }

    /**
     * The action method for "gc". Runs the garbage collection.
     */
    @ActionMethod
    public void gc() {
        //noinspection CallToSystemGC
        System.gc();
        System.runFinalization();
        statusBar.setStatusText("Garbage collection - done.");
    }

    /**
     * The action method for "zoom". Opens the dialog to create zoomed images of
     * the current map.
     */
    @ActionMethod
    public void zoom() {
        final MapControl<G, A, R> mapControl = mapManager.getCurrentMap();
        if (mapControl == null) {
            JOptionPane.showMessageDialog(mainViewFrame, "No map loaded! Please load a map!!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new MapPreview(rendererFactory.newSimpleMapRenderer(mapControl.getMapModel()).getFullImage());
    }

    /**
     * The action method for "options". Opens the options dialog.
     */
    @ActionMethod
    public void options() {
        if (preferencesGroup == null) {
            preferencesGroup = editorFactory.createPreferencesGroup(globalSettings, validators, appPreferencesModel, exitConnectorModel, configSourceFactory);
        }
        PreferencesPane.showPreferencesDialog(mainViewFrame, preferencesGroup, false);
    }

    /**
     * The action method for "shortcuts". Opens the dialog to configure keyboard
     * shortcuts.
     */
    @ActionMethod
    public void shortcuts() {
        shortcutsManager.showShortcutsDialog(mainViewFrame);
    }

    /**
     * The action method for "exit". Invoked when user wants to exit from the
     * program.
     */
    @ActionMethod
    public void exit() {
        if (canExit()) {
            exiter.doExit(0);
        }
    }

    /**
     * Prepares existing the application: save modified data (possibly ask the
     * user if applicable).
     * @return whether all modified data has been saved
     */
    private boolean canExit() {
        return scriptEditControl.closeAllTabs() && fileControl.closeAllMaps() && pickmapChooserControl.canExit() && pluginControl.canExit();
    }

    /**
     * Loads a map file.
     * @param file the file to load
     * @return the map view of the loaded file or <code>null</code>
     * @throws IOException if an I/O error occurs
     */
    @Nullable
    public MapView<G, A, R> openFile(@NotNull final File file) throws IOException {
        final boolean isScriptFile = file.getName().toLowerCase().endsWith(scriptExtension);
        if (file.isFile()) {
            if (isScriptFile) {
                scriptEditControl.openScriptFile(file.getAbsolutePath());
            } else {
                return mapViewsManager.openMapFileWithView(file, null, null);
            }
        } else if (!file.exists()) {
            if (isScriptFile) {
                // TODO: pass filename
                scriptEditControl.newScript();
            } else {
                newMapDialogFactory.newMap(); // XXX: pass file
            }
        } // If neither branch matches, it's a directory - what to do with directories?

        return null;
    }

    /**
     * The action method for "controlServer". Opens the dialog to control the
     * server.
     */
    @ActionMethod
    public void controlServer() {
        if (controlServer == null) {
            controlServer = new ProcessRunner("controlServer", new String[] { appPreferencesModel.getServer(), });
            ACTION_BUILDER.showOnetimeMessageDialog(mainViewFrame, JOptionPane.WARNING_MESSAGE, "controlServerWarning");
        } else {
            controlServer.setCommand(new String[] { appPreferencesModel.getServer(), });
        }
        controlServer.showDialog(mainViewFrame);
    }

    /**
     * The action method for "controlClient". Opens the dialog to control the
     * client.
     */
    @ActionMethod
    public void controlClient() {
        if (controlClient == null) {
            controlClient = new ProcessRunner("controlClient", new String[] { appPreferencesModel.getClient(), });
        } else {
            controlClient.setCommand(new String[] { appPreferencesModel.getClient(), });
        }
        controlClient.showDialog(mainViewFrame);
    }

    /**
     * Load a list of map files.
     * @param filenames collection of filenames to load
     */
    public void openFiles(final Iterable<String> filenames) {
        for (final String filename : filenames) {
            final File file = new File(filename);
            try {
                openFile(file);
            } catch (final IOException ex) {
                fileControl.reportLoadError(file, ex.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleThrowable(@NotNull final Throwable t) {
        mainView.handleThrowable(t);
    }

    /**
     * The action method for "collectSpells". Opens the dialog to import spell
     * definitions.
     */
    @ActionMethod
    public void collectSpells() {
        if (spellUtils != null) {
            spellUtils.importSpells(globalSettings.getConfigurationDirectory(), mainViewFrame);
        }
    }

    /**
     * The action method for "cleanCompletelyBlockedSquares". Cleans all
     * completely blocked squares of a map.
     * @fixme this implementation does not take care of multi square objects.
     */
    @ActionMethod
    public void cleanCompletelyBlockedSquares() {
        final MapControl<G, A, R> mapControl = mapManager.getCurrentMap();
        if (mapControl == null) {
            return;
        }

        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("cleanCompletelyBlockedSquares"); // TODO: I18N/L10N
        try {
            for (final GameObjectContainer<G, A, R> completelyBlockedSquare : BlockedSquareChecker.findCompletelyBlockedSquares(mapModel)) {
                completelyBlockedSquare.removeAll();
            }
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Starts the editor: makes the main window visible and opens map files.
     * @param args the map files to open
     */
    public void run(@NotNull final Iterable<String> args) {
        mainViewFrame.setVisible(true);
        TipOfTheDayManager.showAtStartup(mainViewFrame);
        updaterManager.startup();

        if (archetypeSet.getArchetypeCount() == 0) {
            ACTION_BUILDER.showMessageDialog(mainViewFrame, "loadArchesNoArchetypes");
        }

        openFiles(args);
    }

}
