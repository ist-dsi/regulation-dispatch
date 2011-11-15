<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="queueId" name="queue" property="externalId" />

<p>
	<html:link action="/regulationDispatch.do?method=viewQueue" paramId="queueId" paramName="queueId"> 
		Voltar 
	</html:link>
</p>

<h1> Editar despacho </h1>

<fr:form action="<%= "/createRegulationDispatch.do?method=edit&amp;queueId=" + queueId %>">

	<fr:edit id="bean" name="bean" visible="false" />

	<fr:edit id="bean-create" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="reference" required="true"/>
			<fr:slot name="emissionDate" required="true" />
			<fr:slot name="dispatchDescription" required="true" />
			<fr:slot name="regulationReference" />
			<fr:slot name="emissor" layout="autoComplete"
				validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
				
		        <fr:property name="labelField" value="name"/>
				<fr:property name="format" value="${partyName} (${user.username})"/>
				<fr:property name="minChars" value="3"/>
				<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PersonAutoCompleteProvider"/>
				<fr:property name="size" value="60"/>
			</fr:slot>
		</fr:schema>
		
		<fr:destination name="invalid" path="<%= "/createRegulationDispatch.do?method=editInvalid&amp;queueId=" + queueId %>"/>
		<fr:destination name="cancel" path="<%= "/regulationDispatch.do?method=viewQueue&amp;queueId=" + queueId %>"/>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
		
	</fr:edit>
	
	<p>
		<html:submit>Editar</html:submit>
		<html:cancel>Cancelar</html:cancel>
	</p>
</fr:form>
