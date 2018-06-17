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

package net.sf.gridarta.model.baseobject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A set of key/value attributes.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class GameObjectText implements Cloneable, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The objectText with the differences from the Archetype.
     * @note Every line in the objectText must end on '\n', including the last
     * line.
     * @serial
     */
    @NotNull
    private StringBuilder objectText = new StringBuilder();

    /**
     * Map which caches attributes for faster access. Key: attribute name value:
     * attribute value
     * @serial
     */
    private Map<String, String> attributeCache = new HashMap<String, String>();

    /**
     * Clears the attribute cache.
     */
    private void clearAttributeCache() {
        attributeCache.clear();
    }

    /**
     * Returns an attribute value by attribute name.
     * @param attributeName the attribute name
     * @return the attribute value or <code>null</code>
     */
    @Nullable
    public String getAttributeValue(@NotNull final String attributeName) {
        final String result;
        if (attributeCache.containsKey(attributeName)) {
            result = attributeCache.get(attributeName);
        } else {
            result = getAttributeValueInt(attributeName);
            attributeCache.put(attributeName, result);
        }
        return result;
    }

    /**
     * Returns the requested attribute value from the objectText.
     * @param attributeName the name of the attribute to search
     * @return the attribute value from the objectText or <code>null</code> if
     *         not found
     */
    @Nullable
    private String getAttributeValueInt(@NotNull final String attributeName) {
        final String attr2 = attributeName.trim() + ' ';
        String result = null;
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(objectText.toString(), 0)) {
            if (line.startsWith(attr2)) {
                result = line.substring(attr2.length());
                break;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GameObjectText clone() {
        final GameObjectText clone;
        try {
            clone = (GameObjectText) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
        //noinspection CloneCallsConstructors
        clone.objectText = new StringBuilder(objectText);
        clone.attributeCache = new HashMap<String, String>(attributeCache);
        return clone;
    }

    /**
     * Returns the object text.
     * @return the object text
     */
    @NotNull
    public String getObjectText() {
        return objectText.toString();
    }

    /**
     * Returns whether an object text is set.
     * @return whether an object text is set
     */
    public boolean hasObjectText() {
        return objectText.length() > 0;
    }

    /**
     * Appends a line to the object text.
     * @param line the line to append, may contain '\n' for appending multiple
     * lines
     */
    public void addObjectText(@NotNull final String line) {
        if (line.length() == 0) {
            return;
        }
        objectText.append(line);
        if (!line.endsWith("\n")) {
            objectText.append('\n');
        }
        clearAttributeCache();
    }

    /**
     * Clears the object text.
     */
    public void resetObjectText() {
        objectText.setLength(0);
        clearAttributeCache();
    }

    /**
     * Sets the object text.
     * @param objectText the object text to set
     */
    public void setObjectText(@NotNull final String objectText) {
        this.objectText.setLength(0);
        this.objectText.append(objectText);
        if (objectText.length() > 0 && !objectText.endsWith("\n")) {
            this.objectText.append('\n');
        }
        clearAttributeCache();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return objectText.toString().hashCode();
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
        final GameObjectText gameObjectText = (GameObjectText) obj;
        return gameObjectText.objectText.toString().equals(objectText.toString());
    }

}
