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

package net.sf.gridarta.gui.dialog.prefs;

import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.model.io.PathManager;
import org.jetbrains.annotations.NotNull;

/**
 * Maintains the application preferences state.
 * @author Andreas Kirschbaum
 */
public class AppPreferencesModel {

    /**
     * Preferences key for server application.
     */
    private static final String PREFERENCES_APP_SERVER = "appServer";

    /**
     * Preferences key for client application.
     */
    private static final String PREFERENCES_APP_CLIENT = "appClient";

    /**
     * Preferences key for editor application.
     */
    private static final String PREFERENCES_APP_EDITOR = "appEditor";

    /**
     * Preferences.
     */
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The default value for the server setting.
     */
    @NotNull
    private final String serverDefault;

    /**
     * The default value for the client setting.
     */
    @NotNull
    private final String clientDefault;

    /**
     * The default value for the editor setting.
     */
    @NotNull
    private final String editorDefault;

    /**
     * Creates a new instance.
     * @param serverDefault the default value for the server setting
     * @param clientDefault the default value for the client setting
     * @param editorDefault the default value for the editor setting
     */
    public AppPreferencesModel(@NotNull final String serverDefault, @NotNull final String clientDefault, @NotNull final String editorDefault) {
        this.serverDefault = serverDefault;
        this.clientDefault = clientDefault;
        this.editorDefault = editorDefault;
    }

    /**
     * Sets the server setting.
     * @param server the server setting
     */
    public static void setServer(@NotNull final CharSequence server) {
        PREFERENCES.put(PREFERENCES_APP_SERVER, PathManager.path(server));
    }

    /**
     * Sets the client setting.
     * @param client the client setting
     */
    public static void setClient(@NotNull final CharSequence client) {
        PREFERENCES.put(PREFERENCES_APP_CLIENT, PathManager.path(client));
    }

    /**
     * Sets the editor setting.
     * @param editor the editor setting
     */
    public static void setEditor(@NotNull final CharSequence editor) {
        PREFERENCES.put(PREFERENCES_APP_EDITOR, PathManager.path(editor));
    }

    /**
     * Returns the server setting.
     * @return the server setting
     */
    @NotNull
    public String getServer() {
        return PREFERENCES.get(PREFERENCES_APP_SERVER, serverDefault);
    }

    /**
     * Returns the client setting.
     * @return the client setting
     */
    @NotNull
    public String getClient() {
        return PREFERENCES.get(PREFERENCES_APP_CLIENT, clientDefault);
    }

    /**
     * Returns the editor setting.
     * @return the editor setting
     */
    @NotNull
    public String getEditor() {
        return PREFERENCES.get(PREFERENCES_APP_EDITOR, editorDefault);
    }

    /**
     * Returns the server setting's default value.
     * @return the server setting's default value
     */
    @NotNull
    public String getServerDefault() {
        return serverDefault;
    }

    /**
     * Returns the client setting's default value.
     * @return the client setting's default value
     */
    @NotNull
    public String getClientDefault() {
        return clientDefault;
    }

    /**
     * Returns the editor setting's default value.
     * @return the editor setting's default value
     */
    @NotNull
    public String getEditorDefault() {
        return editorDefault;
    }

}
