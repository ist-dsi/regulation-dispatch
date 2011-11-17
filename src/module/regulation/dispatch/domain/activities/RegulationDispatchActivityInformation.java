package module.regulation.dispatch.domain.activities;

import java.io.IOException;
import java.io.InputStream;

import module.organization.domain.Person;
import module.regulation.dispatch.domain.RegulationDispatchProcessFile;
import module.regulation.dispatch.domain.RegulationDispatchQueue;
import module.regulation.dispatch.domain.RegulationDispatchWorkflowMetaProcess;
import module.workflow.activities.ActivityInformation;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.Strings;

public class RegulationDispatchActivityInformation extends ActivityInformation<RegulationDispatchWorkflowMetaProcess> {

    private static final long serialVersionUID = 1L;

    private String reference;
    private LocalDate emissionDate;
    private String dispatchDescription;
    private Person emissor;
    private String regulationReference;
    private RegulationDispatchQueue queue;
    private Strings articles;

    private InputStream file;
    private String fileName;
    private Long fileSize;
    private String mimeType;

    private RegulationDispatchProcessFile regulationDispatchProcessFile;

    protected RegulationDispatchActivityInformation() {
	super(null, null);
    }

    public RegulationDispatchActivityInformation(final RegulationDispatchWorkflowMetaProcess process, final AbstractWorkflowActivity activity) {
	super(process, activity);

	setReference(process.getReference());
	setEmissionDate(process.getEmissionDate());
	setDispatchDescription(process.getDispatchDescription());
	setEmissor(process.getEmissor());
	setRegulationReference(process.getRegulationReference());
	setArticles(process.getArticles());
    }
    
    public RegulationDispatchActivityInformation(final RegulationDispatchWorkflowMetaProcess process,
	    final RegulationDispatchProcessFile file, final AbstractWorkflowActivity activity) {
	this(process, activity);
	setRegulationDispatchProcessFile(file);
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

    public InputStream getFile() {
	return file;
    }

    public void setFile(InputStream file) {
	this.file = file;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Long getFileSize() {
	return fileSize;
    }

    public void setFileSize(Long fileSize) {
	this.fileSize = fileSize;
    }

    public String getMimeType() {
	return mimeType;
    }

    public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
    }

    public RegulationDispatchProcessFile getRegulationDispatchProcessFile() {
	return regulationDispatchProcessFile;
    }

    public void setRegulationDispatchProcessFile(RegulationDispatchProcessFile regulationDispatchProcessFile) {
	this.regulationDispatchProcessFile = regulationDispatchProcessFile;
    }

    public Strings getArticles() {
	return articles;
    }

    public void setArticles(Strings articles) {
	this.articles = articles;
    }

    public byte[] getFileContent() throws IOException {
	byte[] contents = new byte[(int) this.getFileSize().longValue()];
	getFile().read(contents);

	return contents;
    }
}
