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

import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for filter configurations.
 * @author tchize
 */
public abstract class AbstractFilterConfig<F extends Filter<F, C>, C extends FilterConfig<F, C>> implements FilterConfig<F, C> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(AbstractFilterConfig.class);

    /**
     * Whether the filter is enabled.
     */
    private boolean enabled;

    /**
     * The registered listeners.
     */
    @NotNull
    private final EventListenerList2<FilterConfigListener> listenerList = new EventListenerList2<FilterConfigListener>(FilterConfigListener.class);

    /**
     * The {@link Filter} this filter config belongs to.
     */
    @NotNull
    private final F filter;

    /**
     * Creates a new instance.
     * @param filter the filter this filter config belongs to
     */
    protected AbstractFilterConfig(@NotNull final F filter) {
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public F getFilter() {
        return filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;

        if (log.isDebugEnabled()) {
            log.debug((enabled ? "enabling" : "disabling") + " filter");
        }
        fireEvent(enabled ? FilterConfigChangeType.ENABLE : FilterConfigChangeType.DISABLE, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Notify all listeners that a {@link FilterConfig} has happened.
     * @param filterConfigChangeType the config event type
     * @param filterConfig the changed filter config
     */
    protected void fireEvent(@NotNull final FilterConfigChangeType filterConfigChangeType, @NotNull final FilterConfig<?, ?> filterConfig) {
        for (final FilterConfigListener listener : listenerList.getListeners()) {
            listener.configChanged(filterConfigChangeType, filterConfig);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConfigChangeListener(@NotNull final FilterConfigListener listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConfigChangeListener(@NotNull final FilterConfigListener listener) {
        listenerList.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(@NotNull final GameObject<?, ?, ?> gameObject) {
        return filter.match(getThis(), gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reset() {
        return filter.reset(getThis());
    }

    /**
     * Returns this filter config.
     * @return this filter config
     */
    @NotNull
    protected abstract C getThis();

}
