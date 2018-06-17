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

package net.sf.gridarta.gui.panel.gameobjectattributes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.FaceSource;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The "Arch" tab in the archetype chooser. It display information about the
 * currently selected object.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ArchTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObjectAttributesTab<G, A, R> {

    /**
     * The action builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The archetype type set.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The model to track.
     */
    @NotNull
    private final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel;

    /**
     * The input field containing the game object's name.
     */
    private final JTextComponent archNameField = new JTextField(14);

    /**
     * The label containing the game object's face name.
     */
    private final JLabel archFaceText = new JLabel();

    /**
     * The label containing information about the game object's type.
     */
    private final JLabel archTypeText = new JLabel();

    /**
     * The label containing the location of the game object on the map.
     */
    private final JLabel archMapPos = new JLabel();

    /**
     * The {@link ButtonGroup} for the direction buttons.
     * @see #directionButtons
     */
    private final ButtonGroup directionButtonGroup = new ButtonGroup();

    /**
     * The buttons for the direction panel.
     * @see #directionButtonGroup
     */
    private final JToggleButton[] directionButtons = new JToggleButton[9];

    /**
     * The content panel.
     */
    @NotNull
    private final JPanel panel = new JPanel();

    /**
     * The initial value of the {@link #archNameField}.
     */
    @NotNull
    private String defaultArchNameFieldValue = "";

    /**
     * Creates a new instance.
     * @param archetypeTypeSet the archetype type set
     * @param gameObjectAttributesModel the model to track
     */
    public ArchTab(@NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel) {
        super(gameObjectAttributesModel);
        this.archetypeTypeSet = archetypeTypeSet;
        this.gameObjectAttributesModel = gameObjectAttributesModel;

        final GridBagLayout gridBagLayout = new GridBagLayout();
        final GridBagConstraints gbc = new GridBagConstraints();

        panel.setLayout(gridBagLayout);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(4, 4, 0, 0);

        setArchNameField("", Color.black);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridBagLayout.setConstraints(archNameField, gbc);
        panel.add(archNameField);

        archFaceText.setText("");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridBagLayout.setConstraints(archFaceText, gbc);
        panel.add(archFaceText);

        archTypeText.setText("");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridBagLayout.setConstraints(archTypeText, gbc);
        panel.add(archTypeText);

        archMapPos.setText("");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gridBagLayout.setConstraints(archMapPos, gbc);
        panel.add(archMapPos);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 5;
        gbc.weightx = 0.0;
        final Component dirPanel = createDirPanel();
        panel.add(dirPanel, gbc);

        addAutoApply(archNameField);
        refresh(gameObjectAttributesModel.getSelectedGameObject());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Arch";
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Creates the panel consisting of the direction buttons.
     * @return the panel
     */
    private Component createDirPanel() {
        final Container dirPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 2, 2, 2);

        createButton(7, dirPanel, gbc, 0, 0);
        createButton(8, dirPanel, gbc, 1, 0);
        createButton(1, dirPanel, gbc, 2, 0);
        createButton(6, dirPanel, gbc, 0, 1);
        createButton(0, dirPanel, gbc, 1, 1);
        createButton(2, dirPanel, gbc, 2, 1);
        createButton(5, dirPanel, gbc, 0, 2);
        createButton(4, dirPanel, gbc, 1, 2);
        createButton(3, dirPanel, gbc, 2, 2);

        return dirPanel;
    }

    /**
     * Creates a direction button.
     * @param direction the direction for the button
     * @param panel the panel to add the button to
     * @param gbc the GridBagConstraints to modify
     * @param x the x-position of the button
     * @param y the y-position of the button
     */
    private void createButton(final int direction, final Container panel, final GridBagConstraints gbc, final int x, final int y) {
        final JToggleButton button = new JToggleButton(ACTION_BUILDER.createAction(false, "direction" + direction, this));
        directionButtonGroup.add(button);
        button.setFocusable(false);
        button.setEnabled(false);
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(button, gbc);
        directionButtons[direction] = button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canApply() {
        return !archNameField.getText().equals(defaultArchNameFieldValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        archNameField.requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void apply(@NotNull final G gameObject) {
        gameObject.setAttributeString(BaseObject.NAME, archNameField.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void refresh(@Nullable final G gameObject) {
        //noinspection VariableNotUsedInsideIf
        setTabSeverity(gameObject == null ? Severity.DEFAULT : Severity.MODIFIED);

        // archNameField
        if (gameObject == null) {
            setArchNameField("", Color.black);
        } else {
            final String objName = gameObject.getAttributeString(BaseObject.NAME, false);
            if (objName.length() > 0) {
                setArchNameField(objName, Color.blue);
            } else {
                final String archObjName = gameObject.getArchetype().getObjName();
                setArchNameField(archObjName.length() > 0 ? archObjName : gameObject.getArchetype().getArchetypeName(), Color.black);
            }
        }

        // archFaceText
        if (gameObject == null) {
            archFaceText.setText("Image:");
        } else {
            final StringBuilder faceText = new StringBuilder("Image: ");
            appendFaceSource(faceText, gameObject);
            archFaceText.setText(faceText.toString());
        }

        // archTypeText
        if (gameObject == null) {
            archTypeText.setText("Type:");
        } else {
            final StringBuilder typeText = new StringBuilder("Type: ");
            typeText.append(archetypeTypeSet.getDisplayName(gameObject));
            typeText.append(" [").append(gameObject.getArchetype().getArchetypeName()).append(']');
            if (gameObject.isMulti()) {
                typeText.append(" [").append(gameObject.getSizeX()).append('x').append(gameObject.getSizeY()).append(']');
            } else {
                typeText.append(" [single]");
            }
            archTypeText.setText(typeText.toString());
        }

        // archMapPos
        if (gameObject == null) {
            archMapPos.setText("Status: ");
        } else {
            final StringBuilder specialText = new StringBuilder("Status:");
            if (!gameObject.isInContainer()) {
                specialText.append(" (map: ").append(gameObject.getMapX()).append(", ").append(gameObject.getMapY()).append(')');
            }
            if (gameObject.isScripted()) {
                specialText.append(" (script)");
            }
            if (gameObject.getMsgText() != null || gameObject.getArchetype().getMsgText() != null) {
                specialText.append(" (msg)");
            }
            final int invObjects = gameObject.countInvObjects();
            if (invObjects > 0) {
                specialText.append(" (inv: ").append(invObjects).append(')');
            }
            final BaseObject<G, A, R, ?> cont = gameObject.getContainerGameObject();
            if (cont != null) {
                specialText.append(" (env: ").append(cont.getArchetype().getArchetypeName()).append(')');
            }
            archMapPos.setText(specialText.toString());
        }

        // directions
        for (final Component button : directionButtons) {
            button.setBackground(null);
        }
        final boolean enableDirButtons = gameObject != null && gameObject.usesDirection();
        for (final Component button : directionButtons) {
            button.setEnabled(enableDirButtons);
        }
        final int direction = enableDirButtons ? gameObject.getDirection() : 0;
        directionButtons[direction < directionButtons.length ? direction : 0].setSelected(true);
    }

    /**
     * Appends a description of a game object's face to a {@link
     * StringBuilder}.
     * @param sb the string builder
     * @param gameObject the game object
     */
    private static void appendFaceSource(@NotNull final StringBuilder sb, @NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        final String faceName = gameObject.getFaceObjName();
        if (faceName != null) {
            final FaceSource faceSource = gameObject.getFaceObjSource();
            sb.append(faceName).append(" (");
            switch (faceSource) {
            case FACE_NOT_FOUND:
                sb.append("face not found");
                break;

            case FACE:
                sb.append("face");
                break;

            case ARCHETYPE_FACE:
                sb.append("archetype face");
                break;

            case ANIM:
                sb.append("anim");
                break;

            case ARCHETYPE_ANIM:
                sb.append("archetype anim");
                break;
            }
            sb.append(')');
        } else {
            sb.append(">no face<");
        }
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction0() {
        direction(0);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction1() {
        direction(1);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction2() {
        direction(2);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction3() {
        direction(3);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction4() {
        direction(4);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction5() {
        direction(5);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction6() {
        direction(6);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction7() {
        direction(7);
    }

    /**
     * Action method for direction.
     */
    @ActionMethod
    public void direction8() {
        direction(8);
    }

    /**
     * Action proxy for direction.
     * @param direction the direction number
     */
    private void direction(final int direction) {
        final GameObject<G, A, R> selectedGameObject = gameObjectAttributesModel.getSelectedGameObject();
        if (selectedGameObject == null) {
            return;
        }

        final MapSquare<G, A, R> mapSquare = selectedGameObject.getMapSquare();
        if (mapSquare == null) {
            return;
        }

        final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
        mapModel.beginTransaction("Change object attributes");
        try {
            selectedGameObject.setAttributeInt(BaseObject.DIRECTION, direction);
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Updates the value of the {@link #archNameField}.
     * @param objName the value to set
     * @param color the color to set
     */
    private void setArchNameField(@NotNull final String objName, @NotNull final Color color) {
        archNameField.setForeground(color);
        archNameField.setText(objName);
        defaultArchNameFieldValue = objName;
    }

}
