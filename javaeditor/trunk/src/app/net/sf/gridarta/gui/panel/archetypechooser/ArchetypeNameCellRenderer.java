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

package net.sf.gridarta.gui.panel.archetypechooser;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link DisplayMode} showing archetypes as images and archetype name.
 * @author Andreas Kirschbaum
 */
public class ArchetypeNameCellRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DisplayMode<G, A, R> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public ArchetypeNameCellRenderer(@NotNull final FaceObjectProviders faceObjectProviders) {
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        Component cmp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final Archetype<?, ?, ?> archetype = (Archetype<?, ?, ?>) value;
        setText(archetype.getArchetypeName());
        JPanel panel = new JPanel(new BorderLayout());  
        JLabel topIcon = new JLabel("");  
        JLabel bottomIcon = new JLabel("");  
        panel.add(topIcon, BorderLayout.NORTH);  
        panel.add(bottomIcon, BorderLayout.CENTER);
        topIcon.setIcon(faceObjectProviders.getSecondFace(archetype));
        bottomIcon.setIcon(faceObjectProviders.getFace(archetype));
        panel.add(this,BorderLayout.SOUTH);
        return panel;
       // setIcon(faceObjectProviders.getFace(archetype));
       // return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(final Archetype<G, A, R> o1, final Archetype<G, A, R> o2) {
        return o1.getArchetypeName().compareToIgnoreCase(o2.getArchetypeName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLayoutOrientation() {
        return JList.VERTICAL;
    }

}
