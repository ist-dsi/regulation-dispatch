package module.regulation.dispatch.domain;

import org.joda.time.LocalDate;

public interface IRegulationDispatchEntry {
    public String getReference();

    public LocalDate getEmissionDate();

    public String getDescription();

    public String getEmissor();

    public String getRegulationReference();

}
