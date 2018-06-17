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

package net.sf.gridarta.gui.dialog.errorview;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog displaying a tree of error messages.
 * @author Andreas Kirschbaum
 */
public class DefaultErrorView implements ErrorView {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link DefaultTreeModel} used for the error dialog.
     */
    @NotNull
    private final DefaultTreeModel treeModel = new DefaultTreeModel(null);

    /**
     * The tree of defined objects.
     */
    @NotNull
    private final DefaultMutableTreeNode treeRoot = new ErrorEntry(treeModel, "/", true);

    /**
     * Maps category name to child node.
     */
    @NotNull
    private final Map<ErrorViewCategory, ErrorEntry> categories = new EnumMap<ErrorViewCategory, ErrorEntry>(ErrorViewCategory.class);

    /**
     * The parent component for showing the error dialog or <code>null</code>.
     */
    @Nullable
    private final Component parent;

    /**
     * Whether at least one error message has been added.
     */
    private boolean errors;

    /**
     * The dialog instance. Set to <code>null</code> until created.
     */
    @Nullable
    private Dialog dialog;

    /**
     * The synchronization object used to access {@link #dialog}.
     */
    @NotNull
    private final Object dialogSync = new Object();

    /**
     * The "ok" button of the error dialog. Set to <code>null</code> until
     * created.
     */
    @Nullable
    private JButton okButton;

    /**
     * The {@link Semaphore} used to wait for the dismissal of the error {@link
     * #dialog}.
     */
    @NotNull
    private final Semaphore semaphore = new Semaphore(0);

    /**
     * Creates a new instance.
     * @param parent the parent component for showing the error dialog or
     * <code>null</code>
     */
    public DefaultErrorView(@Nullable final Component parent) {
        treeModel.setRoot(treeRoot);
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError(@NotNull final ErrorViewCategory categoryName, @NotNull final String message) {
        addEntry(categoryName, message);
        errors = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError(@NotNull final ErrorViewCategory categoryName, final int lineNo, @NotNull final String message) {
        addEntry(categoryName, lineNo + ": " + message);
        errors = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWarning(@NotNull final ErrorViewCategory categoryName, @NotNull final String message) {
        addEntry(categoryName, "Warning: " + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWarning(@NotNull final ErrorViewCategory categoryName, final int lineNo, @NotNull final String message) {
        addEntry(categoryName, "Warning: line " + lineNo + ": " + message);
    }

    /**
     * Adds a text message.
     * @param categoryName the category to add to
     * @param message the message to add
     */
    private void addEntry(final ErrorViewCategory categoryName, final String message) {
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                final ErrorEntry category = getCategory(categoryName);
                final ErrorEntry errorEntry = new ErrorEntry(treeModel, message, false);
                category.add(errorEntry);
                showDialog(category, errorEntry);
            }

        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (final InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } catch (final InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Returns the node for a category name.
     * @param categoryName the category name
     * @return the node
     */
    @NotNull
    private ErrorEntry getCategory(@NotNull final ErrorViewCategory categoryName) {
        final ErrorEntry existingErrorEntry = categories.get(categoryName);
        if (existingErrorEntry != null) {
            return existingErrorEntry;
        }

        final ErrorEntry category = new ErrorEntry(treeModel, categoryName.toString(), true);
        categories.put(categoryName, category);
        treeRoot.add(category);
        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasErrors() {
        return errors;
    }

    /**
     * Shows a dialog showing the object tree. Returns after the dialog has been
     * dismissed.
     * @param category the category node
     * @param errorEntry the error node
     */
    private void showDialog(@NotNull final ErrorEntry category, @NotNull final ErrorEntry errorEntry) {
        synchronized (dialogSync) {
            if (dialog == null) {
                showDialogInt(category, errorEntry);
            }
        }
    }

    /**
     * Shows a dialog showing the object tree. Returns after the dialog has been
     * created.
     * @param category the category node
     * @param errorEntry the error node
     */
    private void showDialogInt(@NotNull final ErrorEntry category, @NotNull final ErrorEntry errorEntry) {
        final JTree tree = new JTree(treeModel);
        final JScrollPane scrollPane = new JScrollPane(tree);
        tree.setExpandsSelectedPaths(true);
        final TreePath path = new TreePath(new Object[] { treeRoot, category, errorEntry, });
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
        okButton = new JButton(ACTION_BUILDER.createAction(false, "errorViewOk", this));
        final JOptionPane pane = new JOptionPane(scrollPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] { okButton, }, okButton) {

            /** The serial version UID. */
            private static final long serialVersionUID = 1L;

            @Override
            public void setValue(@Nullable final Object newValue) {
                super.setValue(newValue);
                if (newValue != UNINITIALIZED_VALUE) {
                    errorViewOk();
                }
            }

        };
        pane.setPreferredSize(new Dimension(800, 600));
        assert okButton != null;
        okButton.setEnabled(false);
        dialog = pane.createDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "errorViewTitle"));
        pane.selectInitialValue();
        assert dialog != null;
        dialog.pack();
        assert dialog != null;
        dialog.setModal(false);
        assert dialog != null;
        dialog.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitDialog() throws InterruptedException {
        if (hasDialog()) {
            final Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    assert okButton != null;
                    okButton.setEnabled(true);
                }

            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(runnable);
                } catch (final InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }

            semaphore.acquire();
        }

        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    /**
     * Action method for "ok" button of error dialog.
     */
    public void errorViewOk() {
        synchronized (dialogSync) {
            if (dialog != null) {
                try {
                    dialog.dispose();
                    dialog = null;
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    /**
     * Returns whether the {@link #dialog} has been created.
     * @return whether the dialog has been created
     */
    private boolean hasDialog() {
        synchronized (dialogSync) {
            return dialog != null;
        }
    }

}
