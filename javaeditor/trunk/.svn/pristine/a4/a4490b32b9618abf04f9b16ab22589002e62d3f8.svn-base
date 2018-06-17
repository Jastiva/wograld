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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mappathnormalizer.IOErrorException;
import net.sf.gridarta.model.mappathnormalizer.InvalidPathException;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.mappathnormalizer.RelativePathOnUnsavedMapException;
import net.sf.gridarta.model.mappathnormalizer.SameMapException;
import net.sf.gridarta.model.tiles.MapLink;
import net.sf.gridarta.model.tiles.TileLink;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Attaches maps to adjacent tiled maps.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class AttachTiledMaps<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The mainControl to use.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The tile links for the attach map algorithm.
     */
    @NotNull
    private final TileLink[] tileLinks;

    /**
     * The {@link MapPathNormalizer} for converting map paths to {@link File
     * Files}.
     */
    @NotNull
    private final MapPathNormalizer mapPathNormalizer;

    /**
     * Creates a new instance.
     * @param mapManager the map manager to use
     * @param tileLinks the tile links to use
     * @param mapPathNormalizer the map path normalizer for converting map paths
     * the files
     */
    public AttachTiledMaps(@NotNull final MapManager<G, A, R> mapManager, @NotNull final TileLink[] tileLinks, @NotNull final MapPathNormalizer mapPathNormalizer) {
        this.mapManager = mapManager;
        this.tileLinks = tileLinks.clone();
        this.mapPathNormalizer = mapPathNormalizer;
    }

    /**
     * Updates tile paths of a map.
     * @param mapModel the map model to update
     * @param tilePaths the new tile paths; empty slots will be filled in
     * @param mapsDirectory the maps directory
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     * @throws CannotLoadMapFileException if a map file cannot be loaded
     * @throws CannotSaveMapFileException if a map file cannot be saved
     * @throws InvalidPathNameException if a map path cannot be converted
     * @throws MapSizeMismatchException if adjacent map sizes do not match
     */
    public boolean attachTiledMaps(@NotNull final MapModel<G, A, R> mapModel, @NotNull final String[] tilePaths, @NotNull final File mapsDirectory, final boolean performAction) throws CannotLoadMapFileException, CannotSaveMapFileException, InvalidPathNameException, MapSizeMismatchException {
        if (mapModel.getMapFile() == null) {
            return false; // cannot update unsaved map
        }

        if (performAction) {
            final List<MapControl<G, A, R>> mapControls = new ArrayList<MapControl<G, A, R>>(tilePaths.length);
            try {
                // first action: we go around all links and try to load the maps
                loadAdjacentMaps(mapModel, mapControls, tilePaths);

                // We have loaded all direct linked maps around our map.
                // now, lets check free spaces.
                fillAdjacentMaps(mapControls);

                validateMapSizes(mapModel, mapControls);

                // finally... set the links!
                updateTilePaths(mapModel, mapControls, tilePaths, mapsDirectory);

                // all done! now we write all back
                saveAdjacentMaps(mapControls);
            } finally {
                for (final MapControl<G, A, R> mapControl : mapControls) {
                    if (mapControl != null) {
                        mapManager.release(mapControl);
                    }
                }
            }
        }

        return true;
    }

    /**
     * Loads adjacent map files by "filling" them by checking the "side" path
     * links of the loaded ones.
     * @param mapModel the map model
     * @param mapControls the return value
     * @param tilePaths the new tile paths
     * @throws CannotLoadMapFileException if a map file cannot be loaded
     */
    private void loadAdjacentMaps(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Collection<MapControl<G, A, R>> mapControls, @NotNull final String[] tilePaths) throws CannotLoadMapFileException {
        for (@Nullable final String tilePath : tilePaths) {
            @Nullable final MapControl<G, A, R> mapControl;
            if (tilePath != null && tilePath.length() != 0) {
                try {
                    mapControl = loadMapControl(mapModel, tilePath);
                } catch (final IOException ex) {
                    throw new CannotLoadMapFileException(tilePath, ex);
                }
            } else {
                mapControl = null;
            }
            mapControls.add(mapControl);
        }
    }

    /**
     * Saves adjacent maps. I/O errors are reported to the user but otherwise
     * ignored.
     * @param mapControls the adjacent maps to save
     * @throws CannotSaveMapFileException if the map cannot be saved
     */
    private void saveAdjacentMaps(@NotNull final Iterable<MapControl<G, A, R>> mapControls) throws CannotSaveMapFileException {
        for (final MapControl<G, A, R> mapControl : mapControls) {
            if (mapControl != null) {
                try {
                    if (mapControl.getMapModel().isModified()) {
                        mapControl.save();
                    }
                } catch (final IOException e) {
                    final File mapFile = mapControl.getMapModel().getMapFile();
                    assert mapFile != null;
                    throw new CannotSaveMapFileException(mapFile, e);
                }
            }
        }
    }

    /**
     * Fills missing adjacent map slots.
     * @param mapControls the loaded maps; will be updated
     * @throws CannotLoadMapFileException if a map file cannot be loaded
     */
    private void fillAdjacentMaps(@NotNull final List<MapControl<G, A, R>> mapControls) throws CannotLoadMapFileException {
        boolean repeatFlag = true;
        while (repeatFlag) {
            repeatFlag = false;
            for (int i = 0; i < mapControls.size(); i++) {
                final MapControl<G, A, R> mapControl = mapControls.get(i);
                if (mapControl != null) {
                    final TileLink tileLink = tileLinks[i];
                    final MapLink[] mapLinks = tileLink.getMapLinks();
                    for (final MapLink mapLink : mapLinks) {
                        final Direction direction = mapLink.getMapDirection();
                        if (mapControls.get(direction.ordinal()) == null) {
                            final String tilePath = mapControl.getMapModel().getMapArchObject().getTilePath(mapLink.getLinkDirection());
                            if (tilePath.length() > 0) {
                                try {
                                    mapControls.set(direction.ordinal(), loadMapControl(mapControl.getMapModel(), tilePath));
                                    repeatFlag = true;
                                } catch (final IOException ex) {
                                    throw new CannotLoadMapFileException(tilePath, ex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Validates all links to check that attached maps have matching
     * width/height. Reports an error to the user if a non-matching size is
     * detected.
     * @param mapModel the map model
     * @param mapControls the attached maps to check
     * @throws MapSizeMismatchException if a mismatched map size was detected
     */
    private void validateMapSizes(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Iterable<MapControl<G, A, R>> mapControls) throws MapSizeMismatchException {
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        for (final MapControl<G, A, R> mapControl : mapControls) {
            if (mapControl != null) {
                final MapArchObject<A> otherMap = mapControl.getMapModel().getMapArchObject();
                final Size2D otherMapSize = otherMap.getMapSize();
                if (!mapSize.equals(otherMapSize)) {
                    throw new MapSizeMismatchException(mapControl.getMapModel().getMapFile(), mapSize, otherMapSize);
                }
                for (int ii = 0; ii < 2; ii++) {
                    // TODO: check links
                }
            }
        }
    }

    /**
     * Updates map tile paths to match the loaded maps.
     * @param mapModel the map model
     * @param mapControls the loaded maps
     * @param tilePaths the tile paths to update
     * @param mapsDirectory the maps directory
     * @throws CannotSaveMapFileException if a map file cannot be saved
     * @throws InvalidPathNameException if a map path cannot be converted
     */
    private void updateTilePaths(@NotNull final MapModel<G, A, R> mapModel, @NotNull final List<MapControl<G, A, R>> mapControls, @NotNull final String[] tilePaths, @NotNull final File mapsDirectory) throws CannotSaveMapFileException, InvalidPathNameException {
        for (int i = 0; i < tilePaths.length; i++) {
            final MapControl<G, A, R> mapControl = mapControls.get(i);
            if (mapControl != null) {
                // generate a valid path relative to both map positions
                final File mapFile1 = mapModel.getMapFile();
                final String canonicalMapPath1;
                try {
                    canonicalMapPath1 = mapFile1.getCanonicalPath();
                } catch (final IOException ex) {
                    throw new InvalidPathNameException(mapFile1, ex);
                }
                final File mapFile2 = mapControl.getMapModel().getMapFile();
                final String canonicalMapPath2;
                try {
                    canonicalMapPath2 = mapFile2.getCanonicalPath();
                } catch (final IOException ex) {
                    throw new InvalidPathNameException(mapFile2, ex);
                }
                final String link1 = getTilePath(canonicalMapPath1, canonicalMapPath2, mapsDirectory);
                // set the link of our source map to the map around
                tilePaths[i] = link1;
                // generate again a valid path relative to both map positions
                final String link2 = getTilePath(canonicalMapPath2, canonicalMapPath1, mapsDirectory);
                final MapModel<G, A, R> mapModel2 = mapControl.getMapModel();
                mapModel2.beginTransaction("Attach maps");
                try {
                    final MapArchObject<A> mapArchObject2 = mapModel2.getMapArchObject();
                    mapArchObject2.beginTransaction();
                    try {
                        mapArchObject2.setTilePath(tileLinks[i].getRevLink(), link2 == null ? "" : link2);
                    } finally {
                        mapArchObject2.endTransaction();
                    }
                } finally {
                    mapModel2.endTransaction();
                }
            }
        }
    }

    /**
     * Loads an adjacent {@link MapControl}, ignoring any (I/O-)errors.
     * @param mapModel the map model for relative map paths
     * @param path the map path
     * @return the map control or <code>null</code> if the map cannot be loaded
     * @throws IOException if an I/O error occurs
     */
    @Nullable
    private MapControl<G, A, R> loadMapControl(@NotNull final MapModel<G, A, R> mapModel, @NotNull final String path) throws IOException {
        final File file;
        try {
            file = mapPathNormalizer.normalizeMapPath(mapModel, path);
        } catch (final InvalidPathException ignored) {
            return null;
        } catch (final IOErrorException ignored) {
            return null;
        } catch (final RelativePathOnUnsavedMapException ignored) {
            return null;
        } catch (final SameMapException ignored) {
            return null;
        }
        return mapManager.openMapFile(file, false);
    }

    /**
     * Returns the map path for a tile of a map and a tile path.
     * @param base the map path of the map
     * @param link the tile path of the map; may be relative to
     * <code>base</code>
     * @param mapsDirectory the maps directory
     * @return the map path
     * @throws CannotSaveMapFileException if an I/O error occurs
     */
    @Nullable
    private static String getTilePath(@NotNull final String base, @NotNull final String link, @NotNull final File mapsDirectory) throws CannotSaveMapFileException {
        final int pos = getLastSlashIndex(base);
        final int pos2 = getLastSlashIndex(link);
        final CharSequence mapDirectory;
        try {
            mapDirectory = mapsDirectory.getCanonicalPath();
        } catch (final IOException e) {
            throw new CannotSaveMapFileException(mapsDirectory, e);
        }
        final String first = base.substring(mapDirectory.length(), pos).trim();
        final String second = link.substring(mapDirectory.length(), pos2).trim();

        final String sep = Matcher.quoteReplacement(File.separator);

        /* our map is in root - second is higher or same map */
        if (first.length() == 0) {
            return link.substring(mapDirectory.length()).trim().replaceAll(sep, "/");
        }
        // same folder... we return the name without '/'
        if (first.compareTo(second) == 0) {
            return link.substring(pos2 + 1).trim().replaceAll(sep, "/");
        }
        // second is sub-folder of first
        if (second.startsWith(first)) {
            return link.substring(pos + 1).trim().replaceAll(sep, "/");
        }
        // in any other case we return a absolute path
        return link.substring(mapDirectory.length()).trim().replaceAll(sep, "/");
    }

    /**
     * Returns the index of the last '/' or {@link File#separator} within a
     * string.
     * @param base the string
     * @return the index or <code>-1</code> if not found
     */
    private static int getLastSlashIndex(@NotNull final String base) {
        final int slashIndex1 = base.lastIndexOf('/');
        final int separatorIndex1 = base.lastIndexOf(File.separator);
        int pos = slashIndex1;
        if (separatorIndex1 > slashIndex1) {
            pos = separatorIndex1;
        }
        return pos;
    }

}
