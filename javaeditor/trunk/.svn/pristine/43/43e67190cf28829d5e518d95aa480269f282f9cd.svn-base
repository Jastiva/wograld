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
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.MapFileFilter;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.prefs.AbstractPrefs;
import org.jetbrains.annotations.NotNull;

/**
 * Preferences Module for miscellaneous preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class MiscPreferences extends AbstractPrefs {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link ExitConnectorModel} to affect.
     */
    @NotNull
    private final ExitConnectorModel exitConnectorModel;

    /**
     * The {@link GlobalSettings} to affect.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * TextField for user / artist name.
     */
    private JTextComponent userField;

    /**
     * Check box to set whether map files should be really checked.
     */
    private AbstractButton checkMaps;

    /**
     * Check box to set whether the map name should be pasted into exit game
     * objects.
     */
    private AbstractButton pasteExitName;

    /**
     * Check box to set whether exit game objects should be auto-created.
     */
    private AbstractButton autoCreateExit;

    /**
     * The archetype name for auto-creating exits.
     */
    private JTextComponent exitArchetypeName;

    /**
     * Creates a new instance.
     * @param exitConnectorModel the exit connector model to affect
     * @param globalSettings the global settings to affect
     */
    public MiscPreferences(@NotNull final ExitConnectorModel exitConnectorModel, @NotNull final GlobalSettings globalSettings) {
        this.exitConnectorModel = exitConnectorModel;
        this.globalSettings = globalSettings;

        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsMisc.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsMisc.icon"));

        add(createUserPanel());
        add(createCheckMapsPanel());
        add(createExitConnectorPanel());
        add(Box.createVerticalGlue());
    }

    /**
     * Create a titled border.
     * @param title the border title
     * @return titled border
     */
    private static Border createTitledBorder(final String title) {
        return new CompoundBorder(new TitledBorder(title), GUIConstants.DIALOG_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply() {
        globalSettings.setUserName(userField.getText());
        MapFileFilter.setPerformingRealChecks(checkMaps.isSelected());
        exitConnectorModel.setAutoCreateExit(autoCreateExit.isSelected());
        exitConnectorModel.setPasteExitName(pasteExitName.isSelected());
        exitConnectorModel.setExitArchetypeName(exitArchetypeName.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        userField.setText(globalSettings.getUserName());
        checkMaps.setSelected(MapFileFilter.isPerformingRealChecks());
        autoCreateExit.setSelected(exitConnectorModel.isAutoCreateExit());
        pasteExitName.setSelected(exitConnectorModel.isPasteExitName());
        exitArchetypeName.setText(exitConnectorModel.getExitArchetypeName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        userField.setText(globalSettings.getUserNameDefault());
        checkMaps.setSelected(true);
        autoCreateExit.setSelected(ExitConnectorModel.AUTO_CREATE_EXIT_DEFAULT);
        pasteExitName.setSelected(ExitConnectorModel.PASTE_EXIT_NAME_DEFAULT);
        exitArchetypeName.setText(ExitConnectorModel.EXIT_ARCHETYPE_NAME_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        return !(userField.getText().equals(globalSettings.getUserName()) && checkMaps.isSelected() == MapFileFilter.isPerformingRealChecks() && autoCreateExit.isSelected() == exitConnectorModel.isAutoCreateExit() && pasteExitName.isSelected() == exitConnectorModel.isPasteExitName() && exitArchetypeName.getText().equals(exitConnectorModel.getExitArchetypeName()));
    }

    /**
     * Creates the sub-panel with the user settings.
     * @return sub-panel
     */
    private Component createUserPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "optionsUser")));
        userField = new JTextField(globalSettings.getUserName());
        preferencesHelper.addComponent(userField);
        return panel;
    }

    /**
     * Creates the sub-panel with the check maps settings.
     * @return sub-panel
     */
    private Component createCheckMapsPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "optionsMisc")));
        checkMaps = new JCheckBox(ACTION_BUILDER.createAction(false, "optionsCheckMaps"));
        checkMaps.setSelected(MapFileFilter.isPerformingRealChecks());
        preferencesHelper.addComponent(checkMaps);
        return panel;
    }

    /**
     * Creates the sub-panel with the exit connector settings.
     * @return sub-panel
     */
    private Component createExitConnectorPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, "optionsExitConnector")));

        pasteExitName = new JCheckBox(ACTION_BUILDER.createAction(false, "optionsExitConnectorPasteExitName"));
        pasteExitName.setSelected(exitConnectorModel.isPasteExitName());
        preferencesHelper.addComponent(pasteExitName);

        autoCreateExit = new JCheckBox(ACTION_BUILDER.createAction(false, "optionsExitConnectorAutoCreateExit"));
        autoCreateExit.setSelected(exitConnectorModel.isAutoCreateExit());
        preferencesHelper.addComponent(autoCreateExit);

        final JComponent label = ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsExitConnectorExitArchetypeName.text");
        label.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "optionsExitConnectorExitArchetypeName.shortdescription"));
        preferencesHelper.addComponent(label);
        exitArchetypeName = new JTextField(exitConnectorModel.getExitArchetypeName());
        preferencesHelper.addComponent(exitArchetypeName);

        autoCreateExit.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                exitArchetypeName.setEnabled(autoCreateExit.isSelected());
            }

        });
        exitArchetypeName.setEnabled(autoCreateExit.isSelected());

        return panel;
    }

}
