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

package net.sf.gridarta.gui.utils;

import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.MenuElement;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class implementing menu related functions.
 * @author Andreas Kirschbaum
 */
public class MenuUtils {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Private constructor to prevent instantiation.
     */
    private MenuUtils() {
    }

    /**
     * Remove all actions attached to menu entries in a given menu element and
     * its children.
     * @param menuElement the menu element to process
     */
    public static void disposeMenuElement(@NotNull final MenuElement menuElement) {
        if (menuElement instanceof AbstractButton) {
            ((AbstractButton) menuElement).setAction(null);
        }

        for (final MenuElement child : menuElement.getSubElements()) {
            disposeMenuElement(child);
        }
    }

    /**
     * Remove all menu entries.
     * @param menu the menu to remove the entries from
     */
    public static void removeAll(@NotNull final JMenu menu) {
        for (final MenuElement child : menu.getSubElements()) {
            disposeMenuElement(child);
        }
        menu.removeAll();
    }

    /**
     * Remove all menu entries up to (but not including) the first separator.
     * @param menu the menu to remove the entries from
     */
    public static void removeAllToSeparator(@NotNull final JMenu menu) {
        while (true) {
            final Component menuItem = menu.getMenuComponent(0);
            if (menuItem == null || menuItem instanceof JSeparator) {
                break;
            }
            menu.remove(0);
            if (menuItem instanceof MenuElement) {
                disposeMenuElement((MenuElement) menuItem);
            }
        }
    }

    /**
     * Remove all menu entries from (but not including) the last separator.
     * @param menu the menu to remove the entries from
     */
    public static void removeAllFromSeparator(@NotNull final JMenu menu) {
        while (true) {
            final int menuComponentCount = menu.getMenuComponentCount();
            if (menuComponentCount <= 0) {
                break;
            }
            final Component menuItem = menu.getMenuComponent(menuComponentCount - 1);
            if (menuItem == null || menuItem instanceof JSeparator) {
                break;
            }
            menu.remove(menuComponentCount - 1);
            if (menuItem instanceof MenuElement) {
                disposeMenuElement((MenuElement) menuItem);
            }
        }
    }

    /**
     * Returns a named {@link JMenu} entry of a {@link JMenuBar}.
     * @param menuBar the menu bar
     * @param name the name
     * @return the menu or <code>null</code> if not found
     */
    @Nullable
    public static JMenu getMenu(@NotNull final JMenuBar menuBar, @NotNull final String name) {
        final JMenuItem menuItem = ACTION_BUILDER.find(menuBar, name);
        return menuItem != null && menuItem instanceof JMenu ? (JMenu) menuItem : null;
    }

    /**
     * Returns a {@link JMenu} by action key.
     * @param menuElement the menu element to search for the action key
     * @param key the action key to search
     * @return the menu or <code>null</code> if not found
     */
    @Nullable
    public static JMenu findMenu(@NotNull final MenuElement menuElement, @NotNull final String key) {
        for (final MenuElement subMenuElement : menuElement.getSubElements()) {
            if (subMenuElement instanceof JMenu) {
                final JMenu subMenu = (JMenu) subMenuElement;
                if (ActionUtils.getActionId(subMenu.getAction()).equals(key)) {
                    return subMenu;
                }

                final JMenu ret = findMenu(subMenu, key);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

}
