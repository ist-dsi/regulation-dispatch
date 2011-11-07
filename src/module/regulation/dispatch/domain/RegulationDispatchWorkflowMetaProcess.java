package module.regulation.dispatch.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.metaWorkflow.domain.LocalDateFieldValue;
import module.metaWorkflow.domain.Requestor;
import module.metaWorkflow.domain.StringFieldValue;
import module.metaWorkflow.domain.UserRequestor;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.Person;
import module.regulation.dispatch.domain.activities.AbstractWorkflowActivity;
import module.regulation.dispatch.domain.activities.CreateDispatch;
import module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import myorg.domain.User;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchWorkflowMetaProcess extends RegulationDispatchWorkflowMetaProcess_Base implements
	IRegulationDispatchEntry {

    protected static Map<String, AbstractWorkflowActivity> activityMap = new HashMap<String, AbstractWorkflowActivity>();

    static {
	activityMap.put(CreateDispatch.class.getSimpleName(), new CreateDispatch());
    }

    protected RegulationDispatchWorkflowMetaProcess() {
	super();
    }

    protected RegulationDispatchWorkflowMetaProcess(RegulationDispatchQueue queue, String reference, LocalDate emissionDate,
	    Person emissor, String regulationReference) {
	this();
	init(queue, reference, emissionDate, emissor, regulationReference);
    }

    @Service
    public static RegulationDispatchWorkflowMetaProcess createNewProcess(RegulationDispatchActivityInformation information,
	    final User user) {
	String reference = information.getReference();
	LocalDate emissionDate = information.getEmissionDate();
	Person emissor = information.getEmissor();
	String regulationReference = information.getRegulationReference();
	RegulationDispatchQueue queue = information.getQueue();

	return new RegulationDispatchWorkflowMetaProcess(queue, reference, emissionDate, emissor, regulationReference);
    }

    @Override
    protected void init(WorkflowMetaType type, String subject, String instanceDescription, WorkflowQueue queue,
	    Requestor requestor) {
	throw new RuntimeException("use other init()");
    }

    protected void init(RegulationDispatchQueue queue, String reference, LocalDate emissionDate,
	    Person emissor, String regulationReference) {
	WorkflowMetaType type = RegulationDispatchSystem.getInstance().getMetaType();
	
	Requestor requestor = emissor.getUser().getRequestor();
	if (requestor == null) {
	    requestor = new UserRequestor(emissor.getUser());
	}
	
	super.init(type, reference, "", queue, requestor);
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
    public String getDescription() {
	return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
	setDescription(description);
    }

    @Override
    public LocalDate getEmissionDate() {
	// TODO Retrieve the field value by a meta field reference
	// getField("some meta field reference");
	return null;
    }

    @Override
    public void setEmissionDate(final LocalDate emissionDate) {
	LocalDateFieldValue fieldValue = null;
	fieldValue.setLocalDateValue(emissionDate);
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
	// TODO Retrieve the field value by a meta field reference
	// getField("some meta field reference");
	return null;
    }

    @Override
    public void setRegulationReference(final String regulationReference) {
	StringFieldValue fieldValue = null;
	fieldValue.setStringValue(regulationReference);
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
    public boolean isActive() {
	return isOpen();
    }

    @Override
    public List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> getActivities() {
	List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> list = new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
	list.addAll(activityMap.values());
	return list;
    }
}
