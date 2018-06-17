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

package net.sf.gridarta.model.mapviewsettings;

import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.match.ViewGameObjectMatcherManager;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link MapViewSettings} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapViewSettings implements MapViewSettings {

    /**
     * The visibility of the grid.
     */
    private boolean gridVisible = loadGridVisible();

    /**
     * The visibility of the light.
     */
    private boolean lightVisible = loadLightVisible();

    /**
     * Whether smoothing display is active.
     */
    private boolean smoothing = loadSmoothing();

    /**
     * Whether double faces should be drawn double height.
     */
    private boolean doubleFaces = loadDoubleFaces();

    /**
     * Bit field of edit types to show transparent.
     */
    private int alphaType = loadAlphaType();

    /**
     * Bit field of edit types to show only.
     */
    private int editType = loadEditType();

    /**
     * Whether autojoining is on/off.
     */
    private boolean autojoin = loadAutojoin();

    /**
     * The transparency settings.
     */
    // TODO: use this field (it looks unused but usage is planned)
    @Nullable
    private final ViewGameObjectMatcherManager transparencyManager = null;

    /**
     * The visibility settings.
     */
    // TODO: use this field (it looks unused but usage is planned)
    @Nullable
    private final ViewGameObjectMatcherManager visibilityManager = null;

    /**
     * The MapViewSettingsListeners to inform of changes.
     */
    @NotNull
    private final EventListenerList2<MapViewSettingsListener> listenerList = new EventListenerList2<MapViewSettingsListener>(MapViewSettingsListener.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapViewSettingsListener(@NotNull final MapViewSettingsListener listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapViewSettingsListener(@NotNull final MapViewSettingsListener listener) {
        listenerList.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGridVisible() {
        return gridVisible;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGridVisible(final boolean gridVisible) {
        if (this.gridVisible == gridVisible) {
            return;
        }

        this.gridVisible = gridVisible;
        saveGridVisible(gridVisible);
        fireGridVisibleChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLightVisible() {
        return lightVisible;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLightVisible(final boolean lightVisible) {
        if (this.lightVisible == lightVisible) {
            return;
        }

        this.lightVisible = lightVisible;
        saveLightVisible(lightVisible);
        fireLightVisibleChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSmoothing() {
        return smoothing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSmoothing(final boolean smoothing) {
        if (this.smoothing == smoothing) {
            return;
        }

        this.smoothing = smoothing;
        saveSmoothing(smoothing);
        fireSmoothingChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDoubleFaces() {
        return doubleFaces;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDoubleFaces(final boolean doubleFaces) {
        if (this.doubleFaces == doubleFaces) {
            return;
        }

        this.doubleFaces = doubleFaces;
        saveDoubleFaces(doubleFaces);
        fireDoubleFacesChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlphaType(final int v) {
        return v > 0 && (alphaType & v) == v;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlphaType(final int v, final boolean state) {
        if (state) {
            alphaType |= v;
        } else {
            alphaType &= ~v;
        }
        saveAlphaType(alphaType);
        fireAlphaTypeChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearAlpha() {
        alphaType = 0;
        saveAlphaType(alphaType);
        fireAlphaTypeChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEditType() {
        return editType;
    }

    /**
     * Set the map view to show squares of the given type. (If no edit type is
     * set, everything is displayed)
     * @param editType edit type bitmask of types to show
     */
    private void setEditType(final int editType) {
        if ((this.editType & editType) == editType) {
            return;
        }

        this.editType |= editType;
        saveEditType(this.editType);
        fireEditTypeChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetEditType(final int editType) {
        if ((this.editType & editType) == 0) {
            return;
        }

        this.editType &= ~editType;
        saveEditType(this.editType);
        fireEditTypeChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEditType(final int editType) {
        final int mask = editType == 0 ? BaseObject.EDIT_TYPE_NONE : editType;
        return (this.editType & mask) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEditType(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        return editType == 0 || isEditType(gameObject.getEditType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEditTypeSet() {
        return (editType & BaseObject.EDIT_TYPE_NONE) == 0 && editType != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleEditType(final int editType) {
        if (isEditType(editType)) {
            unsetEditType(editType);
        } else {
            setEditType(editType);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutojoin() {
        return autojoin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutojoin(final boolean autojoin) {
        if (this.autojoin == autojoin) {
            return;
        }

        this.autojoin = autojoin;
        saveAutojoin(autojoin);
        fireAutojoinChanged();
    }

    /**
     * Informs all registered listeners that the grid visibility has changed.
     */
    private void fireGridVisibleChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.gridVisibleChanged(gridVisible);
        }
    }

    /**
     * Informs all registered listeners that the light visibility has changed.
     */
    private void fireLightVisibleChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.lightVisibleChanged(lightVisible);
        }
    }

    /**
     * Informs all registered listeners that the smoothing setting has changed.
     */
    private void fireSmoothingChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.smoothingChanged(smoothing);
        }
    }

    /**
     * Informs all registered listeners that the double faces visibility has
     * changed.
     */
    private void fireDoubleFacesChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.doubleFacesChanged(doubleFaces);
        }
    }

    /**
     * Informs all registered listeners that the alpha type haves changed.
     */
    private void fireAlphaTypeChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.alphaTypeChanged(alphaType);
        }
    }

    /**
     * Notify all listeners about changed {@link #editType}.
     */
    private void fireEditTypeChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.editTypeChanged(editType);
        }
    }

    /**
     * Notify all listeners about changed {@link #autojoin}.
     */
    private void fireAutojoinChanged() {
        for (final MapViewSettingsListener listener : listenerList.getListeners()) {
            listener.autojoinChanged(autojoin);
        }
    }

    /**
     * Loads the default value for {@link #gridVisible}.
     * @return the default value
     */
    protected abstract boolean loadGridVisible();

    /**
     * Saves the {@link #gridVisible} value.
     * @param gridVisible the grid visible value
     */
    protected abstract void saveGridVisible(final boolean gridVisible);

    /**
     * Loads the default value for {@link #lightVisible}.
     * @return the default value
     */
    protected abstract boolean loadLightVisible();

    /**
     * Saves the {@link #lightVisible} value.
     * @param lightVisible the light visible value
     */
    protected abstract void saveLightVisible(final boolean lightVisible);

    /**
     * Loads the default value for {@link #smoothing}.
     * @return the default value
     */
    protected abstract boolean loadSmoothing();

    /**
     * Saves the {@link #smoothing} value.
     * @param smoothing the smoothing value
     */
    protected abstract void saveSmoothing(final boolean smoothing);

    /**
     * Loads the default value for {@link #doubleFaces}.
     * @return the default value
     */
    protected abstract boolean loadDoubleFaces();

    /**
     * Saves the {@link #doubleFaces} value.
     * @param doubleFaces the double faces value
     */
    protected abstract void saveDoubleFaces(final boolean doubleFaces);

    /**
     * Loads the default value for {@link #alphaType}.
     * @return the default value
     */
    protected abstract int loadAlphaType();

    /**
     * Saves the {@link #alphaType} value.
     * @param alphaType the alpha type value
     */
    protected abstract void saveAlphaType(final int alphaType);

    /**
     * Loads the default value for {@link #editType}.
     * @return the default value
     */
    protected abstract int loadEditType();

    /**
     * Saves the {@link #editType} value.
     * @param editType the edit type value
     */
    protected abstract void saveEditType(final int editType);

    /**
     * Loads the default value for {@link #autojoin}.
     * @return the default value
     */
    protected abstract boolean loadAutojoin();

    /**
     * Saves the autojoin value.
     * @param autojoin the autojoin value
     */
    protected abstract void saveAutojoin(final boolean autojoin);

}
