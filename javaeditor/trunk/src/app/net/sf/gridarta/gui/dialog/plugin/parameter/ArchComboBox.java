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

package net.sf.gridarta.gui.dialog.plugin.parameter;

import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesModel;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ArchComboBox<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JComboBox {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ArchComboBox.class);

    private static final long serialVersionUID = 1L;

    private final ArchComboBoxEditor<G, A, R> archComboBoxEditor;

    private final ArchComboBoxModel<G, A, R> archComboBoxModel;

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public ArchComboBox(@NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final FaceObjectProviders faceObjectProviders) {
        setMaximumRowCount(4);
        archComboBoxModel = new ArchComboBoxModel<G, A, R>(archetypeSet);
        archComboBoxEditor = new ArchComboBoxEditor<G, A, R>(this, archComboBoxModel, objectChooser, gameObjectAttributesModel, faceObjectProviders);
        final ListCellRenderer cellRenderer = new ArchComboBoxCellRenderer(archComboBoxEditor, faceObjectProviders);
        //setPrototypeDisplayValue(cellRenderer.sizeTester);
        setRenderer(cellRenderer);
        setModel(archComboBoxModel);
        setEditable(true);
        setEditor(archComboBoxEditor);
        final Dimension d = getPreferredSize();
        if (log.isDebugEnabled()) {
            log.debug("Preferred size: " + d);
        }
        setPreferredSize(new Dimension(d.width, archComboBoxEditor.getEditorComponent().getPreferredSize().height));
        if (log.isDebugEnabled()) {
            log.debug("NEW Preferred size: " + getPreferredSize());
        }
    }

    public void editorEntryChange() {
        archComboBoxEditor.lockEditor();
        final Archetype<G, A, R> nearestMatch = archComboBoxModel.getNearestMatch(archComboBoxEditor.getEditor().getText());
        setSelectedItem(nearestMatch);
        archComboBoxEditor.setItem(nearestMatch);
        archComboBoxEditor.unlockEditor();
    }

}
