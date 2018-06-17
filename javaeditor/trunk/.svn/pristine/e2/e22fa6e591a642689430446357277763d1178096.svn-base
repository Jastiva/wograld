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

package net.sf.gridarta.var.daimonin.model.io;

import java.io.BufferedReader;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.var.daimonin.model.archetype.Archetype;
import net.sf.gridarta.var.daimonin.model.archetype.DefaultArchetypeBuilder;
import net.sf.gridarta.var.daimonin.model.gameobject.GameObject;
import net.sf.gridarta.var.daimonin.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The <code>ArchetypeParser</code> class handles the parsing of arches. It is a
 * class separated from ArchetypeSet because it is also involved in loading
 * arches in map files.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ArchetypeParser extends AbstractArchetypeParser<GameObject, MapArchObject, Archetype, DefaultArchetypeBuilder> {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link MultiPositionData} instance to query for multi-part objects.
     */
    @NotNull
    private final MultiPositionData multiPositionData;

    /**
     * The multi-shape ID of the currently parsed archetype.
     */
    private int multiShapeID;

    /**
     * Creates an ArchetypeParser.
     * @param animationObjects the animation objects instance to use
     * @param archetypeSet the archetype set
     * @param gameObjectFactory the factory for creating game objects
     * @param multiPositionData the multi position data to query for multi-part
     * objects
     */
    public ArchetypeParser(@NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final MultiPositionData multiPositionData) {
        super(new DefaultArchetypeBuilder(gameObjectFactory), animationObjects, archetypeSet);
        this.multiPositionData = multiPositionData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParseArchetype() {
        multiShapeID = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStartLine(@NotNull final String line) {
        return line.startsWith("Object ") || line.equals("Object");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean processLine(@NotNull final BufferedReader in, @NotNull final String line, @NotNull final String line2, @NotNull final DefaultArchetypeBuilder archetypeBuilder, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final List<GameObject> invObjects) {
        if (line.startsWith("mpart_id ")) {
            // shape ID for multi-parts
            try {
                multiShapeID = Integer.parseInt(line.substring(9).trim());

                if (multiShapeID <= 0 || multiShapeID >= MultiPositionData.Y_DIM) {
                    errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Arch " + archetypeBuilder.getArchetypeName() + " mpart_id number is '" + line.substring(9) + '\'');
                }
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Arch " + archetypeBuilder.getArchetypeName() + " has a invalid mpart_id (" + line.substring(9) + ')');
                archetypeBuilder.addObjectText(line);
            }
            return true;
        }

        if (line.startsWith("mpart_nr ")) {
            // part nr for multi-parts
            try {
                final int i = Integer.parseInt(line.substring(9).trim());
                archetypeBuilder.setMultiPartNr(i);
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Arch " + archetypeBuilder.getArchetypeName() + " has a invalid mpart_nr (" + line.substring(9) + ')');
                archetypeBuilder.addObjectText(line);
            }
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetypePart(@Nullable final Archetype firstArch, @NotNull final Archetype archetype, @NotNull final ErrorViewCollector errorViewCollector) {
        // set or check mpart_id
        archetype.setMultiShapeID(multiShapeID);
        if (firstArch != null && multiShapeID != firstArch.getMultiShapeID()) {
            errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("logDefArchWithInvalidMpartId", archetype.getArchetypeName(), firstArch.getArchetypeName(), Integer.toString(multiShapeID), Integer.toString(firstArch.getMultiShapeID())));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetype(@NotNull final Archetype archetype) {
        if (archetype.isMulti()) {
            calculateLowestMulti(archetype);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean addToPanel(final boolean isInternPath, @NotNull final String editorFolder, @NotNull final Archetype archetype) {
        // isInternPath is bugged (always false) so lets ignore that and just match editorFolder.
        // This was also bugged (no leading "/", we add the trailing one to mark the end [in case of an internfoo folder, etc).)
        //return !isInternPath && !(editorFolder + "/").startsWith("/" + net.sf.gridarta.model.gameobject.GameObject.EDITOR_FOLDER_INTERN + "/");
        return !(editorFolder + "/").startsWith(net.sf.gridarta.model.gameobject.GameObject.EDITOR_FOLDER_INTERN + "/");
    }

    /**
     * Calculate the lowest part of this multi-arch. This lowest part is needed
     * because in ISO view, the big image is drawn for it's lowest part, in
     * order to get the overlapping correct. <p/>
     * @param arch last tail part of this multi
     */
    private void calculateLowestMulti(final net.sf.gridarta.model.archetype.Archetype<GameObject, MapArchObject, Archetype> arch) {
        final Archetype head = arch.getHead();
        int minYOffset = multiPositionData.getYOffset(head.getMultiShapeID(), head.getMultiPartNr());

        // 1.step: find the maximal y-offset
        for (Archetype tail = head.getMultiNext(); tail != null; tail = tail.getMultiNext()) {
            final int t = multiPositionData.getYOffset(tail.getMultiShapeID(), tail.getMultiPartNr());
            if (t < minYOffset) {
                minYOffset = t;
            }
        }

        // 2.step: set 'lowestPart' flag for all squares with maximum offset
        for (Archetype part = head; part != null; part = part.getMultiNext()) {
            part.setLowestPart(multiPositionData.getYOffset(part.getMultiShapeID(), part.getMultiPartNr()) <= minYOffset);
        }
    }

}
