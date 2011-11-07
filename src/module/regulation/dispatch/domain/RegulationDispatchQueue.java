package module.regulation.dispatch.domain;

import java.util.ArrayList;
import java.util.List;

import myorg.domain.User;

public class RegulationDispatchQueue extends RegulationDispatchQueue_Base {
    
    RegulationDispatchQueue(String name, List<User> baseUsers) {
	super();
	init(name, baseUsers);
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
    
}
