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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.Component;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttribute;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Information about one attribute line in a {@link GameObjectAttributesDialog}.
 * @author Andreas Kirschbaum
 */
public class GuiInfo<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, T extends ArchetypeAttribute> {

    // now create the attribute-GUI-instance

    @NotNull
    private final DialogAttribute<G, A, R, T> newAttr;

    @Nullable
    private final Component cLabel;

    @Nullable
    private final Component cComp;

    @Nullable
    private final Component cRow;

    @Nullable
    private final Component cGlue;

    private final boolean isText;

    public GuiInfo(@NotNull final DialogAttribute<G, A, R, T> newAttr, @Nullable final Component cLabel, @Nullable final Component cComp, @Nullable final Component cRow, @Nullable final Component cGlue, final boolean isText) {
        this.newAttr = newAttr;
        this.cLabel = cLabel;
        this.cComp = cComp;
        this.cRow = cRow;
        this.cGlue = cGlue;
        this.isText = isText;
    }

    @NotNull
    public DialogAttribute<G, A, R, T> getNewAttr() {
        return newAttr;
    }

    @Nullable
    public Component getCLabel() {
        return cLabel;
    }

    @Nullable
    public Component getCComp() {
        return cComp;
    }

    @Nullable
    public Component getCRow() {
        return cRow;
    }

    @Nullable
    public Component getCGlue() {
        return cGlue;
    }

    public boolean isText() {
        return isText;
    }

}
