<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>
<%@ taglib prefix="auth" uri="http://common.zv.org/auth" %>

<%@ page errorPage="/error.jsp" %>

<tmpl:composition template="/WEB-INF/jsp/include/template.jsp">
	<tmpl:define name="header">
		<script language="JavaScript" src="<c:url value="/js/fintrack.input.js"/>"></script>
	</tmpl:define>
	<tmpl:define name="title">Finance Tracker - <c:if test="${empty income.incomeId}">Add</c:if><c:if test="${not empty income.incomeId}">Edit</c:if> Income</tmpl:define>
	<tmpl:define name="body">
		<div id="heading"><c:if test="${empty income.incomeId}">Add</c:if><c:if test="${not empty income.incomeId}">Edit</c:if> Income</div><p/>
		
		<form method="post" action="<c:url value="/user/income!save.page"/>">
			<input type="hidden" name="preinitId" value="${preinitId}"/>
			<table cellspacing=5 cellpading=5>
				<tr>
					<td>Date:</td>
					<td><input type="text" class="date-pick" name="createDate" value="<fmt:formatDate value="${income.createDate}" pattern="yyyy-MM-dd"/>" size="12"/></td>
				</tr>
				<tr>
					<td>Amount:</td>
					<td><input type="text" name="amount" class="focus" value="<c:if test="${income.amount != 0.0}">${income.amount}</c:if>" size="12"/></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><textarea name="descr" cols="30" rows="5">${income.descr}</textarea></td>
				</tr>
			</table><p>
			<table><tr>
				<td><input type="submit" value="Save"/></td>
				<td><input type="reset" value="Reset"/></td>
				<c:if test="${not empty income.incomeId}">
					<td><input type="submit" class="back" value="Back to list"/></td>
				</c:if>
			</tr></table>
			<div class="error">${error}</div>
			<div>${message}</div>
		</form>
	</tmpl:define>
</tmpl:composition>
