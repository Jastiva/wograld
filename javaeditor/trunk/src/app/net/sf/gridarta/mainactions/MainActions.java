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

package net.sf.gridarta.mainactions;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JFrame;
import net.sf.gridarta.commands.Collector;
import net.sf.gridarta.gui.copybuffer.CopyBuffer;
import net.sf.gridarta.gui.dialog.find.FindDialogManager;
import net.sf.gridarta.gui.dialog.replace.ReplaceDialogManager;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.misc.ShiftProcessor;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.utils.AsynchronousProgress;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.ArchetypeSetListener;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.floodfill.FillUtils;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.MapGridEvent;
import net.sf.gridarta.model.mapgrid.MapGridListener;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.ExiterListener;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.misc.Progress;
import net.sf.japi.swing.misc.ProgressDisplay;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates actions and related functions.
 * @author Andreas Kirschbaum
 */
public class MainActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(MainActions.class);

    /**
     * The find dialog manager to use.
     */
    @NotNull
    private final FindDialogManager<G, A, R> findDialogManager;

    /**
     * The replace dialog manager to use.
     */
    @NotNull
    private final ReplaceDialogManager<G, A, R> replaceDialogManager;

    /**
     * The parent component for dialog windows.
     */
    @NotNull
    private final JFrame parent;

    /**
     * The global settings instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The map validators.
     */
    @NotNull
    private final DelegatingMapValidator<G, A, R> validators;

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The archetype set instance.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * The copy buffer instance.
     */
    @NotNull
    private final CopyBuffer<G, A, R> copyBuffer;

    /**
     * The ObjectChooser instance to use.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link AbstractResources} to collect.
     */
    @NotNull
    private final AbstractResources<G, A, R> resources;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * Action called for "clear".
     */
    @NotNull
    private final Action aClear;

    /**
     * Action called for "cut".
     */
    @NotNull
    private final Action aCut;

    /**
     * Action called for "copy".
     */
    @NotNull
    private final Action aCopy;

    /**
     * Action called for "shift north".
     */
    @NotNull
    private final Action aShiftNorth;

    /**
     * Action called for "shift north east".
     */
    @NotNull
    private final Action aShiftNorthEast;

    /**
     * Action called for "shift east".
     */
    @NotNull
    private final Action aShiftEast;

    /**
     * Action called for "shift south east".
     */
    @NotNull
    private final Action aShiftSouthEast;

    /**
     * Action called for "shift south".
     */
    @NotNull
    private final Action aShiftSouth;

    /**
     * Action called for "shift south west".
     */
    @NotNull
    private final Action aShiftSouthWest;

    /**
     * Action called for "shift west".
     */
    @NotNull
    private final Action aShiftWest;

    /**
     * Action called for "shift north west".
     */
    @NotNull
    private final Action aShiftNorthWest;

    /**
     * Action called for "paste".
     */
    @NotNull
    private final Action aPaste;

    /**
     * Action called for "paste tiled".
     */
    @NotNull
    private final Action aPasteTiled;

    /**
     * Action called for "find".
     */
    @NotNull
    private final Action aFind;

    /**
     * Action called for "find next".
     */
    @NotNull
    private final Action aFindNext;

    /**
     * Action called for "find prev".
     */
    @NotNull
    private final Action aFindPrev;

    /**
     * Action called for "replace".
     */
    @NotNull
    private final Action aReplace;

    /**
     * Action called for "fill".
     */
    @NotNull
    private final Action aFillAuto;

    /**
     * Action called for "fill above".
     */
    @NotNull
    private final Action aFillAbove;

    /**
     * Action called for "fill below".
     */
    @NotNull
    private final Action aFillBelow;

    /**
     * Action called for "random fill".
     */
    @NotNull
    private final Action aRandFillAuto;

    /**
     * Action called for "random fill above".
     */
    @NotNull
    private final Action aRandFillAbove;

    /**
     * Action called for "random fill below".
     */
    @NotNull
    private final Action aRandFillBelow;

    /**
     * Action called for "flood fill".
     */
    @NotNull
    private final Action aFloodFill;

    /**
     * Action called for "select all".
     */
    @NotNull
    private final Action aSelectAll;

    /**
     * Action called for "invert selection".
     */
    @NotNull
    private final Action aInvertSelection;

    /**
     * Action called for "grow empty selection".
     */
    @NotNull
    private final Action aExpandEmptySelection;

    /**
     * Action called for "grow selection".
     */
    @NotNull
    private final Action aGrowSelection;

    /**
     * Action called for "shrink selection".
     */
    @NotNull
    private final Action aShrinkSelection;

    /**
     * Action called for "collect archetypes".
     */
    private final Action aCollectArches;

    /**
     * Action called for "reload faces".
     */
    @NotNull
    private final Action aReloadFaces;

    /**
     * Action called for "validate map".
     */
    @NotNull
    private final Action aValidateMap;

    /**
     * The {@link RandomFillDialog} instance.
     */
    @NotNull
    private final RandomFillDialog randomFillDialog = new RandomFillDialog();

    /**
     * The last known active map, or <code>null</code> if no map is active.
     */
    @Nullable
    private MapControl<G, A, R> currentMapControl;

    /**
     * The last known active map view, or <code>null</code> if no map is
     * active.
     */
    @Nullable
    private MapView<G, A, R> currentMapView;

    /**
     * The {@link Collector} if an archetype collection is running, or else
     * <code>null</code>.
     */
    @Nullable
    private volatile Collector collector;

    /**
     * The map grid listener used to detect map grid changes.
     */
    @NotNull
    private final MapGridListener mapGridListener = new MapGridListener() {

        @Override
        public void mapGridChanged(@NotNull final MapGridEvent e) {
            refreshMenus(); // selection state may have changed
        }

        @Override
        public void mapGridResized(@NotNull final MapGridEvent e) {
            // ignore
        }

    };

    /**
     * The map cursor listener used to detect map cursor changes.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            // ignore
        }

        @Override
        public void mapCursorChangedMode() {
            refreshMenus(); // cursor may have been activated or deactivated
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            // ignore
        }

    };

    /**
     * Create a new instance.
     * @param findDialogManager the find dialog manager to use
     * @param replaceDialogManager the replace dialog manager to use
     * @param parent the parent component for dialog windows
     * @param globalSettings the global settings instance
     * @param validators the map validators
     * @param mapViewSettings the map view settings instance
     * @param archetypeSet the archetype set
     * @param copyBuffer the copy buffer instance
     * @param objectChooser the animation objects instance to use
     * @param mapManager the map manager instance
     * @param mapViewManager the map view manager instance
     * @param resources the resources to collect
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param insertionModeSet the insertion mode set to use
     * @param exiter the exiter instance
     */
    public MainActions(@NotNull final FindDialogManager<G, A, R> findDialogManager, @NotNull final ReplaceDialogManager<G, A, R> replaceDialogManager, @NotNull final JFrame parent, @NotNull final GlobalSettings globalSettings, @NotNull final DelegatingMapValidator<G, A, R> validators, @NotNull final MapViewSettings mapViewSettings, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final CopyBuffer<G, A, R> copyBuffer, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final AbstractResources<G, A, R> resources, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final InsertionModeSet<G, A, R> insertionModeSet, @NotNull final Exiter exiter) {
        this.findDialogManager = findDialogManager;
        this.replaceDialogManager = replaceDialogManager;
        this.parent = parent;
        this.globalSettings = globalSettings;
        this.validators = validators;
        this.mapViewSettings = mapViewSettings;
        this.archetypeSet = archetypeSet;
        this.copyBuffer = copyBuffer;
        this.objectChooser = objectChooser;
        this.resources = resources;
        this.faceObjectProviders = faceObjectProviders;
        this.insertionModeSet = insertionModeSet;
        aClear = ActionUtils.newAction(ACTION_BUILDER, "Copy Buffer", this, "clear");
        aCut = ActionUtils.newAction(ACTION_BUILDER, "Copy Buffer", this, "cut");
        aCopy = ActionUtils.newAction(ACTION_BUILDER, "Copy Buffer", this, "copy");
        aPaste = ActionUtils.newAction(ACTION_BUILDER, "Copy Buffer", this, "paste");
        aPasteTiled = ActionUtils.newAction(ACTION_BUILDER, "Copy Buffer", this, "pasteTiled");
        aShiftNorth = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftNorth");
        aShiftNorthEast = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftNorthEast");
        aShiftEast = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftEast");
        aShiftSouthEast = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftSouthEast");
        aShiftSouth = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftSouth");
        aShiftSouthWest = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftSouthWest");
        aShiftWest = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftWest");
        aShiftNorthWest = ActionUtils.newAction(ACTION_BUILDER, "Map/Shift", this, "shiftNorthWest");
        aFind = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "find");
        aFindNext = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "findNext");
        aFindPrev = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "findPrev");
        aReplace = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "replace");
        aFillAuto = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "fillAuto");
        aFillAbove = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "fillAbove");
        aFillBelow = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "fillBelow");
        aRandFillAuto = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "randFillAuto");
        aRandFillAbove = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "randFillAbove");
        aRandFillBelow = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "randFillBelow");
        aFloodFill = ActionUtils.newAction(ACTION_BUILDER, "Map/Fill", this, "floodFill");
        aSelectAll = ActionUtils.newAction(ACTION_BUILDER, "Map/Selection", this, "selectAll");
        aInvertSelection = ActionUtils.newAction(ACTION_BUILDER, "Map/Selection", this, "invertSelection");
        aExpandEmptySelection = ActionUtils.newAction(ACTION_BUILDER, "Map/Selection", this, "expandEmptySelection");
        aGrowSelection = ActionUtils.newAction(ACTION_BUILDER, "Map/Selection", this, "growSelection");
        aShrinkSelection = ActionUtils.newAction(ACTION_BUILDER, "Map/Selection", this, "shrinkSelection");
        aCollectArches = ActionUtils.newAction(ACTION_BUILDER, "Tool", this, "collectArches");
        aReloadFaces = ActionUtils.newAction(ACTION_BUILDER, "Image,Tool", this, "reloadFaces");
        aValidateMap = ActionUtils.newAction(ACTION_BUILDER, "Map,Tool", this, "validateMap");

        mapManager.addMapManagerListener(newMapManagerListener());
        currentMapControl = mapManager.getCurrentMap();

        mapViewManager.addMapViewManagerListener(newMapViewManagerListener());
        currentMapView = mapViewManager.getActiveMapView();

        archetypeSet.addArchetypeSetListener(newArchetypeSetListener());
        copyBuffer.addMapModelListener(newMapModelListener());
        exiter.addExiterListener(newExiterListener());
        refreshMenus();
    }

    /**
     * Creates a new {@link MapManagerListener} that refreshes the actions when
     * the current map changes.
     * @return the map manager listener
     */
    @NotNull
    private MapManagerListener<G, A, R> newMapManagerListener() {
        final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

            @Override
            public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
                currentMapControl = mapControl;
                refreshMenus();
            }

            @Override
            public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
                // ignore: a current map changed event will be generated
            }

            @Override
            public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
                // ignore: a current map changed event will be generated
            }

            @Override
            public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
                // ignore: a current map changed event will be generated
            }

        };
        return mapManagerListener;
    }

    /**
     * Creates a new {@link MapManagerListener} that refreshes the actions when
     * map views are created or closed.
     * @return the map manager listener
     */
    @NotNull
    private MapViewManagerListener<G, A, R> newMapViewManagerListener() {
        final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

            @Override
            public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
                currentMapView = mapView;
                refreshMenus();
            }

            @Override
            public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
                mapView.getMapGrid().addMapGridListener(mapGridListener);
                mapView.getMapCursor().addMapCursorListener(mapCursorListener);
            }

            @Override
            public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
                mapView.getMapCursor().removeMapCursorListener(mapCursorListener);
                mapView.getMapGrid().removeMapGridListener(mapGridListener);
            }

        };
        return mapViewManagerListener;
    }

    /**
     * Creates a new {@link ArchetypeSetListener} that refreshes the actions
     * when the "load from archive" flag has changed.
     * @return the archetype set listener
     */
    @NotNull
    private ArchetypeSetListener<G, A, R> newArchetypeSetListener() {
        final ArchetypeSetListener<G, A, R> archetypeSetListener = new ArchetypeSetListener<G, A, R>() {

            @Override
            public void loadedFromArchiveChanged() {
                refreshMenus();
            }

        };
        return archetypeSetListener;
    }

    /**
     * Creates a new {@link MapModelListener} that refreshes the actions when
     * the map contents change.
     * @return the map model listener
     */
    @NotNull
    private MapModelListener<G, A, R> newMapModelListener() {
        final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

            @Override
            public void mapSizeChanged(@NotNull final Size2D newSize) {
                // ignore
            }

            @Override
            public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
                refreshMenus();
            }

            @Override
            public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
                // ignore
            }

            @Override
            public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
                // ignore
            }

            @Override
            public void mapFileChanged(@Nullable final File oldMapFile) {
                // ignore
            }

            @Override
            public void modifiedChanged() {
                // ignore
            }

        };
        return mapModelListener;
    }

    /**
     * Creates a new {@link ExiterListener} that delays application exit until
     * archetype collection has finished.
     * @return the exiter listener
     */
    @NotNull
    private ExiterListener newExiterListener() {
        final ExiterListener exiterListener = new ExiterListener() {

            @Override
            public void preExitNotify() {
                // ignore
            }

            @Override
            public void appExitNotify() {
                // ignore
            }

            @Override
            public void waitExitNotify() {
                final Collector tmp = collector;
                if (tmp != null) {
                    try {
                        tmp.waitUntilFinished();
                    } catch (final InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        log.warn("DelayedMapModelListenerManager was interrupted");
                    }
                }
            }

        };
        return exiterListener;
    }

    /**
     * Enable/disable menu entries based on the current state.
     */
    private void refreshMenus() {
        aClear.setEnabled(doClear(false));
        aCut.setEnabled(doCut(false));
        aCopy.setEnabled(doCopy(false));
        aPaste.setEnabled(doPaste(false));
        aPasteTiled.setEnabled(doPasteTiled(false));
        aShiftNorth.setEnabled(doShift(false, Direction.NORTH));
        aShiftNorthEast.setEnabled(doShift(false, Direction.NORTH_EAST));
        aShiftEast.setEnabled(doShift(false, Direction.EAST));
        aShiftSouthEast.setEnabled(doShift(false, Direction.SOUTH_EAST));
        aShiftSouth.setEnabled(doShift(false, Direction.SOUTH));
        aShiftSouthWest.setEnabled(doShift(false, Direction.SOUTH_WEST));
        aShiftWest.setEnabled(doShift(false, Direction.WEST));
        aShiftNorthWest.setEnabled(doShift(false, Direction.NORTH_WEST));
        aFind.setEnabled(doFind(false));
        aFindNext.setEnabled(doFindNext(false));
        aFindPrev.setEnabled(doFindPrev(false));
        aReplace.setEnabled(doReplace(false));
        aFillAuto.setEnabled(doFillAuto(false));
        aFillAbove.setEnabled(doFillAbove(false));
        aFillBelow.setEnabled(doFillBelow(false));
        aRandFillAuto.setEnabled(doRandFillAuto(false));
        aRandFillAbove.setEnabled(doRandFillAbove(false));
        aRandFillBelow.setEnabled(doRandFillBelow(false));
        aFloodFill.setEnabled(doFloodFill(false));
        aSelectAll.setEnabled(doSelectAll(false));
        aInvertSelection.setEnabled(doInvertSelection(false));
        aExpandEmptySelection.setEnabled(doExpandEmptySelection(false));
        aGrowSelection.setEnabled(doGrowSelection(false));
        aShrinkSelection.setEnabled(doShrinkSelection(false));
        aCollectArches.setEnabled(doCollectArches(false));
        aReloadFaces.setEnabled(doReloadFaces(false));
        aValidateMap.setEnabled(doValidateMap(false));
    }

    /**
     * "Clear" was selected from the Edit menu.
     */
    @ActionMethod
    public void clear() {
        doClear(true);
    }

    /**
     * "Cut" was selected from the Edit menu.
     */
    @ActionMethod
    public void cut() {
        doCut(true);
    }

    /**
     * "Copy" was selected from the Edit menu.
     */
    @ActionMethod
    public void copy() {
        doCopy(true);
    }

    /**
     * "Paste" was selected from the Edit menu.
     */
    @ActionMethod
    public void paste() {
        doPaste(true);
    }

    /**
     * "Paste Tiled" was selected from the Edit menu.
     */
    @ActionMethod
    public void pasteTiled() {
        doPasteTiled(true);
    }

    /**
     * "Shift North" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftNorth() {
        doShift(true, Direction.NORTH);
    }

    /**
     * "Shift North East" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftNorthEast() {
        doShift(true, Direction.NORTH_EAST);
    }

    /**
     * "Shift East" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftEast() {
        doShift(true, Direction.EAST);
    }

    /**
     * "Shift South East" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftSouthEast() {
        doShift(true, Direction.SOUTH_EAST);
    }

    /**
     * "Shift South" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftSouth() {
        doShift(true, Direction.SOUTH);
    }

    /**
     * "Shift South West" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftSouthWest() {
        doShift(true, Direction.SOUTH_WEST);
    }

    /**
     * "Shift West" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftWest() {
        doShift(true, Direction.WEST);
    }

    /**
     * "Shift North West" was selected from the Edit menu.
     */
    @ActionMethod
    public void shiftNorthWest() {
        doShift(true, Direction.NORTH_WEST);
    }

    /**
     * "Find" was selected from the Edit menu.
     */
    @ActionMethod
    public void find() {
        doFind(true);
    }

    /**
     * "Find next" was selected from the Edit menu.
     */
    @ActionMethod
    public void findNext() {
        doFindNext(true);
    }

    /**
     * "Find previous" was selected from the Edit menu.
     */
    @ActionMethod
    public void findPrev() {
        doFindPrev(true);
    }

    /**
     * "Replace" was selected from the Edit menu.
     */
    @ActionMethod
    public void replace() {
        doReplace(true);
    }

    /**
     * "Fill" was selected from the Edit menu.
     */
    @ActionMethod
    public void fillAuto() {
        doFillAuto(true);
    }

    /**
     * "Fill above" was selected from the Edit menu.
     */
    @ActionMethod
    public void fillAbove() {
        doFillAbove(true);
    }

    /**
     * "Fill below" was selected from the Edit menu.
     */
    @ActionMethod
    public void fillBelow() {
        doFillBelow(true);
    }

    /**
     * "Random fill" was selected from the Edit menu.
     */
    @ActionMethod
    public void randFillAuto() {
        doRandFillAuto(true);
    }

    /**
     * "Random fill above" was selected from the Edit menu.
     */
    @ActionMethod
    public void randFillAbove() {
        doRandFillAbove(true);
    }

    /**
     * "Random fill below" was selected from the Edit menu.
     */
    @ActionMethod
    public void randFillBelow() {
        doRandFillBelow(true);
    }

    /**
     * "Flood fill" was selected from the Edit menu.
     */
    @ActionMethod
    public void floodFill() {
        doFloodFill(true);
    }

    /**
     * Invoked when the user wants to select all squares from a map.
     */
    @ActionMethod
    public void selectAll() {
        doSelectAll(true);
    }

    /**
     * Invoked when the user wants to invert all selected squares from a map.
     */
    @ActionMethod
    public void invertSelection() {
        doInvertSelection(true);
    }

    /**
     * Invoked when the user wants to expand the selection of empty map squares
     * to surrounding empty map squares.
     */
    @ActionMethod
    public void expandEmptySelection() {
        doExpandEmptySelection(true);
    }

    /**
     * Invoked when the user wants to grow the selection by one square.
     */
    @ActionMethod
    public void growSelection() {
        doGrowSelection(true);
    }

    /**
     * Invoked when the user wants to shrink the selection by one square.
     */
    @ActionMethod
    public void shrinkSelection() {
        doShrinkSelection(true);
    }

    /**
     * Invoked when "collect archetypes" was selected.
     */
    @ActionMethod
    public void collectArches() {
        doCollectArches(true);
    }

    /**
     * Invoked when the user wants to reload the images.
     */
    @ActionMethod
    public void reloadFaces() {
        doReloadFaces(true);
    }

    /**
     * Invoked when "validate map" was selected.
     */
    @ActionMethod
    public void validateMap() {
        doValidateMap(true);
    }

    /**
     * Determine if the current map has a selection.
     * @return the map view if a selection exists, or <code>null</code>
     *         otherwise
     */
    @Nullable
    private MapView<G, A, R> getSelection() {
        return currentMapView != null && currentMapView.getMapGrid().getSelectedRec() != null ? currentMapView : null;
    }

    /**
     * Executes the "find" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFind(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            findDialogManager.showDialog(mapView);
        }

        return true;
    }

    /**
     * Executes the "find next" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFindNext(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        return mapView != null && findDialogManager.findNext(mapView, performAction);
    }

    /**
     * Executes the "find prev" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFindPrev(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        return mapView != null && findDialogManager.findPrev(mapView, performAction);
    }

    /**
     * Executes the "replace" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doReplace(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            replaceDialogManager.showDialog(mapView);
        }

        return true;
    }

    /**
     * Executes the "clear" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doClear(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Rectangle selectedRec = mapView.getMapGrid().getSelectedRec();
        if (selectedRec == null) {
            return false;
        }

        if (performAction) {
            copyBuffer.clear(mapView, selectedRec);
        }

        return true;
    }

    /**
     * Executes the "cut" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doCut(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Rectangle selectedRec = mapView.getMapGrid().getSelectedRec();
        if (selectedRec == null) {
            return false;
        }

        if (performAction) {
            copyBuffer.cut(mapView, selectedRec);
        }

        return true;
    }

    /**
     * Executes the "copy" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doCopy(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Rectangle selectedRec = mapView.getMapGrid().getSelectedRec();
        if (selectedRec == null) {
            return false;
        }

        if (performAction) {
            copyBuffer.copy(mapView, selectedRec);
        }

        return true;
    }

    /**
     * Executes the "paste" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doPaste(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Point startLocation = mapView.getMapCursor().getLocation();
        if (startLocation == null) {
            return false;
        }

        if (copyBuffer.isEmpty()) {
            return false;
        }

        if (performAction) {
            copyBuffer.paste(mapView, startLocation);
        }

        return true;
    }

    /**
     * Executes the "paste tiled" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doPasteTiled(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Point startLocation = mapView.getMapCursor().getLocation();
        if (startLocation == null) {
            return false;
        }

        if (copyBuffer.isEmpty()) {
            return false;
        }

        final List<MapSquare<G, A, R>> selectedSquares = mapView.getSelectedSquares();
        if (selectedSquares.isEmpty()) {
            return false;
        }

        final MapGrid mapGrid = mapView.getMapGrid();
        final Rectangle selectedRec = mapGrid.getSelectedRec();
        if (selectedRec == null) {
            return false;
        }

        if (performAction) {
            final Point cursorLocation = mapView.getMapCursor().getLocation();
            final Point origin = cursorLocation == null ? selectedRec.getLocation() : cursorLocation;
            copyBuffer.pasteTiled(mapView, selectedSquares, origin);
        }

        return true;
    }

    /**
     * Executes the "shift" action.
     * @param performAction whether the action should be performed
     * @param direction the direction to shift
     * @return whether the action was or can be performed
     */
    private boolean doShift(final boolean performAction, @NotNull final Direction direction) {
        final MapControl<G, A, R> mapControl = currentMapControl;
        if (mapControl == null) {
            return false;
        }

        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final ShiftProcessor<G, A, R> shiftProcessor = new ShiftProcessor<G, A, R>(mapViewSettings, mapView, mapControl.getMapModel(), insertionModeSet);
        if (!shiftProcessor.canShift(direction)) {
            return false;
        }

        if (performAction) {
            shiftProcessor.shift(direction);
        }

        return true;
    }

    /**
     * Executes the "fill auto" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFillAuto(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            fill(mapView, insertionModeSet.getAutoInsertionMode());
        }

        return true;
    }

    /**
     * Executes the "fill above" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFillAbove(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            fill(mapView, insertionModeSet.getTopmostInsertionMode());
        }

        return true;
    }

    /**
     * Executes the "fill below" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFillBelow(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            fill(mapView, insertionModeSet.getBottommostInsertionMode());
        }

        return true;
    }

    /**
     * Executes the "rand fill auto" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doRandFillAuto(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            fillRandom(mapView, insertionModeSet.getAutoInsertionMode());
        }

        return true;
    }

    /**
     * Executes the "rand fill above" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doRandFillAbove(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            fillRandom(mapView, insertionModeSet.getTopmostInsertionMode());
        }

        return true;
    }

    /**
     * Executes the "rand fill below" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doRandFillBelow(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            fillRandom(mapView, insertionModeSet.getBottommostInsertionMode());
        }

        return true;
    }

    /**
     * Executes the "flood fill" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doFloodFill(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Point mapCursorLocation = mapView.getMapCursor().getLocation();
        if (mapCursorLocation == null) {
            return false;
        }

        if (performAction) {
            FillUtils.floodFill(mapView.getMapControl().getMapModel(), mapCursorLocation, objectChooser.getSelections(), insertionModeSet);
        }

        return true;
    }

    /**
     * Executes the "select all" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSelectAll(final boolean performAction) {
        if (currentMapView == null) {
            return false;
        }

        if (performAction) {
            currentMapView.getMapGrid().selectAll();
        }

        return true;
    }

    /**
     * Executes the "invert selection" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doInvertSelection(final boolean performAction) {
        if (currentMapView == null) {
            return false;
        }

        if (performAction) {
            currentMapView.getMapGrid().invertSelection();
        }

        return true;
    }

    /**
     * Executes the "expand empty selection" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doExpandEmptySelection(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        boolean foundEmptySelectedSquare = false;
        final List<MapSquare<G, A, R>> selectedSquares = mapView.getSelectedSquares();
        for (final MapSquare<G, A, R> selectedSquare : selectedSquares) {
            if (selectedSquare.isEmpty()) {
                foundEmptySelectedSquare = true;
                break;
            }
        }
        if (!foundEmptySelectedSquare) {
            return false;
        }

        if (performAction) {
            final Map<MapSquare<G, A, R>, Void> newSelection = new IdentityHashMap<MapSquare<G, A, R>, Void>();
            Map<MapSquare<G, A, R>, Void> todo = new IdentityHashMap<MapSquare<G, A, R>, Void>();
            for (final MapSquare<G, A, R> mapSquare : selectedSquares) {
                todo.put(mapSquare, null);
                newSelection.put(mapSquare, null);
            }
            final MapModel<G, A, R> mapModel = mapView.getMapControl().getMapModel();
            final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
            final Point point = new Point();
            while (!todo.isEmpty()) {
                final Map<MapSquare<G, A, R>, Void> tmp = new IdentityHashMap<MapSquare<G, A, R>, Void>();
                for (final MapSquare<G, A, R> mapSquare : todo.keySet()) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (dx != 0 || dy != 0) {
                                mapSquare.getMapLocation(point, dx, dy);
                                if (mapArchObject.isPointValid(point)) {
                                    final MapSquare<G, A, R> newMapSquare = mapModel.getMapSquare(point);
                                    if (newMapSquare.isEmpty() && !newSelection.containsKey(newMapSquare)) {
                                        tmp.put(newMapSquare, null);
                                        newSelection.put(newMapSquare, null);
                                    }
                                }
                            }
                        }
                    }
                }
                todo = tmp;
            }
            final MapGrid mapGrid = mapView.getMapGrid();
            mapGrid.beginTransaction();
            try {
                mapGrid.unSelect();
                for (final MapSquare<G, A, R> mapSquare : newSelection.keySet()) {
                    mapSquare.getMapLocation(point);
                    mapGrid.select(point, SelectionMode.ADD);
                }
            } finally {
                mapGrid.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "grow selection" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doGrowSelection(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        final List<MapSquare<G, A, R>> selectedSquares = mapView.getSelectedSquares();
        if (selectedSquares.isEmpty()) {
            return false;
        }

        if (performAction) {
            final MapModel<G, A, R> mapModel = mapView.getMapControl().getMapModel();
            final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
            final Point point = new Point();
            final MapGrid mapGrid = mapView.getMapGrid();
            mapGrid.beginTransaction();
            try {
                for (final MapSquare<G, A, R> mapSquare : selectedSquares) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            mapSquare.getMapLocation(point, dx, dy);
                            if (mapArchObject.isPointValid(point)) {
                                mapGrid.select(point, SelectionMode.ADD);
                            }
                        }
                    }
                }
            } finally {
                mapGrid.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "shrink selection" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doShrinkSelection(final boolean performAction) {
        final MapView<G, A, R> mapView = getSelection();
        if (mapView == null) {
            return false;
        }

        final List<MapSquare<G, A, R>> selectedSquares = mapView.getSelectedSquares();
        if (selectedSquares.isEmpty()) {
            return false;
        }

        if (performAction) {
            final MapGrid mapGrid = mapView.getMapGrid();
            final MapModel<G, A, R> mapModel = mapView.getMapControl().getMapModel();
            final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
            final Point point = new Point();
            final Map<MapSquare<G, A, R>, Void> mapSquaresToShrink = new IdentityHashMap<MapSquare<G, A, R>, Void>();
            mapGrid.beginTransaction();
            try {
                for (final MapSquare<G, A, R> mapSquare : selectedSquares) {
LOOP:
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (dx != 0 || dy != 0) {
                                mapSquare.getMapLocation(point, dx, dy);
                                if (mapArchObject.isPointValid(point) && (mapGrid.getFlags(point) & MapGrid.GRID_FLAG_SELECTION) == 0) {
                                    mapSquaresToShrink.put(mapSquare, null);
                                    break LOOP;
                                }
                            }
                        }
                    }
                }
                for (final MapSquare<G, A, R> mapSquare : mapSquaresToShrink.keySet()) {
                    mapSquare.getMapLocation(point);
                    mapGrid.select(point, SelectionMode.SUB);
                }
            } finally {
                mapGrid.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "collect arches" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doCollectArches(final boolean performAction) {
        synchronized (this) {
            if (collector != null || !resources.canWriteCollected()) {
                return false;
            }

            if (performAction) {
                final Progress progress = new AsynchronousProgress(new ProgressDisplay(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectTitle"), 0, ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectArches")));
                final Progress collectArches = new Progress() {

                    @Override
                    public void finished() {
                        progress.finished();
                        collector = null;
                        aCollectArches.setEnabled(doCollectArches(false));
                    }

                    @Override
                    public Component getParentComponent() {
                        return progress.getParentComponent();
                    }

                    @Override
                    public void setLabel(final String msg, final int max) {
                        progress.setLabel(msg, max);
                    }

                    @Override
                    public void setValue(final int value) {
                        progress.setValue(value);
                    }

                };

                collector = new Collector(collectArches, resources, globalSettings.getCollectedDirectory());
                assert collector != null;
                collector.start();
                aCollectArches.setEnabled(doCollectArches(false));
            }
        }

        return true;
    }

    /**
     * Executes the "reload faces" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doReloadFaces(final boolean performAction) {
        if (archetypeSet.isLoadedFromArchive()) {
            return false;
        }

        if (performAction) {
            faceObjectProviders.reloadAll();
        }

        return true;
    }

    /**
     * Executes the "validate map" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doValidateMap(final boolean performAction) {
        final MapControl<G, A, R> mapControl = currentMapControl;
        if (mapControl == null) {
            return false;
        }

        if (performAction) {
            validators.validateAll(mapControl.getMapModel());
        }

        return true;
    }

    /**
     * "Fill" was selected from the Edit menu.
     * @param mapView the map view to fill
     * @param insertionMode the insertion mode to use
     */
    private void fill(@NotNull final MapView<G, A, R> mapView, @NotNull final InsertionMode<G, A, R> insertionMode) {
        FillUtils.fill(mapView.getMapControl().getMapModel(), mapView.getSelectedSquares(), insertionMode, objectChooser.getSelections(), -1, false);
    }

    /**
     * "RandomFill" was selected from the Edit menu.
     * @param mapView the map view to fill
     * @param insertionMode the insertion mode to use
     */
    private void fillRandom(@NotNull final MapView<G, A, R> mapView, @NotNull final InsertionMode<G, A, R> insertionMode) {
        if (!randomFillDialog.showRandomFillDialog(parent)) {
            return;
        }

        final int fillDensity = randomFillDialog.getFillDensity();
        final boolean noAdjacent = randomFillDialog.isRandomFillSkipAdjacentSquares();
        FillUtils.fill(mapView.getMapControl().getMapModel(), mapView.getSelectedSquares(), insertionMode, objectChooser.getSelections(), fillDensity, noAdjacent);
    }

}
