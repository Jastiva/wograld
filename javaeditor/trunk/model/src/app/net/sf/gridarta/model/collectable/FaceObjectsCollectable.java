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

package net.sf.gridarta.model.collectable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Collectable} that creates the atrinik.0/crossfire.0/daimonin.0 files
 * which contains all defined faces. It also creates index or editor helper
 * files such as bmaps or bmaps.paths.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class FaceObjectsCollectable implements Collectable {

    /**
     * The {@link ActionBuilder} instance.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link FaceObjects} being collected.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * The {@link ArchFaceProvider} to use for collection.
     */
    @NotNull
    private final ArchFaceProvider archFaceProvider;

    /**
     * Creates a new instance.
     * @param faceObjects the face objects to collect
     * @param archFaceProvider the arch face provider to use
     */
    public FaceObjectsCollectable(@NotNull final FaceObjects faceObjects, @NotNull final ArchFaceProvider archFaceProvider) {
        this.faceObjects = faceObjects;
        this.archFaceProvider = archFaceProvider;
    }

    /**
     * {@inheritDoc} Collects the faces. The graphics information is written to
     * "crossfire.0" resp. "daimonin.0". The meta information (offsets etc.) is
     * written to "bmaps". The tree information for the editor is written to
     * "bmaps.paths" resp. "facetree". <p/> Theoretically it would also be
     * possible to recode the images. But the Java image encoder isn't as good
     * as that of gimp in many cases (yet much better as the old visualtek's).
     */
    @Override
    public void collect(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
        collectTreeFile(progress, collectedDirectory);
        collectBmapsFile(progress, collectedDirectory);
        collectExtraFile(progress, collectedDirectory);
        collectImageFile(progress, collectedDirectory);
        collectImageFile2(progress,collectedDirectory);
    }

    /**
     * Creates the image file.
     * @param progress the progress to report progress to
     * @param collectedDirectory the destination directory to collect data to
     * @throws IOException in case of I/O problems during collection
     */
    private void collectImageFile(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
      //  final File file = new File(collectedDirectory, ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.image.name"));
      //  final FileOutputStream fos = new FileOutputStream(file);
        final FileOutputStream fos = new FileOutputStream(ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.image.name"));
        try {
            final FileChannel outChannel = fos.getChannel();
            try {
                final int numOfFaceObjects = faceObjects.size();
                progress.setLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectImages"), numOfFaceObjects);
                final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                final Charset charset = Charset.forName("ISO-8859-1");
                int i = 0;
                for (final FaceObject faceObject : faceObjects) {
                    final String face = faceObject.getFaceName();
                    final String path = archFaceProvider.getFilename(face);
                    try {
                        final FileInputStream fin = new FileInputStream(path);
                        try {
                            final FileChannel inChannel = fin.getChannel();
                            final long imageSize = inChannel.size();
                            byteBuffer.clear();
                            byteBuffer.put(("IMAGE " + (faceObjects.isIncludeFaceNumbers() ? i + " " : "") + imageSize + " " + faceObject.getPath()+ "\n").getBytes(charset));
                            byteBuffer.flip();
                            outChannel.write(byteBuffer);
                            inChannel.transferTo(0L, imageSize, outChannel);
                        } finally {
                            fin.close();
                        }
                    } catch (final FileNotFoundException ignored) {
                        ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorFileNotFound", path);
                        return;
                    } catch (final IOException e) {
                        ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorIOException", path, e);
                        return;
                    }

                    if (i++ % 100 == 0) {
                        progress.setValue(i);
                    }
                }
                progress.setValue(faceObjects.size()); // finished
            } finally {
                outChannel.close();
            }
        } finally {
            fos.close();
        }
    }
    
     private void collectImageFile2(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
      //  final File file = new File(collectedDirectory, ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.image.name"));
      //  final FileOutputStream fos = new FileOutputStream(file);
        final FileOutputStream fos = new FileOutputStream(ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.topimg.name"));
        try {
            final FileChannel outChannel = fos.getChannel();
            try {
                final int numOfFaceObjects = faceObjects.size();
                progress.setLabel(ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectImages"), numOfFaceObjects);
                final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                final Charset charset = Charset.forName("ISO-8859-1");
                int i = 0;
                for (final FaceObject faceObject : faceObjects) {
                    final String face = faceObject.getFaceName();
                    final String path = archFaceProvider.getFilename2(face);
                    if(path != null){
                    try {
                        final FileInputStream fin = new FileInputStream(path);
                        if(fin != null){
                        try {
                            final FileChannel inChannel = fin.getChannel();
                            final long imageSize = inChannel.size();
                            byteBuffer.clear();
                            byteBuffer.put(("IMAGE " + (faceObjects.isIncludeFaceNumbers() ? i + " " : "") + imageSize + " " + faceObject.getPath() + "\n").getBytes(charset));
                            byteBuffer.flip();
                            outChannel.write(byteBuffer);
                            inChannel.transferTo(0L, imageSize, outChannel);
                        } finally {
                            fin.close();
                        }
                        }
                    } catch (final FileNotFoundException ignored) {
                        ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorFileNotFound", path);
                        return;
                    } catch (final IOException e) {
                        ACTION_BUILDER.showMessageDialog(progress.getParentComponent(), "archCollectErrorIOException", path, e);
                        return;
                    }
                    
                }

                    if (i++ % 100 == 0) {
                        progress.setValue(i);
                    }
                }
                progress.setValue(faceObjects.size()); // finished
            } finally {
                outChannel.close();
            }
        } finally {
            fos.close();
        }
    }
    

    /**
     * Creates the tree file.
     * @param progress the progress to report progress to
     * @param collectedDirectory the destination directory to collect data to
     * @throws IOException in case of I/O problems during collection
     */
    private void collectTreeFile(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
       // collectFile(progress, new File(collectedDirectory, ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.facetree.name")), ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectTree"), ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.facetree.output"));
        final FileOutputStream fos = new FileOutputStream(ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.facetree.name"));
        String label = ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectTree");
        String format = ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.facetree.output");
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            try {
                final BufferedWriter bw = new BufferedWriter(osw);
                try {
                    final int numOfFaceObjects = faceObjects.size();
                    progress.setLabel(label, numOfFaceObjects);
                    int i = 0;
                    for (final FaceObject faceObject : faceObjects) {
                        final String path = faceObject.getPath();
                        final String face = faceObject.getFaceName();
                        bw.append(String.format(format, i, path, face)).append('\n');
                        if (i++ % 100 == 0) {
                            progress.setValue(i);
                        }
                    }
                    progress.setValue(numOfFaceObjects);
                } finally {
                    bw.close();
                }
            } finally {
                osw.close();
            }
        } finally {
            fos.close();
        }
    }

    /**
     * Creates the bmaps file.
     * @param progress the progress to report progress to
     * @param collectedDirectory the destination directory to collect data to
     * @throws IOException in case of I/O problems during collection
     */
    private void collectBmapsFile(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
       // collectFile(progress, new File(collectedDirectory, ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.face.name")), ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectBmaps"), ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.face.output"));
        final FileOutputStream fos = new FileOutputStream(ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.face.name"));
        String label = ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectBmaps");
        String format = ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.face.output");
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            try {
                final BufferedWriter bw = new BufferedWriter(osw);
                try {
                    final int numOfFaceObjects = faceObjects.size();
                    progress.setLabel(label, numOfFaceObjects);
                    int i = 0;
                    for (final FaceObject faceObject : faceObjects) {
                        final String path = faceObject.getPath();
                        final String face = faceObject.getFaceName();
                        bw.append(String.format(format, i, path, face)).append('\n');
                        if (i++ % 100 == 0) {
                            progress.setValue(i);
                        }
                    }
                    progress.setValue(numOfFaceObjects);
                } finally {
                    bw.close();
                }
            } finally {
                osw.close();
            }
        } finally {
            fos.close();
        }
    }
    
    private void collectExtraFile(@NotNull final Progress progress, @NotNull final File collectedDirectory) throws IOException {
       // collectFile(progress, new File(collectedDirectory, ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.face.name")), ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectBmaps"), ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.face.output"));
        final FileOutputStream fos = new FileOutputStream(ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.extra.name"));
        String label = ActionBuilderUtils.getString(ACTION_BUILDER, "archCollectBmaps");
      //  String format = ActionBuilderUtils.getString(ACTION_BUILDER, "configSource.extra.output");
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            try {
                final BufferedWriter bw = new BufferedWriter(osw);
                try {
                    final int numOfFaceObjects = faceObjects.size();
                    progress.setLabel(label, numOfFaceObjects);
                    int i = 0;
                    for (final FaceObject faceObject : faceObjects) {
                        
                        final boolean x1 = faceObject.getVisibility().isEmpty();
                        final boolean x2 = faceObject.getMagicmap().isEmpty();
                        final boolean x3 = faceObject.getForegroundColor().isEmpty();
                        final boolean x4 = faceObject.getBackgroundColor().isEmpty();
                        final boolean x5 = faceObject.getIsFloor().isEmpty();
                        final boolean x6 = faceObject.getFaceQuad().isEmpty();
                        if(x1 && x2 && x3 && x4 && x5 && x6){
                            if (i++ % 100 == 0) {
                            progress.setValue(i);
                            }
                        } else {
                        final String face = faceObject.getFaceName();
                        bw.append("face ").append(face).append('\n');
                        final String vis_str = faceObject.getVisibility();
                        if(!vis_str.isEmpty()){
                            bw.append(vis_str).append('\n');
                        }
                        final String mag_str = faceObject.getMagicmap();
                        if(!mag_str.isEmpty()){
                            bw.append(mag_str).append('\n');
                        }
                        final String foreground_str = faceObject.getForegroundColor();
                        if(!foreground_str.isEmpty()){
                            bw.append(foreground_str).append('\n');
                        }
                        final String background_str = faceObject.getBackgroundColor();
                        if(!background_str.isEmpty()){
                            bw.append(background_str).append('\n');
                        }
                        final String is_floor_str = faceObject.getIsFloor();
                        if(!is_floor_str.isEmpty()){
                            bw.append(is_floor_str).append('\n');
                        }
                        final String quad_str = faceObject.getFaceQuad();
                        if(!quad_str.isEmpty()){
                            bw.append(quad_str).append('\n');
                        }
                        bw.append("end").append('\n');
                     //   bw.append(String.format(format, i, path, face)).append('\n');
                        
                            if (i++ % 100 == 0) {
                                progress.setValue(i);
                            }
                        }
                    }
                    progress.setValue(numOfFaceObjects);
                } finally {
                    bw.close();
                }
            } finally {
                osw.close();
            }
        } finally {
            fos.close();
        }
    }

    

    /**
     * Creates an output file containing all faces.
     * @param progress the process to report progress to
     * @param file the output file to write
     * @param label the progress label
     * @param format the format string for writing the output file
     * @throws IOException if an I/O error occurs
     */
    private void collectFile(@NotNull final Progress progress, @NotNull final File file, @NotNull final String label, @NotNull final String format) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file);
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            try {
                final BufferedWriter bw = new BufferedWriter(osw);
                try {
                    final int numOfFaceObjects = faceObjects.size();
                    progress.setLabel(label, numOfFaceObjects);
                    int i = 0;
                    for (final FaceObject faceObject : faceObjects) {
                        final String path = faceObject.getPath();
                        final String face = faceObject.getFaceName();
                        bw.append(String.format(format, i, path, face)).append('\n');
                        if (i++ % 100 == 0) {
                            progress.setValue(i);
                        }
                    }
                    progress.setValue(numOfFaceObjects);
                } finally {
                    bw.close();
                }
            } finally {
                osw.close();
            }
        } finally {
            fos.close();
        }
    }

}
