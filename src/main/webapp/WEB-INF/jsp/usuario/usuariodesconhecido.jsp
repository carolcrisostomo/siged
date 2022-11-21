<%@ include file="../includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Usu�rio desconhecido</title>
</head>
<body>
<div class="body">
<% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
<div class="nav"><span class="menuButton"><a
	href="<c:url value="/j_spring_security_logout"/>" class="home"><spring:message
	code="default.button.back.label" /></a></span></div>
</div>
</body>
</html>
