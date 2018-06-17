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

package net.sf.gridarta.model.maparchobject;

import java.awt.Point;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base implementation of {@link MapArchObject} that covers similarities between
 * Crossfire maps and Daimonin maps.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @warning NEVER use this class in a Collection! {@link #hashCode()} and {@link
 * #equals(Object)} work on non-final fields and thus will break any operation
 * inside Collections!
 */
public abstract class AbstractMapArchObject<A extends MapArchObject<A>> implements MapArchObject<A> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of an unnamed map.
     */
    @NotNull
    public static final String MAP_NAME_UNNAMED = "<unnamed>";

    /**
     * The prefix for the map attribute that is updated with the last
     * modification timestamp.
     */
    @NotNull
    private static final String MODIFIED_ATTRIBUTE = "Modified:";

    /**
     * The pattern to find the {@link #MODIFIED_ATTRIBUTE} in the message text.
     */
    @NotNull
    private static final Pattern MODIFIED_ATTRIBUTE_PATTERN = Pattern.compile("^" + MODIFIED_ATTRIBUTE + " *(.*)$", Pattern.MULTILINE);

    /**
     * The map text.
     */
    @NotNull
    private StringBuilder msgText = new StringBuilder();

    /**
     * The size of the map reflected by this map arch object.
     */
    @NotNull
    private Size2D mapSize = Size2D.ONE;

    /**
     * The name of this map.
     */
    @NotNull
    private String mapName = MAP_NAME_UNNAMED;

    /**
     * The x coordinate for entering the map.
     */
    private int enterX;

    /**
     * The y coordinate for entering the map.
     */
    private int enterY;

    /**
     * If set, this is an outdoor map.
     */
    private boolean outdoor;

    /**
     * The number of ticks that need to elapse before this map will be reset.
     */
    private int resetTimeout;

    /**
     * The number of ticks that must elapse after tha map has not been used
     * before it gets swapped out.
     */
    private int swapTime;

    /**
     * The map difficulty. If zero, server calculates something.
     */
    private int difficulty;

    /**
     * If nonzero, the map reset time will not be updated when someone enters /
     * exits the map.
     */
    private boolean fixedReset;

    /**
     * The light / darkness of map (overall). Zero means fully bright.
     */
    private int darkness;

    /**
     * The map tile paths used for map tiling. 0 = north, 1 = east, 2 = south, 3
     * = west. 4 = northeast, 5 = southeast, 6 = southwest, 7 = northwest
     */
    @NotNull
    private String[] tilePaths = new String[Direction.values().length];

    /**
     * The registered event listeners.
     */
    @NotNull
    private final EventListenerList2<MapArchObjectListener> listenerList = new EventListenerList2<MapArchObjectListener>(MapArchObjectListener.class);

    /**
     * The transaction depth. A value of 0 means there's no transaction going
     * on. A value > 0 means there's a transaction going on and denotes the
     * nesting level.
     * @invariant transactionDepth >= 0
     */
    private int transactionDepth;

    /**
     * The thread that performs the current transaction.
     * @invariant transactionDepth > 0 || transactionThread == null
     */
    @Nullable
    private transient Thread transactionThread;

    /**
     * Set if any attribute has changed inside the current transaction.
     */
    private boolean attributeHasChanged;

    /**
     * Creates a new instance.
     */
    protected AbstractMapArchObject() {
        Arrays.fill(tilePaths, "");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Size2D getMapSize() {
        return mapSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapSize(@NotNull final Size2D mapSize) {
        if (this.mapSize.equals(mapSize)) {
            return;
        }

        this.mapSize = mapSize;
        setModified();
        for (final MapArchObjectListener listener : listenerList.getListeners()) {
            listener.mapSizeChanged(mapSize);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getMapName() {
        return mapName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapName(@NotNull final String name) {
        final String trimmedName = name.trim();
        if (mapName.equals(trimmedName)) {
            return;
        }

        mapName = trimmedName;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEnterX() {
        return enterX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnterX(final int enterX) {
        if (this.enterX == enterX) {
            return;
        }

        this.enterX = enterX;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEnterY() {
        return enterY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnterY(final int enterY) {
        if (this.enterY == enterY) {
            return;
        }

        this.enterY = enterY;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Point getEnter() {
        return new Point(enterX, enterY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOutdoor() {
        return outdoor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOutdoor(final boolean outdoor) {
        if (this.outdoor == outdoor) {
            return;
        }

        this.outdoor = outdoor;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResetTimeout() {
        return resetTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResetTimeout(final int resetTimeout) {
        if (this.resetTimeout == resetTimeout) {
            return;
        }

        this.resetTimeout = resetTimeout;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSwapTime() {
        return swapTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSwapTime(final int swapTime) {
        if (this.swapTime == swapTime) {
            return;
        }

        this.swapTime = swapTime;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDifficulty(final int difficulty) {
        if (this.difficulty == difficulty) {
            return;
        }

        this.difficulty = difficulty;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFixedReset() {
        return fixedReset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFixedReset(final boolean fixedReset) {
        if (this.fixedReset == fixedReset) {
            return;
        }

        this.fixedReset = fixedReset;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDarkness() {
        return darkness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDarkness(final int darkness) {
        if (this.darkness == darkness) {
            return;
        }

        this.darkness = darkness;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTilePaths() {
        return tilePaths.length;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getTilePath(@NotNull final Direction direction) {
        return tilePaths[direction.ordinal()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTilePath(@NotNull final Direction direction, @NotNull final String tilePath) {
        if (tilePaths[direction.ordinal()].equals(tilePath)) {
            return;
        }

        tilePaths[direction.ordinal()] = tilePath;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(@NotNull final A mapArchObject) {
        final AbstractMapArchObject<?> abstractMapArchObject = (AbstractMapArchObject<?>) mapArchObject;
        setText(abstractMapArchObject.msgText.toString());
        setMapSize(abstractMapArchObject.mapSize);
        setMapName(abstractMapArchObject.mapName);
        setEnterX(abstractMapArchObject.enterX);
        setEnterY(abstractMapArchObject.enterY);
        setOutdoor(abstractMapArchObject.outdoor);
        setResetTimeout(abstractMapArchObject.resetTimeout);
        setSwapTime(abstractMapArchObject.swapTime);
        setDifficulty(abstractMapArchObject.difficulty);
        setFixedReset(abstractMapArchObject.fixedReset);
        setDarkness(abstractMapArchObject.darkness);
        for (final Direction direction : Direction.values()) {
            setTilePath(direction, abstractMapArchObject.tilePaths[direction.ordinal()]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final AbstractMapArchObject<?> mapArchObject = (AbstractMapArchObject<?>) obj;
        return mapArchObject.msgText.toString().equals(msgText.toString()) && mapArchObject.mapSize.equals(mapSize) && mapArchObject.mapName.equals(mapName) && mapArchObject.enterX == enterX && mapArchObject.enterY == enterY && mapArchObject.outdoor == outdoor && mapArchObject.resetTimeout == resetTimeout && mapArchObject.swapTime == swapTime && mapArchObject.difficulty == difficulty && mapArchObject.fixedReset == fixedReset && mapArchObject.darkness == darkness && Arrays.equals(mapArchObject.tilePaths, tilePaths);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return msgText.hashCode() + mapSize.hashCode() + mapName.hashCode() + enterX + enterY + (outdoor ? 2 : 0) + resetTimeout + swapTime + difficulty + (fixedReset ? 1 : 0) + darkness + Arrays.hashCode(tilePaths);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMapArchObjectListener(@NotNull final MapArchObjectListener listener) {
        listenerList.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMapArchObjectListener(@NotNull final MapArchObjectListener listener) {
        listenerList.remove(listener);
    }

    /**
     * Marks this map arch object as changed. The map arch object then notifies
     * the registered listeners of the change at the end of the transaction.
     */
    protected void setModified() {
        attributeHasChanged = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beginTransaction() {
        if (transactionDepth == 0) {
            attributeHasChanged = false;
            transactionThread = Thread.currentThread();
        } else {
            // == is okay for threads.
            //noinspection ObjectEquality
            if (transactionThread != Thread.currentThread()) {
                throw new IllegalStateException("A transaction must only be used by one thread.");
            }
        }
        transactionDepth++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endTransaction() {
        endTransaction(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endTransaction(final boolean fireEvent) {
        if (transactionDepth <= 0) {
            throw new IllegalStateException("Tried to end a transaction but no transaction was open.");
        }
        transactionDepth--;
        assert transactionDepth >= 0;
        if (transactionDepth == 0) {
            commitTransaction();
        } else if (fireEvent && transactionDepth > 0) {
            if (attributeHasChanged) {
                fireMetaChangedEvent();
            }
        }
    }

    /**
     * Performs ending a transaction. Resets all transaction states and fires an
     * event.
     */
    private void commitTransaction() {
        transactionDepth = 0;
        transactionThread = null;
        if (attributeHasChanged) {
            fireMetaChangedEvent();
            attributeHasChanged = false;
        }
    }

    /**
     * Fires a MapMetaChangedEvent.
     */
    private void fireMetaChangedEvent() {
        for (final MapArchObjectListener listener : listenerList.getListeners()) {
            listener.mapMetaChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addText(@NotNull final String text) {
        if (text.length() == 0) {
            return;
        }

        msgText.append(text);
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(final String text) {
        if (msgText.toString().equals(text)) {
            return;
        }

        msgText.delete(0, msgText.length());
        msgText.append(text);
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getText() {
        return msgText.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateModifiedAttribute(@NotNull final String userName) {
        final String modifiedAttribute = MODIFIED_ATTRIBUTE + " " + String.format("%tF", System.currentTimeMillis()) + " " + userName;

        final Matcher matcher = MODIFIED_ATTRIBUTE_PATTERN.matcher(msgText);
        if (!matcher.find()) {
            // attribute does not exist => add a new one
            msgText.append("\n");
            msgText.append(modifiedAttribute);
            setModified();
        } else if (!matcher.group().equals(modifiedAttribute)) {
            // attribute exists => replace it
            msgText.replace(matcher.start(), matcher.end(), modifiedAttribute);
            setModified();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPointValid(@Nullable final Point pos) {
        return pos != null && pos.x >= 0 && pos.y >= 0 && pos.x < mapSize.getWidth() && pos.y < mapSize.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public A clone() {
        final AbstractMapArchObject<A> clone;
        try {
            //noinspection OverriddenMethodCallDuringObjectConstruction
            clone = (AbstractMapArchObject<A>) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
        //noinspection CloneCallsConstructors
        clone.msgText = new StringBuilder(msgText.toString());
        clone.tilePaths = tilePaths.clone();
        return clone.getThis();
    }

    /**
     * Returns this map arch object cast to its real type.
     * @return this map arch object
     */
    @NotNull
    protected abstract A getThis();

}
