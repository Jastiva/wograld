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
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.gui.autovalidator.AutoValidator;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import net.sf.gridarta.model.validation.Validator;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.prefs.AbstractPrefs;
import org.jetbrains.annotations.NotNull;

/**
 * Preferences Module for map validator preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class MapValidatorPreferences extends AbstractPrefs {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Whether the auto-validator is enabled by default.
     */
    private final boolean autoDefault;

    /**
     * The delegating validator that contains all the validators.
     */
    @NotNull
    private final DelegatingMapValidator<?, ?, ?> validators;

    /**
     * Checkboxes to enabled the individual validator checks.
     */
    @NotNull
    private final Map<Validator<?, ?, ?>, JCheckBox> checkBoxes = new HashMap<Validator<?, ?, ?>, JCheckBox>();

    /**
     * Check box to set whether map files should be really checked.
     */
    @NotNull
    private final AbstractButton autoValidate = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, "autoValidate.text"), AutoValidator.isEnabled());

    /**
     * Creates a new instance.
     * @param validators the delegating map validator that contains all the
     * validators
     * @param autoDefault whether the auto-validator is enabled by default
     */
    public MapValidatorPreferences(@NotNull final DelegatingMapValidator<?, ?, ?> validators, final boolean autoDefault) {
        this.autoDefault = autoDefault;
        this.validators = validators;
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsMapValidator.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsMapValidator.icon"));

        add(createValidationPanel());
        add(createValidatorsPanel());
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
        for (final Map.Entry<Validator<?, ?, ?>, JCheckBox> entry : checkBoxes.entrySet()) {
            entry.getKey().setEnabled(entry.getValue().isSelected());
        }
        AutoValidator.setEnabled(autoValidate.isSelected());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        for (final Map.Entry<Validator<?, ?, ?>, JCheckBox> entry : checkBoxes.entrySet()) {
            entry.getValue().setSelected(entry.getKey().isEnabled());
        }
        autoValidate.setSelected(AutoValidator.isEnabled());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        for (final Map.Entry<Validator<?, ?, ?>, JCheckBox> entry : checkBoxes.entrySet()) {
            entry.getValue().setSelected(entry.getKey().isDefaultEnabled());
        }
        autoValidate.setSelected(autoDefault);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        for (final Map.Entry<Validator<?, ?, ?>, JCheckBox> entry : checkBoxes.entrySet()) {
            if (entry.getValue().isSelected() != entry.getKey().isEnabled()) {
                return true;
            }
        }
        return autoValidate.isSelected() != AutoValidator.isEnabled();
    }

    /**
     * Creates the sub-panel with the generic validation settings.
     * @return the sub-panel
     */
    @NotNull
    private Component createValidationPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsValidation"));
        preferencesHelper.addComponent(autoValidate);
        return panel;
    }

    /**
     * Creates the sub-panel with the validators information.
     * @return the sub-panel
     */
    @NotNull
    private Component createValidatorsPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsValidators"));
        for (final Validator<?, ?, ?> validator : validators) {
            final JCheckBox checkBox = new JCheckBox(ActionBuilderUtils.getString(ACTION_BUILDER, validator.getKey() + ".title"), validator.isEnabled());
            checkBoxes.put(validator, checkBox);
            preferencesHelper.addComponent(checkBox);
        }
        return panel;
    }

}
