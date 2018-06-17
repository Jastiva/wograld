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

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.commands.BatchPngCommand;
import net.sf.gridarta.commands.CollectArchesCommand;
import net.sf.gridarta.commands.SinglePngCommand;
import net.sf.gridarta.gui.dialog.errorview.ConsoleErrorView;
import net.sf.gridarta.gui.dialog.errorview.DefaultErrorView;
import net.sf.gridarta.gui.dialog.prefs.GUIPreferences;
import net.sf.gridarta.gui.filter.DefaultFilterControl;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.gui.scripts.ScriptedEventEditor;
import net.sf.gridarta.mainactions.DefaultExiter;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.DefaultAnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.configsource.ConfigSourceFactory;
import net.sf.gridarta.model.configsource.DefaultConfigSourceFactory;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.DefaultFaceObjects;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.filter.NamedFilter;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.DefaultMapReaderFactory;
import net.sf.gridarta.model.io.DefaultMapWriter;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.GameObjectParserFactory;
import net.sf.gridarta.model.io.MapArchObjectParserFactory;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapcontrol.MapControlFactory;
import net.sf.gridarta.model.mapmanager.AbstractMapManager;
import net.sf.gridarta.model.mapmanager.DefaultMapManager;
import net.sf.gridarta.model.mapmanager.DefaultPickmapManager;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mapmodel.TopmostInsertionMode;
import net.sf.gridarta.model.mapviewsettings.DefaultMapViewSettings;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import net.sf.gridarta.model.validation.DefaultValidatorPreferences;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.plugin.PluginExecException;
import net.sf.gridarta.plugin.PluginExecutor;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginParameters;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.preferences.FilePreferencesFactory;
import net.sf.gridarta.utils.ConfigFileUtils;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.SyntaxErrorException;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Main class of the editor; parses command-line arguments, initializes and
 * starts the editor.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class GridartaEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(GridartaEditor.class);

    /**
     * Creates a new instance.
     * @param tipOfTheDayPackage the package name for tip of the day messages
     */
    public GridartaEditor(@NotNull final String tipOfTheDayPackage) {
        System.setProperty("net.sf.japi.swing.tod", tipOfTheDayPackage);
        try {
            if (log.isInfoEnabled()) {
                log.info("build number: " + ResourceBundle.getBundle("build").getString("build.number"));
            }
        } catch (final MissingResourceException e) {
            log.warn("No build number found:", e);
        }
    }

    /**
     * Runs the editor.
     * @param actionBuilderPackage the package name for creating an action
     * builder
     * @param editorJarName the name of the .jar file containing the executable
     * @param editorFactory the editor factory to use
     * @param defaultConfig the default config file or <code>null</code> to use
     * Java's preferences
     * @param args the command line parameters given to the level editor
     */
    public void run(@NotNull final String actionBuilderPackage, @NotNull final String editorJarName, @NotNull final EditorFactory<G, A, R> editorFactory, @Nullable final String defaultConfig, @NotNull final String... args) {
        boolean err = false; // whether a fatal error (exit required) occurred.
        String plugin = null;
        final LongOpt[] longOpts = { new LongOpt("batchpng", LongOpt.NO_ARGUMENT, null, (int) 'b'), new LongOpt("normal", LongOpt.NO_ARGUMENT, null, (int) 'n'), new LongOpt("singlepng", LongOpt.NO_ARGUMENT, null, (int) 's'), new LongOpt("collectarches", LongOpt.NO_ARGUMENT, null, (int) 'c'), new LongOpt("collectArches", LongOpt.NO_ARGUMENT, null, (int) 'c'), new LongOpt("help", LongOpt.NO_ARGUMENT, null, (int) 'h'), new LongOpt("noexit", LongOpt.NO_ARGUMENT, null, 2), new LongOpt("script", LongOpt.REQUIRED_ARGUMENT, null, 1), new LongOpt("plugin", LongOpt.REQUIRED_ARGUMENT, null, 1), new LongOpt("config", LongOpt.REQUIRED_ARGUMENT, null, 3), };
        final Getopt g = new Getopt(editorJarName, args, "bchns", longOpts);
        GridartaRunMode mode = GridartaRunMode.NORMAL;
        File config = defaultConfig == null ? null : ConfigFileUtils.getHomeFile(defaultConfig);
        boolean doExit = true;
        while (true) {
            final int ch = g.getopt();
            if (ch == -1) {
                break;
            }

            switch (ch) {
            case 'b':
                mode = GridartaRunMode.BATCH_PNG;
                break;

            case 'c':
                mode = GridartaRunMode.COLLECT_ARCHES;
                break;

            case 'h':
                usage(editorJarName, defaultConfig);
                return;

            case 'n':
                mode = GridartaRunMode.NORMAL;
                break;

            case 's':
                mode = GridartaRunMode.SINGLE_PNG;
                break;

            case 1:
                plugin = g.getOptarg();
                break;

            case 2:
                doExit = false;
                break;

            case 3: // --config
                config = new File(g.getOptarg());
                if (!config.exists()) {
                    System.err.println(config + ": configuration file does not exist");
                    err = true;
                }
                break;

            case '?':
            default:
                err = true;
                break;
            }
        }
        int returnCode;
        if (err) {
            returnCode = 1;
        } else {
            if (config != null) {
                System.setProperty("java.util.prefs.PreferencesFactory", "net.sf.gridarta.preferences.FilePreferencesFactory");
                FilePreferencesFactory.initialize("user_net_sf_gridarta", config);
            }

            // Make sure the locale is set before any ActionBuilder is used.
            final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);
            final String locName = preferences.get(GUIPreferences.PREFERENCES_LANGUAGE, null);
            if (locName != null) {
                Locale.setDefault(new Locale(locName));
            }

            final List<String> args2 = Arrays.asList(args).subList(g.getOptind(), args.length);

            // print jre version, for easier recognition of jre-specific problems
            System.err.println("Running java version " + System.getProperty("java.version"));
            // Now add preferences to the ActionBuilder.
            final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");
            actionBuilder.addPref(DefaultMainControl.class);
            actionBuilder.addParent(ActionBuilderFactory.getInstance().getActionBuilder(actionBuilderPackage));

            // Create the application and give it the parameters
            final ErrorView errorView = GraphicsEnvironment.isHeadless() || mode.isConsoleMode() || plugin != null ? new ConsoleErrorView() : new DefaultErrorView(null);
            final GUIUtils guiUtils = new GUIUtils();
            final SystemIcons systemIcons = new SystemIcons(guiUtils);
            final ConfigSourceFactory configSourceFactory = new DefaultConfigSourceFactory();
            final MapViewSettings mapViewSettings = new DefaultMapViewSettings();
            final GlobalSettings globalSettings = editorFactory.newGlobalSettings();
            final PathManager pathManager = new PathManager(globalSettings);
            final GameObjectMatchers gameObjectMatchers = new GameObjectMatchers();
            final FaceObjects faceObjects = new DefaultFaceObjects(editorFactory.getIncludeFaceNumbers());
            editorFactory.initSmoothFaces(faceObjects);
            final int doubleFaceOffset = editorFactory.getDoubleFaceOffset();
            final FaceObjectProviders faceObjectProviders = new FaceObjectProviders(doubleFaceOffset, faceObjects, systemIcons);
            final ArchetypeTypeSet archetypeTypeSet = new ArchetypeTypeSet();
            final AnimationObjects animationObjects = new DefaultAnimationObjects();
            final GameObjectFactory<G, A, R> gameObjectFactory = editorFactory.newGameObjectFactory(faceObjectProviders, animationObjects, archetypeTypeSet);
            final ArchetypeFactory<G, A, R> archetypeFactory = editorFactory.newArchetypeFactory(faceObjectProviders, animationObjects);
            final ArchetypeSet<G, A, R> archetypeSet = editorFactory.newArchetypeSet(globalSettings, archetypeFactory);
            faceObjectProviders.addFaceObjectProvidersListener(new FaceObjectProvidersListener() {

                @Override
                public void facesReloaded() {
                    archetypeSet.connectFaces();
                }

            });
            final GameObjectParserFactory<G, A, R> gameObjectParserFactory = editorFactory.newGameObjectParserFactory(gameObjectFactory, archetypeSet);
            final GameObjectParser<G, A, R> gameObjectParser = gameObjectParserFactory.newGameObjectParser();
            final MapArchObjectFactory<A> mapArchObjectFactory = editorFactory.newMapArchObjectFactory(globalSettings);
            final MapArchObjectParserFactory<A> mapArchObjectParserFactory = editorFactory.newMapArchObjectParserFactory();
            final DefaultMapReaderFactory<G, A, R> mapReaderFactory = new DefaultMapReaderFactory<G, A, R>(mapArchObjectFactory, mapArchObjectParserFactory, gameObjectParserFactory, mapViewSettings);
            final ArchetypeChooserModel<G, A, R> archetypeChooserModel = new ArchetypeChooserModel<G, A, R>();
            final InsertionMode<G, A, R> topmostInsertionMode = new TopmostInsertionMode<G, A, R>();
            final AutojoinLists<G, A, R> autojoinLists = new AutojoinLists<G, A, R>(mapViewSettings);
            final MapWriter<G, A, R> mapWriter = new DefaultMapWriter<G, A, R>(mapArchObjectParserFactory, gameObjectParser);
            final MapModelFactory<G, A, R> mapModelFactory = new MapModelFactory<G, A, R>(archetypeChooserModel, autojoinLists, mapViewSettings, gameObjectFactory, gameObjectMatchers, topmostInsertionMode);
            final MapControlFactory<G, A, R> mapControlFactory = editorFactory.newMapControlFactory(mapWriter, globalSettings, mapModelFactory);
            final AbstractMapManager<G, A, R> mapManager = new DefaultMapManager<G, A, R>(mapReaderFactory, mapControlFactory, globalSettings, faceObjectProviders);
            final MapManager<G, A, R> pickmapManager = new DefaultPickmapManager<G, A, R>(mapReaderFactory, mapControlFactory, globalSettings, faceObjectProviders);
            final PluginModel<G, A, R> pluginModel = new PluginModel<G, A, R>();
            final ValidatorPreferences validatorPreferences = new DefaultValidatorPreferences();
            final DelegatingMapValidator<G, A, R> validators = new DelegatingMapValidator<G, A, R>(validatorPreferences);
            final ScriptedEventEditor<G, A, R> scriptedEventEditor = new ScriptedEventEditor<G, A, R>(globalSettings);
            final NamedFilter defaultFilterList = new NamedFilter(Collections.<NamedGameObjectMatcher>emptyList());
            final AbstractArchetypeParser<G, A, R, ?> archetypeParser = editorFactory.newArchetypeParser(errorView, gameObjectParser, animationObjects, archetypeSet, gameObjectFactory, globalSettings);
            final ArchFaceProvider archFaceProvider = new ArchFaceProvider();
            final AbstractResources<G, A, R> resources = editorFactory.newResources(gameObjectParser, archetypeSet, archetypeParser, mapViewSettings, faceObjects, animationObjects, archFaceProvider, faceObjectProviders);
            final Spells<NumberSpell> numberSpells = new Spells<NumberSpell>();
            final Spells<GameObjectSpell<G, A, R>> gameObjectSpells = new Spells<GameObjectSpell<G, A, R>>();
            final PluginParameterFactory<G, A, R> pluginParameterFactory = new PluginParameterFactory<G, A, R>(archetypeSet, mapManager, defaultFilterList, globalSettings);
            final DefaultMainControl<G, A, R> mainControl = editorFactory.newMainControl(mode == GridartaRunMode.COLLECT_ARCHES, errorView, globalSettings, configSourceFactory, pathManager, gameObjectMatchers, gameObjectFactory, archetypeTypeSet, archetypeSet, archetypeChooserModel, autojoinLists, mapManager, pluginModel, validators, scriptedEventEditor, resources, numberSpells, gameObjectSpells, pluginParameterFactory, validatorPreferences, mapWriter);

            final NamedFilter defaultNamedFilterList = new NamedFilter(gameObjectMatchers.getFilters());
            final FilterControl<G, A, R> filterControl = new DefaultFilterControl<G, A, R>(defaultNamedFilterList);
            final RendererFactory<G, A, R> rendererFactory = editorFactory.newRendererFactory(mapViewSettings, filterControl, gameObjectParser, faceObjectProviders, systemIcons);

            final PluginParameters pluginParameters = new PluginParameters();
            pluginParameters.addPluginParameter("archetypeSet", archetypeSet);
            pluginParameters.addPluginParameter("globalSettings", globalSettings);
            pluginParameters.addPluginParameter("mapManager", mapManager);
            pluginParameters.addPluginParameter("validators", validators);
            pluginParameters.addPluginParameter("rendererFactory", rendererFactory);
            pluginParameters.addPluginParameter("gameObjectMatchers", gameObjectMatchers);
            final PluginExecutor<G, A, R> pluginExecutor = new PluginExecutor<G, A, R>(pluginModel, pluginParameters);

            if (plugin != null) {
                returnCode = runPlugin(plugin, errorView, args2, pluginExecutor);
            } else {
                try {
                    switch (mode) {
                    case NORMAL:
                        returnCode = runNormal(args2, mainControl, editorFactory, errorView, guiUtils, configSourceFactory, rendererFactory, filterControl, pluginExecutor, pluginParameters, mapManager, pickmapManager, mapModelFactory, archetypeSet, faceObjects, globalSettings, mapViewSettings, faceObjectProviders, pathManager, topmostInsertionMode, gameObjectFactory, systemIcons, archetypeTypeSet, mapArchObjectFactory, mapReaderFactory, validators, gameObjectMatchers, pluginModel, animationObjects, archetypeChooserModel, scriptedEventEditor, resources, numberSpells, gameObjectSpells, pluginParameterFactory);
                        break;

                    case BATCH_PNG:
                        returnCode = new BatchPngCommand(args2, new ImageCreatorFactory<G, A, R>(systemIcons).newImageCreator(editorFactory, faceObjectProviders, animationObjects, archetypeSet)).execute();
                        break;

                    case SINGLE_PNG:
                        if (args2.size() != 2) {
                            throw new SyntaxErrorException("input output");
                        }

                        returnCode = new SinglePngCommand(new File(args2.get(0)), new File(args2.get(1)), new ImageCreatorFactory<G, A, R>(systemIcons).newImageCreator(editorFactory, faceObjectProviders, animationObjects, archetypeSet)).execute();
                        break;

                    case COLLECT_ARCHES:
                        returnCode = new CollectArchesCommand(resources, globalSettings).execute();
                        break;

                    default:
                        assert false;
                        returnCode = 1;
                        break;
                    }
                } catch (final SyntaxErrorException ex) {
                    System.err.println("Usage: " + ex.getMessage());
                    returnCode = 1;
                }
            }
        }
        if (doExit && (mode != GridartaRunMode.NORMAL || returnCode != 0)) {
            System.exit(returnCode);
        }
    }

    /**
     * Executes a plugin.
     * @param plugin the name of the plugin to run
     * @param errorView the error view to add errors to
     * @param args the arguments for the plugin
     * @param pluginExecutor the plugin executor to use
     * @return return code suitable for passing to {@link System#exit(int)}
     */
    private int runPlugin(@NotNull final String plugin, final ErrorView errorView, final Iterable<String> args, @NotNull final PluginExecutor<G, A, R> pluginExecutor) {
        checkForErrors(errorView);
        waitDialog(errorView);

        try {
            pluginExecutor.executePlugin(plugin, args);
        } catch (final PluginExecException ex) {
            System.err.println(ex.getMessage());
            return 1;
        }

        return 0;
    }

    /**
     * Checks whether a {@link ErrorView} instance contains at least one error.
     * Quits the application if an error exists.
     * @param errorView the error view to check
     */
    private static void checkForErrors(@NotNull final ErrorView errorView) {
        if (errorView.hasErrors()) {
            waitDialog(errorView);
            DefaultExiter.callExit(1);
            throw new AssertionError();
        }
    }

    /**
     * Waits until the error dialog has been dismissed.
     * @param errorView the error view instance
     */
    private static void waitDialog(final ErrorView errorView) {
        try {
            errorView.waitDialog();
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            DefaultExiter.callExit(1);
            throw new AssertionError(ex);
        }
    }

    /**
     * Run in normal mode.
     * @param args the files to open
     * @param mainControl the main control to use
     * @param editorFactory the editor factory to use
     * @param errorView the error view to add errors to
     * @param guiUtils the gui utils to use
     * @param configSourceFactory the config source factory to use
     * @param rendererFactory the renderer factory to use
     * @param filterControl the filter control to use
     * @param pluginExecutor the plugin executor to use
     * @param pluginParameters the plugin parameters to use
     * @param mapManager the map manager to use
     * @param pickmapManager the pickmap manager to use
     * @param mapModelFactory the map model factory to use
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
     * @param pluginModel the plugin model
     * @param animationObjects the animation objects
     * @param archetypeChooserModel the archetype chooser model
     * @param scriptedEventEditor the scripted event editor
     * @param resources the resources
     * @param numberSpells the number spells to use
     * @param gameObjectSpells the game object spells to use
     * @param pluginParameterFactory the plugin parameter factory to use
     * @return return code suitable for passing to {@link System#exit(int)}
     */
    private int runNormal(@NotNull final Iterable<String> args, @NotNull final DefaultMainControl<G, A, R> mainControl, @NotNull final EditorFactory<G, A, R> editorFactory, @NotNull final ErrorView errorView, @NotNull final GUIUtils guiUtils, @NotNull final ConfigSourceFactory configSourceFactory, @NotNull final RendererFactory<G, A, R> rendererFactory, @NotNull final FilterControl<G, A, R> filterControl, @NotNull final PluginExecutor<G, A, R> pluginExecutor, @NotNull final PluginParameters pluginParameters, @NotNull final AbstractMapManager<G, A, R> mapManager, @NotNull final MapManager<G, A, R> pickmapManager, @NotNull final MapModelFactory<G, A, R> mapModelFactory, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final FaceObjects faceObjects, @NotNull final GlobalSettings globalSettings, @NotNull final MapViewSettings mapViewSettings, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final PathManager pathManager, @NotNull final InsertionMode<G, A, R> topmostInsertionMode, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final SystemIcons systemIcons, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final DefaultMapReaderFactory<G, A, R> mapReaderFactory, @NotNull final DelegatingMapValidator<G, A, R> validators, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull final ScriptedEventEditor<G, A, R> scriptedEventEditor, @NotNull final AbstractResources<G, A, R> resources, @NotNull final Spells<NumberSpell> numberSpells, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory) {
        final GUIMainControl<?, ?, ?>[] guiMainControl = new GUIMainControl<?, ?, ?>[1];
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                guiMainControl[0] = editorFactory.createGUIMainControl(mainControl, errorView, guiUtils, configSourceFactory, rendererFactory, filterControl, pluginExecutor, pluginParameters, mapManager, pickmapManager, mapModelFactory, archetypeSet, faceObjects, globalSettings, mapViewSettings, faceObjectProviders, pathManager, topmostInsertionMode, gameObjectFactory, systemIcons, archetypeTypeSet, mapArchObjectFactory, mapReaderFactory, validators, gameObjectMatchers, pluginModel, animationObjects, archetypeChooserModel, scriptedEventEditor, resources, numberSpells, gameObjectSpells, pluginParameterFactory);
            }

        };
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (final InterruptedException ex) {
            log.fatal(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
            return 1;
        } catch (final InvocationTargetException ex) {
            log.fatal(ex.getMessage(), ex);
            return 1;
        }

        checkForErrors(errorView);
        waitDialog(errorView);

        final Runnable runnable2 = new Runnable() {

            @Override
            public void run() {
                guiMainControl[0].run(args);
            }

        };
        try {
            SwingUtilities.invokeAndWait(runnable2);
        } catch (final InterruptedException ex) {
            log.fatal(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
            return 1;
        } catch (final InvocationTargetException ex) {
            log.fatal(ex.getMessage(), ex);
            return 1;
        }

        return 0;
    }

    /**
     * Prints the editor's command-line options to {@link System#out}.
     * @param editorJarName the name of the editor's .jar file
     * @param defaultConfig the default config file or <code>null</code> for
     * Java preferences
     */
    private static void usage(@NotNull final String editorJarName, @Nullable final String defaultConfig) {
        System.out.println("usage: java -jar " + editorJarName + " [option...] [map-file...]");
        System.out.println("");
        System.out.println(" -h, --help           print this help");
        System.out.println(" -b, --batchpng       create PNG files for all given maps in their directories");
        System.out.println(" -c, --collectarches  collect archetypes");
        System.out.println(" -n, --normal         start editor with GUI (default)");
        System.out.println(" -s, --singlepng      create a PNG file from the specified map");
        System.out.println(" --config=config-file use given config file; uses " + (defaultConfig == null ? "Java preferences" : "~/.gridarta/" + defaultConfig) + " is not given");
        System.out.println(" --noexit             do not call System.exit()");
        System.out.println(" --plugin=name [arg=value...]");
        System.out.println("                    run a plugin with the given arguments");
    }

}
