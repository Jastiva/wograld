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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.scripts.ScriptArchDataUtils;
import net.sf.gridarta.gui.scripts.ScriptArchEditor;
import net.sf.gridarta.gui.scripts.ScriptTask;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.scripts.ScriptArchData;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The "Events" tab in the game object attributes panel.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class EventsTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObjectAttributesTab<G, A, R> {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The parent frame for dialog boxes.
     */
    @NotNull
    private final Frame parent;

    /**
     * The {@link MapManager}.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link ScriptArchEditor} to use.
     */
    @NotNull
    private final ScriptArchEditor<G, A, R> scriptArchEditor;

    /**
     * The {@link ScriptArchData} instance to use.
     */
    @NotNull
    private final ScriptArchData<G, A, R> scriptArchData;

    /**
     * The {@link ScriptArchUtils} to use.
     */
    @NotNull
    private final ScriptArchUtils scriptArchUtils;

    /**
     * The {@link ScriptArchDataUtils} to use.
     */
    @NotNull
    private final ScriptArchDataUtils<G, A, R> scriptArchDataUtils;

    /**
     * The content panel.
     */
    @NotNull
    private final JPanel panel = new JPanel();

    /**
     * The {@link JScrollPane} displaying all event.s
     */
    @NotNull
    private final JScrollPane scrollPane;

    /**
     * The action for "add new event".
     */
    @NotNull
    private final Action aEventAddNew = ACTION_BUILDER.createAction(false, "eventAddNew", this);

    /**
     * The action for "edit event parameters".
     */
    @NotNull
    private final Action aEventEditData = ACTION_BUILDER.createAction(false, "eventEditData", this);

    /**
     * The action for "edit event code".
     */
    @NotNull
    private final Action aEventEdit = ACTION_BUILDER.createAction(false, "eventEdit", this);

    /**
     * The action for "remove event".
     */
    @NotNull
    private final Action aEventRemove = ACTION_BUILDER.createAction(false, "eventRemove", this);

    /**
     * The {@link JList} that shows all events.
     */
    @NotNull
    private final JList eventList = new JList();

    /**
     * The currently selected game object. Set to <code>null</code> if none is
     * selected.
     */
    @Nullable
    private GameObject<G, A, R> selectedGameObject;

    /**
     * Creates a new instance.
     * @param parent the parent frame for dialog boxes
     * @param mapManager the map manager
     * @param gameObjectAttributesModel the model to track
     * @param scriptArchEditor the script arch editor instance to use
     * @param scriptArchData the script arch data instance to use
     * @param scriptArchDataUtils the script arch data utils to use
     * @param scriptArchUtils the script arch utils to use
     */
    public EventsTab(@NotNull final Frame parent, @NotNull final MapManager<G, A, R> mapManager, @NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final ScriptArchEditor<G, A, R> scriptArchEditor, @NotNull final ScriptArchData<G, A, R> scriptArchData, @NotNull final ScriptArchDataUtils<G, A, R> scriptArchDataUtils, @NotNull final ScriptArchUtils scriptArchUtils) {
        super(gameObjectAttributesModel);
        this.parent = parent;
        this.mapManager = mapManager;
        this.scriptArchEditor = scriptArchEditor;
        this.scriptArchData = scriptArchData;
        this.scriptArchDataUtils = scriptArchDataUtils;
        this.scriptArchUtils = scriptArchUtils;

        scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(new EtchedBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(80, 40));

        final Container buttons = new JPanel(new GridLayout(4, 1));
        buttons.add(new JButton(aEventAddNew));
        buttons.add(new JButton(aEventEditData));
        buttons.add(new JButton(aEventEdit));
        buttons.add(new JButton(aEventRemove));

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);
        panel.setPreferredSize(new Dimension(100, 40));

        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final ListSelectionListener listSelectionListener = new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                updateActions();
            }

        };
        eventList.addListSelectionListener(listSelectionListener);
        refresh(gameObjectAttributesModel.getSelectedGameObject());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Events";
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canApply() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        scrollPane.requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void refresh(@Nullable final G gameObject) {
        selectedGameObject = gameObject;
        if (gameObject == null || !gameObject.isScripted()) {
            setTabSeverity(Severity.DEFAULT);
            final ListModel listModel = eventList.getModel();
            if (listModel != null && listModel.getSize() > 0) {
                eventList.setModel(new DefaultListModel());
            }
        } else {
            setTabSeverity(Severity.MODIFIED);
            eventList.removeAll();
            scriptArchUtils.addEventsToJList(eventList, gameObject);
        }
        updateActions();
    }

    /**
     * Updates the enabled state of all actions.
     */
    private void updateActions() {
        aEventAddNew.setEnabled(doAddNewEvent(false));
        aEventEditData.setEnabled(doEditEvent(ScriptTask.EVENT_EDIT_PATH, false));
        aEventEdit.setEnabled(doEditEvent(ScriptTask.EVENT_OPEN, false));
        aEventRemove.setEnabled(doEditEvent(ScriptTask.EVENT_REMOVE, false));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void apply(@NotNull final G gameObject) {
    }

    /**
     * Action method for creating a new event.
     */
    @ActionMethod
    public void eventAddNew() {
        doAddNewEvent(true);
    }

    /**
     * Action method for editing the data of an existing event.
     */
    @ActionMethod
    public void eventEditData() {
        doEditEvent(ScriptTask.EVENT_EDIT_PATH, true);
    }

    /**
     * Action method for editing an existing event.
     */
    @ActionMethod
    public void eventEdit() {
        doEditEvent(ScriptTask.EVENT_OPEN, true);
    }

    /**
     * Action method for removing an existing event.
     */
    @ActionMethod
    public void eventRemove() {
        doEditEvent(ScriptTask.EVENT_REMOVE, true);
    }

    /**
     * This method is invoked when the user pressed the "new event" button.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doAddNewEvent(final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedGameObject;
        if (gameObject == null) {
            return false;
        }

        if (performAction) {
            final G selectedHead = gameObject.getHead();
            final MapSquare<G, A, R> mapSquare = selectedHead.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Add event");
            try {
                scriptArchEditor.addEventScript(selectedHead, scriptArchData, parent);

                if (scriptArchData.isEmpty(selectedHead)) {
                    setEventPanelButtonState(true, false, false, false);
                } else {
                    setEventPanelButtonState(true, true, true, true);
                    scriptArchUtils.addEventsToJList(eventList, selectedHead);
                }
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * This method is invoked when the user pressed the "edit
     * event"/"path"/"remove" button from the event panel. If there is a valid
     * selection in the event list, the appropriate action for this event is
     * triggered.
     * @param task the task to execute
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doEditEvent(@NotNull final ScriptTask task, final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedGameObject;
        if (gameObject == null) {
            return false;
        }

        final ListModel listModel = eventList.getModel();
        if (listModel == null || listModel.getSize() <= 0) {
            return false;
        }

        final int index = eventList.getSelectedIndex();
        if (index < 0) {
            return false;
        }

        if (performAction) {
            final G selectedHead = gameObject.getHead();
            final MapSquare<G, A, R> mapSquare = selectedHead.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Modify event");
            try {
                scriptArchDataUtils.modifyEventScript(index, task, eventList, mapManager, parent, selectedHead);
                if (scriptArchData.isEmpty(selectedHead)) {
                    setEventPanelButtonState(true, false, false, false);
                }
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * Sets the enable/disable states for the four buttons in the event panel.
     * @param newButton the enabled state for the "new" button
     * @param modifyButton the enabled state for the "modify" button
     * @param pathButton the enabled state for the "path" button
     * @param removeButton the enabled state for the "remove" button
     */
    private void setEventPanelButtonState(final boolean newButton, final boolean modifyButton, final boolean pathButton, final boolean removeButton) {
        aEventAddNew.setEnabled(newButton);
        aEventEditData.setEnabled(pathButton);
        aEventEdit.setEnabled(modifyButton);
        aEventRemove.setEnabled(removeButton);
    }

}
