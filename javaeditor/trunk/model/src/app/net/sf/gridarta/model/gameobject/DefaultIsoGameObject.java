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

import javax.swing.ImageIcon;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default implementation for {@link GameObject} implementing classes.
 * @author Andreas Kirschbaum
 */
public abstract class DefaultIsoGameObject<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObject<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the "z" attribute.
     */
    @NotNull
    public static final String Z = "z";

    /**
     * The name of the "layer" attribute.
     */
    @NotNull
    public static final String LAYER = "layer";

    /**
     * The name of the "sys_object" attribute.
     */
    @NotNull
    public static final String SYS_OBJECT = "sys_object";

    /**
     * The name of the "draw_double_always" attribute.
     */
    @NotNull
    public static final String DRAW_DOUBLE_ALWAYS = "draw_double_always";

    /**
     * The name of the "draw_double" attribute.
     */
    @NotNull
    public static final String DRAW_DOUBLE = "draw_double";

    /**
     * The name of the "align" attribute.
     */
    @NotNull
    public static final String ALIGN = "align";

    /**
     * The name of the "zoom" attribute.
     */
    @NotNull
    public static final String ZOOM = "zoom";

    /**
     * The name of the "alpha" attribute.
     */
    @NotNull
    public static final String ALPHA = "alpha";

    /**
     * The name of the "rotate" attribute.
     */
    @NotNull
    public static final String ROTATE = "rotate";

    /**
     * The name of the "glow_radius" attribute.
     */
    @NotNull
    public static final String GLOW_RADIUS = "glow_radius";

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final transient FaceObjectProviders faceObjectProviders;

    /**
     * The transparent face.
     * @serial
     */
    @Nullable
    private ImageIcon transFace;

    /**
     * The double face.
     * @serial
     */
    @Nullable
    private ImageIcon doubleFace;

    /**
     * The transparent double face.
     * @serial
     */
    @Nullable
    private ImageIcon transDoubleFace;

    /**
     * Creates a new instance.
     * @param archetype the base archetype
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    protected DefaultIsoGameObject(@NotNull final R archetype, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        super(archetype, faceObjectProviders, animationObjects);
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean usesDirection() {
        return getAttributeInt(IS_TURNABLE) != 0 || getAttributeInt(IS_ANIMATED) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectFace() {
        transFace = null;
        doubleFace = null;
        transDoubleFace = null;
        super.setObjectFace();
    }

    /**
     * Returns a transparent variant of the face for this GameObject.
     * @return the transparent face for this game object
     */
    @NotNull
    protected ImageIcon getTransparentImage() {
        if (transFace == null) {
            transFace = faceObjectProviders.getTrans(this);
        }
        return transFace;
    }

    /**
     * Returns a double variant of the face for this GameObject.
     * @return the double face for this game object
     */
    @NotNull
    protected ImageIcon getDoubleImage() {
        if (doubleFace == null) {
            doubleFace = faceObjectProviders.getDouble(this);
        }
        return doubleFace;
    }

    /**
     * Returns a transparent variant of the face for this GameObject.
     * @return the transparent face for this game object
     */
    @NotNull
    protected ImageIcon getTransparentDoubleImage() {
        if (transDoubleFace == null) {
            transDoubleFace = faceObjectProviders.getTransDouble(this);
        }
        return transDoubleFace;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public G clone() {
        //noinspection OverriddenMethodCallDuringObjectConstruction
        final DefaultIsoGameObject<G, A, R> clone = (DefaultIsoGameObject<G, A, R>) super.clone();
        return clone.getThis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqual(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        return super.isEqual(gameObject);
        // ignore "scriptArchData"
        // ignore "transFace"
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void facesReloaded() {
        transFace = null;
        doubleFace = null;
        transDoubleFace = null;
        super.facesReloaded();
    }

}
