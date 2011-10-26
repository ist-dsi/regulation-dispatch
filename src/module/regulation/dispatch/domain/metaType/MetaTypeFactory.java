package module.regulation.dispatch.domain.metaType;

import java.util.ArrayList;
import java.util.List;

import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.OrganizationalModel;
import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import module.workflow.domain.ProcessFile;
import myorg.util.BundleUtil;

public class MetaTypeFactory {

    public static WorkflowMetaType createMetaType() {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
	
	if(system.hasMetaType()) {
	    throw new RegulationDispatchException("workflow meta type for regulation dispatch is already created");
	}

	
	String name = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatch", "label.workflow.meta.type.name");
	String description = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatch", "label.workflow.meta.type.description");
	
	
	OrganizationalModel model = readOrganizationalModel();
	List<Class<? extends ProcessFile>> associatedProcessFileTypes = associatedProcessFileTypes();
	
	WorkflowMetaType createNewMetaType = WorkflowMetaType.createNewMetaType(name, description, model, associatedProcessFileTypes);

	defineParameters(createNewMetaType);

	return createNewMetaType;
    }

    private static void defineParameters(WorkflowMetaType metaType) {
	MetaFieldSet rootFieldSet = metaType.getFieldSet();

    }

    private static OrganizationalModel readOrganizationalModel() {
	return null;
    }

    private static List<Class<? extends ProcessFile>> associatedProcessFileTypes() {
	List<Class<? extends ProcessFile>> classes = new ArrayList<Class<? extends ProcessFile>>();
	classes.add(RegulationDispatchProcessFile.class);

	return classes;
    }
}
