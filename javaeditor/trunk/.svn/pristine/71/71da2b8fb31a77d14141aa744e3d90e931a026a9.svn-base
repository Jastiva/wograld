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

package net.sf.gridarta.gui.dialog.goexit;

import java.awt.Component;
import java.awt.Point;
import java.util.Comparator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maplocation.MapLocation;
import net.sf.gridarta.model.maplocation.NoExitPathException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link net.sf.gridarta.gui.panel.connectionview.CellRenderer} for the
 * locked items view.
 * @author Andreas Kirschbaum
 */
public class MapListCellRenderer extends DefaultListCellRenderer implements Comparator<GameObject<?, ?, ?>> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link PathManager} for converting relative exit paths.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Creates a new instance.
     * @param pathManager the path manager for converting relative exit paths
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public MapListCellRenderer(@NotNull final PathManager pathManager, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.pathManager = pathManager;
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final GameObject<?, ?, ?> gameObject = (GameObject<?, ?, ?>) value;
        final MapLocation mapLocation = getMapLocation(gameObject);
        setIcon(faceObjectProviders.getFace(gameObject));
        final StringBuilder sb = new StringBuilder();
        sb.append(gameObject.getBestName());
        sb.append(" [");
        if (mapLocation == null) {
            sb.append("?");
        } else {
            sb.append(mapLocation.getMapPath());
            sb.append("@");
            final Point mapCoordinate = mapLocation.getMapCoordinate();
            sb.append(mapCoordinate.x);
            sb.append("/");
            sb.append(mapCoordinate.y);
        }
        sb.append("]");
        setText(sb.toString());
        return this;
    }

    /**
     * Returns the {@link MapLocation} for a {@link GameObject}.
     * @param gameObject the game object
     * @return the map location or <code>null</code> if unknown
     */
    @Nullable
    private MapLocation getMapLocation(@NotNull final GameObject<?, ?, ?> gameObject) {
        try {
            return MapLocation.newAbsoluteMapLocation(gameObject, true, pathManager);
        } catch (final NoExitPathException ignored) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(final GameObject<?, ?, ?> o1, final GameObject<?, ?, ?> o2) {
        final String name1 = o1.getBestName();
        final String name2 = o2.getBestName();
        final int cmp1 = name1.compareToIgnoreCase(name2);
        if (cmp1 != 0) {
            return cmp1;
        }

        final MapLocation mapLocation1 = getMapLocation(o1);
        final MapLocation mapLocation2 = getMapLocation(o2);
        if (mapLocation1 == null) {
            if (mapLocation2 != null) {
                return -1;
            }
        } else if (mapLocation2 == null) {
            return +1;
        } else {
            final int cmp2 = mapLocation1.compareTo(mapLocation2);
            if (cmp2 != 0) {
                return cmp2;
            }
        }

        final String faceObjName1 = o1.getFaceObjName();
        final String faceObjName2 = o2.getFaceObjName();
        if (faceObjName1 == null) {
            if (faceObjName2 != null) {
                return -1;
            }
        } else if (faceObjName2 == null) {
            return +1;
        } else {
            final int cmp3 = faceObjName1.compareTo(faceObjName2);
            if (cmp3 != 0) {
                return cmp3;
            }
        }

        return 0;
    }

}
