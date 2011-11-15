package module.regulation.dispatch.domain.activities;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.RegulationDispatchQueue;

import org.joda.time.LocalDate;


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
