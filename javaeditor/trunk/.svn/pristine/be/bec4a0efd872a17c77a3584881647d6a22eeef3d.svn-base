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

package net.sf.gridarta.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class to run an external process.
 * @author <a href="mailto:cher@riedquat.de">Christian.Hujer</a>
 */
public class ProcessRunner extends JPanel {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(ProcessRunner.class);

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The Dialog.
     * @serial include
     */
    @Nullable
    private Window dialog;

    /**
     * The i18n key.
     * @serial include
     */
    @NotNull
    private final String key;

    /**
     * The command and arguments.
     * @serial include
     */
    @NotNull
    private String[] command;

    /**
     * The working directory for the command.
     * @serial include
     */
    @Nullable
    private final File dir;

    /**
     * The Process.
     */
    @Nullable
    private transient Process process;

    /**
     * JTextArea with log.
     * @serial include
     */
    @NotNull
    private final JTextArea stdtxt = new JTextArea(25, 80);

    /**
     * CopyOutput for stdout.
     * @serial include
     */
    @NotNull
    private final CopyOutput stdout = new CopyOutput("stdout", stdtxt);

    /**
     * CopyOutput for stderr.
     * @serial include
     */
    @NotNull
    private final CopyOutput stderr = new CopyOutput("stderr", stdtxt);

    /**
     * Action for start.
     * @serial include
     */
    @NotNull
    private final Action controlStart = ACTION_BUILDER.createAction(false, "controlStart", this);

    /**
     * Action for stop.
     * @serial include
     */
    @NotNull
    private final Action controlStop = ACTION_BUILDER.createAction(false, "controlStop", this);

    /**
     * The lock object for thread synchronization.
     */
    @NotNull
    private final Object lock = new Object();

    /**
     * Creates a ProcessRunner for running the given command in the given
     * directory.
     * @param key i18n key
     * @param command the command to run and its arguments
     * @param dir the working directory for command
     */
    private ProcessRunner(@NotNull final String key, @NotNull final String[] command, @Nullable final String dir) {
        this.key = key;
        this.command = command.clone();
        this.dir = dir == null ? null : new File(dir);
        setLayout(new BorderLayout());
        stdtxt.setFont(new Font("monospaced", Font.PLAIN, stdtxt.getFont().getSize()));
        stdtxt.setEditable(false);
        stdtxt.setFocusable(false);
        stdtxt.setLineWrap(true);
        stdtxt.setBackground(Color.BLACK);
        stdtxt.setForeground(Color.WHITE);
        final Component scrollPane = new JScrollPane(stdtxt);
        scrollPane.setFocusable(true);
        add(scrollPane, BorderLayout.CENTER);
        final JToolBar toolBar = new JToolBar();
        toolBar.add(controlStart);
        toolBar.add(controlStop);
        toolBar.add(ACTION_BUILDER.createAction(false, "controlClear", this));
        toolBar.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "controlCloseOkay"));
        controlStop.setEnabled(false);
        add(toolBar, BorderLayout.SOUTH);
    }

    /**
     * Creates a ProcessRunner for running the given command in its directory.
     * @param key i18n key
     * @param command the command to run and its arguments
     */
    public ProcessRunner(@NotNull final String key, @NotNull final String[] command) {
        this(key, command, new File(command[0]).getParent());
    }

    /**
     * Show a dialog if not already visible.
     * @param parent owner frame to display on
     */
    public void showDialog(@NotNull final JFrame parent) {
        if (dialog == null || dialog.getOwner() != parent) {
            createDialog(parent);
        }
        assert dialog != null;
        dialog.setVisible(true);
        assert dialog != null;
        dialog.requestFocus();
    }

    /**
     * Create the dialog.
     * @param parent owner frame to display on
     */
    private void createDialog(@NotNull final JFrame parent) {
        dialog = new JDialog(parent, ActionBuilderUtils.getString(ACTION_BUILDER, key + ".title"));
        dialog.add(this);
        assert dialog != null;
        dialog.pack();
        assert dialog != null;
        dialog.setLocationRelativeTo(parent);
    }

    /**
     * Set the command to be executed by this ProcessRunner.
     * @param command the command to run and its arguments
     */
    public void setCommand(@NotNull final String[] command) {
        this.command = command.clone();
    }

    /**
     * Action method for starting.
     */
    @ActionMethod
    public void controlStart() {
        synchronized (lock) {
            if (process != null) {
                try {
                    try {
                        process.getInputStream().close();
                    } catch (final IOException ignored) {
                        // ignore
                    }
                    try {
                        process.getErrorStream().close();
                    } catch (final IOException ignored) {
                        // ignore
                    }
                    try {
                        process.getOutputStream().close();
                    } catch (final IOException ignored) {
                        // ignore
                    }
                    process.exitValue();
                } catch (final IllegalThreadStateException ignored) {
                    log.error("Still running!");
                    // Process is still running, don't start a new one
                    return;
                }
                process = null;
            }
            try {
                process = new ProcessBuilder(command).directory(dir).redirectErrorStream(true).start();
                final InputStream out = process.getInputStream();
                final InputStream err = process.getErrorStream();
                stdout.start(out);
                if (out != err) {
                    stderr.start(err);
                }
                controlStop.setEnabled(true);
                controlStart.setEnabled(false);
            } catch (final IOException e) {
                ACTION_BUILDER.showMessageDialog(this, "controlError", e);
            }
        }
    }

    /**
     * Action method for stopping.
     */
    @ActionMethod
    public void controlStop() {
        if (process != null) {
            process.destroy();
        }
        controlStop.setEnabled(false);
        controlStart.setEnabled(true);
    }

    /**
     * Action method for clearing the log.
     */
    @ActionMethod
    public void controlClear() {
        stdtxt.setText("");
    }

    /**
     * Class for reading data from a stream and appending it to a JTextArea.
     */
    private static class CopyOutput implements Runnable {

        /**
         * BufferedReader to read from.
         */
        @Nullable
        private InputStream in;

        /**
         * JTextArea to write to.
         */
        @NotNull
        private final Appender appender;

        /**
         * Title.
         */
        @NotNull
        private final String title;

        /**
         * Create a CopyOutput.
         * @param title Title for this CopyOutput
         * @param textArea JTextArea to append output to
         */
        CopyOutput(@NotNull final String title, @NotNull final JTextArea textArea) {
            this.title = title;
            appender = new Appender(textArea);
        }

        @Override
        public void run() {
            try {
                try {
                    final byte[] buf = new byte[4096];
                    while (true) {
                        assert in != null;
                        final int bytesRead = in.read(buf);
                        if (bytesRead == -1) {
                            break;
                        }
                        appender.append(new String(buf, 0, bytesRead));
                    }
                    //for (String line; (line = in.readLine()) != null;) {
                    //    appender.append(title, line, "\n");
                    //}
                } finally {
                    assert in != null;
                    in.close();
                }
            } catch (final IOException e) {
                appender.append(title, ": ", e.toString());
            } finally {
                in = null;
            }
        }

        /**
         * Start running.
         * @param stream InputStream to read from
         */
        private void start(@NotNull final InputStream stream) {
            if (in != null) {
                if (log.isInfoEnabled()) {
                    log.info("Trying to stop previous stream.");
                }
                try {
                    assert in != null;
                    in.close();
                } catch (final IOException ignored) {
                    // ignore
                }
                if (log.isInfoEnabled()) {
                    log.info("Stopped previous stream.");
                }
            }
            //in = new BufferedInputStream(stream);
            in = stream;
            new Thread(this).start();
        }

        /**
         * Class for SwingUtilities to append text. Why is this class used?
         * Quite simple. Swing is not thread-safe. All modifications on realized
         * Swing components must be done by the AWT Event thread. Concurrent
         * modifications might crash some or more swing components. The
         * CopyOutput is a thread of its own. This class makes sure that
         * appending text to the JTextArea is done by the AWT Event thread.
         */
        private static class Appender implements Runnable {

            /**
             * Strings to append.
             */
            @NotNull
            private final Queue<String> texts = new ConcurrentLinkedQueue<String>();

            /**
             * JTextArea to append to.
             */
            @NotNull
            private final JTextArea textArea;

            /**
             * Create an Appender.
             * @param textArea JTextArea to append to
             */
            Appender(@NotNull final JTextArea textArea) {
                this.textArea = textArea;
            }

            /**
             * Append text to the JTextArea.
             * @param texts texts to append
             */
            public void append(@NotNull final String... texts) {
                for (final String text : texts) {
                    this.texts.offer(text);
                }
                SwingUtilities.invokeLater(this);
            }

            @Override
            public void run() {
                while (!texts.isEmpty()) {
                    textArea.append(texts.poll());
                }
            }

        }

    }

}
