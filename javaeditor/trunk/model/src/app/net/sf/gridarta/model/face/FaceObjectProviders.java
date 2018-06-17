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

package net.sf.gridarta.model.face;

import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.baseobject.BaseObjectVisitor;
import net.sf.gridarta.model.data.NamedObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.AlphaImageFilterInstance;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provider for faces of {@link GameObject GameObjects} and {@link Archetype
 * Archetypes}. The face can be the "normal" (default) face or a filtered
 * variant such as (semi-)transparent and/or double height.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @author serpentshard
 */
public class FaceObjectProviders {

    /**
     * The gray level for gray-scaled images; 100=darkest gray, 0=lightest
     * gray.
     */
    private static final int GRAY_PERCENTAGE = 50;

    /**
     * The filter to make gray-scaled images.
     */
    @NotNull
    private static final ImageFilter GRAY_FILTER = new GrayFilter(false, GRAY_PERCENTAGE);

    /**
     * The face provider for GRAY faces.
     */
    @NotNull
    private static final FilterFaceProvider GRAY = new FilterFaceProvider(GRAY_FILTER);

    /**
     * The filter to make images red.
     */
    @NotNull
    private static final ImageFilter RED_FILTER = new ColourFilter(ColourFilter.RED_MASK);

    /**
     * The face provider for red faces.
     */
    @NotNull
    private static final FilterFaceProvider RED = new FilterFaceProvider(RED_FILTER);

    /**
     * The filter to make images green.
     */
    @NotNull
    private static final ImageFilter GREEN_FILTER = new ColourFilter(ColourFilter.GREEN_MASK);

    /**
     * The face provider for green faces.
     */
    @NotNull
    private static final FilterFaceProvider GREEN = new FilterFaceProvider(GREEN_FILTER);

    /**
     * The filter to make images blue.
     */
    @NotNull
    private static final ImageFilter BLUE_FILTER = new ColourFilter(ColourFilter.BLUE_MASK);

    /**
     * The face provider for blue faces.
     */
    @NotNull
    private static final FilterFaceProvider BLUE = new FilterFaceProvider(BLUE_FILTER);

    /**
     * The filter using a full alpha grid instead of alpha blending.
     */
    @NotNull
    private static final ImageFilter GRID_FILTER = new RGBImageFilter() {

        /**
         * Converts every second pixel by making it transparent.
         */
        @Override
        public int filterRGB(final int x, final int y, final int rgb) {
            return (x + y) % 2 == 0 ? rgb & ColourFilter.RED_GREEN_BLUE_MASK : rgb;
        }

        @NotNull
        @Override
        public Object clone() {
            return super.clone();
        }

    }; // GRID_FILTER

    /**
     * The face provider for grid faces.
     */
    @NotNull
    private static final FilterFaceProvider GRID = new FilterFaceProvider(GRID_FILTER);

    /**
     * The face provider for normal faces.
     */
    @Nullable
    private FaceProvider normalFaceProvider;

    /**
     * The face provider for alpha faces.
     */
    @NotNull
    private static final FilterFaceProvider ALPHA = new FilterFaceProvider(AlphaImageFilterInstance.ALPHA_FILTER);

    /**
     * The face provider for double faces.
     */
    @NotNull
    private final FilterFaceProvider doubleFaceProvider;

    /**
     * The face provider for transparent double faces.
     */
    @NotNull
    private final FilterFaceProvider doubleAlphaFaceProvider;

    /**
     * The {@link FaceObjects} instance for looking up face names.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * The {@link FaceObjectProvidersListener FaceObjectProvidersListeners} to
     * notify about changes.
     */
    @NotNull
    private final EventListenerList2<FaceObjectProvidersListener> faceObjectProvidersListeners = new EventListenerList2<FaceObjectProvidersListener>(FaceObjectProvidersListener.class);

    /**
     * Creates a new instance.
     * @param doubleFaceOffset the offset for shifting double faces
     * @param faceObjects the face objects instance
     * @param systemIcons the system icons for creating icons
     */
    public FaceObjectProviders(final int doubleFaceOffset, @NotNull final FaceObjects faceObjects, @NotNull final SystemIcons systemIcons) {
        this.faceObjects = faceObjects;
        this.systemIcons = systemIcons;
        doubleFaceProvider = new FilterFaceProvider(new DoubleImageFilter(doubleFaceOffset));
        doubleAlphaFaceProvider = new FilterFaceProvider(new DoubleImageFilter(doubleFaceOffset));
    }

    /**
     * Adds a {@link FaceObjectProvidersListener} to be notified about changes.
     * @param listener the listener
     */
    public void addFaceObjectProvidersListener(@NotNull final FaceObjectProvidersListener listener) {
        faceObjectProvidersListeners.add(listener);
    }

    /**
     * Removes a {@link FaceObjectProvidersListener} to be notified about
     * changes.
     * @param listener the listener
     */
    public void removeFaceObjectProvidersListener(@NotNull final FaceObjectProvidersListener listener) {
        faceObjectProvidersListeners.remove(listener);
    }

    /**
     * Reloads all providers provided by this FaceObjects.
     */
    public void reloadAll() {
        if (normalFaceProvider != null) {
            normalFaceProvider.reload();
        }
        for (final FaceProvider faceProvider : new FaceProvider[] { GRAY, RED, GREEN, BLUE, ALPHA, GRID, doubleFaceProvider, doubleAlphaFaceProvider }) {
            faceProvider.reload();
        }

        for (final FaceObjectProvidersListener faceObjectProvidersListener : faceObjectProvidersListeners.getListeners()) {
            faceObjectProvidersListener.facesReloaded();
        }
    }

    /**
     * Sets the normal {@link FaceProvider}. The normal face provider also
     * serves as parent for all other face providers.
     * @param normalFaceProvider face the provider that provides normal images
     */
    public void setNormal(@NotNull final FaceProvider normalFaceProvider) {
        this.normalFaceProvider = normalFaceProvider;
        GRAY.setParent(normalFaceProvider);
        RED.setParent(normalFaceProvider);
        GREEN.setParent(normalFaceProvider);
        BLUE.setParent(normalFaceProvider);
        ALPHA.setParent(normalFaceProvider);
        GRID.setParent(normalFaceProvider);
        doubleFaceProvider.setParent(normalFaceProvider);
        doubleAlphaFaceProvider.setParent(normalFaceProvider);
    }

    /**
     * Returns the face of a {@link BaseObject} as an {@link ImageIcon}.
     * @param baseObject the base object to get the face for
     * @return the face of the base object as an icon or <code>null</code> if no
     *         face could be found
     */
    @NotNull
    public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> ImageIcon getFace(@NotNull final BaseObject<G, A, R, ?> baseObject) {
        if (normalFaceProvider == null) {
            throw new IllegalStateException();
        }

        final boolean[] undefinedArchetype = new boolean[1];
        baseObject.visit(new BaseObjectVisitor<G, A, R>() {

            @Override
            public void visit(@NotNull final Archetype<G, A, R> archetype) {
                undefinedArchetype[0] = archetype.isUndefinedArchetype();
            }

            @Override
            public void visit(@NotNull final GameObject<G, A, R> gameObject) {
                undefinedArchetype[0] = gameObject.hasUndefinedArchetype();
            }

        });
        //noinspection ConstantConditions
        return getFace(baseObject.getFaceObjName(), undefinedArchetype[0], normalFaceProvider, normalFaceProvider);
    }
    
    @NotNull
    public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> ImageIcon getSecondFace(@NotNull final BaseObject<G, A, R, ?> baseObject) {
        if (normalFaceProvider == null) {
            throw new IllegalStateException();
        }

        final boolean[] undefinedArchetype = new boolean[1];
        baseObject.visit(new BaseObjectVisitor<G, A, R>() {

            @Override
            public void visit(@NotNull final Archetype<G, A, R> archetype) {
                undefinedArchetype[0] = archetype.isUndefinedArchetype();
            }

            @Override
            public void visit(@NotNull final GameObject<G, A, R> gameObject) {
                undefinedArchetype[0] = gameObject.hasUndefinedArchetype();
            }

        });
        //noinspection ConstantConditions
        return getSecondFace(baseObject.getFaceObjName(), undefinedArchetype[0], normalFaceProvider);
    }

    /**
     * Returns the transparent face for a {@link GameObject} as an {@link
     * ImageIcon}.
     * @param gameObject the game object
     * @return the image icon
     */
    @NotNull
    public ImageIcon getTrans(@NotNull final GameObject<?, ?, ?> gameObject) {
        return getFace(gameObject.getFaceObjName(), gameObject.hasUndefinedArchetype(), ALPHA, ALPHA);
    }

    /**
     * Returns the double face for a {@link GameObject} as an {@link
     * ImageIcon}.
     * @param gameObject the game object
     * @return the image icon
     */
    @NotNull
    public ImageIcon getDouble(@NotNull final GameObject<?, ?, ?> gameObject) {
        if (normalFaceProvider == null) {
            throw new IllegalStateException();
        }
        //noinspection ConstantConditions
        return getFace(gameObject.getFaceObjName(), gameObject.hasUndefinedArchetype(), normalFaceProvider, doubleFaceProvider);
    }

    /**
     * Returns the transparent double face for a {@link GameObject} as an {@link
     * ImageIcon}.
     * @param gameObject the game object
     * @return the image icon
     */
    @NotNull
    public ImageIcon getTransDouble(@NotNull final GameObject<?, ?, ?> gameObject) {
        return getFace(gameObject.getFaceObjName(), gameObject.hasUndefinedArchetype(), ALPHA, doubleAlphaFaceProvider);
    }

    /**
     * Returns the {@link ImageIcon} of a face with a certain face name.
     * @param faceName the face name of face
     * @param hasUndefinedArchetype if set, return the face for an game object
     * referencing an undefined archetype
     * @param singleFaceProvider the face provider to use for single faces
     * @param doubleFaceProvider the face provider to use for double faces
     * @return face for face <var>faceName</var>
     */
    @NotNull
    private ImageIcon getFace(@Nullable final String faceName, final boolean hasUndefinedArchetype, @NotNull final FaceProvider singleFaceProvider, @NotNull final FaceProvider doubleFaceProvider) {
        if (hasUndefinedArchetype) {
            return systemIcons.getNoArchSquareIcon();
        }

        if (faceName == null) {
            return systemIcons.getNoFaceSquareIcon();
        }

        final FaceObject faceObject = faceObjects.get(faceName);
        if (faceObject == null) {
            return systemIcons.getNoFaceSquareIcon();
        }

        final FaceProvider faceProvider = faceObject.isDouble() ? doubleFaceProvider : singleFaceProvider;
        final String alternativeFaceName = faceObject.getAlternativeFaceName();
        final String effectiveFaceName = alternativeFaceName != null && singleFaceProvider == doubleFaceProvider ? alternativeFaceName : faceName;
        final ImageIcon face = faceProvider.getImageIconForFacename(effectiveFaceName);
        if (face == null) {
            return systemIcons.getUnknownSquareIcon();
        }

        return face;
    }
    
    @NotNull
    private ImageIcon getSecondFace(@Nullable final String faceName, final boolean hasUndefinedArchetype, @NotNull final FaceProvider singleFaceProvider) {
        if (hasUndefinedArchetype) {
            return systemIcons.getNoArchSquareIcon();
        }

        if (faceName == null) {
            return systemIcons.getNoFaceSquareIcon();
        }

        final FaceObject faceObject = faceObjects.get(faceName);
        if (faceObject == null) {
            return systemIcons.getNoFaceSquareIcon();
        }

       
        final String alternativeFaceName = faceObject.getAlternativeFaceName();
        final String effectiveFaceName = alternativeFaceName != null ? alternativeFaceName : faceName;
        final ImageIcon face = singleFaceProvider.getSecondImageForFacename(effectiveFaceName);
        if (face == null) {
            return systemIcons.getUnknownSquareIcon();
        }

        return face;
    }


    /**
     * Returns the {@link ImageIcon} for a given face object name.
     * @param faceObjName the face object name
     * @return the image icon or <code>null</code> if not found
     */
    @Nullable
    public ImageIcon getImageIconForFacename(@NotNull final String faceObjName) {
        return normalFaceProvider == null ? null : normalFaceProvider.getImageIconForFacename(faceObjName);
    }
    
    @Nullable
    public ImageIcon getSecondImageForFacename(@NotNull final String faceObjName) {
        return normalFaceProvider == null ? null : normalFaceProvider.getSecondImageForFacename(faceObjName);
    }

    /**
     * Returns the display icon for a {@link NamedObject}.
     * @param namedObject the named object
     * @return display icon of this AbstractNamedObject
     */
    @Nullable
    public ImageIcon getDisplayIcon(@NotNull final NamedObject namedObject) {
        return getImageIconForFacename(namedObject.getDisplayIconName());
    }
    
     @Nullable
    public ImageIcon getSecondDisplayIcon(@NotNull final NamedObject namedObject) {
        return getSecondImageForFacename(namedObject.getDisplayIconName());
    }
     
     // this may not be necessary, but why mess with constructor of archetypes
     public FaceObject getFaceObFromName(@NotNull final String faceObjName){
         return faceObjects.get(faceObjName);
     }

}
