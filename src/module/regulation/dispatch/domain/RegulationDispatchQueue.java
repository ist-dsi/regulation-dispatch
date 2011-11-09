package module.regulation.dispatch.domain;

import java.util.ArrayList;
import java.util.List;

import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchQueue extends RegulationDispatchQueue_Base {
    
    RegulationDispatchQueue(String name, List<User> baseUsers) {
	super();
	init(name, baseUsers);
	setRegulationDispatchSystem(RegulationDispatchSystem.getInstance());
    }
    
    RegulationDispatchQueue(final RegulationDispatchQueueBean bean) {
	this(bean.getName(), new ArrayList<User>(bean.getUsers()));
    }
    
    public static List<RegulationDispatchQueue> getRegulationDispatchQueuesForUser(final User user) {
	List<RegulationDispatchQueue> result = new ArrayList<RegulationDispatchQueue>();
	List<RegulationDispatchQueue> queues = RegulationDispatchSystem.getInstance().getQueues();

	for (RegulationDispatchQueue regulationDispatchQueue : queues) {
	    if (regulationDispatchQueue.isUserAbleToAccessQueue(user)) {
		queues.add(regulationDispatchQueue);
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

}
