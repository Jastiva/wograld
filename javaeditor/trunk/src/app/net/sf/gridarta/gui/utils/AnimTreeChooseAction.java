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

package net.sf.gridarta.gui.utils;

import java.awt.event.ActionEvent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
 import javax.swing.JPanel;
 import java.awt.BorderLayout;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.data.NamedObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link TreeChooseAction} that updates an animation label.
 * @author Andreas Kirschbaum
 */
public class AnimTreeChooseAction extends TreeChooseAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The button showing the icon.
     */
   // @NotNull
  //  private final AbstractButton icon;
    
     private final JPanel icon;
     private JPanel icon2 = null;
    
       private final JLabel bottomIcon = new JLabel("");
        private final JLabel topIcon = new JLabel("");  
           private final JLabel bottomIcon2 = new JLabel("");
        private final JLabel topIcon2 = new JLabel("");  
        
       
        
        private Icon face;
        private Icon face2;
        private Icon face3;
        private Icon face4;

    /**
     * The {@link AnimationObjects} to select from.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link ImageIcon} for no animations.
     */
    @NotNull
    private final ImageIcon noFaceSquareIcon;

    /**
     * The {@link ImageIcon} for undefined animations.
     */
    @NotNull
    private final ImageIcon unknownSquareIcon;

    /**
     * Create a TreeChooseAction.
     * @param text the text for the button
     * @param textComponent the text component that holds the current animation
     * name
     * @param icon the label showing the icon
     * @param animationObjects the animation objects providing the animation
     * names tree
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param noFaceSquareIcon the image icon for no animations
     * @param unknownSquareIcon the image icon for undefined animations
     */
    public AnimTreeChooseAction(@NotNull final String text, @NotNull final JTextComponent textComponent, @NotNull final JPanel icon, @NotNull final AnimationObjects animationObjects, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final ImageIcon noFaceSquareIcon, @NotNull final ImageIcon unknownSquareIcon, @Nullable final JPanel icon2) {
        super(text, textComponent, animationObjects, faceObjectProviders);
        this.icon = icon;
         if(icon2 != null){
      this.icon2=icon2;
        }
        this.animationObjects = animationObjects;
        this.faceObjectProviders = faceObjectProviders;
        this.unknownSquareIcon = unknownSquareIcon;
        this.noFaceSquareIcon = noFaceSquareIcon;
        
           icon.add(topIcon, BorderLayout.NORTH);
       icon.add(bottomIcon,BorderLayout.SOUTH);
       if(icon2 != null){
           icon2.add(topIcon2, BorderLayout.NORTH);
       icon2.add(bottomIcon2,BorderLayout.SOUTH);
       }

        final FaceObjectProvidersListener faceObjectProvidersListener = new FaceObjectProvidersListener() {

            @Override
            public void facesReloaded() {
                updateIconLabel();
            }

        };
        faceObjectProviders.addFaceObjectProvidersListener(faceObjectProvidersListener);

        updateIconLabel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        super.actionPerformed(e);
        updateIconLabel();
    }

    /**
     * Updates the icon of {@link #icon} to reflect the current animation name.
     */
    public final void updateIconLabel() {
        final String animationName = getFaceName();
       
        if (animationName.isEmpty() || animationName.equals("NONE")) {
            face = noFaceSquareIcon;
             face3 = noFaceSquareIcon;
        } else {
            final NamedObject animationObject = animationObjects.get(animationName);
            if (animationObject == null) {
                face = unknownSquareIcon;
                face3 = unknownSquareIcon;
            } else {
                final Icon tmp = faceObjectProviders.getDisplayIcon(animationObject);
                face = tmp == null ? unknownSquareIcon : tmp;
                 face3 = tmp == null ? unknownSquareIcon : tmp;
            }
        }
     
        if (animationName.isEmpty() || animationName.equals("NONE")) {
            face2 = noFaceSquareIcon;
            face4 = noFaceSquareIcon;
        } else {
       
            final NamedObject animationObject = animationObjects.get(animationName);
            if (animationObject == null) {
                face2 = unknownSquareIcon;
                face4 = unknownSquareIcon;
            } else {
                final Icon tmp2 = faceObjectProviders.getSecondDisplayIcon(animationObject);
                face2 = tmp2 == null ? unknownSquareIcon : tmp2;
                face4 = tmp2 == null ? unknownSquareIcon : tmp2;
            }
            
        }
        
        
     //
        //    final Icon tmp = faceObjectProviders.getImageIconForFacename(faceName);
         //   final Icon tmp2 = faceObjectProviders.getSecondImageForFacename(faceName);
        
     
       topIcon.setIcon(face2);
       bottomIcon.setIcon(face);
   
       icon.repaint();
    
       if(icon2 != null){
           if(topIcon2 != null){
           topIcon2.setIcon(face4);
           }
           if(bottomIcon2 != null){
       bottomIcon2.setIcon(face3);
           }
  
       icon2.repaint(); 
       }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected Object clone() {
        return super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean newValue) {
        super.setEnabled(newValue);
        updateIconLabel();
        icon.setEnabled(newValue);
    }

}
