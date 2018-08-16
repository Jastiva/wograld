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

package com.realtime.wograld.jxclient.test;

import com.realtime.wograld.jxclient.server.wograld.WograldAccountListener;
import com.realtime.wograld.jxclient.server.wograld.WograldComcListener;
import com.realtime.wograld.jxclient.server.wograld.WograldDrawextinfoListener;
import com.realtime.wograld.jxclient.server.wograld.WograldDrawinfoListener;
import com.realtime.wograld.jxclient.server.wograld.WograldExpTableListener;
import com.realtime.wograld.jxclient.server.wograld.WograldFaceListener;
import com.realtime.wograld.jxclient.server.wograld.WograldFailureListener;
import com.realtime.wograld.jxclient.server.wograld.WograldMagicmapListener;
import com.realtime.wograld.jxclient.server.wograld.WograldMusicListener;
import com.realtime.wograld.jxclient.server.wograld.WograldPickupListener;
import com.realtime.wograld.jxclient.server.wograld.WograldQueryListener;
import com.realtime.wograld.jxclient.server.wograld.WograldQuestListener;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.server.wograld.WograldServerConnectionListener;
import com.realtime.wograld.jxclient.server.wograld.WograldSkillInfoListener;
import com.realtime.wograld.jxclient.server.wograld.WograldSmoothListener;
import com.realtime.wograld.jxclient.server.wograld.WograldSoundListener;
import com.realtime.wograld.jxclient.server.wograld.WograldSpellListener;
import com.realtime.wograld.jxclient.server.wograld.WograldStatsListener;
import com.realtime.wograld.jxclient.server.wograld.WograldTickListener;
import com.realtime.wograld.jxclient.server.wograld.WograldUpdateFaceListener;
import com.realtime.wograld.jxclient.server.wograld.WograldUpdateItemListener;
import com.realtime.wograld.jxclient.server.wograld.WograldUpdateMapListener;
import com.realtime.wograld.jxclient.server.wograld.SentReplyListener;
import com.realtime.wograld.jxclient.server.server.ReceivedPacketListener;
import com.realtime.wograld.jxclient.server.socket.ClientSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

/**
 * Implements {@link WograldServerConnection} for regression tests. All
 * functions do call {@link Assert#fail()}. Sub-classes may override some
 * functions.
 * @author Andreas Kirschbaum
 */
public class TestWograldServerConnection implements WograldServerConnection {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldServerConnectionListener(@NotNull final WograldServerConnectionListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldDrawinfoListener(@NotNull final WograldDrawinfoListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldDrawinfoListener(@NotNull final WograldDrawinfoListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldDrawextinfoListener(@NotNull final WograldDrawextinfoListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldDrawextinfoListener(@NotNull final WograldDrawextinfoListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldQueryListener(@NotNull final WograldQueryListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldQueryListener(@NotNull final WograldQueryListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldMagicmapListener(@NotNull final WograldMagicmapListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldMagicmapListener(@NotNull final WograldMagicmapListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldUpdateFaceListener(@NotNull final WograldUpdateFaceListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldStatsListener(@NotNull final WograldStatsListener wograldStatsListener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldUpdateItemListener(@NotNull final WograldUpdateItemListener wograldUpdateItemListener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldUpdateItemListener(@NotNull final WograldUpdateItemListener wograldUpdateItemListener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWograldUpdateMapListener(@Nullable final WograldUpdateMapListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldTickListener(@NotNull final WograldTickListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldSoundListener(@NotNull final WograldSoundListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldSmoothListener(@NotNull final WograldSmoothListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldMusicListener(@NotNull final WograldMusicListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldComcListener(@NotNull final WograldComcListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldFaceListener(@NotNull final WograldFaceListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldSpellListener(@NotNull final WograldSpellListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldQuestListener(WograldQuestListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldExpTableListener(@NotNull final WograldExpTableListener wograldExpTableListener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldSkillInfoListener(@NotNull final WograldSkillInfoListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldSkillInfoListener(@NotNull final WograldSkillInfoListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldPickupListener(@NotNull final WograldPickupListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldPickupListener(@NotNull final WograldPickupListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPacketWatcherListener(@NotNull final ReceivedPacketListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePacketWatcherListener(@NotNull final ReceivedPacketListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSentReplyListener(@NotNull final SentReplyListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSentReplyListener(@NotNull final SentReplyListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawInfo(@NotNull final String message, final int color) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawextinfo(final int color, final int type, final int subtype, final String message) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawInfoSetDebugMode(final boolean printMessageTypes) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAddme() {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendApply(final int tag) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAskface(final int num) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendExamine(final int tag) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendLock(final boolean val, final int tag) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendLookat(final int dx, final int dy) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMark(final int tag) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMove(final int to, final int tag, final int nrof) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int sendNcom(final int repeat, @NotNull final String command) {
        Assert.fail();
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendReply(@NotNull final String text) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRequestinfo(@NotNull final String infoType) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendSetup(@NotNull final String... options) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendToggleextendedtext(@NotNull final int... types) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendVersion(final int csval, final int scval, @NotNull final String vinfo) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPreferredMapSize(final int preferredMapWidth, final int preferredMapHeight) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPreferredNumLookObjects(final int preferredNumLookObjects) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getAccountName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addClientSocketListener(@NotNull final ClientSocketListener clientSocketListener) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeClientSocketListener(@NotNull final ClientSocketListener clientSocketListener) {
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(@NotNull final String hostname, final int port) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(@NotNull final String reason) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldAccountListener(@NotNull final WograldAccountListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldAccountListener(@NotNull final WograldAccountListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWograldFailureListener(@NotNull final WograldFailureListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWograldFailureListener(@NotNull final WograldFailureListener listener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAccountLogin(@NotNull final String login, @NotNull final String password) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAccountPlay(@NotNull final String name) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAccountLink(final int force, @NotNull final String login, @NotNull final String password) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAccountCreate(@NotNull final String login, @NotNull final String password) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAccountCharacterCreate(@NotNull final String login, @NotNull final String password) {
        Assert.fail();
    }

    @Override
    public void sendAccountPassword(@NotNull final String currentPassword, @NotNull final String newPassword) {
        Assert.fail();
    }

}
