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

package net.sf.gridarta.model.archetypetype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.utils.StringUtils;
import net.sf.gridarta.utils.WrappingStringBuilder;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class manages bitmask values which appear in Gridarta archetype
 * attributes. Attack type, spell path and material are such bitmasks. They are
 * disguised for the user, with the help of the attribute dialog.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AttributeBitmask {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(AttributeBitmask.class);

    /**
     * Maximum number of characters in a line before line break (see {@link
     * #getText(int)}).
     */
    private static final int MAX_CHARS_PER_LINE = 35; // 50

    /**
     * The names of the bitmask entries.
     */
    @NotNull
    private final List<String> bitName = new ArrayList<String>();

    /**
     * Set if the bitmask value may be encoded as strings in external
     * representation; unset if the value is encoded as an integer.
     */
    private final boolean isNamed;

    /**
     * Maps names for value encoding in external representation.
     */
    @NotNull
    private final Map<String, Integer> namedValues = new HashMap<String, Integer>();

    /**
     * Maps bit value to external representation.
     */
    @NotNull
    private final Map<Integer, String> encodings = new HashMap<Integer, String>();

    /**
     * Maps bit value to readable name.
     */
    @NotNull
    private final Map<Integer, String> names = new HashMap<Integer, String>();

    /**
     * Constructor of a bitmask from XML element.
     * @param isNamed whether this attribute's bitmask values are to be encoded
     * as strings in external representation
     */
    public AttributeBitmask(final boolean isNamed) {
        this.isNamed = isNamed;
    }

    /**
     * Generate the text to be displayed for a given bitmask value.
     * @param value bitmask value
     * @return <code>String</code> with all entries belonging to the bitmask
     */
    @NotNull
    // feature envy is natural here - WrappingStringBuilder is a kind of library class.
    public String getText(final int value) {
        final WrappingStringBuilder sb = new WrappingStringBuilder(MAX_CHARS_PER_LINE);
        for (final String word : encodeValueAsList(value, names)) {
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * Convert a value to external representation. <p/> <p>Note that this
     * function does not always return the shortest or simplest possible
     * external representation. This is intentional to create the exact same
     * result as the Crossfire server.
     * @param value the value to convert
     * @return the external representation of the value
     */
    @NotNull
    public String encodeValue(final int value) {
        if (!isNamed) {
            return Integer.toString(value);
        }

        final StringBuilder sb = new StringBuilder();
        for (final String word : encodeValueAsList(value, encodings)) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(word);
        }
        return sb.toString();
    }

    /**
     * Convert a value to string representation. <p/> <p>Note that this function
     * does not always return the shortest or simplest possible external
     * representation. This is intentional to create the exact same result as
     * the Crossfire server.
     * @param value the value to convert
     * @param strings maps bit values to strings
     * @return the string representation of the value
     */
    @NotNull
    private Iterable<String> encodeValueAsList(final int value, @NotNull final Map<Integer, String> strings) {
        final Collection<String> result = new ArrayList<String>();

        final int moveAll = getMaxValue();

        final Collection<String> negResult = new ArrayList<String>();
        negResult.add("all");

        if (value == moveAll) {
            return negResult;
        }
        if (value == 0) {
            final String str = strings.get(0);
            result.add(str == null ? "0" : str);
            return result;
        }

        /* We basically slide the bits down. Why look at moveAll? because we
         * may want to return a string like 'all -swim', and if we just looked
         * at mt, we couldn't get that.
         */
        int allCount = 0;
        int count = 0;
        for (int i = moveAll; i != 0; i >>= 1) {
            final String strNull = strings.get(1 << count);
            final String str = strNull == null ? Integer.toString(1 << count) : strNull;
            if ((value & (1 << count)) == 0) {
                negResult.add("-" + str);
                allCount++;
            } else {
                result.add(str);
            }
            count++;
        }
        if ((value & ~moveAll) != 0) {
            result.add(Integer.toString(value & ~moveAll));
            return result;
        }
        /* Basically, if there is a single negation, return it, eg 'all -swim'.
         * But more than that, just return the enumerated values. It doesn't
         * make sense to return 'all -walk -fly_low' - it is shorter to return
         * 'fly_high swim'
         */
        return count >= 4 && allCount <= 1 ? negResult : result;
    }

    /**
     * Convert a value from external representation.
     * @param encodedValue the value in external representation
     * @return the decoded value
     */
    public int decodeValue(@NotNull final String encodedValue) {
        if (!isNamed) {
            return NumberUtils.parseInt(encodedValue);
        }

        if (encodedValue.length() == 0) {
            return 0;
        }

        int result = 0;
        for (final String word : StringUtils.PATTERN_SPACES.split(encodedValue, 0)) {
            final boolean negated;
            final String name;
            if (word.startsWith("-")) {
                negated = true;
                name = word.substring(1);
            } else {
                negated = false;
                name = word;
            }
            final Integer integerValue = namedValues.get(name);
            int value;
            if (integerValue != null) {
                value = integerValue;
            } else {
                try {
                    value = Integer.parseInt(name);
                } catch (final NumberFormatException ignored) {
                    log.warn("Ignoring unknown bitmask value: " + name);
                    value = 0;
                }
            }
            if (negated) {
                result &= ~value;
            } else {
                result |= value;
            }
        }
        return result;
    }

    /**
     * Returns the maximum allowed bitmask value.
     * @return the maximum allowed bitmask value
     */
    public int getMaxValue() {
        return (1 << bitName.size()) - 1;
    }

    /**
     * Returns the number of bitmask entries (not counting zero).
     * @return the number of bitmask entries
     */
    public int getNumber() {
        return bitName.size();
    }

    /**
     * Returns the name of a bitmask value.
     * @param index the index of the bitmask value
     * @return the name or <code>null</code> if this index is not defined
     */
    @Nullable
    public String getBitName(final int index) {
        return bitName.get(index);
    }

    /**
     * Adds a readable name for a bit value.
     * @param name the name
     * @param value the bit value
     */
    public void addName(@NotNull final String name, final int value) {
        names.put(value, name);
    }

    /**
     * Adds a name for external encoding of a value.
     * @param name the name
     * @param value the value
     */
    public void addNamedValue(@NotNull final String name, final int value) {
        namedValues.put(name, value);
        encodings.put(value, name);
    }

    /**
     * Returns whether an external encoding for value exists.
     * @param value the value
     * @return whether an external encoding exists
     */
    public boolean containsEncoding(final int value) {
        return encodings.containsKey(value);
    }

    /**
     * Defines a bit name for a bit value.
     * @param bitValue the bit value
     * @param name the name
     * @return whether the entry was added
     */
    public boolean addBitName(final int bitValue, @NotNull final String name) {
        if (bitValue >= bitName.size()) {
            do {
                bitName.add(null);
            } while (bitValue >= bitName.size());
        } else if (bitName.get(bitValue) != null) {
            return false;
        }

        bitName.set(bitValue, name);
        return true;
    }

}
