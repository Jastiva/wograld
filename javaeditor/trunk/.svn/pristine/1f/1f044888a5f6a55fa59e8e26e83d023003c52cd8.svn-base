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

import javax.swing.JPanel;
import net.sf.gridarta.gui.panel.gameobjecttexteditor.GameObjectTextEditor;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The tab containing the game object text editor.
 * @author Andreas Kirschbaum
 */
public class TextEditorTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObjectAttributesTab<G, A, R> {

    /**
     * The displayed {@link GameObjectTextEditor} instance.
     */
    @NotNull
    private final GameObjectTextEditor gameObjectTextEditor;

    /**
     * Creates a new instance.
     * @param gameObjectAttributesModel the model to track
     * @param archetypeTypeSet the archetype type set
     */
    public TextEditorTab(@NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        super(gameObjectAttributesModel);
        gameObjectTextEditor = new GameObjectTextEditor(archetypeTypeSet);
        addAutoApply(gameObjectTextEditor.getTextPane());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Text Editor";
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JPanel getPanel() {
        return gameObjectTextEditor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void refresh(@Nullable final G gameObject) {
        setTabSeverity(gameObjectTextEditor.refreshDisplay(gameObject));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canApply() {
        return gameObjectTextEditor.canApplyChanges();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        gameObjectTextEditor.activate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void apply(@NotNull final G gameObject) {
        gameObjectTextEditor.applyChanges(gameObject);
    }

}
