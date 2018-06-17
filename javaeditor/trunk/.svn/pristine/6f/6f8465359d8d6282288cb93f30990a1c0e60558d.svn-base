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

package net.sf.gridarta.model.gameobject;

import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.AbstractBaseObject;
import net.sf.gridarta.model.baseobject.BaseObjectVisitor;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class of {@link GameObject} implementations.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public abstract class AbstractGameObject<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractBaseObject<G, A, R, G> implements GameObject<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link Archetype} of this game object. Set to <code>null</code> if
     * not yet known.
     * @serial
     */
    @NotNull
    private R archetype;

    /**
     * Container of this {@link GameObject}. There are two possibilities for the
     * container: <ul> <li>Another game object, which means this object is in
     * the inventory of that game object.</li> <li>A {@link MapSquare}, which
     * means that this game object is top level on that map square.</li> </ul>
     * @serial
     */
    @Nullable
    private GameObjectContainer<G, A, R> container;

    /**
     * Creates a new instance.
     * @param archetype the archetype associated with with game object
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    protected AbstractGameObject(@NotNull final R archetype, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(faceObjectProviders, animationObjects);
        this.archetype = archetype;
        updateArchetype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getAttributeString(@NotNull final String attributeName, final boolean queryArchetype) {
        final String result = getAttributeValue(attributeName);
        if (result != null) {
            return result;
        }
        return queryArchetype ? archetype.getAttributeString(attributeName) : "";
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    protected String getEffectiveFaceName(@NotNull final String faceName) {
        final String normalizedFaceName = faceName.length() > 0 ? faceName.intern() : null;
        //Strings are interned
        //noinspection StringEquality
        return normalizedFaceName != null && normalizedFaceName.length() > 0 && normalizedFaceName != archetype.getFaceName() ? normalizedFaceName : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyBeginChange() {
        final MapSquare<G, A, R> mapSquare = getMapSquare();
        if (mapSquare != null) {
            mapSquare.getMapModel().beginGameObjectChange(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyEndChange() {
        final MapSquare<G, A, R> mapSquare = getMapSquare();
        if (mapSquare != null) {
            mapSquare.getMapModel().endGameObjectChange(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyTransientChange() {
        final MapSquare<G, A, R> mapSquare = getMapSquare();
        if (mapSquare != null) {
            mapSquare.getMapModel().transientGameObjectChange(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public G clone() {
        //noinspection OverriddenMethodCallDuringObjectConstruction
        final AbstractGameObject<G, A, R> clone = (AbstractGameObject<G, A, R>) super.clone();
        clone.container = null;
        return clone.getThis();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G asGameObject() {
        return getThis();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G newInstance(@NotNull final GameObjectFactory<G, A, R> gameObjectFactory) {
        return gameObjectFactory.cloneGameObject(getThis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final BaseObjectVisitor<G, A, R> baseObjectVisitor) {
        baseObjectVisitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        final G head = getHead();
        for (G tail = head.getMultiNext(); tail != null; tail = tail.getMultiNext()) {
            final GameObjectContainer<G, A, R> tailContainer = tail.getContainer();
            if (tailContainer == null) {
                throw new NotInsideContainerException(getThis());
            }
            tailContainer.remove(tail);
        }
        final GameObjectContainer<G, A, R> headContainer = head.getContainer();
        if (headContainer == null) {
            throw new NotInsideContainerException(getThis());
        }
        headContainer.remove(head);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public GameObjectContainer<G, A, R> getContainer() {
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTop() {
        //noinspection ConstantConditions
        return container == null || container.isTop(getThis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBottom() {
        //noinspection ConstantConditions
        return container == null || container.isBottom(getThis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveTop() {
        if (container != null) {
            //noinspection ConstantConditions
            container.moveTop(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveUp() {
        if (container != null) {
            //noinspection ConstantConditions
            container.moveUp(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveDown() {
        if (container != null) {
            //noinspection ConstantConditions
            container.moveDown(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveBottom() {
        if (container != null) {
            //noinspection ConstantConditions
            container.moveBottom(getThis());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertBefore(@NotNull final G node) {
        if (container == null) {
            throw new NotInsideContainerException(getThis());
        }
        //noinspection ConstantConditions
        container.insertBefore(node, getThis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertAfter(@NotNull final G node) {
        if (container == null) {
            throw new NotInsideContainerException(getThis());
        }
        //noinspection ConstantConditions
        container.insertAfter(getThis(), node);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G getContainerGameObject() {
        return container == null ? null : container.asGameObject();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G getTopContainer() {
        if (container != null) {
            final G gameObject = container.asGameObject();
            if (gameObject != null) {
                return gameObject.getTopContainer();
            }
        }

        return getThis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContainer(@Nullable final GameObjectContainer<G, A, R> container, final int mapX, final int mapY) {
        this.container = container;
        setMapX(mapX);
        setMapY(mapY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInContainer() {
        return container != null && container instanceof GameObject;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public MapSquare<G, A, R> getMapSquare() {
        return container != null ? container.getMapSquare() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G getPrev() {
        //noinspection ConstantConditions
        return container == null ? null : container.getPrev(getThis());
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G getNext() {
        //noinspection ConstantConditions
        return container == null ? null : container.getNext(getThis());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public R getArchetype() {
        return archetype;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArchetype(@NotNull final R archetype) {
        if (this.archetype == archetype) {
            return;
        }

        beginGameObjectChange();
        try {
            this.archetype = archetype;
            updateArchetype();
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasUndefinedArchetype() {
        return archetype.isUndefinedArchetype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markModified() {
        beginGameObjectChange();
        endGameObjectChange();
    }

}
