<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>
<%@ taglib prefix="auth" uri="http://common.zv.org/auth" %>

<%@ page errorPage="/error.jsp" %>

<tmpl:composition template="/WEB-INF/jsp/include/template.jsp">
	<tmpl:define name="header">
		<script language="JavaScript" src="<c:url value="/js/fintrack.input.js"/>"></script>
	</tmpl:define>
	<tmpl:define name="title">Finance Tracker - Delete Income</tmpl:define>
	<tmpl:define name="body">
		<div id="heading">Delete Income</div><p/>
		
		<form method="post" action="<c:url value="/user/income!doDelete.page"/>">
			<input type="hidden" name="preinitId" value="${preinitId}"/>
			<c:if test="${not empty income.incomeId}">
				<table cellspacing=5 cellpading=5>
					<tr>
						<td>Date:</td>
						<td><fmt:formatDate value="${income.createDate}" pattern="yyyy-MM-dd"/></td>
					</tr>
					<tr>
						<td>Amount:</td>
						<td>${income.amount}</td>
					</tr>
					<tr>
						<td>Description:</td>
						<td><textarea name="descr" cols="30" rows="5" readonly="readonly">${income.descr}</textarea></td>
					</tr>
				</table>
			</c:if>
			<p>
			<table><tr>
				<c:if test="${not empty income.incomeId}">
					<td><input type="submit" value="I'm sure I want to delete the item"/></td>
					<td><input type="reset" value="Reset"/></td>
				</c:if>
				<td><input type="submit" class="back" value="Back to list"/></td>
			</tr></table>
			<div class="error">${error}</div>
			<div>${message}</div>
		</form>
	</tmpl:define>
</tmpl:composition>
