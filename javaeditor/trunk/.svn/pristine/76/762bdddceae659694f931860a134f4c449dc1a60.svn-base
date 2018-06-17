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

package net.sf.gridarta.updater;

import java.awt.Component;
import java.util.prefs.Preferences;
import javax.swing.Action;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Exiter;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * This class handles updating the map editor.
 * @author <a href="mailto:cher@riedquat.de">Christian.Hujer</a>
 */
public class UpdaterManager {

    /**
     * Action Builder to create Actions.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Preferences.
     */
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * Preferences key whether to automatically check for updates.
     */
    public static final String AUTO_CHECK_KEY = "UpdaterAutoCheck";

    /**
     * Preferences default value for AUTO_CHECK_KEY.
     */
    public static final boolean AUTO_CHECK_DEFAULT = false;

    /**
     * Preferences key for selected update interval.
     */
    public static final String INTERVAL_KEY = "UpdaterInterval";

    /**
     * Preferences default value for INTERVAL_KEY.
     */
    public static final int INTERVAL_DEFAULT = 2;

    /**
     * The times for update calculation.
     */
    private static final long[] UPDATE_TIMES = { 0L,  // Every startup
        86400000L,  // Once a day
        604800000L,  // Once a week
        2419200000L,  // Once a month
    };

    /**
     * The {@link Exiter} for terminating the application.
     */
    @NotNull
    private final Exiter exiter;

    /**
     * The map manager to use.
     */
    private final MapManager<?, ?, ?> mapManager;

    /**
     * The component to show dialogs on.
     */
    private final Component parentComponent;

    /**
     * The file to update.
     */
    private final String updateFileName;

    /**
     * Whether an update URL is defined.
     */
    private final boolean hasUpdateURL;

    /**
     * Creates a new instance.
     * @param exiter the exiter for terminating the application
     * @param mapManager the map manager to use
     * @param parentComponent the parent component to show dialogs on
     * @param updateFileName the file to update
     */
    public UpdaterManager(@NotNull final Exiter exiter, @NotNull final MapManager<?, ?, ?> mapManager, final Component parentComponent, final String updateFileName) {
        this.mapManager = mapManager;
        this.exiter = exiter;
        this.parentComponent = parentComponent;
        this.updateFileName = updateFileName;
        final Action aUpdate = ActionUtils.newAction(ACTION_BUILDER, "Tool", this, "update");

        final CharSequence propUrl = ACTION_BUILDER.getString("update.url");
        hasUpdateURL = propUrl != null && propUrl.length() > 0;
        aUpdate.setEnabled(hasUpdateURL);
    }

    /**
     * Eventually perform an update during startup.
     */
    public void startup() {
        if (!hasUpdateURL || !preferences.getBoolean(AUTO_CHECK_KEY, AUTO_CHECK_DEFAULT)) {
            return;
        }
        final long timeDifference = UPDATE_TIMES[preferences.getInt(INTERVAL_KEY, INTERVAL_DEFAULT)];
        final long lastUpdate = preferences.getLong(Updater.LAST_UPDATE_KEY, 0L);
        if (System.currentTimeMillis() > lastUpdate + timeDifference) {
            update();
        }
    }

    /**
     * Perform an update.
     */
    @ActionMethod
    public void update() {
        if (mapManager.getOpenedMaps().isEmpty()) {
            new Thread(new Updater(parentComponent, exiter, updateFileName)).start();
        } else {
            ACTION_BUILDER.showMessageDialog(parentComponent, "updateCloseMaps");
        }
    }

}
