<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h1>Gestão dos despachos</h1>

<logic:empty name="queues">
	<em>Não tem permissão para aceder a nenhuma fila</em>
</logic:empty>

<logic:notEmpty name="queues">
	<fr:view name="queues">
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchQueue" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="name" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:link name="view" link="/regulationDispatch.do?method=viewQueue&amp;queueId=${externalId}" 
				label="link.view,REGULATION_DISPATCH_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
