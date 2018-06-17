/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.gameobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class related to {@link GameObject} to store multi-part information. This
 * data is only used for multi-part objects. When the editor is running, usually
 * a big number of <code>GameObject</code>s exist - most of them single-part
 * objects. The encapsulation of this "multi-part-only" data can save a little
 * bit of memory.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class MultiArchData<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, T extends BaseObject<G, A, R, T>> implements Iterable<T>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The maximum coordinate of any part; it is never negative.
     * @serial
     */
    private int maxX;

    /**
     * The maximum coordinate of any part; it is never negative.
     * @serial
     */
    private int maxY;

    /**
     * The minimum coordinate of any part; it is never positive.
     * @serial
     */
    private int minX;

    /**
     * The minimum coordinate of any part; it is never positive.
     * @serial
     */
    private int minY;

    /**
     * The shape ID of this object.
     * @serial
     */
    private int multiShapeID;

    /**
     * All parts belonging to this multi-part object; the first element is the
     * head part.
     * @serial
     */
    @NotNull
    private final List<T> parts = new ArrayList<T>();

    /**
     * Create a new instance.
     * @param head the head part of the multi-part object
     * @param multiShapeID the shape ID of this object
     */
    public MultiArchData(@NotNull final T head, final int multiShapeID) {
        this.multiShapeID = multiShapeID;
        assert head.getArchetype().getMultiX() == 0 && head.getArchetype().getMultiY() == 0;
        parts.add(head);
    }

    /**
     * Returns the number of parts this multi-part object contains.
     * @return the number of parts this multi-part object contains
     */
    public int getMultiRefCount() {
        return parts.size();
    }

    /**
     * Determine the horizontal extent in squares. For single-part objects 1 is
     * returned.
     * @return the horizontal extent
     */
    public int getSizeX() {
        return maxX - minX + 1;
    }

    /**
     * Determine the vertical extent in squares. For single-part objects 1 is
     * returned.
     * @return the vertical extent
     */
    public int getSizeY() {
        return maxY - minY + 1;
    }

    /**
     * Determine the maximum x-coordinate of any part. The value is relative to
     * the head part. For single-part objects 0 is returned.
     * @return the maximum x-coordinate
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Determine the maximum y-coordinate of any part. The value is relative to
     * the head part. For single-part objects 0 is returned.
     * @return the maximum y-coordinate
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Determine the minimum x-coordinate of any part. The value is relative to
     * the head part. For single-part objects 0 is returned.
     * @return the minimum x-coordinate
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Determine the minimum y-coordinate of any part. The value is relative to
     * the head part. For single-part objects 0 is returned.
     * @return the minimum y-coordinate
     */
    public int getMinY() {
        return minY;
    }

    /**
     * Return the shape ID of this object.
     * @return the shape ID of this object
     */
    public int getMultiShapeID() {
        return multiShapeID;
    }

    /**
     * Set the shape ID of this object.
     * @param multiShapeID the new shape ID of this object
     */
    public void setMultiShapeID(final int multiShapeID) {
        this.multiShapeID = multiShapeID;
    }

    /**
     * Return the head part of this multi-part object.
     * @return the head part
     */
    @NotNull
    public T getHead() {
        return parts.get(0);
    }

    /**
     * Return the part following a given part.
     * @param ob the current part
     * @return the part following the current part or <code>null</code> if this
     *         part was the last.
     */
    @Nullable
    public T getNext(@NotNull final T ob) {
        final int index = parts.indexOf(ob);
        assert index != -1;
        return index + 1 < parts.size() ? parts.get(index + 1) : null;
    }

    /**
     * Add a part to this multi-part object.
     * @param tail the tail part to add
     */
    public void addPart(@NotNull final T tail) {
        parts.add(tail);

        final int x = tail.getArchetype().getMultiX();
        if (x < minX) {
            minX = x;
        } else if (x > maxX) {
            maxX = x;
        }

        final int y = tail.getArchetype().getMultiY();
        if (y < minY) {
            minY = y;
        } else if (y > maxY) {
            maxY = y;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return parts.iterator();
    }

}
