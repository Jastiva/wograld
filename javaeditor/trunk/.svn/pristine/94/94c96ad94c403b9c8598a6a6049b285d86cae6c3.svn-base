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

package net.sf.gridarta.model.gameobject;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.utils.IOUtils;
import net.sf.gridarta.utils.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The MultiPositionData class stores an array of numbers which is required in
 * order to calculate display positions of ISO multi-part objects.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class MultiPositionData {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(MultiPositionData.class);

    /**
     * Number of rows in the array.
     */
    public static final int Y_DIM = 16;

    /**
     * The {@link IsoMapSquareInfo} to use.
     */
    @NotNull
    private final IsoMapSquareInfo isoMapSquareInfo;

    /**
     * Array with position data.
     */
    @NotNull
    private final MultiPositionEntry[] data = new MultiPositionEntry[Y_DIM];

    /**
     * Creates a new instance.
     * @param isoMapSquareInfo the iso square info to use
     */
    public MultiPositionData(@NotNull final IsoMapSquareInfo isoMapSquareInfo) {
        this.isoMapSquareInfo = isoMapSquareInfo;
    }

    /**
     * Load the array-data from file. An error is reported when the numbers in
     * the file don't match expected array dimensions.
     * @param errorView the error view for reporting errors
     * @param url the URL of the archdef file to read
     */
    public void load(@NotNull final ErrorView errorView, @NotNull final URL url) {
        final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, url);
        // read data file line by line, parsing numbers into the array
        try {
            final InputStream inputStream = url.openStream();
            try {
                final Reader reader = new InputStreamReader(inputStream, IOUtils.MAP_ENCODING);
                try {
                    final BufferedReader bufferedReader = new BufferedReader(reader);
                    try {
                        final LineNumberReader lnr = new LineNumberReader(bufferedReader);
                        try {
                            int yp = 0;  // y-index in the data array

                            // read the whole file line by line
                            boolean hasErrors = false;
                            while (true) {
                                final String inputLine = lnr.readLine();
                                if (inputLine == null) {
                                    break;
                                }
                                final String line = inputLine.trim();
                                if (line.length() == 0 || line.startsWith("#")) {
                                    continue;
                                }
                                if (yp >= Y_DIM) {
                                    errorViewCollector.addWarning(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "too many entries");
                                    break;
                                }
                                final String[] numbers = StringUtils.PATTERN_SPACE.split(line);
                                if (numbers.length != 2) {
                                    errorViewCollector.addError(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "syntax error");
                                    hasErrors = true;
                                    continue;
                                }
                                final Dimension d = new Dimension();
                                try {
                                    // parse and store it as integer
                                    d.width = Integer.parseInt(numbers[0]);
                                } catch (final NumberFormatException ignored) {
                                    errorViewCollector.addError(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "invalid number: " + numbers[0]);
                                    hasErrors = true;
                                    continue;
                                }
                                try {
                                    // parse and store it as integer
                                    d.height = Integer.parseInt(numbers[1]);
                                } catch (final NumberFormatException ignored) {
                                    errorViewCollector.addError(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, lnr.getLineNumber(), "invalid number: " + numbers[1]);
                                    hasErrors = true;
                                    continue;
                                }
                                data[yp++] = new MultiPositionEntry(isoMapSquareInfo, d);
                            }

                            // report if there haven't been enough rows in the file
                            if (yp < Y_DIM && !hasErrors) {
                                errorViewCollector.addWarning(ErrorViewCategory.ARCHDEF_ENTRY_INVALID, "missing " + (Y_DIM - yp) + " entries");
                            }

                            // confirm load process
                            if (log.isInfoEnabled()) {
                                log.info("Loaded multi-part position data from '" + url + "'");
                            }
                        } finally {
                            lnr.close();
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
            errorViewCollector.addWarning(ErrorViewCategory.ARCHDEF_FILE_INVALID, ex.getMessage());
        }
    }

    /**
     * Calculate the x-offset from the leftmost pixel of the big face image and
     * the default x-position (The default position is where a single-square
     * image would be put).
     * @param shapeID ID number for the multi-square shape (-> rows in position
     * data file)
     * @param positionID number of square in the big bunch
     * @return x-offset
     */
    public int getXOffset(final int shapeID, final int positionID) {
        final MultiPositionEntry entry = data[shapeID];
        return entry == null ? 0 : entry.getXOffset(positionID);
    }

    /**
     * Calculate the y-offset from the topmost pixel of the big face image and
     * the default y-position (The default position is where a single-square
     * image would be put).
     * @param shapeID ID number for the multi-square shape (-> rows in position
     * data file)
     * @param positionID number of square in the big bunch
     * @return y-offset
     */
    public int getYOffset(final int shapeID, final int positionID) {
        final MultiPositionEntry entry = data[shapeID];
        return entry == null ? 0 : entry.getYOffset(positionID);
    }

    /**
     * Returns the total width for a multi-square image.
     * @param shapeID the shape ID
     * @return the width in pixels
     */
    public int getWidth(final int shapeID) {
        final MultiPositionEntry entry = data[shapeID];
        return entry == null ? 0 : entry.getWidth();
    }

    /**
     * Returns the total height for a multi-square image.
     * @param shapeID the shape ID
     * @return the height in pixels
     */
    public int getHeight(final int shapeID) {
        final MultiPositionEntry entry = data[shapeID];
        return entry == null ? 0 : entry.getHeight();
    }

}
