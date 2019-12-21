<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>自己动手写struts2</title>
	</head>

	<body>
		请分别输入下面连接测试查看结果：
		<ul>
			<li>
				http://localhost/framework/
			</li>
			<li>
				http://localhost/framework/hello.action
			</li>
			<li>
				http://localhost/framework/hello.action?message=superleo
			</li>
		</ul>

		<p>
			表单提交测试
		</p>
		<form action="hello.action" method="post">
			<input type="text" name="message" value="" />
			<input type="submit" value="提交" />
		</form>
		<p>
			Action执行的结果：${message}
		</p>

	</body>
</html>
