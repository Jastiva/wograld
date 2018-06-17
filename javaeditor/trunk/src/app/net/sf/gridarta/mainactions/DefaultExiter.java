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

package net.sf.gridarta.mainactions;

import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.dialog.prefs.DevPreferences;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.ExiterListener;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Exits the application.
 * @author Andreas Kirschbaum
 */
public class DefaultExiter implements Exiter {

    /**
     * The {@link ActionBuilder}.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(DefaultExiter.class);

    /**
     * The {@link Preferences}.
     */
    @NotNull
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The main window's frame.
     */
    @NotNull
    private final Window mainViewFrame;

    /**
     * The {@link ExiterListener ExiterListeners} to notify.
     */
    @NotNull
    private final EventListenerList2<ExiterListener> exiterListeners = new EventListenerList2<ExiterListener>(ExiterListener.class);

    /**
     * Creates a new instance.
     * @param mainViewFrame the main window's frame
     */
    public DefaultExiter(@NotNull final Window mainViewFrame) {
        this.mainViewFrame = mainViewFrame;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExiterListener(@NotNull final ExiterListener listener) {
        exiterListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeExiterListener(@NotNull final ExiterListener listener) {
        exiterListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExit(final int returnCode) {
        mainViewFrame.setEnabled(false);
        for (final ExiterListener listener : exiterListeners.getListeners()) {
            listener.preExitNotify();
        }
        for (final ExiterListener listener : exiterListeners.getListeners()) {
            listener.appExitNotify();
        }
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (final ExiterListener listener : exiterListeners.getListeners()) {
                    listener.waitExitNotify();
                }
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            mainViewFrame.dispose();
                        }

                    });
                } catch (final InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    log.warn("Cannot destroy main view: " + ex.getMessage());
                } catch (final InvocationTargetException ex) {
                    log.warn("Cannot destroy main view: " + ex.getMessage());
                }
                callExit(returnCode);
            }

        });
        thread.start();
    }

    /**
     * Calls {@link System#exit(int)} or does nothing depending on the user's
     * settings.
     * @param returnCode the return code to pass
     */
    public static void callExit(final int returnCode) {
        if (preferences.getBoolean(DevPreferences.PREFERENCES_SYSTEM_EXIT, DevPreferences.PREFERENCES_SYSTEM_EXIT_DEFAULT)) {
            if (log.isDebugEnabled()) {
                log.debug(ActionBuilderUtils.getString(ACTION_BUILDER, "logExitWithExit"));
            }
            System.exit(returnCode);
        } else {
            log.debug(ActionBuilderUtils.getString(ACTION_BUILDER, "logExitWithoutExit"));
        }
    }

}
