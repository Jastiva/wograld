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

package net.sf.gridarta.gui.mapdesktop;

import java.beans.PropertyVetoException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewsListener;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.gui.mapimagecache.MapImageCacheListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@link JDesktopPane} containing all map views.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapDesktop<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JDesktopPane {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(MapDesktop.class);

    /**
     * The action builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * All open map views.
     */
    @NotNull
    private final MapViewManager<G, A, R> mapViewManager;

    /**
     * The {@link MapManager} to use.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link MapImageCache} to use.
     */
    @NotNull
    private final MapImageCache<G, A, R> mapImageCache;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The action for "prev window".
     */
    @NotNull
    private final Action aPrevWindow = ActionUtils.newAction(ACTION_BUILDER, "Map,Window", this, "prevWindow");

    /**
     * The action for "next window".
     */
    @NotNull
    private final Action aNextWindow = ActionUtils.newAction(ACTION_BUILDER, "Map,Window", this, "nextWindow");

    /**
     * The actions to select windows.
     */
    @NotNull
    private final Map<MapView<G, A, R>, WindowAction<G, A, R>> windowActions = new IdentityHashMap<MapView<G, A, R>, WindowAction<G, A, R>>();

    /**
     * The {@link MapViewFrameListener MapViewFrameListeners} associated with
     * {@link MapView MapViews}. Maps map view to listener.
     */
    @NotNull
    private final Map<MapView<G, A, R>, MapViewFrameListener> mapViewFrameListeners = new IdentityHashMap<MapView<G, A, R>, MapViewFrameListener>();

    /**
     * The {@link MapManagerListener} attached to {@link #mapManager}.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            mapViewsManager.addMapViewsListener(mapControl, mapViewsListener);
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            mapViewsManager.removeMapViewsListener(mapControl, mapViewsListener);
        }

    };

    /**
     * The {@link MapViewsListener} attached to all existing {@link MapControl
     * MapControls} for maps.
     */
    @NotNull
    private final MapViewsListener<G, A, R> mapViewsListener = new MapViewsListener<G, A, R>() {

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            addMapView(mapView);
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            removeMapView(mapView);
        }

    };

    /**
     * The {@link MapImageCacheListener} registered to {@link #mapImageCache}.
     */
    @NotNull
    private final MapImageCacheListener<G, A, R> mapImageCacheListener = new MapImageCacheListener<G, A, R>() {

        @Override
        public void iconChanged(@NotNull final MapControl<G, A, R> mapControl) {
            final Iterator<MapView<G, A, R>> it = mapViewsManager.getMapViewIterator(mapControl);
            while (it.hasNext()) {
                updateFrameIcon(it.next());
            }
        }

    };

    /**
     * Creates a new instance.
     * @param mapViewManager all open map views
     * @param mapManager the map manager to use
     * @param mapImageCache the map image cache to use
     * @param mapViewsManager the map views
     */
    public MapDesktop(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapImageCache<G, A, R> mapImageCache, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.mapViewManager = mapViewManager;
        this.mapManager = mapManager;
        this.mapImageCache = mapImageCache;
        this.mapViewsManager = mapViewsManager;
        mapManager.addMapManagerListener(mapManagerListener);
        mapImageCache.addMapImageCacheListener(mapImageCacheListener);
        updateFocus(false);
        refreshMenus();
    }

    /**
     * Sets the given level view as the current one.
     * @param mapView the new current level view
     */
    public void setCurrentMapView(@NotNull final MapView<G, A, R> mapView) {
        mapViewManager.setActiveMapView(mapView);
        // De-iconify if necessary
        final JInternalFrame internalFrame = mapView.getInternalFrame();
        if (internalFrame.isIcon()) {
            try {
                internalFrame.setIcon(false);
            } catch (final PropertyVetoException e) {
                log.warn(ACTION_BUILDER.format("logUnexpectedException", e));
            }
            mapView.activate();
            return;
        }
        updateFocus(true);
        internalFrame.requestFocus();
        internalFrame.restoreSubcomponentFocus();
    }

    /**
     * Removes (closes) the map view.
     * @param mapView the map view to be removed (closed)
     */
    private void removeMapView(@NotNull final MapView<G, A, R> mapView) {
        final JInternalFrame internalFrame = mapView.getInternalFrame();
        internalFrame.removeInternalFrameListener(mapViewFrameListeners.remove(mapView));
        mapViewManager.removeMapView(mapView);
        if (windowActions.remove(mapView) == null) {
            assert false;
        }
        remove(internalFrame);
        // This is important: Removing a JInternalFrame from a JDesktopPane doesn't deselect it.
        // Thus it will still be referenced. To prevent a closed map from being referenced by Swing,
        // we check whether it's selected and if so deselect it.
        if (getSelectedFrame() == mapView) {
            setSelectedFrame(null);
        }
        internalFrame.dispose();
        repaint();

        updateFocus(true);
        refreshMenus();
    }

    /**
     * Adds the map view.
     * @param mapView the map view to add
     */
    private void addMapView(@NotNull final MapView<G, A, R> mapView) {
        final WindowAction<G, A, R> windowAction = new WindowAction<G, A, R>(this, mapView, mapManager);
        windowActions.put(mapView, windowAction);
        updateFrameIcon(mapView);

        final JInternalFrame internalFrame = mapView.getInternalFrame();
        final MapViewFrameListener mapViewFrameListener = new MapViewFrameListener(mapView);
        if (mapViewFrameListeners.put(mapView, mapViewFrameListener) != null) {
            assert false;
        }
        internalFrame.addInternalFrameListener(mapViewFrameListener);
        add(internalFrame);
        mapViewManager.addMapView(mapView);
        setCurrentMapView(mapView);
        internalFrame.setVisible(true);
        internalFrame.setBounds(0, 0, getWidth(), getHeight());
        try {
            internalFrame.setMaximum(true);
        } catch (final PropertyVetoException e) {
            log.error("PropertyVetoException: " + e);
        }
        refreshMenus();
    }

    /**
     * Updates the frame icon to the current icon image.
     * @param mapView the map view to update
     */
    private void updateFrameIcon(@NotNull final MapView<G, A, R> mapView) {
        final Action windowAction = windowActions.get(mapView);
        assert windowAction != null;
        final Icon icon = new ImageIcon(mapImageCache.getOrCreateIcon(mapView.getMapControl()));
        mapView.getInternalFrame().setFrameIcon(icon);
        windowAction.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * Adds an action for selecting this window to a menu.
     * @param menu the menu to add the action to
     * @param mapView the map view to add
     * @param index the index of the menu entry
     */
    public void addWindowAction(@NotNull final JMenu menu, @NotNull final MapView<G, A, R> mapView, final int index) {
        final WindowAction<G, A, R> windowAction = windowActions.get(mapView);
        assert windowAction != null;
        windowAction.setIndex(index);
        menu.add(windowAction);
    }

    /**
     * Activates and raises the given map view.
     * @param mapView the map view
     */
    private void activateAndRaiseMapView(@NotNull final MapView<G, A, R> mapView) {
        mapManager.setCurrentMap(mapView.getMapControl());
        mapView.activate();
        final JInternalFrame internalFrame = mapView.getInternalFrame();
        internalFrame.moveToFront();
        setSelectedFrame(internalFrame);
    }

    /**
     * Notifies that the map views focus is lost it is inserted as the second in
     * line to the map view vector.
     * @param mapView the map view who lost the focus
     */
    private void mapViewFocusLostNotify(@NotNull final MapView<G, A, R> mapView) {
        mapViewManager.deactivateMapView(mapView);
        updateFocus(true);
    }

    /**
     * Notifies that the given map view is now set as the current one.
     * @param mapView the new current map view
     */
    private void mapViewFocusGainedNotify(@NotNull final MapView<G, A, R> mapView) {
        mapViewManager.activateMapView(mapView);
        mapViewsManager.setFocus(mapView);
        mapManager.setCurrentMap(mapView.getMapControl());
    }

    /**
     * Updates the focus to the first non-iconified map window.
     * @param careAboutIconification <code>true</code> if the focus update
     * should ignore all windows iconified by the user.
     */
    private void updateFocus(final boolean careAboutIconification) {
        // Show the next map (if such exists)
        for (final MapView<G, A, R> mapView : mapViewManager) {
            final JInternalFrame internalFrame = mapView.getInternalFrame();
            if (internalFrame.isIcon()) {
                if (!careAboutIconification) {
                    try {
                        internalFrame.setIcon(false);
                    } catch (final PropertyVetoException e) {
                        log.warn(ACTION_BUILDER.format("logUnexpectedException", e));
                    }
                    activateAndRaiseMapView(mapView);
                    return;
                }
            } else {
                activateAndRaiseMapView(mapView);
                return;
            }
        }

        // No non-iconified map windows found
        mapManager.setCurrentMap(null);
    }

    /**
     * Gives focus to the next window.
     */
    @ActionMethod
    public void prevWindow() {
        doPrevWindow(true);
    }

    /**
     * Gives focus to the previous window.
     */
    @ActionMethod
    public void nextWindow() {
        doNextWindow(true);
    }

    /**
     * Enables/disables the actions according to the current state.
     */
    private void refreshMenus() {
        aPrevWindow.setEnabled(doPrevWindow(false));
        aNextWindow.setEnabled(doNextWindow(false));
    }

    /**
     * Performs or checks availability of the "prev window" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doPrevWindow(final boolean performAction) {
        if (!mapViewManager.doPrevWindow(performAction)) {
            return false;
        }

        if (performAction) {
            updateFocus(false);
        }

        return true;
    }

    /**
     * Performs or checks availability of the "next window" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doNextWindow(final boolean performAction) {
        if (!mapViewManager.doNextWindow(performAction)) {
            return false;
        }

        if (performAction) {
            updateFocus(false);
        }

        return true;
    }

    /**
     * The listener attached to map views.
     */
    private class MapViewFrameListener implements InternalFrameListener {

        /**
         * The associated {@link MapView}.
         */
        @NotNull
        private final MapView<G, A, R> mapView;

        /**
         * Creates a new instance.
         * @param mapView the associated map view
         */
        private MapViewFrameListener(@NotNull final MapView<G, A, R> mapView) {
            this.mapView = mapView;
        }

        @Override
        public void internalFrameActivated(@NotNull final InternalFrameEvent e) {
            mapViewFocusGainedNotify(mapView);
        }

        @Override
        public void internalFrameClosed(@NotNull final InternalFrameEvent e) {
            // ignore
        }

        @Override
        public void internalFrameClosing(@NotNull final InternalFrameEvent e) {
            mapViewsManager.closeMapView(mapView);
        }

        @Override
        public void internalFrameDeactivated(@NotNull final InternalFrameEvent e) {
            // ignore
        }

        @Override
        public void internalFrameDeiconified(@NotNull final InternalFrameEvent e) {
            // ignore
        }

        @Override
        public void internalFrameIconified(@NotNull final InternalFrameEvent e) {
            mapViewFocusLostNotify(mapView);
        }

        @Override
        public void internalFrameOpened(@NotNull final InternalFrameEvent e) {
            // ignore
        }

    }

}
