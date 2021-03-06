/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.gui.list;

import com.realtime.wograld.jxclient.gui.gui.GUIElement;
import com.realtime.wograld.jxclient.gui.gui.GUIElementChangedListener;
import com.realtime.wograld.jxclient.gui.gui.GUIElementListener;
import com.realtime.wograld.jxclient.gui.gui.GuiUtils;
import com.realtime.wograld.jxclient.gui.gui.TooltipManager;
import com.realtime.wograld.jxclient.gui.item.GUIItemItem;
import com.realtime.wograld.jxclient.gui.item.GUIItemItemFactory;
import com.realtime.wograld.jxclient.gui.label.AbstractLabel;
import com.realtime.wograld.jxclient.items.CfItem;
import com.realtime.wograld.jxclient.items.ItemView;
import com.realtime.wograld.jxclient.items.LocationsListener;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link GUIList} instance that displays {@link GUIItemItem} instances.
 * @author Andreas Kirschbaum
 */
public class GUIItemList extends GUIList {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * The {@link GUIItemItemFactory} for creating new {@link GUIItemItem}
     * instances.
     */
    @NotNull
    private final GUIItemItemFactory itemItemFactory;

    /**
     * The {@link ItemView} to monitor.
     */
    @NotNull
    private final ItemView itemView;

    /**
     * The label to update with information about the selected item.
     */
    @Nullable
    private final AbstractLabel currentItem;

    /**
     * The {@link LocationsListener} to be notified about changes.
     */
    @NotNull
    private final LocationsListener locationsListener = new LocationsListener() {

        @Override
        public void locationsModified(@NotNull final Integer[] changedSlots) {
            rebuildList(changedSlots);
        }

    };

    /**
     * The {@link GUIElementChangedListener} attached to all {@link GUIItemItem}
     * instances in the list.
     */
    @NotNull
    private final GUIElementChangedListener itemChangedListener = new GUIElementChangedListener() {

        @Override
        public void notifyChanged(@NotNull final GUIElement element) {
            selectionChanged();
            setChanged();
        }

    };

    /**
     * Creates a new instance.
     * @param tooltipManager the tooltip manager to update
     * @param elementListener the element listener to notify
     * @param name the name of this element
     * @param cellWidth the width of cells
     * @param cellHeight the height of cells
     * @param itemView the item view to monitor
     * @param currentItem the label to update with information about the
     * selected item.
     * @param itemItemFactory the factory for creating item instances
     */
    public GUIItemList(@NotNull final TooltipManager tooltipManager, @NotNull final GUIElementListener elementListener, @NotNull final String name, final int cellWidth, final int cellHeight, @NotNull final ItemView itemView, @Nullable final AbstractLabel currentItem, @NotNull final GUIItemItemFactory itemItemFactory) {
    //    super(tooltipManager, elementListener, name, cellWidth, cellHeight, new ItemItemCellRenderer(itemItemFactory.newTemplateItem(cellHeight)), null);
        super(tooltipManager, elementListener, name, 64, 64, new ItemItemCellRenderer(itemItemFactory.newTemplateItem(64)), null);
        this.itemView = itemView;
        this.itemItemFactory = itemItemFactory;
        this.currentItem = currentItem;
        setLayoutOrientation(JList.HORIZONTAL_WRAP, -1);
        this.itemView.addLocationsListener(locationsListener);
        rebuildList(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();
        itemView.removeLocationsListener(locationsListener);
    }

    /**
     * Rebuilds the list cells.
     * @param changedSlots the changed slots; <code>null</code>=all slots
     */
    private void rebuildList(@Nullable final Integer[] changedSlots) {
        synchronized (getTreeLock()) {
            final int newSize = itemView.getSize();
            final int oldSize = resizeElements(newSize);
            if (oldSize < newSize) {
                for (int i = oldSize; i < newSize; i++) {
                    final GUIElement item = itemItemFactory.newItem(i);
                    addElement(item);
                    item.setChangedListener(itemChangedListener);
                }
                setChanged(changedSlots, oldSize);
            } else {
                setChanged(changedSlots, newSize);
            }
            selectionChanged();
        }
        setChanged();
    }

    /**
     * Marks some slots as modified.
     * @param changedSlots the slots to mark as modified; <code>null</code>=all
     * @param limit the limit; only slots less than this index are affected
     */
    private void setChanged(@Nullable final Integer[] changedSlots, final int limit) {
        if (changedSlots == null) {
            for (int i = 0; i < limit; i++) {
                setChanged(i);
            }
        } else {
            for (final int i : changedSlots) {
                if (i < limit) {
                    setChanged(i);
                }
            }
        }
    }

    /**
     * Marks one slot as modified.
     * @param index the slot index
     */
    private void setChanged(final int index) {
        getElement(index).setChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void selectionChanged(final int selectedIndex) {
        if (currentItem != null) {
            final CfItem item = itemView.getItem(selectedIndex);
            if (item == null) {
                currentItem.setText("");
                currentItem.setTooltipText("");
            } else {
                final String tooltipText1 = item.getTooltipText1();
                final String tooltipText2 = item.getTooltipText2();
                final String tooltipText3 = item.getTooltipText3();
                if (tooltipText3.isEmpty()) {
                    currentItem.setText(tooltipText1+" "+tooltipText3);
                } else {
                    currentItem.setText(tooltipText1+" ["+tooltipText2+"] "+tooltipText3);
                }
                currentItem.setTooltipText(item.getTooltipText());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateTooltip(final int index, final int x, final int y, final int w, final int h) {
        final CfItem item = itemView.getItem(index);
        setTooltipText(item == null ? null : item.getTooltipText(), x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void activeChanged() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseClicked(@NotNull final MouseEvent e) {
        super.mouseClicked(e);
        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            GuiUtils.setActive(this, true);
            button1Clicked(e.getModifiersEx());
            break;

        case MouseEvent.BUTTON2:
            button2Clicked(e.getModifiersEx());
            break;

        case MouseEvent.BUTTON3:
            button3Clicked(e.getModifiersEx());
            break;
        }
    }

    /**
     * Called if the user has clicked the left mouse button.
     * @param modifiers the active modifiers
     */
    private void button1Clicked(final int modifiers) {
        final GUIItemItem guiItem = getSelectedItem();
        if (guiItem == null) {
            return;
        }

        guiItem.button1Clicked(modifiers);
    }

    /**
     * Called if the user has clicked the middle mouse button.
     * @param modifiers the active modifiers
     */
    private void button2Clicked(final int modifiers) {
        final GUIItemItem guiItem = getSelectedItem();
        if (guiItem == null) {
            return;
        }

        guiItem.button2Clicked(modifiers);
    }

    /**
     * Called if the user has clicked the right mouse button.
     * @param modifiers the active modifiers
     */
    private void button3Clicked(final int modifiers) {
        final GUIItemItem guiItem = getSelectedItem();
        if (guiItem == null) {
            return;
        }

        guiItem.button3Clicked(modifiers);
    }

    /**
     * Returns the selected {@link GUIItemItem} instance.
     * @return the selected instance or <code>null</code> if none is selected
     */
    @Nullable
    public GUIItemItem getSelectedItem() {
        return (GUIItemItem)getSelectedObject();
    }

}
