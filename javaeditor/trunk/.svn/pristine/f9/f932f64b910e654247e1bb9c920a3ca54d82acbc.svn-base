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

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.textedit.scripteditor.Actions;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import org.jetbrains.annotations.NotNull;

/**
 * @author Slava Pestov
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class InputActions {

    /**
     * If this client property is set to Boolean.TRUE on the text area, the
     * home/end keys will support 'smart' BRIEF-like behaviour (one press =
     * start/end of line, two presses = start/end of view screen, three presses
     * = start/end of document). By default, this property is not set.
     */
    public static final String SMART_HOME_END_PROPERTY = "InputHandler.homeEnd";

    public final ActionListener backspace = new Backspace();

    public final ActionListener backspaceWord = new BackspaceWord();

    public final ActionListener delete = new Delete();

    public final ActionListener deleteWord = new DeleteWord();

    public final ActionListener end = new End(false);

    public final ActionListener documentEnd = new DocumentEnd(false);

    public final ActionListener selectEnd = new End(true);

    public final ActionListener selectDocEnd = new DocumentEnd(true);

    public final ActionListener insertBreak = new InsertBreak();

    public final ActionListener insertTab = new InsertTab();

    public final ActionListener home = new Home(false);

    public final ActionListener documentHome = new DocumentHome(false);

    public final ActionListener selectHome = new Home(true);

    public final ActionListener selectDocHome = new DocumentHome(true);

    public final ActionListener nextChar = new NextChar(false);

    public final ActionListener nextLine = new NextLine(false);

    public final ActionListener nextPage = new NextPage(false);

    public final ActionListener nextWord = new NextWord(false);

    public final ActionListener selectNextChar = new NextChar(true);

    public final ActionListener selectNextLine = new NextLine(true);

    public final ActionListener selectNextPage = new NextPage(true);

    public final ActionListener selectNextWord = new NextWord(true);

    public final ActionListener overwrite = new Overwrite();

    public final ActionListener prevChar = new PrevChar(false);

    public final ActionListener prevLine = new PrevLine(false);

    public final ActionListener prevPage = new PrevPage(false);

    public final ActionListener prevWord = new PrevWord(false);

    public final ActionListener selectPrevChar = new PrevChar(true);

    public final ActionListener selectPrevLine = new PrevLine(true);

    public final ActionListener selectPrevPage = new PrevPage(true);

    public final ActionListener selectPrevWord = new PrevWord(true);

    public final ActionListener repeat = new Repeat();

    public final ActionListener toggleRectangle = new ToggleRectangle();

    public final ActionListener functionMenu;

    // Default action

    public final ActionListener insertChar = new InsertChar();

    private final Map<String, ActionListener> actions = new HashMap<String, ActionListener>();

    public InputActions(@NotNull final ScriptEditControl scriptEditControl) {
        final ActionListener save = new Save(scriptEditControl);
        functionMenu = new FunctionMenu(scriptEditControl);
        actions.put("backspace", backspace);
        actions.put("backspace-word", backspaceWord);
        actions.put("delete", delete);
        actions.put("delete-word", deleteWord);
        actions.put("end", end);
        actions.put("select-end", selectEnd);
        actions.put("document-end", documentEnd);
        actions.put("select-doc-end", selectDocEnd);
        actions.put("insert-break", insertBreak);
        actions.put("insert-tab", insertTab);
        actions.put("home", home);
        actions.put("select-home", selectHome);
        actions.put("document-home", documentHome);
        actions.put("select-doc-home", selectDocHome);
        actions.put("next-char", nextChar);
        actions.put("next-line", nextLine);
        actions.put("next-page", nextPage);
        actions.put("next-word", nextWord);
        actions.put("select-next-char", selectNextChar);
        actions.put("select-next-line", selectNextLine);
        actions.put("select-next-page", selectNextPage);
        actions.put("select-next-word", selectNextWord);
        actions.put("overwrite", overwrite);
        actions.put("prev-char", prevChar);
        actions.put("prev-line", prevLine);
        actions.put("prev-page", prevPage);
        actions.put("prev-word", prevWord);
        actions.put("select-prev-char", selectPrevChar);
        actions.put("select-prev-line", selectPrevLine);
        actions.put("select-prev-page", selectPrevPage);
        actions.put("select-prev-word", selectPrevWord);
        actions.put("repeat", repeat);
        actions.put("toggle-rect", toggleRectangle);
        actions.put("insert-char", insertChar);

        actions.put("copy", Actions.COPY);
        actions.put("cut", Actions.CUT);
        actions.put("paste", Actions.PASTE);

        actions.put("save", save);
        actions.put("find", Actions.FIND);
        actions.put("find-again", Actions.FIND_AGAIN);
        actions.put("replace", Actions.REPLACE);
    }

    /**
     * Returns a named text area action.
     * @param name the action name
     * @return action stored for <var>name</var>
     * @throws IllegalArgumentException if no such action was found
     */
    @NotNull
    public ActionListener getAction(final String name) {
        final ActionListener action = actions.get(name);
        if (action != null) {
            return action;
        } else {
            throw new IllegalArgumentException("No action for " + name);
        }
    }

}
