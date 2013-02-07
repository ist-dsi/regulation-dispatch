/*
 * @(#)MetaTypeFactory.java
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
package module.regulation.dispatch.domain.metaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import module.metaWorkflow.domain.LocalDateMetaField;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.StringMetaField;
import module.metaWorkflow.domain.StringsMetaField;
import module.metaWorkflow.domain.WorkflowMetaType;
import module.organization.domain.OrganizationalModel;
import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import module.workflow.domain.ProcessFile;
import pt.ist.bennu.core.util.BundleUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class MetaTypeFactory {

    public static WorkflowMetaType createMetaType() {
        Locale pt = new Locale("pt", "PT");
        Language.setLocale(pt);
        RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();

        if (system.hasMetaType()) {
            throw new RegulationDispatchException("workflow meta type for regulation dispatch is already created");
        }
        String name = getKey("label.workflow.meta.type.name");
        String description = getKey("label.workflow.meta.type.description");

        OrganizationalModel model = readOrganizationalModel();
        List<Class<? extends ProcessFile>> associatedProcessFileTypes = associatedProcessFileTypes();

        WorkflowMetaType metaType = WorkflowMetaType.createNewMetaType(name, description, model, associatedProcessFileTypes);
        system.setMetaType(metaType);

        defineParameters(metaType);

        Language.setLocale(null);

        return metaType;
    }

    private static void defineParameters(WorkflowMetaType metaType) {
        RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();

        MetaFieldSet rootFieldSet = metaType.getFieldSet();

        String emissionDateName = getKey("label.workflow.meta.type.field.emissionDate");
        LocalDateMetaField emissionDateMetaField =
                new LocalDateMetaField(new MultiLanguageString(emissionDateName), 1, rootFieldSet);
        system.setEmissionDateMetaField(emissionDateMetaField);

        String regulationName = getKey("label.workflow.meta.type.field.regulation");
        MetaFieldSet regulationMetaField = new MetaFieldSet(new MultiLanguageString(regulationName), 2, rootFieldSet);
        regulationMetaField.setRegulationDispatchSystemForRegulation(system);

        String regulationReferenceName = getKey("label.workflow.meta.type.field.regulation.reference");
        StringMetaField regulationReferenceMetaField =
                new StringMetaField(new MultiLanguageString(regulationReferenceName), 1, regulationMetaField);
        system.setRegulationReferenceMetaField(regulationReferenceMetaField);

        String articlesName = getKey("label.workflow.meta.type.field.articles");
        StringsMetaField articlesMetaField = new StringsMetaField(new MultiLanguageString(articlesName), 2, regulationMetaField);
        system.setArticlesMetaField(articlesMetaField);
    }

    private static OrganizationalModel readOrganizationalModel() {
        return null;
    }

    private static List<Class<? extends ProcessFile>> associatedProcessFileTypes() {
        List<Class<? extends ProcessFile>> classes = new ArrayList<Class<? extends ProcessFile>>();
        classes.add(RegulationDispatchProcessFile.class);

        return classes;
    }

    public static String getKey(String key) {
        return BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources", key);
    }
}
