package module.regulation.dispatch.scripts.manual;

import jvstm.TransactionalCommand;
import module.metaWorkflow.domain.MetaFieldSet;
import module.metaWorkflow.domain.StringsMetaField;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import myorg.domain.scheduler.CustomTask;
import myorg.util.BundleUtil;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AddArticlesMetaField extends CustomTask implements TransactionalCommand {

    @Override
    public void doIt() {
	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();

	MetaFieldSet parentMetaField = system.getRegulationMetaFieldSet();

	String articlesName = getKey("label.workflow.meta.type.field.articles");
	StringsMetaField articlesMetaField = new StringsMetaField(new MultiLanguageString(articlesName), 2, parentMetaField);
	system.setArticlesMetaField(articlesMetaField);
    }

    @Override
    public void run() {
	Transaction.withTransaction(false, this);
	out.println("Done.");
    }

    public static String getKey(String key) {
	return BundleUtil.getStringFromResourceBundle("/resources/RegulationDispatchResources", key);
    }
}
