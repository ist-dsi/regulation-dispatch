<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<p>
	<a href="regulation-dispatch">
		<bean:message key="link.back" bundle="REGULATION_DISPATCH_RESOURCES" />
	</a>
</p>

<h2 class="mbottom20px"> 
	<bean:message key="title.regulation.dispatch.creation" bundle="REGULATION_DISPATCH_RESOURCES" />
</h2>

<fr:form action="/createRegulationDispatch.do?method=create">

	<fr:edit id="bean" name="bean" visible="false" />

	<fr:edit id="bean-create" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.activities.CreateRegulationDispatchBean" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="reference" required="true"/>
			<fr:slot name="emissionDate" required="true" />
			<fr:slot name="dispatchDescription" required="true" />
			<fr:slot name="regulationReference" />
			<fr:slot name="emissor" layout="autoComplete"
				validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">

		        <fr:property name="labelField" value="user.displayName"/>
				<fr:property name="format" value="<%= "${user.displayName}" %>"/>
				<fr:property name="minChars" value="3"/>
				<fr:property name="args" value="provider=module.organization.presentationTier.renderers.providers.PersonAutoCompleteProvider"/>
				<fr:property name="size" value="60"/>
			</fr:slot>
		</fr:schema>
		
		<fr:destination name="invalid" path="/createRegulationDispatch.do?method=createInvalid"/>
		<fr:destination name="cancel" path="/regulation-dispatch"/>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
		
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="link.create" bundle="REGULATION_DISPATCH_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="link.cancel" bundle="REGULATION_DISPATCH_RESOURCES" /></html:cancel>
	</p>
</fr:form>
