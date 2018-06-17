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

package net.sf.gridarta.gui.dialog.plugin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterViewFactory;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.Plugin;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginModelListener;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JFrame {

    private final JList scripts;

    private final CardLayout scriptLayout = new CardLayout();

    private final Container scriptPanel = new JPanel(scriptLayout);

    private final PluginController<G, A, R> pluginController;

    @NotNull
    private final PluginModel<G, A, R> pluginModel;

    private final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory;

    @NotNull
    private final PluginParameterFactory<G, A, R> pluginParameterFactory;

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    //TODO fix a memory leak. If a script is remove, it stays in hash map along with it's visual component

    private final Map<Plugin<G, A, R>, PluginEditor<G, A, R>> components = new HashMap<Plugin<G, A, R>, PluginEditor<G, A, R>>();

    private static final long serialVersionUID = 1L;

    /**
     * Creates a PluginManager.
     * @param pluginController the script controller to base this script manager
     * on
     * @param systemIcons the system icons for creating icons
     */
    public PluginManager(final PluginController<G, A, R> pluginController, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory, @NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, @NotNull final SystemIcons systemIcons) {
        super("Editor plugins management");
        this.pluginController = pluginController;
        this.pluginModel = pluginModel;
        this.pluginParameterViewFactory = pluginParameterViewFactory;
        this.pluginParameterFactory = pluginParameterFactory;
        this.systemIcons = systemIcons;
        getContentPane().setLayout(new BorderLayout());
        final Container left = new JPanel(new BorderLayout());
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(scriptPanel, BorderLayout.CENTER);
        final ListModel listModel = new AbstractListModel() {

            /**
             * The serial version UID.
             */
            private static final long serialVersionUID = 1L;

            /**
             * The script control listener to be notified about changes in {@link
             * PluginManager#pluginController}.
             */
            private final PluginModelListener<G, A, R> scriptModelListener = new PluginModelListener<G, A, R>() {

                @Override
                public void pluginCreated(@NotNull final Plugin<G, A, R> plugin) {
                    fireContentsChanged(scripts, 0, PluginManager.this.pluginModel.getPluginCount() + 1);
                }

                @Override
                public void pluginDeleted(@NotNull final Plugin<G, A, R> plugin) {
                    fireContentsChanged(scripts, 0, PluginManager.this.pluginModel.getPluginCount() + 1);
                }

                @Override
                public void pluginRegistered(@NotNull final Plugin<G, A, R> plugin) {
                    fireContentsChanged(scripts, 0, PluginManager.this.pluginModel.getPluginCount() + 1);
                }

                @Override
                public void pluginUnregistered(@NotNull final Plugin<G, A, R> plugin) {
                    fireContentsChanged(scripts, 0, PluginManager.this.pluginModel.getPluginCount() + 1);
                }

            };

            {
                PluginManager.this.pluginModel.addPluginModelListener(scriptModelListener);
            }

            @Nullable
            @Override
            public Object getElementAt(final int index) {
                return PluginManager.this.pluginModel.getPlugin(index);
            }

            @Override
            public int getSize() {
                return PluginManager.this.pluginModel.getPluginCount();
            }
        };
        scripts = new JList(listModel);
        scripts.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    //JList does not use type parameters
                    @SuppressWarnings("unchecked")
                    final Plugin<G, A, R> plugin = (Plugin<G, A, R>) scripts.getSelectedValue();
                    showScript(plugin);
                }
            }
        });
        scripts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scripts.setBorder(new LineBorder(Color.black, 1));
        left.add(scripts, BorderLayout.CENTER);
        final AbstractButton addScriptBtn = new JButton("New...");
        addScriptBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final String name = JOptionPane.showInputDialog(scripts, "Name of the new Beanshell plugin?");
                if (name != null) {
                    final Plugin<G, A, R> plugin = new Plugin<G, A, R>(name, pluginParameterFactory);
                    plugin.setCode("//input your beanshell Code");
                    if (PluginManager.this.pluginModel.addPlugin(plugin)) {
                        showScript(plugin);
                    } else {
                        JOptionPane.showMessageDialog(scripts, "The script " + name + " already exists.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        final AbstractButton removeScriptBtn = new JButton("Remove");
        removeScriptBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                //JList does not use type parameters
                @SuppressWarnings("unchecked")
                final Plugin<G, A, R> plugin = (Plugin<G, A, R>) scripts.getSelectedValue();
                if (plugin == null) {
                    return;
                }

                if (JOptionPane.showConfirmDialog(scripts, "remove script name " + plugin.getName(), "remove", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    PluginManager.this.pluginModel.removePlugin(plugin);
                    scripts.setSelectedIndex(0);
                    removeScript(plugin);
                }
            }
        });
        final Container bottomLeft = new JPanel(new GridLayout(2, 1));
        bottomLeft.add(addScriptBtn);
        bottomLeft.add(removeScriptBtn);
        left.add(bottomLeft, BorderLayout.SOUTH);
        scripts.setSelectedIndex(0);
        pack();
        setVisible(true);
    }

    private void showScript(final Plugin<G, A, R> plugin) {
        /* using a card layout is a necessary trick as
         * simply removing previous component and putting
         * the new one in the JPanel lead to problem unless
         * we recreate the component each time
         * (the validate process goes strangely and only portions
         * of component are redrawn until window resize :s)
         */
        if (plugin == null) {
            return;
        }

        PluginEditor<G, A, R> pluginEditor = components.get(plugin);
        if (pluginEditor == null) {
            pluginEditor = new PluginEditor<G, A, R>(pluginParameterFactory, plugin, pluginController, pluginModel, pluginParameterViewFactory, systemIcons);
            components.put(plugin, pluginEditor);
            scriptPanel.add(pluginEditor, Integer.toString(pluginEditor.hashCode()));
        }
        scriptLayout.show(scriptPanel, Integer.toString(pluginEditor.hashCode()));
    }

    private void removeScript(final Plugin<G, A, R> plugin) {
        final Component scriptEditor = components.get(plugin);
        if (scriptEditor != null) {
            components.remove(plugin);
            scriptPanel.remove(scriptEditor);
        }
    }

}
