package module.regulation.dispatch.presentationTier;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.IRegulationDispatchEntry;
import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.regulation.dispatch.utils.NaturalOrderComparator;
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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.servlets.functionalities.CreateNodeAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/regulationDispatch")
public class RegulationDispatchAction extends ContextBaseAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	WorkflowUserGroupQueue regulationProcessesQueue = readQueue(request);
	request.setAttribute("queue", regulationProcessesQueue);

	return super.execute(mapping, form, request, response);
    }

    @CreateNodeAction(bundle = "REGULATION_DISPATCH_RESOURCES", key = "link.node.configuration.regulation.dispatch.interface", groupKey = "title.node.configuration.module.regulation.dispatch")
    public ActionForward prepareCreateNewPage(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final VirtualHost virtualHost = getDomainObject(request, "virtualHostToManageId");
	final Node node = getDomainObject(request, "parentOfNodesToManageId");

	ActionNode.createActionNode(virtualHost, node, "/regulationDispatch", "prepare", "resources.RegulationDispatchResources",
		"label.sideBar.regulation.dispatch.manage", UserGroup.getInstance());

	return forwardToMuneConfiguration(request, virtualHost, node);
    }

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	User user = UserView.getCurrentUser();
	List<RegulationDispatchQueue> queues = RegulationDispatchQueue.getRegulationDispatchQueuesForUser(user);

	request.setAttribute("queues", queues);

	return forward(request, "/regulationDispatch/chooseQueue.jsp");
    }

    public ActionForward viewQueue(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	if (readQueue(request) == null) {
	    return prepare(mapping, form, request, response);
	}

	return forward(request, "/regulationDispatch/viewQueue.jsp");
    }

    public static final NaturalOrderComparator NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator();

    public static final Comparator<IRegulationDispatchEntry> SORT_BY_REFERENCE_COMPARATOR = new Comparator<IRegulationDispatchEntry>() {

	@Override
	public int compare(IRegulationDispatchEntry left, IRegulationDispatchEntry right) {
	    return NATURAL_ORDER_COMPARATOR.compare(left.getReference(), right.getReference());
	}
    };

    public static class RegulationDispatchEntryFieldComparator implements Comparator<IRegulationDispatchEntry> {
	private Comparator beanComparator;

	public RegulationDispatchEntryFieldComparator(final String property) {
	    beanComparator = new BeanComparator(property);
	}

	@Override
	public int compare(IRegulationDispatchEntry left, IRegulationDispatchEntry right) {
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

    private java.util.List<IRegulationDispatchEntry> limitAndOrderSearchedEntries(java.util.List searchedEntries,
	    final Comparator[] propertiesToCompare, final Integer[] orderToUse, Integer iDisplayStart, Integer iDisplayLength) {

	Collections.sort(searchedEntries, new Comparator<IRegulationDispatchEntry>() {

	    @Override
	    public int compare(IRegulationDispatchEntry oLeft, IRegulationDispatchEntry oRight) {
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

	return searchedEntries.subList(iDisplayStart, Math.min(iDisplayStart + iDisplayLength, searchedEntries.size()));
    }

    private String generateLinkForView(HttpServletRequest request, IRegulationDispatchEntry entry) {
	RegulationDispatchQueue queue = readQueue(request);
	String contextPath = request.getContextPath();
	String realLink = contextPath
		+ String.format("/regulationDispatch.do?dispatchId=%s&amp;method=viewDispatch&amp;queueId=%s",
			entry.getExternalId(), queue.getExternalId());
	realLink += String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
		GenericChecksumRewriter.calculateChecksum(realLink));

	return realLink;
    }

    private String generateLinkForEdition(HttpServletRequest request, IRegulationDispatchEntry entry) {
	RegulationDispatchQueue queue = readQueue(request);
	String contextPath = request.getContextPath();
	String realLink = contextPath
		+ String.format("/createRegulationDispatch.do?dispatchId=%s&amp;method=prepareEdit&amp;queueId=%s",
			entry.getExternalId(), queue.getExternalId());
	realLink += String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
		GenericChecksumRewriter.calculateChecksum(realLink));

	return realLink;
    }

    private String generateLinkForRemoval(HttpServletRequest request, IRegulationDispatchEntry entry) {
	RegulationDispatchQueue queue = readQueue(request);
	String contextPath = request.getContextPath();
	String realLink = contextPath
		+ String.format("/createRegulationDispatch.do?dispatchId=%s&amp;method=prepareRemoveDispatch&amp;queueId=%s",
			entry.getExternalId(), queue.getExternalId());
	realLink += String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
		GenericChecksumRewriter.calculateChecksum(realLink));

	return realLink;
    }

    private String generateLinkForMainDocument(HttpServletRequest request, IRegulationDispatchEntry entry) {
	RegulationDispatchQueue queue = readQueue(request);
	String contextPath = request.getContextPath();
	String realLink = contextPath
		+ String.format("/regulationDispatch.do?dispatchId=%s&amp;method=downloadMainDocument&amp;queueId=%s",
			entry.getExternalId(), queue.getExternalId());
	realLink += String.format("&%s=%s", GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME,
		GenericChecksumRewriter.calculateChecksum(realLink));

	return realLink;
    }

    private String serializeAjaxFilterResponse(String sEcho, Integer iTotalRecords, Integer iTotalDisplayRecords,
	    java.util.List<IRegulationDispatchEntry> limitedEntries, HttpServletRequest request) {

	StringBuilder stringBuilder = new StringBuilder("{");
	stringBuilder.append("\"sEcho\": ").append(sEcho).append(", \n");
	stringBuilder.append("\"iTotalRecords\": ").append(iTotalRecords).append(", \n");
	stringBuilder.append("\"iTotalDisplayRecords\": ").append(iTotalDisplayRecords).append(", \n");
	stringBuilder.append("\"aaData\": ").append("[ \n");

	for (IRegulationDispatchEntry entry : limitedEntries) {
	    RegulationDispatchWorkflowMetaProcess meta = ((RegulationDispatchWorkflowMetaProcess) entry);
	    boolean ableToAccessQueue = meta.isCurrentUserAbleToAccessAnyQueues();

	    String reference = entry.getReference();
	    LocalDate emissionDate = entry.getEmissionDate();
	    String dispatchDescription = entry.getDispatchDescription();
	    Person emissor = entry.getEmissor();
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
		    .append(ableToAccessQueue && hasMainDocument ? generateLinkForMainDocument(request, entry)
			    : "permission_not_granted").append("\",");

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

	List<IRegulationDispatchEntry> entries = null;

	RegulationDispatchQueue queue = readQueue(request);
	if (StringUtils.isEmpty(sSearch)) {
	    entries = queue.getActiveEntries();
	} else {
	    entries = queue.findEntriesBy(sSearch);
	}

	Integer numberOfRecordsMatched = entries.size();
	java.util.List<IRegulationDispatchEntry> limitedEntries = limitAndOrderSearchedEntries(entries, propertiesToCompare,
		orderToUse, iDisplayStart, iDisplayLength);

	String jsonResponseString = null;
	jsonResponseString = serializeAjaxFilterResponse(sEcho, queue.getActiveEntries().size(), numberOfRecordsMatched,
		limitedEntries, request);

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
	IRegulationDispatchEntry dispatch = readDispatchEntry(request);

	request.setAttribute("dispatch", dispatch);

	return forward(request, "/regulationDispatch/viewDispatch.jsp");
    }

    public ActionForward downloadMainDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	IRegulationDispatchEntry dispatch = readDispatchEntry(request);

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

    private IRegulationDispatchEntry readDispatchEntry(final HttpServletRequest request) {
	return (IRegulationDispatchEntry) getDomainObject(request, "dispatchId");
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

    @Override
    public Context createContext(String contextPathString, HttpServletRequest request) {
	LayoutContext context = (LayoutContext) super.createContext(contextPathString, request);
	context.addHead("/regulationDispatch/layoutHead.jsp");
	return context;
    }

}
