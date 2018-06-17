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

package net.sf.gridarta.gui.scripts;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.scripts.ScriptedEvent;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptedEventEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The global settings instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link ScriptArchEditor} to use.
     */
    @NotNull
    private ScriptArchEditor<G, A, R> scriptArchEditor;

    /**
     * The {@link ScriptEditControl} to use.
     */
    @Nullable
    private ScriptEditControl scriptEditControl;

    @NotNull
    private JTextComponent inputScriptPath;

    @NotNull
    private JTextComponent inputPluginName;

    @NotNull
    private JTextComponent inputOptions;

    @NotNull
    private PathButtonListener<G, A, R> cancelListener;

    @NotNull
    private PathButtonListener<G, A, R> okListener;

    // popup frame to edit script paths:

    @Nullable
    private JDialog pathFrame;

    /**
     * Creates a new instance.
     * @param globalSettings the global settings instance
     */
    public ScriptedEventEditor(@NotNull final GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    @Deprecated
    public void setScriptArchEditor(@NotNull final ScriptArchEditor<G, A, R> scriptArchEditor) {
        this.scriptArchEditor = scriptArchEditor;
    }

    @Deprecated
    public void setScriptEditControl(@Nullable final ScriptEditControl scriptEditControl) {
        this.scriptEditControl = scriptEditControl;
    }

    /**
     * Opens the script pad to display a script.
     * @param mapManager the map manager instance
     * @param scriptPath the script path
     * @param parent the parent component for dialog boxes
     */
    public void openScript(@NotNull final MapManager<?, ?, ?> mapManager, @NotNull final String scriptPath, @NotNull final Component parent) {
        // trying to get the absolute path to script file:
        final File scriptFile;
        if (scriptPath.startsWith("/")) {
            // file path is absolute (to map base directory):
            scriptFile = new File(globalSettings.getMapsDirectory(), scriptPath.substring(1));
        } else {
            // file path is relative to map dir
            scriptFile = new File(mapManager.getLocalMapDir(), scriptPath);
        }

        // now see if that file really exists:
        if (scriptFile.exists() && scriptFile.isFile()) {
            final String path = scriptFile.getAbsolutePath();
            if (scriptEditControl != null) {
                scriptEditControl.openScriptFile(path);
            }
        } else {
            // file does not exist!
            ACTION_BUILDER.showMessageDialog(parent, "openScriptNotFound", scriptFile);
        }
    }

    /**
     * Edit path and plugin name for an event. A popup dialog is shown with
     * input text fields for file path and plugin name.
     * @param scriptedEvent the event
     * @param parent the parent frame for dialog boxes
     */
    public void editParameters(@NotNull final ScriptedEvent<G, A, R> scriptedEvent, @NotNull final Frame parent) {
        if (pathFrame == null) {
            // initialize popup frame
            pathFrame = new JDialog(parent, "Edit Parameters", true);
            pathFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            final JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5));

            // input line: script path
            final Container line1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            final Component text1 = new JLabel("Script:");
            line1.add(text1);
            inputScriptPath = new JTextField(scriptedEvent.getScriptPath(), 20);
            line1.add(inputScriptPath);
            mainPanel.add(line1);

            // input line: plugin options
            final Container line2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            final Component text2 = new JLabel("Script options:");
            line2.add(text2);
            inputOptions = new JTextField(scriptedEvent.getOptions(), 20);
            line2.add(inputOptions);
            mainPanel.add(line2);
            mainPanel.add(Box.createVerticalStrut(5));

            // input line: plugin name
            final Container line3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            final Component text3 = new JLabel("Plugin name:");
            line3.add(text3);
            inputPluginName = new JTextField(scriptedEvent.getPluginName(), 20);
            line3.add(inputPluginName);
            mainPanel.add(line3);
            mainPanel.add(Box.createVerticalStrut(5));

            // button panel:
            final Container line4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            final AbstractButton okButton = new JButton("OK");
            okListener = new PathButtonListener<G, A, R>(true, pathFrame, null, null, scriptArchEditor, scriptedEvent);
            okButton.addActionListener(okListener);
            line4.add(okButton);

            final AbstractButton cancelButton = new JButton("Cancel");
            cancelListener = new PathButtonListener<G, A, R>(false, pathFrame, null, null, scriptArchEditor, scriptedEvent);
            cancelButton.addActionListener(cancelListener);
            line4.add(cancelButton);
            mainPanel.add(line4);

            pathFrame.getContentPane().add(mainPanel);
            pathFrame.pack();
            pathFrame.setLocationRelativeTo(parent);
            pathFrame.setVisible(true);
        } else {
            // just set fields and show
            okListener.setTargetEvent(scriptedEvent);
            cancelListener.setTargetEvent(scriptedEvent);
            inputScriptPath.setText(scriptedEvent.getScriptPath());
            inputPluginName.setText(scriptedEvent.getPluginName());
            inputOptions.setText(scriptedEvent.getOptions());
            pathFrame.toFront();
            pathFrame.setVisible(true);
        }
    }

    @NotNull
    public String getInputScriptPath() {
        return inputScriptPath.getText().trim();
    }

    @NotNull
    public String getInputPluginName() {
        return inputPluginName.getText().trim();
    }

    @NotNull
    public String getInputOptions() {
        return inputOptions.getText().trim();
    }

}
