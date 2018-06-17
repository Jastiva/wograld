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

package net.sf.gridarta.model.validation.checks;

import java.io.File;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.GameObjectValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.ExitError;
import org.jetbrains.annotations.NotNull;

/**
 * A Validator to assert that exits are connected to maps properly.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ExitChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements GameObjectValidator<G, A, R> {

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The archetype type number of exits.
     */
    private final int exitTypeNo;

    /**
     * Create a ExitChecker.
     * @param validatorPreferences the validator preferences to use
     * @param globalSettings the global settings to use
     * @param exitTypeNo the archetype type number of exits
     */
    public ExitChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GlobalSettings globalSettings, final int exitTypeNo) {
        super(validatorPreferences);
        this.globalSettings = globalSettings;
        this.exitTypeNo = exitTypeNo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGameObject(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (gameObject.getTypeNo() != exitTypeNo) {
            return;
        }

        final String path = gameObject.getAttributeString(BaseObject.SLAYING, false);
        if (path.length() <= 0 || path.equals("/!") || path.startsWith("/random/")) {
            return;
        }

        final File newFile;
        if (path.startsWith(File.pathSeparator) || path.startsWith("/")) {
            // we have an absolute path:
            newFile = new File(globalSettings.getMapsDirectory().getAbsolutePath(), path.substring(1));
        } else {
            // we have a relative path:
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final File mapFile = mapSquare.getMapModel().getMapFile();
            if (mapFile == null) {
                // unsaved map => do not check
                return;
            }
            newFile = new File(mapFile.getParent(), path);
        }
        if (newFile.exists() && !newFile.isDirectory()) {
            return;
        }

        errorCollector.collect(new ExitError<G, A, R>(gameObject, path));
    }

}
