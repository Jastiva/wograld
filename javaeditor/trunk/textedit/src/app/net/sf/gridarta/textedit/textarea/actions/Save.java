/*
 * InputHandler.java - Manages key bindings and executes actions
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import org.jetbrains.annotations.NotNull;

/**
 * Action listener for SAVE actions.
 * @author Andreas Vogl
 */
public class Save implements ActionListener {

    /**
     * The {@link ScriptEditControl} to forwards to.
     */
    @NotNull
    private final ScriptEditControl scriptEditControl;

    /**
     * Creates a new instance.
     * @param scriptEditControl the script edit control to forward to
     */
    public Save(@NotNull final ScriptEditControl scriptEditControl) {
        this.scriptEditControl = scriptEditControl;
    }

    /**
     * {@inheritDoc} Save the currently active tab
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        scriptEditControl.saveActiveTab();
    }

}
