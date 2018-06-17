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

package net.sf.gridarta.model.treasurelist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import net.sf.gridarta.model.configsource.ConfigSource;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.IOUtils;
import net.sf.japi.util.EnumerationIterator;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Loader for treasure list files. The same format is used for Atrinik,
 * Crossfire, and Daimonin.
 * @author Andreas Kirschbaum
 */
public class TreasureLoader {

    /**
     * The {@link Logger} for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(TreasureLoader.class);

    /**
     * {@link Comparator} for {@link TreasureTreeNode TreasureTreeNodes} that
     * compares their object name case insensitive.
     */
    @NotNull
    private static final Comparator<TreasureTreeNode> treasureNodeComparator = new Comparator<TreasureTreeNode>() {

        @Override
        public int compare(@NotNull final TreasureTreeNode o1, @NotNull final TreasureTreeNode o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(o1.getTreasureObj().getName(), o2.getTreasureObj().getName());
        }

    };

    /**
     * The {@link FilenameFilter} for recursively loading treasure list files.
     */
    @NotNull
    private static final FilenameFilter treasureListFilter = new FilenameFilter() {

        @Override
        public boolean accept(@NotNull final File dir, @NotNull final String name) {
            final File fullPath = new File(dir, name);
            if (name.startsWith(".")) {
                return false;
            }
            if (fullPath.isDirectory()) {
                return true;
            }
            final String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".tl") || lowerCaseName.endsWith(".trs");
        }

    };

    /**
     * Private constructor to prevent instantiation.
     */
    private TreasureLoader() {
    }

    /**
     * Parses a treasure file into a {@link TreasureTree} instance.
     * @param errorView the error view to use
     * @param specialTreasureLists maps treasure list name to parent node
     * @param configSource the config source to read from
     * @param globalSettings the global settings to use
     * @return the parsed treasure tree
     */
    @NotNull
    public static TreasureTree parseTreasures(@NotNull final ErrorView errorView, @NotNull final Map<String, TreasureTreeNode> specialTreasureLists, @NotNull final ConfigSource configSource, @NotNull final GlobalSettings globalSettings) {
        final List<TreasureTreeNode> tmpList = new ArrayList<TreasureTreeNode>();   // tmp. container for all treasurelists
        final List<TreasureTreeNode> needLink = new ArrayList<TreasureTreeNode>();  // all sub-treasurelist nodes that need linking

        // first step: parsing data file, adding all treasurelists to the tmpList vector
        int index = 0;
        while (true) {
            final File treasureLocation;
            try {
                treasureLocation = configSource.getFile(globalSettings, "treasures", index);
            } catch (final IOException ex) {
                errorView.addWarning(ErrorViewCategory.TREASURES_FILE_INVALID, ex.getMessage());
                index++;
                continue;
            }
            if (treasureLocation == null) {
                break;
            }

            final int tmpListSize = tmpList.size();
            if (treasureLocation.isDirectory()) {
                loadTreasureDir(errorView, treasureLocation, tmpList, needLink);
            } else if (treasureLocation.isFile()) {
                loadTreasureList(errorView, treasureLocation, tmpList, needLink);
            } else {
                new ErrorViewCollector(errorView, treasureLocation).addWarning(ErrorViewCategory.TREASURES_FILE_INVALID, "location is neither file nor directory");
            }
            if (log.isInfoEnabled()) {
                log.info("Loaded " + (tmpList.size() - tmpListSize) + " treasurelists from '" + treasureLocation + "'.");
            }
            index++;
        }

        return createTreasureTree(tmpList, needLink, specialTreasureLists);
    }

    /**
     * Creates a {@link TreasureTree} instance from a list of {@link
     * TreasureTreeNode TreasureTreeNodes}.
     * @param tmpList the treasure tree nodes to add to the treasure tree
     * @param needLink all treasure tree nodes within <code>tmpList</code>
     * @param specialTreasureLists maps treasure list name to parent node
     * @return the new treasure tree instance
     */
    @NotNull
    private static TreasureTree createTreasureTree(@NotNull final List<TreasureTreeNode> tmpList, @NotNull final Iterable<TreasureTreeNode> needLink, @NotNull final Map<String, TreasureTreeNode> specialTreasureLists) {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Treasurelists:");
        final TreasureTree treasures = new TreasureTree(root);
        treasures.putAll(tmpList);
        addTopLevelEntries(tmpList, specialTreasureLists, root);
        linkSubLists(needLink, treasures);
        addSpecialEntries(specialTreasureLists, root);
        return treasures;
    }

    /**
     * Adds a list of {@link TreasureTreeNode TreasureTreeNodes} to a root
     * {@link DefaultMutableTreeNode}. The entries are added with the ordering
     * specified by {@link #treasureNodeComparator}.
     * @param tmpList the treasure tree nodes to add
     * @param specialTreasureLists maps treasure list name to parent node
     * @param root the root tree node to add to
     */
    private static void addTopLevelEntries(@NotNull final List<TreasureTreeNode> tmpList, @NotNull final Map<String, TreasureTreeNode> specialTreasureLists, @NotNull final DefaultMutableTreeNode root) {
        Collections.sort(tmpList, treasureNodeComparator);

        // Calculate the real ratio of chances (summed up to be 100%). Also
        // attach lists to tree model.
        for (final TreasureTreeNode realNode : tmpList) {
            realNode.recalculateChances();

            // check for special treasurelists, which are put in sub-folders
            final DefaultMutableTreeNode specialTreasureList = specialTreasureLists.get(realNode.getTreasureObj().getName());
            if (specialTreasureList != null) {
                specialTreasureList.add(realNode);
            } else {
                root.add(realNode); // normal treasurelist - attach to root node
            }
        }
    }

    /**
     * Links sub-treasure tree nodes to their parent nodes.
     * @param needLink the sub-treasure tree nodes to link
     * @param treasures the model to link into
     */
    private static void linkSubLists(@NotNull final Iterable<TreasureTreeNode> needLink, @NotNull final TreasureTree treasures) {
        final List<TreasureTreeNode> needSecondLink = new ArrayList<TreasureTreeNode>();
        linkSubList2(needLink, treasures, false, needSecondLink);
        linkSubList2(needSecondLink, treasures, true, null); // do second linking to link all what is left
    }

    private static void linkSubList2(@NotNull final Iterable<TreasureTreeNode> needLink, @NotNull final TreasureTree treasures, final boolean processSecondLinking, @Nullable final List<TreasureTreeNode> needSecondLink) {
        for (final TreasureTreeNode node : needLink) {
            final TreasureTreeNode realNode = getRealNode(treasures, node);
            if (realNode != null) {
                node.getTreasureObj().copyListType(realNode.getTreasureObj());

                for (final TreasureTreeNode ttn : new EnumerationIterator<TreasureTreeNode>(realNode.children())) {
                    node.add(ttn.getClone(processSecondLinking, needSecondLink));
                }
            }
        }
    }

    /**
     * Returns the "real" (top-level) node that corresponds to a given node.
     * @param treasures the treasures to search
     * @param node the node to search for
     * @return the real node or <code>null</code>
     */
    @Nullable
    private static TreasureTreeNode getRealNode(@NotNull final TreasureTree treasures, @NotNull final TreasureTreeNode node) {
        return treasures.get(node.getTreasureObj().getName());
    }

    /**
     * Add the special treasure list parents to the root {@link
     * DefaultMutableTreeNode}.
     * @param specialTreasureLists maps treasure list name to parent node
     * @param root the root default mutable tree node
     */
    private static void addSpecialEntries(@NotNull final Map<String, TreasureTreeNode> specialTreasureLists, @NotNull final DefaultMutableTreeNode root) {
        for (final MutableTreeNode folder : specialTreasureLists.values()) {
            root.add(folder);
        }
    }

    /**
     * Parses one treasurelist file.
     * @param errorView the error view to use
     * @param file the file to read from
     * @param tmpList a collection to which all parsed treasure tree nodes are
     * added
     * @param needLink all sub-treasurelist nodes that need linking
     * @see #parseTreasures(ErrorView, Map, ConfigSource, GlobalSettings)
     */
    private static void loadTreasureList(@NotNull final ErrorView errorView, @NotNull final File file, final Collection<TreasureTreeNode> tmpList, final List<TreasureTreeNode> needLink) {
        final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, file);
        try {
            final InputStream inputStream = new FileInputStream(file);
            try {
                final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                try {
                    final BufferedReader bufferedReader = new BufferedReader(reader);
                    try {
                        while (true) {
                            final String rawLine = bufferedReader.readLine();
                            if (rawLine == null) {
                                break;
                            }
                            final String line = rawLine.trim();
                            if (line.length() > 0 && !line.startsWith("#")) {
                                if (line.startsWith("treasure")) {
                                    final int i = line.indexOf(' ');
                                    if (i == -1) {
                                        errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "unexpected line: \"" + line + "\"");
                                    } else {
                                        final String name = line.substring(i).trim();
                                        final TreasureTreeNode node = new TreasureTreeNode(new TreasureListTreasureObj(name, line.startsWith("treasureone") ? TreasureListTreasureObjType.ONE : TreasureListTreasureObjType.MULTI));
                                        tmpList.add(node);

                                        readInsideList(errorViewCollector, node, bufferedReader, needLink);
                                    }
                                } else {
                                    errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "unexpected line: \"" + line + "\"");
                                }
                            }
                        }
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
            errorViewCollector.addWarning(ErrorViewCategory.TREASURES_FILE_INVALID, ex.getMessage());
        }
    }

    /**
     * Recursively traverses a directory and parses all treasurelist files.
     * @param errorView the error view to use
     * @param dir directory to read from
     * @param tmpList a collection to which all parsed treasure tree nodes are
     * added
     * @param needLink all sub-treasurelist nodes that need linking
     * @see #parseTreasures(ErrorView, Map, ConfigSource, GlobalSettings)
     */
    private static void loadTreasureDir(@NotNull final ErrorView errorView, @NotNull final File dir, @NotNull final List<TreasureTreeNode> tmpList, @NotNull final List<TreasureTreeNode> needLink) {
        final String[] traverse = dir.list(treasureListFilter);
        if (traverse == null) {
            return;
        }

        Arrays.sort(traverse);
        for (final String entry : traverse) {
            final File file = new File(dir, entry);
            if (file.isFile()) {
                loadTreasureList(errorView, file, tmpList, needLink);
            } else if (file.isDirectory()) {
                loadTreasureDir(errorView, file, tmpList, needLink);
            }
        }
    }

    /**
     * Reads and parses the text inside a treasurelist definition.
     * @param errorViewCollector the error view collector to use
     * @param parentNode the parent tree node
     * @param reader the reader to read from
     * @param needLink the list containing all sub-treasurelist nodes which need
     * linking
     * @throws IOException in case of I/O problems reading from
     * <var>reader</var>.
     * @noinspection IfStatementWithIdenticalBranches
     */
    private static void readInsideList(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final TreasureTreeNode parentNode, @NotNull final BufferedReader reader, @NotNull final List<TreasureTreeNode> needLink) throws IOException {
        TreasureTreeNode node = null;

        boolean insideArch = false;

        while (true) {
            final String rawLine = reader.readLine();
            if (rawLine == null) {
                break;
            }
            final String line = rawLine.trim();
            if (line.equals("end")) {
                break;
            }
            if (line.length() > 0 && !line.startsWith("#")) {
                if (insideArch) {
                    if (line.equals("more")) {
                        insideArch = false;
                    } else if (line.startsWith("chance")) {
                        try {
                            node.getTreasureObj().setChance(Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim()));
                        } catch (final NumberFormatException ignored) {
                            errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "list " + parentNode.getTreasureObj().getName() + ": arch " + node.getTreasureObj().getName() + " chance is not a number.");
                        }
                    } else if (line.startsWith("nrof")) {
                        try {
                            node.getTreasureObj().setNrof(Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim()));
                        } catch (final NumberFormatException ignored) {
                            errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "list " + parentNode.getTreasureObj().getName() + ": arch " + node.getTreasureObj().getName() + " nrof value is not a number.");
                        }
                    } else if (line.startsWith("magic")) {
                        try {
                            node.getTreasureObj().setMagic(Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim()));
                        } catch (final NumberFormatException ignored) {
                            errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "list " + parentNode.getTreasureObj().getName() + ": arch " + node.getTreasureObj().getName() + " magic value is not a number.");
                        }
                    } else if (line.startsWith("artifact_chance") || line.startsWith("title") || line.startsWith("difficulty") || line.startsWith("quality_quality") || line.startsWith("quality_range") || line.startsWith("material_quality") || line.startsWith("material_range")) {
                        // ignored for now; prevent error message when loading Daimonin/Atrinik treasure lists
                    } else if (line.startsWith("name") || line.startsWith("t_style")) {
                        // ignored for now; prevent error message when loading Atrinik treasure lists
                    } else if (line.equals("no")) {
                        final int parentChance = node.getTreasureObj().getChance();
                        final int chance;
                        if (parentChance == TreasureObj.UNSET) {
                            errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "list " + parentNode.getTreasureObj().getName() + ": arch " + node.getTreasureObj().getName() + " has NO-list but chance is unset!");
                            chance = 0;
                        } else {
                            chance = 100 - parentChance;
                        }

                        final TreasureTreeNode subNode = new TreasureTreeNode(new NoTreasureObj(chance));
                        node.add(subNode);

                        readInsideList(errorViewCollector, subNode, reader, needLink);
                    } else if (line.equals("yes")) {
                        final TreasureTreeNode subNode = new TreasureTreeNode(new YesTreasureObj(node.getTreasureObj().getChance()));
                        node.add(subNode);
                        readInsideList(errorViewCollector, subNode, reader, needLink);
                    } else {
                        errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, "list " + parentNode.getTreasureObj().getName() + ", arch " + node.getTreasureObj().getName() + ": unexpected line: \"" + line + "\"");
                    }
                } else {
                    if (line.startsWith("arch ")) {
                        node = new TreasureTreeNode(new ArchTreasureObj(line.substring(line.indexOf(' ')).trim()));
                        parentNode.add(node);
                        insideArch = true;
                    } else if (line.startsWith("list ")) {
                        final String newName = line.substring(line.indexOf(' ')).trim();
                        node = new TreasureTreeNode(new TreasureListTreasureObj(newName, TreasureListTreasureObjType.MULTI));
                        parentNode.add(node);
                        needLink.add(node); // this node needs to be linked to it's content later

                        // check for potential infinite loops by lists containing itself
                        if (node.getTreasureObj().isTreasureList() && parentNode.getTreasureObj().getName().equals(newName)) {
                            node.getTreasureObj().setHasLoop(true);
                        }

                        insideArch = true;
                    } else {
                        errorViewCollector.addWarning(ErrorViewCategory.TREASURES_ENTRY_INVALID, parentNode + ": unknown line: \"" + line + "\"");
                    }
                }
            }
        }
    }

}
