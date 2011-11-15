<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h1>Gestão de filas</h1>

<h2>Fila <bean:write name="queue" property="name" /></h2>

<h3>Editar propriedades da fila</h3>

<bean:define id="queueId" name="queue" property="externalId" />

<fr:form action="<%= "/manageRegulationDispatchQueues.do?method=editQueue&amp;queueId=" + queueId %>">
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="edit-bean" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchQueueBean" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="name" />
		</fr:schema>
		
		<fr:destination name="invalid" path="/manageRegulationDispatchQueues.do?method=editQueueInvalid" />
	</fr:edit>

	<p>
		<html:submit>Editar</html:submit>
	</p>	
</fr:form>

<hr />

<h3> Adicionar utilizador </h3>

<fr:form action="<%= "/manageRegulationDispatchQueues.do?method=addUserToQueue&amp;queueId=" + queueId %>">
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="add-user-bean" name="bean" >
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchQueueBean" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="userToAdd" layout="autoComplete"
					validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
				<fr:property name="labelField" value="username" />
				<fr:property name="format" value="${presentationName}" />
				<fr:property name="minChars" value="3" />
				<fr:property name="args"
					value="provider=myorg.presentationTier.renderers.autoCompleteProvider.UserAutoComplete" />
				<fr:property name="size" value="40" />
			</fr:slot>
		</fr:schema>
	</fr:edit>
	
	<p>
		<html:submit>Adicionar</html:submit>
	<p>
</fr:form>


<h3> Utilizadores </h3>

<fr:view name="queue" property="users">
	<fr:schema type="myorg.domain.User" bundle="REGULATION_DISPATCH_RESOURCES">
		<fr:slot name="presentationName" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />
		
		<fr:link name="remove" link="<%= "/manageRegulationDispatchQueues.do?method=removeUserFromQueue&amp;userId=${externalId}&amp;queueId=" + queueId %>"
			label="link.remove,REGULATION_DISPATCH_RESOURCES" 
			confirmation="message.remove.confirmation,REGULATION_DISPATCH_RESOURCES" />
	</fr:layout>
</fr:view>
