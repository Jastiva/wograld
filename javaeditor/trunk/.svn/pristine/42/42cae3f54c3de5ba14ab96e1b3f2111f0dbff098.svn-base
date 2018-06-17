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
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.gridarta.gui.dialog.plugin.parameter.ParameterDescriptionEditor;
import net.sf.gridarta.gui.dialog.plugin.parameter.ParameterNameEditor;
import net.sf.gridarta.gui.dialog.plugin.parameter.ParameterTypeEditor;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterView;
import net.sf.gridarta.gui.dialog.plugin.parameter.PluginParameterViewFactory;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.Plugin;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.parameter.NoSuchParameterException;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.utils.SystemIcons;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jetbrains.annotations.NotNull;

public class PluginEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(PluginEditor.class);

    @NotNull
    private final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory;

    @NotNull
    private final JPanel paramTable;

    @NotNull
    private final AbstractButton removeParameter;

    @NotNull
    private final RSyntaxTextArea code;

    @NotNull
    private final PluginParameterFactory<G, A, R> pluginParameterFactory;

    @NotNull
    private final Plugin<G, A, R> plugin;

    @NotNull
    private final Map<PluginParameter<G, A, R>, ParameterNameEditor<G, A, R>> paramNameEditors = new HashMap<PluginParameter<G, A, R>, ParameterNameEditor<G, A, R>>();

    @NotNull
    private final Map<PluginParameter<G, A, R>, ParameterDescriptionEditor<G, A, R>> paramDescriptionEditors = new HashMap<PluginParameter<G, A, R>, ParameterDescriptionEditor<G, A, R>>();

    @NotNull
    private final Map<PluginParameter<G, A, R>, ParameterTypeEditor<G, A, R>> paramTypeEditors = new HashMap<PluginParameter<G, A, R>, ParameterTypeEditor<G, A, R>>();

    @NotNull
    private final Map<PluginParameter<G, A, R>, PluginParameterView<G, A, R>> paramViews = new HashMap<PluginParameter<G, A, R>, PluginParameterView<G, A, R>>();

    private int selectedRow = -1;

    @NotNull
    private final MouseListener cellMouseListener = new MouseListener() {

        @Override
        public void mouseClicked(final MouseEvent e) {
            selectTableComponent((Component) e.getSource());
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            selectTableComponent((Component) e.getSource());
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            selectTableComponent((Component) e.getSource());
        }
    };

    @NotNull
    private final FocusListener cellFocusListener = new FocusListener() {

        @Override
        public void focusGained(final FocusEvent e) {
            selectTableComponent((Component) e.getSource());
        }

        @Override
        public void focusLost(final FocusEvent e) {
        }
    };

    @NotNull
    private final AbstractButton typeAutoRun;

    @NotNull
    private final AbstractButton typeFilter;

    @NotNull
    private final AbstractButton typeBash;

    /**
     * Create a visual JComponent used to edit the given script.
     * @param plugin the script object to edit
     * @param pluginController the associated script control instance
     * @param systemIcons the system icons for creating icons
     */
    public PluginEditor(@NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory, @NotNull final Plugin<G, A, R> plugin, @NotNull final PluginController<G, A, R> pluginController, @NotNull final PluginModel<G, A, R> pluginModel, @NotNull final PluginParameterViewFactory<G, A, R> pluginParameterViewFactory, @NotNull final SystemIcons systemIcons) {
        this.pluginParameterFactory = pluginParameterFactory;
        this.plugin = plugin;
        this.pluginParameterViewFactory = pluginParameterViewFactory;
        final JTabbedPane tabs = new JTabbedPane();
        setLayout(new BorderLayout());
        add(tabs);
        final Container optionsTab = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        optionsTab.add(new JLabel("Plugin run mode", SwingConstants.LEFT), gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        optionsTab.add(new JPanel(), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        final Component autoRunIcon = new JLabel(systemIcons.getAutoRunSmallIcon());
        optionsTab.add(autoRunIcon, gbc);
        gbc.gridy = 2;
        final Component filterIcon = new JLabel(systemIcons.getFilterSmallIcon());
        optionsTab.add(filterIcon, gbc);
        gbc.gridy = 3;
        final Component runPluginIcon = new JLabel(systemIcons.getRunPluginSmallIcon());
        optionsTab.add(runPluginIcon, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        typeAutoRun = new JCheckBox("auto-run at application startup");
        typeAutoRun.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                plugin.setAutoBoot(typeAutoRun.isSelected());
            }
        });
        typeAutoRun.setSelected(plugin.isAutoBoot());
        optionsTab.add(typeAutoRun, gbc);
        gbc.gridy = 2;
        typeFilter = new JCheckBox("reference in the filters list");
        typeFilter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                plugin.setFilter(typeFilter.isSelected());
            }
        });
        typeFilter.setSelected(plugin.isFilter());
        optionsTab.add(typeFilter, gbc);
        gbc.gridy = 3;
        typeBash = new JCheckBox("reference in the manual run list");
        typeBash.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                plugin.setScript(typeBash.isSelected());
            }
        });
        typeBash.setSelected(plugin.isScript());
        optionsTab.add(typeBash, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        final AbstractButton registerButton = new JButton("re-register script");
        registerButton.setToolTipText("Force plugin manager to unregister this plugin from filter list, launch the auto-run (if plugin is auto-start) and re-register it (if filter plugin)");
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                pluginModel.reRegister(plugin);
            }
        });
        optionsTab.add(registerButton, gbc);
        tabs.add("Options", optionsTab);
        gbc.gridy = 5;
        final AbstractButton exportButton = new JButton("Export script...");
        exportButton.setToolTipText("Export the specified plugin as XML (for distribution)");
        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                pluginController.savePluginAs(plugin, false);
            }
        });
        optionsTab.add(exportButton, gbc);
        tabs.add("Options", optionsTab);
        final Container parameterTab = new JPanel(new BorderLayout());
        paramTable = new JPanel(new GridBagLayout());
        redrawTable();
        plugin.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                redrawTable();
            }
        });
        final Component scrollPane1 = new JScrollPane(paramTable);
        parameterTab.add(scrollPane1, BorderLayout.CENTER);
        //parameterTab.add(paramTable, BorderLayout.CENTER);
        final Container paramButtons = new JPanel();
        paramButtons.setLayout(new StackLayout(5));
        final AbstractButton addParameter = new JButton("Add parameter");
        addParameter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                plugin.newParameter();
            }
        });
        removeParameter = new JButton("Remove parameter");
        removeParameter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (selectedRow < 0) {
                    return;
                }

                final PluginParameter<G, A, R> parameter;
                try {
                    parameter = plugin.getParameter(selectedRow);
                } catch (final NoSuchParameterException ignored) {
                    return;
                }
                final String name = parameter.getName();
                if (JOptionPane.showConfirmDialog(removeParameter, "Delete " + name + "?", "Delete?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    plugin.removeParameter(selectedRow);
                }
            }
        });
        paramButtons.add(addParameter);
        paramButtons.add(removeParameter);
        parameterTab.add(paramButtons, BorderLayout.EAST);
        tabs.addTab("Manual run parameters", parameterTab);
        code = new RSyntaxTextArea();
        code.setText(plugin.getCode());
        code.setFont(new Font("Monospaced", Font.PLAIN, 14));
        code.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(final DocumentEvent e) {
                plugin.setCode(code.getText());
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                plugin.setCode(code.getText());
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                plugin.setCode(code.getText());
            }
        });
        code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        final Component scrollPane2 = new RTextScrollPane(code);
        final Container codePanel = new JPanel(new BorderLayout());
        codePanel.add(scrollPane2, BorderLayout.CENTER);
        final Container codeBottom = new JPanel(new FlowLayout());
        final AbstractButton test = new JButton("Run Script...");
        test.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                pluginController.runPlugin(plugin);
            }
        });
        codeBottom.add(test);
        codePanel.add(codeBottom, BorderLayout.SOUTH);
        tabs.addTab("Code", codePanel);
    }

    public void selectTableComponent(@NotNull final Component c) {
        final GridBagLayout l = (GridBagLayout) paramTable.getLayout();
        final GridBagConstraints gbc = l.getConstraints(c);
        if (gbc != null) {
            selectedRow = gbc.gridy - 1;
        }
    }

    private void redrawTable() {
        final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 5, 0);
        paramTable.removeAll();
        paramTable.add(new JLabel("Name"), gbc);
        gbc.gridx = 1;
        paramTable.add(new JLabel("Description"), gbc);
        gbc.gridx = 2;
        paramTable.add(new JLabel("Type"), gbc);
        gbc.gridx = 3;
        paramTable.add(new JLabel("Config"), gbc);
        gbc.gridx = 4;
        paramTable.add(new JLabel("Default"), gbc);
        int i = 0;
        for (final PluginParameter<G, A, R> parameter : plugin) {
            gbc.gridy = i + 1;
            putRow(gbc, parameter);
            i++;
        }
        repaint();
    }

    private void newTableComponent(@NotNull final Component c) {
        c.addFocusListener(cellFocusListener);
        c.addMouseListener(cellMouseListener);
    }

    private Component getParameterNameEditor(@NotNull final PluginParameter<G, A, R> param) {
        if (log.isDebugEnabled()) {
            log.debug("Doing " + param);
        }

        final Component existingParameterNameEditor = paramNameEditors.get(param);
        if (existingParameterNameEditor != null) {
            return existingParameterNameEditor;
        }

        final ParameterNameEditor<G, A, R> newParameterNameEditor = new ParameterNameEditor<G, A, R>(param);
        paramNameEditors.put(param, newParameterNameEditor);
        newTableComponent(newParameterNameEditor);
        return newParameterNameEditor;
    }

    private Component getParameterDescriptionEditor(@NotNull final PluginParameter<G, A, R> param) {
        final Component existingParameterDescriptionEditor = paramDescriptionEditors.get(param);
        if (existingParameterDescriptionEditor != null) {
            return existingParameterDescriptionEditor;
        }

        final ParameterDescriptionEditor<G, A, R> newParameterDescriptionEditor = new ParameterDescriptionEditor<G, A, R>(param);
        newTableComponent(newParameterDescriptionEditor);
        paramDescriptionEditors.put(param, newParameterDescriptionEditor);
        return newParameterDescriptionEditor;
    }

    private Component getParameterTypeEditor(@NotNull final PluginParameter<G, A, R> param) {
        final Component existingParameterTypeEditor = paramTypeEditors.get(param);
        if (existingParameterTypeEditor != null) {
            return existingParameterTypeEditor;
        }

        final ParameterTypeEditor<G, A, R> newParameterTypeEditor = new ParameterTypeEditor<G, A, R>(pluginParameterFactory, plugin, param);
        paramTypeEditors.put(param, newParameterTypeEditor);
        newTableComponent(newParameterTypeEditor);
        return newParameterTypeEditor;
    }

    private PluginParameterView<G, A, R> getParameterView(@NotNull final PluginParameter<G, A, R> param) {
        final PluginParameterView<G, A, R> existingPluginParameterView = paramViews.get(param);
        if (existingPluginParameterView != null) {
            return existingPluginParameterView;
        }

        final PluginParameterView<G, A, R> newPluginParameterView = pluginParameterViewFactory.getView(paramTable, param);

        newTableComponent(newPluginParameterView.getConfigComponent());
        newTableComponent(newPluginParameterView.getValueComponent());

        paramViews.put(param, newPluginParameterView);
        return newPluginParameterView;
    }

    private void putRow(@NotNull final GridBagConstraints gbc, @NotNull final PluginParameter<G, A, R> param) {
        gbc.gridx = 0;
        paramTable.add(getParameterNameEditor(param), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        paramTable.add(getParameterDescriptionEditor(param), gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.1;
        paramTable.add(getParameterTypeEditor(param), gbc);
        gbc.gridx = 3;
        paramTable.add(getParameterView(param).getConfigComponent(), gbc);
        gbc.gridx = 4;
        paramTable.add(getParameterView(param).getValueComponent(), gbc);
    }

}
