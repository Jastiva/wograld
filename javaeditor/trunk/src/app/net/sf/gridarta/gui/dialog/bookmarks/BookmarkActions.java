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

package net.sf.gridarta.gui.dialog.bookmarks;

import java.awt.Component;
import java.io.File;
import javax.swing.Action;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.gui.mapmenu.AbstractMapMenuPreferences;
import net.sf.gridarta.gui.mapmenu.MapMenu;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryMap;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements actions for managing bookmarks.
 * @author Andreas Kirschbaum
 */
public class BookmarkActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ActionBuilder} instance.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link AbstractMapMenuPreferences} for adding new bookmarks.
     */
    @NotNull
    private final AbstractMapMenuPreferences bookmarksMapMenuPreferences;

    /**
     * The {@link MapMenu} defining the bookmarks menu entries.
     */
    @NotNull
    private final MapMenu mapMenu;

    /**
     * The {@link MapImageCache} for creating map previews.
     */
    @NotNull
    private final MapImageCache<G, A, R> mapImageCache;

    /**
     * The {@link MapViewManager} instance.
     */
    @NotNull
    private final MapViewManager<G, A, R> mapViewManager;

    /**
     * The parent component for dialogs.
     */
    @NotNull
    private final Component parentComponent;

    /**
     * The {@link Action} for "add bookmark".
     */
    @NotNull
    private final Action aAddBookmark = ActionUtils.newAction(ACTION_BUILDER, "Bookmarks", this, "addBookmark");

    /**
     * The {@link ManageBookmarksDialog} instance. Set to <code>null</code> if
     * not yet created.
     */
    @Nullable
    private ManageBookmarksDialog<G, A, R> manageBookmarksDialog;

    /**
     * Creates a new instance.
     * @param bookmarksMapMenuPreferences the map menu preferences defining the
     * bookmarks menu entries
     * @param mapMenu the map menu definition the bookmarks menu entries
     * @param mapViewManager the map view manager instance
     * @param parentComponent the parent component for dialogs
     * @param mapImageCache the map image cache for creating map previews
     */
    public BookmarkActions(@NotNull final AbstractMapMenuPreferences bookmarksMapMenuPreferences, @NotNull final MapMenu mapMenu, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final Component parentComponent, @NotNull final MapImageCache<G, A, R> mapImageCache) {
        this.bookmarksMapMenuPreferences = bookmarksMapMenuPreferences;
        this.mapMenu = mapMenu;
        this.mapViewManager = mapViewManager;
        this.parentComponent = parentComponent;
        this.mapImageCache = mapImageCache;
        ActionUtils.newAction(ACTION_BUILDER, "Bookmarks", this, "manageBookmarks");

        final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

            @Override
            public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
                updateActions();
            }

            @Override
            public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
                // ignore
            }

            @Override
            public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
                // ignore
            }

        };
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);

        updateActions();
    }

    /**
     * Action method for "addBookmark". Creates a new bookmark for the currently
     * opened map.
     */
    @ActionMethod
    public void addBookmark() {
        doAddBookmark(true);
    }

    /**
     * Action method for "manage bookmarks". Opens the dialog for editing
     * existing bookmarks.
     */
    @ActionMethod
    public void manageBookmarks() {
        if (manageBookmarksDialog == null) {
            manageBookmarksDialog = new ManageBookmarksDialog<G, A, R>(parentComponent, mapImageCache, mapMenu);
        }
        manageBookmarksDialog.showDialog(parentComponent);
    }

    /**
     * Updates the enabled state of all actions.
     */
    private void updateActions() {
        aAddBookmark.setEnabled(doAddBookmark(false));
    }

    /**
     * Preforms the action "add bookmark".
     * @param performAction whether the action should be performed
     * @return whether the action has or can be performed
     */
    private boolean doAddBookmark(final boolean performAction) {
        final MapView<G, A, R> mapView = mapViewManager.getActiveMapView();
        if (mapView == null) {
            return false;
        }

        final MapControl<G, A, R> mapControl = mapView.getMapControl();
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final File mapFile = mapModel.getMapFile();
        if (mapFile == null) {
            return false;
        }

        if (performAction) {
            final A mapArchObject = mapModel.getMapArchObject();
            final EditBookmarkDialog editBookmarkDialog = new EditBookmarkDialog(mapView.getInternalFrame(), mapArchObject.getMapName());
            if (editBookmarkDialog.showDialog()) {
                final MapMenuEntryMap mapMenuEntry = new MapMenuEntryMap(mapFile, editBookmarkDialog.getDescription());
                bookmarksMapMenuPreferences.addEntry(mapMenuEntry);
            }
        }

        return true;
    }

}
