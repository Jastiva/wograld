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

package net.sf.gridarta.var.atrinik.model.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Formatter;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.io.AbstractMapArchObjectParser;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.io.MapArchObjectParser} for Atrinik map arch
 * object instances.
 * @author Andreas Kirschbaum
 */
public class MapArchObjectParser extends AbstractMapArchObjectParser<MapArchObject> {

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
        format.format("msg\n%s%sendmsg\n", mapArchObject.getText().trim(), "\n");
        format.format("width %d\n", mapArchObject.getMapSize().getWidth());
        format.format("height %d\n", mapArchObject.getMapSize().getHeight());
        if (mapArchObject.getEnterX() != 0) {
            format.format("enter_x %d\n", mapArchObject.getEnterX());
        }
        if (mapArchObject.getEnterY() != 0) {
            format.format("enter_y %d\n", mapArchObject.getEnterY());
        }
        if (mapArchObject.getResetTimeout() != 0) {
            format.format("reset_timeout %d\n", mapArchObject.getResetTimeout());
        }
        if (mapArchObject.getSwapTime() != 0) {
            format.format("swap_time %d\n", mapArchObject.getSwapTime());
        }
        if (mapArchObject.getDifficulty() != 0) {
            format.format("difficulty %d\n", mapArchObject.getDifficulty());
        }
        if (mapArchObject.getDarkness() != 0) {
            format.format("darkness %d\n", mapArchObject.getDarkness());
        }
        if (mapArchObject.isFixedReset()) {
            appendable.append("fixed_resettime 1\n");
        }
        if (mapArchObject.isOutdoor()) {
            appendable.append("outdoor 1\n");
        }
        if (mapArchObject.isNoSave()) {
            appendable.append("no_save 1\n");
        }
        if (mapArchObject.isNoMagic()) {
            appendable.append("no_magic 1\n");
        }
        if (mapArchObject.isNoPriest()) {
            appendable.append("no_priest 1\n");
        }
        if (mapArchObject.isNoSummon()) {
            appendable.append("no_summon 1\n");
        }
        if (mapArchObject.isNoHarm()) {
            appendable.append("no_harm 1\n");
        }
        if (mapArchObject.isFixedLogin()) {
            appendable.append("fixed_login 1\n");
        }
        if (mapArchObject.isUnique()) {
            appendable.append("unique 1\n");
        }
        if (mapArchObject.isFixedResetTime()) {
            appendable.append("fixed_resettime 1\n");
        }
        if (mapArchObject.isPlayerNoSave()) {
            appendable.append("player_no_save 1\n");
        }
        if (mapArchObject.isPvp()) {
            appendable.append("pvp 1\n");
        }
        for (final Direction direction : Direction.values()) {
            if (mapArchObject.getTilePath(direction).length() > 0) {
                format.format("tile_path_%d %s\n", direction.ordinal() + 1, mapArchObject.getTilePath(direction));
            }
        }
        if (mapArchObject.getTilesetId() != 0) {
            format.format("tileset_id %d\n", mapArchObject.getTilesetId());
            format.format("tileset_x %d\n", mapArchObject.getTilesetX());
            format.format("tileset_y %d\n", mapArchObject.getTilesetY());
        }
        final String backgroundMusic = mapArchObject.getBackgroundMusic();
        if (backgroundMusic.length() > 0) {
            format.format("bg_music %s\n", backgroundMusic);
        }
        final String region = mapArchObject.getRegion();
        if (region.length() > 0) {
            format.format("region %s\n", region);
        }
        final String weather = mapArchObject.getWeather();
        if (weather.length() > 0) {
            format.format("weather %s\n", weather);
        }
        appendable.append("end\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean parseLine(@NotNull final String line, @NotNull final MapArchObject mapArchObject, @NotNull final BufferedReader reader) {
        if (line.startsWith("no_save ")) {
            mapArchObject.setNoSave(NumberUtils.parseInt(line.substring(8)) != 0);
            return true;
        }

        if (line.startsWith("no_magic ")) {
            mapArchObject.setNoMagic(NumberUtils.parseInt(line.substring(9)) != 0);
            return true;
        }

        if (line.startsWith("no_priest ")) {
            mapArchObject.setNoPriest(NumberUtils.parseInt(line.substring(10)) != 0);
            return true;
        }

        if (line.startsWith("no_summon ")) {
            mapArchObject.setNoSummon(NumberUtils.parseInt(line.substring(10)) != 0);
            return true;
        }

        if (line.startsWith("no_harm ")) {
            mapArchObject.setNoHarm(NumberUtils.parseInt(line.substring(8)) != 0);
            return true;
        }

        if (line.startsWith("fixed_login ")) {
            mapArchObject.setFixedLogin(NumberUtils.parseInt(line.substring(12)) != 0);
            return true;
        }

        if (line.startsWith("unique ")) {
            mapArchObject.setUnique(NumberUtils.parseInt(line.substring(7)) != 0);
            return true;
        }

        if (line.startsWith("fixed_resettime ")) {
            mapArchObject.setFixedResetTime(NumberUtils.parseInt(line.substring(16)) != 0);
            return true;
        }

        if (line.startsWith("player_no_save ")) {
            mapArchObject.setPlayerNoSave(NumberUtils.parseInt(line.substring(15)) != 0);
            return true;
        }

        if (line.startsWith("pvp ")) {
            mapArchObject.setPvp(NumberUtils.parseInt(line.substring(4)) != 0);
            return true;
        }

        if (line.startsWith("tileset_id ")) {
            mapArchObject.setTilesetId(NumberUtils.parseInt(line.substring(11)));
            return true;
        }

        if (line.startsWith("tileset_x ")) {
            mapArchObject.setTilesetX(NumberUtils.parseInt(line.substring(10)));
            return true;
        }

        if (line.startsWith("tileset_y ")) {
            mapArchObject.setTilesetY(NumberUtils.parseInt(line.substring(10)));
            return true;
        }

        if (line.startsWith("bg_music ")) {
            mapArchObject.setBackgroundMusic(line.substring(9));
            return true;
        }

        if (line.startsWith("region ")) {
            mapArchObject.setRegion(line.substring(7));
            return true;
        }

        if (line.startsWith("weather ")) {
            mapArchObject.setWeather(line.substring(8));
            return true;
        }

        return false;
    }

}
