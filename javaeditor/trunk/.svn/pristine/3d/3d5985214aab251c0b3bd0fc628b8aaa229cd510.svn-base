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

package net.sf.gridarta.gui.panel.archetypechooser;

import net.sf.gridarta.gui.utils.DirectionComponent;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserFolder;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModelListener;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserPanel;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A GUI component for selecting a direction.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class DirectionPane<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DirectionComponent {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ArchetypeChooserModel} to affect.
     * @serial
     */
    @NotNull
    private final ArchetypeChooserModel<G, A, R> archetypeChooserModel;

    /**
     * The {@link ArchetypeChooserModelListener} attached to {@link
     * #archetypeChooserModel}.
     */
    @NotNull
    private final ArchetypeChooserModelListener<G, A, R> archetypeChooserModelListener = new ArchetypeChooserModelListener<G, A, R>() {

        @Override
        public void selectedPanelChanged(@NotNull final ArchetypeChooserPanel<G, A, R> selectedPanel) {
            // ignore
        }

        @Override
        public void selectedFolderChanged(@NotNull final ArchetypeChooserFolder<G, A, R> selectedFolder) {
            // ignore
        }

        @Override
        public void selectedArchetypeChanged(@Nullable final R selectedArchetype) {
            updateEnabled(selectedArchetype);
        }

        @Override
        public void directionChanged(@Nullable final Integer direction) {
            updateDirection(direction);
        }

    };

    /**
     * Creates a new instance.
     * @param archetypeChooserModel the archetype chooser model to affect
     * @param includeDefault whether the "default" button should be shown
     */
    public DirectionPane(@NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, final boolean includeDefault) {
        super(includeDefault);
        this.archetypeChooserModel = archetypeChooserModel;
        archetypeChooserModel.addArchetypeChooserModelListener(archetypeChooserModelListener);
        final ArchetypeChooserPanel<G, A, R> selectedPanel = archetypeChooserModel.getSelectedPanel();
        updateEnabled(selectedPanel == null ? null : selectedPanel.getSelectedFolder().getSelectedArchetype());
        updateDirection(archetypeChooserModel.getDirection());
    }

    /**
     * Enables/disables the direction buttons for a given archetype.
     * @param baseObject the archetype to check
     */
    private void updateEnabled(@Nullable final BaseObject<?, ?, ?, ?> baseObject) {
        updateEnabled(baseObject != null && baseObject.usesDirection());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void direction(@Nullable final Integer direction) {
        archetypeChooserModel.setDirection(direction);
    }

}
