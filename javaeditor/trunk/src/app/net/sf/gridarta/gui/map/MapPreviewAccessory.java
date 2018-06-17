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

package net.sf.gridarta.gui.map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileView;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import net.sf.japi.swing.action.ToggleAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for MapPreviewAccessories which are used for previewing
 * maps in JFileChoosers.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MapPreviewAccessory extends JComponent {

    /**
     * The serial version UID.
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
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * Preferences key for automatic preview generation.
     */
    @NotNull
    private static final String PREFERENCES_AUTO_GENERATE_PREVIEW = "autoGeneratePreviews";

    /**
     * The cache for map images.
     */
    @NotNull
    private final MapImageCache<?, ?, ?> mapImageCache;

    /**
     * The preview label.
     */
    @NotNull
    private final JLabel preview = new JLabel();

    /**
     * ToggleAction for auto-generate preview.
     */
    @NotNull
    private final ToggleAction autoGeneratePreview = (ToggleAction) ACTION_BUILDER.createToggle(false, "autoGeneratePreviews", this);

    /**
     * Action for generating preview.
     */
    private final Action genPreview = ACTION_BUILDER.createAction(false, "genPreview", this);

    /**
     * The file chooser instance.
     */
    @NotNull
    private final JFileChooser fileChooser;

    /**
     * The currently selected files, or <code>null</code> if no file is
     * selected.
     */
    @Nullable
    private File[] selectedFiles;

    /**
     * Whether previews should be auto-generated.
     */
    private boolean autoGeneratePreviews;

    /**
     * Creates an MapPreviewAccessory.
     * @param mapImageCache the map image cache to use
     * @param fileChooser the file chooser to attach to
     */
    public MapPreviewAccessory(@NotNull final MapImageCache<?, ?, ?> mapImageCache, @NotNull final JFileChooser fileChooser) {
        this.mapImageCache = mapImageCache;
        this.fileChooser = fileChooser;
        buildUI();
        setPreview("No Preview available");

        final PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final String prop = evt.getPropertyName();
                if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
                    setPreview("No Preview available");
                } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
                    final File selectedFile = (File) evt.getNewValue();
                    if (selectedFile != null) {
                        final Image previewImage = getMapPreview(selectedFile);
                        if (previewImage != null) {
                            setPreview(previewImage);
                        } else {
                            setPreview("No Preview available");
                        }
                    } else {
                        setPreview("No Preview available");
                    }
                } else if (JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(prop)) {
                    selectedFiles = (File[]) evt.getNewValue();
                }
            }

        };
        fileChooser.addPropertyChangeListener(propertyChangeListener);
        fileChooser.setFileView(new MapFileView());
    }

    /**
     * Build the user interface.
     */
    private void buildUI() {
        setAutoGeneratePreviews(preferences.getBoolean(PREFERENCES_AUTO_GENERATE_PREVIEW, false));
        setPreferredSize(new Dimension(240, 115));
        setLayout(new BorderLayout());
        add(autoGeneratePreview.createCheckBox(), BorderLayout.NORTH);
        add(new JButton(genPreview), BorderLayout.SOUTH);
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        add(new JScrollPane(preview), BorderLayout.CENTER);
    }

    /**
     * Updates the text and icon of {@link #preview}.
     * @param image the icon to set or <code>null</code>
     */
    @SuppressWarnings("NullableProblems")
    private void setPreview(@Nullable final Image image) {
        preview.setIcon(image == null ? null : new ImageIcon(image));
        preview.setText(null);
    }

    /**
     * Updates the text and icon of {@link #preview}.
     * @param text the text to set
     */
    private void setPreview(@NotNull final String text) {
        preview.setIcon(null);
        preview.setText(text);
    }

    /**
     * Switch automatic preview generation.
     * @param autoGeneratePreviews <code>true</code> for automatically
     * generating previews, <code>false</code> otherwise
     */
    @ActionMethod
    public void setAutoGeneratePreviews(final boolean autoGeneratePreviews) {
        this.autoGeneratePreviews = autoGeneratePreviews;
        autoGeneratePreview.setSelected(autoGeneratePreviews);
        preferences.putBoolean(PREFERENCES_AUTO_GENERATE_PREVIEW, autoGeneratePreviews);
        if (autoGeneratePreviews) {
            fileChooser.validate();
            fileChooser.repaint();
        }
    }

    /**
     * Get whether to automatically generate previews.
     * @return <code>true</code> if previews are automatically generated,
     *         otherwise <code>false</code>
     */
    @ActionMethod
    public boolean isAutoGeneratePreviews() {
        return autoGeneratePreviews;
    }

    /**
     * Generate a preview.
     */
    @ActionMethod
    public void genPreview() {
        final File[] files = selectedFiles;
        if (files == null || files.length == 0) {
            return;
        }

        for (final File file : files) {
            mapImageCache.flush(file);

            final Image image = mapImageCache.getOrCreatePreview(file);
            setPreview(image);
        }
        fileChooser.validate();
        fileChooser.repaint();
    }

    /**
     * Get an icon.
     * @param mapFile map file to get icon for
     * @return icon for file mapFile or <code>null</code> if the icon was
     *         neither cached nor loadable
     */
    @Nullable
    public Image getMapIcon(@NotNull final File mapFile) {
        return autoGeneratePreview.isSelected() ? mapImageCache.getOrCreateIcon(mapFile) : mapImageCache.getIcon(mapFile);
    }

    /**
     * Get a preview.
     * @param mapFile map file to get preview for
     * @return preview for file mapFile or <code>null</code> if the preview was
     *         neither cached nor loadable
     */
    @Nullable
    private Image getMapPreview(@NotNull final File mapFile) {
        return autoGeneratePreview.isSelected() ? mapImageCache.getOrCreatePreview(mapFile) : mapImageCache.getPreview(mapFile);
    }

    /**
     * FileView for giving icons to map files.
     */
    private class MapFileView extends FileView {

        @Override
        public Icon getIcon(@NotNull final File f) {
            @Nullable final Image image;
            if (fileChooser.getCurrentDirectory().equals(f.getParentFile())) {
                image = getMapIcon(f);
            } else {
                image = null;
            }
            return image != null ? new ImageIcon(image) : super.getIcon(f);
        }

    }

}
