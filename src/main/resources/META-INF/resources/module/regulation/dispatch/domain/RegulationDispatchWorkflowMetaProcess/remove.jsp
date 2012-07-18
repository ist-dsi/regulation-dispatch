<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="queueId" name="queue" property="externalId" />
<bean:define id="dispatchId" name="dispatch" property="externalId" />

<h2 class="mbottom20px"> <bean:message key="title.regulation.dispatch.removal" bundle="REGULATION_DISPATCH_RESOURCES" /> </h2>

<p><em><bean:message key="message.regulation.dispatch.remove.confirmation" bundle="REGULATION_DISPATCH_RESOURCES" /></em></p>

<fr:form action="<%= String.format("/createRegulationDispatch.do?method=removeDispatch&amp;queueId=%s&amp;dispatchId=%s", queueId, dispatchId) %>">
	<fr:edit id="bean" name="bean" visible="false">
		<fr:destination name="cancel" path="<%= "/regulationDispatch.do?method=viewQueue&amp;queueId=" + queueId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="button.yes" bundle="REGULATION_DISPATCH_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="button.no" bundle="REGULATION_DISPATCH_RESOURCES" /></html:cancel>
</fr:form>
