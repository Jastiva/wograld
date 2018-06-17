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

package net.sf.gridarta.gui.misc;

import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesControl;
import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesTab;
import net.sf.gridarta.gui.panel.gameobjectattributes.TextEditorTab;
import net.sf.gridarta.gui.utils.tabbedpanel.Tab;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements actions related to the {@link MainView}.
 * @author Andreas Kirschbaum
 */
public class MainViewActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link MainView} instance.
     */
    @NotNull
    private final MainView mainView;

    /**
     * The {@link GameObjectAttributesControl} to display.
     */
    @NotNull
    private final GameObjectAttributesControl<G, A, R> gameObjectAttributesControl;

    /**
     * The game object {@link Tab} to display.
     */
    @NotNull
    private final Tab gameObjectTab;

    /**
     * The {@link GameObjectAttributesTab} that displays the game object text
     * editor.
     */
    @NotNull
    private final TextEditorTab<G, A, R> textEditorTab;

    /**
     * Whether a previously selected tab has been remembered. Applies to {@link
     * #prevTab} and {@link #prevTab2}.
     */
    private boolean prevTabSaved;

    /**
     * The previously active {@link Tab} before the last switch to {@link
     * #gameObjectTab}. Set to <code>null</code> if not yet switched or if no
     * other tab was active. Ignored unless {@link #prevTabSaved} is set.
     */
    @Nullable
    private Tab prevTab;

    /**
     * The previously active {@link GameObjectAttributesTab} in {@link
     * #gameObjectTab} before the last switch to {@link #textEditorTab}. Set to
     * <code>null</code> if not yet switched or if no other tab was active.
     * Ignored unless {@link #prevTabSaved} is set.
     */
    @Nullable
    private GameObjectAttributesTab<G, A, R> prevTab2;

    /**
     * Creates a new instance.
     * @param mainView the main view instance
     * @param gameObjectAttributesControl the game object attributes control to
     * display
     * @param gameObjectTab the game object tab to display
     * @param textEditorTab the game object attributes tab that displays the
     * game object text editor
     */
    public MainViewActions(@NotNull final MainView mainView, @NotNull final GameObjectAttributesControl<G, A, R> gameObjectAttributesControl, @NotNull final Tab gameObjectTab, @NotNull final TextEditorTab<G, A, R> textEditorTab) {
        this.mainView = mainView;
        this.gameObjectAttributesControl = gameObjectAttributesControl;
        this.gameObjectTab = gameObjectTab;
        this.textEditorTab = textEditorTab;
    }

    /**
     * Action method to open the game object text editor.
     */
    @ActionMethod
    public void gameObjectTextEditor() {
        final Tab activeTab = mainView.getActiveTab(gameObjectTab.getLocation(), gameObjectTab.isAlternativeLocation());
        final GameObjectAttributesTab<G, A, R> activeTab2 = gameObjectAttributesControl.getSelectedTab();
        final boolean gameObjectTextEditorIsActive = activeTab != null && activeTab == gameObjectTab && activeTab2 == textEditorTab;
        if (!gameObjectTextEditorIsActive) {
            // save state and activate game object text editor
            prevTabSaved = true;
            prevTab = activeTab;
            prevTab2 = activeTab2;

            gameObjectTab.getButton().setSelected(true);
            gameObjectAttributesControl.selectTab(textEditorTab);
            textEditorTab.activate();
        } else if (prevTabSaved) {
            prevTabSaved = false;
            // restore saved state
            if (prevTab != null) {
                prevTab.getButton().setSelected(true);
                prevTab = null;
            } else {
                gameObjectTab.getButton().setSelected(false);
            }
            if (prevTab2 != null) {
                gameObjectAttributesControl.selectTab(prevTab2);
                prevTab2.activate();
                prevTab2 = null;
            }
        }
    }

}
