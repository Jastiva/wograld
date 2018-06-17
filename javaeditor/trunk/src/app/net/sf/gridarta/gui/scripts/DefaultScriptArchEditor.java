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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.scripts.ScriptArchData;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.scripts.ScriptUtils;
import net.sf.gridarta.model.scripts.ScriptedEvent;
import net.sf.gridarta.model.scripts.ScriptedEventFactory;
import net.sf.gridarta.model.scripts.UndefinedEventArchetypeException;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import net.sf.gridarta.utils.FileChooserUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.util.Arrays2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultScriptArchEditor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements ScriptArchEditor<G, A, R> {

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link ScriptArchUtils} instance to use.
     */
    @NotNull
    private final ScriptArchUtils scriptArchUtils;

    /**
     * The ending for scripts.
     */
    @NotNull
    private final String scriptEnding;

    /**
     * The {@link PathManager} for converting path names.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link ScriptEditControl} to use.
     */
    @Nullable
    private ScriptEditControl scriptEditControl;

    @NotNull
    private final JComboBox eventTypeBox;

    @NotNull
    private final FileFilter scriptFileFilter;

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapManager} to use.
     */
    @NotNull
    private final MapManager<?, ?, ?> mapManager;

    @NotNull
    private final JComboBox pluginNameBox;

    // popup frame for new scripts:

    @Nullable
    private JDialog newScriptFrame;

    @NotNull
    private JLabel headingLabel;

    @NotNull
    private JTextComponent inputScriptPath;

    @NotNull
    private JTextComponent inputOptions;

    @NotNull
    private PathButtonListener<G, A, R> nsOkListener;

    /**
     * The {@link ScriptedEventFactory} instance to use.
     */
    @NotNull
    private final ScriptedEventFactory<G, A, R> scriptedEventFactory;

    /**
     * Creates a new instance.
     * @param scriptedEventFactory the scripted event factory instance to use
     * @param scriptEnding the suffix for script files
     * @param name the default event type
     * @param scriptArchUtils the script arch utils to use
     * @param scriptFileFilter the script file filter to use
     * @param globalSettings the global settings to use
     * @param mapManager the map manager instance to use
     * @param pathManager the path manager for converting path names
     */
    public DefaultScriptArchEditor(@NotNull final ScriptedEventFactory<G, A, R> scriptedEventFactory, final String scriptEnding, final String name, @NotNull final ScriptArchUtils scriptArchUtils, @NotNull final FileFilter scriptFileFilter, @NotNull final GlobalSettings globalSettings, @NotNull final MapManager<?, ?, ?> mapManager, @NotNull final PathManager pathManager) {
        this.scriptedEventFactory = scriptedEventFactory;
        this.scriptEnding = scriptEnding;
        this.scriptArchUtils = scriptArchUtils;
        this.pathManager = pathManager;

        pluginNameBox = new JComboBox(new String[] { name });
        pluginNameBox.setSelectedIndex(0);

        eventTypeBox = createEventTypeBox(scriptArchUtils);
        this.scriptFileFilter = scriptFileFilter;
        this.globalSettings = globalSettings;
        this.mapManager = mapManager;
    }

    @NotNull
    private static JComboBox createEventTypeBox(@NotNull final ScriptArchUtils scriptArchUtils) {
        final String[] valuesArray = scriptArchUtils.getEventNames();
        final JComboBox tmpEventTypeBox = new JComboBox(valuesArray);
        tmpEventTypeBox.setSelectedIndex(Arrays2.linearEqualitySearch("say", valuesArray));
        return tmpEventTypeBox;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public void setScriptEditControl(@Nullable final ScriptEditControl scriptEditControl) {
        this.scriptEditControl = scriptEditControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEventScript(final G gameObject, final ScriptArchData<G, A, R> scriptArchData, @NotNull final Frame parent) {
        final String archName = gameObject.getBestName();
        // create a reasonable default script name for lazy users :-)
        final String defScriptName = ScriptUtils.chooseDefaultScriptName(mapManager.getLocalMapDir(), archName, scriptEnding, pathManager);

        if (newScriptFrame == null) {
            // initialize popup frame
            newScriptFrame = new JDialog(parent, "New Scripted Event", true);
            newScriptFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            final JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5));

            // first line: heading
            final Container line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            headingLabel = new JLabel("New scripted event for \"" + archName + "\":");
            headingLabel.setForeground(Color.black);
            line1.add(headingLabel);
            mainPanel.add(line1);

            // event type
            mainPanel.add(Box.createVerticalStrut(10));
            final Container line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final Component typeLabel = new JLabel("Event type:");
            line2.add(typeLabel);
            line2.add(eventTypeBox);
            //mainPanel.add(line2);
            line2.add(Box.createHorizontalStrut(10));

            // plugin name
            final Component pluginLabel = new JLabel("Plugin:");
            line2.add(pluginLabel);
            line2.add(pluginNameBox);
            mainPanel.add(line2);

            // path
            mainPanel.add(Box.createVerticalStrut(5));
            final Container line3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final Component scriptFileLabel = new JLabel("Script file:");
            line3.add(scriptFileLabel);
            mainPanel.add(line3);
            inputScriptPath = new JTextField(defScriptName, 20);
            final AbstractButton browseButton = new JButton("...");
            browseButton.setMargin(new Insets(0, 10, 0, 10));
            browseButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final File home = mapManager.getLocalMapDir();

                    final JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Select Script File");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    FileChooserUtils.setCurrentDirectory(fileChooser, home);
                    fileChooser.setMultiSelectionEnabled(false);
                    fileChooser.setFileFilter(scriptFileFilter);

                    if (fileChooser.showOpenDialog(newScriptFrame) == JFileChooser.APPROVE_OPTION) {
                        // user has selected a file
                        final File f = fileChooser.getSelectedFile();
                        inputScriptPath.setText(ScriptUtils.localizeEventPath(mapManager.getLocalMapDir(), f, globalSettings.getMapsDirectory()));
                    }
                }
            });
            line3.add(inputScriptPath);
            line3.add(browseButton);
            mainPanel.add(line3);

            // options
            mainPanel.add(Box.createVerticalStrut(5));
            final Container line4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            line4.add(new JLabel("Script options:"));
            inputOptions = new JTextField("", 20);
            line4.add(inputOptions);
            mainPanel.add(line4);

            // description
            final Container line5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            final Component label1 = new JLabel("When you specify an existing file, the new event will be linked");
            textPanel.add(label1);
            final Component label2 = new JLabel("to that existing script. Otherwise a new script file is created.");
            textPanel.add(label2);
            line5.add(textPanel);
            mainPanel.add(line5);

            // button panel:
            mainPanel.add(Box.createVerticalStrut(10));
            final Container line6 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            final AbstractButton nsOkButton = new JButton("OK");
            nsOkListener = new PathButtonListener<G, A, R>(true, newScriptFrame, scriptArchData, gameObject, this, null);
            nsOkButton.addActionListener(nsOkListener);
            line6.add(nsOkButton);

            final AbstractButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new PathButtonListener<G, A, R>(false, newScriptFrame, null, null, this, null));
            line6.add(cancelButton);
            mainPanel.add(line6);

            newScriptFrame.getContentPane().add(mainPanel);
            newScriptFrame.pack();
            newScriptFrame.setLocationRelativeTo(parent);
            newScriptFrame.setVisible(true);
        } else {
            // just set fields and show
            headingLabel.setText("New scripted event for \"" + archName + "\":");
            inputScriptPath.setText(defScriptName);
            inputOptions.setText("");
            nsOkListener.setScriptArchData(scriptArchData, gameObject);
            newScriptFrame.toFront();
            newScriptFrame.setVisible(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNewEvent(@NotNull final JDialog frame, @NotNull final ScriptArchData<G, A, R> scriptArchData, @NotNull final G gameObject) {
        final StringBuilder scriptPath = new StringBuilder(inputScriptPath.getText().trim().replace('\\', '/'));
        final String options = inputOptions.getText().trim();
        final int eventType = scriptArchUtils.indexToEventType(eventTypeBox.getSelectedIndex());
        final String pluginName = ((String) pluginNameBox.getSelectedItem()).trim();

        final File localMapDir = mapManager.getLocalMapDir();

        // first check if that event type is not already in use
        final GameObject<G, A, R> replaceObject = scriptArchData.getScriptedEvent(eventType, gameObject);
        if (replaceObject != null) {
            // collision with existing event -> ask user: replace?
            if (JOptionPane.showConfirmDialog(frame, "An event of type \"" + scriptArchUtils.typeName(eventType) + "\" already exists for this object.\n" + "Do you want to replace the existing event?", "Event exists", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.NO_OPTION) {
                // bail out
                return;
            }
        }

        String absScriptPath;
        if (scriptPath.length() > 0 && scriptPath.charAt(0) == '/') {
            // script path is absolute
            final File mapDir = globalSettings.getMapsDirectory(); // global map directory
            if (!mapDir.exists()) {
                // if map dir doesn't exist, this is not going to work
                frame.setVisible(false);
                ACTION_BUILDER.showMessageDialog(frame, "mapDirDoesntExist", mapDir);
                return;
            }

            absScriptPath = mapDir.getAbsolutePath() + scriptPath;
        } else {
            // script path is relative
            absScriptPath = localMapDir.getAbsolutePath() + "/" + scriptPath;
        }

        // now check if the specified path points to an existing script
        File newScriptFile = new File(absScriptPath);
        if (!newScriptFile.exists() && !absScriptPath.endsWith(scriptEnding)) {
            absScriptPath += scriptEnding;
            scriptPath.append(scriptEnding);
            newScriptFile = new File(absScriptPath);
        }

        if (newScriptFile.exists()) {
            if (newScriptFile.isFile()) {
                // file exists -> link it to the event
                final ScriptedEvent<G, A, R> event;
                try {
                    event = scriptedEventFactory.newScriptedEvent(eventType, pluginName, scriptPath.toString(), options);
                } catch (final UndefinedEventArchetypeException ex) {
                    JOptionPane.showMessageDialog(frame, "Cannot create event of type " + eventType + ":\n" + ex.getMessage() + ".", "Cannot create event", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (replaceObject != null) {
                    replaceObject.remove();
                }
                gameObject.addLast(event.getEventArch());
                frame.setVisible(false); // close dialog
            }

            return;
        }

        if (!absScriptPath.endsWith(scriptEnding)) {
            absScriptPath += scriptEnding;
            scriptPath.append(scriptEnding);
            newScriptFile = new File(absScriptPath);
        }

        // file does not exist -> aks user: create new file?
        if (JOptionPane.showConfirmDialog(frame, "Create new script '" + newScriptFile.getName() + "'?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        boolean couldCreateFile = false; // true when file creation successful
        try {
            // try to create new empty file
            couldCreateFile = newScriptFile.createNewFile();
        } catch (final IOException e) {
            /* ignore (really?) */
        }

        if (!couldCreateFile) {
            JOptionPane.showMessageDialog(frame, "File '" + newScriptFile.getName() + "' could not be created.\n" + "Please check your path and write permissions.", "Cannot create file", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // file has been created, now link it to the event
        final ScriptedEvent<G, A, R> event;
        try {
            event = scriptedEventFactory.newScriptedEvent(eventType, pluginName, scriptPath.toString(), options);
        } catch (final UndefinedEventArchetypeException ex) {
            JOptionPane.showMessageDialog(frame, "Cannot create event of type " + eventType + ":\n" + ex.getMessage() + ".", "Cannot create event", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (replaceObject != null) {
            replaceObject.remove();
        }
        gameObject.addLast(event.getEventArch());
        frame.setVisible(false); // close dialog

        // open new script file
        if (scriptEditControl != null) {
            scriptEditControl.openScriptFile(newScriptFile.getAbsolutePath());
        }
    }

}
