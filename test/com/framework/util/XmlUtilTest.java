

package com.framework.util;

import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * @author superleo
 * @since 2008-8-11
 * @version
 */
public class XmlUtilTest extends TestCase {
	public static final String STRUTS_XML_FILE = "/struts.xml";

	public void testGetDocument() {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		assertTrue(is != null);
		try {
			Document doc = XmlUtil.getDocument(is);
			assertNotNull(doc);
		} catch (DocumentException e) {
			this.fail(e.getMessage());
		}

	}

	public void testGetRoot() throws DocumentException {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		Document doc = XmlUtil.getDocument(is);
		Element root = XmlUtil.getRoot(doc);
		assertNotNull(root);
		assertEquals(root.getName(), "struts");
	}

	public void testGetElements() throws DocumentException {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		Document doc = XmlUtil.getDocument(is);
		Element root = XmlUtil.getRoot(doc);
		List<Element> elements = XmlUtil.getElements(root);
		assertNotNull(elements);
		assertEquals(1, elements.size());
	}

	public void testGetElementsByName() throws DocumentException {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		Document doc = XmlUtil.getDocument(is);
		Element root = XmlUtil.getRoot(doc);
		List<Element> elements = XmlUtil.getElementsByName(root, "package");
		assertNotNull(elements);
		assertEquals(1, elements.size());
	}

	public void testGetElementByName() throws DocumentException {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		Document doc = XmlUtil.getDocument(is);
		Element root = XmlUtil.getRoot(doc);
		Element _package = XmlUtil.getElementByName(root, "package");
		assertEquals(_package.getName(), "package");
		Element action = XmlUtil.getElementByName(_package, "action");
		assertEquals(action.getName(), "action");
	}

	public void testGetAttributes() throws DocumentException {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		Document doc = XmlUtil.getDocument(is);
		Element root = XmlUtil.getRoot(doc);
		Element _package = XmlUtil.getElementByName(root, "package");
		Element action = XmlUtil.getElementByName(_package, "action");
		List<Attribute> attributes = XmlUtil.getAttributes(action);
		assertNotNull(attributes);
		assertEquals(3, attributes.size());
	}

	public void testGetAttributeByName() throws DocumentException {
		InputStream is = this.getClass().getResourceAsStream(STRUTS_XML_FILE);
		Document doc = XmlUtil.getDocument(is);
		Element root = XmlUtil.getRoot(doc);
		Element _package = XmlUtil.getElementByName(root, "package");
		Element action = XmlUtil.getElementByName(_package, "action");
		Attribute name = XmlUtil.getAttributeByName(action, "name");
		Attribute clas = XmlUtil.getAttributeByName(action, "class");
		assertEquals("hello", name.getText());
		assertEquals("com.leo.action.HelloAction", clas.getText());
	}

}
