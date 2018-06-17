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

package net.sf.gridarta.gui.panel.archetypechooser;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.gridarta.gui.utils.GList;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserFolder;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserFolderListener;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserPanel;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserPanelListener;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.CommonConstants;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A panel showing a set of archetypes.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypePanel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The associated {@link ArchetypeChooserPanel}.
     * @serial
     */
    @NotNull
    private final ArchetypeChooserPanel<G, A, R> archetypeChooserPanel;

    /**
     * The popup menu for the archetype lists to bring up the editor.
     * @serial
     */
    // This looks unused, but don't remove it. It will be used in future.
    @NotNull
    private final JPopupMenu popupMenu = createListPopupMenu();

    /**
     * The list model of {@link #archetypeList}.
     * @serial
     */
    @NotNull
    private final DefaultListModel archetypeListModel = new DefaultListModel();

    /**
     * The list of currently shown archetypes; it is updated each time a new
     * folder is selected.
     * @serial
     */
    @NotNull
    private final GList<R> archetypeList = new GList<R>(archetypeListModel);

    /**
     * The currently shown {@link ArchetypeChooserFolder}.
     * @serial
     */
    @Nullable
    private ArchetypeChooserFolder<G, A, R> archetypeListFolder;

    /**
     * The combo box for selecting the active archetype panel.
     * @serial
     */
    @NotNull
    private final JComboBox folders = new JComboBox();

    /**
     * The comparator for sorting archetypes in the active panel.
     * @serial
     */
    @NotNull
    private Comparator<Archetype<G, A, R>> displayMode;

    /**
     * Set while {@link #updateArchetypeList()} is running. Prevents resetting
     * the selected archetype during {@link #archetypeList} rebuild.
     * @serial
     */
    private boolean updateInProgress;

    /**
     * The {@link ArchetypeChooserFolderListener} attached to {@link
     * #archetypeListFolder} to track changes to the selected archetype.
     */
    @NotNull
    private final ArchetypeChooserFolderListener<G, A, R> archetypeChooserFolderListener = new ArchetypeChooserFolderListener<G, A, R>() {

        @Override
        public void selectedArchetypeChanged(@Nullable final R selectedArchetype) {
            setSelectedArchetype(selectedArchetype);
        }

    };

    /**
     * Creates a new instance.
     * @param archetypeChooserView the associated archetype chooser view
     * @param archetypeChooserPanel the associated archetype chooser panel
     */
    public ArchetypePanel(@NotNull final ArchetypeChooserView<G, A, R> archetypeChooserView, @NotNull final ArchetypeChooserPanel<G, A, R> archetypeChooserPanel) {
        super(new BorderLayout());
        this.archetypeChooserPanel = archetypeChooserPanel;
        archetypeList.setFocusable(false);
        archetypeList.setBackground(CommonConstants.BG_COLOR);
        archetypeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(archetypeList);
        add(scrollPane, BorderLayout.CENTER);
        add(folders, BorderLayout.NORTH);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        folders.setAutoscrolls(true);

        final ArchetypeChooserViewListener<G, A, R> archetypeChooserViewListener = new ArchetypeChooserViewListener<G, A, R>() {

            @Override
            public void displayModeChanged(@NotNull final DisplayMode<G, A, R> displayMode) {
                updateCellRenderer(displayMode);
            }

        };
        archetypeChooserView.addArchetypeChooserViewListener(archetypeChooserViewListener);
        updateCellRenderer(archetypeChooserView.getDisplayMode());

        final ArchetypeChooserPanelListener<G, A, R> archetypeChooserPanelListener = new ArchetypeChooserPanelListener<G, A, R>() {

            @Override
            public void selectedFolderChanged(@NotNull final ArchetypeChooserFolder<G, A, R> selectedFolder) {
                folders.setSelectedItem(selectedFolder);
                updateArchetypeList();
            }

            @Override
            public void selectedArchetypeChanged(@Nullable final R selectedArchetype) {
                setSelectedArchetype(selectedArchetype);
            }

        };

        archetypeList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(@NotNull final ListSelectionEvent e) {
                if (!updateInProgress) {
                    final ArchetypeChooserFolder<G, A, R> folder = archetypeListFolder;
                    if (folder != null) {
                        folder.setSelectedArchetype(getSelectedArchetype());
                    }
                }
            }

        });

        archetypeChooserPanel.addArchetypeChooserPanelListener(archetypeChooserPanelListener);

        for (final ArchetypeChooserFolder<G, A, R> folder : archetypeChooserPanel.getFolders()) {
            folders.addItem(folder);
        }
        folders.setRenderer(new FolderListCellRenderer());
        folders.setSelectedItem(archetypeChooserPanel.getSelectedFolder());
        folders.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final ArchetypeChooserFolder<G, A, R> folder = getSelectedFolder();
                if (folder != null) {
                    ArchetypePanel.this.archetypeChooserPanel.setSelectedFolder(folder);
                }
            }

        });

        updateArchetypeList();
    }

    /**
     * Updates the cell renderer state.
     * @param displayMode the new display mode
     */
    private void updateCellRenderer(@NotNull final DisplayMode<G, A, R> displayMode) {
        this.displayMode = displayMode;
        archetypeList.setCellRenderer(displayMode);
        archetypeList.setLayoutOrientation(displayMode.getLayoutOrientation());
        archetypeList.setVisibleRowCount(-1);
        final R selectedValue = getSelectedArchetype();
        updateArchetypeList();
        setSelectedArchetype(selectedValue);
    }

    /**
     * Returns the archetype currently selected in the list.
     * @return the archetype or <code>null</code> if no selection exists
     */
    @Nullable
    public R getSelectedArchetype() {
        return archetypeList.getSelectedValue();
    }

    /**
     * Sets the currently selected archetype in the list. Makes sure the
     * archetype is visible.
     * @param archetype the selected archetype or <code>null</code> to deselect
     * all
     */
    private void setSelectedArchetype(@Nullable final R archetype) {
        archetypeList.setSelectedValue(archetype, true);
        archetypeList.ensureIndexIsVisible(archetypeList.getSelectedIndex());
    }

    /**
     * Returns the selected folder in {@link #folders}.
     * @return the selected folder or <code>null</code> if none is selected
     */
    @Nullable
    private ArchetypeChooserFolder<G, A, R> getSelectedFolder() {
        //JComboBox does not use type parameters
        @SuppressWarnings("unchecked")
        final ArchetypeChooserFolder<G, A, R> selectedFolder = (ArchetypeChooserFolder<G, A, R>) folders.getSelectedItem();
        return selectedFolder;
    }

    /**
     * Updates {@link #archetypeList} to contain the currently selected folder.
     */
    private void updateArchetypeList() {
        synchronized (archetypeList.getTreeLock()) {
            final ArchetypeChooserFolder<G, A, R> folder = getSelectedFolder();
            if (archetypeListFolder != folder) {
                try {
                    updateInProgress = true;
                    archetypeListModel.removeAllElements();
                    if (folder != null) {
                        final List<R> archetypes = new ArrayList<R>();
                        archetypes.addAll(folder.getArchetypes());
                        Collections.sort(archetypes, displayMode);
                        for (final R archetype : archetypes) {
                            archetypeListModel.addElement(archetype);
                        }
                        if (archetypeListFolder != null) {
                            final R oldSelectedArchetype = archetypeListFolder.getSelectedArchetype();
                            if (oldSelectedArchetype != null && folder.containsArchetype(oldSelectedArchetype)) {
                                folder.setSelectedArchetype(oldSelectedArchetype); // retain selected archetype when switching from or to "all"
                            }
                        }
                        setSelectedArchetype(folder.getSelectedArchetype());
                    }
                    if (archetypeListFolder != null) {
                        archetypeListFolder.removeArchetypeChooserFolderListener(archetypeChooserFolderListener);
                    }
                    archetypeListFolder = folder;
                    if (archetypeListFolder != null) {
                        archetypeListFolder.addArchetypeChooserFolderListener(archetypeChooserFolderListener);
                    }
                } finally {
                    updateInProgress = false;
                }
            }
        }
    }

    /**
     * Creates the popup menu for the archetype lists to bring up the editor.
     * @return the popup menu
     */
    @NotNull
    private JPopupMenu createListPopupMenu() {
        final JPopupMenu menu = new JPopupMenu();
        menu.add(ACTION_BUILDER.createAction(false, "editPopup", this));
        return menu;
    }

    /**
     * Action method for the popup menu to edit a default arch.
     */
    @ActionMethod
    public void editPopup() {
        //XXX:        gameObjectAttributesDialogFactory.showAttributeDialog(getSelectedArchetype());
    }

    /**
     * Selects an archetype. If necessary, switches to the correct sub-folder.
     * @param archetype the archetype to select
     */
    public void selectArchetype(@NotNull final R archetype) {
        final List<ArchetypeChooserFolder<G, A, R>> archetypeChooserFolders = archetypeChooserPanel.getFolders();
        for (int index = 1; index < archetypeChooserFolders.size(); index++) {
            final ArchetypeChooserFolder<G, A, R> folder = archetypeChooserFolders.get(index);
            if (folder.containsArchetype(archetype)) {
                folders.setSelectedIndex(index);
                setSelectedArchetype(archetype);
                return;
            }
        }

    }

    /**
     * Returns the associated {@link ArchetypeChooserPanel}.
     * @return the associated archetype chooser panel
     */
    @NotNull
    public ArchetypeChooserPanel<G, A, R> getArchetypeChooserPanel() {
        return archetypeChooserPanel;
    }

}
