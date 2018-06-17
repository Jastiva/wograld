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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JMenu;
import net.sf.gridarta.model.filter.Filter;
import net.sf.gridarta.model.filter.FilterConfig;
import net.sf.gridarta.model.filter.NamedFilter;
import net.sf.gridarta.model.filter.NamedFilterChangeType;
import net.sf.gridarta.model.filter.NamedFilterConfig;
import net.sf.gridarta.model.filter.NamedFilterListener;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class FilterMenuEntry extends JMenu implements NamedFilterListener {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(NamedFilter.class);

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    private final NamedFilterConfig config;

    @NotNull
    private final Collection<FilterComponent> components = new ArrayList<FilterComponent>();

    public FilterMenuEntry(@NotNull final NamedFilterConfig config) {
        super("");
        this.config = config;
        config.getFilter().addFilterListener(this);
        populateComponent(this);
    }

    private void populateComponent(@NotNull final JComponent component) {
        final FilterComponent filterComponent = new FilterComponent(component, config);
        for (final Map.Entry<String, FilterConfig<?, ?>> entry : config.getEntries().entrySet()) {
            filterComponent.addFilter(entry.getKey(), entry.getValue());
        }
        components.add(filterComponent);
    }

    @NotNull
    public JComponent getMenuBarComponent() {
        final JComponent menu = new JMenu(getName());
        populateComponent(menu);
        return menu;
    }

    @NotNull
    public JComponent getMenuItemComponent() {
        final JComponent menu = new JMenu(getName());
        populateComponent(menu);
        return menu;
    }

    @NotNull
    public JComponent getToolbarComponent() {
        return new BtnPopup((JMenu) getMenuItemComponent(), getName()).getButton();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nameFilterChanged(@NotNull final NamedFilterChangeType type, @NotNull final String filterName, @NotNull final Filter<?, ?> filter) {
        switch (type) {
        case ADD:
            for (final FilterComponent filterComponent : components) {
                if (log.isDebugEnabled()) {
                    log.debug("set sub filter enabled(), calling on " + filterName);
                }
                config.setSubFilterEnabled(filterName, false);
                filterComponent.addFilter(filterName, config.getConfig(filterName));
            }
            break;

        case REMOVE:
            for (final FilterComponent filterComponent : components) {
                filterComponent.removeFilter(filterName);
            }
            break;
        }
    }

}
