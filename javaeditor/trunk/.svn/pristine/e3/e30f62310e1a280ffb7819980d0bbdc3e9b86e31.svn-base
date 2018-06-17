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

import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores information needed by the exit connector.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractExitConnectorModel implements ExitConnectorModel {

    /**
     * The listeners to notify.
     */
    private final EventListenerList2<ExitConnectorModelListener> listeners = new EventListenerList2<ExitConnectorModelListener>(ExitConnectorModelListener.class);

    /**
     * Whether an exit has been remembered. Set to <code>null</code> if no exit
     * location has been remembered.
     */
    @Nullable
    private ExitLocation exitLocation;

    /**
     * Whether the exit's name should be set when pasted.
     */
    private boolean pasteExitName = loadPasteExitName();

    /**
     * Whether exit game objects should be auto-created when needed.
     */
    private boolean autoCreateExit = loadAutoCreateExit();

    /**
     * The archetype to insert when creating new exit game objects.
     */
    @NotNull
    private String exitArchetypeName = loadExitArchetypeName();

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public ExitLocation getExitLocation() {
        return exitLocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExitLocation(@Nullable final ExitLocation exitLocation) {
        if (exitLocation == null ? this.exitLocation == null : exitLocation.equals(this.exitLocation)) {
            return;
        }

        this.exitLocation = exitLocation;
        for (final ExitConnectorModelListener listener : listeners.getListeners()) {
            listener.exitLocationChanged(exitLocation);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPasteExitName() {
        return pasteExitName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPasteExitName(final boolean pasteExitName) {
        if (this.pasteExitName == pasteExitName) {
            return;
        }

        this.pasteExitName = pasteExitName;
        savePasteExitName(pasteExitName);
        for (final ExitConnectorModelListener listener : listeners.getListeners()) {
            listener.pasteExitNameChanged(pasteExitName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoCreateExit() {
        return autoCreateExit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoCreateExit(final boolean autoCreateExit) {
        if (this.autoCreateExit == autoCreateExit) {
            return;
        }

        this.autoCreateExit = autoCreateExit;
        saveAutoCreateExit(autoCreateExit);
        for (final ExitConnectorModelListener listener : listeners.getListeners()) {
            listener.autoCreateExitChanged(autoCreateExit);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getExitArchetypeName() {
        return exitArchetypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExitArchetypeName(@NotNull final String exitArchetypeName) {
        if (this.exitArchetypeName.equals(exitArchetypeName)) {
            return;
        }

        this.exitArchetypeName = exitArchetypeName;
        saveExitArchetypeName(exitArchetypeName);
        for (final ExitConnectorModelListener listener : listeners.getListeners()) {
            listener.exitArchetypeNameChanged(exitArchetypeName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExitConnectorModelListener(@NotNull final ExitConnectorModelListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeExitConnectorModelListener(@NotNull final ExitConnectorModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the stored attribute value for {@link #pasteExitName}.
     * @return the attribute value
     */
    protected abstract boolean loadPasteExitName();

    /**
     * Sets the stored attribute value for {@link #pasteExitName}.
     * @param pasteExitName the attribute value
     */
    protected abstract void savePasteExitName(final boolean pasteExitName);

    /**
     * Returns the stored attribute value for {@link #autoCreateExit}.
     * @return the attribute value
     */
    protected abstract boolean loadAutoCreateExit();

    /**
     * Sets the stored attribute value for {@link #isAutoCreateExit()}.
     * @param autoCreateExit the attribute value
     */
    protected abstract void saveAutoCreateExit(final boolean autoCreateExit);

    /**
     * Returns the stored attribute value for {@link #exitArchetypeName}.
     * @return the attribute value
     */
    @NotNull
    protected abstract String loadExitArchetypeName();

    /**
     * Sets the stored attribute value for {@link #setExitArchetypeName(String)}.
     * @param exitArchetypeName the attribute value
     */
    protected abstract void saveExitArchetypeName(@NotNull final String exitArchetypeName);

}
