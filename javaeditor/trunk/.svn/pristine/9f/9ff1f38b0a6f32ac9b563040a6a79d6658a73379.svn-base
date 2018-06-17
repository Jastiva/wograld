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

package net.sf.gridarta.gui.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import net.sf.gridarta.utils.FileFilters;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;

/**
 * The map preview of the level editor. This allows the user to zoom in and out
 * of the map.
 * @author <a href="mailto:plischewsky@hotmail.com">Peter Plischewsky</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @todo add user definable previews
 * @todo add zoom increase, zoom decrease
 */
public class MapPreview extends JComponent {

    /**
     * The serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The original image.
     * @serial
     */
    private final Image original;

    /**
     * The currently used algorithm.
     * @serial
     */
    private int algorithm = Image.SCALE_DEFAULT;

    /**
     * The currently used width.
     * @serial
     */
    private int width;

    /**
     * The currently used height.
     * @serial
     */
    private int height;

    /**
     * The currently displayed image.
     * @serial
     */
    private Image image;

    /**
     * The preview frame.
     * @serial
     */
    private final JFrame frame;

    /**
     * Constructs the user interface. When image is not null it either increases
     * or decreases the zoom. If m_Image is null the the program will wait for a
     * zoom factor to be chosen.
     * @param original the image to be displayed
     */
    public MapPreview(final Image original) {
        this.original = original;
        image = original;
        width = image.getWidth(this);
        height = image.getHeight(this);
        rescale();
        setBackground(new Color(255, 255, 255, 0));
        frame = new JFrame("Map Previewer");
        // WARNING: Do not set the icon. There's a bug that will prevent the frame from getting properly disposed.
        // This code will be put back in as soon as sun fixes the setIconImage() bug in their java.awt.peer.FramePeer impl.
        //ImageIcon icon = GUIUtils.getIcon(GUIConstants.APP_ICON);
        //if (icon != null) {
        //    frame.setIconImage(icon.getImage());
        //}
        frame.setJMenuBar(ACTION_BUILDER.createMenuBar(false, "zoomBar", this));
        final Component scroll = new JScrollPane(this);
        scroll.setFocusable(true);
        frame.add(scroll);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Zoom to 24x24.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom0() throws OutOfMemoryError {
        setScale(48, 23);
    }

    /**
     * Zoom to 12.5%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom12() throws OutOfMemoryError {
        setScale(0.125);
    }

    /**
     * Zoom to 25%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom25() throws OutOfMemoryError {
        setScale(0.25);
    }

    /**
     * Zoom to 50%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom50() throws OutOfMemoryError {
        setScale(0.5);
    }

    /**
     * Zoom to 100%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom100() throws OutOfMemoryError {
        setScale(1.0);
    }

    /**
     * Zoom to 150%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom150() throws OutOfMemoryError {
        setScale(1.5);
    }

    /**
     * Zoom to 200%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom200() throws OutOfMemoryError {
        setScale(2.0);
    }

    /**
     * Zoom to 250%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom250() throws OutOfMemoryError {
        setScale(2.5);
    }

    /**
     * Zoom to 300%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom300() throws OutOfMemoryError {
        setScale(3.0);
    }

    /**
     * Zoom to 400%.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoom400() throws OutOfMemoryError {
        setScale(4.0);
    }

    /**
     * Zoom save.
     * @throws IOException In case of I/O problems.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoomSave() throws IOException, OutOfMemoryError {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(FileFilters.pngFileFilter);
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            if (!file.exists() || ACTION_BUILDER.showQuestionDialog(this, "overwriteOtherFile", file)) {
                final RenderedImage img;
                if (image instanceof RenderedImage) {
                    img = (RenderedImage) image;
                } else {
                    final BufferedImage img2 = new BufferedImage(image.getWidth(this), image.getHeight(this), BufferedImage.TYPE_INT_ARGB);
                    final Graphics g = img2.getGraphics();
                    try {
                        paintComponent(g);
                    } finally {
                        g.dispose();
                    }
                    img = img2;
                }
                ImageIO.write(img, "png", file);
            }
        }
    }

    /**
     * Close the preview.
     */
    @ActionMethod
    public void zoomClose() {
        frame.dispose();
    }

    /**
     * Zoom Algorithm: Default.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoomAlgDefault() throws OutOfMemoryError {
        setAlgorithm(Image.SCALE_DEFAULT);
    }

    /**
     * Zoom Algorithm: Fast.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoomAlgFast() throws OutOfMemoryError {
        setAlgorithm(Image.SCALE_FAST);
    }

    /**
     * Zoom Algorithm: Smooth.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoomAlgSmooth() throws OutOfMemoryError {
        setAlgorithm(Image.SCALE_SMOOTH);
    }

    /**
     * Zoom Algorithm: Replicate.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoomAlgReplicate() throws OutOfMemoryError {
        setAlgorithm(Image.SCALE_REPLICATE);
    }

    /**
     * Zoom Algorithm: Area Averaging.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    @ActionMethod
    public void zoomAlgAreaAveraging() throws OutOfMemoryError {
        setAlgorithm(Image.SCALE_AREA_AVERAGING);
    }

    /**
     * Set the scale factor to be used for displaying the image. The viewport
     * size is automatically updated.
     * @param scale scale factor to be used
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    private void setScale(final double scale) throws OutOfMemoryError {
        width = (int) ((double) original.getWidth(this) * scale);
        height = (int) ((double) original.getHeight(this) * scale);
        rescale();
    }

    /**
     * Set the scale factor to a specific size.
     * @param width width for scale
     * @param height height for scale
     */
    private void setScale(final int width, final int height) {
        this.width = width;
        this.height = height;
        rescale();
    }

    /**
     * Set the algorithm.
     * @param algorithm algorithm for scale
     */
    private void setAlgorithm(final int algorithm) {
        this.algorithm = algorithm;
        rescale();
    }

    /**
     * Recalculate the image.
     * @throws OutOfMemoryError In case this method runs out of memory, which
     * really might happen, so callers are expected to handle this.
     */
    private void rescale() throws OutOfMemoryError {
        try {
            image = original.getScaledInstance(width, height, algorithm);
        } catch (final OutOfMemoryError e) {
            image = original;
            width = image.getWidth(this);
            height = image.getHeight(this);
            throw e;
        } finally {
            setPreferredSize(new Dimension(width, height));
            revalidate();
            if (image == original) {
                // XXX somehow in this case no repainting is done, so we tell to do it.
                repaint();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}
