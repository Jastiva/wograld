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

package net.sf.gridarta.model.select;

import java.util.regex.Pattern;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.japi.util.filter.file.GlobFileFilter;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MatchCriteria} that matches by archetype name.
 * @author Andreas Kirschbaum
 */
public class ArchetypeNameMatchCriteria<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MatchCriteria<G, A, R> {

    /**
     * The archetype name to match.
     */
    @NotNull
    private final Pattern archetypeName;

    /**
     * Creates a new instance.
     * @param archetypeName the archetype name to match
     */
    public ArchetypeNameMatchCriteria(@NotNull final String archetypeName) {
        this.archetypeName = Pattern.compile(GlobFileFilter.createPatternForGlob(archetypeName), Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(@NotNull final G gameObject) {
        return archetypeName.matcher(gameObject.getArchetype().getArchetypeName()).matches();
    }

}
