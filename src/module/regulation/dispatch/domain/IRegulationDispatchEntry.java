package module.regulation.dispatch.domain;

import module.organization.domain.Person;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.Strings;

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
