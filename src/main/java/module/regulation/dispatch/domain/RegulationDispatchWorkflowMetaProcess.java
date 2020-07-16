/*
 * @(#)RegulationDispatchWorkflowMetaProcess.java
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
package module.regulation.dispatch.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.activities.AbstractWorkflowActivity;
import module.regulation.dispatch.domain.activities.CreateRegulationDispatchBean;
import module.regulation.dispatch.domain.activities.EditDispatch;
import module.regulation.dispatch.domain.activities.RegulationDispatchActivityInformation;
import module.regulation.dispatch.domain.activities.RemoveDispatch;
import module.regulation.dispatch.domain.activities.RemoveFile;
import module.regulation.dispatch.domain.activities.SetFileAsMainDocument;
import module.regulation.dispatch.domain.activities.UploadFile;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchWorkflowMetaProcess extends RegulationDispatchWorkflowMetaProcess_Base {

    protected static Map<String, AbstractWorkflowActivity> activityMap = new HashMap<String, AbstractWorkflowActivity>();

    static {
        activityMap.put("EditDispatch", new EditDispatch());
        activityMap.put("UploadFile", new UploadFile());
        activityMap.put("SetFileAsMainDocument", new SetFileAsMainDocument());
        activityMap.put("RemoveFile", new RemoveFile());
        activityMap.put("RemoveDispatch", new RemoveDispatch());
    }

    protected RegulationDispatchWorkflowMetaProcess() {
        super();
        final RegulationDispatchSystem system = RegulationDispatchSystem.getInstance();
        setRegulationDispatchSystem(system);
        setRegulationDispatchSystemFromActive(system);
        setWorkflowSystem(WorkflowSystem.getInstance());
    }

    protected RegulationDispatchWorkflowMetaProcess(String reference, LocalDate emissionDate, Person emissor, String description,
            String regulationReference) {
        this();
        init(reference, emissionDate, emissor, description, regulationReference);
    }

    @Atomic
    public static RegulationDispatchWorkflowMetaProcess createNewProcess(final CreateRegulationDispatchBean bean,
            final User user) {
        String reference = bean.getReference();
        LocalDate emissionDate = bean.getEmissionDate();
        Person emissor = bean.getEmissor();
        String regulationReference = bean.getRegulationReference();
        String description = bean.getDispatchDescription();

        return new RegulationDispatchWorkflowMetaProcess(reference, emissionDate, emissor, description, regulationReference);
    }

    @Atomic
    public static WorkflowProcess createNewProcess(String subject, String instanceDescription, WorkflowQueue queue, User user) {
        throw new RuntimeException("invalid use");
    }

    protected void init(String reference, LocalDate emissionDate, Person emissor, String description,
            String regulationReference) {
        setRequestorUser(emissor.getUser());
        setReference(reference);
        setInstanceDescription(description);
        setRegulationReference(regulationReference);
        setEmissionDate(emissionDate);
    }

    public DateTime getUpdateDate() {
        if (getDateFromLastActivity() != null) {
            return getDateFromLastActivity();
        }

        return getCreationDate();
    }

    @Override
    public List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> getActivities() {
        List<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> list =
                new ArrayList<WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>>();
        list.addAll(activityMap.values());
        return list;
    }

    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> Stream<T> getActivityStream() {
        final Collection activities = activityMap.values();
        return activities.stream();
    }

    public void edit(final RegulationDispatchActivityInformation activityInformation) {
        setReference(activityInformation.getReference());
        setEmissionDate(activityInformation.getEmissionDate());
        setInstanceDescription(activityInformation.getDispatchDescription());
        setRequestorUser(activityInformation.getEmissor().getUser());
        setRegulationReference(activityInformation.getRegulationReference());
        setArticles(activityInformation.getArticles());
    }

    public RegulationDispatchProcessFile getMainDocument() {
        return getFilesSet().stream().map(f -> (RegulationDispatchProcessFile) f).filter(f -> f.getMainDocument()).findAny()
                .orElse(null);
    }

    public List<RegulationDispatchProcessFile> getActiveFiles() {
        return getFilesSet().stream().map(f -> (RegulationDispatchProcessFile) f).filter(f -> f.getActive())
                .collect(Collectors.toList());
    }

    @Override
    public User getProcessCreator() {
        return getRequestorUser();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void notifyUserDueToComment(User arg0, String arg1) {
    }

    public void deactivate() {
        setRegulationDispatchSystemFromActive(null);
        setRegulationDispatchSystemFromNotActive(RegulationDispatchSystem.getInstance());
    }

}
