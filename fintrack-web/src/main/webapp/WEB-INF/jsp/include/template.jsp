<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="http://common.zv.org/template" %>
<%@ taglib prefix="auth" uri="http://common.zv.org/auth" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="<c:url value="/css/jquery.datePicker.css"/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value="/css/fintrack.css"/>" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="/favicon.ico">
	<script language="JavaScript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script language="JavaScript" src="<c:url value="/js/date.js"/>"></script>
	<script language="JavaScript" src="<c:url value="/js/jquery.datePicker.js"/>"></script>
	<script language="JavaScript" src="<c:url value="/js/fintrack.js"/>"></script>
	<tmpl:insert name="header"/>
    <title><tmpl:insert name="title"/></title>
</head>

<body>
<table>
<tr>
	<td valign="top">
		<div id="menu">
		<ul>
			<li class="sub"><img src="<c:url value="/images/fintrack.png"/>"/></li>
			<li class="sub"><div class="heading">Finances</div></li>
			<auth:isUserLoggedIn>
				<li class="sub">
					<ul>
						<li class="item"><a href="<c:url value="/user/home.page"/>">Home</a></li>
						<li class="item"><a href="<c:url value="/user/income.page"/>">Add Income</a></li>
						<li class="item"><a href="<c:url value="/user/expense.page"/>">Add Expense</a></li>
						<li class="item"><a href="<c:url value="/user/list.page"/>">List</a></li>
						<li class="item"><a href="<c:url value="/user/summary.page"/>">Summary</a></li>
					</ul>
				</li>
<%--				
				<auth:isUserInRole roles="admin">
					<li class="sub"><div class="heading">Administration</div></li>
					<li class="sub">
						<ul>
							<li class="item"><a href="<c:url value="/admin/user-mgmt.page"/>">User Mgmt</a></li>
						</ul>
					</li>
				</auth:isUserInRole>
--%>					
				<li class="sub"><div class="heading">Session</div></li>
				<li class="sub">
					<ul>
						<li class="item"><a href="<c:url value="/logout.page"/>">Logout <auth:currentUser default="guest"/></a></li>
					</ul>
				</li>
			</auth:isUserLoggedIn>
			<auth:isUserLoggedIn not="true">
				<li class="sub">
					<ul>
						<li class="item"><a href="<c:url value="/user/home.page"/>">Login</a></li>
					</ul>
				</li>
			</auth:isUserLoggedIn>
		</ul>
		</div>
	</td>
	<td colspan="10"/>
	<td valign="top">
		<div class="body">
			<tmpl:insert name="body"/>
		</div>
	</td>
</tr>
</table>
</body>
</html>
