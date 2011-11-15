package module.regulation.dispatch.domain.activities;

import module.regulation.dispatch.domain.IRegulationDispatchEntry;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.workflow.domain.ActivityLog;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;

public class CreateDispatch {

    @Service
    public IRegulationDispatchEntry create(CreateRegulationDispatchBean bean, final User user) {
	RegulationDispatchWorkflowMetaProcess process = RegulationDispatchWorkflowMetaProcess.createNewProcess(bean, user);
	new ActivityLog(process, user, "CreateRegulationDispatchEntry", process.getReference());
	
	return process;
    }

}
