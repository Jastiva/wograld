/*
 * DefaultInputHandler.java - Default implementation of an input handler
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.KeyStroke;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import net.sf.gridarta.textedit.textarea.actions.InputActions;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The default input handler. It maps sequences of keystrokes into actions and
 * inserts key typed events into the text area.
 * @author Slava Pestov
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class DefaultInputHandler extends InputHandler {

    @NotNull
    private final Map<KeyStroke, ActionListener> bindings;

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DefaultInputHandler.class);

    @NotNull
    private final InputActions inputActions;

    /**
     * Creates a new input handler with no key bindings defined.
     * @param scriptEditControl the script edit control to affect
     */
    public DefaultInputHandler(@NotNull final ScriptEditControl scriptEditControl) {
        inputActions = new InputActions(scriptEditControl);
        bindings = new HashMap<KeyStroke, ActionListener>();
    }

    /**
     * Sets up the default key bindings.
     */
    @Override
    public void addDefaultKeyBindings() {
        addKeyBinding("BACK_SPACE", inputActions.backspace);
        addKeyBinding("C+BACK_SPACE", inputActions.backspaceWord);
        addKeyBinding("DELETE", inputActions.delete);
        addKeyBinding("C+DELETE", inputActions.deleteWord);

        addKeyBinding("ENTER", inputActions.insertBreak);
        addKeyBinding("TAB", inputActions.insertTab);

        addKeyBinding("INSERT", inputActions.overwrite);
        addKeyBinding("C+\\", inputActions.toggleRectangle);

        addKeyBinding("HOME", inputActions.home);
        addKeyBinding("END", inputActions.end);
        addKeyBinding("S+HOME", inputActions.selectHome);
        addKeyBinding("S+END", inputActions.selectEnd);
        addKeyBinding("C+HOME", inputActions.documentHome);
        addKeyBinding("C+END", inputActions.documentEnd);
        addKeyBinding("CS+HOME", inputActions.selectDocHome);
        addKeyBinding("CS+END", inputActions.selectDocEnd);

        addKeyBinding("PAGE_UP", inputActions.prevPage);
        addKeyBinding("PAGE_DOWN", inputActions.nextPage);
        addKeyBinding("S+PAGE_UP", inputActions.selectPrevPage);
        addKeyBinding("S+PAGE_DOWN", inputActions.selectNextPage);

        addKeyBinding("LEFT", inputActions.prevChar);
        addKeyBinding("S+LEFT", inputActions.selectPrevChar);
        addKeyBinding("C+LEFT", inputActions.prevWord);
        addKeyBinding("CS+LEFT", inputActions.selectPrevWord);
        addKeyBinding("RIGHT", inputActions.nextChar);
        addKeyBinding("S+RIGHT", inputActions.selectNextChar);
        addKeyBinding("C+RIGHT", inputActions.nextWord);
        addKeyBinding("CS+RIGHT", inputActions.selectNextWord);
        addKeyBinding("UP", inputActions.prevLine);
        addKeyBinding("S+UP", inputActions.selectPrevLine);
        addKeyBinding("DOWN", inputActions.nextLine);
        addKeyBinding("S+DOWN", inputActions.selectNextLine);

        addKeyBinding("C+ENTER", inputActions.repeat);

        addKeyBinding(".", inputActions.functionMenu);
    }

    /**
     * Adds a key binding to this input handler. The key binding is a list of
     * white space separated key strokes of the form <i>[modifiers+]key</i>
     * where modifier is C for Control, A for Alt, or S for Shift, and key is
     * either a character (a-z) or a field name in the KeyEvent class prefixed
     * with VK_ (e.g., BACK_SPACE)
     * @param keyBinding the key binding
     * @param action the action
     */
    private void addKeyBinding(final String keyBinding, final ActionListener action) {
        final KeyStroke keyStroke = parseKeyStroke(keyBinding);
        if (keyStroke != null) {
            bindings.put(keyStroke, action);
        }
    }

    /**
     * Removes a key binding from this input handler. This is not yet
     * implemented.
     * @param keyBinding the key binding
     */
    @Override
    public void removeKeyBinding(final String keyBinding) {
        throw new InternalError("Not yet implemented");
    }

    /**
     * Removes all key bindings from this input handler.
     */
    @Override
    public void removeAllKeyBindings() {
        bindings.clear();
    }

    /**
     * Returns a copy of this input handler that shares the same key bindings.
     * Setting key bindings in the copy will also set them in the original.
     */
    @Override
    public InputHandler copy() {
        return new DefaultInputHandler(this);
    }

    /**
     * Handle a key pressed event. This will look up the binding for the key
     * stroke and execute it.
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        final int keyCode = e.getKeyCode();
        final int modifiers = e.getModifiers();

        if (keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT || keyCode == KeyEvent.VK_META) {
            return;
        }

        if ((modifiers & ~InputEvent.SHIFT_MASK) != 0 || e.isActionKey() || keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_TAB || keyCode == KeyEvent.VK_ESCAPE) {
            if (handleGrabAction(e)) {
                return;
            }

            final KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
            final ActionListener actionListener = bindings.get(keyStroke);
            if (actionListener != null) {
                executeAction(actionListener, e.getSource(), null);
                e.consume();
            }
        }
    }

    /**
     * Handle a key typed event. This inserts the key into the text area.
     */
    @Override
    public void keyTyped(final KeyEvent e) {
        final int modifiers = e.getModifiers();
        final char c = e.getKeyChar();

        if (c != KeyEvent.CHAR_UNDEFINED && (modifiers & InputEvent.ALT_MASK) == 0) {
            if (c >= (char) 0x20 && c != (char) 0x7f) {
                final KeyStroke keyStroke = KeyStroke.getKeyStroke(Character.toUpperCase(c));
                final ActionListener actionListener = bindings.get(keyStroke);

                if (actionListener != null) {
                    executeAction(actionListener, e.getSource(), String.valueOf(c));
                    return;
                }

                if (handleGrabAction(e)) {
                    return;
                }

                // 0-9 adds another 'digit' to the repeat number
                if (isRepeatEnabled() && Character.isDigit(c)) {
                    repeatCount *= 10;
                    repeatCount += (int) c - (int) '0';
                    return;
                }

                executeAction(inputActions.insertChar, e.getSource(), String.valueOf(e.getKeyChar()));

                repeatCount = 0;
                setRepeatEnabled(false);
            }
        }
    }

    /**
     * Converts a string to a keystroke. The string should be of the form
     * <i>modifiers</i>+<i>shortcut</i> where <i>modifiers</i> is any
     * combination of A for Alt, C for Control, S for Shift or M for Meta, and
     * <i>shortcut</i> is either a single character, or a key code name from the
     * <code>KeyEvent</code> class, without the <code>VK_</code> prefix.
     * @param keyStroke a string description of the key stroke
     * @return the key stroke or <code>null</code> if invalid
     */
    @Nullable
    private static KeyStroke parseKeyStroke(final String keyStroke) {
        if (keyStroke == null) {
            return null;
        }

        int modifiers = 0;
        final int index = keyStroke.indexOf('+');
        if (index != -1) {
            for (int i = 0; i < index; i++) {
                switch (Character.toUpperCase(keyStroke.charAt(i))) {
                case 'A':
                    modifiers |= InputEvent.ALT_MASK;
                    break;
                case 'C':
                    modifiers |= InputEvent.CTRL_MASK;
                    break;
                case 'M':
                    modifiers |= InputEvent.META_MASK;
                    break;
                case 'S':
                    modifiers |= InputEvent.SHIFT_MASK;
                    break;
                }
            }
        }

        final String key = keyStroke.substring(index + 1);
        if (key.length() == 1) {
            final char ch = Character.toUpperCase(key.charAt(0));
            if (modifiers == 0) {
                return KeyStroke.getKeyStroke(ch);
            } else {
                return KeyStroke.getKeyStroke(ch, modifiers);
            }
        } else if (key.length() == 0) {
            log.error("Invalid key stroke: " + keyStroke);
            return null;
        } else {
            final int ch;

            try {
                ch = KeyEvent.class.getField("VK_" + key).getInt(null);
            } catch (final IllegalAccessException ignored) {
                log.error("Invalid key stroke: " + keyStroke);
                return null;
            } catch (final NoSuchFieldException ignored) {
                log.error("Invalid key stroke: " + keyStroke);
                return null;
            }

            return KeyStroke.getKeyStroke(ch, modifiers);
        }
    }

    private DefaultInputHandler(final DefaultInputHandler copy) {
        inputActions = copy.inputActions;
        bindings = copy.bindings;
    }

}
