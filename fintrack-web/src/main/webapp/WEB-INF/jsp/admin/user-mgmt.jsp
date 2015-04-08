<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="http://fintrack.zv.org/template" %>
<%@ taglib prefix="auth" uri="http://fintrack.zv.org/auth" %>

<%@ page errorPage="/error.jsp" %>

<tmpl:composition template="/WEB-INF/include/template.jsp">
	<tmpl:define name="title">Finance Tracker - Users</tmpl:define>
	<tmpl:define name="body">
		<div id="heading">Create/Edit/Delete users</div><p/>

		<table class="data">
			<tr><th class="adm_h">User</th><th class="adm_h">Edit</th><th class="adm_h">Delete</th></tr>
			<c:forEach var="user" items="${users}">
				<tr>
					<td class="adm_l">${user}</td>
					<td class="adm_l"><a href="<c:url value="/admin/user-mgmt.page"/>?preinitId=${user.userId}">Edit</a></td>
					<td class="adm_l"><a href="<c:url value="/admin/user-mgmt!delete.page"/>?preinitId=${user.userId}">Delete</a></td>
				</tr>
			</c:forEach>
		</table>
		<hr/>
		<c:if test="${empty preinitId}">
			<form method="post" action="<c:url value="/admin/user-mgmt!create.page"/>">
				<table cellspacing=5 cellpading=5>
					<tr>
						<td>User Id:</td>
						<td><input type="text" name="userId" size="12"/></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password" size="12"/></td>
					</tr>
					<tr>
						<td>Repeat:</td>
						<td><input type="password" name="repeat" size="12"/></td>
					</tr>
					<tr>
						<td>Roles: </td>
						<td><table>
							<c:forEach var="role" items="${roles}">
								<tr><td><input type="checkbox" name="selectedRole" value="${role}"<c:if test="${categoryIdMap[role]}">checked="true"</c:if>>${role}</td></tr>
							</c:forEach>
						</table></td>
					</tr>
				</table><p>
				<table><tr>
					<td><input type="submit" value="Create New User"/></td>
					<td><input type="reset" value="Reset"/></td>
				</tr></table>
			</form>
		</c:if>
		<c:if test="${not empty preinitId}">
			<form method="post" action="<c:url value="/admin/user-mgmt!update.page"/>">
				<table cellspacing=5 cellpading=5>
					<tr>
						<td>User Id:</td>
						<td><input type="text" name="userId" value="${userId}" size="12"/></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password" value="${password}" size="12"/></td>
					</tr>
					<tr>
						<td>Repeat:</td>
						<td><input type="password" name="repeat" value="${repeat}" size="12"/></td>
					</tr>
					<tr>
						<td>Roles: </td>
						<td><table>
							<c:forEach var="role" items="${roles}">
								<tr><td><input type="checkbox" name="selectedRole" value="${role}"<c:if test="${categoryIdMap[role]}">checked="true"</c:if>>${role}</td></tr>
							</c:forEach>
						</table></td>
					</tr>
				</table><p>
				<table><tr>
					<td><input type="submit" value="Update Selected User"/></td>
					<td><input type="reset" value="Reset"/></td>
				</tr></table>
			</form>
		</c:if>

	</tmpl:define>
</tmpl:composition>
