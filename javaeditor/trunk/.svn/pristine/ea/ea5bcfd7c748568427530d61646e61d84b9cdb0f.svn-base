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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterView;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterViewFactory;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.Plugin;
import net.sf.gridarta.plugin.PluginConsole;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginModelListener;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.utils.SystemIcons;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * View for Scripts.
 * @author tchize
 */
public class PluginView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(PluginView.class);

    @NotNull
    private final PluginController<G, A, R> pluginController;

    /**
     * The script model.
     */
    @NotNull
    private final Iterable<Plugin<G, A, R>> pluginModel;

    @NotNull
    private final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory;

    /**
     * The {@link SystemIcons} for creating icons.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * The menu to add script commands to.
     */
    @Nullable
    private JMenu menuScripts;

    @Nullable
    private JFrame console;

    @Nullable
    private CloseableTabbedPane consolePane;

    /**
     * The object used for synchronization.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * The script control listener to be notified about changes in {@link
     * #pluginController}.
     */
    @NotNull
    private final PluginModelListener<G, A, R> pluginModelListener = new PluginModelListener<G, A, R>() {

        @Override
        public void pluginCreated(@NotNull final Plugin<G, A, R> plugin) {
            updateMenuEntries();
        }

        @Override
        public void pluginDeleted(@NotNull final Plugin<G, A, R> plugin) {
            updateMenuEntries();
        }

        @Override
        public void pluginRegistered(@NotNull final Plugin<G, A, R> plugin) {
            updateMenuEntries();
        }

        @Override
        public void pluginUnregistered(@NotNull final Plugin<G, A, R> plugin) {
            updateMenuEntries();
        }

    };

    /**
     * Creates a PluginView.
     * @param pluginController controller of this PluginView
     * @param pluginModel the script model
     * @param systemIcons the system icons for creating icons
     * @warning Creating a view from a controller instead of a model is error
     * prone.
     */
    public PluginView(@NotNull final PluginController<G, A, R> pluginController, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory, @NotNull final SystemIcons systemIcons) {
        this.pluginController = pluginController;
        this.pluginModel = pluginModel;
        this.pluginParameterViewFactory = pluginParameterViewFactory;
        this.systemIcons = systemIcons;
        pluginModel.addPluginModelListener(pluginModelListener);
    }

    /**
     * Set the menu to add script commands to. Entries already present in the
     * menu are deleted.
     * @param menuScripts the plugins menu
     */
    public void setMenu(@Nullable final JMenu menuScripts) {
        synchronized (sync) {
            this.menuScripts = menuScripts;
            updateMenuEntries();
        }
    }

    /**
     * Refresh the menu entries.
     */
    private void updateMenuEntries() {
        synchronized (sync) {
            final JMenu tmpMenuScripts = menuScripts;
            if (tmpMenuScripts == null) {
                return;
            }

            MenuUtils.removeAllToSeparator(tmpMenuScripts);

            int index = 0;
            for (final Plugin<G, A, R> plugin : pluginModel) {
                if (!plugin.isScript()) {
                    continue;
                }

                final Action action = pluginController.createRunAction(plugin);
                final AbstractButton item = new JMenuItem(action);
                item.setActionCommand(plugin.getName());
                tmpMenuScripts.add(item, index++);
            }
            if (index == 0) {
                final Component def = new JMenuItem("no scripts available");
                def.setEnabled(false);
                tmpMenuScripts.add(def, 0);
            }
        }
    }

    private void showConsoleFrame() {
        synchronized (sync) {
            if (console == null) {
                console = new JFrame("Beanshell scripts I/O Console");
                console.setSize(400, 200);
                consolePane = new CloseableTabbedPane(systemIcons.getCloseTabSmallIcon());
                console.getContentPane().add(consolePane);
            }
            console.setVisible(true);
        }
    }

    @NotNull
    public PluginConsole createConsole(@NotNull final String name) {
        showConsoleFrame();
        final PluginConsole pluginConsole = new PluginConsole();
        consolePane.addCloseableTab(name, pluginConsole, true);
        return pluginConsole;
    }

    public boolean getRunValues(@NotNull final Plugin<G, A, R> plugin, @NotNull final Component parent) {
        if (!plugin.hasParameters()) {
            return true;
        }

        final JOptionPane p = new PluginViewPane();
        p.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        p.setMessageType(JOptionPane.QUESTION_MESSAGE);
        p.setMessage("Please provide runtime parameters for " + plugin.getName());
        final GridBagLayout layout = new GridBagLayout();
        final JComponent panel = new JPanel(layout);
        final JDialog dialog = p.createDialog(parent, plugin.getName());
        dialog.setModal(true);
        dialog.getContentPane().removeAll();
        int i = 0;
        for (final PluginParameter<G, A, R> parameter : plugin) {
            log.debug("adding parameter");
            final Component name = new JLabel(parameter.getName());
            final PluginParameterView<G, A, R> view = pluginParameterViewFactory.getView(panel, parameter);
            final JComponent val = view.getValueComponent();
            val.setToolTipText(parameter.getDescription());
            final GridBagConstraints gn = new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 5, 5);
            final GridBagConstraints gf = new GridBagConstraints(1, i, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 5, 5);
            panel.add(name, gn);
            panel.add(val, gf);
            i++;
        }

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(p, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setVisible(true);
        final Object result = p.getValue();
        if (result instanceof Integer) {
            if ((Integer) result == JOptionPane.YES_OPTION) {
                return true;
            }
        }
        return false;
    }

}
