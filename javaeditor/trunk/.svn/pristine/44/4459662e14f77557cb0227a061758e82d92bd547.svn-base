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

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooserTab;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserFolder;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserPanel;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * That control of the archetype chooser.
 * @author Andreas Kirschbaum
 */
public class ArchetypeChooserControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<R>, ObjectChooserTab<G, A, R> {

    /**
     * The action builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Maps archetype to archetype panel containing the archetype.
     */
    @NotNull
    private final Map<R, ArchetypePanel<G, A, R>> archetypes = new HashMap<R, ArchetypePanel<G, A, R>>();

    /**
     * The archetype chooser's model.
     */
    @NotNull
    private final ArchetypeChooserModel<G, A, R> archetypeChooserModel;

    /**
     * The archetype chooser's view.
     */
    @NotNull
    private final ArchetypeChooserView<G, A, R> archetypeChooserView;

    /**
     * Creates a new instance.
     * @param archetypeChooserModel the archetype chooser model to use; no new
     * archetypes must be added afterwards
     * @param createDirectionPane whether to create a "direction" panel
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public ArchetypeChooserControl(@NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, final boolean createDirectionPane, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.archetypeChooserModel = archetypeChooserModel;
        archetypeChooserView = new ArchetypeChooserView<G, A, R>(createDirectionPane, archetypeChooserModel, faceObjectProviders);

        for (final ArchetypeChooserPanel<G, A, R> panel : archetypeChooserModel.getPanels()) {
            final ArchetypePanel<G, A, R> archetypePanel = archetypeChooserView.findOrCreatePanel(panel.getName());
            final ArchetypeChooserFolder<G, A, R> folder = panel.getDefaultFolder();
            for (final R archetype : folder.getArchetypes()) {
                archetypes.put(archetype, archetypePanel);
            }
        }

    }

    /**
     * Select an archetype. If necessary, select the archetype's tab.
     * @param archetype the archetype to select
     */
    public void selectArchetype(@NotNull final R archetype) {
        final ArchetypePanel<G, A, R> panel = archetypes.get(archetype);
        if (panel == null) {
            return;
        }

        archetypeChooserView.setSelectedPanel(panel);
        panel.selectArchetype(archetype);
    }

    /**
     * Returns all archetypes in the panel. The archetypes are not sorted..
     * @return an iterator returning all archetypes
     */
    @NotNull
    @Override
    public Iterator<R> iterator() {
        return Collections.unmodifiableSet(archetypes.keySet()).iterator();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Component getComponent() {
        return archetypeChooserView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive(final boolean active) {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final G gameObject) {
        return getSelection() == gameObject.getArchetype();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public R getSelection() {
        final ArchetypeChooserPanel<G, A, R> selectedPanel = archetypeChooserModel.getSelectedPanel();
        return selectedPanel != null ? selectedPanel.getSelectedFolder().getSelectedArchetype() : null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<R> getSelections() {
        final R archObject = getSelection();
        if (archObject == null) {
            return Collections.emptyList();
        }

        final List<R> result = new ArrayList<R>(1);
        result.add(archObject);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getTitle() {
        return ActionBuilderUtils.getString(ACTION_BUILDER, "objectChooser.archetypesTabTitle");
    }

}
