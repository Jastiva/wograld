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

package net.sf.gridarta.gui.map.mapactions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maplocation.MapLocation;
import net.sf.gridarta.model.maplocation.NoExitPathException;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mappathnormalizer.IOErrorException;
import net.sf.gridarta.model.mappathnormalizer.InvalidPathException;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.mappathnormalizer.RelativePathOnUnsavedMapException;
import net.sf.gridarta.model.mappathnormalizer.SameMapException;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class for entering maps.
 * @author Andreas Kirschbaum
 */
public class EnterMap<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The component for showing dialog boxes.
     */
    @NotNull
    private final Component parent;

    /**
     * Maps map relative direction to map window direction.
     */
    @NotNull
    private final Direction[] directionMap;

    /**
     * The {@link MapPathNormalizer} to use.
     */
    @NotNull
    private final MapPathNormalizer mapPathNormalizer;

    /**
     * The {@link FileControl}.
     */
    @NotNull
    private final FileControl<G, A, R> fileControl;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * Creates a new instance.
     * @param parent the component for showing dialog boxes
     * @param directionMap maps relative direction to map window direction
     * @param mapPathNormalizer the map path normalizer to use
     * @param fileControl the file control
     * @param mapViewsManager the map views
     */
    public EnterMap(@NotNull final Component parent, @NotNull final Direction[] directionMap, @NotNull final MapPathNormalizer mapPathNormalizer, @NotNull final FileControl<G, A, R> fileControl, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.parent = parent;
        this.directionMap = directionMap.clone();
        this.mapPathNormalizer = mapPathNormalizer;
        this.fileControl = fileControl;
        this.mapViewsManager = mapViewsManager;
    }

    /**
     * Enters a map wanted.
     * @param mapView the map view to leave
     * @param path path to map that should be loaded
     * @param direction the direction to go
     * @param destinationPoint the desired destination point on the map (pass
     * <code>null</code> if unknown, and note that the point gets modified)
     * @return whether the destination map has been entered
     */
    public boolean enterMap(@NotNull final MapView<G, A, R> mapView, @NotNull final String path, @NotNull final Direction direction, @Nullable final Point destinationPoint) {
        final File canonicalNewFile;
        try {
            canonicalNewFile = mapPathNormalizer.normalizeMapPath(mapView.getMapControl().getMapModel(), path);
        } catch (final InvalidPathException ex) {
            ACTION_BUILDER.showMessageDialog(parent, "enterExitInvalidPath", ex.getFile().getAbsolutePath());
            return false;
        } catch (final IOErrorException ex) {
            ACTION_BUILDER.showMessageDialog(parent, "enterTileIOException", ex.getFile().getAbsolutePath());
            return false;
        } catch (final RelativePathOnUnsavedMapException ex) {
            ACTION_BUILDER.showMessageDialog(parent, "enterExitNotSaved", ex.getMessage());
            return false;
        } catch (final SameMapException ignored) {
            // path points to the same map
            if (destinationPoint != null) {
                showLocation(mapView, destinationPoint);
            }
            return true;
        }
        return enterMap(mapView, canonicalNewFile, destinationPoint, direction);
    }

    /**
     * Enters a map.
     * @param mapView the current map view; may be closed it
     * non-<code>null</code>
     * @param mapFile the map file to enter
     * @param destinationPoint the desired destination point on the map or
     * <code>null</code> for default
     * @param direction the direction to go
     * @return whether the destination map has been entered
     */
    public boolean enterMap(@Nullable final MapView<G, A, R> mapView, @NotNull final File mapFile, @Nullable final Point destinationPoint, @NotNull final Direction direction) {
        final MapView<G, A, R> newMapView;
        try {
            newMapView = mapViewsManager.openMapFileWithView(mapFile, null, destinationPoint);
        } catch (final IOException ex) {
            fileControl.reportLoadError(mapFile, ex.getMessage());
            return false;
        }

        if (destinationPoint != null) {
            showLocation(newMapView, destinationPoint);
        } else if (mapView != null) {
            newMapView.getScrollPane().getViewport().setViewPosition(calculateNewViewPosition(mapView.getScrollPane(), newMapView.getScrollPane(), direction));
            final Point newCursorLocation = calculateNewCursorLocation(mapView, newMapView, direction);
            if (newCursorLocation != null) {
                newMapView.getMapCursor().setLocation(newCursorLocation);
            }
        }

        if (mapView != null && ACTION_BUILDER.showOnetimeConfirmDialog(parent, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, "enterExitClose") == JOptionPane.YES_OPTION) {
            // only close current map if a new file was opened and user wants to close it
            mapViewsManager.closeMapView(mapView);
        }

        return true;
    }

    /**
     * Scrolls a map view to make a give tile visible.
     * @param mapView the map view
     * @param point the square
     */
    public void showLocation(@NotNull final MapView<G, A, R> mapView, @NotNull final Point point) {
        final Point point2 = point.x == -1 && point.y == -1 ? mapView.getMapControl().getMapModel().getMapArchObject().getEnter() : point;
        if (!mapView.getMapControl().getMapModel().getMapArchObject().isPointValid(point2)) {
            ACTION_BUILDER.showMessageDialog(parent, "enterExitOutside");
        }

        mapView.setCursorLocation(point2);
    }

    /**
     * Opens the map an exit game object points to.
     * @param mapView the map view to leave
     * @param exit the game object
     * @param allowRandomMapParameters whether exit paths may point to random
     * maps
     * @return whether the destination map could be opened
     */
    public boolean enterExit(@NotNull final MapView<G, A, R> mapView, @NotNull final GameObject<G, A, R> exit, final boolean allowRandomMapParameters) {
        final MapLocation mapLocation;
        try {
            mapLocation = new MapLocation(exit, allowRandomMapParameters);
        } catch (final NoExitPathException ex) {
            ACTION_BUILDER.showMessageDialog(parent, "enterExitRandomDestination", ex.getMessage());
            return false;
        }

        return enterMap(mapView, mapLocation.getMapPath(), Direction.NORTH, mapLocation.getMapCoordinate());
    }

    /**
     * Calculate the view position for the new viewport.
     * @param oldMapView the old map view
     * @param newMapView the new map view
     * @param direction the direction to scroll
     * @return the new view position
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    private Point calculateNewViewPosition(@NotNull final JScrollPane oldMapView, @NotNull final JScrollPane newMapView, @NotNull final Direction direction) {
        final Dimension newViewSize = newMapView.getViewport().getViewSize();
        final Rectangle oldViewRectangle = oldMapView.getViewport().getViewRect();

        final Rectangle scrollTo;
        switch (directionMap[direction.ordinal()]) {
        case SOUTH:
            scrollTo = new Rectangle(oldViewRectangle.x, 0, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case NORTH:
            scrollTo = new Rectangle(oldViewRectangle.x, newViewSize.height - oldViewRectangle.height, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case EAST:
            scrollTo = new Rectangle(0, oldViewRectangle.y, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case WEST:
            scrollTo = new Rectangle(newViewSize.width - oldViewRectangle.width, oldViewRectangle.y, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case NORTH_EAST:
            scrollTo = new Rectangle(0, newViewSize.height - oldViewRectangle.height, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case SOUTH_EAST:
            scrollTo = new Rectangle(0, 0, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case SOUTH_WEST:
            scrollTo = new Rectangle(newViewSize.width - oldViewRectangle.width, 0, oldViewRectangle.width, oldViewRectangle.height);
            break;

        case NORTH_WEST:
            scrollTo = new Rectangle(newViewSize.width - oldViewRectangle.width, newViewSize.height - oldViewRectangle.height, oldViewRectangle.width, oldViewRectangle.height);
            break;

        default:
            throw new AssertionError();
        }

        if (scrollTo.x + scrollTo.width > newViewSize.width) {
            scrollTo.x = newViewSize.width - scrollTo.width;
        }
        if (scrollTo.x < 0) {
            scrollTo.x = 0;
        }
        if (scrollTo.y + scrollTo.height > newViewSize.height) {
            scrollTo.y = newViewSize.height - scrollTo.height;
        }
        if (scrollTo.y < 0) {
            scrollTo.y = 0;
        }
        return scrollTo.getLocation();
    }

    /**
     * Calculate the map cursor location for the new viewport.
     * @param oldMapView the old map view
     * @param newMapView the new map view
     * @param direction the direction to scroll
     * @return the new map cursor location
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    private Point calculateNewCursorLocation(@Nullable final MapView<G, A, R> oldMapView, @NotNull final MapView<G, A, R> newMapView, @NotNull final Direction direction) {
        if (oldMapView == null) {
            return null;
        }
        final Point oldCursorLocation = oldMapView.getMapCursor().getLocation();
        if (oldCursorLocation == null) {
            return null;
        }

        final Size2D mapSize = newMapView.getMapControl().getMapModel().getMapArchObject().getMapSize();
        switch (direction) {
        case SOUTH:
            return new Point(oldCursorLocation.x, 0);

        case NORTH:
            return new Point(oldCursorLocation.x, mapSize.getHeight() - 1);

        case EAST:
            return new Point(0, oldCursorLocation.y);

        case WEST:
            return new Point(mapSize.getWidth() - 1, oldCursorLocation.y);

        case NORTH_EAST:
            return new Point(0, mapSize.getHeight() - 1);

        case SOUTH_EAST:
            return new Point(0, 0);

        case SOUTH_WEST:
            return new Point(mapSize.getWidth() - 1, 0);

        case NORTH_WEST:
            return new Point(mapSize.getWidth() - 1, mapSize.getHeight() - 1);

        default:
            throw new AssertionError();
        }
    }

}
