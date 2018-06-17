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

package net.sf.gridarta.model.anim;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.sf.gridarta.model.data.AbstractNamedObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base implementation of {@link AnimationObject}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DefaultAnimationObject extends AbstractNamedObject implements AnimationObject {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DefaultAnimationObject.class);

    /**
     * The names of the animation frames.
     * @serial
     */
    @NotNull
    private final List<String> frames = new ArrayList<String>();

    /**
     * The number of frames per facing in the animation.
     * @serial
     */
    private final int frameCount;

    /**
     * The number of facings, which is different sub animations, for instance
     * for different directions.
     * @serial
     */
    private final int facings;

    /**
     * The name of this animation.
     * @serial
     */
    @NotNull
    private final String animName;

    /**
     * The animation list of this animation. The individual entries are all
     * suffixed with '\n'.
     * @serial
     */
    @NotNull
    private final String animList;

    /**
     * A {@link Pattern} that matches the end of lines.
     */
    private static final Pattern PATTERN_END_OF_LINE = Pattern.compile("\n");

    /**
     * Creates a new instance. Therefore animList is parsed. Do not invoke this
     * constructor directly, use {@link AnimationObjects#addAnimationObject(String,
     * String, String)} instead. If you invoke this constructor directly, the
     * created AnimationObject will not be registered.
     * @param animName name of the animation
     * @param animList list of animation as found between "anim" and "mina",
     * separated with "\n", including "facings" but including neither "anim" nor
     * "mina"
     * @param path the path for this animation object
     */
    public DefaultAnimationObject(@NotNull final String animName, @NotNull final String animList, @NotNull final String path) {
        super(path);
        this.animName = animName;
        this.animList = animList;
        int tmpFacings = 1;
        for (final String line : PATTERN_END_OF_LINE.split(animList, 0)) {
            if (line.startsWith("facings ")) {
                tmpFacings = Integer.parseInt(line.substring(8));
                break;
            }
        }
        if (tmpFacings < 1) {
            log.warn("Ignoring invalid facings value: " + tmpFacings);
            tmpFacings = 1;
        }
        facings = tmpFacings;
        boolean first = true;
        for (final String line : PATTERN_END_OF_LINE.split(animList, 0)) {
            if (first && line.startsWith("facings ")) {
                first = false;
            } else {
                frames.add(line);
            }
        }
        if (frames.size() <= 0) {
            log.warn("Animation " + animName + " has no frames");
            for (int i = 0; i < facings; i++) {
                frames.add("bug.111");
            }
        } else if (frames.size() % facings != 0) {
            log.warn("Animation " + animName + " has " + frames.size() + " frames but " + facings + " facings");
            while (frames.size() % facings != 0) {
                frames.add(frames.get(frames.size() - 1));
            }
        }
        frameCount = frames.size() / facings;
    }

    /**
     * {@inheritDoc}
     * @return the same String as <code>getAnimName()</code>
     */
    @NotNull
    @Override
    public String getName() {
        return animName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFacings() {
        return facings;
    }

    /**
     * {@inheritDoc}
     * @see #getName()
     */
    @NotNull
    @Override
    public String getAnimName() {
        return animName;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getAnimList() {
        return animList;
    }

    /**
     * {@inheritDoc} The String representation of an AnimationObject is suitable
     * for writing into the animations file.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("anim ");
        sb.append(animName);
        sb.append('\n');
        sb.append(animList);
        sb.append("mina\n");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getDisplayIconName() {
        for (int facing = 1; facing < facings; facing++) {
            final String frame = getFirstFrame(facing);
            if (!frame.startsWith("dummy.")) {
                return frame;
            }
        }
        return getFirstFrame(0);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getFirstFrame(final int facing) {
        return getFrame(facing, 0);
    }

    /**
     * Get a specific frame.
     * @param facing facing to get frame for, usually a direction
     * @param frame index within the sub-animation for facing
     * @return frame name of animation frame
     * @throws IndexOutOfBoundsException in case the frame or facing is invalid
     */
    @NotNull
    public String getFrame(final int facing, final int frame) throws IndexOutOfBoundsException {
        if (facing < 0 || facing >= facings) {
            throw new IndexOutOfBoundsException("Invalid facing " + facing + " (anim only has " + facings + " facings)");
        }
        if (frame < 0 || frame >= frameCount) {
            throw new IndexOutOfBoundsException("Invalid frame " + frame + " (anim only has " + frameCount + " frames)");
        }
        return frames.get(facing * frameCount + frame);
    }

    /**
     * Get the number of frames per facing.
     * @return number of frames per facing
     */
    public int getFrameCount() {
        return frameCount;
    }

}
