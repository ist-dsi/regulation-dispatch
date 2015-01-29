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

import java.util.HashSet;
import java.util.Set;

import module.organization.domain.Person;

import org.joda.time.LocalDate;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchQueue {

    public static Set<RegulationDispatchWorkflowMetaProcess> findEntriesBy(String sSearch) {
        Set<RegulationDispatchWorkflowMetaProcess> result = new HashSet<RegulationDispatchWorkflowMetaProcess>();
        Set<RegulationDispatchWorkflowMetaProcess> activeEntries = RegulationDispatchSystem.getInstance().getActiveProcessesSet();

        for (RegulationDispatchWorkflowMetaProcess dispatch : activeEntries) {
            String reference = dispatch.getReference();
            LocalDate emissionDate = dispatch.getEmissionDate();
            String dispatchDescription = dispatch.getInstanceDescription();
            Person emissor = dispatch.getRequestorUser().getPerson();
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

}
