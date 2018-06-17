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

package net.sf.gridarta.gui.panel.selectedsquare;

import java.awt.Point;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.NotNull;

/**
 * Implementing actions that operate on {@link SelectedSquareModel
 * SelectedSquareModels}.
 * @author Andreas Kirschbaum
 */
public class SelectedSquareActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link SelectedSquareModel} for this controller.
     */
    @NotNull
    private final SelectedSquareModel<G, A, R> selectedSquareModel;

    /**
     * Creates a new instance.
     * @param selectedSquareModel the selected square model to operate on
     */
    public SelectedSquareActions(@NotNull final SelectedSquareModel<G, A, R> selectedSquareModel) {
        this.selectedSquareModel = selectedSquareModel;
    }

    /**
     * Executes the "move square prev" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquarePrev(final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        G prevGameObject = gameObject.getPrev();
        if (prevGameObject == null) {
            prevGameObject = gameObject.getContainerGameObject();
            if (prevGameObject == null) {
                return false;
            }
        } else if (performAction) {
            while (true) {
                final G tmp = prevGameObject.getFirst();
                if (tmp == null) {
                    break;
                }
                prevGameObject = tmp;
            }
        }

        if (performAction) {
            selectedSquareModel.setSelectedGameObject(prevGameObject);
        }

        return true;
    }

    /**
     * Executes the "move square next" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareNext(final boolean performAction) {
        final G gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        G nextGameObject = gameObject.getLast();
        if (nextGameObject == null) {
            nextGameObject = gameObject;
            while (true) {
                final G tmp = nextGameObject.getNext();
                if (tmp != null) {
                    nextGameObject = tmp;
                    break;
                }
                nextGameObject = nextGameObject.getContainerGameObject();
                if (nextGameObject == null) {
                    return false;
                }
            }
        }

        if (performAction) {
            selectedSquareModel.setSelectedGameObject(nextGameObject);
        }

        return true;
    }

    /**
     * Executes the "move square top" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareTop(final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        if (gameObject.isTop()) {
            return false;
        }

        if (performAction) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Move Top");
            try {
                gameObject.moveTop();
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "move square up" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareUp(final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        if (gameObject.isTop()) {
            return false;
        }

        if (performAction) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Move Up");
            try {
                gameObject.moveUp();
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "move square down" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareDown(final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        if (gameObject.isBottom()) {
            return false;
        }

        if (performAction) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Move Down");
            try {
                gameObject.moveDown();
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "move square bottom" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareBottom(final boolean performAction) {
        final GameObject<G, A, R> gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        if (gameObject.isBottom()) {
            return false;
        }

        if (performAction) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Move Bottom");
            try {
                gameObject.moveBottom();
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "move square env" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareEnv(final boolean performAction) {
        final G gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        final G envGameObject = gameObject.getContainerGameObject();
        if (envGameObject == null) {
            return false;
        }

        final MapSquare<G, A, R> mapSquare = selectedSquareModel.getSelectedMapSquare();
        if (mapSquare == null) {
            return false;
        }

        final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
        final Point pos = mapSquare.getMapLocation();
        if (!envGameObject.isInContainer() && gameObject.getArchetype().isMulti() && !mapModel.isMultiArchFittingToMap(gameObject.getArchetype(), pos, true)) {
            return false;
        }

        if (performAction) {
            mapModel.beginTransaction("Move To Environment");
            try {
                mapModel.moveEnv(gameObject, pos, envGameObject);
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

    /**
     * Executes the "move square inv" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doMoveSquareInv(final boolean performAction) {
        final G gameObject = selectedSquareModel.getSelectedGameObject();
        if (gameObject == null || !gameObject.isHead()) {
            return false;
        }

        final GameObject<G, A, R> prevGameObject = gameObject.getPrev();
        if (prevGameObject == null) {
            return false;
        }

        if (performAction) {
            final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
            assert mapSquare != null;
            final MapModel<G, A, R> mapModel = mapSquare.getMapModel();
            mapModel.beginTransaction("Move To Inventory");
            try {
                mapModel.moveInv(gameObject, prevGameObject.getHead());
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

}
