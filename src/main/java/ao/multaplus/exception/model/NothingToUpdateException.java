package ao.multaplus.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class NothingToUpdateException extends RuntimeException {
    public NothingToUpdateException(String message) {
        super(message);
    }
}
