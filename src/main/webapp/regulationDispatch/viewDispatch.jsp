<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<p>
	<a href="regulation-dispatch">Voltar</a>
</p>

<h2 class="mbottom20px"> 
	<bean:message key="title.dispatch" bundle="REGULATION_DISPATCH_RESOURCES" /> <bean:write name="dispatch" property="reference" />
</h2>

<fr:view name="dispatch">
	<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess" bundle="REGULATION_DISPATCH_RESOURCES">
		<fr:slot name="reference" />
		<fr:slot name="emissionDate" />
		<fr:slot name="instanceDescription" />
		<fr:slot name="requestorUser.presentationName" />
		<fr:slot name="regulationReference" layout="null-as-label"/>
		<fr:slot name="articles"/>
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mbottom20px" />
	</fr:layout>
</fr:view>

<%--
<h3> <bean:message key="title.observations" /></h3>

<fr:view name="dispatch">
	<fr:schema type="module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess" bundle="REGULATION_DISPATCH_RESOURCES">
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
				link="<%= "/regulationDispatch.do?method=downloadFile&amp;fileId=${externalId}" %>"
				label="link.download,REGULATION_DISPATCH_RESOURCES" />
		
		</fr:layout>

	</fr:view>
</logic:notEmpty>
