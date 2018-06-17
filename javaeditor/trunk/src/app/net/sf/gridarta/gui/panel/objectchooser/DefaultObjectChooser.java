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

package net.sf.gridarta.gui.panel.objectchooser;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.mapfiles.MapFile;
import net.sf.gridarta.gui.panel.objectchoicedisplay.ObjectChoiceDisplay;
import net.sf.gridarta.gui.panel.pickmapchooser.PickmapChooserModel;
import net.sf.gridarta.gui.panel.pickmapchooser.PickmapChooserModelListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserFolder;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModelListener;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserPanel;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The object Chooser implementation.
 * @author Andreas Kirschbaum
 */
public class DefaultObjectChooser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel implements ObjectChooser<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The pickmap chooser control.
     */
    private final ObjectChooserTab<G, A, R> pickmapChooserControl;

    /**
     * The {@link ArchetypeChooserModel}.
     */
    @NotNull
    private final ArchetypeChooserModel<G, A, R> archetypeChooserModel;

    /**
     * The {@link ObjectChoiceDisplay} that display information about the
     * currently selected object.
     */
    @NotNull
    private final ObjectChoiceDisplay objectChoiceDisplay;

    /**
     * Panel holding both archetype chooser and pickmap chooser.
     */
    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);

    /**
     * The tabs in the same order as {@link #tabbedPane}.
     */
    private final List<ObjectChooserTab<G, A, R>> tabs = new ArrayList<ObjectChooserTab<G, A, R>>();

    /**
     * The registered listeners.
     */
    private final Collection<ObjectChooserListener<G, A, R>> listeners = new ArrayList<ObjectChooserListener<G, A, R>>();

    /**
     * The active tab or <code>null</code> if no tab exists.
     */
    @Nullable
    private ObjectChooserTab<G, A, R> activeTab;

    /**
     * The last known active {@link MapFile}.
     */
    @Nullable
    private MapFile<G, A, R> activeMapFile;

    /**
     * The {@link MapView} of {@link #activeMapFile}.
     */
    @Nullable
    private MapView<G, A, R> activePickmapView;

    /**
     * The last reported selection.
     */
    @Nullable
    private BaseObject<G, A, R, ?> cursorSelection;

    /**
     * The current state of selection.
     */
    private int selectedIndex = -2;

    /**
     * The map cursor listener attached to {@link #activePickmapView}.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            updatePickmapInfo(activePickmapView == null ? null : activePickmapView.getMapCursor());
        }

        @Override
        public void mapCursorChangedMode() {
            updatePickmapInfo(activePickmapView == null ? null : activePickmapView.getMapCursor());
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param archetypeChooserControl the archetype chooser control
     * @param pickmapChooserControl the pickmap chooser control
     * @param archetypeChooserModel the archetype chooser model
     * @param pickmapChooserModel the pickmap chooser model
     * @param archetypeTypeSet the archetype type set
     */
    public DefaultObjectChooser(@NotNull final ObjectChooserTab<G, A, R> archetypeChooserControl, @NotNull final ObjectChooserTab<G, A, R> pickmapChooserControl, @NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull final PickmapChooserModel<G, A, R> pickmapChooserModel, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        super(new BorderLayout());
        this.pickmapChooserControl = pickmapChooserControl;
        this.archetypeChooserModel = archetypeChooserModel;
        objectChoiceDisplay = new ObjectChoiceDisplay(archetypeTypeSet);

        cursorSelection = getSelection();

        addTab(archetypeChooserControl);
        addTab(pickmapChooserControl);

        final ChangeListener changeListener = new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                updateSelectedIndex();
            }

        };
        tabbedPane.addChangeListener(changeListener);

        updateSelectedIndex();
        add(tabbedPane, BorderLayout.CENTER);
        add(objectChoiceDisplay, BorderLayout.SOUTH);

        final ArchetypeChooserModelListener<G, A, R> archetypeChooserModelListener = new ArchetypeChooserModelListener<G, A, R>() {

            @Override
            public void selectedPanelChanged(@NotNull final ArchetypeChooserPanel<G, A, R> selectedPanel) {
                // ignore
            }

            @Override
            public void selectedFolderChanged(@NotNull final ArchetypeChooserFolder<G, A, R> selectedFolder) {
                // ignore
            }

            @Override
            public void selectedArchetypeChanged(@Nullable final R selectedArchetype) {
                if (!isPickmapActive()) {
                    fireSelectionChanged(selectedArchetype);
                }
            }

            @Override
            public void directionChanged(@Nullable final Integer direction) {
                // ignore
            }

        };
        archetypeChooserModel.addArchetypeChooserModelListener(archetypeChooserModelListener);

        final PickmapChooserModelListener<G, A, R> pickmapChooserModelListener = new PickmapChooserModelListener<G, A, R>() {

            @Override
            public void activePickmapChanged(@Nullable final MapFile<G, A, R> mapFile) {
                if (activeMapFile != mapFile) { // ignore non-changes
                    updateActivePickmap(mapFile);
                }
            }

            @Override
            public void pickmapReverted(@NotNull final MapFile<G, A, R> mapFile) {
                if (activeMapFile == mapFile) { // ignore unless active pickmap was reverted
                    updateActivePickmap(mapFile);
                }
            }

            @Override
            public void pickmapModifiedChanged(final int index, final MapFile<G, A, R> mapFile) {
                // ignore
            }

        };
        pickmapChooserModel.addPickmapChooserListener(pickmapChooserModelListener);

        objectChoiceDisplay.showObjectChooserQuickObject(cursorSelection, isPickmapActive());
    }

    /**
     * Adds a tab.
     * @param tab the tab to add
     */
    private void addTab(final ObjectChooserTab<G, A, R> tab) {
        tabs.add(tab);
        tabbedPane.addTab(tab.getTitle(), tab.getComponent());
    }

    /**
     * Records whether the archetype chooser or the pickmap chooser is active.
     * @param index the active tab index
     */
    private void setActiveTab(final int index) {
        @Nullable ObjectChooserTab<G, A, R> tab;
        try {
            tab = tabs.get(index);
        } catch (final IndexOutOfBoundsException ignored) {
            tab = null;
        }
        if (activeTab == tab) {
            return;
        }

        if (activeTab != null) {
            activeTab.setActive(false);
        }
        activeTab = tab;
        if (activeTab != null) {
            activeTab.setActive(true);
        }
        for (final ObjectChooserListener<G, A, R> listener : listeners) {
            listener.pickmapActiveChanged(isPickmapActive());
        }
        if (isPickmapActive()) {
            updatePickmapInfo(activePickmapView == null ? null : activePickmapView.getMapCursor());
        } else {
            final ArchetypeChooserPanel<G, A, R> selectedPanel = archetypeChooserModel.getSelectedPanel();
            fireSelectionChanged(selectedPanel == null ? null : selectedPanel.getSelectedFolder().getSelectedArchetype());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isPickmapActive() {
        return activeTab == pickmapChooserControl;
    }

    /**
     * Returns whether a given game object matches the selection.
     * @param gameObject the game object to check
     * @return whether the game object matches
     */
    @Override
    public boolean isMatching(@NotNull final G gameObject) {
        return activeTab != null && activeTab.isMatching(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObjectChooserListener(@NotNull final ObjectChooserListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObjectChooserListener(@NotNull final ObjectChooserListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveArchetypeChooserToFront() {
        tabbedPane.setSelectedIndex(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePickmapChooserToFront() {
        tabbedPane.setSelectedIndex(1);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public BaseObject<G, A, R, ?> getCursorSelection() {
        return cursorSelection;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public final BaseObject<G, A, R, ?> getSelection() {
        return activeTab == null ? null : activeTab.getSelection();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<? extends BaseObject<G, A, R, ?>> getSelections() {
        return activeTab == null ? Collections.<G>emptyList() : activeTab.getSelections();
    }

    /**
     * Updates {@link #selectedIndex}. Does nothing if the value didn't change.
     */
    private void updateSelectedIndex() {
        final int index = tabbedPane.getSelectedIndex();
        if (index != selectedIndex) {
            selectedIndex = index;
            setActiveTab(index);
        }
    }

    /**
     * Records an active pickmap.
     * @param mapFile the pickmap
     */
    private void updateActivePickmap(@Nullable final MapFile<G, A, R> mapFile) {
        if (activePickmapView != null) {
            activePickmapView.getMapCursor().removeMapCursorListener(mapCursorListener);
        }
        activeMapFile = mapFile;
        updateActivePickmap();
        if (activePickmapView != null) {
            activePickmapView.getMapCursor().addMapCursorListener(mapCursorListener);
        }
    }

    /**
     * Updates the display state after {@link #activeMapFile} has changed.
     */
    private void updateActivePickmap() {
        activePickmapView = activeMapFile != null ? activeMapFile.getMapView() : null;
        updatePickmapInfo(activePickmapView == null ? null : activePickmapView.getMapCursor());
    }

    /**
     * Updates the display information for the active pickmap.
     * @param mapCursor the map cursor or <code>null</code>
     */
    private void updatePickmapInfo(@Nullable final MapCursor<G, A, R> mapCursor) {
        if (isPickmapActive()) {
            @Nullable final BaseObject<G, A, R, ?> gameObject;
            if (mapCursor == null) {
                gameObject = null;
            } else {
                final Point location = mapCursor.getLocation();
                if (location == null) {
                    gameObject = null;
                } else if (activeMapFile == null) {
                    gameObject = null;
                } else {
                    final MapControl<G, A, R> pickmap = activeMapFile.getPickmap();
                    if (pickmap == null) {
                        gameObject = null;
                    } else {
                        final MapModel<G, A, R> mapModel = pickmap.getMapModel();
                        gameObject = mapModel.getMapSquare(location).getFirst();
                    }
                }
            }
            fireSelectionChanged(gameObject);
        }
    }

    /**
     * Notifies all listeners that the selection may have changed.
     * @param gameObject the selected game object
     */
    private void fireSelectionChanged(@Nullable final BaseObject<G, A, R, ?> gameObject) {
        if (cursorSelection == gameObject) {
            return;
        }
        cursorSelection = gameObject;

        for (final ObjectChooserListener<G, A, R> listener : listeners) {
            listener.selectionChanged(gameObject);
        }

        objectChoiceDisplay.showObjectChooserQuickObject(gameObject, isPickmapActive());
    }

}
