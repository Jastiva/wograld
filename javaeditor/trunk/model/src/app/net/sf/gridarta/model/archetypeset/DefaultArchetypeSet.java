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

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetype.ArchetypeSetListener;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base implementation of ArchetypeSet.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DefaultArchetypeSet<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ArchetypeSet<G, A, R> {

    /**
     * The archetype factory to use.
     */
    @NotNull
    private final ArchetypeFactory<G, A, R> archetypeFactory;

    /**
     * The image set to use.
     */
    @Nullable
    private final String imageSet;

    /**
     * The defined Archetypes mapped by name. The defined Archetypes are sorted
     * by load order. Collecting them must retain the load order. When they are
     * loaded from the collection, the editor_folders must not be mixed.
     * @todo solve this issue
     */
    private final Map<String, R> archetypeMap = new LinkedHashMap<String, R>();

    /**
     * Whether Archetypes were loaded form an archive.
     * @val <code>true</code> when Archetypes were loaded from the big collected
     * archive files,
     * @val <code>false</code> when Archetypes were loaded from individual
     * .arc-files
     */
    private boolean loadedFromArchive;

    /**
     * The archetypes used for game objects which reference undefined
     * archetypes. It maps archetype name to archetype.
     */
    private final Map<String, WeakReference<R>> undefinedArchetypes = new HashMap<String, WeakReference<R>>();

    /**
     * The ArchetypeSetListeners to inform of changes.
     */
    private final EventListenerList2<ArchetypeSetListener<G, A, R>> listenerList = new EventListenerList2<ArchetypeSetListener<G, A, R>>(ArchetypeSetListener.class);

    /**
     * Create an DefaultArchetypeSet.
     * @param archetypeFactory the archetype factory to use
     * @param imageSet the image set to use
     */
    public DefaultArchetypeSet(@NotNull final ArchetypeFactory<G, A, R> archetypeFactory, @Nullable final String imageSet) {
        this.archetypeFactory = archetypeFactory;
        this.imageSet = imageSet;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getImageSet() {
        return imageSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLoadedFromArchive() {
        return loadedFromArchive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getArchetypeCount() {
        return archetypeMap.size();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public R getArchetype(@NotNull final String archetypeName) throws UndefinedArchetypeException {
        final R archetype = archetypeMap.get(archetypeName);
        if (archetype == null) {
            throw new UndefinedArchetypeException(archetypeName);
        }

        return archetype;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public R getOrCreateArchetype(@NotNull final String archetypeName) {
        try {
            return getArchetype(archetypeName);
        } catch (final UndefinedArchetypeException ignored) {
            synchronized (undefinedArchetypes) {
                final WeakReference<R> existingUndefinedArchetypeRef = undefinedArchetypes.get(archetypeName);
                if (existingUndefinedArchetypeRef != null) {
                    final R existingUndefinedArchetype = existingUndefinedArchetypeRef.get();
                    if (existingUndefinedArchetype != null) {
                        return existingUndefinedArchetype;
                    }
                }

                final R newArchetype = archetypeFactory.newUndefinedArchetype(archetypeName);
                undefinedArchetypes.put(archetypeName, new WeakReference<R>(newArchetype));
                return newArchetype;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addArchetype(@NotNull final R archetype) throws DuplicateArchetypeException {
        final String name = archetype.getArchetypeName();
        if (archetypeMap.containsKey(name)) {
            throw new DuplicateArchetypeException(name);
        }

        archetypeMap.put(name, archetype);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLoadedFromArchive(final boolean loadedFromArchive) {
        if (this.loadedFromArchive == loadedFromArchive) {
            return;
        }

        this.loadedFromArchive = loadedFromArchive;
        fireLoadedFromArchiveChangedEvent();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Collection<R> getArchetypes() {
        return Collections.unmodifiableCollection(archetypeMap.values());
    }

    /**
     * Register an ArchetypeSetListener.
     * @param listener ArchetypeSetListener to register
     */
    @Override
    public void addArchetypeSetListener(@NotNull final ArchetypeSetListener<G, A, R> listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeArchetypeSetListener(@NotNull final ArchetypeSetListener<G, A, R> listener) {
        listenerList.remove(listener);
    }

    /**
     * Notifies all listeners that {@link #isLoadedFromArchive()} has changed.
     */
    private void fireLoadedFromArchiveChangedEvent() {
        for (final ArchetypeSetListener<G, A, R> listener : listenerList.getListeners()) {
            listener.loadedFromArchiveChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectFaces() {
      for (final BaseObject<G, A, R, ?> archetype : getArchetypes()) {
          archetype.setObjectFace();
      }
        
    }
    
    @Override
    public void addDataToFaces(){
        for(final Archetype archetype : getArchetypes()){
           
            archetype.augmentFacedata();
        }
    } 

}
