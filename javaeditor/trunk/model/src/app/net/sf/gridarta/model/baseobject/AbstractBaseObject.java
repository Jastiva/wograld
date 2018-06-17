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

package net.sf.gridarta.model.baseobject;

import java.awt.Point;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.anim.AnimationObject;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.FaceSource;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.MultiArchData;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.NumberUtils;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default implementation for {@link GameObject} implementing classes. This
 * abstract class serves as a common base for GameObjects for Daimonin and
 * Crossfire to allow classes that use GameObjects being reused and moved to
 * Gridarta.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author serpentshard
 */
public abstract class AbstractBaseObject<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, T extends BaseObject<G, A, R, T>> extends GameObjectContainer<G, A, R> implements BaseObject<G, A, R, T> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link AnimationObjects} for looking up animations.
     */
    @Nullable
    private final AnimationObjects animationObjects;

    /**
     * The object text of this game object.
     * @serial
     */
    @NotNull
    private GameObjectText gameObjectText = new GameObjectText();

    /**
     * Face name, can be from animation or face. This is determined by taking
     * {@link #faceName} or {@link #animName} and takes {@link #direction} into
     * account.
     * @serial
     */
    @Nullable
    private String faceObjName;

    /**
     * The state where the face comes from.
     * @serial
     */
    @NotNull
    private FaceSource faceObjSource = FaceSource.FACE_NOT_FOUND;

    /**
     * The normal face.
     * @serial
     */
    @Nullable
    private ImageIcon normalFace;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final transient FaceObjectProviders faceObjectProviders;

    /**
     * The name of this object.
     * @serial
     */
    @NotNull
    private String objName = "";

    /**
     * The message text.
     * @note Every line in msgText must end on '\n', including the last line.
     * @note If the value is <code>null</code>, this GameObject has no message
     * text, if the value exists but is empty, this GameObject has an empty
     * message text.
     * @serial
     */
    @Nullable
    private StringBuilder msgText;

    /**
     * The map x position if on map.
     * @serial
     */
    private int mapX;

    /**
     * The map y position if on map.
     * @serial
     */
    private int mapY;

    /**
     * Data for multi-part objects. Stays <code>null</code> for single-part
     * objects.
     * @serial
     */
    @Nullable
    private MultiArchData<G, A, R, T> multi;

    /**
     * Edit Type.
     * @serial
     * @deprecated bad design for multiple reasons: Data is knowing view
     *             information (that's not nice) Not extensible
     */
    @Deprecated
    // default arches don't get an editType (not worth the time)
    // they get one assigned as soon as put on a map though.
    // TODO: This should be changed.
    private int editType = EDIT_TYPE_NONE;

    /**
     * The name of the face. Object face name <code>face
     * <var>name</var></code>.
     * @serial
     */
    @Nullable
    protected String faceName;

    /**
     * The object type.
     * @serial
     */
    private int typeNo;

    /**
     * The object's animation animation.
     * @serial
     */
    @Nullable
    protected String animName;

    /**
     * The direction determines to which direction the GameObject's face is
     * facing. E.g. some walls have two faces, some mobs 4 and very good
     * animated objects might even have 8 or 9 (8 + still) facings.
     * @serial
     */
    private int direction;

    /**
     * The map lore.
     */
    @NotNull
    private String loreText = "";

    /**
     * Creates a new instance.
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects for looking up animations
     */
    protected AbstractBaseObject(@NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects) {
        this.faceObjectProviders = faceObjectProviders;
        this.animationObjects = animationObjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTypeNo() {
        return typeNo;
    }

    /**
     * Sets the type number of this {@link Archetype}.
     * @param typeNo the type number of this archetype
     */
    private void setTypeNo(final int typeNo) {
        if (this.typeNo == typeNo) {
            return;
        }

        beginGameObjectChange();
        try {
            this.typeNo = typeNo;
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * Sets the face name.
     * @param faceName the new face name
     */
    @SuppressWarnings("NullableProblems")
    private void setFaceName(@NotNull final String faceName) {
        final String effectiveFaceName = getEffectiveFaceName(faceName);
        //Strings are interned
        //noinspection StringEquality
        if (this.faceName == effectiveFaceName) {
            return;
        }

        beginGameObjectChange();
        try {
            this.faceName = effectiveFaceName;
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * Returns the effective face name for {@link #faceName} for a given real
     * face name.
     * @param faceName the real face name
     * @return the effective face name or <code>null</code> if the real face
     *         name is unset or equals the archetype's face name
     */
    @Nullable
    protected abstract String getEffectiveFaceName(@NotNull final String faceName);

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getFaceName() {
        return faceName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countInvObjects() {
        int count = 0;
        for (final BaseObject<G, A, R, ?> gameObject : this) {
            count++;
            count += gameObject.countInvObjects();
        }
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAttribute(@NotNull final String attributeName, final boolean queryArchetype) {
        return getAttributeString(attributeName, queryArchetype).length() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAttribute(@NotNull final String attributeName) {
        return hasAttribute(attributeName, true);
    }

    /**
     * Returns an attribute value by attribute name.
     * @param attributeName the attribute name
     * @return the attribute value or <code>null</code>
     */
    @Nullable
    protected String getAttributeValue(@NotNull final String attributeName) {
        return gameObjectText.getAttributeValue(attributeName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getAttributeString(@NotNull final String attributeName) {
        return getAttributeString(attributeName, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttributeInt(@NotNull final String attributeName, final boolean queryArchetype) {
        return NumberUtils.parseInt(getAttributeString(attributeName, queryArchetype));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttributeInt(@NotNull final String attributeName) {
        return getAttributeInt(attributeName, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAttributeLong(@NotNull final String attributeName, final boolean queryArchetype) {
        return NumberUtils.parseLong(getAttributeString(attributeName, queryArchetype));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAttributeLong(@NotNull final String attributeName) {
        return getAttributeLong(attributeName, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAttributeDouble(@NotNull final String attributeName, final boolean queryArchetype) {
        return NumberUtils.parseDouble(getAttributeString(attributeName, queryArchetype));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAttributeDouble(@NotNull final String attributeName) {
        return getAttributeDouble(attributeName, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeString(@NotNull final String attributeName, @NotNull final String value) {
        if (value.isEmpty()) {
            removeAttribute(attributeName);
            return;
        }

        final String attributeNameWithSpace = attributeName.trim() + " ";        // attributeName must be followed by space
        final boolean sameAsInArchetype = getArchetype().getAttributeString(attributeName).equals(value);
        boolean exists = false;
        final StringBuilder result = new StringBuilder();
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(getObjectText(), 0)) {
            if (line.length() == 0) {
                // skip empty lines that occur due to split on empty object texts.
            } else if (!line.startsWith(attributeNameWithSpace)) {
                result.append(line).append('\n');
            } else {
                exists = true;
                if (!sameAsInArchetype) {
                    result.append(attributeNameWithSpace).append(value).append('\n');
                }
            }
        }
        if (!exists && !sameAsInArchetype) {
            result.append(attributeNameWithSpace).append(value).append('\n');
        }
        setObjectText(result.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeInt(@NotNull final String attributeName, final int value) {
        final String attributeNameWithSpace = attributeName.trim() + " ";
        final boolean sameAsInArchetype = getArchetype().getAttributeInt(attributeName) == value;
        boolean exists = false;
        final StringBuilder result = new StringBuilder();
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(getObjectText(), 0)) {
            if (line.length() == 0) {
                // skip empty lines that occur due to split on empty object texts.
            } else if (!line.startsWith(attributeNameWithSpace)) {
                result.append(line).append('\n');
            } else {
                exists = true;
                if (!sameAsInArchetype) {
                    result.append(attributeNameWithSpace).append(value).append('\n');
                }
            }
        }
        if (!exists && !sameAsInArchetype) {
            result.append(attributeNameWithSpace).append(value).append('\n');
        }
        setObjectText(result.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAttribute(@NotNull final String attributeName) {
        final String attributeNameWithSpace = attributeName.trim() + " ";        // attributeName must be followed by space

        final StringBuilder sb = new StringBuilder();
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(getObjectText(), 0)) {
            if (line.length() > 0 && !line.startsWith(attributeNameWithSpace)) {
                sb.append(line).append('\n');
            }
        }
        setObjectText(sb.toString());
    }

    /**
     * {@inheritDoc}
     * @noinspection AbstractMethodOverridesAbstractMethod // needed because of
     * public modifier
     */
    @Override
    public abstract void notifyBeginChange();

    /**
     * {@inheritDoc}
     * @noinspection AbstractMethodOverridesAbstractMethod // needed because of
     * public modifier
     */
    @Override
    public abstract void notifyEndChange();

    /**
     * Called whenever {@link #getArchetype()} has changed.
     */
    protected void updateArchetype() {
        beginGameObjectChange();
        try {
            updateCachedAttributes();
            if (multi != null) {
                final int multiShapeID = getArchetype().getMultiShapeID();
                assert multi != null;
                multi.setMultiShapeID(multiShapeID);
            }
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEditType() {
        return isHead() ? editType : getHead().getEditType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEditType(final int editType) {
        assert isHead();
        if (this.editType == editType) {
            return;
        }

        this.editType = editType;
        transientGameObjectChange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMsgTextLine(@NotNull final String text) {
        // It's intentional that if text == null, msgText still is created if it's empty.
        // FIXME: Though it's intentional, it's not nice, search for users and fix this.
        // (Also look at deleteMsgText() and getMsgText() then)
        if (msgText == null) {
            msgText = new StringBuilder();
        }

        beginGameObjectChange();
        try {
            final String trimmedText = StringUtils.removeTrailingWhitespace(text);
            assert msgText != null;
            msgText.append(trimmedText);
            assert msgText != null;
            msgText.append('\n');
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getMsgText() {
        return msgText != null ? msgText.toString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMsgText(@Nullable final String msgText) {
        final String trimmedMsgText = msgText == null ? null : StringUtils.removeTrailingWhitespaceFromLines(StringUtils.ensureTrailingNewline(msgText));
        if (this.msgText == null ? trimmedMsgText == null : this.msgText.toString().equals(trimmedMsgText)) {
            return;
        }

        beginGameObjectChange();
        try {
            if (trimmedMsgText == null) {
                this.msgText = null;
            } else if (this.msgText == null) {
                this.msgText = new StringBuilder(trimmedMsgText);
            } else {
                this.msgText.setLength(0);
                assert this.msgText != null;
                this.msgText.append(trimmedMsgText);
            }
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMapX() {
        return mapX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMapY() {
        return mapY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getMapLocation() {
        return new Point(mapX, mapY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapX(final int mapX) {
        this.mapX = mapX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMapY(final int mapY) {
        this.mapY = mapY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHead() {
        // == is okay because that's the definition of being head.
        //noinspection ObjectEquality
        return getHead() == this;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public T getMultiNext() {
        //noinspection ConstantConditions
        return multi != null ? multi.getNext(getThis()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMultiRefCount() {
        return multi != null ? multi.getMultiRefCount() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTailPart(@NotNull final T tail) {
        initMultiData();
        assert multi != null;
        tail.setMulti(multi);
        assert multi != null;
        multi.addPart(tail);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTailParts() {
        multi = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getHead() {
        return multi != null ? multi.getHead() : getThis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTail() {
        return getHead() != this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSizeX() {
        return multi != null ? multi.getSizeX() : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSizeY() {
        return multi != null ? multi.getSizeY() : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxX() {
        return multi != null ? multi.getMaxX() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxY() {
        return multi != null ? multi.getMaxY() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinX() {
        return multi != null ? multi.getMinX() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinY() {
        return multi != null ? multi.getMinY() : 0;
    }

    /**
     * Initialize the multi-part data object - must only be called for
     * multi-part arches.
     */
    private void initMultiData() {
        if (multi == null) {
            multi = new MultiArchData<G, A, R, T>(getThis(), getArchetype().getMultiShapeID());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public T clone() {
        //noinspection OverriddenMethodCallDuringObjectConstruction
        final AbstractBaseObject<G, A, R, T> clone = (AbstractBaseObject<G, A, R, T>) super.clone();
        //        clone.archetype = archetype; // will NOT be cloned: archetypes are unique
        clone.gameObjectText = gameObjectText.clone();
        if (msgText != null) {
            //noinspection CloneCallsConstructors
            clone.msgText = new StringBuilder(msgText);
        }
        clone.multi = null;
        return clone.getThis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setThisContainer(@NotNull final G gameObject) {
        gameObject.setContainer(this, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getObjName() {
        return objName;
    }

    /**
     * Sets the name of this object.
     * @param objName the name of this object
     */
    private void setObjName(@NotNull final String objName) {
        final String objName2 = objName.length() == 0 ? getArchetype().getArchetypeName() : objName;
        final String newObjName = objName2.intern();
        // Using == on these Strings is okay, they are both interned.
        //noinspection StringEquality
        if (this.objName == newObjName) {
            return;
        }

        beginGameObjectChange();
        try {
            this.objName = newObjName;
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getBestName() {
        final String baseName;
        if (objName.length() > 0) {
            baseName = objName;
        } else {
            final Archetype<G, A, R> archetype = getArchetype();
            final String archObjName = archetype.getObjName();
            if (archObjName.length() > 0) {
                baseName = archObjName;
            } else {
                baseName = archetype.getArchetypeName();
            }
        }

        final String title = getAttributeString(TITLE);
        return title.isEmpty() ? baseName : baseName + " " + title;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getObjectText() {
        return gameObjectText.getObjectText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObjectText(@NotNull final String line) {
        if (line.length() <= 0) {
            return;
        }

        beginGameObjectChange();
        try {
            gameObjectText.addObjectText(line);
            updateCachedAttributes();
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectText(@NotNull final String objectText) {
        final String oldObjectText = gameObjectText.getObjectText();
        if (oldObjectText.length() == objectText.length()) {
            final String oldSortedObjectText = StringUtils.sortLines(oldObjectText);
            final String newSortedObjectText = StringUtils.sortLines(objectText);
            if (oldSortedObjectText.equals(newSortedObjectText)) {
                return;
            }
        }

        beginGameObjectChange();
        try {
            gameObjectText.setObjectText(objectText);
            updateCachedAttributes();
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefaultGameObject() {
        final R archetype = getArchetype();
        if (!objName.equals(archetype.getObjName())) {
            return false;
        }
        if (gameObjectText.hasObjectText()) {
            return false;
        }
        if (msgText != null && !msgText.toString().equals(archetype.getMsgText())) {
            return false;
        }
        //noinspection ConstantConditions
        if (faceName != null && !faceName.equals(archetype.getFaceName())) {
            return false;
        }
        //noinspection ConstantConditions,SimplifiableIfStatement
        if (animName != null && !animName.equals(archetype.getAnimName())) {
            return false;
        }
        return typeNo == archetype.getTypeNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqual(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        if (!(gameObject instanceof AbstractBaseObject)) {
            return false;
        }
        final AbstractBaseObject<?, ?, ?, ?> abstractBaseObject = (AbstractBaseObject<?, ?, ?, ?>) gameObject;
        if (!hasSameContents(abstractBaseObject)) {
            return false;
        }
        if (abstractBaseObject.faceObjName == null ? faceObjName != null : !abstractBaseObject.faceObjName.equals(faceObjName)) {
            return false;
        }
        // ignore "faceObjSource"
        // ignore "normalFace"
        // ignore "container"
        if (abstractBaseObject.getArchetype() != getArchetype()) {
            return false;
        }
        if (!abstractBaseObject.getArchetype().getArchetypeName().equals(getArchetype().getArchetypeName())) {
            return false;
        }
        if (!abstractBaseObject.objName.equals(objName)) {
            return false;
        }
        if (!abstractBaseObject.gameObjectText.equals(gameObjectText)) {
            return false;
        }
        if (abstractBaseObject.msgText == null ? msgText != null : msgText == null || !msgText.toString().equals(abstractBaseObject.msgText.toString())) {
            return false;
        }
        // ignore "mapX"
        // ignore "mapY"
        // ignore "attributeCache"
        // ignore "multi"
        // ignore "editType"
        if (abstractBaseObject.faceName == null ? faceName != null : !abstractBaseObject.faceName.equals(faceName)) {
            return false;
        }
        if (abstractBaseObject.animName == null ? animName != null : !abstractBaseObject.animName.equals(animName)) {
            return false;
        }
        if (abstractBaseObject.typeNo != typeNo) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (abstractBaseObject.direction != direction) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDirection() {
        return direction;
    }

    /**
     * Records that this game object is about to change.
     */
    protected void beginGameObjectChange() {
        if (multi == null) {
            notifyBeginChange();
        } else {
            for (final BaseObject<G, A, R, T> part : multi) {
                part.notifyBeginChange();
            }
        }
    }

    /**
     * Records that this game object has changed.
     */
    protected void endGameObjectChange() {
        if (multi == null) {
            notifyEndChange();
        } else {
            for (final BaseObject<G, A, R, T> part : multi) {
                part.notifyEndChange();
            }
        }
    }

    /**
     * Records that this game object has changed but need not be restored by
     * undo/redo actions.
     */
    protected void transientGameObjectChange() {
        if (multi == null) {
            notifyTransientChange();
        } else {
            for (final BaseObject<G, A, R, T> part : multi) {
                part.notifyTransientChange();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FaceSource getFaceObjSource() {
        return faceObjSource;
    }


    /**
     * Sets object animation <code>animation <var>animName</var></code>.
     * @param animName object animation <code>animation <var>animName</var></code>
     */
    @SuppressWarnings("NullableProblems")
    private void setAnimName(@NotNull final String animName) {
        final String newAnimName = animName.length() > 0 ? animName.intern() : null;
        // Using == on these Strings is okay, they are both interned.
        //noinspection StringEquality
        if (this.animName == newAnimName) {
            return;
        }

        beginGameObjectChange();
        try {
            this.animName = newAnimName;
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getAnimName() {
        return animName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMulti() {
        return multi != null;
    }

    /**
     * Returns the {@link MultiArchData} for this base object.
     * @return the multi arch data or <code>null</code> if this object is not a
     *         multi-square object
     */
    @Nullable
    protected MultiArchData<G, A, R, T> getMulti() {
        return multi;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public void setMulti(@NotNull final MultiArchData<G, A, R, T> multi) {
        this.multi = multi;
    }

    /**
     * Sets the effective face name.
     * @param faceObjName the face name
     */
    private void setFaceObjName(@Nullable final String faceObjName) {
        final String newFaceObjName = faceObjName != null ? faceObjName.intern() : null;
        // Using == on these Strings is okay, they are both interned.
        //noinspection StringEquality
        if (this.faceObjName == newFaceObjName) {
            return;
        }

        beginGameObjectChange();
        try {
            this.faceObjName = newFaceObjName;
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getFaceObjName() {
        return faceObjName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectFace() {
        @Nullable String effectiveAnimName;
        if (getAttributeInt(IS_ANIMATED) == 0 && getAttributeInt(IS_TURNABLE) == 0) {
            effectiveAnimName = null;
            faceObjSource = FaceSource.ANIM;
        } else if (animName != null) {
            effectiveAnimName = animName;
            faceObjSource = FaceSource.ANIM;
        } else if (getArchetype() != null) {
            effectiveAnimName = getArchetype().getAnimName();
            faceObjSource = FaceSource.ARCHETYPE_ANIM;
        } else {
            effectiveAnimName = null;
            faceObjSource = FaceSource.ARCHETYPE_ANIM;
        }
        if (effectiveAnimName != null && effectiveAnimName.equals("NONE")) {
            effectiveAnimName = null;
        }

        @Nullable String effectiveFaceObjName;
        if (effectiveAnimName != null) { // we have a animation - get the frame picture
            final AnimationObject animationObject = animationObjects.get(effectiveAnimName);
            if (animationObject == null) {
                effectiveFaceObjName = null;
            } else {
                try {
                    effectiveFaceObjName = animationObject.getFirstFrame(direction);
                } catch (final IndexOutOfBoundsException ignored) {
                    effectiveFaceObjName = null;
                }
            }
        } else { // ok, we fallback to the face picture
            if (faceName != null) {
                effectiveFaceObjName = faceName;
                faceObjSource = FaceSource.FACE;
            } else if (getArchetype() != null) {
                effectiveFaceObjName = getArchetype().getFaceName();
                faceObjSource = FaceSource.ARCHETYPE_FACE;
            } else {
                effectiveFaceObjName = null;
                faceObjSource = FaceSource.ARCHETYPE_FACE;
            }
        }

        setFaceObjName(effectiveFaceObjName);

        if (effectiveFaceObjName == null || faceObjectProviders.getImageIconForFacename(effectiveFaceObjName) == null) {
            faceObjSource = FaceSource.FACE_NOT_FOUND;
        }
        normalFace = null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ImageIcon getNormalImage() {
        if (normalFace == null) {
            normalFace = faceObjectProviders.getFace(getThis());
        }
        return normalFace;
    }
    
    @NotNull
    @Override      
    public ImageIcon getSecondImage(){
        return faceObjectProviders.getSecondFace(getThis());
 
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getLoreText() {
        return loreText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLoreText(@NotNull final CharSequence loreText) {
        final String trimmedLoreText = StringUtils.removeTrailingWhitespace(loreText);
        if (this.loreText.equals(trimmedLoreText)) {
            return;
        }

        beginGameObjectChange();
        try {
            this.loreText = trimmedLoreText.intern();
        } finally {
            endGameObjectChange();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void facesReloaded() {
        normalFace = null;
        transientGameObjectChange();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString(@NotNull final String format) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = format.toCharArray();
        int pos = 0;
        while (pos < chars.length) {
            if (chars[pos] == '$' && pos + 2 < chars.length && chars[pos + 1] == '{') {
                pos += 2;
                final int startPos = pos;
                int nesting = 1;
                while (pos < chars.length) {
                    if (chars[pos] == '}') {
                        nesting--;
                        pos++;
                        if (nesting <= 0) {
                            break;
                        }
                    } else if (chars[pos] == '$' && pos + 1 < chars.length && chars[pos + 1] == '{') {
                        nesting++;
                        pos += 2;
                    } else {
                        pos++;
                    }
                }
                if (nesting > 0) {
                    sb.append("${");
                    pos++;
                } else {
                    final int endPos = pos - 1;
                    assert startPos <= endPos;
                    final CharSequence spec = new String(chars, startPos, endPos - startPos);
                    final String[] tmp = StringUtils.PATTERN_COLON.split(spec, 2);
                    if (tmp.length < 2) {
                        if (tmp[0].equals("NAME")) {
                            sb.append(getBestName());
                        } else {
                            sb.append(getAttributeString(tmp[0]));
                        }
                    } else {
                        if (hasAttribute(tmp[0])) {
                            sb.append(toString(tmp[1]));
                        }
                    }
                }
            } else {
                sb.append(chars[pos]);
                pos++;
            }
        }

        return sb.toString();
    }

    /**
     * Returns <code>this</code> as its real type.
     * @return <code>this<c/code>
     */
    @NotNull
    protected abstract T getThis();

    /**
     * Updates attribute values that are cached. This function is called
     * whenever the object text has changed.
     */
    private void updateCachedAttributes() {
        direction = getAttributeInt(DIRECTION);
        setFaceName(getAttributeString(FACE));
        setAnimName(getAttributeString(ANIMATION));
        setTypeNo(getAttributeInt(TYPE));
        setObjName(getAttributeString(NAME));
        setObjectFace();
    }
    
    

}
