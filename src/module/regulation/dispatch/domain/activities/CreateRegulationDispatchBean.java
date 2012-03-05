/*
 * @(#)CreateRegulationDispatchBean.java
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
package module.regulation.dispatch.domain.activities;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.RegulationDispatchQueue;

import org.joda.time.LocalDate;


/**
 * 
 * @author Anil Kassamali
 * 
 */
public class CreateRegulationDispatchBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String reference;
    private LocalDate emissionDate;
    private String dispatchDescription;
    private Person emissor;
    private String regulationReference;
    private RegulationDispatchQueue queue;

    public CreateRegulationDispatchBean(final RegulationDispatchQueue queue) {
	setQueue(queue);
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public LocalDate getEmissionDate() {
	return emissionDate;
    }

    public void setEmissionDate(LocalDate emissionDate) {
	this.emissionDate = emissionDate;
    }

    public String getDispatchDescription() {
	return dispatchDescription;
    }

    public void setDispatchDescription(String description) {
	this.dispatchDescription = description;
    }

    public Person getEmissor() {
	return emissor;
    }

    public void setEmissor(Person emissor) {
	this.emissor = emissor;
    }

    public String getRegulationReference() {
	return regulationReference;
    }

    public void setRegulationReference(String regulationReference) {
	this.regulationReference = regulationReference;
    }

    public RegulationDispatchQueue getQueue() {
	return queue;
    }

    public void setQueue(RegulationDispatchQueue queue) {
	this.queue = queue;
    }

}
