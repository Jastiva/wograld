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

package net.sf.gridarta.gui.map.mapview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import net.sf.gridarta.gui.map.renderer.AbstractMapRenderer;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectListener;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlListener;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default {@link MapView} implementation.
 * @author Andreas Kirschbaum
 */
public class DefaultMapView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapView<G, A, R> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DefaultMapView.class);

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The controller of this view.
     */
    @NotNull
    private final MapControl<G, A, R> mapControl;

    /**
     * The {@link JInternalFrame} instance associated with this map view.
     */
    @NotNull
    private final JInternalFrame internalFrame;

    /**
     * View number.
     */
    private final int number;

    /**
     * The {@link PathManager} for converting path names.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link AbstractMapRenderer} for rendering the map model.
     */
    @NotNull
    private final AbstractMapRenderer<G, A, R> renderer;

    /**
     * The erroneous {@link MapSquare MapSquares}.
     */
    @NotNull
    private final ErroneousMapSquares<G, A, R> erroneousMapSquares;

    /**
     * The erroneous {@link MapSquare MapSquares}.
     */
    @NotNull
    private final MapCursorTracker<G, A, R> mapCursorTracker;

    /**
     * The {@link MapModelListener} used to detect changes in the map model that
     * should be reflected in the window title.
     */
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            // ignore
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
            updateTitle();
        }

        @Override
        public void modifiedChanged() {
            updateTitle();
        }

    };

    /**
     * The {@link MapArchObjectListener} used to detect changes in the map model
     * that should be reflected in the window title.
     */
    private final MapArchObjectListener mapArchObjectListener = new MapArchObjectListener() {

        @Override
        public void mapMetaChanged() {
            updateTitle();
        }

        @Override
        public void mapSizeChanged(@NotNull final Size2D mapSize) {
            // ignore
        }

    };

    /**
     * The {@link MapControlListener} used to detect changes in the map control
     * that should be reflected in the window title.
     */
    private final MapControlListener<G, A, R> mapControlListener = new MapControlListener<G, A, R>() {

        @Override
        public void saved(@NotNull final DefaultMapControl<G, A, R> mapControl) {
            updateTitle();
        }

    };

    /**
     * Create a new instance.
     * @param mapControl the controller of this view
     * @param number each view of a map will get a number
     * @param pathManager the path manager for converting path names
     * @param mapGrid the map grid for this map view
     * @param mapCursor the map cursor for this map view
     * @param renderer the map renderer for rendering the map model
     * @param viewPosition the initial view position to show; null=show top left
     * corner
     * @param xScrollDistance the x distance when scrolling
     * @param yScrollDistance the y distance when scrolling
     */
    public DefaultMapView(@NotNull final MapControl<G, A, R> mapControl, final int number, @NotNull final PathManager pathManager, @NotNull final MapGrid mapGrid, @NotNull final MapCursor<G, A, R> mapCursor, final AbstractMapRenderer<G, A, R> renderer, @Nullable final Point viewPosition, final int xScrollDistance, final int yScrollDistance) {
        super(mapControl.getMapModel(), mapGrid, mapCursor);
        internalFrame = new JInternalFrame(getWindowTitle(mapControl, number, pathManager), true, true, true, true);
        this.mapControl = mapControl;
        this.number = number;
        this.pathManager = pathManager;
        this.renderer = renderer;
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        erroneousMapSquares = new ErroneousMapSquares<G, A, R>(mapModel, mapGrid, renderer);
        mapCursorTracker = new MapCursorTracker<G, A, R>(mapControl.isPickmap(), viewPosition, xScrollDistance, yScrollDistance, mapCursor, renderer);
        mapModel.addMapModelListener(mapModelListener);
        mapModel.getMapArchObject().addMapArchObjectListener(mapArchObjectListener);
        mapControl.addMapControlListener(mapControlListener);
        internalFrame.getContentPane().setLayout(new BorderLayout());
        internalFrame.getContentPane().add(mapCursorTracker.getScrollPane(), BorderLayout.CENTER);
        internalFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        internalFrame.setAutoscrolls(true);
        internalFrame.setFocusable(true);
        for (final Direction direction : Direction.values()) {
            installAccelerator("moveCursor" + direction.getId());
        }
        installAccelerator("moveSquarePrev");
        installAccelerator("moveSquareNext");
        installAccelerator("moveSquareUp");
        installAccelerator("moveSquareDown");
        installAccelerator("moveSquareInv");
        installAccelerator("moveSquareEnv");
    }

    /**
     * Installs accelerator keys for an action.
     * @param key the action's key
     */
    private void installAccelerator(@NotNull final String key) {
        final Action action = ACTION_BUILDER.getAction(key);
        if (action != null) {
            final KeyStroke keyStroke1 = ActionUtils.getShortcut(action);
            if (keyStroke1 != null) {
                internalFrame.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke1, key);
            }
            final KeyStroke keyStroke2 = ActionUtils.getAlternativeShortcut(action);
            if (keyStroke2 != null) {
                internalFrame.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke2, key);
            }
            internalFrame.getActionMap().put(key, action);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeNotify() {
        internalFrame.getContentPane().remove(mapCursorTracker.getScrollPane());
        mapCursorTracker.closeNotify();
        erroneousMapSquares.closeNotify();
        renderer.closeNotify();
        mapControl.removeMapControlListener(mapControlListener);
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        mapModel.getMapArchObject().removeMapArchObjectListener(mapArchObjectListener);
        mapModel.removeMapModelListener(mapModelListener);
        //mapFileActions.closeNotify();
        MenuUtils.disposeMenuElement(internalFrame.getJMenuBar());
    }

    /**
     * Update the Map-Window Title (according to name and changeFlag).
     */
    private void updateTitle() {
        internalFrame.setTitle(getWindowTitle());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getWindowTitle() {
        return getWindowTitle(mapControl, number, pathManager);
    }

    /**
     * Returns the title for the map window.
     * @param mapControl the map control to use
     * @param number the view number
     * @param pathManager the path manager for converting path names
     * @return the title
     */
    @NotNull
    private static String getWindowTitle(@NotNull final MapControl<?, ?, ?> mapControl, final int number, @NotNull final PathManager pathManager) {
        final File mapFile = mapControl.getMapModel().getMapFile();
        final String mapPath;
        if (mapFile == null) {
            mapPath = "<unsaved>";
        } else {
            final String tmp = pathManager.getMapPath2(mapFile);
            mapPath = tmp == null ? mapFile.getPath() : tmp;
        }
        return mapPath + " [ " + mapControl.getMapModel().getMapArchObject().getMapName() + " ] (" + number + ")" + (mapControl.getMapModel().isModified() ? " *" : "");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Component getComponent() {
        return internalFrame;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControl<G, A, R> getMapControl() {
        return mapControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        try {
            internalFrame.setSelected(true);
        } catch (final PropertyVetoException e) {
            log.warn("Unexpected exception", e);
        }
        internalFrame.setVisible(true);
        internalFrame.requestFocus();
        internalFrame.restoreSubcomponentFocus();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JInternalFrame getInternalFrame() {
        return internalFrame;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapRenderer getRenderer() {
        return renderer;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JScrollPane getScrollPane() {
        return mapCursorTracker.getScrollPane();
    }

}
