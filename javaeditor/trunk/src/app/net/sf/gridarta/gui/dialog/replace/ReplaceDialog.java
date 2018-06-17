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

package net.sf.gridarta.gui.dialog.replace;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;
import net.sf.gridarta.gui.copybuffer.CopyBuffer;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooserListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.select.ArchetypeNameMatchCriteria;
import net.sf.gridarta.model.select.MatchCriteria;
import net.sf.gridarta.model.select.ObjectNameMatchCriteria;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.RandomUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This dialog manages the replace action.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian.Hujer</a>
 */
public class ReplaceDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

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
     * Index for {@link #replaceWithBox}: replace with object chooser
     * selection.
     */
    private static final int REPLACE_WITH_OBJECT_CHOOSER = 0;

    /**
     * Index for {@link #replaceWithBox}: replace with copy buffer contents.
     */
    private static final int REPLACE_WITH_COPY_BUFFER = 1;

    /**
     * Index for {@link #replaceWithBox}: replace with pickmap contents.
     */
    private static final int REPLACE_WITH_PICKMAP = 2;

    /**
     * Index for {@link #replaceWithBox}: delete matching game objects.
     */
    private static final int REPLACE_WITH_NOTHING = 3;

    /**
     * Index for {@link #replaceEntireBox}: replace map.
     */
    private static final int REPLACE_ON_MAP = 0;

    /**
     * Index for {@link #replaceEntireBox}: replace selection.
     */
    private static final int REPLACE_ON_SELECTION = 1;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * The dialog instance.
     */
    @NotNull
    private final JDialog dialog;

    /**
     * The parent component for dialogs.
     */
    @NotNull
    private final Component parent;

    /**
     * The {@link CopyBuffer}.
     */
    @NotNull
    private final CopyBuffer<G, A, R> copyBuffer;

    /**
     * The object chooser to use.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * Whether this replace dialog has been displayed.
     */
    private boolean isBuilt;

    /**
     * The {@link MapView} to operate on.
     */
    @NotNull
    private MapView<G, A, R> mapView;

    /**
     * Objects will be replaced with this game object.
     */
    @Nullable
    private BaseObject<G, A, R, ?> replaceArch;

    @NotNull
    private List<G> replaceCopyBuffer; // objects in CopyBuffer

    @NotNull
    private List<? extends BaseObject<G, A, R, ?>> replacePickmap; // selected objects in pickmap or all if none is selected

    @NotNull
    private JLabel rfHeading;

    @Nullable
    private JLabel rfArchName;

    @NotNull
    private JLabel iconLabel;

    @NotNull
    private JLabel colonLabel;

    @NotNull
    private JComboBox replaceCriteria;

    @NotNull
    private JComboBox replaceWithBox;

    @NotNull
    private JComboBox replaceEntireBox;

    @NotNull
    private JTextComponent replaceInput1;

    /**
     * Input field for replace density value.
     */
    @NotNull
    private JTextComponent replaceDensityInput;

    private int lastSelectedIndex = REPLACE_WITH_OBJECT_CHOOSER;

    /**
     * The {@link ObjectChooserListener} for tracking the selection.
     */
    @NotNull
    private final ObjectChooserListener<G, A, R> objectChooserListener = new ObjectChooserListener<G, A, R>() {

        @Override
        public void pickmapActiveChanged(final boolean pickmapActive) {
            // ignore
        }

        @Override
        public void selectionChanged(@Nullable final BaseObject<G, A, R, ?> gameObject) {
            updateArchSelection(gameObject, false);
        }

    };

    /**
     * The {@link FaceObjectProvidersListener} for detecting reloaded faces.
     */
    @NotNull
    private final FaceObjectProvidersListener faceObjectProvidersListener = new FaceObjectProvidersListener() {

        @Override
        public void facesReloaded() {
            updateArchSelection(objectChooser.getSelection(), false);
        }

    };

    /**
     * Creates a new instance.
     * @param parent the parent component for dialogs
     * @param copyBuffer the copy buffer's
     * @param objectChooser the object chooser to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param insertionModeSet the insertion mode set to use
     */
    public ReplaceDialog(@NotNull final Component parent, @NotNull final CopyBuffer<G, A, R> copyBuffer, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        this.insertionModeSet = insertionModeSet;
        dialog = createDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "replaceTitle"));
        dialog.setModal(false);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.parent = parent;
        this.copyBuffer = copyBuffer;
        this.objectChooser = objectChooser;
        this.faceObjectProviders = faceObjectProviders;
        objectChooser.addObjectChooserListener(objectChooserListener);
        faceObjectProviders.addFaceObjectProvidersListener(faceObjectProvidersListener);
    }

    /**
     * Replace objects on the map.
     * @param mapView map view of the active map where the action was invoked
     */
    public void display(@NotNull final MapView<G, A, R> mapView) {
        replaceArch = objectChooser.getCursorSelection(); // highlighted arch
        replacePickmap = objectChooser.getSelections(); // selected arches
        replaceCopyBuffer = copyBuffer.getAllGameObjects();

        if (isBuilt) {
            // just set fields and show
            rfHeading.setText("\"" + mapView.getMapControl().getMapModel().getMapArchObject().getMapName() + "\":");
            replaceInput1.setText("");

            this.mapView = mapView;
            if (replaceArch == null) {
                replaceWithBox.setSelectedIndex(REPLACE_WITH_COPY_BUFFER);
                iconLabel.setIcon(null);
                rfArchName.setText("");
                colonLabel.setText("");
            } else {
                replaceWithBox.setSelectedIndex(REPLACE_WITH_OBJECT_CHOOSER);
                iconLabel.setIcon(faceObjectProviders.getFace(replaceArch));
                rfArchName.setText(" " + replaceArch.getBestName());
                colonLabel.setText(":");
            }

            if (mapView.getSelectedSquares().size() > 1) {
                replaceEntireBox.setSelectedIndex(REPLACE_ON_SELECTION);
            } else {
                replaceEntireBox.setSelectedIndex(REPLACE_ON_MAP);
            }

            replaceDensityInput.setText("100");

            dialog.pack();
            dialog.toFront();
        } else {
            this.mapView = mapView;
            final JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 2, 5));

            // first line: heading
            final Container line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JComponent labelOn = ActionBuilderUtils.newLabel(ACTION_BUILDER, "replaceOn");
            labelOn.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceOn.shortdescription"));
            line1.add(labelOn);
            line1.add(Box.createVerticalStrut(3));
            replaceEntireBox = new JComboBox(new String[] { ActionBuilderUtils.getString(ACTION_BUILDER, "replaceOnMap"), ActionBuilderUtils.getString(ACTION_BUILDER, "replaceOnSelection") });
            replaceEntireBox.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceOn.shortdescription"));
            if (mapView.getSelectedSquares().size() > 1) {
                replaceEntireBox.setSelectedIndex(REPLACE_ON_SELECTION);
            } else {
                replaceEntireBox.setSelectedIndex(REPLACE_ON_MAP);
            }
            line1.add(replaceEntireBox);
            line1.add(Box.createVerticalStrut(3));
            rfHeading = new JLabel("\"" + mapView.getMapControl().getMapModel().getMapArchObject().getMapName() + "\":");
            line1.add(rfHeading);
            mainPanel.add(line1);

            // second line: replace what?
            final Container line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JComponent label1 = ActionBuilderUtils.newLabel(ACTION_BUILDER, "replaceDelete");
            label1.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceDelete.shortdescription"));
            line2.add(label1);
            line2.add(Box.createVerticalStrut(5));

            replaceCriteria = new JComboBox(new String[] { ActionBuilderUtils.getString(ACTION_BUILDER, "replaceArchetype"), ActionBuilderUtils.getString(ACTION_BUILDER, "replaceName") });
            replaceCriteria.setSelectedIndex(0);
            replaceCriteria.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceDelete.shortdescription"));
            line2.add(replaceCriteria);
            line2.add(Box.createVerticalStrut(5));

            replaceInput1 = new JTextField(20);
            replaceInput1.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceInput1.shortdescription"));
            line2.add(replaceInput1);
            mainPanel.add(line2);

            // third line: replace by?
            final Container line3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JComponent label2 = ActionBuilderUtils.newLabel(ACTION_BUILDER, "replaceBy");
            label2.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceBy.shortdescription"));
            line3.add(label2);
            line3.add(Box.createVerticalStrut(5));
            replaceWithBox = new JComboBox(new String[] { ActionBuilderUtils.getString(ACTION_BUILDER, "replaceByObject"), ActionBuilderUtils.getString(ACTION_BUILDER, "replaceByCopyBuffer"), ActionBuilderUtils.getString(ACTION_BUILDER, "replaceByPickmap"), ActionBuilderUtils.getString(ACTION_BUILDER, "replaceByNothing") });
            replaceWithBox.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceBy.shortdescription"));
            //noinspection VariableNotUsedInsideIf
            if (replaceArch == null) {
                replaceWithBox.setSelectedIndex(REPLACE_WITH_NOTHING);
            } else {
                replaceWithBox.setSelectedIndex(REPLACE_WITH_OBJECT_CHOOSER);
            }
            replaceWithBox.addItemListener(new ReplaceWithBoxItemListener());
            lastSelectedIndex = replaceWithBox.getSelectedIndex();
            line3.add(replaceWithBox);

            iconLabel = new JLabel();
            if (replaceArch != null) {
                colonLabel = new JLabel(":");
                iconLabel.setIcon(faceObjectProviders.getFace(replaceArch));
                rfArchName = new JLabel(" " + replaceArch.getBestName());
            } else {
                colonLabel = new JLabel("");
                rfArchName = new JLabel("");
            }
            line3.add(colonLabel);
            line3.add(Box.createVerticalStrut(5));
            line3.add(iconLabel);
            line3.add(rfArchName);
            mainPanel.add(line3);

            // fourth line: replace density
            final Container line4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            final JComponent label4 = ActionBuilderUtils.newLabel(ACTION_BUILDER, "replaceDensity");
            label4.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceDensity.shortdescription"));
            line4.add(label4);
            line4.add(Box.createVerticalStrut(5));
            replaceDensityInput = new JTextField(5);
            replaceDensityInput.setToolTipText(ActionBuilderUtils.getString(ACTION_BUILDER, "replaceDensity.shortdescription"));
            line4.add(replaceDensityInput);
            replaceDensityInput.setText("100");
            mainPanel.add(line4);

            final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "replaceOk", this));
            final JButton applyButton = new JButton(ACTION_BUILDER.createAction(false, "replaceApply", this));
            final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "replaceCancel", this));

            setMessage(mainPanel);
            setOptions(new Object[] { okButton, applyButton, cancelButton });
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
            dialog.setLocationRelativeTo(parent);
            isBuilt = true;
        }
        dialog.setVisible(true);
    }

    /**
     * Update which arch is displayed as replace object.
     * @param newArch the new 'replaceArch' to be shown and stored
     * @param alwaysPack if false, the frame is packed only when icon size
     * changed if true, the frame is always packed (packing resizes but also
     * causes flicker)
     */
    private void updateArchSelection(@Nullable final BaseObject<G, A, R, ?> newArch, final boolean alwaysPack) {
        if (isShowing() && replaceWithBox.getSelectedIndex() == REPLACE_WITH_OBJECT_CHOOSER) {
            replaceArch = newArch;
            if (newArch != null) {
                final Icon oldIcon = iconLabel.getIcon();

                iconLabel.setIcon(faceObjectProviders.getFace(newArch));
                rfArchName.setText(" " + newArch.getBestName());
                colonLabel.setText(":");

                // pack frame only if height of icon changed
                if (alwaysPack || (oldIcon == null && iconLabel.getIcon() != null) || (oldIcon != null && iconLabel.getIcon() == null) || (oldIcon != iconLabel.getIcon() && oldIcon != null && oldIcon.getIconHeight() != iconLabel.getIcon().getIconHeight())) {
                    dialog.pack();
                }
            } else {
                iconLabel.setIcon(null);
                rfArchName.setText("");
                colonLabel.setText("");
            }
        }
    }

    /**
     * This method performs the actual replace action on a map.
     * @param matchCriteria matching criteria for replace
     * @param entireMap if true, the entire map is affected - if false, only
     * highlighted area
     * @param deleteOnly if true matching arches get only deleted and not
     * replaced
     * @param replaceDensity the replace density in %
     * @return number of arches that have been replaced
     */
    private int doReplace(@NotNull final MatchCriteria<G, A, R> matchCriteria, final boolean entireMap, final boolean deleteOnly, final int replaceDensity) {
        @Nullable final List<? extends BaseObject<G, A, R, ?>> replaceList;
        switch (lastSelectedIndex) {
        case REPLACE_WITH_OBJECT_CHOOSER:
            if (replaceArch == null) {
                replaceList = null;
            } else {
                replaceList = newList(replaceArch);
            }
            break;

        case REPLACE_WITH_COPY_BUFFER:
            replaceList = replaceCopyBuffer;
            break;

        case REPLACE_WITH_PICKMAP:
            replaceList = replacePickmap;
            break;

        default: // REPLACE_WITH_NOTHING
            replaceList = null;
            break;
        }
        final Collection<G> objectsToReplace = new ArrayList<G>();
        final int replaceListSize = replaceList == null ? 0 : replaceList.size();
        final MapControl<G, A, R> mapControl = mapView.getMapControl();
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        mapModel.beginTransaction("Replace"); // TODO: I18N/L10N
        try {
            int replaceCount = 0;
            for (final MapSquare<G, A, R> square : entireMap ? mapModel : mapView.getSelectedSquares()) {
                // Operate on a copy of the nodes to prevent ConcurrentModificationException

                // find objects to replace
                objectsToReplace.clear();
                for (final G node : square) {
                    if (node.isHead() && matchCriteria.matches(node)) {
                        if (replaceDensity > RandomUtils.rnd.nextInt(100)) {
                            objectsToReplace.add(node);
                        }
                    }
                }

                // actually replace the objects
                for (final G objectToReplace : objectsToReplace) {
                    final Iterator<G> it = square.iterator();
                    G prevArch = null;
                    G node = null;
                    while (it.hasNext()) {
                        node = it.next();

                        if (node == objectToReplace) {
                            break;
                        }

                        prevArch = node;
                    }
                    assert node != null;

                    // first, delete the old arch
                    node.remove();

                    if (replaceListSize > 0 && !deleteOnly) {
                        final BaseObject<G, A, R, ?> randomArch;
                        if (replaceListSize == 1) {
                            randomArch = replaceList.get(0);
                        } else {
                            randomArch = replaceList.get(RandomUtils.rnd.nextInt(replaceList.size()));
                        }
                        // insert replacement object
                        if (randomArch.isMulti()) {
                            mapModel.insertBaseObject(randomArch, square.getMapLocation(), false, false, insertionModeSet.getTopmostInsertionMode());
                        } else {
                            mapModel.insertArchToMap(randomArch, prevArch, square.getMapLocation(), false);
                        }
                    }
                    replaceCount++;
                }
            }
            return replaceCount;
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Creates a new list containing one element.
     * @param element the element
     * @return the list
     */
    @NotNull
    private static <T> List<T> newList(@NotNull final T element) {
        final List<T> list = new ArrayList<T>(1);
        list.add(element);
        return list;
    }

    /**
     * Item-listener for the "replace with"-selection box.
     */
    private class ReplaceWithBoxItemListener implements ItemListener {

        @Override
        public void itemStateChanged(@NotNull final ItemEvent e) {
            final int selectedIndex = replaceWithBox.getSelectedIndex();
            if (e.getStateChange() == ItemEvent.SELECTED && lastSelectedIndex != selectedIndex) {
                final int size;
                switch (selectedIndex) {
                case REPLACE_WITH_OBJECT_CHOOSER:
                    replaceArch = objectChooser.getSelection(); // selected arch
                    updateArchSelection(replaceArch, true);
                    break;

                case REPLACE_WITH_COPY_BUFFER:
                    replaceCopyBuffer = copyBuffer.getAllGameObjects();
                    iconLabel.setIcon(null);
                    size = replaceCopyBuffer.size();
                    rfArchName.setText(String.valueOf(size));
                    colonLabel.setText(":");
                    dialog.pack();
                    break;

                case REPLACE_WITH_PICKMAP:
                    replacePickmap = objectChooser.getSelections(); // selected arches
                    iconLabel.setIcon(null);
                    size = replacePickmap.size();
                    rfArchName.setText(String.valueOf(size));
                    colonLabel.setText(":");
                    dialog.pack();
                    break;

                case REPLACE_WITH_NOTHING:
                    iconLabel.setIcon(null);
                    rfArchName.setText("");
                    colonLabel.setText("");
                    dialog.pack();
                    break;
                }
                lastSelectedIndex = selectedIndex;
            }
        }

    }

    /**
     * Action method for Ok button.
     */
    @ActionMethod
    public void replaceOk() {
        if (doReplace()) {
            dialog.setVisible(false);
        }
    }

    /**
     * Action method for Apply button.
     */
    @ActionMethod
    public void replaceApply() {
        doReplace();
    }

    /**
     * Executes one replace operation.
     * @return whether the replace operation was successful
     */
    private boolean doReplace() {
        final String matchString = replaceInput1.getText().trim();
        final boolean deleteOnly = replaceWithBox.getSelectedIndex() == REPLACE_WITH_NOTHING;
        final boolean entireMap = replaceEntireBox.getSelectedIndex() == REPLACE_ON_MAP;

        if (!entireMap && mapView.getMapGrid().getSelectedRec() == null) {
            // user selected "replace highlighted" but nothing is highlighted
            ACTION_BUILDER.showMessageDialog(this, "replaceMapNoSelection", mapView.getMapControl().getMapModel().getMapArchObject().getMapName());
            return false;
        }

        final MatchCriteria<G, A, R> matchCriteria;
        if (replaceCriteria.getSelectedIndex() == 0) {
            matchCriteria = new ArchetypeNameMatchCriteria<G, A, R>(matchString);
        } else if (replaceCriteria.getSelectedIndex() == 1) {
            matchCriteria = new ObjectNameMatchCriteria<G, A, R>(matchString);
        } else {
            return false;
        }

        final int replaceDensity;
        try {
            replaceDensity = Integer.parseInt(replaceDensityInput.getText());
        } catch (final NumberFormatException ignored) {
            ACTION_BUILDER.showMessageDialog(this, "replaceInvalidDensity");
            return false;
        }
        if (replaceDensity < 1 || replaceDensity > 100) {
            ACTION_BUILDER.showMessageDialog(this, "replaceInvalidDensity");
            return false;
        }

        final int replaceCount = doReplace(matchCriteria, entireMap, deleteOnly, replaceDensity);
        if (replaceCount <= 0) {
            ACTION_BUILDER.showMessageDialog(this, "replacedZero");
            return false;
        }

        if (replaceCount == 1) {
            ACTION_BUILDER.showMessageDialog(this, "replacedOne");
        } else {
            ACTION_BUILDER.showMessageDialog(this, "replacedMany", replaceCount);
        }
        return true;
    }

    /**
     * Action method for Cancel button.
     */
    @ActionMethod
    public void replaceCancel() {
        dialog.setVisible(false);
    }

    /**
     * Disposes the replace dialog.
     * @param mapView the map view to dispose the dialog of; do nothing if no
     */
    public void dispose(@NotNull final MapView<G, A, R> mapView) {
        if (mapView == this.mapView) {
            dialog.setVisible(false);
        }
    }

}
