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
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.updater.UpdaterManager;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.prefs.AbstractPrefs;

/**
 * Preferences Module for update preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class UpdatePreferences extends AbstractPrefs {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Preferences.
     */
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(MainControl.class);

    /**
     * Checkbox whether to automatically check for updates on startup.
     */
    private AbstractButton autoUpdate;

    /**
     * ComboBox with selected interval.
     */
    private JComboBox interval;

    /**
     * Creates a new instance.
     */
    public UpdatePreferences() {
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsUpdate.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsUpdate.icon"));

        add(createUpdatePanel());
        add(Box.createVerticalGlue());
    }

    /**
     * Creates a titled border.
     * @param titleKey the action key for border title
     * @return the titled border
     */
    private static Border createTitledBorder(final String titleKey) {
        return new CompoundBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, titleKey)), GUIConstants.DIALOG_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply() {
        PREFERENCES.putBoolean(UpdaterManager.AUTO_CHECK_KEY, autoUpdate.isSelected());
        PREFERENCES.putInt(UpdaterManager.INTERVAL_KEY, interval.getSelectedIndex());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        autoUpdate.setSelected(PREFERENCES.getBoolean(UpdaterManager.AUTO_CHECK_KEY, UpdaterManager.AUTO_CHECK_DEFAULT));
        interval.setSelectedIndex(PREFERENCES.getInt(UpdaterManager.INTERVAL_KEY, UpdaterManager.INTERVAL_DEFAULT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        autoUpdate.setSelected(UpdaterManager.AUTO_CHECK_DEFAULT);
        interval.setSelectedIndex(UpdaterManager.INTERVAL_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        return !(autoUpdate.isSelected() == PREFERENCES.getBoolean(UpdaterManager.AUTO_CHECK_KEY, UpdaterManager.AUTO_CHECK_DEFAULT) && interval.getSelectedIndex() == PREFERENCES.getInt(UpdaterManager.INTERVAL_KEY, UpdaterManager.INTERVAL_DEFAULT));
    }

    /**
     * Creates the sub-panel with the update settings.
     * @return the sub-panel
     */
    private Component createUpdatePanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsUpdate"));
        autoUpdate = new JCheckBox(ACTION_BUILDER.createToggle(false, "autoUpdate", this));
        autoUpdate.setSelected(PREFERENCES.getBoolean(UpdaterManager.AUTO_CHECK_KEY, UpdaterManager.AUTO_CHECK_DEFAULT));
        preferencesHelper.addComponent(autoUpdate);
        preferencesHelper.addComponent(createComboBox());
        return panel;
    }

    /**
     * Sets the auto-update state.
     * @param autoUpdate the auto-update state
     */
    @ActionMethod
    public void setAutoUpdate(final boolean autoUpdate) {
        interval.setEnabled(autoUpdate);
    }

    /**
     * Returns the auto-update state.
     * @return the auto-update state
     */
    @ActionMethod
    public boolean isAutoUpdate() {
        return interval.isEnabled();
    }

    /**
     * Creates the component with update choices.
     * @return the component with update choices
     */
    private Component createComboBox() {
        final String[] items = { ActionBuilderUtils.getString(ACTION_BUILDER, "prefsUpdateAuto0.text"), ActionBuilderUtils.getString(ACTION_BUILDER, "prefsUpdateAuto1.text"), ActionBuilderUtils.getString(ACTION_BUILDER, "prefsUpdateAuto2.text"), ActionBuilderUtils.getString(ACTION_BUILDER, "prefsUpdateAuto3.text"), };
        interval = new JComboBox(items);
        interval.setEditable(false);
        interval.setSelectedIndex(PREFERENCES.getInt(UpdaterManager.INTERVAL_KEY, UpdaterManager.INTERVAL_DEFAULT));
        interval.setEnabled(PREFERENCES.getBoolean(UpdaterManager.AUTO_CHECK_KEY, UpdaterManager.AUTO_CHECK_DEFAULT));
        return interval;
    }

}
