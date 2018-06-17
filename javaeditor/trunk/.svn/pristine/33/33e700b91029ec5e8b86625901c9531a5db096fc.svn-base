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
 * A {@link MatchCriteria} that matches by object name.
 * @author Andreas Kirschbaum
 */
public class ObjectNameMatchCriteria<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MatchCriteria<G, A, R> {

    /**
     * The object name to match.
     */
    @NotNull
    private final Pattern objectName;

    /**
     * Creates a new instance.
     * @param objectName the object name to match
     */
    public ObjectNameMatchCriteria(@NotNull final String objectName) {
        this.objectName = Pattern.compile(GlobFileFilter.createPatternForGlob(objectName), Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(@NotNull final G gameObject) {
        return objectName.matcher(gameObject.getBestName()).find();
    }

}
