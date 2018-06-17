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

package net.sf.gridarta.textedit.scripteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import net.sf.gridarta.textedit.textarea.actions.Copy;
import net.sf.gridarta.textedit.textarea.actions.Cut;
import net.sf.gridarta.textedit.textarea.actions.Find;
import net.sf.gridarta.textedit.textarea.actions.FindAgain;
import net.sf.gridarta.textedit.textarea.actions.Paste;
import net.sf.gridarta.textedit.textarea.actions.Replace;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Actions used by the script editor.
 * @author Andreas Kirschbaum
 */
public class Actions {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    public static final ActionListener COPY = new Copy();

    public static final ActionListener CUT = new Cut();

    public static final ActionListener PASTE = new Paste();

    /**
     * The "find" action listener.
     */
    public static final Find FIND = new Find();

    /**
     * The "replace" action listener.
     */
    public static final Replace REPLACE = new Replace();

    /**
     * The "find again" action listener.
     */
    public static final FindAgain FIND_AGAIN = new FindAgain(FIND, REPLACE);

    /**
     * Action called for "new script".
     */
    private final Action aNewScript = ACTION_BUILDER.createAction(true, "scriptEditNewScript", this);

    /**
     * Action called for "open".
     */
    private final Action aOpen = ACTION_BUILDER.createAction(true, "scriptEditOpen", this);

    /**
     * Action called for "save as".
     */
    private final Action aSaveAs = ACTION_BUILDER.createAction(true, "scriptEditSaveAs", this);

    /**
     * Action called for "save".
     */
    private final Action aSave = ACTION_BUILDER.createAction(true, "scriptEditSave", this);

    /**
     * Action called for "close".
     */
    private final Action aClose = ACTION_BUILDER.createAction(true, "scriptEditClose", this);

    /**
     * Action called for "close".
     */
    private final Action aCloseAll = ACTION_BUILDER.createAction(true, "scriptEditCloseAll", this);

    /**
     * Action called for "copy".
     */
    private final Action aCopy = ACTION_BUILDER.createAction(true, "scriptEditCopy", this);

    /**
     * Action called for "cut".
     */
    private final Action aCut = ACTION_BUILDER.createAction(true, "scriptEditCut", this);

    /**
     * Action called for "paste".
     */
    private final Action aPaste = ACTION_BUILDER.createAction(true, "scriptEditPaste", this);

    /**
     * Action called for "find".
     */
    private final Action aFind = ACTION_BUILDER.createAction(true, "scriptEditFind", this);

    /**
     * Action called for "replace".
     */
    private final Action aReplace = ACTION_BUILDER.createAction(true, "scriptEditReplace", this);

    /**
     * Action called for "find again".
     */
    private final Action aFindAgain = ACTION_BUILDER.createAction(true, "scriptEditFindAgain", this);

    /**
     * The controller to forward to.
     */
    @NotNull
    private final ScriptEditControl control;

    /**
     * Create a new instance.
     * @param control The controller to forward to.
     */
    public Actions(@NotNull final ScriptEditControl control) {
        this.control = control;
    }

    /**
     * Action for "new script".
     */
    @ActionMethod
    public void scriptEditNewScript() {
        control.newScript();
    }

    /**
     * Action for "open".
     */
    @ActionMethod
    public void scriptEditOpen() {
        control.openUser();
    }

    /**
     * Action for "save as".
     */
    @ActionMethod
    public void scriptEditSaveAs() {
        control.saveAsActiveTab();
    }

    /**
     * Action for "save".
     */
    @ActionMethod
    public void scriptEditSave() {
        control.saveActiveTab();
    }

    /**
     * Action for "close".
     */
    @ActionMethod
    public void scriptEditClose() {
        control.closeActiveTab();
    }

    /**
     * Action for "close all".
     */
    @ActionMethod
    public void scriptEditCloseAll() {
        control.closeAllTabs();
    }

    /**
     * Action for "cut".
     */
    @ActionMethod
    public void scriptEditCut() {
        executeAction("cut", CUT);
    }

    /**
     * Action for "copy".
     */
    @ActionMethod
    public void scriptEditCopy() {
        executeAction("copy", COPY);
    }

    /**
     * Action for "paste".
     */
    @ActionMethod
    public void scriptEditPaste() {
        executeAction("paste", PASTE);
    }

    /**
     * Action for "find".
     */
    @ActionMethod
    public void scriptEditFind() {
        executeAction("find", FIND);
    }

    /**
     * Action for "replace".
     */
    @ActionMethod
    public void scriptEditReplace() {
        executeAction("replace", REPLACE);
    }

    /**
     * Action for "find again".
     */
    @ActionMethod
    public void scriptEditFindAgain() {
        executeAction("find-again", FIND_AGAIN);
    }

    /**
     * Refreshes the enable/disable state of all menus.
     */
    public void refresh() {
        aSave.setEnabled(control.getActiveFilePath() != null);
    }

    /**
     * Executes a named action.
     * @param actionName the name of the action to execute
     * @param actionListener the action listener to call
     */
    private void executeAction(@NotNull final String actionName, @NotNull final ActionListener actionListener) {
        final JEditTextArea activeTA = control.getActiveTextArea();
        if (activeTA != null) {
            actionListener.actionPerformed(new ActionEvent(activeTA, 0, actionName));
        }
    }

}
