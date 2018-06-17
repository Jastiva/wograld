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

package net.sf.gridarta.textedit.textarea.tokenmarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.text.Segment;
import net.sf.gridarta.textedit.textarea.Token;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link TokenMarker} for the message field of Daimonin AI objects.
 * @author Andreas Kirschbaum
 */
public class DaimoninAITokenMarker extends TokenMarker {

    /**
     * Commands having a fixed command name. Maps command name to command
     * specification.
     */
    private static final Map<String, Spec> fixedSpecs = new HashMap<String, Spec>();

    /**
     * Commands having variable command names. Maps command name to command
     * specification.
     */
    private static final Map<Pattern, Spec> regexSpecs = new HashMap<Pattern, Spec>();

    static {
        fixedSpecs.put("actions:", new Spec(Token.LABEL));
        fixedSpecs.put("moves:", new Spec(Token.LABEL));
        fixedSpecs.put("processes:", new Spec(Token.LABEL));
        fixedSpecs.put("bow_attack_enemy", new Spec(Token.KEYWORD1));
        fixedSpecs.put("choose_enemy", new Spec(Token.KEYWORD1, new Parameter("antilure_distance", Token.KEYWORD2, ParameterType.INTEGER)));
        fixedSpecs.put("dont_stand_still", new Spec(Token.KEYWORD1, new Parameter("max_idle_time", Token.KEYWORD2, ParameterType.INTEGER)));
        fixedSpecs.put("friendship", new Spec(Token.KEYWORD1, new Parameter("group", Token.KEYWORD2, ParameterType.STRING_INTEGER), new Parameter("name", Token.KEYWORD2, ParameterType.STRING_INTEGER), new Parameter("player", Token.KEYWORD2, ParameterType.INTEGER), new Parameter("race", Token.KEYWORD2, ParameterType.STRING_INTEGER)));
        fixedSpecs.put("groups", new Spec(Token.KEYWORD1, new Parameter("name", Token.KEYWORD2, ParameterType.STRING)));
        fixedSpecs.put("heal_friend", new Spec(Token.KEYWORD1, new Parameter("healing_min_friendship", Token.KEYWORD2, ParameterType.INTEGER)));
        fixedSpecs.put("look_for_other_mobs", new Spec(Token.KEYWORD1));
        regexSpecs.put(Pattern.compile("lua:.*"), new Spec(Token.KEYWORD1));
        fixedSpecs.put("melee_attack_enemy", new Spec(Token.KEYWORD1));
        fixedSpecs.put("move_randomly", new Spec(Token.KEYWORD1, new Parameter("xlimit", Token.KEYWORD2, ParameterType.INTEGER), new Parameter("ylimit", Token.KEYWORD2, ParameterType.INTEGER)));
        fixedSpecs.put("move_towards_enemy", new Spec(Token.KEYWORD1));
        fixedSpecs.put("move_towards_enemy_last_known_pos", new Spec(Token.KEYWORD1));
        fixedSpecs.put("move_towards_home", new Spec(Token.KEYWORD1));
        fixedSpecs.put("move_towards_waypoint", new Spec(Token.KEYWORD1));
        fixedSpecs.put("run_away_from_enemy", new Spec(Token.KEYWORD1, new Parameter("hp_threshold", Token.KEYWORD2, ParameterType.INTEGER)));
        fixedSpecs.put("search_for_lost_enemy", new Spec(Token.KEYWORD1));
        fixedSpecs.put("sleep", new Spec(Token.KEYWORD1));
        fixedSpecs.put("spell_attack_enemy", new Spec(Token.KEYWORD1));
        fixedSpecs.put("stand_still", new Spec(Token.KEYWORD1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte markTokensImpl(final byte token, @NotNull final Segment line, final int lineIndex) {
        final char[] array = line.array;
        int offset = line.offset;
        final int end = line.count + offset;
        if (offset >= end) {
            // empty line ==> mark nothing
        } else if (array[offset] == '#') {
            // comment line
            addToken(line.count, Token.COMMENT1);
        } else if (array[offset] == ' ') {
            // syntax error
            addToken(line.count, Token.INVALID);
        } else {
            // normal line

            // 1. parse line into segments

            // Collects parsed segments: command, space, parameter, space,
            // parameter, space, ...
            final List<Integer> segments = new ArrayList<Integer>();

            while (offset < end) {
                final int commandStart = offset;
                while (offset < end && array[offset] != ' ') {
                    offset++;
                }
                segments.add(offset - commandStart);

                final int spacesStart = offset;
                while (offset < end && array[offset] == ' ') {
                    offset++;
                }
                segments.add(offset - spacesStart);
            }

            // 2. create tokens
            final Iterator<Integer> it = segments.iterator();
            int offset2 = line.offset;

            final int commandLength = it.next();
            final String command = new String(array, offset2, commandLength);
            final Spec spec = getSpec(command);
            addToken(commandLength, spec != null ? spec.getId() : Token.INVALID);
            offset2 += commandLength;

            final int spacesLength1 = it.next();
            addToken(spacesLength1, Token.NULL);
            offset2 += spacesLength1;

            while (it.hasNext()) {
                final int paramLength = it.next();
                final String[] tmp = StringUtils.PATTERN_EQUAL.split(new String(array, offset2, paramLength), 2);
                if (tmp.length == 2) {
                    final String param = tmp[0];
                    final CharSequence arg = tmp[1];

                    final Parameter parameter = spec != null ? spec.getParameter(param) : null;
                    final boolean parameterValid = parameter != null;
                    final boolean valueValid = parameter == null || parameter.getParameterType().getPattern().matcher(arg).matches();
                    addToken(param.length(), parameterValid ? parameter.getId() : Token.INVALID);
                    addToken(1, (param.length() == 0 && !parameterValid) || (arg.length() == 0 && !valueValid) ? Token.INVALID : Token.OPERATOR);
                    addToken(arg.length(), valueValid ? Token.NULL : Token.INVALID);
                } else {
                    // missing '=' sign ==> syntax error
                    addToken(paramLength, Token.INVALID);
                }
                offset2 += paramLength;

                final int spacesLength2 = it.next();
                addToken(spacesLength2, Token.NULL);
                offset2 += spacesLength2;
            }
        }

        return Token.NULL;
    }

    /**
     * Returns the command specification from a command name.
     * @param command the command name
     * @return the command specification, or <code>null</code> if the command
     *         name is undefined.
     */
    @Nullable
    private static Spec getSpec(@NotNull final String command) {
        final Spec fixedSpec = fixedSpecs.get(command);
        if (fixedSpec != null) {
            return fixedSpec;
        }

        for (final Map.Entry<Pattern, Spec> entry : regexSpecs.entrySet()) {
            if (entry.getKey().matcher(command).matches()) {
                return entry.getValue();
            }
        }

        return null;
    }

}
