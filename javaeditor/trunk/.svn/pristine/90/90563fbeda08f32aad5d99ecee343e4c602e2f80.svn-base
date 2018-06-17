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

package net.sf.gridarta.model.validation.checks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttribute;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeAnimationName;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBitmask;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBool;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBoolSpec;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeFaceName;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeFixed;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeFloat;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeInt;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeInvSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeList;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeLong;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeMapPath;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeScriptFile;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeString;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeText;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeTreasure;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeVisitor;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeZSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.archetypetype.AttributeBitmask;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Creates checks from {@link ArchetypeType ArchetypeTypes}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeChecks {

    /**
     * The logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(ArchetypeTypeChecks.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private ArchetypeTypeChecks() {
    }

    /**
     * Adds attribute range checks for all defined attributes.
     * @param attributeRangeChecker the attribute range checker to add to
     * @param environmentChecker the environment checker to add to
     * @param archetypeTypeSet the archetype type set to use
     */
    public static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void addChecks(@NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final AttributeRangeChecker<G, A, R> attributeRangeChecker, @NotNull final EnvironmentChecker<G, A, R> environmentChecker) {
        final Set<Integer> allowsAllInvTypes = new HashSet<Integer>();
        for (final ArchetypeType archetypeType : archetypeTypeSet) {
            if (archetypeType.isAllowsAllInv()) {
                allowsAllInvTypes.add(archetypeType.getTypeNo());
            }
        }
        for (final ArchetypeType archetypeType : archetypeTypeSet) {
            addChecks(archetypeTypeSet, attributeRangeChecker, archetypeType);
            if (!archetypeType.isMap()) {
                environmentChecker.addNoMap(archetypeType);
            }
            final int[] types = archetypeType.getInv();
            if (types != null) {
                final Set<Integer> tmp = new HashSet<Integer>(allowsAllInvTypes);
                for (final int type : types) {
                    tmp.add(type);
                }
                final int[] tmp2 = new int[tmp.size()];
                final Iterator<Integer> it = tmp.iterator();
                for (int i = 0; i < tmp2.length; i++) {
                    tmp2[i] = it.next();
                }
                assert !it.hasNext();
                environmentChecker.addInv(archetypeType, tmp2);
            }
        }
    }

    /**
     * Adds attribute range checks for all defined attributes of an {@link
     * ArchetypeType}.
     * @param attributeRangeChecker the attribute range checker to add to
     * @param archetypeTypeSet the archetype type set to use
     * @param archetypeType the archetype type
     */
    private static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void addChecks(@NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final AttributeRangeChecker<G, A, R> attributeRangeChecker, @NotNull final ArchetypeType archetypeType) {
        if (archetypeType.hasTypeAttributes()) {
            return; // XXX: make such attributes work
        }

        final ArchetypeAttributeVisitor archetypeAttributeVisitor = new ArchetypeAttributeVisitor() {

            @Override
            public void visit(@NotNull final ArchetypeAttributeAnimationName archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeBitmask archetypeAttribute) {
                final AttributeBitmask bitmask = archetypeTypeSet.getBitmask(archetypeAttribute.getBitmaskName());
                if (bitmask != null) {
                    try {
                        attributeRangeChecker.add(archetypeType.getTypeNo(), archetypeAttribute.getArchetypeAttributeName(), archetypeAttribute.getAttributeName(), 0, bitmask.getMaxValue());
                    } catch (final InvalidCheckException ex) {
                        log.warn(ex.getMessage() + " in type " + archetypeType.getTypeNo() + ".");
                    }
                }
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeBool archetypeAttribute) {
                try {
                    attributeRangeChecker.add(archetypeType.getTypeNo(), archetypeAttribute.getArchetypeAttributeName(), archetypeAttribute.getAttributeName(), 0, 1);
                } catch (final InvalidCheckException ex) {
                    log.warn(ex.getMessage() + " in type " + archetypeType.getTypeNo() + ".");
                }
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeBoolSpec archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeFaceName archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeFixed archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeFloat archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeInt archetypeAttribute) {
                try {
                    attributeRangeChecker.add(archetypeType.getTypeNo(), archetypeAttribute.getArchetypeAttributeName(), archetypeAttribute.getAttributeName(), archetypeAttribute.getMinCheckValue(), archetypeAttribute.getMaxCheckValue());
                } catch (final InvalidCheckException ex) {
                    log.warn(ex.getMessage() + " in type " + archetypeType.getTypeNo() + ".");
                }
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeInvSpell archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeList archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeLong archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeMapPath archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeScriptFile archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeSpell archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeString archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeText archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeTreasure archetypeAttribute) {
                // XXX: missing
            }

            @Override
            public void visit(@NotNull final ArchetypeAttributeZSpell archetypeAttribute) {
                // XXX: missing
            }

        };

        for (final ArchetypeAttribute archetypeAttribute : archetypeType) {
            archetypeAttribute.visit(archetypeAttributeVisitor);
        }
    }

}
