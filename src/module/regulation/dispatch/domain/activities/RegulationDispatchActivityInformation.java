package module.regulation.dispatch.domain.activities;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;

import org.joda.time.LocalDate;

public class RegulationDispatchActivityInformation extends ActivityInformation<RegulationDispatchWorkflowMetaProcess> {

    private static final long serialVersionUID = 1L;

    private String reference;
    private LocalDate emissionDate;
    private String description;
    private Person emissor;
    private String regulationReference;
    private RegulationDispatchQueue queue;

    protected RegulationDispatchActivityInformation() {
	super(null, null);
    }

    public RegulationDispatchActivityInformation(RegulationDispatchWorkflowMetaProcess process, AbstractWorkflowActivity activity) {
	super(process, activity);

	setReference(process.getReference());
	setEmissionDate(process.getEmissionDate());
	setDescription(process.getDescription());
	setEmissor(process.getEmissor());
	setRegulationReference(process.getRegulationReference());
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

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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
