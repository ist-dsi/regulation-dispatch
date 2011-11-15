package module.regulation.dispatch.domain;

public class RegulationDispatchProcessFile extends RegulationDispatchProcessFile_Base {

    private RegulationDispatchProcessFile(final RegulationDispatchWorkflowMetaProcess process, final String displayName,
	    final String filename, final byte[] content) {
	super();
	init(displayName, filename, content);
	RegulationDispatchProcessFile mainDocument = process.getMainDocument();
	setProcess(process);

	activate();
	setMainDocument(mainDocument == null);
    }

    public static RegulationDispatchProcessFile create(final RegulationDispatchWorkflowMetaProcess process,
	    final String displayName, final String filename, final byte[] content) {
	return new RegulationDispatchProcessFile(process, displayName, filename, content);
    }

    public void activate() {
	setActive(true);
    }

    public void deactivate() {
	setActive(false);
    }

    @Override
    public RegulationDispatchWorkflowMetaProcess getProcess() {
	return (RegulationDispatchWorkflowMetaProcess) super.getProcess();
    }

    public void setAsMainDocument() {
	RegulationDispatchProcessFile currentMainDocument = getProcess().getMainDocument();
	currentMainDocument.setMainDocument(false);
	setMainDocument(true);
    }

    public boolean isAbleToRemove() {
	return getActive() && !getMainDocument();
    }

    public boolean isAbleToSetAsMainDocument() {
	return getActive() && !getMainDocument();
    }

}
