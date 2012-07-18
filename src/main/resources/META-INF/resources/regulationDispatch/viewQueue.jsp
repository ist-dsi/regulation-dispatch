<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2 class="mbottom20px"><bean:write name="queue" property="name" /></h2>

<bean:define id="queueId" name="queue" property="externalId" />

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>

<style type="text/css" title="currentStyle">
	@import "<%= request.getContextPath() + "/javaScript/dataTables/media/css/demo_table.css" %>";

</style>

<style type="text/css" title="currentStyle">

	.entry_deleted {
		opacity: 0.5;
		filter: alpha(opacity = 20);
		zoom: 1;
	}
	
</style>

<script type="text/javascript">

	function rowCallBackImpl(nRow, aData, iDisplayIndex) {
		if(aData[6] == "false" || aData[7] == "false") {
			$(nRow).addClass("entry_deleted");
		}
		
		$(nRow).find("td.width0").remove();

		return nRow;
	}
	
</script>


<bean:define id="activeEntries" name="queue" property="activeEntries" />

<logic:empty name="activeEntries">
	<bean:message key="message.regulation.dispatches.empty" bundle="REGULATION_DISPATCH_RESOURCES" /> 
</logic:empty> 

<p class="mtop20px mbottom20px">
	<html:link action="/createRegulationDispatch.do?method=prepare" paramId="queueId" paramName="queueId" >
		<bean:message key="link.regulation.dispatch.create.entry" bundle="REGULATION_DISPATCH_RESOURCES" />
	</html:link>
</p>


<style type="text/css" title="currentStyle">
	th.actions {
		width : 140px;
	}
</style>

<logic:notEmpty name="activeEntries">

	<fr:view name="activeEntries">
		<fr:schema type="module.regulation.dispatch.domain.IRegulationDispatchEntry" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="reference" />
			<fr:slot name="emissionDate" layout="null-as-label" />
			<fr:slot name="dispatchDescription" />
			<fr:slot name="emissor" />
			<fr:slot name="regulationReference" layout="null-as-label" />
		</fr:schema>
		
		<fr:layout name="ajax-tabular">
			<fr:property name="classes" value="tstyle3 mtop05 mbottom05"/>
			<fr:property name="style" value="width: 100%;"/>
			
			<fr:property name="headerClasses" value="width40px,width60px,,,,actions" />
			<fr:property name="columnClasses" value="width40px,width60px,,,,nowrap" />
			
			<fr:property name="ajaxSourceUrl" value="/regulationDispatch.do" />
		

			<fr:property name="linkFormat(view)" value="<%= "/regulationDispatch.do?method=viewDispatch&amp;dispatchId=${externalId}&amp;queueId=" + queueId %>" />
			<fr:property name="bundle(view)" value="REGULATION_DISPATCH_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="order(view)" value="1" />
			<fr:property name="icon(view)" value="view" />
	
			<fr:property name="linkFormat(edit)" value="<%= "/createRegulationDispatch.do?method=prepareEdit&amp;dispatchId=${externalId}" %>"/>
			<fr:property name="bundle(edit)" value="REGULATION_DISPATCH_RESOURCES"/>
			<fr:property name="key(edit)" value="link.edit"/>
			<fr:property name="order(edit)" value="2" />
			<fr:property name="icon(edit)" value="edit" />
	
			<fr:property name="linkFormat(delete)" value="<%= "/createRegulationDispatch.do?method=removeDispatch&amp;dispatchId=${externalId}&amp;queueId=" + queueId %>"/>
			<fr:property name="bundle(delete)" value="REGULATION_DISPATCH_RESOURCES"/>
			<fr:property name="key(delete)" value="link.remove"/>
			<fr:property name="order(delete)" value="3" />
			<fr:property name="confirmationBundle(delete)" value="REGULATION_DISPATCH_RESOURCES" />
			<fr:property name="confirmationKey(delete)" value="message.regulation.dispatch.confirmation" />
			<fr:property name="icon(delete)" value="delete" />
			
			
			<fr:property name="linkFormat(document)" value="<%= "/regulationDispatch.do?method=downloadMainDocument&amp;dispatchId=${externalId}&amp;queueId=" + queueId %>" />
			<fr:property name="bundle(document)" value="REGULATION_DISPATCH_RESOURCES" />
			<fr:property name="key(document)" value="link.view.document" />
			<fr:property name="order(document)" value="4" />
			<fr:property name="icon(document)" value="document" />
			
			<fr:property name="extraParameter(method)" value="processesForAjaxDataTable" />
			<fr:property name="extraParameter(queueId)" value="<%= queueId.toString() %>" />
			
		</fr:layout>
		
	</fr:view>

</logic:notEmpty>
