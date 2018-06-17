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

package net.sf.gridarta.model.filter;

import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.utils.EventListenerList2;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Filter} that aggregates named filters.
 * <p/>
 * This filter aggregates a list of named sub-filters. Each sub-filter can be
 * (dis)enabled.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class NamedFilter implements Filter<NamedFilter, NamedFilterConfig> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(NamedFilter.class);

    /**
     * The sub-filters. Maps filter name to {@link Filter} instance.
     */
    @NotNull
    private final Map<String, Filter<?, ?>> subFilters;

    /**
     * The {@link NamedFilterListener NamedFilterListeners} to notify about
     * changes.
     */
    @NotNull
    private final EventListenerList2<NamedFilterListener> listenerList = new EventListenerList2<NamedFilterListener>(NamedFilterListener.class);

    /**
     * Creates a new instance.
     * @param matchers the matchers to create filters from
     */
    public NamedFilter(@NotNull final Iterable<NamedGameObjectMatcher> matchers) {
        subFilters = new LinkedHashMap<String, Filter<?, ?>>();
        for (final NamedGameObjectMatcher matcher : matchers) { // FIXME: use localized name; FIXME: sort by name
            subFilters.put(matcher.getName(), new NamedGameObjectMatcherFilter(matcher));
        }
    }

    /**
     * Disables all sub-filters.
     * @param config the filter config to reset
     */
    public void resetConfig(@NotNull final NamedFilterConfig config) {
        for (final String name : subFilters.keySet()) {
            config.setSubFilterEnabled(name, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(@NotNull final NamedFilterConfig config, @NotNull final GameObject<?, ?, ?> gameObject) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("match called on " + gameObject.getArchetype().getArchetypeName());
            }
            for (final String name : subFilters.keySet()) {
                if (log.isDebugEnabled()) {
                    log.debug("checking if filter " + name + " is enabled()");
                }
                if (config.isSubFilterEnabled(name)) {
                    log.debug("enabled!");
                    if (config.getConfig(name).match(gameObject)) {
                        log.debug("and matched!");
                        return !config.isInverted();
                    }
                }
            }
            log.debug("finished scanning sub filters");
            return config.isInverted();
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reset(@NotNull final NamedFilterConfig config) {
        try {
            boolean didMatch = false;
            for (final String name : subFilters.keySet()) {
                final FilterConfig<?, ?> filterConfig = config.getConfig(name);
                if (filterConfig.reset()) {
                    didMatch = true;
                }
            }
            return didMatch ^ config.isInverted();
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasGlobalMatch(@NotNull final NamedFilterConfig config) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public NamedFilterConfig createConfig() {
        final NamedFilterConfig filterConfig = new NamedFilterConfig(this);
        for (final Map.Entry<String, Filter<?, ?>> filterEntry : subFilters.entrySet()) {
            fireEvent(NamedFilterChangeType.ADD, filterEntry.getKey(), filterEntry.getValue());
        }
        resetConfig(filterConfig);
        return filterConfig;
    }

    /**
     * Adds a sub-{@link Filter}.
     * @param name the name of the sub-filter
     * @param filter the sub-filter
     */
    public void addFilter(@NotNull final String name, @NotNull final Filter<?, ?> filter) {
        if (subFilters.containsKey(name)) {
            return;
        }

        subFilters.put(name, filter);
        fireEvent(NamedFilterChangeType.ADD, name, filter);
    }

    /**
     * Removes a sub-filter.
     * @param name the name of the sub-filter
     */
    public void removeFilter(@NotNull final String name) {
        final Filter<?, ?> filter = subFilters.remove(name);
        if (filter == null) {
            return;
        }

        fireEvent(NamedFilterChangeType.REMOVE, name, filter);
    }

    /**
     * Notifies all listeners about a change.
     * @param type the change type
     * @param filterName the name of the sub-filter that has changed
     * @param filter the sub-filter that has changed
     */
    private void fireEvent(@NotNull final NamedFilterChangeType type, @NotNull final String filterName, @NotNull final Filter<?, ?> filter) {
        for (final NamedFilterListener listener : listenerList.getListeners()) {
            listener.nameFilterChanged(type, filterName, filter);
        }
    }

    /**
     * Adds a {@link NamedFilterListener} to be notified about changes.
     * @param listener the listener
     */
    public void addFilterListener(@NotNull final NamedFilterListener listener) {
        listenerList.add(listener);
    }

    /**
     * Removes a {@link NamedFilterListener} to be notified about changes.
     * @param listener the listener
     */
    public void removeFilterListener(@NotNull final NamedFilterListener listener) {
        listenerList.remove(listener);
    }

    /**
     * Returns whether this filter matches a {@link GameObject}.
     * @param gameObject the game object
     * @param filterOutConfig the filter config to use
     * @return whether this filter matches the game object
     */
    public boolean canShow(@NotNull final GameObject<?, ?, ?> gameObject, @NotNull final NamedFilterConfig filterOutConfig) {
        reset(filterOutConfig);
        if (hasGlobalMatch(filterOutConfig)) {
            match(filterOutConfig, gameObject);
            return !reset(filterOutConfig);
        }

        return !match(filterOutConfig, gameObject);
    }

}
