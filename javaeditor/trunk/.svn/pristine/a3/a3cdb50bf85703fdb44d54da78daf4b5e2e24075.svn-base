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

package net.sf.gridarta.gui.panel.gameobjectattributes;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The "Msg Text" tab in the game object attributes panel.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MsgTextTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractGameObjectAttributesTab<G, A, R> {

    /**
     * Arch text field.
     */
    private final JTextArea archTextArea = new JTextArea(4, 25);

    /**
     * The content panel.
     */
    @NotNull
    private final JPanel textPanel = new JPanel(new GridLayout(1, 1));

    /**
     * The initial value of the {@link #archTextArea}.
     */
    @NotNull
    private String defaultArchTextAreaValue = "";

    /**
     * Creates a new instance.
     * @param gameObjectAttributesModel the model to track
     */
    public MsgTextTab(@NotNull final GameObjectAttributesModel<G, A, R> gameObjectAttributesModel) {
        super(gameObjectAttributesModel);
        archTextArea.setLineWrap(true);

        // create ScrollPane for text scrolling
        final JScrollPane sta = new JScrollPane(archTextArea);
        sta.setBorder(new EtchedBorder());
        sta.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sta.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //textPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        textPanel.add(sta);

        addAutoApply(archTextArea);
        refresh(gameObjectAttributesModel.getSelectedGameObject());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return "Msg Text";
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JPanel getPanel() {
        return textPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void refresh(@Nullable final G gameObject) {
        final boolean hasMessage;
        if (gameObject == null) {
            setArchTextArea("", Color.black);
            hasMessage = false;
        } else {
            final String msgText = gameObject.getMsgText();
            if (msgText != null) {
                setArchTextArea(msgText, Color.blue);
                hasMessage = true;
            } else {
                final String archMsgText = gameObject.getArchetype().getMsgText();
                if (archMsgText != null) {
                    setArchTextArea(archMsgText, Color.black);
                    hasMessage = true;
                } else {
                    setArchTextArea("", Color.black);
                    hasMessage = false;
                }
            }
        }
        setTabSeverity(hasMessage ? Severity.MODIFIED : Severity.DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canApply() {
        return !archTextArea.getText().equals(defaultArchTextAreaValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        archTextArea.requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void apply(@NotNull final G gameObject) {
        gameObject.setMsgText(getMsgText(gameObject));
    }

    /**
     * Returns the message text to be set.
     * @param gameObject the game object to match against
     * @return the message text
     */
    @Nullable
    private String getMsgText(@NotNull final GameObject<G, A, R> gameObject) {
        final BaseObject<G, A, R, ?> archetype = gameObject.getArchetype();
        // the msg TEXT!! ("msg ... endmsg")
        // if there is an entry in the archTextArea (msg window), this
        // overrules the default text (if any)
        final String msgText = StringUtils.removeTrailingWhitespaceFromLines(archTextArea.getText());
        if (msgText.length() > 0) { // there is something in the msg win
            final String archetypeMsgText = archetype.getMsgText();
            if (archetypeMsgText != null) {
                if (msgText.equals(archetypeMsgText)) {
                    return null; // yes, we don't need it in map
                } else {
                    return msgText;
                }
            } else {
                return msgText;
            }
        } else { // there is nothing in the msg win
            if (archetype.getMsgText() != null) {
                return ""; // but here we must overrule default with msg/endmsg (empty msg)
            } else {
                return null; // always delete this...
            }
        }
    }

    /**
     * Updates the value of the {@link #archTextArea}.
     * @param objText the value to set
     * @param color the color to set
     */
    private void setArchTextArea(@NotNull final String objText, @NotNull final Color color) {
        archTextArea.setForeground(color);
        archTextArea.setText(objText);
        archTextArea.setCaretPosition(0);
        defaultArchTextAreaValue = objText;
    }

}
