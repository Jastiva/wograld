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

package net.sf.gridarta.gui.treasurelist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.JLabel;
 import javax.swing.JPanel;
 import java.awt.BorderLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.treasurelist.ArchTreasureObj;
import net.sf.gridarta.model.treasurelist.FolderTreasureObj;
import net.sf.gridarta.model.treasurelist.NoTreasureObj;
import net.sf.gridarta.model.treasurelist.TreasureListTreasureObj;
import net.sf.gridarta.model.treasurelist.TreasureListTreasureObjType;
import net.sf.gridarta.model.treasurelist.TreasureObj;
import net.sf.gridarta.model.treasurelist.TreasureObjVisitor;
import net.sf.gridarta.model.treasurelist.TreasureTreeNode;
import net.sf.gridarta.model.treasurelist.YesTreasureObj;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;

/**
 * This cell renderer is responsible for drawing the treasure-object cells in
 * the JTree.
 * @author unknown
 */
public class TreasureCellRenderer extends DefaultTreeCellRenderer {

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
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * The {@link ArchetypeSet}.
     */
    @NotNull
    private final ArchetypeSet<?, ?, ?> archetypeSet;

    /**
     * The root node.
     * @serial
     */
    @NotNull
    private final DefaultMutableTreeNode root;

      
      JPanel panel;
             
    /**
     * The {@link TreasureObjVisitor} for setting rendering parameters.
     */
    private final TreasureObjVisitor treasureObjVisitor = new TreasureObjVisitor() {

        @Override
        public void visit(@NotNull final ArchTreasureObj treasureObj) {
            try {
               // setIcon(faceObjectProviders.getFace(archetypeSet.getArchetype(treasureObj.getName())));
                //
                   
                JLabel topIcon = new JLabel("");  
                JLabel bottomIcon = new JLabel("");  
                panel.add(topIcon, BorderLayout.NORTH);  
                panel.add(bottomIcon, BorderLayout.CENTER);
                topIcon.setIcon(faceObjectProviders.getSecondFace(archetypeSet.getArchetype(treasureObj.getName())   ));
                bottomIcon.setIcon(faceObjectProviders.getFace(archetypeSet.getArchetype(treasureObj.getName())));
     //   setIcon(faceObjectProviders.getFace(archetype));
              //  setText("");
               // panel.add(this,BorderLayout.SOUTH);
              
                
                
            } catch (final UndefinedArchetypeException ignored) {
                setIcon(systemIcons.getNoArchSquareIcon());
            }
            setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
        }

        @Override
        public void visit(@NotNull final FolderTreasureObj treasureObj) {
        }

        @Override
        public void visit(@NotNull final NoTreasureObj treasureObj) {
            setForeground(Color.gray);
            setIcon(systemIcons.getTreasureNoIcon());
        }

        @Override
        public void visit(@NotNull final TreasureListTreasureObj treasureObj) {
            if (treasureObj.getListType() == TreasureListTreasureObjType.MULTI) {
                if (treasureObj.getName().equalsIgnoreCase("none")) {
                    setIcon(null);
                    setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                } else {
                    setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
                    setIcon(systemIcons.getTreasureListIcon());
                }
            } else {
                if (treasureObj.getName().equalsIgnoreCase("none")) {
                    setIcon(null);
                    setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                } else {
                    setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
                    setIcon(systemIcons.getTreasureOneListIcon());
                }
            }
        }

        @Override
        public void visit(@NotNull final YesTreasureObj treasureObj) {
            setForeground(Color.gray);
            setIcon(systemIcons.getTreasureYesIcon());
        }

    };

    /**
     * Create a TreasureCellRenderer: load icons and initialize fonts.
     * @param archetypeSet the archetype set to get treasures from
     * @param root the root node
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param systemIcons the system icons for creating icons
     */
    public TreasureCellRenderer(@NotNull final ArchetypeSet<?, ?, ?> archetypeSet, @NotNull final DefaultMutableTreeNode root, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final SystemIcons systemIcons) {
        this.archetypeSet = archetypeSet;
        this.root = root;
        this.faceObjectProviders = faceObjectProviders;
        this.systemIcons = systemIcons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        setFont(getFont().deriveFont(Font.PLAIN));
        
        panel = new JPanel(new BorderLayout());
        
        if (node.isRoot()) {
            setForeground(Color.gray);
            setIcon(null);
        } else {
            final TreasureObj content = ((TreasureTreeNode) value).getTreasureObj();
            content.visit(treasureObjVisitor);

            if (node.getParent() == root) {
                if (!(content instanceof FolderTreasureObj)) {
                    setFont(getFont().deriveFont(Font.BOLD));
                }
            } else {
                final TreasureTreeNode parent = (TreasureTreeNode) node.getParent();
                if (parent.getTreasureObj() instanceof FolderTreasureObj) {
                    setFont(getFont().deriveFont(Font.BOLD));
                }
            }
        }
         panel.add(this,BorderLayout.SOUTH);
       return panel;

       // return this;
    }

}
