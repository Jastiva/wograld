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

package com.realtime.wograld.jxclient.commands;

import com.realtime.wograld.jxclient.server.wograld.WograldServerConnection;
import com.realtime.wograld.jxclient.settings.options.CheckBoxOption;
import com.realtime.wograld.jxclient.settings.options.OptionException;
import com.realtime.wograld.jxclient.settings.options.OptionManager;
import com.realtime.wograld.jxclient.util.Patterns;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the command "set". It changes settings options.
 * @author Andreas Kirschbaum
 */
public class SetCommand extends AbstractCommand {

    /**
     * The option manager instance.
     */
    @NotNull
    private final OptionManager optionManager;

    /**
     * Creates a new instance.
     * @param wograldServerConnection the connection instance
     * @param optionManager the option manager to use
     */
    public SetCommand(@NotNull final WograldServerConnection wograldServerConnection, @NotNull final OptionManager optionManager) {
        super("set", wograldServerConnection);
        this.optionManager = optionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean allArguments() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull final String args) {
        final String[] tmp = Patterns.PATTERN_WHITESPACE.split(args, 2);
        if (tmp.length != 2) {
            drawInfoError("The set command needs two arguments: set <option> <value>");
            return;
        }

        final String optionName = tmp[0];
        final String optionArgs = tmp[1];
        final CheckBoxOption option;
        try {
            option = optionManager.getCheckBoxOption(optionName);
        } catch (final OptionException ex) {
            drawInfoError(ex.getMessage());
            return;
        }

        final boolean checked;
        if (optionArgs.equals("on")) {
            checked = true;
        } else if (optionArgs.equals("off")) {
            checked = false;
        } else {
            drawInfoError("The '"+optionArgs+"' for option '"+optionName+"' is invalid. Valid arguments are 'on' or 'off'.");
            return;
        }

        option.setChecked(checked);
    }

}
