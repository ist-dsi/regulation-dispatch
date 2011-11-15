package module.regulation.dispatch.domain.activities;

import java.io.IOException;

import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import myorg.domain.User;

public class UploadFile extends AbstractWorkflowActivity {

    @Override
    public boolean isActive(RegulationDispatchWorkflowMetaProcess process, User user) {
	return process.isUserAbleToAccessCurrentQueues(user);
    }

    @Override
    protected void process(RegulationDispatchActivityInformation activityInformation) {
	try {
	    byte[] fileContent = activityInformation.getFileContent();
	    String fileName = activityInformation.getFileName();

	    RegulationDispatchProcessFile.create(activityInformation.getProcess(), fileName, fileName, fileContent);
	} catch (IOException e) {
	    throw new RegulationDispatchException(e.getMessage());
	}
    }

}
