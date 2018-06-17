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

package net.sf.gridarta.gui.dialog.mapproperties;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating map property dialog instances.
 * @author Andreas Kirschbaum
 */
public interface MapPropertiesDialogFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Show a dialog querying the user for map properties.
     * @param parent the parent component to show dialog on
     * @param helpParent the parent frame for help windows
     * @param mapModel the map to show dialog about
     * @param mapFileFilter the Swing file filter to use
     */
    void showDialog(@NotNull Component parent, @NotNull JFrame helpParent, @NotNull MapModel<G, A, R> mapModel, @NotNull FileFilter mapFileFilter);

}
