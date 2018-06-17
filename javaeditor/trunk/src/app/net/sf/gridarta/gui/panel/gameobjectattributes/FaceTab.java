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

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.utils.AnimationComponent;
import net.sf.gridarta.gui.utils.DirectionComponent;
import net.sf.gridarta.gui.utils.FaceComponent;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.data.NamedObjects;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The "Face" tab in the archetype chooser. It display information about the
 * face and animation of the currently selected {@link GameObject}.
 * @author Andreas Kirschbaum
 */
public class FaceTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObjectAttributesTab<G, A, R> {

    /**
     * The {@link FaceObjects} for looking up faces.
     */
    @NotNull
    private final NamedObjects<?> faceObjects;

    /**
     * The {@link AnimationObjects} for looking up animations.
     */
    @NotNull
    private final NamedObjects<?> animationObjects;

    /**
     * The {@link FaceComponent} for selecting the game object's face.
     */
    @NotNull
    private final FaceComponent faceComponent;

    /**
     * The {@link FaceComponent} for selecting the game object's animation.
     */
    @NotNull
    private final AnimationComponent animationComponent;

    /**
     * The {@link JCheckBox} for selecting whether the game object is animated.
     */
    @NotNull
    private final AbstractButton animatedCheckBox = new JCheckBox("is animated");

    /**
     * The {@link JTextField} for selecting the animation speed.
     */
    @NotNull
    private final JTextComponent animSpeedTextField = new JTextField();

    /**
     * The content panel.
     */
    @NotNull
    private final JPanel panel = new JPanel();

    /**
     * The {@link DirectionComponent} for selecting the game object's
     * direction.
     */
    @NotNull
    private final DirectionComponent directionComponent = new DirectionComponent(false) {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        protected void direction(@Nullable final Integer direction) {
            final int dir = direction == null ? 0 : direction;
            final GameObject<?, ?, ?> gameObject = getSelectedGameObject();
            if (gameObject != null) {
                final MapSquare<?, ?, ?> mapSquare = gameObject.getMapSquare();
                if (mapSquare != null) {
                    final MapModel<?, ?, ?> mapModel = mapSquare.getMapModel();
                    mapModel.beginTransaction("Change object attributes");
                    try {
                        gameObject.setAttributeInt(BaseObject.DIRECTION, dir);
                    } finally {
                        mapModel.endTransaction();
                    }
                }
            }
        }

    };

    /**
     * The default value of {@link #faceComponent}.
     */
    @NotNull
    private String defaultFaceComponentValue = "";

    /**
     * The default value of {@link #animationComponent}.
     */
    @NotNull
    private String defaultAnimComponentValue = "";

    /**
     * The default value of {@link #animatedCheckBox}.
     */
    private boolean defaultAnimatedCheckBoxValue;

    /**
     * The default value of {@link #animSpeedTextField}.
     */
    @NotNull
    private String defaultAnimSpeedTextFieldValue = "";

    /**
     * Creates a new instance.
     * @param gameObjectAttributesModel the model to track
     * @param faceObjects the face objects for looking up faces
     * @param faceObjectProviders the face object providers to use
     * @param animationObjects the animation objects for looking up animations
     * @param noFaceSquareIcon the image icon for no animations
     * @param unknownSquareIcon the image icon for undefined animations
     */
    public FaceTab(@NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel, @NotNull final FaceObjects faceObjects, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final ImageIcon noFaceSquareIcon, @NotNull final ImageIcon unknownSquareIcon) {
        super(gameObjectAttributesModel);
        this.faceObjects = faceObjects;
        this.animationObjects = animationObjects;
        faceComponent = new FaceComponent("", faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon, null);
        animationComponent = new AnimationComponent("", animationObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon,null);

        final GridBagLayout gridBagLayout = new GridBagLayout();
        final GridBagConstraints gbc = new GridBagConstraints();

        panel.setLayout(gridBagLayout);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        final Component facePanel = createFacePanel();
        gridBagLayout.setConstraints(facePanel, gbc);
        panel.add(facePanel);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        final Component animationPanel = createAnimationPanel();
        gridBagLayout.setConstraints(animationPanel, gbc);
        panel.add(animationPanel);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        final Component directionPanel = createDirectionPanel();
        gridBagLayout.setConstraints(directionPanel, gbc);
        panel.add(directionPanel);

        addAutoApply(faceComponent.getInputComponent());
        addAutoApply(animationComponent.getInputComponent());
        addAutoApply(animatedCheckBox);
        addAutoApply(animSpeedTextField);
        addAutoApply(directionComponent);
        refresh(gameObjectAttributesModel.getSelectedGameObject());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void refresh(@Nullable final G gameObject) {
        Severity severity = Severity.DEFAULT;

        final String faceName;
        if (gameObject == null) {
            faceName = "";
        } else {
            final String tmpFaceName = gameObject.getFaceName();
            if (tmpFaceName != null) {
                faceName = normalizeFace(tmpFaceName);
                if (severity == Severity.DEFAULT) {
                    severity = Severity.MODIFIED;
                }
            } else {
                faceName = normalizeFace(gameObject.getArchetype().getFaceName());
            }
            if (!faceName.equals("NONE") && !faceObjects.containsKey(faceName)) {
                severity = Severity.ERROR;
            }
        }

        final String animName;
        if (gameObject == null) {
            animName = "";
        } else {
            final String tmpAnimName = gameObject.getAnimName();
            if (tmpAnimName != null) {
                animName = normalizeFace(tmpAnimName);
                if (severity == Severity.DEFAULT) {
                    severity = Severity.MODIFIED;
                }
            } else {
                animName = normalizeFace(gameObject.getArchetype().getAnimName());
            }
            if (!animName.equals("NONE") && !animationObjects.containsKey(animName)) {
                severity = Severity.ERROR;
            }
        }

        final boolean animated;
        if (gameObject == null) {
            animated = false;
        } else {
            animated = gameObject.getAttributeInt(BaseObject.IS_ANIMATED) != 0;
            //noinspection UnnecessaryParentheses
            if (severity == Severity.DEFAULT && animated != (gameObject.getArchetype().getAttributeInt(BaseObject.IS_ANIMATED) != 0)) {
                severity = Severity.MODIFIED;
            }
        }

        final String animSpeed;
        if (gameObject == null) {
            animSpeed = "";
        } else {
            animSpeed = gameObject.getAttributeString(BaseObject.ANIM_SPEED);
            if (severity == Severity.DEFAULT && !gameObject.getArchetype().getAttributeString(BaseObject.ANIM_SPEED).equals(animSpeed)) {
                severity = Severity.MODIFIED;
            }
        }

        final int direction;
        if (gameObject == null || !gameObject.getArchetype().usesDirection()) {
            direction = 0;
        } else {
            direction = gameObject.getDirection();
            if (severity == Severity.DEFAULT && direction != gameObject.getArchetype().getDirection()) {
                severity = Severity.MODIFIED;
            }
        }

        setTabSeverity(severity);
        faceComponent.setEnabled(gameObject != null);
        faceComponent.setFaceName(faceName);
        defaultFaceComponentValue = faceName;
        animationComponent.setEnabled(gameObject != null);
        animationComponent.setAnimName(animName);
        defaultAnimComponentValue = animName;
        animatedCheckBox.setEnabled(gameObject != null);
        animatedCheckBox.setSelected(animated);
        defaultAnimatedCheckBoxValue = animated;
        animSpeedTextField.setEnabled(gameObject != null);
        animSpeedTextField.setText(animSpeed);
        defaultAnimSpeedTextFieldValue = animSpeed;
        directionComponent.setEnabled(gameObject != null);
        directionComponent.updateDirection(direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canApply() {
        return !faceComponent.getFaceName().equals(defaultFaceComponentValue) || !animationComponent.getAnimName().equals(defaultAnimComponentValue) || animatedCheckBox.isSelected() != defaultAnimatedCheckBoxValue || !animSpeedTextField.getText().equals(defaultAnimSpeedTextFieldValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        faceComponent.requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void apply(@NotNull final G gameObject) {
        final String faceName = normalizeFace(faceComponent.getFaceName());
        final String faceName2;
        if (faceName.equals("NONE")) {
            final String archFaceName = gameObject.getArchetype().getFaceName();
            faceName2 = archFaceName == null || archFaceName.isEmpty() ? "" : "NONE";
        } else {
            faceName2 = faceName;
        }
        gameObject.setAttributeString(BaseObject.FACE, faceName2);

        final String animName = normalizeFace(animationComponent.getAnimName());
        final String animName2;
        if (animName.equals("NONE")) {
            final String archAnimName = gameObject.getArchetype().getAnimName();
            animName2 = archAnimName == null || archAnimName.isEmpty() ? "" : "NONE";
        } else {
            animName2 = animName;
        }
        gameObject.setAttributeString(BaseObject.ANIMATION, animName2);

        final boolean isAnimated = animatedCheckBox.isSelected();
        if (isAnimated == (gameObject.getArchetype().getAttributeInt(BaseObject.IS_ANIMATED) != 0)) {
            gameObject.removeAttribute(BaseObject.IS_ANIMATED);
        } else {
            gameObject.setAttributeInt(BaseObject.IS_ANIMATED, isAnimated ? 1 : 0);
        }

        final String animSpeed = animSpeedTextField.getText().trim();
        final String animSpeedAttribute = gameObject.getArchetype().getAttributeString(BaseObject.ANIM_SPEED);
        if (animSpeed.length() == 0 || (animSpeed.equals("0") ? "" : animSpeed).equals(animSpeedAttribute)) {
            gameObject.removeAttribute(BaseObject.ANIM_SPEED);
        } else {
            gameObject.setAttributeString(BaseObject.ANIM_SPEED, animSpeed);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Face";
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
     * Creates the "Face" panel.
     * @return the face panel
     */
    @NotNull
    private Component createFacePanel() {
        final GridBagLayout gridBagLayout = new GridBagLayout();

        final JComponent facePanel = new JPanel();
        facePanel.setLayout(gridBagLayout);
        facePanel.setBorder(new TitledBorder("Face"));

        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gridBagLayout.setConstraints(faceComponent, gbc);
        facePanel.add(faceComponent);

        addFiller(gridBagLayout, facePanel, 1, 1);
        return facePanel;
    }

    /**
     * Creates the "Animation" panel.
     * @return the animation panel
     */
    @NotNull
    private Component createAnimationPanel() {
        final GridBagLayout gridBagLayout = new GridBagLayout();
        final JComponent animationPanel = new JPanel();
        animationPanel.setLayout(gridBagLayout);
        animationPanel.setBorder(new TitledBorder("Animation"));

        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gridBagLayout.setConstraints(animationComponent, gbc);
        animationPanel.add(animationComponent);

        gbc.gridy = 1;
        gridBagLayout.setConstraints(animatedCheckBox, gbc);
        animationPanel.add(animatedCheckBox);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        final Component speedLabel = new JLabel("speed:");
        gridBagLayout.setConstraints(speedLabel, gbc);
        animationPanel.add(speedLabel);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 1;
        gridBagLayout.setConstraints(animSpeedTextField, gbc);
        animationPanel.add(animSpeedTextField);

        addFiller(gridBagLayout, animationPanel, 2, 3);
        return animationPanel;
    }

    /**
     * Creates the "Direction" panel.
     * @return the direction panel
     */
    @NotNull
    private Component createDirectionPanel() {
        final GridBagLayout gridBagLayout = new GridBagLayout();

        final JComponent directionPanel = new JPanel();
        directionPanel.setLayout(gridBagLayout);
        directionPanel.setBorder(new TitledBorder("Direction"));

        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gridBagLayout.setConstraints(directionComponent, gbc);
        directionPanel.add(directionComponent);

        addFiller(gridBagLayout, directionPanel, 1, 1);
        return directionPanel;
    }

    /**
     * Adds a filler component that fills the remaining space.
     * @param gridBagLayout the grid bag layout layout to use
     * @param container the container to add the filler to
     * @param gridWidth the grid width
     * @param gridY the grid y coordinate
     */
    private static void addFiller(@NotNull final GridBagLayout gridBagLayout, @NotNull final Container container, final int gridWidth, final int gridY) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridwidth = gridWidth;
        gbc.gridy = gridY;
        final Component filler = new JPanel();
        gridBagLayout.setConstraints(filler, gbc);
        container.add(filler);
    }

    /**
     * Normalizes a face or animation name. Returns the face/animation name or
     * "NONE" for no face/animation.
     * @param value the value to normalize
     * @return the normalized name
     */
    @NotNull
    private static String normalizeFace(@Nullable final String value) {
        return value == null || value.isEmpty() ? "NONE" : value;
    }

}
