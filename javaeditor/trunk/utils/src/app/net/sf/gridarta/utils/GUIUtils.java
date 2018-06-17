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

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import javax.swing.ImageIcon;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * <code>CGUtils</code> is a collection of GUI utility methods. Mainly focusing
 * on resource management.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class GUIUtils {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(GUIUtils.class);

    /**
     * Caches image icons. Maps icon name to image icon.
     */
    @NotNull
    private final Map<String, ImageIcon> imageCache = new HashMap<String, ImageIcon>();

    /**
     * Returns the image icon for the given icon name. Loads every icon only
     * once and uses hash table to return the same instance if same icon name is
     * given. Note: There must not be conflicting icon names from different
     * directories.
     * @param iconName the icon name
     * @return the image icon for the given icon name
     * @throws MissingResourceException if the icon cannot be loaded
     */
    @NotNull
    public ImageIcon getResourceIcon(@NotNull final String iconName) throws MissingResourceException {
        // check cache
        if (imageCache.containsKey(iconName)) {
            if (log.isDebugEnabled()) {
                log.debug("getResourceIcon(" + iconName + "): return cached");
            }
            return imageCache.get(iconName);
        }

        @NotNull final ImageIcon icon;

        final URL iconURL = GUIUtils.class.getClassLoader().getResource(iconName);
        if (iconURL != null) {
            if (log.isDebugEnabled()) {
                log.debug("getResourceIcon(" + iconName + "): loading from resource: " + iconURL);
            }
            icon = new ImageIcon(iconURL);
        } else {
            final File iconFile = new File(iconName);
            if (iconFile.exists()) {
                if (log.isDebugEnabled()) {
                    log.debug("getResourceIcon(" + iconName + "): loading from file: " + iconFile);
                }
                icon = new ImageIcon(iconFile.getAbsolutePath());
            } else {
                log.warn("Cannot find icon '" + iconName + "'");
                throw new MissingResourceException("missing resource: icon " + iconName, GUIUtils.class.getName(), iconName);
            }
        }

        imageCache.put(iconName, icon);
        return icon;
    }

    /**
     * Add an image to the cache.
     * @param name the name
     * @param imageIcon the image icon
     */
    public void addToCache(@NotNull final String name, @NotNull final ImageIcon imageIcon) {
        imageCache.put(name, imageIcon);
    }

}
