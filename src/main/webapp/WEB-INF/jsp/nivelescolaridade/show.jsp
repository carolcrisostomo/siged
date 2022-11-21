<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="nivelEscolaridade.label" /></title>
</head>
<body>

  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="nivelEscolaridade.label" /></h1>
    	<c:url var="url" value="/nivelescolaridade/${nivelescolaridade.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="nome"><spring:message code="nivelEscolaridade.descricao.label" />:</label>
				</td>
				<td valign="top" class="value">${nivelescolaridade.descricao}
				</td>
			</tr>
		</tbody>
		</table>
		
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/nivelescolaridade/"/>"><button type="button"
							class="btn btn-outline-secondary">
							<spring:message code="default.button.list.label" />
						</button></a>
				</div>
			</div>
		
	</div>
</div>
</body>
</html>
