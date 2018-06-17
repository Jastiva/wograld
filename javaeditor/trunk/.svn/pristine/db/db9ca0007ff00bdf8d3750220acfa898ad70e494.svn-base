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

package net.sf.gridarta.gui.map.event;

import java.awt.Point;
import java.util.EventObject;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import org.jetbrains.annotations.NotNull;

/**
 * A MouseOpEvent is an event triggered for a MouseOpListener. Note that
 * MouseOpEvent objects may be reused.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MouseOpEvent<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventObject {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map view on which this event occurred.
     */
    @NotNull
    private final MapView<G, A, R> mapView;

    /**
     * The cursor to use for this event.
     */
    @NotNull
    private final MapCursor<G, A, R> mapCursor;

    /**
     * The map control for this event.
     */
    @NotNull
    private final MapControl<G, A, R> mapControl;

    /**
     * The location of the point on the map model.
     */
    private Point mapLocation;

    /**
     * The pressed mouse button.
     * @see java.awt.event.MouseEvent#getButton()
     */
    private int button;

    /**
     * The type of event.
     * @see java.awt.AWTEvent#getID()
     */
    private int id;

    /**
     * The modifiers during this event.
     * @see java.awt.event.MouseEvent#getModifiers()
     */
    private int modifiers;

    /**
     * The number of consecutive mouse clicks (only valid for click events).
     */
    private int clickCount;

    /**
     * Create an empty MouseOpEvent.
     */
    public MouseOpEvent(@NotNull final MapView<G, A, R> mapView) {
        super("");
        this.mapView = mapView;
        mapCursor = mapView.getMapCursor();
        mapControl = mapView.getMapControl();
    }

    /**
     * Returns the map view on which this event occurred.
     * @return the map view
     */
    @NotNull
    public MapView<G, A, R> getMapView() {
        return mapView;
    }

    public Point getMapLocation() {
        return mapLocation;
    }

    public int getModifiers() {
        return modifiers;
    }

    @NotNull
    public MapCursor<G, A, R> getMapCursor() {
        return mapCursor;
    }

    public int getClickCount() {
        return clickCount;
    }

    /**
     * Return the mouse button that changed.
     * @return the changed mouse button
     */
    public int getButton() {
        return button;
    }

    /**
     * Sets the mouse button that changed.
     * @param button the mouse button that changed
     */
    public void setButton(final int button) {
        this.button = button;
    }

    public void setMapLocation(final Point mapLocation) {
        this.mapLocation = mapLocation;
    }

    public void setModifiers(final int modifiers) {
        this.modifiers = modifiers;
    }

    public void setClickCount(final int clickCount) {
        this.clickCount = clickCount;
    }

    @NotNull
    public MapControl<G, A, R> getMapControl() {
        return mapControl;
    }

    /**
     * Returns the event type.
     * @return the event type
     * @see java.awt.AWTEvent#getID()
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the event type.
     * @param id the event type
     */
    public void setId(final int id) {
        this.id = id;
    }

}
