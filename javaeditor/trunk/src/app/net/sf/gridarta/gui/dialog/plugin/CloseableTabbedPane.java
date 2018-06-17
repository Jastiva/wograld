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

/*
* Section two. Copy of licensing declaration xnap source code.
*
*  Java Napster version x.yz (for current version number as well as for
*  additional information see version.txt)
*
*  Previous versions of this program were written by Florian Student
*  and Michael Ransburg available at www.weblicity.de/jnapster and
*  http://www.tux.org/~daneel/content/projects/10.shtml respectively.
*
*
*  This program is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*/

package net.sf.gridarta.gui.dialog.plugin;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CloseableTabbedPane extends JTabbedPane {

    /**
     * The icon or <code>null</code>.
     */
    @Nullable
    private final ImageIcon closingIcon;

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param closingIcon the icons or <code>null</code>
     */
    public CloseableTabbedPane(@Nullable final ImageIcon closingIcon) {
        this.closingIcon = closingIcon;

        addMouseListener(new ClosingListener(this));
    }

    public void addCloseableTab(@NotNull final String title, @NotNull final Component component, final boolean closeable) {
        if (closeable) {
            addTab(title, new ClosingIcon(closingIcon), component);
        } else {
            super.addTab(title, component);
        }
        setSelectedComponent(component);
    }

    @Override
    public void addTab(@NotNull final String title, @NotNull final Component component) {
        addCloseableTab(title, component, true);
    }

}
