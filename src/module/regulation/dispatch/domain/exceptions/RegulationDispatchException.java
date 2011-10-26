package module.regulation.dispatch.domain.exceptions;

import java.util.ResourceBundle;

import myorg.domain.exceptions.DomainException;

public class RegulationDispatchException extends DomainException {

    private static final long serialVersionUID = 1L;

    public RegulationDispatchException() {
	super();
    }

    public RegulationDispatchException(String key, ResourceBundle bundle, String... args) {
	super(key, bundle, args);
    }

    public RegulationDispatchException(String key, String... args) {
	super(key, args);
    }

    public RegulationDispatchException(String key, Throwable cause, ResourceBundle bundle, String... args) {
	super(key, cause, bundle, args);
    }

    public RegulationDispatchException(String key, Throwable cause, String... args) {
	super(key, cause, args);
    }

}
