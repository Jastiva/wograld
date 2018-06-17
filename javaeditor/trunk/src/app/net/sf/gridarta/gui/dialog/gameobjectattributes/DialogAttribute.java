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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttribute;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A single Attribute, combining an {@link ArchetypeAttribute} with its input
 * component(s).
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public abstract class DialogAttribute<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, T extends ArchetypeAttribute> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DialogAttribute.class);

    /**
     * Reference to the attribute data.
     */
    @NotNull
    private final T ref;

    /**
     * Create a DialogAttribute.
     * @param ref reference to the attribute data
     */
    protected DialogAttribute(@NotNull final T ref) {
        this.ref = ref;
    }

    /**
     * Returns the attribute data.
     * @return the attribute data
     */
    @NotNull
    public T getRef() {
        return ref;
    }

    @Nullable
    public abstract String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent);

    public abstract void appendSummary(@NotNull final Document doc, @NotNull final Style style);

    protected static void addLine(@NotNull final Document doc, @NotNull final AttributeSet style, @NotNull final String line) {
        try {
            doc.insertString(doc.getLength(), line + "\n", style);
        } catch (final BadLocationException e) {
            log.error("toggleSummary: Bad Location in Document!", e);
        }
    }

}
