<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>
<%@ page isErrorPage="true" %>

<%= exception %>

<tmpl:composition template="/WEB-INF/jsp/include/template.jsp">
	<tmpl:define name="header"></tmpl:define>
	<tmpl:define name="title">Fintrack - Error</tmpl:define>
	<tmpl:define name="body">
		<div id="heading">Error</div><p/>

		<c:if test="${not empty exception}">
			<style type="text/css">
				<!-- body {color:red} -->
			</style>	
		</c:if>
	</tmpl:define>
</tmpl:composition>
