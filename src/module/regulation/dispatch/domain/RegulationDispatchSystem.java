package module.regulation.dispatch.domain;

import myorg.domain.MyOrg;

public class RegulationDispatchSystem extends RegulationDispatchSystem_Base {
    
    private RegulationDispatchSystem() {
        super();
	setMyOrg(MyOrg.getInstance());
    }
    
    public static RegulationDispatchSystem getInstance() {
	MyOrg myOrg = MyOrg.getInstance();

	if (!myOrg.hasRegulationDispatchSystem()) {
	    return createRegulationDispatchSystem();
	}

	return myOrg.getRegulationDispatchSystem();
    }

    private static RegulationDispatchSystem createRegulationDispatchSystem() {
	return new RegulationDispatchSystem();
    }
}
