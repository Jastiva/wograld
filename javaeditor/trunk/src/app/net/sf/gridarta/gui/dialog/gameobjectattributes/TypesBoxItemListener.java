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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ItemListener for the type-selection box on the attribute-dialog.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class TypesBoxItemListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ItemListener {

    /**
     * The attribute dialog main frame.
     */
    @NotNull
    private final GameObjectAttributesDialog<G, A, R> gameObjectAttributesDialog;

    /**
     * The game object which has the error to be added.
     */
    @NotNull
    private final GameObject<G, A, R> gameObject;

    /**
     * The {@link ArchetypeTypeSet} to display.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The {@link DialogAttribute DialogAttributes} to update.
     */
    @NotNull
    private final Collection<DialogAttribute<G, A, R, ?>> dialogAttributes;

    /**
     * The selection box for the type.
     */
    @NotNull
    private final JComboBox typeComboBox;

    /**
     * Reference to the type data.
     * @serial
     */
    @NotNull
    private ArchetypeType archetypeType;

    /**
     * The latest deselected item.
     */
    @Nullable
    private String deselected;

    /**
     * While <code>true</code>, this listener ignores all events.
     */
    private boolean ignoreEvent;

    /**
     * The position of this type in the type list. This differs from the
     * GameObject if the type is undefined.
     * @serial
     */
    private int type;

    /**
     * Creates a new instance.
     * @param frameNew the attribute dialog main frame
     * @param gameObject the game object which has the error to be added
     * @param type the initially selected type
     * @param archetypeTypeSet the archetype type set to display
     * @param dialogAttributes the dialog attributes to update
     * @param typeComboBox the selection box for the type
     * @param archetypeType the references to the data type
     */
    public TypesBoxItemListener(@NotNull final GameObjectAttributesDialog<G, A, R> frameNew, @NotNull final GameObject<G, A, R> gameObject, final int type, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final Collection<DialogAttribute<G, A, R, ?>> dialogAttributes, @NotNull final JComboBox typeComboBox, @NotNull final ArchetypeType archetypeType) {
        gameObjectAttributesDialog = frameNew;
        this.gameObject = gameObject;
        this.type = type;
        this.archetypeTypeSet = archetypeTypeSet;
        this.dialogAttributes = new ArrayList<DialogAttribute<G, A, R, ?>>(dialogAttributes);
        this.typeComboBox = typeComboBox;
        this.archetypeType = archetypeType;
        ignoreEvent = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(final ItemEvent e) {
        if (ignoreEvent) {
            return;
        }

        if (e.getStateChange() == ItemEvent.DESELECTED) {
            deselected = ((String) e.getItem()).trim();
        } else if (e.getStateChange() == ItemEvent.SELECTED && !e.getItem().equals(deselected)) {
            final ArchetypeType newType = archetypeTypeSet.getArchetypeTypeByName((String) e.getItem());

            typeComboBox.hidePopup();
            gameObjectAttributesDialog.update(gameObjectAttributesDialog.getGraphics());

            if (deselected == null) {
                deselected = archetypeType.getTypeName();
            }
            if (JOptionPane.showConfirmDialog(gameObjectAttributesDialog, "Do you really want to change the type of this\n" + "object from \"" + deselected + "\" to \"" + newType.getTypeName() + "\"?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                archetypeType = newType;

                final MapModel<G, A, R> mapModel = gameObject.getMapSquare().getMapModel();
                mapModel.beginTransaction("change type to " + newType.getTypeName());
                try {
                    gameObject.setAttributeInt(BaseObject.TYPE, newType.getTypeNo());
                } finally {
                    mapModel.endTransaction();
                }
                dialogAttributes.clear();

                type = typeComboBox.getSelectedIndex();

                gameObjectAttributesDialog.buildAttribute();
                gameObjectAttributesDialog.update(gameObjectAttributesDialog.getGraphics());
            } else {
                ignoreEvent = true;
                typeComboBox.setSelectedIndex(type);
                ignoreEvent = false;
            }
        }
    }

    /**
     * Returns a reference to the type data.
     * @return the reference
     */
    @NotNull
    public ArchetypeType getArchetypeType() {
        return archetypeType;
    }

}
