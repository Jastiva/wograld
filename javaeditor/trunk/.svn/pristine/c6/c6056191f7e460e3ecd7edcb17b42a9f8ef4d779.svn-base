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

package net.sf.gridarta.var.atrinik.maincontrol;

import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import net.sf.gridarta.gui.dialog.mapproperties.MapPropertiesDialogFactory;
import net.sf.gridarta.gui.dialog.newmap.NewMapDialogFactory;
import net.sf.gridarta.gui.dialog.prefs.AppPreferences;
import net.sf.gridarta.gui.dialog.prefs.AppPreferencesModel;
import net.sf.gridarta.gui.dialog.prefs.DevPreferences;
import net.sf.gridarta.gui.dialog.prefs.GUIPreferences;
import net.sf.gridarta.gui.dialog.prefs.MapValidatorPreferences;
import net.sf.gridarta.gui.dialog.prefs.MiscPreferences;
import net.sf.gridarta.gui.dialog.prefs.NetPreferences;
import net.sf.gridarta.gui.dialog.prefs.ResPreferences;
import net.sf.gridarta.gui.dialog.prefs.UpdatePreferences;
import net.sf.gridarta.gui.filter.FilterControl;
import net.sf.gridarta.gui.map.mapview.DefaultMapViewFactory;
import net.sf.gridarta.gui.map.mapview.MapViewFactory;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.map.renderer.GridMapSquarePainter;
import net.sf.gridarta.gui.map.renderer.RendererFactory;
import net.sf.gridarta.gui.scripts.ScriptArchDataUtils;
import net.sf.gridarta.gui.scripts.ScriptedEventEditor;
import net.sf.gridarta.maincontrol.DefaultMainControl;
import net.sf.gridarta.maincontrol.EditorFactory;
import net.sf.gridarta.maincontrol.GUIMainControl;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.ArchetypeFactory;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.archetypeset.DefaultArchetypeSet;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeList;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.autojoin.AutojoinLists;
import net.sf.gridarta.model.configsource.ConfigSourceFactory;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.face.ArchFaceProvider;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.IsoMapSquareInfo;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.DefaultMapReaderFactory;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.io.GameObjectParserFactory;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.io.SubDirectoryCacheFiles;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapcontrol.MapControlFactory;
import net.sf.gridarta.model.mapmanager.AbstractMapManager;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.resource.AbstractResources;
import net.sf.gridarta.model.scripts.DefaultScriptArchData;
import net.sf.gridarta.model.scripts.ScriptArchData;
import net.sf.gridarta.model.scripts.ScriptArchUtils;
import net.sf.gridarta.model.scripts.ScriptedEventFactory;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import net.sf.gridarta.model.validation.DelegatingMapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.checks.AttributeRangeChecker;
import net.sf.gridarta.model.validation.checks.InvalidCheckException;
import net.sf.gridarta.plugin.PluginExecutor;
import net.sf.gridarta.plugin.PluginModel;
import net.sf.gridarta.plugin.PluginParameters;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import net.sf.gridarta.utils.GUIUtils;
import net.sf.gridarta.utils.GuiFileFilters;
import net.sf.gridarta.utils.IOUtils;
import net.sf.gridarta.utils.SystemIcons;
import net.sf.gridarta.var.atrinik.IGUIConstants;
import net.sf.gridarta.var.atrinik.actions.AtrinikServerActions;
import net.sf.gridarta.var.atrinik.gui.map.renderer.DefaultRendererFactory;
import net.sf.gridarta.var.atrinik.gui.mappropertiesdialog.DefaultMapPropertiesDialogFactory;
import net.sf.gridarta.var.atrinik.gui.scripts.DefaultScriptArchUtils;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.archetype.DefaultArchetypeFactory;
import net.sf.gridarta.var.atrinik.model.gameobject.DefaultGameObjectFactory;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.io.ArchetypeParser;
import net.sf.gridarta.var.atrinik.model.io.DefaultGameObjectParserFactory;
import net.sf.gridarta.var.atrinik.model.io.DefaultMapArchObjectParserFactory;
import net.sf.gridarta.var.atrinik.model.maparchobject.DefaultMapArchObjectFactory;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import net.sf.gridarta.var.atrinik.model.mapcontrol.DefaultMapControlFactory;
import net.sf.gridarta.var.atrinik.model.scripts.DefaultScriptedEventFactory;
import net.sf.gridarta.var.atrinik.model.settings.DefaultGlobalSettings;
import net.sf.gridarta.var.atrinik.resource.DefaultResources;
import net.sf.japi.swing.prefs.PreferencesGroup;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link EditorFactory} that creates Crossfire objects.
 * @author Andreas Kirschbaum
 */
public class DefaultEditorFactory implements EditorFactory<GameObject, MapArchObject, Archetype> {

    /**
     * Preferences default for auto validation.
     */
    private static final boolean PREFERENCES_VALIDATOR_AUTO_DEFAULT = false;

    /**
     * The horizontal size of a map square.
     */
    private static final int SQUARE_ISO_X_LEN = 48;

    /**
     * The horizontal center of a map square.
     */
    private static final int SQUARE_ISO_X_LEN2 = 24;

    /**
     * The vertical size of a map square.
     */
    private static final int SQUARE_ISO_Y_LEN = 23;

    /**
     * The vertical center of a map square.
     */
    private static final int SQUARE_ISO_Y_LEN2 = 12;

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DefaultEditorFactory.class);

    /**
     * The {@link IsoMapSquareInfo} instance.
     */
    @NotNull
    private final IsoMapSquareInfo isoMapSquareInfo = new IsoMapSquareInfo(SQUARE_ISO_X_LEN, SQUARE_ISO_X_LEN2, SQUARE_ISO_Y_LEN, SQUARE_ISO_Y_LEN2);

    /**
     * The {@link MultiPositionData} to query for multi-part objects.
     */
    @NotNull
    private final MultiPositionData multiPositionData = new MultiPositionData(isoMapSquareInfo);

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public DefaultMainControl<GameObject, MapArchObject, Archetype> newMainControl(final boolean forceReadFromFiles, @NotNull final ErrorView errorView, @NotNull final GlobalSettings globalSettings, @NotNull final ConfigSourceFactory configSourceFactory, @NotNull final PathManager pathManager, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final ArchetypeChooserModel<GameObject, MapArchObject, Archetype> archetypeChooserModel, @NotNull final AutojoinLists<GameObject, MapArchObject, Archetype> autojoinLists, @NotNull final AbstractMapManager<GameObject, MapArchObject, Archetype> mapManager, @NotNull final PluginModel<GameObject, MapArchObject, Archetype> pluginModel, @NotNull final DelegatingMapValidator<GameObject, MapArchObject, Archetype> validators, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor, @NotNull final AbstractResources<GameObject, MapArchObject, Archetype> resources, @NotNull final Spells<NumberSpell> numberSpells, @NotNull final Spells<GameObjectSpell<GameObject, MapArchObject, Archetype>> gameObjectSpells, @NotNull final PluginParameterFactory<GameObject, MapArchObject, Archetype> pluginParameterFactory, @NotNull final ValidatorPreferences validatorPreferences, @NotNull final MapWriter<GameObject, MapArchObject, Archetype> mapWriter) {
        return new DefaultMainControl<GameObject, MapArchObject, Archetype>(GuiFileFilters.pythonFileFilter, ".py", "Python", 0, IGUIConstants.SPELL_FILE, IGUIConstants.SCRIPTS_DIR, errorView, this, forceReadFromFiles, globalSettings, configSourceFactory, pathManager, gameObjectMatchers, gameObjectFactory, archetypeTypeSet, archetypeSet, archetypeChooserModel, autojoinLists, mapManager, pluginModel, validators, scriptedEventEditor, resources, numberSpells, gameObjectSpells, pluginParameterFactory, validatorPreferences, mapWriter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDoubleFaceOffset() {
        return isoMapSquareInfo.getYLen() - 1;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapArchObjectFactory<MapArchObject> newMapArchObjectFactory(@NotNull final GlobalSettings globalSettings) {
        return new DefaultMapArchObjectFactory(globalSettings);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public DefaultMapArchObjectParserFactory newMapArchObjectParserFactory() {
        return new DefaultMapArchObjectParserFactory();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObjectFactory<GameObject, MapArchObject, Archetype> newGameObjectFactory(@NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        return new DefaultGameObjectFactory(faceObjectProviders, animationObjects, archetypeTypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GameObjectParserFactory<GameObject, MapArchObject, Archetype> newGameObjectParserFactory(@NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        return new DefaultGameObjectParserFactory(gameObjectFactory, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GlobalSettings newGlobalSettings() {
        return new DefaultGlobalSettings();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeFactory<GameObject, MapArchObject, Archetype> newArchetypeFactory(@NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        return new DefaultArchetypeFactory(faceObjectProviders, animationObjects);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeSet<GameObject, MapArchObject, Archetype> newArchetypeSet(@NotNull final GlobalSettings globalSettings, @NotNull final ArchetypeFactory<GameObject, MapArchObject, Archetype> archetypeFactory) {
        return new DefaultArchetypeSet<GameObject, MapArchObject, Archetype>(archetypeFactory, null);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapControlFactory<GameObject, MapArchObject, Archetype> newMapControlFactory(@NotNull final MapWriter<GameObject, MapArchObject, Archetype> mapWriter, @NotNull final GlobalSettings globalSettings, @NotNull final MapModelFactory<GameObject, MapArchObject, Archetype> mapModelFactory) {
        return new DefaultMapControlFactory(mapWriter, globalSettings, mapModelFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getIncludeFaceNumbers() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initSmoothFaces(@NotNull final FaceObjects faceObjects) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initMapValidators(@NotNull final DelegatingMapValidator<GameObject, MapArchObject, Archetype> mapValidators, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final GlobalSettings globalSettings, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final AttributeRangeChecker<GameObject, MapArchObject, Archetype> attributeRangeChecker, @NotNull final ValidatorPreferences validatorPreferences) {
        final GameObjectMatcher monsterMatcher = gameObjectMatchers.getMatcherWarn(errorViewCollector, "system_monster_only", "system_monster", "monster");
        if (monsterMatcher != null) {
            addAttributeRangeChecker(attributeRangeChecker, monsterMatcher, "level", "level", 0, 125);
            addAttributeRangeChecker(attributeRangeChecker, monsterMatcher, "dam", "damage", 1, 500);
        }
        final GameObjectMatcher combatValuesObjectsMatcher = gameObjectMatchers.getMatcherWarn(errorViewCollector, "system_combat_values_objects");
        if (combatValuesObjectsMatcher != null) {
            // XXX: these checks should be defined in types.xml
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_impact", "impact (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_slash", "slash (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_cleave", "cleave (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_pierce", "pierce (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_cold", "cold (elemental)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_fire", "fire (elemental)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_electricity", "electricity (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_acid", "acid (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_poison", "poison (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_sonic", "sonic (physical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_magic", "magic (magical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_light", "light (magical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_shadow", "shadow (magical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_psionic", "psionic (magical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_lifesteal", "lifesteal (magical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_death", "death (spherical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_chaos", "chaos (spherical)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_nether", "nether (nether)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_aether", "aether (aether)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_weaponmagic", "weaponmagic", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_godpower", "godpower", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_drain", "drain (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_depletion", "depletion (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_corruption", "corruption (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_confusion", "confusion (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_fear", "fear (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_slow", "slow (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_paralyze", "paralyze (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "attack_snare", "snare (effect)", 0, Integer.MAX_VALUE);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_impact", "impact (physical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_slash", "slash (physical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_cleave", "cleave (physical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_pierce", "pierce (physical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_cold", "cold (elemental)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_fire", "fire (elemental)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_electricity", "electricity (elemental)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_acid", "acid (elemental)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_poison", "poison (elemental)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_sonic", "sonic (elemental)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_magic", "magic (magical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_light", "light (magical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_shadow", "shadow (magical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_psionic", "psionic (magical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_lifesteal", "lifesteal (magical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_death", "death (spherical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_chaos", "chaos (spherical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_nether", "nether (spherical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_aether", "aether (spherical)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_weaponmagic", "weaponmagic", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_godpower", "godpower", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_drain", "drain (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_depletion", "depletion (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_corruption", "corruption (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_confusion", "confusion (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_fear", "fear (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_slow", "slow (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_paralyze", "paralyze (effect)", Integer.MIN_VALUE, 100);
            addAttributeRangeChecker(attributeRangeChecker, combatValuesObjectsMatcher, "resist_snare", "snare (effect)", Integer.MIN_VALUE, 100);
        }
    }

    private static void addAttributeRangeChecker(@NotNull final AttributeRangeChecker<GameObject, MapArchObject, Archetype> attributeRangeChecker, @NotNull final GameObjectMatcher matcher, @NotNull final String name, @NotNull final String displayName, final int minValue, final int maxValue) {
        try {
            attributeRangeChecker.add(matcher, name, displayName, minValue, maxValue);
        } catch (final InvalidCheckException ex) {
            log.warn(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArchetypeParser newArchetypeParser(@NotNull final ErrorView errorView, final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, final AnimationObjects animationObjects, final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final GlobalSettings globalSettings) {
        try {
            final URL url = IOUtils.getResource(globalSettings.getConfigurationDirectory(), IGUIConstants.ARCHDEF_FILE);
            multiPositionData.load(errorView, url);
        } catch (final IOException ex) {
            errorView.addWarning(ErrorViewCategory.ARCHDEF_FILE_INVALID, IGUIConstants.ARCHDEF_FILE + ": " + ex.getMessage());
        }
        return new ArchetypeParser(gameObjectParser, animationObjects, archetypeSet, gameObjectFactory, multiPositionData);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public DefaultScriptArchUtils newScriptArchUtils(@NotNull final ArchetypeTypeList eventTypes) {
        return new DefaultScriptArchUtils(eventTypes, "sub_type", Archetype.TYPE_EVENT_OBJECT);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ScriptedEventFactory<GameObject, MapArchObject, Archetype> newScriptedEventFactory(@NotNull final ScriptArchUtils scriptArchUtils, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        return new DefaultScriptedEventFactory(scriptArchUtils, "sub_type", gameObjectFactory, scriptedEventEditor, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ScriptArchData<GameObject, MapArchObject, Archetype> newScriptArchData() {
        return new DefaultScriptArchData<GameObject, MapArchObject, Archetype>("sub_type", Archetype.TYPE_EVENT_OBJECT);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ScriptArchDataUtils<GameObject, MapArchObject, Archetype> newScriptArchDataUtils(@NotNull final ScriptArchUtils scriptArchUtils, @NotNull final ScriptedEventFactory<GameObject, MapArchObject, Archetype> scriptedEventFactory, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor) {
        return new ScriptArchDataUtils<GameObject, MapArchObject, Archetype>(Archetype.TYPE_EVENT_OBJECT, scriptArchUtils, scriptedEventFactory, scriptedEventEditor);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public RendererFactory<GameObject, MapArchObject, Archetype> newRendererFactory(@NotNull final MapViewSettings mapViewSettings, @NotNull final FilterControl<GameObject, MapArchObject, Archetype> filterControl, @NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final SystemIcons systemIcons) {
        return new DefaultRendererFactory(mapViewSettings, filterControl, multiPositionData, isoMapSquareInfo, new GridMapSquarePainter(systemIcons), gameObjectParser, systemIcons);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapViewFactory<GameObject, MapArchObject, Archetype> newMapViewFactory(@NotNull final RendererFactory<GameObject, MapArchObject, Archetype> rendererFactory, @NotNull final PathManager pathManager) {
        return new DefaultMapViewFactory<GameObject, MapArchObject, Archetype>(rendererFactory, isoMapSquareInfo.getXLen(), isoMapSquareInfo.getYLen(), pathManager);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public AppPreferencesModel createAppPreferencesModel() {
        return new AppPreferencesModel("../server/atrinik_server", System.getProperty("os.name").toLowerCase().startsWith("win") ? "../client/atrinik.exe" : "../client-1.1.1/atrinik-client", "vim");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapPropertiesDialogFactory<GameObject, MapArchObject, Archetype> newMapPropertiesDialogFactory(@NotNull final GlobalSettings globalSettings, @NotNull final MapManager<GameObject, MapArchObject, Archetype> mapManager, @NotNull final MapPathNormalizer mapPathNormalizer) {
        return new DefaultMapPropertiesDialogFactory(mapManager, globalSettings, mapPathNormalizer);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public NewMapDialogFactory<GameObject, MapArchObject, Archetype> newNewMapDialogFactory(@NotNull final MapViewsManager<GameObject, MapArchObject, Archetype> mapViewsManager, @NotNull final MapArchObjectFactory<MapArchObject> mapArchObjectFactory, @NotNull final Component parent) {
        return new NewMapDialogFactory<GameObject, MapArchObject, Archetype>(mapViewsManager, mapArchObjectFactory, IGUIConstants.DEF_MAPSIZE, IGUIConstants.DEF_MAPSIZE, IGUIConstants.DEF_MAPDIFFICULTY, true, true, IGUIConstants.DEF_PICKMAP_WIDTH, IGUIConstants.DEF_PICKMAP_HEIGHT, parent);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public PreferencesGroup createPreferencesGroup(@NotNull final GlobalSettings globalSettings, @NotNull final DelegatingMapValidator<GameObject, MapArchObject, Archetype> validators, @NotNull final AppPreferencesModel appPreferencesModel, @NotNull final ExitConnectorModel exitConnectorModel, @NotNull final ConfigSourceFactory configSourceFactory) {
        return new PreferencesGroup("Gridarta for Atrinik", new ResPreferences(globalSettings, configSourceFactory), new AppPreferences(appPreferencesModel), new NetPreferences(), new GUIPreferences(globalSettings), new MiscPreferences(exitConnectorModel, globalSettings), new DevPreferences(), new UpdatePreferences(), new MapValidatorPreferences(validators, PREFERENCES_VALIDATOR_AUTO_DEFAULT));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public GUIMainControl<GameObject, MapArchObject, Archetype> createGUIMainControl(@NotNull final DefaultMainControl<GameObject, MapArchObject, Archetype> mainControl, @NotNull final ErrorView errorView, @NotNull final GUIUtils guiUtils, @NotNull final ConfigSourceFactory configSourceFactory, @NotNull final RendererFactory<GameObject, MapArchObject, Archetype> rendererFactory, @NotNull final FilterControl<GameObject, MapArchObject, Archetype> filterControl, @NotNull final PluginExecutor<GameObject, MapArchObject, Archetype> pluginExecutor, @NotNull final PluginParameters pluginParameters, @NotNull final AbstractMapManager<GameObject, MapArchObject, Archetype> mapManager, @NotNull final MapManager<GameObject, MapArchObject, Archetype> pickmapManager, @NotNull final MapModelFactory<GameObject, MapArchObject, Archetype> mapModelFactory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final FaceObjects faceObjects, @NotNull final GlobalSettings globalSettings, @NotNull final MapViewSettings mapViewSettings, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final PathManager pathManager, @NotNull final InsertionMode<GameObject, MapArchObject, Archetype> topmostInsertionMode, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final SystemIcons systemIcons, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final MapArchObjectFactory<MapArchObject> mapArchObjectFactory, @NotNull final DefaultMapReaderFactory<GameObject, MapArchObject, Archetype> mapReaderFactory, @NotNull final DelegatingMapValidator<GameObject, MapArchObject, Archetype> validators, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final PluginModel<GameObject, MapArchObject, Archetype> pluginModel, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeChooserModel<GameObject, MapArchObject, Archetype> archetypeChooserModel, @NotNull final ScriptedEventEditor<GameObject, MapArchObject, Archetype> scriptedEventEditor, @NotNull final AbstractResources<GameObject, MapArchObject, Archetype> resources, @NotNull final Spells<NumberSpell> numberSpells, @NotNull final Spells<GameObjectSpell<GameObject, MapArchObject, Archetype>> gameObjectSpells, @NotNull final PluginParameterFactory<GameObject, MapArchObject, Archetype> pluginParameterFactory) {
        return mainControl.createGUIMainControl(GuiFileFilters.pythonFileFilter, ".py", true, mapManager, pickmapManager, archetypeSet, mapModelFactory, guiUtils.getResourceIcon(IGUIConstants.TILE_NORTH), "AtrinikEditor.jar", new int[] { Archetype.TYPE_LOCKED_DOOR, Archetype.TYPE_SPECIAL_KEY, Archetype.TYPE_ALTAR_TRIGGER, Archetype.TYPE_MARKER, Archetype.TYPE_INVENTORY_CHECKER, Archetype.TYPE_SPAWN_POINT, Archetype.TYPE_CONTAINER, }, PREFERENCES_VALIDATOR_AUTO_DEFAULT, IGUIConstants.SPELL_FILE, this, errorView, new SubDirectoryCacheFiles(".dedit"), configSourceFactory, rendererFactory, filterControl, pluginExecutor, pluginParameters, faceObjects, globalSettings, mapViewSettings, faceObjectProviders, pathManager, topmostInsertionMode, gameObjectFactory, systemIcons, -1, archetypeTypeSet, mapArchObjectFactory, mapReaderFactory, validators, gameObjectMatchers, IGUIConstants.SCRIPTS_DIR, pluginModel, animationObjects, archetypeChooserModel, false, scriptedEventEditor, new Direction[] { Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH, }, resources, gameObjectSpells, numberSpells, pluginParameterFactory);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public AbstractResources<GameObject, MapArchObject, Archetype> newResources(@NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final AbstractArchetypeParser<GameObject, MapArchObject, Archetype, ?> archetypeParser, @NotNull final MapViewSettings mapViewSettings, @NotNull final FaceObjects faceObjects, @NotNull final AnimationObjects animationObjects, @NotNull final ArchFaceProvider archFaceProvider, @NotNull final FaceObjectProviders faceObjectProviders) {
        return new DefaultResources(gameObjectParser, archetypeSet, archetypeParser, mapViewSettings, faceObjects, animationObjects, archFaceProvider, faceObjectProviders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newServerActions(@NotNull final MapViewManager<GameObject, MapArchObject, Archetype> mapViewManager, @NotNull final FileControl<GameObject, MapArchObject, Archetype> fileControl, @NotNull final PathManager pathManager) {
        new AtrinikServerActions(mapViewManager, fileControl, pathManager);
    }

}
