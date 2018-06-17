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

package net.sf.gridarta.gui.spells;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.utils.FileChooserUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.util.filter.file.FilenameFileFilter;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class SpellsUtils {

    /**
     * Maximum number of characters to read in readUntil.
     */
    private static final long READ_MAX = 10000L;

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(SpellsUtils.class);

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * File filter for filtering spellist.h files.
     */
    private static final FileFilter SPELL_LIST_H_FILE_FILTER = new FilenameFileFilter(true, "spellist.h", "spellist.h");

    /**
     * The spell file name.
     */
    @NotNull
    private final String spellFile;

    /**
     * Creates a new instance.
     * @param spellFile the spell file name
     */
    public SpellsUtils(@NotNull final String spellFile) {
        this.spellFile = spellFile;
    }

    /**
     * Opens a file chooser to select the spell list file, then import spells.
     * @param dir the directory to use
     * @param parent the parent component for dialog boxes
     */
    public void importSpells(@NotNull final File dir, @NotNull final Component parent) {
        // open a file chooser window
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open CF Spell List File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(SPELL_LIST_H_FILE_FILTER);  // apply file filter
        final File cd = new File(System.getProperty("user.dir"));
        final File sd = new File(cd, "../server/src/include");
        FileChooserUtils.setCurrentDirectory(fileChooser, sd.exists() ? sd : cd);

        final int returnVal = fileChooser.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // now import spells from selected file
            final File tmpSpellFile = fileChooser.getSelectedFile();
            final int spNum = importSpells(tmpSpellFile, dir);
            if (spNum > 0) {
                // yeah it worked
                ACTION_BUILDER.showMessageDialog(parent, "importSpellsSuccess", spNum);
            } else {
                // spell collect failed
                ACTION_BUILDER.showMessageDialog(parent, "importSpellsFailed");
            }
        }
    }

    /**
     * Reads all spells from a Crossfire or Daimonin spell list file and write
     * an alphabetical list into "spells.def".
     * @param spellFile spell file to read
     * @param dir the base directory to load the spells file from
     * @return number of successfully collected spells
     */
    private int importSpells(final File spellFile, @NotNull final File dir) {
        final Map<String, String> spells = new TreeMap<String, String>();
        if (spellFile.getName().equalsIgnoreCase("spellist.h")) {
            try {
                final FileInputStream fis = new FileInputStream(spellFile.getAbsolutePath());
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader in = new BufferedReader(isr);
                        try {
                            readUntil(in, "spell spells", null);
                            readUntil(in, "{", null);

                            int counter = 0;
                            while (true) {
                                try {
                                    readUntil(in, "{", "}");
                                } catch (final EOFException ignored) {
                                    // Eventually expected exception, don't handle.
                                    break;
                                }
                                readUntil(in, "\"", null);
                                final String name = readUntil(in, "\"").trim();
                                readUntil(in, "}", null);

                                spells.put(name, Integer.toString(counter++));
                            }
                        } finally {
                            in.close();
                        }
                    } finally {
                        isr.close();
                    }
                } finally {
                    fis.close();
                }
            } catch (final FileNotFoundException ignored) {
                log.error("File '" + spellFile.getAbsolutePath() + "' not found!");
            } catch (final IOException ex) {
                log.error("Cannot read file '" + spellFile.getAbsolutePath() + "': " + ex.getMessage());
            }
        }

        // --------- now write the "spells.def" file ---------
        if (!spells.isEmpty()) {
            // FIXME: This should use DOM for writing the file.
            // create new file for writing (replaces old one if existent)
            if (!dir.exists() || !dir.isDirectory()) { // FIXME What if dir exists and is not a directory? mkdirs will fail then!
                // create the config dir
                dir.mkdirs();
            }
            final File dirFile = new File(dir, this.spellFile);

            try {
                final PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(dirFile), "utf-8"));
                try {
                    // header:
                    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                    out.println("<!DOCTYPE spells SYSTEM \"spells.dtd\">");
                    out.println("<!--");
                    out.println("  - ##########################################################");
                    out.println("  - #   You may add new spells to this file, but there's no  #");
                    out.println("  - #  need to do it because the file can be auto-generated. #");
                    out.println("  - # In the editor, select menu \"Resources->Collect Spells\" #");
                    out.println("  - #        to generate a new version of this file.         #");
                    out.println("  - ##########################################################");
                    out.println("  -->");
                    out.println("<!-- Generated on: " + new Date() + " -->");
                    out.println("<spells>");

                    final String[] spaces = { "  ", " ", "" };
                    for (final Map.Entry<String, String> entry : spells.entrySet()) {
                        final String id = entry.getValue();
                        out.println("  <spell id=\"" + id + '\"' + spaces[id.length() - 1] + " name=\"" + entry.getKey() + "\" />");
                    }
                    out.println("</spells>");
                } finally {
                    out.close();
                }
            } catch (final IOException ex) {
                log.error("Cannot write file '" + dirFile.getAbsolutePath() + "': " + ex.getMessage());
            }
        }
        return spells.size();
    }

    /**
     * Reads characters from the BufferedReader stream till 'tag' is found. If
     * found, the method returns with stream pointing right after the appearance
     * of 'tag'.
     * @param stream ascii input stream to read from
     * @param tag stop reading at the string 'tag'
     * @param abort throw <code>EOFException</code> at string 'abort' (this can
     * be null)
     * @throws IOException an I/O-error occurred while reading the file
     * @throws EOFException the end of file was reached, or the 'abort' string
     * has been encountered
     * @todo Should the encounter of the abort string before the tag really be
     * an EOFException? That's semantically wrong, but current usage code relies
     * on this :(
     */
    private static void readUntil(@NotNull final BufferedReader stream, @NotNull final CharSequence tag, @Nullable final CharSequence abort) throws IOException {
        int c; // character value, read from the stream
        int t = 0; // tag index
        int a = 0; // abort index

        if (abort != null) {
            // look both for 'tag' and 'abort'
            do {
                c = stream.read();
                if (c == tag.charAt(t)) {
                    t++;
                } else {
                    t = 0;
                }
                if (c == abort.charAt(a)) {
                    a++;
                } else {
                    a = 0;
                }
            } while (t < tag.length() && a < abort.length() && c != -1);
        } else {
            // look only for 'tag'
            do {
                c = stream.read();
                if (c == tag.charAt(t)) {
                    t++;
                } else {
                    t = 0;
                }
            } while (t < tag.length() && c != -1);
        }

        // if we did not find the tag, an EOFException is thrown
        if (c == -1) {
            throw new EOFException();
        }

        // if we found the string 'abort', throw EOFException as well
        if (abort != null && a == abort.length()) {
            throw new EOFException();
        }
    }

    /**
     * Reads characters from the BufferedReader stream till 'tag' is found.
     * Similar to readUntil(), except that the read String is returned. 'tag' is
     * not included in the returned String.
     * @param stream ascii input stream to read from
     * @param tag stop reading at the string 'tag'
     * @return the string between the starting position of 'stream' (inclusive)
     *         and the first character of 'tag' (exclusive)
     * @throws IOException an I/O-error occurred while reading the file
     * @throws EOFException the end of file was reached
     */
    private static String readUntil(@NotNull final BufferedReader stream, @NotNull final CharSequence tag) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int c; // character value, read from the stream
        int t = 0; // index

        long count = 0L; // counter (to realize when shooting past EOF)
        final long maxCount = READ_MAX; // bail out when counter exceeds this value

        do {
            c = stream.read(); // read one character
            sb.append((char) c);
            if (c == tag.charAt(t)) {
                t++;
            } else {
                t = 0;
            }
        } while (t < tag.length() && c != -1 && count++ < maxCount);

        // if we did not find the tag, an EOFException is thrown
        if (c == -1 || count >= maxCount) {
            throw new EOFException();
        }
        // cut 'tag' off, at the end of the string
        return sb.substring(0, sb.length() - tag.length());
    }

}
