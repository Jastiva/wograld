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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.prefs.AbstractPrefs;

/**
 * Preferences Module for developer preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class DevPreferences extends AbstractPrefs {

    /**
     * Preferences key for using System.exit().
     */
    public static final String PREFERENCES_SYSTEM_EXIT = "systemExit";

    /**
     * Preferences default value for using System.exit().
     */
    public static final boolean PREFERENCES_SYSTEM_EXIT_DEFAULT = true;

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
     * Use System.exit() for exiting the program.
     */
    private AbstractButton systemExit;

    /**
     * Creates a new instance.
     */
    public DevPreferences() {
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsDev.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsDev.icon"));

        add(createMiscPanel());
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
        PREFERENCES.putBoolean(PREFERENCES_SYSTEM_EXIT, systemExit.isSelected());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        systemExit.setSelected(PREFERENCES.getBoolean(PREFERENCES_SYSTEM_EXIT, PREFERENCES_SYSTEM_EXIT_DEFAULT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        systemExit.setSelected(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        return !(systemExit.isSelected() == PREFERENCES.getBoolean(PREFERENCES_SYSTEM_EXIT, PREFERENCES_SYSTEM_EXIT_DEFAULT));
    }

    /**
     * Creates the sub-panel with the misc settings.
     * @return sub-panel
     */
    private Component createMiscPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsMisc"));

        systemExit = new JCheckBox(ACTION_BUILDER.createAction(false, "optionsSystemExit"));
        systemExit.setSelected(PREFERENCES.getBoolean(PREFERENCES_SYSTEM_EXIT, PREFERENCES_SYSTEM_EXIT_DEFAULT));
        preferencesHelper.addComponent(systemExit);
        return panel;
    }

}
