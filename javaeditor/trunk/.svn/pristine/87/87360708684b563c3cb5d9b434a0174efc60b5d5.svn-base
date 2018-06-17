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

import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract base class implementing a {@link AbstractNewMapDialog} supporting
 * map size input fields.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapsizeNewMapDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractNewMapDialog<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The preference key for storing the map width; may be <code>null</code>.
     */
    @Nullable
    private final String widthKey;

    /**
     * The preference key for storing the map height; may be <code>null</code>.
     */
    @Nullable
    private final String heightKey;

    /**
     * The default width for new maps.
     */
    private final int defaultWidth;

    /**
     * The default height for new maps.
     */
    private final int defaultHeight;

    /**
     * Textfield for the width of the new map.
     */
    @NotNull
    private final JTextField mapWidthField = new JTextField();

    /**
     * Textfield for the height of the new map.
     */
    @NotNull
    private final JTextField mapHeightField = new JTextField();

    /**
     * Creates a new instance.
     * @param widthKey the preference key for storing the map width; may be
     * <code>null</code>
     * @param heightKey the preference key for storing the map height; may be
     * <code>null</code>
     * @param defaultWidth the default width for new maps
     * @param defaultHeight the default height for new maps
     */
    protected AbstractMapsizeNewMapDialog(@Nullable final String widthKey, @Nullable final String heightKey, final int defaultWidth, final int defaultHeight) {
        this.widthKey = widthKey;
        this.heightKey = heightKey;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        addDocumentListener(mapWidthField);
        addDocumentListener(mapHeightField);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields(@NotNull final JPanel panel, @NotNull final GridBagConstraints gbcLabel, @NotNull final GridBagConstraints gbcField) {
        mapWidthField.setText(Integer.toString(widthKey == null ? defaultWidth : preferences.getInt(widthKey, defaultWidth)));
        mapWidthField.setColumns(3);
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapWidth"), gbcLabel);
        panel.add(mapWidthField, gbcField);
        mapWidthField.selectAll();

        mapHeightField.setText(Integer.toString(heightKey == null ? defaultHeight : preferences.getInt(heightKey, defaultHeight)));
        mapHeightField.setColumns(3);
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapHeight"), gbcLabel);
        panel.add(mapHeightField, gbcField);
        mapHeightField.selectAll();
    }

    /**
     * Validate the map size fields and return the result.
     * @return whether the map size if the fields are valid or else
     *         <code>null</code>
     */
    @Nullable
    protected Size2D getMapSize() {
        final int width = getMapWidth();
        if (width < 0) {
            return null;
        }

        final int height = getMapHeight();
        if (height < 0) {
            return null;
        }

        if (widthKey != null) {
            preferences.putInt(widthKey, width);
        }
        if (heightKey != null) {
            preferences.putInt(heightKey, height);
        }

        return new Size2D(width, height);
    }

    /**
     * Returns the current map width value.
     * @return the map width or <code>-1</code> if unset or invalid
     */
    protected int getMapWidth() {
        final String text = mapWidthField.getText();
        final int width = NumberUtils.parseInt(text);
        return width < 1 ? -1 : width;

    }

    /**
     * Returns the current map height value.
     * @return the map height or <code>-1</code> if unset or invalid
     */
    protected int getMapHeight() {
        final String text = mapHeightField.getText();
        final int height = NumberUtils.parseInt(text);
        return height < 1 ? -1 : height;

    }

    /**
     * Enables or disables the map size input fields.
     * @param enabled whether to enable the fields
     */
    protected void setMapSizeEnabled(final boolean enabled) {
        mapWidthField.setEnabled(enabled);
        mapHeightField.setEnabled(enabled);
    }

}
