package module.regulation.dispatch.domain;

import java.util.ArrayList;
import java.util.List;

import module.organization.domain.Person;
import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchQueue extends RegulationDispatchQueue_Base {
    
    RegulationDispatchQueue(String name, List<User> baseUsers) {
	super();
	init(name, baseUsers);
	setRegulationDispatchSystem(RegulationDispatchSystem.getInstance());
	setMetaType(RegulationDispatchSystem.getInstance().getMetaType());
    }
    
    RegulationDispatchQueue(final RegulationDispatchQueueBean bean) {
	this(bean.getName(), new ArrayList<User>(bean.getUsers()));
    }
    
    public static List<RegulationDispatchQueue> getRegulationDispatchQueuesForUser(final User user) {
	List<RegulationDispatchQueue> result = new ArrayList<RegulationDispatchQueue>();
	List<RegulationDispatchQueue> queues = RegulationDispatchSystem.getInstance().getQueues();

	for (RegulationDispatchQueue regulationDispatchQueue : queues) {
	    if (regulationDispatchQueue.isUserAbleToAccessQueue(user)) {
		result.add(regulationDispatchQueue);
	    }
	}

	return result;
    }
    
    @Override
    @Service
    public void addUsers(User user) {
	if (isUserAbleToAccessQueue(user)) {
	    throw new DomainException("user is already in queue", new String[0]);
	}

	super.addUsers(user);
    }

    public List<IRegulationDispatchEntry> getActiveEntries() {
	List<IRegulationDispatchEntry> result = new ArrayList<IRegulationDispatchEntry>();
	List<WorkflowProcess> activeProcesses = super.getActiveProcesses();

	for (WorkflowProcess workflowProcess : activeProcesses) {
	    result.add((IRegulationDispatchEntry) workflowProcess);
	}

	return result;
    }

    public List<IRegulationDispatchEntry> getNotActiveEntries() {
	List<IRegulationDispatchEntry> result = new ArrayList<IRegulationDispatchEntry>();
	List<WorkflowProcess> notActiveProcesses = super.getNotActiveProcesses();

	for (WorkflowProcess workflowProcess : notActiveProcesses) {
	    result.add((IRegulationDispatchEntry) workflowProcess);
	}

	return result;
    }

    public List<IRegulationDispatchEntry> getAllEntries() {
	List<IRegulationDispatchEntry> result = getActiveEntries();
	result.addAll(getNotActiveEntries());

	return result;
    }

    public List<IRegulationDispatchEntry> findEntriesBy(String sSearch) {
	List<IRegulationDispatchEntry> result = new ArrayList<IRegulationDispatchEntry>();
	List<IRegulationDispatchEntry> activeEntries = getActiveEntries();

	for (IRegulationDispatchEntry dispatch : activeEntries) {
	    String reference = dispatch.getReference();
	    LocalDate emissionDate = dispatch.getEmissionDate();
	    String dispatchDescription = dispatch.getDispatchDescription();
	    Person emissor = dispatch.getEmissor();
	    String regulationReference = dispatch.getRegulationReference() != null ? dispatch.getRegulationReference() : "";

	    if (reference.toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
		result.add(dispatch);
	    } else if (emissionDate.toString("dd/MM/yyyy").indexOf(sSearch.toLowerCase()) > -1) {
		result.add(dispatch);
	    } else if (dispatchDescription.toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
		result.add(dispatch);
	    } else if (emissor.getName().toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
		result.add(dispatch);
	    } else if (regulationReference.toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
		result.add(dispatch);
	    }
	}

	return result;
    }

}
