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

package net.sf.gridarta.var.crossfire.model.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Formatter;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.io.AbstractMapArchObjectParser;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.io.MapArchObjectParser} for Crossfire map arch
 * object instances.
 * @author Andreas Kirschbaum
 */
public class MapArchObjectParser extends AbstractMapArchObjectParser<MapArchObject> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void load(@NotNull final BufferedReader reader, @NotNull final MapArchObject mapArchObject) throws IOException {
        super.load(reader, mapArchObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(@NotNull final Appendable appendable, @NotNull final MapArchObject mapArchObject) throws IOException {
        final Formatter format = new Formatter(appendable);
        appendable.append("arch map\n");
        if (mapArchObject.getMapName().length() > 0) {
            format.format("name %s\n", mapArchObject.getMapName());
        }
        if (mapArchObject.getSwapTime() != 0) {
            format.format("swap_time %d\n", mapArchObject.getSwapTime());
        }
        if (mapArchObject.getResetTimeout() != 0) {
            format.format("reset_timeout %d\n", mapArchObject.getResetTimeout());
        }
        if (mapArchObject.isFixedReset()) {
            appendable.append("fixed_resettime 1\n");
        }
        if (mapArchObject.getDifficulty() != 0) {
            format.format("difficulty %d\n", mapArchObject.getDifficulty());
        }
        if (mapArchObject.getRegion().length() > 0) {
            format.format("region %s\n", mapArchObject.getRegion());
        }
        if (mapArchObject.getShopItems().length() > 0) {
            format.format("shopitems %s\n", mapArchObject.getShopItems());
        }
        // The following floating point equality comparison is assumed to be okay
        if (mapArchObject.getShopGreed() != 0.0) {
            format.format("shopgreed %g\n", mapArchObject.getShopGreed());
        }
        if (mapArchObject.getShopMin() != 0) {
            format.format("shopmin %d\n", mapArchObject.getShopMin());
        }
        if (mapArchObject.getShopMax() != 0) {
            format.format("shopmax %d\n", mapArchObject.getShopMax());
        }
        if (mapArchObject.getShopRace().length() > 0) {
            format.format("shoprace %s\n", mapArchObject.getShopRace());
        }
        if (mapArchObject.getDarkness() != 0) {
            format.format("darkness %d\n", mapArchObject.getDarkness());
        }
        format.format("width %d\n", mapArchObject.getMapSize().getWidth());
        format.format("height %d\n", mapArchObject.getMapSize().getHeight());
        if (mapArchObject.getEnterX() != 0) {
            format.format("enter_x %d\n", mapArchObject.getEnterX());
        }
        if (mapArchObject.getEnterY() != 0) {
            format.format("enter_y %d\n", mapArchObject.getEnterY());
        }
        if (mapArchObject.getText().trim().length() > 0) {
            format.format("msg\n" + "%s\n" + "endmsg\n", mapArchObject.getText().trim());
        }
        if (mapArchObject.getLore().trim().length() > 0) {
            format.format("maplore\n" + "%s\n" + "endmaplore\n", mapArchObject.getLore().trim());
        }
        if (mapArchObject.isUnique()) {
            appendable.append("unique 1\n");
        }
        if (mapArchObject.isTemplate()) {
            appendable.append("template 1\n");
        }
        if (mapArchObject.isOutdoor()) {
            appendable.append("outdoor 1\n");
        }
        if (mapArchObject.getTemperature() != 0) {
            format.format("temp %d\n", mapArchObject.getTemperature());
        }
        if (mapArchObject.getPressure() != 0) {
            format.format("pressure %d\n", mapArchObject.getPressure());
        }
        if (mapArchObject.getHumidity() != 0) {
            format.format("humid %d\n", mapArchObject.getHumidity());
        }
        if (mapArchObject.getWindSpeed() != 0) {
            format.format("windspeed %d\n", mapArchObject.getWindSpeed());
        }
        if (mapArchObject.getWindDirection() != 0) {
            format.format("winddir %d\n", mapArchObject.getWindDirection());
        }
        if (mapArchObject.getSky() != 0) {
            format.format("sky %d\n", mapArchObject.getSky());
        }
        for (final Direction direction : Direction.values()) {
            if (mapArchObject.getTilePath(direction).length() > 0) {
                format.format("tile_path_%d %s\n", direction.ordinal() + 1, mapArchObject.getTilePath(direction));
            }
        }
        if (mapArchObject.isNoSmooth()) {
            appendable.append("nosmooth 1\n");
        }
        if (mapArchObject.getBackgroundMusic().length() > 0) {
            format.format("background_music %s\n", mapArchObject.getBackgroundMusic());
        }
        appendable.append("end\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean parseLine(@NotNull final String line, @NotNull final MapArchObject mapArchObject, @NotNull final BufferedReader reader) throws IOException {
        if (line.startsWith("region ")) {
            mapArchObject.setRegion(line.substring(7));
            return true;
        }

        if (line.startsWith("unique ")) {
            mapArchObject.setUnique(NumberUtils.parseInt(line.substring(7)) != 0);
            return true;
        }

        if (line.startsWith("template ")) {
            mapArchObject.setTemplate(NumberUtils.parseInt(line.substring(9)) != 0);
            return true;
        }

        if (line.startsWith("nosmooth ")) {
            mapArchObject.setNoSmooth(NumberUtils.parseInt(line.substring(9)) != 0);
            return true;
        }

        if (line.startsWith("shopitems ")) {
            mapArchObject.setShopItems(line.substring(10));
            return true;
        }

        if (line.startsWith("shoprace ")) {
            mapArchObject.setShopRace(line.substring(9));
            return true;
        }

        if (line.startsWith("shopmin ")) {
            mapArchObject.setShopMin(NumberUtils.parseInt(line.substring(8)));
            return true;
        }

        if (line.startsWith("shopmax ")) {
            mapArchObject.setShopMax(NumberUtils.parseInt(line.substring(8)));
            return true;
        }

        if (line.startsWith("shopgreed ")) {
            mapArchObject.setShopGreed(NumberUtils.parseDouble(line.substring(10)));
            return true;
        }

        if (line.startsWith("temp ")) {
            mapArchObject.setTemperature(NumberUtils.parseInt(line.substring(5)));
            return true;
        }

        if (line.startsWith("pressure ")) {
            mapArchObject.setPressure(NumberUtils.parseInt(line.substring(9)));
            return true;
        }

        if (line.startsWith("humid ")) {
            mapArchObject.setHumidity(NumberUtils.parseInt(line.substring(6)));
            return true;
        }

        if (line.startsWith("windspeed ")) {
            mapArchObject.setWindSpeed(NumberUtils.parseInt(line.substring(10)));
            return true;
        }

        if (line.startsWith("winddir ")) {
            mapArchObject.setWindDirection(NumberUtils.parseInt(line.substring(8)));
            return true;
        }

        if (line.startsWith("sky ")) {
            mapArchObject.setSky(NumberUtils.parseInt(line.substring(4)));
            return true;
        }

        if (line.equals("maplore")) {
            while (true) {
                final String loreLine = reader.readLine();
                if (loreLine == null) {
                    throw new IOException("unexpected end of file in maplore..endmaplore field");
                }

                if (loreLine.equals("endmaplore")) {
                    break;
                }

                if (mapArchObject.getLore().length() > 0) {
                    mapArchObject.addLore("\n");
                }
                mapArchObject.addLore(loreLine);
            }

            return true;
        }

        if (line.startsWith("background_music ")) {
            mapArchObject.setBackgroundMusic(line.substring(17));
            return true;
        }

        return false;
    }

}
