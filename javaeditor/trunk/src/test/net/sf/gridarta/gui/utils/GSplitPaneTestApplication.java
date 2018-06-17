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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * Simple application for testing {@link GSplitPane} behavior. Displays a {@link
 * JFrame} consisting of colored areas separated by {@link GSplitPane
 * GSplitPanes}.
 * @author Andreas Kirschbaum
 */
public class GSplitPaneTestApplication {

    /**
     * The minimal size of components.
     */
    private static final int MIN_SIZE = 2;

    /**
     * Private constructor to prevent instantiation.
     */
    private GSplitPaneTestApplication() {
    }

    /**
     * The entry point of the application.
     * @param args the command-line arguments (ignored)
     * @throws InterruptedException if an unexpected error occurs
     * @throws InvocationTargetException if an unexpected error occurs
     */
    public static void main(final String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                final JPanel green = newPanel(MIN_SIZE, MIN_SIZE, Color.GREEN);
                final JPanel blue = newPanel(MIN_SIZE, MIN_SIZE, Color.BLUE);
                final JPanel yellow = newPanel(MIN_SIZE, MIN_SIZE, Color.YELLOW);
                final JPanel orange = newPanel(MIN_SIZE, MIN_SIZE, Color.ORANGE);
                final GSplitPane splitPane1 = new GSplitPane(JSplitPane.HORIZONTAL_SPLIT, green, blue, "TEST_GREEN_BLUE", 70);
                splitPane1.setResizeWeight(0.5);
                final GSplitPane splitPane2 = new GSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane1, yellow, "TEST_GREEN_BLUE_YELLOW", 40);
                final GSplitPane splitPane3 = new GSplitPane(JSplitPane.HORIZONTAL_SPLIT, orange, splitPane2, "TEST_ORANGE_GREEN_BLUE_YELLOW", 30);
                final AbstractButton button = new JButton("save divider locations and quit");
                final Window frame = new JFrame();
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        splitPane1.saveLocation();
                        splitPane2.saveLocation();
                        splitPane3.saveLocation();
                        frame.setVisible(false);
                        System.exit(0);
                    }
                });
                frame.setLayout(new BorderLayout());
                frame.add(button, BorderLayout.SOUTH);
                frame.add(splitPane3);
                frame.pack();
                frame.setSize(new Dimension(800, 600));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    /**
     * Creates a new colored area.
     * @param width the minimal width of the area
     * @param height the minimal height of the area
     * @param color the color of the area
     * @return the area
     */
    private static JPanel newPanel(final int width, final int height, @NotNull final Color color) {
        final JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(width, height));
        panel.setOpaque(true);
        panel.setBackground(color);
        return panel;
    }

}
