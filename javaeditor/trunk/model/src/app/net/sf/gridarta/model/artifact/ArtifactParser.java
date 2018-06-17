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

package net.sf.gridarta.model.artifact;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parser for artifact definitions.
 * @author Andreas Kirschbaum
 */
public class ArtifactParser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ArtifactParser.class);

    /**
     * The {@link ArchetypeSet} for looking up archetypes.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;

    /**
     * The {@link ErrorView} for reporting errors.
     */
    @NotNull
    private final ErrorView errorView;

    /**
     * The {@link AbstractArchetypeParser} to use.
     */
    @NotNull
    private final AbstractArchetypeParser<G, A, R, ?> archetypeParser;

    /**
     * Collects all inventory objects.
     */
    @NotNull
    private final List<G> invObjects;

    /**
     * A {@link FilenameFilter} that matches "*.art" files.
     */
    @NotNull
    private final FilenameFilter artifactFilenameFilter = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            final File fullPath = new File(dir, name);
            // TODO: Replace this with a proper FileFilter.
            return fullPath.isDirectory() && !name.equalsIgnoreCase("cvs") && !name.equalsIgnoreCase(".xvpics") && !name.equalsIgnoreCase(".svn") || name.toLowerCase().endsWith(".art");
        }

    };

    /**
     * Creates a new instance.
     * @param archetypeSet the archetype set for looking up archetypes
     * @param errorView the error view for reporting errors
     * @param archetypeParser the archetype parser to use
     * @param invObjects collects all inventory objects
     */
    public ArtifactParser(@NotNull final ArchetypeSet<G, A, R> archetypeSet, @NotNull final ErrorView errorView, @NotNull final AbstractArchetypeParser<G, A, R, ?> archetypeParser, @NotNull final List<G> invObjects) {
        this.archetypeSet = archetypeSet;
        this.errorView = errorView;
        this.archetypeParser = archetypeParser;
        this.invObjects = invObjects;
    }

    /**
     * This method takes a filename or directory name as argument. In case of a
     * directory it is recursively traversed and all artifact files (*.art) get
     * parsed.
     * @param f This can be a filename or a directory name Load "pseudo arches"
     * from file "artifacts" WARNING: Don't include multi arches in the
     * artifacts file This code can't handle it nor the real server
     * @param panelName the panel to add artifacts to
     * @param folderName the folder to add artifacts to
     */
    public void loadArchesFromArtifacts(@NotNull final File f, @NotNull final String panelName, @NotNull final String folderName) {
        final int archetypes = archetypeSet.getArchetypeCount();
        loadArchesFromArtifactsRecursive(f, "", panelName, folderName);
        if (log.isInfoEnabled()) {
            log.info("Loaded " + (archetypeSet.getArchetypeCount() - archetypes) + " artifacts from '" + f + "'.");
        }
    }

    /**
     * This method takes a filename or directory name as argument. In case of a
     * directory it is recursively traversed and all artifact files (*.art) get
     * parsed.
     * @param f This can be a filename or a directory name Load "pseudo arches"
     * from file "artifacts" WARNING: Don't include multi arches in the
     * artifacts file This code can't handle it nor the real server
     * @param archPath the archetype path
     * @param panelName the panel to add artifacts to
     * @param folderName the folder to add artifacts to
     */
    private void loadArchesFromArtifactsRecursive(@NotNull final File f, @NotNull final String archPath, @NotNull final String panelName, @NotNull final String folderName) {
        if (f.isDirectory()) {
            final String[] traverse = f.list(artifactFilenameFilter);
            if (traverse != null) {
                Arrays.sort(traverse);
                for (final String entry : traverse) {
                    loadArchesFromArtifactsRecursive(new File(f, entry), archPath + "/" + entry, panelName, folderName);
                }
            }
        } else {
            loadArtifact(new ErrorViewCollector(errorView, f), f, archPath, panelName, folderName);
        }
    }

    /**
     * Loads one artifact.
     * @param errorViewCollector the error view collector for reporting errors
     * @param f This can be a filename or a directory name Load "pseudo arches"
     * from file "artifacts" WARNING: Don't include multi arches in the
     * artifacts file This code can't handle it nor the real server
     * @param archPath the archetype path
     * @param panelName the panel to add artifacts to
     * @param folderName the folder to add artifacts to
     */
    private void loadArtifact(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final File f, @NotNull final String archPath, @NotNull final String panelName, @NotNull final String folderName) {
        try {
            final FileInputStream fis = new FileInputStream(f);
            try {
                final InputStreamReader isr = new InputStreamReader(fis);
                try {
                    final BufferedReader in = new BufferedReader(isr);
                    try {
                        loadArtifact(in, errorViewCollector, archPath, panelName, folderName);
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
            errorViewCollector.addWarning(ErrorViewCategory.ARTIFACT_FILE_INVALID, ex.getMessage());
        }
    }

    /**
     * Loads one artifact.
     * @param in the reader to read from
     * @param errorViewCollector the error view collector for reporting errors
     * @param archPath the archetype path
     * @param panelName the panel to add artifacts to
     * @param folderName the folder to add artifacts to
     * @throws IOException if the artifact file cannot be read
     */
    public void loadArtifact(@NotNull final BufferedReader in, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String archPath, @NotNull final String panelName, @NotNull final String folderName) throws IOException {
        int editorCode = -1;
        @Nullable String name = null;
        @Nullable String defArchName = null;
        int lineCount = 0;

        @Nullable String editorPath = null;
        while (true) {
            final String thisLine2 = in.readLine();
            if (thisLine2 == null) {
                break;
            }
            lineCount++;
            final String thisLine = thisLine2.trim();
            // ignore white space lines or '#' comment lines
            if (!thisLine.startsWith("#") && thisLine.length() != 0) {
                if (thisLine.startsWith("artifact ")) {
                    name = thisLine.substring(9);
                } else if (thisLine.startsWith("def_arch ")) {
                    defArchName = thisLine.substring(9);
                } else if (thisLine.startsWith("editor ")) {
                    final String editor = thisLine.substring(7);
                    final int tIndex = editor.indexOf(':');
                    if (tIndex == -1) {
                        editorCode = Integer.parseInt(editor);
                    } else {
                        editorPath = editor.substring(tIndex + 1);
                        // TODO: use editorPath for determining the place of this GameObject
                        editorCode = Integer.parseInt(editor.substring(0, tIndex));
                    }
                } else if (thisLine.startsWith("Object")) {
                    if (editorCode == -1) {
                        // TOOD: Warn user
                    }
                    if (editorCode == 0 || editorCode == 2/* || editorCode == -1 */) {
                        // TODO: read until "end"
                    }
                    final String objTitle = thisLine.length() > 7 ? thisLine.substring(7) : "";
                    // TODO: Allow not having a def arch
                    // at this point we MUST have a legal name and def arch
                    if (defArchName == null) {
                        errorViewCollector.addWarning(ErrorViewCategory.ARTIFACT_ENTRY_INVALID, "line " + lineCount + ", Object '" + defArchName + "' / '" + name + "' / '" + objTitle + "' has missing def_arch");
                    } else if (defArchName.isEmpty()) {
                        errorViewCollector.addWarning(ErrorViewCategory.ARTIFACT_ENTRY_INVALID, "line " + lineCount + ", Object '" + defArchName + "' / '" + name + "' / '" + objTitle + "' has empty def_arch");
                    } else if (name == null) {
                        errorViewCollector.addWarning(ErrorViewCategory.ARTIFACT_ENTRY_INVALID, "line " + lineCount + ", Object '" + defArchName + "' / '" + name + "' / '" + objTitle + "' has missing name");
                    } else if (name.isEmpty()) {
                        errorViewCollector.addWarning(ErrorViewCategory.ARTIFACT_ENTRY_INVALID, "line " + lineCount + ", Object '" + defArchName + "' / '" + name + "' / '" + objTitle + "' has empty name");
                    } else {
                        try {
                            final R archetype = archetypeSet.getArchetype(defArchName);
                            if (editorCode != 0 && editorCode != 2) { // the next line of our file is part of a arch parse until a "end" comes
                                // now the editor will do the same as the real server:
                                // get the default arch as base and parse the new values over it
                                // the extended functions of the artifacts file can be ignored here.
                                archetypeParser.parseArchetypeFromStream(in, editorCode == 2 ? null : archetype, thisLine, name, panelName, folderName, archPath, invObjects, errorViewCollector); // XXX: editorCode == 2 looks incorrect due to the enclosing condition editorCode != 2
                                // note: in the parser is a small part where we handle the title setting
                                // and the reverse type setting for type == -1 (unique items marker in artifacts file)
                            } else {
                                while (true) {
                                    final String thisLine3 = in.readLine();
                                    if (thisLine3 == null || thisLine3.equals("end")) {
                                        break;
                                    }
                                }
                            }
                        } catch (final UndefinedArchetypeException ex) {
                            errorViewCollector.addWarning(ErrorViewCategory.ARTIFACT_ENTRY_INVALID, "line " + lineCount + ", Object '" + defArchName + "' / '" + name + "' / '" + objTitle + "' references undefined archetype '" + ex.getMessage() + "'");
                        }
                    }
                    name = null;
                    defArchName = null;
                    editorPath = null;
                    editorCode = -1;
                }
            }
        }
    }

}
