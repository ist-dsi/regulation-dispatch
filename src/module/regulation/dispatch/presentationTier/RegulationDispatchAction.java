package module.regulation.dispatch.presentationTier;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.workflow.domain.WorkflowUserGroupQueue;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.VirtualHost;
import myorg.domain.contents.ActionNode;
import myorg.domain.contents.Node;
import myorg.domain.groups.UserGroup;
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

    @CreateNodeAction(bundle = "REGULATION_DISPATCH_RESOURCES", key = "label.regulation.dispatch.interface", groupKey = "title.module.regulationDispatch")
    public ActionForward prepareCreateNewPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	ActionNode.createActionNode(virtualHost, node, "/regulationDispatch", "prepare",
		"resources.RegulationDispatchResources", "link.sideBar.regulationDispatch.manageRegulationDispatch",
		UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	User user = UserView.getCurrentUser();
	List<RegulationDispatchQueue> queues = RegulationDispatchQueue.getRegulationDispatchQueuesForUser(user);
	
	if (queues.isEmpty()) {
	    return forward(request, "/regulationDispatch/permissionDenied.jsp");
	}

	request.setAttribute("queues", queues);
	
	return forward(request, "/regulationDispatch/chooseQueue.jsp");
    }
    
    public ActionForward viewQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return null;
    }

    public ActionForward prepareInsertDispatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return null;
    }

    public ActionForward insertDispatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return null;
    }

    private RegulationDispatchQueue readQueue(HttpServletRequest request) {
	return getDomainObject(request, "queueId");
    }

}
