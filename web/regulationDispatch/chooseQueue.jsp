<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2> <bean:message key="title.regulation.dispatch" bundle="REGULATION_DISPATCH_RESOURCES" /> </h2>

<logic:empty name="queues">
	<em><bean:message key="message.none.regulation.dispatch.systems" bundle="REGULATION_DISPATCH_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="queues">
	<em> <bean:message key="message.please.choose.one.of.systems.below" bundle="REGULATION_DISPATCH_RESOURCES" /></em>
	
	<fr:view name="queues">
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchQueue" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="name" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2" />
			<fr:link name="view" link="/regulationDispatch.do?method=viewQueue&amp;queueId=${externalId}" 
				label="link.view,REGULATION_DISPATCH_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:present role="myorg.domain.RoleType.MANAGER">

	<h3> <bean:message key="title.regulation.dispatch.system.management" bundle="REGULATION_DISPATCH_RESOURCES" /> </h3>
	
	<p><em><bean:message key="message.regulation.dispatch.management.system" bundle="REGULATION_DISPATCH_RESOURCES" /></em></p>
	
	<p>
		<html:link action="/manageRegulationDispatchQueues.do?method=manageQueues">
			<bean:message key="link.regulation.dispatch.systems.management" bundle="REGULATION_DISPATCH_RESOURCES" />	
		</html:link>
	</p>

</logic:present>
