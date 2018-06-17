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

package net.sf.gridarta.gui.dialog.plugin.parameter;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * Extends JSpinner to work around it's tooltip bug This bug has been fixed
 * since Java 5.0 but we need this workaround for java 4.x users.
 * @author tchize
 */
public class TooltipSpinner extends JSpinner {

    private static final long serialVersionUID = -797350272052837471L;

    public TooltipSpinner(final SpinnerModel model) {
        super(model);
    }

    /**
     * This override the JSpinner method to force the tooltip in all
     * sub-components.
     * @param text the tooltip to show
     */
    @Override
    public void setToolTipText(final String text) {
        forceTooltip(this, text);
    }

    private void forceTooltip(final JComponent tooltipComponent, final String tooltip) {
        if (tooltipComponent == this) {
            super.setToolTipText(tooltip);
        } else {
            tooltipComponent.setToolTipText(tooltip);
        }
        final Component[] components = tooltipComponent.getComponents();
        for (final Component component : components) {
            if (component instanceof JComponent) {
                forceTooltip((JComponent) component, tooltip);
            }
        }
    }

}
