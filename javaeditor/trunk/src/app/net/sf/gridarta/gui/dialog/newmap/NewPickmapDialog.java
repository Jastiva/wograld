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

package net.sf.gridarta.gui.dialog.newmap;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.mapfiles.DuplicatePickmapException;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.panel.pickmapchooser.PickmapChooserControl;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog to create a new pickmap file.
 * @author Andreas Kirschbaum
 */
public class NewPickmapDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapsizeNewMapDialog<G, A, R> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The object chooser instance to use.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The pickmap chooser control.
     */
    @NotNull
    private final PickmapChooserControl<G, A, R> pickmapChooserControl;

    /**
     * Textfield for the name of the map. The filename of the new map is unset
     * and will be specified when the user saves the map for the first time.
     */
    @NotNull
    private final JTextComponent pickmapNameField = new JTextField(16);

    /**
     * Creates a "new pickmap" dialog.
     * @param objectChooser the object chooser instance to use
     * @param parentComponent the parent component of this dialog
     * @param defaultWidth the default width for new maps
     * @param defaultHeight the default height for new maps
     * @param pickmapChooserControl the pickmap chooser control
     */
    public NewPickmapDialog(@NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final Component parentComponent, final int defaultWidth, final int defaultHeight, @NotNull final PickmapChooserControl<G, A, R> pickmapChooserControl) {
        super(null, null, defaultWidth, defaultHeight);
        this.objectChooser = objectChooser;
        this.pickmapChooserControl = pickmapChooserControl;
        init1(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "newPickmap.title"));
        init2();
        addDocumentListener(pickmapNameField);
        updateOkButton();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected JPanel createMapNamePanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "newMapPickmapName"));
        panel.add(pickmapNameField);
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean createNew() {
        final Size2D mapSize = getMapSize();
        if (mapSize == null) {
            return false;
        }

        final String pickmapName = getPickmapName();
        if (pickmapName == null) {
            return false;
        }

        try {
            pickmapChooserControl.newPickmap(mapSize, pickmapName);
        } catch (final DuplicatePickmapException ex) {
            ACTION_BUILDER.showMessageDialog(this, "pickmapExists", pickmapName);
            return false;
        } catch (final IOException ex) {
            ACTION_BUILDER.showMessageDialog(this, "pickmapIOError", pickmapName, ex.getMessage());
            return false;
        }

        objectChooser.movePickmapChooserToFront();
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isOkButtonEnabled() {
        return super.isOkButtonEnabled() && getMapWidth() >= 0 && getMapHeight() >= 0 && getPickmapName() != null;
    }

    /**
     * Returns the current pickmap name value.
     * @return the pickmap name or <code>null</code> if unset or invalid
     */
    @Nullable
    private String getPickmapName() {
        final String folderName = pickmapNameField.getText();
        if (folderName.length() <= 0) {
            return null;
        }

        return folderName;
    }

}
