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
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.validation.AbstractValidator;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.MapValidator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.TilePathsError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Validator that checks whether all tile paths are valid.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class TilePathsChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractValidator<G, A, R> implements MapValidator<G, A, R> {

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The number of tile paths to check.
     */
    private final int tilePaths;

    /**
     * Create a TilePathsChecker.
     * @param validatorPreferences the validator preferences to use
     * @param globalSettings the global settings to use
     * @param tilePaths the number of tile paths to check
     */
    public TilePathsChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GlobalSettings globalSettings, final int tilePaths) {
        super(validatorPreferences);
        this.globalSettings = globalSettings;
        this.tilePaths = tilePaths;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final File mapFile = mapModel.getMapFile();
        final String mapDir = mapFile == null ? null : mapFile.getParent();
        final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
        final Direction[] directions = Direction.values();
        for (int direction = 0; direction < tilePaths; direction++) {
            final String path = mapArchObject.getTilePath(directions[direction]);
            if (path.length() > 0) {
                @Nullable final File newFile;
                if (path.startsWith(File.pathSeparator) || path.startsWith("/")) {
                    // we have an absolute path:
                    newFile = new File(globalSettings.getMapsDirectory().getAbsolutePath(), path.substring(1));
                } else {
                    // we have a relative path:
                    newFile = mapDir == null ? null : new File(mapDir, path);
                }
                if (newFile != null && (!newFile.exists() || newFile.isDirectory())) {
                    errorCollector.collect(new TilePathsError<G, A, R>(mapModel, directions[direction], path));
                }
            }
        }
    }

}
