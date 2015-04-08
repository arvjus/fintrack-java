<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>

<%@ page errorPage="/error.jsp" %>

<tmpl:composition template="/WEB-INF/jsp/include/template.jsp">
	<tmpl:define name="title">Fintrack - Recently added records</tmpl:define>
	<tmpl:define name="body">
		<div id="heading">Recently added records</div><p/>
		
		<b>Incomes</b>
		<table class="data">
			<tr><th class="inc_h">Date</th><th class="inc_h">Amount</th><th class="inc_h">Description</th></tr>
			<c:forEach var="income" items="${requestScope.recentIncomes}">
				<tr>
					<td class="inc_l"><fmt:formatDate value="${income.createDate}" pattern="yyyy-MM-dd"/></td>
					<td class="inc_r">${income.amount}</td>
					<td class="inc_l">${income.descr}</td>
				</tr>
			</c:forEach>
		</table>
		<br/>
		<b>Expences</b>
		<table class="data">
			<tr><th class="exp_h">Date</th><th class="exp_h">Amount</th><th class="exp_h">Category</th><th class="exp_h">Description</th></tr>
			<c:forEach var="expense" items="${requestScope.recentExpenses}">
				<tr>
					<td class="exp_l"><fmt:formatDate value="${expense.createDate}" pattern="yyyy-MM-dd"/></td>
					<td class="exp_r">${expense.amount}</td>
					<td class="exp_l">${expense.category.nameShort}</td>
					<td class="exp_l">${expense.descr}</td>
				</tr>
			</c:forEach>
		</table>
	</tmpl:define>
</tmpl:composition>
