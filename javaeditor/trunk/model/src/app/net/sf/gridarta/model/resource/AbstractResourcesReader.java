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

package net.sf.gridarta.model.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.AnimationParseException;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.DuplicateFaceException;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.FaceProvider;
import net.sf.gridarta.model.face.IllegalFaceException;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.AnimationObjectsReader;
import net.sf.gridarta.utils.IOUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for archetype set loader classes.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @author serpentshard
 */
public abstract class AbstractResourcesReader<G extends GameObject<G, ?, ?>> {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(AbstractResourcesReader.class);

    /**
     * The collected directory.
     */
    @NotNull
    private final File collectedDirectory;

    /**
     * The current image set. Set to <code>null</code> if no explicit image set
     * is used.
     */
    @Nullable
    private final String imageSet;

    /**
     * The animation objects instance.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * The face objects instance.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * Creates a new instance.
     * @param collectedDirectory the collected directory
     * @param imageSet the active image set or <code>null</code>
     * @param animationObjects the animation objects instance
     * @param faceObjects the face objects instance
     */
    protected AbstractResourcesReader(@NotNull final File collectedDirectory, @Nullable final String imageSet, @NotNull final AnimationObjects animationObjects, @NotNull final FaceObjects faceObjects) {
        this.collectedDirectory = collectedDirectory;
        this.imageSet = imageSet;
        this.animationObjects = animationObjects;
        this.faceObjects = faceObjects;
    }

    /**
     * Loads all animations from the big collected animations file.
     * @param errorView the error view for reporting errors
     * @param animations maps animation name to animation path
     */
    protected void loadAnimationsFromCollect(@NotNull final ErrorView errorView, @NotNull final Map<String, String> animations) {
        final int animationObjectsSize = animationObjects.size();

        try {
            final URL animationsURL = IOUtils.getResource(collectedDirectory, "animations");
            final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, animationsURL);
            try {
                final InputStream inputStream = animationsURL.openStream();
                try {
                    final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                    try {
                        final Reader bufferedReader = new BufferedReader(reader);
                        try {
                            AnimationObjectsReader.loadAnimations(animationObjects, errorViewCollector, bufferedReader, "anim ", false, null, animations);
                        } finally {
                            bufferedReader.close();
                        }
                    } finally {
                        reader.close();
                    }
                } finally {
                    inputStream.close();
                }
            } catch (final IOException ex) {
                errorView.addWarning(ErrorViewCategory.ANIMATIONS_FILE_INVALID, animationsURL + ": " + ex.getMessage());
            } catch (final AnimationParseException ex) {
                errorView.addWarning(ErrorViewCategory.ANIMATIONS_FILE_INVALID, ex.getLineNumber(), animationsURL + ": " + ex.getMessage());
            }

            if (log.isInfoEnabled()) {
                log.info("Loaded " + (animationObjects.size() - animationObjectsSize) + " animations from '" + animationsURL + "'.");
            }
        } catch (final FileNotFoundException ex) {
            errorView.addWarning(ErrorViewCategory.ANIMATIONS_FILE_INVALID, new File(collectedDirectory, "animations") + ": " + ex.getMessage());
        }
    }
    
    protected void loadAnimationsFromCollect2(@NotNull final ErrorView errorView) {
        final int animationObjectsSize = animationObjects.size();

        try {
            final URL animationsURL = IOUtils.getResource(collectedDirectory, "animations");
            final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, animationsURL);
            try {
                final InputStream inputStream = animationsURL.openStream();
                try {
                    final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                    try {
                        final Reader bufferedReader = new BufferedReader(reader);
                        try {
                            AnimationObjectsReader.loadAnimations2(animationObjects, errorViewCollector, bufferedReader, "anim ", false, faceObjects);
                        } finally {
                            bufferedReader.close();
                        }
                    } finally {
                        reader.close();
                    }
                } finally {
                    inputStream.close();
                }
            } catch (final IOException ex) {
                errorView.addWarning(ErrorViewCategory.ANIMATIONS_FILE_INVALID, animationsURL + ": " + ex.getMessage());
            } catch (final AnimationParseException ex) {
                errorView.addWarning(ErrorViewCategory.ANIMATIONS_FILE_INVALID, ex.getLineNumber(), animationsURL + ": " + ex.getMessage());
            }

            if (log.isInfoEnabled()) {
                log.info("Loaded " + (animationObjects.size() - animationObjectsSize) + " animations from '" + animationsURL + "'.");
            }
        } catch (final FileNotFoundException ex) {
            errorView.addWarning(ErrorViewCategory.ANIMATIONS_FILE_INVALID, new File(collectedDirectory, "animations") + ": " + ex.getMessage());
        }
    }

    /**
     * Loads a png from the file, convert it to IconImage and attach it to the
     * face list.
     * @param filename the filename, an absolute path
     * @param path the path name of <code>name</code>
     * @param name the name of the png (e.g. blocked.111.png)
     * @param errorView the error view to use
     * @param archFaceProvider the arch face provider to add to
     */
    protected void addPNGFace(@NotNull final String filename, @NotNull final String path, @NotNull final String name, @NotNull final ErrorView errorView, @NotNull final ArchFaceProvider archFaceProvider) {
        final String facename = generateFaceName(name);
        archFaceProvider.addInfo(facename, filename);
        try {
            faceObjects.addFaceObject(facename, path + "/" + facename, 0, (int) new File(filename).length());
        } catch (final DuplicateFaceException e) {
            errorView.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("loadDuplicateFace", e.getDuplicate().getFaceName(), e.getDuplicate().getOriginalFilename(), e.getExisting().getOriginalFilename()));
        } catch (final IllegalFaceException ex) {
            errorView.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("loadIllegalFace", ex.getFaceObject().getFaceName(), ex.getFaceObject().getOriginalFilename()));
        }
    }
    
    /**
     * Loads a png from the file, convert it to IconImage and attach it to the
     * face list.
     * @param filename the filename, an absolute path
     * @param path the path name of <code>name</code>
     * @param name the name of the png (e.g. blocked.111.png)
     * @param errorView the error view to use
     * @param archFaceProvider the arch face provider to add to
     */
    protected void addPNGFace2(@NotNull final String filename, @NotNull final String path, @NotNull final String name, @NotNull final ErrorView errorView, @NotNull final ArchFaceProvider archFaceProvider) {
        final String facename = generateFaceName(name);
        archFaceProvider.addInfo2(facename, filename);
        // when loading the upper image later, re use the lower image face
    }

    /**
     * Generates the facename for a face.
     * @param name the filename to generate facename from
     * @return the facename generated from <var>name</var>
     */
    @NotNull
    private String generateFaceName(@NotNull final String name) {
        //noinspection VariableNotUsedInsideIf
        if (imageSet != null) {
            // we have to snip out the image set information here from
            // the 'name', and the ".png": (e.g. blocked.base.111.png -> blocked.111)
            int firstDot = 0;
            int secondDot = 0;
            for (int t = 0; t < name.length() && secondDot == 0; t++) {
                if (name.charAt(t) == '.') {
                    if (firstDot == 0) {
                        firstDot = t;
                    } else {
                        secondDot = t;
                    }
                }
            }

            if (firstDot != 0 && secondDot != 0) {
                return name.substring(0, firstDot) + name.substring(secondDot, name.length() - 4);
            } else {
                return name.substring(0, name.length() - 4);
            }
        }

        // no image set: we need only cut off the ".png"
        return name.substring(0, name.length() - 4);
    }

    /**
     * Reads the resources.
     * @param errorView the error view for reporting problems
     * @param invObjects all read archetypes are added to this list
     * @return the face provider for accessing the read faces
     */
    @NotNull
    public abstract FaceProvider read(@NotNull final ErrorView errorView, @NotNull final List<G> invObjects);

    /**
     * Loads animations from a file.
     * @param url the URL to load animations tree from
     * @return a map from animation name to animation path
     * @throws IOException in case of I/O errors
     * @throws java.io.FileNotFoundException in case the file couldn't be
     * opened
     */
    @NotNull
    protected static Map<String, String> loadAnimTree(@NotNull final URL url) throws IOException {
        final Map<String, String> animations;
        final InputStream inputStream = url.openStream();
        try {
            final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
            try {
                final BufferedReader bufferedReader = new BufferedReader(reader);
                try {
                    animations = loadAnimTree(bufferedReader);
                } finally {
                    bufferedReader.close();
                }
            } finally {
                reader.close();
            }
        } finally {
            inputStream.close();
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + animations.size() + " animations from '" + url + "'.");
        }
        return animations;
    }

    /**
     * Reads Animation Tree from Stream.
     * @param reader Input stream
     * @return a map from animation name to animation path
     * @throws IOException in case of I/O errors
     */
    @NotNull
    private static Map<String, String> loadAnimTree(@NotNull final BufferedReader reader) throws IOException {
        final Map<String, String> animations = new HashMap<String, String>();
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            final int index = line.lastIndexOf('/');
            final String name = line.substring(index + 1);
            animations.put(name, line);
        }
        return animations;
    }

}
