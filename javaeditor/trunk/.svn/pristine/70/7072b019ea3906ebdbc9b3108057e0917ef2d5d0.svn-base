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

package net.sf.gridarta.gui.misc;

import java.awt.Component;
import java.io.File;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.settings.GlobalSettingsListener;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * The main toolbar of the application.
 * @author Andreas Kirschbaum
 */
public class MainToolbar {

    /**
     * Action Builder to create Actions.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The toolbar.
     */
    private final Component toolbar;

    /**
     * The {@link GlobalSettingsListener} used to detect settings changes.
     */
    private final GlobalSettingsListener globalSettingsListener = new GlobalSettingsListener() {

        @Override
        public void showMainToolbarChanged(final boolean visible) {
            toolbar.setVisible(visible);
        }

        @Override
        public void mapsDirectoryChanged(@NotNull final File mapsDirectory) {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param globalSettings the global settings to use
     */
    public MainToolbar(@NotNull final GlobalSettings globalSettings) {
        toolbar = ACTION_BUILDER.createToolBar("main");
        globalSettings.addGlobalSettingsListener(globalSettingsListener);
        toolbar.setVisible(globalSettings.isShowMainToolbar());
    }

    /**
     * Returns the toolbar component.
     * @return the toolbar component
     */
    @NotNull
    public Component getComponent() {
        return toolbar;
    }

}
