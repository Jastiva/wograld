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

package net.sf.gridarta.gui.map.maptilepane;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.actions.AttachTiledMaps;
import net.sf.gridarta.actions.CannotLoadMapFileException;
import net.sf.gridarta.actions.CannotSaveMapFileException;
import net.sf.gridarta.actions.InvalidPathNameException;
import net.sf.gridarta.actions.MapSizeMismatchException;
import net.sf.gridarta.gui.utils.DirectionLayout;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.tiles.MapLink;
import net.sf.gridarta.model.tiles.TileLink;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * A Panel for managing the tiling of maps. It creates a Panel for a map where
 * the user can manage the tile paths. He can choose files to tile, attach
 * tiles, clear all or individual tile paths and switch between relative and
 * absolute paths.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @noinspection AbstractClassExtendsConcreteClass
 */
public abstract class AbstractMapTilePane<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

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
     * The global settings instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The map in context.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The MapArchObject to show.
     */
    @NotNull
    private final MapArchObject<A> mapArchObject;

    /**
     * The tile links for the attach map algorithm.
     */
    @NotNull
    private final TileLink[] tileLinks;

    /**
     * Indices of next focus.
     */
    @NotNull
    private final int[] nextFocus;

    /**
     * Swing FileFilter for map files.
     */
    @NotNull
    private final FileFilter mapFileFilter;

    /**
     * The JTextFields with the tile paths.
     */
    @NotNull
    private final MapTilePanel[] tilePaths;

    @NotNull
    private final AttachTiledMaps<G, A, R> attachTiledMaps;

    /**
     * Whether "attach maps" function is available.
     */
    private final boolean canAttachMaps;

    /**
     * Create an AbstractMapTilePane.
     * @param mapManager the map manager to use
     * @param globalSettings the global settings instance
     * @param mapModel the map that's tiles are to be viewed / controlled
     * @param tileLink the tile links used for the attach map algorithm
     * @param directionMapping maps direction to direction layout direction
     * @param nextFocus indices of next focus
     * @param mapFileFilter the file filter for map files
     * @param mapPathNormalizer the map path normalizer for converting map paths
     * to files
     */
    protected AbstractMapTilePane(@NotNull final MapManager<G, A, R> mapManager, @NotNull final GlobalSettings globalSettings, @NotNull final MapModel<G, A, R> mapModel, @NotNull final MapLink[][] tileLink, @NotNull final Direction[] directionMapping, @NotNull final int[] nextFocus, @NotNull final FileFilter mapFileFilter, @NotNull final MapPathNormalizer mapPathNormalizer) {
        this.globalSettings = globalSettings;
        this.mapModel = mapModel;
        this.nextFocus = nextFocus.clone();
        this.mapFileFilter = mapFileFilter;
        assert tileLink.length == 8;
        if((directionMapping.length==4)||(directionMapping.length==8)){
        tileLinks = new TileLink[] { newTileLink("mapNorth", tileLink[0], Direction.SOUTH), newTileLink("mapEast", tileLink[1], Direction.WEST), newTileLink("mapSouth", tileLink[2], Direction.NORTH), newTileLink("mapWest", tileLink[3], Direction.EAST), newTileLink("mapNorthEast", tileLink[4], Direction.SOUTH_WEST), newTileLink("mapSouthEast", tileLink[5], Direction.NORTH_WEST), newTileLink("mapSouthWest", tileLink[6], Direction.NORTH_EAST), newTileLink("mapNorthWest", tileLink[7], Direction.SOUTH_EAST), };
        } else {
        tileLinks = new TileLink[] { newTileLink("mapNorth", tileLink[0], Direction.SOUTH), newTileLink("mapEast", tileLink[1], Direction.WEST), newTileLink("mapSouth", tileLink[2], Direction.NORTH), newTileLink("mapWest", tileLink[3], Direction.EAST), newTileLink("mapAbove", tileLink[4], Direction.SOUTH_WEST), newTileLink("mapBelow", tileLink[5], Direction.NORTH_WEST), newTileLink("mapSouthWest", tileLink[6], Direction.NORTH_EAST), newTileLink("mapNorthWest", tileLink[7], Direction.SOUTH_EAST), };
        }
        attachTiledMaps = new AttachTiledMaps<G, A, R>(mapManager, tileLinks, mapPathNormalizer);
        canAttachMaps = tileLink[0].length + tileLink[1].length + tileLink[2].length + tileLink[3].length + tileLink[4].length + tileLink[5].length + tileLink[6].length + tileLink[7].length > 0;
        mapArchObject = mapModel.getMapArchObject();
        tilePaths = buildComponents(directionMapping);
    }

    /**
     * Creates a new {@link TileLink}.
     * @param key the resource key
     * @param revLink direction for reverse map linking
     * @return the new instance
     */
    @NotNull
    private static TileLink newTileLink(@NotNull final String key, @NotNull final MapLink[] mapLinks, @NotNull final Direction revLink) {
        return new TileLink(ActionBuilderUtils.getString(ACTION_BUILDER, key), mapLinks, revLink);
    }

    /**
     * Builds the components of this panel.
     * @param directionMapping maps direction to direction layout direction
     * @return the JTextFields with the tile paths
     */
    private MapTilePanel[] buildComponents(@NotNull final Direction[] directionMapping) {
        setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        final MapTilePanel[] tilePanels = createTilePanels(directionMapping.length);
        add(buildTilePanels(tilePanels, directionMapping), gbc);
        add(buildSubPanel(), gbc);
        return tilePanels;
    }

    /**
     * Builds all {@link MapTilePanel MapTilePanels} for all directions.
     * @param directions the number of directions
     * @return the tile panels
     */
    @NotNull
    private MapTilePanel[] createTilePanels(final int directions) {
        final MapTilePanel[] tilePanels = new MapTilePanel[directions];
        for (int index = 0; index < tilePanels.length; index++) {
            final File mapDir = globalSettings.getMapsDirectory();
            final File mapFile = mapModel.getMapFile();
            final TilePanel tilePanel = new TilePanel(mapFileFilter, mapArchObject.getTilePath(Direction.values()[index]), mapFile, mapDir);
            tilePanels[index] = new MapTilePanel(index, nextFocus, tilePanel, tileLinks[index].getName());
        }
        return tilePanels;
    }

    /**
     * Builds the tile panel.
     * @param tilePanels the tile panels
     * @param directionMapping the directions
     * @return the newly built tile panel
     */
    @NotNull
    private static Component buildTilePanels(@NotNull final MapTilePanel[] tilePanels, @NotNull final Direction[] directionMapping) {
        final JComponent panel = new JPanel(new DirectionLayout());
        panel.setBorder(new CompoundBorder(BorderFactory.createTitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "mapTiles")), GUIConstants.DIALOG_BORDER));
        for (int index = 0; index < tilePanels.length; index++) {
            panel.add(tilePanels[index].getTilePanel(), directionMapping[index]);
        }
        return panel;
    }

    /**
     * Creates the sub-panel that holds the buttons to attach maps or clear all
     * paths.
     * @return the newly built sub-panel
     */
    @NotNull
    private Component buildSubPanel() {
        final JComponent subPanel = new JPanel(new GridBagLayout());
        subPanel.setBorder(new CompoundBorder(BorderFactory.createTitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "mapControl")), GUIConstants.DIALOG_BORDER));
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        if (canAttachMaps) {
            gbc.anchor = GridBagConstraints.EAST;
            subPanel.add(new JButton(ACTION_BUILDER.createAction(false, "mapTilesAttach", this)), gbc);
        }
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = canAttachMaps ? GridBagConstraints.WEST : GridBagConstraints.CENTER;
        subPanel.add(new JButton(ACTION_BUILDER.createAction(false, "mapTilesClear", this)), gbc);
        return subPanel;
    }

    /**
     * Action method for tiles attaching automatically.
     */
    @ActionMethod
    public void mapTilesAttach() {
        final String[] tmpTilePaths = new String[tilePaths.length];
        for (int i = 0; i < tmpTilePaths.length; i++) {
            tmpTilePaths[i] = tilePaths[i].getTilePanel().getText();
        }
        final File mapsDirectory = globalSettings.getMapsDirectory();
        try {
            attachTiledMaps.attachTiledMaps(mapModel, tmpTilePaths, mapsDirectory, true);
        } catch (final CannotLoadMapFileException ex) {
            ACTION_BUILDER.showMessageDialog(this, "mapErrorPath2", ex.getMapPath()); // XXX: ignores ex.getMessage()
            return;
        } catch (final CannotSaveMapFileException ex) {
            ACTION_BUILDER.showMessageDialog(this, "mapErrorFatalWrite", ex.getMessage()); // XXX: ignores ex.getMapFile()
            return;
        } catch (final InvalidPathNameException ex) {
            ACTION_BUILDER.showMessageDialog(this, "mapErrorInvalid", ex.getMessage());
            return;
        } catch (final MapSizeMismatchException ex) {
            ACTION_BUILDER.showMessageDialog(this, "mapErrorDifferentSize", mapModel.getMapArchObject().getMapName(), ex.getMapSize().getWidth(), ex.getMapSize().getHeight(), ex.getMapFile(), ex.getOtherMapSize().getWidth(), ex.getOtherMapSize().getHeight());
            return;
        }
        for (int i = 0; i < tmpTilePaths.length; i++) {
            tilePaths[i].getTilePanel().setText(tmpTilePaths[i], true);
        }
    }

    /**
     * Action method for tiles clearing paths.
     */
    @ActionMethod
    public void mapTilesClear() {
        for (final MapTilePanel tilePath : tilePaths) {
            tilePath.getTilePanel().setText("", true);
        }
    }

    /**
     * Invoke this method if the dialog using this pane is confirmed with OK to
     * write the information from this pane back to the map.
     */
    public void modifyMapProperties() {
        for (int i = 0; i < tilePaths.length; i++) {
            mapArchObject.setTilePath(Direction.values()[i], tilePaths[i].getTilePanel().getText());
        }
    }

    /**
     * Restores the settings from the map.
     */
    public void restoreMapProperties() {
        for (final MapTilePanel tilePath : tilePaths) {
            tilePath.getTilePanel().mapTileRevert();
        }
    }

    /**
     * Returns one tile path.
     * @param direction the direction
     * @return the tile path
     */
    @NotNull
    public MapTilePanel getTilePath(final int direction) {
        return tilePaths[direction];
    }

}
