/*
 * @(#)AddArticlesMetaField.java
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

import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.StringsMetaField;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;
import pt.ist.bennu.core.util.BundleUtil;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class AddArticlesMetaField extends WriteCustomTask {

    @Override
    public void doService() {
        RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();

        MetaFieldSet parentMetaField = system.getRegulationMetaFieldSet();

        String articlesName = getKey("label.workflow.meta.type.field.articles");
        StringsMetaField articlesMetaField = new StringsMetaField(new MultiLanguageString(articlesName), 2, parentMetaField);
        system.setArticlesMetaField(articlesMetaField);
    }

    public static String getKey(String key) {
        return BundleUtil.getStringFromResourceBundle("resources/RegulationDispatchResources", key);
    }
}
