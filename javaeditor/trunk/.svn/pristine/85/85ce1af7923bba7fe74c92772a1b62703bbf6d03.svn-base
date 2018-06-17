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

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.model.filter.FilterConfig;
import net.sf.gridarta.model.filter.NamedFilterConfig;

public class FilterComponent {

    private final Map<String, JComponent> content = new HashMap<String, JComponent>();

    private final Container component;

    private final NamedFilterConfig config;

    private final AbstractButton active;

    private final AbstractButton inverted;

    public FilterComponent(final Container component, final NamedFilterConfig config) {
        this.component = component;
        this.config = config;
        active = new JCheckBoxMenuItem("active");
        active.getModel().setSelected(this.config.isEnabled());
        active.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                FilterComponent.this.config.setEnabled(active.getModel().isSelected());
            }
        });
        component.add(active);
        inverted = new JCheckBoxMenuItem("invert");
        inverted.getModel().setSelected(this.config.isInverted());
        inverted.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                FilterComponent.this.config.setInverted(inverted.getModel().isSelected());
            }
        });
        component.add(inverted);
        component.add(new JSeparator());
    }

    public void addFilter(final String name, final FilterConfig<?, ?> config) {
        if (content.containsKey(name)) {
            return;
        }

        final AbstractButton entry = new MenuItemCreator(config).getMenuItem();
        entry.setVisible(true);
        entry.setText(name);
        content.put(name, entry);
        component.add(entry);
    }

    public void removeFilter(final String name) {
        if (!content.containsKey(name)) {
            return;
        }

        final Component c = content.get(name);
        content.remove(name);
        component.remove(c);
    }

}
