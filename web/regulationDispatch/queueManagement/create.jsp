<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<p>
	<html:link action="/manageRegulationDispatchQueues.do?method=manageQueues">
		Voltar
	</html:link>
</p>


<h1>Gestão de filas</h1>

<h2>Criar fila</h2>


<fr:form action="/manageRegulationDispatchQueues.do?method=createQueue">
	
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="create-bean" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchQueueBean" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="name" />
		</fr:schema>
		
		<fr:destination name="invalid" path="/manageRegulationDispatchQueues.do?method=createQueueInvalid" />
		<fr:destination name="cancel" path="/manageRegulationDispatchQueues.do?method=manageQueues" />
	</fr:edit>

	<p>
		<html:submit>Submeter</html:submit>
		<html:cancel>Cancelar</html:cancel>
	</p>
</fr:form>

