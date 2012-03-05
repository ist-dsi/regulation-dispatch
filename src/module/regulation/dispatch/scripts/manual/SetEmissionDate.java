/*
 * @(#)SetEmissionDate.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Anil Kassamali
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Dispatch Registry Module.
 *
 *   The Dispatch Registry Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Dispatch Registry Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Dispatch Registry Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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

/**
 * 
 * @author Anil Kassamali
 * 
 */
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
