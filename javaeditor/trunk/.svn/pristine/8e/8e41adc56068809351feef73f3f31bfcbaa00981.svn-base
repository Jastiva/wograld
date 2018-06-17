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

package net.sf.gridarta.model.scripts;

import java.io.File;
import java.io.IOException;
import net.sf.gridarta.model.io.PathManager;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ScriptUtils {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ScriptUtils.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private ScriptUtils() {
    }

    /**
     * This method is called when the user selects a new event to be created.
     * The path relative to the map dir is calculated, and if reasonable, a
     * relative path is created (relative to the map the event is on).
     * @param localMapDir the base directory
     * @param f script file
     * @param mapDir the map directory
     * @return local event path
     */
    public static String localizeEventPath(@NotNull final File localMapDir, final File f, @NotNull final File mapDir) {
        if (!mapDir.exists()) {
            log.warn("Map directory '" + mapDir.getAbsolutePath() + "' does not exist!");
            return f.getName();
        }

        // find out if the script file is in a sub-directory of the map file
        File tmp;
        for (tmp = f.getParentFile(); tmp != null && !tmp.getAbsolutePath().equalsIgnoreCase(localMapDir.getAbsolutePath()); tmp = tmp.getParentFile()) {
        }

        // FIXME: It would be a good idea to perform the canonization somewhere else.
        //        The paths returned by mapFile.getParentFile() and mControl.getMapDefaultFolder() should already be canonical
        String path;
        if (tmp == null) {
            // script file is NOT in a sub-directory of map file -> absolute path
            try {
                path = f.getAbsolutePath().substring(mapDir.getCanonicalPath().length());
            } catch (final IOException ignored) {
                path = f.getAbsolutePath().substring(mapDir.getAbsolutePath().length());
            }
            path = path.replace('\\', '/');
            if (!path.startsWith("/")) {
                path = "/" + path; // leading slash
            }
        } else {
            // script file is in a sub-directory of map file -> relative path
            try {
                path = f.getAbsolutePath().substring(localMapDir.getCanonicalPath().length());
            } catch (final IOException ignored) {
                path = f.getAbsolutePath().substring(localMapDir.getAbsolutePath().length());
            }
            path = path.replace('\\', '/');
            while (path.length() > 0 && path.startsWith("/")) {
                path = path.substring(1); // no leading slash
            }
        }
        return path;
    }

    /**
     * Try to create a reasonable default script name for lazy users.
     * @param baseDir the base dir for the default script name
     * @param archetypeName the best suitable name for the archetype (see
     * GameObject.getBestName())
     * @param scriptEnding the ending for scripts
     * @param pathManager the path manager for converting path names
     * @return a nice default script name without whitespaces
     */
    public static String chooseDefaultScriptName(@NotNull final File baseDir, final String archetypeName, final String scriptEnding, @NotNull final PathManager pathManager) {
        String defScriptName = archetypeName.trim();
        final int i = defScriptName.indexOf(' ');
        if (i >= 0) {
            if (defScriptName.length() > 12 || defScriptName.lastIndexOf(' ') != i) {
                // if there are several whitespaces or the name is too long, just cut off the end
                defScriptName = defScriptName.substring(0, i);
            } else {
                // if there is only one whitespace in a short name, remove whitespace
                defScriptName = defScriptName.substring(0, i) + defScriptName.substring(i + 1, i + 2).toUpperCase() + defScriptName.substring(i + 2);
            }
        }
        if (defScriptName.length() >= 3) {
            defScriptName = defScriptName.substring(0, 1).toUpperCase() + defScriptName.substring(1);
        }
        defScriptName += "Script" + scriptEnding;

        return pathManager.getMapPath(new File(baseDir, defScriptName));
    }

}
