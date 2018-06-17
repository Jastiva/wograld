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

package net.sf.gridarta.gui.mapmenu;

import javax.swing.JMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.FileControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the recent menu entries.
 * @author Andreas Kirschbaum
 */
public class MapMenuManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The menu to update.
     * @serial
     */
    @Nullable
    private JMenu recentMenu;

    /**
     * The {@link MapMenu} to use.
     */
    @NotNull
    private final MapMenu mapMenu;

    /**
     * The {@link ActionFactory} for creating {@link MapMenuAction
     * MapMenuActions}.
     */
    @NotNull
    private final ActionFactory actionFactory;

    /**
     * The {@link PopupMenuListener} for updating {@link #recentMenu} when it
     * opens.
     */
    @NotNull
    private final PopupMenuListener popupMenuListener = new PopupMenuListener() {

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            final TreeNode treeNode = mapMenu.getRoot();
            if (recentMenu != null) {
                updateMenu(recentMenu, treeNode);
            }
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            // ignore
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            // ignore
        }

    };

    /**
     * Create a new instance.
     * @param mapMenu the map menu to use
     * @param mapViewsManager the map views manager for opening map files
     * @param fileControl the file control for reporting errors
     * @param mapImageCache the map image cache to query
     */
    public MapMenuManager(@NotNull final MapMenu mapMenu, @NotNull final MapViewsManager<?, ?, ?> mapViewsManager, @NotNull final FileControl<?, ?, ?> fileControl, @NotNull final MapImageCache<?, ?, ?> mapImageCache) {
        this.mapMenu = mapMenu;
        actionFactory = new ActionFactory(mapViewsManager, fileControl, mapImageCache);
    }

    /**
     * Sets the menu to update.
     * @param recentMenu the menu or <code>null</code>
     */
    public void setMenu(@Nullable final JMenu recentMenu) {
        if (this.recentMenu != null) {
            this.recentMenu.getPopupMenu().removePopupMenuListener(popupMenuListener);
        }
        this.recentMenu = recentMenu;
        if (this.recentMenu != null) {
            this.recentMenu.getPopupMenu().addPopupMenuListener(popupMenuListener);
        }
    }

    /**
     * Initializes the recent state.
     */
    public void initRecent() {
        mapMenu.load();
    }

    /**
     * Updates a {@link JMenu} from a menu instance.
     * @param menu the menu to update
     * @param root the menu instance to update from
     */
    private void updateMenu(@NotNull final JMenu menu, @NotNull final TreeNode root) {
        MenuUtils.removeAllFromSeparator(menu);
        actionFactory.begin();
        for (int i = 0; i < root.getChildCount(); i++) {
            final DefaultMutableTreeNode treeNode2 = (DefaultMutableTreeNode) root.getChildAt(i);
            final MapMenuEntryVisitor mapMenuEntryVisitor = new MapMenuEntryVisitor() {

                @Override
                public void visit(@NotNull final MapMenuEntryDir mapMenuEntry) {
                    final JMenu subMenu = new JMenu(mapMenuEntry.getTitle());
                    updateMenu(subMenu, treeNode2);
                    menu.add(subMenu);
                }

                @Override
                public void visit(@NotNull final MapMenuEntryMap mapMenuEntry) {
                    menu.add(actionFactory.getAction(mapMenuEntry));
                }

            };
            ((MapMenuEntry) treeNode2.getUserObject()).visit(mapMenuEntryVisitor);
        }
        actionFactory.end();
    }

}
