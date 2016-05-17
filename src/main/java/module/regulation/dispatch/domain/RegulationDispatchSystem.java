/*
 * @(#)RegulationDispatchSystem.java
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

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchSystem extends RegulationDispatchSystem_Base {

    private RegulationDispatchSystem() {
        super();
        setBennu(Bennu.getInstance());
    }

    public static RegulationDispatchSystem getInstance() {
        final RegulationDispatchSystem system = Bennu.getInstance().getRegulationDispatchSystem();
        return system == null ? createRegulationDispatchSystem() : system;
    }

    private static RegulationDispatchSystem createRegulationDispatchSystem() {
        return new RegulationDispatchSystem();
    }

    public static boolean isRegulationDispatchManager(final User user) {
        return Group.dynamic("RegulationDispatchManagers").isMember(user);
    }

}
