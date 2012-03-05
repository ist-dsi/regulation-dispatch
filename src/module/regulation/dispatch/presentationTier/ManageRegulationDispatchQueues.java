/*
 * @(#)ManageRegulationDispatchQueues.java
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
package module.regulation.dispatch.presentationTier;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchQueueBean;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import module.regulation.dispatch.utils.RegulationDispatchUtils;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.presentationTier.actions.ContextBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/manageRegulationDispatchQueues")
/**
 * 
 * @author Anil Kassamali
 * 
 */
public class ManageRegulationDispatchQueues extends ContextBaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	User currentUser = UserView.getCurrentUser();

	if(!RegulationDispatchUtils.isMyOrgManager(currentUser)) {
	    return new ActionForward("/regulationDispatch.do?method=prepare", true);
	}

	return super.execute(mapping, form, request, response);
    }

    public ActionForward manageQueues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
	List<RegulationDispatchQueue> queues = system.getQueues();

	request.setAttribute("queues", queues);
	return forward(request, "/regulationDispatch/queueManagement/manage.jsp");
    }
    
    public ActionForward prepareCreateQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchQueueBean bean = new RegulationDispatchQueueBean();
	request.setAttribute("bean", bean);

	return forward(request, "/regulationDispatch/queueManagement/create.jsp");
    }
    
    public ActionForward createQueueInvalid(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchQueueBean bean = getRenderedObject("bean");
	request.setAttribute("bean", bean);

	return forward(request, "/regulationDispatch/queueManagement/create.jsp");
    }

    public ActionForward createQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
	RegulationDispatchQueueBean bean = getRenderedObject("bean");
	
	try {
	    bean.createWorkflowQueue();
	} catch(RegulationDispatchException e) {
	    addMessage(request, "error", e.getKey(), e.getArgs());
	    return createQueueInvalid(mapping, form, request, response);
	}

	RenderUtils.invalidateViewState();
	return manageQueues(mapping, form, request, response);
    }

    public ActionForward viewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchQueue queue = readQueue(request);
	request.setAttribute("queue", queue);
	request.setAttribute("bean", new RegulationDispatchQueueBean(queue));

	return forward(request, "/regulationDispatch/queueManagement/view.jsp");
    }

    public ActionForward editQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchQueue queue = readQueue(request);
	RegulationDispatchQueueBean bean = getRenderedObject("bean");
	queue.edit(bean);

	RenderUtils.invalidateViewState();

	return viewQueue(mapping, form, request, response);
    }

    public ActionForward addUserToQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchQueue queue = readQueue(request);
	RegulationDispatchQueueBean bean = getRenderedObject("bean");

	try {
	    queue.addUsers(bean.getUserToAdd());
	    RenderUtils.invalidateViewState();

	    return viewQueue(mapping, form, request, response);
	} catch (RegulationDispatchException e) {
	    addMessage(request, "error", e.getKey(), e.getArgs());
	    request.setAttribute("queue", queue);
	    request.setAttribute("bean", new RegulationDispatchQueueBean());

	    return forward(request, "/regulationDispatch/queueManagement/view.jsp");
	}
    }

    public ActionForward addUserToQueueInvalid(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	request.setAttribute("queue", readQueue(request));
	request.setAttribute("bean", getRenderedObject("bean"));

	return forward(request, "/regulationDispatch/queueManagement/view.jsp");
    }

    public ActionForward removeUserFromQueue(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request,
	    final HttpServletResponse response) {
	RegulationDispatchQueue queue = readQueue(request);
	User user = readUser(request);

	try {
	    queue.removeUsers(user);
	} catch (RegulationDispatchException e) {
	    addMessage(request, "error", e.getKey(), e.getArgs());
	}

	return viewQueue(mapping, form, request, response);
    }

    private User readUser(HttpServletRequest request) {
	return getDomainObject(request, "userId");
    }

    private RegulationDispatchQueue readQueue(final HttpServletRequest request) {
	return getDomainObject(request, "queueId");
    }
}
