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

package net.sf.gridarta.actions;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.exitconnector.ExitLocation;
import net.sf.gridarta.model.exitconnector.ExitMatcher;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class implementing actions that operate on {@link ExitConnectorModel
 * ExitConnectorModels}.
 * @author Andreas Kirschbaum
 */
public class ExitConnectorActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ExitConnectorModel} to use.
     */
    @NotNull
    private final ExitConnectorModel exitConnectorModel;

    /**
     * The {@link ExitMatcher} to use.
     */
    @NotNull
    private final ExitMatcher<G, A, R> exitMatcher;

    /**
     * The {@link ArchetypeSet} to use.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * The {@link MapManager} for loading maps.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link FileControl} to use.
     */
    @NotNull
    private final FileControl<G, A, R> fileControl;

    /**
     * The {@link PathManager} for converting path names.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * Creates a new instance.
     * @param exitConnectorModel the exit connector model to use
     * @param exitMatcher the exit matcher to use
     * @param archetypeSet the archetype set to use
     * @param mapManager the map manager for loading maps
     * @param fileControl the file control to use
     * @param pathManager the path manager for converting path names
     * @param insertionModeSet the insertion mode set to use
     */
    public ExitConnectorActions(@NotNull final ExitConnectorModel exitConnectorModel, @NotNull final ExitMatcher<G, A, R> exitMatcher, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final MapManager<G, A, R> mapManager, @NotNull final FileControl<G, A, R> fileControl, @NotNull final PathManager pathManager, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        this.exitConnectorModel = exitConnectorModel;
        this.exitMatcher = exitMatcher;
        this.archetypeSet = archetypeSet;
        this.mapManager = mapManager;
        this.fileControl = fileControl;
        this.pathManager = pathManager;
        this.insertionModeSet = insertionModeSet;
    }

    /**
     * Executes the "exit copy" action.
     * @param performAction whether the action should be performed
     * @param mapControl the map control to copy from
     * @param location the cursor location
     * @return whether the action was or can be performed
     */
    public boolean doExitCopy(final boolean performAction, @NotNull final MapControl<G, A, R> mapControl, @NotNull final Point location) {
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final File mapFile = mapModel.getMapFile();
        if (mapFile == null) {
            // unsaved maps do not have a map path ==> no location to remember
            return false;
        }

        if (performAction) {
            final ExitLocation exitLocation = new ExitLocation(mapFile, location, mapModel.getMapArchObject().getMapName(), pathManager);
            exitConnectorModel.setExitLocation(exitLocation);
        }

        return true;
    }

    /**
     * Executes the "exit paste" action.
     * @param performAction whether the action should be performed
     * @param mapControl the map control to paste into
     * @param targetLocation the target location to paste to
     * @return whether the action was or can be performed
     */
    public boolean doExitPaste(final boolean performAction, @NotNull final MapControl<G, A, R> mapControl, @NotNull final Point targetLocation) {
        final ExitLocation sourceExitLocation = exitConnectorModel.getExitLocation();
        if (sourceExitLocation == null) {
            return false;
        }

        final MapModel<G, A, R> targetMapModel = mapControl.getMapModel();
        @Nullable final BaseObject<G, A, R, ?> targetExit;
        //XXX   final G selectedExit = exitMatcher.getValidExit(selectedSquareModel.getSelectedGameObject());
        //XXX   if (selectedExit != null) {
        //XXX       targetExit = selectedExit;
        //XXX   } else {
        final BaseObject<G, A, R, ?> cursorExit = exitMatcher.getExit(targetMapModel, targetLocation);
        if (cursorExit != null) {
            targetExit = cursorExit;
        } else if (exitConnectorModel.isAutoCreateExit()) {
            targetExit = null;
        } else {
            return false;
        }

        final File targetMapFile = targetMapModel.getMapFile();

        if (targetExit != null) {
            // paste into existing exit

            if (performAction) {
                pasteExit(targetExit, targetMapModel, sourceExitLocation, targetMapFile);
            }
        } else {
            // paste into newly created exit game object

            final BaseObject<G, A, R, ?> targetArchetype;
            try {
                targetArchetype = archetypeSet.getArchetype(exitConnectorModel.getExitArchetypeName());
            } catch (final UndefinedArchetypeException ignored) {
                return false;
            }

            if (performAction) {
                if (!pasteExit(targetLocation, targetMapModel, targetArchetype, sourceExitLocation, targetMapFile)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Executes the "exit connect" action.
     * @param performAction whether the action should be performed
     * @param mapControl the map control to connect with
     * @param targetLocation the target location to connect
     * @return whether the action was or can be performed
     */
    public boolean doExitConnect(final boolean performAction, @NotNull final MapControl<G, A, R> mapControl, @NotNull final Point targetLocation) {
        final ExitLocation sourceExitLocation = exitConnectorModel.getExitLocation();
        if (sourceExitLocation == null) {
            return false;
        }

        final MapModel<G, A, R> targetMapModel = mapControl.getMapModel();
        final File targetMapFile = targetMapModel.getMapFile();
        if (targetMapFile == null) {
            // target map file is unsaved ==> no location to connect
            return false;
        }

        @Nullable final BaseObject<G, A, R, ?> targetExit;
        @Nullable final BaseObject<G, A, R, ?> targetArchetype;
        //XXX   final G selectedExit = exitMatcher.getValidExit(selectedSquareModel.getSelectedGameObject());
        //XXX   if (selectedExit != null) {
        //XXX       targetExit = selectedExit;
        //XXX       targetArchetype = null;
        //XXX   } else {
        final BaseObject<G, A, R, ?> cursorExit = exitMatcher.getExit(targetMapModel, targetLocation);
        if (cursorExit != null) {
            targetExit = cursorExit;
            targetArchetype = null;
        } else if (exitConnectorModel.isAutoCreateExit()) {
            targetExit = null;
            try {
                targetArchetype = archetypeSet.getArchetype(exitConnectorModel.getExitArchetypeName());
            } catch (final UndefinedArchetypeException ignored) {
                return false;
            }
        } else {
            return false;
        }

        final MapControl<G, A, R> sourceMapControl;
        try {
            sourceMapControl = mapManager.openMapFile(sourceExitLocation.getFile(), false);
        } catch (final IOException ex) {
            fileControl.reportLoadError(sourceExitLocation.getFile(), ex.getMessage());
            return false;
        }
        try {
            return doExitConnect(performAction, targetExit, targetArchetype, targetMapModel, targetLocation, targetMapFile, sourceMapControl.getMapModel(), sourceExitLocation);
        } finally {
            try {
                // XXX: remove hack when MapManager automatically saves released maps
                if (sourceMapControl.getMapModel().isModified() && sourceMapControl.getUseCounter() <= 1) {
                    try {
                        sourceMapControl.save();
                    } catch (final IOException ex) {
                        fileControl.reportSaveError(sourceMapControl, ex.getMessage());
                    }
                }
            } finally {
                mapManager.release(sourceMapControl);
            }
        }
    }

    /**
     * Executes part of the "exit connect" action.
     * @param performAction whether the action should be performed
     * @param targetExit the target exit game object or <code>null</code>
     * @param targetArchetype the target exit archetype; <code>null</code> if
     * <code>targetExit</code> is non-<code>null</code>; unused otherwise
     * @param targetMapModel the target's map model
     * @param targetLocation the target's coordinates
     * @param targetMapFile the target's file
     * @param sourceMapModel the source's map model
     * @param sourceExitLocation the source's exit location @return whether the
     * action was or can be performed
     * @return whether the action was or can be performed
     */
    private boolean doExitConnect(final boolean performAction, @Nullable final BaseObject<?, ?, ?, ?> targetExit, @Nullable final BaseObject<G, A, R, ?> targetArchetype, @NotNull final MapModel<G, A, R> targetMapModel, @NotNull final Point targetLocation, @NotNull final File targetMapFile, @NotNull final MapModel<G, A, R> sourceMapModel, @NotNull final ExitLocation sourceExitLocation) {
        final File sourceMapFile = sourceMapModel.getMapFile();
        if (sourceMapFile == null) {
            // source map is unsaved ==> cannot connect
            return false;
        }

        final Point sourceLocation = sourceExitLocation.getMapCoordinate();

        @Nullable final BaseObject<G, A, R, ?> sourceExit;
        @Nullable final BaseObject<G, A, R, ?> sourceArchetype;
        final BaseObject<G, A, R, ?> exit = exitMatcher.getExit(sourceMapModel, sourceLocation);
        if (exit != null) {
            sourceExit = exit;
            sourceArchetype = null;
        } else if (exitConnectorModel.isAutoCreateExit()) {
            sourceExit = null;
            try {
                sourceArchetype = archetypeSet.getArchetype(exitConnectorModel.getExitArchetypeName());
            } catch (final UndefinedArchetypeException ignored) {
                return false;
            }
        } else {
            return false;
        }

        if (performAction) {
            final ExitLocation targetExitLocation = new ExitLocation(targetMapFile, targetLocation, targetMapModel.getMapArchObject().getMapName(), pathManager);
            if (sourceExit != null) {
                pasteExit(sourceExit, sourceMapModel, targetExitLocation, sourceMapFile);
            } else {
                if (!pasteExit(sourceLocation, sourceMapModel, sourceArchetype, targetExitLocation, sourceMapFile)) {
                    return false;
                }
            }

            if (targetExit != null) {
                pasteExit(targetExit, targetMapModel, sourceExitLocation, targetMapFile);
            } else {
                assert targetArchetype != null;
                if (!pasteExit(targetLocation, targetMapModel, targetArchetype, sourceExitLocation, targetMapFile)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Pastes exit information into an exit game object.
     * @param gameObject the exit game object to paste into
     * @param mapModel the map model to paste into
     * @param exitLocation the exit information to paste
     * @param gameObjectMapFile the map file of <code>gameObject</code> or
     * <code>null</code> if on an unsaved map
     */
    private void pasteExit(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final MapModel<G, A, R> mapModel, @NotNull final ExitLocation exitLocation, @Nullable final File gameObjectMapFile) {
        mapModel.beginTransaction("paste exit");
        try {
            exitLocation.updateExitObject(gameObject, exitConnectorModel.isPasteExitName(), gameObjectMapFile);
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Creates a new exit game object.
     * @param location the location to insert into
     * @param mapModel the map model to insert into
     * @param archetype the archetype to insert
     * @param exitLocation the exit information to use
     * @param mapFile the map file to insert into or <code>null</code> for
     * unsaved maps
     * @return whether insertion was successful
     */
    private boolean pasteExit(@NotNull final Point location, @NotNull final MapModel<G, A, R> mapModel, @NotNull final BaseObject<G, A, R, ?> archetype, @NotNull final ExitLocation exitLocation, @Nullable final File mapFile) {
        mapModel.beginTransaction("paste exit");
        try {
            final BaseObject<G, A, R, ?> newExit = mapModel.insertBaseObject(archetype, location, true, false, insertionModeSet.getTopmostInsertionMode());
            if (newExit == null) {
                return false;
            }

            exitLocation.updateExitObject(newExit, exitConnectorModel.isPasteExitName(), mapFile);
        } finally {
            mapModel.endTransaction();
        }

        return true;
    }

}
