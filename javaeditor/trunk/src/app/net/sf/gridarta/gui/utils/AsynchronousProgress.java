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

package net.sf.gridarta.gui.utils;

import java.awt.Component;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import net.sf.japi.swing.misc.Progress;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a {@link Progress} that forwards to another <code>Progress</code>
 * instance but can be used from threads other than EDT.
 * @author Andreas Kirschbaum
 */
public class AsynchronousProgress implements Progress {

    /**
     * The {@link Progress} instance to forward to.
     */
    @NotNull
    private final Progress progress;

    /**
     * Creates a new instance.
     * @param progress the <code>Progress</code> instance to forward to
     */
    public AsynchronousProgress(@NotNull final Progress progress) {
        this.progress = progress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    progress.finished();
                }

            });
        } catch (final InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } catch (final InvocationTargetException ex) {
            // XXX: ignore?
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getParentComponent() {
        return progress.getParentComponent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLabel(final String msg, final int max) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                progress.setLabel(msg, max);
            }

        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final int value) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                progress.setValue(value);
            }

        });
    }

} // AsynchronousProgress
