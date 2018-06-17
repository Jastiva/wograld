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

package net.sf.gridarta.gui.panel.archetypechooser;

import java.util.Comparator;
import javax.swing.DefaultListCellRenderer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;

/**
 * Abstract base class for classes implementing display modes of the archetype
 * chooser. It defines both a {@link DefaultListCellRenderer} and a {@link
 * Comparator} for sorting the list entries.
 * @author Andreas Kirschbaum
 * @noinspection AbstractClassExtendsConcreteClass
 */
public abstract class DisplayMode<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DefaultListCellRenderer implements Comparator<Archetype<G, A, R>> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     */
    protected DisplayMode() {
    }

    /**
     * Returns the layout orientation. See {@link javax.swing.JList#getLayoutOrientation}.
     * @return the layout orientation
     */
    public abstract int getLayoutOrientation();

}
