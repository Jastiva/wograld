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

import org.jetbrains.annotations.NotNull;

/**
 * {@link ExitConnectorModel} implementation for regression tests. It does not
 * retain attribute values.
 * @author Andreas Kirschbaum
 */
public class TestExitConnectorModel extends AbstractExitConnectorModel {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadPasteExitName() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void savePasteExitName(final boolean pasteExitName) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean loadAutoCreateExit() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveAutoCreateExit(final boolean autoCreateExit) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected String loadExitArchetypeName() {
        return "exit";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveExitArchetypeName(@NotNull final String exitArchetypeName) {
        // ignore
    }

}
