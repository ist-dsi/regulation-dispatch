package module.regulation.dispatch.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.domain.FieldSetValue;
import module.metaWorkflow.domain.FieldValue;
import module.metaWorkflow.domain.LocalDateFieldValue;
import module.metaWorkflow.domain.LocalDateMetaField;
import module.metaWorkflow.domain.MetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.Requestor;
import module.metaWorkflow.domain.StringFieldValue;
import module.metaWorkflow.domain.StringMetaField;
import module.metaWorkflow.domain.UserRequestor;
import module.metaWorkflow.domain.WorkflowMetaProcess;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.Person;
import module.regulation.dispatch.domain.activities.AbstractWorkflowActivity;
import module.regulation.dispatch.domain.activities.CreateRegulationDispatchBean;
import module.regulation.dispatch.domain.activities.EditDispatch;
import module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import myorg.domain.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchWorkflowMetaProcess extends RegulationDispatchWorkflowMetaProcess_Base implements
	IRegulationDispatchEntry {

    protected static Map<String, AbstractWorkflowActivity> activityMap = new HashMap<String, AbstractWorkflowActivity>();

    static {
	activityMap.put("EditDispatch", new EditDispatch());
    }

    protected RegulationDispatchWorkflowMetaProcess() {
	super();
    }

    protected RegulationDispatchWorkflowMetaProcess(RegulationDispatchQueue queue, String reference, LocalDate emissionDate,
	    Person emissor, String description, String regulationReference) {
	this();
	init(queue, reference, emissionDate, emissor, description, regulationReference);
    }

    public static RegulationDispatchWorkflowMetaProcess createNewProcess(final CreateRegulationDispatchBean bean, final User user) {
	String reference = bean.getReference();
	LocalDate emissionDate = bean.getEmissionDate();
	Person emissor = bean.getEmissor();
	String regulationReference = bean.getRegulationReference();
	RegulationDispatchQueue queue = bean.getQueue();
	String description = bean.getDispatchDescription();

	return new RegulationDispatchWorkflowMetaProcess(queue, reference, emissionDate, emissor, description,
		regulationReference);
    }

    @Service
    public static WorkflowMetaProcess createNewProcess(String subject, String instanceDescription, WorkflowQueue queue, User user) {
	throw new RuntimeException("invalid use");
    }

    @Override
    protected void init(WorkflowMetaType type, String subject, String instanceDescription, WorkflowQueue queue,
	    Requestor requestor) {
	throw new RuntimeException("use other init()");
    }

    protected void init(RegulationDispatchQueue queue, String reference, LocalDate emissionDate, Person emissor,
	    String description, String regulationReference) {
	WorkflowMetaType type = RegulationDispatchSystem.getInstance().getMetaType();

	Requestor requestor = emissor.getUser().getRequestor();
	if (requestor == null) {
	    requestor = new UserRequestor(emissor.getUser());
	}

	super.init(type, reference, description, queue, requestor);

	setRegulationReference(regulationReference);
	setEmissionDate(emissionDate);
    }

    @Override
    public String getReference() {
	return getSubject();
    }

    @Override
    public void setReference(final String reference) {
	setSubject(reference);
    }

    @Override
    public String getDispatchDescription() {
	return super.getInstanceDescription();
    }

    @Override
    public void setDispatchDescription(String description) {
	super.setInstanceDescription(description);
    }

    @Override
    public LocalDate getEmissionDate() {
	RegulationDispatchSystem instance = RegulationDispatchSystem.getInstance();
	LocalDateFieldValue fieldValue = (LocalDateFieldValue) findFieldValueByMetaField(instance.getEmissionDateMetaField());

	if (fieldValue == null) {
	    return null;
	}

	return fieldValue.getLocalDateValue();
    }

    @Override
    public void setEmissionDate(final LocalDate emissionDate) {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
	LocalDateMetaField emissionDateMetaField = system.getEmissionDateMetaField();
	LocalDateFieldValue fieldValue = (LocalDateFieldValue) findFieldValueByMetaField(emissionDateMetaField);

	if (fieldValue != null) {
	    fieldValue.setLocalDateValue(emissionDate);
	    return;
	}

	new LocalDateFieldValue((LocalDateMetaField) emissionDateMetaField, getFieldSet(), emissionDate);
    }

    @Override
    public Person getEmissor() {
	return getRequestor().getUser().getPerson();
    }

    @Override
    public void setEmissor(Person emissor) {
	setRequestor(emissor.getUser().getRequestor());
    }

    @Override
    public String getRegulationReference() {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
	StringMetaField metaField = system.getRegulationReferenceMetaField();
	StringFieldValue fieldValue = (StringFieldValue) findFieldValueByMetaField(metaField);

	if (fieldValue == null) {
	    return null;
	}

	return fieldValue.getStringValue();
    }

    @Override
    public void setRegulationReference(final String regulationReference) {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
	StringMetaField metaField = system.getRegulationReferenceMetaField();
	StringFieldValue fieldValue = (StringFieldValue) findFieldValueByMetaField(metaField);

	if (fieldValue == null) {
	    fieldValue.setStringValue(regulationReference);
	    return;
	}

	MetaFieldSet parentMetaFieldSet = system.getRegulationMetaFieldSet();
	FieldSetValue parentFieldSet = (FieldSetValue) findFieldValueByMetaField(parentMetaFieldSet);
	new StringFieldValue(regulationReference, parentFieldSet, metaField);
    }

    @Override
    public DateTime getUpdateDate() {
	if (getDateFromLastActivity() != null) {
	    return getDateFromLastActivity();
	}

	return getCreationDate();
    }

    @Override
    public void activate() {
	open();
    }

    @Override
    public void deactivate() {
	close();
    }

    @Override
    public List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> getActivities() {
	List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> list = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
	list.addAll(activityMap.values());
	return list;
    }

    private FieldValue findFieldValueByMetaField(final MetaField metaField) {
	return findFieldValueByMetaFieldRec(getFieldSet(), metaField);
    }

    private FieldValue findFieldValueByMetaFieldRec(final FieldSetValue fieldSetValue, final MetaField metaField) {
	if (fieldSetValue.getMetaField() == metaField) {
	    return fieldSetValue;
	}

	for (FieldValue fieldValue : fieldSetValue.getChildFieldValues()) {

	    if (fieldValue.isFieldSet()) {
		FieldValue ret = findFieldValueByMetaFieldRec((FieldSetValue) fieldValue, metaField);

		if (ret != null) {
		    return ret;
		}
	    } else {
		if (fieldValue.getMetaField() == metaField) {
		    return fieldValue;
		}
	    }
	}

	return null;
    }

    public void edit(RegulationDispatchActivityInformation activityInformation) {
	setReference(activityInformation.getReference());
	setEmissionDate(activityInformation.getEmissionDate());
	setDispatchDescription(activityInformation.getDispatchDescription());
	setEmissor(activityInformation.getEmissor());
	setRegulationReference(activityInformation.getRegulationReference());
    }
}
