<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>

<tmpl:composition template="/WEB-INF/jsp/include/template.jsp">
	<tmpl:define name="title">Fintrack - Login</tmpl:define>
	<tmpl:define name="body">
		<div id="heading">Fintrack login</div><p/>

		<form action="j_security_check">
			<table>
				<tr><td>User name:</td><td><input type="text" name="j_username"/></td></tr>			
				<tr><td>Password:</td><td><input type="password" name="j_password"/></td></tr>			
				<tr><td></td><td><input type="submit" value="Login"/></td></tr>			
			</table>
		</form>
	</tmpl:define>
</tmpl:composition>

