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

package net.sf.gridarta.gui.panel.gameobjectattributes;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for {@link GameObjectAttributesTab} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractGameObjectAttributesTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements GameObjectAttributesTab<G, A, R> {

    /**
     * The registered listeners.
     */
    private final Collection<GameObjectAttributesTabListener<G, A, R>> listeners = new ArrayList<GameObjectAttributesTabListener<G, A, R>>();

    /**
     * The tab severity.
     */
    @NotNull
    private Severity tabSeverity = Severity.DEFAULT;

    /**
     * The currently selected game object.
     */
    @Nullable
    private G selectedGameObject;

    /**
     * The focus listener to implement auto-applying. See {@link
     * #addAutoApply(Component)}.
     */
    @NotNull
    private final FocusListener focusListener = new FocusListener() {

        @Override
        public void focusGained(final FocusEvent e) {
            // ignore
        }

        @Override
        public void focusLost(final FocusEvent e) {
            fireApply();
        }

    };

    /**
     * Creates a new instance.
     * @param gameObjectAttributesModel the model to track
     */
    protected AbstractGameObjectAttributesTab(@NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel) {
        final GameObjectAttributesModelListener<G, A, R> gameObjectAttributesModelListener = new GameObjectAttributesModelListener<G, A, R>() {

            @Override
            public void selectedGameObjectChanged(@Nullable final G selectedGameObject) {
                AbstractGameObjectAttributesTab.this.selectedGameObject = selectedGameObject;
                refresh(selectedGameObject);
            }

            @Override
            public void refreshSelectedGameObject() {
                refresh(selectedGameObject);
            }

        };
        gameObjectAttributesModel.addGameObjectAttributesModelListener(gameObjectAttributesModelListener);
    }

    /**
     * Sets the tab severity.
     * @param tabSeverity the tab severity
     */
    protected void setTabSeverity(@NotNull final Severity tabSeverity) {
        if (this.tabSeverity == tabSeverity) {
            return;
        }

        this.tabSeverity = tabSeverity;
        for (final GameObjectAttributesTabListener<G, A, R> listener : listeners) {
            listener.tabSeverityChanged(this, tabSeverity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGameObjectAttributesTabListener(@NotNull final GameObjectAttributesTabListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeGameObjectAttributesTabListener(@NotNull final GameObjectAttributesTabListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Severity getTabSeverity() {
        return tabSeverity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply() {
        if (selectedGameObject != null) {
            apply(selectedGameObject);
        }
    }

    /**
     * Refreshes the display.
     * @param gameObject the game object to set the contents from
     */
    protected abstract void refresh(@Nullable final G gameObject);

    /**
     * Applies settings to the given game object.
     * @param gameObject the game object to set the contents of
     */
    protected abstract void apply(@NotNull final G gameObject);

    /**
     * Notify all listeners to apply the changes.
     */
    private void fireApply() {
        for (final GameObjectAttributesTabListener<G, A, R> listener : listeners) {
            listener.apply();
        }
    }

    /**
     * Registers a component that auto-applies when the focus is lost.
     * @param component the component
     */
    protected void addAutoApply(@NotNull final Component component) {
        component.addFocusListener(focusListener);
    }

    /**
     * Returns the currently selected {@link GameObject}.
     * @return the currently selected game object or <code>null</code>
     */
    @Nullable
    protected GameObject<G, A, R> getSelectedGameObject() {
        return selectedGameObject;
    }

}
