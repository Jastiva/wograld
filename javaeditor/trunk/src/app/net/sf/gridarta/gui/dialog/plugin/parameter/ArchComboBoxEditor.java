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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.panel.gameobjectattributes.GameObjectAttributesModel;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.CommonConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArchComboBoxEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ComboBoxEditor {

    private final ArchComboBox<G, A, R> archComboBox;

    private final ArchComboBoxModel<G, A, R> archComboBoxModel;

    private final ObjectChooser<G, A, R> objectChooser;

    private final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel;

    private Container editorPanel;

    private JLabel icon;

    private JTextComponent editor;

    private JPopupMenu popup;

    private volatile boolean locked;

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
    public ArchComboBoxEditor(final ArchComboBox<G, A, R> archComboBox, final ArchComboBoxModel<G, A, R> archComboBoxModel, final ObjectChooser<G, A, R> objectChooser, final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.archComboBox = archComboBox;
        this.archComboBoxModel = archComboBoxModel;
        this.objectChooser = objectChooser;
        this.gameObjectAttributesModel = gameObjectAttributesModel;
        this.faceObjectProviders = faceObjectProviders;
    }

    public void lockEditor() {
        locked = true;
    }

    public void unlockEditor() {
        locked = false;
    }

    private synchronized void buildPanel() {
        if (editorPanel == null) {
            editorPanel = new JPanel(new GridBagLayout());
            final GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.gridwidth = 4;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editor = new JTextField();
            editorPanel.add(editor, gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0.0;
            gbc.gridx = 1;
            final AbstractButton fromSelect = new JButton("From tile selection");
            fromSelect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final BaseObject<G, A, R, ?> gameObject = objectChooser.getSelection();
                    if (gameObject != null) {
                        final Archetype<G, A, R> ao = archComboBoxModel.getNearestMatch(gameObject.getArchetype().getArchetypeName());
                        archComboBox.setSelectedItem(ao);
                        setItem(ao);
                    }
                }
            });
            editorPanel.add(fromSelect, gbc);
            final AbstractButton fromActive = new JButton("From map selection");
            fromActive.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final BaseObject<G, A, R, ?> gameObject = gameObjectAttributesModel.getSelectedGameObject();
                    if (gameObject != null) {
                        final Archetype<G, A, R> ao = archComboBoxModel.getNearestMatch(gameObject.getArchetype().getArchetypeName());
                        archComboBox.setSelectedItem(ao);
                        setItem(ao);
                    }
                }
            });
            gbc.gridx = 2;
            editorPanel.add(fromActive, gbc);
            editor.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    //popup.setLocation(p.x, p.y+archComboBoxEditor.getHeight()+5);
                    popup.setPreferredSize(null);
                    final Dimension d = popup.getPreferredSize();
                    final Dimension p = editorPanel.getSize();
                    if (d.width < p.width) {
                        d.width = p.width;
                    }
                    if (d.height < p.height) {
                        d.height = p.height;
                    }
                    popup.setPreferredSize(d);
                    popup.show(editorPanel, 0, editorPanel.getHeight() + 5);
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    popup.setVisible(false);
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                }
            });
            editor.setEditable(true);
            editor.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    archComboBox.editorEntryChange();
                }

                @Override
                public void insertUpdate(final DocumentEvent e) {
                    archComboBox.editorEntryChange();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    archComboBox.editorEntryChange();
                }
            });
            icon = new JLabel();
            popup = new JPopupMenu();
            popup.setLayout(new FlowLayout());
            popup.setBackground(CommonConstants.BG_COLOR);
            popup.setBorder(new LineBorder(Color.black));
            popup.add(icon);
            popup.setFocusable(false);
            archComboBox.setBackground(CommonConstants.BG_COLOR);
        }
    }

    @Override
    public void selectAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public Component getEditorComponent() {
        if (editorPanel == null) {
            buildPanel();
        }
        return editorPanel;
    }

    @Override
    public void addActionListener(final ActionListener l) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeActionListener(final ActionListener l) {
        // TODO Auto-generated method stub

    }

    @Nullable
    @Override
    public Object getItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setItem(final Object anObject) {
        final BaseObject<?, ?, ?, ?> arch = (BaseObject<?, ?, ?, ?>) anObject;
        if (anObject == null) {
            icon.setIcon(null);
            icon.setText("No item selected");
        } else {
            icon.setIcon(faceObjectProviders.getFace(arch));
        }

        if (arch == null) {
            icon.setText("");
            if (!locked) {
                editor.setText("");
            }
        } else {
            if (!locked) {
                editor.setText(arch.getArchetype().getArchetypeName());
            }
            icon.setText(arch.getArchetype().getArchetypeName());
        }
    }

    public JTextComponent getEditor() {
        return editor;
    }

}
