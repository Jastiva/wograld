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

package net.sf.gridarta.model.archetypeset;

import java.util.Collection;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.ArchetypeSetListener;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface that captures similarities between different ArchetypeSet
 * implementations.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface ArchetypeSet<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Returns whether the Archetypes in this ArchetypeSet were loaded from an
     * archive.
     * @return <code>true</code> if loaded from an archive, otherwise
     *         <code>false</code>
     */
    boolean isLoadedFromArchive();

    /**
     * Returns the number of Archetypes available.
     * @return the number of Archetypes available
     */
    int getArchetypeCount();

    /**
     * Returns an Archetype by its name.
     * @param archetypeName the name of the Archetype to get
     * @return the archetype
     * @throws UndefinedArchetypeException if the no such archetype exists
     * @see #getOrCreateArchetype(String) for a similar method that creates
     *      undefined archetypes
     */
    @NotNull
    R getArchetype(@NotNull String archetypeName) throws UndefinedArchetypeException;

    /**
     * Returns an archetype by its name. If this archetype does not exist,
     * return an {@link Archetype} which has {@link Archetype#isUndefinedArchetype()}
     * set.
     * @param archetypeName the archetype name
     * @return the archetype
     * @see #getArchetype(String)
     */
    @NotNull
    R getOrCreateArchetype(@NotNull final String archetypeName);

    /**
     * Adds an Archetype to this Set.
     * @param archetype the archetype to add
     * @throws DuplicateArchetypeException if the archetype name is not unique
     */
    void addArchetype(@NotNull R archetype) throws DuplicateArchetypeException;

    /**
     * Registers an {@link ArchetypeSetListener}.
     * @param listener the listener to register
     */
    void addArchetypeSetListener(@NotNull ArchetypeSetListener<G, A, R> listener);

    /**
     * Removes an {@link ArchetypeSetListener}.
     * @param listener the listener to remove
     */
    void removeArchetypeSetListener(@NotNull ArchetypeSetListener<G, A, R> listener);

    /**
     * Returns a read-only collection of all {@link Archetype Archetypes}.
     * @return a read-only collection of all archetypes
     */
    @NotNull
    Collection<R> getArchetypes();

    void connectFaces();

    /**
     * Sets whether Archetypes were loaded from an archive.
     * @param loadedFromArchive <code>true</code> when loaded from archive,
     * otherwise <code>false</code>
     * @see #isLoadedFromArchive()
     */
    void setLoadedFromArchive(boolean loadedFromArchive);

    /**
     * Returns the image set.
     * @return the image set or <code>null</code>
     */
    @Nullable
    String getImageSet();
    
    public void addDataToFaces();

}
