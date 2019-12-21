

package com.framework.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.framework.util.XmlUtil;
import com.leo.action.HelloAction;

/**
 * @author superleo
 * @since 2008-8-12
 * @version
 */
public class ActionManagerTest extends TestCase {

	public void testGetActionClass() {
		String str1 = "hello";
		String str2 = "leo";
		String str3 = "";
		String str4 = null;
		ActionManager manager = new ActionManager();
		assertEquals("com.leo.action.HelloAction", manager.getActionClass(str1)
				.getClassName());
		try {
			ActionClass actionClass = manager.getActionClass(str2);
			List<Element> elements = actionClass.getElements();
			assertNotNull(elements);
			assertEquals(1, elements.size());
			Attribute att = XmlUtil.getAttributeByName(elements.get(0), "name");
			assertEquals("success", att.getText());
		} catch (RuntimeException e) {
			assertNotNull(e.getMessage());
		}
		try {
			manager.getActionClass(str3);
		} catch (RuntimeException e) {
			assertNotNull(e.getMessage());
		}
		try {
			manager.getActionClass(str4);
		} catch (RuntimeException e) {
			assertNotNull(e.getMessage());
		}
	}

	public void testSetActionValue() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		params.put("message", new String[] { "haha" });
		params.put("leo", new String[] { "leo" });

		ActionManager manager = new ActionManager();
		ActionClass actionClass = new ActionClass();
		actionClass.setClassName("com.leo.action.HelloAction");
		Object obj = manager.setActionValues(actionClass, params);
		HelloAction action = (HelloAction) obj;
		assertEquals(action.getMessage(), "haha");
	}

	public void testGetResult() throws DocumentException {
		String text = "<root><result name=\"success\">/index.jsp</result></root>";
		Document document = DocumentHelper.parseText(text);

		ActionClass actionClass = new ActionClass();
		actionClass.setClassName("com.framework.core.ActionManager");
		actionClass.setMethod("test");
		List<Element> elements = XmlUtil.getElementsByName(document
				.getRootElement(), "result");
		actionClass.setElements(elements);
		ActionManager manager = new ActionManager();
		Object object = manager.invokeAction(actionClass);
		assertEquals("com.framework.core.ActionManager", object.getClass()
				.getName());
		try {
			Field field = object.getClass().getDeclaredField("filePath");
			assertEquals("java.lang.String", field.getType().getName());
			field.setAccessible(true);
			assertEquals("/leo.xml", field.get(object));

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
