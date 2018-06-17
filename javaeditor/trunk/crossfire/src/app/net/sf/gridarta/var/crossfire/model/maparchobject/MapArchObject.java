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

package net.sf.gridarta.var.crossfire.model.maparchobject;

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
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map lore attribute.
     * @serial
     */
    private StringBuilder loreText = new StringBuilder();

    /**
     * If set, this entire map is unique.
     * @serial
     */
    private boolean unique;

    /**
     * If set, this entire map is a template map.
     * @serial
     */
    private boolean template;

    /**
     * If set, this entire map is no smooth.
     * @serial
     */
    private boolean noSmooth;

    /**
     * Weather variable: temperature.
     * @serial
     */
    private int temperature;

    /**
     * Weather variable: pressure.
     * @serial
     */
    private int pressure;

    /**
     * Weather variable: humidity (water in the air).
     * @serial
     */
    private int humidity;

    /**
     * Weather variable: wind speed.
     * @serial
     */
    private int windSpeed;

    /**
     * Weather variable: wind direction.
     * @serial
     */
    private int windDirection;

    /**
     * Weather variable: sky settings.
     * @serial
     */
    private int sky;

    /**
     * The item spec for the shop, if there is one.
     * @serial
     */
    @NotNull
    private String shopItems = "";

    /**
     * The preferred race of the shop.
     * @serial
     */
    @NotNull
    private String shopRace = "";

    /**
     * The greed of the shop.
     * @serial
     */
    private double shopGreed;

    /**
     * The minimum price the shop will trade for.
     * @serial
     */
    private int shopMin;

    /**
     * The maximum price the shop will trade for.
     * @serial
     */
    private int shopMax;

    /**
     * The region the map is in.
     * @serial
     */
    @NotNull
    private String region = "";

    /**
     * The background music to play.
     * @serial
     */
    @NotNull
    private String backgroundMusic = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(@NotNull final MapArchObject mapArchObject) {
        super.setState(mapArchObject);
        setLore(mapArchObject.loreText.toString());
        setUnique(mapArchObject.unique);
        setTemplate(mapArchObject.template);
        setNoSmooth(mapArchObject.noSmooth);
        setTemperature(mapArchObject.temperature);
        setPressure(mapArchObject.pressure);
        setHumidity(mapArchObject.humidity);
        setWindSpeed(mapArchObject.windSpeed);
        setWindDirection(mapArchObject.windDirection);
        setSky(mapArchObject.sky);
        setShopItems(mapArchObject.shopItems);
        setShopRace(mapArchObject.shopRace);
        setShopGreed(mapArchObject.shopGreed);
        setShopMin(mapArchObject.shopMin);
        setShopMax(mapArchObject.shopMax);
        setRegion(mapArchObject.region);
        setBackgroundMusic(mapArchObject.backgroundMusic);
    }

    /**
     * Get whether this map is unique.
     * @return whether this map is unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * Set whether this map is unique.
     * @param unique whether this map should be unique
     */
    public void setUnique(final boolean unique) {
        if (this.unique == unique) {
            return;
        }

        this.unique = unique;
        setModified();
    }

    /**
     * Returns whether this map is a template map.
     * @return whether this map is a template map
     */
    public boolean isTemplate() {
        return template;
    }

    /**
     * Sets whether this map is a template map.
     * @param template whether this map is a template map
     */
    public void setTemplate(final boolean template) {
        if (this.template == template) {
            return;
        }

        this.template = template;
        setModified();
    }

    /**
     * Returns whether this map is no smooth.
     * @return whether this map is no smooth
     */
    public boolean isNoSmooth() {
        return noSmooth;
    }

    /**
     * Sets whether this map is no smooth.
     * @param noSmooth whether this map is no smooth
     */
    public void setNoSmooth(final boolean noSmooth) {
        if (this.noSmooth == noSmooth) {
            return;
        }

        this.noSmooth = noSmooth;
        setModified();
    }

    /**
     * Returns the region the map is in.
     * @return the region the map is in
     */
    @NotNull
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region the map is in.
     * @param region the region the map is in
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
     * Returns the background music to play.
     * @return the background music to play
     */
    @NotNull
    public String getBackgroundMusic() {
        return backgroundMusic;
    }

    /**
     * Sets the background music to play.
     * @param backgroundMusic the background music to play
     */
    public void setBackgroundMusic(@NotNull final String backgroundMusic) {
        final String trimmedBackgroundMusic = backgroundMusic.trim();
        if (this.backgroundMusic.equals(trimmedBackgroundMusic)) {
            return;
        }

        this.backgroundMusic = trimmedBackgroundMusic;
        setModified();
    }

    /**
     * Returns the item spec for the shop.
     * @return the item spec for the shop
     */
    @NotNull
    public String getShopItems() {
        return shopItems;
    }

    /**
     * Sets the item spec for the shop.
     * @param shopItems the item spec for the shop
     */
    public void setShopItems(@NotNull final String shopItems) {
        final String trimmedShopItems = shopItems.trim();
        if (this.shopItems.equals(trimmedShopItems)) {
            return;
        }

        this.shopItems = trimmedShopItems;
        setModified();
    }

    /**
     * Returns the preferred race of the shop.
     * @return the preferred race of the shop
     */
    @NotNull
    public String getShopRace() {
        return shopRace;
    }

    /**
     * Sets the preferred race of the shop.
     * @param shopRace the preferred race of te shop
     */
    public void setShopRace(@NotNull final String shopRace) {
        final String trimmedShopRace = shopRace.trim();
        if (this.shopRace.equals(trimmedShopRace)) {
            return;
        }

        this.shopRace = shopRace;
        setModified();
    }

    /**
     * Returns the minimum price the shop will trade for.
     * @return the minimum price the shop will trade for
     */
    public int getShopMin() {
        return shopMin;
    }

    /**
     * Sets the minimum price the shop will trade for.
     * @param shopMin the minimum price the shop will trade for
     */
    public void setShopMin(final int shopMin) {
        if (this.shopMin == shopMin) {
            return;
        }

        this.shopMin = shopMin;
        setModified();
    }

    /**
     * Returns the maximum price the shop will trade for.
     * @return the maximum price the shop will trade for
     */
    public int getShopMax() {
        return shopMax;
    }

    /**
     * Sets the maximum price the shop will trade for.
     * @param shopMax the maximum price the shop will trade for
     */
    public void setShopMax(final int shopMax) {
        if (this.shopMax == shopMax) {
            return;
        }

        this.shopMax = shopMax;
        setModified();
    }

    /**
     * Returns the greed of the shop.
     * @return the greed of the shop
     */
    public double getShopGreed() {
        return shopGreed;
    }

    /**
     * Sets the greed of the shop.
     * @param shopGreed the greed of the shop
     */
    public void setShopGreed(final double shopGreed) {
        // FloatingPointEquality is okay here - no delta needed.
        //noinspection FloatingPointEquality
        if (this.shopGreed == shopGreed) {
            return;
        }

        this.shopGreed = shopGreed;
        setModified();
    }

    /**
     * Returns the temperature.
     * @return the temperature
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature.
     * @param temperature the temperature
     */
    public void setTemperature(final int temperature) {
        if (this.temperature == temperature) {
            return;
        }

        this.temperature = temperature;
        setModified();
    }

    /**
     * Returns the pressure.
     * @return the pressure
     */
    public int getPressure() {
        return pressure;
    }

    /**
     * Sets the pressure.
     * @param pressure the pressure
     */
    public void setPressure(final int pressure) {
        if (this.pressure == pressure) {
            return;
        }

        this.pressure = pressure;
        setModified();
    }

    /**
     * Returns the humidity.
     * @return the humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Sets the humidity.
     * @param humidity the humidity
     */
    public void setHumidity(final int humidity) {
        if (this.humidity == humidity) {
            return;
        }

        this.humidity = humidity;
        setModified();
    }

    /**
     * Returns the wind speed.
     * @return the wind speed
     */
    public int getWindSpeed() {
        return windSpeed;
    }

    /**
     * Sets the wind speed.
     * @param windSpeed the wind speed
     */
    public void setWindSpeed(final int windSpeed) {
        if (this.windSpeed == windSpeed) {
            return;
        }

        this.windSpeed = windSpeed;
        setModified();
    }

    /**
     * Returns the wind direction.
     * @return the wind direction
     */
    public int getWindDirection() {
        return windDirection;
    }

    /**
     * sets the wind direction.
     * @param windDirection the wind direction
     */
    public void setWindDirection(final int windDirection) {
        if (this.windDirection == windDirection) {
            return;
        }

        this.windDirection = windDirection;
        setModified();
    }

    /**
     * Returns the sky settings.
     * @return the sky settings
     */
    public int getSky() {
        return sky;
    }

    /**
     * Sets the sky settings.
     * @param sky the sky settings
     */
    public void setSky(final int sky) {
        if (this.sky == sky) {
            return;
        }

        this.sky = sky;
        setModified();
    }

    /**
     * Appends text to the map lore.
     * @param text the text to append
     */
    public void addLore(@NotNull final String text) {
        if (text.length() == 0) {
            return;
        }

        loreText.append(text);
        setModified();
    }

    /**
     * Sets the map lore.
     * @param text the map lore
     */
    public void setLore(@NotNull final String text) {
        if (loreText.toString().equals(text)) {
            return;
        }

        loreText.delete(0, loreText.length());
        loreText.append(text);
        setModified();
    }

    /**
     * Returns the map lore.
     * @return the map lore
     */
    @NotNull
    public String getLore() {
        return loreText.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDifficulty(final int difficulty) {
        super.setDifficulty(difficulty < 0 ? 0 : difficulty);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("FloatingPointEquality")
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final MapArchObject mapArchObject = (MapArchObject) obj;
        return super.equals(obj) && mapArchObject.loreText.toString().equals(loreText.toString()) && mapArchObject.unique == unique && mapArchObject.template == template && mapArchObject.noSmooth == noSmooth && mapArchObject.temperature == temperature && mapArchObject.pressure == pressure && mapArchObject.humidity == humidity && mapArchObject.windSpeed == windSpeed && mapArchObject.windDirection == windDirection && mapArchObject.sky == sky && mapArchObject.shopItems.equals(shopItems) && mapArchObject.shopRace.equals(shopRace) && mapArchObject.shopGreed == shopGreed && mapArchObject.shopMin == shopMin && mapArchObject.shopMax == shopMax && mapArchObject.region.equals(region) && mapArchObject.backgroundMusic.equals(backgroundMusic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode() + loreText.hashCode() + (unique ? 1 : 0) + (template ? 2 : 0) + (noSmooth ? 8 : 0) + temperature + pressure + humidity + windSpeed + windDirection + sky + shopItems.hashCode() + shopRace.hashCode() + (int) shopGreed + shopMin + shopMax + region.hashCode() + backgroundMusic.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapArchObject clone() {
        final MapArchObject clone = super.clone();
        //noinspection CloneCallsConstructors
        clone.loreText = new StringBuilder(loreText.toString());
        return clone;
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
