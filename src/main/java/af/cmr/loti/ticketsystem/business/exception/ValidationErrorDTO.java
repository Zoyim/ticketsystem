package af.cmr.loti.ticketsystem.business.exception;

import java.util.List;

public class ValidationErrorDTO {
	private List<String> errors;

    public ValidationErrorDTO(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
