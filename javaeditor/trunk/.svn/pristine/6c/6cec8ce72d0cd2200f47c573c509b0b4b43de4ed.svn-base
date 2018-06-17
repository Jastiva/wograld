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

package net.sf.gridarta.var.atrinik.model.maparchobject;

import net.sf.gridarta.model.maparchobject.AbstractMapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MapArchObject contains the specific meta data about a map that is stored in
 * the map-arch, at the very beginning of the map file. The map meta data is
 * information like mapSize, difficulty level, darkness etc.).
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 * @note Though this class is named MapArchObject, it is <strong>not a subclass
 * of GameObject</strong>; the name is for technical reasons, not for semantic /
 * inheritance!
 * @todo This class should be changed so map attributes are reflected in a more
 * generic way like arch attributes.
 */
public class MapArchObject extends AbstractMapArchObject<MapArchObject> {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * No save map.
     * @serial
     */
    private boolean noSave;

    /**
     * No magic spells.
     * @serial
     */
    private boolean noMagic;

    /**
     * No prayers.
     * @serial
     */
    private boolean noPriest;

    /**
     * No harmful spells allowed.
     * @serial
     */
    private boolean noHarm;

    /**
     * No summoning allowed.
     * @serial
     */
    private boolean noSummon;

    /**
     * Check map reset status after re-login.
     * @serial
     */
    private boolean fixedLogin;

    /**
     * Unique map.
     * @serial
     */
    private boolean unique;

    /**
     * Fixed reset time.
     * @serial
     */
    private boolean fixedResetTime;

    /**
     * Players cannot save on this map.
     * @serial
     */
    private boolean playerNoSave;

    /**
     * Player vs Player combat allowed.
     * @serial
     */
    private boolean pvp;

    /**
     * The tileset id. 0 means no available tileset id
     * @serial
     */
    private int tilesetId;

    /**
     * The tileset x coordinate.
     * @serial
     */
    private int tilesetX;

    /**
     * The tileset y coordinate.
     * @serial
     */
    private int tilesetY;

    /**
     * The name of the background music. Set to empty string if unset.
     * @serial
     */
    @NotNull
    private String backgroundMusic = "";

    /**
     * The region the map is in.
     * @serial
     */
    @NotNull
    private String region = "";

    /**
     * The weather effect active on this map.
     * @serial
     */
    @NotNull
    private String weather = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(@NotNull final MapArchObject mapArchObject) {
        super.setState(mapArchObject);
        setNoSave(mapArchObject.noSave);
        setNoMagic(mapArchObject.noMagic);
        setNoPriest(mapArchObject.noPriest);
        setNoHarm(mapArchObject.noHarm);
        setNoSummon(mapArchObject.noSummon);
        setFixedLogin(mapArchObject.fixedLogin);
        setUnique(mapArchObject.unique);
        setFixedResetTime(mapArchObject.fixedResetTime);
        setPlayerNoSave(mapArchObject.playerNoSave);
        setPvp(mapArchObject.pvp);
        setTilesetId(mapArchObject.tilesetId);
        setTilesetX(mapArchObject.tilesetX);
        setTilesetY(mapArchObject.tilesetY);
        setBackgroundMusic(mapArchObject.backgroundMusic);
        setRegion(mapArchObject.region);
        setWeather(mapArchObject.weather);
    }

    /**
     * Returns the no save attribute.
     * @return the no save attribute
     */
    public boolean isNoSave() {
        return noSave;
    }

    /**
     * Sets the no save attribute.
     * @param noSave the no save attribute
     */
    public void setNoSave(final boolean noSave) {
        if (this.noSave == noSave) {
            return;
        }

        this.noSave = noSave;
        setModified();
    }

    /**
     * Returns the no magic attribute.
     * @return the no magic attribute
     */
    public boolean isNoMagic() {
        return noMagic;
    }

    /**
     * Sets the no magic attribute.
     * @param noMagic the no magic attribute
     */
    public void setNoMagic(final boolean noMagic) {
        if (this.noMagic == noMagic) {
            return;
        }

        this.noMagic = noMagic;
        setModified();
    }

    /**
     * Returns the no priest attribute.
     * @return the no priest attribute
     */
    public boolean isNoPriest() {
        return noPriest;
    }

    /**
     * Sets the no priest attribute.
     * @param noPriest the no priest attribute
     */
    public void setNoPriest(final boolean noPriest) {
        if (this.noPriest == noPriest) {
            return;
        }

        this.noPriest = noPriest;
        setModified();
    }

    /**
     * Returns the no summon attribute.
     * @return the no summon attribute
     */
    public boolean isNoSummon() {
        return noSummon;
    }

    /**
     * Sets the no summon attribute.
     * @param noSummon the no summon attribute
     */
    public void setNoSummon(final boolean noSummon) {
        if (this.noSummon == noSummon) {
            return;
        }

        this.noSummon = noSummon;
        setModified();
    }

    /**
     * Returns the no harm attribute.
     * @return the no harm attribute
     */
    public boolean isNoHarm() {
        return noHarm;
    }

    /**
     * Sets the no harm attribute.
     * @param noHarm the no harm attribute
     */
    public void setNoHarm(final boolean noHarm) {
        if (this.noHarm == noHarm) {
            return;
        }

        this.noHarm = noHarm;
        setModified();
    }

    /**
     * Returns the fixed login attribute.
     * @return the fixed login attribute
     */
    public boolean isFixedLogin() {
        return fixedLogin;
    }

    /**
     * Sets the fixed login attribute.
     * @param fixedLogin the fixed login attribute
     */
    public void setFixedLogin(final boolean fixedLogin) {
        if (this.fixedLogin == fixedLogin) {
            return;
        }

        this.fixedLogin = fixedLogin;
        setModified();
    }

    /**
     * Returns the unique attribute.
     * @return the unique attribute
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * Sets the unique save attribute.
     * @param unique the unique attribute
     */
    public void setUnique(final boolean unique) {
        if (this.unique == unique) {
            return;
        }

        this.unique = unique;
        setModified();
    }

    /**
     * Returns the fixed reset time attribute.
     * @return the fixed reset time attribute
     */
    public boolean isFixedResetTime() {
        return fixedResetTime;
    }

    /**
     * Sets the fixed reset time attribute.
     * @param fixedResetTime the fixed reset time attribute
     */
    public void setFixedResetTime(final boolean fixedResetTime) {
        if (this.fixedResetTime == fixedResetTime) {
            return;
        }

        this.fixedResetTime = fixedResetTime;
        setModified();
    }

    /**
     * Returns the player no save attribute.
     * @return the player no save attribute
     */
    public boolean isPlayerNoSave() {
        return playerNoSave;
    }

    /**
     * Sets the player no save attribute.
     * @param playerNoSave the player no save attribute
     */
    public void setPlayerNoSave(final boolean playerNoSave) {
        if (this.playerNoSave == playerNoSave) {
            return;
        }

        this.playerNoSave = playerNoSave;
        setModified();
    }

    /**
     * Returns the pvp attribute.
     * @return the pvp attribute
     */
    public boolean isPvp() {
        return pvp;
    }

    /**
     * Sets the pvp attribute.
     * @param pvp the pvp attribute
     */
    public void setPvp(final boolean pvp) {
        if (this.pvp == pvp) {
            return;
        }

        this.pvp = pvp;
        setModified();
    }

    /**
     * Returns the tileset id attribute.
     * @return the tileset id attribute
     */
    public int getTilesetId() {
        return tilesetId;
    }

    /**
     * Sets the tileset id attribute.
     * @param tilesetId the tileset id attribute
     */
    public void setTilesetId(final int tilesetId) {
        if (this.tilesetId == tilesetId) {
            return;
        }

        this.tilesetId = tilesetId;
        setModified();
    }

    /**
     * Returns the tileset x attribute.
     * @return the tileset x attribute
     */
    public int getTilesetX() {
        return tilesetX;
    }

    /**
     * Sets the tileset x attribute.
     * @param tilesetX the tileset x attribute
     */
    public void setTilesetX(final int tilesetX) {
        if (this.tilesetX == tilesetX) {
            return;
        }

        this.tilesetX = tilesetX;
        setModified();
    }

    /**
     * Returns the tileset y attribute.
     * @return the tileset y attribute
     */
    public int getTilesetY() {
        return tilesetY;
    }

    /**
     * Sets the tileset y attribute.
     * @param tilesetY the tileset y attribute
     */
    public void setTilesetY(final int tilesetY) {
        if (this.tilesetY == tilesetY) {
            return;
        }

        this.tilesetY = tilesetY;
        setModified();
    }

    /**
     * Returns the name of the background music.
     * @return the name or an empty string if unset
     */
    @NotNull
    public String getBackgroundMusic() {
        return backgroundMusic;
    }

    /**
     * Sets the name of the background music.
     * @param backgroundMusic the name or an empty string to unset
     */
    public void setBackgroundMusic(@NotNull final String backgroundMusic) {
        if (this.backgroundMusic.equals(backgroundMusic)) {
            return;
        }

        this.backgroundMusic = backgroundMusic;
        setModified();
    }

    /**
     * Returns the region attribute.
     * @return the region attribute
     */
    @NotNull
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region attribute.
     * @param region the region attribute
     */
    public void setRegion(@NotNull final String region) {
        final String trimmedRegion = region.trim();
        if (this.region.equals(trimmedRegion)) {
            return;
        }

        this.region = trimmedRegion;
        setModified();
    }

    /**
     * Returns the weather attribute.
     * @return the weather attribute
     */
    @NotNull
    public String getWeather() {
        return weather;
    }

    /**
     * Sets the weather attribute.
     * @param weather the weather attribute
     */
    public void setWeather(@NotNull final String weather) {
        final String trimmedWeather = weather.trim();
        if (this.weather.equals(trimmedWeather)) {
            return;
        }

        this.weather = trimmedWeather;
        setModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDifficulty(final int difficulty) {
        super.setDifficulty(difficulty < 1 ? 1 : difficulty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final MapArchObject mapArchObject = (MapArchObject) obj;
        return super.equals(obj) && mapArchObject.noSave == noSave && mapArchObject.noMagic == noMagic && mapArchObject.noPriest == noPriest && mapArchObject.noHarm == noHarm && mapArchObject.noSummon == noSummon && mapArchObject.fixedLogin == fixedLogin && mapArchObject.unique == unique && mapArchObject.fixedResetTime == fixedResetTime && mapArchObject.playerNoSave == playerNoSave && mapArchObject.pvp == pvp && mapArchObject.tilesetId == tilesetId && mapArchObject.tilesetX == tilesetX && mapArchObject.tilesetY == tilesetY && mapArchObject.backgroundMusic.equals(backgroundMusic) && mapArchObject.region.equals(region) && mapArchObject.weather.equals(weather);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode() + (noSave ? 2 : 0) + (noMagic ? 4 : 0) + (noPriest ? 8 : 0) + (noHarm ? 16 : 0) + (noSummon ? 32 : 0) + (fixedLogin ? 64 : 0) + (unique ? 128 : 0) + (fixedResetTime ? 256 : 0) + (playerNoSave ? 512 : 0) + (pvp ? 1024 : 0) + tilesetId + tilesetX + tilesetY + backgroundMusic.hashCode() + region.hashCode() + weather.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapArchObject clone() {
        return super.clone();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected MapArchObject getThis() {
        return this;
    }

}
