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

package net.sf.gridarta.model.validation;

import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.io.DefaultMapWriter;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.MapArchObjectParserFactory;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.io.TestMapArchObjectParserFactory;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.NamedGameObjectMatcher;
import net.sf.gridarta.model.match.TypeNrsGameObjectMatcher;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.settings.TestGlobalSettings;
import net.sf.gridarta.model.validation.checks.ValidatorFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for helper functions needed be regression tests.
 * @author Andreas Kirschbaum
 */
public class ValidationUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ValidationUtils() {
    }

    /**
     * Creates a new {@link ValidatorFactory} instance.
     * @return the new instance
     */
    @NotNull
    public static ValidatorFactory<TestGameObject, TestMapArchObject, TestArchetype> newValidatorFactory() {
        final ValidatorPreferences validatorPreferences = new TestValidatorPreferences();
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final GameObjectMatchers gameObjectMatchers = mapModelCreator.getGameObjectMatchers();
        gameObjectMatchers.addGameObjectMatcher(new NamedGameObjectMatcher(0, "matcher", "name", false, null, new TypeNrsGameObjectMatcher(1)));
        final GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> gameObjectParser = mapModelCreator.newGameObjectParser();
        final MapArchObjectParserFactory<TestMapArchObject> mapArchObjectParserFactory = new TestMapArchObjectParserFactory();
        final MapWriter<TestGameObject, TestMapArchObject, TestArchetype> mapWriter = new DefaultMapWriter<TestGameObject, TestMapArchObject, TestArchetype>(mapArchObjectParserFactory, gameObjectParser);
        final GlobalSettings globalSettings = new TestGlobalSettings();
        return new ValidatorFactory<TestGameObject, TestMapArchObject, TestArchetype>(validatorPreferences, gameObjectMatchers, globalSettings, mapWriter);
    }

}
