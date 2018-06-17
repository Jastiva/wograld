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

package net.sf.gridarta.model.anim;

import java.util.regex.Pattern;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.face.FaceObjects;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for validating {@link AnimationObject} instances.
 * @author Andreas Kirschbaum
 */
public class AnimationValidator {

    /**
     * Pattern for splitting animation lists.
     */
    private static final Pattern PATTERN_END_OF_LINE = Pattern.compile("\n");

    /**
     * The {@link FaceObjects} instance for looking up face names.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * The {@link ErrorView} for reporting errors.
     */
    @NotNull
    private final ErrorView errorView;

    /**
     * Creates a new instance.
     * @param faceObjects the face objects instance for looking up face names
     * @param errorView the error view for reporting errors
     */
    public AnimationValidator(@NotNull final FaceObjects faceObjects, final ErrorView errorView) {
        this.faceObjects = faceObjects;
        this.errorView = errorView;
    }

    /**
     * Validates a set of {@link AnimationObject AnimationObjects}.
     * @param animationObjects the animation objects instance to validate
     */
    public void validate(@NotNull final Iterable<AnimationObject> animationObjects) {
        for (final AnimationObject animationObject : animationObjects) {
            validateAnimation(animationObject);
        }
    }

    /**
     * Validates an {@link AnimationObject} instance.
     * @param animationObject the animation object instance to validate
     */
    private void validateAnimation(@NotNull final AnimationObject animationObject) {
        for (final String faceName : PATTERN_END_OF_LINE.split(animationObject.getAnimList())) {
            if (!faceName.startsWith("facings ")) {
                final FaceObject face = faceObjects.get(faceName);
                if (face == null) {
                    errorView.addWarning(ErrorViewCategory.ANIMATION_INVALID, animationObject.getAnimName() + " references undefined face '" + faceName + "'");
                }
            }
        }
    }

}
