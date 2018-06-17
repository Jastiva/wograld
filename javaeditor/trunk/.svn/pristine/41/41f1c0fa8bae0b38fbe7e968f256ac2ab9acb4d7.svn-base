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

package net.sf.gridarta.var.crossfire.model.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.smoothface.DuplicateSmoothFaceException;
import net.sf.gridarta.model.smoothface.SmoothFace;
import net.sf.gridarta.model.smoothface.SmoothFaces;
import net.sf.gridarta.utils.StringUtils;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.archetype.DefaultArchetypeBuilder;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
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
     * The tag starting the map lore.
     */
    @NotNull
    private static final String LORE = "lore";

    /**
     * The tag ending the map lore.
     */
    @NotNull
    private static final String ENDLORE = "endlore";

    /**
     * The tag for smoothing information.
     */
    @NotNull
    private static final String SMOOTHFACE = "smoothface";

    /**
     * The tag for smoothing information, including a trailing space.
     */
    @NotNull
    private static final String SMOOTHFACE_SPACE = SMOOTHFACE + " ";

    /**
     * The game object parser instance.
     */
    private final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser;

    /**
     * The {@link SmoothFaces} instance to update.
     */
    @NotNull
    private final SmoothFaces smoothFaces;

    /**
     * Creates an ArchetypeParser.
     * @param gameObjectParser the game object parser instance to use
     * @param animationObjects the animation objects instance to use
     * @param archetypeSet the archetype set
     * @param gameObjectFactory the factory for creating game objects
     * @param smoothFaces the smooth faces instance to update
     */
    public ArchetypeParser(@NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final SmoothFaces smoothFaces) {
        super(new DefaultArchetypeBuilder(gameObjectFactory), animationObjects, archetypeSet);
        this.gameObjectParser = gameObjectParser;
        this.smoothFaces = smoothFaces;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParseArchetype() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStartLine(@NotNull final String line) {
        return line.startsWith("Object ");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean processLine(@NotNull final BufferedReader in, @NotNull final String line, @NotNull final String line2, @NotNull final DefaultArchetypeBuilder archetypeBuilder, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final List<GameObject> invObjects) throws IOException {
        if (line.startsWith("arch ")) {
            final GameObject invObject = gameObjectParser.load(in, line, invObjects);
            assert invObject != null;
            archetypeBuilder.addLast(invObject);
            return true;
        }

        if (line.equals(LORE)) {
            parseLore(in, archetypeBuilder, errorViewCollector);
            return true;
        }

        if (line.startsWith(SMOOTHFACE_SPACE)) {
            final String[] tmp = StringUtils.PATTERN_WHITESPACE.split(line.substring(SMOOTHFACE_SPACE.length()));
            if (tmp.length == 2) {
                try {
                    smoothFaces.add(new SmoothFace(tmp[0], tmp[1]));
                } catch (final DuplicateSmoothFaceException ex) {
                    errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, archetypeBuilder.getArchetypeName() + ": duplicate " + SMOOTHFACE + " '" + ex.getMessage() + "': " + line);
                }
            } else {
                errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, archetypeBuilder.getArchetypeName() + ": invalid " + SMOOTHFACE + " info: " + line);
            }
            return true;
        }

        return false;
    }

    /**
     * Parses a "lore..endlore" block.
     * @param in the reader to read from
     * @param archetypeBuilder the archetype builder to update
     * @param errorViewCollector the error view collector for reporting errors
     * @throws IOException if an I/O error occurs
     */
    private static void parseLore(@NotNull final BufferedReader in, @NotNull final DefaultArchetypeBuilder archetypeBuilder, @NotNull final ErrorViewCollector errorViewCollector) throws IOException {
        final StringBuilder loreText = new StringBuilder();

        while (true) {
            final String thisLine = in.readLine();
            if (thisLine == null) {
                errorViewCollector.addError(ErrorViewCategory.ARCHETYPE_INVALID, "Truncated archetype: " + LORE + " not terminated by " + ENDLORE);
                return;
            }

            if (thisLine.trim().equals(ENDLORE)) {
                break;
            }

            // keep leading whitespace
            loreText.append(thisLine).append("\n");
        }

        archetypeBuilder.setLoreText(loreText.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetypePart(@Nullable final Archetype firstArch, @NotNull final Archetype archetype, @NotNull final ErrorViewCollector errorViewCollector) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetype(@NotNull final Archetype archetype) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean addToPanel(final boolean isInternPath, @NotNull final String editorFolder, @NotNull final Archetype archetype) {
        return !archetype.getArchetypeName().equals(START_ARCH_NAME);
    }

}
