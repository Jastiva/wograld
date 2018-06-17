/*
 * InputHandler.java - Manages key bindings and executes actions
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.JPopupMenu;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An input handler converts the user's key strokes into concrete actions. It
 * also takes care of macro recording and action repetition.<p> <p/> This class
 * provides all the necessary support code for an input handler, but doesn't
 * actually do any key binding logic. It is up to the implementations of this
 * class to do so.
 * @author Slava Pestov
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @see DefaultInputHandler
 */
public abstract class InputHandler extends KeyAdapter {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(InputHandler.class);

    @Nullable
    private ActionListener grabAction;

    private boolean repeat;

    protected int repeatCount;

    /**
     * Adds the default key bindings to this input handler. This should not be
     * called in the constructor of this input handler, because applications
     * might load the key bindings from a file, etc.
     */
    public abstract void addDefaultKeyBindings();

    /**
     * Removes a key binding from this input handler.
     * @param keyBinding the key binding
     */
    public abstract void removeKeyBinding(String keyBinding);

    /**
     * Removes all key bindings from this input handler.
     */
    public abstract void removeAllKeyBindings();

    /**
     * Grabs the next key typed event and invokes the specified action with the
     * key as a the action command.
     * @param listener the action
     */
    public void grabNextKeyStroke(final ActionListener listener) {
        grabAction = listener;
    }

    /**
     * Returns if repetition is enabled. When repetition is enabled, actions
     * will be executed multiple times. This is usually invoked with a special
     * key stroke in the input handler.
     * @return <code>true</code> if repeating is enabled, otherwise
     *         <code>false</code>
     */
    public boolean isRepeatEnabled() {
        return repeat;
    }

    /**
     * Sets the enabled state of repetition. When repetition is enabled, actions
     * will be executed multiple times. Once repeating is enabled, the input
     * handler should read a number from the keyboard.
     * @param repeat <code>true</code> for enabling repetition,
     * <code>false</code> for disabling it
     */
    public void setRepeatEnabled(final boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Returns the number of times the next action will be repeated.
     * @return number of times the next action will be repeated (1 or the set
     *         repeat count)
     */
    public int getRepeatCount() {
        return repeat ? Math.max(1, repeatCount) : 1;
    }

    /**
     * Sets the number of times the next action will be repeated.
     * @param repeatCount the repeat count
     */
    public void setRepeatCount(final int repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
     * Returns a copy of this input handler that shares the same key bindings.
     * Setting key bindings in the copy will also set them in the original.
     * @return copy of this InputHandler
     */
    public abstract InputHandler copy();

    /**
     * Executes the specified action, repeating and recording it as necessary.
     * @param listener the action listener
     * @param source the event source
     * @param actionCommand the action command
     */
    public void executeAction(final ActionListener listener, final Object source, @Nullable final String actionCommand) {
        // create event
        final ActionEvent evt = new ActionEvent(source, ActionEvent.ACTION_PERFORMED, actionCommand);

        // remember old values, in case action changes them
        final boolean repeatBak = repeat;

        // execute the action
        if (listener instanceof NonRepeatable) {
            listener.actionPerformed(evt);
        } else {
            for (int i = 0; i < Math.max(1, repeatCount); i++) {
                listener.actionPerformed(evt);
            }
        }

        // do recording. Notice that we do no recording whatsoever
        // for actions that grab keys
        if (grabAction == null) {
            // If repeat was true originally, clear it
            // Otherwise it might have been set by the action, etc
            if (repeatBak) {
                repeat = false;
                repeatCount = 0;
            }
        }
    }

    /**
     * Returns the text area that fired the specified event. This method will
     * throw an {@link Error} if <var>evt</var> does not have a JEditTextArea in
     * it's source component hierarchy.
     * @param evt the event
     * @return the JEditTextArea found as the source of <var>evt</var>
     */
    @NotNull
    public static JEditTextArea getTextArea(final EventObject evt) {
        if (evt != null) {
            final Object o = evt.getSource();
            if (o instanceof Component) {
                // find the parent text area
                Component c = (Component) o;
                while (true) {
                    if (c instanceof JEditTextArea) {
                        return (JEditTextArea) c;
                    } else if (c == null) {
                        break;
                    }
                    if (c instanceof JPopupMenu) {
                        c = ((JPopupMenu) c).getInvoker();
                    } else {
                        c = c.getParent();
                    }
                }
            }
        }

        // this shouldn't happen
        log.fatal("BUG: getTextArea() returning null");
        log.fatal("Report this to Slava Pestov <sp@gjt.org>");
        assert false : "BUG: getTextArea() returning null";
        throw new Error("BUG: getTextArea() returning null");
    }

    // protected members

    /**
     * If a key is being grabbed, this method should be called with the
     * appropriate key event. It executes the grab action with the typed
     * character as the parameter.
     * @param evt The KeyEvent the key should be grabbed of
     * @return whether a grab action was active
     */
    protected boolean handleGrabAction(final KeyEvent evt) {
        if (grabAction == null) {
            return false;
        }

        // Clear it *before* it is executed so that executeAction()
        // resets the repeat count
        final ActionListener grabAction2 = grabAction;
        grabAction = null;
        executeAction(grabAction2, evt.getSource(), String.valueOf(evt.getKeyChar()));
        return true;
    }

    /**
     * If an action implements this interface, it should not be repeated.
     * Instead, it will handle the repetition itself.
     * @noinspection MarkerInterface, PublicInnerClass
     */
    public interface NonRepeatable {

    }

}
