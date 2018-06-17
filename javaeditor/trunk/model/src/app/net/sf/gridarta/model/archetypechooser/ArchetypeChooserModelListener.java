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

package net.sf.gridarta.model.archetypechooser;

import java.util.EventListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners interested in {@link ArchetypeChooserModel} related
 * events.
 * @author Andreas Kirschbaum
 */
public interface ArchetypeChooserModelListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * The selected panel has changed.
     * @param selectedPanel the selected panel
     */
    void selectedPanelChanged(@NotNull ArchetypeChooserPanel<G, A, R> selectedPanel);

    /**
     * The selected folder has changed.
     * @param selectedFolder the selected folder
     */
    void selectedFolderChanged(@NotNull ArchetypeChooserFolder<G, A, R> selectedFolder);

    /**
     * The selected archetype has changed.
     * @param selectedArchetype the selected archetype or <code>null</code> if
     * none is selected
     */
    void selectedArchetypeChanged(@Nullable R selectedArchetype);

    /**
     * The default direction has changed.
     * @param direction the new direction; <code>null</code> means "default"
     */
    void directionChanged(@Nullable Integer direction);

}
