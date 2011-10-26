package module.regulation.dispatch.domain;

import module.metaWorkflow.domain.Requestor;
import module.metaWorkflow.domain.UserRequestor;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.workflow.domain.WorkflowQueue;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchWorkflowMetaProcess extends RegulationDispatchWorkflowMetaProcess_Base {
    // TODO implements IRegulationDispatchEntry
    
    protected RegulationDispatchWorkflowMetaProcess() {
        super();

    }

    protected RegulationDispatchWorkflowMetaProcess(WorkflowMetaType type, String subject, String instanceDescription,
	    WorkflowQueue queue, Requestor requestor) {
	this();
	init(type, subject, instanceDescription, queue, requestor);
    }
    
    @Service
    public static RegulationDispatchWorkflowMetaProcess createNewProcess(String subject, String instanceDescription,
	    WorkflowQueue queue, User user) {
	Requestor requestor = user.hasRequestor() ? user.getRequestor() : new UserRequestor(user);

	return new RegulationDispatchWorkflowMetaProcess(queue.getMetaType(), subject, instanceDescription, queue, requestor);
    }
}
