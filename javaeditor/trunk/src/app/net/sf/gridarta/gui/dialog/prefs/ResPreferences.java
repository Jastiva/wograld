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

package net.sf.gridarta.gui.dialog.prefs;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.gui.utils.JFileField;
import net.sf.gridarta.model.configsource.ConfigSource;
import net.sf.gridarta.model.configsource.ConfigSourceFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.prefs.AbstractPrefs;
import net.sf.japi.util.Arrays2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Preferences Module for resource preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @serial exclude
 */
public class ResPreferences extends AbstractPrefs {

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
     * The global settings instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link ConfigSourceFactory} to use.
     */
    @NotNull
    private final ConfigSourceFactory configSourceFactory;

    /**
     * TextField for arch directory path.
     */
    @NotNull
    private JFileField archField;

    /**
     * TextField for map directory path.
     */
    @NotNull
    private JFileField mapField;

    /**
     * TextField for media directory path.
     */
    @Nullable
    private JFileField mediaField;

    /**
     * ComboBox for choosing the configuration source.
     */
    @NotNull
    private JComboBox configSourceComboBox;

    /**
     * ComboBox for choosing the image set.
     */
    @Nullable
    private JComboBox imageSetBox;

    /**
     * Contains all supported image sets.
     */
    @Nullable
    private String[] imageSets;

    /**
     * The {@link ItemListener} attached to {@link #configSourceComboBox}.
     */
    @NotNull
    private final ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                final ConfigSource configSource = (ConfigSource) e.getItem();
                archField.setEnabled(configSource.isArchDirectoryInputFieldEnabled());
            }
        }

    };

    /**
     * Create a ResPreferences pane.
     * @param globalSettings the global settings instance
     * @param configSourceFactory the config source factory to use
     */
    public ResPreferences(@NotNull final GlobalSettings globalSettings, @NotNull final ConfigSourceFactory configSourceFactory) {
        this.globalSettings = globalSettings;
        this.configSourceFactory = configSourceFactory;
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsRes.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsRes.icon"));

        add(createResourcePanel());
        add(createGlobalPanel());
        add(Box.createVerticalGlue());
    }

    /**
     * Creates a titled border.
     * @param titleKey the action key for border title
     * @return the titled border
     */
    @NotNull
    private static Border createTitledBorder(final String titleKey) {
        return new CompoundBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, titleKey)), GUIConstants.DIALOG_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply() {
        globalSettings.setArchDirectory(archField.getFile());
        globalSettings.setMapsDirectory(mapField.getFile());
        if (globalSettings.hasMediaDirectory()) {
            assert mediaField != null;
            globalSettings.setMediaDirectory(mediaField.getFile());
        }
        globalSettings.setConfigSourceName(((ConfigSource) configSourceComboBox.getSelectedItem()).getName());

        if (globalSettings.hasImageSet()) {
            assert imageSetBox != null;
            final String imageSet = (String) imageSetBox.getSelectedItem();
            globalSettings.setImageSet(imageSet == null || imageSet.equals("disabled") ? "none" : imageSet);
        }

        ACTION_BUILDER.showOnetimeMessageDialog(this, JOptionPane.WARNING_MESSAGE, "optionsRestart");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        archField.setFile(globalSettings.getArchDirectory());
        mapField.setFile(globalSettings.getMapsDirectory());
        if (globalSettings.hasMediaDirectory()) {
            final File mediaDirectory = globalSettings.getMediaDirectory();
            assert mediaField != null;
            mediaField.setFile(mediaDirectory);
        }
        configSourceComboBox.setSelectedItem(configSourceFactory.getConfigSource(globalSettings.getConfigSourceName()));
        if (globalSettings.hasImageSet()) {
            final String imageSet = getCurrentImageSet();
            assert imageSets != null;
            final int index = Arrays2.linearEqualitySearch(imageSet, imageSets);
            assert imageSetBox != null;
            imageSetBox.setSelectedIndex(index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        archField.setFile(globalSettings.getArchDirectoryDefault());
        mapField.setFile(globalSettings.getMapsDirectoryDefault());
        if (globalSettings.hasMediaDirectory()) {
            final File mediaDirectory = globalSettings.getMediaDirectoryDefault();
            assert mediaField != null;
            mediaField.setFile(mediaDirectory);
        }
        configSourceComboBox.setSelectedItem(configSourceFactory.getDefaultConfigSource());
        if (globalSettings.hasImageSet()) {
            assert imageSets != null;
            final int index = Arrays2.linearEqualitySearch("disabled", imageSets);
            assert imageSetBox != null;
            imageSetBox.setSelectedIndex(index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        final String imageSet;
        if (globalSettings.hasImageSet()) {
            assert imageSetBox != null;
            imageSet = convertImageSet((String) imageSetBox.getSelectedItem());
        } else {
            imageSet = convertImageSet(null);
        }
        if (!archField.getFile().equals(globalSettings.getArchDirectory()) || !mapField.getFile().equals(globalSettings.getMapsDirectory())) {
            return true;
        }

        if (globalSettings.hasMediaDirectory()) {
            assert mediaField != null;
            if (!mediaField.getFile().equals(globalSettings.getMediaDirectory())) {
                return true;
            }
        }

        return !(imageSet.equals(getCurrentImageSet()) && ((ConfigSource) configSourceComboBox.getSelectedItem()).getName().equals(globalSettings.getConfigSourceName()));
    }

    /**
     * Creates the sub-panel with the resource paths.
     * @return sub-panel
     */
    private Component createResourcePanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsResPaths"));
        archField = new JFileField(this, "optionsResArch", null, globalSettings.getArchDirectory(), JFileChooser.DIRECTORIES_ONLY);
        preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsResArch"));
        preferencesHelper.addComponent(archField);
        mapField = new JFileField(this, "optionsResMaps", null, globalSettings.getMapsDirectory(), JFileChooser.DIRECTORIES_ONLY);
        preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsResMaps"));
        preferencesHelper.addComponent(mapField);
        if (globalSettings.hasMediaDirectory()) {
            mediaField = new JFileField(this, "optionsResMedia", null, globalSettings.getMediaDirectory(), JFileChooser.DIRECTORIES_ONLY);
            preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsResMedia"));
            assert mediaField != null;
            preferencesHelper.addComponent(mediaField);
        }
        return panel;
    }

    /**
     * Creates the sub-panel with the global settings.
     * @return the sub-panel
     */
    @NotNull
    private Component createGlobalPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsGlobal"));

        configSourceComboBox = new JComboBox(configSourceFactory.getConfigSources());
        final ConfigSource configSource = configSourceFactory.getConfigSource(globalSettings.getConfigSourceName());
        configSourceComboBox.setSelectedItem(configSource);
        configSourceComboBox.addItemListener(itemListener);
        preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsConfigSource.text"));
        preferencesHelper.addComponent(configSourceComboBox);
        archField.setEnabled(configSource.isArchDirectoryInputFieldEnabled());

        if (globalSettings.hasImageSet()) {
            preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsImageSet"));
            preferencesHelper.addComponent(buildImageSetBox());
        }

        return panel;
    }

    /**
     * Constructs the combo box for the selection of image sets.
     * @return the combo box with image sets to select
     */
    @NotNull
    private Component buildImageSetBox() {
        final String[] imageSetNames = StringUtils.PATTERN_WHITESPACE.split(ActionBuilderUtils.getString(ACTION_BUILDER, "availableImageSets"), 0);
        Arrays.sort(imageSetNames);
        imageSets = new String[imageSetNames.length + 1];
        imageSets[0] = "disabled";
        System.arraycopy(imageSetNames, 0, imageSets, 1, imageSetNames.length);

        imageSetBox = new JComboBox(imageSets);     // set "content"
        final String imageSet = getCurrentImageSet();
        assert imageSets != null;
        final int index = Arrays2.linearEqualitySearch(imageSet, imageSets);
        assert imageSetBox != null;
        imageSetBox.setSelectedIndex(index);

        assert imageSetBox != null;
        return imageSetBox;
    }

    /**
     * Returns the name of the currently selected image set.
     * @return the name or "disabled" if no image set is selected
     */
    @NotNull
    private String getCurrentImageSet() {
        return convertImageSet(globalSettings.getImageSet());
    }

    /**
     * Returns a human readable name for a given image set.
     * @param imageSet the image set
     * @return the name
     */
    @NotNull
    private static String convertImageSet(@Nullable final String imageSet) {
        return imageSet == null || imageSet.isEmpty() || imageSet.equals("none") ? "disabled" : imageSet;
    }

}
