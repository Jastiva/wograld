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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.EnumSet;
import java.util.prefs.Preferences;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.prefs.AbstractPrefs;

/**
 * Preferences Module for networking preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class NetPreferences extends AbstractPrefs implements ItemListener {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The preferences key for the type.
     */
    private static final String NET_PREFERENCES_KEY_TYPE = "proxy.type";

    /**
     * The preferences key for the type.
     */
    private static final String NET_PREFERENCES_KEY_HOST = "proxy.host";

    /**
     * The preferences key for the type.
     */
    private static final String NET_PREFERENCES_KEY_PORT = "proxy.port";

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Preferences.
     */
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(MainControl.class);

    /**
     * JComboBox for selecting the proxy type.
     */
    private final JComboBox proxyType = createProxyType();

    /**
     * TextField for server executable.
     */
    private final JTextComponent proxyHost = new JTextField(PREFERENCES.get(NET_PREFERENCES_KEY_HOST, ""));

    /**
     * TextField for client executable.
     */
    private final JSpinner proxyPort = new JSpinner(new SpinnerNumberModel(PREFERENCES.getInt(NET_PREFERENCES_KEY_PORT, 3128), 1, 65535, 1));

    /**
     * Creates a new instance.
     */
    public NetPreferences() {
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsNet.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsNet.icon"));
        proxyType.addItemListener(this);

        add(createNetPanel());
        add(Box.createVerticalGlue());
        revert();
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
    public boolean isChanged() {
        return !(proxyType.getSelectedItem() == Proxy.Type.valueOf(PREFERENCES.get(NET_PREFERENCES_KEY_TYPE, "DIRECT")) && PREFERENCES.get(NET_PREFERENCES_KEY_HOST, "").equals(proxyHost.getText()) && PREFERENCES.getInt(NET_PREFERENCES_KEY_PORT, 3128) == (Integer) proxyPort.getValue());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        PREFERENCES.remove(NET_PREFERENCES_KEY_TYPE);
        PREFERENCES.remove(NET_PREFERENCES_KEY_HOST);
        PREFERENCES.remove(NET_PREFERENCES_KEY_PORT);
        revert();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void revert() {
        //Cannot weaken tp Enum<Proxy.Type> because some javac versions (1.6.0_01, 1.6.0_16, 1.7.0-ea) report incomparable types
        final Proxy.Type keyType = Proxy.Type.valueOf(PREFERENCES.get(NET_PREFERENCES_KEY_TYPE, "DIRECT"));
        proxyType.setSelectedIndex(keyType.ordinal());
        final boolean enableProxy = keyType != Proxy.Type.DIRECT;
        proxyHost.setEnabled(enableProxy);
        proxyPort.setEnabled(enableProxy);
        proxyHost.setText(PREFERENCES.get(NET_PREFERENCES_KEY_HOST, ""));
        proxyPort.setValue(PREFERENCES.getInt(NET_PREFERENCES_KEY_PORT, 3128));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply() {
        //JComboBox does not use type parameters
        @SuppressWarnings("unchecked")
        final Enum<Proxy.Type> typeEnum = (Enum<Proxy.Type>) proxyType.getSelectedItem();
        PREFERENCES.put(NET_PREFERENCES_KEY_TYPE, typeEnum.name());
        PREFERENCES.put(NET_PREFERENCES_KEY_HOST, proxyHost.getText());
        PREFERENCES.putInt(NET_PREFERENCES_KEY_PORT, (Integer) proxyPort.getValue());
    }

    /**
     * Creates the sub-panel with the external applications.
     * @return the sub-panel
     */
    private Component createNetPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsNetProxy"));
        preferencesHelper.addComponent(proxyType);
        preferencesHelper.addComponent(proxyHost);
        preferencesHelper.addComponent(proxyPort);
        return panel;
    }

    /**
     * Creates the {@link JComboBox} for selecting the proxy type.
     * @return the <code>JComboBox</code> for selecting the proxy type
     */
    private static JComboBox createProxyType() {

        return new JComboBox(EnumSet.allOf(Proxy.Type.class).toArray());
    }

    /**
     * Returns the currently preferred proxy.
     * @return the currently preferred proxy
     */
    public static Proxy getProxy() {
        final Proxy.Type proxyType = Proxy.Type.valueOf(PREFERENCES.get(NET_PREFERENCES_KEY_TYPE, "DIRECT"));
        if (proxyType == Proxy.Type.DIRECT) {
            return Proxy.NO_PROXY;
        }
        return new Proxy(proxyType, new InetSocketAddress(PREFERENCES.get(NET_PREFERENCES_KEY_HOST, "proxy"), PREFERENCES.getInt(NET_PREFERENCES_KEY_PORT, 3128)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(final ItemEvent e) {
        final Proxy.Type proxyTypeSelection = (Proxy.Type) proxyType.getSelectedItem();
        final boolean enableProxy = proxyTypeSelection != Proxy.Type.DIRECT;
        proxyHost.setEnabled(enableProxy);
        proxyPort.setEnabled(enableProxy);
    }

}
