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
import java.awt.GridBagConstraints;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog to create a new map file.
 * @author Andreas Kirschbaum
 */
public class NewMapDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractMapsizeNewMapDialog<G, A, R> {

    /**
     * The key used to store the last used map width in preferences.
     */
    @NotNull
    private static final String DEFAULT_LEVEL_WIDTH_KEY = "NewMapDialog.mapWidth";

    /**
     * The key used to store the last used map height in preferences.
     */
    @NotNull
    private static final String DEFAULT_LEVEL_HEIGHT_KEY = "NewMapDialog.mapHeight";

    /**
     * The key used to store the last used difficulty in preferences.
     */
    @NotNull
    private static final String DEFAULT_DIFFICULTY_KEY = "NewMapDialog.difficulty";

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
     * The {@link MapViewsManager} to use.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The map arch object factory instance.
     */
    @NotNull
    private final MapArchObjectFactory<A> mapArchObjectFactory;

    /**
     * Whether to show the "mapSizeDefault" checkbox.
     */
    private final boolean showMapSizeDefault;

    /**
     * Whether to use the default map size.
     * @val true override default map size
     * @val false use default map size
     * @default true
     */
    private boolean mapSizeDefault;

    /**
     * Whether to show the "mapDifficulty" field.
     */
    private final boolean showMapDifficulty;

    /**
     * The default width for new maps.
     */
    private final int defaultWidth;

    /**
     * The default height for new maps.
     */
    private final int defaultHeight;

    /**
     * The default height for new maps.
     */
    private final int defaultDifficulty;

    /**
     * Textfield for the name of the map. The filename of the new map is unset
     * and will be specified when the user saves the map for the first time.
     */
    @NotNull
    private final JTextComponent mapNameField = new JTextField(16);

    /**
     * Checkbox to set whether the default width / height should be overridden.
     */
    @NotNull
    private final AbstractButton mapSizeDefaultCheckbox = new JCheckBox(ACTION_BUILDER.createToggle(false, "mapSizeDefault", this));

    /**
     * Textfield for the difficulty of the new map.
     */
    @NotNull
    private final JTextField mapDifficultyField = new JTextField();

    /**
     * Creates a "new map" dialog.
     * @param mapViewsManager the map views manager to use
     * @param mapArchObjectFactory the map arch object factory instance
     * @param parentComponent the parent component of this dialog
     * @param showMapSizeDefault whether to show the "mapSizeDefault" checkbox
     * @param showMapDifficulty whether to show the "mapDifficulty" field
     * @param defaultWidth the default width for new maps
     * @param defaultHeight the default height for new maps
     * @param defaultDifficulty the default difficulty for new maps
     */
    public NewMapDialog(@NotNull final MapViewsManager<G, A, R> mapViewsManager, @NotNull final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final Component parentComponent, final boolean showMapSizeDefault, final boolean showMapDifficulty, final int defaultWidth, final int defaultHeight, final int defaultDifficulty) {
        super(DEFAULT_LEVEL_WIDTH_KEY, DEFAULT_LEVEL_HEIGHT_KEY, defaultWidth, defaultHeight);
        this.mapViewsManager = mapViewsManager;
        this.mapArchObjectFactory = mapArchObjectFactory;
        this.showMapSizeDefault = showMapSizeDefault;
        this.showMapDifficulty = showMapDifficulty;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.defaultDifficulty = defaultDifficulty;
        mapSizeDefault = showMapSizeDefault;

        init1(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "newMap.title"));
        init2();
        mapNameField.selectAll();

        addDocumentListener(mapNameField);
        addDocumentListener(mapDifficultyField);
        updateOkButton();
    }

    @Override
    protected void init2() {
        if (showMapSizeDefault) {
            setMapSizeDefault(true);
        }
        super.init2();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected JPanel createMapNamePanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "newMapMapName"));
        panel.add(mapNameField);
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addFields(@NotNull final JPanel panel, @NotNull final GridBagConstraints gbcLabel, @NotNull final GridBagConstraints gbcField) {
        if (showMapSizeDefault) {
            panel.add(mapSizeDefaultCheckbox, gbcField);
        }
        super.addFields(panel, gbcLabel, gbcField);
        if (showMapDifficulty) {
            mapDifficultyField.setText(Integer.toString(preferences.getInt(DEFAULT_DIFFICULTY_KEY, defaultDifficulty)));
            panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapDifficulty"), gbcLabel);
            mapDifficultyField.setColumns(3);
            panel.add(mapDifficultyField, gbcField);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean createNew() {
        final A mapArchObject = mapArchObjectFactory.newMapArchObject(true);

        final Size2D mapSize = getMapSize();
        if (mapSize == null) {
            return false;
        }
        mapArchObject.setMapSize(mapSize);

        final String mapName = getMapName();
        if (mapName == null) {
            return false;
        }

        mapArchObject.setMapName(mapName);
        if (showMapDifficulty) {
            final int difficulty = getDifficulty();
            if (difficulty < 0) {
                return false;
            }
            mapArchObject.setDifficulty(difficulty);
        }

        mapViewsManager.newMapWithView(null, mapArchObject, null, null);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isOkButtonEnabled() {
        return super.isOkButtonEnabled() && (mapSizeDefault || (getMapWidth() >= 0 && getMapHeight() >= 0)) && getMapName() != null && (!showMapDifficulty || getDifficulty() >= 0);
    }

    /**
     * Returns the current map name value.
     * @return the map name or <code>null</code> if unset or invalid
     */
    @Nullable
    private String getMapName() {
        final String mapName = mapNameField.getText();
        return mapName.length() > 0 ? mapName : null;
    }

    /**
     * Returns the current difficulty value.
     * @return the difficulty or <code>-1</code> if unset or invalid
     */
    private int getDifficulty() {
        final String text = mapDifficultyField.getText();
        final int difficulty = NumberUtils.parseInt(text);
        return difficulty >= 1 ? difficulty : -1;
    }

    /**
     * Set whether to override the default map size.
     * @return mapSizeDefault <code>true</code> if the user wants to specify her
     *         own map size, otherwise <code>false</code>
     */
    public boolean isMapSizeDefault() {
        return mapSizeDefault;
    }

    /**
     * Set whether to override the default map size.
     * @param mapSizeDefault <code>true</code> if the user wants to specify her
     * own map size, otherwise <code>false</code>
     */
    public void setMapSizeDefault(final boolean mapSizeDefault) {
        mapSizeDefaultCheckbox.setSelected(mapSizeDefault);
        this.mapSizeDefault = mapSizeDefault;
        setMapSizeEnabled(!mapSizeDefault);
        updateOkButton();
    }

    /**
     * Validate the map size fields and return the result.
     * @return whether the map size if the fields are valid or else
     *         <code>null</code>
     */
    @Nullable
    @Override
    protected Size2D getMapSize() {
        return mapSizeDefault ? new Size2D(defaultWidth, defaultHeight) : super.getMapSize();
    }

}
