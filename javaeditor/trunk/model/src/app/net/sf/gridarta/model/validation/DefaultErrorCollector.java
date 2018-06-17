/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.errors.ValidationError;
import org.jetbrains.annotations.NotNull;

/**
 * Simple error collector that just collects the error in a collection for later
 * retrieval through iteration.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DefaultErrorCollector<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ErrorCollector<G, A, R>, Iterable<ValidationError<G, A, R>> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Errors.
     */
    @NotNull
    private final List<ValidationError<G, A, R>> errors = new ArrayList<ValidationError<G, A, R>>();

    /**
     * Whether {@link #errors} is sorted.
     */
    private boolean sorted = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public void collect(@NotNull final ValidationError<G, A, R> error) {
        errors.add(error);
        sorted = false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<ValidationError<G, A, R>> iterator() {
        if (!sorted) {
            sorted = true;
            Collections.sort(errors, new ValidationErrorComparator<G, A, R>());
        }
        return Collections.unmodifiableList(errors).iterator();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterable<ValidationError<G, A, R>> getErrors() {
        return this;
    }

}
