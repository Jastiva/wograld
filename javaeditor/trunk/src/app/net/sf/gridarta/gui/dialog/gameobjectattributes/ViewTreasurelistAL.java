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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.treasurelist.CFTreasureListTree;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link javax.swing.Action Action} for the buttons on treasurelists. When
 * such a button is pressed, the dialog with treasurelists pops up.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ViewTreasurelistAL extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link CFTreasureListTree} to display.
     */
    @NotNull
    private final CFTreasureListTree treasureListTree;

    /**
     * The input field which contains the selected treasurelist.
     */
    @NotNull
    private final JTextComponent input;

    /**
     * The parent component to show on.
     */
    @NotNull
    private final Component parent;

    /**
     * Constructor.
     * @param input the input field which contains the selected treasurelist
     * @param treasureListTree the treasure list tree to display
     * @param parent the parent component to show on
     */
    public ViewTreasurelistAL(@NotNull final JTextComponent input, @NotNull final Component parent, @NotNull final CFTreasureListTree treasureListTree) {
        super("treasurelist:");
        this.treasureListTree = treasureListTree;
        this.input = input;
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        treasureListTree.showDialog(input, parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

}
