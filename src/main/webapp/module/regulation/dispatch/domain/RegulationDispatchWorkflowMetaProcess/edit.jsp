<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="dispatchId" name="dispatch" property="externalId" />

<p>
	<a href="regulation-dispatch">
		<bean:message key="link.back" bundle="REGULATION_DISPATCH_RESOURCES" />
	</a>
</p>

<h2 class="mbottom20px"> <bean:message key="title.regulation.dispatch.edition" bundle="REGULATION_DISPATCH_RESOURCES" /> </h2>

<fr:form action="<%= String.format("/createRegulationDispatch.do?method=edit&amp;dispatchId=%s", dispatchId) %>">

	<fr:edit id="bean" name="bean" visible="false" />

	<fr:edit id="bean-create" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="reference" required="true"/>
			<fr:slot name="emissionDate" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
			</fr:slot>
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
			<fr:slot name="articles" />
		</fr:schema>
		
		<fr:destination name="invalid" path="<%= String.format("/createRegulationDispatch.do?method=editInvalid&amp;dispatchId=%s", dispatchId) %>"/>
		<fr:destination name="cancel" path="/regulationDispatch.do?method=viewQueue"/>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
		
	</fr:edit>
	
	<p><html:submit><bean:message key="link.edit" bundle="REGULATION_DISPATCH_RESOURCES" /></html:submit></p>
	
</fr:form>

<h2 class="mbottom20px mtop20px"> <bean:message key="title.documents" bundle="REGULATION_DISPATCH_RESOURCES" /></h2>

<fr:view name="dispatch" property="files">
	<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchProcessFile" bundle="REGULATION_DISPATCH_RESOURCES">
		<fr:slot name="displayName" />
		<fr:slot name="creationDate" />
		<fr:slot name="active" />
		<fr:slot name="mainDocument" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />

		<fr:link name="download" 
			link="<%= String.format("/createRegulationDispatch.do?method=download&amp;dispatchId=%s&amp;fileId=${externalId}", dispatchId) %>" 
			label="link.download,REGULATION_DISPATCH_RESOURCES" />
			
		<fr:link name="remove" 
			link="<%= String.format("/createRegulationDispatch.do?method=removeFile&amp;dispatchId=%s&amp;fileId=${externalId}", dispatchId) %>"
			label="link.remove,REGULATION_DISPATCH_RESOURCES" 
			condition="ableToRemove" />
			
		<fr:link name="mainDocument" 
			link="<%= String.format("/createRegulationDispatch.do?method=putFileAsMainDocument&amp;dispatchId=%s&amp;fileId=${externalId}", dispatchId) %>"
			label="link.module.regulation.dispatch.domain.RegulationDispatchProcessFile.asMainDocument,REGULATION_DISPATCH_RESOURCES" 
			condition="ableToSetAsMainDocument" />
	</fr:layout>
		
</fr:view>

<h3 class="mtop20px"> <bean:message key="title.upload.documents" bundle="REGULATION_DISPATCH_RESOURCES" /> </h3>

<p> <em><bean:message key="message.upload.documents.pdf.maximum.8mb" bundle="REGULATION_DISPATCH_RESOURCES" /></em> </p>

<fr:form action="<%= String.format("/createRegulationDispatch.do?method=upload&amp;dispatchId=%s", dispatchId) %>" encoding="multipart/form-data">
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-upload" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="file" required="true" >
				<fr:property name="fileNameSlot" value="fileName" />
				<fr:property name="fileSizeSlot" value="fileSize" />
				<fr:property name="fileContentTypeSlot" value="mimeType" />
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator" >
					<property name="required" value="true" />
					<property name="maxSize" value="8388608" />
					<property name="acceptedExtensions" value="pdf" />
				</fr:validator>
			</fr:slot>
		</fr:schema>

		<fr:destination name="invalid" 
			path="<%= String.format("/createRegulationDispatch.do?method=uploadInvalid&amp;dispatchId=%s", dispatchId) %>"/>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="link.submit" bundle="REGULATION_DISPATCH_RESOURCES" /></html:submit>
	</p>
</fr:form>

