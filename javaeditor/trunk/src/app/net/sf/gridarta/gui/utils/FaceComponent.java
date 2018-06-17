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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
// import javax.swing.JLabel;
import java.awt.BorderLayout;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
// import net.sf.gridarta.gui.dialog.gameobjectattributes.GameObjectAttributesDialog;

/**
 * A {@link JPanel} that allows the user to select a face name.
 * @author Andreas Kirschbaum
 */
public class FaceComponent extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link JTextField} that displays the face name.
     */
    @NotNull
    private final JTextField textField = new JTextField();

    /**
     * The associated {@link FaceTreeChooseAction}.
     */
    @NotNull
    private final FaceTreeChooseAction faceTreeChooseAction;
    
    private final JPanel label;
    
    private JPanel label2 = null;

    /**
     * Creates a new instance.
     * @param faceName the initial face name
     * @param faceObjects the face objects to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param noFaceSquareIcon the image icon for no animations
     * @param unknownSquareIcon the image icon for undefined animations
     */
    public FaceComponent(@NotNull final String faceName, @NotNull final FaceObjects faceObjects, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final ImageIcon noFaceSquareIcon, @NotNull final ImageIcon unknownSquareIcon, @Nullable JPanel label2) {
        super(new GridBagLayout());

        textField.setText(faceName);
        textField.setColumns(8);
      //  final AbstractButton label = new JButton();
     //   final AbstractButton label2 = new JButton();
   //     final JPanel label = new JPanel(new BorderLayout());
        label = new JPanel(new BorderLayout());
        
        if(label2 != null){
        this.label2 = label2;
        }
        
       
      //    final JLabel label = new JLabel();
   //     label.setMargin(new Insets(1, 0, 0, 0));
    //    label2.setMargin(new Insets(0,0,1,0));
     //   faceTreeChooseAction = new FaceTreeChooseAction("...", textField, label, faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon);
     //   faceTreeChooseAction = new FaceTreeChooseAction("...", textField, label, label2,faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon);
     //   faceTreeChooseAction = new FaceTreeChooseAction("...", textField, label, faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon, dia);
        faceTreeChooseAction = new FaceTreeChooseAction("...", textField, label, faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon, this.label2);
      // ?  label.addActionListener(faceTreeChooseAction);
  //      label2.addActionListener(faceTreeChooseAction);
        final AbstractButton button = new JButton(faceTreeChooseAction);
        // final AbstractButton button2 = new JButton(faceTreeChooseAction);
        button.setMargin(new Insets(0, 1, 0, 1));
     //    button.setMargin(new Insets(1, 1, 0, 1));
    //    button2.setMargin(new Insets(0, 1, 1, 1));
        textField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(final FocusEvent e) {
                // ignore
            }

            @Override
            public void focusLost(final FocusEvent e) {
                faceTreeChooseAction.updateIconLabel();
            }
        });

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
       
        gbc.ipadx = 2;

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
     //   add(label2, gbc);
       
     //   gbc.gridy = 1; 
     //   gbc.weightx = 0.0;
      
     add(label, gbc);
     //   gbc.gridy = 0;
         gbc.gridx = 1;  // GridBagConstraints.RELATIVE;
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(textField, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridx=2;
        add(button, gbc);
        
     //   retrieve = new JPanel(new BorderLayout());
     //   add(button2, gbc);
    //       JPanel panel = new JPanel(new BorderLayout());  
   //    JLabel topIcon = new JLabel("");  
    //   JLabel bottomIcon = new JLabel("");  
    //  panel.add(topIcon, BorderLayout.NORTH);  
     //   panel.add(bottomIcon, BorderLayout.SOUTH);
       
    //   topIcon.setIcon(faceTreeChooseAction.getTopIcon());
    //   bottomIcon.setIcon(faceTreeChooseAction.getBottomIcon());
    
       
       // icon.add(panel);
       
    }

    /**
     * Returns the current face name.
     * @return the current face name
     */
    @NotNull
    public String getFaceName() {
        return textField.getText();
    }

    /**
     * Sets the current face name.
     * @param faceName the face name
     */
    public void setFaceName(@NotNull final String faceName) {
        textField.setText(faceName);
        faceTreeChooseAction.updateIconLabel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        faceTreeChooseAction.setEnabled(enabled);
    }

    /**
     * Returns the input field component.
     * @return the input field component
     */
    @NotNull
    public Component getInputComponent() {
        return textField;
    }
    
  //  public JPanel getPanel() {
   //     return label;
        // not good OOP to pass in this way?
 //   }
    
   // public Icon getBottomIcon(){
    //    if(faceTreeChooseAction != null){
    //    return faceTreeChooseAction.getBottomIcon();
    //    } else {
    //        return null;
    //    }
  //  }
    
    
   // public Icon getTopIcon(){
   //     if(faceTreeChooseAction != null){
    //    return faceTreeChooseAction.getTopIcon();
   //     } else {
    //        return null;
  //      }
  //  }
    
    public void modifyFacePanel(){
        if(faceTreeChooseAction != null){
            if(label2 != null){
                faceTreeChooseAction.modifyFacePanel();
            }
        }
    }

}
