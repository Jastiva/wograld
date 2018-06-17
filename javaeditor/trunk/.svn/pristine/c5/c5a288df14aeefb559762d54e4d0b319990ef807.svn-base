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

package net.sf.gridarta.var.daimonin.gui.mappropertiesdialog;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.gui.dialog.mapproperties.MapPropertiesDialogFactory;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.var.daimonin.model.archetype.Archetype;
import net.sf.gridarta.var.daimonin.model.gameobject.GameObject;
import net.sf.gridarta.var.daimonin.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapPropertiesDialogFactory} creating Crossfire map properties
 * dialogs.
 * @author Andreas Kirschbaum
 */
public class DefaultMapPropertiesDialogFactory implements MapPropertiesDialogFactory<GameObject, MapArchObject, Archetype> {

    /**
     * The {@link MapManager} to use.
     */
    @NotNull
    private final MapManager<GameObject, MapArchObject, Archetype> mapManager;

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapPathNormalizer} for converting map paths to {@link
     * java.io.File Files}.
     */
    @NotNull
    private final MapPathNormalizer mapPathNormalizer;

    /**
     * Creates a new instance.
     * @param mapManager the map manager to use
     * @param globalSettings the global settings instance
     * @param mapPathNormalizer the map path normalize for converting map paths
     * to files
     */
    public DefaultMapPropertiesDialogFactory(@NotNull final MapManager<GameObject, MapArchObject, Archetype> mapManager, @NotNull final GlobalSettings globalSettings, @NotNull final MapPathNormalizer mapPathNormalizer) {
        this.mapManager = mapManager;
        this.globalSettings = globalSettings;
        this.mapPathNormalizer = mapPathNormalizer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDialog(@NotNull final Component parent, @NotNull final JFrame helpParent, @NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final FileFilter mapFileFilter) {
        final MapPropertiesDialog pane = new MapPropertiesDialog(helpParent, mapManager, globalSettings, mapModel, mapFileFilter, mapPathNormalizer);
        pane.showDialog(parent);
    }

}
