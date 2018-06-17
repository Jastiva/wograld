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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.Icon;
import javax.swing.text.StyleContext;
import net.sf.gridarta.gui.dialog.help.Help;
import net.sf.gridarta.gui.map.maptilepane.TilePanel;
import net.sf.gridarta.gui.treasurelist.CFTreasureListTree;
import net.sf.gridarta.gui.utils.AnimationComponent;
import net.sf.gridarta.gui.utils.FaceComponent;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.AbstractArchetypeAttributeSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeAttribute;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeAnimationName;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBitmask;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBool;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBoolSpec;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeFaceName;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeFixed;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeFloat;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeInt;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeInvSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeList;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeLong;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeMapPath;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeScriptFile;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeString;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeText;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeTreasure;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeVisitor;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeZSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeList;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.archetypetype.AttributeBitmask;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjectProvidersListener;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectUtils;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spell;
import net.sf.gridarta.model.spells.Spells;
import net.sf.gridarta.model.treasurelist.TreasureTree;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.textedit.textarea.JEditTextArea;
import net.sf.gridarta.textedit.textarea.SyntaxDocument;
import net.sf.gridarta.textedit.textarea.TextAreaDefaults;
import net.sf.gridarta.textedit.textarea.tokenmarker.TokenMarkerFactory;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common base class for game object attributes dialogs.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class GameObjectAttributesDialog<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JOptionPane {

    /**
     * Color for float values.
     */
    @NotNull
    public static final Color FLOAT_COLOR = new Color(19, 134, 0);

    /**
     * Color for int values.
     */
    @NotNull
    public static final Color INT_COLOR = new Color(74, 70, 156);

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(GameObjectAttributesDialog.class);

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The associated factory.
     */
    @NotNull
    private final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory;

    /**
     * Reference to the {@link ArchetypeTypeSet}.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The parent frame for showing dialog boxes.
     * @serial
     */
    @NotNull
    private final JFrame parent;

    /**
     * The {@link CFTreasureListTree} to use.
     * @serial
     */
    @NotNull
    private final CFTreasureListTree treasureListTree;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link AnimationObjects} instance for choosing animation names.
     * @serial
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * The {@link GlobalSettings} instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link FileFilter} to use for map files.
     */
    @NotNull
    private final FileFilter mapFileFilter;

    /**
     * The {@link FileFilter} to use for script files.
     */
    @NotNull
    private final FileFilter scriptFileFilter;

    /**
     * The {@link FaceObjects} instance for choosing face names.
     * @serial
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * The game object spells to use.
     */
    @NotNull
    private final Spells<GameObjectSpell<G, A, R>> gameObjectSpells;

    /**
     * The numbered spells.
     */
    @NotNull
    private final Spells<NumberSpell> numberSpells;

    /**
     * The index for "no spell".
     * @serial
     */
    private final int undefinedSpellIndex;

    /**
     * The {@link TreasureTree} to use.
     */
    @NotNull
    private final TreasureTree treasureTree;

    /**
     * The {@link ImageIcon} for no animations.
     * @serial
     */
    @NotNull
    private final ImageIcon noFaceSquareIcon;

    /**
     * The {@link ImageIcon} for undefined animations.
     * @serial
     */
    @NotNull
    private final ImageIcon unknownSquareIcon;

    /**
     * The {@link TextAreaDefaults} for text fields.
     */
    @NotNull
    private final TextAreaDefaults textAreaDefaults;

    /**
     * The {@link MapManager} instance.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * All {@link DialogAttribute DialogAttributes} in the dialog.
     */
    @NotNull
    private final Collection<DialogAttribute<G, A, R, ?>> dialogAttributes = new ArrayList<DialogAttribute<G, A, R, ?>>();

    /**
     * The {@link TypesBoxItemListener} attached to the combo box box for
     * selecting the object's type.
     */
    @NotNull
    private TypesBoxItemListener<G, A, R> typesBoxItemListener;

    /**
     * The panel for object's face (png).
     * @serial
     */
  //  @NotNull
 //   private JLabel faceLabel;

    private JPanel facePanel;
    
    
    // info from panel inside dialog, initially null
   // private JPanel retrievedPanel;
    
    private boolean foundImage = false;
    // try to have the primary image attribute affect the header image
   
    // reference to first facecomponent of attributes, to call to modify facepanel
    private FaceComponent firstRef = null;
    
    /**
     * The game object being modified.
     * @serial
     */
    @NotNull
    private final G gameObject;

    /**
     * {@link #gameObject}'s initial map.
     * @serial
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The position of the game object.
     * @serial
     */
    @NotNull
    private final Point mapPos;

    /**
     * {@link #gameObject}'s archetype.
     * @serial
     */
    @NotNull
    private final Archetype<G, A, R> archetype;

    /**
     * The CardLayout for toggling between edit and summary.
     * @serial
     */
    @NotNull
    private final CardLayout cardLayout = new CardLayout();

    /**
     * The Action for switching to the summary.
     * @serial
     */
    @NotNull
    private final Action summaryAction = ACTION_BUILDER.createAction(false, "attribSummary", this);

    /**
     * The Action for switching to the edit.
     * @serial
     */
    @NotNull
    private final Action editAction = ACTION_BUILDER.createAction(false, "attribEdit", this);

    /**
     * The Button for toggling the summary.
     * @serial
     */
    @NotNull
    private AbstractButton summaryEditButton;

    /**
     * The Button for cancel.
     * @serial
     */
    @NotNull
    private AbstractButton cancelButton;

    /**
     * The Button for ok.
     * @serial
     */
    @NotNull
    private JButton okButton;

    /**
     * The central tabbed pane (the place where all the attribute tabs are).
     * @serial
     */
    @NotNull
    private final JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * The central pane, this is the parent component of above tabbed pane.
     * @serial
     */
    @NotNull
    private final Container centerPanel;

    /**
     * The text pane where the summary is displayed.
     * @serial
     */
    @NotNull
    private final JTextPane summaryTextPane;

    @NotNull
    private final FocusListener focusListener = new ScrollToVisibleFocusListener();

    @NotNull
    private final Style summaryTextStyle;

    /**
     * The return value for {@link #archetypeAttributeVisitor}.
     */
    @Nullable
    private GuiInfo<G, A, R, ?> guiInfo;

    /**
     * The {@link ArchetypeAttributeVisitor} for creating GUI elements for
     * {@link ArchetypeAttribute ArchetypeAttributes}.
     */
    @NotNull
    private final ArchetypeAttributeVisitor archetypeAttributeVisitor = new ArchetypeAttributeVisitor() {

        @Override
        public void visit(@NotNull final ArchetypeAttributeAnimationName archetypeAttribute) {
            final String attributeName = archetypeAttribute.getArchetypeAttributeName();
            final String defaultText = gameObject.getAttributeString(attributeName);
            final AnimationComponent input = new AnimationComponent(defaultText, animationObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon,null);
            // what effect should choosing from treechooseaction have on top image, for animations?
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeAnimationName>(new DialogAttributeAnimationName<G, A, R>(archetypeAttribute, input), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeBitmask archetypeAttribute) {
            final JTextArea input = new JTextArea();
            final DialogAttributeBitmask<G, A, R> tmpAttribute = new DialogAttributeBitmask<G, A, R>(archetypeAttribute, input);
            @Nullable final JTextArea cComp;
            @Nullable final JButton cLabel;
            @Nullable final JLabel cRow;
            final AttributeBitmask bitmask = archetypeTypeSet.getBitmask(archetypeAttribute.getBitmaskName());
            if (bitmask != null) {
                tmpAttribute.setBitmask(bitmask);
                cLabel = new JButton(new MaskChangeAL<G, A, R>(archetypeAttribute.getAttributeName() + ":", tmpAttribute));
                input.setBackground(getBackground());
                input.setEditable(false);
                input.setBorder(BorderFactory.createLineBorder(Color.gray));
                input.setText(bitmask.getText(tmpAttribute.getValue()));
                cComp = input;
                tmpAttribute.setEncodedValue(gameObject.getAttributeString(archetypeAttribute.getArchetypeAttributeName()));
                cRow = null;
            } else {
                cLabel = null;
                cComp = null;
                cRow = new JLabel("Error: Undefined Bitmask");
            }
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeBitmask>(tmpAttribute, cLabel, cComp, cRow, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeBool archetypeAttribute) {
            final JCheckBox input = new JCheckBox(archetypeAttribute.getAttributeName(), gameObject.getAttributeInt(archetypeAttribute.getArchetypeAttributeName()) == 1);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeBool>(new DialogAttributeBool<G, A, R>(archetypeAttribute, input), null, null, input, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeBoolSpec archetypeAttribute) {
            final CharSequence attributeString = gameObject.getAttributeString(archetypeAttribute.getArchetypeAttributeName());
            final boolean defaultValue;
            if (archetypeAttribute.getTrueValue().equals("0")) {
                defaultValue = attributeString.length() == 0 || attributeString.equals("0");
            } else {
                defaultValue = attributeString.equals(archetypeAttribute.getTrueValue());
            }
            final JCheckBox input = new JCheckBox(archetypeAttribute.getAttributeName(), defaultValue);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeBoolSpec>(new DialogAttributeBoolSpec<G, A, R>(archetypeAttribute, input), null, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeFaceName archetypeAttribute) {
            final String attributeName = archetypeAttribute.getArchetypeAttributeName();
            final String defaultText = gameObject.getAttributeString(attributeName);
           // final FaceComponent input = createFaceComponent(defaultText);
            FaceComponent input = null;
            if(foundImage == false){
            input = new FaceComponent(defaultText, faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon,facePanel);
            foundImage = true;
            firstRef=input;
            } else {
                input = new FaceComponent(defaultText, faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon,null);
            }
                
         
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ":");
            if(input != null){
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeFaceName>(new DialogAttributeFaceName<G, A, R>(archetypeAttribute, input), cLabel, input, null, null, false);
            // but if guiInfo doesnt update, then what?
            }
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeFixed archetypeAttribute) {
            throw new AssertionError();
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeFloat archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(FLOAT_COLOR);
            final int fieldLength = archetypeAttribute.getInputLength() == 0 ? ArchetypeAttribute.TEXTFIELD_COLUMNS : archetypeAttribute.getInputLength();
            final DecimalFormat format = new DecimalFormat("#0.0#");
            format.setMaximumFractionDigits(10);
            format.setGroupingUsed(false);
            final NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Double.class);
            final DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
            // parse value from gameObject
            final Number value = gameObject.getAttributeDouble(archetypeAttribute.getArchetypeAttributeName());
            final JFormattedTextField input = new JFormattedTextField(factory, value);
            input.setColumns(fieldLength);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeFloat>(new DialogAttributeFloat<G, A, R>(archetypeAttribute, input), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeInt archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(INT_COLOR);
            final int fieldLength = archetypeAttribute.getInputLength() == 0 ? ArchetypeAttribute.TEXTFIELD_COLUMNS : archetypeAttribute.getInputLength();
            final NumberFormat format = NumberFormat.getIntegerInstance();
            format.setGroupingUsed(false);
            final NumberFormatter formatter = new NumberFormatter(format);
            final DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
            // parse value from gameObject
            final Number value = gameObject.getAttributeInt(archetypeAttribute.getArchetypeAttributeName());
            final JFormattedTextField input = new JFormattedTextField(factory, value);
            input.setColumns(fieldLength);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeInt>(new DialogAttributeInt<G, A, R>(archetypeAttribute, input), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeInvSpell archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(INT_COLOR);
            // create ComboBox with parsed selection
            @Nullable final JComboBox input = buildInvSpellBox(gameObjectSpells, gameObject, archetypeAttribute.isOptionalSpell(), archetypeAttribute.getAttributeName());
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeInvSpell>(new DialogAttributeInvSpell<G, A, R>(archetypeAttribute.isOptionalSpell(), archetypeAttribute, input, gameObjectSpells), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeList archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(INT_COLOR);
            // create ComboBox with parsed selection
            final JComponent cComp;
            @Nullable final JComboBox input;
            final ArchetypeTypeList list = archetypeTypeSet.getList(archetypeAttribute.getListName());
            if (list != null) {
                // build the list from vector data
                input = buildArrayBox(list, gameObject, archetypeAttribute.getArchetypeAttributeName(), archetypeAttribute.getAttributeName());
                cComp = input;
            } else {
                // error: list data is missing or corrupt
                input = new JComboBox();
                cComp = new JLabel("Error: Undefined List");
            }
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeList>(new DialogAttributeList<G, A, R>(archetypeAttribute, input, archetypeTypeSet), cLabel, cComp, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeLong archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(INT_COLOR);
            final int fieldLength = archetypeAttribute.getInputLength() == 0 ? ArchetypeAttribute.TEXTFIELD_COLUMNS : archetypeAttribute.getInputLength();
            final NumberFormat format = NumberFormat.getIntegerInstance();
            format.setGroupingUsed(false);
            final NumberFormatter formatter = new NumberFormatter(format);
            final DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
            // parse value from gameObject
            final Number value = gameObject.getAttributeLong(archetypeAttribute.getArchetypeAttributeName());
            final JFormattedTextField input = new JFormattedTextField(factory, value);
            input.setColumns(fieldLength);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeLong>(new DialogAttributeLong<G, A, R>(archetypeAttribute, input), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeMapPath archetypeAttribute) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            File relativeReference = mapSquare == null ? null : mapSquare.getMapModel().getMapFile();
            if (relativeReference == null) {
                relativeReference = new File(globalSettings.getMapsDirectory(), "dummy");
            }
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            final TilePanel tilePanel = new TilePanel(mapFileFilter, gameObject.getAttributeString(archetypeAttribute.getArchetypeAttributeName()), relativeReference, globalSettings.getMapsDirectory());
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeMapPath>(new DialogAttributeMapPath<G, A, R>(archetypeAttribute, tilePanel), cLabel, tilePanel, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeScriptFile archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            final File mapsDirectory = globalSettings.getMapsDirectory();
            final TilePanel tilePanel = new TilePanel(scriptFileFilter, gameObject.getAttributeString(archetypeAttribute.getArchetypeAttributeName()), new File(mapsDirectory, "dummy"), mapsDirectory);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeScriptFile>(new DialogAttributeScriptFile<G, A, R>(archetypeAttribute, tilePanel), cLabel, tilePanel, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeSpell archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(INT_COLOR);
            // create ComboBox with parsed selection
            @Nullable final JComboBox input = buildSpellBox(gameObject, numberSpells, undefinedSpellIndex, false, archetypeAttribute);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeSpell>(new DialogAttributeSpell<G, A, R>(archetypeAttribute, input, numberSpells), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeString archetypeAttribute) {
            final String defaultText = gameObject.getAttributeString(archetypeAttribute.getArchetypeAttributeName());
            final JTextField input = new JTextField(defaultText, ArchetypeAttribute.TEXTFIELD_COLUMNS);
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeString>(new DialogAttributeString<G, A, R>(archetypeAttribute, input), cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeText archetypeAttribute) {
            String text = "";
            if (archetypeAttribute.getArchetypeAttributeName().equals("msg")) {
                final String archetypeMsgText = archetype.getMsgText();
                final String gameObjectMsgText = gameObject.getMsgText();
                if (archetypeMsgText != null && archetypeMsgText.length() > 0 && (gameObjectMsgText == null || gameObjectMsgText.length() == 0)) {
                    text = archetypeMsgText;
                } else {
                    text = gameObjectMsgText;
                }
            }
            final JEditTextArea input = new JEditTextArea(textAreaDefaults);
            input.setDocument(new SyntaxDocument());
            input.getDocument().setTokenMarker(TokenMarkerFactory.createTokenMarker(archetypeAttribute.getFileExtension()));
            input.setText(text == null ? "" : text);
            input.setCaretPosition(0);
            input.setBorder(BorderFactory.createEmptyBorder(3, 7, 0, 0));
            input.getPainter().setInvalidLinesPainted(false);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeText>(new DialogAttributeText<G, A, R>(archetypeAttribute, input), null, null, null, input, true);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeTreasure archetypeAttribute) {
            // textfield (no direct input, text is set by the treasurelist dialog)
            String treasureName = gameObject.getAttributeString(archetypeAttribute.getArchetypeAttributeName());
            if (treasureName.trim().length() == 0 || treasureName.trim().equalsIgnoreCase("none")) {
                treasureName = CFTreasureListTree.NONE_SYM;
            }
            final JTextField input = new JTextField(" " + treasureName, ArchetypeAttribute.TEXTFIELD_COLUMNS);
            input.setEditable(false);
            final DialogAttributeTreasure<G, A, R> tmpAttribute = new DialogAttributeTreasure<G, A, R>(archetypeAttribute, input, treasureTree);
            final JButton cLabel = new JButton(new ViewTreasurelistAL(input, GameObjectAttributesDialog.this, treasureListTree));
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeTreasure>(tmpAttribute, cLabel, input, null, null, false);
        }

        @Override
        public void visit(@NotNull final ArchetypeAttributeZSpell archetypeAttribute) {
            final JLabel cLabel = new JLabel(archetypeAttribute.getAttributeName() + ": ");
            cLabel.setForeground(INT_COLOR);
            // create ComboBox with parsed selection
            @Nullable final JComboBox input = buildSpellBox(gameObject, numberSpells, undefinedSpellIndex, true, archetypeAttribute);
            guiInfo = new GuiInfo<G, A, R, ArchetypeAttributeZSpell>(new DialogAttributeZSpell<G, A, R>(archetypeAttribute, input, numberSpells, undefinedSpellIndex), cLabel, input, null, null, false);
        }

    };

    /**
     * Tracks {@link #mapModel}'s map for changes: when the {@link #gameObject}
     * is removed, cancels this dialog.
     */
    @NotNull
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            if (gameObject.getMapSquare() == null) {
                setValue(cancelButton);
            }
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            if (gameObject.getMapSquare() != mapModel.getMapSquare(mapPos)) {
                setValue(cancelButton);
            }
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            if (gameObject.getMapSquare() == null) {
                setValue(cancelButton);
            }
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            // ignore
        }

    };

    /**
     * The {@link MapManagerListener} to detect closed maps.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            // ignore
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            if (mapControl.getMapModel() == mapModel) {
                setValue(cancelButton);
            }
        }

    };

    /**
     * The {@link FaceObjectProvidersListener} for detecting reloaded faces.
     */
    @NotNull
    private final FaceObjectProvidersListener faceObjectProvidersListener = new FaceObjectProvidersListener() {

        @Override
        public void facesReloaded() {
           // faceLabel.setIcon(getFace(gameObject));
      /*       facePanel = new JPanel(new BorderLayout());  */
       // JLabel topIcon = new JLabel("");  
     //  JLabel bottomIcon = new JLabel("");  
     //   facePanel.add(topIcon, BorderLayout.NORTH);  
      //  facePanel.add(bottomIcon, BorderLayout.SOUTH);
   //     topIcon.setIcon(faceObjectProviders.getSecondFace(gameObject));
    //   bottomIcon.setIcon(faceObjectProviders.getFace(gameObject));

        //       faceLabel = new JLabel();
     //   JPanel panel = new JPanel(new BorderLayout());  
    /*    JLabel topIcon = new JLabel("");  
       JLabel bottomIcon = new JLabel("");  */
    //   panel.add(topIcon, BorderLayout.NORTH);  
     //   panel.add(bottomIcon, BorderLayout.SOUTH);
     /*   facePanel.add(topIcon, BorderLayout.NORTH);  
       facePanel.add(bottomIcon, BorderLayout.SOUTH); */
       
     //  faceLabel.add(topIcon, BorderLayout.NORTH);  
     //   faceLabel.add(bottomIcon, BorderLayout.SOUTH);
       
        // in the past this displayed the gameobject face, not the archetype face
     // displays arch   topIcon.setIcon(faceObjectProviders.getSecondFace(archetype));
     // displays arch  bottomIcon.setIcon(faceObjectProviders.getFace(archetype));
  /*   topIcon.setIcon(faceObjectProviders.getSecondFace(gameObject));
     bottomIcon.setIcon(faceObjectProviders.getFace(gameObject)); */
    // faceLabel.add(panel);
            
        }

    };

    /**
     * Creates a new instance.
     * @param gameObjectAttributesDialogFactory the associated factory
     * @param archetypeTypeSet the reference to the list of archetype types
     * @param gameObject the game object to show dialog for; must be part of a
     * map
     * @param parent the parent frame for showing dialog boxes
     * @param treasureListTree the treasure list tree to display
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects instance for choosing
     * animation names
     * @param globalSettings the global settings to use
     * @param mapFileFilter the file filter to use for map files
     * @param scriptFileFilter the file filter to use for script files
     * @param faceObjects the face objects instance for choosing face names
     * @param gameObjectSpells the game object spells to use
     * @param numberSpells the numbered spells to use
     * @param undefinedSpellIndex the index for "no spell"
     * @param treasureTree the treasure tree to use
     * @param noFaceSquareIcon the image icon for no animations
     * @param unknownSquareIcon the image icon for undefined animations
     * @param textAreaDefaults the text area defaults for text fields
     * @param mapManager the map manager instance
     */
    public GameObjectAttributesDialog(@NotNull final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory, final ArchetypeTypeSet archetypeTypeSet, @NotNull final G gameObject, @NotNull final JFrame parent, @NotNull final CFTreasureListTree treasureListTree, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final GlobalSettings globalSettings, @NotNull final FileFilter mapFileFilter, @NotNull final FileFilter scriptFileFilter, @NotNull final FaceObjects faceObjects, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull final Spells<NumberSpell> numberSpells, final int undefinedSpellIndex, @NotNull final TreasureTree treasureTree, @NotNull final ImageIcon noFaceSquareIcon, @NotNull final ImageIcon unknownSquareIcon, @NotNull final TextAreaDefaults textAreaDefaults, @NotNull final MapManager<G, A, R> mapManager) {
        this.gameObjectAttributesDialogFactory = gameObjectAttributesDialogFactory;
        this.archetypeTypeSet = archetypeTypeSet;
        this.gameObject = gameObject;
        this.parent = parent;
        this.treasureListTree = treasureListTree;
        this.faceObjectProviders = faceObjectProviders;
        this.animationObjects = animationObjects;
        this.globalSettings = globalSettings;
        this.mapFileFilter = mapFileFilter;
        this.scriptFileFilter = scriptFileFilter;
        this.faceObjects = faceObjects;
        this.gameObjectSpells = gameObjectSpells;
        this.numberSpells = numberSpells;
        this.undefinedSpellIndex = undefinedSpellIndex;
        this.treasureTree = treasureTree;
        this.noFaceSquareIcon = noFaceSquareIcon;
        this.unknownSquareIcon = unknownSquareIcon;
        this.textAreaDefaults = textAreaDefaults;
        this.mapManager = mapManager;
        archetype = gameObject.getArchetype();

        final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
        if (mapSquare == null) {
            throw new IllegalArgumentException();
        }
        mapModel = mapSquare.getMapModel();
        mapPos = mapSquare.getMapLocation();
        mapModel.addMapModelListener(mapModelListener);
        mapManager.addMapManagerListener(mapManagerListener);

        // first split top-left and -right
        final ArchetypeType archetypeType = archetypeTypeSet.getArchetypeTypeByBaseObject(gameObject);
        final JComponent leftPane = buildHeader(archetypeTypeSet.getArchetypeTypeIndex(archetypeType), archetypeType);

        // Now split horizontally
        buildAttribute();
        centerPanel = new JPanel(cardLayout);
        centerPanel.add("edit", tabbedPane);
        summaryTextPane = new JTextPane();
        summaryTextPane.setEditable(false);
        summaryTextPane.setBorder(BorderFactory.createEmptyBorder(3, 15, 0, 0));
        centerPanel.add("summary", new JScrollPane(summaryTextPane));
        final Dimension size = centerPanel.getPreferredSize();
        size.height = 256;
        centerPanel.setMinimumSize(size);
        centerPanel.setPreferredSize(size);

        final Container contentPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 0.0;
        contentPanel.add(leftPane, gbc);
        gbc.weighty = 1.0;
        contentPanel.add(centerPanel, gbc);

        setOptions(buildOptions());

        summaryTextStyle = summaryTextPane.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(summaryTextStyle, Color.black);

        faceObjectProviders.addFaceObjectProvidersListener(faceObjectProvidersListener);

        setMessage(contentPanel);
    }

    /**
     * Creates a new {@link JDialog} instance for this dialog.
     * @return the new dialog
     */
    @NotNull
    public JDialog createDialog() {
        final JDialog dialog = createDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, "attribTitle"));
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setResizable(true);
        dialog.setModal(false);
        dialog.setVisible(true);
        return dialog;
    }
    
    // used by visitor
  //  private FaceComponent createFaceComponent(String text){
  //      return new FaceComponent(text, faceObjects, faceObjectProviders, noFaceSquareIcon, unknownSquareIcon,this);
 //   }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (newValue != UNINITIALIZED_VALUE) {
            gameObjectAttributesDialogFactory.hideAttributeDialog(gameObject);
            mapModel.removeMapModelListener(mapModelListener);
            mapManager.removeMapManagerListener(mapManagerListener);
            faceObjectProviders.removeFaceObjectProvidersListener(faceObjectProvidersListener);
        }
    }

    /**
     * Constructs the combo box of the available archetypes.
     * @param type the initially selected type
     * @param archetypeType the archetype type to display
     * @return a <code>JComponent</code> with the combo box in it
     */
    @NotNull
    private Component buildTypesBox(final int type, @NotNull final ArchetypeType archetypeType) {
        final String[] nameList = new String[archetypeTypeSet.getArchetypeTypeCount()];

        // read all type names
        int i = 0;
        for (final ArchetypeType tmp : archetypeTypeSet) {
            nameList[i++] = " " + tmp.getTypeName();
        }

        // the active type appears selected in the box
        final int selection = type;

        final JComboBox typeComboBox = new JComboBox(nameList);
        typeComboBox.setSelectedIndex(selection);

        //typeComboBox.setKeySelectionManager(new StringKeyManager(typeComboBox));

        typeComboBox.setName("Types");

        // the listener:
        typesBoxItemListener = new TypesBoxItemListener<G, A, R>(this, gameObject, type, archetypeTypeSet, dialogAttributes, typeComboBox, archetypeType);
        typeComboBox.addItemListener(typesBoxItemListener);
        return typeComboBox;
    }

    /**
     * Returns the face for a GameObject.
     * @param gameObject the game object to return face for
     * @return the face for the supplied GameObject
     */
    @NotNull
    private ImageIcon getFace(@NotNull final BaseObject<G, A, R, ?> gameObject) {
        return faceObjectProviders.getFace(gameObject);
    }

    /**
     * Constructs the upper left part of the attribute dialog, containing name,
     * type, archetype name and face.
     * @param type the initially selected type
     * @param archetypeType the archetype type to display
     * @return a <code>JScrollPane</code> with the upper left part of the dialog
     *         window
     */
    @NotNull
    private JComponent buildHeader(final int type, @NotNull final ArchetypeType archetypeType) {
        final JComponent header = new JPanel(new GridBagLayout());   // the final thing, in a panel
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 3.0;
        gbc.gridheight = 3;
        facePanel = new JPanel(new BorderLayout());  
  //      faceLabel = new JLabel();
       // JPanel panel = new JPanel(new BorderLayout());  
  /*      JLabel topIcon = new JLabel("");  
       JLabel bottomIcon = new JLabel("");  */
     //  panel.add(topIcon, BorderLayout.NORTH);  
     //   panel.add(bottomIcon, BorderLayout.SOUTH);
    //   faceLabel.add(topIcon, BorderLayout.NORTH); 
    //   faceLabel.add(bottomIcon, BorderLayout.SOUTH);
    /*    facePanel.add(topIcon, BorderLayout.NORTH);  
        facePanel.add(bottomIcon, BorderLayout.SOUTH); */
        // in the past this displayed the gameobject face, not the archetype face
     // displays arch   topIcon.setIcon(faceObjectProviders.getSecondFace(archetype));
     // displays arch  bottomIcon.setIcon(faceObjectProviders.getFace(archetype));
   /*  topIcon.setIcon(faceObjectProviders.getSecondFace(gameObject));
     bottomIcon.setIcon(faceObjectProviders.getFace(gameObject));  */
  //   faceLabel.add(panel);
     // try to redraw when subwindow facecomponent is updated, referring to center popup
     
      //   setText("");
     //   panel.add(this,BorderLayout.SOUTH);
       // return panel;
       
      //  faceLabel = new JLabel(getFace(gameObject));
      //  faceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
      //  header.add(faceLabel, gbc);
       
     //  header.add(panel,gbc);
       header.add(facePanel,gbc);
   //  header.add(faceLabel,gbc);
        gbc.gridheight = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0.0;
        gbc.gridx = 2;
        gbc.gridy = 1;
        header.add(new JLabel("Name: ", SwingConstants.TRAILING), gbc);  // create label
        gbc.gridy++;
        header.add(new JLabel("Type: ", SwingConstants.TRAILING), gbc);
        gbc.gridy++;
        header.add(new JLabel("Archetype: ", SwingConstants.TRAILING), gbc);  // create label

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        final JTextComponent nameTextField;
        final String objName = gameObject.getObjName();
        if (objName.length() > 0) {
            nameTextField = new JTextField(objName, 16);
        } else {
            final String archObjName = archetype.getObjName();
            final String nameText = archObjName.length() > 0 ? archObjName : archetype.getArchetypeName();
            nameTextField = new JTextField(nameText, 16);
        }
        nameTextField.setEditable(false);
        header.add(nameTextField, gbc);
        gbc.gridy++;
        header.add(buildTypesBox(type, archetypeType), gbc);   // build type-selection box
        gbc.gridy++;
        final JTextComponent archetypeTextField = new JTextField(archetype.getArchetypeName(), 16);
        archetypeTextField.setEditable(false);
        header.add(archetypeTextField, gbc);

        return header;
    }
    
  //  public final void updateTopLabel(Icon bottom, Icon top){
      //   facePanel = new JPanel(new BorderLayout());  
      //  JPanel panel = new JPanel(new BorderLayout());  
     
     //  panel.add(topIcon, BorderLayout.NORTH);  
     //   panel.add(bottomIcon, BorderLayout.SOUTH);
     //   facePanel.add(topIcon, BorderLayout.NORTH);  
     //   facePanel.add(bottomIcon, BorderLayout.SOUTH);
        // in the past this displayed the gameobject face, not the archetype face
     //   faceLabel = new JLabel();
   // JPanel panel = new JPanel(new BorderLayout());  
    //    JLabel topIcon = new JLabel("");  
    //   JLabel bottomIcon = new JLabel("");  
    //   panel.add(topIcon, BorderLayout.NORTH);  
      //  panel.add(bottomIcon, BorderLayout.SOUTH);
      //  facePanel.add(topIcon, BorderLayout.NORTH);  
     //   facePanel.add(bottomIcon, BorderLayout.SOUTH);
     //   faceLabel.add(topIcon, BorderLayout.NORTH);  
     //   faceLabel.add(bottomIcon, BorderLayout.SOUTH);
       
        // in the past this displayed the gameobject face, not the archetype face
     // displays arch   topIcon.setIcon(faceObjectProviders.getSecondFace(archetype));
     // displays arch  bottomIcon.setIcon(faceObjectProviders.getFace(archetype));
    
  //   topIcon.setIcon(top);
  //   bottomIcon.setIcon(bottom);
    // faceLabel.add(panel);
  //  }

    /**
     * Constructs the central part of the attribute dialog, containing the
     * object's gameObject attributes.
     */
    public final void buildAttribute() {
        tabbedPane.removeAll();
        facePanel.removeAll();
        foundImage = false;
        final ArchetypeType archetypeType = typesBoxItemListener.getArchetypeType();
        for (int sectionId = 0; sectionId < archetypeType.getSectionNum(); sectionId++) {
            final Component panel = makeAttributePanel(sectionId);
            if (panel != null) {
                final String sectionName = archetypeType.getSectionName(sectionId);
                tabbedPane.addTab(sectionName, null, panel);
            }
        }
        
        // set selected tab
        if(tabbedPane.getComponentCount() > 0){
        tabbedPane.setSelectedIndex(0);
        }
        tabbedPane.validate();
    }

    /**
     * This method creates an attribute panel for one section of attributes. If
     * the section is empty, null is returned.
     * @param sectionId the identifier of the section
     * @return a <code>Component</code> containing the attribute panel
     *         (currently always a JScrollPane)
     */
    @Nullable
    private Component makeAttributePanel(final int sectionId) {
        int matchingAttributes = 0;   // number of attributes in this section
        boolean hasBitmask = false;  // true if this section contains a bitmask attribute

        // first we check how many attributes this section has
        for (final ArchetypeAttribute archetypeAttribute : typesBoxItemListener.getArchetypeType()) {
            if (archetypeAttribute.getSectionId() == sectionId) {
                // count number of attributes
                if (!(archetypeAttribute instanceof ArchetypeAttributeFixed)) {
                    matchingAttributes++;
                }
                // check for bitmask attributes
                if (!hasBitmask && archetypeAttribute instanceof ArchetypeAttributeBitmask) {
                    hasBitmask = true;
                }
            }
        }
        if (matchingAttributes == 0) {
            return null;
        }

        // All attribute-"lines" go into this panel:
        final JPanel panel = new JPanel(new GridBagLayout());
        final Insets gbcInsets = new Insets(2, 2, 2, 2);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = gbcInsets;
        final Object helpGbc = gbc.clone();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        final Object labelGbc = gbc.clone();
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        final Object compGbc = gbc.clone();
        gbc.anchor = GridBagConstraints.WEST;
        final Object rowGbc = gbc.clone();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        final Object glueGbc = gbc.clone();

        boolean isText = false;
        // now add the entries, line by line
        for (final ArchetypeAttribute archetypeAttribute : typesBoxItemListener.getArchetypeType()) {
            if (archetypeAttribute.getSectionId() == sectionId && !(archetypeAttribute instanceof ArchetypeAttributeFixed)) {
                final AbstractButton helpButton = new JButton("?");
                helpButton.setMargin(new Insets(0, 5, 0, 5));
                panel.add(helpButton, helpGbc);
                helpButton.addFocusListener(focusListener);

                guiInfo = null;
                archetypeAttribute.visit(archetypeAttributeVisitor);
                final GuiInfo<G, A, R, ?> tmpGuiInfo = guiInfo;
                assert tmpGuiInfo != null;

                dialogAttributes.add(tmpGuiInfo.getNewAttr());
                helpButton.addActionListener(new HelpActionListener(tmpGuiInfo.getNewAttr().getRef(), this));
                addElement(panel, tmpGuiInfo.getCLabel(), labelGbc);
                addElement(panel, tmpGuiInfo.getCComp(), compGbc);
                addElement(panel, tmpGuiInfo.getCRow(), rowGbc);
                addElement(panel, tmpGuiInfo.getCGlue(), glueGbc);
                isText |= tmpGuiInfo.isText();
            }
        }

        if (!isText) {
            // if the component does not already have glue, put glue inside to align its contents to the top.
            panel.add(Box.createGlue(), glueGbc);
        }
        final JScrollPane panelReturn = new JScrollPane(panel);
        panelReturn.getVerticalScrollBar().setUnitIncrement(8);
        return panelReturn;
    }

    /**
     * Adds a {@link Component} to a {@link Container}.
     * @param container the container to add to
     * @param component the component to add or <code>null</code>
     * @param constraints the constraints
     */
    private void addElement(@NotNull final Container container, @Nullable final Component component, @NotNull final Object constraints) {
        if (component != null) {
            container.add(component, constraints);
            component.addFocusListener(focusListener);
        }
    }

    /**
     * Constructs the dialog options: help, default, okay, apply, cancel.
     * @return Object[] with dialog options
     */
    @NotNull
    private Object[] buildOptions() {
        summaryEditButton = new JButton(summaryAction);
        okButton = new JButton(ACTION_BUILDER.createAction(false, "attribOk", this));
        cancelButton = new JButton(ACTION_BUILDER.createAction(false, "attribCancel", this));
        return new Object[] { new JButton(ACTION_BUILDER.createAction(false, "attribHelp", this)), summaryEditButton, Box.createHorizontalStrut(32), okButton, new JButton(ACTION_BUILDER.createAction(false, "attribApply", this)), cancelButton, };
    }

    /**
     * Action method for help.
     */
    @ActionMethod
    public void attribHelp() {
        final ArchetypeType archetypeType = typesBoxItemListener.getArchetypeType();
        final String helpText = ACTION_BUILDER.format("arcDoc.htmlText", archetypeType.getTypeName(), archetypeType.getDescription(), archetypeType.getUse());
        new Help(/* XXX */ parent, helpText).setVisible(true);
    }

    /**
     * Action method for ok.
     */
    @ActionMethod
    public void attribOk() {
        if (applySettings()) {
            setValue(okButton);
        }
    }

    /**
     * Action method for apply.
     */
    @ActionMethod
    public void attribApply() {
        applySettings();
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void attribCancel() {
        setValue(cancelButton);
    }

    /**
     * Action method for summary. Switches the card layout to the summary list
     * of all nonzero attributes.
     */
    @ActionMethod
    public void attribSummary() {
        // interface is displayed, switch to summary
        final Document doc = summaryTextPane.getDocument();

        try {
            // clear document
            if (doc.getLength() > 0) {
                doc.remove(0, doc.getLength());
            }

            // now loop through all attributes and write out nonzero ones
            for (final DialogAttribute<?, ?, ?, ?> dialogAttribute : dialogAttributes) {
                dialogAttribute.appendSummary(doc, summaryTextStyle);
            }
        } catch (final BadLocationException e) {
            log.error("toggleSummary: Bad Location in Document!", e);
        }

        summaryTextPane.setCaretPosition(0); // this prevents the document from scrolling down
        summaryEditButton.setAction(editAction);
        cardLayout.show(centerPanel, "summary");
    }

    /**
     * Turns the summary off. Switches to the input-interface for all attributes
     * and the summary list of all nonzero attributes.
     */
    @ActionMethod
    public void attribEdit() {
        summaryEditButton.setAction(summaryAction);
        cardLayout.show(centerPanel, "edit");
    }

    /**
     * This method is called when the "apply"-button has been pressed. All the
     * settings from the dialog get written into the GameObject.
     * @return true if the settings were applied, false if error occurred
     */
    private boolean applySettings() {
        final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
        assert mapSquare != null;
        final MapModel<G, A, R> tmpMapModel = mapSquare.getMapModel();
        tmpMapModel.beginTransaction("Change object attributes");
        try {
            return applySettings2();
        } finally {
            tmpMapModel.endTransaction();
        }
    }

    /**
     * This method is called when the "apply"-button has been pressed. All the
     * settings from the dialog get written into the GameObject.
     * @return true if the settings were applied, false if error occurred
     */
    private boolean applySettings2() {
        final ArchetypeType tmpArchetypeType = archetypeTypeSet.getArchetypeTypeByBaseObject(gameObject); // the type structure for this gameObject

        final StringBuilder newArchText = new StringBuilder();
        final String[] newMsg = new String[1];
        for (final DialogAttribute<G, A, R, ?> dialogAttribute : dialogAttributes) {
            final String text = dialogAttribute.getText2(gameObject, archetype, newMsg, tmpArchetypeType, this);
            if (text == null) {
                return false;
            }
            if (text.length() > 0) {
                newArchText.append(text).append("\n");
            }
        }

        // Also write all the 'fixed' attributes into the archetype text
        for (final ArchetypeAttribute archetypeAttribute : typesBoxItemListener.getArchetypeType()) {
            // ### TODO: for changed types, copy fixed attributes over default arches ###
            if (archetypeAttribute instanceof ArchetypeAttributeFixed) {
                final String defaultValue = archetype.getAttributeString(archetypeAttribute.getArchetypeAttributeName());
                if (defaultValue.length() == 0 || (gameObject.getTypeNo() != archetype.getTypeNo() && !defaultValue.equalsIgnoreCase(archetypeAttribute.getAttributeName()))) {
                    // usually, fixed attributes are only applied when *not* defined in the archetype.
                    // the reason behind this is: if the default gameObject violates our fixed attribute,
                    // we assume the default gameObject is "right" and we are "wrong". The typedefs aren't that trustworthy.
                    // BUT - if the gameObject has a changed type, the archetype has lost it's credibility.
                    // So, in this special case, the fixed attribute applies always.
                    newArchText.append(archetypeAttribute.getArchetypeAttributeName()).append(" ").append(archetypeAttribute.getAttributeName()).append("\n");
                }
            }
        }

        // before we modify the archetype text, we look for errors and save them.
        // later the user must confirm whether to keep or dump those errors
        final String errors = GameObjectUtils.getSyntaxErrors(gameObject, tmpArchetypeType);

        // --- parsing succeeded, now we write it into the gameObject/map ---
        final int typeNo = gameObject.getTypeNo();
        gameObject.setObjectText(newArchText.toString());
        gameObject.setAttributeInt(BaseObject.TYPE, typeNo);

        if (newMsg[0] != null) {
            // set new msg text only when it is not equal to Archetype
            final String msgText = StringUtils.removeTrailingWhitespaceFromLines(newMsg[0]);
            final String archetypeMsgText = archetype.getMsgText();
            gameObject.setMsgText(msgText.equals(archetypeMsgText == null ? "" : archetypeMsgText) ? null : msgText);
        } else {
            final CharSequence archetypeMsgText = archetype.getMsgText();
            gameObject.setMsgText(archetypeMsgText != null && archetypeMsgText.length() > 0 ? "" : null);
        }

        if(firstRef != null){
            firstRef.modifyFacePanel();
        }
        // now lets assign the visible face - perhaps we have still an animation
  //      ImageIcon tmp1 = getFace(gameObject);
  //     ImageIcon tmp2 = getFace(gameObject);
   //   ImageIcon tmp2 =faceObjectProviders.getSecondFace(gameObject);
      //  if(tmp1 != null){
      //  faceLabel.setIcon(tmp1);
      //  }
        // does this break concurrent action?
   /*     facePanel = new JPanel(new BorderLayout());  
       JLabel topIcon = new JLabel("");  
       JLabel bottomIcon = new JLabel("");  
        facePanel.add(topIcon, BorderLayout.NORTH);  
        facePanel.add(bottomIcon, BorderLayout.SOUTH); */
   //   if(retrievedPanel != null){
    //      facePanel = retrievedPanel;
          // do not copy its position though
 //     }
 //     else{
   /*   topIcon.setIcon(faceObjectProviders.getSecondFace(gameObject));
      bottomIcon.setIcon(faceObjectProviders.getFace(gameObject));  */
      //  topIcon.setIcon(tmp1);
    //  bottomIcon.setIcon(tmp2);
        
  //    }
   //   facePanel.repaint();
        //   faceLabel = new JLabel();
     //   JPanel panel = new JPanel(new BorderLayout());  
     //   JLabel topIcon = new JLabel("");  
     //  JLabel bottomIcon = new JLabel("");  
   //    panel.add(topIcon, BorderLayout.NORTH);  
    //    panel.add(bottomIcon, BorderLayout.SOUTH);
      //  facePanel.add(topIcon, BorderLayout.NORTH);  
     //   facePanel.add(bottomIcon, BorderLayout.SOUTH);
    //   faceLabel.add(topIcon, BorderLayout.NORTH);  
   //     faceLabel.add(bottomIcon, BorderLayout.SOUTH);
        // in the past this displayed the gameobject face, not the archetype face
     // displays arch   topIcon.setIcon(faceObjectProviders.getSecondFace(archetype));
     // displays arch  bottomIcon.setIcon(faceObjectProviders.getFace(archetype));
   //  topIcon.setIcon(faceObjectProviders.getSecondFace(gameObject));
   //  bottomIcon.setIcon(faceObjectProviders.getFace(gameObject));
     // faceLabel.add(panel);

        // deal with syntax errors now
        if (errors != null) {
            // For the fallback archetype type, all errors are automatically
            // kept. "Misc" is no real type - it is more a default mask for
            // unknown types. For all other archetype types, a popup dialog is
            // opened and the user may decide what to do with his errors.
            final boolean keepErrors = archetypeTypeSet.isFallbackArchetypeType(tmpArchetypeType) || ConfirmErrorsDialog.askConfirmErrors(errors, this);
            if (keepErrors) {
                gameObject.addObjectText(errors.trim());
            }
        }

        return true; // apply succeeded
    }

    /**
     * Constructs the combo box of the available spells.
     * @param gameObjectSpells the game object spells to use
     * @param isOptionalSpell whether the entry &lt;none&gt; should be shown
     * @param attributeName the archetype attribute's name
     * @param gameObject the associated game object
     * @return the completed <code>JComboBox</code>
     */
    @NotNull
    private static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> JComboBox buildInvSpellBox(@NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull final BaseObject<?, ?, ?, ?> gameObject, final boolean isOptionalSpell, @NotNull final String attributeName) {
        final int selectedIndex;
        @Nullable final String title;
        switch (gameObject.countInvObjects()) {
        case 0:
            selectedIndex = gameObjectSpells.size();
            title = isOptionalSpell ? null : "<none>";
            break;

        default:
            selectedIndex = gameObjectSpells.size() + (isOptionalSpell ? 1 : 0);
            title = "<multiple>";
            break;

        case 1:
            final GameObject<?, ?, ?> invObject = gameObject.iterator().next();
            final String invObjectArchetypeName = invObject.getArchetype().getArchetypeName();
            int index = 0;
            @Nullable String tmpTitle = "<customized spell>";
            for (final GameObjectSpell<?, ?, ?> spellObject : gameObjectSpells) {
                if (invObjectArchetypeName.equals(spellObject.getArchetypeName())) {
                    tmpTitle = invObject.isDefaultGameObject() ? null : spellObject.getName() + " <customized>";
                    break;
                }
                index++;
            }
            selectedIndex = tmpTitle != null ? gameObjectSpells.size() + (isOptionalSpell ? 1 : 0) : index;
            title = tmpTitle;
            break;
        }

        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (final Spell spell : gameObjectSpells) {
            model.addElement(spell.getName());
        }
        if (isOptionalSpell) {
            model.addElement("<none>");
        }
        if (title != null) {
            model.addElement(title);
        }
        final JComboBox comboBox = new JComboBox(model);
        comboBox.setSelectedIndex(selectedIndex);
        comboBox.setMaximumRowCount(10);
        comboBox.setKeySelectionManager(new StringKeyManager(comboBox));
        comboBox.setName(attributeName);
        return comboBox;
    }

    /**
     * Constructs the combo box for arrays of "list data".
     * @param listData list with list items and corresponding values
     * @param gameObject the associated game object
     * @param archetypeAttributeName the name of the shown attribute
     * @param attributeName the label to show on the combo box
     * @return the completed <code>JComboBox</code>
     */
    @NotNull
    private static JComboBox buildArrayBox(@NotNull final ArchetypeTypeList listData, @NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final String archetypeAttributeName, @NotNull final String attributeName) {
        // build the array of list-items
        final String[] array = new String[listData.size()];
        boolean hasSelection = false;
        int active = gameObject.getAttributeInt(archetypeAttributeName);
        for (int i = 0; i < array.length; i++) {
            array[i] = listData.get(i).getSecond();
            if (!hasSelection && listData.get(i).getFirst() == active) {
                hasSelection = true;
                active = i;          // set selection to this index in the array
            }
        }
        // if there is no valid pre-selection, show first element of list
        if (!hasSelection) {
            active = 0;
        }
        final JComboBox comboBox = new JComboBox(array);  // set "content"
        comboBox.setSelectedIndex(active); // set active selection
        comboBox.setMaximumRowCount(10);
        comboBox.setKeySelectionManager(new StringKeyManager(comboBox));
        comboBox.setName(attributeName);
        return comboBox;
    }

    /**
     * Constructs the combo box of the available spells.
     * @param gameObject the associated game object
     * @param numberSpells the number spells to use
     * @param undefinedSpellIndex the index to use for the undefined spell
     * @param isZSpell whether a z-spell or a spell combo box is built
     * @param archetypeAttribute the archetype attribute
     * @return the completed <code>JComboBox</code>
     */
    @NotNull
    private static JComboBox buildSpellBox(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final Spells<NumberSpell> numberSpells, final int undefinedSpellIndex, final boolean isZSpell, @NotNull final ArchetypeAttribute archetypeAttribute) {
        int spellNumber = gameObject.getAttributeInt(archetypeAttribute.getArchetypeAttributeName());

        if (spellNumber < 0 || spellNumber >= numberSpells.size()) {
            spellNumber = undefinedSpellIndex;
        }

        // do we have "none" spell?
        final int selectedIndex;
        if (spellNumber == undefinedSpellIndex && isZSpell) {
            selectedIndex = 0;
        } else {
            // now look up the spell-number in the array of spells
            selectedIndex = 1 + AbstractArchetypeAttributeSpell.findSpellIndex(numberSpells, spellNumber);
        }

        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("<none>");
        for (final Spell spell : numberSpells) {
            model.addElement(spell.getName());
        }
        final JComboBox comboBox = new JComboBox(model);
        comboBox.setSelectedIndex(selectedIndex);
        comboBox.setMaximumRowCount(10);
        comboBox.setKeySelectionManager(new StringKeyManager(comboBox));
        comboBox.setName(archetypeAttribute.getAttributeName());
        return comboBox;
    }

}
