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

import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.annotations.NotNull;

/**
 * Helper for reducing the number of change events: calls to {@link #change()}
 * will be forwarded to {@link DelayedChangeListener#change()}. Forwarded calls
 * are dropped if they would happen very quickly.
 * @author Andreas Kirschbaum
 */
public class DelayedChangeManager {

    /**
     * The delay in millisecond the first forwarded event is delayed. This
     * allows combining the first few events of a long series of events without
     * delaying the delivery for a long time ({@link #notificationDelay}).
     */
    private final int initialDelay;

    /**
     * The interval in milliseconds events are forwarded during a long series of
     * events.
     */
    private final int notificationDelay;

    /**
     * The {@link DelayedChangeListener} events are forwarded to.
     */
    @NotNull
    private final DelayedChangeListener delayedChangeListener;

    /**
     * The {@link Timer} for delaying events.
     */
    @NotNull
    private final Timer timer = new Timer();

    /**
     * A {@link TimerTask} that calls {@link State#timeout()} when the timer
     * expires.
     */
    private class TimeoutTimerTask extends TimerTask {

        @Override
        public void run() {
            state = state.timeout();
        }

    }

    /**
     * The states of the FSM.
     */
    private interface State {

        /**
         * Executes the event "change".
         * @return the next state
         */
        @NotNull
        State change();

        /**
         * Executes the event "finish".
         * @return the next state
         */
        @NotNull
        State finish();

        /**
         * Executes the event "timeout".
         * @return the next state
         */
        @NotNull
        State timeout();

    }

    /**
     * The state "idle" of the FSM.
     */
    @NotNull
    private final State idle = new State() {

        @NotNull
        @Override
        public State change() {
            timer.schedule(new TimeoutTimerTask(), initialDelay);
            return pending;
        }

        @NotNull
        @Override
        public State finish() {
            return idle;
        }

        @NotNull
        @Override
        public State timeout() {
            assert false;
            return idle;
        }

    };

    /**
     * The state "pending" of the FSM.
     */
    @NotNull
    private final State pending = new State() {

        @NotNull
        @Override
        public State change() {
            return pending;
        }

        @NotNull
        @Override
        public State finish() {
            delayedChangeListener.change();
            return wait;
        }

        @NotNull
        @Override
        public State timeout() {
            timer.schedule(new TimeoutTimerTask(), notificationDelay);
            delayedChangeListener.change();
            return wait;
        }

    };

    /**
     * The state "wait" of the FSM.
     */
    @NotNull
    private final State wait = new State() {

        @NotNull
        @Override
        public State change() {
            return pending;
        }

        @NotNull
        @Override
        public State finish() {
            return wait;
        }

        @NotNull
        @Override
        public State timeout() {
            return idle;

        }

    };

    /**
     * The FSM's current state.
     */
    @NotNull
    private State state = idle;

    /**
     * Creates a new instance.
     * @param initialDelay the initial delay for a series of events in
     * milliseconds
     * @param notificationDelay the delay for following events in a series of
     * events; in milliseconds
     * @param delayedChangeListener the delayed change listener to forward
     * events to
     */
    public DelayedChangeManager(final int initialDelay, final int notificationDelay, @NotNull final DelayedChangeListener delayedChangeListener) {
        this.initialDelay = initialDelay;
        this.notificationDelay = notificationDelay;
        this.delayedChangeListener = delayedChangeListener;
    }

    /**
     * Delivers a change event to the associated {@link DelayedChangeListener}.
     */
    public void change() {
        state = state.change();
    }

    /**
     * Finishes a series of {@link #change() change} events. Calling this
     * function is optional: if it is called, a pending change event is
     * immediately forwarded; otherwise when the notification timeout has
     * expired.
     */
    public void finish() {
        state = state.finish();
    }

}
