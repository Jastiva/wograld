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

package net.sf.gridarta.gui.dialog.goexit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.TreeSet;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import net.sf.gridarta.gui.map.mapactions.EnterMap;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.utils.SwingUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.maplocation.MapLocation;
import net.sf.gridarta.model.maplocation.NoExitPathException;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;

/**
 * A dialog to ask the user for a map to open.
 * @author Andreas Kirschbaum
 */
public class GoExitDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ActionBuilder}.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * An empty array of {@link JButton} instances.
     */
    @NotNull
    private static final JButton[] EMPTY_BUTTON_ARRAY = new JButton[0];

    /**
     * The {@link MapView} for this dialog.
     */
    @NotNull
    private final MapView<G, A, R> mapView;

    /**
     * The {@link GameObjectMatcher} for selecting exits.
     */
    @NotNull
    private final GameObjectMatcher exitGameObjectMatcher;

    /**
     * The {@link PathManager} for converting relative exit paths.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The {@link EnterMap} instance for entering maps.
     */
    @NotNull
    private final EnterMap<G, A, R> enterMap;

    /**
     * The {@link JDialog} instance.}
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The list model containing the search results.
     */
    @NotNull
    private final DefaultListModel listModel = new DefaultListModel();

    /**
     * The {@link JList} showing the matching maps.
     */
    @NotNull
    private final JList list = new JList(listModel);

    /**
     * The {@link MapListCellRenderer} for {@link #list}.
     */
    @NotNull
    private final MapListCellRenderer mapListCellRenderer;

    /**
     * Creates a new instance.
     * @param parent the parent component for this dialog
     * @param mapView the map view for this dialog
     * @param exitGameObjectMatcher the game object matcher for selecting exits
     * @param pathManager the path manager for converting relative exit paths
     * @param enterMap the enter map instance to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     */
    public GoExitDialog(@NotNull final Component parent, @NotNull final MapView<G, A, R> mapView, @NotNull final GameObjectMatcher exitGameObjectMatcher, @NotNull final PathManager pathManager, @NotNull final EnterMap<G, A, R> enterMap, @NotNull final FaceObjectProviders faceObjectProviders) {
        this.mapView = mapView;
        this.exitGameObjectMatcher = exitGameObjectMatcher;
        this.pathManager = pathManager;
        this.enterMap = enterMap;
        SwingUtils.addAction(list, ACTION_BUILDER.createAction(false, "goExitApply", this));
        list.setFocusable(false);
        final Component scrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mapListCellRenderer = new MapListCellRenderer(pathManager, faceObjectProviders);
        list.setCellRenderer(mapListCellRenderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final MouseListener mouseListener = new MouseListener() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                // ignore
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getClickCount() > 1) {
                    goExitApply();
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                // ignore
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                // ignore
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                // ignore
            }

        };
        list.addMouseListener(mouseListener);
        list.setFocusable(true);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, EMPTY_BUTTON_ARRAY, list);
        dialog = optionPane.createDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "goExitTitle"));
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.setSize(500, 250);
        dialog.setLocationRelativeTo(parent);
    }

    /**
     * Opens the dialog.
     */
    public void showDialog() {
        final MapControl<G, A, R> mapControl = mapView.getMapControl();
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final Collection<G> exits = new TreeSet<G>(mapListCellRenderer);
        for (final Iterable<G> mapSquare : mapModel) {
            for (final G gameObject : mapSquare) {
                if (exitGameObjectMatcher.isMatching(gameObject)) {
                    try {
                        MapLocation.newAbsoluteMapLocation(gameObject, true, pathManager);
                        exits.add(gameObject);
                    } catch (final NoExitPathException ignored) {
                        // ignore
                    }
                }
            }
        }
        for (final G exit : exits) {
            listModel.addElement(exit);
        }

        list.setSelectedIndex(0);
        list.ensureIndexIsVisible(0);
        dialog.setVisible(true);
    }

    /**
     * Action method for apply.
     */
    @ActionMethod
    public void goExitApply() {
        if (goExit()) {
            dialog.dispose();
        }
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void goExitCancel() {
        dialog.dispose();
    }

    /**
     * Action method for scroll up.
     */
    @ActionMethod
    public void goExitScrollUp() {
        final int index = list.getMinSelectionIndex();
        final int newIndex = index > 0 ? index - 1 : listModel.size() - 1;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll down.
     */
    @ActionMethod
    public void goExitScrollDown() {
        final int index = list.getMaxSelectionIndex() + 1;
        final int newIndex = index < listModel.size() ? index : 0;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll page up.
     */
    @ActionMethod
    public void goExitScrollPageUp() {
        final int index = list.getMinSelectionIndex();
        final int firstIndex = list.getFirstVisibleIndex();
        final int newIndex;
        if (firstIndex == -1) {
            newIndex = -1;
        } else if (index == -1) {
            newIndex = firstIndex;
        } else if (index > firstIndex) {
            newIndex = firstIndex;
        } else {
            newIndex = Math.max(firstIndex - (list.getLastVisibleIndex() - firstIndex), 0);
        }
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll page down.
     */
    @ActionMethod
    public void goExitScrollPageDown() {
        final int index = list.getMaxSelectionIndex();
        final int lastIndex = list.getLastVisibleIndex();
        final int newIndex;
        if (lastIndex == -1) {
            newIndex = -1;
        } else if (index == -1) {
            newIndex = lastIndex;
        } else if (index < lastIndex) {
            newIndex = lastIndex;
        } else {
            newIndex = Math.min(lastIndex + (lastIndex - list.getFirstVisibleIndex()), listModel.size() - 1);
        }
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll top.
     */
    @ActionMethod
    public void goExitScrollTop() {
        final int newIndex = 0;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for scroll bottom.
     */
    @ActionMethod
    public void goExitScrollBottom() {
        final int newIndex = listModel.size() - 1;
        list.setSelectedIndex(newIndex);
        list.ensureIndexIsVisible(newIndex);
    }

    /**
     * Action method for select up.
     */
    @ActionMethod
    public void goExitSelectUp() {
        final int index = list.getMinSelectionIndex();
        if (index != 0) {
            final int newIndex = index > 0 ? index - 1 : listModel.size() - 1;
            list.addSelectionInterval(newIndex, newIndex);
            list.ensureIndexIsVisible(newIndex);
        }
    }

    /**
     * Action method for select down.
     */
    @ActionMethod
    public void goExitSelectDown() {
        final int index = list.getMaxSelectionIndex();
        if (index + 1 < listModel.size()) {
            final int newIndex = index + 1;
            list.addSelectionInterval(newIndex, newIndex);
            list.ensureIndexIsVisible(newIndex);
        }
    }

    /**
     * Opens the selected maps.
     * @return whether at least one map was opened
     */
    private boolean goExit() {
        final Object selectedValue = list.getSelectedValue();
        if (selectedValue == null) {
            return false;
        }
        @SuppressWarnings("unchecked") final GameObject<G, A, R> exit = (GameObject<G, A, R>) selectedValue;
        return enterMap.enterExit(mapView, exit, true);
    }

}
