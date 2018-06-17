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
import java.awt.Container;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import net.sf.gridarta.gui.map.event.MouseOpEvent;
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
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Pane for controlling which mouse to select and configure tools for. Also
 * serves as a proxy for querying which tool is bound to which mouse button.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ToolPalette<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta.gui.panel.tools");

    /**
     * The ToolSelector for lmb.
     */
    private final ToolSelector<G, A, R> lmbSelector;

    /**
     * The ToolSelector for mmb.
     */
    private final ToolSelector<G, A, R> mmbSelector;

    /**
     * The ToolSelector for rmb.
     */
    private final ToolSelector<G, A, R> rmbSelector;

    /**
     * Create a ToolPalette.
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
    public ToolPalette(@NotNull final MapViewSettings mapViewSettings, @NotNull final SelectedSquareView<G, A, R> selectedSquareView, @NotNull final SelectedSquareModel<G, A, R> selectedSquareModel, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final PickmapSettings pickmapSettings, @Nullable final GameObjectMatcher floorGameObjectMatcher, @Nullable final GameObjectMatcher wallGameObjectMatcher, @Nullable final GameObjectMatcher monsterGameObjectMatcher, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        super(new BorderLayout());
        lmbSelector = new ToolSelector<G, A, R>("selection", mapViewSettings, selectedSquareView, selectedSquareModel, objectChooser, pickmapSettings, floorGameObjectMatcher, wallGameObjectMatcher, monsterGameObjectMatcher, insertionModeSet);
        mmbSelector = new ToolSelector<G, A, R>("deletion", mapViewSettings, selectedSquareView, selectedSquareModel, objectChooser, pickmapSettings, floorGameObjectMatcher, wallGameObjectMatcher, monsterGameObjectMatcher, insertionModeSet);
        rmbSelector = new ToolSelector<G, A, R>("insertion", mapViewSettings, selectedSquareView, selectedSquareModel, objectChooser, pickmapSettings, floorGameObjectMatcher, wallGameObjectMatcher, monsterGameObjectMatcher, insertionModeSet);
        add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mouse"), BorderLayout.NORTH);
        final Container mouseTabs = new JTabbedPane();
        mouseTabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "left"), lmbSelector);
        mouseTabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "middle"), mmbSelector);
        mouseTabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "right"), rmbSelector);
        add(mouseTabs);
    }

    /**
     * Returns a {@link MouseOpListener} depending on the <code>event</code> and
     * the default settings.
     * @param event this event's button and modifiers are needed
     * @return the default <code>MouseOpListener</code> or <code>null</code> if
     *         mouse button is not bound to a tool
     */
    @Nullable
    public MouseOpListener<G, A, R> getTool(final MouseOpEvent<G, A, R> event) {
        final int button = event.getButton();
        final ToolSelector<G, A, R> toolSelector;
        switch (button) {
        case MouseEvent.BUTTON1:
            toolSelector = lmbSelector;
            break;
        case MouseEvent.BUTTON2:
            toolSelector = mmbSelector;
            break;
        case MouseEvent.BUTTON3:
            toolSelector = rmbSelector;
            break;
        default:
            final int mask = event.getModifiers();
            if ((mask & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK) {
                toolSelector = lmbSelector;
            } else if ((mask & InputEvent.BUTTON2_DOWN_MASK) == InputEvent.BUTTON2_DOWN_MASK) {
                toolSelector = mmbSelector;
            } else if ((mask & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK) {
                toolSelector = rmbSelector;
            } else {
                return null;
            }
            break;
        }
        return toolSelector.getSelectedTool();
    }

}
