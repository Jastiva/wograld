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

package net.sf.gridarta.model.io;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for classes implementing {@link GameObjectParser
 * GameObjectParsers}. This class contains the common code for reading game
 * objects. Subclasses can extend the parser ({@link #parseLine(String,
 * GameObject)}. No support for writing is present.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractGameObjectParser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements GameObjectParser<G, A, R> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(AbstractGameObjectParser.class);

    /**
     * The game object factory for creating new game object instances.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link ArchetypeSet} for looking up archetypes.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * Create a new instance.
     * @param gameObjectFactory the game object factory for creating new game
     * object instances
     * @param archetypeSet the archetype set for looking up archetypes
     */
    protected AbstractGameObjectParser(@NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final ArchetypeSet<G, A, R> archetypeSet) {
        this.gameObjectFactory = gameObjectFactory;
        this.archetypeSet = archetypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G load(@NotNull final BufferedReader reader, @Nullable final Collection<G> objects) throws IOException {
        final String line = reader.readLine();
        if (line == null) {
            return null;
        }

        return load(reader, line, objects);
    }

    /**
     * {@inheritDoc}
     * @todo Attributes not relevant during map parsing shouldn't be evaluated
     * here, but in GameObject / ArchetypeParser instead.
     */
    @Nullable
    @Override
    public G load(@NotNull final BufferedReader reader, @NotNull final String firstLine, @Nullable final Collection<G> objects) throws IOException {
        final String archName = readArchName(reader, firstLine);
        if (archName == null) {
            return null;
        }

        final R archetype = archetypeSet.getOrCreateArchetype(archName);
        final G gameObject = gameObjectFactory.createGameObject(archetype);

        while (true) {
            final String thisLine2 = reader.readLine();
            if (thisLine2 == null) {
                throw new IOException("unexpected end of file while reading 'arch " + archName + "'");
            }
            final String thisLine = thisLine2.trim();

            if (thisLine.startsWith("arch ")) {
                final G invItem = load(reader, thisLine, objects);
                assert invItem != null : "invItem must not be null here.";
                gameObject.addLast(invItem);
            } else if (thisLine.equals("end")) {
                if (objects != null) {
                    objects.add(gameObject);
                }
                return gameObject;
            } else if (thisLine.equals("msg")) {
                gameObject.setMsgText("");
                while (true) {
                    final String thisLine3 = reader.readLine();
                    if (thisLine3 == null) {
                        throw new IOException("unexpected end of file while reading 'arch " + archName + "'");
                    }

                    if (thisLine3.trim().equals("endmsg")) {
                        break;
                    }

                    gameObject.addMsgTextLine(thisLine3);
                }
            } else if (parseLine(thisLine, gameObject)) {
                // ignore
            } else if (thisLine.startsWith("x ")) {
                gameObject.setMapX(Integer.parseInt(thisLine.substring(2)));
            } else if (thisLine.startsWith("y ")) {
                gameObject.setMapY(Integer.parseInt(thisLine.substring(2)));
            } else {
                gameObject.addObjectText(thisLine);
            }
        }
    }

    /**
     * Parse one line. This function performs actions needed for this editor.
     * @param line the line to process
     * @param gameObject the current game object
     * @return whether the line has been consumed; if <code>false</code> is
     *         returned, the default parsing is performed
     * @noinspection UnusedDeclaration
     */
    protected boolean parseLine(@NotNull final String line, @NotNull final G gameObject) {
        return false;
    }

    /**
     * Read an archetype name.
     * @param reader the reader to read from
     * @param firstLine the first line to parse before reading from
     * <code>reader</code>
     * @return the archetype name, or <code>null</code> if the end of the file
     *         was reached
     * @throws IOException if an error occurs
     */
    @Nullable
    private static String readArchName(@NotNull final BufferedReader reader, @NotNull final String firstLine) throws IOException {
        for (String thisLine2 = firstLine; thisLine2 != null; thisLine2 = reader.readLine()) {
            final String thisLine = thisLine2.trim();
            if (thisLine.startsWith("arch ")) {
                return thisLine.substring(5).trim();
            } else if (thisLine.length() > 0 && !thisLine.startsWith("#")) {
                throw new IOException("unexpected first line of game object: '" + thisLine + "', expected 'arch ...'");
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collectTempList(@NotNull final MapViewSettings mapViewSettings, final List<G> objects) {
        final Collection<G> tailList = new ArrayList<G>();

        for (final G gameObject : objects) {
            if (!gameObject.isInContainer()) {
                expandMulti(gameObject, tailList);
            }
        }

        for (final G tail : tailList) {
            objects.add(tail);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(@NotNull final Appendable appendable, @NotNull final G gameObject) throws IOException {
        final Map<String, String> fields = getModifiedFields(gameObject);

        appendable.append("arch ");
        appendable.append(gameObject.getArchetype().getArchetypeName());
        appendable.append("\n");
        for (final Entry<String, String> entry : fields.entrySet()) {
            appendable.append(entry.getKey());
            appendable.append(entry.getValue());
            appendable.append("\n");
        }

        for (final G inventoryItem : gameObject) {
            save(appendable, inventoryItem);
        }

        appendable.append("end\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addModifiedFields(@NotNull final G gameObject, @NotNull final Map<String, String> fields) {
        final BaseObject<G, A, R, ?> archetype = gameObject.getArchetype();

        final String msgText = gameObject.getMsgText();
        final String archMsgText = archetype.getMsgText();
        if (msgText != null && !msgText.equals(archMsgText == null ? "" : archMsgText)) {
            fields.put("msg\n", msgText + "endmsg");
        }

        final CharSequence objText = gameObject.getObjectText();
        if (objText.length() != 0) {
            for (final String aTmp : StringUtils.PATTERN_END_OF_LINE.split(objText, 0)) {
                final String[] line = StringUtils.PATTERN_SPACES.split(aTmp, 2);
                if (line.length == 2) {
                    fields.put(line[0] + " ", line[1]);
                } else {
                    log.warn("writeMapArch: ignoring invalid arch line: " + aTmp);
                }
            }
        }

        // map coordinates only belong into map arches (not inventory arches)
        if (gameObject.getMapX() != 0) {
            fields.put("x ", Integer.toString(gameObject.getMapX()));
        }
        if (gameObject.getMapY() != 0) {
            fields.put("y ", Integer.toString(gameObject.getMapY()));
        }
    }

    /**
     * If the given gameObject is a multi-part head, we generate the appropriate
     * tail (from the archetype stack) and attach it. The new gameObjects get
     * added to the temp list, not a map. This method should only be called
     * after map-loading. The ArchetypeSet should be fully initialized at this
     * point.
     * @param gameObject multi-part head that needs tail attached
     * @param objects list with objects that should get the tails attached
     * @todo think whether expanding multi-parts here is a good idea, maybe
     * MapModel is a better place.
     */
    private void expandMulti(@NotNull final G gameObject, @NotNull final Collection<G> objects) {
        final Archetype<G, A, R> archetype = gameObject.getArchetype();

        if (!archetype.isMulti() || gameObject.getMultiRefCount() > 1) {
            return;
        }

        if (gameObject.isInContainer()) {
            log.warn("Multi-part expansion for a GameObject inside a container requested.");
            return;
        }

        // do insertion for all non-head parts of the multi
        final Point pos = gameObject.getMapLocation();
        for (R oldPart = archetype.getMultiNext(); oldPart != null; oldPart = oldPart.getMultiNext()) {
            final G tailGameObject = gameObjectFactory.createGameObjectPart(oldPart, gameObject);
            tailGameObject.setMapX(pos.x + oldPart.getMultiX());
            tailGameObject.setMapY(pos.y + oldPart.getMultiY());
            objects.add(tailGameObject);
        }
    }

}
