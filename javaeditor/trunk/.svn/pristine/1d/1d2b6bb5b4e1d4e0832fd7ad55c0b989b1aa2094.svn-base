/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.textedit.scripteditor;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.geom.RectangularShape;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import net.sf.gridarta.textedit.textarea.SyntaxDocument;
import net.sf.gridarta.textedit.textarea.TextAreaDefaults;
import net.sf.gridarta.textedit.textarea.tokenmarker.TokenMarkerFactory;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.ExiterListener;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The script editor frame. This class should only exist in ScriptEditControl.
 * No other class should refer to it.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class ScriptEditView extends JDialog {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(ScriptEditView.class);

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link TextAreaDefaults} for tabs.
     */
    @NotNull
    private TextAreaDefaults textAreaDefaults;

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The key used to store the editor window x-coordinate in preferences
     * file.
     */
    @NotNull
    private static final String WINDOW_X = "ScriptEditWindow.x";

    /**
     * The key used to store the editor window y-coordinate in preferences
     * file.
     */
    @NotNull
    private static final String WINDOW_Y = "ScriptEditWindow.y";

    /**
     * The key used to store the editor window width in preferences file.
     */
    @NotNull
    private static final String WINDOW_WIDTH = "ScriptEditWindow.width";

    /**
     * The key used to store the editor window height in preferences file.
     */
    @NotNull
    private static final String WINDOW_HEIGHT = "ScriptEditWindow.height";

    /**
     * The actions for the script editor.
     */
    @NotNull
    private final Actions actions;

    /**
     * The undo related actions for the script editor.
     */
    @NotNull
    private final ScriptEditUndoActions scriptEditUndoActions;

    @NotNull
    private final JTabbedPane tabPane;       // tab pane

    @NotNull
    private final List<JEditTextArea> textAreas;          // list of 'JEditTextArea' objects, same order as tabs

    /**
     * Builds a frame but keep it hidden (it is shown when first file is
     * opened).
     * @param owner the owner of this view
     * @param preferences the preferences to use
     * @param exiter the exiter instance
     */
    public ScriptEditView(@NotNull final ScriptEditControl control, @NotNull final Frame owner, @NotNull final Preferences preferences, @NotNull final Exiter exiter) {
        super(owner, "Script Pad");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        textAreas = new ArrayList<JEditTextArea>();
        actions = new Actions(control);
        scriptEditUndoActions = new ScriptEditUndoActions();
        setJMenuBar(ACTION_BUILDER.createMenuBar(true, "scriptEditMenu"));

        tabPane = new JTabbedPane(SwingConstants.TOP); // init tab pane
        tabPane.addChangeListener(new EditTabListener(this));

        getContentPane().add(tabPane);
        addWindowListener(new EditWindowListener(control)); // add listener for close box

        // calculate some default values in case there is no settings file
        final RectangularShape screen = getGraphicsConfiguration().getBounds();
        final int width = preferences.getInt(WINDOW_WIDTH, (int) (0.8 * screen.getWidth()));
        final int height = preferences.getInt(WINDOW_HEIGHT, (int) (0.8 * screen.getHeight()));
        final int x = preferences.getInt(WINDOW_X, (int) (screen.getX() + (screen.getWidth() - (double) width) / 2.0));
        final int y = preferences.getInt(WINDOW_Y, (int) (screen.getY() + (screen.getHeight() - (double) height) / 2.0));
        setBounds(x, y, width, height);

        final ExiterListener exiterListener = new ExiterListener() {

            @Override
            public void preExitNotify() {
                // ignore
            }

            @Override
            public void appExitNotify() {
                final Rectangle bounds = getBounds();
                preferences.putInt(WINDOW_X, bounds.x);
                preferences.putInt(WINDOW_Y, bounds.y);
                preferences.putInt(WINDOW_WIDTH, bounds.width);
                preferences.putInt(WINDOW_HEIGHT, bounds.height);
            }

            @Override
            public void waitExitNotify() {
                // ignore
            }

        };
        exiter.addExiterListener(exiterListener);
    }

    @Deprecated
    public void setTextAreaDefaults(@NotNull final TextAreaDefaults textAreaDefaults) {
        this.textAreaDefaults = textAreaDefaults;
    }

    /**
     * Adds a new TextArea Panel to the TabbedPane.
     * @param title title of this script (filename)
     * @param file file where this script is stored, null if new script opened
     */
    public void addTab(@NotNull final String title, @Nullable final File file) {
        final JEditTextArea ta = new JEditTextArea(textAreaDefaults); // open new TextArea
        //ta.setFont(new Font("Courier New", Font.PLAIN, 12));
        final SyntaxDocument syntaxDocument = new SyntaxDocument();
        ta.setDocument(syntaxDocument);
        textAreas.add(ta);
        ta.getDocument().setTokenMarker(TokenMarkerFactory.createTokenMarker(file));
        scriptEditUndoActions.addDocument(syntaxDocument);
        tabPane.addTab(title, ta);
        if (getTabCount() <= 1 || !isShowing()) {
            setVisible(true);
        }

        scriptEditUndoActions.setCurrentDocument(syntaxDocument);

        tabPane.setSelectedIndex(getTabCount() - 1);

        // very important: There must be a drawing update after showing the frame, to make
        // sure the graphics context is fully initialized before calling 'setEditingFocus()'
        //if (isFirstTimeShowing) {
        update(getGraphics());
        //}

        if (file != null && file.exists()) {
            // print file into this document
            try {
                final FileInputStream fis = new FileInputStream(file);
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader in = new BufferedReader(isr);
                        try {
                            boolean firstLine = true;
                            final StringBuilder buff = new StringBuilder("");
                            while (true) {
                                final String line = in.readLine();
                                if (line == null) {
                                    break;
                                }
                                if (firstLine) {
                                    firstLine = false;
                                } else {
                                    buff.append('\n');
                                }
                                buff.append(line);
                            }
                            ta.getDocument().insertString(0, buff.toString(), null);
                        } finally {
                            in.close();
                        }
                    } finally {
                        isr.close();
                    }
                } finally {
                    fis.close();
                }
                // insert buffer into the document
            } catch (final FileNotFoundException e) {
                log.info("addTab(): File '" + file.getName() + "' not found.");
            } catch (final IOException e) {
                log.info("addTab(): I/O-Error while reading '" + file.getName() + "'.");
            } catch (final BadLocationException e) {
                log.info("addTab(): Bad Location in Document!");
            }
            scriptEditUndoActions.resetUndo(syntaxDocument);
        }

        ta.setEditingFocus(); // set focus to TextArea in order to respond to key press events
        //ta.scrollToCaret();   // make sure the window shows caret (top left corner)

        ta.resetModified();

        toFront(); // bring window to front
    }

    /**
     * Closes the active script-tab.
     */
    public void closeActiveTab() {
        if (textAreas.isEmpty()) {
            setVisible(false);
        } else {
            final int oldIndex = tabPane.getSelectedIndex();
            final int newIndex = oldIndex >= tabPane.getTabCount() - 1 ? oldIndex - 1 : oldIndex;
            tabPane.setSelectedIndex(newIndex);
            final JEditTextArea textArea = textAreas.get(oldIndex);
            scriptEditUndoActions.removeDocument(textArea.getDocument());
            textAreas.remove(oldIndex);
            tabPane.remove(oldIndex);
            actions.refresh();
            scriptEditUndoActions.setCurrentDocument(newIndex == -1 ? null : textAreas.get(newIndex).getDocument());
        }
    }

    /**
     * @return the currently active TextArea (in front)
     */
    @Nullable
    public JEditTextArea getActiveTextArea() {
        if (getTabCount() > 0) {
            return textAreas.get(tabPane.getSelectedIndex());
        }

        return null; // no window is open
    }

    /**
     * @return the index of the selected tab pane
     */
    public int getSelectedIndex() {
        return tabPane.getSelectedIndex();
    }

    /**
     * @return the number of open tabs
     */
    public int getTabCount() {
        return tabPane.getTabCount();
    }

    /**
     * Sets the title of the tab at specified index.
     * @param index the index of the tab to change title
     * @param title the new title string
     */
    public void setTitleAt(final int index, @NotNull final String title) {
        tabPane.setTitleAt(index, title);
    }

    /**
     * Returns the title of the active tab.
     * @return the title of the active tab, or <code>null</code> if no tab
     *         exists
     */
    @Nullable
    public String getActiveTitle() {
        return getTabCount() > 0 ? tabPane.getTitleAt(tabPane.getSelectedIndex()) : null;
    }

    /**
     * Shows the given confirmation message as popup frame. The message is a
     * yes/no option. The parent frame is disabled until the user picks an
     * answer.
     * @param title the title of the message
     * @param message the message to be shown
     * @return true if the user agrees, false if user disagrees
     */
    public boolean askConfirm(@NotNull final String title, @NotNull final String message) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Shows the given message in the UI.
     * @param title the title of the message
     * @param message the message to be shown
     * @param messageType type of message (see JOptionPane constants), defines
     * icon used
     */
    public void showMessage(@NotNull final String title, @NotNull final String message, final int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public void showMessage(@NotNull final String title, @NotNull final String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Inner class: Listener for ChangeEvents in the tabPane.
     */
    private class EditTabListener implements ChangeListener {

        @NotNull
        private final ScriptEditView view;       // view

        private int index;                 // index of selected tab

        private EditTabListener(@NotNull final ScriptEditView view) {
            this.view = view;
            index = view.getSelectedIndex();
        }

        @Override
        public void stateChanged(@NotNull final ChangeEvent e) {
            if (index != view.getSelectedIndex()) {
                index = view.getSelectedIndex();
                // active selected tab has changed
                actions.refresh(); // refresh state of menus
                scriptEditUndoActions.setCurrentDocument(index == -1 ? null : textAreas.get(index).getDocument());
            }
        }

    }

}
