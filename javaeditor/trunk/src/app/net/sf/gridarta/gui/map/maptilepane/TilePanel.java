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

package net.sf.gridarta.gui.map.maptilepane;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.FileChooserUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tile panel displays exactly one direction for map tiling. It's basically an
 * extended text field with some additional buttons to make usage easier for
 * users.
 * @author unknown
 */
public class TilePanel extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link FileFilter} to use.
     */
    @NotNull
    private final FileFilter fileFilter;

    /**
     * The original value.
     */
    @NotNull
    private final String original;

    /**
     * The context reference for relative paths.
     */
    @Nullable
    private final File relativeReference;

    /**
     * The context reference for absolute paths.
     */
    @NotNull
    private final File absoluteReference;

    /**
     * The {@link JTextField} with the text.
     */
    @NotNull
    private final JTextField textField;

    /**
     * The {@link RASwitch}.
     */
    @NotNull
    private final RASwitch raSwitch;

    /**
     * Creates a new instance.
     * @param fileFilter the file filter to use
     * @param original the original value, used as initial value and for
     * restoring it
     * @param relativeReference file to reference for relative paths (context
     * file or directory, not parent directory)
     * @param absoluteReference file to reference for absolute paths (context
     * file or directory, not parent directory)
     */
    public TilePanel(@NotNull final FileFilter fileFilter, @NotNull final String original, @Nullable final File relativeReference, @NotNull final File absoluteReference) {
        super(new GridBagLayout());
        this.fileFilter = fileFilter;
        this.original = original;
        this.relativeReference = relativeReference == null ? null : getAbsolutePath(relativeReference);
        this.absoluteReference = getAbsolutePath(absoluteReference);
        final GridBagConstraints gbc = new GridBagConstraints();
        textField = new JTextField();
        textField.setColumns(16);

        gbc.fill = GridBagConstraints.NONE;
        add(iconButton(ACTION_BUILDER.createAction(false, "mapTileRevert", this)), gbc);
        add(iconButton(ACTION_BUILDER.createAction(false, "mapTileClear", this)), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(textField, gbc);
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        raSwitch = new RASwitch();
        add(raSwitch, gbc);
        add(iconButton(ACTION_BUILDER.createAction(false, "mapTileChoose", this)), gbc);

        textField.setText(original);
        raSwitch.updateRAState();
    }

    /**
     * Returns the absolute path of a {@link File}.
     * @param file the file
     * @return the absolute path
     */
    @NotNull
    private static File getAbsolutePath(@NotNull final File file) {
        try {
            return file.getCanonicalFile();
        } catch (final IOException ignored) {
            return file.getAbsoluteFile();
        }
    }

    /**
     * Creates a new button for reverting a path entry.
     * @param action the action for the button
     * @return the button
     */
    @NotNull
    private static Component iconButton(@NotNull final Action action) {
        final AbstractButton button = new JButton(action);
        button.setMargin(new Insets(0, 0, 0, 0));
        return button;
    }

    /**
     * Action method for reverting to stored path.
     */
    @ActionMethod
    public void mapTileRevert() {
        setText(original, false);
    }

    /**
     * Action method for deleting the path.
     */
    @ActionMethod
    public void mapTileClear() {
        setText("", true);
    }

    /**
     * Action method for choosing the path.
     */
    @ActionMethod
    public void mapTileChoose() {
        final File tmpRelativeReference = relativeReference;
        final JFileChooser chooser = tmpRelativeReference == null ? new JFileChooser() : new JFileChooser(tmpRelativeReference.getParentFile());
        final String oldFilename = getText();
        if (oldFilename.length() > 0) {
            // Point the chooser on the current path tile file
            final File oldFile;
            if (tmpRelativeReference == null || oldFilename.startsWith("/")) {
                oldFile = new File(absoluteReference, oldFilename.substring(1));
            } else {
                oldFile = new File(tmpRelativeReference.getParentFile(), oldFilename);
            }
            FileChooserUtils.setCurrentDirectory(chooser, oldFile.getParentFile());
            chooser.setSelectedFile(oldFile);
        }
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(fileFilter);
        FileChooserUtils.sanitizeCurrentDirectory(chooser);
        final int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                final File selected = chooser.getSelectedFile();
                final String relPath = selected.getCanonicalPath().substring(absoluteReference.getCanonicalPath().length()).replace('\\', '/');
                setText(relPath, true);
            } catch (final IOException ex) {
                setText("Error: " + ex, true);
            }
        }
    }

    /**
     * Sets the text.
     * @param text the ext to set
     * @param keepRA if set, modify <code>text</code> to match the current RA
     * state
     */
    public void setText(@NotNull final String text, final boolean keepRA) {
        textField.setText(text);
        if (keepRA) {
            raSwitch.actionPerformed(false);
        } else {
            raSwitch.updateRAState();
        }
    }

    /**
     * Returns the text.
     * @return the text
     */
    @NotNull
    public String getText() {
        final String text = textField.getText();
        assert text != null;
        return text;
    }

    /**
     * Activates the text input field.
     */
    public void activateTextField() {
        textField.requestFocusInWindow();
    }

    /**
     * Adds an {@link ActionListener} to the text input field.
     * @param actionListener the action listener
     */
    public void addTextFieldActionListener(@NotNull final ActionListener actionListener) {
        textField.addActionListener(actionListener);
    }

    /**
     * Updates the state of the {@link #raSwitch}.
     */
    public void updateRAState() {
        raSwitch.updateRAState();
    }

    /**
     * A JButton that converts relative to absolute paths and vice versa.
     */
    private class RASwitch extends JButton implements ActionListener {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Whether the current mode is relative.
         */
        private boolean isRelative;

        /**
         * Creates a new instance.
         */
        private RASwitch() {
            setMargin(new Insets(0, 0, 0, 0));
            setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "mapTilePathMode.shortdescription"));
            updateText();
            addActionListener(this);
            setPreferredSize(getMinimumSize());
        }

        @Override
        public void actionPerformed(@NotNull final ActionEvent e) {
            actionPerformed(true);
        }

        public void actionPerformed(final boolean toggleRelative) {
            final String path = TilePanel.this.getText();
            final File tmpRelativeReference = relativeReference;
            if (path.isEmpty() || tmpRelativeReference == null) {
                return;
            }
            if (toggleRelative) {
                isRelative = !isRelative;
                updateText();
            }
            final String relRef = new StringBuilder().append('/').append(PathManager.absoluteToRelative(absoluteReference.getPath() + '/', tmpRelativeReference.getPath())).toString();
            if (isRelative) {
                textField.setText(PathManager.absoluteToRelative(relRef, path));
            } else {
                textField.setText(PathManager.relativeToAbsolute(relRef, path));
            }
        }

        /**
         * Updates the state of the button.
         */
        public void updateRAState() {
            final String path = getText();
            isRelative = PathManager.isRelative(path);
            updateText();
        }

        /**
         * Updates the button text to reflect the current state.
         */
        private void updateText() {
            setText(isRelative ? "R" : "A");
        }

    }

}
