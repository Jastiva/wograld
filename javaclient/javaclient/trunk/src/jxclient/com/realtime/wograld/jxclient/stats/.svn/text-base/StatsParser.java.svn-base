/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.stats;

import com.realtime.wograld.jxclient.server.wograld.WograldStatsListener;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to parse stat names.
 * @author Andreas Kirschbaum
 */
public class StatsParser {

    /**
     * Maps stat names to stat index values. Only stats useful in skin files are
     * included.
     */
    @NotNull
    private static final Map<String, Integer> STAT_TABLE = new HashMap<String, Integer>();

    static {
        STAT_TABLE.put("AC", WograldStatsListener.CS_STAT_AC);
        STAT_TABLE.put("ARM", WograldStatsListener.CS_STAT_ARMOUR);
        STAT_TABLE.put("CHA", WograldStatsListener.CS_STAT_CHA);
        STAT_TABLE.put("CHA_APPLIED", WograldStatsListener.CS_STAT_APPLIED_CHA);
        STAT_TABLE.put("CHA_BASE", WograldStatsListener.CS_STAT_BASE_CHA);
        STAT_TABLE.put("CHA_RACE", WograldStatsListener.CS_STAT_RACE_CHA);
        STAT_TABLE.put("CON", WograldStatsListener.CS_STAT_CON);
        STAT_TABLE.put("CON_APPLIED", WograldStatsListener.CS_STAT_APPLIED_CON);
        STAT_TABLE.put("CON_BASE", WograldStatsListener.CS_STAT_BASE_CON);
        STAT_TABLE.put("CON_RACE", WograldStatsListener.CS_STAT_RACE_CON);
        STAT_TABLE.put("DAM", WograldStatsListener.CS_STAT_DAM);
        STAT_TABLE.put("DEX", WograldStatsListener.CS_STAT_DEX);
        STAT_TABLE.put("DEX_APPLIED", WograldStatsListener.CS_STAT_APPLIED_DEX);
        STAT_TABLE.put("DEX_BASE", WograldStatsListener.CS_STAT_BASE_DEX);
        STAT_TABLE.put("DEX_RACE", WograldStatsListener.CS_STAT_RACE_DEX);
        STAT_TABLE.put("EXP", WograldStatsListener.CS_STAT_EXP64);
        STAT_TABLE.put("EXP_NEXT_LEVEL", WograldStatsListener.C_STAT_EXP_NEXT_LEVEL);
        STAT_TABLE.put("FOOD", WograldStatsListener.CS_STAT_FOOD);
        STAT_TABLE.put("GOLEM_HP", WograldStatsListener.CS_STAT_GOLEM_HP);
        STAT_TABLE.put("GRACE", WograldStatsListener.CS_STAT_GRACE);
        STAT_TABLE.put("HP", WograldStatsListener.CS_STAT_HP);
        STAT_TABLE.put("INT", WograldStatsListener.CS_STAT_INT);
        STAT_TABLE.put("INT_APPLIED", WograldStatsListener.CS_STAT_APPLIED_INT);
        STAT_TABLE.put("INT_BASE", WograldStatsListener.CS_STAT_BASE_INT);
        STAT_TABLE.put("INT_RACE", WograldStatsListener.CS_STAT_RACE_INT);
        STAT_TABLE.put("LEVEL", WograldStatsListener.CS_STAT_LEVEL);
        STAT_TABLE.put("LOWFOOD", WograldStatsListener.C_STAT_LOWFOOD);
        STAT_TABLE.put("POISONED", WograldStatsListener.C_STAT_POISONED);
        STAT_TABLE.put("POW", WograldStatsListener.CS_STAT_POW);
        STAT_TABLE.put("POW_APPLIED", WograldStatsListener.CS_STAT_APPLIED_POW);
        STAT_TABLE.put("POW_BASE", WograldStatsListener.CS_STAT_BASE_POW);
        STAT_TABLE.put("POW_RACE", WograldStatsListener.CS_STAT_RACE_POW);
        STAT_TABLE.put("RANGE", WograldStatsListener.CS_STAT_RANGE);
        STAT_TABLE.put("RES_ACID", WograldStatsListener.CS_STAT_RES_ACID);
        STAT_TABLE.put("RES_BLIND", WograldStatsListener.CS_STAT_RES_BLIND);
        STAT_TABLE.put("RES_COLD", WograldStatsListener.CS_STAT_RES_COLD);
        STAT_TABLE.put("RES_CONF", WograldStatsListener.CS_STAT_RES_CONF);
        STAT_TABLE.put("RES_DEATH", WograldStatsListener.CS_STAT_RES_DEATH);
        STAT_TABLE.put("RES_DEPLETE", WograldStatsListener.CS_STAT_RES_DEPLETE);
        STAT_TABLE.put("RES_DRAIN", WograldStatsListener.CS_STAT_RES_DRAIN);
        STAT_TABLE.put("RES_ELEC", WograldStatsListener.CS_STAT_RES_ELEC);
        STAT_TABLE.put("RES_FEAR", WograldStatsListener.CS_STAT_RES_FEAR);
        STAT_TABLE.put("RES_FIRE", WograldStatsListener.CS_STAT_RES_FIRE);
        STAT_TABLE.put("RES_GHOSTHIT", WograldStatsListener.CS_STAT_RES_GHOSTHIT);
        STAT_TABLE.put("RES_HOLYWORD", WograldStatsListener.CS_STAT_RES_HOLYWORD);
        STAT_TABLE.put("RES_MAG", WograldStatsListener.CS_STAT_RES_MAG);
        STAT_TABLE.put("RES_PARA", WograldStatsListener.CS_STAT_RES_PARA);
        STAT_TABLE.put("RES_PHYS", WograldStatsListener.CS_STAT_RES_PHYS);
        STAT_TABLE.put("RES_POISON", WograldStatsListener.CS_STAT_RES_POISON);
        STAT_TABLE.put("RES_SLOW", WograldStatsListener.CS_STAT_RES_SLOW);
        STAT_TABLE.put("RES_TURN_UNDEAD", WograldStatsListener.CS_STAT_RES_TURN_UNDEAD);
        STAT_TABLE.put("SP", WograldStatsListener.CS_STAT_SP);
        STAT_TABLE.put("SPEED", WograldStatsListener.CS_STAT_SPEED);
        STAT_TABLE.put("STR", WograldStatsListener.CS_STAT_STR);
        STAT_TABLE.put("STR_APPLIED", WograldStatsListener.CS_STAT_APPLIED_STR);
        STAT_TABLE.put("STR_BASE", WograldStatsListener.CS_STAT_BASE_STR);
        STAT_TABLE.put("STR_RACE", WograldStatsListener.CS_STAT_RACE_STR);
        STAT_TABLE.put("TITLE", WograldStatsListener.CS_STAT_TITLE);
        STAT_TABLE.put("WC", WograldStatsListener.CS_STAT_WC);
        STAT_TABLE.put("WEIGHT", WograldStatsListener.C_STAT_WEIGHT);
        STAT_TABLE.put("WEIGHT_LIMIT", WograldStatsListener.CS_STAT_WEIGHT_LIM);
        STAT_TABLE.put("WIS", WograldStatsListener.CS_STAT_WIS);
        STAT_TABLE.put("WIS_APPLIED", WograldStatsListener.CS_STAT_APPLIED_WIS);
        STAT_TABLE.put("WIS_BASE", WograldStatsListener.CS_STAT_BASE_WIS);
        STAT_TABLE.put("WIS_RACE", WograldStatsListener.CS_STAT_RACE_WIS);
        STAT_TABLE.put("WEAPON_SPEED", WograldStatsListener.CS_STAT_WEAP_SP);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private StatsParser() {
    }

    /**
     * Converts a stat name into a stat index.
     * @param name the stat name
     * @return the stat index
     * @throws IllegalArgumentException if the stat name is undefined
     */
    public static int parseStat(@NotNull final String name) {
        if (!STAT_TABLE.containsKey(name)) {
            throw new IllegalArgumentException();
        }

        return STAT_TABLE.get(name);
    }

}
