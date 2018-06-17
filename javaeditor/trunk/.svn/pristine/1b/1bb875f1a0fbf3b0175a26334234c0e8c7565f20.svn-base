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

package net.sf.gridarta.model.validation;

import junit.framework.TestCase;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.TestActionBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Test for {@link AbstractValidator}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AbstractValidatorTest extends TestCase {

    /**
     * Object Under Test: A AbstractValidator.
     */
    @Nullable
    private Validator<?, ?, ?> oUT;

    /**
     * {@inheritDoc}
     * @noinspection ProhibitedExceptionDeclared
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestActionBuilder.initialize();
        final ValidatorPreferences validatorPreferences = new TestValidatorPreferences();
        //noinspection EmptyClass
        oUT = new AbstractValidator<TestGameObject, TestMapArchObject, TestArchetype>(validatorPreferences, DelegatingMapValidator.DEFAULT_KEY) {

        };
    }

    /**
     * {@inheritDoc}
     * @noinspection ProhibitedExceptionDeclared
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        oUT = null;
    }

    /**
     * Test case for {@link AbstractValidator#setEnabled(boolean)}.
     */
    public void testEnabled() {
        assert oUT != null;
        oUT.setEnabled(false);
        assert oUT != null;
        assertFalse(oUT.isEnabled());
        assert oUT != null;
        oUT.setEnabled(true);
        assert oUT != null;
        assertTrue(oUT.isEnabled());
    }

}
