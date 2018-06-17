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

package net.sf.gridarta.var.daimonin.maincontrol;

import net.sf.gridarta.maincontrol.GridartaEditor;
import net.sf.gridarta.var.daimonin.model.archetype.Archetype;
import net.sf.gridarta.var.daimonin.model.gameobject.GameObject;
import net.sf.gridarta.var.daimonin.model.maparchobject.MapArchObject;

/**
 * Main class, launches the level editor application.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DaimoninEditor {

    /**
     * Prevent instantiation (UtilityClass).
     */
    private DaimoninEditor() {
    }

    /**
     * The main method that is invoked by the Java Runtime.
     * @param args The command line parameters given to the level editor.
     */
    public static void main(final String... args) {
        new GridartaEditor<GameObject, MapArchObject, Archetype>("net.sf.gridarta.var.daimonin.tod").run("net.sf.gridarta.var.daimonin", "DaimoninEditor.jar", new DefaultEditorFactory(), null, args);
    }

}
