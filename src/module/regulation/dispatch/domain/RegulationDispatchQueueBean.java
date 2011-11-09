package module.regulation.dispatch.domain;

import java.io.Serializable;

import module.workflow.domain.WorkflowUserGroupQueueBean;
import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchQueueBean extends WorkflowUserGroupQueueBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public RegulationDispatchQueueBean() {
	super();
    }

    public RegulationDispatchQueueBean(final RegulationDispatchQueue queue) {
	super();
	setQueue(queue);
    }

    @Override
    @Service
    public RegulationDispatchQueue createWorkflowQueue() {
	return new RegulationDispatchQueue(this);
    }

}
