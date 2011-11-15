package module.regulation.dispatch.presentationTier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.regulation.dispatch.domain.activities.AbstractWorkflowActivity;
import module.regulation.dispatch.domain.activities.CreateRegulationDispatchBean;
import module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import module.workflow.activities.WorkflowActivity;
import module.workflow.presentationTier.WorkflowLayoutContext;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.presentationTier.Context;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/createRegulationDispatch")
public class CreateRegulationDispatchAction extends ContextBaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	RegulationDispatchQueue queue = readQueue(request);
	request.setAttribute("queue", queue);

	return super.execute(mapping, form, request, response);
    }

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	RegulationDispatchQueue queue = readQueue(request);
	CreateRegulationDispatchBean bean = new CreateRegulationDispatchBean(queue);

	request.setAttribute("bean", bean);

	return forward(request, "/module/regulation/dispatch/domain/RegulationDispatchWorkflowMetaProcess/create.jsp");
    }

    public ActionForward createInvalid(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	request.setAttribute("bean", getRenderedObject("bean"));

	return forward(request, "/module/regulation/dispatch/domain/RegulationDispatchWorkflowMetaProcess/create.jsp");
    }

    public ActionForward create(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	CreateRegulationDispatchBean bean = getRenderedObject("bean");
	RegulationDispatchQueue queue = readQueue(request);
	User user = UserView.getCurrentUser();

	try {
	    RegulationDispatchWorkflowMetaProcess.createNewProcess(bean, user);
	    return forwardToViewQueue(queue);
	} catch (final RegulationDispatchException e) {
	    addMessage(request, "error", e.getMessage(), e.getArgs());
	    return createInvalid(mapping, form, request, response);
	}
    }

    private ActionForward forwardToViewQueue(RegulationDispatchQueue queue) {
	return new ActionForward("/regulationDispatch.do?method=viewQueue&amp;queueId=" + queue.getExternalId(), true);
    }

    public ActionForward prepareEdit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchWorkflowMetaProcess process = readProcess(request);
	WorkflowActivity<RegulationDispatchWorkflowMetaProcess, RegulationDispatchActivityInformation> activity = process
		.getActivity("EditDispatch");
	RegulationDispatchActivityInformation bean = new RegulationDispatchActivityInformation(process,
		(AbstractWorkflowActivity) activity);
	request.setAttribute("bean", bean);

	return forward(request, "/module/regulation/dispatch/domain/RegulationDispatchWorkflowMetaProcess/edit.jsp");
    }
    
    public ActionForward editInvalid(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchActivityInformation bean = getRenderedObject("bean");
	request.setAttribute("bean", bean);

	return forward(request, "/module/regulation/dispatch/domain/RegulationDispatchWorkflowMetaProcess/edit.jsp");
    }

    public ActionForward edit(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	try {
	    RegulationDispatchActivityInformation bean = getRenderedObject("bean");
	    RegulationDispatchWorkflowMetaProcess process = bean.getProcess();
	    WorkflowActivity<RegulationDispatchWorkflowMetaProcess, RegulationDispatchActivityInformation> activity = process
		    .getActivity("EditDispatch");

	    activity.execute(bean);

	    RegulationDispatchQueue queue = readQueue(request);
	    return forwardToViewQueue(queue);
	} catch(RegulationDispatchException e) {
	    addMessage(request, "error", e.getKey(), e.getArgs());
	    return editInvalid(mapping, form, request, response);
	}
    }

    private RegulationDispatchWorkflowMetaProcess readProcess(final HttpServletRequest request) {
	return getDomainObject(request, "dispatchId");
    }

    private RegulationDispatchQueue readQueue(final HttpServletRequest request) {
	return getDomainObject(request, "queueId");
    }

    @Override
    public Context createContext(String contextPathString, HttpServletRequest request) {
	WorkflowLayoutContext context = WorkflowLayoutContext
		.getDefaultWorkflowLayoutContext(RegulationDispatchWorkflowMetaProcess.class);
	context.setElements(contextPathString);

	return context;
    }

}
