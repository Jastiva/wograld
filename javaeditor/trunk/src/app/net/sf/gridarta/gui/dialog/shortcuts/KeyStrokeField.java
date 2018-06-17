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

package net.sf.gridarta.gui.dialog.shortcuts;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link javax.swing.JComponent} for selecting a {@link KeyStroke}.
 * @author Andreas Kirschbaum
 */
public class KeyStrokeField extends JTextField {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link KeyStrokeFieldListener KeyStrokeFieldListeners} to be
     * notified.
     */
    @NotNull
    private final EventListenerList2<KeyStrokeFieldListener> listeners = new EventListenerList2<KeyStrokeFieldListener>(KeyStrokeFieldListener.class);

    /**
     * The currently shown {@link KeyStroke}.
     */
    @Nullable
    private KeyStroke keyStroke;

    /**
     * Creates a new instance.
     * @param keyStroke the key stroke to show initially or <code>null</code>
     */
    public KeyStrokeField(@Nullable final KeyStroke keyStroke) {
        this.keyStroke = keyStroke;

        setFocusable(true);
        final KeyListener keyListener = new KeyListener() {

            @Override
            public void keyTyped(@NotNull final KeyEvent e) {
                // ignore
            }

            @Override
            public void keyPressed(@NotNull final KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_SHIFT:
                case KeyEvent.VK_CONTROL:
                case KeyEvent.VK_ALT:
                case KeyEvent.VK_CAPS_LOCK:
                case KeyEvent.VK_META:
                case KeyEvent.VK_ALT_GRAPH:
                    // ignore modifier keys
                    break;

                default:
                    setKeyStroke(KeyStroke.getKeyStrokeForEvent(e));
                    break;
                }
            }

            @Override
            public void keyReleased(@NotNull final KeyEvent e) {
                // ignore
            }

        };
        addKeyListener(keyListener);

        getInputMap(WHEN_IN_FOCUSED_WINDOW).clear();
        getInputMap(WHEN_IN_FOCUSED_WINDOW).setParent(null);
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).clear();
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).setParent(null);
        getInputMap(WHEN_FOCUSED).clear();
        getInputMap(WHEN_FOCUSED).setParent(null);

        updateKeyStroke();
    }

    /**
     * Adds a {@link KeyStrokeFieldListener} to be notified about changes.
     * @param listener the listener
     */
    public void addKeyStrokeListener(@NotNull final KeyStrokeFieldListener listener) {
        listeners.add(listener);
    }

    /**
     * Returns the currently shown {@link KeyStroke}.
     * @return the key stroke or <code>null</code>
     */
    @Nullable
    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    /**
     * Updates the current key stroke.
     * @param keyStroke the new key stroke
     */
    @SuppressWarnings("NullableProblems")
    private void setKeyStroke(@NotNull final KeyStroke keyStroke) {
        if (this.keyStroke == keyStroke) {
            return;
        }

        this.keyStroke = keyStroke;
        updateKeyStroke();
        for (final KeyStrokeFieldListener listener : listeners.getListeners()) {
            listener.keyStrokeChanged(keyStroke);
        }
    }

    /**
     * Updates the shown text to reflect the current value of {@link
     * #keyStroke}.
     */
    private void updateKeyStroke() {
        setText(keyStroke == null ? "none" : keyStroke.toString());
    }

}
