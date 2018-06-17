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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.prefs.Preferences;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog used to ask the user the properties for the new level. Contains a
 * tabbed pane for creating a level either based on a template or from a
 * scratch.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @noinspection AbstractClassExtendsConcreteClass
 */
public abstract class AbstractNewMapDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

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
     * Preferences.
     */
    @NotNull
    protected static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * JButton for ok.
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "mapOkay", this));

    /**
     * JButton for cancel.
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "mapCancel", this));

    @Nullable
    private JDialog dialog;

    /**
     * The {@link DocumentListener} attached to input fields for detecting
     * changes.
     */
    @NotNull
    private final DocumentListener documentListener = new DocumentListener() {

        @Override
        public void insertUpdate(@NotNull final DocumentEvent e) {
            updateOkButton();
        }

        @Override
        public void removeUpdate(@NotNull final DocumentEvent e) {
            updateOkButton();
        }

        @Override
        public void changedUpdate(@NotNull final DocumentEvent e) {
            updateOkButton();
        }

    };

    /**
     * Constructs a new map dialog. Builds the dialog UI.
     */
    protected AbstractNewMapDialog() {
    }

    /**
     * Initializes the dialog.
     * @param parentComponent the parent component of this dialog
     * @param dialogTitle the dialog's title
     */
    protected void init1(@NotNull final Component parentComponent, @NotNull final String dialogTitle) {
        okButton.setDefaultCapable(true);
        setOptions(new Object[] { okButton, cancelButton });
        setMessage(createPanel());

        dialog = createDialog(parentComponent, dialogTitle);
        assert dialog != null;
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        assert dialog != null;
        dialog.getRootPane().setDefaultButton(okButton);
        assert dialog != null;
        dialog.setModal(false);
    }

    /**
     * Initializes the dialog.
     */
    protected void init2() {
        assert dialog != null;
        dialog.setVisible(true);
    }

    @NotNull
    private JPanel createPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(GUIConstants.DIALOG_BORDER);
        panel.add(createMapNamePanel());
        final Component parametersPanel = createMapParametersPanel();
        if (parametersPanel != null) {
            panel.add(parametersPanel);
        }
        return panel;
    }

    @NotNull
    protected abstract JPanel createMapNamePanel();

    @Nullable
    protected JPanel createMapParametersPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());

        final GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.insets = new Insets(2, 2, 2, 2);
        gbcLabel.anchor = GridBagConstraints.EAST;

        final GridBagConstraints gbcField = new GridBagConstraints();
        gbcField.insets = new Insets(2, 2, 2, 2);
        gbcField.gridwidth = GridBagConstraints.REMAINDER;

        panel.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), ActionBuilderUtils.getString(ACTION_BUILDER, "newMapParameters")), GUIConstants.DIALOG_BORDER));
        addFields(panel, gbcLabel, gbcField);
        return panel;
    }

    /**
     * Adds additional fields to the dialog. Will be called once when the dialog
     * is created.
     * @param panel the panel to add the fields to
     * @param gbcLabel the grid bag constraints for the label part
     * @param gbcField the grid bag constraints for the input field
     */
    protected abstract void addFields(@NotNull final JPanel panel, @NotNull final GridBagConstraints gbcLabel, @NotNull final GridBagConstraints gbcField);

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void mapOkay() {
        if (isOkButtonEnabled() && createNew()) {
            setValue(okButton);
        }
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
     * Checks the given values and creates a new map.
     * @return <code>true</code> if the map was created, <code>false</code> if
     *         the parameters were wrong
     */
    protected abstract boolean createNew();

    /**
     * Updates the enabled state of the "OK" button depending on the dialog's
     * contents.
     */
    protected void updateOkButton() {
        okButton.setEnabled(isOkButtonEnabled());
    }

    /**
     * Returns whether the "OK" button is enabled depending on the dialog's
     * contents.
     * @return whether the "OK" button is enabled
     */
    protected boolean isOkButtonEnabled() {
        return true;
    }

    /**
     * Watches for text changes in a text component and enables the "OK" button
     * accordingly.
     * @param textComponent the text component to watch
     */
    protected void addDocumentListener(@NotNull final JTextComponent textComponent) {
        textComponent.getDocument().addDocumentListener(documentListener);
        textComponent.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(@NotNull final FocusEvent e) {
                textComponent.selectAll();
            }

            @Override
            public void focusLost(@NotNull final FocusEvent e) {
                textComponent.select(0, 0);
            }

        });
        textComponent.selectAll();
    }

}
