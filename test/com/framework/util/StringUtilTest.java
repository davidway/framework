
package com.framework.util;

import junit.framework.TestCase;

/**
 * @author superleo
 * @since 2008-8-11
 * @version
 */
public class StringUtilTest extends TestCase {

	public void testIsNotBlank() {
		String str1 = "";
		String str2 = " ";
		String str3 = "test";
		String str4 = "test ";

		assertEquals(false, StringUtil.isNotBlank(str1));
		assertEquals(true, StringUtil.isNotBlank(str2));
		assertEquals(true, StringUtil.isNotBlank(str3));
		assertEquals(true, StringUtil.isNotBlank(str4));
	}

	public void testIsBlank() {
		String str1 = "";
		String str2 = " ";
		String str3 = "test";
		String str4 = "test ";

		assertEquals(true, StringUtil.isBlank(str1));
		assertEquals(false, StringUtil.isBlank(str2));
		assertEquals(false, StringUtil.isBlank(str3));
		assertEquals(false, StringUtil.isBlank(str4));
	}

	public void testIsNotEmpty() {
		String str1 = "";
		String str2 = " ";
		String str3 = "test";
		String str4 = "test ";

		assertEquals(false, StringUtil.isNotEmpty(str1));
		assertEquals(false, StringUtil.isNotEmpty(str2));
		assertEquals(true, StringUtil.isNotEmpty(str3));
		assertEquals(true, StringUtil.isNotEmpty(str4));
	}

	public void testIsEmpty() {
		String str1 = "";
		String str2 = " ";
		String str3 = "test";
		String str4 = "test ";

		assertEquals(true, StringUtil.isEmpty(str1));
		assertEquals(true, StringUtil.isEmpty(str2));
		assertEquals(false, StringUtil.isEmpty(str3));
		assertEquals(false, StringUtil.isEmpty(str4));
	}

	public void testEquals() {
		String str1 = "";
		String str2 = " ";
		String str3 = "test";
		String str4 = "test ";
		String str5 = null;
		String str6 = null;
		assertEquals(false, StringUtil.equals(str1, str2));
		assertEquals(false, StringUtil.equals(str3, str4));
		assertEquals(true, StringUtil.equals(str5, str6));
	}

	public void testCleanServletPath() {
		String path1 = "http://localhost/leo/test.jsp";
		String path2 = "http://localhost/leo/test.action";
		String path3 = "http://localhost/leo/test.do";
		String path4 = "http://localhost/leo/test";

		assertEquals("", StringUtil.parseServletPath(path1));
		assertEquals("test", StringUtil.parseServletPath(path2));
		assertEquals("", StringUtil.parseServletPath(path3));
		assertEquals("", StringUtil.parseServletPath(path4));
	}

	public void testStringArrayToString() {
		String[] arr1 = { "1", "2" };
		String[] arr2 = { "1" };
		String[] arr3 = { "1", null };
		String[] arr4 = { null, null };
		String[] arr5 = { null};
		assertEquals("1,2", StringUtil.StringArrayToString(arr1));
		assertEquals("1", StringUtil.StringArrayToString(arr2));
		assertEquals("1", StringUtil.StringArrayToString(arr3));
		assertEquals("", StringUtil.StringArrayToString(arr4));
		assertEquals("", StringUtil.StringArrayToString(arr5));
	}
}
