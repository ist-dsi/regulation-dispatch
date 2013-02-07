/*
 * @(#)RegulationDispatchQueue.java
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
import java.util.List;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.exceptions.RegulationDispatchException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowQueueBean;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.User;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchQueue extends RegulationDispatchQueue_Base {

    RegulationDispatchQueue(String name, List<User> baseUsers) {
        super();
        init(name, baseUsers);
        setRegulationDispatchSystem(RegulationDispatchSystem.getInstance());
        setMetaType(RegulationDispatchSystem.getInstance().getMetaType());
    }

    RegulationDispatchQueue(final RegulationDispatchQueueBean bean) {
        this(bean.getName(), new ArrayList<User>(bean.getUsers()));
    }

    public static List<RegulationDispatchQueue> getRegulationDispatchQueuesForUser(final User user) {
        List<RegulationDispatchQueue> result = new ArrayList<RegulationDispatchQueue>();
        List<RegulationDispatchQueue> queues = RegulationDispatchSystem.getInstance().getQueues();

        for (RegulationDispatchQueue regulationDispatchQueue : queues) {
            if (regulationDispatchQueue.isUserAbleToAccessQueue(user)) {
                result.add(regulationDispatchQueue);
            }
        }

        return result;
    }

    @Override
    @Service
    public void addUsers(User user) {
        if (isUserAbleToAccessQueue(user)) {
            throw new RegulationDispatchException("error.regulation.dispatch.queue.user.already.in.queue");
        }

        super.addUsers(user);
    }

    public List<IRegulationDispatchEntry> getActiveEntries() {
        List<IRegulationDispatchEntry> result = new ArrayList<IRegulationDispatchEntry>();
        List<WorkflowProcess> activeProcesses = super.getActiveProcesses();

        for (WorkflowProcess workflowProcess : activeProcesses) {
            result.add((IRegulationDispatchEntry) workflowProcess);
        }

        return result;
    }

    public List<IRegulationDispatchEntry> getNotActiveEntries() {
        List<IRegulationDispatchEntry> result = new ArrayList<IRegulationDispatchEntry>();
        List<WorkflowProcess> notActiveProcesses = super.getNotActiveProcesses();

        for (WorkflowProcess workflowProcess : notActiveProcesses) {
            result.add((IRegulationDispatchEntry) workflowProcess);
        }

        return result;
    }

    public List<IRegulationDispatchEntry> getAllEntries() {
        List<IRegulationDispatchEntry> result = getActiveEntries();
        result.addAll(getNotActiveEntries());

        return result;
    }

    public List<IRegulationDispatchEntry> findEntriesBy(String sSearch) {
        List<IRegulationDispatchEntry> result = new ArrayList<IRegulationDispatchEntry>();
        List<IRegulationDispatchEntry> activeEntries = getActiveEntries();

        for (IRegulationDispatchEntry dispatch : activeEntries) {
            String reference = dispatch.getReference();
            LocalDate emissionDate = dispatch.getEmissionDate();
            String dispatchDescription = dispatch.getDispatchDescription();
            Person emissor = dispatch.getEmissor();
            String regulationReference = dispatch.getRegulationReference() != null ? dispatch.getRegulationReference() : "";

            if (reference.toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
                result.add(dispatch);
            } else if (emissionDate.toString("dd/MM/yyyy").indexOf(sSearch.toLowerCase()) > -1) {
                result.add(dispatch);
            } else if (dispatchDescription.toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
                result.add(dispatch);
            } else if (emissor.getName().toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
                result.add(dispatch);
            } else if (regulationReference.toLowerCase().indexOf(sSearch.toLowerCase()) > -1) {
                result.add(dispatch);
            }
        }

        return result;
    }

    @Override
    public void edit(WorkflowQueueBean bean) {
        setName(bean.getName());
    }

}
