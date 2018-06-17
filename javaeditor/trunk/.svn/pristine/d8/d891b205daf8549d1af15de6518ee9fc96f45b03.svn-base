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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Copies a {@link Reader} into a {@link String}.
 * @author Andreas Kirschbaum
 */
public class CopyReader {

    /**
     * The {@link Reader} to read from.
     */
    @NotNull
    private final Reader reader;

    /**
     * The synchronization object for accessing {@link #stopped}, {@link
     * #failure}, and {@link #charArrayWriter}.
     */
    @NotNull
    private final Object sync = new Object();

    /**
     * The {@link CharArrayWriter} collecting data read from {@link #reader}.
     */
    @NotNull
    private final CharArrayWriter charArrayWriter = new CharArrayWriter();

    /**
     * Whether the worker thread was stopped.
     */
    private boolean stopped;

    /**
     * The failure reason. Set to <code>null</code> if no failure occurred.
     */
    @Nullable
    private String failure;

    /**
     * The {@link Runnable} implementing the worker thread.
     */
    @NotNull
    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            final char[] buf = new char[1024];
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final int len = reader.read(buf);
                    if (len == -1) {
                        break;
                    }
                    synchronized (sync) {
                        if (stopped) {
                            break;
                        }
                        charArrayWriter.write(buf, 0, len);
                    }
                } catch (final IOException ex) {
                    setFailure(ex.getMessage());
                }
            }
        }

    };

    /**
     * The worker {@link Thread} executing {@link #runnable}.
     */
    @NotNull
    private final Thread thread = new Thread(runnable);

    /**
     * Creates a new instance.
     * @param reader the reader to read from
     */
    public CopyReader(@NotNull final Reader reader) {
        this.reader = reader;
    }

    /**
     * Starts reading.
     */
    public void start() {
        thread.start();
    }

    /**
     * Stops reading.
     */
    public void stop() {
        thread.interrupt();
    }

    /**
     * Sets the failure reason. Does nothing if a preceding failure reason was
     * recorded.
     * @param failure the failure reason
     */
    @SuppressWarnings("NullableProblems")
    private void setFailure(@NotNull final String failure) {
        synchronized (sync) {
            stopped = true;
            if (this.failure == null) {
                this.failure = failure;
            }
        }
    }

    /**
     * Returns the failure reason.
     * @return the failure reason or <code>null</code> if no failure occurred
     */
    @Nullable
    public String getFailure() {
        synchronized (sync) {
            stopped = true;
            thread.interrupt();
            return failure;
        }
    }

    /**
     * Returns the reader's output.
     * @return the reader's output
     */
    @NotNull
    public String getOutput() {
        synchronized (sync) {
            stopped = true;
            thread.interrupt();
            return charArrayWriter.toString();
        }
    }

    /**
     * Waits for the worker thread to terminate.
     * @throws InterruptedException if the current thread was interrupted
     * waiting for the worker thread to terminate
     */
    public void join() throws InterruptedException {
        thread.join();
    }

}
