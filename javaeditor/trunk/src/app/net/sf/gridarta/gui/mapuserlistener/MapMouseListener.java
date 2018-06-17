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

package net.sf.gridarta.gui.mapuserlistener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import net.sf.gridarta.gui.map.event.MouseOpEvent;
import net.sf.gridarta.gui.map.event.MouseOpListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.gui.panel.tools.ToolPalette;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks mouse actions and calls the appropriate {@link MouseOpListener
 * MouseOpListeners}.
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 * @author Andreas Kirschbaum
 */
public class MapMouseListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ToolPalette} for mapping mouse events to {@link
     * MouseOpListener actions}.
     */
    @NotNull
    private final ToolPalette<G, A, R> toolPalette;

    /**
     * The {@link MapRenderer} being tracked for mouse actions.
     */
    @NotNull
    private final MapRenderer renderer;

    /**
     * Temporary point. Stored in a field to avoid frequent reallocation.
     */
    @NotNull
    private final Point tmpPoint = new Point();

    /**
     * The parameters for the currently processed event. Stored in a field to
     * avoid frequent reallocation.
     */
    @NotNull
    private final MouseOpEvent<G, A, R> mouseOpEvent;

    /**
     * The {@link MouseListener} attached to {@link #renderer}.
     */
    @NotNull
    private final MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(@NotNull final MouseEvent e) {
            final MouseOpListener<G, A, R> mouseOpListener = getMouseOperation(e);
            if (mouseOpListener != null) {
                mouseOpListener.clicked(mouseOpEvent);
            }
        }

        @Override
        public void mousePressed(@NotNull final MouseEvent e) {
            final MouseOpListener<G, A, R> mouseOpListener = getMouseOperation(e);
            if (mouseOpListener != null) {
                mouseOpListener.pressed(mouseOpEvent);
            }
        }

        @Override
        public void mouseReleased(@NotNull final MouseEvent e) {
            final MouseOpListener<G, A, R> mouseOpListener = getMouseOperation(e);
            if (mouseOpListener != null) {
                mouseOpListener.released(mouseOpEvent);
            }
        }

        @Override
        public void mouseEntered(@NotNull final MouseEvent e) {
            // ignore
        }

        @Override
        public void mouseExited(@NotNull final MouseEvent e) {
            // ignore
        }

    };

    /**
     * The {@link MouseMotionListener} attached to {@link #renderer}.
     */
    @NotNull
    private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

        @Override
        public void mouseDragged(@NotNull final MouseEvent e) {
            final MouseOpListener<G, A, R> mouseOpListener = getMouseOperation(e);
            if (mouseOpListener != null) {
                mouseOpListener.dragged(mouseOpEvent);
            }
        }

        @Override
        public void mouseMoved(@NotNull final MouseEvent e) {
            final MouseOpListener<G, A, R> mouseOpListener = getMouseOperation(e);
            if (mouseOpListener != null) {
                mouseOpListener.moved(mouseOpEvent);
            }
        }

    };

    /**
     * Creates a new instance.
     * @param renderer the map renderer to track for mouse actions
     * @param toolPalette the tool palette for mapping mouse events to actions
     * @param mapView the map view associated with the renderer
     */
    public MapMouseListener(@NotNull final MapRenderer renderer, @NotNull final ToolPalette<G, A, R> toolPalette, @NotNull final MapView<G, A, R> mapView) {
        this.toolPalette = toolPalette;
        this.renderer = renderer;
        renderer.addMouseListener(mouseListener);
        renderer.addMouseMotionListener(mouseMotionListener);
        mouseOpEvent = new MouseOpEvent<G, A, R>(mapView);
    }

    /**
     * Must be called when this object is freed. Unregisters all listeners.
     */
    public void closeNotify() {
        renderer.removeMouseListener(mouseListener);
        renderer.removeMouseMotionListener(mouseMotionListener);
    }

    /**
     * Initializes {@link #mouseOpEvent} from a {@link MouseEvent}.
     * @param event the mouse event
     */
    private void initEvent(@NotNull final MouseEvent event) {
        mouseOpEvent.setButton(event.getButton());
        mouseOpEvent.setId(event.getID());
        mouseOpEvent.setMapLocation(getMapLocation(event));
        mouseOpEvent.setModifiers(event.getModifiersEx());
        mouseOpEvent.setClickCount(event.getClickCount());
        event.consume();
    }

    /**
     * Get the mouse operation for a MouseEvent.
     * @param event the mouse event to get mouse operation for
     * @return the mouse operation for <var>event</var>
     */
    @Nullable
    private MouseOpListener<G, A, R> getMouseOperation(@NotNull final MouseEvent event) {
        initEvent(event);
        return toolPalette.getTool(mouseOpEvent);
    }

    /**
     * Get the map location for a MouseEvent.
     * @param event the mouse event to get map location for
     * @return the map location for <var>event</var> or <code>null</code> if
     *         outside map
     */
    @Nullable
    private Point getMapLocation(@NotNull final MouseEvent event) {
        return renderer.getSquareLocationAt(event.getPoint(), tmpPoint);
    }

}
