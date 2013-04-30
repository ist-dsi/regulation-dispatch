/*
 * @(#)RegulationDispatchQueueBean.java
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

import java.io.Serializable;

import module.workflow.domain.WorkflowUserGroupQueueBean;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchQueueBean extends WorkflowUserGroupQueueBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public RegulationDispatchQueueBean() {
        super();
    }

    public RegulationDispatchQueueBean(final RegulationDispatchQueue queue) {
        super();
        setQueue(queue);
    }

    @Override
    @Atomic
    public RegulationDispatchQueue createWorkflowQueue() {
        return new RegulationDispatchQueue(this);
    }

}
