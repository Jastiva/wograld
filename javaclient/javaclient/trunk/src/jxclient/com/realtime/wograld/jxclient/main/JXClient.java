/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.main;

import com.realtime.wograld.jxclient.account.CharacterModel;
import com.realtime.wograld.jxclient.commands.ListQuestsCommand;
import com.realtime.wograld.jxclient.commands.QuestInfoCommand;
import com.realtime.wograld.jxclient.commands.BindCommand;
import com.realtime.wograld.jxclient.commands.ClearCommand;
import com.realtime.wograld.jxclient.commands.Commands;
import com.realtime.wograld.jxclient.commands.DebugMessagesCommand;
import com.realtime.wograld.jxclient.commands.ExecCommand;
import com.realtime.wograld.jxclient.commands.Macros;
import com.realtime.wograld.jxclient.commands.ScreenshotCommand;
import com.realtime.wograld.jxclient.commands.ScriptCommand;
import com.realtime.wograld.jxclient.commands.ScriptkillCommand;
import com.realtime.wograld.jxclient.commands.ScriptkillallCommand;
import com.realtime.wograld.jxclient.commands.ScriptsCommand;
import com.realtime.wograld.jxclient.commands.ScripttellCommand;
import com.realtime.wograld.jxclient.commands.SetCommand;
import com.realtime.wograld.jxclient.commands.UnbindCommand;
import com.realtime.wograld.jxclient.faces.DefaultFacesManager;
import com.realtime.wograld.jxclient.faces.FaceCache;
import com.realtime.wograld.jxclient.faces.FacesManager;
import com.realtime.wograld.jxclient.faces.FacesQueue;
import com.realtime.wograld.jxclient.faces.FileCache;
import com.realtime.wograld.jxclient.faces.SmoothFaces;
import com.realtime.wograld.jxclient.gui.commands.GUICommandFactory;
import com.realtime.wograld.jxclient.gui.commands.ScreenshotFiles;
import com.realtime.wograld.jxclient.gui.gui.GuiFactory;
import com.realtime.wograld.jxclient.gui.gui.JXCWindowRenderer;
import com.realtime.wograld.jxclient.gui.gui.MouseTracker;
import com.realtime.wograld.jxclient.gui.gui.TooltipManager;
import com.realtime.wograld.jxclient.gui.keybindings.KeyBindings;
import com.realtime.wograld.jxclient.guistate.GuiState;
import com.realtime.wograld.jxclient.guistate.GuiStateManager;
import com.realtime.wograld.jxclient.items.FloorView;
import com.realtime.wograld.jxclient.items.InventoryComparator;
import com.realtime.wograld.jxclient.items.InventoryView;
import com.realtime.wograld.jxclient.items.ItemSet;
import com.realtime.wograld.jxclient.items.ItemsManager;
import com.realtime.wograld.jxclient.items.QuestsView;
import com.realtime.wograld.jxclient.items.SpellsView;
import com.realtime.wograld.jxclient.mapupdater.CfMapUpdater;
import com.realtime.wograld.jxclient.mapupdater.MapUpdaterState;
import com.realtime.wograld.jxclient.metaserver.Metaserver;
import com.realtime.wograld.jxclient.metaserver.MetaserverModel;
import com.realtime.wograld.jxclient.metaserver.MetaserverProcessor;
import com.realtime.wograld.jxclient.quests.QuestsManager;
import com.realtime.wograld.jxclient.queue.CommandQueue;
import com.realtime.wograld.jxclient.scripts.ScriptManager;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.wograld.DefaultWograldServerConnection;
import com.realtime.wograld.jxclient.settings.Filenames;
import com.realtime.wograld.jxclient.settings.Settings;
import com.realtime.wograld.jxclient.settings.options.OptionException;
import com.realtime.wograld.jxclient.settings.options.OptionManager;
import com.realtime.wograld.jxclient.settings.options.Pickup;
import com.realtime.wograld.jxclient.shortcuts.Shortcuts;
import com.realtime.wograld.jxclient.skills.SkillSet;
import com.realtime.wograld.jxclient.skin.io.JXCSkinLoader;
import com.realtime.wograld.jxclient.sound.MusicWatcher;
import com.realtime.wograld.jxclient.sound.SoundCheckBoxOption;
import com.realtime.wograld.jxclient.sound.SoundManager;
import com.realtime.wograld.jxclient.sound.SoundWatcher;
import com.realtime.wograld.jxclient.sound.StatsWatcher;
import com.realtime.wograld.jxclient.spells.SpellsManager;
import com.realtime.wograld.jxclient.stats.ActiveSkillWatcher;
import com.realtime.wograld.jxclient.stats.ExperienceTable;
import com.realtime.wograld.jxclient.stats.PoisonWatcher;
import com.realtime.wograld.jxclient.stats.Stats;
import com.realtime.wograld.jxclient.util.DebugWriter;
import com.realtime.wograld.jxclient.window.GuiManager;
import com.realtime.wograld.jxclient.window.JXCConnection;
import com.realtime.wograld.jxclient.window.KeyHandler;
import com.realtime.wograld.jxclient.window.KeybindingsManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.player.JOrbisPlayer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * This is the entry point for JXClient. Note that this class doesn't do much by
 * itself - most of the work in done in JXCWindow or WograldServerConnection.
 * @author Lauwenmark
 * @version 1.0
 * @see JXCWindow
 * @see WograldServerConnection
 * @since 1.0
 */
public class JXClient {

    /**
     * The program entry point.
     * @param args the command line arguments
     */
    public static void main(@NotNull final String[] args) {
        Thread.currentThread().setName("JXClient:Main");
        final String buildNumber = getBuildNumber();
        System.out.println("JXClient "+buildNumber+" - Wograld Java Client");
        System.out.println("(C)2005 by Lauwenmark.");
        System.out.println("This software is placed under the GPL License");
        final Options options = new Options();
        options.parse(args);
        //noinspection InstantiationOfUtilityClass
        new JXClient(options, buildNumber);
    }

    /**
     * Returns the build number as a string.
     * @return the build number
     */
    @NotNull
    private static String getBuildNumber() {
        try {
            return ResourceBundle.getBundle("build").getString("build.number");
        } catch (final MissingResourceException ignored) {
            return "unknown";
        }
    }

    /**
     * The constructor of the class. This is where the main window is created.
     * Initialization of a JXCWindow is the only task performed here.
     * @param options the options
     * @param buildNumber the client's build number
     */
    private JXClient(@NotNull final Options options, @NotNull final String buildNumber) {
        try {
            final Writer debugProtocolOutputStreamWriter = openDebugStream(options.getDebugProtocolFilename());
            try {
                final Writer debugKeyboardOutputStreamWriter = openDebugStream(options.getDebugKeyboardFilename());
                try {
                    final Writer debugScreenOutputStreamWriter = openDebugStream(options.getDebugScreenFilename());
                    try {
                        final Writer debugSoundOutputStreamWriter = openDebugStream(options.getDebugSoundFilename());
                        try {
                            final Settings settings = new Settings(Filenames.getSettingsFile());
                            settings.remove("resolution"); // delete obsolete entry
                            settings.remove("width"); // delete obsolete entry
                            settings.remove("height"); // delete obsolete entry
                            settings.remove("skin"); // delete obsolete entry
                            final OptionManager optionManager = new OptionManager(settings);
                            final MetaserverModel metaserverModel = new MetaserverModel();
                            final CharacterModel characterModel = new CharacterModel();
                            
                         //   final WograldServerConnection server = new DefaultWograldServerConnection(debugProtocolOutputStreamWriter == null ? null : new DebugWriter(debugProtocolOutputStreamWriter), "JXClient "+buildNumber);
                           JOrbisPlayer player=new JOrbisPlayer();  
                            final WograldServerConnection server = new DefaultWograldServerConnection(debugProtocolOutputStreamWriter == null ? null : new DebugWriter(debugProtocolOutputStreamWriter), "JXClient "+buildNumber, player );
                            
                            server.start();
                            try {
                                final GuiStateManager guiStateManager = new GuiStateManager(server);
                                final ExperienceTable experienceTable = new ExperienceTable(server);
                                final SkillSet skillSet = new SkillSet(server, guiStateManager);
                                final Stats stats = new Stats(server, experienceTable, skillSet, guiStateManager);
                                final FaceCache faceCache = new FaceCache(server);
                                final FacesQueue facesQueue = new FacesQueue(server, new FileCache(Filenames.getOriginalImageCacheDir()), new FileCache(Filenames.getScaledImageCacheDir()), new FileCache(Filenames.getMagicMapImageCacheDir()));
                                final FacesManager facesManager = new DefaultFacesManager(faceCache, facesQueue);
                                final ItemSet itemSet = new ItemSet();
                                final InventoryView inventoryView = new InventoryView(itemSet, new InventoryComparator());
                                final FloorView floorView = new FloorView(itemSet);
                                new ItemsManager(server, facesManager, stats, skillSet, guiStateManager, itemSet);
                                final Metaserver metaserver = new Metaserver(Filenames.getMetaserverCacheFile(), metaserverModel);
                                new MetaserverProcessor(metaserver, guiStateManager);
                                final SoundManager soundManager = new SoundManager(guiStateManager, debugSoundOutputStreamWriter == null ? null : new DebugWriter(debugSoundOutputStreamWriter));
                                try {
                                    optionManager.addOption("sound_enabled", "Whether sound is enabled.", new SoundCheckBoxOption(soundManager));
                                } catch (final OptionException ex) {
                                    throw new AssertionError(ex);
                                }

                                final MouseTracker mouseTracker = new MouseTracker(options.isDebugGui());
                                final JXCWindowRenderer windowRenderer = new JXCWindowRenderer(mouseTracker, server, debugScreenOutputStreamWriter);
                                new MusicWatcher(server, soundManager);
                                new SoundWatcher(server, soundManager);
                                new StatsWatcher(stats, windowRenderer, server, soundManager);
                                new PoisonWatcher(stats, server);
                                new ActiveSkillWatcher(stats, server);
                                final Macros macros = new Macros(server);
                                final MapUpdaterState mapUpdaterState = new MapUpdaterState(facesManager, guiStateManager);
                                new CfMapUpdater(mapUpdaterState, server, facesManager, guiStateManager);
                                final SpellsManager spellsManager = new SpellsManager(server, guiStateManager, skillSet, stats);
                                final SpellsView spellsView = new SpellsView(spellsManager, facesManager);
                                final QuestsManager questsManager = new QuestsManager(server, guiStateManager);
                                final QuestsView questsView = new QuestsView(questsManager, facesManager);
                                final CommandQueue commandQueue = new CommandQueue(server, guiStateManager);
                                final ScriptManager scriptManager = new ScriptManager(commandQueue, server, stats, floorView, itemSet, spellsManager, mapUpdaterState, skillSet);
                                final Shortcuts shortcuts = new Shortcuts(commandQueue, spellsManager);

                                final Exiter exiter = new Exiter();
                                final JXCWindow[] window = new JXCWindow[1];
                                SwingUtilities.invokeAndWait(new Runnable() {

                                    @Override
                                    public void run() {
                                        final TooltipManager tooltipManager = new TooltipManager();
                                        final Pickup characterPickup;
                                        try {
                                            characterPickup = new Pickup(commandQueue, optionManager);
                                        } catch (final OptionException ex) {
                                            throw new AssertionError(ex);
                                        }
                                        final GuiManagerCommandCallback commandCallback = new GuiManagerCommandCallback(exiter, server);
                                        final ScreenshotFiles screenshotFiles = new ScreenshotFiles();
                                        final Commands commands = new Commands(commandQueue);
                                        final GUICommandFactory guiCommandFactory = new GUICommandFactory(commandCallback, commands, macros);
                                        commands.addCommand(new BindCommand(server, commandCallback, guiCommandFactory));
                                        commands.addCommand(new UnbindCommand(commandCallback, server));
                                        commands.addCommand(new ListQuestsCommand(server, commandCallback, guiCommandFactory, commandQueue));
                                        commands.addCommand(new QuestInfoCommand(server, commandCallback, guiCommandFactory, commandQueue));
                                        commands.addCommand(new ScreenshotCommand(windowRenderer, server, screenshotFiles));
                                        commands.addCommand(new ScriptCommand(scriptManager, server));
                                        commands.addCommand(new ScriptkillCommand(scriptManager, server));
                                        commands.addCommand(new ScriptkillallCommand(scriptManager, server));
                                        commands.addCommand(new ScriptsCommand(scriptManager, server));
                                        commands.addCommand(new ScripttellCommand(scriptManager, server));
                                        commands.addCommand(new ExecCommand(commandCallback, server));
                                        commands.addCommand(new SetCommand(server, optionManager));
                                        commands.addCommand(new ClearCommand(windowRenderer, server));
                                        commands.addCommand(new DebugMessagesCommand(server));
                                        final File keybindingsFile;
                                        try {
                                            keybindingsFile = Filenames.getKeybindingsFile(null, null);
                                        } catch (final IOException ex) {
                                            System.err.println("Cannot read keybindings file: "+ex.getMessage());
                                            exiter.terminate();
                                            return;
                                        }
                                        final KeybindingsManager keybindingsManager = new KeybindingsManager(keybindingsFile, guiCommandFactory);
                                        final JXCConnection connection = new JXCConnection(keybindingsManager, shortcuts, settings, characterPickup, server, guiStateManager);
                                        final GuiFactory guiFactory = new GuiFactory(guiCommandFactory);
                                        final GuiManager guiManager = new GuiManager(guiStateManager, tooltipManager, settings, server, windowRenderer, guiFactory, keybindingsManager, connection);
                                        commandCallback.init(guiManager);
                                        final KeyBindings defaultKeyBindings = new KeyBindings(null, guiCommandFactory);
                                        final JXCSkinLoader jxcSkinLoader = new JXCSkinLoader(itemSet, inventoryView, floorView, spellsView, spellsManager, facesManager, stats, mapUpdaterState, defaultKeyBindings, optionManager, experienceTable, skillSet, options.getTileSize(), keybindingsManager, questsManager, questsView);
                                        final SmoothFaces smoothFaces = new SmoothFaces(server);
                                        final SkinLoader skinLoader = new SkinLoader(commandCallback, metaserverModel, options.getResolution(), macros, windowRenderer, server, guiStateManager, tooltipManager, commandQueue, jxcSkinLoader, commands, shortcuts, characterModel, smoothFaces, guiCommandFactory);
                                        new FacesTracker(guiStateManager, facesManager);
                                        new PlayerNameTracker(guiStateManager, connection, itemSet);
                                        new OutputCountTracker(guiStateManager, server, commandQueue);
                                        final DefaultKeyHandler defaultKeyHandler = new DefaultKeyHandler(exiter, guiManager, server, guiStateManager);
                                        final KeyHandler keyHandler = new KeyHandler(debugKeyboardOutputStreamWriter, keybindingsManager, commandQueue, windowRenderer, defaultKeyHandler);
                                        window[0] = new JXCWindow(exiter, server, optionManager, guiStateManager, windowRenderer, commandQueue, guiManager, keyHandler, characterModel, connection);
                                        window[0].init(options.getResolution(), options.getSkin(), options.isFullScreen(), skinLoader);
                                        keybindingsManager.loadKeybindings();
                                        final String serverInfo = options.getServer();
                                        
                                        
  
                                        
                                        
                                        if (serverInfo != null) {
                                            guiStateManager.connect(serverInfo);
                                        } else {
                                            guiStateManager.changeGUI(JXCWindow.DISABLE_START_GUI ? GuiState.METASERVER : GuiState.START);
                                        }
                                    }

                                });
                                exiter.waitForTermination();
                                SwingUtilities.invokeAndWait(new Runnable() {

                                    @Override
                                    public void run() {
                                        window[0].term();
                                        soundManager.shutdown();
                                    }

                                });
                            } finally {
                                server.stop();
                            }
                        } finally {
                            if (debugSoundOutputStreamWriter != null) {
                                debugSoundOutputStreamWriter.close();
                            }
                        }
                    } finally {
                        if (debugScreenOutputStreamWriter != null) {
                            debugScreenOutputStreamWriter.close();
                        }
                    }
                } finally {
                    if (debugKeyboardOutputStreamWriter != null) {
                        debugKeyboardOutputStreamWriter.close();
                    }
                }
            } finally {
                if (debugProtocolOutputStreamWriter != null) {
                    debugProtocolOutputStreamWriter.close();
                }
            }
        } catch (final IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.exit(1);
            throw new AssertionError();
        } catch (final InterruptedException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.exit(1);
            throw new AssertionError();
        } catch (final InvocationTargetException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.exit(1);
            throw new AssertionError();
        }

        System.exit(0);
    }

    /**
     * Opens an debug output stream.
     * @param filename the filename to write to or <code>null</code>
     * @return the output stream or <code>null</code>
     */
    @Nullable
    private static Writer openDebugStream(@Nullable final String filename) {
        if (filename == null) {
            return null;
        }

        Writer writer = null;
        try {
            final FileOutputStream outputStream = new FileOutputStream(filename);
            try {
                writer = new OutputStreamWriter(outputStream, "UTF-8");
            } finally {
                if (writer == null) {
                    outputStream.close();
                }
            }
        } catch (final IOException ex) {
            System.err.println(filename+": cannot create output file: "+ex.getMessage());
            return null;
        }
        return writer;
    }

}
