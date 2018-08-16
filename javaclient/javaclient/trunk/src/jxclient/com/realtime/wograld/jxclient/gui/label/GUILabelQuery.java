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

package com.realtime.wograld.jxclient.gui.label;

import com.realtime.wograld.jxclient.gui.gui.GUIElementListener;
import com.realtime.wograld.jxclient.gui.gui.TooltipManager;
import com.realtime.wograld.jxclient.server.wograld.WograldQueryListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import java.awt.Color;
import java.awt.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link GUIHTMLLabel} that displays the last received "query" command.
 * @author Andreas Kirschbaum
 */
public class GUILabelQuery extends GUIMultiLineLabel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * The {@link WograldServerConnection} to monitor.
     */
    @NotNull
    private final WograldServerConnection wograldServerConnection;

    /**
     * The {@link WograldQueryListener} registered to receive query commands.
     */
    @NotNull
    private final WograldQueryListener wograldQueryListener = new WograldQueryListener() {

        @Override
        public void commandQueryReceived(@NotNull final String prompt, final int queryType) {
            setText(prompt);
        }

    };

    /**
     * Creates a new instance.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param wograldServerConnection the connection instance
     * @param font the font to use
     * @param color the color to use
     * @param backgroundColor the background color
     */
    public GUILabelQuery(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, @NotNull final WograldServerConnection wograldServerConnection, @NotNull final Font font, @NotNull final Color color, @Nullable final Color backgroundColor) {
        super(tooltipManager, elementListener, name, null, font, color, backgroundColor, Alignment.LEFT, "");
        this.wograldServerConnection = wograldServerConnection;
        this.wograldServerConnection.addWograldQueryListener(wograldQueryListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();
        wograldServerConnection.removeWograldQueryListener(wograldQueryListener);
    }

}
