/*
 * This file is part of JXClient, the Fullscreen Java Wograld Client.
 *
 * JXClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JXClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JXClient; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Copyright (C) 2005-2008 Yann Chachkoff.
 * Copyright (C) 2006-2011 Andreas Kirschbaum.
 */

package com.realtime.wograld.jxclient.scripts;

import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.server.ReceivedPacketListener;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements the "watch" function for client-sided scripts.
 * @author Andreas Kirschbaum
 */
public class PacketWatcher {

    /**
     * The commands to watch for.
     */
    @NotNull
    private final Collection<String> commands = new HashSet<String>();

    /**
     * The {@link WograldServerConnection} to watch.
     */
    @NotNull
    private final WograldServerConnection wograldServerConnection;

    /**
     * The {@link ScriptProcess} for sending commands.
     */
    @NotNull
    private final ScriptProcess scriptProcess;

    /**
     * A {@link Pattern} matching all {@link #commands}. Set to
     * <code>null</code> when not watching for commands.
     */
    @Nullable
    private Pattern pattern = null;

    /**
     * The {@link ReceivedPacketListener} attached to {@link
     * #wograldServerConnection}. It is attached if and only if {@link
     * #pattern} is non-<code>null</code>.
     */
    @NotNull
    private final ReceivedPacketListener receivedPacketListener = new ReceivedPacketListener() {

        @Override
        public void processEmpty(@NotNull final String command) {
            if (matchesCommand(command)) {
                scriptProcess.commandSent("watch "+command);
            }
        }

        @Override
        public void processAscii(@NotNull final String command, @NotNull final ByteBuffer packet) {
            if (matchesCommand(command)) {
                final byte[] data = new byte[packet.remaining()];
                packet.get(data);
                scriptProcess.commandSent("watch "+command+" "+new String(data)); // XXX: uses default encoding
            }
        }

        @Override
        public void processShortArray(@NotNull final String command, @NotNull final ByteBuffer packet) {
            if (matchesCommand(command)) {
                final StringBuilder sb = new StringBuilder("watch ");
                sb.append(command);
                for (int i = 0; i < 100 && packet.remaining() >= 2; i++) {
                    sb.append(' ');
                    sb.append(getShort(packet));
                }
                scriptProcess.commandSent(sb.toString());
            }
        }

        @Override
        public void processIntArray(@NotNull final String command, @NotNull final ByteBuffer packet) {
            if (matchesCommand(command)) {
                final StringBuilder sb = new StringBuilder("watch ");
                sb.append(command);
                while (packet.remaining() >= 4) {
                    sb.append(' ');
                    sb.append(getInt(packet));
                }
                scriptProcess.commandSent(sb.toString());
            }
        }

        @Override
        public void processShortInt(@NotNull final String command, @NotNull final ByteBuffer packet) {
            if (packet.remaining() == 6 && matchesCommand(command)) {
                scriptProcess.commandSent("watch "+command+" "+getShort(packet)+" "+getInt(packet));
            }
        }

        @Override
        public void processMixed(@NotNull final String command, @NotNull final ByteBuffer packet) {
            if (matchesCommand(command)) {
                final StringBuilder sb = new StringBuilder("watch ");
                sb.append(command);
                sb.append(' ');
                sb.append(packet.remaining());
                scriptProcess.commandSent(sb.toString());
            }
        }

        @Override
        public void processStats(@NotNull final String command, final int stat, @NotNull final Object[] args) {
            if (matchesCommand(command)) {
                final StringBuilder sb = new StringBuilder("watch ");
                sb.append(command);
                sb.append(' ');
                sb.append(StatUtils.getStatNames(stat));
                for (final Object arg : args) {
                    sb.append(' ');
                    sb.append(arg);
                }
                scriptProcess.commandSent(sb.toString());
            }
        }

        @Override
        public void processNoData(@NotNull final String command, @NotNull final ByteBuffer packet) {
            processMixed(command, packet);
        }

        /**
         * Extracts a two-byte integer value from a <code>byte</code> array.
         * @param packet the <code>byte</code> array
         * @return the integer value
         */
        private int getShort(@NotNull final ByteBuffer packet) {
            return packet.getShort()&0xFFFF;
        }

        /**
         * Extracts a four-byte integer value from a <code>byte</code> array.
         * @param packet the <code>byte</code> array
         * @return the integer value
         */
        private int getInt(@NotNull final ByteBuffer packet) {
            return packet.getInt();
        }

    };

    /**
     * Creates a new instance.
     * @param wograldServerConnection the server connection to watch
     * @param scriptProcess the script process for sending commands
     */
    public PacketWatcher(@NotNull final WograldServerConnection wograldServerConnection, @NotNull final ScriptProcess scriptProcess) {
        this.wograldServerConnection = wograldServerConnection;
        this.scriptProcess = scriptProcess;
        rebuildPattern();
    }

    /**
     * Releases allocated resources. Must be called before this instance is
     * freed.
     */
    public void destroy() {
        if (pattern != null) {
            pattern = null;
            wograldServerConnection.removePacketWatcherListener(receivedPacketListener);
        }
    }

    /**
     * Rebuilds {@link #pattern} from {@link #commands}.
     */
    private void rebuildPattern() {
        final StringBuilder sb = new StringBuilder();
        for (final String command : commands) {
            sb.append(Pattern.quote(command));
            sb.append(".*|");
        }
        final int length = sb.length();
        if (length <= 0) {
            if (pattern != null) {
                pattern = null;
                wograldServerConnection.removePacketWatcherListener(receivedPacketListener);
            }
        } else {
            if (pattern == null) {
                wograldServerConnection.addPacketWatcherListener(receivedPacketListener);
            }
            sb.setLength(length-1);
            pattern = Pattern.compile(sb.toString());
        }
    }

    /**
     * Adds a command to watch for.
     * @param command the command
     */
    public void addCommand(@NotNull final String command) {
        if (commands.add(command)) {
            rebuildPattern();
        }
    }

    /**
     * Removes a command to watch for.
     * @param command the command
     */
    public void removeCommand(@NotNull final String command) {
        if (commands.remove(command)) {
            rebuildPattern();
        }
    }

    /**
     * Returns whether a command matches the currently watched commands.
     * @param command the command
     * @return whether the command matches
     */
    private boolean matchesCommand(@NotNull final CharSequence command) {
        return pattern != null && pattern.matcher(command).matches();
    }

}
