<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="queueId" name="queue" property="externalId" />

<p>
	<html:link action="/regulationDispatch?method=viewQueue" paramId="queueId" paramName="queueId">Voltar</html:link>
</p>

<h2 class="mbottom20px"> 
	<bean:message key="title.dispatch" bundle="REGULATION_DISPATCH_RESOURCES" /> <bean:write name="dispatch" property="reference" />
</h2>

<fr:view name="dispatch">
	<fr:schema type="module.regulation.dispatch.domain.IRegulationDispatchEntry" bundle="REGULATION_DISPATCH_RESOURCES">
		<fr:slot name="reference" />
		<fr:slot name="emissionDate" />
		<fr:slot name="dispatchDescription" />
		<fr:slot name="emissor.name" />
		<fr:slot name="regulationReference" layout="null-as-label"/>
		<fr:slot name="articles" layout="format">
			<fr:property name="format" value="${presentationString}" />
		</fr:slot>
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mbottom20px" />
	</fr:layout>
</fr:view>

<%--
<h3> <bean:message key="title.observations" /></h3>

<fr:view name="dispatch">
	<fr:schema type="module.regulation.dispatch.domain.IRegulationDispatchEntry" bundle="REGULATION_DISPATCH_RESOURCES">
		<fr:slot name="observations" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />
	</fr:layout>
</fr:view>
--%>

<h3 class="mtop20px mbottom20px"> <bean:message key="title.documents" bundle="REGULATION_DISPATCH_RESOURCES"/></h3>

<logic:empty name="dispatch" property="activeFiles">
	<p class="mtop20px mbottom20px"><em><bean:message key="message.regulation.dispatch.documents.empty" bundle="REGULATION_DISPATCH_RESOURCES" /><em></p>
</logic:empty>

<logic:notEmpty name="dispatch" property="activeFiles">
	<fr:view name="dispatch" property="activeFiles">
	
		<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchProcessFile" bundle="REGULATION_DISPATCH_RESOURCES">
			<fr:slot name="displayName" />
			<fr:slot name="mainDocument" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop20px mbottom20px" />
			
			<fr:link name="download" 
				link="<%= "/regulationDispatch.do?method=downloadFile&amp;fileId=${externalId}&amp;queueId=" + queueId %>"
				label="link.download,REGULATION_DISPATCH_RESOURCES" />
		
		</fr:layout>

	</fr:view>
</logic:notEmpty>
