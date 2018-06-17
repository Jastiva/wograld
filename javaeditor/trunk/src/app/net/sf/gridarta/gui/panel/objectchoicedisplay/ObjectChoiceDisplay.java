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

package net.sf.gridarta.gui.panel.objectchoicedisplay;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The object choice display shows information about the selected object in the
 * object chooser.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author Andreas Kirschbaum
 */
public class ObjectChoiceDisplay extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link ArchetypeTypeSet} for looking up game object types.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * Displays the selected game object's name.
     */
    @NotNull
    private final JLabel gameObjectNameLabel = new JLabel();

    /**
     * Display the selected game object's archetype name.
     */
    @NotNull
    private final JLabel gameObjectArchetypeNameLabel = new JLabel();

    /**
     * Display the selected game object's type information.
     */
    @NotNull
    private final JLabel gameObjectTypeLabel = new JLabel();

    /**
     * Displays the selected game object's extents.
     */
    @NotNull
    private final JLabel gameObjectExtentsLabel = new JLabel();

    /**
     * Creates a new instance.
     * @param archetypeTypeSet the archetype type set
     */
    public ObjectChoiceDisplay(@NotNull final ArchetypeTypeSet archetypeTypeSet) {
        this.archetypeTypeSet = archetypeTypeSet;
        setLayout(new GridBagLayout());

        final GridBagConstraints gcl = new GridBagConstraints();
        final GridBagConstraints gcr = new GridBagConstraints();
        gcl.fill = GridBagConstraints.HORIZONTAL;
        gcr.fill = GridBagConstraints.HORIZONTAL;
        gcl.weightx = 0.0;
        gcr.weightx = 1.0;
        gcl.anchor = GridBagConstraints.WEST;
        gcr.anchor = GridBagConstraints.WEST;
        gcl.gridwidth = 1;
        gcr.gridwidth = GridBagConstraints.REMAINDER;

        add(new JLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.captionName")), gcl);
        add(gameObjectNameLabel, gcr);

        add(new JLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.captionArchetype")), gcl);
        add(gameObjectArchetypeNameLabel, gcr);

        add(new JLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.captionType")), gcl);
        add(gameObjectTypeLabel, gcr);

        add(new JLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.captionTile")), gcl);
        add(gameObjectExtentsLabel, gcr);
    }

    /**
     * Displays information about the selected game object.
     * @param gameObject the game object to display; this may be any part of a
     * multi-part object or <code>null</code> to clear the attributes
     * @param isPickmapActive whether the pickmap chooser (<code>true</code>) or
     * the archetype chooser (<code>false</code>) is active
     */
    public void showObjectChooserQuickObject(@Nullable final BaseObject<?, ?, ?, ?> gameObject, final boolean isPickmapActive) {
        if (gameObject == null) {
            if (isPickmapActive) {
                gameObjectNameLabel.setText(ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.randomPick"));
                gameObjectNameLabel.setForeground(Color.BLUE);
            } else {
                gameObjectNameLabel.setText(null);
            }
            gameObjectArchetypeNameLabel.setText(null);
            gameObjectTypeLabel.setText(null);
            gameObjectExtentsLabel.setText(null);
        } else {
            final BaseObject<?, ?, ?, ?> headObject = gameObject.getHead();
            final String objName = headObject.getBestName();
            gameObjectNameLabel.setForeground(Color.BLACK);
            gameObjectNameLabel.setText(objName);

            gameObjectArchetypeNameLabel.setText(headObject.getArchetype().getArchetypeName());

            gameObjectTypeLabel.setText(archetypeTypeSet.getDisplayName(headObject));

            if (headObject.isMulti()) {
                gameObjectExtentsLabel.setText(ACTION_BUILDER.format("objectChooser.tileSize", headObject.getMultiRefCount(), headObject.getSizeX(), headObject.getSizeY()));
            } else {
                gameObjectExtentsLabel.setText(ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.tileSingle"));
            }
        }
    }

}
