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

package net.sf.gridarta.model.archetypetype;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.gridarta.model.errorview.ErrorView;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.errorview.TestErrorView;
import net.sf.gridarta.utils.XmlHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

/**
 * Regression tests for {@link ArchetypeTypeSetParser}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeSetParserTest {

    /**
     * The {@link ErrorView} used for parsing.
     */
    private final ErrorView errorView = new TestErrorView();

    /**
     * The {@link ErrorViewCollector} used for parsing.
     */
    private final ErrorViewCollector errorViewCollector = new ErrorViewCollector(errorView, new File("*input*"));

    /**
     * Read a simple types.xml file.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void test1() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch=\"name\" editor=\"name_editor\" type=\"string\">name description</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"123\" name=\"name 123\">\n");
        typesXml.append("<description>description 123</description>\n");
        typesXml.append("<attribute arch=\"f\" value=\"v\" type=\"fixed\"/>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:123,name 123\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        expectedResult.append("fixed/f[v] section=1/Special\n");
        expectedResult.append("string/name[name_editor] section=0/General\n");
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that imports do work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testImport1() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch=\"default_attr1\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"default_attr2\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<description>description1</description>\n");
        typesXml.append("<attribute arch=\"name1_attr1\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"name1_attr2\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        typesXml.append("<type number=\"2\" name=\"name2\">\n");
        typesXml.append("<import_type name=\"name1\"/>\n");
        typesXml.append("<description>description2</description>\n");
        typesXml.append("<attribute arch=\"name2_attr1\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"name1_attr2\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"default_attr2\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        expectedResult.append("string/name1_attr1[name1] section=1/Special\n");
        expectedResult.append("string/name1_attr2[name1] section=1/Special\n");
        expectedResult.append("string/default_attr1[default] section=0/General\n");
        expectedResult.append("string/default_attr2[default] section=0/General\n");
        expectedResult.append("\n");
        expectedResult.append("type:2,name2\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        expectedResult.append("string/name2_attr1[name2] section=1/Special\n");
        expectedResult.append("string/name1_attr2[name2] section=1/Special\n");
        expectedResult.append("string/default_attr2[name2] section=1/Special\n");
        /* imports */
        expectedResult.append("string/name1_attr1[name1] section=1/Special\n");
        /* defaults */
        expectedResult.append("string/default_attr1[default] section=0/General\n");
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that multi-imports do work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testImport2() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch=\"attr1\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr3\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr5\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr7\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr9\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrB\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrD\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrF\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<description>description1</description>\n");
        typesXml.append("<attribute arch=\"attr2\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr3\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr6\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr7\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrA\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrB\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrE\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrF\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        typesXml.append("<type number=\"2\" name=\"name2\">\n");
        typesXml.append("<description>description2</description>\n");
        typesXml.append("<attribute arch=\"attr4\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr5\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr6\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr7\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrC\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrD\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrE\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrF\" editor=\"name2\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        typesXml.append("<type number=\"3\" name=\"name3\">\n");
        typesXml.append("<import_type name=\"name1\"/>\n");
        typesXml.append("<import_type name=\"name2\"/>\n");
        typesXml.append("<description>description3</description>\n");
        typesXml.append("<attribute arch=\"attr8\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attr9\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrA\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrB\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrC\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrD\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrE\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"attrF\" editor=\"name3\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        expectedResult.append("string/attr2[name1] section=1/Special\n");
        expectedResult.append("string/attr3[name1] section=1/Special\n");
        expectedResult.append("string/attr6[name1] section=1/Special\n");
        expectedResult.append("string/attr7[name1] section=1/Special\n");
        expectedResult.append("string/attrA[name1] section=1/Special\n");
        expectedResult.append("string/attrB[name1] section=1/Special\n");
        expectedResult.append("string/attrE[name1] section=1/Special\n");
        expectedResult.append("string/attrF[name1] section=1/Special\n");
        /* defaults */
        expectedResult.append("string/attr1[default] section=0/General\n");
        expectedResult.append("string/attr5[default] section=0/General\n");
        expectedResult.append("string/attr9[default] section=0/General\n");
        expectedResult.append("string/attrD[default] section=0/General\n");
        /* imports */
        expectedResult.append("\n");
        expectedResult.append("type:2,name2\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        expectedResult.append("string/attr4[name2] section=1/Special\n");
        expectedResult.append("string/attr5[name2] section=1/Special\n");
        expectedResult.append("string/attr6[name2] section=1/Special\n");
        expectedResult.append("string/attr7[name2] section=1/Special\n");
        expectedResult.append("string/attrC[name2] section=1/Special\n");
        expectedResult.append("string/attrD[name2] section=1/Special\n");
        expectedResult.append("string/attrE[name2] section=1/Special\n");
        expectedResult.append("string/attrF[name2] section=1/Special\n");
        /* defaults */
        expectedResult.append("string/attr1[default] section=0/General\n");
        expectedResult.append("string/attr3[default] section=0/General\n");
        expectedResult.append("string/attr9[default] section=0/General\n");
        expectedResult.append("string/attrB[default] section=0/General\n");
        /* attributes */
        /* imports */
        expectedResult.append("\n");
        expectedResult.append("type:3,name3\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        expectedResult.append("string/attr8[name3] section=1/Special\n");
        expectedResult.append("string/attr9[name3] section=1/Special\n");
        expectedResult.append("string/attrA[name3] section=1/Special\n");
        expectedResult.append("string/attrB[name3] section=1/Special\n");
        expectedResult.append("string/attrC[name3] section=1/Special\n");
        expectedResult.append("string/attrD[name3] section=1/Special\n");
        expectedResult.append("string/attrE[name3] section=1/Special\n");
        expectedResult.append("string/attrF[name3] section=1/Special\n");
        /* imports */
        expectedResult.append("string/attr2[name1] section=1/Special\n");
        expectedResult.append("string/attr3[name1] section=1/Special\n");
        expectedResult.append("string/attr6[name1] section=1/Special\n");
        expectedResult.append("string/attr7[name1] section=1/Special\n");
        expectedResult.append("string/attr4[name2] section=1/Special\n");
        expectedResult.append("string/attr5[name2] section=1/Special\n");
        /* defaults */
        expectedResult.append("string/attr1[default] section=0/General\n");
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that ignoring default attributes do work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testIgnoreDefaultAttribute1() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch=\"default_attr1\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"default_attr2\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<ignore>\n");
        typesXml.append("<attribute arch=\"default_attr1\"/>\n");
        typesXml.append("</ignore>\n");
        typesXml.append("<description>description1</description>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        /* imports */
        /* defaults */
        expectedResult.append("string/default_attr2[default] section=0/General\n");
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that ignoring import attributes do work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testIgnoreImportAttribute1() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch=\"default_attr1\" editor=\"default\" type=\"string\">description</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<description>description1</description>\n");
        typesXml.append("<attribute arch=\"name1_attr1\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"name1_attr2\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        typesXml.append("<type number=\"2\" name=\"name2\">\n");
        typesXml.append("<import_type name=\"name1\"/>\n");
        typesXml.append("<ignore>\n");
        typesXml.append("<attribute arch=\"name1_attr1\"/>\n");
        typesXml.append("</ignore>\n");
        typesXml.append("<description>description2</description>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        expectedResult.append("string/name1_attr1[name1] section=1/Special\n");
        expectedResult.append("string/name1_attr2[name1] section=1/Special\n");
        expectedResult.append("string/default_attr1[default] section=0/General\n");
        expectedResult.append("\n");
        expectedResult.append("type:2,name2\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        /* imports */
        expectedResult.append("string/name1_attr1[name1] section=1/Special\n"); // imported attributes are not affected by ignores
        expectedResult.append("string/name1_attr2[name1] section=1/Special\n");
        /* defaults */
        expectedResult.append("string/default_attr1[default] section=0/General\n");
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that ignoring defined attributes do work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testIgnoreDefinedAttribute1() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type/>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<ignore>\n");
        typesXml.append("<attribute arch=\"name1_attr1\"/>\n");
        typesXml.append("</ignore>\n");
        typesXml.append("<description>description1</description>\n");
        typesXml.append("<attribute arch=\"name1_attr1\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("<attribute arch=\"name1_attr2\" editor=\"name1\" type=\"string\">description</attribute>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        expectedResult.append("string/name1_attr1[name1] section=1/Special\n"); // defined attributes are not affected by ignores
        expectedResult.append("string/name1_attr2[name1] section=1/Special\n");
        /* imports */
        /* defaults */
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that a "msg" field in default_type does work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testMsgDefault1() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch_begin=\"msg\" arch_end=\"endmsg\" editor=\"default\" type=\"text\">msg</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<description>description</description>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        /* imports */
        /* defaults */
        expectedResult.append("text/msg[default] section=2/default\n");
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that a "msg" field in default_type does work.
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testMsgDefault2() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch_begin=\"msg\" arch_end=\"endmsg\" editor=\"default\" type=\"text\">msg1</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<description>description</description>\n");
        typesXml.append("<attribute arch_begin=\"msg\" arch_end=\"endmsg\" editor=\"name1\" type=\"text\">msg2</attribute>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        expectedResult.append("text/msg[name1] section=2/name1\n");
        /* imports */
        /* defaults */
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that a "msg" field in default_type CAN BE "ignored".
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    @Test
    public void testMsgDefault3() throws ParserConfigurationException, UnsupportedEncodingException {
        final StringBuilder typesXml = new StringBuilder();
        typesXml.append("<bitmasks/>\n");
        typesXml.append("<lists/>\n");
        typesXml.append("<ignorelists/>\n");
        typesXml.append("<default_type>\n");
        typesXml.append("<attribute arch_begin=\"msg\" arch_end=\"endmsg\" editor=\"default\" type=\"text\">msg1</attribute>\n");
        typesXml.append("</default_type>\n");
        typesXml.append("<type number=\"1\" name=\"name1\">\n");
        typesXml.append("<ignore>\n");
        typesXml.append("<attribute arch=\"msg\"/>\n");
        typesXml.append("</ignore>\n");
        typesXml.append("<description>description</description>\n");
        typesXml.append("</type>\n");
        final StringBuilder expectedResult = new StringBuilder();
        expectedResult.append("type:1,name1\n");
        expectedResult.append(":\n");
        expectedResult.append(":\n");
        /* attributes */
        /* imports */
        /* defaults */
        expectedResult.append("\n");
        check(typesXml.toString(), false, expectedResult.toString());
    }

    /**
     * Checks that a types.xml file can be read.
     * @param typesXml the types.xml file contents
     * @param expectedHasErrors whether errors are expected
     * @param expectedResult the expected result
     * @throws ParserConfigurationException if the test fails
     * @throws UnsupportedEncodingException if the test fails
     */
    private void check(final String typesXml, final boolean expectedHasErrors, final String expectedResult) throws ParserConfigurationException, UnsupportedEncodingException {
        final ArchetypeTypeSet archetypeTypeSet = new ArchetypeTypeSet();
        final ArchetypeTypeSetParser parser = createArchetypeTypeSetParser(archetypeTypeSet);
        parser.loadTypesFromXML(errorViewCollector, createInputSource(typesXml));
        Assert.assertEquals(expectedHasErrors, errorView.hasErrors());
        Assert.assertEquals(expectedResult, archetypeTypeSet.toString());
    }

    /**
     * Creates a new {@link ArchetypeTypeSetParser} instance.
     * @param archetypeTypeSet the archetype type set to modify
     * @return the archetype type set parser instance
     * @throws ParserConfigurationException if the parser cannot be created
     */
    private static ArchetypeTypeSetParser createArchetypeTypeSetParser(final ArchetypeTypeSet archetypeTypeSet) throws ParserConfigurationException {
        final XmlHelper xmlHelper = new XmlHelper();
        final ArchetypeAttributeFactory archetypeAttributeFactory = new TestArchetypeAttributeFactory();
        final ArchetypeAttributeParser archetypeAttributeParser = new ArchetypeAttributeParser(archetypeAttributeFactory);
        final ArchetypeTypeParser archetypeTypeParser = new ArchetypeTypeParser(archetypeAttributeParser);
        return new ArchetypeTypeSetParser(xmlHelper.getDocumentBuilder(), archetypeTypeSet, archetypeTypeParser);
    }

    /**
     * Creates an {@link InputSource} from a <code>String</code>.
     * @param string the string to wrap
     * @return the input stream
     * @throws UnsupportedEncodingException if the input stream cannot be
     * created
     */
    @NotNull
    private static InputSource createInputSource(@NotNull final String string) throws UnsupportedEncodingException {
        final StringBuilder typesString = new StringBuilder();
        typesString.append("<?xml version=\"1.0\" standalone=\"no\" ?>\n");
        typesString.append("<!DOCTYPE types SYSTEM \"types.dtd\">\n");
        typesString.append("<types>\n");
        typesString.append(string);
        typesString.append("</types>\n");
        final InputSource inputSource = new InputSource(new ByteArrayInputStream(typesString.toString().getBytes("UTF-8")));
        inputSource.setSystemId("types.dtd");
        return inputSource;
    }

}
