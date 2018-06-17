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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.AnimationParseException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.FaceProvider;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.AnimationObjectsReader;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.FileFilters;
import net.sf.gridarta.utils.Pair;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link AbstractResourcesReader
 * AbstractResourcesReaders} that read from individual files.
 * @author Andreas Kirschbaum
 * @author serpentshard
 */
public abstract class AbstractFilesResourcesReader<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractResourcesReader<G> {

    /**
     * The logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(AbstractFilesResourcesReader.class);

    /**
     * The "arch" directory to read.
     */
    @NotNull
    private final File archDirectory;

    /**
     * The {@link ArchetypeSet} to update.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * The {@link AbstractArchetypeParser} to use.
     */
    @NotNull
    private final AbstractArchetypeParser<G, A, R, ?> archetypeParser;

    /**
     * The {@link AnimationObjects} to use.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * The {@link ArchFaceProvider} to use.
     */
    @NotNull
    private final ArchFaceProvider archFaceProvider;

    /**
     * Creates a new instance.
     * @param archDirectory the "arch" directory to read
     * @param archetypeSet the archetype set to update
     * @param archetypeParser the archetype parser to use
     * @param archFaceProvider the arch face provider to use
     * @param collectedDirectory the collected directory
     * @param imageSet the active image set or <code>null</code>
     * @param animationObjects the animation objects instance
     * @param faceObjects the face objects instance
     */
    protected AbstractFilesResourcesReader(@NotNull final File archDirectory, @NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final AbstractArchetypeParser<G, A, R, ?> archetypeParser, @NotNull final ArchFaceProvider archFaceProvider, @NotNull final File collectedDirectory, @Nullable final String imageSet, @NotNull final AnimationObjects animationObjects, @NotNull final FaceObjects faceObjects) {
        super(collectedDirectory, imageSet, animationObjects, faceObjects);
        this.archDirectory = archDirectory;
        this.archetypeSet = archetypeSet;
        this.archetypeParser = archetypeParser;
        this.archFaceProvider = archFaceProvider;
        this.animationObjects = animationObjects;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FaceProvider read(@NotNull final ErrorView errorView, @NotNull final List<G> invObjects) {
        archetypeSet.setLoadedFromArchive(false);
        final int animationObjectsSize = animationObjects.size();
        final int archetypeCount = archetypeSet.getArchetypeCount();
        final Collection<Pair<String, File>> animFiles = new ArrayList<Pair<String, File>>();
        final ErrorViewCollector archDirectoryErrorViewCollector = new ErrorViewCollector(errorView, archDirectory);
        loadArchetypesFromFiles(archDirectoryErrorViewCollector, archetypeParser, "", archDirectory, 0, "default", "default", invObjects, archFaceProvider, errorView, animFiles);
        loadAnimationsFromFiles(archDirectoryErrorViewCollector, animFiles);
        if (log.isInfoEnabled()) {
            log.info("Loaded " + (animationObjects.size() - animationObjectsSize) + " animations from '" + archDirectory + "'.");
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + (archetypeSet.getArchetypeCount() - archetypeCount) + " archetypes from '" + archDirectory + "'.");
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + archFaceProvider.size() + " faces from '" + archDirectory + "'.");
        }
        return archFaceProvider;
    }

    /**
     * Loads all archetypes and faces by recursing through the
     * <code>arch/</code> directory tree.
     * @param errorViewCollector the error view collector for reporting errors
     * @param archetypeParser the archetype parser to use
     * @param path the base directory relative path of <code>f</code>
     * @param f file path where we currently are
     * @param folderLevel level of the folder
     * @param panelName the panel to add archetypes to
     * @param folderName the folder to add archetypes to
     * @param invObjects collects all inventory objects
     * @param archFaceProvider the arch face provider to add to
     * @param errorView the error view to use
     * @param animFiles the found animation files
     */
    private void loadArchetypesFromFiles(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final AbstractArchetypeParser<G, A, R, ?> archetypeParser, @NotNull final String path, @NotNull final File f, final int folderLevel, @NotNull final String panelName, @NotNull final String folderName, @NotNull final List<G> invObjects, @NotNull final ArchFaceProvider archFaceProvider, @NotNull final ErrorView errorView, @NotNull final Collection<Pair<String, File>> animFiles) {
        final String name = f.getName();
        if (f.isDirectory()) {
            // now, setup the archetype panels
            if (isValidEntry(folderLevel, name)) {
                final String[] entries = f.list();
                if (entries != null) {
                    Arrays.sort(entries);
                    final String thisPanelName = folderLevel == 1 ? name : panelName;
                    final String thisFolderName = folderLevel == 1 || folderLevel == 2 ? name : folderName;
                    final String newPath = folderLevel == 0 ? "" : path + "/" + name;
                    for (final String entry : entries) {
                        loadArchetypesFromFiles(errorViewCollector, archetypeParser, newPath, new File(f, entry), folderLevel + 1, thisPanelName, thisFolderName, invObjects, archFaceProvider, errorView, animFiles);
                    }
                } else {
                    errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_DIR_INVALID, f.getPath());
                }
            }
        } else if (f.isFile()) { // watch out for stuff that's not a file and not a directory!!!
            if (FileFilters.arcFileFilter.accept(f)) {
                try {
                    final FileInputStream fis = new FileInputStream(f);
                    try {
                        final InputStreamReader isr = new InputStreamReader(fis);
                        try {
                            final BufferedReader in = new BufferedReader(isr);
                            try {
                                archetypeParser.parseArchetypeFromStream(in, null, null, null, panelName, folderName, path, invObjects, new ErrorViewCollector(errorView, f));
                            } finally {
                                in.close();
                            }
                        } finally {
                            isr.close();
                        }
                    } finally {
                        fis.close();
                    }
                } catch (final IOException ex) {
                    new ErrorViewCollector(errorView, f).addWarning(ErrorViewCategory.ARCHETYPE_FILE_INVALID, ex.getMessage());
                }
            } else if (FileFilters.pngFileFilter.accept(f)) {
                if (archetypeSet.getImageSet() == null || name.contains("." + archetypeSet.getImageSet() + ".")) {
                    addPNGFace(f.getAbsolutePath(), path, name, errorView, archFaceProvider);
                } else if (name.contains(".ceil.")) {
                    addPNGFace2(f.getAbsolutePath(), path, name, errorView, archFaceProvider);
                }
            } else if (FileFilters.faceFileFilter.accept(f)) {
                parseDefFace(errorView, f.getAbsolutePath(), path);
            } else if (FileFilters.animFileFilter.accept(f)) {
                animFiles.add(new Pair<String, File>(path, f));
            }
        }
    }

    /**
     * Loads a .face file.
     * @param errorView the error view for reporting errors
     * @param path the base directory relative path of <code>filename</code>
     * @param filename filename
     */
    private void parseDefFace(@NotNull final ErrorView errorView, @NotNull final String path, @NotNull final String filename) {
        final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, new File(path));
        try {
            final FileInputStream fis = new FileInputStream(path);
            try {
                final InputStreamReader isr = new InputStreamReader(fis);
                try {
                    final Reader in = new BufferedReader(isr);
                    try {
                        AnimationObjectsReader.loadAnimations(animationObjects, errorViewCollector, in, "animation ", true, filename, null);
                    } finally {
                        in.close();
                    }
                } finally {
                    isr.close();
                }
            } finally {
                fis.close();
            }
        } catch (final IOException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.FACE_FILE_INVALID, ex.getMessage());
        } catch (final AnimationParseException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.FACE_FILE_INVALID, "line " + ex.getLineNumber() + ": parsing error: " + ex.getMessage());
        }
    }

    /**
     * This method loads animations that are separately defined by looping
     * through all files that were previously collected by {@link
     * #loadArchetypesFromFiles(ErrorViewCollector, AbstractArchetypeParser,
     * String, File, int, String, String, List, ArchFaceProvider, ErrorView,
     * Collection)}. Do not invoke this method if <code>loadArchetypesFromFiles()</code>
     * wasn't invoked.
     * @param errorViewCollector the error view collector for reporting errors
     * @param animFiles the animation files
     */
    private void loadAnimationsFromFiles(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final Iterable<Pair<String, File>> animFiles) {
        for (final Pair<String, File> pair : animFiles) {
            final String animPath = pair.getFirst();
            final File animFile = pair.getSecond();
            try {
                AnimationObjectsReader.loadAnimations(animationObjects, errorViewCollector, animPath, animFile, "anim ", false);
            } catch (final IOException ex) {
                errorViewCollector.addWarning(ErrorViewCategory.ANIM_FILE_INVALID, ex.getMessage() + ", " + animFile);
            } catch (final AnimationParseException ex) {
                errorViewCollector.addWarning(ErrorViewCategory.ANIM_ENTRY_INVALID, "line " + ex.getLineNumber() + ": parsing error: " + ex.getMessage() + ", " + animFile);
            }
        }
    }

    /**
     * Returns whether a file name should be processed.
     * @param folderLevel the folder level
     * @param name the base name of the file name
     * @return whether the entry is valid and should be processed
     */
    protected abstract boolean isValidEntry(final int folderLevel, final String name);

}
