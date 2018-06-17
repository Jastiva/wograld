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

package net.sf.gridarta.model.match;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jetbrains.annotations.NotNull;

/**
 * Manages {@link GameObjectMatcher GameObjectMatchers}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ViewGameObjectMatcherManager {

    /**
     * The {@link MutableOrGameObjectMatcher} that is manipulated by the
     * actions.
     */
    @NotNull
    private final MutableOrGameObjectMatcher mutableOrGameObjectMatcher;

    /**
     * The actions.
     */
    @NotNull
    private final List<GameObjectMatcherToggleAction> actions = new ArrayList<GameObjectMatcherToggleAction>();

    /**
     * The reset action.
     */
    @NotNull
    private final Action resetAction = new ResetAction(Collections.unmodifiableList(actions));

    /**
     * Creates a <code>ViewArchObjectMatcher</code>.
     * @param mutableOrGameObjectMatcher the <code>MutableOrGameObjectMatcher</code>
     * to manipulate
     */
    public ViewGameObjectMatcherManager(@NotNull final MutableOrGameObjectMatcher mutableOrGameObjectMatcher) {
        this.mutableOrGameObjectMatcher = mutableOrGameObjectMatcher;
    }

    /**
     * Adds an {@link GameObjectMatcher}.
     * @param gameObjectMatcher the <code>GameObjectMatcher</code> to add
     * @return the <code>Action</code> created for toggling
     *         <code>gameObjectMatcher</code>
     */
    @NotNull
    public Action addArchObjectMatcher(@NotNull final GameObjectMatcher gameObjectMatcher) {
        final GameObjectMatcherToggleAction action = new GameObjectMatcherToggleAction(gameObjectMatcher, mutableOrGameObjectMatcher);
        actions.add(action);
        return action;
    }

    /**
     * Gets all actions.
     * @return all actions created so far (except reset)
     */
    @NotNull
    public List<? extends Action> getAllActions() {
        return Collections.unmodifiableList(actions);
    }

    /**
     * Gets the reset action.
     * @return the reset action
     */
    @NotNull
    public Action getResetAction() {
        return resetAction;
    }

    /**
     * The reset action.
     */
    private static class ResetAction extends AbstractAction {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The actions to reset.
         */
        @NotNull
        private final Iterable<? extends GameObjectMatcherToggleAction> actions;

        /**
         * Creates a <code>ResetAction</code>.
         * @param actions the <code>Action</code>s to reset
         */
        ResetAction(@NotNull final Iterable<? extends GameObjectMatcherToggleAction> actions) {
            this.actions = actions;
        }

        @Override
        public void actionPerformed(@NotNull final ActionEvent e) {
            for (final GameObjectMatcherToggleAction action : actions) {
                action.setSelected(false);
            }
        }

        @NotNull
        @Override
        protected Object clone() {
            try {
                return super.clone();
            } catch (final CloneNotSupportedException ex) {
                throw new AssertionError(ex);
            }
        }

    }

    /**
     * An {@link Action} to toggle an {@link GameObjectMatcher} within an {@link
     * MutableOrGameObjectMatcher}.
     */
    private static class GameObjectMatcherToggleAction extends AbstractAction {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Selected state.
         */
        private boolean selected;

        /**
         * A {@link MutableOrGameObjectMatcher} to add / remove {@link
         * GameObjectMatcher} to.
         */
        @NotNull
        private final MutableOrGameObjectMatcher mutableOrArchObjectMatcher;

        /**
         * The {@link GameObjectMatcher} to add / remove.
         */
        @NotNull
        private final GameObjectMatcher gameObjectMatcher;

        /**
         * Creates an <code>GameObjectMatcherToggleAction</code>.
         * @param gameObjectMatcher the matcher to add / remove to / from
         * <code>mutableOrGameObjectMatcher</code>
         * @param mutableOrArchObjectMatcher the <code>MutableOrGameObjectMatcher</code>
         * to add / remove <code>gameObjectMatcher</code> to / from
         */
        GameObjectMatcherToggleAction(@NotNull final GameObjectMatcher gameObjectMatcher, @NotNull final MutableOrGameObjectMatcher mutableOrArchObjectMatcher) {
            this.gameObjectMatcher = gameObjectMatcher;
            this.mutableOrArchObjectMatcher = mutableOrArchObjectMatcher;
        }

        @Override
        public void actionPerformed(@NotNull final ActionEvent e) {
            if (!isEnabled()) {
                return;
            }
            setSelected(!selected);
        }

        /**
         * Sets the selected state.
         * @param selected the new selected state
         */
        public void setSelected(final boolean selected) {
            this.selected = selected;
            if (selected) {
                mutableOrArchObjectMatcher.addArchObjectMatcher(gameObjectMatcher);
            } else {
                mutableOrArchObjectMatcher.removeArchObjectMatcher(gameObjectMatcher);
            }
        }

        /**
         * Gets the selected state.
         * @return the selected state
         * @retval <code>true</code> if this action is selected
         * @retval <code>false</code> if this action is not selected
         */
        public boolean isSelected() {
            return selected;
        }

        @NotNull
        @Override
        protected Object clone() {
            try {
                return super.clone();
            } catch (final CloneNotSupportedException ex) {
                throw new AssertionError(ex);
            }
        }

    }

}
