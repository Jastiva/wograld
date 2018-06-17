/*
 * This file is part of JXClient, the Fullscreen Java Crossfire Client.
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

package com.realtime.crossfire.jxclient.test;

import com.realtime.crossfire.jxclient.scripts.ScriptProcess;
import com.realtime.crossfire.jxclient.scripts.ScriptProcessListener;
import junit.framework.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements {@link ScriptProcess} for regression tests. All functions do call
 * {@link Assert#fail()}. Sub-classes may override some functions.
 * @author Andreas Kirschbaum
 */
public class TestScriptProcess implements ScriptProcess {

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScriptId() {
        Assert.fail();
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getFilename() {
        Assert.fail();
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commandSent(@NotNull final String cmd) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addScriptProcessListener(@NotNull final ScriptProcessListener scriptProcessListener) {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void killScript() {
        Assert.fail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull final ScriptProcess o) {
        Assert.fail();
        throw new AssertionError();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        return obj != null && obj.getClass() == getClass();
    }

}
