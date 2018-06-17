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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.filter.Filter;
import net.sf.gridarta.model.filter.FilterConfig;
import net.sf.gridarta.model.filter.FilterConfigChangeType;
import net.sf.gridarta.model.filter.FilterConfigListener;
import net.sf.gridarta.model.filter.NamedFilter;
import net.sf.gridarta.model.filter.NamedFilterConfig;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;

/**
 * Control for filters. <p/> Control to check if 1) a specific GameObject can be
 * shown (filter out of view part) 2) a specific MapSquare must be highlighted
 * (analysis part) It provides only one filterOut path (and so elements are
 * visible or not, they can't be twice visible or such). It provides 3 highlight
 * paths. each of them can be (dis)enabled. So you can, e.g., highlight walls in
 * a specific color, teleporters in another and monsters in a third. highlight
 * and filterOut filters works all the same. You can activate specific
 * predefined filters or you can provide your own If several predefined Filters
 * are activated for a path, they are combined in an OR way. For example, if you
 * enable the filterOut path, activate in this path the wall and the floor
 * filters, only walls and floors will be shown.
 * @author tchize
 */
//TODO allow implementation of own filter
public class DefaultFilterControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements FilterControl<G, A, R> {

    @NotNull
    private final NamedFilter filterList;

    @NotNull
    private final NamedFilterConfig filterOutConfig;

    @NotNull
    private final NamedFilterConfig[] highlightConfig;

    /**
     * The {@link FilterConfigListener FilterConfigListeners} to notify.
     */
    @NotNull
    private final EventListenerList2<FilterConfigListener> configListeners = new EventListenerList2<FilterConfigListener>(FilterConfigListener.class);

    @NotNull
    private final FilterConfigListener filterConfigListener = new FilterConfigListener() {

        @Override
        public void configChanged(@NotNull final FilterConfigChangeType filterConfigChangeType, @NotNull final FilterConfig<?, ?> filterConfig) {
            for (final FilterConfigListener listener : configListeners.getListeners()) {
                listener.configChanged(filterConfigChangeType, filterConfig);
            }
        }

    };

    /**
     * Create a new FilterControl. Do not highlight anything and does not
     * filterOut anything
     * @param filterList the filter list instance to use
     */
    public DefaultFilterControl(@NotNull final NamedFilter filterList) {
        this.filterList = filterList;
        filterOutConfig = filterList.createConfig();
        highlightConfig = new NamedFilterConfig[MAX_HIGHLIGHT];
        for (int i = 0; i < MAX_HIGHLIGHT; i++) {
            highlightConfig[i] = filterList.createConfig();
        }
        filterOutConfig.addConfigChangeListener(filterConfigListener);
        for (int i = 0; i < MAX_HIGHLIGHT; i++) {
            highlightConfig[i].addConfigChangeListener(filterConfigListener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConfigListener(@NotNull final FilterConfigListener listener) {
        configListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConfigListener(@NotNull final FilterConfigListener listener) {
        configListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMenuEntries(@NotNull final JMenu menu) {
        final JMenuItem menuItem = new MenuItemCreator(filterOutConfig).getMenuItem();
        menuItem.setText("Filter view");
        menu.add(menuItem);
        menu.addSeparator();
        for (int i = 0; i < MAX_HIGHLIGHT; i++) {
            final JMenuItem menuItem2 = new MenuItemCreator(highlightConfig[i]).getMenuItem();
            menuItem2.setText("Highlight " + i);
            menu.add(menuItem2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newSquare(@NotNull final FilterState filterState) {
        for (int i = 0; i < MAX_HIGHLIGHT; i++) {
            if (highlightConfig[i].isEnabled()) {
                filterList.reset(highlightConfig[i]);
            }
        }
        filterState.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHighlightedSquare(@NotNull final FilterState filterState, final int path) {
        return highlightConfig[path].isEnabled() && filterState.isHighlightedSquare(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void objectInSquare(@NotNull final FilterState filterState, @NotNull final G gameObject) {
        for (int i = 0; i < MAX_HIGHLIGHT; i++) {
            if (highlightConfig[i].isEnabled()) {
                if (!filterState.isHighlightedSquare(i)) {
                    filterState.setHighlightedSquare(i, filterList.match(highlightConfig[i], gameObject));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canShow(@NotNull final G gameObject) {
        return !filterOutConfig.isEnabled() || filterList.canShow(gameObject, filterOutConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFilter(@NotNull final String name, @NotNull final Filter<?, ?> filter) {
        filterList.addFilter(name, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFilter(@NotNull final String name) {
        filterList.removeFilter(name);
    }

}
