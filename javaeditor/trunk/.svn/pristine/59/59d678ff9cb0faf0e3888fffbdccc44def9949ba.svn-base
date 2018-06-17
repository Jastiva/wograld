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

package net.sf.gridarta.model.mapmodel;

import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.anim.TestAnimationObjects;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetype.DuplicateArchetypeException;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.archetype.TestArchetypeFactory;
import net.sf.gridarta.model.archetype.TestDefaultArchetype;
import net.sf.gridarta.model.archetype.UndefinedArchetypeException;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.DefaultArchetypeSet;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.EmptyFaceProvider;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.face.FaceProvider;
import net.sf.gridarta.model.face.TestFaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.gameobject.TestGameObjectFactory;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.TestGameObjectParser;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.mapviewsettings.TestMapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.match.TypeNrsGameObjectMatcher;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.utils.SystemIcons;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

/**
 * Helper class for regression tests to create {@link MapModel} instances.
 * @author Andreas Kirschbaum
 */
public class TestMapModelCreator {

    /**
     * The {@link ArchetypeSet} instance.
     */
    @NotNull
    private final ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> archetypeSet;

    /**
     * Predefined icon names.
     */
    @NotNull
    private static final String[] ICON_NAMES = { SystemIcons.SQUARE_SELECTED_SQUARE, SystemIcons.SQUARE_SELECTED_SQUARE_NORTH, SystemIcons.SQUARE_SELECTED_SQUARE_EAST, SystemIcons.SQUARE_SELECTED_SQUARE_SOUTH, SystemIcons.SQUARE_SELECTED_SQUARE_WEST, SystemIcons.SQUARE_PRE_SELECTED_SQUARE, SystemIcons.SQUARE_CURSOR, SystemIcons.SQUARE_EMPTY, SystemIcons.SQUARE_UNKNOWN, SystemIcons.SQUARE_NO_FACE, SystemIcons.SQUARE_NO_ARCH, SystemIcons.DEFAULT_ICON, SystemIcons.DEFAULT_PREVIEW, SystemIcons.SQUARE_WARNING, };

    /**
     * The {@link MapViewSettings} instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings = new TestMapViewSettings();

    /**
     * The {@link SystemIcons} instance.
     */
    @NotNull
    private final SystemIcons systemIcons;

    /**
     * The {@link FaceObjectProviders} instance.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link GameObjectMatchers} instance.
     */
    @NotNull
    private final GameObjectMatchers gameObjectMatchers = new GameObjectMatchers();

    /**
     * The {@link ArchetypeChooserModel} instance.
     */
    @NotNull
    private final ArchetypeChooserModel<TestGameObject, TestMapArchObject, TestArchetype> archetypeChooserModel = new ArchetypeChooserModel<TestGameObject, TestMapArchObject, TestArchetype>();

    /**
     * The {@link AutojoinLists} instance.
     */
    @NotNull
    private final AutojoinLists<TestGameObject, TestMapArchObject, TestArchetype> autojoinLists;

    /**
     * The {@link GameObjectFactory} instance.
     */
    @NotNull
    private final GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> gameObjectFactory;

    /**
     * The {@link AnimationObjects} instance.
     */
    @NotNull
    private final AnimationObjects animationObjects = new TestAnimationObjects();

    /**
     * The "topmost" {@link InsertionMode} instance.
     */
    @NotNull
    private final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> topmostInsertionMode;

    /**
     * The {@link InsertionModeSet} instance.
     */
    @NotNull
    private final InsertionModeSet<TestGameObject, TestMapArchObject, TestArchetype> insertionModeSet;

    /**
     * Creates a new instance.
     * @param createIcons whether to create icon instances
     */
    public TestMapModelCreator(final boolean createIcons) {
        autojoinLists = new AutojoinLists<TestGameObject, TestMapArchObject, TestArchetype>(mapViewSettings);
        final GUIUtils guiUtils = new GUIUtils();
        if (createIcons) {
            final ImageIcon imageIcon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
            for (final String iconName : ICON_NAMES) {
                guiUtils.addToCache(iconName, imageIcon);
            }
        }
        systemIcons = new SystemIcons(guiUtils);
        final FaceObjects faceObjects = new TestFaceObjects();
        faceObjectProviders = new FaceObjectProviders(0, faceObjects, systemIcons);
        final FaceProvider faceProvider = new EmptyFaceProvider();
        faceObjectProviders.setNormal(faceProvider);
        final ArchetypeFactory<TestGameObject, TestMapArchObject, TestArchetype> archetypeFactory = new TestArchetypeFactory(faceObjectProviders, animationObjects);
        archetypeSet = new DefaultArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype>(archetypeFactory, null);
        gameObjectFactory = new TestGameObjectFactory(faceObjectProviders, animationObjects);
        topmostInsertionMode = new TopmostInsertionMode<TestGameObject, TestMapArchObject, TestArchetype>();
        insertionModeSet = new InsertionModeSet<TestGameObject, TestMapArchObject, TestArchetype>(topmostInsertionMode, new TypeNrsGameObjectMatcher(), new TypeNrsGameObjectMatcher(), new TypeNrsGameObjectMatcher(), new TypeNrsGameObjectMatcher());
    }

    /**
     * Creates a new {@link MapModel} instance.
     * @param w the width in squares
     * @param h the height in squares
     * @return the new map model instance
     */
    @NotNull
    public MapModel<TestGameObject, TestMapArchObject, TestArchetype> newMapModel(final int w, final int h) {
        final TestMapArchObject mapArchObject = new TestMapArchObject();
        mapArchObject.setMapSize(new Size2D(w, h));
        return new DefaultMapModel<TestGameObject, TestMapArchObject, TestArchetype>(autojoinLists, mapArchObject, archetypeChooserModel, 0, gameObjectFactory, gameObjectMatchers, topmostInsertionMode);
    }

    /**
     * Creates a new game object.
     * @param archetypeName the name of the game object´s archetype
     * @param objectName the object name to set
     * @return the game object
     */
    @NotNull
    public TestGameObject newGameObject(@NotNull final String archetypeName, @NotNull final String objectName) {
        final TestArchetype archetype = getArchetype(archetypeName);
        final TestGameObject gameObject = new TestGameObject(archetype, faceObjectProviders, animationObjects);
        gameObject.setAttributeString(BaseObject.NAME, objectName);
        return gameObject;
    }

    /**
     * Inserts a game object into a map.
     * @param mapModel the map to add to
     * @param archetypeName the name of the game object´s archetype
     * @param name the name of the game object to add
     * @param x the x coordinate to add to
     * @param y the y coordinate to add to
     * @param insertionMode the insertion mode to use
     */
    public void addGameObjectToMap(@NotNull final MapModel<TestGameObject, TestMapArchObject, TestArchetype> mapModel, @NotNull final String archetypeName, @NotNull final String name, final int x, final int y, @NotNull final InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> insertionMode) {
        final TestGameObject gameObject = newGameObject(archetypeName, name);
        mapModel.addGameObjectToMap(gameObject, new Point(x, y), insertionMode);
    }

    /**
     * Inserts a game object into the inventory of another game object.
     * @param gameObject the game object to add to
     * @param archetypeName the name of the game object´s archetype
     * @param name the name of the game object to add
     */
    public void insertGameObject(@NotNull final GameObject<TestGameObject, TestMapArchObject, TestArchetype> gameObject, @NotNull final String archetypeName, @NotNull final String name) {
        final TestGameObject inv = newGameObject(archetypeName, name);
        gameObject.addLast(inv);
    }

    /**
     * Returns the {@link InsertionModeSet} instance.
     * @return the insertion mode set instance
     */
    @NotNull
    public InsertionModeSet<TestGameObject, TestMapArchObject, TestArchetype> getInsertionModeSet() {
        return insertionModeSet;
    }

    /**
     * Returns the "topmost" insertion mode.
     * @return the "topmost" insertion mode
     */
    @NotNull
    public InsertionMode<TestGameObject, TestMapArchObject, TestArchetype> getTopmostInsertionMode() {
        return topmostInsertionMode;
    }

    /**
     * Returns an archetype.
     * @param archetypeName the archetype name
     * @return the archetype
     */
    @NotNull
    public TestArchetype getArchetype(@NotNull final String archetypeName) {
        try {
            return archetypeSet.getArchetype(archetypeName);
        } catch (final UndefinedArchetypeException ignored) {
            // ignore
        }

        final TestArchetype archetype = newArchetype(archetypeName);
        try {
            archetypeSet.addArchetype(archetype);
        } catch (final DuplicateArchetypeException ex) {
            Assert.fail(ex.getMessage());
            throw new AssertionError(ex);
        }
        return archetype;
    }

    /**
     * Creates a new {@link TestArchetype} instance but doesn't add it to the
     * archetype set.
     * @param archetypeName the archetype´s name
     * @return the new instance
     */
    @NotNull
    public TestArchetype newArchetype(@NotNull final String archetypeName) {
        return new TestDefaultArchetype(archetypeName, faceObjectProviders, animationObjects);
    }

    /**
     * Returns the {@link GameObjectFactory} instance.
     * @return the game object factory instance
     */
    @NotNull
    public GameObjectFactory<TestGameObject, TestMapArchObject, TestArchetype> getGameObjectFactory() {
        return gameObjectFactory;
    }

    /**
     * Returns the {@link MapViewSettings} instance.
     * @return the map view settings instance
     */
    @NotNull
    public MapViewSettings getMapViewSettings() {
        return mapViewSettings;
    }

    /**
     * Creates a new {@link TestMapModelHelper} instance.
     * @return the new test map model creator instance
     * @throws DuplicateArchetypeException if an internal error occurs
     */
    @NotNull
    public TestMapModelHelper newTestMapModelHelper() throws DuplicateArchetypeException {
        return new TestMapModelHelper(topmostInsertionMode, gameObjectFactory, archetypeSet, faceObjectProviders, animationObjects);
    }

    /**
     * Returns the {@link AutojoinLists} instance.
     * @return the autojoin lists instance
     */
    @NotNull
    public AutojoinLists<TestGameObject, TestMapArchObject, TestArchetype> getAutojoinLists() {
        return autojoinLists;
    }

    /**
     * Returns the {@link ArchetypeChooserModel} instance.
     * @return the archetype chooser model instance
     */
    @NotNull
    public ArchetypeChooserModel<TestGameObject, TestMapArchObject, TestArchetype> getArchetypeChooserModel() {
        return archetypeChooserModel;
    }

    /**
     * Returns the {@link GameObjectMatchers} instance.
     * @return the game object matchers instance
     */
    @NotNull
    public GameObjectMatchers getGameObjectMatchers() {
        return gameObjectMatchers;
    }

    /**
     * Returns the {@link ArchetypeSet}.
     * @return the archetype set
     */
    @NotNull
    public ArchetypeSet<TestGameObject, TestMapArchObject, TestArchetype> getArchetypeSet() {
        return archetypeSet;
    }

    /**
     * Creates a new {@link GameObjectParser} instance.
     * @return the new game object parser instance
     */
    public GameObjectParser<TestGameObject, TestMapArchObject, TestArchetype> newGameObjectParser() {
        return new TestGameObjectParser(gameObjectFactory, archetypeSet);
    }

    /**
     * Returns the {@link FaceObjectProviders} instance.
     * @return the face object providers instance
     */
    @NotNull
    public FaceObjectProviders getFaceObjectProviders() {
        return faceObjectProviders;
    }

    /**
     * Returns the {@link SystemIcons} instance.
     * @return the system icons instance
     */
    @NotNull
    public SystemIcons getSystemIcons() {
        return systemIcons;
    }

}
