/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.items;

import java.util.EventListener;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for listeners for changes of item locations.
 * @author Andreas Kirschbaum
 */
public interface ItemListener extends EventListener {

    /**
     * The watched item has changed.
     * @param tag the watched item's tag
     */
    void itemChanged(int tag);

    /**
     * The watched item has been removed from the location.
     * @param tag the watched item's tag
     */
    void itemRemoved(int tag);

    /**
     * An inventory {@link CfItem} has been added to the watched item.
     * @param tag the watched item's tag
     * @param index the inventory index
     * @param item the inventory item
     */
    void inventoryAdded(int tag, int index, @NotNull CfItem item);

    /**
     * An inventory item has been removed from the watched item.
     * @param tag the watched item's tag
     * @param index the inventory index
     */
    void inventoryRemoved(int tag, int index);

}
