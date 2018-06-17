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

package net.sf.gridarta.gui.mainwindow;

import net.sf.gridarta.gui.panel.gameobjectattributes.ErrorListView;
import net.sf.gridarta.gui.panel.gameobjectattributes.ErrorListViewListener;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.gridarta.gui.utils.tabbedpanel.Tab;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Tab} that displays map validator results.
 * @author Andreas Kirschbaum
 */
public class WarningsTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends Tab {

    /**
     * Creates a new instance.
     * @param ident the tab's identification string
     * @param errorListView the error list view to display
     * @param location the tab's location
     * @param alternativeLocation whether the tab is shown in the alternative
     * location
     * @param index the tab's index for ordering
     * @param defaultOpen the tab's default opened status
     */
    public WarningsTab(@NotNull final String ident, @NotNull final ErrorListView<G, A, R> errorListView, @NotNull final Location location, final boolean alternativeLocation, final int index, final boolean defaultOpen) {
        super(ident, errorListView, location, alternativeLocation, index, defaultOpen);
        final ErrorListViewListener errorListViewListener = new ErrorListViewListener() {

            @Override
            public void warningsChanged(final boolean hasWarnings) {
                setSeverity(hasWarnings ? Severity.ERROR : Severity.DEFAULT);
            }

        };
        errorListView.addErrorListViewListener(errorListViewListener);
        setSeverity(errorListView.hasWarnings() ? Severity.ERROR : Severity.DEFAULT);
    }

}
