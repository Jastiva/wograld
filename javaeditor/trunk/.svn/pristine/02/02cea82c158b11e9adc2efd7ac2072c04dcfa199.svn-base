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

package net.sf.gridarta.textedit.textarea.tokenmarker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A factory for creating{@link TokenMarker} instances for {@link File Files}.
 * @author Andreas Kirschbaum
 */
public class TokenMarkerFactory {

    /**
     * File extension for {@link CrossfireDialogTokenMarker}.
     */
    private static final String CROSSFIRE_DIALOG = "crossfire-dialog";

    /**
     * File extension for {@link DaimoninAITokenMarker}.
     */
    private static final String DAIMONIN_AI = "daimonin-ai";

    /**
     * Maps file extensions to token marker classes.
     */
    private static final Map<String, Class<? extends TokenMarker>> tokenMarkers = new HashMap<String, Class<? extends TokenMarker>>();

    static {
        tokenMarkers.put("c", CTokenMarker.class);
        tokenMarkers.put("cc", CTokenMarker.class);
        tokenMarkers.put("cpp", CTokenMarker.class);
        tokenMarkers.put("h", CTokenMarker.class);
        tokenMarkers.put("hh", CTokenMarker.class);
        tokenMarkers.put("htm", HTMLTokenMarker.class);
        tokenMarkers.put("html", HTMLTokenMarker.class);
        tokenMarkers.put("js", JavaScriptTokenMarker.class);
        tokenMarkers.put("lua", LuaTokenMarker.class);
        tokenMarkers.put("py", PythonTokenMarker.class);
        tokenMarkers.put("xml", XMLTokenMarker.class);
        tokenMarkers.put(CROSSFIRE_DIALOG, CrossfireDialogTokenMarker.class);
        tokenMarkers.put(DAIMONIN_AI, DaimoninAITokenMarker.class);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private TokenMarkerFactory() {
    }

    /**
     * Creates a suitable {@link TokenMarker} for a given file.
     * @param file the file to create a token marker for
     * @return the created token marker
     */
    @NotNull
    public static TokenMarker createTokenMarker(@Nullable final File file) {
        if (file == null) {
            return new EmptyTokenMarker();
        }

        final String filename = file.getName();
        final int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            return new EmptyTokenMarker();
        }
        return createTokenMarker(filename.substring(dotIndex + 1));
    }

    /**
     * Creates a {@link TokenMarker} for a given file extension.
     * @param extension the file extension
     * @return the token marker
     */
    @NotNull
    public static TokenMarker createTokenMarker(@Nullable final String extension) {
        if (extension == null) {
            return new EmptyTokenMarker();
        }

        final Class<? extends TokenMarker> tokenMarkerClass = tokenMarkers.get(extension);
        if (tokenMarkerClass == null) {
            return new EmptyTokenMarker();
        }
        try {
            return tokenMarkerClass.newInstance();
        } catch (final InstantiationException ex) {
            return new EmptyTokenMarker();
        } catch (final IllegalAccessException ex) {
            return new EmptyTokenMarker();
        }
    }

} // TokenMarkerFactory
