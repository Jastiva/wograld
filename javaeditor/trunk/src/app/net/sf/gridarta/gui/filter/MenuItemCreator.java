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

package net.sf.gridarta.gui.filter;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import net.sf.gridarta.model.filter.FilterConfig;
import net.sf.gridarta.model.filter.FilterConfigVisitor;
import net.sf.gridarta.model.filter.NamedFilterConfig;
import net.sf.gridarta.model.filter.NamedGameObjectMatcherFilterConfig;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Creates menu items for {@link net.sf.gridarta.model.filter.Filter}
 * instances.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class MenuItemCreator {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link JMenuItem}.
     */
    @Nullable
    private JMenuItem menuItem;

    /**
     * The {@link FilterConfigVisitor} for creating the menu item.
     */
    @NotNull
    private final FilterConfigVisitor filterVisitor = new FilterConfigVisitor() {

        @Override
        public void visit(@NotNull final NamedFilterConfig filterConfig) {
            menuItem = new FilterMenuEntry(filterConfig);
        }

        @Override
        public void visit(@NotNull final NamedGameObjectMatcherFilterConfig filterConfig) {
            menuItem = new JCheckBoxMenuItem(ACTION_BUILDER.createToggle(false, "enabled", filterConfig));
        }

    };

    /**
     * Creates a new instance.
     * @param config the filter config to use
     */
    public MenuItemCreator(@NotNull final FilterConfig<?, ?> config) {
        config.accept(filterVisitor);
        assert menuItem != null;
    }

    /**
     * Returns the menu item.
     * @return the menu item
     */
    @NotNull
    @SuppressWarnings("NullableProblems")
    public JMenuItem getMenuItem() {
        assert menuItem != null;
        return menuItem;
    }

}
