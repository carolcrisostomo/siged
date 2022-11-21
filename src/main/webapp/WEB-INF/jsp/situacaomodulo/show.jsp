<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="situacaoModulo.label" /></title>
</head>
<body>
	<div class="nav">
    	<span class="menuButton"><a href="<c:url value="/situacaomodulo/"/>" class="list"><spring:message code="default.button.list.label" /></a></span>
    </div>
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="situacaoModulo.label" /></h1>
    	<c:url var="url" value="/situacaomodulo/${situacaomodulo.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="situacaoModulo.descricao.label" />:</label>
				</td>
				<td valign="top" class="value">${situacaomodulo.descricao}
				</td>
			</tr>
		</tbody>
		</table>
	</div>
</div>
</body>
</html>
