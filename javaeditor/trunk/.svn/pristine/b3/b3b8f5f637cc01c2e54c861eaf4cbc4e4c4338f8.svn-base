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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.io.AbstractGameObjectParser;
import net.sf.gridarta.var.crossfire.model.archetype.Archetype;
import net.sf.gridarta.var.crossfire.model.gameobject.GameObject;
import net.sf.gridarta.var.crossfire.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link net.sf.gridarta.model.io.GameObjectParser} for Crossfire game object
 * instances.
 * @author Andreas Kirschbaum
 */
public class DefaultGameObjectParser extends AbstractGameObjectParser<GameObject, MapArchObject, Archetype> {

    /**
     * Determines the field order when writing objects to a file. The key is the
     * field name, the value is an Integer that describes the order. Lower
     * values are written first.
     */
    private static final Map<String, Integer> keys = new HashMap<String, Integer>();

    /*
     * Initialize {@link keys} array.
     * @todo this information should be read from a file.
     */

    static {
        addKey("name");
        addKey("name_pl");
        addKey("custom_name");
        addKey("title");
        addKey("race");
        addKey("slaying");
        addKey("skill");
        addKey("msg");
        addKey("lore");
        addKey("other_arch");
        addKey("face");
        addKey("animation");
        addKey("is_animated");
        addKey("Str");
        addKey("Dex");
        addKey("Con");
        addKey("Wis");
        addKey("Pow");
        addKey("Cha");
        addKey("Int");
        addKey("hp");
        addKey("maxhp");
        addKey("sp");
        addKey("maxsp");
        addKey("grace");
        addKey("maxgrace");
        addKey("exp");
        addKey("perm_exp");
        addKey("expmul");
        addKey("food");
        addKey("dam");
        addKey("luck");
        addKey("wc");
        addKey("ac");
        addKey("x");
        addKey("y");
        addKey("speed");
        addKey("speed_left");
        addKey("move_state");
        addKey("attack_movement");
        addKey("nrof");
        addKey("level");
        addKey("direction");
        addKey("type");
        addKey("subtype");
        addKey("attacktype");
        addKey("resist_physical");
        addKey("resist_magic");
        addKey("resist_fire");
        addKey("resist_electricity");
        addKey("resist_cold");
        addKey("resist_confusion");
        addKey("resist_acid");
        addKey("resist_drain");
        addKey("resist_weaponmagic");
        addKey("resist_ghosthit");
        addKey("resist_poison");
        addKey("resist_slow");
        addKey("resist_paralyze");
        addKey("resist_turn_undead");
        addKey("resist_fear");
        addKey("resist_cancellation");
        addKey("resist_deplete");
        addKey("resist_death");
        addKey("resist_chaos");
        addKey("resist_counterspell");
        addKey("resist_godpower");
        addKey("resist_holyword");
        addKey("resist_blind");
        addKey("resist_internal");
        addKey("resist_life_stealing");
        addKey("resist_disease");
        addKey("path_attuned");
        addKey("path_repelled");
        addKey("path_denied");
        addKey("material");
        addKey("materialname");
        addKey("value");
        addKey("carrying");
        addKey("weight");
        addKey("invisible");
        addKey("state");
        addKey("magic");
        addKey("last_heal");
        addKey("last_sp");
        addKey("last_grace");
        addKey("last_eat");
        addKey("connected");
        addKey("glow_radius");
        addKey("randomitems");
        addKey("npc_status");
        addKey("npc_program");
        addKey("run_away");
        addKey("pick_up");
        addKey("container");
        addKey("will_apply");
        addKey("smoothlevel");
        addKey("map_layer");
        addKey("current_weapon_script");
        addKey("weapontype");
        addKey("tooltype");
        addKey("elevation");
        addKey("client_type");
        addKey("item_power");
        addKey("duration");
        addKey("range");
        addKey("range_modifier");
        addKey("duration_modifier");
        addKey("dam_modifier");
        addKey("gen_sp_armour");
        addKey("move_type");
        addKey("move_block");
        addKey("move_allow");
        addKey("move_on");
        addKey("move_off");
        addKey("move_slow");
        addKey("move_slow_penalty");
        addKey("alive");
        addKey("wiz");
        addKey("was_wiz");
        addKey("applied");
        addKey("unpaid");
        addKey("can_use_shield");
        addKey("no_pick");
        addKey("walk_on");
        addKey("no_pass");
        addKey("is_animated");
        addKey("slow_move");
        addKey("flying");
        addKey("monster");
        addKey("friendly");
        addKey("generator");
        addKey("is_thrown");
        addKey("auto_apply");
        addKey("treasure");
        addKey("player sold");
        addKey("see_invisible");
        addKey("can_roll");
        addKey("overlay_floor");
        addKey("is_turnable");
        addKey("walk_off");
        addKey("fly_on");
        addKey("fly_off");
        addKey("is_used_up");
        addKey("identified");
        addKey("reflecting");
        addKey("changing");
        addKey("splitting");
        addKey("hitback");
        addKey("startequip");
        addKey("blocksview");
        addKey("undead");
        addKey("scared");
        addKey("unaggressive");
        addKey("reflect_missile");
        addKey("reflect_spell");
        addKey("no_magic");
        addKey("no_fix_player");
        addKey("is_lightable");
        addKey("tear_down");
        addKey("run_away");
        addKey("pass_thru");
        addKey("can_pass_thru");
        addKey("unique");
        addKey("no_drop");
        addKey("wizcast");
        addKey("can_cast_spell");
        addKey("can_use_scroll");
        addKey("can_use_range");
        addKey("can_use_bow");
        addKey("can_use_armour");
        addKey("can_use_weapon");
        addKey("can_use_ring");
        addKey("has_ready_range");
        addKey("has_ready_bow");
        addKey("xrays");
        addKey("is_floor");
        addKey("lifesave");
        addKey("no_strength");
        addKey("sleep");
        addKey("stand_still");
        addKey("random_move");
        addKey("only_attack");
        addKey("confused");
        addKey("stealth");
        addKey("cursed");
        addKey("damned");
        addKey("see_anywhere");
        addKey("known_magical");
        addKey("known_cursed");
        addKey("can_use_skill");
        addKey("been_applied");
        addKey("has_ready_scroll");
        addKey("can_use_rod");
        addKey("can_use_horn");
        addKey("make_invisible");
        addKey("inv_locked");
        addKey("is_wooded");
        addKey("is_hilly");
        addKey("has_ready_skill");
        addKey("has_ready_weapon");
        addKey("no_skill_ident");
        addKey("is_blind");
        addKey("can_see_in_dark");
        addKey("is_cauldron");
        addKey("is_dust");
        addKey("no_steal");
        addKey("one_hit");
        addKey("berserk");
        addKey("neutral");
        addKey("no_attack");
        addKey("no_damage");
        addKey("activate_on_push");
        addKey("activate_on_release");
        addKey("is_water");
        addKey("use_content_on_gen");
        addKey("is_buildable");
        addKey("body_range");
        addKey("body_arm");
        addKey("body_torso");
        addKey("body_head");
        addKey("body_neck");
        addKey("body_skill");
        addKey("body_finger");
        addKey("body_shoulder");
        addKey("body_foot");
        addKey("body_hand");
        addKey("body_wrist");
        addKey("body_waist");
    }

    /**
     * The next key added via {@link #addKey(String)} is assigned this id.
     */
    private static int idKey = 1;

    /**
     * Adds a new key to {@link #keys}. The order in which the keys are added is
     * the order in which the objects fields are written out.
     * @param key the key to add
     */
    private static void addKey(final String key) {
        keys.put(key.equals("msg") || key.equals("lore") ? key + "\n" : key + " ", idKey++);
    }

    /**
     * Comparator to compare two object fields. The comparator sorts the fields
     * in the order in which they should be written to disk.
     */
    private static final Comparator<String> keyOrderComparator = new Comparator<String>() {

        @Override
        public int compare(final String o1, final String o2) {
            final Integer i1 = keys.get(o1);
            final Integer i2 = keys.get(o2);
            final int v1 = i1 == null ? Integer.MAX_VALUE : i1;
            final int v2 = i2 == null ? Integer.MAX_VALUE : i2;
            if (v1 < v2) {
                return -1;
            }
            if (v1 > v2) {
                return +1;
            }
            assert v1 == v2;
            if (v1 == Integer.MAX_VALUE) {
                return o1.compareTo(o2);
            }
            return 0;
        }

    };

    /**
     * Create a new instance.
     * @param gameObjectFactory the game object factory for creating new game
     * object instances
     * @param archetypeSet the archetype set for looking up archetypes
     */
    public DefaultGameObjectParser(@NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet) {
        super(gameObjectFactory, archetypeSet);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Map<String, String> getModifiedFields(@NotNull final GameObject gameObject) {
        final Map<String, String> fields = new TreeMap<String, String>(keyOrderComparator);
        addModifiedFields(gameObject, fields);
        return fields;
    }

}
