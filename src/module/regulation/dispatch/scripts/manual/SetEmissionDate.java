package module.regulation.dispatch.scripts.manual;

import java.util.List;

import jvstm.TransactionalCommand;
import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.scheduler.CustomTask;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixframework.pstm.Transaction;

public class SetEmissionDate extends CustomTask implements TransactionalCommand {

    @Override
    public void doIt() {
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");

	RegulationDispatchQueue queue = readQueue();

	List<WorkflowProcess> activeProcesses = queue.getActiveProcesses();

	for (WorkflowProcess workflowProcess : activeProcesses) {
	    RegulationDispatchWorkflowMetaProcess entry = (RegulationDispatchWorkflowMetaProcess) workflowProcess;

	    if ("116/2010".equals(entry.getReference())) {
		entry.setEmissionDate(dateFormatter.parseDateTime("09.07.2010").toLocalDate());
	    }

	    if ("115/2010".equals(entry.getReference())) {
		entry.setEmissionDate(dateFormatter.parseDateTime("09.07.2010").toLocalDate());
	    }

	    if ("114/2010".equals(entry.getReference())) {
		entry.setEmissionDate(dateFormatter.parseDateTime("09.07.2010").toLocalDate());
	    }
	}
    }

    @Override
    public void run() {
	Transaction.withTransaction(false, this);
	out.println("Done.");
    }

    private RegulationDispatchQueue readQueue() {
	return RegulationDispatchQueue.fromExternalId("7602092115122");
    }

}
