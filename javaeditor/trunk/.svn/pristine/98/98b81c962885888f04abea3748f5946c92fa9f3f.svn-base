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

package net.sf.gridarta.gui.dialog.newmap;

import java.awt.Component;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapfiles.MapFolder;
import net.sf.gridarta.gui.mapfiles.MapFolderTree;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.panel.pickmapchooser.PickmapChooserControl;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class to create {@link NewMapDialog} instances.
 * @author Andreas Kirschbaum
 */
public class NewMapDialogFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link MapViewsManager} to use.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The gridarta objects factory instance.
     */
    @NotNull
    private final MapArchObjectFactory<A> mapArchObjectFactory;

    /**
     * The default width for new maps.
     */
    private final int defaultMapWidth;

    /**
     * The default height for new maps.
     */
    private final int defaultMapHeight;

    /**
     * The default difficulty for new maps.
     */
    private final int defaultMapDifficulty;

    /**
     * Whether to show the "mapSizeDefault" checkbox.
     */
    private final boolean showMapDifficulty;

    /**
     * Whether to show the "mapDifficulty" field.
     */
    private final boolean showMapSizeDefault;

    /**
     * The default width for new pickmaps.
     */
    private final int defaultPickmapWidth;

    /**
     * The default height for new pickmaps.
     */
    private final int defaultPickmapHeight;

    /**
     * The pickmap chooser control.
     */
    @NotNull
    private PickmapChooserControl<G, A, R> pickmapChooserControl;

    /**
     * The object chooser instance.
     */
    @NotNull
    private ObjectChooser<G, A, R> objectChooser;

    /**
     * The parent component for dialog windows.
     */
    @NotNull
    private final Component parent;

    /**
     * Creates a new instance.
     * @param mapViewsManager the map views manager to use
     * @param mapArchObjectFactory the map arch object factory instance
     * @param defaultMapWidth the default width for new maps
     * @param defaultMapHeight the default height for new maps
     * @param defaultMapDifficulty the default difficulty for new maps
     * @param showMapSizeDefault whether to show the "mapSizeDefault" checkbox
     * @param showMapDifficulty whether to show the "mapDifficulty" field
     * @param defaultPickmapWidth the default width for new pickmaps
     * @param defaultPickmapHeight the default height for new pickmaps
     * @param parent the parent component
     */
    public NewMapDialogFactory(@NotNull final MapViewsManager<G, A, R> mapViewsManager, @NotNull final MapArchObjectFactory<A> mapArchObjectFactory, final int defaultMapWidth, final int defaultMapHeight, final int defaultMapDifficulty, final boolean showMapDifficulty, final boolean showMapSizeDefault, final int defaultPickmapWidth, final int defaultPickmapHeight, @NotNull final Component parent) {
        this.mapViewsManager = mapViewsManager;
        this.mapArchObjectFactory = mapArchObjectFactory;
        this.defaultMapWidth = defaultMapWidth;
        this.defaultMapHeight = defaultMapHeight;
        this.defaultMapDifficulty = defaultMapDifficulty;
        this.showMapSizeDefault = showMapSizeDefault;
        this.showMapDifficulty = showMapDifficulty;
        this.defaultPickmapWidth = defaultPickmapWidth;
        this.defaultPickmapHeight = defaultPickmapHeight;
        this.parent = parent;
    }

    @Deprecated
    public void setPickmapChooserControl(@NotNull final PickmapChooserControl<G, A, R> pickmapChooserControl) {
        this.pickmapChooserControl = pickmapChooserControl;
    }

    @Deprecated
    public void setObjectChooser(@NotNull final ObjectChooser<G, A, R> objectChooser) {
        this.objectChooser = objectChooser;
    }

    /**
     * Shows a dialog for creating a new map.
     */
    @ActionMethod
    public void newMap() {
        new NewMapDialog<G, A, R>(mapViewsManager, mapArchObjectFactory, parent, showMapSizeDefault, showMapDifficulty, defaultMapWidth, defaultMapHeight, defaultMapDifficulty);
    }

    /**
     * Shows a dialog for creating a new pickmap.
     */
    public void showNewPickmapDialog() {
        new NewPickmapDialog<G, A, R>(objectChooser, parent, defaultPickmapWidth, defaultPickmapHeight, pickmapChooserControl);
    }

    /**
     * Shows a dialog for creating a new pickmap folder.
     * @param mapFolderTree the model to add the pickmap folder to
     * @param parent the parent folder to add the pickmap folder to
     */
    public void showNewPickmapFolderDialog(@NotNull final MapFolderTree<G, A, R> mapFolderTree, @Nullable final MapFolder<G, A, R> parent) {
        new NewPickmapFolderDialog<G, A, R>(this.parent, mapFolderTree, parent, mapViewsManager);
    }

}
