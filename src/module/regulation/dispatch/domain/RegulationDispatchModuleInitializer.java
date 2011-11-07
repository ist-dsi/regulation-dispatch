package module.regulation.dispatch.domain;

import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.services.Service;

public class RegulationDispatchModuleInitializer extends RegulationDispatchModuleInitializer_Base {

    private static boolean isInitialized = false;

    private static ThreadLocal<RegulationDispatchModuleInitializer> init = null;

    public static RegulationDispatchModuleInitializer getInstance() {
	if (init != null) {
	    return init.get();
	}

	if (!isInitialized) {
	    initialize();
	}
	final MyOrg myOrg = MyOrg.getInstance();
	return myOrg.getRegulationDispatchModuleInitializer();
    }

    @Service
    public synchronized static void initialize() {
	if (!isInitialized) {
	    try {
		final MyOrg myOrg = MyOrg.getInstance();
		final RegulationDispatchModuleInitializer initializer = myOrg.getRegulationDispatchModuleInitializer();
		if (initializer == null) {
		    new RegulationDispatchModuleInitializer();
		}
		init = new ThreadLocal<RegulationDispatchModuleInitializer>();
		init.set(myOrg.getRegulationDispatchModuleInitializer());

		isInitialized = true;
	    } finally {
		init = null;
	    }
	}
    }
    
    private RegulationDispatchModuleInitializer() {
        super();
	setMyOrg(MyOrg.getInstance());
    }

    @Override
    public void init(MyOrg root) {
	// MetaTypeFactory.createMetaType();
    }
    
}
