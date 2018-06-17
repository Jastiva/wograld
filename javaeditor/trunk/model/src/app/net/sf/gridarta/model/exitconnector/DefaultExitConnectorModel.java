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

package net.sf.gridarta.model.exitconnector;

import java.util.prefs.Preferences;
import net.sf.gridarta.MainControl;
import org.jetbrains.annotations.NotNull;

/**
 * Default {@link ExitConnectorModel} implementation. It saves and restores
 * attribute values to/from preferences.
 * @author Andreas Kirschbaum
 */
public class DefaultExitConnectorModel extends AbstractExitConnectorModel {

    /**
     * The preferences key for "paste exit name".
     */
    private static final String PASTE_EXIT_NAME_KEY = "exitConnectorPasteExitName";

    /**
     * The default value for {@link #PASTE_EXIT_NAME_KEY}.
     */
    private static final boolean PASTE_EXIT_NAME_DEFAULT = true;

    /**
     * The preferences key for "auto create exit".
     */
    private static final String AUTO_CREATE_EXIT_KEY = "exitConnectorAutoCreateExit";

    /**
     * The default value for {@link #AUTO_CREATE_EXIT_KEY}.
     */
    private static final boolean AUTO_CREATE_EXIT_DEFAULT = true;

    /**
     * The preferences key for "exit archetype".
     */
    private static final String EXIT_ARCHETYPE_NAME_KEY = "exitConnectorExitArchetypeName";

    /**
     * The default value for {@link #EXIT_ARCHETYPE_NAME_KEY}.
     */
    private static final String EXIT_ARCHETYPE_NAME_DEFAULT = "invis_exit";

    /**
     * The {@link Preferences}.
     */
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadPasteExitName() {
        return preferences.getBoolean(PASTE_EXIT_NAME_KEY, PASTE_EXIT_NAME_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void savePasteExitName(final boolean pasteExitName) {
        preferences.putBoolean(PASTE_EXIT_NAME_KEY, pasteExitName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadAutoCreateExit() {
        return preferences.getBoolean(AUTO_CREATE_EXIT_KEY, AUTO_CREATE_EXIT_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveAutoCreateExit(final boolean autoCreateExit) {
        preferences.putBoolean(AUTO_CREATE_EXIT_KEY, autoCreateExit);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected String loadExitArchetypeName() {
        return preferences.get(EXIT_ARCHETYPE_NAME_KEY, EXIT_ARCHETYPE_NAME_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveExitArchetypeName(@NotNull final String exitArchetypeName) {
        preferences.put(EXIT_ARCHETYPE_NAME_KEY, exitArchetypeName);
    }

}
