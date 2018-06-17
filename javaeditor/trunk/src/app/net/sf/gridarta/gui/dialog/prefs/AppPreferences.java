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
import java.io.File;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.gui.utils.JFileField;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.prefs.AbstractPrefs;
import org.jetbrains.annotations.NotNull;

/**
 * Preferences Module for application preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class AppPreferences extends AbstractPrefs {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The model.
     */
    @NotNull
    private final AppPreferencesModel appPreferencesModel;

    /**
     * TextField for server executable.
     */
    private JFileField serverField;

    /**
     * TextField for client executable.
     */
    private JFileField clientField;

    /**
     * TextField for external editor executable.
     */
    private JFileField editorField;

    /**
     * Creates a new instance.
     * @param appPreferencesModel the model
     */
    public AppPreferences(@NotNull final AppPreferencesModel appPreferencesModel) {
        this.appPreferencesModel = appPreferencesModel;
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsApp.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsApp.icon"));

        add(createAppPanel());
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
        AppPreferencesModel.setServer(serverField.getFile().getPath());
        AppPreferencesModel.setClient(clientField.getFile().getPath());
        AppPreferencesModel.setEditor(editorField.getFile().getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        serverField.setFile(new File(appPreferencesModel.getServer()));
        clientField.setFile(new File(appPreferencesModel.getClient()));
        editorField.setFile(new File(appPreferencesModel.getEditor()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        serverField.setFile(new File(appPreferencesModel.getServerDefault()));
        clientField.setFile(new File(appPreferencesModel.getClientDefault()));
        editorField.setFile(new File(appPreferencesModel.getEditorDefault()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        return !(serverField.getFile().equals(new File(appPreferencesModel.getServer())) && clientField.getFile().equals(new File(appPreferencesModel.getClient())) && editorField.getFile().equals(new File(appPreferencesModel.getEditor())));
    }

    /**
     * Creates the sub-panel with the external applications.
     * @return the sub-panel
     */
    private Component createAppPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsApps"));

        serverField = new JFileField(this, "optionsAppServer", null, new File(appPreferencesModel.getServer()), JFileChooser.FILES_ONLY);
        preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsAppServer"));
        preferencesHelper.addComponent(serverField);
        clientField = new JFileField(this, "optionsAppClient", null, new File(appPreferencesModel.getClient()), JFileChooser.FILES_ONLY);
        preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsAppClient"));
        preferencesHelper.addComponent(clientField);
        editorField = new JFileField(this, "optionsAppEditor", null, new File(appPreferencesModel.getEditor()), JFileChooser.FILES_ONLY);
        preferencesHelper.addComponent(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsAppEditor"));
        preferencesHelper.addComponent(editorField);

        return panel;
    }

}
