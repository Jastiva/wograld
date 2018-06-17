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

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for displaying a dialog to keep or dump invalid attributes of a
 * game object.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ConfirmErrorsDialog {

    /**
     * Private constructor to prevent instantiation.
     */
    private ConfirmErrorsDialog() {
    }

    /**
     * Open a popup dialog and ask the user to confirm (or modify) the
     * encountered syntax errors. If the user chooses to keep any errors, these
     * get attached to the archetype text by the action listener
     * (<code>ConfirmErrorsAL</code>). <p/> Note that this method does not fork
     * off with a new thread. It freezes the parent frames (and threads) until
     * the popup window is closed, which mimics non-event-driven behaviour.
     * @param errors a textual list of the encountered errors
     * @param parent the parent dialog
     * @return whether to keep the errors
     */
    public static boolean askConfirmErrors(@NotNull final String errors, @NotNull final Component parent) {
        final JDialog dialog = new JDialog((JFrame) null, "Syntax Errors", true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        final Container headerPanel = new JPanel(new GridLayout(2, 1));
        final Container buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // create header labels
        final Component header1 = new JLabel("The following lines from the archetype text appear to be wrong.");
        final Component header2 = new JLabel("They do not match the type definitions:");
        headerPanel.add(header1);
        headerPanel.add(header2);

        // create text area for showing errors
        final JTextArea textArea = new JTextArea(errors, 7, 25);
        textArea.setBorder(BorderFactory.createEmptyBorder(1, 4, 0, 0));
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        // create buttons
        final AbstractButton dumpButton = new JButton("Dump All Errors");
        final AbstractButton keepButton = new JButton("Keep Above Text");
        buttonPanel.add(dumpButton);
        buttonPanel.add(keepButton);

        final boolean[] result = { true };

        // attach action listener to the buttons (and the dialog)
        final ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                result[0] = e.getSource() == keepButton;
                dialog.dispose();
            }

        };
        keepButton.addActionListener(actionListener);
        dumpButton.addActionListener(actionListener);
        final WindowListener windowListener = new WindowListener() {

            @Override
            public void windowOpened(final WindowEvent e) {
            }

            @Override
            public void windowClosing(final WindowEvent e) {
                result[0] = true;
                dialog.dispose();
            }

            @Override
            public void windowClosed(final WindowEvent e) {
            }

            @Override
            public void windowIconified(final WindowEvent e) {
            }

            @Override
            public void windowDeiconified(final WindowEvent e) {
            }

            @Override
            public void windowActivated(final WindowEvent e) {
            }

            @Override
            public void windowDeactivated(final WindowEvent e) {
            }

        };
        dialog.addWindowListener(windowListener);

        // stick panels together
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonPanel);

        dialog.getContentPane().add(mainPanel);

        // pack, position and show the popup
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return result[0];
    }

}
