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

import net.sf.gridarta.model.anim.AnimationObject;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.face.FaceObjects;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for validating {@link ArchetypeSet} instances.
 * @author Andreas Kirschbaum
 */
public class ArchetypeValidator {

    /**
     * The {@link AnimationObjects} instance for looking up animation names.
     */
    @NotNull
    private final AnimationObjects animationObjects;

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
     * @param animationObjects the animation objects instance for looking up
     * animation names
     * @param faceObjects the face objects instance for looking up face names
     * @param errorView the error view for reporting errors
     */
    public ArchetypeValidator(@NotNull final AnimationObjects animationObjects, @NotNull final FaceObjects faceObjects, final ErrorView errorView) {
        this.animationObjects = animationObjects;
        this.faceObjects = faceObjects;
        this.errorView = errorView;
    }

    /**
     * Validates an {@link ArchetypeSet} instance.
     * @param archetypeSet the archetype set instance to validate
     */
    public void validate(@NotNull final ArchetypeSet<?, ?, ?> archetypeSet) {
        for (final Archetype<?, ?, ?> archetype : archetypeSet.getArchetypes()) {
            validateArchetype(archetype);
        }
    }

    /**
     * Validates an {@link Archetype} instance.
     * @param archetype the archetype instance to validate
     */
    private void validateArchetype(@NotNull final Archetype<?, ?, ?> archetype) {
        final String animName = archetype.getAnimName();
        if (animName != null && !animName.equals("NONE")) {
            final AnimationObject animation = animationObjects.get(animName);
            if (animation == null) {
                errorView.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, archetype.getArchetypeName() + " references undefined animation '" + animName + "'");
            }
        }

        final String faceName = archetype.getFaceName();
        if (faceName != null) {
            final FaceObject face = faceObjects.get(faceName);
            if (face == null) {
                errorView.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, archetype.getArchetypeName() + " references undefined face '" + faceName + "'");
            }
        }
    }

}
