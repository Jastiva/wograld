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
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.data.NamedObjectsUtils;
import net.sf.gridarta.model.data.NamedObject;
import net.sf.gridarta.model.data.NamedObjects;
import net.sf.gridarta.model.face.FaceObjectProviders;
import org.jetbrains.annotations.NotNull;

/**
 * Action for choosing a face or animation.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class TreeChooseAction extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link JTextComponent} that holds the current face/animation name.
     */
    @NotNull
    private final JTextComponent textComponent;

    /**
     * The {@link NamedObjects} providing the face/animation names tree.
     */
    @NotNull
    private final NamedObjects<? extends NamedObject> namedObjects;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Create a TreeChooseAction.
     * @param text the text for the button
     * @param textComponent the text component that holds the current
     * face/animation name
     * @param namedObjects the named objects providing the face/animation names
     * tree
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public TreeChooseAction(@NotNull final String text, @NotNull final JTextComponent textComponent, @NotNull final NamedObjects<? extends NamedObject> namedObjects, @NotNull final FaceObjectProviders faceObjectProviders) {
        super(text);
        this.textComponent = textComponent;
        this.namedObjects = namedObjects;
        this.faceObjectProviders = faceObjectProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        String initial = textComponent.getText();
        final NamedObject selected = namedObjects.get(initial);
        if (selected != null) {
            initial = selected.getPath();
        }
        final String newValue = NamedObjectsUtils.showNodeChooserDialog(textComponent, initial, faceObjectProviders, namedObjects);
        if (newValue != null) {
            textComponent.setText(newValue);
        }
    }

    /**
     * Returns the current face name.
     * @return the current face name
     */
    @NotNull
    public String getFaceName() {
        return textComponent.getText();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

}
