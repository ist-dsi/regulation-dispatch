/*
 * @(#)RegulationDispatchAction.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchSystem;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.regulation.dispatch.utils.NaturalOrderComparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.presentationTier.actions.BaseAction;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "RegulationDispatchResources", path = "regulationDispatch",
        titleKey = "title.node.configuration.module.regulation.dispatch", accessGroup = "#RegulationDispatchManagers",
        hint = "Regulation Dispatch")
@StrutsFunctionality(app = RegulationDispatchAction.class, path = "regulationDispatch",
        titleKey = "title.node.configuration.module.regulation.dispatch")
@Mapping(path = "/regulationDispatch")
/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchAction extends BaseAction {

    @EntryPoint
    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        User user = Authenticate.getUser();
        if (RegulationDispatchSystem.isRegulationDispatchManager(user)) {
            return viewQueue(mapping, form, request, response);
        }
        return forward("/regulationDispatch/chooseQueue.jsp");
    }

    public ActionForward viewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        request.setAttribute("regulationDispatchSystem", RegulationDispatchSystem.getInstance());
        return forward("/regulationDispatch/viewQueue.jsp");
    }

    public static final NaturalOrderComparator NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator();

    public static final Comparator<RegulationDispatchWorkflowMetaProcess> SORT_BY_REFERENCE_COMPARATOR =
            new Comparator<RegulationDispatchWorkflowMetaProcess>() {

                @Override
                public int compare(RegulationDispatchWorkflowMetaProcess left, RegulationDispatchWorkflowMetaProcess right) {
                    return NATURAL_ORDER_COMPARATOR.compare(left.getReference(), right.getReference());
                }
            };

    public static class RegulationDispatchEntryFieldComparator implements Comparator<RegulationDispatchWorkflowMetaProcess> {
        private Comparator beanComparator;

        public RegulationDispatchEntryFieldComparator(final String property) {
            beanComparator = new BeanComparator(property);
        }

        @Override
        public int compare(RegulationDispatchWorkflowMetaProcess left, RegulationDispatchWorkflowMetaProcess right) {
            int value = beanComparator.compare(left, right);

            if (value == 0) {
                return SORT_BY_REFERENCE_COMPARATOR.compare(left, right);
            }

            return value;
        }

    }

    private static final java.util.Map<String, Object> DISPATCH_TABLE_COLUMNS_MAP = new java.util.HashMap<String, Object>();
    static {
        DISPATCH_TABLE_COLUMNS_MAP.put("0", SORT_BY_REFERENCE_COMPARATOR);
        DISPATCH_TABLE_COLUMNS_MAP.put("1", new RegulationDispatchEntryFieldComparator("emissionDate"));
        DISPATCH_TABLE_COLUMNS_MAP.put("2", new RegulationDispatchEntryFieldComparator("dispatchDescription"));
        DISPATCH_TABLE_COLUMNS_MAP.put("3", new RegulationDispatchEntryFieldComparator("emissor.name"));
        DISPATCH_TABLE_COLUMNS_MAP.put("4", new RegulationDispatchEntryFieldComparator("regulationReference"));
        DISPATCH_TABLE_COLUMNS_MAP.put("asc", 1);
        DISPATCH_TABLE_COLUMNS_MAP.put("desc", -1);
    }

    private Comparator[] getPropertiesToCompare(HttpServletRequest request, Integer iSortingCols) {
        java.util.List<Comparator> properties = new java.util.ArrayList<Comparator>();

        java.util.Map<String, Object> mapToUse = DISPATCH_TABLE_COLUMNS_MAP;

        for (int i = 0; i < iSortingCols; i++) {
            String iSortingColIdx = request.getParameter("iSortCol_" + i);
            properties.add((Comparator) mapToUse.get(iSortingColIdx));
        }

        return properties.toArray(new Comparator[] {});
    }

    private Integer[] getOrdering(HttpServletRequest request, Integer iSortingCols) {
        java.util.List<Integer> order = new java.util.ArrayList<Integer>();

        java.util.Map<String, Object> mapToUse = DISPATCH_TABLE_COLUMNS_MAP;

        for (int i = 0; i < iSortingCols; i++) {
            String iSortingColDir = request.getParameter("iSortDir_" + i);
            order.add((Integer) mapToUse.get(iSortingColDir));
        }

        return order.toArray(new Integer[] {});
    }

    private java.util.List<RegulationDispatchWorkflowMetaProcess> limitAndOrderSearchedEntries(java.util.Set searchedEntries,
            final Comparator[] propertiesToCompare, final Integer[] orderToUse, Integer iDisplayStart, Integer iDisplayLength) {
        final List<RegulationDispatchWorkflowMetaProcess> result = new ArrayList<>();
        result.addAll(searchedEntries);

        Collections.sort(result, new Comparator<RegulationDispatchWorkflowMetaProcess>() {

            @Override
            public int compare(RegulationDispatchWorkflowMetaProcess oLeft, RegulationDispatchWorkflowMetaProcess oRight) {
                for (int i = 0; i < propertiesToCompare.length; i++) {
                    try {
                        Comparator comparator = propertiesToCompare[i];

                        if (comparator.compare(oLeft, oRight) != 0) {
                            return orderToUse[i] * comparator.compare(oLeft, oRight);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                return 0;
            }
        });

        return result.subList(iDisplayStart, Math.min(iDisplayStart + iDisplayLength, searchedEntries.size()));
    }

    private String generateLinkForView(HttpServletRequest request, RegulationDispatchWorkflowMetaProcess entry) {
        String contextPath = request.getContextPath();
        String realLink =
                contextPath
                        + String.format("/regulationDispatch.do?dispatchId=%s&amp;method=viewDispatch", entry.getExternalId());
        realLink +=
                String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
                        GenericChecksumRewriter.calculateChecksum(realLink, request.getSession()));

        return realLink;
    }

    private String generateLinkForEdition(HttpServletRequest request, RegulationDispatchWorkflowMetaProcess entry) {
        String contextPath = request.getContextPath();
        String realLink =
                contextPath
                        + String.format("/createRegulationDispatch.do?dispatchId=%s&amp;method=prepareEdit",
                                entry.getExternalId());
        realLink +=
                String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
                        GenericChecksumRewriter.calculateChecksum(realLink, request.getSession()));

        return realLink;
    }

    private String generateLinkForRemoval(HttpServletRequest request, RegulationDispatchWorkflowMetaProcess entry) {
        String contextPath = request.getContextPath();
        String realLink =
                contextPath
                        + String.format("/createRegulationDispatch.do?dispatchId=%s&amp;method=prepareRemoveDispatch",
                                entry.getExternalId());
        realLink +=
                String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
                        GenericChecksumRewriter.calculateChecksum(realLink, request.getSession()));

        return realLink;
    }

    private String generateLinkForMainDocument(HttpServletRequest request, RegulationDispatchWorkflowMetaProcess entry) {
        String contextPath = request.getContextPath();
        String realLink =
                contextPath
                        + String.format("/regulationDispatch.do?dispatchId=%s&amp;method=downloadMainDocument&amp;queueId=%s",
                                entry.getExternalId());
        realLink +=
                String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
                        GenericChecksumRewriter.calculateChecksum(realLink, request.getSession()));

        return realLink;
    }

    private String serializeAjaxFilterResponse(String sEcho, Integer iTotalRecords, Integer iTotalDisplayRecords,
            java.util.List<RegulationDispatchWorkflowMetaProcess> limitedEntries, HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder("{");
        stringBuilder.append("\"sEcho\": ").append(sEcho).append(", \n");
        stringBuilder.append("\"iTotalRecords\": ").append(iTotalRecords).append(", \n");
        stringBuilder.append("\"iTotalDisplayRecords\": ").append(iTotalDisplayRecords).append(", \n");
        stringBuilder.append("\"aaData\": ").append("[ \n");

        for (RegulationDispatchWorkflowMetaProcess entry : limitedEntries) {
            RegulationDispatchWorkflowMetaProcess meta = ((RegulationDispatchWorkflowMetaProcess) entry);
            boolean ableToAccessQueue = meta.isCurrentUserAbleToAccessAnyQueues();

            String reference = entry.getReference();
            LocalDate emissionDate = entry.getEmissionDate();
            String dispatchDescription = entry.getInstanceDescription();
            Person emissor = entry.getRequestorUser().getPerson();
            String regulationReference = entry.getRegulationReference() != null ? entry.getRegulationReference() : "";
            Boolean hasMainDocument = entry.getMainDocument() != null;

            stringBuilder.append("[ \"").append(reference).append("\", ");
            stringBuilder.append("\"").append(escapeQuotes(emissionDate.toString("dd/MM/yyyy"))).append("\", ");
            stringBuilder.append("\"").append(escapeQuotes(dispatchDescription)).append("\", ");
            stringBuilder.append("\"").append(escapeQuotes(emissor.getName())).append("\", ");
            stringBuilder.append("\"").append(escapeQuotes(regulationReference)).append("\", ");

            stringBuilder.append("\"").append(ableToAccessQueue ? generateLinkForView(request, entry) : "permission_not_granted")
                    .append(",");

            stringBuilder.append(ableToAccessQueue ? generateLinkForEdition(request, entry) : "permission_not_granted").append(
                    ",");

            stringBuilder.append(
                    ableToAccessQueue && entry.isActive() ? generateLinkForRemoval(request, entry) : "permission_not_granted")
                    .append(",");

            stringBuilder
                    .append(ableToAccessQueue && hasMainDocument ? generateLinkForMainDocument(request, entry) : "permission_not_granted")
                    .append("\",");

            stringBuilder.append("\"").append(entry.isActive()).append("\" ], ");

        }

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        stringBuilder.append(" ]\n }");

        return stringBuilder.toString();
    }

    private Object escapeQuotes(String value) {
        return value.replaceAll("\\\"", "\\\\\"");
    }

    public ActionForward processesForAjaxDataTable(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        String sEcho = request.getParameter("sEcho");
        Integer iSortingCols = Integer.valueOf(request.getParameter("iSortingCols"));
        String sSearch = request.getParameter("sSearch");
        Integer iDisplayStart = Integer.valueOf(request.getParameter("iDisplayStart"));
        Integer iDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));

        Comparator[] propertiesToCompare = getPropertiesToCompare(request, iSortingCols);
        Integer[] orderToUse = getOrdering(request, iSortingCols);

        if (propertiesToCompare.length == 0) {
            propertiesToCompare = new Comparator[] { new BeanComparator("reference") };
            orderToUse = new Integer[] { -1 };
        }

        Set<RegulationDispatchWorkflowMetaProcess> entries = null;

        if (StringUtils.isEmpty(sSearch)) {
            entries = RegulationDispatchSystem.getInstance().getActiveProcessesSet();
        } else {
            entries = RegulationDispatchQueue.findEntriesBy(sSearch);
        }

        Integer numberOfRecordsMatched = entries.size();
        java.util.List<RegulationDispatchWorkflowMetaProcess> limitedEntries =
                limitAndOrderSearchedEntries(entries, propertiesToCompare, orderToUse, iDisplayStart, iDisplayLength);

        String jsonResponseString = null;
        jsonResponseString =
                serializeAjaxFilterResponse(sEcho, RegulationDispatchSystem.getInstance().getActiveProcessesSet().size(),
                        numberOfRecordsMatched, limitedEntries, request);

        final byte[] jsonResponsePayload = jsonResponseString.getBytes("iso-8859-15");

        response.setContentType("application/json; charset=iso-8859-15");
        response.setContentLength(jsonResponsePayload.length);
        response.getOutputStream().write(jsonResponsePayload);
        response.getOutputStream().flush();
        response.getOutputStream().close();

        return null;
    }

    public ActionForward viewDispatch(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        RegulationDispatchWorkflowMetaProcess dispatch = readDispatchEntry(request);

        request.setAttribute("dispatch", dispatch);

        return forward("/regulationDispatch/viewDispatch.jsp");
    }

    public ActionForward downloadMainDocument(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        RegulationDispatchWorkflowMetaProcess dispatch = readDispatchEntry(request);

        RegulationDispatchProcessFile mainDocument = dispatch.getMainDocument();

        if (mainDocument == null) {
            throw new RuntimeException("this should not be here");
        }

        return download(response, mainDocument.getFilename(), mainDocument.getStream(), mainDocument.getContentType());
    }

    public ActionForward downloadFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        RegulationDispatchProcessFile file = readFile(request);

        if (file == null) {
            throw new RuntimeException("this should not be here");
        }

        return download(response, file.getFilename(), file.getStream(), file.getContentType());
    }

    private RegulationDispatchWorkflowMetaProcess readDispatchEntry(final HttpServletRequest request) {
        return (RegulationDispatchWorkflowMetaProcess) getDomainObject(request, "dispatchId");
    }

    private RegulationDispatchProcessFile readFile(final HttpServletRequest request) {
        return (RegulationDispatchProcessFile) getDomainObject(request, "fileId");
    }

    private RegulationDispatchQueue readQueue(final HttpServletRequest request) {
        return getDomainObject(request, "queueId");
    }

    protected RegulationDispatchWorkflowMetaProcess getProcess(final HttpServletRequest request) {
        return getDomainObject(request, "processId");
    }

}
