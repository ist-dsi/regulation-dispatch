package module.regulation.dispatch.domain.activities;

import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import myorg.domain.User;

public class RemoveDispatch extends AbstractWorkflowActivity {

    @Override
    public boolean isActive(RegulationDispatchWorkflowMetaProcess process, User user) {
	return process.isUserAbleToAccessCurrentQueues(user);
    }

    @Override
    protected void process(RegulationDispatchActivityInformation activityInformation) {
	activityInformation.getProcess().deactivate();
    }

}
