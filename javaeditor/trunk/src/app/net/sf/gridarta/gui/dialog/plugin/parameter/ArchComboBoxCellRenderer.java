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

package net.sf.gridarta.gui.dialog.plugin.parameter;

import java.awt.Component;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import org.jetbrains.annotations.NotNull;

public class ArchComboBoxCellRenderer extends DefaultListCellRenderer {

    private static final String SIZE_TESTER = "**SizeTester**";

    private static final long serialVersionUID = 1L;

    @NotNull
    private final ComboBoxEditor archComboBoxEditor;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Creates a new instance.
     * @param archComboBoxEditor the instance to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public ArchComboBoxCellRenderer(@NotNull final ComboBoxEditor archComboBoxEditor, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.archComboBoxEditor = archComboBoxEditor;
        this.faceObjectProviders = faceObjectProviders;
    }

    /* This is the only method defined by ListCellRenderer.  We just
     * reconfigure the label each time we're called.
     */

    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        if (SIZE_TESTER.equals(value)) {
            return archComboBoxEditor.getEditorComponent();
        }
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         *colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        /* We additionally set the JLabels icon property here.
         */
        final BaseObject<?, ?, ?, ?> arch = (BaseObject<?, ?, ?, ?>) value;
        if (arch == null) {
            setText("");
            setIcon(null);
            return this;
        }
        setText(arch.getArchetype().getArchetypeName());

        setIcon(faceObjectProviders.getFace(arch));

        return this;
    }

}
