/*
 * @(#)IRegulationDispatchEntry.java
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

import module.organization.domain.Person;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.Strings;

/**
 * 
 * @author Anil Kassamali
 * 
 */
public interface IRegulationDispatchEntry {
    public String getReference();

    public void setReference(final String subject);

    public LocalDate getEmissionDate();

    public void setEmissionDate(final LocalDate emissionDate);

    public String getDispatchDescription();

    public void setDispatchDescription(final String description);

    public Person getEmissor();

    public void setEmissor(final Person emissor);

    public String getRegulationReference();

    public void setRegulationReference(final String regulationReference);

    public void activate();

    public void deactivate();

    public boolean isActive();

    public DateTime getCreationDate();

    public DateTime getUpdateDate();

    public String getExternalId();

    public RegulationDispatchProcessFile getMainDocument();

    public Strings getArticles();

    public void setArticles(Strings value);
}
