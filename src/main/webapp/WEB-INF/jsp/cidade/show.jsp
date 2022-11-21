<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="cidade.label" /></title>
</head>
<body>
	<div class="nav">
    	<span class="menuButton"><a href="<c:url value="/cidade/"/>" class="list"><spring:message code="default.button.list.label" /></a></span>
    	<span class="menuButton"><a href="<c:url value="/cidade/form"/>" class="create"><spring:message code="default.add.label" /></a></span>
    </div>
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="cidade.label" /></h1>
    	<c:url var="url" value="/cidade/${cidade.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="cidade.uf.label" />:</label>
				</td>
				<td valign="top" class="value">${cidade.ufId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="cidade.descricao.label" />:</label>
				</td>
				<td valign="top" class="value">${cidade.descricao}
				</td>
			</tr>
		</tbody>
		</table>
	</div>
	<div class="buttons">
	<c:url var="url" value="/cidade/${cidade.id}" />
            <form:form action="${url}/form" method="GET">
                <input alt="<spring:message code="default.button.edit.label" />" title="<spring:message code="default.button.edit.label" />" type="submit" value="<spring:message code="default.button.edit.label" />" class="edit"/>
            </form:form>
        </div>
</div>
</body>
</html>
