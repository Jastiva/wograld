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

package net.sf.gridarta.gui.misc;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.data.NamedObjects;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.DefaultMapControl;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcontrol.MapControlListener;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <code>StatusBar</code> implements the main status bar of the application.
 * Used to show one line text messages to the user about progress, state etc.
 * Also includes level info and memory info panels.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 * @todo Separate labels and methods for mouse coordinates
 */
public class StatusBar<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link ArchetypeSet}.
     * @serial
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * The {@link NamedObjects} instance to use.
     * @serial
     */
    @NotNull
    private final NamedObjects<FaceObject> faceObjects;

    /**
     * The label that shows the mouse.
     * @serial
     */
    private final JLabel mouse;

    /**
     * The label that shows the cursor.
     * @serial
     */
    private final JLabel cursor;

    /**
     * The label that shows the one line text message.
     * @serial
     */
    private final JLabel status;

    /**
     * The label that shows the memory status.
     * @serial
     */
    private final JLabel memory;

    /**
     * Temporary used to get map coordinates.
     */
    private final Point mouseMapTmp = new Point();

    /**
     * The map view for which {@link #mapCursorListener} is registered, or
     * <code>null</code> if none is registered.
     */
    @Nullable
    private MapView<G, A, R> mapView;

    /**
     * The map cursor listener to detect map cursor changes.
     */
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            mapCursorChanged(mapView == null ? null : mapView.getMapCursor());
        }

        @Override
        public void mapCursorChangedMode() {
            mapCursorChanged(mapView == null ? null : mapView.getMapCursor());
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            // ignore
        }

    };

    /**
     * The map manager listener to detect current map changes.
     */
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            setStatusText("Creating new map " + mapControl.getMapModel().getMapArchObject().getMapName() + ".");
            mapControl.addMapControlListener(mapControlListener);
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            mapControl.removeMapControlListener(mapControlListener);
        }

    };

    /**
     * The map view manager listener to detect current map changes.
     */
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            setCurrentMapView(mapView);
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

    /**
     * The {@link MapControlListener} used to detect saved maps.
     */
    private final MapControlListener<G, A, R> mapControlListener = new MapControlListener<G, A, R>() {

        @Override
        public void saved(@NotNull final DefaultMapControl<G, A, R> mapControl) {
            final String mapType = mapControl.isPickmap() ? "map" : "pickmap";
            setStatusText("Saved " + mapType + " '" + mapControl.getMapModel().getMapArchObject().getMapName() + "'.");
        }

    };

    /**
     * The {@link MouseMotionListener} for tracking the mouse position in map
     * windows.
     */
    @NotNull
    private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

        @Override
        public void mouseDragged(@NotNull final MouseEvent e) {
            mousePosChanged(e);
        }

        @Override
        public void mouseMoved(@NotNull final MouseEvent e) {
            mousePosChanged(e);
        }

    };

    /**
     * The action listener which is registered to periodically update the status
     * bar.
     */
    private final ActionListener statusBarUpdate = new ActionListener() {

        @Override
        public void actionPerformed(final ActionEvent e) {
            final int archetypeCount = archetypeSet.getArchetypeCount();

            final int faceObjectsCount = faceObjects.size();

            final Runtime runtime = Runtime.getRuntime();
            final long freeMem = runtime.freeMemory();
            final long totMem = runtime.totalMemory();
            final long usedMem = totMem - freeMem;

            memory.setText(ACTION_BUILDER.format("memory", archetypeCount, faceObjectsCount, getMemoryString(usedMem), getMemoryString(freeMem), getMemoryString(totMem)));
        }

    };

    /**
     * Constructs a status bar that has the given main controller object set as
     * its controller.
     * @param mapManager the map manager
     * @param mapViewManager the map view manager
     * @param archetypeSet the archetype set
     * @param faceObjects the named objects instance to use
     */
    public StatusBar(@NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final NamedObjects<FaceObject> faceObjects) {
        this.archetypeSet = archetypeSet;
        this.faceObjects = faceObjects;
        setLayout(new GridBagLayout());
        setBorder(new BevelBorder(BevelBorder.LOWERED));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 1;

        gbc.weightx = 0.0;
        mouse = new JLabel(" ");
        mouse.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(mouse, gbc);

        cursor = new JLabel(" ");
        cursor.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(cursor, gbc);

        gbc.weightx = 5.0;
        status = new JLabel(" ");
        status.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(status, gbc);

        gbc.weightx = 0.0;
        memory = new JLabel(" ");
        memory.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(memory, gbc);

        mapManager.addMapManagerListener(mapManagerListener);
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);

        new Timer(5000, statusBarUpdate).start();
    }

    /**
     * Sets the level status text, which usually displays arch numbers.
     * @param text the text
     */
    public void setStatusText(final String text) {
        status.setText(text);
    }

    /**
     * The DecimalFormat to use for formatting the numbers in {@link
     * #getMemoryString(long)}..
     */
    private static final NumberFormat FORMAT = NumberFormat.getInstance();

    static {
        FORMAT.setMinimumFractionDigits(1);
        FORMAT.setMaximumFractionDigits(1);
    }

    /**
     * The units used by {@link #getMemoryString(long)}.
     */
    private static final String[] UNITS = { "Bytes", "KB", "MB", "GB" };

    /**
     * Returns the given memory amount as a string scales the value to be bytes,
     * kilobytes or megabytes.
     * @param mem memory amount to calculate
     * @return String for <var>memory</var>
     */
    @Nullable
    private static String getMemoryString(final long mem) {
        for (int i = UNITS.length - 1; i >= 0; i--) {
            final long m = 1L << (long) (i * 10);
            if (mem > m) {
                return FORMAT.format((double) mem / (double) m) + UNITS[i];
            }
        }
        assert false;
        return null;
    }

    /**
     * Sets the coordinates of the {@link MapCursor} to cursor label and the
     * offset when in drag mode.
     * @param mapCursor the map cursor to set coordinates from
     */
    private void mapCursorChanged(@Nullable final MapCursor<G, A, R> mapCursor) {
        final String formatCursor;
        if (mapCursor == null) {
            formatCursor = "";
        } else {
            final Point pos = mapCursor.getLocation();
            if (pos == null) {
                formatCursor = ACTION_BUILDER.format("statusCursorInactive");
            } else {
                final int cursorX = pos.x;
                final int cursorY = pos.y;
                if (mapCursor.isDragging()) {
                    final Dimension offset = mapCursor.getDragOffset();
                    assert offset != null;
                    final int offsetX = Math.abs(offset.width) + 1;
                    final int offsetY = Math.abs(offset.height) + 1;
                    formatCursor = ACTION_BUILDER.format("statusCursorDragging", cursorX, cursorY, offsetX, offsetY);
                } else {
                    formatCursor = ACTION_BUILDER.format("statusCursorActive", cursorX, cursorY);
                }
            }
        }
        cursor.setText(formatCursor);
    }

    /**
     * Set new mouse and map coordinates to mouse label.
     * @param e Event that was fired from #MapCursor
     */
    private void mousePosChanged(@NotNull final MouseEvent e) {
        final MapRenderer renderer = (MapRenderer) e.getSource();
        final Point mouseMap = renderer.getSquareLocationAt(e.getPoint(), mouseMapTmp);
        if (mouseMap != null) {
            final int mouseMapX = mouseMap.x;
            final int mouseMapY = mouseMap.y;
            mouse.setText(ACTION_BUILDER.format("statusMouseOn", mouseMapX, mouseMapY));
        } else {
            mouse.setText(ACTION_BUILDER.format("statusMouseOff"));
        }
    }

    private void setCurrentMapView(@Nullable final MapView<G, A, R> mapView) {
        if (this.mapView != null) {
            this.mapView.getMapCursor().removeMapCursorListener(mapCursorListener);
            this.mapView.getRenderer().removeMouseMotionListener(mouseMotionListener);
        }

        this.mapView = mapView;

        if (this.mapView != null) {
            this.mapView.getMapCursor().addMapCursorListener(mapCursorListener);
            this.mapView.getRenderer().addMouseMotionListener(mouseMotionListener);
        }

        mapCursorChanged(mapView == null ? null : mapView.getMapCursor());
        mouse.setText("");
    }

}
