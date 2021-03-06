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

package com.realtime.wograld.jxclient.gui.commands;

import com.realtime.wograld.jxclient.gui.item.GUIItemItem;
import com.realtime.wograld.jxclient.items.CfItem;
import com.realtime.wograld.jxclient.queue.CommandQueue;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The executable commands.
 * @author Andreas Kirschbaum
 */
public enum CommandType {

    /**
     * Apply an item.
     */
    APPLY {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            wograldServerConnection.sendApply(item.getTag());
        }
    },

    /**
     * Drop an item (to the ground of into an opened container).
     */
    DROP {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            if (item.isLocked()) {
                wograldServerConnection.drawInfo("This item is locked. To drop it, first unlock by SHIFT+left-clicking on it.", 3);
            } else {
                commandQueue.sendMove(floor, item.getTag());
            }
        }
    },

    /**
     * Examine an item.
     */
    EXAMINE {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            wograldServerConnection.sendExamine(item.getTag());
        }
    },

    /**
     * Lock an item.
     */
    LOCK {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            wograldServerConnection.sendLock(true, item.getTag());
        }
    },

    /**
     * Toggle the lock of an item.
     */
    LOCK_TOGGLE {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            wograldServerConnection.sendLock(!item.isLocked(), item.getTag());
        }
    },

    /**
     * Mark an item.
     */
    MARK {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            wograldServerConnection.sendMark(item.getTag());
        }
    },

    /**
     * Unlock an item.
     */
    UNLOCK {
        /** {@inheritDoc} */
        @Override
        protected void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
            wograldServerConnection.sendLock(false, item.getTag());
        }
    },;

    /**
     * Returns whether the action can be executed.
     * @param guiItem the item to check for
     * @return whether the action can be executed
     */
    public static boolean canExecute(@Nullable final GUIItemItem guiItem) {
        return guiItem != null && guiItem.getItem() != null;
    }

    /**
     * Executes the action.
     * @param guiItem the item to execute on
     * @param wograldServerConnection the server connection to use
     * @param floor the current floor index
     * @param commandQueue the command queue to use
     */
    public void execute(@Nullable final GUIItemItem guiItem, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue) {
        if (guiItem == null) {
            return;
        }

        final CfItem item = guiItem.getItem();
        if (item == null) {
            return;
        }

        doExecute(item, wograldServerConnection, floor, commandQueue);
    }

    /**
     * Executes the action.
     * @param item the item to execute on
     * @param wograldServerConnection the server connection to use
     * @param floor the current floor index
     * @param commandQueue the command queue to use
     */
    protected abstract void doExecute(@NotNull final CfItem item, @NotNull final WograldServerConnection wograldServerConnection, final int floor, @NotNull final CommandQueue commandQueue);

}
