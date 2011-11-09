package module.regulation.dispatch.presentationTier;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.workflow.domain.WorkflowUserGroupQueue;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.UserGroup;
import myorg.presentationTier.Context;
import myorg.presentationTier.LayoutContext;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/regulationDispatch")
public class RegulationDispatchAction extends ContextBaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	WorkflowUserGroupQueue regulationProcessesQueue = readQueue(request);
	request.setAttribute("queue", regulationProcessesQueue);

	return super.execute(mapping, form, request, response);
    }

    @CreateNodeAction(bundle = "REGULATION_DISPATCH_RESOURCES", key = "link.node.configuration.regulation.dispatch.interface", groupKey = "title.node.configuration.module.regulation.dispatch")
    public ActionForward prepareCreateNewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	ActionNode.createActionNode(virtualHost, node, "/regulationDispatch", "prepare", "resources.RegulationDispatchResources",
		"label.sideBar.regulation.dispatch.manage", UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	User user = UserView.getCurrentUser();
	List<RegulationDispatchQueue> queues = RegulationDispatchQueue.getRegulationDispatchQueuesForUser(user);

	request.setAttribute("queues", queues);

	return forward(request, "/regulationDispatch/chooseQueue.jsp");
    }

    public ActionForward viewQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (readQueue(request) == null) {
	    return prepare(mapping, form, request, response);
	}

	return forward(request, "/regulationDispatch/viewQueue.jsp");
    }

    public ActionForward processesForAjaxDataTable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return null;
    }

    private RegulationDispatchQueue readQueue(final HttpServletRequest request) {
	return getDomainObject(request, "queueId");
    }

    protected RegulationDispatchWorkflowMetaProcess getProcess(final HttpServletRequest request) {
	return getDomainObject(request, "processId");
    }

    @Override
    public Context createContext(String contextPathString, HttpServletRequest request) {
	LayoutContext context = (LayoutContext) super.createContext(contextPathString, request);
	context.addHead("/regulationDispatch/layoutHead.jsp");
	return context;
    }

}
