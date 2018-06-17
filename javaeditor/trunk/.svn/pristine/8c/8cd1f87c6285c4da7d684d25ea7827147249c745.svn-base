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

package net.sf.gridarta.gui.scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.scripts.ScriptArchData;
import net.sf.gridarta.model.scripts.ScriptedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Small class, listening for button-press events in the popup frame for script
 * paths or create-new-event frame.
 * @author Andreas Kirschbaum
 */
public class PathButtonListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ActionListener {

    @NotNull
    private final JDialog frame;

    private final boolean isOkButton;

    /**
     * The target event to script.
     */
    @Nullable
    private ScriptedEvent<G, A, R> scriptedEvent;

    /**
     * The ScriptArchData to operate on.
     */
    @Nullable
    private ScriptArchData<G, A, R> scriptArchData;

    /**
     * The affected game object.
     */
    @Nullable
    private G gameObject;

    /**
     * The {@link ScriptArchEditor} instance to use.
     */
    @NotNull
    private final ScriptArchEditor<G, A, R> scriptArchEditor;

    /**
     * Create a PathButtonListener.
     * @param isOkButton true for ok-buttons
     * @param frame frame this listener belongs to
     * @param scriptArchData this is only set for the ok-button of "create new"
     * frame, otherwise null
     * @param gameObject the affected game object
     * @param scriptArchEditor the script arch editor to use
     * @param scriptedEvent the target event to script
     */
    public PathButtonListener(final boolean isOkButton, @NotNull final JDialog frame, @Nullable final ScriptArchData<G, A, R> scriptArchData, @Nullable final G gameObject, @NotNull final ScriptArchEditor<G, A, R> scriptArchEditor, @Nullable final ScriptedEvent<G, A, R> scriptedEvent) {
        this.isOkButton = isOkButton;
        this.frame = frame;
        this.scriptArchData = scriptArchData;
        this.gameObject = gameObject;
        this.scriptArchEditor = scriptArchEditor;
        this.scriptedEvent = scriptedEvent;
    }

    /**
     * Set the target event to script.
     * @param scriptedEvent the new target event to script
     */
    public void setTargetEvent(@NotNull final ScriptedEvent<G, A, R> scriptedEvent) {
        this.scriptedEvent = scriptedEvent;
    }

    /**
     * Set the ScriptArchData to operate on.
     * @param scriptArchData the script arch data to operate on
     * @param gameObject the affected game object
     */
    public void setScriptArchData(@NotNull final ScriptArchData<G, A, R> scriptArchData, @NotNull final G gameObject) {
        this.scriptArchData = scriptArchData;
        this.gameObject = gameObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        if (isOkButton && scriptArchData == null && scriptedEvent != null) {
            scriptedEvent.modifyEventPath(); // ok button for modifying path
        }

        if (isOkButton && scriptArchData != null && gameObject != null) {
            // ok button for creating a new event/script
            scriptArchEditor.createNewEvent(frame, scriptArchData, gameObject);
        } else {
            frame.setVisible(false); // hide dialog
        }
    }

}
