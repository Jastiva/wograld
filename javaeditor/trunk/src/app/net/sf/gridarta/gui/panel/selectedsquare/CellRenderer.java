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

package net.sf.gridarta.gui.panel.selectedsquare;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
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
 * CellRenderer for rendering ArchObjects on a certain map square in a list.
 * @author Andreas Kirschbaum
 */
public class CellRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DefaultListCellRenderer {

    /**
     * The number of pixels to indent inventory items.
     */
    private static final int INDENT_PIXELS = 16;

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
     * The {@link Icon} for unknown squares.
     */
    @NotNull
    private final Icon unknownSquareIcon;

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param unknownSquareIcon the icon for unknown squares
     */
    public CellRenderer(@NotNull final FaceObjectProviders faceObjectProviders, @NotNull final Icon unknownSquareIcon) {
        this.faceObjectProviders = faceObjectProviders;
        this.unknownSquareIcon = unknownSquareIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        //DefaultListCellRenderer does not use type parameters
        @SuppressWarnings("unchecked") GameObject<G, A, R> arch = (GameObject<G, A, R>) value;

        // arch == null should not happen, but it *can* happen when the active
        // window gets changed by user and java is still blitting here
        
        JPanel panel = new JPanel(new BorderLayout());  
        JLabel topIcon = new JLabel("");  
        JLabel bottomIcon = new JLabel(""); 
        JLabel indentation = new JLabel("");
        panel.add(topIcon, BorderLayout.NORTH);  
        JPanel panel2 = new JPanel(new BorderLayout());
        panel.add(bottomIcon, BorderLayout.CENTER);
          
        if (arch != null) {
            arch = arch.getHead();
         //   setIcon(faceObjectProviders.getFace(arch));
           topIcon.setIcon(faceObjectProviders.getSecondFace(arch));
        bottomIcon.setIcon(faceObjectProviders.getFace(arch)); 
            setText(arch.getBestName());
        } else {
           // setIcon(unknownSquareIcon);
            topIcon.setIcon(unknownSquareIcon);
        bottomIcon.setIcon(unknownSquareIcon); 
            
            setText("?");
        }

        int indent = 0;
        if (arch != null) {
            while (true) {
                final GameObject<G, A, R> env = arch.getContainerGameObject();
                if (env == null) {
                    break;
                }
                arch = env;
                indent++;
            }
        }
        if (indent > 0) {
            indentation.setBorder(BorderFactory.createEmptyBorder(0, indent * INDENT_PIXELS, 0, 0)); // indentation
        }

     //   return this;
         panel.add(this,BorderLayout.SOUTH);
         panel2.add(panel,BorderLayout.CENTER);
         panel2.add(indentation, BorderLayout.WEST);
        return panel2;
        
    }

}
