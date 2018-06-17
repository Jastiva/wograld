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

package net.sf.gridarta.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link StringUtils}.
 * @author Andreas Kirschbaum
 */
public class StringUtilsTest {

    /**
     * Test case for {@link StringUtils#removeTrailingWhitespace(CharSequence)}.
     */
    @Test
    public void testRemoveTrailingWhitespace() {
        testRemoveTrailingWhitespace("", "");
        testRemoveTrailingWhitespace("   ", "");
        testRemoveTrailingWhitespace("  abc  def  ", "  abc  def");
        testRemoveTrailingWhitespace(" abc \n def ", " abc \n def");
    }

    /**
     * Checks one invocation of {@link StringUtils#removeTrailingWhitespace(CharSequence)}.
     * @param input the input string
     * @param output the expected output string
     */
    private static void testRemoveTrailingWhitespace(final CharSequence input, final String output) {
        Assert.assertEquals(output, StringUtils.removeTrailingWhitespace(input));
    }

    /**
     * Test case for {@link StringUtils#removeTrailingWhitespaceFromLines(CharSequence)}.
     */
    @Test
    public void testRemoveTrailingWhitespaceFromLines() {
        testRemoveTrailingWhitespaceFromLines("", "");
        testRemoveTrailingWhitespaceFromLines("   ", "");
        testRemoveTrailingWhitespaceFromLines("  abc  def  ", "  abc  def");
        testRemoveTrailingWhitespaceFromLines("\n", "\n");
        testRemoveTrailingWhitespaceFromLines("abc\n" + "def\n" + "ghi\n", "abc\n" + "def\n" + "ghi\n");
        testRemoveTrailingWhitespaceFromLines("   abc\n" + "def\n" + "ghi\n", "   abc\n" + "def\n" + "ghi\n");
        testRemoveTrailingWhitespaceFromLines("abc   \n" + "def\n" + "ghi\n", "abc\n" + "def\n" + "ghi\n");
        testRemoveTrailingWhitespaceFromLines(" abc \n d e f \n  g  h  i  \n   ", " abc\n d e f\n  g  h  i\n");
    }

    /**
     * Checks one invocation of {@link StringUtils#removeTrailingWhitespaceFromLines(CharSequence)}.
     * @param input the input string
     * @param output the expected output string
     */
    private static void testRemoveTrailingWhitespaceFromLines(@NotNull final CharSequence input, @NotNull final String output) {
        Assert.assertEquals(output, StringUtils.removeTrailingWhitespaceFromLines(input));
    }

    /**
     * Test case for {@link StringUtils#ensureTrailingNewline(String)}.
     */
    @Test
    public void testEnsureTrailingNewline() {
        testEnsureTrailingNewline("", "");
        testEnsureTrailingNewline("\n", "\n");
        testEnsureTrailingNewline("  abc  def  ", "  abc  def  \n");
        testEnsureTrailingNewline("\n\n\n", "\n\n\n");
        testEnsureTrailingNewline("abc\n\n" + "def", "abc\n\n" + "def\n");
    }

    /**
     * Checks one invocation of {@link StringUtils#ensureTrailingNewline(String)}.
     * @param input the input string
     * @param output the expected output string
     */
    private static void testEnsureTrailingNewline(@NotNull final String input, @NotNull final String output) {
        Assert.assertEquals(output, StringUtils.ensureTrailingNewline(input));
    }

    /**
     * Test case for {@link StringUtils#diffTextString(CharSequence, String,
     * boolean)}.
     */
    @Test
    public void testDiffTextString() {
        testDiffTextString("", "abc", null, null);

        testDiffTextString("abc", "abc", "abc", "abc");
        testDiffTextString("abc\n" + "def", "abc", "abc", "abc");
        testDiffTextString("def\n" + "abc\n" + "def", "abc", "abc", "abc");
        testDiffTextString("def\n" + "abc", "abc", "abc", "abc");

        testDiffTextString("abc def", "abc", null, "abc def");
        testDiffTextString("def abc\n" + "abc def", "abc", null, "abc def");
        testDiffTextString("abc def\n" + "def abc", "abc", null, "abc def");
        testDiffTextString("def abc\n" + "abc def\n" + "def abc", "abc", null, "abc def");
    }

    /**
     * Checks two invocations of {@link StringUtils#diffTextString(CharSequence,
     * String, boolean)}.
     * @param base the 'base' parameter to use
     * @param str the 'str' parameter to use
     * @param expectedFalse the expected return value if 'ignoreValues' is
     * <code>false</code>
     * @param expectedTrue the expected return value if 'ignoreValues' is
     * <code>true</code>
     */
    private static void testDiffTextString(@NotNull final CharSequence base, @NotNull final String str, @Nullable final String expectedFalse, @Nullable final String expectedTrue) {
        Assert.assertEquals(expectedFalse, StringUtils.diffTextString(base, str, false));
        Assert.assertEquals(expectedTrue, StringUtils.diffTextString(base, str, true));
    }

    /**
     * Test case for {@link StringUtils#getAttribute(CharSequence, String)}.
     */
    @Test
    public void testRemoveAttribute() {
        testGetAttribute("", "abc", null);
        testGetAttribute("abc def", "abc", "def");
        testGetAttribute("abc def", "ab", null);
        testGetAttribute("abc def", "def", null);
        testGetAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "abc", "def");
        testGetAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "ghi", "jkl");
        testGetAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "mno", "pqr");
        testGetAttribute("abc def\n" + "ghi jkl\n" + "mno pqr\n", "xxx", null);
    }

    /**
     * Checks that {@link StringUtils#getAttribute(CharSequence, String)} does
     * work.
     * @param attributes the attributes to search
     * @param attributeName the attribute name to search for
     * @param expectedAttributeValue the found attribute value
     */
    private static void testGetAttribute(@NotNull final CharSequence attributes, @NotNull final String attributeName, @Nullable final String expectedAttributeValue) {
        final String attributeValue = StringUtils.getAttribute(attributes, attributeName);
        Assert.assertEquals(expectedAttributeValue, attributeValue);
    }

}
