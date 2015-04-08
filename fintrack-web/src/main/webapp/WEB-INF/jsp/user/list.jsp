<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>
<%@ taglib prefix="auth" uri="http://common.zv.org/auth" %>

<%@ page errorPage="/error.jsp" %>

<tmpl:composition template="/WEB-INF/jsp/include/template.jsp">
	<tmpl:define name="title">Finance Tracker - List</tmpl:define>
	<tmpl:define name="body">
		<div id="heading">Filter Records For Listing/Editing/Deletion</div><p/>
		
		<form method="post" action="<c:url value="/user/list!refresh.page"/>">
			<table cellspacing=5 cellpading=5>
				<tr>
					<td>Date from:</td>
					<td><input type="text" class="date-pick" id="dateFrom" name="dateFrom" value="<fmt:formatDate value="${dateFrom}" pattern="yyyy-MM-dd"/>" size="12"/></td>
				</tr>
				<tr>
					<td>Date to:</td>
					<td><input type="text" class="date-pick" id="dateTo" name="dateTo" value="<fmt:formatDate value="${dateTo}" pattern="yyyy-MM-dd"/>" size="12"/></td>
				</tr>
				<tr>
					<td>Categories: </td>
					<td><table>
						<tr><td><input type="checkbox" name="incomeSelected" value="true" <c:if test="${incomeSelected}">checked="true"</c:if>>Income</td></tr>
						<c:forEach var="category" items="${categories}">
							<tr><td><input type="checkbox" name="categoryIds" value="${category.categoryId}" title="${category.descr}"<c:if test="${categoryIds[category.categoryId]}">checked="true"</c:if>>${category.name}</td></tr>
						</c:forEach>
					</table></td>
				</tr>
				<tr>
					<td>User:</td>
					<td><input type="text" name="userId" value="${userId}" size="12"/></td>
				</tr>
			</table><p>
			<table><tr>
				<td><input type="submit" value="Refresh"/></td>
				<td><input type="reset" value="Reset"/></td>
			</tr></table>
		</form>
		<hr/>
		<b>Incomes</b>
		<table class="data">
			<tr><th class="inc_h">Date</th><th class="inc_h">Amount</th><th class="inc_h">Description</th><th class="inc_h">User</th><auth:isUserInRole roles="reporter"><th class="inc_h">Edit</th><th class="inc_h">Delete</th></auth:isUserInRole></tr>
			<c:forEach var="income" items="${incomes}">
				<tr>
					<td class="inc_l"><fmt:formatDate value="${income.createDate}" pattern="yyyy-MM-dd"/></td>
					<td class="inc_r">${income.amount}</td>
					<td class="inc_l">${income.descr}</td>
					<td class="inc_l">${income.userId}</td>
					<auth:isUserInRole roles="reporter">
						<td class="inc_l"><a href="<c:url value="/user/income!edit.page"/>?preinitId=${income.incomeId}">Edit</a></td>
						<td class="inc_l"><a href="<c:url value="/user/income!delete.page"/>?preinitId=${income.incomeId}">Delete</a></td>
					</auth:isUserInRole>
				</tr>
			</c:forEach>
		</table>
		<br/>
		<b>Expences</b>
		<table class="data">
			<tr><th class="exp_h">Date</th><th class="exp_h">Amount</th><th class="exp_h">Category</th><th class="exp_h">Description</th><th class="exp_h">User</th><auth:isUserInRole roles="reporter"><th class="exp_h">Edit</th><th class="exp_h">Delete</th></auth:isUserInRole></tr>
			<c:forEach var="expense" items="${expenses}">
				<tr>
					<td class="exp_l"><fmt:formatDate value="${expense.createDate}" pattern="yyyy-MM-dd"/></td>
					<td class="exp_r">${expense.amount}</td>
					<td class="exp_l">${expense.category.nameShort}</td>
					<td class="exp_l">${expense.descr}</td>
					<td class="exp_l">${expense.userId}</td>
					<auth:isUserInRole roles="reporter">
						<td class="exp_l"><a href="<c:url value="/user/expense!edit.page"/>?preinitId=${expense.expenseId}">Edit</a></td>
						<td class="exp_l"><a href="<c:url value="/user/expense!delete.page"/>?preinitId=${expense.expenseId}">Delete</a></td>
					</auth:isUserInRole>
				</tr>
			</c:forEach>
		</table>
	</tmpl:define>
</tmpl:composition>
