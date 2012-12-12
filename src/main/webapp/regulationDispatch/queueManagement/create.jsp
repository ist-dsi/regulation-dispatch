<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<p>
	<html:link action="/manageRegulationDispatchQueues.do?method=manageQueues">
		<bean:message key="link.back" bundle="REGULATION_DISPATCH_RESOURCES" />
	</html:link>
</p>

<h2 class="mtop20px mbottom20px">
	<bean:message key="title.regulation.dispatch.system.creation" bundle="REGULATION_DISPATCH_RESOURCES" />
</h2>

<fr:form action="/manageRegulationDispatchQueues.do?method=createQueue">
	
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="create-bean" name="bean">
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchQueueBean" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="name" required="true"/>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
		
		<fr:destination name="invalid" path="/manageRegulationDispatchQueues.do?method=createQueueInvalid" />
		<fr:destination name="cancel" path="/manageRegulationDispatchQueues.do?method=manageQueues" />
	</fr:edit>

	<p>
		<html:submit><bean:message key="link.submit" bundle="REGULATION_DISPATCH_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="link.cancel" bundle="REGULATION_DISPATCH_RESOURCES" /></html:cancel>
	</p>
</fr:form>
