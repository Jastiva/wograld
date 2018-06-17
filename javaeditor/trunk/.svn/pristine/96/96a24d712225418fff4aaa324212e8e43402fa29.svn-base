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

package net.sf.gridarta.model.errorview;

import org.jetbrains.annotations.NotNull;

/**
 * Defines possible error categories for {@link ErrorView} instances.
 * @author Andreas Kirschbaum
 */
public enum ErrorViewCategory {

    ANIM_ENTRY_INVALID("Invalid animation definition"),

    ANIM_FILE_INVALID("Animation definition file cannot be read"),

    ANIMATION_INVALID("Invalid animation"),

    ANIMATIONS_ENTRY_INVALID("Invalid animation definition"),

    ANIMATIONS_FILE_INVALID("Animation definitions file cannot be read"),

    ANIMTREE_FILE_INVALID("Animation tree definition file cannot be read"),

    ARCHDEF_ENTRY_INVALID("Invalid archdef entry"),

    ARCHDEF_FILE_INVALID("Archdef file cannot be read"),

    ARCHETYPE_DIR_INVALID("Archetype directory cannot be read"),

    ARCHETYPE_FILE_INVALID("Archetype file cannot be read"),

    ARCHETYPE_INVALID("Invalid archetype"),

    ARCHETYPES_FILE_INVALID("Collected archetypes file cannot be read"),

    ARTIFACT_ENTRY_INVALID("Invalid artifact definition"),

    ARTIFACT_FILE_INVALID("Artifact definition file cannot be read"),

    AUTOJOIN_ENTRY_INVALID("Invalid autojoin list entry"),

    AUTOJOIN_FILE_INVALID("Autojoin list definitions file cannot be read"),

    FACE_FILE_INVALID("Face definition file cannot be read"),

    FACES_ENTRY_INVALID("Invalid face definition"),

    FACES_FILE_INVALID("Faces file cannot be read"),

    GAMEOBJECTMATCHERS_ENTRY_INVALID("Invalid game object matcher definition"),

    GAMEOBJECTMATCHERS_FILE_INVALID("Game object matcher definitions file cannot be read"),

    MAP_VALIDATOR_ENTRY_INVALID("Invalid map validator"),

    PICKMAPS_DIR_INVALID("Invalid pickmaps directory"),

    PICKMAPS_FILE_INVALID("Pickmap file cannot be read"),

    SCRIPTS_DIR_INVALID("Script directory cannot be read"),

    SCRIPTS_FILE_INVALID("Script file cannot be read"),

    SMOOTH_FILE_INVALID("Smooth file cannot be read"),

    SPELLS_ENTRY_INVALID("Invalid spell definition"),

    SPELLS_FILE_INVALID("Spell definitions file cannot be read"),

    TREASURES_ENTRY_INVALID("Invalid treasure list definition"),

    TREASURES_FILE_INVALID("Treasure list definitions file cannot be read"),

    TYPES_ENTRY_INVALID("Undefined entry in type definitions file"),

    TYPES_FILE_INVALID("Type definitions file cannot be read");

    /**
     * The string representation.
     */
    @NotNull
    private final String name;

    /**
     * Creates a new instance.
     * @param name the string representation
     */
    ErrorViewCategory(@NotNull final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @NotNull
    @Override
    public String toString() {
        return name;

    }
}
