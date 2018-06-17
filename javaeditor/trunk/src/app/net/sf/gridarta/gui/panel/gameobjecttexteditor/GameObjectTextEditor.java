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

package net.sf.gridarta.gui.panel.gameobjecttexteditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.AttributeListUtils;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectUtils;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements the "Game Object Text Editor". It displays the attributes of the
 * currently selected game object as a text document.
 * @author Andreas Kirschbaum
 */
public class GameObjectTextEditor extends JPanel {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link ArchetypeTypeSet}.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The {@link JTextPane} that contains the attributes of the game object.
     */
    @NotNull
    private final JTextPane archEdit = new ScrollingTextPane();

    /**
     * The initial value of {@link #archEdit}.
     */
    @NotNull
    private String defaultArchEditValue = "";

    /**
     * Creates a new instance.
     * @param archetypeTypeSet the archetype type set
     */
    public GameObjectTextEditor(@NotNull final ArchetypeTypeSet archetypeTypeSet) {
        super(new BorderLayout());
        this.archetypeTypeSet = archetypeTypeSet;
        final JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(archEdit);
        scrollPane.setBackground(archEdit.getBackground());
        scrollPane.getViewport().add(archEdit);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    }

    /**
     * Returns whether the current text field differs from the initial value.
     * @return whether the text messages differ
     */
    public boolean canApplyChanges() {
        return !archEdit.getText().equals(defaultArchEditValue);
    }

    /**
     * Updates a {@link GameObject}'s attributes from the input field.
     * @param gameObject the game object to update
     */
    public void applyChanges(@NotNull final GameObject<?, ?, ?> gameObject) {
        gameObject.setObjectText(AttributeListUtils.diffArchTextValues(gameObject.getArchetype(), archEdit.getText()));
    }

    /**
     * Refreshes the input field to show the attributes of a {@link
     * GameObject}.
     * @param gameObject the game object to set the contents from
     * @return the severity corresponding to the attribute values
     */
    @NotNull
    public Severity refreshDisplay(@Nullable final GameObject<?, ?, ?> gameObject) {
        archEdit.setEnabled(gameObject != null);
        archEdit.setText("");
        Severity severity = Severity.DEFAULT;
        if (gameObject != null) {
            final MutableAttributeSet currentAttributes = archEdit.getStyle(StyleContext.DEFAULT_STYLE);
            try {
                final Document document = archEdit.getDocument();

                final ArchetypeType typeStruct = archetypeTypeSet.getArchetypeTypeByBaseObject(gameObject);
                final String errorText = GameObjectUtils.getSyntaxErrors(gameObject, typeStruct);

                final String objectText = gameObject.getObjectText();
                if (errorText == null) {
                    StyleConstants.setForeground(currentAttributes, Color.blue);
                    document.insertString(document.getLength(), objectText, currentAttributes);
                    if (!objectText.isEmpty()) {
                        severity = Severity.MODIFIED;
                    }
                } else {
                    severity = Severity.ERROR;

                    StyleConstants.setForeground(currentAttributes, Color.red);
                    document.insertString(document.getLength(), errorText, currentAttributes);

                    final Collection<String> errors = new HashSet<String>();
                    errors.addAll(Arrays.asList(StringUtils.PATTERN_END_OF_LINE.split(errorText)));
                    StyleConstants.setForeground(currentAttributes, Color.blue);
                    for (final String line : StringUtils.PATTERN_END_OF_LINE.split(objectText)) {
                        if (!errors.contains(line)) {
                            document.insertString(document.getLength(), line + '\n', currentAttributes);
                        }
                    }
                }

                StyleConstants.setForeground(currentAttributes, Color.black);
                document.insertString(document.getLength(), AttributeListUtils.diffArchTextKeys(gameObject, gameObject.getArchetype()), currentAttributes);
            } catch (final BadLocationException ex) {
                throw new AssertionError(ex);
            }
        }
        archEdit.setCaretPosition(0);
        defaultArchEditValue = archEdit.getText();
        return severity;
    }

    /**
     * Returns the text input pane.
     * @return the text input pane
     */
    @NotNull
    public Component getTextPane() {
        return archEdit;
    }

    /**
     * Activates this component.
     */
    public void activate() {
        archEdit.requestFocusInWindow();
        final Document document = archEdit.getDocument();
        if (document != null) {
            archEdit.setCaretPosition(document.getLength());
        }
    }

}
