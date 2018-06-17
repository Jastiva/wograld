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

import java.awt.Point;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import net.sf.gridarta.gui.map.renderer.AbstractMapRenderer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.CommonConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks the {@link MapCursor} of map and scrolls the {@link
 * AbstractMapRenderer} so that the map cursor remains visible.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapCursorTracker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link MapCursor} to track.
     */
    @NotNull
    private final MapCursor<G, A, R> mapCursor;

    /**
     * The {@link AbstractMapRenderer} to update.
     */
    @NotNull
    private final AbstractMapRenderer<G, A, R> renderer;

    /**
     * The {@link JScrollPane} for this instance.
     */
    @NotNull
    private final JScrollPane scrollPane;

    /**
     * The {@link MapCursorListener} attached to {@link #mapCursor}.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            ensureVisibleMapCursor();
        }

        @Override
        public void mapCursorChangedMode() {
            // Ignore mode change events
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param isPickmap whether the map model belongs to a pickmap
     * @param viewPosition the initial view position to show; null=show top left
     * corner
     * @param xScrollDistance the x distance when scrolling
     * @param yScrollDistance the y distance when scrolling
     * @param mapCursor the map cursor to track
     * @param renderer the map renderer to update
     */
    public MapCursorTracker(final boolean isPickmap, @Nullable final Point viewPosition, final int xScrollDistance, final int yScrollDistance, @NotNull final MapCursor<G, A, R> mapCursor, @NotNull final AbstractMapRenderer<G, A, R> renderer) {
        this.mapCursor = mapCursor;
        this.renderer = renderer;
        this.mapCursor.addMapCursorListener(mapCursorListener);
        scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        if (isPickmap) {
            scrollPane.setBackground(CommonConstants.BG_COLOR);
        }

        // set the pixel increment scrolling for clicking once on a scroll bar arrow
        scrollPane.getVerticalScrollBar().setUnitIncrement(yScrollDistance);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(xScrollDistance);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollPane.setViewportView(renderer);
        if (viewPosition != null) {
            scrollPane.getViewport().setViewPosition(viewPosition);
        }
        scrollPane.setFocusable(true);
        ensureVisibleMapCursor();
    }

    /**
     * Must be called when this instance is not used anymore. It un-registers
     * all listeners.
     */
    public void closeNotify() {
        scrollPane.setViewportView(null);
        mapCursor.removeMapCursorListener(mapCursorListener);
    }

    /**
     * Makes sure the {@link #mapCursor} is visible.
     */
    private void ensureVisibleMapCursor() {
        final Point cursorLocation = mapCursor.getLocation();
        if (cursorLocation != null) {
            renderer.scrollRectToVisible(renderer.getSquareBounds(cursorLocation));
        }
    }

    /**
     * Returns the {@link JScrollPane} of the renderer.
     * @return the scroll pane
     */
    @NotNull
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

}
