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

package net.sf.gridarta.gui.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RectangularShape;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.gui.utils.borderpanel.Location;
import net.sf.gridarta.gui.utils.tabbedpanel.Tab;
import net.sf.gridarta.gui.utils.tabbedpanel.TabListener;
import net.sf.gridarta.gui.utils.tabbedpanel.TabbedPanel;
import net.sf.gridarta.utils.Exiter;
import net.sf.gridarta.utils.ExiterListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Main window of the level editor. Contains the desktop for the map windows
 * (internal frames) and the tools.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MainView {

    /**
     * Preferences.
     */
    private static final Preferences preferences = Preferences.userNodeForPackage(MainControl.class);

    /**
     * The key used to store the main window state in the preferences.
     */
    private static final String WINDOW_STATE = "WindowState";

    /**
     * The key used to store the main windows x-coordinate to INI file.
     */
    private static final String WINDOW_X = "MainWindow.x";

    /**
     * The key used to store the main windows y-coordinate to INI file.
     */
    private static final String WINDOW_Y = "MainWindow.y";

    /**
     * The key used to store the main windows width to INI file.
     */
    private static final String WINDOW_WIDTH = "MainWindow.width";

    /**
     * The key used to store the main windows height to INI file.
     */
    private static final String WINDOW_HEIGHT = "MainWindow.height";

    /**
     * The main window of the editor.
     */
    @NotNull
    private final Component frame;

    /**
     * The {@link TabbedPanel} that displays {@link #frame} and all open tabs.
     */
    @NotNull
    private final TabbedPanel tabbedPanel;

    /**
     * Constructs the main view and registers the given main controller.
     * @param frame the main window of the editor
     * @param exitAction the action to execute when the window is closed
     * @param mapDesktop the map desktop to show
     * @param icon the application's icon or <code>null</code> for default
     * @param exiter the exiter instance
     */
    public MainView(@NotNull final JFrame frame, @NotNull final ActionListener exitAction, @NotNull final Component mapDesktop, @Nullable final ImageIcon icon, @NotNull final Exiter exiter) {
        this.frame = frame;
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                exitAction.actionPerformed(new ActionEvent(e.getSource(), 0, null));
            }
        });
        if (icon != null) {
            frame.setIconImage(icon.getImage());
        }

        final ExiterListener exiterListener = new ExiterListener() {

            @Override
            public void preExitNotify() {
                // ignore
            }

            @Override
            public void appExitNotify() {
                final Rectangle bounds = frame.getBounds();
                preferences.putInt(WINDOW_X, bounds.x);
                preferences.putInt(WINDOW_Y, bounds.y);
                preferences.putInt(WINDOW_WIDTH, bounds.width);
                preferences.putInt(WINDOW_HEIGHT, bounds.height);
                preferences.putInt(WINDOW_STATE, frame.getExtendedState());
            }

            @Override
            public void waitExitNotify() {
                // ignore
            }

        };
        exiter.addExiterListener(exiterListener);

        // calculate some default values in case there is no settings file
        final RectangularShape screen = frame.getGraphicsConfiguration().getBounds();
        final int defaultWidth = (int) (0.9 * screen.getWidth());
        final int defaultHeight = (int) (0.9 * screen.getHeight());

        tabbedPanel = new TabbedPanel(mapDesktop);
        frame.add(tabbedPanel, BorderLayout.CENTER);

        // set bounds (location and size) of the main frame
        frame.setBounds(preferences.getInt(WINDOW_X, (int) (screen.getX() + (screen.getWidth() - (double) defaultWidth) / 2.0)), preferences.getInt(WINDOW_Y, (int) (screen.getY() + (screen.getHeight() - (double) defaultHeight) / 2.0)), preferences.getInt(WINDOW_WIDTH, defaultWidth), preferences.getInt(WINDOW_HEIGHT, defaultHeight));
        frame.setExtendedState(preferences.getInt(WINDOW_STATE, frame.getExtendedState()));
    }

    /**
     * Adds a tab.
     * @param tab the tab
     */
    public void addTab(@NotNull final Tab tab) {
        tabbedPanel.addTab(tab);
        final TabListener tabListener = new TabListener() {

            @Override
            public void severityChanged(@NotNull final Severity severity) {
                setSeverity(tab, severity);
            }

        };
        tab.addTabListener(tabListener);
        setSeverity(tab, tab.getSeverity());
    }

    /**
     * Returns the active {@link Tab} on a given {@link Location} of the main
     * view.
     * @param location the location
     * @param alternativeLocation whether the alternative location should be
     * checked
     * @return the tab or <code>null</code> if no active tab exists
     */
    @Nullable
    public Tab getActiveTab(@NotNull final Location location, final boolean alternativeLocation) {
        return tabbedPanel.getActiveTab(location, alternativeLocation);
    }

    /**
     * Sets a {@link Tab}'s {@link Severity}. The severity affects the tab's
     * button's color.
     * @param tab the tab
     * @param severity the severity
     */
    private static void setSeverity(@NotNull final Tab tab, @NotNull final Severity severity) {
        tab.getButton().setForeground(severity.getColor());
    }

    /**
     * This implementation displays the exception in a modal message dialog.
     */
    public void handleThrowable(final Throwable t) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(frame, t.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }

}
