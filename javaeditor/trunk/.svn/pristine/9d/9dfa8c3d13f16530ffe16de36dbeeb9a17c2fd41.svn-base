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

package net.sf.gridarta.gui.panel.tools;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.sf.gridarta.gui.map.event.MouseOpListener;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareModel;
import net.sf.gridarta.gui.panel.selectedsquare.SelectedSquareView;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.pickmapsettings.PickmapSettings;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User interface for selecting a tool and displaying its options. Note: A
 * ToolSelector automatically has always at least one Tool, the VoidTool.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ToolSelector<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta.gui.panel.tools");

    /**
     * The pane with the selections.
     */
    @NotNull
    private final Container selectionPane = new JPanel(new FlowLayout());

    /**
     * The CardLayout for the pane that shows a tool's options.
     */
    @NotNull
    private final CardLayout optionCards = new CardLayout();

    /**
     * The ButtonGroup for the toggle buttons.
     */
    @NotNull
    private final ButtonGroup selectionButtonGroup = new ButtonGroup();

    /**
     * The pane with the options of a tool.
     */
    @NotNull
    private final Container optionsPane = new JPanel(optionCards);

    /**
     * The currently selected tool.
     */
    @NotNull
    private Tool<G, A, R> selectedTool;

    /**
     * The tools.
     */
    @NotNull
    private final Map<String, Tool<G, A, R>> tools = new HashMap<String, Tool<G, A, R>>();

    /**
     * Empty margin.
     */
    private static final Insets EMPTY_MARGIN = new Insets(0, 0, 0, 0);

    /**
     * Creates a new instance.
     * @param defaultTool name of the tool that should be selected by default
     * @param mapViewSettings the map view settings instance
     * @param selectedSquareView the selected square view
     * @param selectedSquareModel the selected square model
     * @param objectChooser the object chooser to use
     * @param pickmapSettings the pickmap settings to use
     * @param floorGameObjectMatcher the floor matcher to use
     * @param wallGameObjectMatcher the wall matcher to use
     * @param monsterGameObjectMatcher the monster matcher to use
     * @param insertionModeSet the insertion mode set to use
     */
    public ToolSelector(@NotNull final String defaultTool, @NotNull final MapViewSettings mapViewSettings, @NotNull final SelectedSquareView<G, A, R> selectedSquareView, @NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final PickmapSettings pickmapSettings, @Nullable final GameObjectMatcher floorGameObjectMatcher, @Nullable final GameObjectMatcher wallGameObjectMatcher, @Nullable final GameObjectMatcher monsterGameObjectMatcher, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        createUI();
        addTool(new VoidTool<G, A, R>(), defaultTool);
        addTool(new SelectionTool<G, A, R>(objectChooser, insertionModeSet), defaultTool);
        addTool(new DeletionTool<G, A, R>(mapViewSettings, objectChooser, pickmapSettings, floorGameObjectMatcher, wallGameObjectMatcher, monsterGameObjectMatcher), defaultTool);
        addTool(new InsertionTool<G, A, R>(selectedSquareView, selectedSquareModel, objectChooser, pickmapSettings, insertionModeSet), defaultTool);
    }

    /**
     * Adds a tool to this tool selector.
     * @param tool the tool to add
     * @param defaultTool the name of the default tool
     */
    private void addTool(@NotNull final Tool<G, A, R> tool, @NotNull final String defaultTool) {
        add(tool, tool.getId().equals(defaultTool));
    }

    /**
     * Creates the user interface elements of the ToolSelector.
     */
    private void createUI() {
        setLayout(new BorderLayout());
        add(selectionPane, BorderLayout.NORTH);
        add(optionsPane, BorderLayout.CENTER);
    }

    /**
     * Adds a tool to this tool selector.
     * @param tool the tool to add
     */
    @SuppressWarnings("MethodOverloadsMethodOfSuperclass")
    public void add(@NotNull final Tool<G, A, R> tool) {
        add(tool, false);
    }

    /**
     * Adds a tool to this tool selector.
     * @param tool the tool to add
     * @param selected <code>true</code> if the tool should be made the selected
     * tool, otherwise <code>false</code>
     */
    private void add(@NotNull final Tool<G, A, R> tool, final boolean selected) {
        @NotNull final Action selectionAction = new SelectionAction(tool);
        @NotNull final AbstractButton toggleButton = new JToggleButton(selectionAction);
        toggleButton.setMargin(EMPTY_MARGIN);
        @NotNull final Component optionsView = createOptionsView(tool);
        selectionButtonGroup.add(toggleButton);
        selectionPane.add(toggleButton);
        final Container panel = new JPanel(new BorderLayout());
        panel.add(optionsView, BorderLayout.NORTH);
        optionsPane.add(panel, tool.getId());
        toggleButton.setSelected(selected);
        tools.put(tool.getId(), tool);
        if (selected) {
            selectedTool = tool;
            optionCards.show(optionsPane, tool.getId());
        }
    }

    /**
     * Creates the options view for a tool. This method is a delegate to {@link
     * Tool#createOptionsView()} but will provide a fallback if the tool doesn't
     * provide tweaking its options.
     * @param tool the tool to create options view for
     * @return the options view (the tool's options view or a dummy fallback if
     *         the tool doesn't provide an options view)
     */
    @NotNull
    private static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> Component createOptionsView(@NotNull final Tool<G, A, R> tool) {
        @Nullable final Component optionsView = tool.createOptionsView();
        return optionsView != null ? optionsView : new JPanel();
    }

    /**
     * Makes a tool the currently selected tool to edit its options.
     * @param tool the tool to select
     * @pre the tool must be controlled by this tool-selector.
     * @see #setSelectedTool(String)
     */
    private void setSelectedTool(@NotNull final Tool<G, A, R> tool) {
        selectedTool = tool;
        optionCards.show(optionsPane, tool.getId());
    }

    /**
     * Makes a tool the currently selected tool to edit its options. This method
     * exists to allow programs to store the currently selected tool in
     * preferences and restore it after startup.
     * @param id the ID of tool to select
     * @pre the tool must be controlled by this tool-selector.
     * @see #setSelectedTool(Tool)
     * @see Tool#getId()
     */
    public void setSelectedTool(@NotNull final String id) {
        setSelectedTool(tools.get(id));
    }

    /**
     * Returns the tool that is currently selected.
     * @return the currently selected tool
     */
    @NotNull
    public MouseOpListener<G, A, R> getSelectedTool() {
        return selectedTool;
    }

    /**
     * Action for selecting a tool.
     */
    private class SelectionAction extends AbstractAction {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * The tool to select with this action.
         */
        @NotNull
        private final Tool<G, A, R> tool;

        /**
         * Creates a SelectionAction.
         * @param tool the tool to select with this action
         */
        SelectionAction(@NotNull final Tool<G, A, R> tool) {
            this.tool = tool;
            ACTION_BUILDER.initAction(false, this, tool.getId());
        }

        @Override
        public void actionPerformed(@NotNull final ActionEvent e) {
            setSelectedTool(tool);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

    }

}
