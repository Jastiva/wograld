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

package net.sf.gridarta.gui.dialog.findarchetypes;

import java.awt.Component;
import net.sf.gridarta.gui.panel.archetypechooser.ArchetypeChooserControl;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.gui.utils.AbstractDialogManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog manager for the find archetypes dialog.
 * @author Andreas Kirschbaum
 */
public class FindArchetypesDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractDialogManager {

    /**
     * The synchronization object for accessing {@link #findArchetypesDialog}.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * The {@link FindArchetypesDialogManager} instance or <code>null</code> if
     * the dialog isn't built yet.
     */
    @Nullable
    private FindArchetypesDialog<G, A, R> findArchetypesDialog;

    /**
     * The parent {@link Component} for the dialog.
     */
    @NotNull
    private final Component parent;

    /**
     * The {@link ArchetypeChooserControl} to search.
     */
    @NotNull
    private final ArchetypeChooserControl<G, A, R> archetypeChooserControl;

    /**
     * The {@link ObjectChooser} to use when selecting search results.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link ArchetypeTypeSet} for looking up archetype types.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * Creates a new instance.
     * @param parent the parent component for the dialog
     * @param archetypeChooserControl the archetype chooser control to search
     * @param objectChooser the object chooser to use when selecting search
     * results
     * @param archetypeTypeSet the archetype type set for looking up archetype
     * types
     */
    public FindArchetypesDialogManager(@NotNull final Component parent, @NotNull final ArchetypeChooserControl<G, A, R> archetypeChooserControl, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        super("findArchetypes");
        this.parent = parent;
        this.archetypeChooserControl = archetypeChooserControl;
        this.objectChooser = objectChooser;
        this.archetypeTypeSet = archetypeTypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDialog() {
        FindArchetypesDialog<G, A, R> dialog;
        synchronized (sync) {
            dialog = findArchetypesDialog;
            if (dialog == null) {
                dialog = new FindArchetypesDialog<G, A, R>(parent, archetypeChooserControl, objectChooser, archetypeTypeSet);
                findArchetypesDialog = dialog;
            }
        }
        dialog.show();
    }

    /**
     * Action method for "find archetype".
     */
    @ActionMethod
    public void findArchetypes() {
        showDialog();
    }

}
