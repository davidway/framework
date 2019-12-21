package com.framework.core;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.framework.util.StringUtil;
import com.framework.util.XmlUtil;

/**
 * @author superleo
 * @since 2008-8-12
 * @version
 */
public class ActionManager {

	/**
	 * struts.xml默认类路径
	 */
	public static final String STRUTS_XML_FILE = "/struts.xml";

	/**
	 * struts.xml类路径
	 */
	private String filePath;

	public ActionManager() {
		this.filePath = STRUTS_XML_FILE;
	}

	public ActionManager(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 用于单元测试
	 */
	public String test() {
		filePath = "/leo.xml";
		return "success";
	}

	/**
	 * 根据StrutsFilter传过来的actionName,得到相应的Action类
	 * 
	 * @param actionName
	 *            StrutsFilter传过来的actionName
	 * @return 相应的Action类的字符串
	 */
	public ActionClass getActionClass(String actionName) {
		// 得到struts.xml类路径
		InputStream is = this.getClass().getResourceAsStream(filePath);
		try {
			Document doc = XmlUtil.getDocument(is);
			Element root = XmlUtil.getRoot(doc);
			// 得到所有的package结点
			List<Element> packages = XmlUtil.getElementsByName(root, "package");
			if (packages != null && packages.size() > 0) {
				for (Element _package : packages) {
					// 得到某个package下的所有结点
					List<Element> actions = XmlUtil.getElements(_package);
					for (Element elem : actions) {
						// 判断某个<result>结点元素的name属性是否与传递过来的actionName相等,如果相等那么将其method属性取出
						Attribute att = XmlUtil
								.getAttributeByName(elem, "name");
						if (StringUtil.equals(att.getText(), actionName)) {
							Attribute cls = XmlUtil.getAttributeByName(elem,
									"class");
							Attribute method = XmlUtil.getAttributeByName(elem,
									"method");
							List<Element> results = XmlUtil.getElementsByName(
									elem, "result");
							ActionClass actionClass = new ActionClass();
							actionClass.setClassName(cls.getText());
							actionClass.setMethod(method.getText());
							actionClass.setElements(results);
							return actionClass;
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("struts.xml 不存在或有误");
		}
		throw new RuntimeException("找不到名称为 [" + actionName + "] 的Action映射");
	}

	/**
	 * 调用Action,并执行Action的无参方法
	 * 
	 * @param actionClass
	 * @param request.getParameterMap()
	 * @return
	 */
	public Object setActionValues(ActionClass actionClass,
			Map<String, String[]> params) {
		try {
			// 得到Action的Class,并根据无参构造函数生成一个Action对象
			Class clas = Class.forName(actionClass.getClassName());
			Object obj = clas.newInstance();

			if (params != null && params.size() > 0) {
				Iterator<String> it = params.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String[] value = params.get(key);
					String upperFirstLetter = key.substring(0, 1).toUpperCase();
					// 获得和属性对应的setXXX()方法的名字
					String setMethodName = "set" + upperFirstLetter
							+ key.substring(1);
					Method method = null;
					// 看看该页面提交的参数名中,是否在Action有set方法
					try {
						method = clas.getMethod(setMethodName,
								new Class[] { String.class });
					} catch (NoSuchMethodException e) {
						System.out.println("警告 " + actionClass.getClassName()
								+ "." + setMethodName + "("
								+ String.class.getName() + ") 不存在");
					}
					if (method != null) {
						// 如果有set方法,就调用set方法,进行赋值操作
						String result = StringUtil.StringArrayToString(value);
						method.invoke(obj, new String[] { result });
					}

				}

			}
			return obj;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("出现未知异常");
	}

	/**
	 * 调用Action,并执行Action的无参方法
	 * 
	 * @param actionClass
	 * @param obj
	 *            要处理的对象
	 * @return
	 */
	public Object invokeAction(ActionClass actionClass) {
		try {
			Object obj = actionClass.getAction();
			Class clas = obj.getClass();
			Method method = clas.getMethod(actionClass.getMethod(), null);
			String result = (String) method.invoke(obj, null);
			this.setInvokeResult(result, actionClass);
			actionClass.setAction(obj);
			return obj;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("出现InvocationTargetException异常:"
					+ e.getMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException("出现SecurityException异常:"
					+ e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("出现NoSuchMethodException异常:"
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("出现IllegalAccessException异常:"
					+ e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("出现IllegalArgumentException异常:"
					+ e.getMessage());
		}
	}

	/**
	 * 匹配<result name="success">/index.jsp</result> Xml中的result
	 * name属性，如果匹配成功,设置返回结果"/index.jsp"
	 * 
	 * @param result
	 * @param actionClass
	 */
	private void setInvokeResult(String result, ActionClass actionClass) {
		List<Element> elements = actionClass.getElements();
		for (Element elem : elements) {
			Attribute name = XmlUtil.getAttributeByName(elem, "name");
			if (StringUtil.equals(result, name.getText())) {
				actionClass.setResult(elem.getText());
				return;
			}

		}
		throw new RuntimeException("请确定在xml配置文件中是否有名叫 [" + result
				+ "]　的返回类型结点 ");
	}

	public String getFilePath() {
		return filePath;
	}

}
