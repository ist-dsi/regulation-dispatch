package module.regulation.dispatch.domain.activities;

import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import myorg.domain.User;

public class CreateDispatch extends AbstractWorkflowActivity {

    @Override
    public boolean isActive(RegulationDispatchWorkflowMetaProcess process, User user) {
	return true;
    }

    @Override
    protected void process(RegulationDispatchActivityInformation activityInformation) {
	RegulationDispatchWorkflowMetaProcess.createNewProcess(activityInformation, getLoggedPerson());
    }

}
