package module.regulation.dispatch.domain.activities;

import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;


public class CreateRegulationDispatchActivityInformation extends RegulationDispatchActivityInformation {

    private static final long serialVersionUID = 1L;

    public CreateRegulationDispatchActivityInformation(RegulationDispatchQueue queue) {
	super();
	setQueue(queue);
    }

    @Override
    public RegulationDispatchWorkflowMetaProcess getProcess() {
	throw new RuntimeException("invalid use");
    }

    @Override
    public String getLocalizedName() {
	throw new RuntimeException("invalid use");
    }

    @Override
    public void setLocalizedName(String localizedName) {
	throw new RuntimeException("invalid use");
    }

    @Override
    public void setProcess(RegulationDispatchWorkflowMetaProcess process) {
	throw new RuntimeException("invalid use");
    }

    @Override
    public boolean hasAllneededInfo() {
	throw new RuntimeException("invalid use");
    }

    @Override
    public void execute() {
	throw new RuntimeException("invalid use");
    }

    @Override
    public Class getActivityClass() {
	throw new RuntimeException("invalid use");
    }

    @Override
    public String getUsedSchema() {
	throw new RuntimeException("invalid use");
    }

}
