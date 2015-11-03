/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of MGP Viewer.
 *
 * MGP Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MGP Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.regulation.dispatch.ui;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Strings;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.regulation.dispatch.view.SearchRegulationDispatchView;

@SpringApplication(group = "#RegulationDispatchManagers", path = "regulation-dispatch",
        title = "title.node.configuration.module.regulation.dispatch", hint = "regulation-dispatch")
@SpringFunctionality(app = RegulationDispatchController.class, title = "title.node.configuration.module.regulation.dispatch")
@RequestMapping("/regulation-dispatch")
public class RegulationDispatchController {

    @RequestMapping
    public String home(@RequestParam(required = false) String searchString, final Model model) {
        final String search = searchString == null ? Integer.toString(LocalDate.now().getYear()) : searchString;
        model.addAttribute("searchString", search);

        if (!Strings.isNullOrEmpty(search)) {
            model.addAttribute("searchResult", SearchRegulationDispatchView.search(search));
        }

        return "regulation-dispatch/list";
    }

    @RequestMapping(value = "/{dispatchId}/view", method = RequestMethod.GET)
    public String view(@PathVariable String dispatchId, final Model model) {
        return redirect(dispatchId, "regulationDispatch", "viewDispatch");
    }

    @RequestMapping(value = "/{dispatchId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String dispatchId, final Model model) {
        return redirect(dispatchId, "createRegulationDispatch", "prepareEdit");
    }

    @RequestMapping(value = "/{dispatchId}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable String dispatchId, final Model model) {
        return redirect(dispatchId, "createRegulationDispatch", "prepareRemoveDispatch");
    }

    @RequestMapping(value = "/{dispatchId}/document", method = RequestMethod.GET)
    public String document(@PathVariable String dispatchId, final Model model) {
        return redirect(dispatchId, "regulationDispatch", "downloadMainDocument");
    }

    private String redirect(final String id, final String action, final String method) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String contextPath = request.getContextPath();
        final String path = "/" + action + ".do?method=" + method + "&dispatchId=" + id;
        final String safePath = path + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                + GenericChecksumRewriter.calculateChecksum(contextPath + path, request.getSession());
        return "redirect:" + safePath;
    }

}
