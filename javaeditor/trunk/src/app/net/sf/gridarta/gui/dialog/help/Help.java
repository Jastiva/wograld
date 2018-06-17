/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.dialog.help;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Rectangle;
import javax.swing.JDialog;
import net.sf.japi.swing.action.DisposeAction;

/**
 * <code>CFHelp</code> implements the Help Window is a separate frame with html
 * content.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo make 1 single class out of these two
 * @todo add back and home buttons so they needn't be in the documentation
 * @todo add method to set the currently displayed help page from the outside
 * @todo add bookmarks to help
 * @todo add index to help
 * @deprecated We'll use JavaHelp instead.
 */
@Deprecated
public class Help extends JDialog {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a "Help".
     * @param parent the main view to get bounds from to position the help
     * @param fileName may contain different things: 1. File name of a html-file
     * to be opened 3. html-text to be displayed directly (no file) (this text
     * must start with "&lt;HTML&gt;")
     * @todo refactor this. The dual semantic of fileName is a Bad Thing.
     * @todo always creating new help windows is a Bad Thing as well, make this
     * constructor private
     */
    public Help(final Frame parent, final String fileName) {
        super(parent, "Help", false);    // super constructor
        setResizable(true);

        final Rectangle mvb = parent.getBounds(); // get main view bounds
        setBounds(mvb.x + mvb.width / 2 - 260, mvb.y + 70, 520, 600); // standard

        final Container html;
        if (fileName.startsWith("<HTML>") || fileName.startsWith("<html>")) {
            html = new HtmlPane("text/html", fileName); // direct text
        } else {
            html = new HtmlPane(fileName);  // read html file
        }
        DisposeAction.install(this);
        setContentPane(html);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

}
