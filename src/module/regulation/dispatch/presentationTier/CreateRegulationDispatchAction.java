package module.regulation.dispatch.presentationTier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.regulation.dispatch.domain.activities.CreateRegulationDispatchActivityInformation;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/createRegulationDispatch")
public class CreateRegulationDispatchAction extends RegulationDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {

	RegulationDispatchQueue queue = readQueue(request);
	CreateRegulationDispatchActivityInformation activityBean = new CreateRegulationDispatchActivityInformation(queue);

	request.setAttribute("activityBean", activityBean);

	return forward(request, "/module/regulation/dispatch/domain/create.jsp");
    }
    
    public ActionForward createInvalid(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	request.setAttribute("activityBean", getRenderedObject("activityBean"));
	
	return forward(request, "/module/regulation/dispatch/domain/create.jsp");
    }

    public ActionForward create(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	CreateRegulationDispatchActivityInformation activityBean = getRenderedObject("activityBean");
	RegulationDispatchQueue queue = readQueue(request);
	User user = UserView.getCurrentUser();
	
	try {

	    RegulationDispatchWorkflowMetaProcess.createNewProcess(activityBean, user);
	    return new ActionForward("/regulationDispatch.do?method=viewQueue&amp;queueId=" + queue.getExternalId(), true);

	} catch (final RegulationDispatchException e) {
	    addMessage(request, "error", e.getMessage(), e.getArgs());
	    return createInvalid(mapping, form, request, response);
	}
    }

}
