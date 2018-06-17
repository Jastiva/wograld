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

package net.sf.gridarta.gui.mapfiles;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * An action for the entry in the pickmaps folder menu. If invoked, it sets the
 * active folder.
 * @author Andreas Kirschbaum
 */
public class MapFolderTreeAction<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The model the {@link #mapFolder} is part of.
     */
    @NotNull
    private final MapFolderTree<G, A, R> mapFolderTree;

    /**
     * The folder to activate.
     */
    @NotNull
    private final MapFolder<G, A, R> mapFolder;

    /**
     * The associated menu item.
     */
    @NotNull
    private final AbstractButton menuItem;

    /**
     * Creates a new instance.
     * @param mapFolderTree the model the <code>mapFolder</code> is part of
     * @param mapFolder the folder to activate
     * @param menuItem the associated menu item
     */
    public MapFolderTreeAction(@NotNull final MapFolderTree<G, A, R> mapFolderTree, @NotNull final MapFolder<G, A, R> mapFolder, @NotNull final AbstractButton menuItem) {
        super(mapFolder.getName());
        this.mapFolderTree = mapFolderTree;
        this.mapFolder = mapFolder;
        this.menuItem = menuItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        mapFolderTree.setActiveMapFolder(mapFolder);
        menuItem.setSelected(mapFolderTree.getActiveMapFolder() == mapFolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return !(obj == null || !(obj instanceof MapFolderTreeAction)) && mapFolder == ((MapFolderTreeAction<?, ?, ?>) obj).mapFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return mapFolder.getDir().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

}
