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

package net.sf.gridarta.gui.data;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.JLabel;
 import javax.swing.JPanel;
 import java.awt.BorderLayout;
import net.sf.gridarta.model.data.NamedObject;
import net.sf.gridarta.model.data.NamedTreeNode;
import net.sf.gridarta.model.face.FaceObjectProviders;
import org.jetbrains.annotations.NotNull;

/**
 * TreeCellRenderer for NamedTreeNodes in trees.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class NamedNodeTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
     * The serial version UID.
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
    public NamedNodeTreeCellRenderer(@NotNull final FaceObjectProviders faceObjectProviders) {
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        final NamedTreeNode<?> node = (NamedTreeNode<?>) value;
        final NamedObject object = node.getNamedObject();
        
          JPanel panel = new JPanel(new BorderLayout());  
        JLabel topIcon = new JLabel("");  
       JLabel bottomIcon = new JLabel("");  
       panel.add(topIcon, BorderLayout.NORTH);  
        panel.add(bottomIcon, BorderLayout.CENTER);
       
     //   setIcon(faceObjectProviders.getFace(archetype));
     //    setText("");
        if (object != null) {
        //    setIcon(faceObjectProviders.getDisplayIcon(object));
             topIcon.setIcon(faceObjectProviders.getSecondDisplayIcon(object));
       bottomIcon.setIcon(faceObjectProviders.getDisplayIcon(object));
        }
        setText(node.getName());
      //  return this;
          panel.add(this,BorderLayout.SOUTH);
       return panel;
        
    }

}
