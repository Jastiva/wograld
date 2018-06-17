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

package net.sf.gridarta.gui.dialog.findarchetypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The model for displaying a set of archetypes. For each archetype the
 * following information is shown: <ul> <li>The in-game object name <li>The
 * archetype name <li>The object type <li>The folder location in the archetype
 * chooser </ul>
 * @author Andreas Kirschbaum
 */
public class TableModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractTableModel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The column titles.
     */
    private static final String[] columnName = { ActionBuilderUtils.getString(ACTION_BUILDER, "findArchetypesColumnName"), ActionBuilderUtils.getString(ACTION_BUILDER, "findArchetypesColumnArch"), ActionBuilderUtils.getString(ACTION_BUILDER, "findArchetypesColumnType"), ActionBuilderUtils.getString(ACTION_BUILDER, "findArchetypesColumnFolder"), };

    /**
     * The instance for looking up archetype types.
     */
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The model's contents.
     * @serial
     */
    private final List<R> archetypes = new ArrayList<R>();

    /**
     * Reflects the current sorting of the model's contents.
     * @serial
     */
    @Nullable
    private Integer[] sorting;

    /**
     * Comparator for sorting the model's contents. The sort order is:
     * case-insensitive in-game object name, case-insensitive archetype name,
     * archetype name.
     */
    private final Comparator<Integer> comparator = new Comparator<Integer>() {

        @Override
        public int compare(final Integer o1, final Integer o2) {
            final Archetype<G, A, R> archetype1 = archetypes.get(o1);
            final Archetype<G, A, R> archetype2 = archetypes.get(o2);

            final int cmpBestName = archetype1.getBestName().compareToIgnoreCase(archetype2.getBestName());
            if (cmpBestName != 0) {
                return cmpBestName;
            }

            final int cmpArchetypeName = archetype1.getArchetypeName().compareToIgnoreCase(archetype2.getArchetypeName());
            if (cmpArchetypeName != 0) {
                return cmpArchetypeName;
            }

            return archetype1.getArchetypeName().compareTo(archetype2.getArchetypeName());
        }

    };

    /**
     * Creates a new instance.
     * @param archetypeTypeSet the instance for looking up archetype types
     */
    public TableModel(final ArchetypeTypeSet archetypeTypeSet) {
        this.archetypeTypeSet = archetypeTypeSet;
    }

    /**
     * Clear the model's contents.
     */
    public void clear() {
        archetypes.clear();
        sorting = null;
    }

    /**
     * Add an archetype to the model.
     * @param archetype the archetype to add
     */
    public void add(@NotNull final R archetype) {
        archetypes.add(archetype);
    }

    /**
     * Return one archetype.
     * @param index the table row index
     * @return the row content
     */
    @NotNull
    @SuppressWarnings("TypeMayBeWeakened")
    public R get(final int index) {
        return archetypes.get(sorting[index]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return 4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return archetypes.size();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Archetype<G, A, R> archetype = archetypes.get(sorting[rowIndex]);
        switch (columnIndex) {
        case 0:
            return archetype.getBestName();

        case 1:
            return archetype.getArchetypeName();

        case 2:
            return archetypeTypeSet.getDisplayName(archetype);

        case 3:
            return archetype.getEditorFolder();
        }

        throw new IllegalArgumentException();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getColumnName(final int column) {
        return columnName[column];
    }

    /**
     * Sort the table contents by name.
     */
    public void sortTable() {
        sorting = new Integer[archetypes.size()];
        for (int i = 0; i < sorting.length; i++) {
            sorting[i] = i;
        }

        Arrays.sort(sorting, comparator);
    }

    /**
     * Finish updating the model's contents. This function must be called after
     * one or more elements have been added with {@link #add(Archetype)}. If at
     * least one element has been added, {@link #sortTable()} must be called
     * first.
     */
    public void finishUpdate() {
        if (sorting == null && !archetypes.isEmpty()) {
            throw new IllegalStateException();
        }

        fireTableDataChanged();
    }

    /**
     * Return the row index of an archetype.
     * @param archetype the archetype to look up
     * @return the row index, or <code>-1</code> if the archetype is not part of
     *         the model
     */
    public int findTableIndex(@Nullable final R archetype) {
        if (archetype != null) {
            final int archetypeIndex = archetypes.indexOf(archetype);
            if (archetypeIndex != -1) {
                for (final Integer index : sorting) {
                    if (archetypeIndex == sorting[index]) {
                        return index;
                    }
                }
            }
        }

        if (!archetypes.isEmpty()) {
            return 0;
        }

        return -1;
    }

}
