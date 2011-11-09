<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h1>Fila <bean:write name="queue" property="name" /></h1>

<bean:define id="queueId" name="queue" property="externalId" />

<p><html:link action="<%= "/createRegulationDispatch?method=prepare&amp;queueId=" + queueId %>"> Inserir processo </html:link></p>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>

<style type="text/css" title="currentStyle">
	@import "<%= request.getContextPath() + "/javaScript/dataTables/media/css/demo_table.css" %>";
	.saviourDiv {
		height: 30px;
	}
</style>

<style type="text/css" title="currentStyle">

	.entry_deleted {
		opacity: 0.5;
		filter: alpha(opacity = 20);
		zoom: 1;
	}
	
</style>


<logic:empty name="searchEntries">
	<bean:message key="message.searched.correspondence.entries.empty" bundle="MAIL_TRACKING_RESOURCES" /> 
</logic:empty> 


<style type="text/css" title="currentStyle">
	th.actions {
		width : 140px;
	}
</style>

<logic:notEmpty name="searchEntries">

<%--
	<fr:view name="searchEntries" schema="<%= CorrespondenceType.SENT.name().equals(correspondenceType) ? "module.mailtracking.correspondence.sent.entries.view" : "module.mailtracking.correspondence.received.entries.view" %>" >
		<fr:layout name="ajax-tabular">
			<fr:property name="classes" value="tstyle3 mtop05 mbottom05"/>
			<fr:property name="style" value="width: 100%;"/>
			
			<fr:property name="headerClasses" value="<%= CorrespondenceType.SENT.name().equals(correspondenceType) ? ",,,,," : ",,,,,," %>" />
			<fr:property name="columnClasses" value="<%= CorrespondenceType.SENT.name().equals(correspondenceType) ? "width30px,width50px,,,,nowrap" : "width30px,width50px,,width20px,,,nowrap" %>" />
			
			<fr:property name="ajaxSourceUrl" value="/regulationDispatch.do" />
	
			<fr:property name="linkFormat(view)" value="<%= mailTrackingUrl + "&amp;method=viewEntry&amp;entryId=${externalId}" %>" />
			<fr:property name="bundle(view)" value="MAIL_TRACKING_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="order(view)" value="2" />
			<fr:property name="visibleIf(view)" value="userAbleToView" />
			<fr:property name="icon(view)" value="view" />
	
			<fr:property name="linkFormat(edit)" value="<%= mailTrackingUrl + "&amp;method=prepareEditEntry&amp;entryId=${externalId}" %>"/>
			<fr:property name="bundle(edit)" value="MAIL_TRACKING_RESOURCES"/>
			<fr:property name="key(edit)" value="link.edit"/>
			<fr:property name="order(edit)" value="3" />
			<fr:property name="visibleIf(edit)" value="userAbleToEdit" />
			<fr:property name="icon(edit)" value="edit" />
	
			<fr:property name="linkFormat(delete)" value="<%= mailTrackingUrl + "&amp;method=prepareDeleteEntry&amp;entryId=${externalId}" %>"/>
			<fr:property name="bundle(delete)" value="MAIL_TRACKING_RESOURCES"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="order(delete)" value="4" />
			<fr:property name="visibleIf(delete)" value="userAbleToDelete" />
			<fr:property name="icon(delete)" value="delete" />
			
			<fr:property name="linkFormat(document)" value="mailtracking.do" />
			<fr:property name="bundle(document)" value="MAIL_TRACKING_RESOURCES" />
			<fr:property name="key(document)" value="link.view.document" />
			<fr:property name="order(document)" value="5" />
			<fr:property name="visibleIf(document)" value="userAbleToViewDocument" />
			<fr:property name="icon(document)" value="document" />
	
			<fr:property name="linkFormat(copyEntry)" value=""/>
			<fr:property name="bundle(copyEntry)" value="MAIL_TRACKING_RESOURCES" />
			<fr:property name="key(copyEntry)" value="link.copy.entry" />
			<fr:property name="order(copyEntry)" value="6" />
			<fr:property name="visibleIf(copyEntry)" value="userAbleToCopyEntry" />
			<fr:property name="icon(copyEntry)" value="copyEntry" />
					
			<fr:property name="extraParameter(method)" value="processesForAjaxDataTable" />
			<fr:property name="extraParameter(queueId)" value="<%= (String) queueId %>" />
		</fr:layout>
	</fr:view>
	
--%>

</logic:notEmpty>
