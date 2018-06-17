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
import javax.swing.AbstractAction;
import org.jetbrains.annotations.NotNull;

/**
 * Action for adding / removing an {@link GameObjectMatcher} from this a
 * <code>MutableOrGameObjectMatcher</code>.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class SetEnabledAction extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link MutableOrGameObjectMatcher} to add to / remove from.
     * @serial
     */
    @NotNull
    private final MutableOrGameObjectMatcher mutableOrGameObjectMatcher;

    /**
     * The {@link GameObjectMatcher} to be added / removed.
     * @serial
     */
    @NotNull
    private final GameObjectMatcher gameObjectMatcher;

    /**
     * Creates a <code>SetEnabledAction</code>.
     * @param mutableOrGameObjectMatcher the <code>MutableOrGameObjectMatcher</code>
     * to add to / remove from
     * @param gameObjectMatcher the <code>ArchGameObjectMatcher</code> to be
     * added / removed
     */
    public SetEnabledAction(@NotNull final MutableOrGameObjectMatcher mutableOrGameObjectMatcher, @NotNull final GameObjectMatcher gameObjectMatcher) {
        this.mutableOrGameObjectMatcher = mutableOrGameObjectMatcher;
        this.gameObjectMatcher = gameObjectMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (!isEnabled()) {
            return;
        }
        setSelected(!isSelected());
    }

    /**
     * Adds / removes the {@link #gameObjectMatcher}.
     * @param selected whether to add (<code>true</code>) or remove
     * (<code>false</code>)
     */
    private void setSelected(final boolean selected) {
        if (selected) {
            mutableOrGameObjectMatcher.addArchObjectMatcher(gameObjectMatcher);
        } else {
            mutableOrGameObjectMatcher.removeArchObjectMatcher(gameObjectMatcher);
        }
    }

    /**
     * Returns whether {@link #gameObjectMatcher} is currently enabled.
     * @return whether the matcher is enabled
     */
    public boolean isSelected() {
        return mutableOrGameObjectMatcher.containsArchObjectMatcher(gameObjectMatcher);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public SetEnabledAction clone() {
        try {
            return (SetEnabledAction) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

}
