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

import java.awt.Frame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.scripts.ScriptedEvent;
import net.sf.gridarta.model.scripts.ScriptedEventFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * {@link net.sf.gridarta.model.scripts.ScriptArchData} related functions.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class ScriptArchDataUtils<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(ScriptArchDataUtils.class);

    /**
     * The object type for event objects.
     * @serial
     */
    private final int eventTypeNo;

    /**
     * The {@link ScriptArchUtils} instance to use.
     */
    @NotNull
    private final ScriptArchUtils scriptArchUtils;

    /**
     * The {@link ScriptedEventFactory} instance to use.
     */
    @NotNull
    private final ScriptedEventFactory<G, A, R> scriptedEventFactory;

    /**
     * The {@link ScriptedEventEditor} to use.
     */
    @NotNull
    private final ScriptedEventEditor<G, A, R> scriptedEventEditor;

    /**
     * Creates a new instance.
     * @param eventTypeNo the object type for event objects
     * @param scriptArchUtils the script arch utils instance to use
     * @param scriptedEventFactory the scripted event factory instance to use
     * @param scriptedEventEditor the scripted event editor to use
     */
    public ScriptArchDataUtils(final int eventTypeNo, @NotNull final ScriptArchUtils scriptArchUtils, @NotNull final ScriptedEventFactory<G, A, R> scriptedEventFactory, @NotNull final ScriptedEventEditor<G, A, R> scriptedEventEditor) {
        this.eventTypeNo = eventTypeNo;
        this.scriptArchUtils = scriptArchUtils;
        this.scriptedEventFactory = scriptedEventFactory;
        this.scriptedEventEditor = scriptedEventEditor;
    }

    /**
     * If there is a scripted event of the specified type, the script pad is
     * opened and the appropriate script displayed.
     * @param eventIndex index of event in the owner's inventory
     * @param task the task to execute
     * @param panelList the <code>JList</code> from the <code>MapArchPanel</code>
     * (script tab) which displays the events
     * @param mapManager the map manager instance
     * @param parent the parent frame for dialog boxes
     * @param gameObject the game object to operate on
     */
    public void modifyEventScript(final int eventIndex, final ScriptTask task, @NotNull final JList panelList, @NotNull final MapManager<?, ?, ?> mapManager, @NotNull final Frame parent, @NotNull final Iterable<G> gameObject) {
        G oldEvent = null;

        /* Find the event object */
        int eventCount = 0;
        for (final G tmp : gameObject) {
            if (tmp.getTypeNo() == eventTypeNo) {
                if (eventCount == eventIndex) {
                    oldEvent = tmp;
                    break;
                }
                eventCount++;
            }
        }

        if (oldEvent != null) {
            final ScriptedEvent<G, A, R> event = scriptedEventFactory.newScriptedEvent(oldEvent);
            switch (task) {
            case EVENT_OPEN:
                scriptedEventEditor.openScript(mapManager, event.getScriptPath(), parent);
                break;

            case EVENT_EDIT_PATH:
                scriptedEventEditor.editParameters(event, parent);
                break;

            case EVENT_REMOVE:
                if (JOptionPane.showConfirmDialog(panelList, "Are you sure you want to remove this \"" + scriptArchUtils.typeName(event.getEventType()) + "\" event which is\n" + "linked to the script: '" + event.getScriptPath() + "'?\n" + "(The script file itself is not going to be deleted)", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    oldEvent.remove();
                    scriptArchUtils.addEventsToJList(panelList, gameObject);
                }
                break;
            }
        } else {
            log.error("Error in modifyEventScript(): No event selected?");
        }
    }

}
