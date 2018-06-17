/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.map.renderer;

import java.util.Collections;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.errors.ValidationError;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Andreas Kirschbaum
 */
public class ToolTipAppender<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link GameObjectParser} for creating tooltip information or
     * <code>null</code>.
     */
    @Nullable
    private final GameObjectParser<G, A, R> gameObjectParser;

    @NotNull
    private final StringBuilder sb = new StringBuilder("<html>");

    private boolean empty = true;

    /**
     * Creates a new instance.
     * @param gameObjectParser the game object parser for creating tooltip
     * information or <code>null</code>
     */
    public ToolTipAppender(@Nullable final GameObjectParser<G, A, R> gameObjectParser) {
        this.gameObjectParser = gameObjectParser;
    }

    public void appendGameObject(@NotNull final G gameObject, final boolean alwaysInclude, @NotNull final String prefix) {
        if (false) {
            final String objectText = gameObject.getObjectText();
            final String msgText = gameObject.getMsgText();
            final String text = objectText + (msgText == null ? "" : msgText + "\n" + "msg\n" + msgText + "\n" + "endmsg");
            if (alwaysInclude || text.length() > 0 || !gameObject.isEmpty()) {
                if (empty) {
                    empty = false;
                } else {
                    sb.append("<br><hr>");
                }
                sb.append(prefix);
                sb.append(("<b>" + encode(gameObject.getBestName()) + "</b>\n" + encode(text)).trim().replaceAll("\n", "<br>" + prefix));
                for (final G invGameObject : gameObject.reverse()) {
                    appendGameObject(invGameObject, true, prefix + "&nbsp;&nbsp;&nbsp;&nbsp;");
                }
            }
        } else {
            final Map<String, String> fields = gameObjectParser != null ? gameObjectParser.getModifiedFields(gameObject) : Collections.<String, String>emptyMap();
            fields.remove("x ");
            fields.remove("y ");
            if (alwaysInclude || !fields.isEmpty() || !gameObject.isEmpty()) {
                if (empty) {
                    empty = false;
                } else {
                    sb.append("<br><hr>");
                }
                sb.append(prefix);
                sb.append("<b>").append(encode(gameObject.getBestName())).append("</b>");
                for (final Map.Entry<String, String> field : fields.entrySet()) {
                    sb.append("<br>");
                    sb.append(prefix);
                    sb.append((encode(field.getKey()) + encode(field.getValue())).trim().replaceAll("\n", "<br>" + prefix));
                }
                for (final G invGameObject : gameObject.reverse()) {
                    appendGameObject(invGameObject, true, prefix + "&nbsp;&nbsp;&nbsp;&nbsp;");
                }
            }
        }
    }

    public void appendValidationError(@NotNull final ValidationError<G, ?, ?> error) {
        if (empty) {
            empty = false;
        } else {
            sb.append("\n<hr>");
        }
        sb.append(error.getMessage().trim());
    }

    @Nullable
    public String finish() {
        return empty ? null : StringUtils.PATTERN_NEWLINE.matcher(sb.toString()).replaceAll("<br>");
    }

    /**
     * Encodes a string so that the result can be embedded into HTML.
     * @param str the string to encode
     * @return the encoded string
     */
    @NotNull
    private static String encode(@NotNull final String str) {
        final StringBuilder sb = new StringBuilder(str.length());
        for (final char ch : str.toCharArray()) {
            switch (ch) {
            case '<':
                sb.append("&lt;");
                break;

            case '>':
                sb.append("&gt;");
                break;

            case '&':
                sb.append("&amp;");
                break;

            default:
                sb.append(ch);
                break;
            }
        }
        return sb.toString();
    }

}
