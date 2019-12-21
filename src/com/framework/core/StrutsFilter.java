/************************************************************

  Copyright (C), 1996-2008, resoft Tech. Co., Ltd.
  FileName: StrutsFilter.java
  Author: superleo       
  Version: 1.0     
  Date:2008-8-5 上午11:23:51
  Description: StrutsFilter   

  Function List:   
    1. -------

  History:         
      <author>    <time>   <version >   <desc>


 ***********************************************************/

package com.framework.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.framework.util.StringUtil;

/**
 * @author superleo
 * @since 2008-8-5
 * @version
 */
public class StrutsFilter implements Filter {

	private FilterConfig filterConfig;

	private ActionManager manager = new ActionManager();

	public void destroy() {

	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		ServletContext servletContext = filterConfig.getServletContext();
		// 解析Request的URL和传过来的参数
		String actionName = StringUtil.parseServletPath(request
				.getServletPath());

		// 如果后缀不为.action,那么直接放过，不进行拦截
		if (StringUtil.isEmpty(actionName)) {
			chain.doFilter(request, response);
		} else {
			// 解析得到ActionClass,里面包括Action的类全名,返回页面值,Action执行的方法
			ActionClass clas = this.getActionClass(actionName);
			// 得到页面的所有parameters参数(没考虑上传情况)
			Map<String, String[]> params = request.getParameterMap();
			// 为要调用的Action的set**方法设值,并返回要调用的Action对象本身
			setBeforeActionValue(clas, params);
			// 调用的Action执行方法,并返回值设置在request域中
			setResultValue(clas, request);
			// 返回相应的JSP页面
			servletContext.getRequestDispatcher(clas.getResult()).forward(
					request, response);
		}
	}

	/**
	 * 提到ActionClass,里面包括要处理的类,返回页面值,等详细信息
	 * 
	 * @param actionName
	 * @see com.framework.core.ActionClass
	 * @return
	 */
	private ActionClass getActionClass(String actionName) {
		if (StringUtil.isNotBlank(actionName)) {
			// 判断是否存在此actionName
			return manager.getActionClass(actionName);
		}
		throw new RuntimeException("actionName 不能为空");
	}

	/**
	 * 在Action处理之前,将页面的参数与Action对应的set**方法进行调用。
	 * 
	 * @param clas
	 *            ActionClass
	 * @param params
	 *            request.getParameterMap()
	 * @return Action对象
	 * 
	 */
	private void setBeforeActionValue(ActionClass clas,
			Map<String, String[]> params) {
		Object action = manager.setActionValues(clas, params);
		clas.setAction(action);
	}

	/**
	 * 调用Action的执行方法,并将结果存于request域中
	 * 
	 * @param clas
	 *            ActionClass类
	 * @param request
	 */
	private void setResultValue(ActionClass clas, HttpServletRequest request) {
		Object obj = manager.invokeAction(clas);
		Field[] fields = obj.getClass().getDeclaredFields();
		Method[] methods = obj.getClass().getMethods();
		// 判断某个字段是否有get方法，如果有，则将其设置在request中
		for (Field field : fields) {
			String fieldName = field.getName();
			String upperFirstLetter = fieldName.substring(0, 1).toUpperCase();
			// 获得和属性对应的getXXX()方法的名字
			String getMethodName = "get" + upperFirstLetter
					+ fieldName.substring(1);
			// 获得和属性对应的getXXX()方法
			for (Method method : methods) {
				if (StringUtil.equals(getMethodName, method.getName())) {
					field.setAccessible(true);
					try {
						request.setAttribute(field.getName(), field.get(obj));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

}
