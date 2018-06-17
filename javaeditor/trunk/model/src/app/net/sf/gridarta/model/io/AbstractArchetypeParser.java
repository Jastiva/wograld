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

package net.sf.gridarta.model.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.DuplicateAnimationException;
import net.sf.gridarta.model.anim.IllegalAnimationException;
import net.sf.gridarta.model.archetype.AbstractArchetypeBuilder;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetype.AttributeListUtils;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base implementation of {@link ArchetypeParser}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public abstract class AbstractArchetypeParser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, B extends AbstractArchetypeBuilder<G, A, R>> implements ArchetypeParser<G, A, R> {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link AbstractArchetypeBuilder} to use.
     */
    @NotNull
    private final B archetypeBuilder;

    /**
     * The animation objects instance.
     */
    private final AnimationObjects animationObjects;

    /**
     * The {@link ArchetypeSet} to use.
     */
    @NotNull
    private final ArchetypeSet<G, A, R> archetypeSet;
    
    

    /**
     * Creates an ArchetypeParser.
     * @param archetypeBuilder the archetype builder to use
     * @param animationObjects the animation objects instance to use
     * @param archetypeSet the archetype set
     */
    protected AbstractArchetypeParser(@NotNull final B archetypeBuilder, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeSet<G, A, R> archetypeSet) {
        this.archetypeBuilder = archetypeBuilder;
        this.animationObjects = animationObjects;
        this.archetypeSet = archetypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseArchetypeFromStream(@NotNull final BufferedReader in, @Nullable final R prototype, @Nullable final String line, @Nullable final String archName, @NotNull final String panelName, @NotNull final String folderName, @NotNull final String archPath, @NotNull final List<G> invObjects, @NotNull final ErrorViewCollector errorViewCollector) throws IOException {
        archetypeBuilder.init(prototype, errorViewCollector);
        final boolean isInternPath;
        // path is needed when we don't read from collection because there is no editor_folder in arc files
        @Nullable final String path;
        if (!archetypeSet.isLoadedFromArchive() && archName == null) {
            final String tmpPath = archPath + "/";
            isInternPath = tmpPath.startsWith("/intern/");
            path = tmpPath;
        } else {
            isInternPath = false;
            path = "";
        }

        String thisLine2;
        if (line == null) {
            thisLine2 = in.readLine();
        } else {
            thisLine2 = line; // pre read "Object" from artifacts file loader
        }

        initParseArchetype();
        @Nullable R firstArch = null;
        boolean archMore = false;
        R lastArch = null;
        boolean reportError = true;

LOOP:
        while (true) {
            while (true) {
                if (thisLine2 == null) {
                    break LOOP;
                }

                final String thisLine = thisLine2.trim();
                if (thisLine.startsWith("#") || thisLine.length() == 0) {
                    thisLine2 = in.readLine();
                    continue;
                }

                if (thisLine.equals("More")) {
                    if (firstArch == null) {
                        firstArch = lastArch;
                    }
                    archMore = true;
                } else if (isStartLine(thisLine)) {
                    break;
                } else if (thisLine.length() > 0 && thisLine.charAt(0) != '#') {
                    if (reportError) {
                        reportError = false;
                        errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "unexpected line: " + thisLine);
                    }
                }

                thisLine2 = in.readLine();
            }

            final String archetypeName = archName != null ? archName : thisLine2.trim().substring(7);
            archetypeBuilder.reInit(archetypeName);
            if (!archMore) {
                if (firstArch != null) {
                    finishParseArchetype(firstArch);
                }
                firstArch = null;
            }

            initParseArchetype();

            @NotNull String editorFolder = archPath.isEmpty() ? "default" : archPath;
            while (true) {
                thisLine2 = in.readLine();
                if (thisLine2 == null) {
                    errorViewCollector.addError(ErrorViewCategory.ARCHETYPE_INVALID, "missing 'end' line");
                    break LOOP;
                }

                final String thisLine = thisLine2.trim();
                if (thisLine.startsWith("#") || thisLine.length() == 0) {
                    continue;
                }

                if (processLine(in, thisLine, thisLine2, archetypeBuilder, errorViewCollector, invObjects)) {
                    // ignore
                } else if (isStartLine(thisLine)) {
                    errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("logInventoryInDefArch", thisLine));
                } else if (thisLine.equals("end")) {
                    break;
                } else if (thisLine.equals("msg")) {
                    parseMsg(in, errorViewCollector);
                } else if (thisLine.equals("anim")) {
                    parseAnim(in, errorViewCollector, path);
                } else if (thisLine.startsWith("visibility ")) {
                    archetypeBuilder.set_visibility(thisLine.trim());
                 archetypeBuilder.addObjectText(thisLine);
                 //   errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Ignoring obsolete 'visibility' attribute: " + archetypeBuilder.getArchetypeName());
                } else if (thisLine.startsWith("magicmap ")) {
                    archetypeBuilder.set_magicmap(thisLine.trim());
                archetypeBuilder.addObjectText(thisLine);
                 //   errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Ignoring obsolete 'magicmap' attribute: " + archetypeBuilder.getArchetypeName());
                } else if(thisLine.startsWith("color_fg")){
                    archetypeBuilder.setForegroundColor(thisLine.trim());
                  archetypeBuilder.addObjectText(thisLine);
                } else if(thisLine.startsWith("color_bg")){
                    archetypeBuilder.setBackgroundColor(thisLine.trim());
                 archetypeBuilder.addObjectText(thisLine);
                } else if(thisLine.startsWith("is_floor")){
                    // but if found on an object of a map file, works differently
                    // and in fact is used with visibility to prioritize server
                    // map protocol to client
                    // see DefaultGameObjectParser
                    // is parsearch called while reading maps, not just arch?
                    archetypeBuilder.setFloorStr(thisLine.trim());
                   archetypeBuilder.addObjectText(thisLine);
                } else if(thisLine.startsWith("quad")) {
                    archetypeBuilder.setQuad(thisLine.trim());
                    archetypeBuilder.addObjectText(thisLine);
                } else if (thisLine.startsWith("x ")) {
                    if (!archMore && !archetypeBuilder.getArchetypeName().equals(START_ARCH_NAME)) {
                        errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("logFoundCoordInDefArchSingleSquareOrHead", "x", archetypeBuilder.getArchetypeName()));
                        archetypeBuilder.addObjectText(thisLine);
                    } else {
                        archetypeBuilder.setMultiX(Integer.parseInt(thisLine.substring(2)));
                    }
                } else if (thisLine.startsWith("y ")) {
                    if (!archMore && !archetypeBuilder.getArchetypeName().equals(START_ARCH_NAME)) {
                        errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("logFoundCoordInDefArchSingleSquareOrHead", "y", archetypeBuilder.getArchetypeName()));
                        archetypeBuilder.addObjectText(thisLine);
                    } else {
                        archetypeBuilder.setMultiY(Integer.parseInt(thisLine.substring(2)));
                    }
                } else if (thisLine.startsWith("editor_folder ")) {
                    editorFolder = thisLine.substring(14).trim();
                } else {
                    archetypeBuilder.addObjectText(thisLine);
                }
            }

            final R archetype = archetypeBuilder.finish();

            if (firstArch != null) {
                firstArch.addTailPart(archetype);
            } else if (addToPanel(isInternPath, editorFolder, archetype)) {
                final String panel;
                final String folder;
                if (!archetypeSet.isLoadedFromArchive() || archName != null) {
                    panel = panelName;
                    folder = folderName;
                } else {
                    final String[] names = StringUtils.PATTERN_SLASH.split(editorFolder, 3);
                    panel = names[0];
                    folder = names.length >= 2 ? names[1] : names[0];
                }
                archetype.setEditorFolder(panel + "/" + folder);
            } else {
                archetype.setEditorFolder(GameObject.EDITOR_FOLDER_INTERN);
            }
            finishParseArchetypePart(firstArch, archetype, errorViewCollector);
            try {
                archetypeSet.addArchetype(archetype);
            } catch (final DuplicateArchetypeException ex) {
                errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ex.getMessage());
            }

            archMore = false;

            if (archName != null) {
                archetype.setArtifact();
                if (prototype != null) {
                    archetype.addObjectText(AttributeListUtils.diffArchTextKeys(archetype, prototype));
                }
                break;
            }
            lastArch = archetype;
            reportError = true;

            thisLine2 = in.readLine();
            
            // careful, what about files containing multiple Object

        }

        
        if (firstArch != null) {
            finishParseArchetype(firstArch);
        }
    }

    /**
     * Parses a "msg..endmsg" block.
     * @param in the reader to read from
     * @param errorViewCollector the error view collector for reporting errors
     * @throws IOException if an I/O error occurs
     */
    private void parseMsg(@NotNull final BufferedReader in, @NotNull final ErrorViewCollector errorViewCollector) throws IOException {
        final StringBuilder msgText = new StringBuilder();

        while (true) {
            final String thisLine2 = in.readLine();
            if (thisLine2 == null) {
                errorViewCollector.addError(ErrorViewCategory.ARCHETYPE_INVALID, "Truncated archetype: msg not terminated by endmsg");
                return;
            }

            final String thisLine3 = thisLine2.trim();
            if (thisLine3.equals("endmsg")) {
                break;
            }

            // keep leading whitespace
            msgText.append(thisLine2).append("\n");
        }

        if (msgText.length() > 0) {
            archetypeBuilder.setMsgText(msgText.toString());
        }
    }

    /**
     * Parses an "anim..mina" block.
     * @param in the reader to read from
     * @param errorViewCollector the error view collector for reporting errors
     * @param path the archetype's path name
     * @throws IOException if an I/O error occurs
     */
    private void parseAnim(@NotNull final BufferedReader in, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String path) throws IOException {
        final StringBuilder animText = new StringBuilder();

        while (true) {
            final String thisLine2 = in.readLine();
            if (thisLine2 == null) {
                errorViewCollector.addError(ErrorViewCategory.ARCHETYPE_INVALID, "Truncated animation: anim not terminated by mina");
                return;
            }

            final String thisLine3 = thisLine2.trim();
            if (thisLine3.startsWith("#") || thisLine3.length() == 0) {
                continue;
            }

            if (thisLine3.equals("mina")) {
                break;
            }

            animText.append(thisLine3).append("\n");
        }

        final String animationName = "_" + archetypeBuilder.getArchetypeName();
        try {
            animationObjects.addAnimationObject(animationName, animText.toString(), path + animationName);
        } catch (final DuplicateAnimationException e) {
            errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("logDuplicateAnimation", e.getDuplicate().getAnimName()));
        } catch (final IllegalAnimationException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "illegal animation: " + ex.getAnimationObject().getPath());
        }
    }
    
    

    /**
     * Called when a new archetype starts.
     */
    protected abstract void initParseArchetype();

    /**
     * Returns whether a give input line denotes the start of a new archetype.
     * @param line the input line
     * @return whether the line is a start line
     */
    protected abstract boolean isStartLine(@NotNull final String line);

    /**
     * Called for each processed line.
     * @param in the reader reading the archetype definition
     * @param line the input line
     * @param line2 the tripped input line
     * @param archetypeBuilder the archetype builder for the current archetype
     * @param errorViewCollector the error view collector for reporting errors
     * @param invObjects the inventory objects of the current archetype
     * @return whether the line has been consumed
     * @throws IOException if an I/O error occurs
     */
    protected abstract boolean processLine(@NotNull final BufferedReader in, @NotNull final String line, @NotNull final String line2, @NotNull final B archetypeBuilder, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final List<G> invObjects) throws IOException;

    /**
     * Called after the "end" line of a part has been read.
     * @param firstArch the head part or <code>null</code> if
     * <code>archetype</code> is the head part
     * @param archetype the tail part
     * @param errorViewCollector the error view collector for reporting errors
     */
    protected abstract void finishParseArchetypePart(@Nullable final R firstArch, @NotNull final R archetype, @NotNull final ErrorViewCollector errorViewCollector);

    /**
     * Called after all parts of an archetype have been processed.
     * @param archetype the archetype
     */
    protected abstract void finishParseArchetype(@NotNull final R archetype);

    /**
     * Returns whether an archetype should be added to the archetype chooser.
     * @param isInternPath whether the archetype's path contains "/intern/"
     * @param editorFolder the editor_folder attribute
     * @param archetype the archetype to add
     * @return whether the archetype should be added
     */
    protected abstract boolean addToPanel(final boolean isInternPath, @NotNull final String editorFolder, @NotNull final R archetype);

}
