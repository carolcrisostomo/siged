<%@ include file="../includes.jsp" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <style type="text/css" media="screen">   
    @import url("<c:url value="/static/styles/style.css"/>");
  </style> 
<title><spring:message code="tipoLocalizacaoEvento.label" /></title>
</head>
<body>
	
  		<div class="body"><% if (request.getParameter("mensagem")!=null) {%><div class="message"><%= request.getParameter("mensagem")%></div><% } %>
  		<h1><spring:message code="default.button.show.label" /> - <spring:message code="tipoLocalizacaoEvento.label" /></h1>
    	<c:url var="url" value="/tipolocalizacaoevento/${tipolocalizacaoevento.id}" />
    	<div class="dialog">
	<table>
		<tbody>
			<tr class="prop">
				<td valign="top" class="name"><label for="descricao"><spring:message code="tipoLocalizacaoEvento.descricao.label" />:</label>
				</td>
				<td valign="top" class="value">${tipolocalizacaoevento.descricao}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="endereco"><spring:message code="tipoLocalizacaoEvento.endereco.label" />:</label>
				</td>
				<td valign="top" class="value">${tipolocalizacaoevento.endereco}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="paisId"><spring:message code="tipoLocalizacaoEvento.pais.label" />:</label>
				</td>
				<td valign="top" class="value">${tipolocalizacaoevento.paisId}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="uf"><spring:message code="tipoLocalizacaoEvento.uf.label" />:</label>
				</td>
				<td valign="top" class="value">${tipolocalizacaoevento.municipio.ufMunicipio}
				</td>
			</tr>
			<tr class="prop">
				<td valign="top" class="name"><label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
				</td>
				<td valign="top" class="value">${fn:toUpperCase(tipolocalizacaoevento.municipio.nome)}
				</td>
			</tr>
			
			<tr class="prop" id="cidadePaisLinha">
				<td valign="top" class="name"><label for="cidadeId"><spring:message code="tipoLocalizacaoEvento.cidade.label" />:</label>
				</td>
				<td valign="top" class="value">${tipolocalizacaoevento.cidadePais}
				</td>
			</tr>
			
			<tr class="prop">
				<td valign="top" class="name"><label for="tipoLocalizacaoModalidade"><spring:message code="tipoLocalizacaoEvento.tipoLocalizacaoModalidade.label" />:</label>
				</td>
				<td valign="top" class="value">${tipolocalizacaoevento.tipoLocalizacaoModalidade.label}
				</td>
			</tr>

		</tbody>
		</table>
	</div>
		<div class="mx-auto" style="width: 400px;">
				<div class="d-flex justify-content-around p-2 ">

					<a href="<c:url value="/tipolocalizacaoevento/"/>"><button type="button" class="btn btn-outline-secondary"><spring:message
							code="default.button.list.label" /></button></a>

				</div>
			</div>
</div>
</body>
</html>
