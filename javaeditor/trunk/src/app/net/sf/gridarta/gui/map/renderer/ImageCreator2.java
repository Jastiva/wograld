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

package net.sf.gridarta.gui.map.renderer;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.FileChooserUtils;
import net.sf.gridarta.utils.FileFilters;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Creates images from map instances.
 * @author Andreas Kirschbaum
 */
public class ImageCreator2<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link ImageCreator} to forward to.
     */
    @NotNull
    private final ImageCreator<G, A, R> imageCreator;

    /**
     * Creates a new instance.
     * @param globalSettings the global settings to use
     * @param imageCreator the image creator to forward to
     */
    public ImageCreator2(@NotNull final GlobalSettings globalSettings, @NotNull final ImageCreator<G, A, R> imageCreator) {
        this.globalSettings = globalSettings;
        this.imageCreator = imageCreator;
    }

    /**
     * Create an image of a map and save it as a file. In this method, a file
     * chooser is opened to let the user select an output file name/path for the
     * png image.
     * @param mapModel the map to print
     * @param component the parent component to use
     */
    public void createImage(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Component component) {
        final File mapFile = mapModel.getMapFile();
        final File file = new File(globalSettings.getImageDirectory(), mapFile == null ? "image" : mapFile.getName() + ".png");
        final JFileChooser fileChooser = new JFileChooser(globalSettings.getImageDirectory());
        fileChooser.setDialogTitle("Save Image As");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(file);
        // set a file filter for "*.png" files
        fileChooser.setFileFilter(FileFilters.pngFileFilter);

        FileChooserUtils.sanitizeCurrentDirectory(fileChooser);
        final int returnVal = fileChooser.showSaveDialog(component);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // got the file path, now create image
            File imageFile = fileChooser.getSelectedFile();
            if (!imageFile.getName().endsWith(".png")) {
                imageFile = new File(imageFile.getParentFile(), imageFile.getName() + ".png");
            }
            globalSettings.setImageDirectory(imageFile.getParentFile());
            if (!imageFile.exists() || ACTION_BUILDER.showConfirmDialog(component, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, "overwriteOtherFile", imageFile) == JOptionPane.YES_OPTION) {
                try {
                    imageCreator.createImage(mapModel, imageFile);
                } catch (final IOException ex) {
                    ACTION_BUILDER.showMessageDialog(component, "createImageIOException", imageFile, ex.getMessage());
                }
            }
        }
    }

}
