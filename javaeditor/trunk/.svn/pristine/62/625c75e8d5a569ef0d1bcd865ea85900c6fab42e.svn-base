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

package net.sf.gridarta.var.daimonin.model.collectable;

import java.io.IOException;
import java.io.Writer;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.collectable.AbstractArchetypeSetCollectable;
import net.sf.gridarta.var.daimonin.IGUIConstants;
import net.sf.gridarta.var.daimonin.model.archetype.Archetype;
import net.sf.gridarta.var.daimonin.model.gameobject.GameObject;
import net.sf.gridarta.var.daimonin.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.collectable.Collectable} that creates the
 * Daimonin specific "archetypes" file.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class DaimoninArchetypeSetCollectable extends AbstractArchetypeSetCollectable<GameObject, MapArchObject, Archetype> {

    /**
     * Creates a new instance.
     * @param archetypeSet the archetype set to collect
     */
    public DaimoninArchetypeSetCollectable(@NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        super(archetypeSet, IGUIConstants.ARCH_FILE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int collectArchetype(@NotNull final Archetype archetype, @NotNull final Writer out) throws IOException {
        writeArchetype(out, archetype, true);

        // process the multi-part tails:
        int result = 1;
        for (Archetype tail = archetype.getMultiNext(); tail != null; tail = tail.getMultiNext()) {
            result++;

            out.append("More\n");
            writeArchetype(out, tail, false);
        }

        return result;
    }

    /**
     * Writes an {@link Archetype}.
     * @param writer the writer to write to
     * @param archetype the archetype to write
     * @param isHeadPart whether this part is the head part
     * @throws IOException if an I/O error occurs
     */
    private static void writeArchetype(@NotNull final Writer writer, @NotNull final net.sf.gridarta.model.archetype.Archetype<GameObject, MapArchObject, Archetype> archetype, final boolean isHeadPart) throws IOException {
        writer.append("Object ").append(archetype.getArchetypeName()).append('\n');

        if (archetype.getMultiShapeID() > 0) {
            writer.append("mpart_id ").append(Integer.toString(archetype.getMultiShapeID())).append('\n');
        }
        if (archetype.getMultiPartNr() > 0) {
            writer.append("mpart_nr ").append(Integer.toString(archetype.getMultiPartNr())).append('\n');
        }

        if (archetype.getMsgText() != null) {
            writer.append("msg\n").append(archetype.getMsgText()).append("endmsg\n");
        }

        if (isHeadPart) {
            // special: add a string-attribute with the display-category
            writer.append("editor_folder ").append(archetype.getEditorFolder()).append('\n');
        }

        writer.append(archetype.getObjectText());

        if (!isHeadPart) {
            // position of multi relative to head
            if (archetype.getMultiX() != 0) {
                writer.append("x ").append(Integer.toString(archetype.getMultiX())).append('\n');
            }
            if (archetype.getMultiY() != 0) {
                writer.append("y ").append(Integer.toString(archetype.getMultiY())).append('\n');
            }
        }

        writer.append("end\n");
    }

}
