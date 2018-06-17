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

import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements undo and redo actions.
 * @author Andreas Kirschbaum
 */
public class ScriptEditUndoActions {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Action called for "undo".
     */
    private final Action aUndo = ACTION_BUILDER.createAction(true, "scriptEditUndo", this);

    /**
     * Action called for "redo".
     */
    private final Action aRedo = ACTION_BUILDER.createAction(true, "scriptEditRedo", this);

    /**
     * Records an {@link UndoManager} for each known {@link Document}.
     */
    private final Map<Document, UndoManager> undo = new HashMap<Document, UndoManager>();

    /**
     * The currently active document.
     */
    private Document currentDocument;

    /**
     * The {@link UndoableEditListener} to detect document changes. The same
     * instance is used for all documents.
     */
    private final UndoableEditListener undoableEditListener = new UndoableEditListener() {

        @Override
        public void undoableEditHappened(final UndoableEditEvent e) {
            getUndoManager((Document) e.getSource()).addEdit(e.getEdit());
            refresh();
        }

    };

    /**
     * Adds a document.
     * @param document the document to add
     */
    public void addDocument(@NotNull final Document document) {
        assert !undo.containsKey(document);
        document.addUndoableEditListener(undoableEditListener);
        undo.put(document, new UndoManager());
    }

    /**
     * Removes a document.
     * @param document the document to remove
     */
    public void removeDocument(@NotNull final Document document) {
        assert undo.containsKey(document);
        undo.remove(document);
        document.removeUndoableEditListener(undoableEditListener);
        if (currentDocument == document) {
            setCurrentDocument(null);
        }
    }

    /**
     * Forget all undo-able operations for a document.
     * @param document The document.
     */
    public void resetUndo(@NotNull final Document document) {
        getUndoManager(document).discardAllEdits();
    }

    /**
     * Action method for "undo".
     */
    @ActionMethod
    public void scriptEditUndo() {
        final UndoManager undoManager = getCurrentUndoManager();
        try {
            if (undoManager != null && undoManager.canUndo()) {
                undoManager.undo();
                refresh();
            }
        } catch (final CannotUndoException ignored) {
            // ignore
        }
    }

    /**
     * Action method for "redo".
     */
    @ActionMethod
    public void scriptEditRedo() {
        final UndoManager undoManager = getCurrentUndoManager();
        try {
            if (undoManager != null && undoManager.canRedo()) {
                undoManager.redo();
                refresh();
            }
        } catch (final CannotRedoException ignored) {
        }
    }

    /**
     * Refreshes menu actions.
     */
    private void refresh() {
        final UndoableEdit undoManager = getCurrentUndoManager();

        final boolean canUndo = undoManager != null && undoManager.canUndo();
        aUndo.setEnabled(canUndo);
        if (canUndo) {
            aUndo.putValue(Action.NAME, ACTION_BUILDER.format("scriptEditUndo.name", undoManager.getUndoPresentationName()));
        } else {
            aUndo.putValue(Action.NAME, ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEditUndo.text"));
        }

        final boolean canRedo = undoManager != null && undoManager.canRedo();
        aRedo.setEnabled(canRedo);
        if (canRedo) {
            aRedo.putValue(Action.NAME, ACTION_BUILDER.format("scriptEditRedo.name", undoManager.getRedoPresentationName()));
        } else {
            aRedo.putValue(Action.NAME, ActionBuilderUtils.getString(ACTION_BUILDER, "scriptEditRedo.text"));
        }
    }

    /**
     * Sets the current document. Undo, redo and menu states use this document.
     * @param currentDocument the current document to set
     */
    public void setCurrentDocument(@Nullable final Document currentDocument) {
        if (this.currentDocument != currentDocument) {
            this.currentDocument = currentDocument;
            refresh();
        }
    }

    /**
     * Returns the undo manager for a document.
     * @param document the document
     * @return the undo manager for <code>document</code>
     */
    @NotNull
    private UndoManager getUndoManager(@NotNull final Document document) {
        final UndoManager result = undo.get(document);
        assert result != null;
        return result;
    }

    /**
     * Returns the undo manager for the current document.
     * @return the undo manager for the current document, or <code>null</code>
     *         if no current document exists
     */
    @Nullable
    private UndoManager getCurrentUndoManager() {
        return currentDocument != null ? getUndoManager(currentDocument) : null;
    }

}
