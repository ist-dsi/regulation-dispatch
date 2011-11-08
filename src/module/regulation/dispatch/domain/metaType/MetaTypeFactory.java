package module.regulation.dispatch.domain.metaType;

import java.util.ArrayList;
import java.util.List;

import module.metaWorkflow.domain.LocalDateMetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.StringMetaField;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.OrganizationalModel;
import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import module.workflow.domain.ProcessFile;
import myorg.util.BundleUtil;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MetaTypeFactory {

    public static WorkflowMetaType createMetaType() {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
	
	if(system.hasMetaType()) {
	    throw new RegulationDispatchException("workflow meta type for regulation dispatch is already created");
	}
	
	String name = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources",
		"label.workflow.meta.type.name");
	String description = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources",
		"label.workflow.meta.type.description");
	
	OrganizationalModel model = readOrganizationalModel();
	List<Class<? extends ProcessFile>> associatedProcessFileTypes = associatedProcessFileTypes();
	
	WorkflowMetaType createNewMetaType = WorkflowMetaType.createNewMetaType(name, description, model, associatedProcessFileTypes);

	defineParameters(createNewMetaType);

	return createNewMetaType;
    }

    private static void defineParameters(WorkflowMetaType metaType) {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();

	MetaFieldSet rootFieldSet = metaType.getFieldSet();

	String emissionDateName = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources",
		"label.workflow.meta.type.field.emissionDate");
	LocalDateMetaField emissionDateMetaField = new LocalDateMetaField(new MultiLanguageString(emissionDateName), 1, rootFieldSet);
	system.setEmissionDateMetaField(emissionDateMetaField);

	String regulationName = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources",
		"label.workflow.meta.type.field.regulation");
	MetaFieldSet regulationMetaField = new MetaFieldSet(new MultiLanguageString(regulationName), 2, rootFieldSet);
	system.setRegulationMetaFieldSet(regulationMetaField);
	
	String regulationReferenceName = BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources",
		"label.workflow.meta.type.field.regulation.reference");
	StringMetaField regulationReferenceMetaField = new StringMetaField(new MultiLanguageString(regulationReferenceName), 1,
		regulationMetaField);
	system.setRegulationReferenceMetaField(regulationReferenceMetaField);
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
