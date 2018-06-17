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

package net.sf.gridarta.var.atrinik.model.scripts;

import net.sf.gridarta.gui.scripts.ScriptedEventEditor;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.scripts.AbstractScriptedEvent;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.scripts.ScriptedEventFactory;
import net.sf.gridarta.model.scripts.UndefinedEventArchetypeException;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * Class which stores information about one scripted event.
 * @author Andreas Kirschbaum
 */
public class DefaultScriptedEvent extends AbstractScriptedEvent<GameObject, MapArchObject, Archetype> {

    /**
     * The attribute that holds the script path.
     */
    @NotNull
    private static final String SCRIPT_PATH = "race";

    /**
     * The attribute that holds the options.
     */
    @NotNull
    private static final String OPTIONS = "slaying";

    /**
     * The {@link ScriptArchUtils} instance to use.
     */
    @NotNull
    private final ScriptArchUtils scriptArchUtils;

    /**
     * The underlying game object that represents the event.
     */
    private final GameObject event;

    /**
     * The attribute name for the subtype field.
     */
    @NotNull
    private final String subtypeAttribute;

    /**
     * The {@link ScriptedEventEditor} to use.
     */
    @NotNull
    private final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor;

    /**
     * Create a ScriptedEvent of given type (This is used for map-loading).
     * @param scriptArchUtils the script arch utils instance to use
     * @param event GameObject that describes the event.
     * @param subtypeAttribute the attribute name for the subtype field
     * @param scriptedEventEditor the scripted event editor to use
     */
    DefaultScriptedEvent(@NotNull final ScriptArchUtils scriptArchUtils, final GameObject event, @NotNull final String subtypeAttribute, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor) {
        this.scriptArchUtils = scriptArchUtils;
        this.event = event;
        this.subtypeAttribute = subtypeAttribute;
        this.scriptedEventEditor = scriptedEventEditor;
    }

    /**
     * Creates a fully initialized ScriptedEvent.
     * @param scriptArchUtils the script arch utils instance to use
     * @param eventType type of the event
     * @param subtypeAttribute the attribute name for the subtype field
     * @param pluginName name of the plugin
     * @param scriptPath path to the file for this event
     * @param options the options for this event
     * @param scriptedEventFactory the scripted event factory for creating game
     * objects
     * @param scriptedEventEditor the scripted event editor to use
     * @throws UndefinedEventArchetypeException In case there is no Archetype to
     * create a ScriptedEvent.
     */
    DefaultScriptedEvent(@NotNull final ScriptArchUtils scriptArchUtils, final int eventType, @NotNull final String subtypeAttribute, @NotNull final String pluginName, final String scriptPath, final String options, @NotNull final ScriptedEventFactory<GameObject, MapArchObject, Archetype> scriptedEventFactory, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor) throws UndefinedEventArchetypeException {
        this.scriptArchUtils = scriptArchUtils;
        this.subtypeAttribute = subtypeAttribute;
        this.scriptedEventEditor = scriptedEventEditor;
        event = scriptedEventFactory.newEventGameObject(eventType);
        setEventData(pluginName, scriptPath, options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyEventPath() {
        final String newPath = scriptedEventEditor.getInputScriptPath();
        final String newPluginName = scriptedEventEditor.getInputPluginName();
        final String newOptions = scriptedEventEditor.getInputOptions();

        if (newPath.length() > 0) {
            setScriptPath(newPath);
        }
        if (newPluginName.length() > 0) {
            setPluginName(newPluginName);
        }
        if (newOptions.length() > 0) {
            setOptions(newOptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObject getEventArch() {
        return event;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEventType() {
        return event.getAttributeInt(subtypeAttribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getPluginName() {
        return event.getBestName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getScriptPath() {
        return event.getAttributeString(SCRIPT_PATH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getOptions() {
        return event.getAttributeString(OPTIONS);
    }

    private void setPluginName(@NotNull final String pluginName) {
        setEventData(pluginName, getScriptPath(), getOptions());
    }

    private void setScriptPath(@NotNull final String scriptPath) {
        setEventData(getPluginName(), scriptPath, getOptions());
    }

    private void setOptions(@NotNull final String options) {
        setEventData(getPluginName(), getScriptPath(), options);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected String typeName(final int eventType) {
        return scriptArchUtils.typeName(eventType);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected final void setEventData(@NotNull final String pluginName, @NotNull final String scriptPath, @NotNull final String options) {
        final int eventType = getEventType();
        event.setObjectText("");
        event.setAttributeInt(subtypeAttribute, eventType);
        event.setAttributeString(BaseObject.NAME, pluginName);
        event.setAttributeString(SCRIPT_PATH, scriptPath);
        event.setAttributeString(OPTIONS, options);
    }

}
