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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArchComboBoxModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractListModel implements ComboBoxModel {

    @Nullable
    private Object value;

    @NotNull
    private final List<Archetype<G, A, R>> archList;

    // FIXME: This constant looks pretty pointless.

    @NotNull
    private static final String CURRENT_FILTER = "";

    private static final long serialVersionUID = 1L;

    public ArchComboBoxModel(@NotNull final ArchetypeSet<G, A, R> archetypeSet) {
        final Collection<R> archetypes = archetypeSet.getArchetypes();
        archList = new ArrayList<Archetype<G, A, R>>(archetypes);
        Collections.sort(archList, new Comparator<Archetype<G, A, R>>() {

            @Override
            public int compare(final Archetype<G, A, R> o1, final Archetype<G, A, R> o2) {
                return o1.getArchetypeName().toLowerCase().compareTo(o2.getArchetypeName().toLowerCase());
            }
        });
    }

    @Nullable
    @Override
    public Object getSelectedItem() {
        return value;
    }

    @Override
    public void setSelectedItem(final Object anItem) {
        value = anItem;
    }

    @Override
    public int getSize() {
        return archList.size();
    }

    @NotNull
    @Override
    public Object getElementAt(final int index) {
        return archList.get(index);
    }

    public void setFilter(@NotNull final String filter) {
        if (filter.startsWith(CURRENT_FILTER)) {
            narrowFilter(filter);
        } else if (CURRENT_FILTER.startsWith(filter)) {
            enlargeFilter(filter);
        } else {
            final int p = getCommonPrefix(CURRENT_FILTER, filter);
            enlargeFilter(filter.substring(0, p));
            narrowFilter(filter);
        }
        fireContentsChanged(this, 0, archList.size());
    }

    private static int getCommonPrefix(@NotNull final CharSequence s1, @NotNull final CharSequence s2) {
        int i = 0;
        while (s1.length() > i && s2.length() > i && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    private void enlargeFilter(@NotNull final String filter) {
        // "abcd" -> "abc"
    }

    private void narrowFilter(@NotNull final String filter) {
        // "abc" -> "abcd"
    }

    /**
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    public Archetype<G, A, R> getNearestMatch(@NotNull final String name) {
        int pos = Collections.binarySearch(archList, name, new Comparator<Object>() {

            @Override
            public int compare(final Object o1, final Object o2) {
                final String s1;
                if (o1 instanceof Archetype) {
                    s1 = ((Archetype<?, ?, ?>) o1).getArchetypeName();
                } else {
                    s1 = o1.toString();
                }
                final String s2;
                if (o2 instanceof Archetype) {
                    s2 = ((Archetype<?, ?, ?>) o2).getArchetypeName();
                } else {
                    s2 = o2.toString();
                }
                return s1.compareToIgnoreCase(s2);
            }
        });
        if (pos < 0) {
            pos = -(pos + 1);
        }
        if (pos >= archList.size()) {
            pos = archList.size() - 1;
        }
        if (pos < 0) {
            pos = 0;
        }
        return archList.get(pos);
    }

}
