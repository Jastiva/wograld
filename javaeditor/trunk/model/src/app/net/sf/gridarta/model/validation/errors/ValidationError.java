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

package net.sf.gridarta.model.validation.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Super class of all errors that could occur during map validation. The base
 * methods of this class all return null and might return meaningful values if
 * overridden in a subclass.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public abstract class ValidationError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The maximum number of map squares to show in the title.
     */
    private static final int TITLE_MAP_SQUARES = 3;

    /**
     * The maximum number of game objects to show in the title.
     */
    private static final int TITLE_GAME_OBJECTS = 5;

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Key.
     * @serial
     */
    @NotNull
    private final String key;

    /**
     * The {@link MapModel} that caused the error.
     * @serial
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link MapSquare MapSquares} that caused the error.
     * @serial
     */
    @NotNull
    private final List<MapSquare<G, A, R>> mapSquares = new ArrayList<MapSquare<G, A, R>>(0);

    /**
     * The {@link GameObject GameObjects} that caused the error.
     * @serial
     */
    @NotNull
    private final List<G> gameObjects = new ArrayList<G>(0);

    /**
     * The {@link Formatter} for formatting {@link MapSquare} instances.
     * @serial
     */
    @NotNull
    private final MapSquareFormatter mapSquareFormatter = new MapSquareFormatter();

    /**
     * The {@link Formatter} for formatting {@link GameObject} instances.
     * @serial
     */
    @NotNull
    private final GameObjectFormatter gameObjectFormatter = new GameObjectFormatter();

    /**
     * Creates a new instance. The key is generated from the validator's name,
     * which must end on "Checker".
     * @param mapModel the map model that caused the error
     * @throws IllegalArgumentException in case the validator's name does not
     * end on "Checker"
     */
    protected ValidationError(@NotNull final MapModel<G, A, R> mapModel) throws IllegalArgumentException {
        final String name = getClass().getSimpleName();
        if (!name.endsWith("Error")) {
            throw new IllegalArgumentException("Class name must end with \"Error\"");
        }
        key = "Validator." + name.substring(0, name.indexOf("Error"));
        this.mapModel = mapModel;
    }

    /**
     * Creates a new instance.
     * @param key Key
     * @param mapModel the map model that caused the error
     */
    protected ValidationError(@NotNull final String key, @NotNull final MapModel<G, A, R> mapModel) {
        this.key = key;
        this.mapModel = mapModel;
    }

    /**
     * Return the map on which the error occurred.
     * @return the erroneous map
     */
    @NotNull
    public MapModel<G, A, R> getMapModel() {
        return mapModel;
    }

    /**
     * Returns the {@link MapSquare MapSquares} that caused the error.
     * @return the map squares
     */
    @NotNull
    public List<MapSquare<G, A, R>> getMapSquares() {
        return Collections.unmodifiableList(mapSquares);
    }

    /**
     * Adds an erroneous {@link MapSquare}.
     * @param mapSquare the erroneous map square
     */
    protected void addMapSquare(@NotNull final MapSquare<G, A, R> mapSquare) {
        if (!mapSquares.contains(mapSquare)) {
            if (mapSquare.getMapModel() != mapModel) {
                throw new IllegalArgumentException();
            }
            mapSquares.add(mapSquare);
        }
    }

    /**
     * Returns the {@link GameObject GameObjects} that caused the error.
     * @return the game objects
     */
    @NotNull
    @SuppressWarnings("TypeMayBeWeakened")
    public List<G> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }

    /**
     * Adds an erroneous {@link GameObject}.
     * @param gameObject the erroneous game object
     */
    protected void addGameObject(@NotNull final G gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            if (mapSquare != null) {
                addMapSquare(mapSquare);
            }
        }
    }

    /**
     * Returns a parameter string to be used in the error message. It can be
     * accessed with {4+<code>id</code>}.
     * @param id the error id
     * @return the error string
     */
    @Nullable
    public abstract String getParameter(final int id);

    /**
     * Returns the error message for this validation error.
     * @return the error message for this validation error
     * @todo validation errors should be able to provide their own arguments for
     * message formatting
     */
    @NotNull
    public String getMessage() {
        final int x;
        final int y;
        if (mapSquares.isEmpty()) {
            x = 0;
            y = 0;
        } else {
            final MapSquare<G, A, R> mapSquare = mapSquares.get(0);
            x = mapSquare != null ? mapSquare.getMapX() : -1;
            y = mapSquare != null ? mapSquare.getMapY() : -1;
        }
        final String archName = gameObjects.isEmpty() ? null : gameObjects.get(0).getBestName();
        final String parameter0 = getParameter(0);
        final String parameter1 = getParameter(1);
        final String parameter2 = getParameter(2);
        final String parameter3 = getParameter(3);
        final StringBuilder sb = new StringBuilder();
        if (mapSquares.size() > 1) {
            appendMapSquares(sb);
        }
        appendTitle(sb);
        appendGameObjects(sb);
        return ACTION_BUILDER.format(key + ".msg", sb.toString(), x, y, archName, parameter0, parameter1, parameter2, parameter3);
    }

    /**
     * {@inheritDoc} The String representation of an error is its title.
     */
    @Nullable
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        appendMapSquares(sb);
        appendTitle(sb);
        appendGameObjects(sb);
        return sb.toString();
    }

    /**
     * Appends the map squares of this error to a {@link StringBuilder}.
     * @param sb the string builder
     */
    private void appendMapSquares(@NotNull final StringBuilder sb) {
        appendObjects(sb, mapSquares, mapSquareFormatter, "[", "] ", TITLE_MAP_SQUARES);
    }

    /**
     * Appends the title of this error to a {@link StringBuilder}.
     * @param sb the string builder
     */
    private void appendTitle(@NotNull final StringBuilder sb) {
        final String title = ACTION_BUILDER.getString(key + ".title");
        if (title == null) {
            sb.append(key);
        } else {
            sb.append(title);
        }
    }

    /**
     * Appends the game objects of this error to a {@link StringBuilder}.
     * @param sb the string builder
     */
    private void appendGameObjects(@NotNull final StringBuilder sb) {
        appendObjects(sb, gameObjects, gameObjectFormatter, " [", "]", TITLE_GAME_OBJECTS);
    }

    /**
     * Appends the given objects to a {@link StringBuilder}.
     * @param sb the string builder
     * @param objects the objects to format
     * @param formatter the formatter for formatting the objects
     * @param prefix the prefix to prepend to the objects
     * @param postfix the postfix to append to the objects
     * @param maxObjects the maximum number of objects to show
     */
    private static <O> void appendObjects(@NotNull final StringBuilder sb, @NotNull final Collection<O> objects, @NotNull final Formatter<O> formatter, @NotNull final String prefix, @NotNull final String postfix, final int maxObjects) {
        if (objects.isEmpty()) {
            return;
        }

        final List<O> sortedObjects = new ArrayList<O>(objects);
        Collections.sort(sortedObjects, formatter);

        sb.append(prefix);
        int count = 0;
        for (final O object : sortedObjects) {
            if (count > 0) {
                sb.append(' ');
            }
            sb.append(formatter.toString(object));
            count++;
            if (count >= maxObjects && sortedObjects.size() > maxObjects + 1) {
                sb.append("...");
                break;
            }
        }
        sb.append(postfix);
    }

    /**
     * Interface for formatting objects.
     * @param <O> the object type to format
     * @author Andreas Kirschbaum
     */
    private interface Formatter<O> extends Comparator<O>, Serializable {

        /**
         * Returns a string representation of an object.
         * @param obj the object
         * @return the string representation
         */
        @NotNull
        String toString(@NotNull O obj);

    }

    /**
     * A {@link Formatter} for {@link GameObject GameObjects}.
     * @author Andreas Kirschbaum
     */
    private class GameObjectFormatter implements Formatter<G> {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @NotNull
        @Override
        public String toString(@NotNull final G obj) {
            return obj.getBestName();
        }

        @Override
        public int compare(@NotNull final G o1, @NotNull final G o2) {
            return toString(o1).compareTo(toString(o2));
        }

    }

    /**
     * A {@link Formatter} for {@link MapSquare MapSquares}.
     * @author Andreas Kirschbaum
     */
    private class MapSquareFormatter implements Formatter<MapSquare<G, A, R>> {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @NotNull
        @Override
        public String toString(@NotNull final MapSquare<G, A, R> obj) {
            return obj.getMapX() + "|" + obj.getMapY();
        }

        @Override
        public int compare(@NotNull final MapSquare<G, A, R> o1, @NotNull final MapSquare<G, A, R> o2) {
            final int y1 = o1.getMapY();
            final int y2 = o2.getMapY();
            if (y1 < y2) {
                return -1;
            } else if (y1 > y2) {
                return +1;
            }

            final int x1 = o1.getMapX();
            final int x2 = o2.getMapX();
            if (x1 < x2) {
                return -1;
            } else if (x1 > x2) {
                return +1;
            }

            return 0;
        }

    }

}
