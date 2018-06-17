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
import java.util.ArrayList;
import java.util.Collection;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserModel;
import net.sf.gridarta.model.archetypechooser.ArchetypeChooserPanel;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.CommonConstants;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The view of the archetype chooser.
 * @author Andreas Kirschbaum
 */
public class ArchetypeChooserView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JPanel {

    /**
     * The key for saving the display mode selection.
     */
    @NotNull
    private static final String DISPLAY_MODE_KEY = "archetypeChooserDisplayMode";

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Preferences.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The registered listeners.
     */
    @NotNull
    private final Collection<ArchetypeChooserViewListener<G, A, R>> listeners = new ArrayList<ArchetypeChooserViewListener<G, A, R>>();

    /**
     * The button for "display game object names".
     * @serial
     */
    @NotNull
    private final AbstractButton buttonDisplayGameObjectNames = new JToggleButton(ActionUtils.newAction(ACTION_BUILDER, "Archetype Chooser", this, "displayGameObjectNames"));

    /**
     * The button for "display archetype names".
     * @serial
     */
    @NotNull
    private final AbstractButton buttonDisplayArchetypeNames = new JToggleButton(ActionUtils.newAction(ACTION_BUILDER, "Archetype Chooser", this, "displayArchetypeNames"));

    /**
     * The button for "display icons only".
     * @serial
     */
    @NotNull
    private final AbstractButton buttonDisplayIconsOnly = new JToggleButton(ActionUtils.newAction(ACTION_BUILDER, "Archetype Chooser", this, "displayIconsOnly"));

    /**
     * The archetype chooser model.
     * @serial
     */
    @NotNull
    private final ArchetypeChooserModel<G, A, R> archetypeChooserModel;

    /**
     * The tab panel containing the archetype lists.
     * @serial
     */
    @NotNull
    private final JTabbedPane tabDesktop = new JTabbedPane(SwingConstants.TOP);

    /**
     * Indicates whether object names are shown.
     * @serial
     */
    @NotNull
    private DisplayMode<G, A, R> displayMode;

    /**
     * The list cell renderer displaying object names.
     * @serial
     */
    @NotNull
    private final DisplayMode<G, A, R> displayModeGameObjectNames;

    /**
     * The list cell renderer displaying archetype names.
     * @serial
     */
    @NotNull
    private final DisplayMode<G, A, R> displayModeArchetypeNames;

    /**
     * The list cell renderer displaying archetype icons.
     * @serial
     */
    @NotNull
    private final DisplayMode<G, A, R> displayModeIconsOnly;

    /**
     * The change listener attached to {@link #tabDesktop}.
     */
    @NotNull
    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            final JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
            final int index = tabbedPane.getSelectedIndex();
            if (index != -1) {
                setSelectedPanelInt(getArchetypePanel(index));
            }
        }
    };

    /**
     * Creates a new instance.
     * @param createDirectionPane whether the direction pane should be shown
     * @param archetypeChooserModel the model to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public ArchetypeChooserView(final boolean createDirectionPane, @NotNull final ArchetypeChooserModel<G, A, R> archetypeChooserModel, @NotNull final FaceObjectProviders faceObjectProviders) {
        super(new BorderLayout());
        this.archetypeChooserModel = archetypeChooserModel;
        displayModeGameObjectNames = new DisplayNameCellRenderer<G, A, R>(faceObjectProviders);
        displayModeArchetypeNames = new ArchetypeNameCellRenderer<G, A, R>(faceObjectProviders);
        displayModeIconsOnly = new ArchetypeIconCellRenderer<G, A, R>(faceObjectProviders);

        final FaceObjectProvidersListener faceObjectProvidersListener = new FaceObjectProvidersListener() {

            @Override
            public void facesReloaded() {
                final ArchetypeChooserPanel<G, A, R> archetypeChooserPanel = archetypeChooserModel.getSelectedPanel();
                if (archetypeChooserPanel != null) {
                    final ArchetypePanel<G, A, R> archetypePanel = findPanel(archetypeChooserPanel.getName());
                    if (archetypePanel != null) {
                        archetypePanel.repaint();
                    }
                }
            }

        };
        faceObjectProviders.addFaceObjectProvidersListener(faceObjectProvidersListener);

        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(buttonDisplayGameObjectNames);
        buttonGroup.add(buttonDisplayArchetypeNames);
        buttonGroup.add(buttonDisplayIconsOnly);
        final int preferencesDisplayMode = preferences.getInt(DISPLAY_MODE_KEY, 0);
        switch (preferencesDisplayMode) {
        case 0:
        default:
            displayMode = displayModeGameObjectNames;
            break;

        case 1:
            displayMode = displayModeArchetypeNames;
            break;

        case 2:
            displayMode = displayModeIconsOnly;
            break;
        }
        fireDisplayObjectNamesChangedEvent();
        tabDesktop.setBorder(BorderFactory.createEmptyBorder(CommonConstants.SPACE_PICKMAP_ARCHETYPE_TOP, 0, 0, 0));
        add(tabDesktop);
        if (createDirectionPane) {
            add(new DirectionPane<G, A, R>(archetypeChooserModel, true), BorderLayout.SOUTH);
        }
        updateActions();
        final ArchetypeChooserPanel<G, A, R> selectedPanel = archetypeChooserModel.getSelectedPanel();
        if (selectedPanel != null) {
            final ArchetypePanel<G, A, R> archetypePanel = findPanel(selectedPanel.getName());
            if (archetypePanel != null) {
                setSelectedPanel(archetypePanel);
            }
        }
        tabDesktop.addChangeListener(changeListener);
    }

    /**
     * Returns a panel by name. If this panel does not exist, create a new one.
     * @param panelName the panel name
     * @return the panel
     */
    @NotNull
    public ArchetypePanel<G, A, R> findOrCreatePanel(@NotNull final String panelName) {
        final ArchetypePanel<G, A, R> existingPanel = findPanel(panelName);
        if (existingPanel != null) {
            return existingPanel;
        }

        final ArchetypeChooserPanel<G, A, R> archetypeChooserPanel = archetypeChooserModel.getPanel(panelName);
        final ArchetypePanel<G, A, R> newPanel = new ArchetypePanel<G, A, R>(this, archetypeChooserPanel);

        // insert new panel in alphabetical order
        int i;
        for (i = 0; i < tabDesktop.getTabCount() && panelName.compareToIgnoreCase(tabDesktop.getTitleAt(i)) > 0; i++) {
        }
        tabDesktop.insertTab(panelName, null, newPanel, null, i);

        return newPanel;
    }

    /**
     * Returns an {@link ArchetypePanel} by name.
     * @param panelName the panel name
     * @return the archetype panel or <code>null</code> if the panel does not
     *         exist
     */
    @Nullable
    private ArchetypePanel<G, A, R> findPanel(@NotNull final String panelName) {
        for (int i = 0; i < tabDesktop.getTabCount(); i++) {
            if (panelName.equals(tabDesktop.getTitleAt(i))) {
                return getArchetypePanel(i);
            }
        }

        return null;
    }

    /**
     * Returns an {@link ArchetypePanel} by tab index.
     * @param index the tab index
     * @return the archetype panel
     */
    @NotNull
    private ArchetypePanel<G, A, R> getArchetypePanel(final int index) {
        //JTabbedPane does not use type parameters
        @SuppressWarnings("unchecked")
        final ArchetypePanel<G, A, R> archetypePanel = (ArchetypePanel<G, A, R>) tabDesktop.getComponentAt(index);
        return archetypePanel;
    }

    /**
     * Selects an archetype panel. Does nothing if the panel does not exist.
     * @param selectedPanel the archetype panel to show
     */
    public final void setSelectedPanel(@NotNull final ArchetypePanel<G, A, R> selectedPanel) {
        final int selectedIndex = tabDesktop.indexOfComponent(selectedPanel);
        if (selectedIndex == -1 || selectedIndex == tabDesktop.getSelectedIndex()) {
            return;
        }

        setSelectedPanelInt(selectedPanel);
        tabDesktop.setSelectedIndex(selectedIndex);
    }

    /**
     * Notifies all listeners that the display mode has changed.
     */
    private void fireDisplayObjectNamesChangedEvent() {
        if (displayMode == displayModeGameObjectNames) {
            preferences.putInt(DISPLAY_MODE_KEY, 0);
        } else if (displayMode == displayModeArchetypeNames) {
            preferences.putInt(DISPLAY_MODE_KEY, 1);
        } else if (displayMode == displayModeIconsOnly) {
            preferences.putInt(DISPLAY_MODE_KEY, 2);
        }
        updateActions();
        for (final ArchetypeChooserViewListener<G, A, R> listener : listeners) {
            listener.displayModeChanged(displayMode);
        }
    }

    /**
     * Returns the current display mode.
     * @return the current display mode
     */
    @NotNull
    public DisplayMode<G, A, R> getDisplayMode() {
        return displayMode;
    }

    /**
     * Updates the display mode.
     * @param displayMode the new display mode
     */
    private void setDisplayMode(@NotNull final DisplayMode<G, A, R> displayMode) {
        if (this.displayMode == displayMode) {
            return;
        }

        this.displayMode = displayMode;
        fireDisplayObjectNamesChangedEvent();
    }

    /**
     * Sets whether game object names are shown.
     */
    @ActionMethod
    public void displayGameObjectNames() {
        setDisplayMode(displayModeGameObjectNames);
    }

    /**
     * Sets whether archetype names are shown.
     */
    @ActionMethod
    public void displayArchetypeNames() {
        setDisplayMode(displayModeArchetypeNames);
    }

    /**
     * Sets whether icons are shown.
     */
    @ActionMethod
    public void displayIconsOnly() {
        setDisplayMode(displayModeIconsOnly);
    }

    /**
     * Adds a listener to be notified of events.
     * @param listener the listener to add
     */
    public void addArchetypeChooserViewListener(@NotNull final ArchetypeChooserViewListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be notified of events.
     * @param listener the listener to remove
     */
    public void removeArchetypeChooserViewListener(@NotNull final ArchetypeChooserViewListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Update the actions' state.
     */
    private void updateActions() {
        if (displayMode == displayModeGameObjectNames) {
            buttonDisplayGameObjectNames.setSelected(true);
        } else if (displayMode == displayModeArchetypeNames) {
            buttonDisplayArchetypeNames.setSelected(true);
        } else if (displayMode == displayModeIconsOnly) {
            buttonDisplayIconsOnly.setSelected(true);
        }
    }

    /**
     * Sets the selected {@link ArchetypePanel} in {@link
     * #archetypeChooserModel}.
     * @param archetypePanel the panel to select
     */
    private void setSelectedPanelInt(@NotNull final ArchetypePanel<G, A, R> archetypePanel) {
        archetypeChooserModel.setSelectedPanel(archetypePanel.getArchetypeChooserPanel());
    }

}
