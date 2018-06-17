/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.var.crossfire.gui.mappropertiesdialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.dialog.help.Help;
import net.sf.gridarta.gui.map.maptilepane.AbstractMapTilePane;
import net.sf.gridarta.gui.map.maptilepane.FlatMapTilePane;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog to change the properties of a map, like several flags and settings
 * about the environment and the map tiles.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapPropertiesDialog extends JOptionPane {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The parent frame for help windows.
     * @serial
     */
    @NotNull
    private final JFrame helpParent;

    /**
     * The {@link MapModel} this dialog shows.
     * @serial
     */
    @NotNull
    private final MapModel<GameObject, MapArchObject, Archetype> mapModel;

    /**
     * The {@link Window} that represents this dialog.
     * @serial
     */
    @Nullable
    private Window dialog;

    /**
     * The {@link GridBagConstraints} for label fields before text input
     * fields.
     * @serial
     */
    @NotNull
    private final GridBagConstraints gbcLabel = new GridBagConstraints();

    /**
     * The {@link GridBagConstraints} for text input fields.
     * @serial
     */
    @NotNull
    private final GridBagConstraints gbcTextField = new GridBagConstraints();

    /**
     * The {@link GridBagConstraints} for check boxes.
     * @serial
     */
    @NotNull
    private final GridBagConstraints gbcCheckBox = new GridBagConstraints();

    /**
     * The {@link GridBagConstraints} for filling the remaining space below all
     * input fields.
     * @serial
     */
    @NotNull
    private final GridBagConstraints gbcFiller = new GridBagConstraints();

    /**
     * The message text.
     * @serial
     */
    @NotNull
    private final JTextComponent mapDescription = new JTextArea(4, 4);

    /**
     * The map lore.
     * @serial
     */
    @NotNull
    private final JTextComponent mapLore = new JTextArea();

    /**
     * The map's name.
     * @serial
     */
    @NotNull
    private final JTextField mapName = new JTextField();

    /**
     * The map's region.
     * @serial
     */
    @NotNull
    private final JTextField mapRegion = new JTextField();

    /**
     * The width in squares.
     * @serial
     */
    @NotNull
    private final JTextField mapWidthField = new JTextField();

    /**
     * The height in squares.
     * @serial
     */
    @NotNull
    private final JTextField mapHeightField = new JTextField();

    /**
     * The unique attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxUnique = new JCheckBox();

    /**
     * The outdoor attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxOutdoor = new JCheckBox();

    /**
     * The no smooth attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoSmooth = new JCheckBox();

    /**
     * The enter x attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldEnterX = new JTextField();

    /**
     * The enter y attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldEnterY = new JTextField();

    /**
     * The swap time.
     * @serial
     */
    @NotNull
    private final JTextField fieldSwapTime = new JTextField();

    /**
     * The reset timeout.
     * @serial
     */
    @NotNull
    private final JTextField fieldResetTimeout = new JTextField();

    /**
     * The background music.
     * @serial
     */
    @NotNull
    private final JTextField fieldBackgroundMusic = new JTextField();

    /**
     * The map difficulty.
     * @serial
     */
    @NotNull
    private final JTextField fieldDifficulty = new JTextField();

    /**
     * The map darkness.
     * @serial
     */
    @NotNull
    private final JTextField fieldDarkness = new JTextField();

    /**
     * The fixed reset attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxFixedReset = new JCheckBox();

    /**
     * The shop item spec.
     * @serial
     */
    @NotNull
    private final JTextField fieldShopItems = new JTextField();

    /**
     * The shop greed.
     * @serial
     */
    @NotNull
    private final JTextField fieldShopgreed = new JTextField();

    /**
     * The shop maximum price.
     * @serial
     */
    @NotNull
    private final JTextField fieldShopMax = new JTextField();

    /**
     * The shop minimum price.
     * @serial
     */
    @NotNull
    private final JTextField fieldShopMin = new JTextField();

    /**
     * The shop's preferred race.
     * @serial
     */
    @NotNull
    private final JTextField fieldShopRace = new JTextField();

    /**
     * The temperature.
     * @serial
     */
    @NotNull
    private final JTextField fieldTemperature = new JTextField();

    /**
     * The pressure.
     * @serial
     */
    @NotNull
    private final JTextField fieldPressure = new JTextField();

    /**
     * The humidity.
     * @serial
     */
    @NotNull
    private final JTextField fieldHumidity = new JTextField();

    /**
     * The wind speed.
     * @serial
     */
    @NotNull
    private final JTextField fieldWindSpeed = new JTextField();

    /**
     * The wind direction.
     * @serial
     */
    @NotNull
    private final JTextField fieldWindDirection = new JTextField();

    /**
     * The sky settings.
     * @serial
     */
    @NotNull
    private final JTextField fieldSkySetting = new JTextField();

    /**
     * The button for ok.
     * @serial
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "mapOkay", this));

    /**
     * The button for cancel.
     * @serial
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "mapCancel", this));

    /**
     * The {@link AbstractMapTilePane}.
     * @serial
     */
    @NotNull
    private final AbstractMapTilePane<GameObject, MapArchObject, Archetype> mapTilePane;

    /**
     * Whether the map tile pane was enabled.
     * @serial
     */
    private final boolean mapTilePaneEnabled;

    /**
     * Creates a map-options dialog.
     * @param helpParent the parent frame for help windows
     * @param mapManager the map manager to use
     * @param globalSettings the global settings instance
     * @param mapModel the map model whose properties are shown/edited
     * @param mapFileFilter the Swing file filter to use
     * @param mapPathNormalizer the map path normalizer for converting map paths
     * to files
     */
    public MapPropertiesDialog(@NotNull final JFrame helpParent, @NotNull final MapManager<GameObject, MapArchObject, Archetype> mapManager, @NotNull final GlobalSettings globalSettings, @NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final FileFilter mapFileFilter, @NotNull final MapPathNormalizer mapPathNormalizer) {
        okButton.setDefaultCapable(true);
        final JButton helpButton = new JButton(ACTION_BUILDER.createAction(false, "mapHelp", this));
        final JButton restoreButton = new JButton(ACTION_BUILDER.createAction(false, "mapRestore", this));
        setOptions(new Object[] { helpButton, okButton, restoreButton, cancelButton });

        this.helpParent = helpParent;
        this.mapModel = mapModel;
        final MapArchObject map = mapModel.getMapArchObject();

        gbcLabel.insets = new Insets(2, 2, 2, 2);
        gbcLabel.anchor = GridBagConstraints.EAST;

        gbcTextField.insets = new Insets(2, 2, 2, 2);
        gbcTextField.fill = GridBagConstraints.HORIZONTAL;
        gbcTextField.weightx = 1.0;
        gbcTextField.gridwidth = GridBagConstraints.REMAINDER;

        gbcCheckBox.insets = new Insets(2, 2, 2, 2);
        gbcCheckBox.fill = GridBagConstraints.HORIZONTAL;
        gbcCheckBox.gridwidth = GridBagConstraints.REMAINDER;

        gbcFiller.fill = GridBagConstraints.BOTH;
        gbcFiller.weighty = 1.0;

        final JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(10, 4, 4, 4));

        final Component mapPanel = createMapPanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapMapTabTitle"), mapPanel);

        final Component textPanel = createMapTextPanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapTextTabTitle"), textPanel);

        final Component lorePanel = createMapLorePanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapLoreTabTitle"), lorePanel);

        final Component parametersPanel = createParametersPanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapParametersTabTitle"), parametersPanel);

        final Component shopPanel = createShopPanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapShopTabTitle"), shopPanel);

        final Component weatherPanel = createWeatherPanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapWeatherTabTitle"), weatherPanel);

        mapTilePane = new FlatMapTilePane<GameObject, MapArchObject, Archetype>(mapManager, globalSettings, mapModel, mapFileFilter, mapPathNormalizer);
        final Component tilePanel = createTilePathPanel(mapTilePane);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapTilesTabTitle"), tilePanel);

        mapTilePaneEnabled = mapModel.getMapFile() != null;
        tabs.setEnabledAt(tabs.indexOfComponent(tilePanel), mapTilePaneEnabled);

        // The layoutHack is used because otherwise a tabbed pane isn't increased to its vertical size by some OptionPaneUI implementations.
        final Container layoutHack = new JPanel(new BorderLayout());
        layoutHack.add(tabs);
        setMessage(layoutHack);

        TextComponentUtils.setAutoSelectOnFocus(mapDescription);
        TextComponentUtils.setAutoSelectOnFocus(mapLore);
        TextComponentUtils.setAutoSelectOnFocus(mapName);
        TextComponentUtils.setAutoSelectOnFocus(mapRegion);
        TextComponentUtils.setAutoSelectOnFocus(mapWidthField);
        TextComponentUtils.setAutoSelectOnFocus(mapHeightField);
        TextComponentUtils.setAutoSelectOnFocus(fieldEnterX);
        TextComponentUtils.setAutoSelectOnFocus(fieldEnterY);
        TextComponentUtils.setAutoSelectOnFocus(fieldSwapTime);
        TextComponentUtils.setAutoSelectOnFocus(fieldResetTimeout);
        TextComponentUtils.setAutoSelectOnFocus(fieldBackgroundMusic);
        TextComponentUtils.setAutoSelectOnFocus(fieldDifficulty);
        TextComponentUtils.setAutoSelectOnFocus(fieldDarkness);
        TextComponentUtils.setAutoSelectOnFocus(fieldShopItems);
        TextComponentUtils.setAutoSelectOnFocus(fieldShopgreed);
        TextComponentUtils.setAutoSelectOnFocus(fieldShopMax);
        TextComponentUtils.setAutoSelectOnFocus(fieldShopMin);
        TextComponentUtils.setAutoSelectOnFocus(fieldShopRace);
        TextComponentUtils.setAutoSelectOnFocus(fieldTemperature);
        TextComponentUtils.setAutoSelectOnFocus(fieldPressure);
        TextComponentUtils.setAutoSelectOnFocus(fieldHumidity);
        TextComponentUtils.setAutoSelectOnFocus(fieldWindSpeed);
        TextComponentUtils.setAutoSelectOnFocus(fieldWindDirection);
        TextComponentUtils.setAutoSelectOnFocus(fieldSkySetting);
    }

    /**
     * Create the map panel.
     * @param map the map arch object to create map panel for
     * @return the newly created map panel
     */
    @NotNull
    private Component createMapPanel(@NotNull final MapArchObject map) {
        final Container panel = new JPanel(new GridBagLayout());
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        addInputFieldLine(panel, mapName, 16, map.getMapName(), "mapName");
        addInputFieldLine(panel, mapRegion, 16, map.getRegion(), "mapRegion");
        addInputFieldLine(panel, mapWidthField, 10, String.valueOf(mapSize.getWidth()), "mapWidth");
        addInputFieldLine(panel, mapHeightField, 10, String.valueOf(mapSize.getHeight()), "mapHeight");
        addCheckBoxLine(panel, checkboxUnique, map.isUnique(), "mapUnique");
        addCheckBoxLine(panel, checkboxOutdoor, map.isOutdoor(), "mapOutdoor");
        addCheckBoxLine(panel, checkboxFixedReset, map.isFixedReset(), "mapFixedReset");
        addCheckBoxLine(panel, checkboxNoSmooth, map.isNoSmooth(), "mapNosmooth");
        addFiller(panel);
        return panel;
    }

    /**
     * Create the parameters panel.
     * @param map the map arch object to create parameters panel for
     * @return the newly created parameters panel
     */
    @NotNull
    private Component createParametersPanel(@NotNull final MapArchObject map) {
        final Container panel = new JPanel(new GridBagLayout());
        addInputFieldLine(panel, fieldEnterX, 10, String.valueOf(map.getEnterX()), "mapEnterX");
        addInputFieldLine(panel, fieldEnterY, 10, String.valueOf(map.getEnterY()), "mapEnterY");
        addInputFieldLine(panel, fieldDifficulty, 10, String.valueOf(map.getDifficulty()), "mapDifficulty");
        addInputFieldLine(panel, fieldDarkness, 10, String.valueOf(map.getDarkness()), "mapDarkness");
        addInputFieldLine(panel, fieldSwapTime, 10, String.valueOf(map.getSwapTime()), "mapSwapTime");
        addInputFieldLine(panel, fieldResetTimeout, 10, String.valueOf(map.getResetTimeout()), "mapResetTimeout");
        addInputFieldLine(panel, fieldBackgroundMusic, 10, String.valueOf(map.getBackgroundMusic()), "mapBackgroundMusic");
        addFiller(panel);
        return panel;
    }

    /**
     * Create the shop panel.
     * @param map the map arch object to create shop panel for
     * @return the newly created shop panel
     */
    @NotNull
    private Component createShopPanel(@NotNull final MapArchObject map) {
        final Container panel = new JPanel(new GridBagLayout());
        addInputFieldLine(panel, fieldShopItems, 10, map.getShopItems(), "mapShopType");
        addInputFieldLine(panel, fieldShopgreed, 10, String.valueOf(map.getShopGreed()), "mapShopGreed");
        addInputFieldLine(panel, fieldShopMax, 10, String.valueOf(map.getShopMax()), "mapUpperPriceLimit");
        addInputFieldLine(panel, fieldShopMin, 10, String.valueOf(map.getShopMin()), "mapLowerPriceLimit");
        addInputFieldLine(panel, fieldShopRace, 10, map.getShopRace(), "mapShopRace");
        addFiller(panel);
        return panel;
    }

    /**
     * Create the weather panel.
     * @param map the map arch object to create weather panel for
     * @return the newly created weather panel
     */
    @NotNull
    private Component createWeatherPanel(@NotNull final MapArchObject map) {
        final Container panel = new JPanel(new GridBagLayout());
        addInputFieldLine(panel, fieldTemperature, 10, String.valueOf(map.getTemperature()), "mapTemperature");
        addInputFieldLine(panel, fieldPressure, 10, String.valueOf(map.getPressure()), "mapPressure");
        addInputFieldLine(panel, fieldHumidity, 10, String.valueOf(map.getHumidity()), "mapHumidity");
        addInputFieldLine(panel, fieldWindSpeed, 10, String.valueOf(map.getWindSpeed()), "mapWindSpeed");
        addInputFieldLine(panel, fieldWindDirection, 10, String.valueOf(map.getWindDirection()), "mapWindDirection");
        addInputFieldLine(panel, fieldSkySetting, 10, String.valueOf(map.getSky()), "mapSkySetting");
        addFiller(panel);
        return panel;
    }

    /**
     * Create the lore panel.
     * @param map the map arch object to create lore panel for
     * @return the newly created lore panel
     */
    @NotNull
    private Component createMapLorePanel(@NotNull final MapArchObject map) {
        final Container panel = new JPanel(new BorderLayout(1, 1));

        mapLore.setText(map.getLore());
        mapLore.setCaretPosition(0);
        panel.add(mapLore);

        return panel;
    }

    /**
     * Create the map text panel.
     * @param map the map arch object to create map text panel for
     * @return the newly created map text panel
     */
    @NotNull
    private Component createMapTextPanel(@NotNull final net.sf.gridarta.model.maparchobject.MapArchObject<MapArchObject> map) {
        final Container panel = new JPanel(new BorderLayout(1, 1));

        mapDescription.setText(map.getText());
        mapDescription.setCaretPosition(0);
        panel.add(mapDescription);

        return panel;
    }

    /**
     * Create the tile path panel.
     * @param mapTilePane the <code>MapTilePane<code> contents
     * @return the newly created tile path panel
     */
    @NotNull
    private static Component createTilePathPanel(@NotNull final Component mapTilePane) {
        final Container panel = new JPanel(new BorderLayout());
        panel.add(mapTilePane, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Action method for help.
     */
    @ActionMethod
    public void mapHelp() {
        new Help(helpParent, "tut_mapattr.html").setVisible(true);
    }

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void mapOkay() {
        if (modifyMapProperties()) {
            setValue(okButton);
        }
    }

    /**
     * Action method for restore.
     */
    @ActionMethod
    public void mapRestore() {
        restoreMapProperties();
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void mapCancel() {
        setValue(cancelButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (dialog != null && newValue != UNINITIALIZED_VALUE) {
            dialog.dispose();
        }
    }

    /**
     * Adds a line with a label and an input field.
     * @param panel the panel to add to
     * @param textField the text field to add
     * @param n the number of columns of the text field
     * @param defaultText the default text of the text field
     * @param labelKey the key of the label
     */
    private void addInputFieldLine(@NotNull final Container panel, @NotNull final JTextField textField, final int n, @NotNull final String defaultText, @NotNull final String labelKey) {
        textField.setColumns(n);
        textField.setText(defaultText);
        panel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, labelKey), gbcLabel);
        panel.add(textField, gbcTextField);
    }

    /**
     * Adds a line with a check box.
     * @param panel the panel to add to
     * @param checkBox the check box to add
     * @param state the default state of the check box
     * @param labelKey the key of the label
     */
    private void addCheckBoxLine(@NotNull final Container panel, @NotNull final AbstractButton checkBox, final boolean state, @NotNull final String labelKey) {
        checkBox.setText(ActionBuilderUtils.getString(ACTION_BUILDER, labelKey));
        checkBox.setSelected(state);
        panel.add(checkBox, gbcCheckBox);
    }

    /**
     * Fills the remaining space.
     * @param panel the panel to fill
     */
    private void addFiller(@NotNull final Container panel) {
        panel.add(new JPanel(), gbcFiller);
    }

    /**
     * Checks the given values and modifies the current map.
     * @return <code>true</code> if the map properties were edited,
     *         <code>false</code> if the parameters were wrong.
     */
    private boolean modifyMapProperties() {
        // first check if the entries are all okay
        final int darkness;
        final int difficulty;
        final int swapTime;
        final int resetTimeout;
        final String backgroundMusic;
        final int temperature;
        final int pressure;
        final int humidity;
        final int windSpeed;
        final int windDirection;
        final int skySetting;
        final double shopGreed;
        final int shopMin;
        final int shopMax;
        final String shopItems;
        final String shopRace;
        final String region;
        final Size2D mapSize;
        final int enterX;
        final int enterY;
        try {
            // try to parse everything
            final int width = parseProperty(mapWidthField.getText(), "Width");
            final int height = parseProperty(mapHeightField.getText(), "Height");
            enterX = parseProperty(fieldEnterX.getText(), "Enter X");
            enterY = parseProperty(fieldEnterY.getText(), "Enter Y");
            swapTime = parseProperty(fieldSwapTime.getText(), "Swap Time");
            resetTimeout = parseProperty(fieldResetTimeout.getText(), "Reset Timeout");
            backgroundMusic = fieldBackgroundMusic.getText();
            difficulty = parseProperty(fieldDifficulty.getText(), "Difficulty");
            darkness = parseProperty(fieldDarkness.getText(), "Darkness");
            region = mapRegion.getText();

            shopItems = fieldShopItems.getText();
            shopRace = fieldShopRace.getText();
            shopMax = parseProperty(fieldShopMax.getText(), "Shop Maximum");
            shopMin = parseProperty(fieldShopMin.getText(), "Shop Minimum");
            shopGreed = parsePropertyToDouble(fieldShopgreed.getText(), "Shop Greed");
            temperature = parseProperty(fieldTemperature.getText(), "Temperature");
            pressure = parseProperty(fieldPressure.getText(), "Pressure");
            humidity = parseProperty(fieldHumidity.getText(), "Humidity");
            windSpeed = parseProperty(fieldWindSpeed.getText(), "Wind Speed");
            windDirection = parseProperty(fieldWindDirection.getText(), "Wind Direction");
            skySetting = parseProperty(fieldSkySetting.getText(), "Sky Setting");

            // Now do some sanity checks:
            if (width < 1 || height < 1) {
                ACTION_BUILDER.showMessageDialog(this, "mapErrorIllegalSize");
                return false;
            }
            mapSize = new Size2D(width, height);
            if (darkness < 0 || darkness > 5) {
                ACTION_BUILDER.showMessageDialog(this, "mapErrorInvalidDarkness");
                return false;
            }
            if (mapName.getText().length() == 0) {
                ACTION_BUILDER.showMessageDialog(this, "mapErrorMissingMapName");
                return false;
            }
        } catch (final IllegalArgumentException e) {
            ACTION_BUILDER.showMessageDialog(this, "mapErrorInvalidEntry", e.getMessage());
            return false;
        }

        // now that all is well, write the new values into the map arch object

        mapModel.beginTransaction("Map properties"); // TODO: I18N/L10N
        try {
            final MapArchObject mapArchObject = mapModel.getMapArchObject();
            mapArchObject.beginTransaction();
            try {
                final String mapNameString = mapName.getText();
                mapArchObject.setMapSize(mapSize);
                mapArchObject.setText(mapDescription.getText());
                mapArchObject.setLore(mapLore.getText());
                mapArchObject.setMapName(mapNameString);

                mapArchObject.setRegion(region);
                mapArchObject.setEnterX(enterX);
                mapArchObject.setEnterY(enterY);
                mapArchObject.setResetTimeout(resetTimeout);
                mapArchObject.setBackgroundMusic(backgroundMusic);
                mapArchObject.setSwapTime(swapTime);
                mapArchObject.setDifficulty(difficulty);
                mapArchObject.setFixedReset(checkboxFixedReset.isSelected());
                mapArchObject.setDarkness(darkness);
                mapArchObject.setUnique(checkboxUnique.isSelected());
                mapArchObject.setOutdoor(checkboxOutdoor.isSelected());
                mapArchObject.setNoSmooth(checkboxNoSmooth.isSelected());

                mapArchObject.setShopItems(shopItems);
                mapArchObject.setShopRace(shopRace);
                mapArchObject.setShopMin(shopMin);
                mapArchObject.setShopMax(shopMax);
                mapArchObject.setShopGreed(shopGreed);

                mapArchObject.setTemperature(temperature);
                mapArchObject.setPressure(pressure);
                mapArchObject.setHumidity(humidity);
                mapArchObject.setWindSpeed(windSpeed);
                mapArchObject.setWindDirection(windDirection);
                mapArchObject.setSky(skySetting);

                mapTilePane.modifyMapProperties();
            } finally {
                mapArchObject.endTransaction();
            }
        } finally {
            mapModel.endTransaction();
        }

        return true;
    }

    /**
     * This is a simple string-to-int parser that throws
     * IllegalArgumentExceptions with appropriate error messages.
     * @param s string to be parse
     * @param label attribute label for error message
     * @return value of String 's', zero if 's' is empty
     * @throws IllegalArgumentException when parsing fails
     */
    private static int parseProperty(@NotNull final String s, @NotNull final String label) {
        if (s.length() == 0) {
            return 0;   // empty string is interpreted as zero
        }

        final int r;  // return value
        try {
            r = Integer.parseInt(s);  // trying to parse
        } catch (final NumberFormatException ignored) {
            // 's' is not a number
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new IllegalArgumentException(label + ": '" + s + "' is not a numerical integer value.");
        }

        // negative values are not allowed
        if (r < 0) {
            throw new IllegalArgumentException(label + ": '" + s + "' is negative.");
        }

        return r; // everything okay
    }

    /**
     * This is a simple string-to-double parser that throws
     * IllegalArgumentExceptions with appropriate error messages.
     * @param s string to be parse
     * @param label attribute label for error message
     * @return value of String 's', zero if 's' is empty
     * @throws IllegalArgumentException when parsing fails
     */
    private static double parsePropertyToDouble(@NotNull final String s, @NotNull final String label) {
        if (s.length() == 0) {
            return 0.0;   // empty string is interpreted as zero
        }

        final double r;  // return value
        try {
            r = Double.parseDouble(s);  // trying to parse
        } catch (final NumberFormatException ignored) {
            // 's' is not a number
            //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
            throw new IllegalArgumentException(label + ": '" + s + "' is not a numerical double value.");
        }

        // negative values are not allowed
        if (r < 0.0) {
            throw new IllegalArgumentException(label + ": '" + s + "' is negative.");
        }

        return r; // everything okay
    }

    /**
     * Reset all map properties to the saved values in the map arch object.
     */
    private void restoreMapProperties() {
        final MapArchObject map = mapModel.getMapArchObject();

        mapDescription.setText(map.getText());
        mapLore.setText(map.getLore());
        mapName.setText(map.getMapName());
        mapRegion.setText(map.getRegion());
        final Size2D mapSize = map.getMapSize();
        mapWidthField.setText(String.valueOf(mapSize.getWidth()));
        mapHeightField.setText(String.valueOf(mapSize.getHeight()));
        fieldEnterX.setText(String.valueOf(map.getEnterX()));
        fieldEnterY.setText(String.valueOf(map.getEnterY()));
        fieldSwapTime.setText(String.valueOf(map.getSwapTime()));
        fieldResetTimeout.setText(String.valueOf(map.getResetTimeout()));
        fieldBackgroundMusic.setText(String.valueOf(map.getBackgroundMusic()));
        fieldDarkness.setText(String.valueOf(map.getDarkness()));
        fieldDifficulty.setText(String.valueOf(map.getDifficulty()));

        checkboxUnique.setSelected(map.isUnique());
        checkboxOutdoor.setSelected(map.isOutdoor());
        checkboxFixedReset.setSelected(map.isFixedReset());
        checkboxNoSmooth.setSelected(map.isNoSmooth());

        fieldShopItems.setText(map.getShopItems());
        fieldShopRace.setText(map.getShopRace());
        fieldShopMax.setText(String.valueOf(map.getShopMax()));
        fieldShopMin.setText(String.valueOf(map.getShopMin()));
        fieldShopgreed.setText(String.valueOf(map.getShopGreed()));
        fieldTemperature.setText(String.valueOf(map.getTemperature()));
        fieldPressure.setText(String.valueOf(map.getPressure()));
        fieldHumidity.setText(String.valueOf(map.getHumidity()));
        fieldWindSpeed.setText(String.valueOf(map.getWindSpeed()));
        fieldWindDirection.setText(String.valueOf(map.getWindDirection()));
        fieldSkySetting.setText(String.valueOf(map.getSky()));

        mapTilePane.restoreMapProperties();
    }

    /**
     * Creates and displays the map properties dialog.
     * @param parentComponent the parent component of the dialog
     */
    public void showDialog(@NotNull final Component parentComponent) {
        final String title = ACTION_BUILDER.format("mapTitle", mapModel.getMapArchObject().getMapName(), mapModel.getMapArchObject().getMapName());
        final JDialog window = createDialog(parentComponent, title);
        dialog = window;
        window.getRootPane().setDefaultButton(okButton);
        window.setResizable(true);
        window.setModal(false);
        window.setVisible(true);
        if (!mapTilePaneEnabled) {
            ACTION_BUILDER.showOnetimeMessageDialog(window, WARNING_MESSAGE, "mapTilesNoMapFileNoMapTilePane");
        }
    }

}
