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

package net.sf.gridarta.gui.utils;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import net.sf.gridarta.model.io.PathManager;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component for selecting files. It consists of a text field and a button
 * which pops up a file chooser.
 * @author Andreas Kirschbaum
 */
public class JFileField extends JComponent {

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
     * The parent component.
     */
    @NotNull
    private final JComponent parent;

    /**
     * The base directory. When non-<code>null</code>, the contents of {@link
     * #textField} are relative to this directory.
     */
    @Nullable
    private final File baseDir;

    /**
     * The text file that contains the currently selected file.
     */
    @NotNull
    private final JTextField textField;

    /**
     * The {@link JFileChooser} for selecting the file.
     */
    @NotNull
    private final JFileChooser fileChooser = new JFileChooser();

    /**
     * Creates a new instance.
     * @param parent the parent component
     * @param key the resource key for showing tooltips; <code>null</code>
     * disables tooltips
     * @param baseDir the base directory; when non-<code>null</code> the
     * contents of the text input field are relative to this directory
     * @param file the currently selected file
     * @param fileSelectionMode the file selection mode for the file chooser
     */
    public JFileField(@NotNull final JComponent parent, @Nullable final String key, @Nullable final File baseDir, @NotNull final File file, final int fileSelectionMode) {
        this.parent = parent;
        this.baseDir = baseDir;
        textField = new JTextField(getRelativeFile(file), 20);

        fileChooser.setFileSelectionMode(fileSelectionMode);

        if (key != null) {
            final String tooltip = ACTION_BUILDER.getString(key + ".shortdescription");
            if (tooltip != null) {
                textField.setToolTipText(tooltip);
            }
        }

        final AbstractButton fileChooserButton = new JButton(ACTION_BUILDER.createAction(false, "fileChooserButton", this));
        fileChooserButton.setMargin(new Insets(0, 0, 0, 0));

        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(fileChooserButton, BorderLayout.EAST);
    }

    /**
     * Returns the currently selected file.
     * @return the currently selected file
     */
    @NotNull
    public File getFile() {
        final String text = textField.getText();
        if (text.isEmpty()) {
            return baseDir == null ? new File(System.getProperty("user.dir")) : baseDir;
        } else {
            return baseDir == null ? new File(text) : new File(baseDir, text);
        }
    }

    /**
     * Sets the currently selected file.
     * @param file the currently selected file
     */
    public void setFile(@NotNull final File file) {
        final String text = getRelativeFile(file);
        textField.setText(text.isEmpty() ? "/" : text);
    }

    /**
     * Returns the contents for the text input field for a file.
     * @param file the file
     * @return the contents
     */
    @NotNull
    private String getRelativeFile(final File file) {
        return baseDir == null ? file.getPath() : PathManager.getMapPath(file.getAbsolutePath(), baseDir);
    }

    /**
     * Adds a {@link DocumentListener} to the text input field.
     * @param documentListener the document listener to add
     */
    public void addDocumentListener(@NotNull final DocumentListener documentListener) {
        textField.getDocument().addDocumentListener(documentListener);
    }

    /**
     * The action method for the button. It shows the file chooser.
     */
    @ActionMethod
    public void fileChooserButton() {
        final File file = getFile();
        fileChooser.setSelectedFile(file);
        //if (file.isDirectory()) {
        //FileChooserUtils.setCurrentDirectory(fileChooser, file);
        //fileChooser.setSelectedFile(null);
        //} else {
        //FileChooserUtils.setCurrentDirectory(fileChooser, file.getParentFile());
        //fileChooser.setSelectedFile(file);
        //}
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            setFile(fileChooser.getSelectedFile());
        }
    }

}
