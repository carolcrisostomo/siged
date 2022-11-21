<%@ include file="../includes.jsp"%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<head>
<title><spring:message code="nota.label" /></title>
</head>

<body>
	<script type="text/javascript">
		jQuery('#justificativachefe1').attr('checked', false);
		jQuery('#justificativachefe2').attr('checked', false);
		jQuery('#justificativachefe3').attr('checked', false);
	</script>

	<div class="body">
		<%
			if (request.getParameter("mensagem") != null) {
		%><div class="message"><%=request.getParameter("mensagem")%></div>
		<%
			}
		%>
		<h1>Justificativa</h1>
		<c:url var="url" value="/inscricao/indicacao" />
		<form:form action="${url}" method="POST" modelAttribute="inscricao">
			<div class="dialog">
				<table>
					<tbody>
						<tr class="prop">
							<td valign="top" class="name"><form:checkbox
									id="justificativachefe1" path="justificativachefe"
									value="� necess�rio este conhecimento para um melhor desempenho dos servi�os da �rea." />
								� necess�rio este conhecimento para um melhor desempenho dos
								servi�os da �rea <br /> <br /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><form:checkbox
									id="justificativachefe2" path="justificativachefe"
									value="Embora j� conhe�a o assunto, a reciclagem � fundamental para o melhor desempenho dos servi�os da �rea." />
								Embora j� conhe�a o assunto, a reciclagem � fundamental para o
								melhor desempenho dos servi�os da �rea <br /> <br /></td>
						</tr>
						<tr class="prop">
							<td valign="top" class="name"><form:checkbox
									id="justificativachefe3" path="justificativachefe"
									value="� importante para o desenvolvimento geral do participante." />
								� importante para o desenvolvimento geral do participante <br /> <br /> <br /></td>
						</tr>
						<form:hidden path="id" />

					</tbody>
				</table>
			</div>
			<div class="buttons">
				<input id="atualizar" type="submit" class="edit" value="Indicar" />
			</div>
		</form:form>
	</div>
</body>
</html>
